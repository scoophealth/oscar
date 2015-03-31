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

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.upload.handlers.OscarToOscarHl7V2.AdtA09Handler;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v26.message.ADT_A09;

public class OscarToOscarHl7V2Handler implements MessageHandler {
	private Logger logger = MiscUtils.getLogger();

	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {
		
		try {
	        byte[] dataBytes=FileUtils.readFileToByteArray(new File(fileName));
	        String dataString=new String(dataBytes, MiscUtils.DEFAULT_UTF8_ENCODING);
	        logger.debug("Incoming HL7 Message : \n"+dataString);
	        
			AbstractMessage message=OscarToOscarUtils.pipeParserParse(dataString);

			if (message instanceof ADT_A09) 
			{
				AdtA09Handler.handle(loggedInInfo, (ADT_A09) message);
			}
			else
			{
				MessageUploader.routeReport(loggedInInfo, serviceName, OscarToOscarUtils.UPLOAD_MESSAGE_TYPE, dataString, fileId);
			}
			
			return("success");
		} catch (Exception e) {
	        logger.error("Unexpected error.", e);
	        MessageUploader.clean(fileId);
	        throw(new RuntimeException(e));
        }
    }
}
