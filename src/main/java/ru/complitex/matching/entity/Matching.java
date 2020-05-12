package ru.complitex.matching.entity;

import com.google.common.base.MoreObjects;

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
    private Long additionalParentId;
    private Long externalId;
    private String additionalExternalId;
    private String name;
    private String additionalName;
    private Date startDate;
    private Date endDate;
    private Long companyId;
    private Long userCompanyId;

    private String entityName;

    public Matching() {
    }

    public Matching(String entityName, Long objectId, Long externalId, String name, Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.externalId = externalId;
        this.name = name;
        this.companyId = companyId;
    }

    public Matching(String entityName, Long objectId, Long externalId, String name, String additionalName,
                    Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.externalId = externalId;
        this.name = name;
        this.additionalName = additionalName;
        this.companyId = companyId;
    }

    public Matching(String entityName, Long objectId, Long parentId, Long externalId, String name, Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.parentId = parentId;
        this.externalId = externalId;
        this.name = name;
        this.companyId = companyId;
    }

    public Matching(String entityName, Long objectId, Long parentId, Long externalId, String name, String additionalName,
                    Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.parentId = parentId;
        this.externalId = externalId;
        this.name = name;
        this.additionalName = additionalName;
        this.companyId = companyId;
    }

    public Matching(String entityName, Long objectId, Long parentId, Long additionalParentId, Long externalId,
                    String name, Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.parentId = parentId;
        this.additionalParentId = additionalParentId;
        this.externalId = externalId;
        this.name = name;
        this.companyId = companyId;
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

    public Long getAdditionalParentId() {
        return additionalParentId;
    }

    public void setAdditionalParentId(Long additionalParentId) {
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(Long userCompanyId) {
        this.userCompanyId = userCompanyId;
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
                .add("objectId", objectId)
                .add("parentId", parentId)
                .add("additionalParentId", additionalParentId)
                .add("externalId", externalId)
                .add("additionalExternalId", additionalExternalId)
                .add("name", name)
                .add("additionalName", additionalName)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("companyId", companyId)
                .add("userCompanyId", userCompanyId)
                .add("entityName", entityName)
                .toString();
    }
}
