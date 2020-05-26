package ru.complitex.address.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.address.entity.Street;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:07 PM
 */
public class StreetPage extends DomainPage<Street> {
    public StreetPage() {
        super(Street.class, Street.CITY, Street.STREET_TYPE, Street.NAME);
    }
}