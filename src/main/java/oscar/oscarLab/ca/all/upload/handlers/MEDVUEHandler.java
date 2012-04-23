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
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.MEDVUEUtilities;

public class MEDVUEHandler implements MessageHandler {

	Logger logger = Logger.getLogger(MEDVUEHandler.class);

	public MEDVUEHandler() {
		logger.info("NEW MEDVUEHandler UPLOAD HANDLER instance just instantiated. ");
	}

	public String parse(String serviceName, String fileName, int fileId) {
		logger.info("ABOUT TO PARSE!");

		MEDVUEUtilities u = new MEDVUEUtilities();

		int i = 0;
		try {
			ArrayList<String> messages = u.separateMessages(fileName);

			for (i = 0; i < messages.size(); i++) {

				String msg = messages.get(i);


				MessageUploader.routeReport(serviceName, "MEDVUE", msg, fileId);

			}

			updateLabStatus(messages.size());
			logger.info("Parsed OK");
		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not upload message", e);
			return null;
		}
		return ("success");

	}

	// recheck the abnormal status of the last 'n' labs
	private void updateLabStatus(int n) {

		 Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
		 List<Hl7TextInfo> labList = hl7TextInfoDao.getAllLabsByLabNumberResultStatus();
		 ListIterator<Hl7TextInfo> iter = labList.listIterator();


		 while (iter.hasNext() && n>0) {
			 if (!iter.next().getResultStatus().equals("A")) {
				 oscar.oscarLab.ca.all.parsers.MessageHandler h = Factory.getHandler(((Integer)iter.next().getLabNumber()).toString());

	                int i=0;
	                int j=0;
	                String resultStatus = "";
	                while(resultStatus.equals("") && i < h.getOBRCount()){
	                    j = 0;
	                    while(resultStatus.equals("") && j < h.getOBXCount(i)){
	                        logger.info("obr("+i+") obx("+j+") abnormal ? : "+h.getOBXAbnormalFlag(i, j));
	                        if(h.isOBXAbnormal(i, j)){
	                            resultStatus = "A";
	                            hl7TextInfoDao.updateResultStatusByLabId("A", iter.next().getLabNumber());

	                        }
	                        j++;
	                    }
	                    i++;
	                }
			 }
			 n--;
		 }


	    }


}
