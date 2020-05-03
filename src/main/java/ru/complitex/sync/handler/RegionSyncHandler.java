package ru.complitex.sync.handler;

import ru.complitex.address.entity.Country;
import ru.complitex.address.entity.Region;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.domain.service.DomainService;
import ru.complitex.domain.util.Locales;
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
 * @author Anatoly A. Ivanov
 * 19.01.2018 17:18
 */
@RequestScoped
public class RegionSyncHandler implements ISyncHandler<Region> {
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
        return syncMapper.getSyncs(FilterWrapper.of(new Sync(Country.ENTITY_ID, SyncStatus.SYNCHRONIZED)));
    }

    private Long getParentId(Sync sync, Long organizationId){
        List<Matching> matchingList = matchingMapper.getMatchingListByExternalId(Country.ENTITY_NAME,
                sync.getParentId(), organizationId);

        if (matchingList.isEmpty()){
            throw new RuntimeException("country matching not found " + sync);
        }

        return matchingList.get(0).getObjectId();
    }

    @Override
    public boolean isMatch(Region region, Sync sync, Long organizationId) {
        return Objects.equals(region.getCountryId(), getParentId(sync, organizationId)) &&
                equalsIgnoreCase(region.getName(), sync.getName()) &&
                equalsIgnoreCase(region.getName(Locales.getAltLocaleId()), sync.getAltName());
    }

    @Override
    public boolean isMatch(Matching matching, Sync sync, Long organizationId) {
        return Objects.equals(matching.getParentId(), getParentId(sync, organizationId)) &&
                equalsIgnoreCase(matching.getName(), sync.getName());
    }

    @Override
    public boolean isMatch(Matching matching1, Matching matching2) {
        return Objects.equals(matching1.getParentId(), matching2.getParentId()) &&
                equalsIgnoreCase(matching1.getName(), matching2.getName());
    }

    @Override
    public List<Region> getDomains(Sync sync, Long organizationId) {
        Region region = new Region();

        region.setCountryId(getParentId(sync, organizationId));
        region.setName(sync.getName());
        region.setName(sync.getAltName(), Locales.getAltLocaleId());

        return domainService.getDomains(Region.class, FilterWrapper.of(region).setFilter(FilterWrapper.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(Region region, Sync sync, Long organizationId) {
        return matchingMapper.insert(new Matching(Region.ENTITY_NAME, region.getObjectId(), region.getCountryId(),
                sync.getExternalId(), sync.getName(), organizationId));
    }

    @Override
    public void updateMatching(Matching matching, Sync sync, Long organizationId) {
        matching.setParentId(getParentId(sync, organizationId));

        matching.setName(sync.getName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(Region region, Sync sync, Long organizationId) {
        region.setCountryId(getParentId(sync, organizationId));
        region.setName(sync.getName());
        region.setName(sync.getAltName(), Locales.getAltLocaleId());
    }
}
