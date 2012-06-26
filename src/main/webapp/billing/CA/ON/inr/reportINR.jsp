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
<%
if(session.getAttribute("user") == null)    response.sendRedirect("../../../../logout.jsp");
String user_no="";
user_no = (String) session.getAttribute("user");
%>

<%@ page import="java.util.*, java.sql.*, java.net.*"
	errorPage="errorpage.jsp"%>
<%@ include file="../../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />

<%@ include file="dbINR.jspf"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
%>
<% 	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);
  String nowTime = now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
 String clinicview = oscarVariables.getProperty("clinic_view");
   String Clinic_no = oscarVariables.getProperty("clinic_no");


  %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>INR Billing</title>
<script language="JavaScript">
<!--
function openBrWindow(theURL,winName,features) {
  window.open(theURL,winName,features);
}

function jumpMenu(targ,selObj,restore){
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
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


//-->
</script>
<link rel="stylesheet" href="../billing.css">
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="0" onLoad="setfocus()">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF"> INR Batch Billing </font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>

<% String providerview=request.getParameter("provider_no")==null?"":request.getParameter("provider_no");

 %>
<table width="100%" border="0" bgcolor="#E6F0F7">
	<form name="serviceform" method="post"
		action="<%=oscarVariables.getProperty("isNewONbilling","").equals("true")? "onGenINRbilling.jsp":"genINRbilling.jsp" %>">
	<tr>
		<td width="220"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Select provider </font></b></td>
		<td width="254"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"> <select name="provider"
			onChange="jumpMenu('parent',this,0)">
			<option value="#"><b>Select Provider</b></option>
			<option value="reportINR.jsp?provider_no=all"
				<%=providerview.equals("all")?"selected":""%>><b>All
			Provider</b></option>
			<% String proFirst="";
           String proLast="";
           String proName="";
            String proName1="";
           String proOHIP="";
          String specialty_code;
String billinggroup_no;
ArrayList providerArray = new ArrayList();
String[] providerArr = new String[2];
           int Count = 0;
        ResultSet rslocal;
        rslocal = null;
 rslocal = apptMainBean.queryResults("%", "search_provider_dt");
 while(rslocal.next()){
 proFirst = rslocal.getString("first_name");
 proLast = rslocal.getString("last_name");
proName = proFirst + " " + proLast;
 proOHIP = rslocal.getString("provider_no");
// billinggroup_no= SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
// specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");

providerArr[0] = proOHIP;
providerArr[1] = proName;
providerArray.add(providerArr);
 %>
			<option value="reportINR.jsp?provider_no=<%=proOHIP%>"
				<%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<%

 }
//
  %>
		</select> </font></b></td>
		<td width="254"><font color="#003366"><b><font
			face="Arial, Helvetica, sans-serif" size="2">Clinic Location:
		<input type="hidden" name="billcenter" value="G"> </font></b><font
			face="Arial, Helvetica, sans-serif" size="2"> </font></font></td>
		<td width="277"><select name="xml_location"
			datafld='xml_location'>
			<% ResultSet rsclinic = null;
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
		</select><font color="#003366"> <input type="hidden" name="verCode"
			value="V03"> <input type="hidden" name="curUser"
			value="<%=user_no%>"> <input type="hidden" name="curDate"
			value="<%=nowDate%>"> <input type="hidden" name="curTime"
			value="<%=nowTime%>"> </font></td>
	</tr>
	<tr>
		<td colspan=5>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">

			<tr bgcolor="#CCCCFF">
				<td width="12%" height="16">Selection</td>
				<td width="22%" height="16">Demographic</td>
				<td width="22%" height="16">Provider</td>
				<td width="12%" height="16">Service</td>
				<td width="12%" height="16">Amount</td>
				<td width="10%" height="16">Diagnostic</td>
				<td width="10%" height="16">Last Bill Date</td>

			</tr>
			<% String demono="",demo_name="",demo_dob="",demo_hin="", billinginr_no="", provider_no="";
    String provider_ohip_no="", provider_rma_no="", diagnostic_code="", service_code="", billing_amount="";
    String billing_unit="", billdate="", billstatus="";
    int colorCount = 0;
       String color="";
  int Count1 = 0;


       if (providerview.compareTo("all")==0){

   //  for (int i=0;i<providerArray.size(); i++){


     ResultSet rsdemo = null;
      rsdemo = apptMainBean.queryResults( "%", "search_inrbilling_dt");
      while(rsdemo.next()){

      billinginr_no = rsdemo.getString("billinginr_no");
      demono = rsdemo.getString("demographic_no");
      demo_name = rsdemo.getString("demographic_name");
      demo_hin = rsdemo.getString("hin");
      demo_dob = rsdemo.getString("dob");
      provider_no = rsdemo.getString("provider_no");

              rslocal = null;
       rslocal = apptMainBean.queryResults(provider_no, "search_provider_name");
       while(rslocal.next()){
       proFirst = rslocal.getString("first_name");
       proLast = rslocal.getString("last_name");
proName1 = proFirst + " " + proLast;
}
      provider_ohip_no = rsdemo.getString("provider_ohip_no");
      provider_rma_no = rsdemo.getString("provider_rma_no");
      diagnostic_code = rsdemo.getString("diagnostic_code");
      service_code = rsdemo.getString("service_code");
      billing_amount = rsdemo.getString("billing_amount");
      billing_unit = rsdemo.getString("billing_unit");
      billdate = rsdemo.getString("createdatetime");
      billstatus = rsdemo.getString("status");
      if (colorCount == 0){
    	      colorCount = 1;
    	      color = "#FFFFFF";
    	      } else {
    	      colorCount = 0;
	      color="#EEEEFF";
	      }
  Count1 = Count1 + 1;
    %>
			<tr bgcolor="<%=color%>">
				<td width="12%" height="16"><input type="checkbox"
					name="inrbilling<%=billinginr_no%>"></td>
				<td width="22%" height="16"><a href="#"
					onClick='rs("billinginrupdate","updateINRbilling.jsp?demono=<%=demono%>&billinginr_no=<%=billinginr_no%>&servicecode=<%=service_code%>&billingamount=<%=billing_amount%>&dxcode=<%=diagnostic_code%>&demo_name=<%=URLEncoder.encode(demo_name)%>&provider_name=<%=URLEncoder.encode(proName1)%>","380","300","0")'><%=demo_name%></a></td>
				<td width="22%" height="16"><%=proName1%></td>
				<td width="12%" height="16"><%=service_code%></td>
				<td width="12%" height="16"><%=billing_amount%></td>
				<td width="10%" height="16"><%=diagnostic_code%></td>
				<td width="10%" height="16">
				<% if (billstatus.compareTo("A")==0) {%><%=billdate.substring(0,10)%>
				<%}else{%>Not Available<%}%>
				</td>
			</tr>
			<%}
  } else{

  	//String[] billArray = (String[])providerArray.get(i);
       ResultSet rsdemo = null;
        rsdemo = apptMainBean.queryResults(providerview, "search_inrbilling_dt");
        while(rsdemo.next()){

        billinginr_no = rsdemo.getString("billinginr_no");
        demono = rsdemo.getString("demographic_no");
        demo_name = rsdemo.getString("demographic_name");
        demo_hin = rsdemo.getString("hin");
        demo_dob = rsdemo.getString("dob");
        provider_no = rsdemo.getString("provider_no");
                      rslocal = null;
	       rslocal = apptMainBean.queryResults(provider_no, "search_provider_name");
	       while(rslocal.next()){
	       proFirst = rslocal.getString("first_name");
	       proLast = rslocal.getString("last_name");
	proName1 = proFirst + " " + proLast;
}
        provider_ohip_no = rsdemo.getString("provider_ohip_no");
        provider_rma_no = rsdemo.getString("provider_rma_no");
        diagnostic_code = rsdemo.getString("diagnostic_code");
        service_code = rsdemo.getString("service_code");
        billing_amount = rsdemo.getString("billing_amount");
        billing_unit = rsdemo.getString("billing_unit");
          billdate = rsdemo.getString("createdatetime");
      billstatus = rsdemo.getString("status");
        if (colorCount == 0){
      	      colorCount = 1;
      	      color = "#FFFFFF";
      	      } else {
      	      colorCount = 0;
  	      color="#EEEEFF";
  	      }

  Count1 = Count1 + 1;
  %>
			<tr bgcolor="<%=color%>">
				<td width="12%" height="16"><input type="checkbox"
					name="inrbilling<%=billinginr_no%>"></td>
				<td width="22%" height="16"><a href="#"
					onClick='rs("billinginrupdate","updateINRbilling.jsp?demono=<%=demono%>&billinginr_no=<%=billinginr_no%>&servicecode=<%=service_code%>&billingamount=<%=billing_amount%>&dxcode=<%=diagnostic_code%>&demo_name=<%=URLEncoder.encode(demo_name)%>&provider_name=<%=URLEncoder.encode(proName1)%>","380","300","0")'><%=demo_name%></a></td>
				<td width="22%" height="16"><%=proName1%></td>
				<td width="12%" height="16"><%=service_code%></td>
				<td width="12%" height="16"><%=billing_amount%></td>
				<td width="10%" height="16"><%=diagnostic_code%></td>
				<td width="10%" height="16">
				<% if (billstatus.compareTo("A")==0) {%><%=billdate.substring(0,10)%>
				<%}else{%>Not Available<%}%>
				</td>
			</tr>
			<%}}%>
			<% if ( Count1 == 0) { %>
			<tr bgcolor="#FFFFFF">
				<td colspan=7>No Match Found</td>
				</td>
				<%} else {%>

			<tr bgcolor="#FFFFFF">
				<a href="#"
					onClick='rs("billingcalendar","../billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=service","380","300","0")'>Service
				Date:</a>
				<input type="text" name="xml_appointment_date"
					value="<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>"
					size="12" datafld='xml_appointment_date'>
				<td colspan=7><input type="submit" name="submit"
					value="Generate INR Batch Billing"> <input type="hidden"
					name="rowCount" value="<%=Count1%>"> <input type="hidden"
					name="clinic_no" value="<%=Clinic_no%>"
					<input type="hidden" name="visittype" value="00"

  </td>
   </td>
<%
}
%>
</table>
    </td>
    </tr>
  </form>
</table>

</body>
</html>
