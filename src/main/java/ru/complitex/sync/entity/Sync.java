package ru.complitex.sync.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

/**
 * @author Anatoly Ivanov
 * 20.04.2020 5:57 PM
 */
public class Sync implements Serializable {
    private Long id;
    private Long parentId;
    private String additionalParentId;
    private Long externalId;
    private String additionalExternalId;
    private String name;
    private String additionalName;
    private String altName;
    private String altAdditionalName;
    private String servicingOrganization;
    private String balanceHolder;
    private Date date;
    private int status;
    private int entityId;

    public Sync() {
    }

    public Sync(int entityId, int status) {
        this.entityId = entityId;
        this.status = status;
    }

    public Sync(int entityId, int status, Long externalId) {
        this.entityId = entityId;
        this.status = status;
        this.externalId = externalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAdditionalParentId() {
        return additionalParentId;
    }

    public void setAdditionalParentId(String additionalParentId) {
        this.additionalParentId = additionalParentId;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getAdditionalExternalId() {
        return additionalExternalId;
    }

    public void setAdditionalExternalId(String additionalExternalId) {
        this.additionalExternalId = additionalExternalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    public String getAltAdditionalName() {
        return altAdditionalName;
    }

    public void setAltAdditionalName(String altAdditionalName) {
        this.altAdditionalName = altAdditionalName;
    }

    public String getServicingOrganization() {
        return servicingOrganization;
    }

    public void setServicingOrganization(String servicingOrganization) {
        this.servicingOrganization = servicingOrganization;
    }

    public String getBalanceHolder() {
        return balanceHolder;
    }

    public void setBalanceHolder(String balanceHolder) {
        this.balanceHolder = balanceHolder;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Sync.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("parentId=" + parentId)
                .add("additionalParentId='" + additionalParentId + "'")
                .add("externalId=" + externalId)
                .add("additionalExternalId='" + additionalExternalId + "'")
                .add("name='" + name + "'")
                .add("additionalName='" + additionalName + "'")
                .add("altName='" + altName + "'")
                .add("altAdditionalName='" + altAdditionalName + "'")
                .add("servicingOrganization='" + servicingOrganization + "'")
                .add("balanceHolder='" + balanceHolder + "'")
                .add("date=" + date)
                .add("status=" + status)
                .add("entityId=" + entityId)
                .toString();
    }
}
