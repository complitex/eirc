package ru.complitex.matching.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import ru.complitex.common.component.table.KeyColumn;
import ru.complitex.common.component.table.Provider;
import ru.complitex.common.component.table.Table;
import ru.complitex.common.component.table.TableForm;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.company.entity.Company;
import ru.complitex.domain.component.table.ActionColumn;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.service.DomainService;
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

    @Inject
    private DomainService domainService;

    private Table<Matching> table;

    private MatchingModal modal;

    public MatchingPage(Class<T> domainClass) {
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        NotificationPanel notification = new NotificationPanel("notification");
        notification.setOutputMarkupId(true);
        container.add(notification);

        T domain = Domains.newObject(domainClass);

        Filter<Matching> filter = Filter.of(new Matching(domain.getEntityName()));

        Provider<Matching> provider = new Provider<Matching>(filter) {
            @Override
            protected List<Matching> data() {
                return matchingMapper.getMatchingList(filter);
            }

            @Override
            public long size() {
                return matchingMapper.getMatchingListCount(filter);
            }
        };

        List<IColumn<Matching, Sort>> columns = new ArrayList<>();

        columns.add(new KeyColumn<Matching>("id").setCssClass("id-column"));
        columns.add(newObjectId("objectId"));

        if (isParentIdVisible()) {
            columns.add(newParentId("parentId"));
        }

        if (isAdditionalParentIdVisible()) {
            columns.add(new KeyColumn<>("additionalParentId"));
        }

        columns.add(new KeyColumn<>("code"));

        if (isAdditionalCodeVisible()) {
            columns.add(new KeyColumn<>("additionalCode"));
        }

        columns.add(new KeyColumn<>("name"));

        if (isAdditionalNameVisible()) {
            columns.add(new KeyColumn<>("additionalName"));
        }

        columns.add(new KeyColumn<>("startDate"));

        columns.add(new KeyColumn<>("companyId"){
            @Override
            protected IModel<?> newItemModel(IModel<Matching> rowModel) {
                Long companyId = rowModel.getObject().getCompanyId();

                return Model.of(companyId != null ? domainService.getDomain(Company.class, companyId).getName() : "");
            }
        });

        columns.add(new ActionColumn<>(){
            @Override
            protected void onSearch(AjaxRequestTarget target) {
                table.update(target);
            }

            @Override
            protected void onEdit(IModel<Matching> rowModel, AjaxRequestTarget target) {
                modal.edit(rowModel.getObject(), target);
            }
        });

        TableForm<Matching> form = new TableForm<>("form", filter);
        container.add(form);

        table = new Table<>("table", provider, columns, form, 10, MatchingPage.class.getName()){
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
                Component component = MatchingPage.this.newParentId(componentId, getModel());

                return component != null ? component : super.newParentId(componentId);
            }

            @Override
            protected boolean isAdditionalParentIdVisible() {
                return MatchingPage.this.isAdditionalParentIdVisible();
            }


            @Override
            protected boolean isAdditionalCodeVisible(){
                return MatchingPage.this.isAdditionalCodeVisible();
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

    protected KeyColumn<Matching> newObjectId(String columnKey) {
        return new KeyColumn<>(columnKey);
    }

    protected Component newObjectId(String componentId, IModel<Matching> model) {
        return null;
    }

    protected boolean isParentIdVisible() {
        return true;
    }

    protected KeyColumn<Matching> newParentId(String columnKey) {
        return new KeyColumn<>(columnKey);
    }

    protected Component newParentId(String componentId, IModel<Matching> model) {
        return null;
    }

    protected boolean isAdditionalParentIdVisible() {
        return true;
    }

    protected KeyColumn<Matching> newAdditionalParentId(String columnKey) {
        return new KeyColumn<>(columnKey);
    }

    protected boolean isAdditionalCodeVisible(){
        return true;
    }

    protected boolean isAdditionalNameVisible(){
        return false;
    }

}
