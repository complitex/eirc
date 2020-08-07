package ru.complitex.address.service.sync;

import ru.complitex.address.entity.*;
import ru.complitex.sync.service.ISyncHandler;
import ru.complitex.sync.service.SyncService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 07.08.2020 22:17
 */
@ApplicationScoped
public class AddressSyncService extends SyncService {
    @Inject
    private CountrySyncService countrySyncService;

    @Inject
    private RegionSyncService regionSyncService;

    @Inject
    private CityTypeSyncService cityTypeSyncService;

    @Inject
    private CitySyncService citySyncService;

    @Inject
    private DistrictSyncService districtSyncService;

    @Inject
    private StreetTypeSyncService streetTypeSyncService;

    @Inject
    private StreetSyncService streetSyncService;

    @Inject
    private BuildingSyncService buildingSyncService;

    protected ISyncHandler<?> getHandler(int entityId){
        switch (entityId){
            case Country.ID:
                return countrySyncService;
            case Region.ID:
                return regionSyncService;
            case CityType.ID:
                return cityTypeSyncService;
            case City.ID:
                return citySyncService;
            case District.ID:
                return districtSyncService;
            case StreetType.ID:
                return streetTypeSyncService;
            case Street.ID:
                return streetSyncService;
            case Building.ID:
                return buildingSyncService;

            default:
                throw new IllegalArgumentException();
        }
    }
}
