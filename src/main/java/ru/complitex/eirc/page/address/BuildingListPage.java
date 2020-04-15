package ru.complitex.eirc.page.address;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.eirc.entity.address.Building;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:09 PM
 */
public class BuildingListPage extends DomainListModalPage<Building> {
    public BuildingListPage() {
        super(Building.class);
    }
}
