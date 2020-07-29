package ru.complitex.address.entity;

import ru.complitex.domain.entity.ShortNameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:41 PM
 */
public class StreetType extends ShortNameDomain<StreetType> {
    public final static int ID = 6;
    public final static String ENTITY = "street_type";

    public final static int NAME = 1;
    public final static int SHORT_NAME = 2;

    public StreetType() {
        super(ID, ENTITY, NAME, SHORT_NAME);
    }
}
