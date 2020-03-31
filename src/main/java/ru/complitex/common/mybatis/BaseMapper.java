package ru.complitex.common.mybatis;

import org.apache.ibatis.session.SqlSession;
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

    public SqlSession sqlSession() {
        if (sqlSessionManagerRegistry == null){
            NonContextual.of(this).inject(this);
        }

        return sqlSessionManagerRegistry.getManagers().iterator().next();
    }
}
