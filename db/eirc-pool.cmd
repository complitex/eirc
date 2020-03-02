@ECHO off

SET GLASSFISH_ASADMIN=C:\payara5\bin\asadmin.bat --port 14848

ECHO ---------------------------------------------------
ECHO Local database and Realm
ECHO ---------------------------------------------------
ECHO.
ECHO Register the JDBC connection pool
call %GLASSFISH_ASADMIN% create-jdbc-connection-pool ^
    --datasourceclassname="com.mysql.cj.jdbc.MysqlConnectionPoolDataSource" ^
    --restype="javax.sql.ConnectionPoolDataSource" ^
    --property="url=jdbc\:mysql\://g1\:3306/eirc_db:user=eirc:password=eirc:characterResultSets=utf8:characterEncoding=utf8:useUnicode=true:connectionCollation=utf8_unicode_ci:autoReconnect=true" eircPool

ECHO.
ECHO Create a JDBC resource with the specified JNDI name
call %GLASSFISH_ASADMIN% create-jdbc-resource --connectionpoolid eircPool jdbc/eircResource

ECHO.
ECHO Add the named authentication realm
call %GLASSFISH_ASADMIN% create-auth-realm ^
    --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm ^
    --property jaas-context=jdbcRealm:datasource-jndi=jdbc/eircResource:user-table=user:user-name-column=login:password-column=password:group-table=usergroup:group-name-column=group_name:charset=UTF-8:digest-algorithm=SHA-256 eircRealm
 
ECHO.
ECHO ---------------------------------------------------
ECHO Remote database
ECHO ---------------------------------------------------
ECHO.
ECHO Register the JDBC connection pool
call %GLASSFISH_ASADMIN% create-jdbc-connection-pool ^
    --driverclassname oracle.jdbc.OracleDriver ^
    --restype java.sql.Driver --property url=jdbc\:oracle\:thin\:@10.50.4.15\:1521\:cnreal:user=export:password=export:characterResultSets=utf8:characterEncoding=utf8:useUnicode=true:connectionCollation=utf8_unicode_ci:autoReconnect=true eircRemotePool

ECHO.
ECHO Create a JDBC resource with the specified JNDI name
call %GLASSFISH_ASADMIN% create-jdbc-resource --connectionpoolid eircRemotePool jdbc/eircRemoteResource