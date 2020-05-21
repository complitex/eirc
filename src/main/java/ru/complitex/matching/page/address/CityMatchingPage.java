package ru.complitex.matching.page.address;

import ru.complitex.address.entity.City;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:07
 */
public class CityMatchingPage extends MatchingPage<City> {
    public CityMatchingPage() {
        super(City.class);
    }
}
