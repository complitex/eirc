package ru.complitex.company.entity;

import ru.complitex.domain.entity.ShortNameDomain;

/**
 * @author Anatoly Ivanov
 * 27.05.2020 22:05
 */
public class Company extends ShortNameDomain<Company> {
    public final static Long ENTITY_ID = 11L;
    public final static String ENTITY_NAME = "company";

    public final static Long PARENT = 1L;
    public final static Long NAME = 2L;
    public final static Long SHORT_NAME = 3L;
    public final static Long EDRPOU = 4L;
    public final static Long CODE = 5L;

    public Company() {
        super(ENTITY_ID, ENTITY_NAME, NAME, SHORT_NAME);
    }

    public Long getParentId(){
        return getNumber(PARENT);
    }

    public void setParentId(Long parentId){
        setNumber(PARENT, parentId);
    }

    public String getEdrpou(){
        return getText(EDRPOU);
    }

    public void setEdrpou(String edrpou){
        setText(EDRPOU, edrpou);
    }

    public String getCode(){
        return getText(CODE);
    }

    public void setCode(String code){
        setText(CODE, code);
    }
}
