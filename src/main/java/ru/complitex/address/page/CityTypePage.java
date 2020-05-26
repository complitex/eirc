package ru.complitex.address.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.address.entity.CityType;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 11:46 PM
 */
public class CityTypePage extends DomainPage<CityType> {
    public CityTypePage() {
        super(CityType.class, CityType.NAME, CityType.SHORT_NAME);
    }
}
