package ru.complitex.sync.page;

import ru.complitex.address.entity.City;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 01:09
 */
public class CitySyncPage extends SyncPage<City>{
    public CitySyncPage() {
        super(City.class);
    }
}
