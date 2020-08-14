package ru.complitex.address.service.sync;


import ru.complitex.address.entity.Country;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.service.DomainService;
import ru.complitex.sync.adapter.SyncAdapter;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.exception.SyncException;
import ru.complitex.sync.service.ISyncHandler;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static ru.complitex.common.util.Strings.equalsIgnoreCase;

/**
 * @author Anatoly A. Ivanov
 * 19.01.2018 17:19
 */
@RequestScoped
public class CountrySyncService implements ISyncHandler<Country> {
    @Inject
    private DomainService domainService;

    @Inject
    private MatchingMapper matchingMapper;

    @Inject
    private SyncAdapter syncAdapter;

    @Override
    public Cursor<Sync> getCursorSyncs(Sync parentSync, Date date) throws SyncException {
        return syncAdapter.getCountrySyncs(date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return null;
    }

    @Override
    public boolean isMatch(Country country, Sync sync, Long companyId) {
        return equalsIgnoreCase(sync.getName(), country.getName()) &&
                equalsIgnoreCase(sync.getAltName(), country.getAltName());
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
    public List<Country> getDomains(Sync sync, Long companyId) {
        Country country = new Country();

        country.setName(sync.getName());
        country.setAltName(sync.getAltName());

        return domainService.getDomains(Country.class, Filter.of(country).setFilter(Filter.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(Country country, Sync sync, Long companyId) {
        Matching matching = new Matching(Country.ENTITY);

        matching.setObjectId(country.getObjectId());
        matching.setName(sync.getName());
        matching.setNumber(sync.getExternalId());
        matching.setCode(sync.getAdditionalExternalId());
        matching.setStartDate(sync.getDate());
        matching.setCompanyId(companyId);

        return matchingMapper.insert(matching);
    }

    @Override
    public void updateMatching(Matching matching, Sync sync, Long companyId) {
        matching.setName(sync.getName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(Country country, Sync sync, Long companyId) {
        country.setName(sync.getName());
        country.setAltName(sync.getAltName());
    }
}
