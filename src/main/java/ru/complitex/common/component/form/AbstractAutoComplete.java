package ru.complitex.common.component.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Locale;

/**
 * @author Anatoly Ivanov
 * 12.06.2020 19:27
 */
public abstract class AbstractAutoComplete<T extends Serializable> extends Panel {
    private final HiddenField<Long> idField;

    private final AutoCompleteTextField<T> nameField;

    public AbstractAutoComplete(String id, IModel<Long> model) {
        super(id);

        idField = new HiddenField<>("id", new Model<>(), Long.class);

        idField.setConvertEmptyInputStringToNull(true);
        idField.setOutputMarkupId(true);

        add(idField);

        nameField = new AutoCompleteTextField<T>("name",
                new Model<T>(){
                    @Override
                    public T getObject() {
                        return AbstractAutoComplete.this.getObject(model.getObject());
                    }

                    @Override
                    public void setObject(T object) {
                        super.setObject(object);

                        model.setObject(getId(object));
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
                }, new AutoCompleteSettings()
                .setAdjustInputWidth(true)
                .setShowListOnFocusGain(true)
                .setPreselect(true)
        ) {
            @Override
            protected void onComponentTag(final ComponentTag tag){
                super.onComponentTag(tag);

                tag.put("autocomplete", "off");
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
                        return getObject(idField.getConvertedInput());
                    }

                    @Override
                    public String convertToString(T value, Locale locale) {
                        return getTextValue(value);
                    }
                };
            }
        };

        nameField.setType(Object.class);

        add(nameField);
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

    protected void onChange(AjaxRequestTarget target){

    }
}
