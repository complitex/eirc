package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 11:36 PM
 */
public class Country extends Domain<Country> {
    public static final Long NAME = 1L;

    public Country() {
        super(1L, "country");
    }
}
