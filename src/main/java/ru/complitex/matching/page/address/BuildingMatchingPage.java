package ru.complitex.matching.page.address;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.BuildingGroup;
import ru.complitex.address.entity.Building;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:09
 */
public class BuildingMatchingPage extends MatchingPage<Building> {
    public BuildingMatchingPage() {
        super(Building.class);
    }

    @Override
    protected Component newObjectId(String componentId, IModel<Matching> model) {
        return new BuildingGroup(componentId, PropertyModel.of(model, "objectId"), true);
    }
}
