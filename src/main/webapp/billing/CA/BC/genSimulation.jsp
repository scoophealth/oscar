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
if(session.getValue("user") == null) response.sendRedirect("../../../logout.jsp");
%>

<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, oscar.oscarBilling.ca.bc.MSP.*, java.net.*"
	errorPage="../../../errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbBilling.jspf"%>

<%
String errorMsg = "";

int bCount = 1;
String batchCount = "0";
String provider = request.getParameter("provider");

String proOHIP=""; 
String billinggroup_no;
   
String dateRange = "";
String htmlValue="";
String oscar_home= oscarVariables.getProperty("project_home")+".properties";

String dateBegin = request.getParameter("xml_vdate");
String dateEnd = request.getParameter("xml_appointment_date");
if (dateEnd.compareTo("") == 0) dateEnd = request.getParameter("curDate");
if (dateBegin.compareTo("") == 0){
	dateRange = " and billing_date <= '" + dateEnd + "'";
}else{
	dateRange = " and billing_date >='" + dateBegin + "' and billing_date <='" + dateEnd + "'";
}

ResultSet rslocal = apptMainBean.queryResults(request.getParameter("provider"), "search_provider_ohip_dt");
while(rslocal.next()){
	proOHIP = rslocal.getString("ohip_no"); 
   
	billinggroup_no= rslocal.getString("billing_no"); //SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");

	if (billinggroup_no == null ||  billinggroup_no.compareTo("") == 0 ||  billinggroup_no.compareTo("null")==0 ){
		//error msg here
		//errorMsg += "The provider's group no is not correct!<br>";
		billinggroup_no = "";
	} 

	oscar.oscarBilling.ca.bc.MSP.ExtractBean extract = new oscar.oscarBilling.ca.bc.MSP.ExtractBean();
	extract.setOscarHome(oscar_home);
	extract.seteFlag("0");
	extract.setDateRange(dateRange);
	extract.setOhipVer(request.getParameter("verCode"));
	extract.setProviderNo(proOHIP);
	extract.setOhipCenter(request.getParameter("billcenter"));
	extract.setGroupNo(billinggroup_no);
	extract.setBatchCount(String.valueOf(bCount));
	extract.dbQuery();

	htmlValue += "<font color='red'>" + errorMsg + "</font>" + extract.getHtmlCode()+ "<hr/><br/><br/>";
}
rslocal.close();

request.setAttribute("html",htmlValue);
%>

<jsp:forward page='billingSim.jsp'>
	<jsp:param name="xml_appointment_date" value='<%=dateEnd%>' />
	<jsp:param name="xml_v_date" value='<%=dateBegin%>' />
	<jsp:param name="provider" value='<%=provider%>' />
</jsp:forward>
