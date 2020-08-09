package ru.complitex.domain.component.form;

import org.apache.wicket.model.IModel;
import ru.complitex.common.component.form.AbstractAutoComplete;
import ru.complitex.common.entity.Filter;
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

    private final Filter<Domain<?>> filter;

    public DomainInput(String id, EntityAttribute entityAttribute, IModel<Long> model) {
        super(id, model);

        this.entityAttribute = entityAttribute;

        filter = newFilter();
    }

    public DomainInput(String id, String entityName, IModel<Long> model, int entityAttributeId) {
        super(id, model);

        entityAttribute = entityService.getEntityAttribute(entityName, entityAttributeId);

        filter = newFilter();
    }

    protected Filter<Domain<?>> newFilter(){
        Filter<Domain<?>> filter = Filter.of(new Domain<>(entityAttribute.getEntityName()));

        filter.limit(10L);

        return filter;
    }

    protected void onFilter(Filter<Domain<?>> filter){

    }

    @Override
    protected String getTextValue(Domain<?> object) {
        return domainService.getTextValue(entityAttribute.getEntityName(), object.getObjectId(),
                entityAttribute.getEntityAttributeId());
    }

    @Override
    protected Iterator<Domain<?>> getChoices(String input) {
        filter.getObject().setText(entityAttribute.getEntityAttributeId(), input);

        onFilter(filter);

        return domainService.getDomains(filter).iterator();
    }

    @Override
    protected Long getId(Domain<?> object) {
        return object != null ? object.getObjectId() : null;
    }

    @Override
    protected Domain<?> getObject(Long id) {
        return domainService.getDomain(entityAttribute.getEntityName(), id);
    }
}
