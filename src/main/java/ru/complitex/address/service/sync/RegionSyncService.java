package ru.complitex.address.service.sync;

import ru.complitex.address.entity.Country;
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
 * 19.01.2018 17:18
 */
@RequestScoped
public class RegionSyncService implements ISyncHandler<Region> {
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
        return syncAdapter.getRegionSyncs(parentSync.getName(), date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return syncMapper.getSyncs(Filter.of(new Sync(Country.ID, SyncStatus.SYNCHRONIZED)));
    }

    private Long getParentId(Sync sync, Long organizationId){
        List<Matching> matchingList = matchingMapper.getMatchingListByNumber(Country.ENTITY,
                sync.getParentId(), organizationId);

        if (matchingList.isEmpty()){
            throw new RuntimeException("country matching not found " + sync);
        }

        return matchingList.get(0).getObjectId();
    }

    @Override
    public boolean isMatch(Region region, Sync sync, Long companyId) {
        return Objects.equals(region.getCountryId(), getParentId(sync, companyId)) &&
                equalsIgnoreCase(region.getName(), sync.getName()) &&
                equalsIgnoreCase(region.getAltName(), sync.getAltName());
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
    public List<Region> getDomains(Sync sync, Long companyId) {
        Region region = new Region();

        region.setCountryId(getParentId(sync, companyId));
        region.setName(sync.getName());
        region.setAltName(sync.getAltName());

        return domainService.getDomains(Region.class, Filter.of(region).setFilter(Filter.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(Region region, Sync sync, Long companyId) {
        Matching matching = new Matching(Region.ENTITY);

        matching.setObjectId(region.getObjectId());
        matching.setParentId(region.getCountryId());
        matching.setName(sync.getName());
        matching.setNumber(sync.getExternalId());
        matching.setCode(sync.getAdditionalExternalId());
        matching.setStartDate(sync.getDate());
        matching.setCompanyId(companyId);

        return matchingMapper.insert(matching);
    }

    @Override
    public void updateMatching(Matching matching, Sync sync, Long companyId) {
        matching.setParentId(getParentId(sync, companyId));
        matching.setName(sync.getName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(Region region, Sync sync, Long companyId) {
        region.setCountryId(getParentId(sync, companyId));
        region.setName(sync.getName());
        region.setAltName(sync.getAltName());
    }
}
