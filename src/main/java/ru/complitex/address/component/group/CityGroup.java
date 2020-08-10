package ru.complitex.address.component.group;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.component.input.CityInput;
import ru.complitex.common.component.form.Group;

/**
 * @author Anatoly Ivanov
 * 09.08.2020 21:20
 */
public class CityGroup extends Group {
    public CityGroup(String id, IModel<Long> cityModel) {
        super(id, new ResourceModel("city"));

        add(new CityInput("city", cityModel){
            @Override
            public boolean isCityRequired() {
                return CityGroup.this.isRequired();
            }
        });
    }
}
