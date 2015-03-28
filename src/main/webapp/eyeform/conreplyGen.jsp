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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarRx.data.RxPatientData"%>
<%@ include file="/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page import="org.oscarehr.eyeform.model.*"%>
<%@page import="org.oscarehr.eyeform.web.EyeformAction"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.DemographicContact"%>

<html:html>
<head>
<html:base />
<title>generate consultation report</title>

<style type="text/css">
table td {
	width: 100% padding : 0px;
	cell-spacing: 0;
	margin: 0;
	border: 0;
	font-size: 10pt;
}

.full input {
	border: 1px solid gray;
}

select {
	border: 1px solid gray;
}
</style>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String demographicNo = (String) request.getAttribute("demographicNo");
	org.oscarehr.common.model.Allergy[] allergies = RxPatientData.getPatient(loggedInInfo, Integer.parseInt(demographicNo)).getAllergies(loggedInInfo);
		String aller = "";
		for (int j = 0; j < allergies.length; j++) {
			aller += allergies[j].getShortDesc(13, 8,
					"...")
					+ ";";
		}
		String presc = "";

		oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
		oscar.oscarRx.data.RxPrescriptionData.Prescription[] arr = {};
		arr = prescriptData.getUniquePrescriptionsByPatient(Integer
				.parseInt(demographicNo));
		if (arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				String rxD = arr[i].getRxDate().toString();

				String rxP = arr[i].getFullOutLine().replaceAll(";",
						" ");
				rxP = rxP + "   " + arr[i].getEndDate();
				if (arr[i].getEndDate().after(new java.util.Date()))
					presc += rxD + "  " + rxP + "\n";
			}
		}
%>

<%
	request.setAttribute("sections",EyeformAction.getMeasurementSections());
	request.setAttribute("headers",EyeformAction.getMeasurementHeaders());
	request.setAttribute("providers",EyeformAction.getActiveProviders());
%>

<style type="text/css">
/* Used for "import from enctounter" button */
input.btn {
	color: black;
	font-family: 'trebuchet ms', helvetica, sans-serif;
	font-size: 84%;
	font-weight: bold;
	background-color: #B8B8FF;
	border: 1px solid;
	border-top-color: #696;
	border-left-color: #696;
	border-right-color: #363;
	border-bottom-color: #363;
}

td.tite {
	background-color: #bbbbFF;
	color: black;
	font-size: 12pt;
}

td.tite1 {
	background-color: #ccccFF;
	color: black;
	font-size: 12pt;
}

th,td.tite2 {
	background-color: #BFBFFF;
	color: black;
	font-size: 12pt;
}

td.tite3 {
	background-color: #B8B8FF;
	color: black;
	font-size: 12pt;
}

td.tite4 {
	background-color: #ddddff;
	color: black;
	font-size: 12pt;
}

td.stat {
	font-size: 10pt;
}

input.righty {
	text-align: right;
}
</style>
<link rel="stylesheet" type="text/css" href="css/encounterStyles.css">
</head>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<script type="text/javascript" src="<c:out value="${ctx}"/>/js/jquery.js"></script>
<script type="text/javascript" src="js/optiontransfer.js"></script>
<script type="text/javascript" language=javascript>
function confirmCompleted(btn) {
	var str="This report is tracked by online referral system and can be automatically completed if you select the 'completed, and not sent' option.\nAre you sure you want to mark it as 'complete and sent', which means you have to do everything manually?";
	return confirm(str);
}

function confirmPrint(btn) {
	var str="This referral is created and monitored automatically by CRM. Are you sure you want to print & fax it manually?";
	return confirm(str);
}


    con_cHis='<%=request.getAttribute("currentHistory")%>';
	con_oHis='<%=request.getAttribute("otherMeds")%>';
	con_pHis='<%=request.getAttribute("pastOcularHistory")%>';
	con_sHis='<%=request.getAttribute("specs")%>';
	con_diag='<%=request.getAttribute("diagnosticNotes")%>';
	con_impress='<%=request.getAttribute("impression")%>';
	con_mHis='<%=request.getAttribute("medHistory")%>';
	con_fHis='<%=request.getAttribute("famHistory")%>';
	con_oMeds='<%=request.getAttribute("ocularMedication")%>';
	con_probook='<%=request.getAttribute("probooking")%>';
	con_testbook='<%=request.getAttribute("testbooking")%>';
	con_ocularpro='<%=request.getAttribute("ocularProc")%>';
	con_follow='<%=request.getAttribute("followup")%>';
	con_aller='<%=StringEscapeUtils.escapeJavaScript(aller) %>';
	con_presc='<%=StringEscapeUtils.escapeJavaScript(presc) %>';

<%
	String customCppIssues[] = oscar.OscarProperties.getInstance().getProperty("encounter.custom_cpp_issues", "").split(",");
	for(String customCppIssue:customCppIssues) {
		%>
		con_<%=customCppIssue%>='<%=request.getAttribute(customCppIssue)%>';
		<%
	}
%>


  function trim(s) {
  	if (s==null || s=='') return s;
  	while (s.substring(0,1) == ' ') {
    	s = s.substring(1,s.length);
  	}
  	while (s.substring(s.length-1,s.length) == ' ') {
    	s = s.substring(0,s.length-1);
  	}
  return s;
}


 	function printsubmit(){
 		document.eyeForm.target='_top';
 		document.eyeForm.method.value='printConRequest';
 		document.eyeForm.submit();
 	}
 	function savesubmit(){
 		document.eyeForm.method.value='saveConRequest';
 		//window.close()
 		document.eyeForm.submit();
 	}
	function checkform(){
		if (document.eyeForm.elements['cp.referralNo'].value=='')
		{
			alert("Please choose the referral doctor.");
			return false;
		}else return true;
	}
	function addDoc(){
		if (document.eyeForm.elements["cp.cc"].value.length<=0)
			document.eyeForm.elements["cp.cc"].value=document.eyeForm.clDoctor.value;
		else document.eyeForm.elements["cp.cc"].value=document.eyeForm.elements["cp.cc"].value+"; "+document.eyeForm.clDoctor.value;
	}
	function addFamDoc(){
		var fd = $("#fam_doc").val();
		if (document.eyeForm.elements["cp.cc"].value.length<=0)
			document.eyeForm.elements["cp.cc"].value=fd;
		else document.eyeForm.elements["cp.cc"].value=document.eyeForm.elements["cp.cc"].value+"; "+ fd;
	}
	function clinicalInfoAdd(str,name){
		if (document.eyeForm.elements["cp.clinicalInfo"].value.length>0 && name!=null && trim(name)!='')
			document.eyeForm.elements["cp.clinicalInfo"].value+='\n\n';

		if (name!=null && trim(name)!='')
			document.eyeForm.elements["cp.clinicalInfo"].value+=name;
	}
	function ocluarproAdd(str,name){
		if (document.eyeForm.elements["cp.clinicalInfo"].value.length>0 && name!=null && trim(name)!='')
			document.eyeForm.elements["cp.clinicalInfo"].value+='\n\n';

		if (name!=null && trim(name)!='')
			document.eyeForm.elements["cp.clinicalInfo"].value+=name;
	}

	function allergiesAdd(){
		if (con_aller!=null && trim(con_aller)!='')
			document.eyeForm.elements["cp.allergies"].value+="Allergies:"+con_aller+"\n";
	}

	function prescriptionsAdd(){
		if (con_presc!=null && trim(con_presc)!='')
			document.eyeForm.elements["cp.allergies"].value+="Current Prescriptions:\n"+con_presc+"\n";
	}
	function currentMedsAdd(str){
		if (str!=null && trim(str)!='')
			document.eyeForm.elements["cp.allergies"].value+="Current Medications:\n"+str+"\n";
	}
	function ocularHisAdd(str){
		if (str!=null && trim(str)!='')
			document.eyeForm.elements["cp.allergies"].value+="Past Ocular History:\n"+str+"\n";
	}

	function impressionAdd(){
		if (con_impress!=null && trim(con_impress)!='')
			document.eyeForm.elements["cp.impression"].value+=con_impress+"\n";
	}
	function planAdd(val){
		document.eyeForm.elements["cp.plan"].value+=val;

	}
	function addExam(ob){
		var selected = new Array();
		for (var i = 0; i < ob.options.length; i++)
			if (ob.options[ i ].selected)
				selected.push(ob.options[ i ].value);
		for (var i = 0; i < selected.length; i++)
			addField(selected[i]);

	}
	function addField(val){
		var temps='';
		switch (val){
			case "specs":

				temps+=(trim(specs['od_'+val+'_sph'])=='')?'':('OD '+trim(specs['od_'+val+'_sph']));
				temps+=(trim(specs['od_'+val+'_cyl'])=='')?'':(trim(specs['od_'+val+'_cyl']));
				temps+=(trim(specs['od_'+val+'_axis'])=='')?'':('x'+trim(specs['od_'+val+'_axis']));
				temps+=(trim(specs['od_'+val+'_add'])=='')?'':(' add '+trim(specs['od_'+val+'_add']));
				temps+=(trim(specs['od_'+val+'_prism'])=='')?'\n':(' prism '+trim(specs['od_'+val+'_prism'])+'\n');
				temps+='      ';
				temps+=(trim(specs['os_'+val+'_sph'])=='')?'':('OS '+trim(specs['os_'+val+'_sph']));
				temps+=(trim(specs['os_'+val+'_cyl'])=='')?'':(trim(specs['os_'+val+'_cyl']));
				temps+=(trim(specs['os_'+val+'_axis'])=='')?'':('x'+trim(specs['os_'+val+'_axis']));
				temps+=(trim(specs['os_'+val+'_add'])=='')?'':(' add '+trim(specs['os_'+val+'_add']));
				temps+=(trim(specs['os_'+val+'_prism'])=='')?'\n':(' prism '+trim(specs['os_'+val+'_prism'])+'\n');
				if (trim(temps)!='\n      \n')
					document.eyeForm.elements['cp.examination'].value+='Specs:'+temps;
				break;
			case "ar":

				temps+=(trim(odMap['od_'+val+'_sph'])=='')?'':('OD '+trim(odMap['od_'+val+'_sph']));
				temps+=(trim(odMap['od_'+val+'_cyl'])=='')?'':(trim(odMap['od_'+val+'_cyl']));
				temps+=(trim(odMap['od_'+val+'_axis'])=='')?'\n':('x'+trim(odMap['od_'+val+'_axis'])+'\n');
				temps+='   ';
				temps+=(trim(osMap['os_'+val+'_sph'])=='')?'':('OS '+trim(osMap['os_'+val+'_sph']));
				temps+=(trim(osMap['os_'+val+'_cyl'])=='')?'':(trim(osMap['os_'+val+'_cyl']));
				temps+=(trim(osMap['os_'+val+'_axis'])=='')?'\n':('x'+trim(osMap['os_'+val+'_axis'])+'\n');
				if (trim(temps)!='\n   \n')
					document.eyeForm.elements['cp.examination'].value+='AR:'+temps;
				break;

			case "k":
				temps+=(trim(odMap['od_'+val+'1'])==''&&trim(odMap['od_'+val+'2'])==''&&trim(odMap['od_'+val+'2_axis'])=='')?'':'OD ';
				temps+=(trim(odMap['od_'+val+'1'])=='')?'':(''+trim(odMap['od_'+val+'1']));
				temps+=(trim(odMap['od_'+val+'2'])=='')?'':('x'+trim(odMap['od_'+val+'2']));
				temps+=(trim(odMap['od_'+val+'2_axis'])=='')?'\n':('@'+trim(odMap['od_'+val+'2_axis'])+'\n');
				temps+='  ';
				temps+=(trim(osMap['os_'+val+'1'])==''&&trim(osMap['os_'+val+'2'])==''&&trim(osMap['os_'+val+'2_axis'])=='')?'':'OS ';
				temps+=(trim(osMap['os_'+val+'1'])=='')?'':(''+trim(osMap['os_'+val+'1']));
				temps+=(trim(osMap['os_'+val+'2'])=='')?'':('x'+trim(osMap['os_'+val+'2']));
				temps+=(trim(osMap['os_'+val+'2_axis'])=='')?'\n':('@'+trim(osMap['os_'+val+'2_axis'])+'\n');
				if (trim(temps)!='\n  \n')
					document.eyeForm.elements['cp.examination'].value+='K:'+temps;
				break;
			case "manifest_refraction":

				temps+=(trim(odMap['od_'+val+'_sph'])=='')?'':('OD '+trim(odMap['od_'+val+'_sph']));
				temps+=(trim(odMap['od_'+val+'_cyl'])=='')?'':(trim(odMap['od_'+val+'_cyl']));
				temps+=(trim(odMap['od_'+val+'_axis'])=='')?'':('x'+trim(odMap['od_'+val+'_axis']));
				temps+=(trim(odMap['od_'+val+'_add'])=='')?'\n':(' add '+trim(odMap['od_'+val+'_add'])+'\n');
				temps+='                    ';
				temps+=(trim(osMap['os_'+val+'_sph'])=='')?'':('OS '+trim(osMap['os_'+val+'_sph']));
				temps+=(trim(osMap['os_'+val+'_cyl'])=='')?'':(trim(osMap['os_'+val+'_cyl']));
				temps+=(trim(osMap['os_'+val+'_axis'])=='')?'':('x'+trim(osMap['os_'+val+'_axis']));
				temps+=(trim(osMap['os_'+val+'_add'])=='')?'\n':(' add '+trim(osMap['os_'+val+'_add'])+'\n');
				if (trim(temps)!='\n                    \n')
					document.eyeForm.elements['cp.examination'].value+='Manifest refraction:'+temps;
				break;
			case "cycloplegic_refraction":

				temps+=(trim(odMap['od_'+val+'_sph'])=='')?'':('OD '+trim(odMap['od_'+val+'_sph']));
				temps+=(trim(odMap['od_'+val+'_cyl'])=='')?'':(trim(odMap['od_'+val+'_cyl']));
				temps+=(trim(odMap['od_'+val+'_axis'])=='')?'':('x'+trim(odMap['od_'+val+'_axis']));
				temps+=(trim(odMap['od_'+val+'_add'])=='')?'\n':(' add '+trim(odMap['od_'+val+'_add'])+'\n');
				temps+='                       ';
				temps+=(trim(osMap['os_'+val+'_sph'])=='')?'':('OS '+trim(osMap['os_'+val+'_sph']));
				temps+=(trim(osMap['os_'+val+'_cyl'])=='')?'':(trim(osMap['os_'+val+'_cyl']));
				temps+=(trim(osMap['os_'+val+'_axis'])=='')?'':('x'+trim(osMap['os_'+val+'_axis']));
				temps+=(trim(osMap['os_'+val+'_add'])=='')?'\n':(' add '+trim(osMap['os_'+val+'_add'])+'\n');
				if (trim(temps)!='\n                       \n')
				document.eyeForm.elements['cp.examination'].value+='Cycloplegic refraction:'+temps;
				break;
			case "EOM":
				temps+=(ouMap['EOM']=='')?'':'EOM:'+(trim(ouMap['EOM'])+'\n');
				if (trim(temps)!='')
					document.eyeForm.elements['cp.examination'].value+=temps;
				break;
			case "cd_ratio_horizontal":

				temps+=(trim(odMap['od_'+val])=='')?'':('OD '+trim(odMap['od_'+val])+'\n');
				temps+='          ';
				temps+=(trim(osMap['os_'+val])=='')?'\n':('OS '+trim(osMap['os_'+val])+'\n');
				if (trim(temps)!='' && trim(temps)!='\n')
					document.eyeForm.elements['cp.examination'].value+='c/d ratio:'+temps;
				break;
			case "angle":
				temps+=(trim(odMap['od_'+val+'_middle1'])=='')?'':('OD '+trim(odMap['od_'+val+'_middle1'])+'\n');
				temps+='      ';
				temps+=(trim(osMap['os_'+val+'_middle1'])=='')?'\n':('OS '+trim(osMap['os_'+val+'_middle1'])+'\n');
				if (trim(temps)!='' && trim(temps)!='\n')
					document.eyeForm.elements['cp.examination'].value+='angle:'+temps;
				break;
			default:
				var ts="";
				for(var i=0;i<val.length;i++) ts+=" ";

				temps+=(trim(odMap['od_'+val])=='')?'':('OD '+trim(odMap['od_'+val])+'\n');
				temps+=ts+' ';
				temps+=(trim(osMap['os_'+val])=='')?'\n':('OS '+trim(osMap['os_'+val])+'\n');
				if (trim(temps)!=null && trim(temps)!='' && trim(temps)!='\n')
					document.eyeForm.elements['cp.examination'].value+=val+':'+temps;
		}
		return;
	}
	var specs=[];
	specs['od_specs_sph']='';
	specs['od_specs_cyl']='';
	specs['od_specs_axis']='';
	specs['od_specs_add']='';
	specs['od_specs_prism']='';
	specs['os_specs_sph']='';
	specs['os_specs_cyl']='';
	specs['os_specs_axis']='';
	specs['os_specs_add']='';
	specs['os_specs_prism']='';
	var osMap=[];
	<c:forEach items="${sessionScope.osMap}" var="field">
    osMap['']='';
    </c:forEach>
    var ouMap=[];
	<c:forEach items="${sessionScope.ouMap}" var="field">
    ouMap['']='';
    </c:forEach>
    var odMap=[];
	<c:forEach items="${sessionScope.odMap}" var="field">
    odMap['>']='';
    </c:forEach>

	 function popupPageSmall(varpage,name) {
        var page = "" + varpage;
        windowprops = "height=300,width=700,location=no,"
          + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
        window.open(page, name, windowprops);
    }
    function sendTickler(){
    	var req="<%=request.getContextPath()%>/eyeform/Eyeform.do?method=specialRepTickler&demographicNo=<%=request.getAttribute("demographicNo")%>";
    	if (document.eyeForm.ack.checked==true){
    		req+='&docFlag=true';
    		popupPageSmall(req,'sendTickler');
    	}else{
    		alert("Please check the checkbox before click this button!");
    	}
    }
	function rs(n,u,w,h,x) {
  		args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=360,left=30";
  		remote=window.open(u,n,args);
  		if (remote != null) {
    		if (remote.opener == null)
      		remote.opener = self;
  		}
  		if (x == 1) { return remote; }
	}
	function referralScriptAttach2(elementName, name2) {
     var d = elementName;
     t0 = escape("document.forms[0].elements[\'"+d+"\'].value");
     t1 = escape("document.forms[0].elements[\'"+name2+"\'].value");
     rs('att',('<%=(String)session.getAttribute("oscar_context_path")%>/billing/CA/ON/searchRefDoc.jsp?param='+t0+'&param2='+t1),600,600,1);
	}
  </script>
   <c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
   <script src="<c:out value="${ctx}/js/jquery.js"/>"></script>
   <script>
     jQuery.noConflict();
   </script>

	<oscar:customInterface section="conreport"/>

<script>
var ctx;
var appointmentNo;

jQuery(document).ready(function() {
	ctx = '<%=request.getContextPath()%>';
	demoNo = '<%=demographicNo%>';
	appointmentNo = document.eyeForm.elements['cp.appointmentNo'].value;
});
</script>

</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<html:form action="/eyeform/Eyeform">
	<input type="hidden" name="method" value="saveConRequest"/>
	<input type="hidden" name="demographicNo" value="<%=EyeformAction.getField(request,"demographicNo")%>"/>
	<input type="hidden" name="referralNo" value=""/>
	<input type="hidden" name="otherDocId" value=""/>
	<input type="hidden" name="famDoctor" value=""/>
	<input type="hidden" name="apptno" value=""/>

	<html:hidden property="cp.id"/>
	<html:hidden property="cp.demographicNo"/>
	<html:hidden property="cp.providerNo"/>
	<html:hidden property="cp.appointmentNo"/>
	<html:hidden property="cp.urgency"/>
	<html:hidden property="cp.reason"/>
	<html:hidden property="cp.referralId"/>
	<html:hidden property="cp.referralNo"/>


	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn">Consultation report</td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td class="Header"
						style="padding-left: 1px; padding-right: 1px; border-right: 1px solid #003399; text-align: left; font-size: 80%; font-weight: bold; width: 100%;"
						NOWRAP><c:out value="${demographicName}"/> </td>
				</tr>
			</table>
			</td>
		</tr>

		<tr style="vertical-align: top">
			<td class="MainTableLeftColumn">
			<table>
				<tr>
					<td class="tite4" colspan="2"><c:if
						test="${requestScope.newFlag!='true' }">
						<table>
							<tr>
								<td class="stat" colspan="2">Created by:</td>

							</tr>
							<tr>
								<td><c:out value="${providerName}" /></td>
							</tr>
						</table>
					</c:if></td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">consultation report status:</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="cp.status"
								value="Incomplete" /></td>
							<td class="stat">Incomplete</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="cp.status"
								value="Completed,not sent" /></td>
							<td class="stat">Completed,not sent</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="cp.status" value="Completed,and sent" /></td>
							<td class="stat">Completed,and sent</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
			<td class="MainTableRightColumn">
			<table cellpadding="0" cellspacing="2"
				style="border-collapse: collapse; width: 100%" bordercolor="#111111"
				width="100%" height="100%" border=1>


				<tr>
					<td>
					<table border=0 width="100%">


						<tr>
							<td class="tite4">to:</td>
							<%
								String referralDocName = (String)request.getAttribute("referral_doc_name");
								if(referralDocName==null)
									referralDocName=new String();
							%>
							<td align="left" class="tite1"><input type="text"
								name="referral_doc_name" value="<%=referralDocName%>"/><a
								href="javascript:referralScriptAttach2('cp.referralNo','referral_doc_name')"><span
								style="font-size: 10;">Search #</span></a></td>
					</table>
					</td>
					<td valign="top" cellspacing="1" class="tite4">
					<table border=0 width="100%" bgcolor="white">
						<tr>
							<td class="tite4">re:</td>

							<td class="tite1"><c:out value="${reason}"/></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td width="50%" class="tite4">
					<table width="100%">
						<tr>
							<td class="tite4">
								<select id="fam_doc">
									<%
										List<DemographicContact> contacts = (List<DemographicContact>)request.getAttribute("contacts");
										for(DemographicContact c:contacts) {
											%><option value="<%=c.getContactName()%>"><%=c.getRole()%> - <%=c.getContactName() %></option><%
										}
									%>
								</select>
								&nbsp;
								<input type="button" class="btn" onclick="addFamDoc();" value="add to cc">
							</td>

							<td></td>
						</tr>
					</table>
					</td>
					<td class="tite4">
					<table width="100%">
						<tr>
							<td class="tite1" colspan="2">
								<input type="text" style="width:120px;" name="clDoctor" />
								<input type="button" class="btn" onclick="addDoc();" value="add to cc">
								<a href="javascript:referralScriptAttach2('otherDocId','clDoctor')">
								<span style="font-size: 10;">Search #</span></a> </td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="4">
					<table style="width: 100%">
						<tr>
							<td width="10%" class="tite4">cc:</td>
							<td width="90%" class="tite4"><html:text style="width:100%"
								property="cp.cc" /></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="4">
					<table style="background-color: #ddddff;" width="100%">
						<tr>
							<td class="tite4" width="10%">Greeting:</td>
							<td class="tite4" width="60%"><html:select
								property="cp.greeting">
								<html:option value="1">standard consult report</html:option>
								<html:option value="2">assessment report</html:option>
							</html:select></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan=2 class="tite4">
					<table width="100%">
						<tr>
							<td width="27%" class="tite4">Clinical information:</td>
							<td>
							<input type="button" class="btn" value="current hx"	name="chis"	onclick="clinicalInfoAdd('Current history:',con_cHis)">
							<input type="button" class="btn" value="past ocular hx" name="phis"	onclick="clinicalInfoAdd('Past ocular history:',con_pHis)">
							<input type="button" class="btn" value="medical hx" name="mhis"	onclick="clinicalInfoAdd('Medical history:',con_mHis)">
							<input type="button" class="btn" value="family hx" name="fhis" onclick="clinicalInfoAdd('Family history:',con_fHis)">
							<input type="button" class="btn" value="specs hx" name="shis" onclick="clinicalInfoAdd('Specs history:',con_sHis)">

							<input type="button" class="btn" value="ocular meds" name="ohis" onclick="clinicalInfoAdd('Ocular meds:',con_oMeds)">
							<input type="button" class="btn" value="other meds" name="ohis"	onclick="clinicalInfoAdd('Other meds:',con_oHis)">
							<input type="button" class="btn" value="diag notes" name="dnote" onclick="clinicalInfoAdd('Diagnostics notes:',con_diag)">
							<input type="button" class="btn" value="ocular proc" name="opro" onclick="ocluarproAdd('Ocular procedure:',con_ocularpro)">

<%
	for(String customCppIssue:customCppIssues) {
		%><input type="button" class="btn" value="<%=customCppIssue %>" name="<%=customCppIssue %>" onclick="clinicalInfoAdd('<%=customCppIssue%>:',con_<%=customCppIssue%>)"><%
	}
%>

							</td>
						</tr>
					</table>
					</td>
				</tr>



				<tr>
					<td colspan=2><html:textarea rows="4" style="width:100%"
						property="cp.clinicalInfo" /></td>
				<tr>
				<tr>
					<td colspan=2 class="tite4">
					<table width="100%">
						<tr>
							<td width="30%" class="tite4">Allergies and Medications:</td>
							<td><input type="button" class="btn" value="Allergies"
								name="allergies" onclick="allergiesAdd()"> <input
								type="button" class="btn" value="Prescriptions" name="prescript"
								onclick="prescriptionsAdd()"></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan=2><html:textarea rows="4" style="width:100%"
						property="cp.allergies" /></td>
				</tr>

				<tr>
					<td colspan=2 class="tite4">
					<table width="100%">
						<tr>
							<td width="30%" class="tite4">Examination field:</td>
						</tr>
					</table>

					</td>
				</tr>

				<tr>
					<td colspan=2 style="width: 100%">
					<table>
						<tr>
               		<td>
                			<select name="fromlist1" multiple="multiple" size="9" ondblclick="addSection(document.eyeForm.elements['fromlist1'],document.eyeForm.elements['fromlist2']);">
                				<c:forEach var="item" items="${sections}">
                					<option value="<c:out value="${item.value}"/>"><c:out value="${item.label}"/></option>
                				</c:forEach>
                			</select>
                		</td>
                		<td valign="middle">
                			<input type="button" value=">>" onclick="addSection(document.eyeForm.elements['fromlist1'],document.eyeForm.elements['fromlist2']);"/>
                		</td>
                		<td>
                			<select id="fromlist2" name="fromlist2" multiple="multiple" size="9" ondblclick="addExam(ctx,'fromlist2',document.eyeForm.elements['cp.examination'],appointmentNo);">
                				<c:forEach var="item" items="${headers}">
                					<option value="<c:out value="${item.value}"/>"><c:out value="${item.label}"/></option>
                				</c:forEach>
                			</select>
							<input style="vertical-align: middle;" type="button" value="add" onclick="addExam(ctx,'fromlist2',document.eyeForm.elements['cp.examination'],appointmentNo);">
						</td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan=2 style="width: 100%">
					<table style="width: 100%">
						<tr>
							<td width="74%"><html:textarea rows="7" style="width:100%"
								property="cp.examination" /></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan=2 class="tite4">
					<table width="100%">
						<tr>
							<td width="30%" class="tite4">Impression/Plan:</td>
							<td><input type="button" class="btn" value="Impression"
								name="impre" onclick="impressionAdd()"></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="2"><html:textarea rows="3" style="width:100%"
						property="cp.impression" /></td>
				</tr>
<!--
				<tr>
					<td colspan=2 class="tite4">
					<table width="100%">
						<tr>
							<td width="30%" class="tite4">Treatment/Follow-up:</td>
							<td><input type="button" class="btn" value="procedure"
								name="proce" onclick="planAdd(con_probook)"> <input
								type="button" class="btn" value="test" name="digtest"
								onclick="planAdd(con_testbook)"> <input type="button"
								class="btn" value="follow-up" name="follow"
								onclick="planAdd(con_follow)"></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="2"><html:textarea rows="4" style="width:100%"
						property="cp.plan" /></td>
				</tr>
-->
				<c:if test='${param.from!="out"}'>
					<tr>
						<td colspan=2 class="tite4">Send Tickler:</td>
					</tr>

					<tr>
						<td colspan="2"><input type="checkbox" name="ack" checked>
						remind me to complete it <input type="button" name="sendtickler"
							value="send tickler" onclick="sendConReportTickler(ctx,demoNo);"></td>
					</tr>
				</c:if>
				<tr>
					<td colspan="2" align="right">
					<input type="button" value="print review"
							onclick="if (checkform())printsubmit();else return false;">
					<input type="button" value="save and close"
						onclick="if (checkform())savesubmit();else return false;">
					</td>
				</tr>



			</table>
			</td>
		</tr>
	</table>
</html:form>

</body>
</html:html>
