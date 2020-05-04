package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:42 PM
 */
public class Street extends NameDomain<Street> {
    public final static Long ENTITY_ID = 7L;
    public final static String ENTITY_NAME = "street";

    public final static Long CITY = 1L;
    public final static Long STREET_TYPE = 2L;
    public final static Long NAME = 3L;
    public final static Long CODE = 4L;

    public Street() {
        super(ENTITY_ID, ENTITY_NAME, NAME);
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

    public String getCode(){
        return getText(CODE);
    }

    public void setCode(String code){
        setText(CODE, code);
    }
}
