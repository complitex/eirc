package ru.complitex.domain.entity;

import ru.complitex.domain.util.Locales;

/**
 * @author Anatoly Ivanov
 * 27.04.2020 21:48
 */
public class ShortNameDomain<T extends NameDomain<T>> extends NameDomain<T> {
    private int shortNameEntityAttributeId;

    public ShortNameDomain(int entityId, String entityName, int nameEntityAttributeId, int shortNameEntityAttributeId) {
        super(entityId, entityName, nameEntityAttributeId);

        this.shortNameEntityAttributeId = shortNameEntityAttributeId;
    }

    public String getShortName(){
        return getTextValue(shortNameEntityAttributeId);
    }

    public void setShortName(String shortName){
        setTextValue(shortNameEntityAttributeId, shortName);
    }

    public String getShortName(int localeId){
        return getTextValue(shortNameEntityAttributeId, localeId);
    }

    public void setShortName(String shortName, int localeId){
        setTextValue(shortNameEntityAttributeId, shortName, localeId);
    }

    public String getAltShortName(){
        return getShortName(Locales.getAltLocaleId());
    }

    public void setAltShortName(String altShortName){
        setShortName(altShortName, Locales.getAltLocaleId());
    }
}
