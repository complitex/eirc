package ru.complitex.address.component.group;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.entity.StreetType;
import ru.complitex.domain.component.form.DomainGroup;

/**
 * @author Anatoly Ivanov
 * 10.08.2020 18:08
 */
public class StreetTypeGroup extends DomainGroup {
    public StreetTypeGroup(String id, IModel<Long> model) {
        super(id, new ResourceModel("streetType"), StreetType.ENTITY, model, StreetType.NAME);
    }
}
