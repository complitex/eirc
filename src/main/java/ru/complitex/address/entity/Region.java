package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 01.04.2020 12:04 PM
 */
public class Region extends NameDomain<Region> {
    public final static int ID = 2;
    public final static String ENTITY = "region";

    public static final int COUNTRY = 1;
    public static final int NAME = 2;

    public Region() {
        super(ID, ENTITY, NAME);
    }

    public Long getCountryId(){
        return getNumber(COUNTRY);
    }

    public void setCountryId(Long countryId){
        setNumber(COUNTRY, countryId);
    }
}
