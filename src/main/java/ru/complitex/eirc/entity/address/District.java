package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:38 PM
 */
public class District extends Domain<District> {
    public final static Long NAME = 1L;
    public final static Long CODE = 5L;

    public District() {
        super(5L, "district");
    }
}
