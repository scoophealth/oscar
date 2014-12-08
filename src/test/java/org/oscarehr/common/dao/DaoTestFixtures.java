/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster University
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package org.oscarehr.common.dao;

import static junit.framework.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.utils.ConfigUtils;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class DaoTestFixtures
{
	private static Logger logger=MiscUtils.getLogger();
	
	private static LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoAsCurrentClassAndMethod();
	
	public static LoggedInInfo getLoggedInInfo()
	{
		return(loggedInInfo);
	}
	
	public void beforeForInnoDB() throws Exception {
		SchemaUtils.dropTable("IntegratorConsent","HnrDataValidation","ClientLink","IntegratorConsentComplexExitInterview",
				"DigitalSignature","appointment","admission" ,"program","demographic");
	}
	
	@BeforeClass
	public static void classSetUp() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		
		
		Logger.getRootLogger().setLevel(Level.WARN);
		
		long start = System.currentTimeMillis();
		if(!SchemaUtils.inited) {
			logger.info("dropAndRecreateDatabase");
			SchemaUtils.dropAndRecreateDatabase();
		}
		long end = System.currentTimeMillis();
		long secsTaken = (end-start)/1000;
		if(secsTaken > 30) {
			logger.info("Setting up db took " + secsTaken + " seconds.");
		}

		start = System.currentTimeMillis();
		if(SpringUtils.beanFactory==null) {
			oscar.OscarProperties p = oscar.OscarProperties.getInstance();
			p.setProperty("db_name", ConfigUtils.getProperty("db_schema") + ConfigUtils.getProperty("db_schema_properties"));
			p.setProperty("db_username", ConfigUtils.getProperty("db_user"));
			p.setProperty("db_password", ConfigUtils.getProperty("db_password"));
			p.setProperty("db_uri", ConfigUtils.getProperty("db_url_prefix"));
			p.setProperty("db_driver", ConfigUtils.getProperty("db_driver"));
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setConfigLocations(new String[]{"/applicationContext.xml","/applicationContextBORN.xml"});
			context.refresh();
			SpringUtils.beanFactory = context;
		}
		end = System.currentTimeMillis();
		secsTaken = (end-start)/1000;
		logger.info("Setting up spring took " + secsTaken + " seconds.");

	}
	
	@Test
	public void doSimpleExceptionTest() {
		List<String> excludeList = getSimpleExceptionTestExcludes();
		
		String daoClassName = this.getClass().getName().replaceAll("Test$", "");
	
		try {
			Class clazz = Class.forName(daoClassName);
			
			Object daoObject = null;
			
			
			for(Field f:this.getClass().getDeclaredFields()) {
				if(f.getType().equals(clazz)) {
					daoObject = f.get(this);
					break;
				}
			}
			
			if(daoObject == null) {
				logger.warn("Unable to find dao field of type " + clazz.getName());
				return;
			}
			
			for(Method m:clazz.getMethods()) {
				if(excludeList.contains(m.getName())) {
					continue;
				}
				if(m.getParameterTypes().length == 0) {
					//logger.info("invoking " + m.getName());
					try {
						m.invoke(daoObject);
					}catch(Exception e) {
						logger.error("error",e);
						fail(e.getMessage());
					}
				} else {
					boolean invoke=true;
					Object[] params = new Object[m.getParameterTypes().length];
					for(int x=0;x<m.getParameterTypes().length;x++) {
						Class c = m.getParameterTypes()[x];
						if(!(c==int.class) && !(c == String.class) && !(c == Integer.class) 
								&& !(c == java.util.Date.class) && !(c == Boolean.class) && !(c == boolean.class)
								&& !(c == Long.class) && !(c == long.class)
								&& !(c == Double.class) && !(c == double.class)
								&& !(c == Float.class) && !(c == float.class)) {
							logger.info("can't handle " + c);
							invoke=false;
							break;
						}
						if(c == int.class || c == Integer.class ) {
							params[x] = 1;
						}
						if(c == long.class || c == Long.class ) {
							params[x] = 1L;
						}
						if(c == Double.class || c == double.class) {
							params[x] = (double)1.0;
						}
						if(c == Float.class || c == float.class) {
							params[x] = (float)1.0;
						}
						if(c == String.class) {
							params[x] = "test";
						}
						if(c == Date.class) {
							params[x] = new Date();
						}
						if(c == Boolean.class || c == boolean.class) {
							params[x] = true;
						}
					}
					if(invoke) {
						//logger.info("invoking " + m.getName());
						try {
							m.invoke(daoObject, params);
						}catch(Exception e) {
							logger.error("error on method " + m.getName(),e);
							fail("error on method " + m.getName() + "(" + Arrays.toString(m.getParameterTypes()) + ") with exception message " + e.getMessage());
						}
					} else {
						logger.info("skipping " + m.getName());
					}
				}
			}
		} catch(ClassNotFoundException e) {
			logger.warn("Unable to find class of type " + daoClassName);
		} catch(Exception e) {
			logger.error("error in class : "+daoClassName,e);
		}
	}

	/**
	 * Gets list of all methods that should be ignored for simple exception test on the DAO. 
	 * 
	 * @return
	 * 		Returns a list of methods to be skipped during exception testing.
	 */
	protected List<String> getSimpleExceptionTestExcludes() {
	    String[] excludes = {"notify","notifyAll","remove","persist","merge","refresh","saveEntity","wait","equals",
				"toString","hashCode","getClass","getModelClass","find","getCountAll","findAll","runNativeQuery","save","removeAll"};
		List<String> excludeList = new ArrayList<String>();
		excludeList.addAll(Arrays.asList(excludes));
	    return excludeList;
    }

}
