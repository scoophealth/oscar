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
<%@page import="org.oscarehr.common.service.AcceptableUseAgreementManager"%>
<%@page import="oscar.OscarProperties, javax.servlet.http.Cookie, oscar.oscarSecurity.CookieSecurity, oscar.login.UAgentInfo" %>
<%@page import="org.apache.velocity.runtime.directive.Foreach"%>
<%@page import="org.oscarehr.common.service.AcceptableUseAgreementManager"%>
<%@page import="java.util.*" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.PMmodule.model.Program" %>
<%@ page import="org.oscarehr.common.model.Facility" %>
<%@ page import="org.oscarehr.PMmodule.service.ProviderManager" %>
<%@ page import="org.oscarehr.PMmodule.service.ProgramManager" %>
<%@ page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.caisi.service.InfirmBedProgramManager"%>
<%@ page import="org.apache.struts.util.LabelValueBean"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<p>&nbsp;</p>
<h3 align="center"><bean:message key="provider.selectClinicSite" /></h3>
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
</head>
<body>
<%
ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);

LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
String providerNo = loggedInInfo.getLoggedInProviderNo();
Facility facility = loggedInInfo.getCurrentFacility();

//List<Program> programs = programManager.getActiveProgramByFacility(providerNo, facility.getId());
InfirmBedProgramManager bpm = (InfirmBedProgramManager) SpringUtils.getBean("infirmBedProgramManager");
List<LabelValueBean> programs = bpm.getProgramBeans(providerNo, facility.getId());
Collections.sort(programs,
        new Comparator<LabelValueBean>()
        {
            public int compare(LabelValueBean f1, LabelValueBean f2)
            {
                return f1.getLabel().compareTo(f2.getLabel());
            }        
        });
int defaultprogramId = bpm.getDefaultProgramId(providerNo);

%>
<p>&nbsp;</p>
<table align="center">
   <tr>
    <td align="right" width="30%"><bean:message key="provider.clinicSite" />:</td>
     <td align="left" width="60%"> 
     
		<select id="programIdForLocation" name="programIdForLocation">
		<%
			if (programs != null && !programs.isEmpty()) {
		       	for (LabelValueBean program : programs) {
		       		String selected = (Integer.parseInt(program.getValue()) == defaultprogramId)?" selected=\"selected\" ":"";
		   	%>
		        <option value="<%=program.getValue()%>" <%=selected%>><%=StringEscapeUtils.escapeHtml(program.getLabel())%></option>
		    <%	}
		    }
		  	%>
		</select>
		<input type="button" value="Go -->" onClick="javascript:setLocation();" />  
     </td>
   </tr>
</table>
</body>
<script type="text/javascript">
function setLocation(){
	var programIdForLocation = jQuery("#programIdForLocation").val();
	window.location.href="provider/providercontrol.jsp?<%=org.oscarehr.util.SessionConstants.CURRENT_PROGRAM_ID%>="+programIdForLocation;
}
</script>
</html>
