package ru.complitex.address.mapper.matching;

import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 07.08.2020 17:08
 */
@RequestScoped
public class CountryMatchingMapper extends BaseMapper {
    public Long getCountryMatchingListCount(Filter<Matching> filter){
        return sqlSession().selectOne("selectCountryMatchingListCount", filter);
    }

    public List<Matching> getCountryMatchingList(Filter<Matching> filter){
        return sqlSession().selectList("selectCountryMatchingList", filter);
    }
}
