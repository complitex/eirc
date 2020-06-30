package ru.complitex.domain.model;

import org.apache.wicket.model.IModel;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 09.01.2019 19:40
 */
public abstract class AttributeModel<T extends Domain<T>, A> implements IModel<A> {
    private final IModel<T> domainModel;
    private final int entityAttributeId;

    public AttributeModel(IModel<T> domainModel, int entityAttributeId) {
        this.domainModel = domainModel;
        this.entityAttributeId = entityAttributeId;
    }

    public IModel<T> getDomainModel() {
        return domainModel;
    }

    public int getEntityAttributeId() {
        return entityAttributeId;
    }
}
