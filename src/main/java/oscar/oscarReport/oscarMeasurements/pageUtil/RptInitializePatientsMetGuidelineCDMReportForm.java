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

public final class RptInitializePatientsMetGuidelineCDMReportForm extends ActionForm {

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
    
    private String[] guidelineCheckbox;
    public String[] getGuidelineCheckbox(){
        return guidelineCheckbox;
    }
    public void setGuidelineCheckbox(String[] guidelineCheckbox){
        this.guidelineCheckbox = guidelineCheckbox;
    }
    
    private String[] startDateB;
    public String[] getStartDateB(){
        return startDateB;
    }
    public void setStartDateB(String[] startDateB){
        this.startDateB = startDateB;
    }
    
    private String[] endDateB;
        public String[] getEndDateB(){
        return endDateB;
    }
    public void setEndDateB(String[] endDateB){
        this.endDateB = endDateB;
    }
    
    private String[] idB;
        public String[] getIdB(){
        return idB;
    }
    public void setIdB(String[] idB){
        this.idB = idB;
    }
    
    private String[] guildlineB;
        public String[] getGuidelineB(){
        return guildlineB;
    }
    public void setGuidelineB(String[] guildlineB){
        this.guildlineB = guildlineB;
    }
    
    private String aboveBelow;
        public String getAboveBelow(){
        return aboveBelow;
    }
    public void setAboveBelow(String aboveBelow){
        this.aboveBelow = aboveBelow;
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
