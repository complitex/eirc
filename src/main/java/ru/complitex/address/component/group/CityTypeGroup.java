package ru.complitex.address.component.group;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.entity.CityType;
import ru.complitex.domain.component.form.DomainGroup;

/**
 * @author Anatoly Ivanov
 * 10.08.2020 18:08
 */
public class CityTypeGroup extends DomainGroup {
    public CityTypeGroup(String id, IModel<Long> cityTypeModel) {
        super(id, new ResourceModel("cityType"), CityType.ENTITY, cityTypeModel, CityType.NAME);
    }
}
