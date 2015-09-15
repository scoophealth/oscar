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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page
	import="java.util.*, java.sql.*, java.net.*,java.text.DecimalFormat, oscar.oscarProvider.data.* "%>

<%

GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);

//../form/SetupForm.do?formName=VTForm&demographic_no=10091&formId=0&provNo=999998
String demo = request.getParameter("demographic_no") ;
String study_no = request.getParameter("demographic_no"); 
String prov_no = (String) session.getAttribute("user");

oscar.oscarProvider.data.ProviderData pd = new oscar.oscarProvider.data.ProviderData(prov_no);
//pd.getProviderName(prov_no);

String userfirstname = pd.getFirst_name(); 
String userlastname  = pd.getLast_name();


String popupUrl = "../form/SetupForm.do?formName=VTForm&demographic_no="+demo+"&study_no="+study_no+"&provNo="+prov_no+"&formId=0";

String url = "../../oscarEncounter/IncomingEncounter.do?providerNo="+prov_no+"&appointmentNo=&demographicNo="+demo+"&curProviderNo=&reason="+URLEncoder.encode("Vascular Study")+"&userName="+URLEncoder.encode( userfirstname+" "+userlastname)+"&curDate="+curYear+"-"+curMonth+"-"+curDay+"&appointmentDate=&startTime=&status=&popupUrl="+URLEncoder.encode(popupUrl);
response.sendRedirect(url );
%>
