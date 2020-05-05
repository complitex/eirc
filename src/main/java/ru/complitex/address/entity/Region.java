package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 01.04.2020 12:04 PM
 */
public class Region extends NameDomain<Region> {
    public final static int ENTITY_ID = 2;
    public final static String ENTITY_NAME = "region";

    public static final int COUNTRY = 1;
    public static final int NAME = 2;

    public Region() {
        super(ENTITY_ID, ENTITY_NAME, NAME);
    }

    public Long getCountryId(){
        return getNumber(COUNTRY);
    }

    public void setCountryId(Long countryId){
        setNumber(COUNTRY, countryId);
    }
}
