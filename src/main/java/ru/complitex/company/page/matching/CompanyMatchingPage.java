package ru.complitex.company.page.matching;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Sort;
import ru.complitex.common.mapper.IFilterMapper;
import ru.complitex.company.entity.Company;
import ru.complitex.company.mapper.matching.CompanyMatchingMapper;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.mapper.AttributeMapper;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 13.08.2020 23:05
 */
public class CompanyMatchingPage extends MatchingPage<Company> {
    @Inject
    private CompanyMatchingMapper companyMatchingMapper;

    @Inject
    private AttributeMapper attributeMapper;

    public CompanyMatchingPage() {
        super(Company.class);
    }

    @Override
    protected IFilterMapper<Matching> getFilterMapper() {
        return companyMatchingMapper;
    }

    @Override
    protected Component newObjectGroup(String componentId, IModel<Matching> model) {
        return new DomainGroup(componentId, new ResourceModel("company"),
                Company.ENTITY, PropertyModel.of(model, "objectId"), Company.NAME)
                .setRequired(true);
    }

    @Override
    protected Component newParentGroup(String componentId, IModel<Matching> model) {
        return new DomainGroup(componentId, new ResourceModel("parentCompany"),
                Company.ENTITY, PropertyModel.of(model, "parentId"), Company.NAME);
    }

    @Override
    protected IColumn<Matching, Sort> newObjectColumn() {
        return new MapColumn<>("company") {
            @Override
            public String text(IModel<Matching> model) {
                return attributeMapper.getTextValue(Company.ENTITY, model.getObject().getObjectId(), Company.NAME);
            }
        };
    }

    @Override
    protected IColumn<Matching, Sort> newParentColumn() {
        return new MapColumn<>("parentCompany") {
            @Override
            public String text(IModel<Matching> model) {
                return attributeMapper.getTextValue(Company.ENTITY, model.getObject().getParentId(), Company.NAME);
            }
        };
    }

    @Override
    protected boolean isAdditionalNameVisible() {
        return true;
    }

    @Override
    protected boolean isCodeVisible() {
        return true;
    }
}

