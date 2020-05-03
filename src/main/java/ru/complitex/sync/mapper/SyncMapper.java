package ru.complitex.sync.mapper;

import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.sync.entity.Sync;

import javax.enterprise.context.RequestScoped;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 20.04.2020 6:08 PM
 */
@RequestScoped
public class SyncMapper extends BaseMapper {
    public void insert(Sync sync){
        sqlSession().insert("insertSync", sync);
    }

    public void updateStatus(Sync sync){
        sqlSession().update("updateSyncStatus", sync);
    }

    public Sync getSync(Long id){
        return sqlSession().selectOne(".selectSync", id);
    }

    public List<Sync> getSyncs(FilterWrapper<Sync> filterWrapper){
        return sqlSession().selectList(".selectSyncList", filterWrapper);
    }

    public Long getSyncsCount(FilterWrapper<Sync> filterWrapper){
        return sqlSession().selectOne(".selectSyncCount", filterWrapper);
    }

    public boolean isExist(Sync domainSync){
        return getSyncsCount(FilterWrapper.of(domainSync)) > 0;
    }

    public void delete(Long id){
        sqlSession().delete(".deleteSync", id);
    }

    public void deleteAll(Long entityId){
        sqlSession().delete(".deleteAllSyncs", entityId);
    }
}
