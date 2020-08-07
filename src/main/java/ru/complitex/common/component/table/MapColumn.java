package ru.complitex.common.component.table;

import org.apache.wicket.Component;
import org.apache.wicket.model.*;
import ru.complitex.common.component.form.TextFieldPanel;
import ru.complitex.common.entity.Sort;

import java.io.Serializable;

/**
 * @author Anatoly Ivanov
 * 07.08.2020 19:46
 */
public abstract class MapColumn<T extends Serializable> extends Column<T>{
    private final String property;

    public MapColumn(String property) {
        super(new ResourceModel(property), new Sort(property));

        this.property = property;
    }

    @Override
    public Component filter(String componentId, Table<T> table) {
        return new TextFieldPanel<>(componentId, PropertyModel.of(table.getFilterModel(), "map." + property), table::update);
    }

    public abstract String text(IModel<T> model);

    @Override
    public IModel<?> model(IModel<T> model) {
        return new LoadableDetachableModel<>() {
            @Override
            protected Object load() {
                return text(model);
            }
        };
    }
}
