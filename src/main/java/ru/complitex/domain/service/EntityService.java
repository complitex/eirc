package ru.complitex.domain.service;

import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.entity.Entity;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.mapper.EntityMapper;
import ru.complitex.domain.util.Domains;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 16.05.2018 14:01
 */
@RequestScoped
public class EntityService implements Serializable {
    @Inject
    private EntityMapper entityMapper;

    public Entity getEntity(int id){
        return entityMapper.getEntity(id);
    }

    public Entity getEntity(String entityName){
        return entityMapper.getEntity(entityName);
    }

    public <T extends Domain<T>> Entity getEntity(Class<T> domainClass){
        return entityMapper.getEntity(Domains.getEntityName(domainClass));
    }

    public EntityAttribute getEntityAttribute(int entityId, int entityAttributeId){
        return getEntity(entityId).getEntityAttribute(entityAttributeId);
    }

    public EntityAttribute getEntityAttribute(String entityName, int entityAttributeId){
        return getEntity(entityName).getEntityAttribute(entityAttributeId);
    }

    public EntityAttribute getReferenceEntityAttribute(EntityAttribute entityAttribute){
        return getEntityAttribute(entityAttribute.getReferenceEntityId(), entityAttribute.getReferenceEntityAttributeId());
    }
}
