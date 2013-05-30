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
  if(session.getValue("user") == null) 
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");    
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");

%>
<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="../errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
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
    
    String[] param1 =new String[2];
	  param1[0]=request.getParameter(code);
	  param1[1]=code;
//	  param1[1]=request.getParameter("apptProvider_no"); param1[2]=request.getParameter("appointment_date"); param1[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
  	 rowsAffected = apptMainBean.queryExecuteUpdate(param1,"updatebillservice");
 
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
