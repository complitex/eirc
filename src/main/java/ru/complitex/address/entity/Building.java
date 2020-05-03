package ru.complitex.address.entity;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:44 PM
 */
public class Building extends Domain<Building> {
    public final static Long ENTITY_ID = 8L;
    public final static String ENTITY_NAME = "building";

    public final static Long DISTRICT = 1L;
    public final static Long STREET = 2L;
    public final static Long NAME = 3L;
    public final static Long CORPS = 4L;
    public final static Long STRUCTURE = 5L;
    public final static Long CODE = 6L;

    public Building() {
        super(ENTITY_ID, ENTITY_NAME);
    }
}
