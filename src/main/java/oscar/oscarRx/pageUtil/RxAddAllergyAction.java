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


package oscar.oscarRx.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.data.RxPatientData;


public final class RxAddAllergyAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_allergy", "w", null)) {
			throw new RuntimeException("missing required security object (_allergy)");
		}
    	
            int id = Integer.parseInt(request.getParameter("ID"));

            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String description = request.getParameter("reactionDescription");

            String startDate = request.getParameter("startDate");
            String ageOfOnset = request.getParameter("ageOfOnset");
            String severityOfReaction = request.getParameter("severityOfReaction");
            String onSetOfReaction = request.getParameter("onSetOfReaction");
            String lifeStage = request.getParameter("lifeStage");
            String allergyToArchive = request.getParameter("allergyToArchive");
            
            RxPatientData.Patient patient = (RxPatientData.Patient)request.getSession().getAttribute("Patient");
            Allergy allergy = new Allergy();
            allergy.setDrugrefId(String.valueOf(id));
            allergy.setDescription(name);
            allergy.setTypeCode(Integer.parseInt(type));
            allergy.setReaction(description);

	    if (startDate.length()>=8 && getCharOccur(startDate,'-')==2) {
	    	allergy.setStartDate(oscar.oscarRx.util.RxUtil.StringToDate(startDate, "yyyy-MM-dd"));
	    } else if (startDate.length()>=6 && getCharOccur(startDate,'-')>=1) {
	    	allergy.setStartDate(oscar.oscarRx.util.RxUtil.StringToDate(startDate, "yyyy-MM"));
	    	allergy.setStartDateFormat(PartialDate.YEARMONTH);
	    } else if (startDate.length()>=4) {
	    	allergy.setStartDate(oscar.oscarRx.util.RxUtil.StringToDate(startDate, "yyyy"));
	    	allergy.setStartDateFormat(PartialDate.YEARONLY);
	    }
            allergy.setAgeOfOnset(ageOfOnset);
            allergy.setSeverityOfReaction(severityOfReaction);
            allergy.setOnsetOfReaction(onSetOfReaction);
            allergy.setLifeStage(lifeStage);

            if (type != null && type.equals("13")){
                RxDrugData drugData = new RxDrugData();
                try{
                RxDrugData.DrugMonograph f = drugData.getDrug(""+id);
                allergy.setRegionalIdentifier(f.regionalIdentifier);
                }catch(Exception e){
                    MiscUtils.getLogger().error("Error", e);
                }
            }

            allergy.setDemographicNo(patient.getDemographicNo());
            allergy.setArchived(false);
            
            patient.addAllergy(oscar.oscarRx.util.RxUtil.Today(), allergy);
            
            String ip = request.getRemoteAddr();
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_ALLERGY, ""+allergy.getAllergyId() , ip,""+patient.getDemographicNo(), allergy.getAuditString());
            
            if (allergyToArchive!=null && !allergyToArchive.isEmpty()) {
            	patient.deleteAllergy(Integer.parseInt(allergyToArchive));
            	LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ARCHIVE, LogConst.CON_ALLERGY, ""+allergyToArchive , ip,""+patient.getDemographicNo(), null);
            }
            
            return (mapping.findForward("success"));
    }

    private int getCharOccur(String str, char ch) {
	int occurence=0, from=0;
	while (str.indexOf(ch,from)>=0) {
	    occurence++;
	    from = str.indexOf(ch,from)+1;
	}
	return occurence;
    }
}
