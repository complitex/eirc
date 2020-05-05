package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:38 PM
 */
public class District extends NameDomain<District> {
    public final static int ENTITY_ID = 5;
    public final static String ENTITY_NAME = "district";

    public final static int CITY = 1;
    public final static int NAME = 2;
    public final static int CODE = 3;

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
