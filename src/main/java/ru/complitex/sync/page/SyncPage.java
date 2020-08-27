package ru.complitex.sync.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.agilecoders.wicket.core.markup.html.bootstrap.utilities.BackgroundColorBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.spinner.SpinnerAjaxButton;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.protocol.ws.api.registry.PageIdKey;
import ru.complitex.common.component.table.PropertyColumn;
import ru.complitex.common.component.table.Provider;
import ru.complitex.common.component.table.Table;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.model.FilterModel;
import ru.complitex.common.util.Exceptions;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.util.Domains;
import ru.complitex.eirc.page.base.BasePage;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.entity.SyncErrorMessage;
import ru.complitex.sync.entity.SyncInfoMessage;
import ru.complitex.sync.entity.SyncMessage;
import ru.complitex.sync.mapper.SyncMapper;
import ru.complitex.sync.service.ISyncListener;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 29.04.2020 23:53
 */
public abstract class SyncPage<T extends Domain<T>> extends BasePage {
    @Inject
    private SyncMapper syncMapper;

    private final Class<T> domainClass;

    private final ISyncListener syncListener;

    private ProgressBar progress;

    public SyncPage(Class<T> domainClass) {
        this.domainClass = domainClass;

        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        NotificationPanel notification = new NotificationPanel("notification");
        notification.setOutputMarkupPlaceholderTag(true);
        notification.setOutputMarkupId(true);
        container.add(notification);

        T domain = Domains.newObject(domainClass);

        Provider<Sync> provider = new Provider<>(FilterModel.of(new Sync(domain.getEntityId()))) {
            @Override
            public Long count() {
                return syncMapper.getSyncsCount(getFilter());
            }

            @Override
            public List<Sync> list() {
                return syncMapper.getSyncs(getFilter());
            }
        };

        provider.setSort(new Sort("id"), SortOrder.DESCENDING);

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
        columns.add(new PropertyColumn<>("balanceHolder"));
        columns.add(new PropertyColumn<>("date"));
        columns.add(new PropertyColumn<>("status"){
            @Override
            public IModel<?> model(IModel<Sync> rowModel) {
                return new ResourceModel("syncStatus." + rowModel.getObject().getStatus());
            }
        });

        Form<Sync> form = new Form<>("form");
        container.add(form);

        Table<Sync> table = new Table<>("table", provider, columns, 10, "syncPage" + domain.getEntityName()){
            @Override
            protected Component newPagingComponent(String componentId) {
                ProgressBar progress = new ProgressBar(componentId, Model.of(50), BackgroundColorBehavior.Color.Primary, true);

                progress.setOutputMarkupId(true);
                progress.setVisible(false);

                return SyncPage.this.progress = progress;
            }
        };
        form.add(table);


        form.add(new SpinnerAjaxButton("load", new ResourceModel("load"), Buttons.Type.Outline_Primary) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                SyncPage.this.load();
            }
        }.setDefaultFormProcessing(false));

        form.add(new SpinnerAjaxButton("sync", new ResourceModel("sync"), Buttons.Type.Outline_Primary) {
            @Override
            public void onSubmit(AjaxRequestTarget target) {
                SyncPage.this.sync();
            }
        }.setDefaultFormProcessing(false));

        add(new WebSocketBehavior() {
            @Override
            protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
                if (message instanceof SyncErrorMessage){
                    error(((SyncErrorMessage) message).getError());

                    handler.add(notification);
                }

                if (message instanceof SyncInfoMessage){
                    success(((SyncInfoMessage) message).getInfo());

                    handler.add(notification);
                }

                if (message instanceof SyncMessage){
                    handler.add(table.getBody());
                    handler.add(table.getBottomToolbars());
                }
            }
        });

        syncListener = new ISyncListener() {
            private long time = 0;
            private int val = 0;

            @Override
            public void onLoading() {
                progress.setVisible(true);
                progress.value(0);

                sendMessage(new SyncInfoMessage(getString("info_loading")));
            }

            @Override
            public void onLoad(Integer value) {
                if (System.currentTimeMillis() - time > 100 && value != val){
                    progress.value(value);

                    sendMessage(new SyncMessage());

                    time = System.currentTimeMillis();
                    val = value;
                }
            }

            @Override
            public void onLoaded() {
                progress.setVisible(false);

                sendMessage(new SyncInfoMessage(getString("info_loaded")));
            }

            @Override
            public void onSyncing() {
                progress.setVisible(true);
                progress.value(0);

                sendMessage(new SyncInfoMessage(getString("info_syncing")));
            }

            @Override
            public void onSync(Integer value) {
                if (System.currentTimeMillis() - time > 100 && value != val){
                    progress.value(value);

                    sendMessage(new SyncMessage());

                    time = System.currentTimeMillis();
                    val = value;
                }
            }

            @Override
            public void onSynced() {
                progress.setVisible(false);

                sendMessage(new SyncInfoMessage(getString("info_synced")));
            }

            @Override
            public void onError(Exception e) {
                sendMessage(new SyncErrorMessage(getString("error") + ": " + Exceptions.getCauseMessage(e, true)));
            }
        };
    }

    protected void sendMessage(IWebSocketPushMessage message){
        try {
            WebSocketSettings.Holder.get(getApplication())
                    .getConnectionRegistry()
                    .getConnection(getApplication(), getSession().getId(), new PageIdKey(getPageId()))
                    .sendMessage(message);
        } catch (Exception e) {
            log.error("error sendMessage ", e);
        }
    }

    public Class<T> getDomainClass() {
        return domainClass;
    }

    protected abstract void load();

    protected abstract void sync();

    public ISyncListener getSyncListener() {
        return syncListener;
    }
}
