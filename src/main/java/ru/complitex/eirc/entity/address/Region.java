package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 01.04.2020 12:04 PM
 */
public class Region extends Domain<Region> {
    public static final Long NAME = 1L;

    public Region() {
        super(2L, "region");
    }
}
