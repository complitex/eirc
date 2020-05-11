package ru.complitex.sync.page;

import ru.complitex.address.entity.Building;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 01:14
 */
public class BuildingSyncPage extends SyncPage<Building>{
    public BuildingSyncPage() {
        super(Building.class);
    }
}
