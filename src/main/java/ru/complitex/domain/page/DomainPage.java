package ru.complitex.domain.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import ru.complitex.common.component.table.Provider;
import ru.complitex.common.component.table.Table;
import ru.complitex.common.component.table.TableForm;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.domain.component.table.AbstractDomainColumn;
import ru.complitex.domain.component.table.DomainActionColumn;
import ru.complitex.domain.component.table.DomainColumn;
import ru.complitex.domain.component.table.DomainIdColumn;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.Entity;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.service.DomainService;
import ru.complitex.domain.service.EntityService;
import ru.complitex.domain.util.Domains;
import ru.complitex.eirc.page.BasePage;

import javax.inject.Inject;
import java.util.*;

/**
 * @author Anatoly A. Ivanov
 * 19.12.2017 3:40
 */
public abstract class DomainPage<T extends Domain<T>> extends BasePage {
    public static final String CURRENT_PAGE_ATTRIBUTE = "_PAGE";

    public static final String DOMAIN_EDIT_MODAL_ID = "edit";

    @Inject
    private EntityService entityService;

    @Inject
    private DomainService domainService;

    private Class<T> domainClass;

    private Entity entity;

    private Filter<T> filter;

    private WebMarkupContainer container;

    private FeedbackPanel notification;

    private Table<T> table;

    private DomainModal<T> domainModal;

    public DomainPage(Class<T> domainClass) {
        this.domainClass = domainClass;

        this.entity = entityService.getEntity(domainClass);

        add(new Label("title", getTitleModel()));

        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        notification = new NotificationPanel("notification");
        notification.setOutputMarkupId(true);
        container.add(notification);


        filter = newFilter();

        Provider<T> provider = new Provider<T>(filter) {
            @Override
            protected List<T> data() {
                return getDomains(getFilter());
            }

            @Override
            public long size() {
                return getDomainsCount(getFilter());
            }
        };

        TableForm<T> form = new TableForm<>("form", filter);
        container.add(form);


        List<IColumn<T, Sort>> columns = new ArrayList<>();

        columns.add(new DomainIdColumn<>());

        getListEntityAttributes().forEach(a -> columns.add(newDomainColumn(a)));


        if (isEditEnabled()) {
            columns.add(new DomainActionColumn<T>() {
                @Override
                protected void onAction(IModel<T> rowModel, AjaxRequestTarget target) {
                    onEdit(rowModel.getObject(), target);
                }

                @Override
                protected void onNewAction(RepeatingView repeatingView, IModel<T> rowModel) {
                    DomainPage.this.onNewAction(repeatingView, rowModel);
                }
            });
        }


        table = new Table<T>("table", provider, columns, form, 10, "domainListModalPage" + domainClass.getName()){
            @Override
            protected Item<T> newRowItem(String id, int index, IModel<T> model) {
                Item<T> item = super.newRowItem(id, index, model);

                onRowItem(item);

                return item;
            }
        };

        table.setCurrentPage((Long) Optional.ofNullable(getSession().getAttribute(getClass().getName() +
                CURRENT_PAGE_ATTRIBUTE)).orElse(0L));

        form.add(table);


        container.add(new AjaxLink<Void>("create") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onCreate(target);
            }

            @Override
            public boolean isVisible() {
                return isAddEnabled();
            }
        });


        Form<T> editForm = new Form<>("editForm");
        container.add(editForm);

        if (isEditEnabled()) {
            domainModal = newDomainModal(DOMAIN_EDIT_MODAL_ID);

            editForm.add(domainModal);
        }else{
            editForm.add(new EmptyPanel("edit"));
        }
    }

    public Entity getEntity() {
        return entity;
    }

    protected IModel<String> getTitleModel() {
        return Model.of(getEntity().getValue().getText());
    }

    protected Filter<T> newFilter() {
        return Filter.of(Domains.newObject(domainClass));
    }

    protected DomainModal<T> newDomainModal(String componentId) {
        return new DomainModal<>(componentId, domainClass, getEditEntityAttributes(), t -> t.add(notification, table)){
            @Override
            protected boolean validate(Domain<T> domain) {
                return DomainPage.this.validate(domain);
            }

            @Override
            protected Component newGroup(String groupId, EntityAttribute entityAttribute) {
                Component group = DomainPage.this.newGroup(groupId, getModel(), entityAttribute);

                return group != null ? group : super.newGroup(groupId, entityAttribute);
            }
        };
    }

    protected Component newGroup(String groupId, IModel<T> domainModel, EntityAttribute entityAttribute) {
        return null;
    }

    protected boolean validate(Domain<T> domain) {
        return true;
    }

    protected void onCreate(AjaxRequestTarget target) {
        domainModal.edit(newDomain(), target);
    }

    protected T newDomain(){
        return Domains.newObject(domainClass);
    }

    protected AbstractDomainColumn<T> newDomainColumn(EntityAttribute a) {
        return new DomainColumn<>(a);
    }

    protected void onEdit(T object, AjaxRequestTarget target) {
        domainModal.edit(object, target);
    }

    protected void onRowItem(Item<T> item){
        item.add(new AjaxEventBehavior("click") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                onRowClick(item.getModelObject(), target);
            }
        });

        item.add(new CssClassNameAppender("pointer"));
    }

    protected void onRowClick(T object, AjaxRequestTarget target) {
        if (isEditEnabled()) {
            onEdit(object, target);
        }
    }

    protected List<T> getDomains(Filter<T> filter) {
        return domainService.getDomains(domainClass, filter);
    }

    protected Long getDomainsCount(Filter<T> filter) {
        return domainService.getDomainsCount(filter);
    }

    protected int[] getRequiredEntityAttributeIds(){
        return new int[]{};
    }

    protected List<EntityAttribute> getListEntityAttributes(){
        return getEntity().getAttributes();
    }

    protected List<EntityAttribute> getEditEntityAttributes(){
        List<EntityAttribute> entityAttributes = getListEntityAttributes();

        Set<Integer> required = new HashSet<>();

        for (int id : getRequiredEntityAttributeIds()){
            required.add(id);
        }

        entityAttributes.forEach(a -> a.setRequired(required.contains(a.getEntityAttributeId())));

        return entityAttributes;
    }

    public Filter<T> getFilter() {
        return filter;
    }

    public WebMarkupContainer getContainer() {
        return container;
    }

    public FeedbackPanel getNotification() {
        return notification;
    }

    public Table<T> getTable() {
        return table;
    }

    protected boolean isEditEnabled(){
        return true;
    }

    protected boolean isAddEnabled(){
        return true;
    }

    protected void onNewAction(RepeatingView repeatingView, IModel<T> rowModel){

    }

}
