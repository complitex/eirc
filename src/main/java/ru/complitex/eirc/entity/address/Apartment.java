package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:47 PM
 */
public class Apartment extends Domain<Apartment> {
    public final static Long NAME = 1L;

    public Apartment() {
        super(9L, "apartment");
    }
}
