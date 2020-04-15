package ru.complitex.eirc.page.address;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.eirc.entity.address.City;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 5:22 PM
 */
public class CityListPage extends DomainListModalPage<City> {
    public CityListPage() {
        super(City.class);
    }
}
