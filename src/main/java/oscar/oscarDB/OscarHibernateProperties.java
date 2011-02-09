package oscar.oscarDB;

import java.util.Properties;

import oscar.OscarProperties;
;

public class OscarHibernateProperties extends Properties {
	public OscarHibernateProperties(){
		//get db config from oscar properties
		Properties opr=OscarProperties.getInstance();
		setProperty("hibernate.connection.url",opr.getProperty("db_uri")+OscarProperties.getInstance().getProperty("db_name"));
		setProperty("hibernate.connection.username",OscarProperties.getInstance().getProperty("db_username"));
		setProperty("hibernate.connection.password",OscarProperties.getInstance().getProperty("db_password"));
		setProperty("hibernate.connection.driver_class",OscarProperties.getInstance().getProperty("db_driver"));
		setProperty("hibernate.dialect",opr.getProperty("hibernate.dialect"));
	}
}
