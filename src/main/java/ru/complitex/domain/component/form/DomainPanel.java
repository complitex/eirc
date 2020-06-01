package ru.complitex.domain.component.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import ru.complitex.domain.entity.Attribute;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.entity.ValueType;
import ru.complitex.domain.service.EntityService;
import ru.complitex.domain.util.Attributes;
import ru.complitex.domain.util.Locales;

import javax.inject.Inject;

/**
 * @author Anatoly A. Ivanov
 * 22.12.2017 12:45
 */
public class DomainPanel extends AbstractDomainPanel {
    @Inject
    private EntityService entityService;

    private EntityAttribute entityAttribute;

    public DomainPanel(String id, EntityAttribute entityAttribute,
                       IModel<Long> model, SerializableConsumer<AjaxRequestTarget> onChange) {
        super(id, entityAttribute.getEntityName(), model, onChange);

        this.entityAttribute = entityAttribute;
    }

    public DomainPanel(String id, EntityAttribute entityAttribute, IModel<Long> model) {
        this(id, entityAttribute,  model, null);
    }

    public DomainPanel(String id, String entityName, int entityAttributeId, IModel<Long> model) {
        super(id, entityName, model, null);

        this.entityAttribute = entityService.getEntityAttribute(entityName, entityAttributeId);
    }

    protected Domain getFilterObject(String input){
        Domain domain = new Domain(entityAttribute.getEntityName());
        domain.getOrCreateAttribute(entityAttribute.getEntityAttributeId()).setText(input);

        return domain;
    }

    protected String getTextValue(Domain domain) {
        Attribute attribute = domain.getOrCreateAttribute(entityAttribute.getEntityAttributeId());

        switch (entityAttribute.getValueTypeId()){
            case ValueType.TEXT_LIST:
                String textValue = attribute.getOrCreateValue(Locales.getSystemLocaleId()).getText();

                return Attributes.displayText(entityAttribute, textValue);
            case ValueType.TEXT:
                return attribute.getText();
            case ValueType.NUMBER:
                return attribute.getNumber() + "";
        }

        return null;
    }
}
