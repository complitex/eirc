package ru.complitex.sync.handler;

import ru.complitex.address.entity.*;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.domain.service.DomainService;
import ru.complitex.eirc.adapter.SyncAdapter;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.entity.SyncStatus;
import ru.complitex.sync.exception.SyncException;
import ru.complitex.sync.mapper.SyncMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static ru.complitex.common.util.Strings.equalsIgnoreCase;

/**
 * @author Anatoly Ivanov
 * Date: 03.08.2014 6:47
 */
@RequestScoped
public class BuildingSyncHandler implements ISyncHandler<Building> {
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
        List<Sync> cityTypeSyncs = syncMapper.getSyncs(FilterWrapper.of(new Sync(CityType.ENTITY_ID, SyncStatus.SYNCHRONIZED,
                Long.valueOf(parentSync.getAdditionalParentId()))));

        if (cityTypeSyncs.isEmpty()){
            throw new RuntimeException("city type matching not found " + parentSync);
        }

        return syncAdapter.getBuildingSyncs(parentSync.getName(), cityTypeSyncs.get(0).getAdditionalName(), null,
                null, null, date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return syncMapper.getSyncs(FilterWrapper.of(new Sync(City.ENTITY_ID, SyncStatus.SYNCHRONIZED)));
    }

    private Long getParentId(Sync sync, Long companyId){
        List<Matching> matchingList = matchingMapper.getMatchingListByExternalId(Street.ENTITY_NAME,
                sync.getParentId(), companyId);

        if (matchingList.isEmpty()){
            throw new RuntimeException("street matching not found " + sync);
        }

        if (matchingList.size() > 1){
            throw new RuntimeException("street correction size > 1 " + sync);
        }

        return matchingList.get(0).getObjectId();
    }

    @Override
    public boolean isMatch(Building building, Sync sync, Long companyId) {
        return Objects.equals(building.getStreetId(), getParentId(sync, companyId)) &&
                equalsIgnoreCase(building.getName(), sync.getName()) &&
                equalsIgnoreCase(building.getAltName(), sync.getAltName()) &&
                equalsIgnoreCase(building.getCorps(), sync.getAdditionalName()) &&
                equalsIgnoreCase(building.getAltCorps(), sync.getAltAdditionalName());
    }

    @Override
    public boolean isMatch(Matching matching, Sync sync, Long companyId) {
        return Objects.equals(matching.getParentId(), getParentId(sync, companyId)) &&
                equalsIgnoreCase(matching.getName(), sync.getName()) &&
                equalsIgnoreCase(matching.getAdditionalName(), sync.getAdditionalName());
    }

    @Override
    public boolean isMatch(Matching matching1, Matching matching2) {
        return Objects.equals(matching1.getParentId(), matching2.getParentId()) &&
                equalsIgnoreCase(matching1.getName(), matching2.getName()) &&
                equalsIgnoreCase(matching1.getAdditionalName(), matching2.getAdditionalName());
    }

    @Override
    public List<Building> getDomains(Sync sync, Long companyId) {
        Building building = new Building();

        building.setStreetId(getParentId(sync, companyId));
        building.setName(sync.getName());
        building.setAltName(sync.getAltName());
        building.setCorps(sync.getAdditionalName());
        building.setAltCorps(sync.getAltAdditionalName());

        return domainService.getDomains(Building.class, FilterWrapper.of(building).setFilter(FilterWrapper.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(Building building, Sync sync, Long companyId) {
        return matchingMapper.insert(new Matching(Building.ENTITY_NAME, building.getObjectId(), building.getStreetId(),
                sync.getExternalId(), sync.getName(), sync.getAdditionalName(), companyId));
    }

    @Override
    public void updateMatching(Matching matching, Sync sync, Long companyId) {
        matching.setParentId(getParentId(sync, companyId));
        matching.setName(sync.getName());
        matching.setAdditionalName(sync.getAdditionalName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(Building building, Sync sync, Long companyId) {
        List<Matching> matchingList = matchingMapper.getMatchingListByExternalId(District.ENTITY_NAME,
                Long.valueOf(sync.getAdditionalParentId()), companyId);

        if (matchingList.isEmpty()){
            throw new RuntimeException("district matching not found " + sync);
        }

        building.setStreetId(getParentId(sync, companyId));
        building.setDistrictId(matchingList.get(0).getObjectId());
        building.setName(sync.getName());
        building.setAltName(sync.getAltName());
        building.setCorps(sync.getAdditionalName());
        building.setAltCorps(sync.getAltAdditionalName());
    }
}
