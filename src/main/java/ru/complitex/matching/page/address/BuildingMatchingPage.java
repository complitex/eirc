package ru.complitex.matching.page.address;

import ru.complitex.address.entity.Building;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:09
 */
public class BuildingMatchingPage extends MatchingPage<Building> {
    public BuildingMatchingPage() {
        super(Building.class);
    }
}
