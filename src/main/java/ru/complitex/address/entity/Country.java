package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 11:36 PM
 */
public class Country extends NameDomain<Country> {
    public final static int ID = 1;
    public final static String ENTITY = "country";

    public static final int NAME = 1;

    public Country() {
        super(ID, ENTITY, NAME);
    }
}
