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

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;

public final class RptInitializePatientsInAbnormalRangeCDMReportForm extends ActionForm {

    private final Map values = new HashMap();

    public void setValue(String key, Object value) {
        values.put(key, value);
    }

    public Object getValue(String key) {
        return values.get(key);
    }
    
    private String[] patientSeenCheckbox;
    public String[] getPatientSeenCheckbox(){
        return patientSeenCheckbox;
    }
    public void setPatientSeenCheckbox(String[] patientSeenCheckbox){
        this.patientSeenCheckbox=patientSeenCheckbox;
    }
    
    private String startDateA;
    public String getStartDateA(){
        return startDateA;
    }
    public void setStartDateA(String startDateA){
        this.startDateA = startDateA;
    }
    
    private String endDateA;
    public String getEndDateA(){
        return endDateA;
    }
    public void setEndDateA(String endDateA){
        this.endDateA = endDateA;
    }    
    


    /***************************************************************
     *Getter and Setter for Percentage of Patient in Abnormal range
     **************************************************************/
    private String[] abnormalCheckbox;
    public String[] getAbnormalCheckbox(){
        return abnormalCheckbox;
    }
    public void setAbnormalCheckbox(String[] abnormalCheckbox){
        this.abnormalCheckbox = abnormalCheckbox;
    }
    
    private String[] startDateC;
    public String[] getStartDateC(){
        return startDateC;
    }
    public void setStartDateC(String[] startDateC){
        this.startDateC = startDateC;
    }
    
    private String[] endDateC;
        public String[] getEndDateC(){
        return endDateC;
    }
    public void setEndDateC(String[] endDateC){
        this.endDateC = endDateC;
    }
    
    private String[] upperBound;
        public String[] getUpperBound(){
        return upperBound;
    }
    public void setUpperBound(String[] upperBound){
        this.upperBound = upperBound;
    }
    
    private String[] lowerBound;
        public String[] getLowerBound(){
        return lowerBound;
    }
    public void setLowerBound(String[] lowerBound){
        this.lowerBound = lowerBound;
    }    


/*    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if(service == null || service.length() == 0)
            errors.add("service", new ActionError("Errors.service.null"));
        try
        {
            int temp = Integer.parseInt(service);
            if(temp < 0)
                errors.add("service", new ActionError("Errors.service.noServiceSelected"));
        }
        catch(Exception e)
        {
            errors.add("fName", new ActionError("Errors.service.notNum"));
        }
        if(!errors.empty())
            request.setAttribute("validateError", "blah");
        return errors;
    }
    */
    
}
