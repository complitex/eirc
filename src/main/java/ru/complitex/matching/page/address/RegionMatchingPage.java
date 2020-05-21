package ru.complitex.matching.page.address;

import ru.complitex.address.entity.Region;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:06
 */
public class RegionMatchingPage extends MatchingPage<Region> {
    public RegionMatchingPage() {
        super(Region.class);
    }
}
