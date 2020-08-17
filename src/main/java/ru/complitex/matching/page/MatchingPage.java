package ru.complitex.matching.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.component.table.PropertyColumn;
import ru.complitex.common.component.table.Provider;
import ru.complitex.common.component.table.Table;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.mapper.IFilterMapper;
import ru.complitex.common.model.FilterModel;
import ru.complitex.company.entity.Company;
import ru.complitex.domain.component.table.ActionColumn;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.service.AttributeService;
import ru.complitex.domain.util.Domains;
import ru.complitex.eirc.page.base.BasePage;
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

    @Inject
    private AttributeService attributeService;

    private final MatchingModal modal;

    public MatchingPage(Class<T> domainClass) {
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        NotificationPanel notification = new NotificationPanel("notification");
        notification.setOutputMarkupId(true);
        container.add(notification);

        T domain = Domains.newObject(domainClass);

        Provider<Matching> provider = new Provider<>(FilterModel.of(new Matching(domain.getEntityName()))) {
            @Override
            public Long count() {
                return MatchingPage.this.getMatchingCount(getFilter());
            }

            @Override
            public List<Matching> list() {
                return MatchingPage.this.getMatchingList(getFilter());
            }
        };
        provider.setSort(new Sort("id"), SortOrder.DESCENDING);

        List<IColumn<Matching, Sort>> columns = new ArrayList<>();

        columns.add(new PropertyColumn<Matching>("id").setCssClass("id-column"));
        columns.add(newObjectColumn());

        if (isParentVisible()) {
            columns.add(newParentColumn());
        }

        if (isAdditionalParentVisible()) {
            columns.add(newAdditionalParentColumn());
        }

        columns.add(new PropertyColumn<>("name"));

        if (isAdditionalNameVisible()) {
            columns.add(new PropertyColumn<>("additionalName"));
        }

        columns.add(new PropertyColumn<>("number"));

        if (isCodeVisible()) {
            columns.add(new PropertyColumn<>("code"));
        }

        columns.add(new PropertyColumn<>("startDate"));

        columns.add(new MapColumn<>("_company"){
            @Override
            public String text(IModel<Matching> model) {
                return attributeService.getTextValue(Company.ENTITY, model.getObject().getCompanyId(), Company.NAME);
            }
        });

        columns.add(new ActionColumn<>(){
            @Override
            protected void onSearch(Table<Matching> table, AjaxRequestTarget target) {
                table.getFilterModel().getObject().setObject(new Matching(domain.getEntityName()));
                table.getFilterModel().getObject().getMap().clear();

                provider.setSort(new Sort("id"), SortOrder.DESCENDING);

                target.add(container);
            }

            @Override
            protected void onEdit(IModel<Matching> rowModel, AjaxRequestTarget target) {
                modal.edit(rowModel.getObject(), target);
            }
        });

        Form<Matching> form = new Form<>("form");
        container.add(form);

        Table<Matching> table = new Table<>("table", provider, columns, 10, MatchingPage.class.getName()) {
            @Override
            protected Item<Matching> newRowItem(String id, int index, IModel<Matching> model) {
                Item<Matching> item = super.newRowItem(id, index, model);

                item.add(new AjaxEventBehavior("click") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        modal.edit(model.getObject(), target);
                    }
                });

                item.add(new CssClassNameAppender("pointer"));

                return item;
            }
        };
        form.add(table);

        Form<Matching> matchingForm = new Form<>("matchingForm");
        form.add(matchingForm);

        modal = new MatchingModal("modal"){
            @Override
            protected Component newObjectGroup(String componentId) {
                Component component =  MatchingPage.this.newObjectGroup(componentId, getModel());

                return component != null ? component : super.newObjectGroup(componentId);
            }

            @Override
            public Component newParentGroup(String componentId) {
                Component component = MatchingPage.this.newParentGroup(componentId, getModel());

                return component != null ? component : super.newParentGroup(componentId);
            }

            @Override
            public Component newAdditionalParentGroup(String componentId) {
                Component component = MatchingPage.this.newAdditionalParentGroup(componentId, getModel());

                return component != null ? component : super.newAdditionalParentGroup(componentId);
            }


            @Override
            protected boolean isParentVisible() {
                return MatchingPage.this.isParentVisible();
            }


            @Override
            protected boolean isAdditionalParentVisible() {
                return MatchingPage.this.isAdditionalParentVisible();
            }

            @Override
            protected boolean isCodeVisible(){
                return MatchingPage.this.isCodeVisible();
            }

            @Override
            protected boolean isAdditionalNameRequired() {
                return MatchingPage.this.isAdditionalNameRequired();
            }

            @Override
            protected boolean isAdditionalNameVisible() {
                return MatchingPage.this.isAdditionalNameVisible();
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
                modal.edit(new Matching(domain.getEntityName()), target);
            }
        });
    }

    protected IFilterMapper<Matching> getFilterMapper(){
        return matchingMapper;
    }

    protected Long getMatchingCount(Filter<Matching> filter) {
        return getFilterMapper().getCount(filter);
    }

    protected List<Matching> getMatchingList(Filter<Matching> filter) {
        return getFilterMapper().getList(filter);
    }

    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return null;
    }

    protected Component newParentGroup(String componentId, IModel<Matching> model) {
        return null;
    }

    protected Component newAdditionalParentGroup(String componentId, IModel<Matching> model) {
        return null;
    }

    protected IColumn<Matching, Sort> newObjectColumn() {
        return new PropertyColumn<>("objectId");
    }

    protected IColumn<Matching, Sort> newParentColumn() {
        return new PropertyColumn<>("parentId");
    }

    protected IColumn<Matching, Sort> newAdditionalParentColumn() {
        return new PropertyColumn<>("additionalParentId");
    }

    protected boolean isParentVisible() {
        return true;
    }

    protected boolean isAdditionalParentVisible() {
        return false;
    }

    protected boolean isAdditionalNameRequired(){
        return false;
    }

    protected boolean isAdditionalNameVisible(){
        return false;
    }

    protected boolean isCodeVisible(){
        return false;
    }
}
