package ru.complitex.domain.service;

import ru.complitex.domain.mapper.AttributeMapper;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 07.08.2020 18:51
 */
@RequestScoped
public class AttributeService {
    @Inject
    private AttributeMapper attributeMapper;

    public String getTextValue(String entityName, Long objectId, int entityAttributeId){
        if (objectId == null){
            return "";
        }

        return attributeMapper.getTextValue(entityName, objectId, entityAttributeId);
    }

    public String getTextValue(String refEntityName, Long refObjectId, int refEntityAttributeId, String entityName, int entityAttributeId){
        if (refObjectId == null){
            return "";
        }

        return attributeMapper.getTextValue(entityName, attributeMapper.getNumber(refEntityName, refObjectId, refEntityAttributeId) , entityAttributeId);
    }
}
