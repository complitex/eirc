package ru.complitex.address.page;

import ru.complitex.address.entity.Apartment;
import ru.complitex.domain.page.DomainListModalPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:11
 */
public class ApartmentListPage extends DomainListModalPage<Apartment> {
    public ApartmentListPage() {
        super(Apartment.class);
    }
}
