package ru.complitex.domain.component.table;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import ru.complitex.common.component.form.AjaxLinkPanel;
import ru.complitex.common.component.table.Column;
import ru.complitex.common.component.table.TableForm;

import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 26.02.2019 20:22
 */
public class ActionColumn<T extends Serializable> extends Column<T> {

    private final AjaxIndicatorAppender ajaxIndicatorAppender = new AjaxIndicatorAppender() {
        @Override
        public void afterRender(final Component component) {
            Response r = component.getResponse();

            r.write("<span class=\"");
            r.write(getSpanClass());
            r.write("\" ");
            r.write("style=\"display: none\"");
            r.write("id=\"");
            r.write(getMarkupId());
            r.write("\">");
            r.write("<img src=\"");
            r.write(getIndicatorUrl());
            r.write("\" alt=\"\"/></span>");
        }
    };

    public ActionColumn() {
        super(Model.of(""), null);
    }

    @Override
    public Component getHeader(String componentId) {
        return super.getHeader(componentId).add(ajaxIndicatorAppender);
    }

    @Override
    public Component newFilter(String componentId, TableForm<T> tableForm) {
        return new AjaxLinkPanel(componentId, "fa fa-undo") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                ActionColumn.this.onSearch(target);
            }
        };
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        RepeatingView repeatingView = new RepeatingView(componentId);
        cellItem.add(repeatingView);

        repeatingView.add(new AjaxLinkPanel(repeatingView.newChildId(), "fa fa-edit") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onEdit(rowModel, target);
            }
        });

        addAction(repeatingView, rowModel);
    }

    @Override
    public String getCssClass() {
        return "action-column";
    }

    protected void onSearch(AjaxRequestTarget target) {
    }

    protected void onEdit(IModel<T> rowModel, AjaxRequestTarget target) {
    }

    public AjaxIndicatorAppender getAjaxIndicatorAppender() {
        return ajaxIndicatorAppender;
    }

    protected void addAction(RepeatingView repeatingView, IModel<T> rowModel) {

    }
}
