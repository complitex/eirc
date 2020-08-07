package ru.complitex.address.page.sync;

import ru.complitex.address.entity.Country;
import ru.complitex.sync.page.SyncPage;

/**
 * @author Anatoly Ivanov
 * 30.04.2020 00:01
 */
public class CountrySyncPage extends SyncPage<Country> {
    public CountrySyncPage() {
        super(Country.class);
    }
}
