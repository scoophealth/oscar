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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%      
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*, oscar.oscarDemographic.data.*"%>
<%@ page
	import="oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*"%>
<%

String color = "", colorflag ="";
BillingViewBean bean = (BillingViewBean)pageContext.findAttribute("billingViewBean");
oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
org.oscarehr.common.model.Demographic demo = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), bean.getPatientNo());

ArrayList billItem = bean.getBillItem();
BillingFormData billform = new BillingFormData();

%>



<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.bc.title" /></title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--

function setfocus() {
		  //document.serviceform.xml_diagnostic_code.focus();
		  //document.serviceform.xml_diagnostic_code.select();
		}
		
function RecordAttachments(Files, File0, File1, File2) {
  window.document.serviceform.elements["File0Data"].value = File0;
  window.document.serviceform.elements["File1Data"].value = File1;
  window.document.serviceform.elements["File2Data"].value = File2;
    window.document.all.Atts.innerText = Files;
  }

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


  t0 = escape(document.serviceform.xml_diagnostic_detail1.value);
  t1 = escape(document.serviceform.xml_diagnostic_detail2.value);
  t2 = escape(document.serviceform.xml_diagnostic_detail3.value);
  awnd=rs('att','billingDigNewSearch.jsp?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',600,600,1);
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


function ResearchScriptAttach() {
  t0 = escape(document.serviceform.xml_referral1.value);
  t1 = escape(document.serviceform.xml_referral2.value);
  
  awnd=rs('att','billingReferralCodeSearch.jsp?name='+t0 + '&name1=' + t1 +  '&search=',600,600,1);
  awnd.focus();
}

function POP(n,h,v) {
  window.open(n,'OSCAR','toolbar=no,location=no,directories=no,status=yes,menubar=no,resizable=yes,copyhistory=no,scrollbars=yes,width='+h+',height='+v+',top=100,left=200');
}
//-->
</SCRIPT>
<script language="JavaScript">
<!--
<!--
function reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
  else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
}
reloadPage(true);
// -->

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

function gotoPrintReceipt(){
   document.location.href="<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath() %>/billing/CA/BC/billingView.do?billing_no=<%=bean.getBillingNo()%>&receipt=yes";
}

//-->
</script>
<link rel="stylesheet" href="../billing/billing.css" type="text/css">
</head>


<html:base />

<body bgcolor="#FFFFFF" text="#000000" rightmargin="0" leftmargin="0"
	topmargin="10" marginwidth="0" marginheight="0"
	onLoad="setfocus();showHideLayers('Layer1','','hide')">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3"><bean:message key="billing.bc.title" /></font></font></b></font></p>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td valign="top" height="221">
		<table width="107%" border="0" cellspacing="2" cellpadding="2">
			<tr bgcolor="#EAEAFF">
				<td colspan="6">Patient Information</td>
			</tr>
			<tr>
				<td width="12%" height="16">Patient Name:</td>
				<td width="12%"><%=bean.getPatientLastName()%>, <%=bean.getPatientFirstName()%>&nbsp;</td>
				<td width="16%">Patient PHN:</td>
				<td width="17%"><%=bean.getPatientPHN()%></td>
				<td width="19%">Health Card Type:</td>
				<td width="24%"><%=bean.getPatientHCType()%></td>
			</tr>
			<tr>
				<td height="16">Patient DoB:</td>
				<td><%=bean.getPatientDoB()%></td>
				<td>Patient Age:</td>
				<td><%=bean.getPatientAge()%></td>
				<td>Patient Sex:</td>
				<td><%=bean.getPatientSex()%></td>
			</tr>
			<tr>
				<td height="16">Patient Address:</td>
				<td><%=bean.getPatientAddress1()%></td>
				<td>City:</td>
				<td><%=bean.getPatientAddress2()%></td>
				<td>Postal:</td>
				<td><%=bean.getPatientPostal()%></td>
			</tr>
			<tr>
				<td height="16">&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>

		<table width="107%" border="0" cellspacing="2" cellpadding="2">
			<tr bgcolor="#EAEAFF">
				<td colspan="6">Billing Information</td>
			</tr>
			<tr>
				<td width="12%" height="16">Billing Type:</td>
				<td width="12%"><%=bean.getBillingType()%></td>
				<td width="16%">Visit Type:</td>
				<td width="17%"><%=bean.getVisitType()%></td>
				<td width="19%">Visit Location:</td>
				<td width="24%"><%=bean.getVisitLocation()%></td>
			</tr>
			<tr>
				<td height="16">Service Date:</td>
				<td><%=bean.getServiceDate()%></td>
				<td>StartTime: <%=bean.getStartTime()%></td>
				<td>EndTime: <%=bean.getEndTime()%></td>
				<td>Admission Date:</td>
				<td><%=bean.getAdmissionDate()%></td>
			</tr>
			<tr>
				<td height="16">Billing Provider:</td>
				<td><%=billform.getProviderName(bean.getBillingProvider())%></td>
				<td>Appointment Provider:</td>
				<td><%=billform.getProviderName(bean.getApptProviderNo())%></td>
				<td>Creator:</td>
				<td><%=billform.getProviderName(bean.getCreator())%></td>
			</tr>
			<tr>
				<td height="16">Referral Doctor1:</td>
				<td><%=bean.getReferral1()%></td>
				<td>Referral Type1: <%=bean.getReferType1()%></td>
				<td>Referral Doctor 2:</td>
				<td><%=bean.getReferral2()%></td>
				<td>Referral Type2:<%=bean.getReferType2()%></td>
			</tr>
		</table>
		<div align="left"></div>

		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			height="137">
			<tr>
				<td valign="top">
				<table width="100%" border="0" cellspacing="1" cellpadding="1">
					<tr bgcolor="#EAEAFF">
						<td><bean:message key="billing.service.code" /></td>
						<td><bean:message key="billing.service.desc" /></td>
						<td><bean:message key="billing.service.unit" /></td>
						<td><bean:message key="billing.service.fee" /></td>
						<td><bean:message key="billing.service.total" /></td>
					</tr>
					<% for (int i=0; i < billItem.size(); i++){ %>
					<tr>
						<td><%=((BillingBillingManager.BillingItem)billItem.get(i)).getServiceCode()%></td>
						<td><%=((BillingBillingManager.BillingItem)billItem.get(i)).getDescription()%></td>
						<td><%=((BillingBillingManager.BillingItem)billItem.get(i)).getUnit()%></td>
						<td><%=((BillingBillingManager.BillingItem)billItem.get(i)).getDispPrice()%></td>
						<td><%=((BillingBillingManager.BillingItem)billItem.get(i)).getDispLineTotal()%></td>
					</tr>
					<% } %>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td><%=bean.getGrandtotal()%></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>
				<table width="100%" border="0" cellspacing="1" cellpadding="1">
					<tr bgcolor="#EAEAFF">
						<td width="10%" height="14"><bean:message
							key="billing.diagnostic.code" /></td>
						<td width="39%"><bean:message key="billing.diagnostic.desc" /></td>
						<td width="37%">&nbsp;</td>
						<td width="14%">&nbsp;</td>
					</tr>
					<tr>
						<td><%=bean.getDx1()%></td>
						<td><%=billform.getDiagDesc(bean.getDx1(), bean.getBillRegion())%></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><%=bean.getDx2()%></td>
						<td><%=billform.getDiagDesc(bean.getDx2(), bean.getBillRegion())%></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><%=bean.getDx3()%></td>
						<td><%=billform.getDiagDesc(bean.getDx3(), bean.getBillRegion())%></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr bgcolor="#EAEAFF">
						<td height="14" colspan="4">Correspondence Note</td>
					</tr>
					<tr>
						<td height="14" colspan="4"><%=bean.getMSPBillingNote()%></td>
					</tr>

					<tr bgcolor="#EAEAFF">
						<td height="14" colspan="4">Messages</td>
					</tr>
					<tr>
						<td height="14" colspan="4"><%=bean.getMessageNotes()%></td>
					</tr>
				</table>
				<table width="100%" border="0">
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td align="right"><html:form
							action="/billing/CA/BC/SaveBilling">
							<input type="button" name="Submit3" value="Print Receipt"
								onclick="javascript:gotoPrintReceipt();" />
							<input type="button" name="Submit" value="Print Bill"
								onClick="javascript:window.print()">
							<input type="button" name="Submit2" value="Cancel"
								onClick="javascript:window.close()">
						</html:form></td>
					</tr>
				</table>

				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="right"></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<p>&nbsp;</p>
</body>
</html>
