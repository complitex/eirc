package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import ru.complitex.address.component.CityGroup;
import ru.complitex.address.entity.District;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:05 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class DistrictPage extends DomainPage<District> {
    public DistrictPage() {
        super(District.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{District.CITY, District.NAME};
    }

    @Override
    protected Component newGroup(String groupId, IModel<District> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == District.CITY){
            return new CityGroup(groupId, NumberModel.of(domainModel, District.CITY)).setCityRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
