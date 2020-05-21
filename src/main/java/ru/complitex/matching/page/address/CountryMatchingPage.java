package ru.complitex.matching.page.address;

import ru.complitex.address.entity.Country;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:02
 */
public class CountryMatchingPage extends MatchingPage<Country> {
    public CountryMatchingPage() {
        super(Country.class);
    }
}
