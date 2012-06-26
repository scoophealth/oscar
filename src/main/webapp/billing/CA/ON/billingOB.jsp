<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing"
	rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../../../noRights.html");%>
</security:oscarSec>

<%
if(session.getAttribute("user") == null) response.sendRedirect("../../../logout.jsp");
String user_no = (String) session.getAttribute("user");
String providerview = request.getParameter("providerview")==null?"":request.getParameter("providerview") ;
String asstProvider_no = "";
String color ="";
String premiumFlag="";
String service_form="";

OscarProperties props = OscarProperties.getInstance();
if(props.getProperty("isNewONbilling", "").equals("true")) {
%>
<jsp:forward page="billingON.jsp" />
<% } %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="errorpage.jsp"%>

<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
%>
<%
String clinicview = oscarVariables.getProperty("clinic_view", "");
String clinicNo = oscarVariables.getProperty("clinic_no", "");
String visitType = oscarVariables.getProperty("visit_type", "");
%>
<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>Billing Record</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--
function setfocus() {
	this.focus();
	//document.serviceform.xml_diagnostic_code.focus();
	//document.serviceform.xml_diagnostic_code.select();
}
/*
function RecordAttachments(Files, File0, File1, File2) {
	window.document.serviceform.elements["File0Data"].value = File0;
	window.document.serviceform.elements["File1Data"].value = File1;
	window.document.serviceform.elements["File2Data"].value = File2;
	window.document.all.Atts.innerText = Files;
}
*/
var remote=null;

function rs(n,u,w,h,x) {
	args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
	remote=window.open(u,n,args);
	if (remote != null) {
		if (remote.opener == null)
			remote.opener = self;
	}
	if (x == 1) { return remote; }
}

var awnd=null;
function ScriptAttach() {
	f0 = escape(document.serviceform.xml_diagnostic_detail.value);
	f1 = document.serviceform.xml_dig_search1.value;
	// f2 = escape(document.serviceform.elements["File2Data"].value);
	// fname = escape(document.Compose.elements["FName"].value);
	awnd=rs('att','billingDigSearch.jsp?name='+f0 + '&search=' + f1,600,600,1);
	awnd.focus();
}

function OtherScriptAttach() {
	t0 = escape(document.serviceform.xml_other1.value);
	t1 = escape(document.serviceform.xml_other2.value);
	t2 = escape(document.serviceform.xml_other3.value);
	// f1 = document.serviceform.xml_dig_search1.value;
	// f2 = escape(document.serviceform.elements["File2Data"].value);
	// fname = escape(document.Compose.elements["FName"].value);
	awnd=rs('att','billingCodeSearch.jsp?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',600,600,1);
	awnd.focus();
}

function ResearchScriptAttach() {
	t0 = escape(document.serviceform.xml_research1.value);
	t1 = escape(document.serviceform.xml_research2.value);
	t2 = escape(document.serviceform.xml_research3.value);
	// f1 = document.serviceform.xml_dig_search1.value;
	// f2 = escape(document.serviceform.elements["File2Data"].value);
	// fname = escape(document.Compose.elements["FName"].value);
	awnd=rs('att','billingResearchCodeSearch.jsp?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',600,600,1);
	awnd.focus();
}

function POP(n,h,v) {
	window.open(n,'OSCAR','toolbar=no,location=no,directories=no,status=yes,menubar=no,resizable=yes,copyhistory=no,scrollbars=yes,width='+h+',height='+v+',top=100,left=200');
}

function reloadPage(init) {  //reloads the window if Nav4 resized
	if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
		document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
	else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
}
reloadPage(true);

function findObj(n, d) { //v4.0
	var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
	if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function showHideLayers() { //v3.0
	var i,p,v,obj,args=showHideLayers.arguments;
	for (i=0; i<(args.length-2); i+=3) if ((obj=findObj(args[i]))!=null) { v=args[i+2];
	if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
	obj.visibility=v; }
}

function checkData() {
    var b = true;
	// toUppercase()
	document.forms[0].xml_other1.value = document.forms[0].xml_other1.value.toUpperCase();
	document.forms[0].xml_other2.value = document.forms[0].xml_other2.value.toUpperCase();
	document.forms[0].xml_other3.value = document.forms[0].xml_other3.value.toUpperCase();

    // for no dx code
	for (i=0;i<document.forms[0].length;i++){
		var cd = document.forms[0].elements[i];
		if(cd.value=="checked" && cd.checked) {
			var na = cd.name;
			na = na.substring(4);
			//alert(cd.value + na);
			if(na=="Q100A" || na=="Q110A" || na=="Q120A" || na=="Q200A" || na=="G373A" || na=="G212A" || na=="G539A" || na=="G591A") {
				if(document.forms[0].xml_diagnostic_detail.value == ""){
					document.forms[0].xml_diagnostic_detail.value = ":::";
				}
				//alert(document.forms[0].xml_diagnostic_detail.value + " ! :"+ na);
			}
		} else if(cd.name=="xml_other1" || cd.name=="xml_other2" || cd.name=="xml_other3" ) {
			//alert(cd.name + cd.value);
			var na = cd.value;
			if(na=="Q100A" || na=="Q110A" || na=="Q120A" || na=="Q200A" || na=="G373A" || na=="G212A" || na=="G539A" || na=="G591A") {
				if(document.forms[0].xml_diagnostic_detail.value == ""){
					document.forms[0].xml_diagnostic_detail.value = ":::";
				}
			}
		}
	}

    if(document.forms[0].xml_provider.value == "000000"){
    	alert("Please select Billing Physician!");
        b = false;
    } else if(document.forms[0].xml_diagnostic_detail.value == ""){
    	alert("Please input a diagnostic code!");
        b = false;
//    } else if(document.forms[0].xml_billtype.options[document.forms[0].xml_billtype.selectedIndex].value == "NOB | Do Not Bill"){
//    	alert("The billing type is 'do not bill'.");
//        b = false;
	} <%if(oscarVariables.getProperty("isSpecialist", "").equals("true")) {%>else if (!document.forms[0].xml_referral.checked){
		//if ref. no check
		for (i=0;i<document.forms[0].length;i++){
			var cd = document.forms[0].elements[i];
			if(cd.value=="checked" && cd.checked) {
				//alert(cd.value);
				var na = cd.name;
				na = na.substring(4);
				//alert(na); A265A, A266A, A565A, A815A, A665A
				if(na=="A265A" || na=="A266A" || na=="A565A" || na=="A815A" || na=="A665A" || na=="A001A") {
					alert("You may forget to check referral box!");
				} else if(na=="C265A" || na=="C266A" || na=="C565A" || na=="C815A" || na=="C665A" ) {
					alert("You may forget to check referral box!");
				} else if(na.substring(0,1)=="W" ) {
					alert("You may forget to check referral box!");
				}
			} else if(cd.name=="xml_other1" || cd.name=="xml_other2" || cd.name=="xml_other3" ) {
				var na = cd.value;
				if(na=="A265A" || na=="A266A" || na=="A565A" || na=="A815A" || na=="A665A" || na=="A001A") {
					alert("You may forget to check referral box!");
				} else if(na=="C265A" || na=="C266A" || na=="C565A" || na=="C815A" || na=="C665A" ) {
					alert("You may forget to check referral box!");
				} else if(na.substring(0,1)=="W" ) {
					alert("You may forget to check referral box!");
				}
			}
		}
	}<%}%>

	checkServiceDate();
    return b;
}

function onDblClickServiceCode(item) {
	//alert(item.id);
	if(document.forms[0].xml_other1.value=="") {
		document.forms[0].xml_other1.value = item.id.substring(3);
	} else if(document.forms[0].xml_other2.value=="") {
		document.forms[0].xml_other2.value = item.id.substring(3);
	} else if(document.forms[0].xml_other3.value=="") {
		document.forms[0].xml_other3.value = item.id.substring(3);
	}
}

function checkServiceDate() {
	var calDate=new Date();
	varYear = calDate.getFullYear();
	varMonth = calDate.getMonth()+1;
	varDate = calDate.getDate();
    var str_date = document.forms[0].xml_appointment_date.value;
    var yyyy = str_date.substring(0, str_date.indexOf("-"));
	var mm = str_date.substring(eval(str_date.indexOf("-")+1), str_date.lastIndexOf("-"));
	var dd = str_date.substring(eval(str_date.lastIndexOf("-")+1));
	var bWrongDate = false;
	sMsg = "";
	if(yyyy > varYear) {
		sMsg = "year";
		bWrongDate = true;
	} else if(yyyy == varYear && mm > varMonth) {
		sMsg = "month";
		bWrongDate = true;
	} else if(yyyy == varYear && mm == varMonth && dd > varDate) {
		sMsg = "date";
		bWrongDate = true;
	}
	if(bWrongDate) {
		alert("You may have a wrong Service Date!" + " Wrong " + sMsg);
	}
}

//-->
</script>

<link rel="stylesheet" href="billing.css" type="text/css">
</head>

<%
boolean bNew = false; //Is that a new file?
if( request.getParameter("bNewForm")!=null )
	bNew = request.getParameter("bNewForm").compareTo("0")==0 ? false : true ;

String content="", billingdatetime="";
String hotclick="";
hotclick = request.getParameter("hotclick");
if (hotclick.compareTo("gel") == 0) {
	content = "<xml_k990>checked</xml_k990><xml_a007>checked</xml_a007>";
}
if( !bNew ) {
	content = (String) session.getAttribute("content");
%>
<xml id="xml_list">
<billing>
<%=content%>
</billing>
</xml>
<%
	bNew = false;
}

GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);
int dob_year=0, dob_month=0, dob_date=0, age=0;
String DemoSex ="",DemoStatus="", DemoRoster="";
String appt_no = request.getParameter("appointment_no");
String demoname = request.getParameter("demographic_name");
String demo_no = request.getParameter("demographic_no");
//user_no =request.getParameter("user_no");
String apptProvider_no = request.getParameter("apptProvider_no");
String ctlBillForm = request.getParameter("billForm");
int ctlCount = 0;
String assgProvider_no = "", assgProvider_name="";

ResultSet rsPatient = apptMainBean.queryResults(demo_no, "search_demographic_details");
while(rsPatient.next()){
	assgProvider_no = rsPatient.getString("provider_no");
	DemoSex = rsPatient.getString("sex");
	DemoStatus = rsPatient.getString("patient_status")==null?"":rsPatient.getString("patient_status").toUpperCase();
	DemoRoster = rsPatient.getString("roster_status")==null?"":rsPatient.getString("roster_status").toUpperCase();
	dob_year = Integer.parseInt(rsPatient.getString("year_of_birth"));
	dob_month = Integer.parseInt(rsPatient.getString("month_of_birth"));
	dob_date = Integer.parseInt(rsPatient.getString("date_of_birth"));
	if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
}
rsPatient.close();

assgProvider_name = providerBean.getProperty(assgProvider_no, "");
ResultSet rslocal = null;
/*
rslocal = apptMainBean.queryResults(assgProvider_no, "search_provider_name");
while(rslocal.next()){
	assgProvider_name = rslocal.getString("first_name")+" " + rslocal.getString("last_name");
}
*/
%>

<body bgcolor="#FFFFFF" text="#000000" rightmargin="0" leftmargin="0"
	topmargin="10" marginwidth="0" marginheight="0"
	onLoad="setfocus();showHideLayers('Layer1','','hide')">

<div id="Layer2"
	style="position: absolute; left: 362px; top: 26px; width: 332px; height: 600px; z-index: 2; background-color: #FFCC00; layer-background-color: #FFCC00; border: 1px none #000000; visibility: hidden">
<table width="98%" border="0" cellspacing="0" cellpadding="0"
	align=center>
	<tr>
		<td width="18%"><b><font size="-2">Dx Code</font></b></td>
		<td width="76%"><b><font size="-2">Description</font></b></td>
		<td width="6%"><a href="#"
			onClick="showHideLayers('Layer2','','hide');return false">X</a></td>
	</tr>

	<%
String ctldiagcode="", ctldiagcodename="";
ctlCount = 0;
ResultSet rsdiagcode = apptMainBean.queryResults(ctlBillForm, "search_ctl_diagnostic_code");

while (rsdiagcode.next()){
	ctldiagcode = rsdiagcode.getString("dcode");
	ctldiagcodename = rsdiagcode.getString("des");
%>
	<tr bgcolor=<%=ctlCount%2==0 ? "#FFFFFF" : "#EEEEFF"%>>
		<td width="18%"><b><font size="-2" color="#7A388D"><a
			href="#"
			onClick="document.serviceform.xml_diagnostic_detail.value='<%=ctldiagcode%>|<%=ctldiagcodename%>';showHideLayers('Layer2','','hide');return false;"><%=ctldiagcode%></a></font></b></td>
		<td colspan="2"><font size="-2" color="#7A388D"> <%=ctldiagcodename.length() < 56 ? ctldiagcodename : ctldiagcodename.substring(0,55)%></font></td>
	</tr>
	<%
}
rsdiagcode.close();
%>

</table>
</div>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="42" width="10%">&nbsp;</td>
		<td width="90%">
		<p><font color="#FFFFFF" size="3"><b>oscarBilling</b></font></p>
		</td>
	</tr>
</table>

<div id="Layer1"
	style="position: absolute; left: 1px; top: 159px; width: 410px; height: 200px; z-index: 1; background-color: #FFCC00; layer-background-color: #FFCC00; border: 1px none #000000; visibility: hidden">
<table width="98%" border="0" cellspacing="0" cellpadding="0"
	align=center>
	<tr bgcolor="#393764">
		<td width="96%" height="7" bgcolor="#FFCC00"><font size="-2"
			face="Geneva, Arial, Helvetica, san-serif" color="#000000"><b>Billing
		Form</b></font></td>
		<td width="3%" bgcolor="#FFCC00" height="7"><b><a href="#"
			onClick="showHideLayers('Layer1','','hide');return false;">X</a></b></font></td>
	</tr>

	<%
ResultSet rs3= null;
ctlCount = 0;
String ctlcode="", ctlcodename="";

ResultSet rsctlcode = apptMainBean.queryResults("%", "search_ctlbillservice");
while (rsctlcode.next()){
	ctlcode = rsctlcode.getString("servicetype");
	ctlcodename = rsctlcode.getString("servicetype_name");

	ctlCount++;
%>
	<tr bgcolor=<%=ctlCount%2==0 ? "#FFFFFF" : "#EEEEFF"%>>
		<td colspan="2"><b><font size="-2" color="#7A388D"><a
			href="billingOB.jsp?billForm=<%=ctlcode%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>&demographic_no=<%=request.getParameter("demographic_no")%>&user_no=<%=user_no%>&apptProvider_no=<%=request.getParameter("apptProvider_no")%>&providerview=<%=request.getParameter("apptProvider_no")%>&appointment_date=<%=request.getParameter("appointment_date")%>&status=<%=request.getParameter("status")%>&start_time=<%=request.getParameter("start_time")%>&bNewForm=1"
			onClick="showHideLayers('Layer1','','hide');"><%=ctlcodename%></a></font></b></td>
	</tr>
	<%
}
rsctlcode.close();
%>
</table>
</div>

<form name="serviceform" id="serviceform" action="billingReview.jsp"
	method="POST" onSubmit="return(checkData());">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr>
		<td valign="top" height="221">

		<table width="107%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="12%"><font size="-2"><b>Patient</b>:</font></td>
				<td><font size="-2"><u><%=demoname%></u><%=billingdatetime%></font></td>
				<td><font size="-2"><b>Patient Status</b>:</font><font size="1"><%=DemoStatus%>
				&nbsp;&nbsp;&nbsp;&nbsp; <b>Roster Status: <%=DemoRoster%></b></font></td>
				<td width="19%"><font size="1"><strong>Assigned
				Physician</strong></font></td>
				<td width="24%"><%=assgProvider_name%></td>
			</tr>
			<tr>
				<td width="12%"><b><font size="1">Age:</font></b></td>
				<td><b><font size="1"><%=age%></font></b></td>
				<td><b><font size="1"> <a href="#"
					onClick="showHideLayers('Layer1','','show');return false;">Billing
				form</a>:</font></b> <%
rsctlcode = apptMainBean.queryResults(ctlBillForm, "search_ctlbillservice");
while (rsctlcode.next()){
	ctlcode = rsctlcode.getString("servicetype");
	ctlcodename = rsctlcode.getString("servicetype_name");
}
rsctlcode.close();
%> <%=ctlcodename.length()<30 ? ctlcodename : ctlcodename.substring(0,30)%>
				</td>
				<td width="19%"><font size="-2"> <%
/*
if( !bNew ) { //the old billing form
	if (billingdatetime == "") {
		billingdatetime = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+ " " + now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE);
	}
}
*/
%> <b>Billing Physician</b> </font></td>
				<td width="24%"><font size="-2"> <select
					name="xml_provider" datafld='xml_provider'>
					<option value="000000"
						<%=providerview.equals("000000")?"selected":""%>><b>Select
					Provider</b></option>
					<%
// Retrieving Provider
String proFirst="", proLast="", proOHIP="";
Vector vecOHIP = new Vector();
Vector vecProviderName = new Vector();
rslocal = apptMainBean.queryResults("%", "search_provider_dt_checkstatus");
while(rslocal.next()){
	proFirst = rslocal.getString("first_name");
	proLast = rslocal.getString("last_name");
	proOHIP = rslocal.getString("provider_no");
	vecOHIP.add(proOHIP);
	vecProviderName.add(proLast + ", " + proFirst);
}
rslocal.close();
for(int i=0; i<vecOHIP.size(); i++) {
	proOHIP = (String)vecOHIP.get(i);
%>
					<option value="<%=proOHIP%>"
						<%=providerview.equals(proOHIP)?"selected":(vecOHIP.size()==1?"selected":"")%>><b><%=(String)vecProviderName.get(i)%></b></option>
					<%
}
%>
				</select></font></td>
			</tr>
			<tr>
				<td width="12%"><font size="-2"><b>Billing Type</b> </font></td>
				<td width="12%"><font size="-2"> <select
					name="xml_billtype" datafld='xml_billtype'>
					<option value="ODP | Bill OHIP" selected>Bill OHIP</option>
					<option value="PAT | Bill Patient">Bill Patient</option>
					<option value="NOB | Do Not Bill"
						<%=DemoStatus.toUpperCase().compareTo("UHIP")==0?"selected":DemoStatus.toUpperCase().compareTo("IFH")==0?"selected":DemoStatus.toUpperCase().compareTo("NO COVERAGE")==0?"selected":DemoStatus.toUpperCase().compareTo("DO NOT BILL")==0?"selected":DemoStatus.toUpperCase().compareTo("NO HEALTH COVERAGE")==0?"selected":""%>>Do
					Not Bill</option>
					<option value="WCB | Worker's Compensation Board">WSIB</option>
				</select> </font></td>
				<td width="33%"><font size="-2"> <%
if (appt_no.compareTo("0") == 0) {
%> <a href="#"
					onClick='rs("billingcalendar","billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=service","380","300","0"); return false;'>
				Service Date:</a> <input type="text" name="xml_appointment_date"
					readonly
					value="<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>"
					size="12" datafld='xml_appointment_date'> <%
} else {
%> <b>Service Date:</b> <input type="text" name="xml_appointment_date"
					readonly value="<%=request.getParameter("appointment_date")%>"
					size="12" datafld='xml_appointment_date'> <%} %> </font></td>
				<td width="19%"><font size="-2"><b>Visit Type</b> </font></td>
				<td width="24%"><font size="-2"> <select
					name="xml_visittype" datafld='xml_visittype'>
					<option value="00| Clinic Visit"
						<%=visitType.equals("Clinic Visit")?"selected":""%>>00 |
					Clinic Visit</option>
					<option value="01| Outpatient Visit"
						<%=visitType.equals("Outpatient Visit")?"selected":""%>>01
					| Outpatient Visit</option>
					<option value="02| Hospital Visit"
						<%=visitType.equals("Hospital Visit")?"selected":""%>>02
					| Hospital Visit</option>
					<option value="03| ER" <%=visitType.equals("ER")?"selected":""%>>03
					| ER</option>
					<option value="04| Nursing Home"
						<%=visitType.equals("Nursing Home")?"selected":""%>>04 |
					Nursing Home</option>
					<option value="05| Home Visit"
						<%=visitType.equals("Home Visit")?"selected":""%>>05 |
					Home Visit</option>
				</select> </font></td>
			</tr>
			<tr>
				<td width="12%"><font size="-2"> <b>Visit Location</b> </font></td>
				<td colspan="2"><font size="-2"> <select
					name="xml_location" datafld='xml_location'>
					<%
					String clinic_location="", clinic_code="";
					List<ClinicLocation> clinicLocations = clinicLocationDao.findByClinicNo(1);
		            for(ClinicLocation clinicLocation:clinicLocations) {
		            	clinic_location = clinicLocation.getClinicLocationName();
		            	clinic_code = clinicLocation.getClinicLocationNo();
%>
					<option value="<%=clinic_code%>"
						<%=clinicview.equals(clinic_code)?"selected":""%>><%=clinic_location%></option>
					<%
}

%>
				</select> <input type="checkbox" name="xml_referral" value="checked"
					<%=bNew?(props.getProperty("billingRefBoxDefault", "").startsWith("checked")?"checked":""):"datafld='xml_referral'"%>>
				<b>Referral ?</b></font></td>
				<td width="19%"><font size="-2"> <a href="#"
					onClick='rs("billingcalendar","billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=admission","380","300","0");return false;'>
				Admission Date:</a> </font></td>
				<td width="24%"><font size="-2"> <%
String inPatient = oscarVariables.getProperty("inPatient");
String admissionDate = "";
try{
	if(inPatient != null && inPatient.trim().equalsIgnoreCase("YES")){
		oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
		admissionDate = demoData.getDemographicDateJoined(demo_no);
	}
}catch(Exception inPatientEx){
	MiscUtils.getLogger().error("Error", inPatientEx);
	admissionDate = "";
}
%> <input type="text" name="xml_vdate" value="<%=admissionDate%>">
				</font></td>
			</tr>
		</table>

		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			height="137">
			<tr>
				<td valign="top" width="33%">

				<table width="100%" border="1" cellspacing="0" cellpadding="0"
					height="0">
					<%
String serviceCode, serviceDesc, serviceValue, servicePercentage, serviceType,serviceDisp="";
String headerTitle="";
String[] param1 =new String[2];
param1[0] = ctlBillForm;
param1[1] = "Group1";

int CountService = 0;
int Count2 = 0;
rslocal = apptMainBean.queryResults(param1, "search_servicecode");
while(rslocal.next()){
	serviceCode = rslocal.getString("service_code");
	serviceDesc = rslocal.getString("description");
	serviceValue = rslocal.getString("value");
	servicePercentage = rslocal.getString("percentage");
	headerTitle = rslocal.getString("service_group_name");
	serviceDisp = serviceValue;
	premiumFlag = "";

	rs3 = apptMainBean.queryResults(serviceCode, "search_billingservice_premium");
	while(rs3.next()){
		premiumFlag = rs3.getString("status");
	}
	rs3.close();

	if (CountService == 0) {
%>
					<tr bgcolor="#CCCCFF">
						<td width="15%" nowrap><font size="1" color="#000000"><b><%=headerTitle%>
						</b></font></td>
						<td width="71%" bgcolor="#CCCCFF"><b><font size="1"
							color="#000000">Description</font></b></td>
						<td width="14%" align="right"><b><font size="1"
							color="#000000">$ Fee</font></b></td>
					</tr>
					<%
	} else {
		serviceType = "";
	}
	CountService++;
%>
					<tr bgcolor=<%=CountService%2==0?"#FFFFFF":"#EEEEFF"%>>
						<td width="15%" nowrap><input type="checkbox"
							name="xml_<%=serviceCode%>" value="checked"
							<%=bNew?"":"datafld='xml_"+serviceCode+ "'"%>> <b><font
							size="1"
							color="<%=premiumFlag.equals("A")? "#993333" : "black"%>"><span
							id="sc<%=(""+CountService).substring(0,1)+serviceCode%>"
							onDblClick="onDblClickServiceCode(this)"><%=serviceCode%></span></font></b>
						&nbsp;</td>
						<td width="71%"><font size="1"><%=serviceDesc%> <input
							type="hidden" name="desc_xml_<%=serviceCode%>"
							value="<%=serviceDesc%>"> </font></td>
						<td width="14%" align="right"><font size="1"><%=serviceDisp%></font>

						<input type="hidden" name="price_xml_<%=serviceCode%>"
							value="<%=serviceDisp%>"> <input type="hidden"
							name="perc_xml_<%=serviceCode%>" value="<%=servicePercentage%>">
						</font></td>
					</tr>
					<%
}
rslocal.close();
%>
				</table>

				</td>
				<td valign="top" width="31%">

				<table width="100%" border="1" cellspacing="0" cellpadding="0">
					<%
param1[0]=ctlBillForm;
param1[1]="Group2";

CountService = 0;
rslocal = apptMainBean.queryResults(param1, "search_servicecode");
while(rslocal.next()){
	serviceCode = rslocal.getString("service_code");
	serviceDesc = rslocal.getString("description");
	serviceValue = rslocal.getString("value");
	servicePercentage = rslocal.getString("percentage");
	headerTitle = rslocal.getString("service_group_name");
	serviceDisp = serviceValue;

	premiumFlag = "";
	rs3 = apptMainBean.queryResults(serviceCode, "search_billingservice_premium");
	while(rs3.next()){
		premiumFlag = rs3.getString("status");
	}
	rs3.close();
	if (CountService == 0) { // first line
%>
					<tr bgcolor="#CCCCFF">
						<td width="15%" nowrap><b></b> <font size="1"><b><%=headerTitle%></b></font>
						</td>
						<td width="71%"><b><font size="1">Description</font></b></td>
						<td width="14%">
						<div align="right"><b><font size="1">$ Fee</font></b></div>
						</td>
					</tr>
					<%
		CountService = 1;
	} else {
		serviceType = "";
	}
%>
					<tr bgcolor=<%=CountService%2==0 ? "#FFFFFF" : "#EEEEFF"%>>
						<td width="15%" nowrap><input type="checkbox"
							name="xml_<%=serviceCode%>" value="checked"
							<%=bNew?"":"datafld='xml_"+serviceCode+ "'"%>> <b><font
							size="1"
							color="<%=premiumFlag.equals("A")? "#993333" : "black"%>"><span
							id="sc<%=(""+CountService).substring(0,1)+serviceCode%>"
							onDblClick="onDblClickServiceCode(this)"><%=serviceCode%></span></font></b>&nbsp;</td>
						<td width="71%"><font size="1"><%=serviceDesc%></font> <input
							type="hidden" name="desc_xml_<%=serviceCode%>"
							value="<%=serviceDesc%>"> </font></td>
						<td width="14%">
						<div align="right"><font size="1"><%=serviceDisp%></font> <input
							type="hidden" name="price_xml_<%=serviceCode%>"
							value="<%=serviceDisp%>"> <input type="hidden"
							name="perc_xml_<%=serviceCode%>" value="<%=servicePercentage%>">
						</font></div>
						</td>
					</tr>
					<%
	CountService++;
}
rslocal.close();
%>

				</table>

				</td>
				<td valign="top" width="36%">

				<table width="100%" border="1" cellspacing="0" cellpadding="0"
					height="0">
					<%
param1[0]=ctlBillForm;
param1[1]="Group3";

CountService = 0;
rslocal = apptMainBean.queryResults(param1, "search_servicecode");
while(rslocal.next()){
	serviceCode = rslocal.getString("service_code");
	serviceDesc = rslocal.getString("description");
	serviceValue = rslocal.getString("value");
	servicePercentage = rslocal.getString("percentage");
	headerTitle = rslocal.getString("service_group_name");
	serviceDisp = serviceValue;

	premiumFlag = "";
	rs3 = apptMainBean.queryResults(serviceCode, "search_billingservice_premium");
	while(rs3.next()){
		premiumFlag = rs3.getString("status");
	}
	rs3.close();

	if (CountService == 0) {
%>
					<tr bgcolor="#CCCCFF">
						<td width="15%" align="left"><b><font size="1"
							color="#000000"><%=headerTitle%></b></font></td>
						<td width="71%" bgcolor="#CCCCFF"><b><font size="1">Description</font></b></td>
						<td width="14%" align="right"><b><font size="1">$
						Fee</font></b></td>
					</tr>
					<%
		CountService = 1;
	} else{
		serviceType = "";
	}
%>
					<tr bgcolor=<%=CountService%2==0 ? "#FFFFFF" : "#EEEEFF"%>>
						<td width="15%" nowrap><b></b> <input type="checkbox"
							name="xml_<%=serviceCode%>" value="checked"
							<%=bNew?"":"datafld='xml_"+serviceCode+ "'"%>> <b><font
							size="1"
							color="<%=premiumFlag.equals("A")? "#993333" : "black"%>"><span
							id="sc<%=(""+CountService).substring(0,1)+serviceCode%>"
							onDblClick="onDblClickServiceCode(this)"><%=serviceCode%></span></font></b>&nbsp;</td>
						<td width="71%"><font size="1"><%=serviceDesc%></font> <input
							type="hidden" name="desc_xml_<%=serviceCode%>"
							value="<%=serviceDesc%>"> </font></td>
						<td width="14%">
						<div align="right"><font size="1"><%=serviceDisp%></font> <input
							type="hidden" name="price_xml_<%=serviceCode%>"
							value="<%=serviceDisp%>"> <input type="hidden"
							name="perc_xml_<%=serviceCode%>" value="<%=servicePercentage%>">
						</font></div>
						</td>
					</tr>
					<%
	CountService++;
}
rslocal.close();
%>

					<tr bgcolor="#CCCCFF">
						<td align="center" valign="top" height="71" colspan="3">

						<table width="100%" border="0" cellspacing="2" cellpadding="2">
							<tr>
								<td width="91%">

								<table width="100%" border="0" cellspacing="0" cellpadding="0"
									height="67" bgcolor="#EEEEFF">
									<tr>
										<td><b><font size="1"> Other
										service/procedure/premium codes</font></b></td>
										<td><b><font size="1"> # units</font></b></td>
									</tr>
									<tr>
										<td><font size="1"> <input type="text"
											name="xml_other1" size="40" datafld='xml_other1'> </font></td>
										<td><font size="1"> <input type="text"
											name="xml_other1_unit" size="5" maxlength="2"
											datafld='xml_other1_unit'> </font></td>
									</tr>
									<tr>
										<td><font size="1"> <input type="text"
											name="xml_other2" size="40" datafld='xml_other2'> </font></td>
										<td><font size="1"> <input type="text"
											name="xml_other2_unit" size="5" maxlength="2"
											datafld='xml_other2_unit'> </font></td>
									</tr>
									<tr>
										<td><font size="1"> <input type="text"
											name="xml_other3" size="40" datafld='xml_other3'> </font></td>
										<td><font size="1"> <input type="text"
											name="xml_other3_unit" size="5" maxlength="2"
											datafld='xml_other3_unit'> </font></td>
									</tr>
									<tr>
										<td colspan="2"><a href="javascript:OtherScriptAttach()"><img
											src="images/search_code.jpg" border="0"></a></td>
									</tr>
								</table>

								</td>
								<td width="9%"></td>

							</tr>
						</table>

						<table width="100%" border="0" cellspacing="2" cellpadding="2">
							<tr bgcolor="#CCCCFF">
								<td colspan="4"><b><font size="1" color="#000000">
								Diagnostic Codes</font></b></td>
							</tr>

							<input type="hidden" name="xml_diagnostic_code"
								value="000|Select a diagnosis">
							<input type="hidden" name="status"
								value="<%=request.getParameter("status")%>">
							<input type="hidden" name="demographic_no"
								value="<%=request.getParameter("demographic_no")%>">
							<input type="hidden" name="billing_name" value="obstetric">
							<input type="hidden" name="user_no" value="<%=user_no%>">
							<input type="hidden" name="apptProvider_no"
								value="<%=apptProvider_no%>">
							<input type="hidden" name="asstProvider_no"
								value="<%=asstProvider_no%>">
							<input type="hidden" name="billing_date"
								value="<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>">
							<input type="hidden" name="billing_time"
								value="<%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)%>">
							<input type="hidden" name="billingservice_code" value="">
							<input type="hidden" name="appointment_no"
								value="<%=request.getParameter("appointment_no")%>">
							<input type="hidden" name="start_time"
								value="<%=request.getParameter("start_time")%>">
							<input type="hidden" name="displaymode" value="savebill">
							<input type="hidden" name="demographic_name"
								value="<%=demoname%>">
							<input type="hidden" name="clinic_no" value="<%=clinicNo%>">
							<input type="hidden" name="billForm" value="<%=ctlBillForm%>">

							<tr bgcolor="#EEEEFF">
								<td align="left" colspan="4" height="9"><b><font
									size="1"> <a href="#"
									onClick="showHideLayers('Layer2','','show','Layer1','','hide'); return false;">
								Diagnostic </a></font></b> <font size="1"> <input
									name="xml_diagnostic_detail" value="" size="25"
									datafld='xml_diagnostic_detail'> <a
									href="javascript:ScriptAttach()"><img
									src="images/search_dx_code.jpg" border="0"></a> &nbsp; &nbsp;
								<input type="hidden" name="xml_dig_search1"> </font></td>
							</tr>
						</table>

						</td>
					</tr>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="right"><input type="submit" name="Submit"
							value="Continue"> <input type="button" name="Button"
							value="Cancel" onClick="window.close();"></td>
					</tr>
				</table>

				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>

<%%>

</body>
</html>
