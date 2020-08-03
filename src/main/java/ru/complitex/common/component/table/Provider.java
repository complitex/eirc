package ru.complitex.common.component.table;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 28.11.2017 16:57
 */
public abstract class Provider<T extends Serializable> extends SortableDataProvider<T, Sort> {
    private IModel<Filter<T>> filterModel;

    public Provider(IModel<Filter<T>> filterModel) {
        this.filterModel = filterModel;
    }

    public IModel<Filter<T>> getFilterModel() {
        return filterModel;
    }

    public void setFilterModel(IModel<Filter<T>> filterModel) {
        this.filterModel = filterModel;
    }

    public Filter<T> getFilter() {
        return filterModel.getObject();
    }

    public void setFilter(Filter<T> filter) {
        filterModel.setObject(filter);
    }

    @Override
    public IModel<T> model(T object) {
        return new CompoundPropertyModel<>(object);
    }

    @Override
    public Iterator<? extends T> iterator(long first, long count) {
        Filter<T> filter = filterModel.getObject().limit(first, count);

        if (getSort() != null){
            filter.setSort(getSort().getProperty());
            filter.setAscending(getSort().isAscending());
        }

        return data().iterator();
    }

    protected abstract List<T> data();
}
