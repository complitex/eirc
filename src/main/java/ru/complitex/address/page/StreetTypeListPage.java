package ru.complitex.address.page;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.address.entity.StreetType;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:06 PM
 */
public class StreetTypeListPage extends DomainListModalPage<StreetType> {
    public StreetTypeListPage() {
        super(StreetType.class);
    }
}
