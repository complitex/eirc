package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 11:36 PM
 */
public class Country extends NameDomain<Country> {
    public final static int ENTITY_ID = 1;
    public final static String ENTITY_NAME = "country";

    public static final int NAME = 1;

    public Country() {
        super(ENTITY_ID, ENTITY_NAME, NAME);
    }
}
