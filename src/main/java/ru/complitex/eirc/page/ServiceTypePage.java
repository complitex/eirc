package ru.complitex.eirc.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.entity.ServiceType;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 20:03
 */
public class ServiceTypePage extends DomainPage<ServiceType> {
    public ServiceTypePage() {
        super(ServiceType.class, ServiceType.NAME);
    }
}
