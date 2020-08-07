package ru.complitex.address.mapper;

import ru.complitex.address.entity.Street;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 14.07.2020 0:36
 */
@RequestScoped
public class StreetMapper extends BaseMapper {
    public Long getStreetsCount(Filter<Street> filter){
        return sqlSession().selectOne("selectStreetsCount", filter);
    }

    public List<Street> getStreets(Filter<Street> filter){
        return sqlSession().selectList("selectStreets", filter);
    }
}
