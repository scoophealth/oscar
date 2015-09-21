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
<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
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

<html:html locale="true">
    <head>

        <title>Dx Register Report</title>



     
        
        
    <link rel="stylesheet" type="text/css" href="../css/jquery.autocomplete.css" />
	<script src="http://www.google.com/jsapi"></script>
	<script>
		google.load("jquery", "1");
	</script>
	<script src="../js/jquery.autocomplete.js"></script>
	
	<style>
		input { font-size: 100%; }
	</style>
	
	<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
    </head>
	
<%
	String editingCodeType = (String) session.getAttribute("editingCodeType");
	String editingCodeCode = (String) session.getAttribute("editingCodeCode");
	String editingCodeDesc = (String) session.getAttribute("editingCodeDesc");

%>
<body>

<nested:form action="/report/DxresearchReport?method=editDesc">

		 <input type="hidden" name="editingCodeType" value=<%=editingCodeType%> /> 
		 <input type="hidden" name="editingCodeCode" value=<%=editingCodeCode%> />
		
	<table class="table">
		<tr>
			<th>Code type</th>
			<th>Code</th>
			<th>Description</th>
			<th>Action</th>
		</tr>
        <tr>
            <td> <%=editingCodeType%> </td>
            <td> <%=editingCodeCode%> </td>
            <td> <input name="editingCodeDesc" value=<%=editingCodeDesc%> class="span4"> </td>
            <td> <input type="submit" name="submit" class="btn btn-primary" value="Modify"> </td>
        </tr>
	</table>
	
</nested:form>	

</body>
</html:html>
