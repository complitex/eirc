package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.RegionGroup;
import ru.complitex.address.entity.Country;
import ru.complitex.address.entity.Region;
import ru.complitex.address.mapper.matching.RegionMatchingMapper;
import ru.complitex.address.service.AddressService;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

import javax.inject.Inject;
import java.util.List;

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
    protected Long getMatchingListCount(Filter<Matching> filter) {
        return regionMatchingMapper.getRegionMatchingListCount(filter);
    }

    @Override
    protected List<Matching> getMatchingList(Filter<Matching> filter) {
        return regionMatchingMapper.getRegionMatchingList(filter);
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new RegionGroup(componentId, PropertyModel.of(model, "objectId")).setRegionRequired(true);
    }

    @Override
    protected Component newParentGroup(String componentId, IModel<Matching> model) {
        return new DomainGroup(componentId, Country.ENTITY, Country.NAME, PropertyModel.of(model, "parentId"));
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
