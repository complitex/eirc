package ru.complitex.address.page.sync;

import ru.complitex.address.entity.Country;

/**
 * @author Anatoly Ivanov
 * 30.04.2020 00:01
 */
public class CountrySyncPage extends AddressSyncPage<Country> {
    public CountrySyncPage() {
        super(Country.class);
    }
}
