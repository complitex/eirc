package ru.complitex.domain.model;

import org.apache.wicket.model.IModel;
import ru.complitex.domain.entity.Domain;

import java.math.BigDecimal;

/**
 * @author Anatoly A. Ivanov
 * 14.09.2018 13:44
 */
public class DecimalModel<T extends Domain<T>> extends AttributeModel<T, BigDecimal> {

    public DecimalModel(IModel<T> domainModel, int entityAttributeId) {
       super(domainModel, entityAttributeId);
    }

    @Override
    public BigDecimal getObject() {
        String val = getDomainModel().getObject().getText(getEntityAttributeId());

        return val != null ? new BigDecimal(val) : null;
    }

    @Override
    public void setObject(BigDecimal object) {
        getDomainModel().getObject().setText(getEntityAttributeId(), object != null ? object.toPlainString() : null);
    }

    public static <T extends Domain<T>>  DecimalModel<T> of(IModel<T> domainModel, int entityAttributeId){
        return new DecimalModel<T>(domainModel, entityAttributeId);
    }
}
