package ru.complitex.domain.component.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.component.form.Group;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 20.05.2020 00:22
 */
public class DomainGroup extends Group {
    public DomainGroup(String id, IModel<String> labelModel, String entityName, IModel<Long> model, int entityAttributeId) {
        super(id, labelModel);

        add(new DomainInput("input", entityName, model, entityAttributeId){
            @Override
            public boolean isRequired() {
                return DomainGroup.this.isRequired();
            }

            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                DomainGroup.this.onFilter(filter);
            }

            @Override
            protected void onChangeId(AjaxRequestTarget target) {
                DomainGroup.this.onChange(target);
            }

            @Override
            protected String getTextValue(Domain<?> object) {
                return DomainGroup.this.getTextValue(object, super.getTextValue(object));
            }
        });
    }

    public DomainGroup(String id, String entityName, IModel<Long> model, int entityAttributeId) {
        this(id, new ResourceModel("_" + id), entityName, model, entityAttributeId);
    }

    protected void onFilter(Filter<Domain<?>> filter) {

    }

    protected void onChange(AjaxRequestTarget target){

    }

    protected String getTextValue(Domain<?> object, String textValue){
        return textValue;
    }
}
