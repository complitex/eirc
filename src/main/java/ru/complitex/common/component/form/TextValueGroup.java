package ru.complitex.common.component.form;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import ru.complitex.domain.util.Locales;

import java.io.Serializable;

/**
 * @author Anatoly Ivanov
 * 29.06.2020 15:39
 */
public class TextValueGroup<T extends Serializable> extends Group {
    private final TextField<T> textFieldRu;
    private final TextField<T> textFieldUa;

    public TextValueGroup(String id, IModel<String> labelModel, IModel<T> modelRu, IModel<T> modelUa) {
        super(id, labelModel);

        add(textFieldRu = new TextField<>("inputRu", modelRu){
            @Override
            public boolean isRequired() {
                return Locales.RU_ID.equals(Locales.getSystemLocaleId()) && TextValueGroup.this.isRequired();
            }
        });

        textFieldRu.setLabel(labelModel);


        add(textFieldUa = new TextField<>("inputUa", modelUa){
            @Override
            public boolean isRequired() {
                return Locales.UA_ID.equals(Locales.getSystemLocaleId()) && TextValueGroup.this.isRequired();
            }
        });

        textFieldUa.setLabel(labelModel);
    }
}
