package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.group.RegionGroup;
import ru.complitex.address.entity.Country;
import ru.complitex.address.entity.Region;
import ru.complitex.address.mapper.matching.RegionMatchingMapper;
import ru.complitex.address.service.AddressService;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.mapper.IFilterMapper;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:06
 */
public class RegionMatchingPage extends MatchingPage<Region> {
    @Inject
    private RegionMatchingMapper regionMatchingMapper;

    @Inject
    private AddressService addressService;

    public RegionMatchingPage() {
        super(Region.class);
    }

    @Override
    protected IFilterMapper<Matching> getFilterMapper() {
        return regionMatchingMapper;
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new RegionGroup(componentId, PropertyModel.of(model, "objectId")).setRequired(true);
    }

    @Override
    protected Component newParentGroup(String componentId, IModel<Matching> model) {
        return new DomainGroup(componentId, Country.ENTITY, PropertyModel.of(model, "parentId"), Country.NAME).setRequired(true);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("region") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getRegionFullName(model.getObject().getObjectId());
            }
        };
    }

    @Override
    protected IColumn<Matching, Sort> newParentColumn() {
        return new MapColumn<>("country") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getCountryName(model.getObject().getParentId());
            }
        };
    }

    @Override
    protected boolean isCodeVisible() {
        return true;
    }
}
