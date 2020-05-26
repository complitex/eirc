package ru.complitex.company.page;

import ru.complitex.company.entity.Company;
import ru.complitex.domain.page.DomainPage;

/**
 * @author Anatoly Ivanov
 * 19.05.2020 22:17
 */
public class CompanyPage extends DomainPage<Company> {
    public CompanyPage() {
        super(Company.class, Company.NAME);
    }
}
