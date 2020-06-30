package ru.complitex.address.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import ru.complitex.address.entity.Country;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 11:35 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class CountryPage extends DomainPage<Country> {
    public CountryPage() {
        super(Country.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{ Country.NAME};
    }
}
