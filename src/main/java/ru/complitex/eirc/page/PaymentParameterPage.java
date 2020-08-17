package ru.complitex.eirc.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.entity.PaymentParameter;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 20:09
 */
public class PaymentParameterPage extends DomainPage<PaymentParameter> {
    public PaymentParameterPage() {
        super(PaymentParameter.class, PaymentParameter.NAME);
    }
}
