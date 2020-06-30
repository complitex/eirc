package ru.complitex.address.model;

import org.apache.wicket.cdi.NonContextual;
import org.apache.wicket.model.IModel;
import ru.complitex.common.model.LoadableModel;
import ru.complitex.domain.mapper.AttributeMapper;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 18.06.2020 00:51
 */
public class AddressModel extends LoadableModel<Long> {
    @Inject
    private AttributeMapper attributeMapper;

    private final String entityName;
    private final IModel<Long> model;
    private final int entityAttributeId;

    public AddressModel(String entityName, IModel<Long> model, int entityAttributeId) {
        this.entityName = entityName;
        this.model = model;
        this.entityAttributeId = entityAttributeId;
    }

    @Override
    protected Long load() {
        if (attributeMapper == null){
            NonContextual.of(this).inject(this);
        }

        return attributeMapper.getNumber(entityName, model.getObject(), entityAttributeId);
    }
}
