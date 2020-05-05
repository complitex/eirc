package ru.complitex.domain.entity;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 06.12.2017 17:37
 */
public class EntityValue implements Serializable {
    private Long id;
    private int entityId;
    private int entityAttributeId;
    private int localeId;
    private String text;

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
}
