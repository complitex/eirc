package ru.complitex.eirc.producer;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.cdi.SessionFactoryProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.IOException;

/**
 * @author Anatoly Ivanov
 * 21.04.2020 22:33
 */
@ApplicationScoped
public class SqlSessionFactoryProducer {
    @Produces
    @SessionFactoryProvider
    @Named("local")
    public SqlSessionFactory produceLocalFactory() throws IOException {
        return new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"), "local");
    }

    @Produces
    @SessionFactoryProvider
    @Named("remote")
    public SqlSessionFactory produceRemoteFactory() throws IOException {
        return new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"), "remote");
    }
}
