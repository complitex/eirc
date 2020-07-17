package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import ru.complitex.address.component.RegionGroup;
import ru.complitex.address.entity.City;
import ru.complitex.common.entity.Sort;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 5:22 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class CityPage extends DomainPage<City> {
    public CityPage() {
        super(City.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{City.REGION, City.CITY_TYPE, City.NAME};
    }

    @Override
    protected void addColumn(List<IColumn<City, Sort>> iColumns, EntityAttribute entityAttribute) {
        //todo add county column

        super.addColumn(iColumns, entityAttribute);
    }

    @Override
    protected Component newGroup(String groupId, IModel<City> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == City.REGION){
            return new RegionGroup(groupId, NumberModel.of(domainModel, City.REGION)).setRegionRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
