package ru.complitex.address.component.group;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.component.input.StreetInput;
import ru.complitex.common.component.form.Group;

/**
 * @author Anatoly Ivanov
 * 10.08.2020 17:47
 */
public class StreetGroup extends Group {
    public StreetGroup(String id, IModel<Long> streetModel) {
        super(id, new ResourceModel("street"));

        add(new StreetInput("street", streetModel){
            @Override
            public boolean isStreetRequired() {
                return StreetGroup.this.isRequired();
            }
        });
    }
}
