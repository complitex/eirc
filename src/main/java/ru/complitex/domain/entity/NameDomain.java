package ru.complitex.domain.entity;

import ru.complitex.domain.util.Locales;

/**
 * @author Anatoly Ivanov
 * 27.05.2020 00:23
 */
public abstract class NameDomain<T extends NameDomain<T>> extends Domain<T> {
    private Long nameEntityAttributeId;

    public NameDomain(Long entityId, String entityName, Long nameEntityAttributeId) {
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

    public String getAltName(){
        return getName(Locales.getAltLocaleId());
    }

    public void setAltName(String altName){
        setName(altName, Locales.getAltLocaleId());
    }
}
