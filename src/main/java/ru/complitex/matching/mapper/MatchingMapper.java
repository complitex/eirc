package ru.complitex.matching.mapper;

import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.matching.entity.Matching;

import java.util.List;

/**
 * @author Anatoly Ivanov
 * 29.04.2020 00:21
 */
public class MatchingMapper extends BaseMapper {
    public List<Matching> getMatchingList(FilterWrapper<Matching> filterWrapper){
        return sqlSession().selectList("selectMatchingList", filterWrapper);
    }

    public List<Matching> getMatchingListByExternalId(String entityName, Long externalId, Long organizationId){
        Matching matching = new Matching();

        matching.setEntityName(entityName);
        matching.setExternalId(externalId);
        matching.setOrganizationId(organizationId);

        return getMatchingList(FilterWrapper.of(matching));
    }

    public Matching insert(Matching matching){

        return matching;
    }

    public void update(Matching matching){

    }
}
