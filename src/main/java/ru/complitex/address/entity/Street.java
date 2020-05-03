package ru.complitex.address.entity;

import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:42 PM
 */
public class Street extends Domain<Street> {
    public final static Long ENTITY_ID = 7L;
    public final static String ENTITY_NAME = "street";

    public final static Long CITY = 1L;
    public final static Long STREET_TYPE = 2L;
    public final static Long NAME = 3L;
    public final static Long CODE = 4L;

    public Street() {
        super(ENTITY_ID, ENTITY_NAME);
    }
}
