package ru.complitex.address.component.group;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.component.input.ApartmentInput;
import ru.complitex.common.component.form.Group;

/**
 * @author Anatoly Ivanov
 * 14.08.2020 1:20
 */
public class ApartmentGroup extends Group {
    public ApartmentGroup(String id, IModel<Long> apartmentModel) {
        super(id, new ResourceModel("apartment"));

        add(new ApartmentInput("apartment", apartmentModel){
            @Override
            public boolean isApartmentRequired() {
                return ApartmentGroup.this.isRequired();
            }
        });
    }
}
