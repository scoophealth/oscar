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

<%@ page import="org.oscarehr.common.service.FieldNoteManager" %>
<%@ page import="org.oscarehr.common.model.EFormValue" %>
<%@ page import="java.util.*" %>
<%@ page import="oscar.util.StringUtils" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
	String residentId = request.getParameter("residentId");
	String residentName = request.getParameter("residentName");
	String method = request.getParameter("method");
	String dateStart = request.getParameter("date_start");
	String dateEnd = request.getParameter("date_end");
	
	if (StringUtils.empty(residentName)) residentName = "no name";
	
	if ("download".equals(method)) {
		response.setContentType("application/msword");
		String filename = residentName.replace(", ","").replace(" ","") + ".doc";
		response.setHeader("Content-Disposition", "attachment; filename="+filename);
	}
	
	HashMap<String, String> purposes = new HashMap<String, String>();
	HashMap<String, String> roleSkills = new HashMap<String, String>();
	HashMap<String, String> impressions = new HashMap<String, String>();
	HashMap<String, String> clinicalDomains = new HashMap<String, String>();
	
	purposes.put("case.discussion", "Case Discussion");
	purposes.put("direct.observation", "Direct Observation");
	purposes.put("chart.review", "Chart Review");
	
	roleSkills.put("fm.expert", "FM Expert");
	roleSkills.put("knowledge", "Knowledge");
	roleSkills.put("comprehensiveness", "Comprehensiveness");
	roleSkills.put("communicator", "Ccommunicator");
	roleSkills.put("collaborator", "Collaborator");
	roleSkills.put("professional", "Professional");
	roleSkills.put("advocate", "Advocate");
	roleSkills.put("scholar", "Scholar");
	roleSkills.put("manager", "Manager");
	
	//added numbering to keys for sorting purpose
	impressions.put("can.teach", "Can Teach");
	impressions.put("performs.with.independence", "Performs with Independence");
	impressions.put("minimal.supervision", "Minimal Supervision");
	impressions.put("close.supervision", "Close Supervision");
	impressions.put("supervisor.takeover", "Supervisor Required to Take Over");
	
	//added numbering to key "n/a" to put it at first
	clinicalDomains.put("n/a", "N/A");
	clinicalDomains.put("adults", "Adults");
	clinicalDomains.put("care of the elderly", "Care of the Elderly");
	clinicalDomains.put("children and adolescents", "Children and Adolescents");
	clinicalDomains.put("mental health", "Mental Health/Behavioural Science");
	clinicalDomains.put("newborn", "Newborn");
	clinicalDomains.put("obstetrics", "Obstetrics");
	clinicalDomains.put("palliative care", "Palliative Care");
	clinicalDomains.put("vulnerable population", "Vulnerable Population");
	clinicalDomains.put("women's health", "Women's Health");
	
	HashMap<Integer, List<EFormValue>> residentFieldNoteValues = FieldNoteManager.getResidentFieldNoteValues(residentId);
%>
<html:html locale="true">
<head>
<title><bean:message key="admin.fieldNote.report"/></title>
<link rel="stylesheet" href="../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" href="../../share/css/eformStyle.css">
<style>
	td { font-size: small; }
</style>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
</head>

<body>
<div class="eformInputHeading" align="center"><bean:message key="admin.fieldNote.report"/></div>

<%	if ("download".equals(method)) { //if-start
%>
<table width="100%">
	<tr>
		<td>
			Resident : <%= residentName %><br/>
			Report dates : <%= dateStart %> ~ <%= dateEnd %><br/>
			Total field notes : <%= FieldNoteManager.getTotalNumberOfFieldNotes(residentId) %><br/>
			<br/>
<%	for (String purpose : purposes.keySet()) {
%>			<%= purposes.get(purpose) %> : <%= FieldNoteManager.countItem(residentFieldNoteValues, purpose) %><br/>
<%	}
%>			MHBS tutorial : <%= FieldNoteManager.countItem(residentFieldNoteValues, "location", "BS tutorial") %>
		</td>
		<td>
<%	for (String roleSkill : roleSkills.keySet()) {
%>		<%= roleSkills.get(roleSkill) %> : <%= FieldNoteManager.countItem(residentFieldNoteValues, roleSkill) %><br/>
<%	}
%>		</td>
	</tr>
</table>

<%	for (String impression : impressions.keySet()) {
		HashMap<Integer, List<EFormValue>> fieldNoteValues_impression = FieldNoteManager.filterResidentFieldNoteValues(residentFieldNoteValues, impression);
%>
		<hr/>
		<%= impressions.get(impression) %> (<%= fieldNoteValues_impression.size() %>)<br/>
		<br/>
<%		if (fieldNoteValues_impression.isEmpty()) {
%>			<div style="color: #707070;">No field note</div>
<%			continue;
		}
		for (String clinicalDomain : clinicalDomains.keySet()) {
			HashMap<Integer, List<EFormValue>> fieldNoteValues_clinicalDomain = FieldNoteManager.filterResidentFieldNoteValues(fieldNoteValues_impression, "clinical_domain", clinicalDomain);
			if (fieldNoteValues_clinicalDomain.isEmpty()) continue;
%>
			<div style="font-weight: bold;" colspan="2">
				Clinical Domain : <%= clinicalDomains.get(clinicalDomain) %> (<%= fieldNoteValues_clinicalDomain.size() %>)
			</div>

<%			for (Integer fdid : fieldNoteValues_clinicalDomain.keySet()) {
				String topic = FieldNoteManager.getValues(fieldNoteValues_clinicalDomain.get(fdid), "clinical.topic", "clinical.topic2", "clinical.topic3", "clinical.topic4");
				String doneWell = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "done.well");
				String workOn = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "work.on");
				String followUp = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "follow-up");
				String apptDate = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "dateField");
				
				String residentRoleSkill = new String();
				for (EFormValue eformValue : fieldNoteValues_clinicalDomain.get(fdid))
				{
					if (roleSkills.containsKey(eformValue.getVarName()))
					{
						if (StringUtils.empty(residentRoleSkill)) residentRoleSkill = roleSkills.get(eformValue.getVarName());
						else residentRoleSkill += ", " + roleSkills.get(eformValue.getVarName());
					}
				}

%>				<table width="100%" style="background-color: #F2F2F2;">
					<tr>
						<td width="15%">Topic(s):</td>
						<td><%= topic %>
					</tr>
<%				if (StringUtils.filled(residentRoleSkill)) {
%>					<tr>
						<td>Role/Skill(s):</td>
						<td><%= residentRoleSkill %>
					</tr>
<%				}
				if (StringUtils.filled(doneWell)) {
%>					<tr>
						<td>Done well:</td>
						<td><%= doneWell %>
					</tr>
<%				}
				if (StringUtils.filled(workOn)) {
%>					<tr>
						<td>Work on:</td>
						<td><%= workOn %>
					</tr>
<%				}
				if (StringUtils.filled(followUp)) {
%>					<tr>
						<td>Follow-up:</td>
						<td><%= followUp %>
					</tr>
<%				}
%>				<tr>
					<td>Date:</td>
					<td><%= apptDate %>
				</tr>
				</table><br/>
<%			}
		}
	}
%>

<%	} else {
%>
<table width="100%">
	<tr>
		<td valign="top">
			<input type="button" value="<bean:message key="admin.fieldNote.close" />" onclick="window.close();" />
		</td>
		<td>
			<table>
				<tr>
					<td>Resident</td>
					<td>:</td>
					<td><%= residentName %></td>
				</tr>
				<tr>
					<td>Report dates</td>
					<td>:</td>
					<td><%= dateStart %> ~ <%= dateEnd %>
				</tr>
				<tr>
					<td>Total field notes</td>
					<td>:</td>
					<td><%= FieldNoteManager.getTotalNumberOfFieldNotes(residentId) %></td>
				</tr>
			</table>
			<br/>
			<table>
			
<%	for (String purpose : purposes.keySet()) {
%>				<tr>
					<td><%= purposes.get(purpose) %></td>
					<td>:</td>
					<td><%= FieldNoteManager.countItem(residentFieldNoteValues, purpose) %></td>
				</tr>
<%	}
%>				<tr>
					<td>MHBS tutorial</td>
					<td>:</td>
					<td><%= FieldNoteManager.countItem(residentFieldNoteValues, "location", "BS tutorial") %></td>
				</tr>
			</table>
		</td>
		<td valign="top">
			<table>

<%	for (String roleSkill : roleSkills.keySet()) {
%>				<tr>
					<td><%= roleSkills.get(roleSkill) %></td>
					<td>:</td>
					<td><%= FieldNoteManager.countItem(residentFieldNoteValues, roleSkill) %></td>
				</tr>
<%	}
%>			</table>
		</td>
	</tr>
</table>
<br/>

<table width="100%">
<%	for (String impression : impressions.keySet())
	{
		HashMap<Integer, List<EFormValue>> fieldNoteValues_impression = FieldNoteManager.filterResidentFieldNoteValues(residentFieldNoteValues, impression);
%>	<tr>
		<td class="eformInputHeadingActive" colspan="2">
			<%= impressions.get(impression) %>
			(<%= fieldNoteValues_impression.size() %>)
		</td>
	</tr>
	<tr><td width="10%">&nbsp;</td></tr>
	
<%		if (fieldNoteValues_impression.isEmpty()) {
%>	<tr>
		<td colspan="2">
			<div style="color: #707070;">No field note</div>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%			continue;
		}

		for (String clinicalDomain : clinicalDomains.keySet())
		{
			HashMap<Integer, List<EFormValue>> fieldNoteValues_clinicalDomain = FieldNoteManager.filterResidentFieldNoteValues(fieldNoteValues_impression, "clinical_domain", clinicalDomain);
			if (fieldNoteValues_clinicalDomain.isEmpty()) continue;
%>	<tr>
		<td style="font-weight: bold;" colspan="2">
			Clinical Domain : <%= clinicalDomains.get(clinicalDomain) %>
			(<%= fieldNoteValues_clinicalDomain.size() %>)
		</td>
	</tr>
<%			for (Integer fdid : fieldNoteValues_clinicalDomain.keySet())
			{
				String topic = FieldNoteManager.getValues(fieldNoteValues_clinicalDomain.get(fdid), "clinical.topic", "clinical.topic2", "clinical.topic3", "clinical.topic4");
				String doneWell = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "done.well");
				String workOn = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "work.on");
				String followUp = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "follow-up");
				String apptDate = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "dateField");
				
				String residentRoleSkill = new String();
				for (EFormValue eformValue : fieldNoteValues_clinicalDomain.get(fdid))
				{
					if (roleSkills.containsKey(eformValue.getVarName()))
					{
						if (StringUtils.empty(residentRoleSkill)) residentRoleSkill = roleSkills.get(eformValue.getVarName());
						else residentRoleSkill += ", " + roleSkills.get(eformValue.getVarName());
					}
				}
%>	<tr style="background-color: #F2F2F2;">
		<td>Topic(s):</td>
		<td><%= topic %>
	</tr>
<%				if (StringUtils.filled(residentRoleSkill)) {
%>	<tr style="background-color: #F2F2F2;">
		<td>Role/Skill(s):</td>
		<td><%= residentRoleSkill %>
	</tr>
<%				}
				if (StringUtils.filled(doneWell)) {
%>	<tr style="background-color: #F2F2F2;">
		<td>Done well:</td>
		<td><%= doneWell %>
	</tr>
<%				}
				if (StringUtils.filled(workOn)) {
%>	<tr style="background-color: #F2F2F2;">
		<td>Work on:</td>
		<td><%= workOn %>
	</tr>
<%				}
				if (StringUtils.filled(followUp)) {
%>	<tr style="background-color: #F2F2F2;">
		<td>Follow-up:</td>
		<td><%= followUp %>
	</tr>
<%				}
%>	<tr style="background-color: #F2F2F2;">
		<td>Date:</td>
		<td><%= apptDate %>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%			}
		}
	}
%>
</table>
<br/>

<input type="button" value="<bean:message key="admin.fieldNote.close" />" onclick="window.close();" />
<%	} //if-end
%>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script>
	$( document ).ready(function() {
		parent.parent.resizeIframe($('html').height());
	});
</script>
</body>
</html:html>
