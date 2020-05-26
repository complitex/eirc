package ru.complitex.common.ui.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.*;
import org.apache.wicket.validation.IValidator;
import org.danekja.java.util.function.serializable.SerializableConsumer;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 22.12.2017 8:03
 */
public class TextFieldGroup<T extends Serializable> extends FormComponentPanel<T> {
    private TextField<T> textField;

    public TextFieldGroup(String id, IModel<String> label, IModel<T> model, Class<T> type) {
        super(id, model);

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        Group group = new Group("group", label){
            @Override
            protected boolean isRequired() {
                return TextFieldGroup.this.isRequired();
            }
        };

        group.add(textField = new TextField<>("input",
                new Model<T>(){
                    @Override
                    public T getObject() {
                        return getModelObject();
                    }
                }, type){
            @Override
            protected void onComponentTag(final ComponentTag tag){
                super.onComponentTag(tag);

                tag.put("autocomplete", "off");

                onTextFieldTag(tag);
            }

            @Override
            public boolean isRequired() {
                return TextFieldGroup.this.isRequired();
            }
        });
        textField.setLabel(label);

        add(group);
    }

    public TextFieldGroup(String id, String resourceKey) {
        this(id, new ResourceModel(resourceKey), null, null);
    }

    public TextFieldGroup(String id) {
        this(id, id);
    }

    public TextFieldGroup(String id, Class<T> type) {
        this(id, new ResourceModel(id), null, type);
    }

    public TextFieldGroup(String id, IModel<T> model, Class<T> type) {
        this(id, new ResourceModel(id), model, type);
    }

    public TextFieldGroup(String id, IModel<T> model) {
        this(id, model, null);
    }

    public TextFieldGroup(String id, IModel<String> label, IModel<T> model) {
        this(id, label, model, null);
    }

    public TextFieldGroup<T> onUpdate(SerializableConsumer<AjaxRequestTarget> onUpdate){
        textField.add(AjaxFormComponentUpdatingBehavior.onUpdate("change", onUpdate));

        return this;
    }

    public TextField<T> getTextField() {
        return textField;
    }

    public TextFieldGroup<T> setType(Class<?> type){
        textField.setType(type);

        return this;
    }

    public TextFieldGroup<T> addValidator(IValidator<T> validator){
        textField.add(validator);

        return this;
    }

    @Override
    protected void onModelChanged() {
        textField.modelChanged();
    }

    protected void onTextFieldTag(ComponentTag tag){

    }

    @Override
    public void convertInput() {
        setConvertedInput(textField.getConvertedInput());
    }
}
