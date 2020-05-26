package ru.complitex.matching.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.ui.datatable.DataColumn;
import ru.complitex.common.ui.datatable.DataForm;
import ru.complitex.common.ui.datatable.DataProvider;
import ru.complitex.common.ui.datatable.DataTable;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.util.Domains;
import ru.complitex.eirc.page.BasePage;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 16:39
 */
public class MatchingPage<T extends Domain<T>> extends BasePage {
    @Inject
    private MatchingMapper matchingMapper;

    private MatchingModal modal;

    public MatchingPage(Class<T> domainClass) {
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        NotificationPanel notification = new NotificationPanel("notification");
        notification.setOutputMarkupId(true);
        container.add(notification);

        T domain = Domains.newObject(domainClass);

        FilterWrapper<Matching> filterWrapper = FilterWrapper.of(new Matching(domain.getEntityName()));

        DataProvider<Matching> provider = new DataProvider<Matching>(filterWrapper) {
            @Override
            protected List<Matching> data() {
                return matchingMapper.getMatchingList(filterWrapper);
            }

            @Override
            public long size() {
                return matchingMapper.getMatchingListCount(filterWrapper);
            }
        };

        List<IColumn<Matching, Sort>> columns = new ArrayList<>();

        columns.add(new DataColumn<Matching>("id").setCssClass("domain-id-column"));
        columns.add(new DataColumn<>("objectId"));

        if (isParentIdVisible()) {
            columns.add(new DataColumn<>("parentId"));
        }

        if (isAdditionalParentIdVisible()) {
            columns.add(new DataColumn<>("additionalParentId"));
        }

        columns.add(new DataColumn<>("externalId"));
        columns.add(new DataColumn<>("additionalExternalId"));
        columns.add(new DataColumn<>("name"));
        columns.add(new DataColumn<>("additionalName"));
        columns.add(new DataColumn<>("startDate"));
        columns.add(new DataColumn<>("endDate"));
        columns.add(new DataColumn<>("companyId"));
        columns.add(new DataColumn<>("userCompanyId"));

        DataForm<Matching> form = new DataForm<>("form", filterWrapper);
        container.add(form);

        DataTable<Matching> table = new DataTable<>("table", provider, columns, form, 10, MatchingPage.class.getName());
        form.add(table);

        Form<Matching> matchingForm = new Form<>("matchingForm");
        form.add(matchingForm);

        modal = new MatchingModal("modal"){
            @Override
            protected Component newObjectId(String componentId) {
                Component component =  MatchingPage.this.newObjectId(componentId, getModel());

                return component != null ? component : super.newObjectId(componentId);
            }

            @Override
            protected boolean isParentIdVisible() {
                return MatchingPage.this.isParentIdVisible();
            }

            @Override
            public Component newParentId(String componentId) {
                Component component = MatchingPage.this.newParentId(componentId);

                return component != null ? component : super.newParentId(componentId);
            }

            @Override
            protected boolean isAdditionalParentIdVisible() {
                return MatchingPage.this.isAdditionalParentIdVisible();
            }

            @Override
            protected void onSave(AjaxRequestTarget target) {
                target.add(container);
            }
        };
        matchingForm.add(modal);

        form.add(new BootstrapAjaxLink<>("create", null, Buttons.Type.Outline_Primary, new StringResourceModel("create", this)) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modal.open(target, new Matching(domain.getEntityName()));
            }
        });
    }

    protected Component newObjectId(String componentId, IModel<Matching> model) {
        return null;
    }

    protected boolean isParentIdVisible() {
        return true;
    }

    protected Component newParentId(String componentId) {
        return null;
    }

    protected boolean isAdditionalParentIdVisible() {
        return true;
    }

}
