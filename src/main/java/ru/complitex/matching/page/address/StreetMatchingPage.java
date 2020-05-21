package ru.complitex.matching.page.address;

import ru.complitex.address.entity.Street;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:09
 */
public class StreetMatchingPage extends MatchingPage<Street> {
    public StreetMatchingPage() {
        super(Street.class);
    }
}
