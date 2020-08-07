package ru.complitex.address.service.sync;

import ru.complitex.address.entity.City;
import ru.complitex.address.entity.CityType;
import ru.complitex.address.entity.Region;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.service.DomainService;
import ru.complitex.sync.adapter.SyncAdapter;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.entity.SyncStatus;
import ru.complitex.sync.exception.SyncException;
import ru.complitex.sync.mapper.SyncMapper;
import ru.complitex.sync.service.ISyncHandler;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static ru.complitex.common.util.Strings.equalsIgnoreCase;

/**
 * @author Anatoly A. Ivanov
 * 19.01.2018 17:17
 */
@RequestScoped
public class CitySyncService implements ISyncHandler<City> {
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
        return syncAdapter.getCitySyncs(parentSync.getName(), date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return syncMapper.getSyncs(Filter.of(new Sync(Region.ID, SyncStatus.SYNCHRONIZED)));
    }

    private Long getParentId(Sync sync, Long organizationId){
        List<Matching> matchingList = matchingMapper.getMatchingListCode(Region.ENTITY,
                sync.getParentId(), organizationId);

        if (matchingList.isEmpty()){
            throw new RuntimeException("region correction not found " + sync);
        }

        return matchingList.get(0).getObjectId();
    }

    private Long getAdditionalParentId(Sync sync, Long organizationId){
        List<Matching> matchingList = matchingMapper.getMatchingListCode(CityType.ENTITY,
                Long.valueOf(sync.getAdditionalParentId()), organizationId);

        if (matchingList.isEmpty()){
            throw new RuntimeException("city type matching not found " + sync);
        }

        return matchingList.get(0).getObjectId();
    }

     @Override
    public boolean isMatch(City city, Sync sync, Long companyId) {
        return Objects.equals(city.getRegionId(), getParentId(sync, companyId)) &&
                Objects.equals(city.getCityTypeId(), getAdditionalParentId(sync, companyId)) &&
                equalsIgnoreCase(city.getName(), sync.getName()) &&
                equalsIgnoreCase(city.getAltName(), sync.getAltName());
    }

    @Override
    public boolean isMatch(Matching matching, Sync sync, Long companyId) {
        return Objects.equals(matching.getParentId(), getParentId(sync, companyId)) &&
                equalsIgnoreCase(matching.getName(), sync.getName());
    }

    @Override
    public boolean isMatch(Matching matching1, Matching matching2) {
        return Objects.equals(matching1.getParentId(), matching2.getParentId()) &&
                equalsIgnoreCase(matching1.getName(), matching2.getName());
    }

    @Override
    public List<City> getDomains(Sync sync, Long companyId) {
        City city = new City();

        city.setRegionId(getParentId(sync, companyId));
        city.setCityTypeId(getAdditionalParentId(sync, companyId));
        city.setName(sync.getName());
        city.setAltName(sync.getAltName());

        return domainService.getDomains(City.class, Filter.of(city).setFilter(Filter.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(City city, Sync sync, Long companyId) {
        return matchingMapper.insert(new Matching(City.ENTITY, city.getObjectId(), city.getRegionId(),
                sync.getExternalId(), sync.getName(), companyId));
    }

    @Override
    public void updateMatching(Matching matching, Sync sync, Long companyId) {
        matching.setParentId(getParentId(sync, companyId));
        matching.setName(sync.getName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(City city, Sync sync, Long companyId) {
        city.setRegionId(getParentId(sync, companyId));
        city.setCityTypeId(getAdditionalParentId(sync, companyId));
        city.setName(sync.getName());
        city.setAltName(sync.getAltName());
    }
}
