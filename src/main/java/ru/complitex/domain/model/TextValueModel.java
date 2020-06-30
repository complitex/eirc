package ru.complitex.domain.model;

import org.apache.wicket.model.IModel;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.StringType;

/**
 * @author Anatoly Ivanov
 * 30.06.2020 5:48
 */
public class TextValueModel<T extends Domain<T>> extends TextModel<T>{
    private final int localeId;

    public TextValueModel(IModel<T> domainModel, int entityAttributeId, StringType stringType, int localeId) {
        super(domainModel, entityAttributeId, stringType);

        this.localeId = localeId;
    }

    @Override
    public String getObject() {
        return getDomainModel().getObject().getTextValue(getEntityAttributeId(), localeId);
    }

    @Override
    public void setObject(String text) {
        if (text != null){
            text = text.trim();

            if (StringType.UPPER_CASE.equals(getStringType())) {
                text = text.toUpperCase();
            }
        }

        getDomainModel().getObject().setTextValue(getEntityAttributeId(), text, localeId);
    }

    public static <T extends Domain<T>> TextValueModel<T> of(IModel<T> domainModel, int entityAttributeId, int localeId){
        return new TextValueModel<>(domainModel, entityAttributeId, StringType.UPPER_CASE, localeId);
    }
}
