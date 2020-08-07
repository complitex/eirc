package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.entity.Country;
import ru.complitex.address.mapper.matching.CountryMatchingMapper;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.mapper.AttributeMapper;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:02
 */
public class CountryMatchingPage extends MatchingPage<Country> {
    @Inject
    private AttributeMapper attributeMapper;

    @Inject
    private CountryMatchingMapper countryMatchingMapper;

    public CountryMatchingPage() {
        super(Country.class);
    }

    @Override
    protected Long getMatchingListCount(Filter<Matching> filter) {
        return countryMatchingMapper.getCountryMatchingListCount(filter);
    }

    @Override
    protected List<Matching> getMatchingList(Filter<Matching> filter) {
        return countryMatchingMapper.getCountryMatchingList(filter);
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new DomainGroup(componentId, Country.ENTITY, PropertyModel.of(model, "objectId"), Country.NAME).setRequired(true);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("country"){
            @Override
            public String text(IModel<Matching> model) {
                return attributeMapper.getTextValue(Country.ENTITY, model.getObject().getObjectId(), Country.NAME);
            }
        };
    }

    @Override
    protected boolean isParentVisible() {
        return false;
    }
}
