package ru.complitex.address.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 11:36 PM
 */
public class Country extends NameDomain<Country> {
    public final static Long ENTITY_ID = 1L;
    public final static String ENTITY_NAME = "country";

    public static final Long NAME = 1L;

    public Country() {
        super(ENTITY_ID, ENTITY_NAME, NAME);
    }
}
