package ru.complitex.eirc.page.address;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.eirc.entity.address.Country;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 11:35 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class CountryListPage extends DomainListModalPage<Country> {
    public CountryListPage() {
        super(Country.class);
    }
}
