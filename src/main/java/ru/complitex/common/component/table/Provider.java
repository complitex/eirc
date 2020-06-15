package ru.complitex.common.component.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
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
public abstract class Provider<T extends Serializable> extends SortableDataProvider<T, Sort>
        implements IFilterStateLocator<Filter<T>> {
    private Filter<T> filter;

    public Provider(Filter<T> filter) {
        this.filter = filter;
    }

    public Filter<T> getFilter() {
        return filter;
    }

    public void setFilter(Filter<T> filter) {
        this.filter = filter;
    }

    @Override
    public Filter<T> getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(Filter<T> filterState) {
        this.filter = filterState;
    }

    @Override
    public IModel<T> model(T object) {
        return new CompoundPropertyModel<>(object);
    }

    @Override
    public Iterator<? extends T> iterator(long first, long count) {
        Filter<T> filter = getFilterState().limit(first, count);

        if (getSort() != null){
            filter.setSort(getSort().getProperty());
            filter.setAscending(getSort().isAscending());
        }

        return data().iterator();
    }

    protected abstract List<T> data();
}
