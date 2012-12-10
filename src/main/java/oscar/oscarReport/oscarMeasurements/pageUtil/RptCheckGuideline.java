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


package oscar.oscarReport.oscarMeasurements.pageUtil;

import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.ValidationsDao;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.Validations;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class RptCheckGuideline{
    
    public RptCheckGuideline(){
    }

     /*****************************************************************************************
     * Check the measurementType is a numeric value
     *
     * @return 0 when it is false, 1 otherwise
     ******************************************************************************************/
    public int getValidation(String measurementType){        
        MeasurementTypeDao dao = SpringUtils.getBean(MeasurementTypeDao.class);
        ValidationsDao vDao = SpringUtils.getBean(ValidationsDao.class); 
        for(MeasurementType mt : dao.findByType(measurementType)) {
            String validation = mt.getValidation();
            
            Validations v = vDao.find(ConversionUtils.fromIntString(validation));
            if (v != null && v.isNumeric()) {
            	return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    /*****************************************************************************************
     * Check if the data stored met guideline
     *
     * @return boolean
     ******************************************************************************************/	
    public boolean isNumericValueMetGuideline(double dataEntry, String guideline, String aboveBelow){
        
        MiscUtils.getLogger().debug("this is a numeric value");
        boolean passAllTests = false;
        
        if(aboveBelow.compareTo(">")==0){                                        
            if(dataEntry>=Double.parseDouble(guideline)){
                passAllTests=true;
                MiscUtils.getLogger().debug("Pass double test");
            }
            else{
                passAllTests=false;
            }
        }
        else if(aboveBelow.compareTo("<")==0){
            if(dataEntry<=Double.parseDouble(guideline)){
                passAllTests=true;
                MiscUtils.getLogger().debug("Pass double test");
            }
            else{
                passAllTests=false;
            }
        }
        return passAllTests;
    }
    
     /*****************************************************************************************
     * Check if the the blood pressure stored met guideline
     *
     * @return boolean
     ******************************************************************************************/       
    public boolean isBloodPressureMetGuideline(String dataEntry, String guideline, String aboveBelow){
         
        MiscUtils.getLogger().debug("this is blood pressure");
        
        int slashIndex = guideline.indexOf("/");
        int dataSlashIndex = dataEntry.indexOf("/");
        boolean passAllTests = false;
        
        if (slashIndex >= 0 && dataSlashIndex >=0){                                                                                                                                                                
            String systolic = guideline.substring(0, slashIndex);
            String diastolic = guideline.substring(slashIndex+1);
            String systolicData = dataEntry.substring(0, dataSlashIndex);
            String diastolicData = dataEntry.substring(dataSlashIndex+1);

            int iGuidelineSystolic = Integer.parseInt(systolic);
            int iGuidelineDiastolic = Integer.parseInt(diastolic); 
            int iDataSystolic = Integer.parseInt(systolicData);
            int iDataDiastolic = Integer.parseInt(diastolicData); 

            MiscUtils.getLogger().debug("guideline Systolic: " + iGuidelineSystolic + " dataSystolic: " + iDataSystolic);
            MiscUtils.getLogger().debug("guideline Diastolic: " + iGuidelineDiastolic + " dataDiastolic: " + iDataDiastolic);
            if(aboveBelow.compareTo("<")==0){
                if(iDataSystolic<=iGuidelineSystolic && iDataDiastolic<=iGuidelineDiastolic){
                    passAllTests=true;
                    MiscUtils.getLogger().debug("pass this BP test");
                }
                else{
                    passAllTests=false;
                    MiscUtils.getLogger().debug("fail this BP test");
                }
            }
            else if(aboveBelow.compareTo(">")==0){
                if(iDataSystolic>=iGuidelineSystolic && iDataDiastolic>=iGuidelineDiastolic){
                    passAllTests=true;
                    MiscUtils.getLogger().debug("pass this BP test");
                }
                else{
                    passAllTests=false;
                    MiscUtils.getLogger().debug("fail this BP test");
                }
            } 
        }
        return passAllTests;
    }    

     /*****************************************************************************************
     * Check if the yes/no question met guideline 
     *
     * @return boolean
     ******************************************************************************************/       
    public boolean isYesNoMetGuideline(String dataEntry, String guideline){
        boolean passAllTests = false;
        
        MiscUtils.getLogger().debug("this is yes/no question");
        if(guideline.compareTo("YES")==0 || guideline.compareTo("yes")==0 
               || guideline.compareTo("Y")==0 || guideline.compareTo("Yes")==0){                                        
            if(dataEntry.compareTo("YES")==0 || dataEntry.compareTo("yes")==0 
               || dataEntry.compareTo("Y")==0 || dataEntry.compareTo("Yes")==0){
                   passAllTests=true;
                   MiscUtils.getLogger().debug("Pass yesno test");
            }
            else{
                passAllTests=false;
                MiscUtils.getLogger().debug("fail yesno test");
            }
        }
        else if(guideline.compareTo("NO")==0 || guideline.compareTo("No")==0 
               || guideline.compareTo("N")==0 || guideline.compareTo("no")==0){
            if(dataEntry.compareTo("NO")==0 || dataEntry.compareTo("No")==0 
               || dataEntry.compareTo("N")==0 || dataEntry.compareTo("no")==0){
                   passAllTests=true;
                   MiscUtils.getLogger().debug("Pass yesno test");
            }
            else{
                passAllTests=false;
                 MiscUtils.getLogger().debug("fail yesno test");
            }
        }
        return passAllTests;
    }
}
