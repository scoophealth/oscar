package org.oscarehr.common.dao;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.oscarehr.common.dao.utils.ConfigUtils;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DaoTestFixtures
{	
	
	@BeforeClass
	public static void classSetUp() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchAlgorithmException
	{
		long start = System.currentTimeMillis();
		SchemaUtils.dropAndRecreateDatabase();
		long end = System.currentTimeMillis();		
		long secsTaken = (end-start)/1000;		
		MiscUtils.getLogger().info("Setting up db took " + secsTaken + " seconds.");
				
		start = System.currentTimeMillis();
		oscar.OscarProperties p = oscar.OscarProperties.getInstance();
		p.loader(ClassLoader.getSystemClassLoader().getResourceAsStream("oscar_mcmaster.properties"));
		p.setProperty("db_name", ConfigUtils.getProperty("db_schema"));
		p.setProperty("db_username", ConfigUtils.getProperty("db_user"));
		p.setProperty("db_password", ConfigUtils.getProperty("db_password"));
		p.setProperty("db_uri", ConfigUtils.getProperty("db_url_prefix"));
		p.setProperty("db_driver", ConfigUtils.getProperty("db_driver"));
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");		
		SpringUtils.beanFactory = context;		
		end = System.currentTimeMillis();		
		secsTaken = (end-start)/1000;		
		MiscUtils.getLogger().info("Setting up spring took " + secsTaken + " seconds.");
		
	}
	
}