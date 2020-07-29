package ru.complitex.common.component.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableConsumer;

/**
 * @author Anatoly Ivanov
 * 29.07.2020 0:25
 */
public class TextFieldPanel<T> extends Panel {
    public TextFieldPanel(String id, IModel<T> model, SerializableConsumer<AjaxRequestTarget> onChange) {
        super(id);

        TextField<T> textField = new TextField<>("text", model);

        if (onChange != null){
            textField.add(OnChangeAjaxBehavior.onChange(onChange));
        }

        add(textField);
    }
}
