package ru.complitex.common.component.table;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import ru.complitex.common.entity.Filter;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 12.12.2018 18:07
 */
public class TableForm<T extends Serializable> extends Form<Filter<T>> {
    public TableForm(String id, Filter<T> filter) {
        super(id, Model.of(filter));

        setOutputMarkupId(true);
    }
}
