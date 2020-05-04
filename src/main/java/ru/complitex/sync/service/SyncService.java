package ru.complitex.sync.service;

import org.apache.ibatis.reflection.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.complitex.common.service.BroadcastService;
import ru.complitex.domain.entity.Status;
import ru.complitex.eirc.adapter.SyncAdapter;
import ru.complitex.matching.entity.Matching;
import ru.complitex.sync.handler.*;
import ru.complitex.sync.mapper.SyncMapper;

import javax.ejb.Asynchronous;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author Anatoly Ivanov
 * 21.04.2020 23:38
 */
public class SyncService {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private SyncMapper syncMapper;

    @Inject
    private BroadcastService broadcastService;

    @Inject
    private SyncAdapter syncAdapter;

    @Inject
    private Matching correctionBean;

    private AtomicBoolean processing = new AtomicBoolean(false);

    private AtomicBoolean cancelSync = new AtomicBoolean(false);

    private ISyncHandler getHandler(SyncEntity syncEntity){
        switch (syncEntity){
            case COUNTRY:
                return EjbBeanLocator.getBean(CountrySyncHandler.class);
            case REGION:
                return EjbBeanLocator.getBean(RegionSyncHandler.class);
            case CITY_TYPE:
                return EjbBeanLocator.getBean(CityTypeSyncHandler.class);
            case CITY:
                return EjbBeanLocator.getBean(CitySyncHandler.class);
            case DISTRICT:
                return EjbBeanLocator.getBean(DistrictSyncHandler.class);
            case STREET_TYPE:
                return EjbBeanLocator.getBean(StreetTypeSyncHandler.class);
            case STREET:
                return EjbBeanLocator.getBean(StreetSyncHandler.class);
            case BUILDING:
                return EjbBeanLocator.getBean(BuildingSyncHandler.class);
            case ORGANIZATION:
                return EjbBeanLocator.getBean(CompanySyncHandler.class);

            default:
                throw new IllegalArgumentException();
        }
    }


    @Asynchronous
    public void load(SyncEntity syncEntity){
        if (processing.get()){
            return;
        }

        try {
            //lock sync
            processing.set(true);
            cancelSync.set(false);

            //clear
            domainSyncBean.delete(syncEntity);

            Date date = DateUtil.getCurrentDate();

            List<DomainSync> parentDomainSyncs = getHandler(syncEntity).getParentDomainSyncs();

            if (parentDomainSyncs != null){
                for (DomainSync ds : parentDomainSyncs) {
                    load(syncEntity, ds, date);
                }
            }else{
                load(syncEntity, null, date);
            }
        } catch (Exception e) {
            log.error("Ошибка синхронизации", e);

            String message = ExceptionUtil.getCauseMessage(e, true);

            broadcastService.broadcast(getClass(), "error", message != null ? message : e.getMessage());
        } finally {
            //unlock sync
            processing.set(false);

            broadcastService.broadcast(getClass(), "done", syncEntity.name());
        }
    }

    private void load(SyncEntity syncEntity, DomainSync parentDomainSync, Date date)
            throws RemoteCallException{
        if (cancelSync.get()){
            return;
        }

        Cursor<DomainSync> cursor = getHandler(syncEntity).getCursorDomainSyncs(parentDomainSync, date);

        SyncBeginMessage message = new SyncBeginMessage();
        message.setSyncEntity(syncEntity);
        message.setCount(cursor.getData() != null ? cursor.getData().size() : 0L);
        if (parentDomainSync != null){
            message.setParentName(parentDomainSync.getName());
        }

        broadcastService.broadcast(getClass(), "begin", message);

        if (cursor.getData() != null) {
            cursor.getData().forEach(s -> {
                s.setStatus(LOADED);
                s.setType(syncEntity);
                s.setDate(date);

                domainSyncBean.insert(s);

                broadcastService.broadcast(getClass(), "processed", s);
            });
        }
    }

    private List<DomainSync> getDomainSyncs(SyncEntity syncEntity, DomainSyncStatus syncStatus, Long externalId) {
        List<DomainSync> domainSyncs = domainSyncBean.getList(of(new DomainSync(syncEntity, syncStatus, externalId)));

        if (syncEntity.equals(SyncEntity.ORGANIZATION)) {
            Map<Long, DomainSync> map = domainSyncs.stream().collect(Collectors.toMap(DomainSync::getExternalId, ds -> ds));

            Map<Long, Integer> levelMap = new HashMap<>();

            domainSyncs.forEach(ds -> {
                int level = 0;

                DomainSync p = ds;

                while (p != null && p.getParentId() != null && level < 100){
                    p = map.get(p.getParentId());

                    level++;
                }

                levelMap.put(ds.getExternalId(), level);
            });

            domainSyncs.sort(Comparator.comparingInt(ds -> levelMap.get(ds.getExternalId())));
        }

        return domainSyncs;
    }

    @Asynchronous
    public void sync(SyncEntity syncEntity){
        processing.set(true);
        cancelSync.set(false);

        try {
            broadcastService.broadcast(getClass(), "info", "Начата синхронизация");
            log.info("sync: begin");

            Long organizationId = syncAdapter.getOrganization().getObjectId();

            IDomainSyncHandler handler = getHandler(syncEntity);

            //sync
            getDomainSyncs(syncEntity, null, null).forEach(ds -> {
                try {
                    if (cancelSync.get()){
                        return;
                    }

                    List<? extends Matching> corrections = correctionBean.getCorrectionsByExternalId(syncEntity,
                            ds.getExternalId(), organizationId, null);

                    if (!corrections.isEmpty()){
                        if (corrections.size() > 1){
                            log.warn("sync: corrections > 1 {}", corrections); //todo
                        }

                        Matching correction = corrections.get(0);

                        if (!handler.isCorresponds(correction, ds, organizationId)){
                            handler.updateCorrection(correction, ds, organizationId);

                            log.info("sync: update correction name {}", correction);
                        }

                        DomainObject domainObject = handler.getStrategy().getDomainObject(correction.getObjectId());

                        if (handler.isCorresponds(domainObject, ds, organizationId)){
                            if (!domainObject.getStatus().equals(Status.ACTIVE)){
                                handler.getStrategy().enable(domainObject);

                                log.info("sync: enable domain object {}", correction);
                            }

                            ds.setStatus(SYNCHRONIZED);
                            domainSyncBean.updateStatus(ds);
                        }else{
                            List<? extends Matching> objectCorrections = correctionBean.getCorrectionsByObjectId(syncEntity,
                                    domainObject.getObjectId(),  organizationId, null);

                            if (objectCorrections.size() == 1 && objectCorrections.get(0).getId().equals(correction.getId())){
                                handler.updateValues(domainObject, ds, organizationId);
                                handler.getStrategy().update(domainObject);

                                log.info("sync: update domain object {}", domainObject);

                                ds.setStatus(SYNCHRONIZED);
                                domainSyncBean.updateStatus(ds);
                            }else {
                                ds.setStatus(DEFERRED);
                                domainSyncBean.updateStatus(ds);
                            }
                        }
                    }else {
                        List<? extends DomainObject> domainObjects = handler.getDomainObjects(ds, organizationId);

                        DomainObject domainObject;

                        if (!domainObjects.isEmpty()){
                            domainObject = domainObjects.get(0);

                            for (int i = 1; i < domainObjects.size(); ++i){
                                handler.getStrategy().disable(domainObjects.get(i));

                                log.info("sync: disable domain object {}", domainObjects.get(i));
                            }
                        }else{
                            domainObject = handler.getStrategy().newInstance();
                            handler.updateValues(domainObject, ds, organizationId);
                            handler.getStrategy().insert(domainObject, ds.getDate());

                            log.info("sync: add domain object {}", domainObject);
                        }

                        Matching correction = handler.insertCorrection(domainObject, ds, organizationId);

                        log.info("sync: add correction {}", correction);

                        ds.setStatus(SYNCHRONIZED);
                        domainSyncBean.updateStatus(ds);
                    }
                } catch (CorrectionNotFoundException e) {
                    ds.setStatus(ERROR);
                    domainSyncBean.updateStatus(ds);

                    log.warn("sync: warn sync {}", e.getMessage());
                } catch (Exception e){
                    log.error("sync: error sync {}", ds);

                    throw e;
                }

                broadcastService.broadcast(getClass(), "processed", ds);
            });

            //clear
            correctionBean.getCorrections(syncEntity, null, organizationId, null).forEach(c -> {
                if (getDomainSyncs(syncEntity, null, c.getExternalId()).isEmpty()){
                    correctionBean.delete(c);

                    log.info("sync: delete correction {}", c);
                }
            });

            //deferred
            getDomainSyncs(syncEntity, DEFERRED, null).forEach(ds -> {
                if (domainSyncBean.getObject(ds.getId()).getStatus().equals(DEFERRED)) {
                    List<? extends Matching> corrections = correctionBean.getCorrectionsByExternalId(syncEntity,
                            ds.getExternalId(), organizationId, null);

                    if (corrections.isEmpty()){
                        throw new RuntimeException("sync: deferred correction nod found " + ds);
                    }

                    Matching correction = corrections.get(0);

                    List<? extends Matching> objectCorrections = correctionBean.getCorrectionsByObjectId(syncEntity,
                            correction.getObjectId(), organizationId, null);

                    boolean corresponds = objectCorrections.stream()
                            .allMatch(c -> c.getId().equals(correction.getId()) || handler.isCorresponds(c, correction));

                    if (corresponds){
                        DomainObject domainObject = handler.getStrategy().getDomainObject(correction.getObjectId());

                        handler.updateValues(domainObject, ds, organizationId);
                        handler.getStrategy().update(domainObject);

                        log.info("sync: update deferred domain object {}", domainObject);

                        objectCorrections.forEach(c -> {
                            if (!c.getId().equals(correction.getId())){
                                DomainSync domainSync = getDomainSyncs(syncEntity, null, c.getExternalId()).get(0);

                                domainSync.setStatus(SYNCHRONIZED);
                                domainSyncBean.updateStatus(domainSync);
                            }
                        });
                    }else{
                        List<? extends DomainObject> domainObjects = handler.getDomainObjects(ds, organizationId);

                        if (!domainObjects.isEmpty()){
                            correction.setObjectId(domainObjects.get(0).getObjectId());

                            correctionBean.save(correction);

                            log.info("sync: update deferred correction {}", correction);
                        }else{
                            DomainObject domainObject = handler.getStrategy().newInstance();
                            handler.updateValues(domainObject, ds, organizationId);
                            handler.getStrategy().insert(domainObject, ds.getDate());

                            log.info("sync: add deferred domain object {}", domainObject);
                        }
                    }

                    ds.setStatus(SYNCHRONIZED);
                    domainSyncBean.updateStatus(ds);
                }
            });

            broadcastService.broadcast(getClass(), "info", "Синхронизация завершена успешно");
            log.info("sync: completed");
        } catch (Exception e) {
            log.error("sync: error", e);

            broadcastService.broadcast(getClass(), "error", ExceptionUtil.getCauseMessage(e));
        } finally {
            processing.set(false);
        }

    }


    public void cancelSync(){
        cancelSync.set(true);
    }

    public boolean getProcessing(){
        return processing.get();
    }

}
