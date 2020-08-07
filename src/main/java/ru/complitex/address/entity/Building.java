package ru.complitex.address.entity;

import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.util.Locales;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:44 PM
 */
public class Building extends Domain<Building> {
    public final static int ID = 8;
    public final static String ENTITY = "building";

    public final static int DISTRICT = 1;
    public final static int STREET = 2;
    public final static int NUMBER = 3;
    public final static int CORPS = 4;
    public final static int STRUCTURE = 5;

    public Building() {
        super(ID, ENTITY);
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

    public String getNumber(){
        return getTextValue(NUMBER);
    }

    public void setNumber(String name){
        setTextValue(NUMBER, name);
    }

    public String getNumber(Integer localeId){
        return getTextValue(NUMBER, localeId);
    }

    public void setNumber(String name, Integer localeId){
        setTextValue(NUMBER, name, localeId);
    }

    public String getAltNumber(){
        return getNumber(Locales.getAltLocaleId());
    }

    public void setAltNumber(String altNumber){
        setNumber(altNumber, Locales.getAltLocaleId());
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

    public String getStructure(){
        return getTextValue(STRUCTURE);
    }

    public void setStructure(String shortName){
        setTextValue(STRUCTURE, shortName);
    }

    public String getStructure(Integer localeId){
        return getTextValue(STRUCTURE, localeId);
    }

    public void setStructure(String shortName, Integer localeId){
        setTextValue(STRUCTURE, shortName, localeId);
    }

    public String getAltStructure(){
        return getStructure(Locales.getAltLocaleId());
    }

    public void setAltStructure(String altStructure){
        setStructure(altStructure, Locales.getAltLocaleId());
    }
}
