package ru.complitex.common.component.table;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.TextField;
import ru.complitex.common.entity.Sort;
import ru.complitex.domain.component.table.ActionColumn;

import java.io.Serializable;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 28.11.2017 17:09
 */
public class Table<T extends Serializable> extends DataTable<T, Sort>
        implements IAjaxIndicatorAware {
    private AjaxIndicatorAppender ajaxIndicatorAppender;

    private boolean hideOnEmpty = false;

    public Table(String id, Provider<T> provider, List<? extends IColumn<T, Sort>> columns,
                 TableForm<T> form, long rowsPerPage, String tableKey) {
        super(id, columns, provider, rowsPerPage);

        ajaxIndicatorAppender = getColumns().stream().filter(c -> c instanceof ActionColumn)
                .findAny()
                .map(c -> ((ActionColumn<?>) c).getAjaxIndicatorAppender())
                .orElse(null);

        getBody().setOutputMarkupId(true);

        getBottomToolbars().setOutputMarkupId(true);
        getBottomToolbars().setOutputMarkupPlaceholderTag(true);

        addTopToolbar(new AjaxFallbackHeadersToolbar<>(this, provider){
            @Override
            public boolean isVisible() {
                return !hideOnEmpty || getRowCount() > 0;
            }
        });

        addTopToolbar(new Toolbar(this, form){
            @Override
            protected void onBeforeRender() {
                super.onBeforeRender();

                visitChildren(TextField.class, (component, visit) ->
                        component.add(new AttributeModifier("class", "form-control")));
            }

            @Override
            public boolean isVisible() {
                return !hideOnEmpty || getRowCount() > 0;
            }
        });

        addBottomToolbar(new Paging(this, tableKey){
            @Override
            public boolean isVisible() {
                return getRowCount() > 5;
            }
        });
    }

    @Override
    public String getAjaxIndicatorMarkupId() {
        try {
            return ajaxIndicatorAppender != null ? ajaxIndicatorAppender.getMarkupId() : "none";
        } catch (Exception e) {
            return "none";
        }
    }

    public boolean isHideOnEmpty() {
        return hideOnEmpty;
    }

    public void setHideOnEmpty(boolean hideOnEmpty) {
        this.hideOnEmpty = hideOnEmpty;
    }

    public void update(AjaxRequestTarget target){
        target.add(getBody());
        target.add(getBottomToolbars());
    }
}
