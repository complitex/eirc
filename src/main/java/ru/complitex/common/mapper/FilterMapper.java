package ru.complitex.common.mapper;

import ru.complitex.common.entity.Filter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 08.08.2020 2:20
 */
public class FilterMapper<T extends Serializable> extends BaseMapper implements IFilterMapper<T> {
    private String name;

    public FilterMapper() {
    }

    public FilterMapper(String name) {
        this.name = name;
    }

    @Override
    public Long getCount(Filter<T> filter) {
        return sqlSession().selectOne("select" + name + "Count", filter);
    }

    @Override
    public List<T> getList(Filter<T> filter) {
        return sqlSession().selectList("select" + name + "List", filter);
    }
}
