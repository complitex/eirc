package ru.complitex.address.mapper;

import ru.complitex.address.entity.Apartment;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 14.07.2020 0:36
 */
@RequestScoped
public class ApartmentMapper extends BaseMapper {
    public List<Apartment> getApartments(Filter<Apartment> filter){
        return sqlSession().selectList("selectApartments", filter);
    }

    public Long getApartmentsCount(Filter<Apartment> filter){
        return sqlSession().selectOne("selectApartmentsCount", filter);
    }
}
