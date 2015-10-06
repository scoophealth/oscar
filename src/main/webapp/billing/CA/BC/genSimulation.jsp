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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.util.DateRange"%>


<%@ page import="java.math.*, java.util.*, java.sql.*, oscar.*, oscar.oscarBilling.ca.bc.MSP.*, java.net.*, oscar.*" errorPage="../../../errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>


<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>

<%
String errorMsg = "";

int bCount = 1;
String batchCount = "0";
String provider = request.getParameter("provider");

String proOHIP=""; 
String billinggroup_no;
   
DateRange dateRange = null;
String htmlValue="";
String oscar_home= oscarVariables.getProperty("project_home")+".properties";

String dateBegin = request.getParameter("xml_vdate");
String dateEnd = request.getParameter("xml_appointment_date");
if (dateEnd.compareTo("") == 0) dateEnd = request.getParameter("curDate");
if (dateBegin.compareTo("") == 0){
	dateRange = new DateRange(null, ConversionUtils.fromDateString(dateEnd));
}else{
	dateRange = new DateRange(ConversionUtils.fromDateString(dateBegin), ConversionUtils.fromDateString(dateEnd));
}

for(Provider p:providerDao.getActiveProviders()) {
	if(p.getOhipNo()!=null && !p.getOhipNo().isEmpty()) {
			
	proOHIP = p.getOhipNo();
   
	billinggroup_no= p.getBillingNo(); //SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");

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
} }


request.setAttribute("html",htmlValue);
%>

<jsp:forward page='billingSim.jsp'>
	<jsp:param name="xml_appointment_date" value='<%=dateEnd%>' />
	<jsp:param name="xml_v_date" value='<%=dateBegin%>' />
	<jsp:param name="provider" value='<%=provider%>' />
</jsp:forward>
