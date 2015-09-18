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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%@ page import="org.oscarehr.common.model.ReportProvider" %>

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
  int xcount = 0, ycount =0;
  String nowDate = String.valueOf(curYear)+"-"+String.valueOf(curMonth) + "-" + String.valueOf(curDay);
//String nowDate = "2002-08-21";
int dob_yy=0, dob_dd=0, dob_mm=0, age=0;
String demo_no="", demo_sex="", provider_no="", roster="", patient_status="", status="";
String demographic_dob="1800";
String action = request.getParameter("action");
String last_name="", first_name="", mygroup="";
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage Provider</title>
<link rel="stylesheet" href="oscarReport.css">
<link rel="stylesheet" href="tree.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />


<script type="text/javascript" src="tree.js"></script>
<script type="text/javascript">
		<!--
		var Tree = new Array;
		// nodeId | parentNodeId | nodeName | nodeUrl

<% 
	boolean bodd=true;	
	int count1 = 0;
	ycount=1; 
	for(String myGroup: myGroupDao.getGroups()) {
		xcount = ycount;
%>
Tree[<%=ycount-1%>]  = "<%=ycount%>|0|<%=myGroup%>  |#";
<%	 	             
		ycount = ycount + 1; 
			               
		for(MyGroup mg: myGroupDao.getGroupByGroupNo(myGroup)) {
			Provider p = providerDao.getProvider(mg.getId().getProviderNo());
			status = "";      
			for(ReportProvider rp:reportProviderDao.findByProviderNoTeamAndAction(p.getProviderNo(), mg.getId().getMyGroupNo(), action)) {
				status = rp.getStatus();
			}
%>

Tree[<%=ycount-1%>]  = "<%=ycount%>|<%=xcount%>|<%=p.getLastName()%>, <%=p.getFirstName()%>|#";  
<%
			count1 = count1 + 1;
			ycount = ycount + 1;
		}
}
 	    
%>
		
		//-->
	</script>
</head>

<body>

<div id="tree"><script type="text/javascript">
<!--
	createTree(Tree); 
//-->
</script></div>

<br />
<br />

<a href="mailto:drop@destroydrop.com">drop@destroydrop.com</a>

</body>

</html>
