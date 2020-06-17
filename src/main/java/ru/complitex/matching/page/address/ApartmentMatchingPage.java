package ru.complitex.matching.page.address;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.ApartmentGroup;
import ru.complitex.address.entity.Apartment;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:13
 */
public class ApartmentMatchingPage extends MatchingPage<Apartment> {
    public ApartmentMatchingPage() {
        super(Apartment.class);
    }

    @Override
    protected Component newObjectId(String componentId, IModel<Matching> model) {
        return new ApartmentGroup(componentId, PropertyModel.of(model, "objectId"), true);
    }
}
