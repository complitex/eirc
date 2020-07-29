package ru.complitex.address.mapper;

import ru.complitex.address.entity.City;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 14.07.2020 0:36
 */
@RequestScoped
public class CityMapper extends BaseMapper {
    public List<City> getCities(Filter<City> filter){
        return sqlSession().selectList("selectCities", filter);
    }

    public Long getCitiesCount(Filter<City> filter){
        return sqlSession().selectOne("selectCitiesCount", filter);
    }
}
