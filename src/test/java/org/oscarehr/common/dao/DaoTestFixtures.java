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

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.oscarehr.common.dao.utils.ConfigUtils;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class DaoTestFixtures
{
	@BeforeClass
	public static void classSetUp() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		Logger.getRootLogger().setLevel(Level.WARN);
		
		long start = System.currentTimeMillis();
		if(!SchemaUtils.inited) {
			MiscUtils.getLogger().info("dropAndRecreateDatabase");
			SchemaUtils.dropAndRecreateDatabase();
		}
		long end = System.currentTimeMillis();
		long secsTaken = (end-start)/1000;
		if(secsTaken > 30) {
			MiscUtils.getLogger().info("Setting up db took " + secsTaken + " seconds.");
		}

		start = System.currentTimeMillis();
		if(SpringUtils.beanFactory==null) {
			oscar.OscarProperties p = oscar.OscarProperties.getInstance();
			p.setProperty("db_name", ConfigUtils.getProperty("db_schema"));
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
		MiscUtils.getLogger().info("Setting up spring took " + secsTaken + " seconds.");

	}

}
