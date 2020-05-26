package ru.complitex.matching.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.ui.form.DateTextFieldGroup;
import ru.complitex.common.ui.form.TextFieldGroup;
import ru.complitex.matching.entity.Matching;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 16:40
 */
public class MatchingModal extends Modal<Matching> {
    public MatchingModal(String markupId) {
        super(markupId, new CompoundPropertyModel<>(new Matching()));

        setBackdrop(Backdrop.FALSE);
        setCloseOnEscapeKey(false);

        header(new ResourceModel("modalHeader"));

        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        NotificationPanel notification = new NotificationPanel("notification");
        container.add(notification);

        container.add(newParentId("parentId").setVisible(isParentIdVisible()));
        container.add(newAdditionalParentId("additionalParentId").setVisible(isAdditionalParentIdVisible()));

        container.add(new TextFieldGroup<>("externalId", Long.class));
        container.add(new TextFieldGroup<>("additionalExternalId"));
        container.add(new TextFieldGroup<>("name"));
        container.add(new TextFieldGroup<>("additionalName"));
        container.add(new DateTextFieldGroup("startDate"));
        container.add(new DateTextFieldGroup("endDate"));
        container.add(new TextFieldGroup<>("companyId"));
        container.add(new TextFieldGroup<>("userCompanyId"));

        addButton(new BootstrapAjaxButton(Modal.BUTTON_MARKUP_ID, Buttons.Type.Outline_Primary) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                MatchingModal.this.save(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(container);
            }
        }.setLabel(new ResourceModel("save")));

        addButton(new BootstrapAjaxLink<Void>(Modal.BUTTON_MARKUP_ID, Buttons.Type.Outline_Secondary) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                MatchingModal.this.cancel(target);
            }
        }.setLabel(new ResourceModel("cancel")));

    }

    protected boolean isParentIdVisible(){
        return true;
    }

    protected Component newParentId(String componentId){
        return new TextFieldGroup<>(componentId, Long.class);
    }

    protected boolean isAdditionalParentIdVisible(){
        return true;
    }

    protected Component newAdditionalParentId(String componentId){
        return new TextFieldGroup<>(componentId, Long.class);
    }

    protected void open(AjaxRequestTarget target, Matching matching){
        setModelObject(matching);

        show(target);
    }


    protected void save(AjaxRequestTarget target) {

    }

    protected void cancel(AjaxRequestTarget target) {
        close(target);
    }
}
