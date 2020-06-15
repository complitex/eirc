package ru.complitex.domain.mapper;

import org.mybatis.cdi.Transactional;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.common.util.Maps;
import ru.complitex.domain.entity.Attribute;
import ru.complitex.domain.entity.Status;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 30.11.2017 16:17
 */
@RequestScoped
public class AttributeMapper extends BaseMapper {
    @Inject
    private ValueMapper valueMapper;

    @Transactional
    public void insertAttribute(Attribute attribute, Date startDate){
        attribute.setId(null);

        attribute.setStartDate(startDate);

        if (attribute.getStatus() == 0){
            attribute.setStatus(Status.ACTIVE);
        }
        if (attribute.getDate() != null){
            sqlSession().insert("insertAttributeWithDate", attribute);
        }else if (attribute.getNumber() != null || (attribute.getText() != null && !attribute.getText().isEmpty()) ||
                attribute.getValues() != null){
            sqlSession().insert("insertAttribute", attribute);
        }

        if (attribute.getValues() != null){
            attribute.getValues().stream()
                    .filter(v -> v.getText() != null || v.getNumber() != null)
                    .forEach(s -> {
                        s.setEntityName(attribute.getEntityName());
                        s.setAttributeId(attribute.getId());

                        valueMapper.insertValue(s);
                    });
        }
    }

    public void archiveAttribute(Attribute attribute, Date endDate){
        attribute.setEndDate(endDate);

        sqlSession().update("archiveAttribute", attribute);
    }

    public List<Attribute> getHistoryAttributes(String entityName, Long objectId){
        return sqlSession().selectList("selectHistoryAttributes", Maps.of("entityName", entityName,
                "objectId", objectId));
    }

    public List<Attribute> getHistoryAttributes(String entityName, Long objectId, int entityAttributeId){
        return sqlSession().selectList("selectHistoryAttributes", Maps.of("entityName", entityName,
                "objectId", objectId, "entityAttributeId", entityAttributeId));
    }

    public List<Attribute> getHistoryAttributes(Filter<Attribute> filter){
        return sqlSession().selectList("selectHistoryAttributesFilter", filter);
    }

    public Long getHistoryAttributesCount(Filter<Attribute> filter){
        return sqlSession().selectOne("selectHistoryAttributesCountFilter", filter);
    }

    public Long getNumber(String entityName, Long objectId, int entityAttributeId){
        return sqlSession().selectOne("selectAttributeNumber", Maps.of("entityName", entityName,
                "objectId", objectId, "entityAttributeId", entityAttributeId));
    }

    public List<Long> getNumberValues(String entityName, Long objectId, int entityAttributeId){
        return sqlSession().selectList("selectAttributeNumberValues", Maps.of("entityName", entityName,
                "objectId", objectId, "entityAttributeId", entityAttributeId));
    }

    public String getText(String entityName, Long objectId, int entityAttributeId){
        return sqlSession().selectOne("selectAttributeText", Maps.of("entityName", entityName,
                "objectId", objectId, "entityAttributeId", entityAttributeId));
    }

    public String getTextValue(String entityName, Long objectId, int entityAttributeId){
        return sqlSession().selectOne("selectAttributeTextValue", Maps.of("entityName", entityName,
                "objectId", objectId, "entityAttributeId", entityAttributeId));
    }
}
