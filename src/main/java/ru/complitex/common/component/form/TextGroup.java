package ru.complitex.common.component.form;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 22.12.2017 8:03
 */
public class TextGroup<T extends Serializable> extends Group {
    private final TextField<T> textField;

    public TextGroup(String id, IModel<String> labelModel, IModel<T> model, Class<T> type) {
        super(id, labelModel);

        add(textField = new TextField<>("input", model, type){
            @Override
            public boolean isRequired() {
                return TextGroup.this.isRequired();
            }
        });

        textField.setLabel(labelModel);
    }

    public TextGroup(String id, IModel<String> labelModel, IModel<T> model) {
        this(id, labelModel, model, null);
    }

    public TextGroup(String id, IModel<T> model, Class<T> type) {
        this(id, new ResourceModel("_" + id), model, type);
    }

    public TextGroup(String id, IModel<T> model) {
        this(id, new ResourceModel("_" + id), model, null);
    }
}
