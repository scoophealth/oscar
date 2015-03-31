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

package oscar.oscarLab.ca.all.upload.handlers;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;
import oscar.oscarLab.ca.on.LabResultData;


public class AlphaHL7Handler implements MessageHandler {

//	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AlphaHL7Handler.class);
	
	@Override
	//public String parse(String serviceName, String fileName, int fileId) {
	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {

		try {
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for(String msg:messages) {
				 MessageUploader.routeReport(loggedInInfo, serviceName, LabResultData.ALPHAHL7, msg, fileId);
			}
		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not upload message: ", e);
			return null;
		}
		return ("success");
	}	

}
