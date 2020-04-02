package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:36 PM
 */
public class CityType extends Domain<CityType> {
    public final static Long NAME = 1L;
    public final static Long SHORT_NAME = 2L;

    public CityType() {
        super(3L, "city_type");
    }
}
