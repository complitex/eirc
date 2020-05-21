package ru.complitex.matching.mapper;

import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.common.util.Dates;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 29.04.2020 00:21
 */
@RequestScoped
public class MatchingMapper extends BaseMapper {

    public List<Matching> getMatchingList(FilterWrapper<Matching> filterWrapper){
        return sqlSession().selectList("selectMatchingList", filterWrapper);
    }

    public Long getMatchingListCount(FilterWrapper<Matching> filterWrapper){
        return sqlSession().selectOne("selectMatchingListCount", filterWrapper);
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
        if (matching.getStartDate() == null){
            matching.setStartDate(Dates.getCurrentDate());
        }

        sqlSession().insert("insertMatching", matching);

        return matching;
    }

    public void update(Matching matching){
        sqlSession().update("updateMatching", matching);
    }

    public void delete(Long id){
        sqlSession().delete("deleteMatching", id);
    }
}
