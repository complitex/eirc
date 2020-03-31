package ru.complitex.domain.component.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.entity.SortProperty;
import ru.complitex.common.wicket.datatable.FilterDataForm;
import ru.complitex.common.wicket.datatable.IFilterDataColumn;
import ru.complitex.common.wicket.datatable.TextDataFilter;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.EntityAttribute;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 20.12.2017 3:20
 */
public abstract class AbstractDomainColumn<T extends Domain>  extends AbstractColumn<T, SortProperty>
        implements IFilterDataColumn<T, SortProperty>, Serializable {
    private EntityAttribute entityAttribute;

    private String columnKey;

    public AbstractDomainColumn(IModel<String> displayModel) {
        super(displayModel);
    }

    public AbstractDomainColumn(IModel<String> displayModel, SortProperty sortProperty) {
        super(displayModel, sortProperty);
    }

    public AbstractDomainColumn(EntityAttribute entityAttribute){
        super(Model.of(entityAttribute.getValueText()), new SortProperty(entityAttribute.getValueType().getKey(),
                entityAttribute));
    }

    public AbstractDomainColumn(String columnKey) {
        super(new ResourceModel(columnKey), new SortProperty(columnKey));

        this.columnKey = columnKey;
    }

    @Override
    public Component getFilter(String componentId, FilterDataForm<?> form) {
        return new TextDataFilter<>(componentId, new PropertyModel<>(form.getModel(),
                "map." + columnKey), form);
    }
}
