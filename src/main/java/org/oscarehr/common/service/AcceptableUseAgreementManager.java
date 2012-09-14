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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;



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
		    	auaText = null;
		    	return; // nothing more to do
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
	 
	 public static Date getAgreementCutoffDate(){
		 Calendar cal = GregorianCalendar.getInstance();
         
         Property latestProperty = findLatestProperty();
         if(latestProperty == null){  //Default to one year
        	 cal.add(Calendar.YEAR,-1);
        	 return cal.getTime();
         }
         
         if("aua_valid_from".equals(latestProperty.getName())){
         	//2012-09-20 01.08.30
         	SimpleDateFormat dateTimeFormatter=new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         	try{
         		Date validFromTime = dateTimeFormatter.parse(latestProperty.getValue());
         		cal.setTimeInMillis(validFromTime.getTime());
         	}catch(Exception e){
         		logger.error("Error: parsing aua_valid_from date "+latestProperty.getName(),e);
         	}
         }else{
         	String val = latestProperty.getValue();
         	String[] splitVal = val.split(" ");
         	int duration = Integer.parseInt(splitVal[0]);
         	duration = duration * -1;
         	int period = Calendar.YEAR;
         	if("month".equals(splitVal[1])){
         		period = Calendar.MONTH;
         	}else if("weeks".equals(splitVal[1])){
         		period = Calendar.WEEK_OF_YEAR;
         	}else if("days".equals(splitVal[1])){
         		period = Calendar.DAY_OF_YEAR;
         	}
         	cal.add(period, duration);
             	
         }
         return cal.getTime();
	 }
	 
	 public static Property findLatestProperty(){
		 PropertyDao propertyDao = SpringUtils.getBean(PropertyDao.class);
         List<Property> auaValidFrom  = propertyDao.findByName("aua_valid_from");
         List<Property> auaValiDuration  = propertyDao.findByName("aua_valid_duration");
         
         Property latestProperty = findLatestProperty(auaValidFrom,auaValiDuration);
         return latestProperty;
	 }
	 
	
	 
	 public static Property findLatestProperty(List<Property> ... propArray){
			Property returnProperty = null;
			for(List<Property> propList: propArray ){
			
				if(propList != null){
					for(Property p: propList){
						if(returnProperty == null){
							returnProperty = p;
						}else if(p !=null && returnProperty.getId() < p.getId()){
							returnProperty = p;
						}
					}
				}
			}
			return returnProperty;
		}
	
}