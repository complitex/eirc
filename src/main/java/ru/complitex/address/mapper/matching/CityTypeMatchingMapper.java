package ru.complitex.address.mapper.matching;

import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 08.08.2020 2:08
 */
@RequestScoped
public class CityTypeMatchingMapper extends BaseMapper {
    public Long getCityTypeMatchingListCount(Filter<Matching> filter){
        return sqlSession().selectOne("selectCityTypeMatchingListCount", filter);
    }

    public List<Matching> getCityTypeMatchingList(Filter<Matching> filter){
        return sqlSession().selectList("selectCityTypeMatchingList", filter);
    }
}
