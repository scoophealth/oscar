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
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.OscarAuditLogger;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.segment.OBR;
import ca.uhn.hl7v2.model.v23.segment.ORC;

public class CLSHandler implements MessageHandler {

	String lineDelimiter = "\r";
	Logger logger = Logger.getLogger(CLSHandler.class);	
	Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao)SpringUtils.getBean("hl7TextInfoDao");
	
	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {

		int i = 0;
        oscar.oscarLab.ca.all.parsers.CLSHandler newVersionCLSParser = new oscar.oscarLab.ca.all.parsers.CLSHandler();
        
        Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao)SpringUtils.getBean("hl7TextInfoDao");
        Hl7TextMessage hl7TextMessage = new Hl7TextMessage();
		
		try {
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {
				String msg = messages.get(i);
				/*
				if(isDuplicate(msg)) {
					return ("success");
				}
				*/
                newVersionCLSParser.init(msg);
                String accessionNumber = newVersionCLSParser.getAccessionNum();
                String fillerOrderNumber = newVersionCLSParser.getFillerOrderNumber(); 
                Hl7TextInfo hl7TextInfo = hl7TextInfoDao.findLatestVersionByAccessionNumberOrFillerNumber(
                		accessionNumber, fillerOrderNumber);
                
                // Glucose labs come back with different accession numbers, but the same filler number.
                // We are going to replace any successive accession numbers with the originals as
                // suggested in the CLS conformance documentation
                if(hl7TextInfo != null && hl7TextInfo.getFillerOrderNum().equals(fillerOrderNumber) && 
                		!hl7TextInfo.getAccessionNumber().equals(accessionNumber)) {

                	msg = this.ReplaceAccessionNumber(msg, accessionNumber, hl7TextInfo.getAccessionNumber());
                }

                if(hl7TextInfo != null) {
                	String lastVersionLab = oscar.oscarLab.ca.all.parsers.Factory.getHL7Body(Integer.toString(hl7TextInfo.getLabNumber()));
                	msg = mergeLabs(lastVersionLab, msg);
                }
				MessageUploader.routeReport(loggedInInfo, serviceName, "CLS", msg, fileId);

			}
		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not upload message: ", e);
			MiscUtils.getLogger().error("Error", e);
			return null;
		}
		return ("success");

	}
	
	private String mergeLabs(String oldVersion, String newVersion) throws HL7Exception
	{
		String outLabString = newVersion;
		StringBuilder test = new StringBuilder(newVersion);

        oscar.oscarLab.ca.all.parsers.CLSHandler oldVersionCLSParser = new oscar.oscarLab.ca.all.parsers.CLSHandler();
        oldVersionCLSParser.init(oldVersion);
        oscar.oscarLab.ca.all.parsers.CLSHandler newVersionCLSParser = new oscar.oscarLab.ca.all.parsers.CLSHandler();
        newVersionCLSParser.init(newVersion);

        int currentObrCount = newVersionCLSParser.getOBRCount();
        
        // Get all OBRs from the old version that don't exist in the new version
        // and append them to the current version
        ArrayList<OBR> oldObrs = this.getObrs(oldVersionCLSParser);
        int obrIndex = 0;
        for(OBR oldObr : oldObrs)
        {
        	String fillerNumber = this.getObrFillerNumber(oldObr);
        	if(!this.obrExists(fillerNumber, newVersionCLSParser))
        	{
        		currentObrCount++;
        		// Remove the old OBR index so we can add the new one
        		String tempObr = oldObr.encode();
                tempObr = tempObr.substring(tempObr.indexOf('|') + 1);
                tempObr = tempObr.substring(tempObr.indexOf('|') + 1);

        		// Set the OBR index
                outLabString += this.lineDelimiter + "OBR|" + Integer.toString(currentObrCount) + "|" + tempObr;

                // Get OBR NTE records
                int obrNteCount = oldVersionCLSParser.getOBRCommentCount(obrIndex);
                for(int obrNteIndex = 0; obrNteIndex < obrNteCount; obrNteIndex++) {
					outLabString += this.lineDelimiter + oldVersionCLSParser.getOBRNTE(obrIndex, obrNteIndex).encode();
                }

        		// Get Previous version OBX records
                int obxCount = oldVersionCLSParser.getOBXCount(obrIndex);
                for(int obxIndex = 0; obxIndex < obxCount; obxIndex++) {
                    outLabString += this.lineDelimiter + oldVersionCLSParser.getOBX(obrIndex, obxIndex).encode();

                    // Get Previous version OBX NTE records
                    int nteCount = oldVersionCLSParser.getOBXCommentCount(obrIndex, obxIndex);
                    for(int nteIndex = 0; nteIndex < nteCount; nteIndex++) {
                        outLabString += this.lineDelimiter + oldVersionCLSParser.getNTE(obrIndex, obxIndex, nteIndex).encode();
                    }
                }

        		// Get Previous version ORC record if one exists
                ORC orc = oldVersionCLSParser.getORC(obrIndex);
                if(orc != null && orc.encode().length() > 5) {
                    test.append(outLabString += this.lineDelimiter + orc.encode());
                }
        	}
        	obrIndex++;
        }
		return outLabString;
	}
	
	private boolean obrExists(String fillerNumber, oscar.oscarLab.ca.all.parsers.CLSHandler parser)
		throws HL7Exception
	{
		ArrayList<OBR> searchObrs = this.getObrs(parser);
		for(OBR searchObr : searchObrs) {
			if(this.getObrFillerNumber(searchObr).equals(fillerNumber)) {
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<OBR> getObrs( oscar.oscarLab.ca.all.parsers.CLSHandler parser)
		throws HL7Exception
	{
		ArrayList<OBR> outOBR = new ArrayList<OBR>();
        for(int obrIndex = 0; obrIndex < parser.getOBRCount(); obrIndex++) {
            OBR newObr = parser.getOBR(obrIndex).getOBR();
            outOBR.add(newObr);
        }
        return outOBR;
	}
    
    private String getObrFillerNumber(OBR obr)
    {
    	return obr.getFillerOrderNumber().getEi1_EntityIdentifier().getValue() + "^" +
                obr.getFillerOrderNumber().getEi2_NamespaceID().getValue();
    }
	
    private String ReplaceAccessionNumber(String message, String oldAccessionNumber, String newAccessionNumber)
    {
    	message = message.replace(oldAccessionNumber, newAccessionNumber);
    	return message;
    }

	private boolean isDuplicate(LoggedInInfo loggedInInfo, String msg) {
		//OLIS requirements - need to see if this is a duplicate
		oscar.oscarLab.ca.all.parsers.MessageHandler h = Factory.getHandler("CLS", msg);
		//if final
		if(h.getOrderStatus().equals("CM")) {
			String acc = h.getAccessionNum().substring(3);
			//do we have this?
			List<Hl7TextInfo> dupResults = hl7TextInfoDao.searchByAccessionNumber(acc);
			for(Hl7TextInfo dupResult:dupResults) {
				if(("CLS"+dupResult.getAccessionNumber()).equals(acc)) {
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
