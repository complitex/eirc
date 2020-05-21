package ru.complitex.matching.page.address;

import ru.complitex.address.entity.District;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:07
 */
public class DistrictMatchingPage extends MatchingPage<District> {
    public DistrictMatchingPage() {
        super(District.class);
    }
}
