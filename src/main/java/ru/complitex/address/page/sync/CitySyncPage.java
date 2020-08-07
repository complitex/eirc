package ru.complitex.address.page.sync;

import ru.complitex.address.entity.City;
import ru.complitex.sync.page.SyncPage;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 01:09
 */
public class CitySyncPage extends SyncPage<City> {
    public CitySyncPage() {
        super(City.class);
    }
}
