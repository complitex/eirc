package ru.complitex.matching.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Anatoly Ivanov
 * 09.04.2020 8:14 PM
 */
public class Matching implements Serializable {
    private Long id;
    private Long objectId;
    private Long parentId;
    private String additionalParentId;
    private Long externalId;
    private String additionalExternalId;
    private String name;
    private String additionalName;
    private Date startDate;
    private Date endDate;
    private Long organizationId;
    private Long userOrganizationId;

    private String entityName;

    public Matching() {
    }

    public Matching(String entityName, Long objectId, Long externalId, String name, Long organizationId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.externalId = externalId;
        this.name = name;
        this.organizationId = organizationId;
    }

    public Matching(String entityName, Long objectId, Long parentId, Long externalId, String name, Long organizationId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.parentId = parentId;
        this.externalId = externalId;
        this.name = name;
        this.organizationId = organizationId;
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

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserOrganizationId() {
        return userOrganizationId;
    }

    public void setUserOrganizationId(Long userOrganizationId) {
        this.userOrganizationId = userOrganizationId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
