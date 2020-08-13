package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.group.ApartmentGroup;
import ru.complitex.address.component.group.BuildingGroup;
import ru.complitex.address.entity.Apartment;
import ru.complitex.address.service.AddressService;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Sort;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:13
 */
public class ApartmentMatchingPage extends MatchingPage<Apartment> {
    @Inject
    private AddressService addressService;

    public ApartmentMatchingPage() {
        super(Apartment.class);
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new ApartmentGroup(componentId, PropertyModel.of(model, "objectId")).setRequired(true);
    }

    @Override
    protected Component newParentGroup(String componentId, IModel<Matching> model) {
        return new BuildingGroup(componentId, PropertyModel.of(model, "parentId")).setRequired(true);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("apartment") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getApartmentFullName(model.getObject().getObjectId());
            }
        };
    }

    @Override
    protected IColumn<Matching, Sort> newParentColumn() {
        return new MapColumn<>("building") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getBuildingFullName(model.getObject().getParentId());
            }
        };
    }
}
