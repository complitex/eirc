package ru.complitex.address.page.sync;

import ru.complitex.address.entity.District;
import ru.complitex.sync.page.SyncPage;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 01:10
 */
public class DistrictSyncPage extends SyncPage<District> {
    public DistrictSyncPage() {
        super(District.class);
    }
}
