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
    private int status;
    private Long permissionId;
    private Long userId;

    private int entityId;
    private String entityName;

    private List<Attribute> attributes = new ArrayList<>();

    private Map<String, Object> map = new HashMap<>();

    public Domain() {
    }

    public Domain(String entityName) {
        this.entityName = entityName;
    }


    public Domain(int entityId, String entityName) {
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

    public List<Attribute> getAttributes(int entityAttributeId) {
        return attributes.stream()
                .filter(a -> a.getEntityAttributeId() == entityAttributeId)
                .filter(a -> a.getEndDate() == null)
                .collect(Collectors.toList());
    }

    public Attribute getAttribute(int entityAttributeId) {
        return attributes.stream()
                .filter(a -> a.getEntityAttributeId() == entityAttributeId)
                .filter(a -> a.getEndDate() == null)
                .findAny()
                .orElse(null);
    }

    public Attribute getOrCreateAttribute(int entityAttributeId) {
        return attributes.stream()
                .filter(a -> a.getEntityAttributeId() == entityAttributeId)
                .filter(a -> a.getEndDate() == null)
                .findAny()
                .orElseGet(() -> {
                    Attribute attribute = new Attribute(entityAttributeId);
                    attributes.add(attribute);

                    return attribute;
                });
    }

    public void removeAttribute(int entityAttributeId) {
        attributes.removeIf(attribute -> attribute.getEntityAttributeId() == entityAttributeId);
    }

    public String getText(int entityAttributeId){
        Attribute attribute = getAttribute(entityAttributeId);

        return attribute != null ? attribute.getText() : null;
    }

    public Domain<T> setText(int entityAttributeId, String text){
        getOrCreateAttribute(entityAttributeId).setText(text);

        return this;
    }

    public Long getNumber(int entityAttributeId){
        Attribute attribute = getAttribute(entityAttributeId);

        return attribute != null ? attribute.getNumber() : null;
    }

    public Long getNumber(int entityAttributeId, Long defaultNumber){
        Attribute attribute = getAttribute(entityAttributeId);

        return attribute != null ? attribute.getNumber() != null ? attribute.getNumber() : defaultNumber : defaultNumber;
    }

    @SuppressWarnings("unchecked")
    public T setNumber(int entityAttributeId, Long number){
        getOrCreateAttribute(entityAttributeId).setNumber(number);

        return (T) this;
    }

    public void setBoolean(int entityAttributeId, Boolean _boolean){
        setNumber(entityAttributeId, _boolean != null ? _boolean ? 1L : 0 : null);
    }

    public Boolean getBoolean(int entityAttributeId){
        Long number = getNumber(entityAttributeId);

        return number != null ? number.equals(1L) : null;
    }

    public boolean isBoolean(int entityAttributeId){
        Long number = getNumber(entityAttributeId);

        return number != null && number.equals(1L);
    }

    public BigDecimal getDecimal(int entityAttributeId){
        String text = getOrCreateAttribute(entityAttributeId).getText();

        return text != null && !text.isEmpty()  ? new BigDecimal(text) : null;
    }

    @SuppressWarnings("unchecked")
    public T setDecimal(int entityAttributeId, BigDecimal decimal){
        setText(entityAttributeId, decimal != null ? decimal.toPlainString() : null);

        return (T) this;
    }

    public Date getDate(int entityAttributeId){
        return Optional.ofNullable(getAttribute(entityAttributeId))
                .map(Attribute::getDate)
                .orElse(null);
    }

    public void setDate(int entityAttributeId, Date date){
        getOrCreateAttribute(entityAttributeId).setDate(date);
    }

    public Value getValue(int entityAttributeId, Locale locale){
        Attribute attribute = getAttribute(entityAttributeId);

        return attribute != null ? attribute.getValue(locale) : null;
    }

    public String getTextValue(int entityAttributeId, int localeId){
        return Optional.ofNullable(getAttribute(entityAttributeId))
                .map(a -> a.getValue(localeId))
                .map(Value::getText)
                .orElse(null);
    }

    public String getTextValue(int entityAttributeId, Locale locale){
        return Optional.ofNullable(getAttribute(entityAttributeId))
                .map(a -> a.getValue(locale))
                .map(Value::getText)
                .orElse(null);
    }

    public String getTextValue(int entityAttributeId){
        return getTextValue(entityAttributeId, Locales.getSystemLocale());
    }

    public void setTextValue(int entityAttributeId, String value, int localeId){
        getOrCreateAttribute(entityAttributeId).setTextValue(value, localeId);
    }

    public void setTextValue(int entityAttributeId, String value){
        setTextValue(entityAttributeId, value, Locales.getSystemLocaleId());
    }

    public void addTextValue(int entityAttributeId, String text){
        getOrCreateAttribute(entityAttributeId).addTextValue(text);
    }

    public void addUpperTextValue(int entityAttributeId, String text){
        if (text != null) {
            addTextValue(entityAttributeId, text.toUpperCase());
        }
    }

    public void addNumberValue(int entityAttributeId, Long number){
        getOrCreateAttribute(entityAttributeId).addNumberValue(number);
    }

    public List<Long> getNumberValues(int entityAttributeId){
        return getOrCreateAttribute(entityAttributeId).getNumberValues();
    }

    public String getNumberValuesString(int entityAttributeId){
        return Strings.emptyToNull(getNumberValues(entityAttributeId).stream().map(Object::toString)
                .collect(Collectors.joining(",")));
    }

    public List<String> getTextValues(int entityAttributeId){
        return getOrCreateAttribute(entityAttributeId).getTextValues();
    }

    public boolean hasValueText(int entityAttributeId, String value){
        return getAttribute(entityAttributeId).getValues().stream().anyMatch(v -> v.getText().equals(value));
    }

    public Map<String, String> getStringMap(int entityAttributeId){
        if (getAttribute(entityAttributeId) == null || getAttribute(entityAttributeId).getValues().isEmpty()){
            return null;
        }

        return getAttribute(entityAttributeId).getValues().stream()
                .filter(s -> s.getText() != null)
                .collect(Collectors.toMap(s -> Locales.getLanguage(s.getLocaleId()), Value::getText));
    }

    public void clearValues(int entityAttributeId){
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

    public int getStatus() {
        return status;
    }

    @SuppressWarnings("unchecked")
    public T setStatus(int status) {
        this.status = status;

        return (T) this;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
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
    public T setFilter(int entityAttributeId, String filter){
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
