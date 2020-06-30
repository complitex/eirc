package ru.complitex.domain.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Anatoly Ivanov
 * 30.06.2020 18:37
 */
public class Id implements Serializable {
    private Long objectId;
    private Date date;
    private String uuid;

    private String entityName;

    public Id(String entityName, String uuid) {
        this.entityName = entityName;
        this.uuid = uuid;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
