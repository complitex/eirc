package ru.complitex.domain.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 30.11.2017 15:42
 */
public class Value implements Serializable {
    private Long id;
    private Long attributeId;
    private int localeId;
    private String text;
    private Long number;

    private String entityName;

    public Value() {
    }

    public Value(int localeId) {
        this.localeId = localeId;
    }

    public Value(Value value){
        copy(value);
    }

    public void copy(Value value){
       id = value.id;
       attributeId = value.attributeId;
       localeId = value.localeId;
       text = value.text;
       number = value.number;

       entityName = value.entityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public int getLocaleId() {
        return localeId;
    }

    public void setLocaleId(int localeId) {
        this.localeId = localeId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("attributeId", attributeId)
                .add("localeId", localeId)
                .add("text", text)
                .add("number", number)
                .add("entityName", entityName)
                .toString();
    }
}
