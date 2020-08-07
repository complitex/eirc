package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.DistrictGroup;
import ru.complitex.address.entity.District;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:07
 */
public class DistrictMatchingPage extends MatchingPage<District> {
    public DistrictMatchingPage() {
        super(District.class);
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new DistrictGroup(componentId, PropertyModel.of(model, "objectId")).setDistrictRequired(true);
    }
}
