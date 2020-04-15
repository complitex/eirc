package ru.complitex.domain.component.datatable;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import org.apache.wicket.Component;
import org.apache.wicket.cdi.NonContextual;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.wicket.component.InputPanel;
import ru.complitex.common.wicket.datatable.FilterDataForm;
import ru.complitex.common.wicket.datatable.TextDataFilter;
import ru.complitex.domain.entity.*;
import ru.complitex.domain.model.DateAttributeModel;
import ru.complitex.domain.model.DecimalAttributeModel;
import ru.complitex.domain.model.NumberAttributeModel;
import ru.complitex.domain.model.TextAttributeModel;
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
public class DomainColumn<T extends Domain<T>> extends AbstractDomainColumn<T> {
    private static Logger log = LoggerFactory.getLogger(DomainColumn.class);

    @Inject
    private transient EntityService entityService;

    @Inject
    private transient DomainService domainService;

    private EntityAttribute entityAttribute;

    public DomainColumn(EntityAttribute entityAttribute) {
        super(entityAttribute);

        this.entityAttribute = entityAttribute;
    }

    public EntityService getEntityService() {
        if (entityService == null){
            NonContextual.of(this).inject(this);
        }

        return entityService;
    }

    public DomainService getDomainService() {
        if (domainService == null){
            NonContextual.of(this).inject(this);
        }

        return domainService;
    }

    @Override
    public Component getFilter(String componentId, FilterDataForm<?> form) {
        Long entityAttributeId = entityAttribute.getEntityAttributeId();

        @SuppressWarnings("unchecked")
        Domain<T> domain = ((FilterWrapper<T>)form.getModelObject()).getObject();

        domain.getOrCreateAttribute(entityAttributeId).setEntityAttribute(entityAttribute);

        switch (entityAttribute.getValueType()){
            case NUMBER:
                TextDataFilter<Long> textFilter = new TextDataFilter<>(componentId, new NumberAttributeModel(domain, entityAttributeId), form);
                textFilter.getFilter().setType(Long.class);

                return textFilter;
            case DECIMAL:
                TextDataFilter<BigDecimal> decimalFilter = new TextDataFilter<>(componentId, new DecimalAttributeModel(domain, entityAttributeId), form);
                decimalFilter.getFilter().setType(BigDecimal.class);

                return decimalFilter;
            case DATE:
                return new InputPanel(componentId, new DateTextField(InputPanel.INPUT_COMPONENT_ID,
                        new DateAttributeModel(domain, entityAttributeId),
                        new DateTextFieldConfig().withFormat("dd.MM.yyyy").withLanguage("ru").autoClose(true)));
            default:
                return new TextDataFilter<>(componentId, new TextAttributeModel(domain, entityAttributeId, StringType.DEFAULT), form);
        }
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        String text = "";

        Attribute attribute = rowModel.getObject().getOrCreateAttribute(entityAttribute.getEntityAttributeId());

        switch (entityAttribute.getValueType()){
            case TEXT_LIST:
                List<Value> values = attribute.getValues();

                if (values != null && !values.isEmpty()) {
                    if (values.get(0).getLocaleId() != null){
                        text = Attributes.displayText(entityAttribute,
                                attribute.getTextValue(entityAttribute.getEntityAttributeId()));
                    }else{
                        text = values.stream()
                                .filter(v -> v.getLocaleId() == null)
                                .map(v -> Attributes.displayText(entityAttribute, v.getText()))
                                .collect(Collectors.joining("\n"));
                    }
                }

                break;
            case ENTITY:
                if (attribute.getNumber() != null) {
                    text = displayEntity(entityAttribute, attribute.getNumber());
                }

                break;
            case NUMBER:
                text = attribute.getNumber() != null ?  attribute.getNumber() + "" : "";

                break;
            case DECIMAL:
                text = attribute.getDecimal() != null ?  attribute.getDecimal() + "" : "";

                break;

            case DATE:
                text = attribute.getDate() != null ? dateFormat.format(attribute.getDate()) : "";

                break;

            case ENTITY_LIST:
                if (attribute.getValues() != null && entityAttribute.getReferenceEntityAttributeId() != null){
                    EntityAttribute referenceEntityAttribute = getEntityService().getReferenceEntityAttribute(entityAttribute);

                    if (referenceEntityAttribute != null){
                        List<Long> list = attribute.getValues().stream()
                                .map(Value::getNumber)
                                .collect(Collectors.toList());

                        text = list.stream()
                                .map(id -> {
                                    Domain<?> domain = getDomainService().getDomain(referenceEntityAttribute.getEntityName(), id);

                                    return  Attributes.displayText(referenceEntityAttribute,
                                            domain.getTextValue(referenceEntityAttribute.getEntityAttributeId()));
                                })
                                .collect(Collectors.joining("\n"));
                    }else{
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

        MultiLineLabel label = new MultiLineLabel(componentId, text);

        //label.add(AttributeAppender.append("style", "white-space: nowrap"));


        cellItem.add(label);
    }

    protected String displayEntity(EntityAttribute entityAttribute, Long objectId){
        if (entityAttribute.getReferenceEntityId() != null) {
            Domain<?> refDomain = getDomainService().getDomainRef(entityAttribute.getReferenceEntityId(), objectId);

            if (refDomain != null){
                EntityAttribute referenceEntityAttribute = getEntityService().getReferenceEntityAttribute(entityAttribute);

                String text;

                switch (referenceEntityAttribute.getValueType()){
                    case ENTITY:
                        text = displayEntity(referenceEntityAttribute, refDomain.getNumber(referenceEntityAttribute.getEntityAttributeId()));
                        break;
                    case TEXT_LIST:
                        text = refDomain.getTextValue(referenceEntityAttribute.getEntityAttributeId());
                        break;
                    case TEXT:
                        text = refDomain.getText(referenceEntityAttribute.getEntityAttributeId());
                        break;
                    case NUMBER:
                        text = refDomain.getNumber(referenceEntityAttribute.getEntityAttributeId()) + "";
                        break;
                    default:
                        text = "[" + referenceEntityAttribute.getEntityAttributeId() + "]";
                }

                return Attributes.displayText(referenceEntityAttribute, text);
            }
        }

        return entityAttribute.getEntityAttributeId() + ":" + objectId;
    }
}
