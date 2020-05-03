package ru.complitex.address.page;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.address.entity.District;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:05 PM
 */
public class DistrictListPage extends DomainListModalPage<District> {
    public DistrictListPage() {
        super(District.class);
    }
}
