package ru.complitex.address.page.sync;

import ru.complitex.address.entity.Region;
import ru.complitex.sync.page.SyncPage;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 00:53
 */
public class RegionSyncPage extends SyncPage<Region> {
    public RegionSyncPage() {
        super(Region.class);
    }
}
