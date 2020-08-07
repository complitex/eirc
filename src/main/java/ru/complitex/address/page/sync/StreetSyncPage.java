package ru.complitex.address.page.sync;

import ru.complitex.address.entity.Street;
import ru.complitex.sync.page.SyncPage;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 01:13
 */
public class StreetSyncPage extends SyncPage<Street> {
    public StreetSyncPage() {
        super(Street.class);
    }
}
