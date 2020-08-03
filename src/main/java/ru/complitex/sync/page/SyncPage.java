package ru.complitex.sync.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.spinner.SpinnerAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.component.table.PropertyColumn;
import ru.complitex.common.component.table.Provider;
import ru.complitex.common.component.table.Table;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.model.FilterModel;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.util.Domains;
import ru.complitex.eirc.page.BasePage;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.mapper.SyncMapper;
import ru.complitex.sync.service.SyncService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 29.04.2020 23:53
 */
public class SyncPage<T extends Domain<T>> extends BasePage {
    @Inject
    private SyncMapper syncMapper;

    @Inject
    private SyncService syncService;

    public SyncPage(Class<T> domainClass) {
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        NotificationPanel notification = new NotificationPanel("notification");
        notification.setOutputMarkupId(true);
        container.add(notification);

        T domain = Domains.newObject(domainClass);

        Provider<Sync> provider = new Provider<>(FilterModel.of(new Sync(domain.getEntityId()))) {
            @Override
            protected List<Sync> data() {
                return syncMapper.getSyncs(getFilter());
            }

            @Override
            public long size() {
                return syncMapper.getSyncsCount(getFilter());
            }
        };

        List<IColumn<Sync, Sort>> columns = new ArrayList<>();

        columns.add(new PropertyColumn<Sync>("id").setCssClass("id-column"));
        columns.add(new PropertyColumn<>("name"));
        columns.add(new PropertyColumn<>("additionalName"));
        columns.add(new PropertyColumn<>("altName"));
        columns.add(new PropertyColumn<>("altAdditionalName"));
        columns.add(new PropertyColumn<>("parentId"));
        columns.add(new PropertyColumn<>("additionalParentId"));
        columns.add(new PropertyColumn<>("externalId"));
        columns.add(new PropertyColumn<>("additionalExternalId"));
        columns.add(new PropertyColumn<>("date"));
        columns.add(new PropertyColumn<>("status"){
            @Override
            protected IModel<?> newItemModel(IModel<Sync> rowModel) {
                return new ResourceModel("syncStatus." + rowModel.getObject().getStatus());
            }
        });

        Form<Sync> form = new Form<>("form");
        container.add(form);

        Table<Sync> table = new Table<>("table", provider, columns, 10, "syncPage" + domain.getEntityName());
        form.add(table);

        form.add(new SpinnerAjaxButton("load", new ResourceModel("load"), Buttons.Type.Outline_Primary) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                syncService.load(domainClass);

                target.add(table);
            }
        }.setDefaultFormProcessing(false));

        form.add(new SpinnerAjaxButton("sync", new ResourceModel("sync"), Buttons.Type.Outline_Primary) {
            @Override
            public void onSubmit(AjaxRequestTarget target) {
                syncService.sync(domainClass);

                target.add(table);
            }
        }.setDefaultFormProcessing(false));
    }
}
