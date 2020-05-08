package ru.complitex.common.mapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.wicket.cdi.NonContextual;
import org.mybatis.cdi.SqlSessionManagerRegistry;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 07.05.2018 0:18
 */
public abstract class BaseMapper implements Serializable {
    @Inject
    private transient SqlSessionManagerRegistry sqlSessionManagerRegistry;

    private transient SqlSessionManager sqlSessionManager;

    private String environmentId;

    public BaseMapper() {
        environmentId = "local";
    }

    public BaseMapper(String environmentId) {
        this.environmentId = environmentId;
    }

    public SqlSession sqlSession() {
        if (sqlSessionManagerRegistry == null){
            NonContextual.of(this).inject(this);
        }

        if (sqlSessionManager == null){
            sqlSessionManager = sqlSessionManagerRegistry.getManagers().stream()
                    .filter(m -> m.getConfiguration().getEnvironment().getId().equals(environmentId))
                    .findAny()
                    .orElse(null);
        }

        return sqlSessionManager;
    }
}
