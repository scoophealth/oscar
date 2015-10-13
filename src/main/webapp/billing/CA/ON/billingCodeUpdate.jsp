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
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%

  String curUser_no = (String) session.getAttribute("user");    
%>
<%@ page import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="../errorpage.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.BillingServiceDao" %>
<%@page import="org.oscarehr.common.model.BillingService" %>
<%
	BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Summary</title>
<script LANGUAGE="JavaScript">
<!--
function CodeAttach(File0, File1, File2) {
      
<% 
if(request.getParameter("nameF") != null) {
		out.println("self.opener." + request.getParameter("nameF") + " = File0;");
} else {
%>      
      self.opener.document.serviceform.xml_other1.value = File0;
      self.opener.document.serviceform.xml_other2.value = File1;
      self.opener.document.serviceform.xml_other3.value = File2;
<% } %>
      self.close();
}
-->
</script>

</head>
<body>
<%
 if (request.getParameter("update").equals("Confirm")) {
 
 
        String temp="";
        String[] param =new String[10];
        param[0] = "";
        param[1] = "";
        param[2] = "";
	
	int Count = 0;
	
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if( temp.indexOf("code_")==-1 ) continue; 
                 param[Count] = temp.substring(5).toUpperCase(); // + " |" + request.getParameter("codedesc_" + temp.substring(5));
                 Count = Count + 1;
                 
      }
    
    if (Count == 1) {
    param[1] = "";
    param[2] = "";
    }
        if (Count == 2) {
        param[2] = "";
       
    }
    
    if (Count ==0) {
    %>
<p>No input selected</p>
<input type="button" name="back" value="back"
	onClick="javascript:history.go(-1);return false;">
<%
    }else{
    %>
<script LANGUAGE="JavaScript">
    <!--
     CodeAttach('<%=param[0]%>','<%=param[1]%>', '<%=param[2]%>' ); 
    -->
    
</script>
<%
}
} else {
%>
<%

  String code = request.getParameter("update");
  code = code.substring(code.length()-5);
  
 int rowsAffected=0;
    
	for( BillingService bs:billingServiceDao.findByServiceCode(code)) {
  		bs.setDescription(request.getParameter(code));
  		billingServiceDao.merge(bs);
  		rowsAffected++;
  	}
%>
<%
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
    history.go(-1);return false;
    self.opener.refresh();
</script>
<% } %>

</body>
</html>
