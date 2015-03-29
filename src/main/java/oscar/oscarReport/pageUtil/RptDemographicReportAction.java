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


package oscar.oscarReport.pageUtil;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarReport.data.RptDemographicQueryBuilder;
import oscar.oscarReport.data.RptDemographicQueryLoader;
import oscar.oscarReport.data.RptDemographicQuerySaver;

public  class RptDemographicReportAction extends Action {


    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    	
        MiscUtils.getLogger().debug("RptDemographicReportAction Jackson");
        RptDemographicReportForm frm = (RptDemographicReportForm) form;
        String[] select = frm.getSelect();
        String studyId = frm.getStudyId();

//        String yearStyle        = frm.getAge();
//        String startYear        = frm.getStartYear();
//        String endYear          = frm.getEndYear();
//        String[] rosterStatus   = frm.getRosterStatus();
//        String[] patientStatus  = frm.getPatientStatus();
//        String[] providers      = frm.getProviderNo();
//
//        String firstName        = frm.getFirstName();
//        String lastName         = frm.getLastName();
//        String sex              = frm.getSex();
//        String queryName        = frm.getQueryName();
          String query            = frm.getQuery();
          
          MiscUtils.getLogger().debug("query "+query);

        if (query.equals("Run Query")){
            MiscUtils.getLogger().debug("run query");
            RptDemographicQueryBuilder demoQ = new RptDemographicQueryBuilder();
            java.util.ArrayList searchedArray = demoQ.buildQuery(loggedInInfo, frm);
            MiscUtils.getLogger().debug("searchArray size "+searchedArray.size());
            request.setAttribute("searchedArray",searchedArray);
            request.setAttribute("selectArray",select);
            request.setAttribute("studyId", studyId);
        }else if( query.equals("Save Query")){
            RptDemographicQuerySaver demoS = new RptDemographicQuerySaver();
            demoS.saveQuery(frm);
        }else if (query.equals("Load Query")){
            RptDemographicQueryLoader demoL = new RptDemographicQueryLoader();
            RptDemographicReportForm dRF = demoL.queryLoader(frm);
            request.setAttribute("formBean",dRF);
        }else if( query.equals("Add to Study")) {
        	RptDemographicQueryBuilder demoQ = new RptDemographicQueryBuilder();
            java.util.ArrayList searchedArray = demoQ.buildQuery(loggedInInfo, frm);
            request.setAttribute("searchedArray",searchedArray);
            MiscUtils.getLogger().info("SELECT ARRAY IS NULL " + String.valueOf(select == null));
            MiscUtils.getLogger().info("STUDY ID IS " + studyId);
            request.setAttribute("selectArray",select);
            request.setAttribute("studyId", studyId);
            return (mapping.findForward("addToStudy"));
        }

        return (mapping.findForward("success"));
  }




}
