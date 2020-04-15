package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:47 PM
 */
public class Apartment extends Domain<Apartment> {
    public final static Long ENTITY_ID = 9L;
    public final static String ENTITY_NAME = "apartment";

    public final static Long BUILDING = 1L;
    public final static Long NAME = 2L;

    public Apartment() {
        super(ENTITY_ID, ENTITY_NAME);
    }
}
