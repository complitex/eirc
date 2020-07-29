package ru.complitex.address.entity;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 27.04.2020 22:33
 */
public class Room extends Domain<Room> {
    public final static int ID = 10;
    public final static String ENTITY = "room";

    public final static int BUILDING = 1;
    public final static int NAME = 2;

    public Room() {
        super(ID, ENTITY);
    }
}
