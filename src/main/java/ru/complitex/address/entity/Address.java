package ru.complitex.address.entity;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 27.05.2020 00:23
 */
public abstract class Address<T extends Address<T>> extends Domain<T> {
    private Long nameEntityAttributeId;

    public Address(Long entityId, String entityName, Long nameEntityAttributeId) {
        super(entityId, entityName);
        this.nameEntityAttributeId = nameEntityAttributeId;
    }

    public String getName(){
        return getTextValue(nameEntityAttributeId);
    }

    public void setName(String name){
        setTextValue(nameEntityAttributeId, name);
    }

    public String getName(Long localeId){
        return getTextValue(nameEntityAttributeId, localeId);
    }

    public void setName(String name, Long localeId){
        setTextValue(nameEntityAttributeId, name, localeId);
    }
}
