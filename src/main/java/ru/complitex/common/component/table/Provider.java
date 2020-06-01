package ru.complitex.common.component.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.entity.Sort;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 28.11.2017 16:57
 */
public abstract class Provider<T extends Serializable> extends SortableDataProvider<T, Sort>
        implements IFilterStateLocator<FilterWrapper<T>> {
    private FilterWrapper<T> filterWrapper;

    public Provider(FilterWrapper<T> filterWrapper) {
        this.filterWrapper = filterWrapper;
    }

    public FilterWrapper<T> getFilterWrapper() {
        return filterWrapper;
    }

    public void setFilterWrapper(FilterWrapper<T> filterWrapper) {
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
            filterWrapper.setSort(getSort().getProperty());
            filterWrapper.setAscending(getSort().isAscending());
        }

        return data().iterator();
    }

    protected abstract List<T> data();
}
