package ru.complitex.common.model;

import org.apache.wicket.model.IModel;
import ru.complitex.common.entity.Filter;

import java.io.Serializable;

/**
 * @author Anatoly Ivanov
 * 03.08.2020 17:15
 */
public class FilterModel<T extends Serializable> implements IModel<Filter<T>> {
    private Filter<T> filter;

    public FilterModel(T object) {
        this.filter = Filter.of(object);
    }

    @Override
    public Filter<T> getObject() {
        return filter;
    }

    @Override
    public void setObject(Filter<T> filter) {
        this.filter = filter;
    }

    public static <T extends Serializable> FilterModel<T> of(T object){
        return new FilterModel<>(object);
    }
}
