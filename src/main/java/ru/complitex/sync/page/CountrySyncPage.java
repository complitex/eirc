package ru.complitex.sync.page;

import ru.complitex.address.entity.Country;

/**
 * @author Anatoly Ivanov
 * 30.04.2020 00:01
 */
public class CountrySyncPage extends SyncPage<Country>{
    public CountrySyncPage() {
        super(Country.class);
    }
}
