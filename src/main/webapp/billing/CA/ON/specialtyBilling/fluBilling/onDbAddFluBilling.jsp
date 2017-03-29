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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.*, oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%//
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

			BillingSpecPrep obj = new BillingSpecPrep();
			Vector vec = obj.getBillingClaimObj(request);
			boolean billSaved = obj.addABillingRecord(loggedInInfo,vec);

			if(billSaved) {
                String prevention = request.getParameter("goPrev");
				if ( prevention != null && !prevention.equalsIgnoreCase("") ) {
					
				response.sendRedirect("../../../../../oscarPrevention/AddPreventionData.jsp?prevention=" + prevention + "&demographic_no="
								+ request.getParameter("functionid"));
				}
			}

%>


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th>ADD A BILLING RECORD</th>
	</tr>
</table>
<%if (billSaved) {

			%>
<h1>Successful Addition of a billing Record.</h1>
<script LANGUAGE="JavaScript">             
              self.close();
              self.opener.refresh();              
        </script> <%} else {%>
<h1>Sorry, addition has failed.</h1>
<%}%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
