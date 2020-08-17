package ru.complitex.eirc.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.entity.Partner;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 20:19
 */
public class PartnerPage extends DomainPage<Partner> {
    public PartnerPage() {
        super(Partner.class, Partner.NAME);
    }
}
