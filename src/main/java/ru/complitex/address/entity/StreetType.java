package ru.complitex.address.entity;

import ru.complitex.domain.entity.ShortNameDomain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:41 PM
 */
public class StreetType extends ShortNameDomain<StreetType> {
    public final static Long ENTITY_ID = 6L;
    public final static String ENTITY_NAME = "street_type";

    public final static Long NAME = 1L;
    public final static Long SHORT_NAME = 2L;

    public StreetType() {
        super(ENTITY_ID, ENTITY_NAME, NAME, SHORT_NAME);
    }
}
