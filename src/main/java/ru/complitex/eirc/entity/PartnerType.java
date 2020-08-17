package ru.complitex.eirc.entity;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 19:35
 */
public class PartnerType extends NameCodeDomain<PartnerType>{
    public final static int ID = 14;
    public final static String ENTITY = "partner_type";

    public final static int NAME = 1;
    public final static int CODE = 2;

    public PartnerType() {
        super(ID, ENTITY, NAME, CODE);
    }
}
