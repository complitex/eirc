package ru.complitex.domain.component.form;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.ui.form.Group;

/**
 * @author Anatoly Ivanov
 * 20.05.2020 00:22
 */
public class DomainAutoCompleteGroup extends Panel {
    public DomainAutoCompleteGroup(String id, String resourceKey, String entityName, int entityAttributeId, IModel<Long> model, boolean required) {
        super(id);

        Group group = new Group("group", new ResourceModel(resourceKey)){
            @Override
            protected boolean isRequired() {
                return required;
            }
        };
        add(group);

        group.add(new DomainAutoComplete("autocomplete", entityName, entityAttributeId, model));
    }

    public DomainAutoCompleteGroup(String id, String entityName, int entityAttributeId, IModel<Long> model, boolean required) {
        this(id, id, entityName, entityAttributeId, model, required);
    }
}
