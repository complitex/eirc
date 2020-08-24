package ru.complitex.address.page.sync;

import ru.complitex.address.service.sync.AddressSyncService;
import ru.complitex.domain.entity.Domain;
import ru.complitex.sync.page.SyncPage;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 07.08.2020 22:15
 */
public class AddressSyncPage<T extends Domain<T>> extends SyncPage<T> {
    @Inject
    private AddressSyncService addressSyncService;

    public AddressSyncPage(Class<T> domainClass) {
        super(domainClass);
    }

    @Override
    protected void load() {
        addressSyncService.load(getDomainClass(), getSyncListener());
    }

    @Override
    protected void sync() {
        addressSyncService.sync(getDomainClass(), getSyncListener());
    }
}
