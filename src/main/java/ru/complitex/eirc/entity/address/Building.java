package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:44 PM
 */
public class Building extends Domain<Building> {
    public final static Long NAME = 1L;
    public final static Long CORP = 2L;
    public final static Long STRUCTURE = 3L;
    public final static Long DISTRICT = 4L;
    public final static Long CODE = 5L;

    public Building() {
        super(8L, "building");
    }
}
