package ru.complitex.address.entity;

import ru.complitex.domain.entity.ShortNameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:36 PM
 */
public class CityType extends ShortNameDomain<CityType> {
    public final static int ID = 3;
    public final static String ENTITY = "city_type";

    public final static int NAME = 1;
    public final static int SHORT_NAME = 2;

    public CityType() {
        super(ID, ENTITY, NAME, SHORT_NAME);
    }
}
