package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:42 PM
 */
public class Street extends NameDomain<Street> {
    public final static int ID = 7;
    public final static String ENTITY = "street";

    public final static int CITY = 1;
    public final static int STREET_TYPE = 2;
    public final static int NAME = 3;

    public Street() {
        super(ID, ENTITY, NAME);
    }

    public Long getCityId(){
        return getNumber(CITY);
    }

    public void setCityId(Long cityId){
        setNumber(CITY, cityId);
    }

    public Long getStreetTypeId(){
        return getNumber(STREET_TYPE);
    }

    public void setStreetTypeId(Long streetTypeId){
        setNumber(STREET_TYPE, streetTypeId);
    }
}
