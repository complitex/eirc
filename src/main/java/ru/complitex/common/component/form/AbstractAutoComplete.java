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

import java.io.Serializable;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Anatoly Ivanov
 * 12.06.2020 19:27
 */
public abstract class AbstractAutoComplete<T extends Serializable> extends Panel {
    private final HiddenField<Long> idField;

    private final AutoCompleteTextField<T> nameField;

    private boolean required;

    private IModel<String> placeholder;

    public AbstractAutoComplete(String id, IModel<Long> model) {
        super(id);

        setOutputMarkupId(true);

        idField = new HiddenField<>("id", model, Long.class){
            @Override
            public boolean isRequired() {
                return required;
            }
        };

        idField.setOutputMarkupId(true);
        idField.setConvertEmptyInputStringToNull(true);

        idField.add(OnChangeAjaxBehavior.onChange(this::onChange));

        add(idField);

        nameField = new AutoCompleteTextField<T>("name",
                new IModel<T>(){
                    @Override
                    public T getObject() {
                        return AbstractAutoComplete.this.getObject(model.getObject());
                    }

                    @Override
                    public void setObject(T object) {

                    }
                }, null,
                new AbstractAutoCompleteTextRenderer<>() {
                    @Override
                    protected String getTextValue(T object) {
                        return AbstractAutoComplete.this.getTextValue(object);
                    }

                    @Override
                    protected CharSequence getOnSelectJavaScriptExpression(T item) {
                        Long id = AbstractAutoComplete.this.getId(item);

                        return  "$('#" + idField.getMarkupId() + "').val('" + id + "'); " +
                                " $('#" + idField.getMarkupId() + "').change();" +
                                " input";
                    }
                }, new AutoCompleteSettings().setPreselect(true).setAdjustInputWidth(true)
        ) {
            private final String inputName = UUID.randomUUID().toString();

            @Override
            public String getInputName() {
                return inputName;
            }

            @Override
            protected void onComponentTag(final ComponentTag tag){
                super.onComponentTag(tag);

                IModel<String> placeholder = AbstractAutoComplete.this.getPlaceholder();

                if (placeholder != null){
                    tag.put("placeholder", placeholder.getObject());
                }
            }

            @Override
            protected Iterator<T> getChoices(String input) {
                return AbstractAutoComplete.this.getChoices(input);
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

        nameField.setType(Object.class);

        nameField.add(OnChangeAjaxBehavior.onChange(target -> {
            if (nameField.getInput().isEmpty()){
                model.setObject(null);

                target.add(idField);
            }
        }));

        add(nameField);
    }

    protected void onChange(AjaxRequestTarget target){

    }

    protected HiddenField<Long> getIdField() {
        return idField;
    }

    protected AutoCompleteTextField<T> getNameField() {
        return nameField;
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
