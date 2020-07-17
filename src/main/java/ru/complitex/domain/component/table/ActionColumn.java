package ru.complitex.domain.component.table;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import ru.complitex.common.component.form.LinkPanel;
import ru.complitex.common.component.table.Column;
import ru.complitex.common.component.table.TableForm;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 26.02.2019 20:22
 */
public abstract class ActionColumn<T extends Domain<T>> extends Column<T> {

    private final AjaxIndicatorAppender ajaxIndicatorAppender = new AjaxIndicatorAppender(){
        @Override
        public void afterRender(final Component component){
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
        return new LinkPanel(componentId, new BootstrapAjaxButton(LinkPanel.LINK_COMPONENT_ID, Buttons.Type.Link){
            @Override
            public void onSubmit(AjaxRequestTarget target) {
                target.add(tableForm);
            }
        }.setIconType(FontAwesomeIconType.search));
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        RepeatingView repeatingView = new RepeatingView(componentId);
        cellItem.add(repeatingView);

        repeatingView.add(new LinkPanel(repeatingView.newChildId(), new BootstrapAjaxLink<>(LinkPanel.LINK_COMPONENT_ID, Buttons.Type.Link) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                ActionColumn.this.onAction(rowModel, target);
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.setEventPropagation(AjaxRequestAttributes.EventPropagation.STOP);
            }
        }.setIconType(FontAwesomeIconType.edit)));

        onNewAction(repeatingView, rowModel);
    }

    protected abstract void onAction(IModel<T> rowModel, AjaxRequestTarget target);

    @Override
    public String getCssClass() {
        return "action-column";
    }

    public AjaxIndicatorAppender getAjaxIndicatorAppender() {
        return ajaxIndicatorAppender;
    }

    protected void onNewAction(RepeatingView repeatingView, IModel<T> rowModel){

    }
}
