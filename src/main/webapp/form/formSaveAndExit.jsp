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
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ page import="java.sql.*, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title></title>
<html:base />
</head>
<script language="javascript">

function write2Parent(text){
    
    self.window.close();    
    opener.document.encForm.enTextarea.value = opener.document.encForm.enTextarea.value + "\n" + text;
    opener.setTimeout("document.encForm.enTextarea.scrollTop=document.encForm.enTextarea.scrollHeight", 0);  // setTimeout is needed to allow browser to realize that text field has been updated 
    opener.focus();
    opener.document.encForm.enTextarea.focus();
 }

 function closePopup(){
    if(self.window && self.open.window && !self.closed){
        self.open.window.close();
        self.close();        
    }
 }
</script>


<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onunload="javascript: closePopup()">
<table>
	<tr>
		<td><script>
            write2Parent("<%=request.getAttribute("diagnosisVT")%>");            
        </script></td>
	</tr>
</table>




</body>
</html:html>
