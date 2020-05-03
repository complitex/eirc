package ru.complitex.common.mapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.wicket.cdi.NonContextual;
import org.mybatis.cdi.SqlSessionManagerRegistry;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Anatoly A. Ivanov
 * 07.05.2018 0:18
 */
public abstract class BaseMapper implements Serializable {
    @Inject
    private transient SqlSessionManagerRegistry sqlSessionManagerRegistry;

    @Inject
    @Named("local")
    private transient SqlSessionFactory sqlSessionFactory;

    public SqlSession sqlSession() {
        if (sqlSessionManagerRegistry == null || sqlSessionFactory == null){
            NonContextual.of(this).inject(this);
        }

        return sqlSessionManagerRegistry.getManager(sqlSessionFactory);
    }
}
