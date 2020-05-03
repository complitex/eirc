package ru.complitex.address.entity;

/**
 * @author Anatoly A. Ivanov
 * 01.04.2020 12:04 PM
 */
public class Region extends Address<Region> {
    public final static Long ENTITY_ID = 2L;
    public final static String ENTITY_NAME = "region";

    public static final Long COUNTRY = 1L;
    public static final Long NAME = 2L;

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
