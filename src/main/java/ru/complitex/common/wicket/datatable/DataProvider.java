package ru.complitex.common.wicket.datatable;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.entity.SortProperty;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 28.11.2017 16:57
 */
public abstract class DataProvider<T extends Serializable> extends SortableDataProvider<T, SortProperty>
        implements IFilterStateLocator<FilterWrapper<T>> {
    private FilterWrapper<T> filterWrapper;

    public DataProvider(FilterWrapper<T> filterWrapper) {
        this.filterWrapper = filterWrapper;
    }

    @Override
    public FilterWrapper<T> getFilterState() {
        return filterWrapper;
    }

    @Override
    public void setFilterState(FilterWrapper<T> filterState) {
        this.filterWrapper = filterState;
    }

    @Override
    public IModel<T> model(T object) {
        return new CompoundPropertyModel<>(object);
    }

    @Override
    public Iterator<? extends T> iterator(long first, long count) {
        FilterWrapper<T> filterWrapper = getFilterState().limit(first, count);

        if (getSort() != null){
            filterWrapper.setSortProperty(getSort().getProperty());
            filterWrapper.setAscending(getSort().isAscending());
        }

        return data().iterator();
    }

    protected abstract List<T> data();
}
