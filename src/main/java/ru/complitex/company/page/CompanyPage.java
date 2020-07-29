package ru.complitex.company.page;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.common.entity.Sort;
import ru.complitex.company.entity.Company;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;

import java.util.List;


/**
 * @author Anatoly Ivanov
 * 19.05.2020 22:17
 */
public class CompanyPage extends DomainPage<Company> {
    public CompanyPage() {
        super(Company.class);
    }

    @Override
    protected void addColumn(EntityAttribute entityAttribute, List<IColumn<Company, Sort>> columns) {
        if (entityAttribute.getEntityAttributeId() != Company.CODE) {
            super.addColumn(entityAttribute, columns);
        }
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{Company.NAME};
    }

    @Override
    protected Component newGroup(String groupId, IModel<Company> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Company.PARENT){
             return new DomainGroup(groupId, new ResourceModel("_parent"), Company.ENTITY, Company.NAME,
                     NumberModel.of(domainModel, Company.PARENT));
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
