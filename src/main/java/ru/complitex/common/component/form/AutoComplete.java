package ru.complitex.common.component.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import ru.complitex.common.util.Ids;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Anatoly Ivanov
 * 12.06.2020 19:27
 */
public abstract class AutoComplete<T extends Serializable> extends Panel {
    private final HiddenField<Long> idField;

    private final AutoCompleteTextField<T> textField;

    private boolean required;

    private IModel<String> placeholder;

    public AutoComplete(String id, IModel<Long> model) {
        super(id);

        setOutputMarkupId(true);

        idField = new HiddenField<>("id", model, Long.class){
            @Override
            public boolean isRequired() {
                return AutoComplete.this.isRequired();
            }
        };

        idField.setOutputMarkupId(true);
        idField.setConvertEmptyInputStringToNull(true);

        idField.add(OnChangeAjaxBehavior.onChange(this::onChangeId));

        add(idField);

        textField = new AutoCompleteTextField<T>("text",
                new IModel<>(){
                    @Override
                    public T getObject() {
                        return AutoComplete.this.getObject(model.getObject());
                    }

                    @Override
                    public void setObject(T object) {

                    }
                }, null,
                new AbstractAutoCompleteTextRenderer<>() {
                    @Override
                    protected String getTextValue(T object) {
                        return AutoComplete.this.getTextValue(object);
                    }

                    @Override
                    protected CharSequence getOnSelectJavaScriptExpression(T item) {
                        Long id = AutoComplete.this.getId(item);

                        return  "$('#" + idField.getMarkupId() + "').val('" + id + "'); " +
                                " $('#" + idField.getMarkupId() + "').change();" +
                                " input";
                    }
                }, new AutoCompleteSettings().setPreselect(true).setAdjustInputWidth(true)
        ) {
            private final String inputName = Ids.randomUUID();

            @Override
            public String getInputName() {
                return inputName;
            }

            @Override
            protected void onComponentTag(final ComponentTag tag){
                super.onComponentTag(tag);

                IModel<String> placeholder = AutoComplete.this.getPlaceholder();

                if (placeholder != null){
                    tag.put("placeholder", placeholder.getObject());
                }
            }

            @Override
            protected Iterator<T> getChoices(String input) {
                return AutoComplete.this.getChoices(input);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return (IConverter<C>) new IConverter<T>() {
                    @Override
                    public T convertToObject(String value, Locale locale) throws ConversionException {
                        return null;
                    }

                    @Override
                    public String convertToString(T value, Locale locale) {
                        return getTextValue(value);
                    }
                };
            }
        };

        textField.setType(Object.class);

        textField.add(OnChangeAjaxBehavior.onChange(target -> {
            if (textField.getInput().isEmpty() || (model.getObject() != null &&
                    !Objects.equals(getTextValue(getObject(model.getObject())), textField.getInput()))){
                model.setObject(null);

                target.appendJavaScript(String.format("Wicket.DOM.get('%s').value = null;", idField.getMarkupId()));
            }

            onChangeText(target);
        }));

        add(textField);
    }

    protected void onChangeId(AjaxRequestTarget target){

    }

    protected void onChangeText(AjaxRequestTarget target){

    }

    public Long getObjectId(){
        return idField.getModelObject();
    }

    public String getInputText(){
        return textField.getInput();
    }

    protected HiddenField<Long> getIdField() {
        return idField;
    }

    protected AutoCompleteTextField<T> getTextField() {
        return textField;
    }

    protected abstract String getTextValue(T object);

    protected abstract Iterator<T> getChoices(String input);

    protected abstract Long getId(T object);

    protected abstract T getObject(Long id);

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public IModel<String> getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(IModel<String> placeholder) {
        this.placeholder = placeholder;
    }

}
