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


package org.oscarehr.integration.born;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This class will determine which ONAREnhanced forms need to be sent to
 * BORN, and perform the transaction.
 *
 * It will also check for any error files, and email any errors
 * to specified administrator
 *
 *
 * @author Marc Dumontier
 *
 */
public class AR2005BornConnector {

	public AR2005BornConnector() {

	}

	public void updateBorn() throws Exception {

		Connection conn = org.oscarehr.util.DbConnectionFilter.getThreadLocalDbConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from formONAR");

		//AR2005Form2XML ar2005Form2XML = new AR2005Form2XML();
		File file = File.createTempFile("born", ".xml");
		//ar2005Form2XML.generateXML(rs, new FileOutputStream(file));

		BornFtpManager ftpManager = new BornFtpManager();
		String path = file.getPath();
		path = path.substring(0,path.lastIndexOf(File.separator));
		ftpManager.uploadDataToRepository(path, file.getName());

		rs.close();
		st.close();
		conn.close();
	}

}
