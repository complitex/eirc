package ru.complitex.company.page;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.company.entity.Company;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;


/**
 * @author Anatoly Ivanov
 * 19.05.2020 22:17
 */
public class CompanyPage extends DomainPage<Company> {
    public CompanyPage() {
        super(Company.class);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{Company.NAME};
    }

    @Override
    protected Component newGroup(String groupId, IModel<Company> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Company.PARENT){
             return new DomainGroup(groupId, new ResourceModel("_parent"), Company.ENTITY, NumberModel.of(domainModel, Company.PARENT), Company.NAME);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
