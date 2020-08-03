package ru.complitex.common.component.table;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.component.form.InputPanel;
import ru.complitex.common.entity.Sort;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 15.03.2019 22:50
 */
public class PropertyColumn<T extends Serializable> extends Column<T> {
    private final String property;

    public PropertyColumn(String property) {
        super(new ResourceModel(property), new Sort(property));

        this.property = property;
    }

    @Override
    public Component newFilter(String componentId, Table<T> table) {
        return InputPanel.of(componentId, new TextField<>(InputPanel.ID,
                PropertyModel.of(table.getFilterModel(),"object." + property)));
    }

    protected IModel<?> newItemModel(IModel<T> rowModel){
        return PropertyModel.of(rowModel, property);
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new Label(componentId, newItemModel(rowModel)));
    }
}
