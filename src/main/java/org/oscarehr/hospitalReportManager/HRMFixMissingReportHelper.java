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
package org.oscarehr.hospitalReportManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

/**
 * See bug 4195.
 * 
 * The issue is that there could be some files that are relative, but the file is not in DOCUMENT_DIR (still in the downloads directory)
 * 
 * This script (runs once on startup), goes through the HRM db records, and tries to file any missing files.
 * 
 * @author marc
 *
 */
public class HRMFixMissingReportHelper {

	private String downloadsDirectory= OscarProperties.getInstance().getProperty("OMD_downloads");

	private HRMDocumentDao hrmDocumentDao = (HRMDocumentDao)SpringUtils.getBean("HRMDocumentDao");
	
	private Logger logger = MiscUtils.getLogger();
	
	private PropertyDao propertyDao = SpringUtils.getBean(PropertyDao.class);
	
	public static final String SCRIPT_PROPERTY = "HRMFixMissingReportHelper.Run";
	
	
	public void fixIt() {
		
		if(hasThisRunBefore()) {
			return;
		}
		
		logger.info("Running HRMFixMissingReportHelper");
		int offset = 0;
		int limit = 100;
		List<HRMDocument> documents = null;
		
		while(true) {
			documents = hrmDocumentDao.findAll(offset,limit);
			
			for(HRMDocument doc:documents) {
				String hrmReportFileLocation = doc.getReportFile();
				
				File tmpXMLholder = new File(hrmReportFileLocation);
				
				if(tmpXMLholder.exists()) {
					continue;
				}
				String place= OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
				tmpXMLholder = new File(place + File.separator + hrmReportFileLocation);
				
				if(tmpXMLholder.exists()) {
					continue;
				}
				
				logger.info("Searching for report file:" + hrmReportFileLocation);
				
				//if we got to here, it means we can't find the file..let's go on a hunt
				File file = searchForFile(tmpXMLholder.getName());
				
				if(file != null) {
					//copy it over to document_dir
					try {
						FileUtils.copyFileToDirectory(file, new File(place));
						logger.info("Fixed:" + hrmReportFileLocation);
					}catch(IOException e) {
						logger.error("Unable to copy the file to DOCUMENT_DIR:"+file);
					}
				} else {
					logger.warn("UNABLE TO FIND THE FILE:" + tmpXMLholder);
				}
			}
			
			if(documents.size() < limit) {
				break;
			}
			offset += limit;
		}

		logger.info("Done running HRMFixMissingReportHelper");
		
		setAsRun();
	}
	
	private File searchForFile(String fileName) {
		//root dir = downloadsDirectory
		File rootDir = new File(downloadsDirectory);
		if(!rootDir.exists() || !rootDir.isDirectory()) {
			logger.error("HRM Downloads directory not found..can't continue with 4195 fixer");
			throw new IllegalArgumentException("Directory not found: "+ downloadsDirectory);
		}
		//these are the dated directories like 18092013
		for(File datedDirectory: rootDir.listFiles()) {
			//should be a decrypted directory within
			if(!datedDirectory.isDirectory()) {
				logger.warn("skipping file in the root directory:"+ datedDirectory);
				continue;
			}
			File decryptedDirectory = new File(datedDirectory,"decrypted");
			if(!decryptedDirectory.exists() || !decryptedDirectory.isDirectory()) {
				logger.warn("skipping.decrypted subdirectory not found in :"+ datedDirectory);
				continue;
			}
			//we can now check for the file
			File theFile = new File(decryptedDirectory,fileName);
			if(theFile != null && theFile.exists()) {
				logger.info("Found the file we were missing:"+theFile);
				return theFile;
			}
		}
		
		return null;
	}
	
	private boolean hasThisRunBefore() {
		List<Property> propList = propertyDao.findByName(SCRIPT_PROPERTY);
		if(propList.isEmpty()) {
			return false;
		}
		Property prop = propList.get(0);
		if(prop != null && "1".equals(prop.getValue())) {
			return true;
		}
		return false;
	}
	
	private void setAsRun() {
		List<Property> propList = propertyDao.findByName(SCRIPT_PROPERTY);
		Property prop = null;
		if(!propList.isEmpty()) {
			prop = propList.get(0);
		}

		if(prop == null) {
			prop = new Property();
			prop.setName(SCRIPT_PROPERTY);
			prop.setValue("1");
			propertyDao.persist(prop);
		} else {
			prop.setValue("1");
			propertyDao.merge(prop);
		}
	}
}

