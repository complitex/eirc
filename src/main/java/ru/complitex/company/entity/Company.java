package ru.complitex.company.entity;

import ru.complitex.domain.entity.ShortNameDomain;

/**
 * @author Anatoly Ivanov
 * 27.05.2020 22:05
 */
public class Company extends ShortNameDomain<Company> {
    public final static int ID = 11;
    public final static String ENTITY = "company";

    public final static int PARENT = 1;
    public final static int NAME = 2;
    public final static int SHORT_NAME = 3;
    public final static int EDRPOU = 4;
    public final static int CODE = 5;

    public Company() {
        super(ID, ENTITY, NAME, SHORT_NAME);
    }

    public Long getParentId(){
        return getNumber(PARENT);
    }

    public void setParentId(Long parentId){
        setNumber(PARENT, parentId);
    }

    public String getEDRPOU(){
        return getText(EDRPOU);
    }

    public void setEDRPOU(String edrpou){
        setText(EDRPOU, edrpou);
    }

    public String getCode(){
        return getText(CODE);
    }

    public void setCode(String code){
        setText(CODE, code);
    }
}
