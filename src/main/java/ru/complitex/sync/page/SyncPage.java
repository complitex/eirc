package ru.complitex.sync.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.ui.datatable.DataForm;
import ru.complitex.common.ui.datatable.DataProvider;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.util.Domains;
import ru.complitex.eirc.page.BasePage;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.mapper.SyncMapper;

import javax.inject.Inject;
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

        DataProvider<Sync> dataProvider = new DataProvider<>(filterWrapper) {
            @Override
            protected List<Sync> data() {
                return syncMapper.getSyncs(filterWrapper);
            }

            @Override
            public long size() {
                return syncMapper.getSyncsCount(filterWrapper);
            }
        };

        DataForm<Sync> dataForm = new DataForm<>("form", filterWrapper);
        container.add(dataForm);



    }
}
