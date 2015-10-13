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
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedProvider"%>
<%@page import="org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page contentType="text/html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.oscarehr.casemgmt.model.*, org.oscarehr.common.dao.DemographicDao, org.oscarehr.util.SpringUtils, org.oscarehr.common.model.Demographic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String demographicId = request.getParameter("demographicId");
	String remoteFacilityId = request.getParameter("remoteFacilityId");
	String remoteProviderId = request.getParameter("remoteProviderId");
	
	DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao"); 
	Demographic demographic = demographicDao.getDemographic(demographicId);
%>
<html>
<head>
<title>Follow-Up Request</title>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0" topmargin="10" onLoad="setfocus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000"> 
    <td height="40" width="10%"> </td>
    <td width="90%" align="left"> 
      <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4">Follow Up Request</font></b></font> 
      </p>
    </td>
  </tr>
</table>

<html:form method="post" action="/demographic/FollowUp">
<table width="100%" border="0" cellspacing="0" cellpadding="0"bgcolor="#EEEEFF">
<tr> 
      <td width="35%"><font color="#003366"><font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>Client Name : </b></font></font></td>
      <td colspan="2" width="65%">
		<div align="left">
			<input type="hidden" name="demographicId" value="<%=demographicId %>" />
			<%=demographic.getLastName() %>, <%=demographic.getFirstName() %>
  		</div>
	  </td>
</tr>
<tr>
	  <td width="35%"><font color="#003366"><font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>Remote Provider Name: </b></font></font></td>
      <td colspan="2" width="65%">
		<div align="left">
			<input type="hidden" name="remoteProviderId" value="<%=remoteProviderId%>" />
			<input type="hidden" name="remoteFacilityId" value="<%=remoteFacilityId%>" />
			<%
				FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
				providerPk.setIntegratorFacilityId(Integer.parseInt(remoteFacilityId));
				providerPk.setCaisiItemId(remoteProviderId);
				CachedProvider cachedProvider=CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(), providerPk);
			%>
			<%=cachedProvider.getLastName()+", "+cachedProvider.getFirstName()%>
  		</div>
	  </td>
</tr>
<tr>
	  <td width="35%"><font color="#003366"><font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>Note: </b></font></font></td>
      <td colspan="2" width="65%">
		<div align="left">
			<textarea name="followUpNote"></textarea>
  		</div>
	  </td>
</tr>	  
<tr> 
      <td><input type="button" onclick="window.close()" value="Cancel" name="Button"></td>
      <td><input type="submit" value="Submit" name="Button"></td>
      <td></td>
</tr>
	  
</table>
</html:form>
</body>
</html>
