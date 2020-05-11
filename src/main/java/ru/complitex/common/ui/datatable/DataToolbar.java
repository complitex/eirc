package ru.complitex.common.ui.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.NoFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 12.12.2018 19:07
 */
public class DataToolbar extends AbstractToolbar {
    private static final String FILTER_ID = "filter";

    public <T extends Serializable, S> DataToolbar(DataTable<T, S> dataTable, DataForm<T> dataForm) {
        super(dataTable);

        Args.notNull(dataTable, "table");

        IModel<List<IColumn<T, S>>> model = (IModel<List<IColumn<T, S>>>) () -> new LinkedList<>(dataTable.getColumns());

        ListView<IColumn<T, S>> filters = new ListView<>("filters", model) {

            @Override
            protected void populateItem(ListItem<IColumn<T, S>> item) {
                final IColumn<T, S> col = item.getModelObject();
                item.setRenderBodyOnly(true);

                Component filter = null;

                if (col instanceof IDataColumn) {
                    IDataColumn<T, S> filteredCol = (IDataColumn<T, S>) col;
                    filter = filteredCol.getFilter(FILTER_ID, dataForm);
                }

                if (filter == null) {
                    filter = new NoFilter(FILTER_ID);
                } else {
                    if (!filter.getId().equals(FILTER_ID)) {
                        throw new IllegalStateException(
                                "filter component returned  with an invalid component id. invalid component id [" +
                                        filter.getId() +
                                        "] required component id [" +
                                        getId() +
                                        "] generating column [" + col.toString() + "] ");
                    }
                }

                if (col instanceof IStyledColumn) {
                    filter.add(new Behavior() {
                        @Override
                        public void onComponentTag(final Component component, final ComponentTag tag) {
                            String className = ((IStyledColumn<?, S>) col).getCssClass();
                            if (!Strings.isEmpty(className)) {
                                tag.append("class", className, " ");
                            }
                        }
                    });
                }

                item.add(filter);
            }
        };
        filters.setReuseItems(true);

        add(filters);
    }

    @Override
    protected void onBeforeRender() {
        if (findParent(DataForm.class) == null) {
            throw new IllegalStateException("FilterToolbar must be contained within a FilterDataForm");
        }
        super.onBeforeRender();
    }

}