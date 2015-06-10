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


// form_class - a part of class name
// c_lastVisited, formId - if the form has multiple pages
package oscar.eform.util;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.form.FrmRecord;
import oscar.form.FrmRecordFactory;
import oscar.oscarEncounter.data.EctFormData;
import oscar.util.UtilDateUtilities;

public final class EFormPrintPDFUtil {
	private static final String HEAD_CIRCUMFERENCE_GRAPH = "HEAD_CIRC";
	private static final String LENGTH_GRAPH = "LENGTH";
    
    private static DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);

    public static Properties getFrmRourkeGraph(LoggedInInfo loggedInInfo, Properties full_props) {
    	String graphType = full_props.getProperty("__graphType");
    	Integer demographicNo = Integer.valueOf(full_props.getProperty("demographic_no"));
    	
    	Demographic demographic = demographicManager.getDemographic(loggedInInfo, demographicNo);
    	Date dob = demographic.getBirthDay().getTime();
        
    	String[] names = {"baby_name", "birthday", "birth_length", "birth_wt", "head_circ",
    					  "visit_date_1w", "visit_date_2w", "visit_date_1m", "visit_date_2m", "visit_date_4m", "visit_date_6m", 
    					  "visit_date_9m", "visit_date_12m", "visit_date_15m", "visit_date_18m", "visit_date_2y", "visit_date_4y", 
    					  "height_1w", "height_2w", "height_1m", "height_2m", "height_4m", "height_6m", 
    					  "height_9m", "height_12m", "height_15m", "height_18m", "height_2y", "height_4y", 
    					  "weight_1w", "weight_2w", "weight_1m", "weight_2m", "weight_4m", "weight_6m", 
    					  "weight_9m", "weight_12m", "weight_15m", "weight_18m", "weight_2y", "weight_4y", 
    					  "headcirc_1w", "headcirc_2w", "headcirc_1m", "headcirc_2m", "headcirc_4m", "headcirc_6m", 
    					  "headcirc_9m", "headcirc_12m", "headcirc_15m", "headcirc_18m", "headcirc_2y"};

    	Properties props = new Properties();
    	for (String name : names) {
    		if (full_props.getProperty(name)!=null) {
    			props.setProperty(name, full_props.getProperty(name));
    		}
    	}
    	
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        String ageStr;
        if( dob != null ) {
            ageStr = String.valueOf((now.getTime()-dob.getTime())/1000L/60L/60L/24L);
        }
        else {
            ageStr = "";
        }
        props.setProperty("c_Age", ageStr);

		//now we add measurements from formGrowth0_36 form
        addMeasurementsInFormGrowth0_36(loggedInInfo, props, demographicNo);

        //now add measurements from Ht and Wt in measurements group
        addMeasurementsGroupHtWt(props, dob, demographicNo, graphType);
        
        return props;
    }
    
    
    
    private static void addMeasurementsGroupHtWt(Properties props, Date dob, Integer demographicNo, String graphType) {
        //first set up cutoff for first page = 2 years of age
        //then set up cutoff for second page = 19 years of age
        //then we can compare measurement dates and slot them accordingly
        
        MeasurementDao measurementDao = (MeasurementDao)SpringUtils.getBean("measurementDao");

        Date mDateHt, mDateWt;
        String date;
        int idx = 0;
        String graphicPage;
        float age = 0;
        
        //set startdate for second page as defined in config file
        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        cal.add(Calendar.YEAR, 2);
        Date startDate = cal.getTime();
        date = UtilDateUtilities.DateToString(startDate, "yyyy-MM-dd");
        props.setProperty("__startDate", date);
        List<Measurement> measurementsHt = measurementDao.findByType(demographicNo, "Ht");
        List<Measurement> measurementsWt = measurementDao.findByType(demographicNo, "Wt");

        for( Measurement mHt : measurementsHt ) {
        	graphicPage = null;
        	
        	if( mHt.getDateObserved() != null ) {
        		mDateHt = mHt.getDateObserved();                		
        	}
        	else {
        		mDateHt = mHt.getCreateDate();                		
        	}
        	        	                	
        	age = calcYears(dob, mDateHt);
        	MiscUtils.getLogger().info("Age " + age);
        	
        	if( age <= 2 ) graphicPage = "0";
        	else if( age <= 19 ) graphicPage = "1";
        	else continue;
        	
        	if( graphType.equals(HEAD_CIRCUMFERENCE_GRAPH )) {
        		for( Measurement mWt : measurementsWt ) {
        			if( mWt.getDateObserved() != null ) {
                		mDateWt = mWt.getDateObserved();                		
                	}
                	else {
                		mDateWt = mWt.getCreateDate();                		
                	}
        			
        			if( mDateHt.compareTo(mDateWt) == 0 ) {
        				//name = elementName_num_section_page
        				props.setProperty("xVal_"+idx + "_1_" + graphicPage, mHt.getDataField());
        				props.setProperty("yVal_"+idx + "_1_" + graphicPage, mWt.getDataField());
        				break;
        			}
        		}
        	}
        	else if( graphType.equals(LENGTH_GRAPH )) {                	
        		date = UtilDateUtilities.DateToString(mDateHt, "yyyy-MM-dd"); //dd/MM/yyyy
        	
        		//name = elementName_num_section_page
        		props.setProperty("xVal_"+idx + "_1_" + graphicPage, date);                	
        		props.setProperty("yVal_"+idx + "_1_" + graphicPage, mHt.getDataField());
        	}
        	
        	++idx;
        }

        if( graphType.equals(LENGTH_GRAPH)) {
            for( Measurement m : measurementsWt ) {
            	graphicPage = null;
            	if( m.getDateObserved() != null ) {
            		mDateWt = m.getDateObserved();                		
            	}
            	else {
            		mDateWt = m.getCreateDate();                		
            	}
            	                	
            	age = calcYears(dob, mDateWt);
            	if( age <= 2 ) graphicPage = "0";
            	else if( age <= 19 ) graphicPage = "1";
            	else continue;
            	
            	date = UtilDateUtilities.DateToString(mDateWt, "yyyy-MM-dd"); //dd/MM/yyyy
            	props.setProperty("xVal_"+idx + "_0_" + graphicPage, date);                	
            	props.setProperty("yVal_"+idx + "_0_" + graphicPage, m.getDataField());
	                	
            	++idx;
            }
        }
        
        //don't forget to set the xAxis scale for the 2 pages
        props.setProperty("__xDateScale_1", String.valueOf(Calendar.MONTH));
        props.setProperty("__xDateScale_2", String.valueOf(Calendar.YEAR));
    }

    private static void addMeasurementsInFormGrowth0_36(LoggedInInfo loggedInInfo, Properties props, Integer demographicNo) {
        EctFormData.PatientForm[] pforms = EctFormData.getPatientFormsFromLocalAndRemote(loggedInInfo, String.valueOf(demographicNo), "formGrowth0_36");
        if (pforms.length > 0) {
        	EctFormData.PatientForm pfrm = pforms[0];
        	FrmRecord rec = (new FrmRecordFactory()).factory("Growth0_36");
        	
            try {
                java.util.Properties growthProps = rec.getFormRecord(loggedInInfo, demographicNo, pfrm.formId);
                Enumeration<Object> keys = growthProps.keys();
                String key;
                String value;
                String date;
                while( keys.hasMoreElements() ) {
                	key = (String) keys.nextElement();
                	if( key.startsWith("date_")) {
                		value = growthProps.getProperty(key, "");
                		if( !value.equals("")) {
                			date = value.replace("/", "-");
                			props.setProperty(key,date);
                		}
                	}
                	else if( key.startsWith("weight_") || key.startsWith("length_") || key.startsWith("headCirc_") ) {
                			props.setProperty(key, growthProps.getProperty(key, ""));
                	}
                }
            } catch (SQLException e) {
            	MiscUtils.getLogger().error("", e);
            }
        }
    }
    
    private static float calcYears(Date startDate, Date endDate) {
    	Calendar startCalendar = Calendar.getInstance();
    	startCalendar.setTime(startDate);
    	
    	Calendar endCalendar = Calendar.getInstance();
    	endCalendar.setTime(endDate);
    	
    	long time = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
    	float years = time/1000.0f/60.0f/60.0f/24.0f/365.0f;
    	
    	return years;
    	
    }
}
