package ru.complitex.eirc.page.address;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.eirc.entity.address.Street;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:07 PM
 */
public class StreetListPage extends DomainListModalPage<Street> {
    public StreetListPage() {
        super(Street.class);
    }
}
