package ru.complitex.eirc.adapter;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.wicket.cdi.NonContextual;
import org.mybatis.cdi.SqlSessionManagerRegistry;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Anatoly Ivanov
 * 21.04.2020 7:36 PM
 */
public abstract class BaseAdapter implements Serializable {
    @Inject
    private transient SqlSessionManagerRegistry sqlSessionManagerRegistry;

    @Inject
    @Named("remote")
    private transient SqlSessionFactory sqlSessionFactory;

    public SqlSession sqlSession() {
        if (sqlSessionManagerRegistry == null || sqlSessionFactory == null){
            NonContextual.of(this).inject(this);
        }

        return sqlSessionManagerRegistry.getManager(sqlSessionFactory);
    }
}
