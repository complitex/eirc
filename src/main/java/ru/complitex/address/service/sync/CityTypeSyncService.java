package ru.complitex.address.service.sync;

import ru.complitex.address.entity.CityType;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.service.DomainService;
import ru.complitex.sync.adapter.SyncAdapter;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.exception.SyncException;
import ru.complitex.sync.mapper.SyncMapper;
import ru.complitex.sync.service.ISyncHandler;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static ru.complitex.common.util.Strings.equalsIgnoreCase;

/**
 * @author Anatoly A. Ivanov
 * 19.01.2018 17:18
 */
@RequestScoped
public class CityTypeSyncService implements ISyncHandler<CityType> {
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
        return syncAdapter.getCityTypeSyncs(date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return null;
    }

    @Override
    public boolean isMatch(CityType cityType, Sync sync, Long companyId) {
        return equalsIgnoreCase(cityType.getName(), sync.getName()) && //todo short name
                equalsIgnoreCase(cityType.getAltName(), sync.getAltName());
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
    public List<CityType> getDomains(Sync sync, Long companyId) {
        CityType cityType = new CityType();

        cityType.setName(sync.getName());
        cityType.setAltName(sync.getAltName());

        return domainService.getDomains(CityType.class, Filter.of(cityType).setFilter(Filter.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(CityType cityType, Sync sync, Long companyId) {
        return matchingMapper.insert(new Matching(CityType.ENTITY, cityType.getObjectId(), sync.getExternalId(),
                sync.getName(), sync.getAdditionalName(), companyId));
    }

    @Override
    public void updateMatching(Matching matching, Sync sync, Long companyId) {
        matching.setName(sync.getName());
        matching.setAdditionalName(sync.getAdditionalName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(CityType cityType, Sync sync, Long companyId) {
        cityType.setName(sync.getName());
        cityType.setAltName(sync.getAltName());
    }
}
