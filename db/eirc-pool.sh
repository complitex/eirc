#!/bin/sh

GLASSFISH_ASADMIN="asadmin --port 14848"

echo ---------------------------------------------------
echo Local database and Realm
echo ---------------------------------------------------
echo
echo Register the JDBC connection pool
$GLASSFISH_ASADMIN create-jdbc-connection-pool \
  --datasourceclassname="com.mysql.cj.jdbc.MysqlConnectionPoolDataSource" \
  --restype="javax.sql.ConnectionPoolDataSource" \
  --property="url=jdbc\:mysql\://localhost\:3306/eirc_db:user=eirc:password=eirc:characterResultSets=utf8:characterEncoding=utf8:useUnicode=true:connectionCollation=utf8_unicode_ci:autoReconnect=true:useSSL=false:serverTimezone=UTC" eircPool

echo
echo Create a JDBC resource with the specified JNDI name
$GLASSFISH_ASADMIN create-jdbc-resource --connectionpoolid eircPool jdbc/eircResource

echo
echo Add the named authentication realm
$GLASSFISH_ASADMIN create-auth-realm \
  --classname="com.sun.enterprise.security.ee.auth.realm.jdbc.JDBCRealm" \
  --property="jaas-context=jdbcRealm:datasource-jndi=jdbc/eircResource:user-table=user:user-name-column=login:password-column=password:group-table=user_group:group-name-column=group_name:charset=UTF-8:digest-algorithm=SHA-256" eircRealm
 
echo
echo ---------------------------------------------------
echo Remote database
echo ---------------------------------------------------
echo
echo Register the JDBC connection pool
$GLASSFISH_ASADMIN create-jdbc-connection-pool \
  --driverclassname oracle.jdbc.OracleDriver \
  --restype java.sql.Driver --property url=jdbc\:oracle\:thin\:@10.50.4.15\:1521\:cnreal:user=export:password=export:characterResultSets=utf8:characterEncoding=utf8:useUnicode=true:connectionCollation=utf8_unicode_ci:autoReconnect=true eircRemotePool

echo
echo Create a JDBC resource with the specified JNDI name
$GLASSFISH_ASADMIN create-jdbc-resource --connectionpoolid eircRemotePool jdbc/eircRemoteResource