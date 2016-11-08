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
	purposes.put("tape.review", "Tape Review");
	
	roleSkills.put("knowledge.base", "Knowledge base");
	roleSkills.put("clinical.reasoning", "Clinical reasoning");
	roleSkills.put("comprehensive.care", "Comprehensive care");
	roleSkills.put("selectivity", "Selectivity");
	roleSkills.put("communication.patient.centered", "Patient centered approach");
	roleSkills.put("communicator", "Communicator");
	roleSkills.put("collaborator", "Professional behaviour");
	roleSkills.put("advocate", "Advocate");
	roleSkills.put("manual.procedural.skill", "Manual and Procedural Skill");
	roleSkills.put("manager", "Manager");
	roleSkills.put("scholar", "Scholar (use of EBM)");
	
	//added numbering to keys for sorting purpose
	impressions.put("1.got.it", "5- Got it");
	impressions.put("2.impression.scale.4", "4-");
	impressions.put("3.getting.there", "3- Getting there");
	impressions.put("4.impression.scale.2", "2-");
	impressions.put("5.attention.required", "1- Attention required");
	
	//added numbering to key "n/a" to put it at first
	clinicalDomains.put("1.n/a", "N/A");
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
<script type="text/javascript">


</script>
</head>
<body>

<div class="eformInputHeading" align="center">
	<bean:message key="admin.fieldNote.report"/>
</div>

<%
	if (!"download".equals(method)) {
%>
<table width="100%">
	<tr>
		<td valign="top">
			<input type="button" value="<bean:message key="admin.fieldNote.back" />" onclick="window.close();" />
		</td>
		<td>
<%
	}
%>
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
			<p>&nbsp;</p>
			<table>
<%
	TreeSet<String> keys = new TreeSet<String>(purposes.keySet());
	for (String purpose : keys) {
%>
				<tr>
					<td><%= purposes.get(purpose) %></td>
					<td>:</td>
					<td><%= FieldNoteManager.countItem(residentFieldNoteValues, purpose) %></td>
				</tr>
<%
	}
%>
            <tr>
                    <td>MHBS tutorial</td>
                    <td>:</td>
                    <td><%= FieldNoteManager.countItem(residentFieldNoteValues, "location", "BS tutorial") %></td>
            </tr>
			</table>
<%
	if ("download".equals(method)) {
%>
			<p>&nbsp;</p>
<%
	} else {
%>
		</td>
		<td valign="top">
<%
	}
%>
			<table>
<%
	keys = new TreeSet<String>(roleSkills.keySet());
	for (String roleSkill : keys) {
%>
				<tr>
					<td><%= roleSkills.get(roleSkill) %></td>
					<td>:</td>
					<td><%= FieldNoteManager.countItem(residentFieldNoteValues, roleSkill) %></td>
				</tr>
<%
	}
%>
			</table>
<%
	if (!"download".equals(method)) {
%>
		</td>
	</tr>
</table>
<%
	}
%>
<p>&nbsp;</p>
<table>
<%
	keys = new TreeSet<String>(impressions.keySet());
	for (String impression : keys)
	{
		HashMap<Integer, List<EFormValue>> fieldNoteValues_impression = FieldNoteManager.filterResidentFieldNoteValues(residentFieldNoteValues, impression.substring(2));
%>
	<tr>
		<td class="eformInputHeadingActive" colspan="2">
			<%= impressions.get(impression) %>
			(<%= fieldNoteValues_impression.size() %>)
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%
		if (fieldNoteValues_impression.isEmpty()) {
%>
	<tr>
		<td colspan="2">
			<div style="color: #707070;">No field note</div>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%
			continue;
		}

		TreeSet<String> clinicalDomainKeys = new TreeSet<String>(clinicalDomains.keySet());
		for (String clinicalDomain : clinicalDomainKeys)
		{
			if (clinicalDomain.startsWith("1.")) clinicalDomain = clinicalDomain.substring(2); //This is for key "1.n/a"
			HashMap<Integer, List<EFormValue>> fieldNoteValues_clinicalDomain = FieldNoteManager.filterResidentFieldNoteValues(fieldNoteValues_impression, "clinical_domain", clinicalDomain);
			if (fieldNoteValues_clinicalDomain.isEmpty()) continue;
%>
	<tr>
		<td style="font-weight: bold;" colspan="2">
			Clinical Domain : <%= clinicalDomains.get(clinicalDomain) %>
			(<%= fieldNoteValues_clinicalDomain.size() %>)
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%			
			for (Integer fdid : fieldNoteValues_clinicalDomain.keySet())
			{
				String topic = FieldNoteManager.getValues(fieldNoteValues_clinicalDomain.get(fdid), "clinical.topic", "clinical.topic2", "clinical.topic3", "clinical.topic4");
				String doneWell = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "done.well");
				String workOn = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "work.on");
				String followUp = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "follow-up");
				String apptDate = FieldNoteManager.getValue(fieldNoteValues_clinicalDomain.get(fdid), "dateField");
				
				String residentRoleSkill = null;
				for (EFormValue eformValue : fieldNoteValues_clinicalDomain.get(fdid))
				{
					if (roleSkills.containsKey(eformValue.getVarName()))
					{
						if (StringUtils.empty(residentRoleSkill)) residentRoleSkill = roleSkills.get(eformValue.getVarName());
						else residentRoleSkill += "\n" + roleSkills.get(eformValue.getVarName());
					}
				}
%>
	<tr style="background-color: #F2F2F2;">
		<td>Topic(s):</td>
		<td><%= topic %>
	</tr>
<%
				if (StringUtils.filled(residentRoleSkill)) {
%>
	<tr style="background-color: #F2F2F2;">
		<td>Role/Skill(s):</td>
		<td><%= residentRoleSkill %>
	</tr>
<%
				}

				if (StringUtils.filled(doneWell)) {
%>
	<tr style="background-color: #F2F2F2;">
		<td>Done well:</td>
		<td><%= doneWell %>
	</tr>
<%
				}

				if (StringUtils.filled(workOn)) {
%>
	<tr style="background-color: #F2F2F2;">
		<td>Work on:</td>
		<td><%= workOn %>
	</tr>
<%
				}

				if (StringUtils.filled(followUp)) {
%>
	<tr style="background-color: #F2F2F2;">
		<td>Follow-up:</td>
		<td><%= followUp %>
	</tr>
<%
				}
%>
	<tr style="background-color: #F2F2F2;">
		<td>Date:</td>
		<td><%= apptDate %>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%
			}
		}
	}
%>
</table>
<%
    if (!"download".equals(method)) {
%>
        <p>&nbsp;</p>
        <input type="button" value="<bean:message key="admin.fieldNote.back" />" onclick="window.close();" />
<%
    }
%>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script>
$( document ).ready(function() {
parent.parent.resizeIframe($('html').height());      

});
</script>
</body>
</html:html>
