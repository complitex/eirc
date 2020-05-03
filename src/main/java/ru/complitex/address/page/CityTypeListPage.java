package ru.complitex.address.page;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.address.entity.CityType;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 11:46 PM
 */
public class CityTypeListPage extends DomainListModalPage<CityType> {
    public CityTypeListPage() {
        super(CityType.class);
    }
}
