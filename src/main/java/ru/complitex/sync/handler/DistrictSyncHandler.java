package ru.complitex.sync.handler;

import ru.complitex.address.entity.City;
import ru.complitex.address.entity.CityType;
import ru.complitex.address.entity.District;
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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static ru.complitex.common.util.Strings.equalsIgnoreCase;

/**
 * @author Anatoly Ivanov
 * Date: 17.07.2014 23:34
 */
@RequestScoped
public class DistrictSyncHandler implements ISyncHandler<District> {
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
        List<Sync> cityTypeSyncs = syncMapper.getSyncs(Filter.of(new Sync(CityType.ID, SyncStatus.SYNCHRONIZED,
                Long.valueOf(parentSync.getAdditionalParentId()))));

        if (cityTypeSyncs.isEmpty()){
            throw new RuntimeException("city type matching not found " + cityTypeSyncs);
        }

        return syncAdapter.getDistrictSyncs(cityTypeSyncs.get(0).getAdditionalName(), parentSync.getName(), date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return syncMapper.getSyncs(Filter.of(new Sync(City.ID, SyncStatus.SYNCHRONIZED)));
    }

    private Long getParentId(Sync domainSync, Long organizationId){
        List<Matching> matchingList = matchingMapper.getMatchingListCode(City.ENTITY,
                domainSync.getParentId(), organizationId);

        if (matchingList.isEmpty()){
            throw new RuntimeException("city mathing not found " + matchingList);
        }

        return matchingList.get(0).getObjectId();
    }

    @Override
    public boolean isMatch(District district, Sync sync, Long companyId) {
        return Objects.equals(district.getCityId(), getParentId(sync, companyId)) &&
                equalsIgnoreCase(district.getName(), sync.getName()) &&
                equalsIgnoreCase(district.getAltName(), sync.getAltName());
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
    public List<District> getDomains(Sync sync, Long companyId) {
        District district = new District();

        district.setCityId(getParentId(sync, companyId));
        district.setName(sync.getName());
        district.setAltName(sync.getAltName());

        return domainService.getDomains(District.class, Filter.of(district).setFilter(Filter.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(District district, Sync sync, Long companyId) {
        return matchingMapper.insert(new Matching(District.ENTITY, district.getObjectId(), district.getCityId(),
                sync.getExternalId(), sync.getName(), companyId));
    }

    @Override
    public void updateMatching(Matching matching, Sync sync, Long companyId) {
        matching.setParentId(getParentId(sync, companyId));
        matching.setName(sync.getName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(District district, Sync sync, Long companyId) {
        district.setCityId(getParentId(sync, companyId));
        district.setName(sync.getName());
        district.setAltName(sync.getAltName());
        district.setCode(sync.getAdditionalExternalId());
    }
}
