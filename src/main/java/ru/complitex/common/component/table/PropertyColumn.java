package ru.complitex.common.component.table;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.component.form.TextInput;
import ru.complitex.common.entity.Sort;

import java.io.Serializable;

/**
 * @author Anatoly Ivanov
 * 07.08.2020 19:46
 */
public class PropertyColumn<T extends Serializable> extends Column<T>{
    private final String property;

    public PropertyColumn(String property) {
        super(new ResourceModel(property), new Sort(property));

        this.property = property;
    }

    @Override
    public Component filter(String componentId, Table<T> table) {
        return new TextInput<>(componentId, PropertyModel.of(table.getFilterModel(), "object." + property), table::update);
    }

    @Override
    public IModel<?> model(IModel<T> model) {
        return PropertyModel.of(model, property);
    }
}
