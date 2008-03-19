package org.oscarehr.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.BeanFactory;

/**
 * This class holds utilities used to work with spring.
 * The main usage is probably the beanFactory singleton.
 */
public class SpringUtils {
    /**
     * This variable is populated by one of the context listeners.
     */
    public static BeanFactory beanFactory = null;

    public static Object getBean(String beanName)
    {
        return(beanFactory.getBean(beanName));
    }
    
    /**
     * This method should only be called by DbConnectionFilter, everyone else should use that to obtain a connection. 
     */
    protected static Connection getDbConnection() throws SQLException {
        BasicDataSource ds = (BasicDataSource)SpringUtils.getBean("dataSource");
        Connection c=ds.getConnection();
        c.setAutoCommit(true);
        return(c);
    }
}
