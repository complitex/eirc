package ru.complitex.sync.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.spinner.SpinnerAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.ui.datatable.DataColumn;
import ru.complitex.common.ui.datatable.DataForm;
import ru.complitex.common.ui.datatable.DataProvider;
import ru.complitex.common.ui.datatable.DataTable;
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

        FilterWrapper<Sync> filterWrapper = FilterWrapper.of(new Sync(domain.getEntityId()));

        DataProvider<Sync> provider = new DataProvider<>(filterWrapper) {
            @Override
            protected List<Sync> data() {
                return syncMapper.getSyncs(filterWrapper);
            }

            @Override
            public long size() {
                return syncMapper.getSyncsCount(filterWrapper);
            }
        };

        List<IColumn<Sync, Sort>> columns = new ArrayList<>();

        columns.add(new DataColumn<Sync>("id").setCssClass("domain-id-column"));
        columns.add(new DataColumn<>("name"));
        columns.add(new DataColumn<>("additionalName"));
        columns.add(new DataColumn<>("altName"));
        columns.add(new DataColumn<>("altAdditionalName"));
        columns.add(new DataColumn<>("parentId"));
        columns.add(new DataColumn<>("additionalParentId"));
        columns.add(new DataColumn<>("externalId"));
        columns.add(new DataColumn<>("additionalExternalId"));
        columns.add(new DataColumn<>("date"));
        columns.add(new DataColumn<>("status"){
            @Override
            protected IModel<?> getLabelModel(IModel<Sync> rowModel) {
                return new ResourceModel("syncStatus." + rowModel.getObject().getStatus());
            }
        });

        DataForm<Sync> form = new DataForm<>("form", filterWrapper);
        container.add(form);

        DataTable<Sync> table = new DataTable<>("table", provider, columns, form, 10, "syncPage" + domain.getEntityName());
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
