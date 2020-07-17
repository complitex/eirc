package ru.complitex.domain.mapper;

import org.mybatis.cdi.Transactional;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.common.util.Ids;
import ru.complitex.domain.entity.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Anatoly A. Ivanov
 * 29.11.2017 17:54
 */
@RequestScoped
public class DomainMapper extends BaseMapper {
    @Inject
    private AttributeMapper attributeMapper;

    @Transactional
    public void insert(Domain<?> domain){
        domain.setId(null);

        if (domain.getObjectId() == null){
            Id id = new Id(domain.getEntityName(), Ids.randomUUID());

            sqlSession().insert("insertId", id);

            domain.setObjectId(id.getObjectId());
        }

        if (domain.getStartDate() == null){
            domain.setStartDate(new Date());
        }

        if (domain.getStatus() == 0){
            domain.setStatus(Status.ACTIVE);
        }

        sqlSession().insert("insertDomain", domain);

        if (domain.getObjectId() < 0) {
            domain.setObjectId(domain.getId());
            sqlSession().update("updateDomainObjectId", domain);
        }

        domain.getAttributes().forEach(a -> {
            if (!a.isEmpty()) {
                a.setId(null);
                a.setEntityName(domain.getEntityName());
                a.setDomainId(domain.getId());
                a.setUserId(domain.getUserId());

                attributeMapper.insertAttribute(a, domain.getStartDate());
            }
        });
    }

    @Transactional
    public void update(Domain<?> domain){
        Date date = new Date();

        Domain<?> dbDomain = getDomain(domain.getEntityName(), domain.getObjectId());

        domain.getAttributes().forEach(a -> {
            a.setEntityName(domain.getEntityName());
            a.setDomainId(domain.getId());
            a.setUserId(domain.getUserId());

            Attribute dbAttribute = dbDomain.getAttribute(a.getEntityAttributeId());

            if (dbAttribute == null){
                attributeMapper.insertAttribute(a, date);
            }else{
                boolean update = !Objects.equals(a.getText(), dbAttribute.getText()) ||
                        !Objects.equals(a.getNumber(), dbAttribute.getNumber()) ||
                        !Objects.equals(a.getDate(), dbAttribute.getDate());

                if (!update){
                    if (a.getValues() != null){
                        if (dbAttribute.getValues() != null) {
                            boolean count = a.getValues().size() == dbAttribute.getValues().size();

                            update =  !count ||
                                    a.getValues().stream().anyMatch(v -> {
                                        if (v.getLocaleId() != 0) {
                                            Value dbValue = dbAttribute.getValue(v.getLocaleId());

                                            return !Objects.equals(v.getText(), dbValue != null ? dbValue.getText() : null);
                                        }else{
                                            return dbAttribute.getValues().stream()
                                                    .noneMatch(dbV -> Objects.equals(v.getText(), dbV.getText()) &&
                                                            Objects.equals(v.getNumber(), dbV.getNumber()));
                                        }
                                    });
                        }else {
                            update = true;
                        }
                    }
                }

                if (update){
                    attributeMapper.archiveAttribute(dbAttribute, date);

                    if (!a.isEmpty()) {
                        attributeMapper.insertAttribute(a, date);
                    }
                }
            }
        });

        dbDomain.getAttributes().forEach(a -> {
            if (domain.getAttribute(a.getEntityAttributeId()) == null){
                attributeMapper.archiveAttribute(a, date);
            }
        });

        sqlSession().update("updateDomain", domain);
    }

    public Boolean hasDomain(String entityName, int entityAttributeId, String text){
        Domain<?> domain = new Domain<>();
        domain.setEntityName(entityName);
        domain.setText(entityAttributeId, text);

        return sqlSession().selectOne("hasDomain", domain);
    }

    public Domain<?> getDomain(String entityName, Long objectId, boolean useDateAttribute, boolean useNumberValue){
        if (objectId == null){
            return null;
        }

        Domain<?> domain = new Domain<>();
        domain.setEntityName(entityName);
        domain.setObjectId(objectId);

        return getDomain(domain);
    }

    public Domain<?> getDomain(String entityName, Long objectId){
        return getDomain(entityName, objectId, false, false);
    }

    public Domain<?> getDomain(String entityName, int entityAttributeId, String text){
        Domain<?> domain = new Domain<>();
        domain.setEntityName(entityName);
        domain.setText(entityAttributeId, text);

        return getDomain(domain);
    }

    public Domain<?> getDomain(String entityName, int entityAttributeId, Long number){
        Domain<?> domain = new Domain<>();
        domain.setEntityName(entityName);
        domain.setNumber(entityAttributeId, number);

        return getDomain(domain);
    }

    private Domain<?> getDomain(Domain<?> domain){
        return sqlSession().selectOne("selectDomain", domain);
    }

    public List<Domain<?>> getDomains(Filter<? extends Domain<?>> filter){
        return sqlSession().selectList("selectDomains", filter);
    }

    public Long getDomainsCount(Filter<? extends Domain<?>> filter){
        return sqlSession().selectOne("selectDomainsCount", filter);
    }

    public Long getDomainObjectId(Domain<?> domain){
        return sqlSession().selectOne("selectDomainObjectId", domain);
    }

    public void delete(Domain<?> domain){
        domain.setStatus(Status.ARCHIVE);

        sqlSession().update("updateDomain", domain);
    }
}
