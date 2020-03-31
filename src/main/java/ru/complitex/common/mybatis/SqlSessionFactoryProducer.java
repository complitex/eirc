package ru.complitex.common.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.cdi.SessionFactoryProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;

/**
 * @author Anatoly A. Ivanov
 * 22.11.2017 15:12
 */
@ApplicationScoped
public class SqlSessionFactoryProducer {
    @Produces
    @SessionFactoryProvider
    public SqlSessionFactory produceFactory() throws IOException {
        return new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
    }
}
