package ru.complitex.address.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.address.entity.District;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:05 PM
 */
public class DistrictListPage extends DomainPage<District> {
    public DistrictListPage() {
        super(District.class);
    }
}
