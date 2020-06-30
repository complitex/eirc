package ru.complitex.domain.model;

import org.apache.wicket.model.IModel;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 14.09.2018 13:44
 */
public class NumberModel<T extends Domain<T>> extends AttributeModel<T, Long> {
    public NumberModel(IModel<T> domainModel, int entityAttributeId) {
        super(domainModel, entityAttributeId);
    }

    @Override
    public Long getObject() {
        return getDomainModel().getObject().getNumber(getEntityAttributeId());
    }

    @Override
    public void setObject(Long object) {
        getDomainModel().getObject().setNumber(getEntityAttributeId(), object);
    }

    public static <T extends Domain<T>> NumberModel<T> of(IModel<T> domainModel, int entityAttributeId){
        return new NumberModel<>(domainModel, entityAttributeId);
    }
}
