package ru.complitex.address.page;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.address.entity.City;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 5:22 PM
 */
public class CityListPage extends DomainListModalPage<City> {
    public CityListPage() {
        super(City.class);
    }
}
