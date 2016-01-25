<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page
	import="java.util.ArrayList, oscar.dms.*, oscar.oscarLab.ca.on.*, oscar.util.StringUtils, java.util.List"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.managers.HRMManager" %>
<%@page import="org.oscarehr.hospitalReportManager.model.HRMDocument" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
  String demo = request.getParameter("demo") ;
  String requestId = request.getParameter("requestId");
  HRMManager hrmManager = SpringUtils.getBean(HRMManager.class);
  
%>
<ul id="attachedList"
	style="background-color: white; padding-left: 20px; list-style-position: outside; list-style-type: lower-roman;">
	<%
            ArrayList privatedocs = new ArrayList();
            privatedocs = EDocUtil.listDocs(loggedInInfo, demo, requestId, EDocUtil.ATTACHED);
            EDoc curDoc;                                        
            for(int idx = 0; idx < privatedocs.size(); ++idx)
            {                    
                curDoc = (EDoc)privatedocs.get(idx);                                            
        %>
	<li class="doc"><%=StringUtils.maxLenString(curDoc.getDescription(),19,16,"...")%></li>
	<%                                           
            }

                CommonLabResultData labData = new CommonLabResultData();
                ArrayList labs = labData.populateLabResultsData(loggedInInfo, demo, requestId, CommonLabResultData.ATTACHED);
                LabResultData resData;
                for(int idx = 0; idx < labs.size(); ++idx) 
                {
                    resData = (LabResultData)labs.get(idx);
        %>
	<li class="lab"><%=resData.getDiscipline()+" "+resData.getDateTime()%></li>
	<%
                }
                Integer iRequestId = null;
                if(requestId != null) {
                	try {
                		iRequestId = Integer.parseInt(requestId);
                	}catch(NumberFormatException e) {
                		iRequestId = null;
                	}
                }
                List<HRMDocument> hrmDocuments = hrmManager.findAttached(loggedInInfo, Integer.parseInt(demo), iRequestId);
                for(HRMDocument hrmDoc: hrmDocuments) {
                	String reportStatus = hrmDoc.getReportStatus();
					String t = StringUtils.isNullOrEmpty(hrmDoc.getDescription())?hrmDoc.getReportType():hrmDoc.getDescription();
					if (reportStatus != null && reportStatus.equalsIgnoreCase("C")) {
						t = "(Cancelled) " + t;
					}
     %>
     <li class="hrm"><%=t%></li>
     <%
                }
                
        %>
</ul>
<%
           if( privatedocs.size() == 0 && labs.size() == 0  && hrmDocuments.size() == 0) {
        %>
<p id="attachDefault"
	style="background-color: white; text-align: center;"><bean:message
	key="oscarEncounter.oscarConsultationRequest.AttachDoc.Empty" /></p>
<%
           }
         %>
