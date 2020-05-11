package ru.complitex.sync.page;

import ru.complitex.address.entity.Street;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 01:13
 */
public class StreetSyncPage extends SyncPage<Street> {
    public StreetSyncPage() {
        super(Street.class);
    }
}
