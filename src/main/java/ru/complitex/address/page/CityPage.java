package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import ru.complitex.address.component.RegionGroup;
import ru.complitex.address.entity.City;
import ru.complitex.address.entity.Country;
import ru.complitex.address.entity.Region;
import ru.complitex.address.mapper.CityMapper;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.domain.service.AttributeService;
import ru.complitex.eirc.security.EircRoles;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 5:22 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class CityPage extends DomainPage<City> {
    @Inject
    private AttributeService attributeService;

    @Inject
    private CityMapper cityMapper;

    public CityPage() {
        super(City.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{City.REGION, City.CITY_TYPE, City.NAME};
    }

    @Override
    protected List<City> getDomains(Filter<City> filter) {
        return cityMapper.getCities(filter);
    }

    @Override
    protected Long getDomainsCount(Filter<City> filter) {
        return cityMapper.getCitiesCount(filter);
    }

    @Override
    protected void addColumn(EntityAttribute entityAttribute, List<IColumn<City, Sort>> columns) {
        if (entityAttribute.getEntityAttributeId() == City.REGION){
            columns.add(new MapColumn<>("country") {
                @Override
                public String text(IModel<City> model) {
                    return attributeService.getTextValue(Region.ENTITY, model.getObject().getRegionId(), Region.COUNTRY, Country.ENTITY, Country.NAME);
                }
            });
        }

        super.addColumn(entityAttribute, columns);
    }

    @Override
    protected Component newGroup(String groupId, IModel<City> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == City.REGION){
            return new RegionGroup(groupId, NumberModel.of(domainModel, City.REGION)).setRegionRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
