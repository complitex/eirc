package ru.complitex.domain.mapper;

import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.common.util.Maps;
import ru.complitex.domain.entity.Attribute;
import ru.complitex.domain.entity.Entity;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly A. Ivanov
 * 29.11.2017 17:54
 */
@RequestScoped
public class EntityMapper extends BaseMapper {
    public Entity getEntity(Long id){
        return sqlSession().selectOne("selectEntity", id);
    }

    public Entity getEntity(String entityName){
        return sqlSession().selectOne("selectEntityByName", entityName);
    }

    public String getReferenceEntityName(String entityName, Long entityAttributeId){
        return sqlSession().selectOne("selectReferenceEntityName", Maps.of("entityName", entityName,
                "entityAttributeId", entityAttributeId));
    }

    public String getReferenceEntityName(Attribute attribute){
        return sqlSession().selectOne("selectReferenceEntityName", Maps.of("entityName", attribute.getEntityName(),
                "entityAttributeId", attribute.getEntityAttributeId()));
    }
}
