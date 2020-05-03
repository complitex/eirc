package ru.complitex.address.page;

import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.address.entity.Building;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:09 PM
 */
public class BuildingListPage extends DomainListModalPage<Building> {
    public BuildingListPage() {
        super(Building.class);
    }
}
