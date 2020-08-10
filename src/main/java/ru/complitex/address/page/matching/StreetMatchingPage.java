package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.group.CityGroup;
import ru.complitex.address.component.group.StreetGroup;
import ru.complitex.address.component.group.StreetTypeGroup;
import ru.complitex.address.entity.Street;
import ru.complitex.address.mapper.matching.StreetMatchingMapper;
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
public class StreetMatchingPage extends MatchingPage<Street> {
    @Inject
    private StreetMatchingMapper streetMatchingMapper;

    @Inject
    private AddressService addressService;

    public StreetMatchingPage() {
        super(Street.class);
    }

    @Override
    protected IFilterMapper<Matching> getFilterMapper() {
        return streetMatchingMapper;
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new StreetGroup(componentId, PropertyModel.of(model, "objectId")).setRequired(true);
    }

    @Override
    protected Component newParentGroup(String componentId, IModel<Matching> model) {
        return new CityGroup(componentId, PropertyModel.of(model, "parentId")).setRequired(true);
    }

    @Override
    protected Component newAdditionalParentGroup(String componentId, IModel<Matching> model) {
        return new StreetTypeGroup(componentId, PropertyModel.of(model, "additionalParentId")).setRequired(true);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("street") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getStreetFullName(model.getObject().getObjectId());
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

    @Override
    protected IColumn<Matching, Sort> newAdditionalParentColumn() {
        return new MapColumn<>("streetType") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getStreetTypeName(model.getObject().getAdditionalParentId());
            }
        };
    }

    @Override
    protected boolean isAdditionalParentVisible() {
        return true;
    }

    @Override
    protected boolean isCodeVisible() {
        return true;
    }
}
