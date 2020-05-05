package ru.complitex.sync.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.complitex.address.entity.*;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.service.BroadcastService;
import ru.complitex.common.util.Dates;
import ru.complitex.common.util.Exceptions;
import ru.complitex.company.entity.Company;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.Entity;
import ru.complitex.domain.entity.Status;
import ru.complitex.domain.service.DomainService;
import ru.complitex.domain.service.EntityService;
import ru.complitex.domain.util.Domains;
import ru.complitex.eirc.adapter.SyncAdapter;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.entity.SyncMessage;
import ru.complitex.sync.entity.SyncStatus;
import ru.complitex.sync.exception.SyncException;
import ru.complitex.sync.handler.*;
import ru.complitex.sync.mapper.SyncMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author Anatoly Ivanov
 * 21.04.2020 23:38
 */
@ApplicationScoped
public class SyncService {
    private Logger log = LoggerFactory.getLogger(SyncService.class);

    @Inject
    private EntityService entityService;

    @Inject
    private DomainService domainService;

    @Inject
    private SyncMapper syncMapper;

    @Inject
    private BroadcastService broadcastService;

    @Inject
    private SyncAdapter syncAdapter;

    @Inject
    private MatchingMapper matchingMapper;

    @Inject
    private CountrySyncHandler countrySyncHandler;

    @Inject
    private RegionSyncHandler regionSyncHandler;

    @Inject
    private CityTypeSyncHandler cityTypeSyncHandler;

    @Inject
    private CitySyncHandler citySyncHandler;

    @Inject
    private DistrictSyncHandler districtSyncHandler;

    @Inject
    private StreetTypeSyncHandler streetTypeSyncHandler;

    @Inject
    private StreetSyncHandler streetSyncHandler;

    @Inject
    private BuildingSyncHandler buildingSyncHandler;

    @Inject
    private CompanySyncHandler companySyncHandler;

    private AtomicBoolean processing = new AtomicBoolean(false);

    private AtomicBoolean cancelSync = new AtomicBoolean(false);

    private ISyncHandler getHandler(int entityId){
        switch (entityId){
            case Country.ENTITY_ID:
                return countrySyncHandler;
            case Region.ENTITY_ID:
                return regionSyncHandler;
            case CityType.ENTITY_ID:
                return cityTypeSyncHandler;
            case City.ENTITY_ID:
                return citySyncHandler;
            case District.ENTITY_ID:
                return districtSyncHandler;
            case StreetType.ENTITY_ID:
                return streetTypeSyncHandler;
            case Street.ENTITY_ID:
                return streetSyncHandler;
            case Building.ENTITY_ID:
                return buildingSyncHandler;
            case Company.ENTITY_ID:
                return companySyncHandler;

            default:
                throw new IllegalArgumentException();
        }
    }

    public void load(int entityId){
        if (processing.get()){
            return;
        }

        try {
            //lock sync
            processing.set(true);
            cancelSync.set(false);

            //clear
            syncMapper.deleteAll(entityId);

            Date date = Dates.getCurrentDate();

            List<Sync> parentSyncs = getHandler(entityId).getParentSyncs();

            if (parentSyncs != null){
                for (Sync s : parentSyncs) {
                    load(entityId, s, date);
                }
            }else{
                load(entityId, null, date);
            }
        } catch (Exception e) {
            log.error("Ошибка синхронизации", e);

            String message = Exceptions.getCauseMessage(e, true);

            broadcastService.broadcast(getClass(), "error", message != null ? message : e.getMessage());
        } finally {
            //unlock sync
            processing.set(false);

            broadcastService.broadcast(getClass(), "done", entityId);
        }
    }

    private void load(int entityId, Sync parentSync, Date date) throws SyncException {
        if (cancelSync.get()){
            return;
        }

        Cursor<Sync> cursor = getHandler(entityId).getCursorSyncs(parentSync, date);

        SyncMessage message = new SyncMessage();
        message.setEntityId(entityId);
        message.setCount(cursor.getData() != null ? cursor.getData().size() : 0L);
        if (parentSync != null){
            message.setParentName(parentSync.getName());
        }

        broadcastService.broadcast(getClass(), "begin", message);

        if (cursor.getData() != null) {
            cursor.getData().forEach(s -> {
                s.setStatus(SyncStatus.LOADED);
                s.setEntityId(entityId);
                s.setDate(date);

                syncMapper.insert(s);

                broadcastService.broadcast(getClass(), "processed", s);
            });
        }
    }

    private List<Sync> getSyncs(int entityId, int syncStatusId, Long externalId) {
        List<Sync> syncs = syncMapper.getSyncs(FilterWrapper.of(new Sync(entityId, syncStatusId, externalId)));

        if (entityId == Company.ENTITY_ID) {
            Map<Long, Sync> map = syncs.stream().collect(Collectors.toMap(Sync::getExternalId, s -> s));

            Map<Long, Integer> levelMap = new HashMap<>();

            syncs.forEach(s -> {
                int level = 0;

                Sync p = s;

                while (p != null && p.getParentId() != null && level < 100){
                    p = map.get(p.getParentId());

                    level++;
                }

                levelMap.put(s.getExternalId(), level);
            });

            syncs.sort(Comparator.comparingInt(ds -> levelMap.get(ds.getExternalId())));
        }

        return syncs;
    }

    public <T extends Domain<T>> void sync(Class<T> domainClass){
        processing.set(true);
        cancelSync.set(false);

        try {
            broadcastService.broadcast(getClass(), "info", "Начата синхронизация");
            log.info("sync: begin");

            Entity entity = entityService.getEntity(domainClass);

            Long companyId = syncAdapter.getCompany().getObjectId();

            ISyncHandler<T> handler = getHandler(entity.getId());

            //sync
            getSyncs(entity.getId(), 0, null).forEach(s -> { //todo sync status id
                try {
                    if (cancelSync.get()){
                        return;
                    }

                    List<Matching> matchingList = matchingMapper.getMatchingListByExternalId(entity.getName(),
                            s.getExternalId(), companyId);

                    if (!matchingList.isEmpty()){
                        if (matchingList.size() > 1){
                            log.warn("sync: matchingList > 1 {}", matchingList); //todo
                        }

                        Matching matching = matchingList.get(0);

                        if (!handler.isMatch(matching, s, companyId)){
                            handler.updateMatching(matching, s, companyId);

                            log.info("sync: update matching name {}", matching);
                        }

                        T domain = domainService.getDomain(domainClass, matching.getObjectId());

                        if (handler.isMatch(domain, s, companyId)){
                            if (domain.getStatus() != Status.ACTIVE){
                                domainService.enable(domain);

                                log.info("sync: enable domain object {}", matching);
                            }

                            s.setStatus(SyncStatus.SYNCHRONIZED);
                            syncMapper.updateStatus(s);
                        }else{
                            List<Matching> objectMatchingList = matchingMapper.getMatchingListByObjectId(entity.getName(),
                                    domain.getObjectId(),  companyId);

                            if (objectMatchingList.size() == 1 && objectMatchingList.get(0).getId().equals(matching.getId())){
                                handler.updateNames(domain, s, companyId);
                                domainService.update(domain);

                                log.info("sync: update domain object {}", domain);

                                s.setStatus(SyncStatus.SYNCHRONIZED);
                            }else {
                                s.setStatus(SyncStatus.DELAYED);

                                log.info("sync: delay domain object {}", domain);
                            }

                            syncMapper.updateStatus(s);
                        }
                    }else {
                        T domain;

                        List<T> domains = handler.getDomains(s, companyId);

                        if (!domains.isEmpty()){
                            domain = domains.get(0);

                            domains.forEach(d -> {
                                domainService.disable(d);

                                log.info("sync: disable domain object {}", d);
                            });
                        }else{
                            domain = Domains.newObject(domainClass);

                            handler.updateNames(domain, s, companyId);

                            domainService.insert(domain, s.getDate());

                            log.info("sync: add domain object {}", domain);
                        }

                        Matching matching = handler.insertMatching(domain, s, companyId);

                        log.info("sync: add matching {}", matching);

                        s.setStatus(SyncStatus.SYNCHRONIZED);
                        syncMapper.updateStatus(s);
                    }
                } catch (Exception e) {
                    s.setStatus(SyncStatus.ERROR);
                    syncMapper.updateStatus(s);

                    log.warn("sync: warn sync {}", e.getMessage());
                }

                broadcastService.broadcast(getClass(), "processed", s);
            });

            //clear
            matchingMapper.getMatchingList(entity.getName(), companyId).forEach(c -> {
                if (getSyncs(entity.getId(), 0, c.getExternalId()).isEmpty()){
                    matchingMapper.delete(c);

                    log.info("sync: delete matching {}", c);
                }
            });

            //delayed
            getSyncs(entity.getId(), SyncStatus.DELAYED, null).forEach(s -> {
                if (syncMapper.getSync(s.getId()).getStatus() == SyncStatus.DELAYED) {
                    List<Matching> matchingList = matchingMapper.getMatchingListByExternalId(entity.getName(),
                            s.getExternalId(), companyId);

                    if (matchingList.isEmpty()){
                        throw new RuntimeException("sync: delayed matching not found " + s);
                    }

                    Matching matching = matchingList.get(0);

                    List<Matching> objectMatchingList = matchingMapper.getMatchingListByObjectId(entity.getName(),
                            matching.getObjectId(), companyId);

                    boolean corresponds = objectMatchingList.stream()
                            .allMatch(c -> c.getId().equals(matching.getId()) || handler.isMatch(c, matching));

                    if (corresponds){
                        T domain = domainService.getDomain(domainClass, matching.getObjectId());

                        handler.updateNames(domain, s, companyId);

                        domainService.update(domain);


                        log.info("sync: update deferred domain object {}", domain);

                        objectMatchingList.forEach(c -> {
                            if (!c.getId().equals(matching.getId())){
                                Sync sync = getSyncs(entity.getId(), 0, c.getExternalId()).get(0);

                                sync.setStatus(SyncStatus.SYNCHRONIZED);
                                syncMapper.updateStatus(sync);
                            }
                        });
                    }else{
                        List<T> domains = handler.getDomains(s, companyId);

                        if (!domains.isEmpty()){
                            matching.setObjectId(domains.get(0).getObjectId());

                            matchingMapper.update(matching);

                            log.info("sync: update deferred matching {}", matching);
                        }else{
                            T domain = Domains.newObject(domainClass);

                            handler.updateNames(domain, s, companyId);

                            domainService.insert(domain, s.getDate());

                            log.info("sync: add deferred domain object {}", domain);
                        }
                    }

                    s.setStatus(SyncStatus.SYNCHRONIZED);
                    syncMapper.updateStatus(s);
                }
            });

            broadcastService.broadcast(getClass(), "info", "Синхронизация завершена успешно");
            log.info("sync: completed");
        } catch (Exception e) {
            log.error("sync: error", e);

            broadcastService.broadcast(getClass(), "error", Exceptions.getCauseMessage(e));
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
