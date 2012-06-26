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
	import="java.math.*, java.util.*, java.sql.*, oscar.*, oscar.oscarBilling.ca.on.OHIP.*, java.net.*"
	errorPage="errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbBilling.jspf"%>

<%
String errorMsg = "";
int PROVIDER_BILLINGNO_LENGTH = 6;
int PROVIDER_SPECIALTYCODE_LENGTH = 2;
int PROVIDER_GROUPNO_LENGTH = 4;

int bCount = 1;
String batchCount = "0";
String provider = request.getParameter("provider");
if (provider.length() != PROVIDER_BILLINGNO_LENGTH) errorMsg = "The provider's billing code is not correct!<br>";

String proOHIP=""; 
String specialty_code; 
String billinggroup_no;
   
String dateRange = "";
String htmlValue="";


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
	billinggroup_no= SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
	specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");

	if (specialty_code == null || specialty_code.compareTo("") == 0 || specialty_code.compareTo("null")==0 || specialty_code.length() != PROVIDER_SPECIALTYCODE_LENGTH){
		//error msg here
		errorMsg += "The provider's specialty code is not correct!<br>";
		specialty_code = "00"; 
	}

	if (billinggroup_no == null ||  billinggroup_no.compareTo("") == 0 ||  billinggroup_no.compareTo("null")==0 || billinggroup_no.length() != PROVIDER_GROUPNO_LENGTH){
		//error msg here
		errorMsg += "The provider's group no is not correct!<br>";
		billinggroup_no = "0000";
	} 

	oscar.oscarBilling.ca.on.OHIP.ExtractBean extract = new oscar.oscarBilling.ca.on.OHIP.ExtractBean();
	//extract.setOscarHome(oscar_home);
	extract.seteFlag("0");
	extract.setDateRange(dateRange);
	extract.setOhipVer(request.getParameter("verCode"));
	extract.setProviderNo(proOHIP);
	extract.setOhipCenter(request.getParameter("billcenter"));
	extract.setGroupNo(billinggroup_no);
	extract.setSpecialty(specialty_code);
	extract.setBatchCount(String.valueOf(bCount));
	extract.dbQuery();

	htmlValue = "<font color='red'>" + errorMsg + "</font>" + extract.getHtmlValue();
}
rslocal.close();

request.setAttribute("html",htmlValue);
%>

<jsp:forward page='billingOHIPsimulation.jsp'>
	<jsp:param name="xml_appointment_date" value='<%=dateEnd%>' />
	<jsp:param name="xml_v_date" value='<%=dateBegin%>' />
	<jsp:param name="provider" value='<%=provider%>' />
</jsp:forward>
