package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:38 PM
 */
public class District extends NameDomain<District> {
    public final static int ID = 5;
    public final static String ENTITY = "district";

    public final static int CITY = 1;
    public final static int NAME = 2;

    public District() {
        super(ID, ENTITY, NAME);
    }

    public Long getCityId(){
        return getNumber(CITY);
    }

    public void setCityId(Long cityId){
        setNumber(CITY, cityId);
    }
}
