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
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.LinkedList"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="java.io.File"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.hospitalReportManager.HRMReportParser"%>
<%@page import="org.oscarehr.hospitalReportManager.HRMReport"%>
<%@page import="org.oscarehr.hospitalReportManager.model.HRMDocument"%>
<%@page import="org.oscarehr.hospitalReportManager.dao.HRMDocumentDao"%>
<%@page import="org.oscarehr.common.model.OscarLog"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.log.LogAction"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page contentType="text/html"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%
String curUser_no = (String) session.getAttribute("user");

DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);

if("migrate".equals(request.getParameter("action"))) {
	
}
%>

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">

<script type="text/JavaScript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}
</script>
<html:base />

<title>Migrate HRM Data to new Schema</title>


</head>


<body>
        <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;width:80%">
            <tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px"></td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Migrate HRM</td>
                            <td style="text-align: right;"  > 
                                    <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                                    <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
         </table>
         <br/>
         
         <%if(request.getParameter("action") == null) { %>
       
        <form action="migrateHRMData.jsp">
          
        <h3>Update</h3>
        
        <p>This utility will update your current HRM data to use the new schema</p>
        <br/>
        
        
       
        	<input type="submit" value="Start Migration"/>
        	<input type="hidden" name="action" value="migrate"/>
        </form>
        
        <% } else { 
        	//loop all HRMDocuments
        	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
        	HRMDocumentDao hrmDocumentDao = SpringUtils.getBean(HRMDocumentDao.class);
        	boolean result =true;
        	
        	for(HRMDocument hrmDocument : hrmDocumentDao.findAll() ) {
        		List<Throwable> errors = new ArrayList<Throwable>();
        		String filename = hrmDocument.getReportFile();
        		filename = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + File.separator + filename;
        		HRMReport report = HRMReportParser.parseReport(LoggedInInfo.getLoggedInInfoFromSession(request), filename,errors);
        		if(report != null) {
        			hrmDocument.setSourceFacilityReportNo(report.getSendingFacilityReportNo());
        			String name = report.getLegalLastName() + ", " + report.getLegalFirstName();
        			for(String iName : report.getLegalOtherNames()) {
        				name  = name + " " + iName; 
        			}
        			hrmDocument.setFormattedName(name);
        			hrmDocument.setDob(report.getDateOfBirthAsString());
        			hrmDocument.setGender(report.getGender());
        			hrmDocument.setHcn(report.getHCN());
        			
        			hrmDocument.setClassName(report.getFirstReportClass());
        			hrmDocument.setSubClassName(report.getFirstReportSubClass());
        			
        			hrmDocument.setRecipientId(report.getDeliverToUserId());
        			hrmDocument.setRecipientName(report.getDeliveryToUserIdFormattedName());
        			
        			Provider sendToProvider = null;
        			String collegeId = report.getDeliverToUserId();
        			if(collegeId != null && !StringUtils.isEmpty(collegeId)) {
        				String collegeIdId = collegeId.substring(1); // We have to remove the first "D"
        				String collegeIdType ="" + collegeId.charAt(0);
        				if("D".equals(collegeIdType)) {
        					sendToProvider = providerDao.getProviderByPractitionerNo("CPSO",collegeIdId);
        				} else if("N".equals(collegeIdType)) {
        					sendToProvider = providerDao.getProviderByPractitionerNo(new String[] {"CNORNP","CNORN","CNORPN"}, collegeIdId);
        				}
        				
        			}
        		
        			if (sendToProvider != null) {	
        				hrmDocument.setRecipientProviderNo(sendToProvider.getProviderNo());
        			}
        			
        			hrmDocumentDao.merge(hrmDocument);
        		} else {
        			result=false;
        			MiscUtils.getLogger().warn("report is null " + filename);
        			for(Throwable error:errors) {
        				MiscUtils.getLogger().error("Error",error);
        			}
        		}
        	}
        	
        	
        	if(result) {
        		%><h4>Changes were successful</h4><%
        	} else {
        		%><h4>An Error Occurred</h4><%
        	}
        } %>
         
</body>

</html:html>
