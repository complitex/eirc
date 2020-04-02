package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:39 PM
 */
public class City extends Domain<City> {
    public final static Long NAME = 1L;
    public final static Long CITY_TYPE = 4L;

    public City() {
        super(4L, "city");
    }
}
