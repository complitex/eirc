package ru.complitex.eirc.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.entity.Contract;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 20:22
 */
public class ContractPage extends DomainPage<Contract> {
    public ContractPage() {
        super(Contract.class, Contract.NAME);
    }
}
