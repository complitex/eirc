package ru.complitex.domain.component.table;

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
import ru.complitex.common.component.form.LinkPanel;
import ru.complitex.common.component.table.TableForm;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly A. Ivanov
 * 26.02.2019 20:22
 */
public abstract class DomainActionColumn<T extends Domain<T>> extends AbstractDomainColumn<T> {

    private AjaxIndicatorAppender ajaxIndicatorAppender = new AjaxIndicatorAppender(){
        @Override
        protected String getSpanClass() {
            return super.getSpanClass();
        }
    };

    public DomainActionColumn() {
        super(Model.of(""));
    }

//    @Override
//    public Component getHeader(String componentId) {
//        return super.getHeader(componentId).add(ajaxIndicatorAppender);
//    }

    @Override
    public Component newFilter(String componentId, TableForm<T> tableForm) {
        return new LinkPanel(componentId, new BootstrapAjaxLink<>(LinkPanel.LINK_COMPONENT_ID, Buttons.Type.Link){
            @Override
            public void onClick(AjaxRequestTarget target) {
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
                DomainActionColumn.this.onAction(rowModel, target);
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
        return "domain-action-column";
    }

    public AjaxIndicatorAppender getAjaxIndicatorAppender() {
        return ajaxIndicatorAppender;
    }

    protected void onNewAction(RepeatingView repeatingView, IModel<T> rowModel){

    }
}
