package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import ru.complitex.address.component.CityGroup;
import ru.complitex.address.entity.Street;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:07 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class StreetPage extends DomainPage<Street> {
    public StreetPage() {
        super(Street.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{Street.CITY, Street.STREET_TYPE, Street.NAME};
    }

    @Override
    protected Component newGroup(String groupId, IModel<Street> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Street.CITY){
            return new CityGroup(groupId, NumberModel.of(domainModel, Street.CITY)).setCityRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
