package ru.complitex.common.ui.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormGroup;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.danekja.java.util.function.serializable.SerializableConsumer;

import java.util.Date;

/**
 * @author Anatoly A. Ivanov
 * 22.12.2017 8:03
 */
public class DateTextFieldGroup extends FormComponentPanel<Date> {
    private DateTextField dateTextField;

    public DateTextFieldGroup(String id, IModel<String> label, IModel<Date> model) {
        super(id);

        setOutputMarkupId(true);

        FormGroup group = new FormGroup("group", label){
            @Override
            protected Component newLabel(String id, IModel<String> model) {
                return new Label(id, model){
                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);

                        if (isRequired()){
                            tag.put("required", "required");
                        }
                    }
                };
            }
        };
        group.add(dateTextField = new DateTextField("input",
                new Model<>(){
                    @Override
                    public Date getObject() {
                        return getModelObject();
                    }
                }, getDateTextFieldConfig()){
            @Override
            public boolean isRequired() {
                return DateTextFieldGroup.this.isRequired();
            }
        });
        dateTextField.setLabel(label);

        add(group);
    }

    public DateTextFieldGroup(String id, IModel<Date> model) {
        this(id, new ResourceModel(id), model);
    }

    public DateTextFieldGroup(String id) {
        this(id, new ResourceModel(id), null);
    }

    protected DateTextFieldConfig getDateTextFieldConfig() {
        return new DateTextFieldConfig()
                .withFormat("dd.MM.yyyy")
                .withLanguage("ru")
                .autoClose(true)
                .highlightToday(true);
    }

    public DateTextFieldGroup onUpdate(SerializableConsumer<AjaxRequestTarget> onUpdate){
        dateTextField.add(OnChangeAjaxBehavior.onChange(onUpdate));

        return this;
    }

    @Override
    public void convertInput() {
        setConvertedInput(dateTextField.getConvertedInput());
    }
}
