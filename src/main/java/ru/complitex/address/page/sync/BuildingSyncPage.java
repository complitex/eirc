package ru.complitex.address.page.sync;

import ru.complitex.address.entity.Building;
import ru.complitex.sync.page.SyncPage;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 01:14
 */
public class BuildingSyncPage extends SyncPage<Building> {
    public BuildingSyncPage() {
        super(Building.class);
    }
}
