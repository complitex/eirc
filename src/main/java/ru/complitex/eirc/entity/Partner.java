package ru.complitex.eirc.entity;

import ru.complitex.domain.entity.ShortNameDomain;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 19:36
 */
public class Partner extends ShortNameDomain<Partner> {
    public final static int ID = 15;
    public final static String ENTITY = "partner";

    public final static int NAME = 1;
    public final static int SHORT_NAME = 2;
    public final static int EDRPOU = 3;
    public final static int BANK_DETAIL = 4;
    public final static int ADDRESS = 5;

    public Partner() {
        super(ID, ENTITY, NAME, SHORT_NAME);
    }

    public String getEDRPOU(){
        return getText(EDRPOU);
    }

    public void setEDRPOU(String edrpou){
        setText(EDRPOU, edrpou);
    }
}
