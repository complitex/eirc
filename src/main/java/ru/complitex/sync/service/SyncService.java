package ru.complitex.sync.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.Filter;
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
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.entity.SyncMessage;
import ru.complitex.sync.entity.SyncStatus;
import ru.complitex.sync.exception.SyncException;
import ru.complitex.sync.mapper.SyncMapper;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author Anatoly Ivanov
 * 21.04.2020 23:38
 */
public abstract class SyncService {
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
    private MatchingMapper matchingMapper;

    private final AtomicBoolean processing = new AtomicBoolean(false);

    private final AtomicBoolean cancelSync = new AtomicBoolean(false);

    protected abstract ISyncHandler<?> getHandler(int entityId);

    public <T extends Domain<T>> void load(Class<T> domainClass){
        if (processing.get()){
            return;
        }

        T domain = Domains.newObject(domainClass);

        int entityId = domain.getEntityId();

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
        List<Sync> syncs = syncMapper.getSyncs(Filter.of(new Sync(entityId, syncStatusId, externalId)));

        if (entityId == Company.ID) {
            Map<Long, Sync> map = syncs.stream().collect(Collectors.toMap(Sync::getExternalId, s -> s));

            Map<Long, Integer> levelMap = new HashMap<>();

            syncs.forEach(s -> {
                int level = 0;

                Sync p = s;

                while (p != null && p.getParentId() != null && level < 100){
                    p = map.get(p.getParentId());

                    level++;
                }

                if (s != null) {
                    levelMap.put(s.getExternalId(), level);
                }
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

            Long companyId = null; //todo

            @SuppressWarnings("unchecked") ISyncHandler<T> handler = (ISyncHandler<T>) getHandler(entity.getId());


            getSyncs(entity.getId(), SyncStatus.LOADED, null).forEach(s -> {
                try {
                    if (cancelSync.get()){
                        return;
                    }

                    List<Matching> matchingList = matchingMapper.getMatchingListByNumber(entity.getName(),
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
                        }

                        syncMapper.updateStatus(s);
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

                    log.error("sync: error ", e);
                }

                broadcastService.broadcast(getClass(), "processed", s);
            });


            matchingMapper.getMatchingList(entity.getName(), companyId).forEach(m -> {
                if (getSyncs(entity.getId(), 0, m.getNumber()).isEmpty()){
                    matchingMapper.delete(m.getId());

                    log.info("sync: delete matching {}", m);
                }
            });


            getSyncs(entity.getId(), SyncStatus.DELAYED, null).forEach(s -> {
                if (syncMapper.getSync(s.getId()).getStatus() == SyncStatus.DELAYED) {
                    List<Matching> matchingList = matchingMapper.getMatchingListByNumber(entity.getName(),
                            s.getExternalId(), companyId);

                    if (matchingList.isEmpty()){
                        throw new RuntimeException("sync: delayed matching not found " + s);
                    }

                    Matching matching = matchingList.get(0);

                    List<Matching> objectMatchingList = matchingMapper.getMatchingListByObjectId(entity.getName(),
                            matching.getObjectId(), companyId);

                    boolean corresponds = objectMatchingList.stream()
                            .allMatch(m -> m.getId().equals(matching.getId()) || handler.isMatch(m, matching));

                    if (corresponds){
                        T domain = domainService.getDomain(domainClass, matching.getObjectId());

                        handler.updateNames(domain, s, companyId);

                        domainService.update(domain);


                        log.info("sync: update deferred domain object {}", domain);

                        objectMatchingList.forEach(m -> {
                            if (!m.getId().equals(matching.getId())){
                                Sync sync = getSyncs(entity.getId(), 0, m.getNumber()).get(0);

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
