package ru.complitex.domain.component.form;

import org.apache.wicket.model.IModel;
import ru.complitex.common.component.form.AbstractAutoComplete;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.service.DomainService;
import ru.complitex.domain.service.EntityService;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * @author Anatoly A. Ivanov
 * 22.12.2017 12:45
 */
public class DomainInput extends AbstractAutoComplete<Domain<?>> {
    @Inject
    private EntityService entityService;

    @Inject
    private DomainService domainService;

    private final EntityAttribute entityAttribute;

    private final Domain<?> domain;

    public DomainInput(String id, EntityAttribute entityAttribute, IModel<Long> model) {
        super(id, model);

        this.entityAttribute = entityAttribute;

        domain = new Domain<>(entityAttribute.getEntityName());
    }

    public DomainInput(String id, String entityName, int entityAttributeId, IModel<Long> model) {
        super(id, model);

        entityAttribute = entityService.getEntityAttribute(entityName, entityAttributeId);

        domain = new Domain<>(entityAttribute.getEntityName());
    }

    @Override
    protected String getTextValue(Domain<?> object) {
        return domainService.getTextValue(entityAttribute.getEntityName(), object.getObjectId(),
                entityAttribute.getEntityAttributeId());
    }

    @Override
    protected Iterator<Domain<?>> getChoices(String input) {
        domain.setText(entityAttribute.getEntityAttributeId(), input);

        return domainService.getDomains(FilterWrapper.of(domain).setFilter("search").limit(10L)).iterator();
    }

    @Override
    protected Long getId(Domain<?> object) {
        return object.getObjectId();
    }

    @Override
    protected Domain<?> getObject(Long id) {
        return domainService.getDomain(entityAttribute.getEntityName(), id);
    }
}
