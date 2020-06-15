package ru.complitex.sync.handler;

import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.Filter;
import ru.complitex.company.entity.Company;
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
import java.util.Objects;

import static ru.complitex.common.util.Strings.equalsIgnoreCase;

/**
 * @author inheaven on 22.01.2016 13:09.
 */
@RequestScoped
public class CompanySyncHandler implements ISyncHandler<Company> {
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
        return syncAdapter.getCompanySyncs(date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return null;
    }

    private Long getParentId(Sync sync, Long companyId) {
        if (sync.getParentId() == null){
            return null;            
        }

        List<Matching> matchingList = matchingMapper.getMatchingListByExternalId(Company.ENTITY_NAME,
                sync.getParentId(),  companyId);

        if (matchingList.isEmpty()) {
            throw new RuntimeException("company parent matching not found " + sync);
        }

        return matchingList.get(0).getObjectId();
    }

    @Override
    public boolean isMatch(Company company, Sync sync, Long companyId) {
        return Objects.equals(company.getParentId(), getParentId(sync, companyId)) &&
                equalsIgnoreCase(company.getName(), sync.getName()) &&
                equalsIgnoreCase(company.getAltName(), sync.getAltName()) &&
                equalsIgnoreCase(company.getShortName(), sync.getAdditionalName()) &&
                equalsIgnoreCase(company.getAltShortName(), sync.getAltAdditionalName());
    }

    @Override
    public boolean isMatch(Matching matching, Sync sync, Long companyId) {
        return Objects.equals(matching.getName(), sync.getName()); //todo additional external id
    }

    @Override
    public boolean isMatch(Matching matching1, Matching matching2) {
        return Objects.equals(matching1.getParentId(), matching2.getParentId()) &&
                matching1.getName().equals(matching2.getName());
    }

    @Override
    public List<Company> getDomains(Sync sync, Long companyId) {
        Company company = new Company();

        company.setParentId(getParentId(sync, companyId));
        company.setName(sync.getName());
        company.setAltName(sync.getAltName());
        company.setShortName(sync.getAdditionalName());
        company.setAltShortName(sync.getAltAdditionalName());

        return domainService.getDomains(Company.class, Filter.of(company).setFilter(Filter.FILTER_EQUAL));
    }

    @Override
    public Matching insertMatching(Company company, Sync sync, Long companyId) {
        return matchingMapper.insert(new Matching(Company.ENTITY_NAME, company.getObjectId(), company.getParentId(),
                sync.getExternalId(), sync.getAdditionalExternalId(), companyId));
    }

    @Override
    public void updateMatching(Matching matching, Sync domainSync, Long companyId) {
        matching.setName(domainSync.getName());

        matchingMapper.update(matching);
    }

    @Override
    public void updateNames(Company company, Sync sync, Long companyId) {
        company.setParentId(getParentId(sync, companyId));
        company.setName(sync.getName());
        company.setAltName(sync.getAltName());
        company.setShortName(sync.getAdditionalName());
        company.setAltShortName(sync.getAltAdditionalName());
        company.setEDRPOU(sync.getAdditionalParentId());
        company.setCode(sync.getAdditionalExternalId());

    }
}
