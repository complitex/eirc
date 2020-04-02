package ru.complitex.eirc.page.address;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import ru.complitex.domain.entity.Attribute;
import ru.complitex.domain.page.DomainListModalPage;
import ru.complitex.eirc.entity.address.Country;
import ru.complitex.eirc.entity.address.Region;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:50 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class RegionListPage extends DomainListModalPage<Region> {
    public RegionListPage() {
        super(Region.class);
    }

    @Override
    protected Attribute getParentAttribute() {
        return new Attribute(new Country(), Country.NAME);
    }
}
