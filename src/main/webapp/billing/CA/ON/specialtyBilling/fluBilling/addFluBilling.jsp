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
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.*, java.sql.*, oscar.login.*, oscar.*" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.ReportProvider" %>
<%@ page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%
	ProviderDao providerDao= SpringUtils.getBean(ProviderDao.class);
    ReportProviderDao reportProviderDao= SpringUtils.getBean(ReportProviderDao.class);
%>

<%@ include file="../../../../../admin/dbconnection.jsp"%>

<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);

String clinicNo = oscarVariables.getProperty("clinic_no");
String clinicview = oscarVariables.getProperty("clinic_view");

String nowDateTime = String.valueOf(curYear)+"-"+String.valueOf(curMonth) + "-" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
String nowDate = String.valueOf(curYear)+"-"+String.valueOf(curMonth) + "-" + String.valueOf(curDay);
String proFirst1="", proLast1="", proOHIP1="", proNo="";
int Count = 0;
Provider p = providerDao.getProvider(request.getParameter("creator"));
if(p != null) {
	proFirst1 = p.getFirstName();
	proLast1 = p.getLastName();
	proOHIP1 = p.getProviderNo();
}

Vector billingProvider = new Vector();

List<String> ids = new ArrayList<String>();
for(ReportProvider rp : reportProviderDao.findAll()) {
	if(!ids.contains(rp.getProviderNo()))
		ids.add(rp.getProviderNo());
}
for(String id:ids) {
	Provider pd = providerDao.getProvider(id);
	if(pd != null) {
		billingProvider.add(pd.getProviderNo());
		billingProvider.add(pd.getOhipNo());
		billingProvider.add(pd.getFormattedName());
	}
}

String actionPage = "onDbAddFluBilling.jsp";

%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>FLU BILLING</title>
<script language="JavaScript">
<!--



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
  awnd=rs('att','../billingDigSearch.jsp?name='+f0 + '&search=' + f1,600,600,1);
  awnd.focus();
}



function OtherScriptAttach() {
  t0 = escape(document.serviceform.xml_other1.value);
 // t1 = escape(document.serviceform.xml_other2.value);
 // t2 = escape(document.serviceform.xml_other3.value);
 // f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','../billingCodeSearch.jsp?name='+t0 + '&name1=' + "" + '&name2=' + "" + '&search=',600,600,1);
  awnd.focus();
}

function validate2(form, prevention){
form.goPrev.value = prevention;
if (validateProviderNo(form)){
form.action = "<%=actionPage%>" ; //"dbAddFluBilling.jsp"
form.submit()
}

else{}
}

function validate(form){
form.goPrev.value = "";
if (validateProviderNo(form)){
form.action = "<%=actionPage%>" ; //"dbAddFluBilling.jsp"
form.submit()
}

else{}
}
function validateProviderNo() {
  if (document.serviceform.provider.value == "") {
alert("Please select a valid Billing Provider!");
	return false;
 }
 else{
 return true;
}

}

//-->
</script>
<style type="text/css">
<!--
BODY {
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #FFFFFF;
}

TD {
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000
}

TD.black {
	font-weight: bold;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #FFFFFF;
	background-color: #666699;
}

TD.lilac {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #EEEEFF;
}

TD.boldlilac {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #EEEEFF;
}

TD.lilac A:link {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #EEEEFF;
}

TD.lilac A:visited {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #EEEEFF;
}

TD.lilac A:hover {
	font-weight: normal;
	color: #000000;
	background-color: #CDCFFF;
}

TD.white {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #FFFFFF;
}

TD.heading {
	font-weight: bold;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #FDCB03;
	background-color: #666699;
}

H2 {
	font-weight: bold;
	font-size: 12pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
}

H3 {
	font-weight: bold;
	font-size: 10pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #FFFFFF;
}

H4 {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #FFFFFF;
}

H6 {
	font-weight: bold;
	font-size: 7pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #FFFFFF;
}

A:link {
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #336666;
	background-color: #FFFFFF;
}

A:visited {
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #336666;
	background-color: #FFFFFF;
}

A:hover {
	color: red;
	background-color: #CDCFFF;
}

TD.cost {
	font-weight: bold;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: red;
	background-color: #FFFFFF;
}

TD.black A:link {
	font-weight: bold;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #FFFFFF;
	background-color: #666699;
}

TD.black A:visited {
	font-weight: bold;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #FFFFFF;
	background-color: #666699;
}

TD.black A:hover {
	color: #FDCB03;
	background-color: #666699;
}

TD.title {
	font-weight: bold;
	font-size: 10pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #FFFFFF;
}

TD.white {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #FFFFFF;
}

TD.white A:link {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #FFFFFF;
}

TD.white A:visited {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #FFFFFF;
}

TD.white A:hover {
	font-weight: normal;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #000000;
	background-color: #CDCFFF;
}

#navbar {
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #FDCB03;
	background-color: #666699;
}

SPAN.navbar A:link {
	font-weight: bold;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #FFFFFF;
	background-color: #666699;
}

SPAN.navbar A:visited {
	font-weight: bold;
	font-size: 8pt;
	font-family: verdana, arial, helvetica;
	color: #EFEFEF;
	background-color: #666699;
}

SPAN.navbar A:hover {
	color: #FDCB03;
	background-color: #666699;
}

SPAN.bold {
	font-weight: bold;
	background-color: #666699;
}

.sbttn {
	background: #EEEEFF;
	border-bottom: 1px solid #104A7B;
	border-right: 1px solid #104A7B;
	border-left: 1px solid #AFC4D5;
	border-top: 1px solid #AFC4D5;
	color: #000066;
	height: 19px;
	text-decoration: none;
	cursor: hand
}

.mbttn {
	background: #D7DBF2;
	border-bottom: 1px solid #104A7B;
	border-right: 1px solid #104A7B;
	border-left: 1px solid #AFC4D5;
	border-top: 1px solid #AFC4D5;
	color: #000066;
	height: 19px;
	text-decoration: none;
	cursor: hand
}
-->
</style>
</head>

<body leftmargin="0" topmargin="5" rightmargin="0" onLoad="setfocus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Billing</font></font></b></font></p>
		</td>
	</tr>
</table>




<table width="100%" border="0" bgcolor="#666699" cellspacing="0">
	<form name="serviceform" method="post">
	<tr>
		<td>

		<table width="100%" border="0" bgcolor="#CCCCCC" cellpadding="5"
			cellspacing="0">
			<tr bgcolor="#E1E1FF">
				<td colspan="4" height="15">
				<p align="center"><font color="#FBF4C6"
					face="Arial, Helvetica, sans-serif"><b><font
					color="#000000">flu billing </font></b></font></p>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="19%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Demographic Name </font><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demo_name"
					value="<%=request.getParameter("demographic_name")%>" size="20">
				<input type="hidden" name="functionid"
					value="<%=request.getParameter("functionid")%>" size="20">
				</font></td>
				<td width="26%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Health Number </font><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demo_hin"
					value="<%=request.getParameter("hin")%>" size="20"> </font><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> </font></td>
				<td width="31%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Demographic DOB</font><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="demo_dob"
					value="<%=request.getParameter("dob")%>" size="20"> </font></td>
				<td width="24%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1">Appointment
				Date <input type="text" name="apptDate" value="<%=nowDate%>">
				</font></td>
			</tr>
			<tr bgcolor="#DFDFEA">
				<td width="19%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Billing Provider <select name="provider">
					<option value=""
						<%=request.getParameter("mrp").equals("")?"selected":""%>>Select
					Provider</option>
					<%
					String proFirst="";
					String proLast="";
					String proOHIP="";
					String specialty_code;
					for(int i=0; i<billingProvider.size(); i+=3) {
						specialty_code = (String)billingProvider.get(i);
						proOHIP = (String)billingProvider.get(i+1);
						String proName = (String)billingProvider.get(i+2);
				%>
					<option value="<%=proOHIP%>|<%=specialty_code%>"
						<%=(request.getParameter("mrp").equals(specialty_code)||billingProvider.size()==3)?"selected":""%>><%=proName%></option>
					<%	} %>

				</select> </font></td>
				<td width="26%"><font size="1"
					face="Verdana, Arial, Helvetica, sans-serif">Appointment
				Provider <select name="apptProvider">
					<option value=""
						<%=request.getParameter("creator").equals("")?"selected":""%>>Select
					Provider</option>
					<%
		          
					for(Provider prov:providerDao.getActiveProviders()) {
		         
					   proFirst = prov.getFirstName();
					   proLast = prov.getLastName();
					   proOHIP = prov.getProviderNo();
					
%>
					<option value="<%=proOHIP%>"
						<%=request.getParameter("creator").equals(proOHIP)?"selected":""%>><%=proLast%>,
					<%=proFirst%></option>
					<%
}
%>
				</select> </font></td>
				<td width="31%"><font size="1"
					face="Verdana, Arial, Helvetica, sans-serif">Billing Type <select
					name="xml_billtype">
					<option value="HSO | Capitated">Capitated</option>
					<option value="ODP | Bill OHIP" selected>Bill OHIP</option>
					<option value="PAT | Bill Patient">Bill Patient</option>
					<option value="NOB | Do Not Bill">Do Not Bill</option>
					<option value="WCB | Worker's Compensation Board">WSIB</option>
				</select> </font></td>
				<td width="24%"><font size="1"
					face="Verdana, Arial, Helvetica, sans-serif">Visit Type <select
					name="xml_visittype">
					<option value="00| Clinic Visit" selected>00 | Clinic
					Visit</option>
				</select> </font></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="19%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Service Codes </font><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="svcCode" size="20" value="G590A,Q590A"> </font></td>
				<td width="26%"><font size="1">Diagnostic Code <input
					type="text" name="dxCode" size="20" value="896"> </font></td>
				<td width="31%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"
					color="#000000">Create Date</font><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="docdate" readonly value="<%=nowDate%>" size="20">
				</font></td>
				<td width="24%"><font
					face="Verdana, Arial, Helvetica, sans-serif" color="#000000"
					size="1">Creator </font><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="dispcreator" readonly
					value="<%=proLast1%>, <%=proFirst1%>" size="20"> </font><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="hidden" name="doccreator"
					value="<%=request.getParameter("creator")%>" size="20"> <input
					type="hidden" name="demo_sex"
					value="<%=request.getParameter("demo_sex")%>" size="20"> <input
					type="hidden" name="rdohip"
					value="<%=request.getParameter("rdohip")%>" size="20"> <input
					type="hidden" name="rd" value="<%=request.getParameter("rd")%>"
					size="20"> <input type="hidden" name="demo_hctype"
					value="<%=request.getParameter("demo_hctype")%>" size="20">
				<input type="hidden" name="clinic_ref_code" value="<%=clinicview%>"
					size="20"> <input type="hidden" name="clinicNo"
					value="<%=clinicNo%>" size="20"> <input type="hidden"
					name="appointment_no" value="0" size="20"> <input
					type="hidden" name="orderby" value="updatedatetime desc" size="20">
				</font><font face="Verdana, Arial, Helvetica, sans-serif" color="#000000"
					size="1"> </font></td>
			</tr>
			<tr bgcolor="#DFDFEA">
				<td width="19%" height="20" colspan="4"><input type="hidden"
					name="goPrev" value="" /> <input type="button" name="cancel"
					value="Submit" class="mbttn" onClick="validate(this.form)">
				<input type="button" name="subPrev" value="Submit & Goto Flu Prevention"
					class="mbttn" onClick="validate2(this.form,'Flu')">
                                <input type="button" name="subPrev" value="Submit & Goto H1N1 Prevention"
					class="mbttn" onClick="validate2(this.form,'H1N1')">
                                <input
					type="button" name="cancel" value="Cancel" onClick="window.close()"
					class="mbttn"></td>

			</tr>
		</table>
		</td>
	</tr>
	</form>
</table>







</body>
</html>
