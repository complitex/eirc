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
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.ui.datatable.DataProvider;
import ru.complitex.common.ui.datatable.DataForm;
import ru.complitex.common.ui.datatable.DataTable;
import ru.complitex.domain.component.datatable.*;
import ru.complitex.domain.entity.*;
import ru.complitex.domain.service.DomainService;
import ru.complitex.domain.service.EntityService;
import ru.complitex.domain.util.Domains;
import ru.complitex.eirc.page.BasePage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Anatoly A. Ivanov
 * 19.12.2017 3:40
 */
public abstract class DomainListModalPage<T extends Domain<T>> extends BasePage {
    public static final String CURRENT_PAGE_ATTRIBUTE = "_PAGE";

    public static final String DOMAIN_EDIT_MODAL_ID = "edit";

    @Inject
    private EntityService entityService;

    @Inject
    private DomainService domainService;

    private Class<T> domainClass;

    private Entity entity;

    private FilterWrapper<T> filterWrapper;

    private WebMarkupContainer container;

    private FeedbackPanel notification;

    private DataTable<T> table;

    private AbstractDomainEditModal<T> domainEditModal;

    public DomainListModalPage(Class<T> domainClass) {
        this.domainClass = domainClass;

        this.entity = entityService.getEntity(domainClass);

        add(new Label("title", getTitleModel()));

        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        notification = new NotificationPanel("notification");
        notification.setOutputMarkupId(true);
        container.add(notification);


        filterWrapper = newFilterWrapper();

        DataProvider<T> dataProvider = new DataProvider<T>(filterWrapper) {
            @Override
            protected List<T> data() {
                return getDomains(getFilterWrapper());
            }

            @Override
            public long size() {
                return getDomainsCount(getFilterWrapper());
            }
        };

        DataForm<T> form = new DataForm<>("form", filterWrapper);
        container.add(form);


        List<IColumn<T, Sort>> columns = new ArrayList<>();

        columns.add(new DomainIdColumn<>());

        getListEntityAttributes().forEach(a -> columns.add(newDomainColumn(a)));


        if (isEditEnabled()) {
            columns.add(new DomainEditActionsColumn<T>() {
                @Override
                protected void onAction(IModel<T> rowModel, AjaxRequestTarget target) {
                    onEdit(rowModel.getObject(), target);
                }

                @Override
                protected void onNewAction(RepeatingView repeatingView, IModel<T> rowModel) {
                    DomainListModalPage.this.onNewAction(repeatingView, rowModel);
                }
            });
        }


        table = new DataTable<T>("table", columns, dataProvider, form, 15, "domainListModalPage" + domainClass.getName()){
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


        container.add(new AjaxLink<Void>("add") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onAdd(target);
            }

            @Override
            public boolean isVisible() {
                return isAddEnabled();
            }
        });


        Form<T> editForm = new Form<>("editForm");
        container.add(editForm);

        if (isEditEnabled()) {
            domainEditModal = newDomainEditModal(DOMAIN_EDIT_MODAL_ID);

            editForm.add(domainEditModal);
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

    protected FilterWrapper<T> newFilterWrapper() {
        return FilterWrapper.of(Domains.newObject(domainClass));
    }

    protected AbstractDomainEditModal<T> newDomainEditModal(String componentId) {
        return new DomainEditModal<T>(componentId, domainClass, getEditEntityAttributes(), t -> t.add(notification, table)){
            @Override
            protected boolean validate(Domain<T> domain) {
                return DomainListModalPage.this.validate(domain);
            }

            @Override
            protected Component getComponent(String componentId, Attribute attribute) {
                return DomainListModalPage.this.getEditComponent(componentId, attribute);
            }
        };
    }

    protected Component getEditComponent(String componentId, Attribute attribute) {
        return null;
    }

    protected boolean validate(Domain<T> domain) {
        return true;
    }

    protected void onAdd(AjaxRequestTarget target) {
        domainEditModal.edit(newDomain(), target);
    }

    protected T newDomain(){
        return Domains.newObject(domainClass);
    }

    protected AbstractDomainColumn<T> newDomainColumn(EntityAttribute a) {
        return new DomainColumn<>(a);
    }

    protected void onEdit(T object, AjaxRequestTarget target) {
        domainEditModal.edit(object, target);
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

    protected List<T> getDomains(FilterWrapper<T> filterWrapper) {
        return domainService.getDomains(domainClass, filterWrapper);
    }

    protected Long getDomainsCount(FilterWrapper<T> filterWrapper) {
        return domainService.getDomainsCount(filterWrapper);
    }

    protected List<EntityAttribute> getListEntityAttributes(){
        return getEntity().getAttributes();
    }

    protected List<EntityAttribute> getEditEntityAttributes(){
        return getListEntityAttributes();
    }

    public FilterWrapper<T> getFilterWrapper() {
        return filterWrapper;
    }

    public WebMarkupContainer getContainer() {
        return container;
    }

    public FeedbackPanel getNotification() {
        return notification;
    }

    public DataTable<T> getTable() {
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
