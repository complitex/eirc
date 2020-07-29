package ru.complitex.address.mapper;

import ru.complitex.address.entity.District;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 14.07.2020 0:36
 */
@RequestScoped
public class DistrictMapper extends BaseMapper {
    public List<District> getDistricts(Filter<District> filter){
        return sqlSession().selectList("selectDistricts", filter);
    }

    public Long getDistrictsCount(Filter<District> filter){
        return sqlSession().selectOne("selectDistrictsCount", filter);
    }
}
