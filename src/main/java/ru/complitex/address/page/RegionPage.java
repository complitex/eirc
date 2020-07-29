package ru.complitex.address.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import ru.complitex.address.entity.Region;
import ru.complitex.domain.mapper.AttributeMapper;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

import javax.inject.Inject;

/**
 * @author Anatoly A. Ivanov
 * 02.04.2020 9:50 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class RegionPage extends DomainPage<Region> {
    @Inject
    private AttributeMapper attributeMapper;

    public RegionPage() {
        super(Region.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{Region.COUNTRY, Region.NAME};
    }
}
