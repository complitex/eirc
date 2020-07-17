package ru.complitex.common.component.form;

import org.apache.wicket.model.IModel;
import ru.complitex.common.util.Ids;

/**
 * @author Anatoly Ivanov
 * 13.07.2020 22:49
 */
public class TextField<T>  extends org.apache.wicket.markup.html.form.TextField<T> {
    private final String inputName = Ids.randomUUID();

    public TextField(String id) {
        super(id);
    }

    public TextField(String id, Class<T> type) {
        super(id, type);
    }

    public TextField(String id, IModel<T> model) {
        super(id, model);
    }

    public TextField(String id, IModel<T> model, Class<T> type) {
        super(id, model, type);
    }

    @Override
    public String getInputName() {
        return inputName;
    }
}
