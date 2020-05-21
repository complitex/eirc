package ru.complitex.address.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.address.entity.Region;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:50 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class RegionListPage extends DomainPage<Region> {
    public RegionListPage() {
        super(Region.class);
    }
}
