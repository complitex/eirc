package ru.complitex.domain.entity;

import com.google.common.base.MoreObjects;
import ru.complitex.domain.util.Locales;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 06.12.2017 17:12
 */
public class EntityAttribute implements Serializable {
    private Long id;
    private int entityId;
    private int entityAttributeId;
    private Date startDate;
    private Date endDate;
    private int valueTypeId;
    private Integer referenceEntityId;
    private Integer referenceEntityAttributeId;

    private List<EntityValue> entityValues;

    private String entityName;

    private StringType stringType = StringType.CAPITALIZE;

    private boolean required;

    private EntityAttribute referenceEntityAttribute;

    public EntityAttribute() {
    }

    public EntityAttribute(String entityName, int entityAttributeId) {
        this.entityName = entityName;
        this.entityAttributeId = entityAttributeId;
    }

    public void copy(EntityAttribute entityAttribute){
        this.id = entityAttribute.getId();
        this.entityId = entityAttribute.getEntityId();
        this.entityAttributeId = entityAttribute.getEntityAttributeId();
        this.startDate = entityAttribute.getStartDate();
        this.endDate = entityAttribute.getEndDate();
        this.valueTypeId = entityAttribute.getValueTypeId();
        this.referenceEntityId = entityAttribute.getReferenceEntityId();
        this.referenceEntityAttributeId = entityAttribute.getReferenceEntityAttributeId();
        this.entityValues = entityAttribute.getEntityValues();
        this.entityName = entityAttribute.getEntityName();
    }

    public EntityValue getValue(){
        return entityValues.stream()
                .filter(v -> v.getLocaleId() == Locales.getSystemLocaleId())
                .findAny()
                .orElse(null);
    }

    public String getValueText(){
        EntityValue entityValue = getValue();

        if (entityValue != null){
            return entityValue.getText();
        }

        return "[" + entityAttributeId + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityAttributeId() {
        return entityAttributeId;
    }

    public void setEntityAttributeId(int entityAttributeId) {
        this.entityAttributeId = entityAttributeId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getValueTypeId() {
        return valueTypeId;
    }

    public void setValueTypeId(int valueTypeId) {
        this.valueTypeId = valueTypeId;
    }

    public Integer getReferenceEntityId() {
        return referenceEntityId;
    }

    public void setReferenceEntityId(Integer referenceEntityId) {
        this.referenceEntityId = referenceEntityId;
    }

    public Integer getReferenceEntityAttributeId() {
        return referenceEntityAttributeId;
    }

    public void setReferenceEntityAttributeId(Integer referenceEntityAttributeId) {
        this.referenceEntityAttributeId = referenceEntityAttributeId;
    }

    public List<EntityValue> getEntityValues() {
        return entityValues;
    }

    public void setEntityValues(List<EntityValue> entityValues) {
        this.entityValues = entityValues;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public StringType getStringType() {
        return stringType;
    }

    public EntityAttribute setStringType(StringType stringType) {
        this.stringType = stringType;

        return this;
    }

    public boolean isRequired() {
        return required;
    }

    public EntityAttribute setRequired(boolean required) {
        this.required = required;

        return this;
    }

    public EntityAttribute getReferenceEntityAttribute() {
        return referenceEntityAttribute;
    }

    public void setReferenceEntityAttribute(EntityAttribute referenceEntityAttribute) {
        this.referenceEntityAttribute = referenceEntityAttribute;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("entityId", entityId)
                .add("entityAttributeId", entityAttributeId)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("valueTypeId", valueTypeId)
                .add("referenceEntityId", referenceEntityId)
                .add("referenceEntityAttributeId", referenceEntityAttributeId)
                .add("values", entityValues)
                .add("entityName", entityName)
                .add("stringType", stringType)
                .add("required", required)
                .toString();
    }
}
