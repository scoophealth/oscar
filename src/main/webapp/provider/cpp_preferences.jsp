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

<%@ include file="/casemgmt/taglibs.jsp"%>
<%@ page import="org.oscarehr.provider.web.CppPreferencesUIBean" %>
<%
  String curUser_no;
  curUser_no = (String) session.getAttribute("user");

  boolean bFirstLoad = request.getAttribute("status") == null;    

  CppPreferencesUIBean bean = (CppPreferencesUIBean)request.getAttribute("bean"); 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title><bean:message key="provider.cppPrefs" /></title>

<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<link rel="stylesheet" type="text/css" media="all" href="<c:out value="${ctx}"/>/share/calendar/calendar.css" title="win2k-cold-1">
<script src="<c:out value="${ctx}"/>/share/javascript/prototype.js"	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/scriptaculous.js"	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/js/jquery.js"></script>
<script>
	jQuery.noConflict();
</script>

<script type="text/javascript">            
            
	function validate() {                
		//make sure none of the positions are duplicates
		if(getTotalPos("R1I1") > 1) { alert("You have a duplicate for Row 1, Column 1..Please fix."); return false;}
		if(getTotalPos("R1I2") > 1) { alert("You have a duplicate for Row 1, Column 2..Please fix."); return false;}
		if(getTotalPos("R2I1") > 1) { alert("You have a duplicate for Row 2, Column 1..Please fix."); return false;}
		if(getTotalPos("R2I2") > 1) { alert("You have a duplicate for Row 2, Column 2..Please fix."); return false;}		
		return true;
	}
	
	function getTotalPos(value) {
		var total = 0;
		jQuery("select").each(function(){
			if(jQuery(this).val() == value) {
				total++;
			}
		});
		return total;
	}
</script>

</head>

<body class="BodyStyle" vlink="#0000FF">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message key="provider.setNoteStaleDate.msgPrefs" /></td>
		<td style="color: white" class="MainTableTopRowRightColumn"><bean:message key="provider.cppPrefs" /></td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
			<!-- form starts here -->
			<form action="<c:out value="${ctx}"/>/provider/CppPreferences.do?method=save" method="post" onSubmit="return validate();">
			<table width="100%" border="1">
				<tr>				
					<td colspan="2">
						<%=CppPreferencesUIBean.getCheckbox("Enable Custom EChart",CppPreferencesUIBean.ENABLE,bean.getEnable()) %>
					</td>
				
				</tr>
				<tr>
					<td>Social History</td>
					<td>
						<select name="<%=CppPreferencesUIBean.SOCIAL_HISTORY_POS%>">
							<%=CppPreferencesUIBean.getPositionSelect(bean.getSocialHxPosition()) %>							
						</select>
						<br/>
						<%=CppPreferencesUIBean.getCheckbox("Show Start Date",CppPreferencesUIBean.SOC_HX_START_DATE,bean.getSocialHxStartDate()) %>
						<%=CppPreferencesUIBean.getCheckbox("Show Resolution Date",CppPreferencesUIBean.SOC_HX_RES_DATE,bean.getSocialHxResDate()) %>													
					</td>											
				</tr>
				
				<tr>
					<td>Medical History</td>
					<td>
						<select name="<%=CppPreferencesUIBean.MEDICAL_HISTORY_POS%>">
							<%=CppPreferencesUIBean.getPositionSelect(bean.getMedicalHxPosition()) %>
						</select>
						<br/>
						<%=CppPreferencesUIBean.getCheckbox("Show Start Date",CppPreferencesUIBean.MED_HX_START_DATE,bean.getMedHxStartDate()) %>
						<%=CppPreferencesUIBean.getCheckbox("Show Resolution Date",CppPreferencesUIBean.MED_HX_RES_DATE,bean.getMedHxResDate()) %>													
						<%=CppPreferencesUIBean.getCheckbox("Show Treatment",CppPreferencesUIBean.MED_HX_TREATMENT,bean.getMedHxTreatment()) %>													
						<%=CppPreferencesUIBean.getCheckbox("Show Procedure Date",CppPreferencesUIBean.MED_HX_PROCEDURE_DATE,bean.getMedHxProcedureDate()) %>													
					</td>											
				</tr>
				
				<tr>
					<td>Ongoing Concerns</td>
					<td>
						<select name="<%=CppPreferencesUIBean.ONGOING_CONCERNS_POS%>">
							<%=CppPreferencesUIBean.getPositionSelect(bean.getOngoingConcernsPosition()) %>
						</select>
						<br/>
						<%=CppPreferencesUIBean.getCheckbox("Show Start Date",CppPreferencesUIBean.ONGOING_START_DATE,bean.getOngoingConcernsStartDate()) %>
						<%=CppPreferencesUIBean.getCheckbox("Show Resolution Date",CppPreferencesUIBean.ONGOING_RES_DATE,bean.getOngoingConcernsResDate()) %>													
						<%=CppPreferencesUIBean.getCheckbox("Show Problem Status",CppPreferencesUIBean.ONGOING_PROBLEM_STATUS,bean.getOngoingConcernsProblemStatus()) %>													
					</td>											
				</tr>
				
				<tr>
					<td>Reminders</td>
					<td>
						<select name="<%=CppPreferencesUIBean.REMINDERS_POS%>">
							<%=CppPreferencesUIBean.getPositionSelect(bean.getRemindersPosition()) %>
						</select>
						<br/>
						<%=CppPreferencesUIBean.getCheckbox("Show Start Date",CppPreferencesUIBean.REMINDERS_START_DATE,bean.getRemindersStartDate()) %>
						<%=CppPreferencesUIBean.getCheckbox("Show Resolution Date",CppPreferencesUIBean.REMINDERS_RES_DATE,bean.getRemindersResDate()) %>													
					</td>											
				</tr>																	
												
			</table>
			<input type="submit" value="Save Changes"/>
			</form>
			<!-- end of form -->
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
