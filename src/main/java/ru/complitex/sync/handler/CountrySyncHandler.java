package ru.complitex.sync.handler;


import ru.complitex.address.entity.Country;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.domain.service.DomainService;
import ru.complitex.domain.util.Locales;
import ru.complitex.eirc.adapter.SyncAdapter;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.exception.SyncException;

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
public class CountrySyncHandler implements ISyncHandler<Country> {
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
    public boolean isMatch(Country country, Sync sync, Long organizationId) {
        return equalsIgnoreCase(sync.getName(), country.getName()) &&
                equalsIgnoreCase(sync.getAltName(), country.getName(Locales.getAltLocaleId()));
    }

    @Override
    public boolean isMatch(Matching matching, Sync sync, Long organizationId) {
        return equalsIgnoreCase(matching.getName(), sync.getName());
    }

    @Override
    public boolean isMatch(Matching matching1, Matching matching2) {
        return equalsIgnoreCase(matching1.getName(), matching2.getName());
    }

    @Override
    public List<Country> getDomains(Sync sync, Long organizationId) {
        Country country = new Country();

        country.setName(sync.getName());
        country.setName(sync.getAltName(), Locales.getAltLocaleId());

        return domainService.getDomains(Country.class, FilterWrapper.of(country).setFilter(FilterWrapper.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(Country country, Sync sync, Long organizationId) {
        return matchingMapper.insert(new Matching(Country.ENTITY_NAME, country.getObjectId(), sync.getExternalId(),
                sync.getName(), organizationId));
    }

    @Override
    public void updateMatching(Matching matching, Sync sync, Long organizationId) {
        matching.setName(sync.getName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(Country country, Sync sync, Long organizationId) {
        country.setName(sync.getName());
        country.setName(sync.getAltName(), Locales.getAltLocaleId());
    }
}
