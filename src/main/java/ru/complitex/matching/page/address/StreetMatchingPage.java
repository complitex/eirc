package ru.complitex.matching.page.address;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.StreetGroup;
import ru.complitex.address.entity.Street;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:09
 */
public class StreetMatchingPage extends MatchingPage<Street> {
    public StreetMatchingPage() {
        super(Street.class);
    }

    @Override
    protected Component newObjectId(String componentId, IModel<Matching> model) {
        return new StreetGroup(componentId, PropertyModel.of(model, "objectId"), true);
    }
}
