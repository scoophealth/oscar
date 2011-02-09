<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
            if (session.getAttribute("userrole") == null) {
                response.sendRedirect("../logout.jsp");
            }
            String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
objectName="_admin,_admin.reporting" rights="r" reverse="<%=true%>">
    <%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/global.js"></script>
        <title>Dx Register Report</title>
        <link rel="stylesheet" type="text/css"
              href="../share/css/OscarStandardLayout.css">

        <script type="text/javascript" language="JavaScript"
        src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" language="JavaScript"
        src="../share/javascript/Oscar.js"></script>
        <link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" ></link>
        	<link rel="stylesheet" type="text/css" href="../css/jquery.autocomplete.css" />
	<script src="http://www.google.com/jsapi"></script>
	<script>
		google.load("jquery", "1");
	</script>
	<script src="../js/jquery.autocomplete.js"></script>
	
	<style>
		input { font-size: 100%; }
	</style>
	
    </head>
	
<%
	String editingCodeType = (String) session.getAttribute("editingCodeType");
	String editingCodeCode = (String) session.getAttribute("editingCodeCode");
	String editingCodeDesc = (String) session.getAttribute("editingCodeDesc");

%>
<body vlink="#0000FF" class="BodyStyle" onLoad="bustOut()" >

<nested:form action="/report/DxresearchReport?method=editDesc">
	<table border="1" width="100%">
		<tr> <input type="hidden" name="editingCodeType" value=<%=editingCodeType%> /> </tr>
		<tr> <input type="hidden" name="editingCodeCode" value=<%=editingCodeCode%> /> </tr>
		<tr>
			<th>Code type</th>
			<th>Code</th>
			<th>Description</th>
			<th>action</th>
		</tr>
        <tr>
            <td> <%=editingCodeType%> </td>
            <td> <%=editingCodeCode%> </td>
            <td> <input name="editingCodeDesc" value=<%=editingCodeDesc%> size=50> </td>
            <td> <input type="submit" name="submit" value="Modify"> </td>
        </tr>
	</table>
</nested:form>	

</body>
</html:html>
