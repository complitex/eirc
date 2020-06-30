package ru.complitex.common.component.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.Date;

/**
 * @author Anatoly A. Ivanov
 * 22.12.2017 8:03
 */
public class DateGroup extends Group {
    private final DateTextField dateTextField;

    public DateGroup(String id, IModel<String> labelModel, IModel<Date> model) {
        super(id, labelModel);

        add(dateTextField = new DateTextField("input", model, getDateTextFieldConfig()){
            @Override
            public boolean isRequired() {
                return DateGroup.this.isRequired();
            }
        });

        dateTextField.setLabel(labelModel);
    }

    public DateGroup(String id, IModel<Date> model) {
        this(id, new ResourceModel("_" + id), model);
    }

    protected DateTextFieldConfig getDateTextFieldConfig() {
        return new DateTextFieldConfig()
                .withFormat("dd.MM.yyyy")
                .withLanguage("ru")
                .autoClose(true)
                .highlightToday(true);
    }
}
