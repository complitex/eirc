package ru.complitex.matching.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.component.form.DateGroup;
import ru.complitex.common.component.form.TextGroup;
import ru.complitex.company.entity.Company;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 16:40
 */
public class MatchingModal extends Modal<Matching> {
    @Inject
    private MatchingMapper matchingMapper;

    private WebMarkupContainer container;

    public MatchingModal(String markupId) {
        super(markupId, Model.of(new Matching()));

        setBackdrop(Backdrop.FALSE);
        setCloseOnEscapeKey(false);
        size(Size.Large);

        header(new ResourceModel("_header"));

        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        NotificationPanel notification = new NotificationPanel("notification");
        container.add(notification);

        container.add(newObjectId("objectId"));

        container.add(newParentId("parentId").setVisible(isParentIdVisible()));
        container.add(newAdditionalParentId("additionalParentId").setVisible(isAdditionalParentIdVisible()));

        container.add(new TextGroup<>("externalId", PropertyModel.of(getModel(), "externalId"), Long.class));
        container.add(new TextGroup<>("additionalExternalId", PropertyModel.of(getModel(), "additionalExternalId")));
        container.add(new TextGroup<>("name", PropertyModel.of(getModel(), "name")).setRequired(true));
        container.add(new TextGroup<>("additionalName", PropertyModel.of(getModel(), "additionalName")));
        container.add(new DateGroup("startDate", PropertyModel.of(getModel(), "startDate")));
        container.add(new DateGroup("endDate", PropertyModel.of(getModel(), "endDate")));

        container.add(new DomainGroup("companyId", Company.ENTITY_NAME, Company.NAME,
                PropertyModel.of(getModel(), "companyId")));
        container.add(new DomainGroup("userCompanyId",  Company.ENTITY_NAME, Company.NAME,
                PropertyModel.of(getModel(), "userCompanyId")));

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

    protected Component newObjectId(String componentId){
        return new TextGroup<>(componentId, PropertyModel.of(getModel(), "objectId"), Long.class);
    }

    protected boolean isParentIdVisible(){
        return true;
    }

    protected Component newParentId(String componentId){
        return new TextGroup<>(componentId, PropertyModel.of(getModel(), "parentId"), Long.class);
    }

    protected boolean isAdditionalParentIdVisible(){
        return true;
    }

    protected Component newAdditionalParentId(String componentId){
        return new TextGroup<>(componentId, PropertyModel.of(getModel(), "additionalParentId"), Long.class);
    }

    protected void open(AjaxRequestTarget target, Matching matching){
        setModelObject(matching);

        target.add(container);

        show(target);
    }

    private void clear(){
        container.visitChildren(FormComponent.class, (c, v) -> {
            ((FormComponent<?>) c).clearInput();
            ((FormComponent<?>) c).setModelObject(null);
        });
    }


    protected void save(AjaxRequestTarget target) {
        matchingMapper.save(getModelObject());

        success(getString("info_saved"));

        close(target);

        onSave(target);

        clear();
    }

    protected void onSave(AjaxRequestTarget target){

    }

    protected void cancel(AjaxRequestTarget target) {
        close(target);

        clear();
    }
}
