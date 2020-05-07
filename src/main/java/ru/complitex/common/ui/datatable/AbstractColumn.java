package ru.complitex.common.ui.datatable;

import org.apache.wicket.model.IModel;
import ru.complitex.common.entity.SortProperty;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 15.03.2019 22:50
 */
public abstract class AbstractColumn<T extends Serializable> extends org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn<T, SortProperty>
        implements IDataColumn<T, SortProperty> {

    public AbstractColumn(IModel<String> displayModel, SortProperty sortProperty) {
        super(displayModel, sortProperty);
    }

    public AbstractColumn(IModel<String> displayModel) {
        super(displayModel);
    }
}
