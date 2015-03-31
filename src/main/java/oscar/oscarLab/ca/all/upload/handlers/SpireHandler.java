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
/*
 * SpireHandler.java
 *
 * Created on May 23, 2007, 4:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.OscarAuditLogger;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

public class SpireHandler implements MessageHandler {
	
    Logger logger = Logger.getLogger(SpireHandler.class);
	Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao)SpringUtils.getBean("hl7TextInfoDao");
	
	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {

		int i = 0;
		try {
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {
				String msg = messages.get(i);
				if(isDuplicate(loggedInInfo, msg)) {
					return ("success");
				}
				MessageUploader.routeReport(loggedInInfo, serviceName, "Spire", msg, fileId);

			}
		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not upload message: ", e);
			MiscUtils.getLogger().error("Error", e);
			return null;
		}
		return ("success");

	}
	
	private boolean isDuplicate(LoggedInInfo loggedInInfo, String msg) {
		//OLIS requirements - need to see if this is a duplicate
		oscar.oscarLab.ca.all.parsers.MessageHandler h = Factory.getHandler("Spire", msg);
		//if final
		if(h.getOrderStatus().equals("CM")) {
			String acc = h.getAccessionNum().substring(3);
			//do we have this?
			List<Hl7TextInfo> dupResults = hl7TextInfoDao.searchByAccessionNumber(acc);
			for(Hl7TextInfo dupResult:dupResults) {
				if(("Spire"+dupResult.getAccessionNumber()).equals(acc)) {
					//if(h.getHealthNum().equals(dupResult.getHealthNumber())) {
						OscarAuditLogger.getInstance().log(loggedInInfo, "Lab", "Skip", "Duplicate lab skipped - accession " + acc + "\n" + msg);
						return true;
					//}					
					
				}
				if(dupResult.getAccessionNumber().indexOf("-")!= -1) {
					if(dupResult.getAccessionNumber().substring(0,dupResult.getAccessionNumber().indexOf("-")).equals(acc) ) {
						//olis match								
						//if(h.getHealthNum().equals(dupResult.getHealthNumber())) {
						OscarAuditLogger.getInstance().log(loggedInInfo, "Lab", "Skip", "Duplicate lab skipped - accession " + acc + "\n" + msg);
						return true;
						//}
					}
				}
			}		
		}
		return false;	
	}

}
