package ru.complitex.address.page;

import ru.complitex.address.entity.Apartment;
import ru.complitex.domain.page.DomainPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:11
 */
public class ApartmentPage extends DomainPage<Apartment> {
    public ApartmentPage() {
        super(Apartment.class, Apartment.BUILDING, Apartment.NAME);
    }
}
