package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.CityGroup;
import ru.complitex.address.entity.City;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:07
 */
public class CityMatchingPage extends MatchingPage<City> {
    public CityMatchingPage() {
        super(City.class);
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new CityGroup(componentId, PropertyModel.of(model, "objectId")).setCityRequired(true);
    }
}
