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

public final class RptInitializeFrequencyOfRelevantTestsCDMReportForm extends ActionForm {

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
      

    /******************************************************************
     *Getter and Setter for Frequency of Relevant tests being performed
     ******************************************************************/
    private String[] frequencyCheckbox;
    public String[] getFrequencyCheckbox(){
        return frequencyCheckbox;
    }
    public void setFrequencyCheckbox(String[] frequencyCheckbox){
        this.frequencyCheckbox = frequencyCheckbox;
    }
    
    private String[] startDateD;
    public String[] getStartDateD(){
        return startDateD;
    }
    public void setStartDateD(String[] startDateD){
        this.startDateD = startDateD;
    }
    
    private String[] endDateD;
        public String[] getEndDateD(){
        return endDateD;
    }
    public void setEndDateD(String[] endDateD){
        this.endDateD = endDateD;
    }    
    
    private int[] exactly;
    public int[] getExactly(){
        return exactly;
    }
    public void setExactly(int[] exactly){
        this.exactly = exactly;
    } 
    
    private int[] moreThan;
        public int[] getMoreThan(){
        return moreThan;
    }
    public void setMoreThan(int[] moreThan){
        this.moreThan = moreThan;
    } 
    
    private int[] lessThan;
        public int[] getLessThan(){
        return lessThan;
    }
    public void setLessThan(int[] lessThan){
        this.lessThan = lessThan;
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
