package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import ru.complitex.address.component.BuildingGroup;
import ru.complitex.address.entity.Apartment;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:11
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class ApartmentPage extends DomainPage<Apartment> {
    public ApartmentPage() {
        super(Apartment.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{Apartment.BUILDING, Apartment.NAME};
    }

    @Override
    protected Component newGroup(String groupId, IModel<Apartment> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Apartment.BUILDING){
            return new BuildingGroup(groupId, NumberModel.of(domainModel, Apartment.BUILDING)).setBuildingRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
