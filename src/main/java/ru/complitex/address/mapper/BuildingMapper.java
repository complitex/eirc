package ru.complitex.address.mapper;

import ru.complitex.address.entity.Building;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 14.07.2020 0:36
 */
@RequestScoped
public class BuildingMapper extends BaseMapper {
    public List<Building> getBuildings(Filter<Building> filter){
        return sqlSession().selectList("selectBuildings", filter);
    }

    public Long getBuildingsCount(Filter<Building> filter){
        return sqlSession().selectOne("selectBuildingsCount", filter);
    }
}
