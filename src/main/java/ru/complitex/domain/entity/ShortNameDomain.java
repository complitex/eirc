package ru.complitex.domain.entity;

import ru.complitex.domain.util.Locales;

/**
 * @author Anatoly Ivanov
 * 27.04.2020 21:48
 */
public class ShortNameDomain<T extends NameDomain<T>> extends NameDomain<T> {
    private Long shortNameEntityAttributeId;

    public ShortNameDomain(Long entityId, String entityName, Long nameEntityAttributeId, Long shortNameEntityAttributeId) {
        super(entityId, entityName, nameEntityAttributeId);

        this.shortNameEntityAttributeId = shortNameEntityAttributeId;
    }

    public String getShortName(){
        return getTextValue(shortNameEntityAttributeId);
    }

    public void setShortName(String shortName){
        setTextValue(shortNameEntityAttributeId, shortName);
    }

    public String getShortName(Long localeId){
        return getTextValue(shortNameEntityAttributeId, localeId);
    }

    public void setShortName(String shortName, Long localeId){
        setTextValue(shortNameEntityAttributeId, shortName, localeId);
    }

    public String getAltShortName(){
        return getShortName(Locales.getAltLocaleId());
    }

    public void setAltShortName(String altShortName){
        setShortName(altShortName, Locales.getAltLocaleId());
    }
}
