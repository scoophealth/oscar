package org.caisi.PMmodule.dao;

import java.io.FileInputStream;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Base class for DAO TestCases.
 *
 * @author Marc Dumontier marc@mdumontier.com
 */
public abstract class BaseDAOTestCase extends DatabaseTestCase {
	
    protected final Log log = LogFactory.getLog(getClass());
    protected ApplicationContext ctx = null;
    protected SessionFactory sf;
       
    /* setup spring */
    public BaseDAOTestCase() {
        String[] paths = {"/WEB-INF/applicationContext-test.xml"};
    	ctx = new ClassPathXmlApplicationContext(paths);
    }
    
    /* setup hibernate sessions and dbunit */
	protected void setUp() throws Exception {
		super.setUp();
		sf = (SessionFactory) ctx.getBean("sessionFactory");

        Session s = sf.openSession();
        TransactionSynchronizationManager
        .bindResource(sf, new SessionHolder(s));
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();

		SessionHolder holder = (SessionHolder)
        TransactionSynchronizationManager.getResource(sf);
        Session s = holder.getSession();
        s.flush();
        TransactionSynchronizationManager.unbindResource(sf);
        SessionFactoryUtils.releaseSession(s, sf);
	}
	
	protected IDatabaseConnection getConnection() 
	  throws Exception {
		DataSource dataSource = (DataSource)ctx.getBean("dataSource");
    	return new DatabaseConnection(dataSource.getConnection());
	   
	}
	
	protected IDataSet getDataSet() throws Exception {
		   return new FlatXmlDataSet(
		      new FileInputStream(getTestDataFile()));
		}
	
	protected DatabaseOperation getSetUpOperation() 
	  throws Exception {
	   return DatabaseOperation.CLEAN_INSERT;
	 }

	protected DatabaseOperation getTearDownOperation() 
	  throws Exception {
	   return DatabaseOperation.DELETE_ALL;
	}
	
	/**
	 * This should return a file in the classpath containing the 
	 * dbunit test data
	 * 
	 * @return Filename
	 */
	abstract protected String getTestDataFile();

}
