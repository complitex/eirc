package ru.complitex.common.ui.datatable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.TextField;
import ru.complitex.common.entity.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 28.11.2017 17:09
 */
public class DataTable<T extends Serializable> extends org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable<T, Sort>
        implements IAjaxIndicatorAware {
    private AjaxIndicatorAppender ajaxIndicatorAppender;

    private boolean hideOnEmpty = false;

    public DataTable(String id, List<? extends IColumn<T, Sort>> columns, DataProvider<T> provider,
                     DataForm<T> form, long rowsPerPage, String tableKey) {
        super(id, columns, provider, rowsPerPage);

//        ajaxIndicatorAppender = getColumns().stream().filter(c -> c instanceof DomainActionColumn)
//                .findAny()
//                .map(c -> ((DomainActionColumn) c).getAjaxIndicatorAppender())
//                .orElse(null);

        addTopToolbar(new AjaxFallbackHeadersToolbar<Sort>(this, provider){
            @Override
            public boolean isVisible() {
                return !hideOnEmpty || getRowCount() > 0;
            }
        });

        addTopToolbar(new DataToolbar(this, form){
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

        addBottomToolbar(new PagingToolbar(this, tableKey){
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

    public AjaxIndicatorAppender getAjaxIndicatorAppender() {
        return ajaxIndicatorAppender;
    }

    public void setAjaxIndicatorAppender(AjaxIndicatorAppender ajaxIndicatorAppender) {
        this.ajaxIndicatorAppender = ajaxIndicatorAppender;
    }

    public boolean isHideOnEmpty() {
        return hideOnEmpty;
    }

    public void setHideOnEmpty(boolean hideOnEmpty) {
        this.hideOnEmpty = hideOnEmpty;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

//        getColumns().forEach(c -> {
//            if (c instanceof DomainColumn){
//                NonContextual.of(DomainColumn.class).inject((DomainColumn) c);
//            }
//        });
    }
}
