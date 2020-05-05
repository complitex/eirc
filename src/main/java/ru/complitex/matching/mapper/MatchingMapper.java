package ru.complitex.matching.mapper;

import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.domain.service.EntityService;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 29.04.2020 00:21
 */
@RequestScoped
public class MatchingMapper extends BaseMapper {
    @Inject
    private EntityService entityService;

    public List<Matching> getMatchingList(FilterWrapper<Matching> filterWrapper){
        return sqlSession().selectList("selectMatchingList", filterWrapper);
    }

    public List<Matching> getMatchingList(String entityName, Long companyId){
        Matching matching = new Matching();

        matching.setEntityName(entityName);
        matching.setCompanyId(companyId);

        return getMatchingList(FilterWrapper.of(matching));
    }

    public List<Matching> getMatchingListByExternalId(String entityName, Long externalId, Long companyId){
        Matching matching = new Matching();

        matching.setEntityName(entityName);
        matching.setExternalId(externalId);
        matching.setCompanyId(companyId);

        return getMatchingList(FilterWrapper.of(matching));
    }

    public List<Matching> getMatchingListByObjectId(String entityName, Long objectId, Long companyId){
        Matching matching = new Matching();

        matching.setEntityName(entityName);
        matching.setObjectId(objectId);
        matching.setCompanyId(companyId);

        return getMatchingList(FilterWrapper.of(matching));
    }

    public Matching insert(Matching matching){

        return matching;
    }

    public void update(Matching matching){

    }

    public void delete(Matching matching){

    }
}
