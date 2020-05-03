package ru.complitex.address.entity;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:36 PM
 */
public class CityType extends Address<CityType> {
    public final static Long ENTITY_ID = 3L;
    public final static String ENTITY_NAME = "city_type";

    public final static Long NAME = 1L;
    public final static Long SHORT_NAME = 2L;

    public CityType() {
        super(ENTITY_ID, ENTITY_NAME, NAME);
    }

    public String getShortName(){
        return getTextValue(SHORT_NAME);
    }

    public void setShortName(String shortName){
        setTextValue(SHORT_NAME, shortName);
    }

    public String getShortName(Long localeId){
        return getTextValue(SHORT_NAME, localeId);
    }

    public void setShortName(String shortName, Long localeId){
        setTextValue(SHORT_NAME, shortName, localeId);
    }
}
