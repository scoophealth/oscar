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
			str='<%=(String)request.getAttribute("medicalHistory") %>';
			break;
		case 'fHis':
			str='<%=(String)request.getAttribute("familyHistory") %>';
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

	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Current History\" onclick=\"currentProAdd('cHis','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Past Ocular History\" onclick=\"currentProAdd('pHis','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input class=\"btn\" value=\"Medical History\" onclick=\"currentProAdd('mHis','clinicalInformation');\" type=\"button\">&nbsp");
	jQuery("#clinicalInfoButtonBar").append("<input class=\"btn\" value=\"Family History\" onclick=\"currentProAdd('fHis','clinicalInformation');\" type=\"button\">&nbsp");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Specs History\" onclick=\"currentProAdd('specs','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<br/>");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Diagnostic Notes\" onclick=\"currentProAdd('dTest','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Ocular Procedures\" onclick=\"currentProAdd('oProc','clinicalInformation');\"/>&nbsp;");
	jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Impression/Plan\" onclick=\"currentProAdd('impress','clinicalInformation');\"/>&nbsp;");

<%
	for(String customCppIssue:customCppIssues) {
		%>jQuery("#clinicalInfoButtonBar").append("<input type=\"button\" class=\"btn\" value=\"<%=customCppIssue %>\" onclick=\"currentProAdd('<%=customCppIssue %>','clinicalInformation');\"/>&nbsp;");<%
	}
%>


	jQuery("#concurrentProblemsButtonBar").html("");
	jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Family History\" onclick=\"currentProAdd('fHis','concurrentProblems');\" />&nbsp;");
	jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Medical History\" onclick=\"currentProAdd('mHis','concurrentProblems');\" />&nbsp;");
	jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Other Meds\" onclick=\"currentProAdd('otherMeds','concurrentProblems');\" />&nbsp;");

<%
	for(String customCppIssue:customCppIssues) {
		%>jQuery("#concurrentProblemsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"<%=customCppIssue %>\" onclick=\"currentProAdd('<%=customCppIssue %>','concurrentProblems');\"/>&nbsp;");<%
	}
%>

	jQuery("#medsButtonBar").html("");
	jQuery("#medsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Ocular Meds\" onclick=\"currentProAdd('oMeds','currentMedications');\"/>&nbsp;");
	jQuery("#medsButtonBar").append("<input type=\"button\" class=\"btn\" value=\"Other Meds\" onclick=\"currentProAdd('otherMeds','currentMedications');\"/>&nbsp;");

	jQuery("#ext_specialProblem").val('<%=request.getAttribute("specialProblem") %>');
	if(jQuery("[name='ext_appNo']").val() == 'null') {
		jQuery("[name='ext_appNo']").val('<%=request.getAttribute("appNo") %>');
	}




});
