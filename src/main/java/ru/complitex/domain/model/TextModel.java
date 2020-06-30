package ru.complitex.domain.model;

import org.apache.wicket.model.IModel;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.StringType;

/**
 * @author Anatoly A. Ivanov
 * 14.09.2018 13:46
 */
public class TextModel<T extends Domain<T>> extends AttributeModel<T, String> {
    private final StringType stringType;

    public TextModel(IModel<T> domainModel, int entityAttributeId, StringType stringType) {
        super(domainModel, entityAttributeId);

        this.stringType = stringType;
    }

    @Override
    public String getObject() {
        return getDomainModel().getObject().getText(getEntityAttributeId());
    }

    @Override
    public void setObject(String text) {
        if (text != null){
            text = text.trim();

            if (StringType.UPPER_CASE.equals(stringType)) {
                text = text.toUpperCase();
            }
        }

        getDomainModel().getObject().setText(getEntityAttributeId(), text);
    }

    public StringType getStringType() {
        return stringType;
    }

    public static <T extends Domain<T>> TextModel<T> of(IModel<T> domainModel, int entityAttributeId){
        return new TextModel<>(domainModel, entityAttributeId, StringType.UPPER_CASE);
    }
}
