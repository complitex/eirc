package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;
import ru.complitex.domain.util.Locales;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:44 PM
 */
public class Building extends NameDomain<Building> {
    public final static int ENTITY_ID = 8;
    public final static String ENTITY_NAME = "building";

    public final static int DISTRICT = 1;
    public final static int STREET = 2;
    public final static int NAME = 3;
    public final static int CORPS = 4;
    public final static int STRUCTURE = 5;
    public final static int CODE = 6;

    public Building() {
        super(ENTITY_ID, ENTITY_NAME, NAME);
    }

    public Long getDistrictId(){
        return getNumber(DISTRICT);
    }

    public void setDistrictId(Long districtId){
        setNumber(DISTRICT, districtId);
    }

    public Long getStreetId(){
        return getNumber(STREET);
    }

    public void setStreetId(Long streetId){
        setNumber(STREET, streetId);
    }

    public String getCorps(){
        return getTextValue(CORPS);
    }

    public void setCorps(String shortName){
        setTextValue(CORPS, shortName);
    }

    public String getCorps(Integer localeId){
        return getTextValue(CORPS, localeId);
    }

    public void setCorps(String shortName, Integer localeId){
        setTextValue(CORPS, shortName, localeId);
    }

    public String getAltCorps(){
        return getCorps(Locales.getAltLocaleId());
    }

    public void setAltCorps(String altCorps){
        setCorps(altCorps, Locales.getAltLocaleId());
    }
}
