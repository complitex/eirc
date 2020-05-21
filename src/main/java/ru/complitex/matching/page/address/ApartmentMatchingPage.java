package ru.complitex.matching.page.address;

import ru.complitex.address.entity.Apartment;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:13
 */
public class ApartmentMatchingPage extends MatchingPage<Apartment> {
    public ApartmentMatchingPage() {
        super(Apartment.class);
    }
}
