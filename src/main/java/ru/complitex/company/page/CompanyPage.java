package ru.complitex.company.page;

import org.apache.wicket.Component;
import ru.complitex.company.entity.Company;
import ru.complitex.domain.component.form.DomainAutoComplete;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberAttributeModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.domain.service.EntityService;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 19.05.2020 22:17
 */
public class CompanyPage extends DomainPage<Company> {
    @Inject
    private EntityService entityService;

    public CompanyPage() {
        super(Company.class, Company.NAME);
    }

    @Override
    protected Component newEditComponent(String componentId, Company company, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Company.PARENT){
             return new DomainAutoComplete(componentId, entityService.getEntityAttribute(Company.ENTITY_ID, Company.NAME),
                     NumberAttributeModel.of(company, Company.PARENT));
        }

        return super.newEditComponent(componentId, company, entityAttribute);
    }
}
