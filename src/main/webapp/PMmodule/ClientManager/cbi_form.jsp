<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed2) {
		return;
	}
%>

<%@page import="org.oscarehr.common.model.OcanStaffFormData"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.DemographicExt"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.dao.AdmissionDao"%>
<%@page import="org.oscarehr.common.dao.DemographicExtDao"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils" %>
<%@page import="org.oscarehr.common.model.FunctionalCentreAdmission" %>
<%@page import="org.oscarehr.common.dao.FunctionalCentreAdmissionDao" %>
<%@page import="org.oscarehr.common.model.FunctionalCentre" %>
<%@page import="org.oscarehr.common.dao.FunctionalCentreDao" %>
<%@page import="org.oscarehr.PMmodule.web.formbean.DemographicExtra"%>

<%@include file="/layouts/caisi_html_top-jquery.jspf"%>


<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String currentProgramId = (String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
    String view = (String) request.getParameter("view");
	String ocanType = request.getParameter("ocanType");
	int prepopulate = 0;
	prepopulate = Integer.parseInt(request.getParameter("prepopulate")==null?"0":request.getParameter("prepopulate"));
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	int ocanStaffFormId =0; boolean newForm=false;
	int referralCount = 0;
	if(request.getParameter("ocanStaffFormId")!=null && request.getParameter("ocanStaffFormId")!="") {
		ocanStaffFormId = Integer.parseInt(request.getParameter("ocanStaffFormId"));
	}
	OcanStaffForm ocanStaffForm = null;
	DemographicExtra demoExtra = null;	
	if(ocanStaffFormId != 0 ) {
		if(view!=null && "history".equals(view)) { //view from form history page, not populate from demographic, directly pull data from ocanStaffForm table.
			ocanStaffForm = OcanForm.getOcanStaffForm(Integer.valueOf(request.getParameter("ocanStaffFormId")));
		} else { //edit cbi form, still needs to populate data from demographic		
			ocanStaffForm = OcanForm.getCbiForm(Integer.valueOf(request.getParameter("ocanStaffFormId")), currentDemographicId,OcanForm.PRE_POPULATION_LEVEL_DEMOGRAPHIC,ocanType, Integer.valueOf(currentProgramId));
		}
	}else {	//new cbi form	
		ocanStaffForm = OcanForm.getCbiInitForm(currentDemographicId,OcanForm.PRE_POPULATION_LEVEL_DEMOGRAPHIC,ocanType, Integer.valueOf(currentProgramId));		
		if(ocanStaffForm!=null) {
			ocanStaffFormId = ocanStaffForm.getId()==null?0:ocanStaffForm.getId().intValue();
		}
	}

	//No matter if it's a new cbi form or not, always pre-populate phone extension.
	String phoneExt = "";
	DemographicExtDao demographicExtDao = (DemographicExtDao) SpringUtils.getBean("demographicExtDao");
	DemographicExt de = demographicExtDao.getDemographicExt(Integer.valueOf(currentDemographicId), "hPhoneExt");
	if(de!=null) {
		if(de.getValue()==null || de.getValue().equals("null"))
			phoneExt = "";
		else
			phoneExt = de.getValue();
	} else {
		//ocanStaffForm.setPhoneExt("");
		phoneExt = "";
	}
	
	DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	String hc_type = demographicDao.getDemographicById(currentDemographicId).getHcType();	
	
	String middleName="";
	String preferredName="";
	String lastNameAtBirth="";
	String maritalStatus="";
	String recipientLocation="";
	String lhinConsumerResides="";
	String address2="";
	
	DemographicExt de_middleName = demographicExtDao.getLatestDemographicExt(currentDemographicId, "middleName");	
	if(de_middleName != null) {
		middleName = de_middleName.getValue();
	}
	
	DemographicExt de_preferredName = demographicExtDao.getLatestDemographicExt(currentDemographicId, "preferredName");	
	if(de_preferredName != null) {
		preferredName = de_preferredName.getValue();
	}
	
	DemographicExt de_lastNameAtBirth = demographicExtDao.getLatestDemographicExt(currentDemographicId, "lastNameAtBirth");	
	if(de_lastNameAtBirth != null) {
		lastNameAtBirth = de_lastNameAtBirth.getValue();
	}
	
	DemographicExt de_maritalStatus = demographicExtDao.getLatestDemographicExt(currentDemographicId, "maritalStatus");	
	if(de_maritalStatus != null) {
		maritalStatus = de_maritalStatus.getValue();
	}
	
	DemographicExt de_recipientLocation = demographicExtDao.getLatestDemographicExt(currentDemographicId, "recipientLocation");	
	if(de_recipientLocation != null) {
		recipientLocation = de_recipientLocation.getValue();
	}
	
	DemographicExt de_lhinConsumerResides = demographicExtDao.getLatestDemographicExt(currentDemographicId, "lhinConsumerResides");	
	if(de_lhinConsumerResides != null) {
		lhinConsumerResides = de_lhinConsumerResides.getValue();
	}
	DemographicExt de_address2 = demographicExtDao.getLatestDemographicExt(currentDemographicId, "address2");	
	if(de_address2 != null) {
		address2 = de_address2.getValue();
	}
	
%>


<script type="text/javascript" src="<%=request.getContextPath()%>/PMmodule/ClientManager/ocan_staff_form_validation.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/check_hin.js"></script>

<script type="text/javascript">
function clearDate(el)
{
	$("#"+el).val("");
}
</script>		

<script>
//setup validation plugin
$("document").ready(function() {	
	$("#cbi_form").validate({meta: "validate"});
		
	$.validator.addMethod('postalCode', function (value) { 
	    return /^((\d{5}-\d{4})|(\d{5})|()|([A-Z]\d[A-Z]\s\d[A-Z]\d))$/.test(value); 
	}, 'Please enter a valid US or Canadian postal code.');

	$.validator.addMethod('digitalNumber', function(value) {
		 return /^((\d|\d{2}|\d{3}|\d{4}|()))$/.test(value);		  
	}, 'Digits only');
});

function checkHin() {
	var hin = document.adddemographic.hin.value;
	var province = document.adddemographic.hc_type.value;

	if (!isValidHin(hin, province))
	{
		alert ("You must type in the right HIN.");
		return(false);
	}

	return(true);
}
function checkDates() {
	var serviceInitDate = document.cbi_form.serviceInitDate.value;
	var admissionDate = document.getElementById("admissionDate").value;	
	var dischargeDate = document.getElementById("dischargeDate").value;	
	if(!serviceInitDate || typeof serviceInitDate == 'undefined') {
		alert("Please give the service initiation date on history page.");
		return false;		
	}
	if(!dischargeDate || typeof dischargeDate == 'undefined') {
		dischargeDate = "";
	}
	
	if(!compareDates(serviceInitDate, admissionDate)) {
		alert("The Service Initiation Date should be later or equal to the Admission Date.");
		return false;
	} 
	if(dischargeDate != "") {
		if(!compareDates(dischargeDate, admissionDate)) {
				alert("The Admission Date should be earlier or equal to the Discharge Date.");
				return false;
		} 
	}
	return true;
	
}

function compareDates(date1, date2) {	
	
	var aDateString = date1.split('-') ;	
	
	if(aDateString[1]=='01') aDateString[1]=1;
	if(aDateString[1]=='02') aDateString[1]=2;
	if(aDateString[1]=='03') aDateString[1]=3;
	if(aDateString[1]=='04') aDateString[1]=4;
	if(aDateString[1]=='05') aDateString[1]=5;
	if(aDateString[1]=='06') aDateString[1]=6;
	if(aDateString[1]=='07') aDateString[1]=7;
	if(aDateString[1]=='08') aDateString[1]=8;
	if(aDateString[1]=='09') aDateString[1]=9;
	
	if(aDateString[2]=='01') aDateString[2]=1;
	if(aDateString[2]=='02') aDateString[2]=2;
	if(aDateString[2]=='03') aDateString[2]=3;
	if(aDateString[2]=='04') aDateString[2]=4;
	if(aDateString[2]=='05') aDateString[2]=5;
	if(aDateString[2]=='06') aDateString[2]=6;
	if(aDateString[2]=='07') aDateString[2]=7;
	if(aDateString[2]=='08') aDateString[2]=8;
	if(aDateString[2]=='09') aDateString[2]=9;	
	
	var sDateString ;
	if(date2 && typeof date2 != 'undefined') {
		sDateString = date2.split('-') ; 
		if(sDateString[1]=='01') sDateString[1]=1;
		if(sDateString[1]=='02') sDateString[1]=2;
		if(sDateString[1]=='03') sDateString[1]=3;
		if(sDateString[1]=='04') sDateString[1]=4;
		if(sDateString[1]=='05') sDateString[1]=5;
		if(sDateString[1]=='06') sDateString[1]=6;
		if(sDateString[1]=='07') sDateString[1]=7;
		if(sDateString[1]=='08') sDateString[1]=8;
		if(sDateString[1]=='09') sDateString[1]=9;
		
		if(sDateString[2]=='01') sDateString[2]=1;
		if(sDateString[2]=='02') sDateString[2]=2;
		if(sDateString[2]=='03') sDateString[2]=3;
		if(sDateString[2]=='04') sDateString[2]=4;
		if(sDateString[2]=='05') sDateString[2]=5;
		if(sDateString[2]=='06') sDateString[2]=6;
		if(sDateString[2]=='07') sDateString[2]=7;
		if(sDateString[2]=='08') sDateString[2]=8;
		if(sDateString[2]=='09') sDateString[2]=9;		
		
		if (sDateString[0]>aDateString[0]) {		  
		  	return false;
		} else if(sDateString[0]==aDateString[0] && sDateString[1] > aDateString[1]) {
			return false;
		} else if(sDateString[0]==aDateString[0] && sDateString[1] ==aDateString[1] && sDateString[2] > aDateString[2]) {
			return false;
		} else {
			return true;
		}
	}
	return true;
}

function checkAdmission() {
	var sel = document.getElementById('admissionId');
	var admissionValue = sel.options[sel.selectedIndex].value; 
	if (sel.options[sel.selectedIndex].value == '') {	
		alert("The CBI form was already created for this functional centre.");
		return false;
	}
	return true;
}


function submitCbiForm() {
	//Homeless people may not have HIN. 
	//if ( !checkHin() ) return false;
	if(!checkDates()) return false;
	if(!checkAdmission()) return false;
	
	
	return true;
}

$('document').ready(function() {
	
	var demographicId='<%=currentDemographicId%>';	
	var ocanStaffFormId = '<%=ocanStaffFormId%>';
	var programId = '<%=currentProgramId%>';
	var view = '<%=view%>';
	
	if(document.getElementById("cbi_dates") == null) {
		$.get('cbi_get_dates.jsp?view='+view+'&ocanStaffFormId='+ocanStaffFormId+'&demographicId='+demographicId+'&admissionId=0&programId='+programId, function(data) {
			$("#center_block_dates").append(data);					 
			});				
	}
});
</script>

<script>

function changeFunctionalCentre(selectBox) {
	
	var selectBoxId = selectBox.id;	
	var selectBoxValue = selectBox.options[selectBox.selectedIndex].value;
	
	var demographicId='<%=currentDemographicId%>';	
	var ocanStaffFormId = '<%=ocanStaffFormId%>';
	var view = '<%=view%>';
	
	if(document.getElementById("admissionDate") == null) {	
			$.get('cbi_get_dates.jsp?view='+view+'&ocanStaffFormId='+ocanStaffFormId+'&demographicId='+demographicId+'&programId=0&admissionId='+selectBoxValue, function(data) {
				  $("#center_block_dates").append(data);					 
				});														
	} 
	else if(document.getElementById("admissionDate") !=null ) {		
		$("#cbi_dates").remove();
		$.get('cbi_get_dates.jsp?view='+view+'&ocanStaffFormId='+ocanStaffFormId+'&demographicId='+demographicId+'&programId=0&admissionId='+selectBoxValue, function(data) {
			  $("#center_block_dates").append(data);					 
			});	
	}
		
}

</script>


<style>
.error {color:red;}

.systemData
{
       background-color:rgb(254,254,184);
}	
	
.userInputedData	
{	
       background-color:rgb(0,254,254);	
}	
	
.mandatoryData	
{	
       border-color:rgb(233,75,68);	
       border-width:2px;	
       border-style:solid;	
}

</style>			
<form id="cbi_form" name="cbi_form" action="ocan_form_action.jsp" method="post" onsubmit="return submitCbiForm(); ">	
		
	<input type="hidden" name="client_date_of_birth" id="client_date_of_birth" value="<%=ocanStaffForm.getClientDateOfBirth()%>" />
	<input type="hidden" id="clientStartDate" name="clientStartDate" value="<%=ocanStaffForm.getFormattedClientStartDate()%>"/>
	<input type="hidden" id="clientCompletionDate" name="clientCompletionDate" value="<%=ocanStaffForm.getFormattedClientCompletionDate()%>"/>
<% if(prepopulate==1) { %> 
	<input type="hidden" name="ocanStaffFormId" id="ocanStaffFormId" value="" />
	<input type="hidden" name="assessmentId" id="assessmentId" value="" />
	
	<!-- client data start here	 -->
	<input type="hidden" name="client_1_1" id="client_1_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_1_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_1_1", prepopulationLevel).get(0).getAnswer():""%>" />
	
	<input type="hidden" name="client_1_comments" id="client_1_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_1_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_1_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_2_1" id="client_2_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_2_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_2_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_2_comments" id="client_2_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_2_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_2_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_3_1" id="client_3_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_3_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_3_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_3_comments" id="client_3_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_3_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_3_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_4_1" id="client_4_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_4_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_4_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_4_comments" id="client_4_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_4_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_4_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_5_1" id="client_5_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_5_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_5_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_5_comments" id="client_5_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_5_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_5_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_6_1" id="client_6_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_6_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_6_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_6_comments" id="client_6_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_6_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_6_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_7_1" id="client_7_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_7_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_7_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_7_comments" id="client_7_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_7_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_7_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_8_1" id="client_8_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_8_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_8_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_8_comments" id="client_8_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_8_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_8_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_9_1" id="client_9_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_9_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_9_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_9_comments" id="client_9_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_9_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_9_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_10_1" id="client_10_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_10_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_10_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_10_comments" id="client_10_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_10_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_10_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_11_1" id="client_11_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_11_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_11_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_11_comments" id="client_11_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_11_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_11_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_12_1" id="client_12_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_12_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_12_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_12_comments" id="client_12_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_12_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_12_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_13_1" id="client_13_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_13_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_13_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_13_comments" id="client_13_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_13_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_13_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_14_1" id="client_14_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_14_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_14_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_14_comments" id="client_14_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_14_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_14_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_15_1" id="client_15_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_15_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_15_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_15_comments" id="client_15_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_15_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_15_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_16_1" id="client_16_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_16_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_16_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_16_comments" id="client_16_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_16_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_16_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_17_1" id="client_17_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_17_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_17_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_17_comments" id="client_17_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_17_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_17_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_18_1" id="client_18_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_18_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_18_1", prepopulationLevel).get(0).getAnswer():""%>" />


	<input type="hidden" name="client_18_comments" id="client_18_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_18_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_18_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_19_1" id="client_19_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_19_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_19_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_19_comments" id="client_19_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_19_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_19_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_20_1" id="client_20_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_20_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_20_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_20_comments" id="client_20_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_20_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_20_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_21_1" id="client_21_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_21_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_21_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_21_comments" id="client_21_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_21_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_21_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_22_1" id="client_22_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_22_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_22_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_22_comments" id="client_22_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_22_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_22_comments", prepopulationLevel).get(0).getAnswer():""%>" />
					
	<input type="hidden" name="client_23_1" id="client_23_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_23_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_23_1", prepopulationLevel).get(0).getAnswer():""%>" />
		
	<input type="hidden" name="client_23_comments" id="client_23_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_23_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_23_comments", prepopulationLevel).get(0).getAnswer():""%>" />
				
	<input type="hidden" name="client_24_1" id="client_24_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_24_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_24_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_24_comments" id="client_24_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_24_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_24_comments", prepopulationLevel).get(0).getAnswer():""%>" />
		
	<input type="hidden" name="client_hopes_future" id="client_hopes_future" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_hopes_future", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_hopes_future", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_hope_future_need" id="client_hope_future_need" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_hope_future_need", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_hope_future_need", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_view_mental_health" id="client_view_mental_health" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_view_mental_health", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_view_mental_health", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_sprituality" id="client_sprituality" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_sprituality", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_sprituality", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_culture_heritage" id="client_culture_heritage" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_culture_heritage", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_culture_heritage", prepopulationLevel).get(0).getAnswer():""%>" />

		<!-- client data end here  -->
<% } else { %>
	<input type="hidden" name="ocanStaffFormId" id="ocanStaffFormId" value="<%=ocanStaffForm.getId()%>" />
	<input type="hidden" name="assessmentId" id="assessmentId" value="<%=ocanStaffForm.getAssessmentId()%>" />
<%} %>
	<input type="hidden" name="prepopulate" id="prepopulate" value="<%=prepopulate%>" />
	<input type="hidden" name="assessment_status" id="assessment_status" value="In Progress" />	
	<input type="hidden" name="submissionId" id="submissionId" value="0" />
	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">
	<tr><td><h3>CBI FORM</h3></td>
		<td></td>	
	</tr>
	<tr><td></td>
		<td></td>
	</tr>
		<tr>
			<td class="genericTableHeader">Select corresponding admission</td>
			<td class="genericTableData">			
					
					<%	
					FunctionalCentreAdmissionDao admissionDao = (FunctionalCentreAdmissionDao) SpringUtils.getBean("functionalCentreAdmissionDao");	
					FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");	
					String selected="";
					if(ocanStaffFormId==0)	{	//new form: list all programs IDs not used in cbi form.
					%>
						<select name="admissionId" id="admissionId" onchange="changeFunctionalCentre(this);" class="{validate: {required:true}}" >
						<option value=""> </option>
					<%
						for (FunctionalCentreAdmission admission : admissionDao.getDistinctAdmissionsByDemographicNo(Integer.valueOf(currentDemographicId)) )
						{	
							FunctionalCentre functionalCentre = functionalCentreDao.find(admission.getFunctionalCentreId());
							OcanStaffForm existingCbiForm = OcanForm.findLatestCbiFormsByFacilityAdmissionId(loggedInInfo.getCurrentFacility().getId(), Integer.valueOf(admission.getId()), null);
							if(existingCbiForm!=null) 
								continue;                                       
									                                                   
							%>
								<option <%=selected%> value="<%=admission.getId()%>"><%=functionalCentre.getDescription() %> - <%=DateFormatUtils.ISO_DATE_FORMAT.format(admission.getAdmissionDate()) %></option>
							<%						
						}
					  } else { //open existing form 
						  selected="selected";
						  FunctionalCentreAdmission ad = admissionDao.find(ocanStaffForm.getAdmissionId());
						  FunctionalCentre functionalCentre = functionalCentreDao.find(ad.getFunctionalCentreId());
					 %>
					 	<input type="hidden" name="admissionId" id="admissionId" value="<%=ocanStaffForm.getAdmissionId()%>" />
					 
					 	  <select name="admissionId_tmp" disabled>
						  <option value=""> </option>
					
						  <option <%=selected%> value="<%=ocanStaffForm.getAdmissionId()%>"><%=functionalCentre.getDescription() %> - <%=DateFormatUtils.ISO_DATE_FORMAT.format(ad.getAdmissionDate()) %></option>
					 <%
					  }
					
					%>
				</select>			
			</td>
		</tr>
		<tr><td colspan="2">
		
		<div id="center_block_dates">
		<!-- results from adding cbi form dates will go into this block -->
		</div>
		</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Last Name at Birth</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextFieldReadOnly(ocanStaffForm.getId(), "lastNameAtBirth", 32,  prepopulationLevel, true, lastNameAtBirth ) %>
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader">Current Last Name </td>
			<td class="genericTableData">
				<input type="text" name="lastName" id="lastName" readonly="readonly" value="<%=ocanStaffForm.getLastName()%>" size="32" maxlength="32"/>
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader">Middle Name</td>
			<td class="genericTableData">					
				<%=OcanForm.renderAsTextFieldReadOnly(ocanStaffForm.getId(), "middle", 32,  prepopulationLevel, true, middleName ) %>
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader">First Name</td>
			<td class="genericTableData">
				<input type="text" name="firstName" id="firstName" readonly="readonly" value="<%=ocanStaffForm.getFirstName()%>" size="32" maxlength="32" />
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Preferred Name</td>
			<td class="genericTableData">				
				<%=OcanForm.renderAsTextFieldReadOnly(ocanStaffForm.getId(), "preferred", 32,  prepopulationLevel, true, preferredName ) %>
			</td>
		</tr>			
		<tr>
			<td class="genericTableHeader">Address Line 1</td>
			<td class="genericTableData">
				<input type="text" name="addressLine1" id="addressLine1" readonly="readonly" value="<%=ocanStaffForm.getAddressLine1()%>" size="64" maxlength="64"/>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Address Line 2</td>
			<td class="genericTableData">				
				<input type="text" name="addressLine2" id="addressLine2" readonly="readonly" value="<%=ocanStaffForm.getAddressLine2()%>" size="64" maxlength="64"/>
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">City</td>
			<td class="genericTableData">
				<input type="text" name="city" id="city" readonly="readonly" value="<%=ocanStaffForm.getCity()%>" size="32" maxlength="32"/>
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Province</td>
			<td class="genericTableData">
			<input type="text" name="province" id="province" value="<%=ocanStaffForm.getProvince()%>" readonly="readonly" size="8" maxlength="8" />							
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Postal Code (e.g. M4H 2T1)</td>
			<td class="genericTableData">
				<input type="text" name="postalCode" id="postalCode" value="<%=ocanStaffForm.getPostalCode()%>" readonly="readonly" size="8" maxlength="8" class="{validate: {postalCode:true}}"/>
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Phone Number</td>
			<td class="genericTableData">
				<input type="text" name="phoneNumber" id="phoneNumber" readonly="readonly" value="<%=ocanStaffForm.getPhoneNumber()%>" size="32" maxlength="32"/>
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Ext: </td>
			<td class="genericTableData">				
				<input id="extension", name="extension" type="text" readonly="readonly" value="<%=phoneExt%>" />
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Email address</td>
			<td class="genericTableData">
				<input type="text" name="email" id="email" readonly="readonly" value="<%=ocanStaffForm.getEmail()%>" size="64" maxlength="64"/>
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Date of Birth (YYYY-MM-DD)</td>
			<td class="genericTableData">							
				<input id="date_of_birth" name="date_of_birth" onfocus="this.blur()" class="{validate: {required:true}}" type="text" readonly="readonly" value="<%=ocanStaffForm.getDateOfBirth()==null?"":ocanStaffForm.getDateOfBirth()%>" />		
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Estimated Age</td>
			<td class="genericTableData">				
				
				<input type="text" name="estimatedAge" id="estimatedAge" readonly="readonly" value="<%=ocanStaffForm.getEstimatedAge()%>" size="3" maxlength="3"/>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Health Card # and Version Code</td>
			<td class="genericTableData">
				<input type="text" name="hcNumber" id="hcNumber" readonly="readonly" value="<%=ocanStaffForm.getHcNumber()%>" size="32" maxlength="32" />
				<input type="text" name="hcVersion" id="hcVersion" readonly="readonly" value="<%=ocanStaffForm.getHcVersion()%>" size="2" maxlength="2"/>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Issuing Territory</td>
			<td class="genericTableData">
				<select name="issuingTerritory">				
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "issuingTerritory", OcanForm.getOcanFormOptions("Province List"),  hc_type, prepopulationLevel)%>
				</select>				
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Service Recipient Location</td>
			<td class="genericTableData">
				<select name="service_recipient_location">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "service_recipient_location", OcanForm.getOcanFormOptions("Recipient Location"), recipientLocation, prepopulationLevel)%>
				</select>					
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">LHIN Consumer Resides in</td>
			<td class="genericTableData">
				<select name="service_recipient_lhin">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "service_recipient_lhin", OcanForm.getOcanFormOptions("LHIN code"), lhinConsumerResides, prepopulationLevel)%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Gender</td>
			<td class="genericTableData">
			<input type="text" name="gender" id="gender" readonly="readonly" value="<%=ocanStaffForm.getGender()%>" />									
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Marital Status</td>
			<td class="genericTableData">
				<select name="marital_status">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "marital_status", OcanForm.getOcanFormOptions("Marital Status"), maritalStatus, prepopulationLevel)%>
				</select>					
			</td>
		</tr>
			
			<tr style="background-color:white">
			<td colspan="2">
				<br />
				<input type="hidden" name="clientId" id="clientId" value="<%=currentDemographicId%>" />
				<input type="hidden" name="ocanType" id="ocanType" value="<%=ocanType%>" />				
				Sign <input type="checkbox" name="signed" id="signed" <%=ocanStaffForm.isSigned()==true?"checked":"" %>/>
				<% if(view==null) {//can only save when edit from summary page. If open from form history page, only view.
				%>
				<input type="submit" name="submit" value="Save"/>&nbsp;&nbsp;&nbsp;&nbsp;			
				<% } %>
				<input type="button" name="cancel" value="Cancel" onclick="history.go(-1)" />
				
			</td>
		</tr>		
		</table>
	
</form>


<%@include file="/layouts/caisi_html_bottom2.jspf"%>
