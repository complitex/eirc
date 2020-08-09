package ru.complitex.common.mapper;

import ru.complitex.common.entity.Filter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 08.08.2020 2:21
 */
public interface IFilterMapper<T extends Serializable> {
    Long getCount(Filter<T> filter);

    List<T> getList(Filter<T> filter);
}
