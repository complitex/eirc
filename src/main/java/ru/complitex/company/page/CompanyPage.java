package ru.complitex.company.page;

import org.apache.wicket.Component;
import ru.complitex.company.entity.Company;
import ru.complitex.domain.component.form.DomainPanel;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.page.DomainPage;

import static ru.complitex.domain.model.NumberAttributeModel.of;

/**
 * @author Anatoly Ivanov
 * 19.05.2020 22:17
 */
public class CompanyPage extends DomainPage<Company> {
    public CompanyPage() {
        super(Company.class, Company.NAME);
    }

    @Override
    protected Component newEditComponent(String componentId, Company company, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Company.PARENT){
             return new DomainPanel(componentId, Company.ENTITY_NAME, Company.NAME, of(company, Company.PARENT));
        }

        return super.newEditComponent(componentId, company, entityAttribute);
    }
}
