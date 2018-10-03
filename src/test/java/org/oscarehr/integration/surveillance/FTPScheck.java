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
package org.oscarehr.integration.surveillance;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.apache.log4j.Logger;

import org.junit.Test;

import org.oscarehr.util.MiscUtils;

public class FTPScheck  {
	private Logger logger = MiscUtils.getLogger();
	
	@Test
	public void testConnector() throws Exception {
		
		String protocol = "SSL";
		int port = 21;
		String domain = "localhost";
		String username = "oscar";
		String password = "******";
		
		FTPSClient ftps = new FTPSClient(protocol);//,false);
		ftps.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		
		
		ftps.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
        ftps.setDefaultPort(port);
        ftps.connect(domain);
        ftps.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
       
        boolean loggedIn  = ftps.login(username, password);
		if(loggedIn) {
			
			ftps.execPBSZ(0);  // Set protection buffer size
			ftps.execPROT("P"); // Set data channel protection to private
			ftps.enterLocalPassiveMode();
			
	        
	        
	        ByteArrayInputStream is = new ByteArrayInputStream("TEST FILE".getBytes());
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmmss");
			String remoteFilename = "test2"+simpleDateFormat.format(new Date())+".txt";//"\013"+simpleDateFormat.format(data.getCreateDate().getTime())+".csv";
			boolean sent = ftps.storeFile(remoteFilename,is);
			logger.debug("remoteFilename:" +remoteFilename+" sent "+sent);
			assertEquals(sent,true);
			
		}
		ftps.disconnect();
		
		
	}

}
