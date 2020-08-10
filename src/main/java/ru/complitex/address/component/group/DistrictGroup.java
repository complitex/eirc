package ru.complitex.address.component.group;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.component.input.DistrictInput;
import ru.complitex.common.component.form.Group;

/**
 * @author Anatoly Ivanov
 * 09.08.2020 21:20
 */
public class DistrictGroup extends Group {
    public DistrictGroup(String id, IModel<Long> districtModel) {
        super(id, new ResourceModel("district"));

        add(new DistrictInput("district", districtModel){
            @Override
            public boolean isDistrictRequired() {
                return DistrictGroup.this.isRequired();
            }
        });
    }
}
