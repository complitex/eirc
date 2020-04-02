package ru.complitex.eirc.entity.address;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:41 PM
 */
public class StreetType extends Domain<StreetType> {
    public final static Long NAME = 1L;
    public final static Long SHORT_NAME = 2L;

    public StreetType() {
        super(6L, "street_type");
    }
}
