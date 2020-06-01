package ru.complitex.domain.component.table;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.component.form.InputPanel;
import ru.complitex.common.component.table.TableForm;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 20.12.2017 2:17
 */
public class DomainIdColumn<T extends Domain<T>> extends AbstractDomainColumn<T> {
    public DomainIdColumn() {
        super(Model.of("№"), new Sort("id"));
    }

    @Override
    public Component newFilter(String componentId, TableForm<T> tableForm) {
        return  InputPanel.of(componentId, new TextField<>(InputPanel.ID, new PropertyModel<>(tableForm.getModelObject(),
                "object.objectId")));
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new Label(componentId, () -> rowModel.getObject().getObjectId()));
    }

    @Override
    public String getCssClass() {
        return "domain-id-column";
    }
}
