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

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%@ page import="org.oscarehr.common.model.ReportProvider" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
    ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
   	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
%>
<%    
 
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"-"+String.valueOf(curMonth) + "-" + String.valueOf(curDay);
//String nowDate = "2002-08-21";
int dob_yy=0, dob_dd=0, dob_mm=0, age=0;
String demo_no="", demo_sex="", provider_no="", roster="", patient_status="", status="";
String demographic_dob="1800";
String action = request.getParameter("action");
String last_name="", first_name="", mygroup="";
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<title><bean:message key="oscarReport.manageProvider.title" /></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>

<div class="container-fluid">
<form name="form1" action="dbManageProvider.jsp" method="post">

<h3><bean:message key="oscarReport.manageProvider.msgManageProvider" /> <span class="text-info"><%=action.toUpperCase()%></span></h3>

	<table class="table table-hover table-condensed">
	
	<thead>
		<tr>
			<td width="40%"><bean:message key="oscarReport.manageProvider.msgTeam" /></td>
			<td width="50%" align="left"><bean:message key="oscarReport.manageProvider.msgProviderName" /></td>
			<td width="10%" align="left"><bean:message	key="oscarReport.manageProvider.msgCheck" /></td>
		</tr>
	</thead>
	
	<tbody>
		<% 
			boolean bodd=true;	
		    int count1 = 0;
			 	          	    	   
		for(String myGroup: myGroupDao.getGroups()) {
			bodd=bodd?false:true; //for the color of rows
			
			
			for(MyGroup mg: myGroupDao.getGroupByGroupNo(myGroup)) {
				Provider p = providerDao.getProvider(mg.getId().getProviderNo());
				status = "";
				if (p != null) {
					for(ReportProvider rp:reportProviderDao.findByProviderNoTeamAndAction(p.getProviderNo(), mg.getId().getMyGroupNo(), action)) {
						status = rp.getStatus();
					}
				} else {
					continue;
				}
	           
	%>
	
		<tr class="<%=bodd?"info":" "%>">
			<td width="40%"><%=mg.getId().getMyGroupNo()%></td>
			<td width="50%" align="left">
			<%=p.getLastName()%>, <%=p.getFirstName()%>
			</td>
		<td>
			<input type="checkbox"
			name="provider<%=count1%>"
			value="<%=p.getProviderNo()%>|<%=mg.getId().getMyGroupNo()%>"
			<%=status.equals("A")?"checked":""%>>
		</td>
		</tr>
	
		<%
		count1 = count1 + 1;
		}
	
	}
	 	    
	%>
	</tbody>
	</table>
	
	<input class="btn" type='button' name='print' value='<bean:message key="global.btnPrint"/>'onClick='window.print()'> 
	<input type="hidden" name="submit" value="Submit">
	<input class="btn btn-primary" type=submit value=<bean:message key="oscarReport.manageProvider.btnSubmit"/>>
	<input type=hidden name=action value=<%=action%>> <input type=hidden name=count value=<%=count1%>>
</form>
</div>
</body>
</html:html>
