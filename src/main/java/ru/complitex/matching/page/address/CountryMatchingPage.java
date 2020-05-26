package ru.complitex.matching.page.address;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.entity.Country;
import ru.complitex.domain.component.form.DomainAutoCompleteGroup;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:02
 */
public class CountryMatchingPage extends MatchingPage<Country> {
    public CountryMatchingPage() {
        super(Country.class);
    }

    @Override
    protected Component newObjectId(String componentId, IModel<Matching> model) {
        return new DomainAutoCompleteGroup(componentId, Country.ENTITY_NAME, Country.NAME,
                PropertyModel.of(model, "objectId"), true);
    }

    @Override
    protected boolean isParentIdVisible() {
        return false;
    }

    @Override
    protected boolean isAdditionalParentIdVisible() {
        return false;
    }
}
