package ru.complitex.address.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.component.group.BuildingGroup;
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
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new BuildingGroup(componentId, PropertyModel.of(model, "objectId")).setBuildingRequired(true);
    }

    //todo add group and component
}
