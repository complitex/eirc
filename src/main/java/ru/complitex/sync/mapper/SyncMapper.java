package ru.complitex.sync.mapper;

import ru.complitex.common.entity.Filter;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.entity.SyncStatus;

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
        return sqlSession().selectOne("selectSync", id);
    }

    public List<Sync> getSyncs(Filter<Sync> filter){
        return sqlSession().selectList("selectSyncs", filter);
    }

    public Long getSyncsCount(Filter<Sync> filter){
        return sqlSession().selectOne("selectSyncsCount", filter);
    }

    public boolean isExist(Sync domainSync){
        return getSyncsCount(Filter.of(domainSync)) > 0;
    }

    public void delete(Long id){
        sqlSession().delete("deleteSync", id);
    }

    public void deleteAll(int entityId){
        sqlSession().delete("deleteAllSyncs", entityId);
    }

    public Sync getSync(int entityId, Long parentId){
        List<Sync> syncs = getSyncs(Filter.of(new Sync(entityId, SyncStatus.SYNCHRONIZED, parentId)));

        if (syncs.isEmpty()){
            throw new RuntimeException("sync not found ");
        }

        return syncs.get(0);
    }
}
