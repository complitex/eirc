package ru.complitex.address.mapper.matching;

import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 08.08.2020 1:52
 */
@RequestScoped
public class RegionMatchingMapper extends BaseMapper {
    public Long getRegionMatchingListCount(Filter<Matching> filter){
        return sqlSession().selectOne("selectRegionMatchingListCount", filter);
    }

    public List<Matching> getRegionMatchingList(Filter<Matching> filter){
        return sqlSession().selectList("selectRegionMatchingList", filter);
    }
}
