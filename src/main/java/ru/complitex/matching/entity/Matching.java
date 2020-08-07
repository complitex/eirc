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
    private String name;
    private String additionalName;
    private Long number;
    private String code;
    private Date startDate;
    private Date endDate;
    private Long companyId;
    private Long userCompanyId;
    private Long localeId;

    private String entityName;

    public Matching() {
    }

    public Matching(String entityName) {
        this.entityName = entityName;
    }

    public Matching(String entityName, Long objectId, String name, Long number, Date startDate, Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.name = name;
        this.number = number;
        this.startDate = startDate;

        this.companyId = companyId;
    }

    public Matching(String entityName, Long objectId, String name, String additionalName, Long number, Date startDate, Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.name = name;
        this.additionalName = additionalName;
        this.number = number;
        this.startDate = startDate;
        this.companyId = companyId;
    }

    public Matching(String entityName, Long objectId, Long parentId, String name, Long number, Date startDate, Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.parentId = parentId;
        this.name = name;
        this.number = number;
        this.startDate = startDate;
        this.companyId = companyId;
    }

    public Matching(String entityName, Long objectId, Long parentId, String name, String additionalName, Long number, Date startDate, Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.parentId = parentId;
        this.name = name;
        this.additionalName = additionalName;
        this.number = number;
        this.startDate = startDate;
        this.companyId = companyId;
    }

    public Matching(String entityName, Long objectId, Long parentId, Long additionalParentId, String name, Long number, Date startDate, Long companyId) {
        this.entityName = entityName;

        this.objectId = objectId;
        this.parentId = parentId;
        this.additionalParentId = additionalParentId;
        this.name = name;
        this.number = number;
        this.startDate = startDate;
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

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getLocaleId() {
        return localeId;
    }

    public void setLocaleId(Long localeId) {
        this.localeId = localeId;
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
                .add("additionalExternalId", code)
                .add("name", name)
                .add("additionalName", additionalName)
                .add("additionalCode", additionalParentId)
                .add("code", number)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("companyId", companyId)
                .add("userCompanyId", localeId)
                .add("localeId", localeId)
                .add("entityName", entityName)
                .toString();
    }
}
