package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;
import ru.complitex.domain.util.Locales;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:44 PM
 */
public class Building extends NameDomain<Building> {
    public final static Long ENTITY_ID = 8L;
    public final static String ENTITY_NAME = "building";

    public final static Long DISTRICT = 1L;
    public final static Long STREET = 2L;
    public final static Long NAME = 3L;
    public final static Long CORPS = 4L;
    public final static Long STRUCTURE = 5L;
    public final static Long CODE = 6L;

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

    public String getCorps(Long localeId){
        return getTextValue(CORPS, localeId);
    }

    public void setCorps(String shortName, Long localeId){
        setTextValue(CORPS, shortName, localeId);
    }

    public String getAltCorps(){
        return getCorps(Locales.getAltLocaleId());
    }

    public void setAltCorps(String altCorps){
        setCorps(altCorps, Locales.getAltLocaleId());
    }
}
