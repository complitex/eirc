package ru.complitex.address.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import ru.complitex.address.entity.CityType;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 11:46 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class CityTypePage extends DomainPage<CityType> {
    public CityTypePage() {
        super(CityType.class, CityType.NAME, CityType.SHORT_NAME);
    }
}
