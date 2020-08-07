package ru.complitex.address.page.sync;

import ru.complitex.address.entity.StreetType;
import ru.complitex.sync.page.SyncPage;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 01:11
 */
public class StreetTypeSyncPage extends SyncPage<StreetType> {
    public StreetTypeSyncPage() {
        super(StreetType.class);
    }
}
