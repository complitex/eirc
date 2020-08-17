package ru.complitex.eirc.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.entity.PartnerType;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 20:18
 */
public class PartnerTypePage extends DomainPage<PartnerType> {
    public PartnerTypePage() {
        super(PartnerType.class, PartnerType.NAME);
    }
}
