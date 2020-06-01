package ru.complitex.common.component.table;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import ru.complitex.common.entity.FilterWrapper;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 12.12.2018 18:07
 */
public class TableForm<T extends Serializable> extends Form<FilterWrapper<T>> {
    public TableForm(String id, FilterWrapper<T> filterWrapper) {
        super(id, Model.of(filterWrapper));

        setOutputMarkupId(true);
    }
}
