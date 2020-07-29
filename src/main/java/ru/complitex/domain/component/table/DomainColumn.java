package ru.complitex.domain.component.table;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.cdi.NonContextual;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.complitex.common.component.form.InputPanel;
import ru.complitex.common.component.form.TextField;
import ru.complitex.common.component.table.Column;
import ru.complitex.common.component.table.TableForm;
import ru.complitex.domain.entity.*;
import ru.complitex.domain.model.DateModel;
import ru.complitex.domain.model.DecimalModel;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.model.TextModel;
import ru.complitex.domain.service.DomainService;
import ru.complitex.domain.service.EntityService;
import ru.complitex.domain.util.Attributes;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Anatoly A. Ivanov
 * 19.12.2017 7:55
 */
public class DomainColumn<T extends Domain<T>> extends Column<T> {
    private static Logger log = LoggerFactory.getLogger(DomainColumn.class);

    @Inject
    private transient EntityService entityService;

    @Inject
    private transient DomainService domainService;

    private EntityAttribute entityAttribute;

    private SerializableConsumer<AjaxRequestTarget> onChange;

    public DomainColumn(EntityAttribute entityAttribute) {
        super(Model.of(entityAttribute.getValueText()), new EntityAttributeSort(entityAttribute));

        this.entityAttribute = entityAttribute;
    }

    public DomainColumn(EntityAttribute entityAttribute, SerializableConsumer<AjaxRequestTarget> onChange) {
        this(entityAttribute);

        this.onChange = onChange;
    }

    public EntityService getEntityService() {
        if (entityService == null) {
            NonContextual.of(this).inject(this);
        }

        return entityService;
    }

    public DomainService getDomainService() {
        if (domainService == null) {
            NonContextual.of(this).inject(this);
        }

        return domainService;
    }

    @Override
    public Component newFilter(String componentId, TableForm<T> tableForm) {
        IModel<T> domainModel = Model.of(tableForm.getModelObject().getObject());

        int entityAttributeId = entityAttribute.getEntityAttributeId();

        FormComponent<?> component;

        switch (entityAttribute.getValueTypeId()) {
            case ValueType.NUMBER:
                component = new TextField<>(InputPanel.ID, new NumberModel<>(domainModel, entityAttributeId),
                        Long.class);
            case ValueType.DECIMAL:
                component = new TextField<>(InputPanel.ID, new DecimalModel<>(domainModel, entityAttributeId),
                        BigDecimal.class);
            case ValueType.DATE:
                component = new DateTextField(InputPanel.ID,
                        new DateModel<>(domainModel, entityAttributeId),
                        new DateTextFieldConfig().withFormat("dd.MM.yyyy").withLanguage("ru").autoClose(true));
            default:
                component = new TextField<>(InputPanel.ID, new TextModel<>(domainModel, entityAttributeId,
                        StringType.DEFAULT));

        }

        if (onChange != null) {
            component.add(OnChangeAjaxBehavior.onChange(onChange));
        }

        return InputPanel.of(componentId, component);
    }

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        String text = "";

        Attribute attribute = rowModel.getObject().getOrCreateAttribute(entityAttribute.getEntityAttributeId());

        switch (entityAttribute.getValueTypeId()) {
            case ValueType.TEXT_VALUE:
                List<Value> values = attribute.getValues();

                if (values != null && !values.isEmpty()) {
                    if (values.get(0).getLocaleId() != 0) {
                        text = Attributes.displayText(entityAttribute,
                                attribute.getTextValue(entityAttribute.getEntityAttributeId()));
                    } else {
                        text = values.stream()
                                .filter(v -> v.getLocaleId() == 0)
                                .map(v -> Attributes.displayText(entityAttribute, v.getText()))
                                .collect(Collectors.joining("\n"));
                    }
                }

                break;
            case ValueType.REFERENCE:
                if (attribute.getNumber() != null) {
                    text = displayReference(entityAttribute.getReferenceEntityId(), attribute.getNumber(), rowModel);
                }

                break;
            case ValueType.NUMBER:
                text = attribute.getNumber() != null ? attribute.getNumber() + "" : "";

                break;
            case ValueType.DECIMAL:
                text = attribute.getDecimal() != null ? attribute.getDecimal() + "" : "";

                break;

            case ValueType.DATE:
                text = attribute.getDate() != null ? DATE_FORMAT.format(attribute.getDate()) : "";

                break;

            case ValueType.REFERENCE_VALUE:
                if (attribute.getValues() != null && entityAttribute.getReferenceEntityAttributeId() != null) {
                    EntityAttribute referenceEntityAttribute = getEntityService().getReferenceEntityAttribute(entityAttribute);

                    if (referenceEntityAttribute != null) {
                        List<Long> list = attribute.getValues().stream()
                                .map(Value::getNumber)
                                .collect(Collectors.toList());

                        text = list.stream()
                                .map(id -> {
                                    Domain<?> domain = getDomainService().getDomain(referenceEntityAttribute.getEntityName(), id);

                                    return Attributes.displayText(referenceEntityAttribute,
                                            domain.getTextValue(referenceEntityAttribute.getEntityAttributeId()));
                                })
                                .collect(Collectors.joining("\n"));
                    } else {
                        List<String> list = attribute.getValues().stream()
                                .map(v -> Attributes.displayText(entityAttribute, v.getText()))
                                .collect(Collectors.toList());

                        text = String.join("\n", list);
                    }
                }

                break;
            default:
                text = Attributes.displayText(entityAttribute, attribute.getText());
        }

        cellItem.add(new Label(componentId, text));
    }

    protected String displayReference(int referenceEntityId, Long objectId, IModel<T> rowModel) {
        Domain<?> refDomain = getDomainService().getDomainRef(referenceEntityId, objectId);

        EntityAttribute referenceEntityAttribute = getEntityService().getReferenceEntityAttribute(entityAttribute);

        String text;

        switch (referenceEntityAttribute.getValueTypeId()) {
            case ValueType.REFERENCE:
                text = displayReference(referenceEntityAttribute.getEntityId(), refDomain.getNumber(referenceEntityAttribute.getEntityAttributeId()), rowModel);
                break;
            case ValueType.TEXT_VALUE:
                text = refDomain.getTextValue(referenceEntityAttribute.getEntityAttributeId());
                break;
            case ValueType.TEXT:
                text = refDomain.getText(referenceEntityAttribute.getEntityAttributeId());
                break;
            case ValueType.NUMBER:
                text = refDomain.getNumber(referenceEntityAttribute.getEntityAttributeId()) + "";
                break;
            default:
                text = "[" + referenceEntityAttribute.getEntityAttributeId() + "]";
        }

        return Attributes.displayText(referenceEntityAttribute, text);
    }

    public EntityAttribute getEntityAttribute() {
        return entityAttribute;
    }
}
