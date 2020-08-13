package ru.complitex.address.component.group;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.component.input.BuildingInput;
import ru.complitex.common.component.form.Group;

/**
 * @author Anatoly Ivanov
 * 11.08.2020 23:22
 */
public class BuildingGroup extends Group {
    public BuildingGroup(String id, IModel<Long> buildingModel) {
        super(id, new ResourceModel("building"));

        add(new BuildingInput("building", buildingModel){
            @Override
            public boolean isBuildingRequired() {
                return BuildingGroup.this.isRequired();
            }
        });
    }
}
