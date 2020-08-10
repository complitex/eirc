package ru.complitex.domain.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.complitex.common.component.form.DateGroup;
import ru.complitex.common.component.form.TextGroup;
import ru.complitex.common.component.form.TextValueGroup;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.Entity;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.entity.ValueType;
import ru.complitex.domain.model.*;
import ru.complitex.domain.service.DomainService;
import ru.complitex.domain.service.EntityService;
import ru.complitex.domain.util.Domains;
import ru.complitex.domain.util.Locales;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 26.02.2019 18:17
 */
public class DomainModal<T extends Domain<T>> extends Modal<T> {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private EntityService entityService;

    @Inject
    private DomainService domainService;

    private WebMarkupContainer container;
    private NotificationPanel notification;

    private ListView<EntityAttribute> listView;

    private Entity entity;

    private SerializableConsumer<AjaxRequestTarget> onChange;

    public DomainModal(String markupId, Class<T> domainClass, List<EntityAttribute> entityAttributes,
                       SerializableConsumer<AjaxRequestTarget> onChange) {
        super(markupId, Model.of(Domains.newObject(domainClass)));

        this.onChange = onChange;

        setBackdrop(Backdrop.FALSE);
        setCloseOnEscapeKey(false);
        size(Size.Large);

        entity = entityService.getEntity(domainClass);

        header(Model.of(entity.getValue().getText()));

        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true)
                .setOutputMarkupPlaceholderTag(true)
                .setVisible(false);
        add(container);

        notification = new NotificationPanel("notification");
        notification.showRenderedMessages(false)
                .setOutputMarkupId(true);
        container.add(notification);

        listView = new ListView<>("attributes", entityAttributes) {
            @Override
            protected void populateItem(ListItem<EntityAttribute> item) {
                item.add(newGroup("group",  item.getModelObject()));
            }
        };
        listView.setReuseItems(true);
        container.add(listView);

        addButton(new BootstrapAjaxButton(Modal.BUTTON_MARKUP_ID, Buttons.Type.Outline_Primary) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                DomainModal.this.save(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(container);
            }
        }.setLabel(new ResourceModel("save")));

        addButton(new BootstrapAjaxLink<Void>(Modal.BUTTON_MARKUP_ID, Buttons.Type.Outline_Secondary) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                DomainModal.this.cancel(target);
            }
        }.setLabel(new ResourceModel("cancel")));
    }

    protected Component newGroup(String groupId, EntityAttribute entityAttribute){
        IModel<String> labelModel = Model.of(entityAttribute.getValue().getText());

        int entityAttributeId = entityAttribute.getEntityAttributeId();
        boolean required  = entityAttribute.isRequired();

        switch (entityAttribute.getValueTypeId()){
            case ValueType.NUMBER:
                return new TextGroup<>(groupId, labelModel, NumberModel.of(getModel(), entityAttributeId), Long.class)
                        .setRequired(required);

            case ValueType.DECIMAL:
                return new TextGroup<>(groupId, labelModel, DecimalModel.of(getModel(), entityAttributeId), BigDecimal.class)
                        .setRequired(required);

            case ValueType.TEXT:
                return new TextGroup<>(groupId, labelModel, TextModel.of(getModel(), entityAttributeId))
                        .setRequired(required);

            case ValueType.DATE:
                return new DateGroup(groupId, labelModel, DateModel.of(getModel(), entityAttributeId))
                        .setRequired(required);

            case ValueType.REFERENCE:
                EntityAttribute reference = entityService.getReferenceEntityAttribute(entityAttribute);

                return new DomainGroup(groupId, labelModel, reference.getEntityName(), NumberModel.of(getModel(), entityAttributeId), reference.getEntityAttributeId()
                )
                        .setRequired(true);

            case ValueType.TEXT_VALUE:
                return new TextValueGroup<>(groupId, labelModel,
                        TextValueModel.of(getModel(), entityAttributeId, Locales.RU_ID),
                        TextValueModel.of(getModel(), entityAttributeId, Locales.UA_ID))
                        .setRequired(required);

            default:
                throw new RuntimeException(entityAttribute.toString());
        }
    }

    public void edit(T domain, AjaxRequestTarget target){
        setModelObject(domain);

        listView.removeAll();
        container.setVisible(true);
        target.add(container);

        show(target);
    }

    private void save(AjaxRequestTarget target){
        try {
            T domain = getModelObject();

            if (!DomainModal.this.validate(domain)){
                target.add(notification);

                return;
            }

//            domain.setUserId(getCurrentUserId());

            domainService.save(domain);

            getSession().success(entity.getValue().getText() + " " + getString("info_saved"));

            close(target);

            if (onChange != null){
                onChange.accept(target);
            }
        } catch (Exception e) {
            log.error("error save domain", e);

            getSession().error("Ошибка сохранения " + e.getLocalizedMessage());

            target.add(notification);
        }
    }

    public void cancel(AjaxRequestTarget target){
        container.setVisible(false);

        container.visitChildren(FormComponent.class, (c, v) -> ((FormComponent) c).clearInput());

        close(target);
    }

    protected boolean validate(Domain<T> domain){
        return true;
    }
}
