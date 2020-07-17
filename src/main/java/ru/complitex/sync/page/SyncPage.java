package ru.complitex.sync.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.spinner.SpinnerAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.component.table.KeyColumn;
import ru.complitex.common.component.table.TableForm;
import ru.complitex.common.component.table.Provider;
import ru.complitex.common.component.table.Table;
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

        Filter<Sync> filter = Filter.of(new Sync(domain.getEntityId()));

        Provider<Sync> provider = new Provider<>(filter) {
            @Override
            protected List<Sync> data() {
                return syncMapper.getSyncs(filter);
            }

            @Override
            public long size() {
                return syncMapper.getSyncsCount(filter);
            }
        };

        List<IColumn<Sync, Sort>> columns = new ArrayList<>();

        columns.add(new KeyColumn<Sync>("id").setCssClass("id-column"));
        columns.add(new KeyColumn<>("name"));
        columns.add(new KeyColumn<>("additionalName"));
        columns.add(new KeyColumn<>("altName"));
        columns.add(new KeyColumn<>("altAdditionalName"));
        columns.add(new KeyColumn<>("parentId"));
        columns.add(new KeyColumn<>("additionalParentId"));
        columns.add(new KeyColumn<>("externalId"));
        columns.add(new KeyColumn<>("additionalExternalId"));
        columns.add(new KeyColumn<>("date"));
        columns.add(new KeyColumn<>("status"){
            @Override
            protected IModel<?> newItemModel(IModel<Sync> rowModel) {
                return new ResourceModel("syncStatus." + rowModel.getObject().getStatus());
            }
        });

        TableForm<Sync> form = new TableForm<>("form", filter);
        container.add(form);

        Table<Sync> table = new Table<>("table", provider, columns, form, 10, "syncPage" + domain.getEntityName());
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
