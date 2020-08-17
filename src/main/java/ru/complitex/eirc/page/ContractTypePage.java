package ru.complitex.eirc.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.entity.ContractType;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 20:21
 */
public class ContractTypePage extends DomainPage<ContractType> {
    public ContractTypePage() {
        super(ContractType.class, ContractType.NAME);
    }
}
