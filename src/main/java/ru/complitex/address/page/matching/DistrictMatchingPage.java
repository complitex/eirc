package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.group.CityGroup;
import ru.complitex.address.component.group.DistrictGroup;
import ru.complitex.address.entity.District;
import ru.complitex.address.mapper.matching.DistrictMatchingMapper;
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
public class DistrictMatchingPage extends MatchingPage<District> {
    @Inject
    private DistrictMatchingMapper districtMatchingMapper;

    @Inject
    private AddressService addressService;

    public DistrictMatchingPage() {
        super(District.class);
    }

    @Override
    protected IFilterMapper<Matching> getFilterMapper() {
        return districtMatchingMapper;
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new DistrictGroup(componentId, PropertyModel.of(model, "objectId")).setRequired(true);
    }

    @Override
    protected Component newParentGroup(String componentId, IModel<Matching> model) {
        return new CityGroup(componentId, PropertyModel.of(model, "parentId")).setRequired(true);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("district") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getDistrictFullName(model.getObject().getObjectId());
            }
        };
    }

    @Override
    protected IColumn<Matching, Sort> newParentColumn() {
        return new MapColumn<>("city") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getCityFullName(model.getObject().getParentId());
            }
        };
    }
}
