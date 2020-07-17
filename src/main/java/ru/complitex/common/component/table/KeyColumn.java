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
public class KeyColumn<T extends Serializable> extends Column<T> {
    private String columnKey;

    public KeyColumn(String columnKey) {
        super(new ResourceModel(columnKey), new Sort(columnKey));

        this.columnKey = columnKey;
    }

    @Override
    public Component newFilter(String componentId, TableForm<T> tableForm) {
        return InputPanel.of(componentId, new TextField<>(InputPanel.ID,
                new PropertyModel<>(tableForm.getModel(),"map." + columnKey)));
    }

    protected IModel<?> newItemModel(IModel<T> rowModel){
        return PropertyModel.of(rowModel, columnKey);
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new Label(componentId, newItemModel(rowModel)));
    }
}
