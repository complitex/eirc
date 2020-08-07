package ru.complitex.address.page.sync;

import ru.complitex.address.entity.CityType;
import ru.complitex.sync.page.SyncPage;

/**
 * @author Anatoly Ivanov
 * 05.05.2020 01:07
 */
public class CityTypeSyncPage extends SyncPage<CityType> {
    public CityTypeSyncPage() {
        super(CityType.class);
    }
}
