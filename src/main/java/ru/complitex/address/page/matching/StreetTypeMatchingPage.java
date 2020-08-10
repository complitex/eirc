package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.entity.StreetType;
import ru.complitex.address.mapper.matching.StreetTypeMatchingMapper;
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
 * 14.05.2020 18:08
 */
public class StreetTypeMatchingPage extends MatchingPage<StreetType> {
    @Inject
    private StreetTypeMatchingMapper streetTypeMatchingMapper;

    @Inject
    private AddressService addressService;

    public StreetTypeMatchingPage() {
        super(StreetType.class);
    }

    @Override
    protected IFilterMapper<Matching> getFilterMapper() {
        return streetTypeMatchingMapper;
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new DomainGroup(componentId, StreetType.ENTITY, PropertyModel.of(model, "objectId"), StreetType.NAME);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("streetType") {
            @Override
            public String text(IModel<Matching> model) {
                return addressService.getStreetTypeName(model.getObject().getObjectId());
            }
        };
    }

    @Override
    protected boolean isParentVisible() {
        return false;
    }

    @Override
    protected boolean isAdditionalNameRequired() {
        return true;
    }

    @Override
    protected boolean isAdditionalNameVisible() {
        return true;
    }
}
