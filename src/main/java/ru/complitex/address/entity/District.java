package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:38 PM
 */
public class District extends NameDomain<District> {
    public final static Long ENTITY_ID = 5L;
    public final static String ENTITY_NAME = "district";

    public final static Long CITY = 1L;
    public final static Long NAME = 2L;
    public final static Long CODE = 3L;

    public District() {
        super(ENTITY_ID, ENTITY_NAME, NAME);
    }

    public Long getCityId(){
        return getNumber(CITY);
    }

    public void setCityId(Long cityId){
        setNumber(CITY, cityId);
    }

    public String getCode(){
        return getText(CODE);
    }

    public void setCode(String code){
        setText(CODE, code);
    }

}
