package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:39 PM
 */
public class City extends NameDomain<City> {
    public final static int ENTITY_ID = 4;
    public final static String ENTITY_NAME = "city";

    public final static int REGION = 1;
    public final static int CITY_TYPE = 2;
    public final static int NAME = 3;

    public City() {
        super(ENTITY_ID, ENTITY_NAME, NAME);
    }

    public Long getRegionId(){
        return getNumber(REGION);
    }

    public void setRegionId(Long regionId){
        setNumber(REGION, regionId);
    }

    public Long getCityTypeId(){
        return getNumber(CITY_TYPE);
    }

    public void setCityTypeId(Long cityTypeId){
        setNumber(CITY_TYPE, cityTypeId);
    }
}
