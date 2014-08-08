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


package org.oscarehr.integration.hl7.handlers.upload;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;

public class PhsStarHandler implements MessageHandler {

	Logger logger = MiscUtils.getLogger();
	
	@Override
	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {
		logger.info("received PHS/STAR message");

		org.oscarehr.integration.hl7.handlers.PhsStarHandler handler = new org.oscarehr.integration.hl7.handlers.PhsStarHandler();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line=in.readLine())!=null) {
				sb.append(line);
				sb.append("\n");			
			}
			handler.init(sb.toString());
		
			return ("success");
		}catch(Exception e) {
	        logger.error("Unexpected error.", e);
	        MessageUploader.clean(fileId);
	        throw(new RuntimeException(e));
		}
		
	}

}
