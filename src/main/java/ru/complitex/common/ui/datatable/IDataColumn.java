package ru.complitex.common.ui.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 12.12.2018 19:05
 */
public interface IDataColumn<T extends Serializable, S> extends IColumn<T, S> {
    Component getFilter(String componentId, DataForm<T> dataForm);
}
