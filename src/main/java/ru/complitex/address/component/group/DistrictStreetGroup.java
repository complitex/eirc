package ru.complitex.address.component.group;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.component.input.DistrictStreetInput;
import ru.complitex.common.component.form.Group;

/**
 * @author Anatoly Ivanov
 * 11.08.2020 23:22
 */
public class DistrictStreetGroup extends Group {
    public DistrictStreetGroup(String id, IModel<Long> districtModel, IModel<Long> streetModel) {
        super(id, new ResourceModel("street"));

        add(new DistrictStreetInput("street", districtModel, streetModel){
            @Override
            public boolean isDistrictRequired() {
                return DistrictStreetGroup.this.isRequired();
            }

            @Override
            public boolean isStreetRequired() {
                return DistrictStreetGroup.this.isRequired();
            }
        });
    }
}
