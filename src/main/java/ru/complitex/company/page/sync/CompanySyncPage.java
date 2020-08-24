package ru.complitex.company.page.sync;

import ru.complitex.company.entity.Company;
import ru.complitex.company.service.sync.CompanySyncService;
import ru.complitex.sync.page.SyncPage;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 13.08.2020 23:03
 */
public class CompanySyncPage extends SyncPage<Company> {
    @Inject
    private CompanySyncService companySyncService;

    public CompanySyncPage() {
        super(Company.class);
    }

    @Override
    protected void load() {
        companySyncService.load(getSyncListener());
    }

    @Override
    protected void sync() {
        companySyncService.sync(getSyncListener());
    }
}
