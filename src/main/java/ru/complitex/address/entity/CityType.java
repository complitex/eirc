package ru.complitex.address.entity;

import ru.complitex.domain.entity.ShortNameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:36 PM
 */
public class CityType extends ShortNameDomain<CityType> {
    public final static Long ENTITY_ID = 3L;
    public final static String ENTITY_NAME = "city_type";

    public final static Long NAME = 1L;
    public final static Long SHORT_NAME = 2L;

    public CityType() {
        super(ENTITY_ID, ENTITY_NAME, NAME, SHORT_NAME);
    }
}
