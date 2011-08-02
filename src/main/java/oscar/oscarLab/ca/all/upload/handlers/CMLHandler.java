/*
 * CMLHandler.java
 *
 * Created on May 23, 2007, 4:32 PM
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
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

public class CMLHandler implements MessageHandler {

	Logger logger = Logger.getLogger(CMLHandler.class);
	Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao)SpringUtils.getBean("hl7TextInfoDao");
	
	public String parse(String serviceName, String fileName, int fileId) {

		int i = 0;
		try {
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {
				String msg = messages.get(i);
				if(isDuplicate(msg)) {
					return ("success");
				}
				MessageUploader.routeReport(serviceName, "CML", msg, fileId);

			}
		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not upload message: ", e);
			MiscUtils.getLogger().error("Error", e);
			return null;
		}
		return ("success");

	}
	
	private boolean isDuplicate(String msg) {
		//OLIS requirements - need to see if this is a duplicate
		oscar.oscarLab.ca.all.parsers.MessageHandler h = Factory.getHandler("CML", msg);
		//if final
		if(h.getOrderStatus().equals("CM")) {
			String acc = h.getAccessionNum().substring(3);
			//do we have this?
			List<Hl7TextInfo> dupResults = hl7TextInfoDao.searchByAccessionNumber(acc);
			for(Hl7TextInfo dupResult:dupResults) {
				if(("CML"+dupResult.getAccessionNumber()).equals(acc)) {
					//exact match
					return true;
				}
				if(dupResult.getAccessionNumber().indexOf("-")!= -1) {
					if(dupResult.getAccessionNumber().subSequence(0,dupResult.getAccessionNumber().indexOf("-")).equals(acc) ) {
						//olis match								
						return true;
					}
				}
			}		
		}
		return false;	
	}
}