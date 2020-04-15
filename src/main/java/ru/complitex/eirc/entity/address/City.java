package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:39 PM
 */
public class City extends Domain<City> {
    public final static Long ENTITY_ID = 4L;
    public final static String ENTITY_NAME = "city";

    public final static Long REGION = 1L;
    public final static Long CITY_TYPE = 2L;
    public final static Long NAME = 3L;

    public City() {
        super(ENTITY_ID, ENTITY_NAME);
    }
}
