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
package org.oscarehr.common.service;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;



public class AcceptableUseAgreementManager {
	private static Logger logger = MiscUtils.getLogger();
	private static String auaText = null;
	private static boolean loadAttempted = false;
	
		
	private static void loadAUA(){
		String path = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/OSCARloginText.txt";
		try{
			File auaFile = new File(path);
		    if(!auaFile.exists()){
		    	loadAttempted = true;
		    	logger.debug("No AcceptableUseAgreement File present. disabling AcceptableUseAgreement prompt" );
		    }
    
		    auaText = FileUtils.readFileToString(auaFile);
		    auaText = auaText.replaceAll("\n","\n<br />");
		}catch(Exception e){
			logger.error("ERROR LOADING AcceptableUseAgreement text from path "+path,e);
			auaText = null;
		}finally{
			loadAttempted = true;
		}

	 }
	 
	public static boolean hasAUA(){
		logger.debug("loadAttempted "+loadAttempted+" auaText "+auaText);
		if(!loadAttempted){
			loadAUA();
		}
		
		if (auaText == null){
			return false;
		}
		return true;
	}
	
	 public static String getAUAText() {
		 if(!loadAttempted){
				loadAUA();
		 }
		 return auaText;
	 }
	
}