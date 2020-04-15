package ru.complitex.eirc.page.address;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.eirc.entity.address.StreetType;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:06 PM
 */
public class StreetTypeListPage extends DomainListModalPage<StreetType> {
    public StreetTypeListPage() {
        super(StreetType.class);
    }
}
