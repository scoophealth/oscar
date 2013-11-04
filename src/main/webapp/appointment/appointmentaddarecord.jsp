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
<%@ page
	import="java.sql.*, java.util.*, oscar.MyDateFormat, oscar.oscarWaitingList.bean.*, oscar.oscarWaitingList.WaitingList, oscar.oscarDemographic.data.*, org.oscarehr.common.OtherIdManager, java.text.SimpleDateFormat, org.caisi.model.Tickler, org.caisi.service.TicklerManager,org.oscarehr.util.SpringUtils"
	errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.common.dao.DemographicDao, org.oscarehr.common.model.Demographic,oscar.appt.AppointmentMailer, org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.event.EventService"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
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
	String[] param = new String[20];
	param[0]=request.getParameter("provider_no");
	param[1]=request.getParameter("appointment_date");
	param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));

	//the keyword(name) must match the demographic_no if it has been changed
        org.oscarehr.common.model.Demographic demo = null;
    if (request.getParameter("demographic_no") != null && !(request.getParameter("demographic_no").equals(""))) {
        DemographicMerged dmDAO = new DemographicMerged();
        param[16] = dmDAO.getHead(request.getParameter("demographic_no"));

		DemographicData demData = new DemographicData();
		demo = demData.getDemographic(param[16]);
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
	param[17]=(String)request.getSession().getAttribute("programId_oscarView");
	param[18]=(request.getParameter("urgency")!=null)?request.getParameter("urgency"):"";
	param[19]=request.getParameter("createdatetime");
	int rowsAffected = oscarSuperManager.update("appointmentDao", request.getParameter("dboperation"), param);

	if (rowsAffected == 1) {

             //email patient appointment record
            if (request.getParameter("emailPt")!= null) {
                try{
                    String[] param3 = new String[7];
                    param3[0]=param[0]; //provider_no
                    param3[1]=param[1]; //appointment_date
                    param3[2]=param[2]; //start_time
                    param3[3]=param[3]; //end_time
                    param3[4]=param[13]; //createdatetime
                    param3[5]=param[14]; //creator
                    param3[6]=param[16]; //demographic_no

		    List<Map<String,Object>> resultList = oscarSuperManager.find("appointmentDao", "search_appt_no", param3);
                    if (resultList.size()>0) {
			Integer apptNo = (Integer)resultList.get(0).get("appointment_no");
                        DemographicDao demoDao = (DemographicDao) SpringUtils.getBean("demographicDao");
                        Demographic demographic = demoDao.getDemographic(param[16]);

                        if ((demographic != null) && (apptNo > 0)) {
                            AppointmentMailer emailer = new AppointmentMailer(apptNo,demographic);
                            emailer.prepareMessage();
                            emailer.send();
                        }
                    }

                }catch(Exception e) {
                    out.print(e.getMessage());
                }
            }


		// turn off reminder of "remove patient from the waiting list"
		oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
		String strMWL = pros.getProperty("MANUALLY_CLEANUP_WL");
		if (strMWL != null && strMWL.equalsIgnoreCase("yes")){
			;
		} else {
			WaitingList wL = WaitingList.getInstance();
			if (wL.getFound()) {
				List<Map<String,Object>> resultList = oscarSuperManager.find("appointmentDao", "search_waitinglist", new Object [] {request.getParameter("demographic_no")});
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

<script LANGUAGE="JavaScript">
	self.opener.refresh();
	self.close();
</script>

<%
		String[] param2 = new String[7];
		param2[0]=param[0]; //provider_no
		param2[1]=param[1]; //appointment_date
		param2[2]=param[2]; //start_time
		param2[3]=param[3]; //end_time
		param2[4]=param[13]; //createdatetime
		param2[5]=param[14]; //creator
		param2[6]=param[16]; //demographic_no

		List<Map<String,Object>> resultList = oscarSuperManager.find("appointmentDao", "search_appt_no", param2);
		if (resultList.size()>0) {
			Integer apptNo = (Integer)resultList.get(0).get("appointment_no");
			String mcNumber = request.getParameter("appt_mc_number");
			OtherIdManager.saveIdAppointment(apptNo, "appt_mc_number", mcNumber);
			
			EventService eventService = SpringUtils.getBean(EventService.class);
			eventService.appointmentCreated(this,apptNo.toString(), param[0]); // called when adding an appointment
			
		}

	} else {
%>
<p>
<h1><bean:message key="appointment.addappointment.msgAddFailure" /></h1>

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
