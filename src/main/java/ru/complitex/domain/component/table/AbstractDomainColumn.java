package ru.complitex.domain.component.table;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.component.form.InputPanel;
import ru.complitex.common.component.table.TableForm;
import ru.complitex.common.component.table.IFilterColumn;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.EntityAttribute;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 20.12.2017 3:20
 */
public abstract class AbstractDomainColumn<T extends Domain<T>> extends AbstractColumn<T, Sort>
        implements IFilterColumn<T, Sort>, Serializable {
    private String columnKey;

    public AbstractDomainColumn(IModel<String> displayModel) {
        super(displayModel);
    }

    public AbstractDomainColumn(IModel<String> displayModel, Sort sort) {
        super(displayModel, sort);
    }

    public AbstractDomainColumn(EntityAttribute entityAttribute){
        super(Model.of(entityAttribute.getValueText()), new Sort("", entityAttribute)); //todo sort key
    }

    public AbstractDomainColumn(String columnKey) {
        super(new ResourceModel(columnKey), new Sort(columnKey));

        this.columnKey = columnKey;
    }

    @Override
    public Component newFilter(String componentId, TableForm<T> tableForm) {
        return InputPanel.of(componentId, new TextField<>(InputPanel.ID, new PropertyModel<>(tableForm.getModel(),
                "map." + columnKey)));
    }
}
