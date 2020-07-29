package ru.complitex.sync.handler;

import ru.complitex.address.entity.StreetType;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.service.DomainService;
import ru.complitex.eirc.adapter.SyncAdapter;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.exception.SyncException;
import ru.complitex.sync.mapper.SyncMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static ru.complitex.common.util.Strings.equalsIgnoreCase;

/**
 * @author Anatoly Ivanov
 * Date: 23.07.2014 22:57
 */
@RequestScoped
public class StreetTypeSyncHandler implements ISyncHandler<StreetType> {
    @Inject
    private DomainService domainService;

    @Inject
    private SyncMapper syncMapper;

    @Inject
    private MatchingMapper matchingMapper;

    @Inject
    private SyncAdapter syncAdapter;

    @Override
    public Cursor<Sync> getCursorSyncs(Sync parentSync, Date date) throws SyncException {
        return syncAdapter.getStreetTypeSyncs(date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return null;
    }

    @Override
    public boolean isMatch(StreetType streetType, Sync sync, Long companyId) {
        return equalsIgnoreCase(streetType.getName(), sync.getName()) &&
                equalsIgnoreCase(streetType.getAltName(), sync.getAltName()) &&
                equalsIgnoreCase(streetType.getShortName(), sync.getAdditionalName()) &&
                equalsIgnoreCase(streetType.getAltShortName(), sync.getAltAdditionalName());
    }

    @Override
    public boolean isMatch(Matching matching, Sync sync, Long companyId) {
        return equalsIgnoreCase(matching.getName(), sync.getName());
    }

    @Override
    public boolean isMatch(Matching matching1, Matching matching2) {
        return equalsIgnoreCase(matching1.getName(), matching2.getName());
    }

    @Override
    public List<StreetType> getDomains(Sync sync, Long companyId) {
        StreetType streetType = new StreetType();

        streetType.setName(sync.getName());
        streetType.setAltName(sync.getAltName());
        streetType.setShortName(sync.getAdditionalName());
        streetType.setAltShortName(sync.getAltAdditionalName());

        return domainService.getDomains(StreetType.class, Filter.of(streetType).setFilter(Filter.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(StreetType streetType, Sync sync, Long companyId) {
        return matchingMapper.insert(new Matching(StreetType.ENTITY, streetType.getObjectId(),
                sync.getExternalId(), sync.getName(), sync.getAdditionalName(), companyId));
    }

    @Override
    public void updateMatching(Matching matching, Sync sync, Long companyId) {
        matching.setName(sync.getName());
        matching.setAdditionalName(sync.getAdditionalName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(StreetType streetType, Sync sync, Long companyId) {
        streetType.setName(sync.getName());
        streetType.setAltName(sync.getAltName());
        streetType.setShortName(sync.getAdditionalName());
        streetType.setAltShortName(sync.getAltAdditionalName());
    }
}
