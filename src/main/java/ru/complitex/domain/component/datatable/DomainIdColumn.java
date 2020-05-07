package ru.complitex.domain.component.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.common.entity.SortProperty;
import ru.complitex.common.ui.datatable.DataForm;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 20.12.2017 2:17
 */
public class DomainIdColumn<T extends Domain<T>> extends AbstractDomainColumn<T> {
    public DomainIdColumn() {
        super(Model.of("№"), new SortProperty("id"));
    }

    @Override
    public Component getFilter(String componentId, DataForm<T> dataForm) {
        return new TextField<>(componentId, new PropertyModel<>(dataForm.getModelObject(), "object.objectId"));
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
