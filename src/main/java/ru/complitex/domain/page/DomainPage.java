package ru.complitex.domain.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
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
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.model.FilterModel;
import ru.complitex.domain.component.table.ActionColumn;
import ru.complitex.domain.component.table.DomainColumn;
import ru.complitex.domain.component.table.IdColumn;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.Entity;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.service.DomainService;
import ru.complitex.domain.service.EntityService;
import ru.complitex.domain.util.Domains;
import ru.complitex.eirc.page.base.BasePage;

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

    private int[] requiredEntityAttributeIds;

    private Entity entity;

    private WebMarkupContainer container;

    private FeedbackPanel notification;

    private Table<T> table;

    private DomainModal<T> modal;

    public DomainPage(Class<T> domainClass, int... requiredEntityAttributeIds) {
        this.domainClass = domainClass;

        this.requiredEntityAttributeIds = requiredEntityAttributeIds;

        this.entity = entityService.getEntity(domainClass);

        add(new Label("title", getTitleModel()));

        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        notification = new NotificationPanel("notification");
        notification.setOutputMarkupId(true);
        container.add(notification);

        Provider<T> provider = new Provider<T>(newFilterModel()) {
            @Override
            public Long count() {
                return getDomainsCount(getFilter());
            }

            @Override
            public List<T> list() {
                return getDomains(getFilter());
            }
        };

        provider.setSort(new Sort("object_id"), SortOrder.DESCENDING);

        Form<T> form = new Form<>("form");
        container.add(form);

        List<IColumn<T, Sort>> columns = new ArrayList<>();

        IdColumn<T> idColumn = new IdColumn<>(t -> table.update(t));

        columns.add(idColumn);

        getListEntityAttributes().forEach(entityAttribute -> addColumn(entityAttribute, columns));

        if (isEditEnabled()) {
            columns.add(new ActionColumn<T>() {
                @Override
                protected void onSearch(Table<T> table, AjaxRequestTarget target) {
                    Filter<T> filter = table.getFilterModel().getObject();

                    filter.getObject().setObjectId(null);

                    filter.getObject().getAttributes().forEach(a -> {
                        a.setNumber(null);
                        a.setText(null);
                    });

                    filter.getMap().clear();

                    provider.setSort(new Sort("object_id"), SortOrder.DESCENDING);

                    target.add(container);
                }

                @Override
                protected void onEdit(IModel<T> rowModel, AjaxRequestTarget target) {
                    DomainPage.this.onEdit(rowModel.getObject(), target);
                }

                @Override
                protected void addAction(RepeatingView repeatingView, IModel<T> rowModel) {
                    DomainPage.this.onNewAction(repeatingView, rowModel);
                }
            });
        }


        table = new Table<>("table", provider, columns, 10, "domainListModalPage" + domainClass.getName()) {
            @Override
            protected Item<T> newRowItem(String id, int index, IModel<T> model) {
                Item<T> item = super.newRowItem(id, index, model);

                item.add(new AjaxEventBehavior("click") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        onRowClick(item.getModelObject(), target);
                    }
                });

                item.add(new CssClassNameAppender("pointer"));

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
            modal = newDomainModal(DOMAIN_EDIT_MODAL_ID);

            editForm.add(modal);
        } else {
            editForm.add(new EmptyPanel("edit"));
        }
    }

    public Entity getEntity() {
        return entity;
    }

    protected IModel<String> getTitleModel() {
        return Model.of(getEntity().getValue().getText());
    }

    protected IModel<Filter<T>> newFilterModel() {
        return FilterModel.of(Domains.newObject(domainClass, entity));
    }

    protected DomainModal<T> newDomainModal(String componentId) {
        return new DomainModal<>(componentId, domainClass, getEditEntityAttributes(), t -> t.add(notification, table)) {
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
        modal.edit(newDomain(), target);
    }

    protected T newDomain() {
        return Domains.newObject(domainClass);
    }

    protected void addColumn(EntityAttribute entityAttribute, List<IColumn<T, Sort>> columns) {
        columns.add(new DomainColumn<>(entityAttribute));
    }

    protected void onEdit(T object, AjaxRequestTarget target) {
        modal.edit(object, target);
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

    protected int[] getRequiredEntityAttributeIds() {
        return requiredEntityAttributeIds;
    }

    protected List<EntityAttribute> getListEntityAttributes() {
        return getEntity().getEntityAttributes();
    }

    protected List<EntityAttribute> getEditEntityAttributes() {
        List<EntityAttribute> entityAttributes = getListEntityAttributes();

        Set<Integer> required = new HashSet<>();

        for (int id : getRequiredEntityAttributeIds()) {
            required.add(id);
        }

        entityAttributes.forEach(a -> a.setRequired(required.contains(a.getEntityAttributeId())));

        return entityAttributes;
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

    protected boolean isEditEnabled() {
        return true;
    }

    protected boolean isAddEnabled() {
        return true;
    }

    protected void onNewAction(RepeatingView repeatingView, IModel<T> rowModel) {

    }
}
