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
<%@page import="oscar.OscarProperties"%>
<%java.util.Properties oscarVariables = OscarProperties.getInstance(); %>
function currentProAdd(val,ob) {
	var str='';
	switch(val) {
		case 'cHis':
			str='<%=(String)request.getAttribute("currentHistory") %>';
			break;
		case 'pHis':
			str='<%=(String)request.getAttribute("pastOcularHistory") %>';
			break;
		case 'oMeds':
			str='<%=(String)request.getAttribute("ocularMedication") %>';
			break;
		case 'dTest':
			str='<%=(String)request.getAttribute("diagnosticNotes") %>';
			break;
		case 'oProc':
			str='<%=(String)request.getAttribute("ocularProc") %>';
			break;
		case 'specs':
			str='<%=(String)request.getAttribute("specs") %>';
			break;
		case 'impress':
			str='<%=(String)request.getAttribute("impression") %>';
			break;
		case 'followup':
			str='<%=(String)request.getAttribute("followup") %>';
			break;
		case 'probooking':
			str='<%=(String)request.getAttribute("probooking") %>';
			break;
		case 'testbooking':
			str='<%=(String)request.getAttribute("testbooking") %>';
			break;
		case 'mHis':
			str='<%=(String)request.getAttribute("medHistory") %>';
			break;
		case 'fHis':
			str='<%=(String)request.getAttribute("famHistory") %>';
			break;
		case 'otherMeds':
			str='<%=(String)request.getAttribute("otherMeds") %>';
			break;
<%
	String customCppIssues[] = oscar.OscarProperties.getInstance().getProperty("encounter.custom_cpp_issues", "").split(",");
	for(String customCppIssue:customCppIssues) {
		%>
		case '<%=customCppIssue %>':
			str='<%=(String)request.getAttribute(customCppIssue) %>';
			break;
		<%
	}
%>

	}
	var prefix = "";
	if(jQuery("#"+ob).val().length>0 && str.length>0) {
		prefix = "\n\n";
	}
	jQuery("#"+ob).val(jQuery("#"+ob).val() + prefix + str);
}

//lets modify some existing HTML
jQuery(document).ready(function(){


	jQuery("#clinicalInfoButtonBar").html("");
<%  if (oscarVariables.getProperty("cme_js", "").equals("eyeform_DrJinapriya")) { %>
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Subjective\" onclick=\"currentProAdd('cHis','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Past Ocular History\" onclick=\"currentProAdd('pHis','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Medical History\" onclick=\"currentProAdd('mHis','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Ocular Diagnoses\" onclick=\"currentProAdd('fHis','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Specs History\" onclick=\"currentProAdd('specs','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<br/>");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Objective\" onclick=\"currentProAdd('dTest','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Ocular Procedures\" onclick=\"currentProAdd('oProc','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Impression/Plan\" onclick=\"currentProAdd('impress','clinicalInformation');\"/>&nbsp;");

<% } else { %>
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Current History\" onclick=\"currentProAdd('cHis','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Past Ocular History\" onclick=\"currentProAdd('pHis','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Medical History\" onclick=\"currentProAdd('mHis','clinicalInformation');\" />&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Family History\" onclick=\"currentProAdd('fHis','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Specs History\" onclick=\"currentProAdd('specs','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<br/>");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Diagnostic Notes\" onclick=\"currentProAdd('dTest','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Ocular Procedures\" onclick=\"currentProAdd('oProc','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Impression/Plan\" onclick=\"currentProAdd('impress','clinicalInformation');\"/>&nbsp;");
<% } %>
<%
	for(String customCppIssue:customCppIssues) {
		%>jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"<%=customCppIssue %>\" onclick=\"currentProAdd('<%=customCppIssue %>','clinicalInformation');\"/>&nbsp;");<%
	}
%>


	jQuery("#concurrentProblemsButtonBar").html("");
<%  if (oscarVariables.getProperty("cme_js", "").equals("eyeform_DrJinapriya")) { %>
	jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Ocular Diagnoses\" onclick=\"currentProAdd('fHis','concurrentProblems');\" />&nbsp;");
	jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Medical History\" onclick=\"currentProAdd('mHis','concurrentProblems');\" />&nbsp;");
	jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Systemic Meds\" onclick=\"currentProAdd('otherMeds','concurrentProblems');\" />&nbsp;");
	
<% } else { %>
	jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Family History\" onclick=\"currentProAdd('fHis','concurrentProblems');\" />&nbsp;");
	jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Medical History\" onclick=\"currentProAdd('mHis','concurrentProblems');\" />&nbsp;");
	jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Other Meds\" onclick=\"currentProAdd('otherMeds','concurrentProblems');\" />&nbsp;");
<% } %>

<%
	for(String customCppIssue:customCppIssues) {
		%>jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"<%=customCppIssue %>\" onclick=\"currentProAdd('<%=customCppIssue %>','concurrentProblems');\"/>&nbsp;");<%
	}
%>

	jQuery("#medsButtonBar").html("");
<%  if (oscarVariables.getProperty("cme_js", "").equals("eyeform_DrJinapriya")) { %>
	jQuery("#medsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Drops Administered This Visit\" onclick=\"currentProAdd('oMeds','currentMedications');\"/>&nbsp;");
	jQuery("#medsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Systemic Meds\" onclick=\"currentProAdd('otherMeds','currentMedications');\"/>&nbsp;");
	
<% } else { %>
	jQuery("#medsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Ocular Meds\" onclick=\"currentProAdd('oMeds','currentMedications');\"/>&nbsp;");
	jQuery("#medsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Other Meds\" onclick=\"currentProAdd('otherMeds','currentMedications');\"/>&nbsp;");
<% } %>
	<%
	String eyeform = oscarVariables.getProperty("cme_js");
	if(("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){
	%>
	document.getElementById("ext_specialProblem").innerHTML = "<%= request.getAttribute("specialProblem")%>";
	<%}else{ %>
	jQuery("#ext_specialProblem").val('<%=request.getAttribute("specialProblem") %>');
	<%} %>
	if(jQuery("[name='ext_appNo']").val() == 'null') {
		jQuery("[name='ext_appNo']").val('<%=request.getAttribute("appNo") %>');
	}




});
