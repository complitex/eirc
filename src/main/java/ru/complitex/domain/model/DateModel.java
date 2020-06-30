package ru.complitex.domain.model;

import org.apache.wicket.model.IModel;
import ru.complitex.domain.entity.Domain;

import java.util.Date;

/**
 * @author Anatoly A. Ivanov
 * 14.09.2018 13:40
 */
public class DateModel<T extends Domain<T>> extends AttributeModel<T, Date> {

    public DateModel(IModel<T> domainModel, int entityAttributeId) {
        super(domainModel, entityAttributeId);
    }

    @Override
    public Date getObject() {
        return getDomainModel().getObject().getDate(getEntityAttributeId());
    }

    @Override
    public void setObject(Date object) {
        getDomainModel().getObject().setDate(getEntityAttributeId(), object);
    }

    public static <T extends Domain<T>> DateModel<T> of(IModel<T> domainModel, int entityAttributeId){
        return new DateModel<>(domainModel, entityAttributeId);
    }
}
