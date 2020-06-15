package ru.complitex.matching.page.address;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.RegionGroup;
import ru.complitex.address.entity.Region;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:06
 */
public class RegionMatchingPage extends MatchingPage<Region> {
    public RegionMatchingPage() {
        super(Region.class);
    }

    @Override
    protected Component newObjectId(String componentId, IModel<Matching> model) {
        return new RegionGroup(componentId, PropertyModel.of(model, "objectId"), true);
    }
}
