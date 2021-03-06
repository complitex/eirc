package ru.complitex.matching.mapper;

import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.FilterMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 29.04.2020 00:21
 */
@RequestScoped
public class MatchingMapper extends FilterMapper<Matching> {

    public MatchingMapper() {
        super("Matching");
    }

    public List<Matching> getMatchingList(String entityName, Long companyId){
        Matching matching = new Matching();

        matching.setEntityName(entityName);
        matching.setCompanyId(companyId);

        return getList(Filter.of(matching));
    }

    public List<Matching> getMatchingListByNumber(String entityName, Long number, Long companyId){
        Matching matching = new Matching();

        matching.setEntityName(entityName);
        matching.setNumber(number);
        matching.setCompanyId(companyId);

        return getList(Filter.of(matching));
    }

    public List<Matching> getMatchingListByObjectId(String entityName, Long objectId, Long companyId){
        Matching matching = new Matching();

        matching.setEntityName(entityName);
        matching.setObjectId(objectId);
        matching.setCompanyId(companyId);

        return getList(Filter.of(matching));
    }

    public Matching insert(Matching matching){
        sqlSession().insert("insertMatching", matching);

        return matching;
    }

    public void update(Matching matching){
        sqlSession().update("updateMatching", matching);
    }

    public void save(Matching matching){
        if (matching.getId() == null){
            insert(matching);
        }else {
            update(matching);
        }
    }

    public void delete(Matching matching){
        sqlSession().delete("deleteMatching", matching);
    }
}
