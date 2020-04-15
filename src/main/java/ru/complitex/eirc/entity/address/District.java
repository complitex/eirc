package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:38 PM
 */
public class District extends Domain<District> {
    public final static Long ENTITY_ID = 5L;
    public final static String ENTITY_NAME = "district";

    public final static Long CITY = 1L;
    public final static Long NAME = 2L;
    public final static Long CODE = 3L;

    public District() {
        super(ENTITY_ID, ENTITY_NAME);
    }
}
