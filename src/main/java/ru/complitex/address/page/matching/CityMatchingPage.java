package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.group.CityGroup;
import ru.complitex.address.component.group.CityTypeGroup;
import ru.complitex.address.component.group.RegionGroup;
import ru.complitex.address.entity.City;
import ru.complitex.address.mapper.matching.CityMatchingMapper;
import ru.complitex.address.service.AddressService;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.mapper.IFilterMapper;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:07
 */
public class CityMatchingPage extends MatchingPage<City> {
    @Inject
    private CityMatchingMapper cityMatchingMapper;

    @Inject
    private AddressService addressService;

    public CityMatchingPage() {
        super(City.class);
    }

    @Override
    protected IFilterMapper<Matching> getFilterMapper() {
        return cityMatchingMapper;
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new CityGroup(componentId, PropertyModel.of(model, "objectId")).setRequired(true);
    }

    @Override
    protected Component newParentGroup(String componentId, IModel<Matching> model) {
        return new RegionGroup(componentId, PropertyModel.of(model, "parentId")).setRequired(true);
    }

    @Override
    protected Component newAdditionalParentGroup(String componentId, IModel<Matching> model) {
        return new CityTypeGroup(componentId, PropertyModel.of(model, "additionalParentId")).setRequired(true);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("city") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getCityFullName(model.getObject().getObjectId());
            }
        };
    }

    @Override
    protected IColumn<Matching, Sort> newParentColumn() {
        return new MapColumn<>("region") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getRegionFullName(model.getObject().getParentId());
            }
        };
    }

    @Override
    protected IColumn<Matching, Sort> newAdditionalParentColumn() {
        return new MapColumn<>("cityType") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getCityTypeName(model.getObject().getAdditionalParentId());
            }
        };
    }

    @Override
    protected boolean isAdditionalParentVisible() {
        return true;
    }
}
