package ru.complitex.address.entity;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:47 PM
 */
public class Apartment extends Domain<Apartment> {
    public final static Integer ENTITY_ID = 9;
    public final static String ENTITY = "apartment";

    public final static Integer BUILDING = 1;
    public final static Integer NAME = 2;

    public Apartment() {
        super(ENTITY_ID, ENTITY);
    }

    public Long getBuildingId(){
        return getNumber(BUILDING);
    }
}
