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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.billing.CA.BC.model.Hl7Obx"%>
<%@page import="org.oscarehr.billing.CA.BC.model.Hl7Obr"%>
<%@page import="org.oscarehr.billing.CA.BC.dao.Hl7ObrDao"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.Hl7PidDao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.Hl7Pid" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.Hl7LinkDao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.Hl7Link" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.Hl7MessageDao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.Hl7Message" %>

<%
	String pid = request.getParameter("pid"),
	sign = request.getParameter("cmd_sign"),
	save = request.getParameter("cmd_save");
	
	Hl7PidDao pidDao = SpringUtils.getBean(Hl7PidDao.class);
	Hl7MessageDao messageDao = SpringUtils.getBean(Hl7MessageDao.class);
	Hl7LinkDao linkDao = SpringUtils.getBean(Hl7LinkDao.class);
	
	if(null != save){
		for(Object[] o : pidDao.findDocNotes(ConversionUtils.fromIntString(pid))) {
			Hl7Message hl7Message = (Hl7Message) o[1];
			int msgId = hl7Message.getId();
			Hl7Message h = messageDao.find(msgId);
			if(h != null) {
				h.setNotes(request.getParameter("notes"));
			}
		}
	}
	if(null != sign){
		Hl7Link h = linkDao.find(Integer.parseInt(pid));
		if(h != null) {
			h.setStatus("S");
			h.setProviderNo((String)session.getAttribute("user"));
			h.setSignedOn(new java.util.Date());
			linkDao.merge(h);
		}
		
	}
	if(null == pid){
		out.print("<script language=\"JavaScript\">window.close();</script>");
	}
	if(request.getParameter("viewed") != null){
		Hl7Link h = linkDao.find(Integer.parseInt(pid));
		if(h != null && !h.getStatus().equals("S")) {
			h.setStatus("A");
			linkDao.merge(h);
		}
		
	}
	
	for(Object[] o : pidDao.findSigned(ConversionUtils.fromIntString(pid))) {
		Hl7Pid hl7_pid = (Hl7Pid) o[0];
		Hl7Link hl7_link = (Hl7Link) o[1];
		Provider provider = (Provider) o[2];
		
		boolean signed = (hl7_link.getStatus() != null && hl7_link.getStatus().equalsIgnoreCase("S"));
		%>
		
		<html>
		<head>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
		<title>OSCAR PathNET - View Lab Report</title>
		<link rel="stylesheet" href="../../../share/css/oscar.css">
		<script language="JavaScript">
		window.opener.location.reload();
		function Sign(check) {
			check.form.submit();
		}
		</script>
		</head>
		<body>
		<form name="signForm" action="report.jsp" method="post">
		<table width="100%" class="DarkBG">
			<tr>
				<td height="40" width="25"></td>
				<td width="50%" align="left"><font color="#4D4D4D"><b><font
					size="4">oscar<font size="3">PathNET - View Lab Report</font></font></b></font>
				</td>
				<td align="right" class="Text" nowrap><%=(signed? ( provider.getLastName() != null ? "<b>Signed Off By: </b>" +  provider.getFormattedName() : "<b>Signed Off By Provider No.:</b> " + provider.getProviderNo()) + " on " + hl7_link.getSignedOn() : "" )%>
				<input type="checkbox" name="cmd_sign" onclick="Sign(this);"
					value="<%=pid%>" <%=(signed? "checked disabled" : "")%> /><input
					type="hidden" name="pid" value="<%=pid%>" />Sign</td>
			</tr>
		</table>
		<%
	}
	
	Hl7Pid hl7pid = pidDao.find(ConversionUtils.fromIntString(pid));
	if(hl7pid != null ){
		int age = 0;
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(hl7pid.getDateOfBirth());
		age = oscar.MyDateFormat.getAge(calendar.get(java.util.GregorianCalendar.YEAR), calendar.get(java.util.GregorianCalendar.MONTH), calendar.get(java.util.GregorianCalendar.DATE));
%>
<table width="100%">
	<tr>
		<td colspan="4" align="right"><input type="button" name="print"
			value="Print" onclick="window.print(); return false;" /></td>
	</tr>
	<tr>
		<td class="Text" width="100px">Patient:</td>
		<td class="Text"><%=hl7pid.getPatientName()%></td>
		<td class="Text" align="right">DOB:</td>
		<td class="Text" width="100px"><%= ConversionUtils.toDateString(hl7pid.getDateOfBirth())%></td>
	</tr>
	<tr>
		<td class="Text">PHN:</td>
		<td class="Text"><%=hl7pid.getExternalId()%></td>
		<td class="Text" align="right">Age:</td>
		<td class="Text"><%=age%></td>
	</tr>
	<tr>
		<td class="Text"></td>
		<td class="Text"></td>
		<td class="Text" align="right">Sex:</td>
		<td class="Text"><%=hl7pid.getSex()%></td>
	</tr>
	<tr>
		<td class="Text">Address:</td>
		<td class="Text" colspan="3"><%=hl7pid.getPatientAddress().replaceAll("\\\\\\.br\\\\", " ")%></td>
	</tr>
	<tr>
		<td class="Text">Phone:</td>
		<td class="Text"><%=hl7pid.getHomeNumber()%></td>
		<td class="Text"></td>
		<td class="Text"></td>
	</tr>
	<%
	}
	
	Hl7ObrDao obrDao = SpringUtils.getBean(Hl7ObrDao.class);
	for(Hl7Obr hl7obr : obrDao.findByPid(ConversionUtils.fromIntString(pid))) {
%>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr>
		<td class="Text">Lab:</td>
		<td class="Text" colspan="3"><%=hl7obr.getFillerOrderNumber().substring(0, hl7obr.getFillerOrderNumber().indexOf("-", 3))%></td>
	</tr>
	<tr>
		<td class="Text">Ordered By:</td>
		<td class="Text"><%=hl7obr.getOrderingProvider().replaceAll("~", ",<br/>")%></td>
		<td class="Text">Requested On:</td>
		<td class="Text"><%=hl7obr.getRequestedDateTime()%></td>
	</tr>
	<tr>
		<td class="Text">Copies To:</td>
		<td class="Text"><%=hl7obr.getResultCopiesTo().replaceAll("~", ",<br/>")%></td>
		<td class="Text">Observed On:</td>
		<td class="Text"><%=hl7obr.getOberservationDateTime()%></td>
	</tr>
	<%
	}
	
%>
</table>
<table cellspacing="0" cellpadding="0" width="100%">
	<%
	boolean other = true;
	String section = "";
	for(Object[] o : obrDao.findLabResultsByPid(ConversionUtils.fromIntString(pid))) {
		Hl7Obr hl7_obr = (Hl7Obr) o[0];
		Hl7Obx hl7_obx = (Hl7Obx) o[1];
		
		if(hl7_obx.getSetId() == null || ConversionUtils.fromIntString(hl7_obx.getSetId()) == 1) {
			if(!section.equalsIgnoreCase(hl7_obr.getDiagnosticServiceSectId())){
				section = hl7_obr.getDiagnosticServiceSectId();
			    %>
				<tr>
					<td colspan="7">&nbsp;</td>
				</tr>
				<tr>
					<td class="Section" colspan="7"><%=((hl7_obr.getDiagnosticServiceSectId() != null) ? hl7_obr.getDiagnosticServiceSectId() : "Other")%></td>
				</tr>
				<%
			}
			%>
			<tr>
				<td colspan="7">&nbsp;</td>
			</tr>
			<tr>
				<td class="Text" colspan="3"><b>Service Id:</b><%=hl7_obr.getUniversalServiceId().substring(hl7_obr.getUniversalServiceId().indexOf(" "))%></td>
				<td class="Text" nowrap><b>Last Modified:</b><%= hl7_obr.getResultsReportStatusChange()%></td>
				<td class="Text" nowrap colspan="3"><b>Result Status:</b><%=(hl7_obr.getResultStatus().equalsIgnoreCase("f")? "Final" : "Pending")%></td>
			</tr>
			<tr>
				<td class="Text" valign="top">Note:</td>
				<td class="Text" colspan="6"><%=hl7_obx.getNote().replaceAll("\\\\\\.br\\\\", " ")%>&nbsp;</td>
			</tr>
			<%
		}
		
		if(hl7_obx.getSetId() != null){
			if("1".equals(hl7_obx.getSetId())) {
				other = true;
				%>
				<tr>
					<td>&nbsp;</td>
					<td style="width: 15%;" class="Header">Test</td>
					<td style="width: 5%;" class="Header">Flags</td>
					<td style="width: 55%;" class="Header">Results</td>
					<td style="width: 8%;" class="Header">Range</td>
					<td style="width: 8%;" class="Header">Units</td>
					<td class="Header">Note</td>
				</tr>
				<%
			}
			%>
			<tr>
				<td>&nbsp;</td>
				<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><%=hl7_obx.getObservationIdentifier().substring(hl7_obx.getObservationIdentifier().indexOf(" "))%></td>
				<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><b><%=((hl7_obx.getAbnormalFlags().toUpperCase().equals("N"))? "&nbsp;" : oscar.Misc.check(hl7_obx.getAbnormalFlags(), "", "&nbsp;"))%></b></td>
				<td class="Text" class="<%=(other? "LightBG" : "WhiteBG")%>"><%=((hl7_obx.getAbnormalFlags().toUpperCase().equals("N"))? hl7_obx.getObservationResults() : "<b>" + hl7_obx.getObservationResults() + "</b>").replaceAll("\\\\\\.br\\\\", " ")%></td>
				<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><%=hl7_obx.getReferenceRange()%></td>
				<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><%=hl7_obx.getUnits()%></td>
				<td class="Text" nowrap
					title="<%=hl7_obx.getNote().replaceAll("\\\\\\.br\\\\", " ")%>"
					class="<%=(other? "LightBG" : "WhiteBG")%>"><%=((hl7_obx.getNote().length() < 20)? hl7_obx.getNote() : hl7_obx.getNote().substring(0, 20)).replaceAll("\\\\\\.br\\\\", " ")%></td>
			</tr>
			<%
		}
		other = !other;
	}
	
	for(Object[] o : pidDao.findDocNotes(ConversionUtils.fromIntString(pid))) {
		Hl7Message hl7_message = (Hl7Message) o[1];
%>
	<tr>
		<td colspan="7"><br>
		<b>Notes:</b></td>
	</tr>
	<tr>
		<td colspan="7"><textarea name="notes" rows="7"
			style="width: 100%;"><%=oscar.Misc.check(hl7_message.getNotes(), "")%></textarea></td>
	</tr>
	<tr class="LightBG">
		<td colspan="7" align="right"><input type="submit"
			name="cmd_save" value="Save" /></td>
	</tr>
</table>
<%
	}
%>
</form>
</body>
</html>
