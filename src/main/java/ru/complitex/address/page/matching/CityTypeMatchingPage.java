package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.entity.CityType;
import ru.complitex.address.mapper.matching.CityTypeMatchingMapper;
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
public class CityTypeMatchingPage extends MatchingPage<CityType> {
    @Inject
    private CityTypeMatchingMapper cityTypeMatchingMapper;

    @Inject
    private AddressService addressService;

    public CityTypeMatchingPage() {
        super(CityType.class);
    }

    @Override
    protected Long getMatchingListCount(Filter<Matching> filter) {
        return cityTypeMatchingMapper.getCityTypeMatchingListCount(filter);
    }

    @Override
    protected List<Matching> getMatchingList(Filter<Matching> filter) {
        return cityTypeMatchingMapper.getCityTypeMatchingList(filter);
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new DomainGroup(componentId, CityType.ENTITY, PropertyModel.of(model, "objectId"), CityType.NAME);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("cityType") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getCityTypeName(model.getObject().getObjectId());
            }
        };
    }

    @Override
    protected boolean isParentVisible() {
        return false;
    }
}
