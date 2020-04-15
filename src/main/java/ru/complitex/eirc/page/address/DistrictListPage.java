package ru.complitex.eirc.page.address;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.eirc.entity.address.District;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:05 PM
 */
public class DistrictListPage extends DomainListModalPage<District> {
    public DistrictListPage() {
        super(District.class);
    }
}
