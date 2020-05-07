package ru.complitex.common.ui.datatable;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Anatoly A. Ivanov
 * 12.12.2018 19:42
 */
public class AbstractFilter extends Panel {
    private final DataForm<?> form;

    public AbstractFilter(String id, DataForm<?> form) {
        super(id);

        this.form = form;
    }

    public DataForm<?> getForm() {
        return form;
    }
}
