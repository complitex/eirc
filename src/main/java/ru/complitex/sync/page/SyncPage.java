package ru.complitex.sync.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

        columns.add(new DataColumn<>("id"));
        columns.add(new DataColumn<>("name"));
        columns.add(new DataColumn<>("additionalName"));
        columns.add(new DataColumn<>("altName"));
        columns.add(new DataColumn<>("altAdditionalName"));
        columns.add(new DataColumn<>("parentId"));
        columns.add(new DataColumn<>("additionalParentId"));
        columns.add(new DataColumn<>("externalId"));
        columns.add(new DataColumn<>("additionalExternalId"));
        columns.add(new DataColumn<>("date"));
        columns.add(new DataColumn<>("status"));

        DataForm<Sync> form = new DataForm<>("form", filterWrapper);
        container.add(form);

        DataTable<Sync> table = new DataTable<>("table", columns, provider, form, 15, "syncPage" + domain.getEntityName());
        form.add(table);

        form.add(new AjaxLink<>("sync") {
            @Override
            public void onClick(AjaxRequestTarget target) {

            }
        });
    }
}
