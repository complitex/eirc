package ru.complitex.matching.page.address;

import ru.complitex.address.entity.StreetType;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:08
 */
public class StreetTypeMatchingPage extends MatchingPage<StreetType> {
    public StreetTypeMatchingPage() {
        super(StreetType.class);
    }
}
