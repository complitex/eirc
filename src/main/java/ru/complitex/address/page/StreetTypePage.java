package ru.complitex.address.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import ru.complitex.address.entity.StreetType;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:06 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class StreetTypePage extends DomainPage<StreetType> {
    public StreetTypePage() {
        super(StreetType.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{ StreetType.NAME, StreetType.SHORT_NAME};
    }
}
