package ru.complitex.common.ui.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.ui.component.InputPanel;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 15.03.2019 22:50
 */
public class DataColumn<T extends Serializable> extends AbstractColumn<T, Sort>
        implements IDataColumn<T, Sort> {
    private String columnKey;

    public DataColumn(String columnKey) {
        super(new ResourceModel(columnKey), new Sort(columnKey));

        this.columnKey = columnKey;
    }

    @Override
    public Component getFilter(String componentId, DataForm<T> dataForm) {
        return InputPanel.of(componentId, new TextField<>(InputPanel.ID,
                new PropertyModel<>(dataForm.getModel(),"map." + columnKey)));
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new Label(columnKey, PropertyModel.of(rowModel, columnKey)));
    }
}
