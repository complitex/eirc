package ru.complitex.matching.page.address;

import ru.complitex.address.entity.CityType;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:06
 */
public class CityTypeMatchingPage extends MatchingPage<CityType> {
    public CityTypeMatchingPage() {
        super(CityType.class);
    }
}
