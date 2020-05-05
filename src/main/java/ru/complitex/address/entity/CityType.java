package ru.complitex.address.entity;

import ru.complitex.domain.entity.ShortNameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:36 PM
 */
public class CityType extends ShortNameDomain<CityType> {
    public final static int ENTITY_ID = 3;
    public final static String ENTITY_NAME = "city_type";

    public final static int NAME = 1;
    public final static int SHORT_NAME = 2;

    public CityType() {
        super(ENTITY_ID, ENTITY_NAME, NAME, SHORT_NAME);
    }
}
