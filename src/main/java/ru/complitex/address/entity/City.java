package ru.complitex.address.entity;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:39 PM
 */
public class City extends Address<City> {
    public final static Long ENTITY_ID = 4L;
    public final static String ENTITY_NAME = "city";

    public final static Long REGION = 1L;
    public final static Long CITY_TYPE = 2L;
    public final static Long NAME = 3L;

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
