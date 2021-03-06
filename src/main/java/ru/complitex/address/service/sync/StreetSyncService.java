package ru.complitex.address.service.sync;

import ru.complitex.address.entity.City;
import ru.complitex.address.entity.CityType;
import ru.complitex.address.entity.Street;
import ru.complitex.address.entity.StreetType;
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
 * @author Anatoly Ivanov
 * Date: 03.08.2014 6:46
 */
@RequestScoped
public class StreetSyncService implements ISyncHandler<Street> {

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

        return syncAdapter.getStreetSyncs(parentSync.getName(), cityTypeSyncs.get(0).getAdditionalName(), date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return syncMapper.getSyncs(Filter.of(new Sync(City.ID, SyncStatus.SYNCHRONIZED)));
    }

    private Long getParentId(Sync sync, Long organizationId){
        List<Matching> matchingList = matchingMapper.getMatchingListByNumber(City.ENTITY,
                sync.getParentId(), organizationId);

        if (matchingList.isEmpty()){
            throw new RuntimeException("city matching not found " + sync);
        }

        return matchingList.get(0).getObjectId();
    }

    private Long getAdditionalParentId(Sync sync, Long organizationId){
        List<Matching> matchingList = matchingMapper.getMatchingListByNumber(StreetType.ENTITY,
                Long.valueOf(sync.getAdditionalParentId()), organizationId);

        if (matchingList.isEmpty()) {
            throw new RuntimeException("street type matching not found" + sync);
        }

        return matchingList.get(0).getObjectId();
    }

    @Override
    public boolean isMatch(Street street, Sync sync, Long companyId) {
        return Objects.equals(street.getCityId(), getParentId(sync, companyId)) &&
                Objects.equals(street.getStreetTypeId(), getAdditionalParentId(sync, companyId)) &&
                equalsIgnoreCase(street.getName(), sync.getName()) &&
                equalsIgnoreCase(street.getAltName(),sync.getAltName());
    }

    @Override
    public boolean isMatch(Matching matching, Sync sync, Long companyId) {
        return Objects.equals(matching.getParentId(),getParentId(sync, companyId)) &&
                Objects.equals(matching.getAdditionalParentId(), getAdditionalParentId(sync, companyId)) &&
                equalsIgnoreCase(matching.getName(), sync.getName());
    }

    @Override
    public boolean isMatch(Matching matching1, Matching matching2) {
        return Objects.equals(matching1.getParentId(), matching2.getParentId()) &&
                Objects.equals(matching1.getAdditionalParentId(), matching2.getAdditionalParentId()) &&
                equalsIgnoreCase(matching1.getName(), matching2.getName());
    }

    @Override
    public List<Street> getDomains(Sync sync, Long companyId) {
        Street street = new Street();

        street.setCityId(getParentId(sync, companyId));
        street.setStreetTypeId(getAdditionalParentId(sync, companyId));
        street.setName(sync.getName());
        street.setAltName(sync.getAltName());

        return domainService.getDomains(Street.class, Filter.of(street).setFilter(Filter.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(Street street, Sync sync, Long companyId) {
        Matching matching = new Matching(Street.ENTITY);

        matching.setObjectId(street.getObjectId());
        matching.setParentId(street.getCityId());
        matching.setAdditionalParentId(street.getStreetTypeId());
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
        matching.setAdditionalParentId(getAdditionalParentId(sync, companyId));
        matching.setName(sync.getName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(Street street, Sync sync, Long companyId) {
        street.setCityId(getParentId(sync, companyId));
        street.setStreetTypeId(getAdditionalParentId(sync, companyId));
        street.setName(sync.getName());
        street.setAltName(sync.getAltName());
    }
}
