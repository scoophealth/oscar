<!DOCTYPE html>
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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.util.*,oscar.oscarBilling.ca.bc.pageUtil.*" %>
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

<html:html locale="true">



<head>
<title>Add Private Billing Code</title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript">

function isNumeric(strString){
    var validNums = "0123456789.";
    var strChar;
    var retval = true;

    for (i = 0; i < strString.length && retval == true; i++){
       strChar = strString.charAt(i);
       if (validNums.indexOf(strChar) == -1){
          retval = false;
       }
    }
     return retval;
}

function checkUnits(){
	if  (!isNumeric(document.BillingAddCodeForm.value.value)){
		alert("Price has to be a numeric value");
	        document.BillingAddCodeForm.value.focus();
		return false;
	}
	return true;
}
</script>
</head>

<body>
	<h3>Add Private Billing Code</h3>
	<div class="container-fluid well">
	
    <html:form action="/billing/CA/BC/billingAddCode" onsubmit="return checkUnits();">
      <%
		BillingAddCodeForm frm = (BillingAddCodeForm) request.getAttribute("BillingAddCodeForm");
		String isEdit = request.getParameter("edit")!=null?request.getParameter("edit"):"";
		if (request.getAttribute("code") != null){
			frm.setCode((String) request.getAttribute("code"));
			frm.setDesc((String) request.getAttribute("desc"));
			frm.setValue((String) request.getAttribute("value"));
		}

		if (request.getAttribute("returnMessage") != null){%>
			<%=request.getAttribute("returnMessage")%>
		<%}%>                
	<html:hidden property="whereTo" value="private"/>

	
        Service Code: <br>
	A-<html:text property="code" maxlength="9" style="width:100px" />
	<div style="margin-top:-10px;margin-bottom:10px;"><small>Private Codes will be prefixed with 'A' by default</small></div>
		
	Description:<br>
	<html:text property="desc"/><br> 

        Price:<br>
	<html:text property="value"/><br> 

	<html:submit styleClass="btn btn-primary" value="Add"/> <a href="billingPrivateCodeAdjust.jsp" class="btn">Cancel</a>
    </html:form>
    </div>
</body>
</html:html>
