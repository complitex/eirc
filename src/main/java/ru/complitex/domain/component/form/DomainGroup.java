package ru.complitex.domain.component.form;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.component.form.Group;

/**
 * @author Anatoly Ivanov
 * 20.05.2020 00:22
 */
public class DomainGroup extends Panel {
    public DomainGroup(String id, String entityName, int entityAttributeId, IModel<Long> model, boolean required) {
        super(id);

        Group group = new Group("group", new ResourceModel("_" + id)){
            @Override
            protected boolean isRequired() {
                return required;
            }
        };
        add(group);

        group.add(new DomainPanel("autocomplete", entityName, entityAttributeId, model));
    }
}
