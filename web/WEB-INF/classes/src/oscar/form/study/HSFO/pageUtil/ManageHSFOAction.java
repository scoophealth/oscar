/*
 *  Copyright (C) 2007  Heart & Stroke Foundation

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

    <HSFO TEAM>
 
   This software was written for the
   The Heart and Stroke Foundation of Ontario
   Toronto, Ontario, Canada
 
   ManageHSFOAction.java
 
   Created on March 1, 2007, 11:03 PM
 
 */

package oscar.form.study.HSFO.pageUtil;

import com.sun.jdi.connect.Connector;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import oscar.Misc;
import oscar.form.study.HSFO.HSFODAO;
import oscar.form.study.HSFO.PatientData;
import oscar.form.study.HSFO.PatientList;
import oscar.form.study.HSFO.VisitData;
import oscar.oscarDemographic.data.DemographicData;

/**
 insert into encounterForm (form_name,form_value,form_table,hidden ) values ( 'HSFP form','../form/HSFOform.do?demographic_no=','form_hsfo_visit',0);

 * Class used to fill data for the HSFO form Study
 */
public class ManageHSFOAction extends Action{
    
    /** Creates a new instance of ManageHSFOAction */
    public ManageHSFOAction() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        PatientData patientData = new PatientData();
        VisitData latestVisitData = new VisitData();
        VisitData visitData = new VisitData();
        PatientList historyList = new PatientList();
        
        List patientHistory = new LinkedList();
        String id = (String) request.getAttribute("demographic_no");
        if (id == null){
            id = (String) request.getParameter("demographic_no");
        }
        String isfirstrecord = "";
        boolean firstrecord=false;
        String user = (String) request.getSession().getAttribute("user");
        System.out.println(request.getAttribute("Id"));
        
     
        HSFODAO hsfoDAO = new HSFODAO();
        firstrecord = hsfoDAO.isFirstRecord(id);
        
        DemographicData demoData = new DemographicData();
        DemographicData.Demographic d = demoData.getDemographic(id);

        if (firstrecord == true) {//		determine if this is the first record
            isfirstrecord="true";
            patientData.setFName(d.getFirstName());
            patientData.setLName(d.getLastName());
            patientData.setBirthDate(d.getDOBObj());
            patientData.setSex(d.getSex());
            patientData.setPostalCode(Misc.cutBackString(d.getPostal(),3));
            patientData.setPatient_Id(id);
        } else {
            isfirstrecord="false";
            
            patientData = hsfoDAO.retrievePatientRecord(id);
            patientHistory = hsfoDAO.retrieveVisitRecord(id);
            
            System.out.println("Size: " + patientHistory.size());
            int size = patientHistory.size();
            
            //retrieve the most recent record
            latestVisitData = (VisitData)patientHistory.get(size-1);
            System.out.println("Recent: " + latestVisitData.getVisitDate_Id());
        }
        
        historyList.setPatientHistory(patientHistory);
        
        //send provider_no and family_doctor to form for processing
        request.setAttribute("EmrHCPId1", user);
        request.setAttribute("EmrHCPId2", d.getProviderNo()); //TODO: may need to convert to provider name
        
        //set request attributes to forward to jsp
        request.setAttribute("patientData", patientData);
        request.setAttribute("historyList", historyList);
        request.setAttribute("visitData", latestVisitData);
        request.setAttribute("isFirstRecord", isfirstrecord);
               
        return mapping.findForward("registration");
    }
    
    
    
}
