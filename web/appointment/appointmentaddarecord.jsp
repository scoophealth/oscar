<%@ page
	import="java.sql.*, java.util.*, oscar.MyDateFormat, oscar.oscarWaitingList.bean.*, oscar.oscarWaitingList.WaitingList, oscar.oscarDemographic.data.*"
	errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
</head>
<body>
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="appointment.addappointment.msgMainLabel" /></font></th>
	</tr>
</table>
<%
	String[] param = new String[17];
	param[0]=request.getParameter("provider_no");
	param[1]=request.getParameter("appointment_date");
	param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
          
	//the keyword(name) must match the demographic_no if it has been changed
    if (request.getParameter("demographic_no") != null && !(request.getParameter("demographic_no").equals(""))) {
        DemographicMerged dmDAO = new DemographicMerged();
        param[16] = dmDAO.getHead(request.getParameter("demographic_no"));

		DemographicData demData = new DemographicData();
		DemographicData.Demographic demo = demData.getDemographic(param[16]);
		param[4] = demo.getLastName()+","+demo.getFirstName();
    } else {
        param[16] = "0";
		param[4] = request.getParameter("keyword");
    }

	param[5]=request.getParameter("notes");
	param[6]=request.getParameter("reason");
	param[7]=request.getParameter("location");
	param[8]=request.getParameter("resources");
	param[9]=request.getParameter("type");
	param[10]=request.getParameter("style");
	param[11]=request.getParameter("billing");
	param[12]=request.getParameter("status");
	param[13]=request.getParameter("createdatetime");
	param[14]=request.getParameter("creator");
	param[15]=request.getParameter("remarks");

	int rowsAffected = oscarSuperManager.update("appointmentDao", request.getParameter("dboperation"), param);

	if (rowsAffected == 1) {
		// turn off reminder of "remove patient from the waiting list"
		oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
		String strMWL = pros.getProperty("MANUALLY_CLEANUP_WL");
		if (strMWL != null && strMWL.equalsIgnoreCase("yes")){
			;
		} else {
			WaitingList wL = WaitingList.getInstance();
			if (wL.getFound()) {
				List<Map> resultList = oscarSuperManager.find("appointmentDao", "search_waitinglist", new Object [] {request.getParameter("demographic_no")});
				if (resultList.size() > 0) {
					Map wlEntry = resultList.get(0);
%>
<form name="updateWLFrm"
	action="../oscarWaitingList/RemoveFromWaitingList.jsp"><input
	type="hidden" name="listId"
	value="<%=wlEntry.get("listID")%>" /><input
	type="hidden" name="demographicNo"
	value="<%=request.getParameter("demographic_no")%>" /><script
	LANGUAGE="JavaScript">
		var removeList = confirm("Click OK to remove patient from the waiting list: <%=wlEntry.get("name")%>");                
		if (removeList) {
			document.forms[0].action = "../oscarWaitingList/RemoveFromWaitingList.jsp?demographicNo=<%=request.getParameter("demographic_no")%>&listID=<%=wlEntry.get("listID")%>";
			document.forms[0].submit();                      
		}
</script></form>
<%
				}
			}
		}
%>
<p>
<h1><bean:message key="appointment.addappointment.msgAddSuccess" /></h1>
</p>
<script LANGUAGE="JavaScript">
	self.opener.refresh();
	self.close();
</script>
<%
	} else {
%>
<p>
<h1><bean:message key="appointment.addappointment.msgAddFailure" /></h1>
</p>
<%  
	}
%>
<p></p>
<hr width="90%" />
<form>
<input type="button" value="<bean:message key="global.btnClose"/>" onClick="closeit()">
</form>
</center>
</body>
</html:html>
