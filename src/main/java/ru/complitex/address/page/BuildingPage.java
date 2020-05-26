package ru.complitex.address.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.address.entity.Building;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:09 PM
 */
public class BuildingPage extends DomainPage<Building> {
    public BuildingPage() {
        super(Building.class, Building.DISTRICT, Building.STREET, Building.NAME);
    }
}
