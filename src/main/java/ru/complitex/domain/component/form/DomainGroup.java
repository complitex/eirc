package ru.complitex.domain.component.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.component.form.Group;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 20.05.2020 00:22
 */
public class DomainGroup extends Panel {
    public DomainGroup(String id, String entityName, int entityAttributeId, IModel<Long> model, boolean required) {
        super(id);

        setOutputMarkupId(true);

        Group group = new Group("group", new ResourceModel("_" + id));
        group.setRequired(required);
        add(group);

        group.add(new DomainInput("autocomplete", entityName, entityAttributeId, model){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                DomainGroup.this.onFilter(filter);
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                DomainGroup.this.onChange(target);
            }

            @Override
            protected String getTextValue(Domain<?> object) {
                return DomainGroup.this.getTextValue(object, super.getTextValue(object));
            }


        });
    }

    protected void onFilter(Filter<Domain<?>> filter) {

    }

    protected void onChange(AjaxRequestTarget target){

    }

    protected String getTextValue(Domain<?> object, String textValue){
        return textValue;
    }
}
