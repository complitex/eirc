package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import ru.complitex.address.component.DistrictStreetGroup;
import ru.complitex.address.entity.Building;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:09 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class BuildingPage extends DomainPage<Building> {
    public BuildingPage() {
        super(Building.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{Building.DISTRICT, Building.STREET, Building.NAME};
    }

    @Override
    protected Component newGroup(String groupId, IModel<Building> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Building.DISTRICT){
            return new EmptyPanel(groupId);
        }

        if (entityAttribute.getEntityAttributeId() == Building.STREET) {
            return new DistrictStreetGroup(groupId, NumberModel.of(domainModel, Building.DISTRICT),
                    NumberModel.of(domainModel, Building.STREET))
                    .setDistrictRequired(true)
                    .setStreetRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
