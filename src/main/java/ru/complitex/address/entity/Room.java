package ru.complitex.address.entity;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 27.04.2020 22:33
 */
public class Room extends Domain<Room> {
    public final static Long ENTITY_ID = 10L;
    public final static String ENTITY_NAME = "room";

    public final static Long BUILDING = 1L;
    public final static Long NAME = 2L;

    public Room() {
        super(ENTITY_ID, ENTITY_NAME);
    }
}
