package ru.complitex.address.component;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.component.form.Group;

/**
 * @author Anatoly Ivanov
 * 09.08.2020 21:20
 */
public class RegionGroup extends Group {
    public RegionGroup(String id, IModel<Long> regionModel) {
        super(id, new ResourceModel("region"));

        add(new RegionInput("region", regionModel){
            @Override
            public boolean isRegionRequired() {
                return RegionGroup.this.isRequired();
            }
        });
    }
}
