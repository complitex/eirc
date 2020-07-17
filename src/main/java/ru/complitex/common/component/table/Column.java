package ru.complitex.common.component.table;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import ru.complitex.common.entity.Sort;

import java.io.Serializable;

/**
 * @author Anatoly Ivanov
 * 13.07.2020 19:03
 */
public abstract class Column<T extends Serializable> extends AbstractColumn<T, Sort> implements IFilterColumn<T, Sort>{
    private String cssClass;

    public Column(IModel<String> displayModel, Sort sortProperty) {
        super(displayModel, sortProperty);
    }

    @Override
    public abstract Component newFilter(String componentId, TableForm<T> tableForm);

    @Override
    public abstract void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel);

    @Override
    public String getCssClass() {
        return cssClass;
    }

    public Column<T> setCssClass(String cssClass){
        this.cssClass = cssClass;

        return this;
    }
}
