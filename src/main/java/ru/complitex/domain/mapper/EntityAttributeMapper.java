package ru.complitex.domain.mapper;

import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.common.util.Maps;
import ru.complitex.domain.entity.EntityAttribute;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly A. Ivanov
 * 06.12.2017 18:09
 */
@RequestScoped
public class EntityAttributeMapper extends BaseMapper {
    public EntityAttribute getEntityAttribute(int entityId, int entityAttributeId){
        return sqlSession().selectOne("selectEntityAttributeByEntityId", Maps.of("entityId", entityId,
                "entityAttributeId", entityAttributeId));
    }

    public EntityAttribute getEntityAttribute(String entityName, int entityAttributeId){
        return sqlSession().selectOne("selectEntityAttributeByEntityName", Maps.of("entityName", entityName,
                "entityAttributeId", entityAttributeId));
    }
}
