package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.group.BuildingGroup;
import ru.complitex.address.component.group.DistrictStreetGroup;
import ru.complitex.address.entity.Building;
import ru.complitex.address.mapper.matching.BuildingMatchingMapper;
import ru.complitex.address.service.AddressService;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.mapper.IFilterMapper;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:09
 */
public class BuildingMatchingPage extends MatchingPage<Building> {
    @Inject
    private BuildingMatchingMapper buildingMatchingMapper;

    @Inject
    private AddressService addressService;

    public BuildingMatchingPage() {
        super(Building.class);
    }

    @Override
    protected IFilterMapper<Matching> getFilterMapper() {
        return buildingMatchingMapper;
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new BuildingGroup(componentId, PropertyModel.of(model, "objectId")).setRequired(true);
    }

    @Override
    protected Component newParentGroup(String componentId, IModel<Matching> model) {
        return new DistrictStreetGroup(componentId, PropertyModel.of(model, "additionalParentId"),
                PropertyModel.of(model, "parentId")).setRequired(true);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("building") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getBuildingFullName(model.getObject().getObjectId());
            }
        };
    }

    @Override
    protected IColumn<Matching, Sort> newParentColumn() {
        return new MapColumn<>("street") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getStreetFullName(model.getObject().getAdditionalParentId(), model.getObject().getParentId());
            }
        };
    }
}
