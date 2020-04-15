package ru.complitex.domain.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import ru.complitex.domain.util.Locales;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Anatoly A. Ivanov
 * 30.11.2017 15:29
 */
public class Domain<T extends Domain<T>> implements Serializable{
    private Long id;
    private Long objectId;
    private Date startDate;
    private Date endDate;
    private Status status;
    private Long permissionId;
    private Long userId;

    private Long entityId;
    private String entityName;

    private List<Attribute> attributes = new ArrayList<>();

    private Map<String, Object> map = new HashMap<>();

    public Domain() {
    }

    public Domain(String entityName) {
        this.entityName = entityName;
    }


    public Domain(Long entityId, String entityName) {
        this.entityId = entityId;
        this.entityName = entityName;
    }

    public void copy(Domain<?> domain){
        id = domain.id;
        objectId = domain.objectId;
        startDate = domain.startDate;
        endDate = domain.endDate;
        status = domain.status;
        permissionId = domain.permissionId;
        entityName = domain.entityName;
        userId = domain.userId;

        domain.attributes.forEach(a -> attributes.add(new Attribute(a)));

        map.putAll(domain.map);
    }

    public List<Attribute> getAttributes(Long entityAttributeId) {
        return attributes.stream()
                .filter(a -> a.getEntityAttributeId().equals(entityAttributeId))
                .filter(a -> a.getEndDate() == null)
                .collect(Collectors.toList());
    }

    public Attribute getAttribute(Long entityAttributeId) {
        return attributes.stream()
                .filter(a -> a.getEntityAttributeId().equals(entityAttributeId))
                .filter(a -> a.getEndDate() == null)
                .findAny()
                .orElse(null);
    }

    public Attribute getOrCreateAttribute(Long entityAttributeId) {
        return attributes.stream()
                .filter(a -> a.getEntityAttributeId().equals(entityAttributeId))
                .filter(a -> a.getEndDate() == null)
                .findAny()
                .orElseGet(() -> {
                    Attribute attribute = new Attribute(entityAttributeId);
                    attributes.add(attribute);

                    return attribute;
                });
    }

    public void removeAttribute(Long entityAttributeId) {
        attributes.removeIf(attribute -> attribute.getEntityAttributeId().equals(entityAttributeId));
    }

    public String getText(Long entityAttributeId){
        Attribute attribute = getAttribute(entityAttributeId);

        return attribute != null ? attribute.getText() : null;
    }

    public Domain<T> setText(Long entityAttributeId, String text){
        getOrCreateAttribute(entityAttributeId).setText(text);

        return this;
    }

    public Long getNumber(Long entityAttributeId){
        Attribute attribute = getAttribute(entityAttributeId);

        return attribute != null ? attribute.getNumber() : null;
    }

    public Long getNumber(Long entityAttributeId, Long defaultNumber){
        Attribute attribute = getAttribute(entityAttributeId);

        return attribute != null ? attribute.getNumber() != null ? attribute.getNumber() : defaultNumber : defaultNumber;
    }

    @SuppressWarnings("unchecked")
    public T setNumber(Long entityAttributeId, Long number){
        getOrCreateAttribute(entityAttributeId).setNumber(number);

        return (T) this;
    }

    public void setBoolean(Long entityAttributeId, Boolean _boolean){
        setNumber(entityAttributeId, _boolean != null ? _boolean ? 1L : 0 : null);
    }

    public Boolean getBoolean(Long entityAttributeId){
        Long number = getNumber(entityAttributeId);

        return number != null ? number.equals(1L) : null;
    }

    public boolean isBoolean(Long entityAttributeId){
        Long number = getNumber(entityAttributeId);

        return number != null && number.equals(1L);
    }

    public BigDecimal getDecimal(Long entityAttributeId){
        String text = getOrCreateAttribute(entityAttributeId).getText();

        return text != null && !text.isEmpty()  ? new BigDecimal(text) : null;
    }

    @SuppressWarnings("unchecked")
    public T setDecimal(Long entityAttributeId, BigDecimal decimal){
        setText(entityAttributeId, decimal != null ? decimal.toPlainString() : null);

        return (T) this;
    }

    public Date getDate(Long entityAttributeId){
        return Optional.ofNullable(getAttribute(entityAttributeId))
                .map(Attribute::getDate)
                .orElse(null);
    }

    public void setDate(Long entityAttributeId, Date date){
        getOrCreateAttribute(entityAttributeId).setDate(date);
    }

    public Value getValue(Long entityAttributeId, Locale locale){
        Attribute attribute = getAttribute(entityAttributeId);

        return attribute != null ? attribute.getValue(locale) : null;
    }

    public String getTextValue(Long entityAttributeId, Locale locale){
        return Optional.ofNullable(getAttribute(entityAttributeId))
                .map(a -> a.getValue(locale))
                .map(Value::getText)
                .orElse(null);
    }

    public String getTextValue(Long entityAttributeId){
        return getTextValue(entityAttributeId, Locales.getSystemLocale());
    }

    public void setTextValue(Long entityAttributeId, String value, Locale locale){
        getOrCreateAttribute(entityAttributeId).setTextValue(value, Locales.getLocaleId(locale));
    }

    public void setTextValue(Long entityAttributeId, String value){
        setTextValue(entityAttributeId, value, Locales.getSystemLocale());
    }

    public void addTextValue(Long entityAttributeId, String text){
        getOrCreateAttribute(entityAttributeId).addTextValue(text);
    }

    public void addUpperTextValue(Long entityAttributeId, String text){
        if (text != null) {
            addTextValue(entityAttributeId, text.toUpperCase());
        }
    }

    public void addNumberValue(Long entityAttributeId, Long number){
        getOrCreateAttribute(entityAttributeId).addNumberValue(number);
    }

    public List<Long> getNumberValues(Long entityAttributeId){
        return getOrCreateAttribute(entityAttributeId).getNumberValues();
    }

    public String getNumberValuesString(Long entityAttributeId){
        return Strings.emptyToNull(getNumberValues(entityAttributeId).stream().map(Object::toString)
                .collect(Collectors.joining(",")));
    }

    public List<String> getTextValues(Long entityAttributeId){
        return getOrCreateAttribute(entityAttributeId).getTextValues();
    }

    public boolean hasValueText(Long entityAttributeId, String value){
        return getAttribute(entityAttributeId).getValues().stream().anyMatch(v -> v.getText().equals(value));
    }

    public Map<String, String> getStringMap(Long entityAttributeId){
        if (getAttribute(entityAttributeId) == null || getAttribute(entityAttributeId).getValues().isEmpty()){
            return null;
        }

        return getAttribute(entityAttributeId).getValues().stream()
                .filter(s -> s.getText() != null)
                .collect(Collectors.toMap(s -> Locales.getLanguage(s.getLocaleId()), Value::getText));
    }

    public void clearValues(Long entityAttributeId){
        getOrCreateAttribute(entityAttributeId).clearValues();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getObjectId() {
        return objectId;
    }

    @SuppressWarnings("unchecked")
    public T setObjectId(Long objectId) {
        this.objectId = objectId;

        return (T) this;
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

    public Status getStatus() {
        return status;
    }

    @SuppressWarnings("unchecked")
    public T setStatus(Status status) {
        this.status = status;

        return (T) this;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @SuppressWarnings("unchecked")
    public T put(String key, Object object){
        map.put(key, object);

        return (T) this;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @SuppressWarnings("unchecked")
    public T setFilter(Long entityAttributeId, String filter){
        getOrCreateAttribute(entityAttributeId).setFilter(filter);

        return (T) this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("objectId", objectId)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("status", status)
                .add("permissionId", permissionId)
                .add("userId", userId)
                .add("entityName", entityName)
                .add("attributes", attributes)
                .add("map", map)
                .toString();
    }
}
