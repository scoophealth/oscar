<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.util.*, oscar.dms.EDocUtil, oscar.dms.data.ChangeDocStatusForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<% 
ArrayList<String> doctypesD = EDocUtil.getDoctypes("demographic");
ArrayList<String> doctypesP = EDocUtil.getDoctypes("provider");

ArrayList<String> doctypes;

ChangeDocStatusForm formdata = new ChangeDocStatusForm();
HashMap<String,String> doctypeerrors = new HashMap<String,String>();
if (request.getAttribute("doctypeerrors") != null) {
	doctypeerrors = (HashMap<String,String>) request.getAttribute("doctypeerrors");
}

%>


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript">
function selectDocMenu() {
	if (document.getElementById(module).options[this.selectedIndex].value=="Demographic"){
		document.getElementById(provdiv).style.display='hidden' ;
	} else {
		document.getElementById(demodiv).style.display= 'hidden' ;
	}
}


</script>
<% Iterator iter = doctypeerrors.keySet().iterator();
while (iter.hasNext()){%>
<font class="warning">Error: <bean:message
	key="<%=doctypeerrors.get(iter.next())%>" /></font><br />
<% } %> 

<link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
<link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
<title>Status Update Page</title>
</head>
<body>
<table class="MainTable" id="scrollNumber1" name="documentCategoryTable" style="margin: 0px;">
			<tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Document Status</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Change Document Status</td>
                        </tr>
                    </table>
                </td>
            </tr>
 
<html:form action="/dms/changeDocStatus" method="POST"
	enctype="multipart/form-data" styleClass="forms"
	onsubmit="return submitUpload(this)" >
	
<table>
	<br>
	<tr>
		<td><b>Select Document Type: </b></td>
		<td >
			<div id='demodiv'>		
			<select id="docTypeD" name="docTypeD" style="width: 160" >
				<option value="">Demographic Document Types</option>
			<% for (String doctypeD : doctypesD) { %>
				<option value="<%= doctypeD%>"
						<%=(formdata.getDocTypeD().equals(doctypeD))?" selected":""%>><%= doctypeD%></option>
						<%}%>
			</select>		
			</div>
		</td>
	
		<td >
			<select id="statusD" name="statusD" style="width: 160" >
				<option value="">Select Status</option>
				<option value="A">Active</option>
				<option value="I">Inactive</option>
				
			</select>
		</td>
	</tr>
</table>

<table>	
<br>	
	<tr> 
		<td><b>Select Document Type: </b></td>
		<td>
		<div id='provdiv'>
			<select id="docTypeP" name="docTypeP" style="width: 160" >
				<option value="">Provider Document Types</option>
				<%
					for ( String doctypeP : doctypesP) { %>
				<option value="<%= doctypeP%>"
					<%=(formdata.getDocTypeP().equals(doctypeP))?" selected":""%>><%= doctypeP%></option>
					<%}%>
			</select>				
		</div></td>
	
		<td >
			<select id="statusP" name="statusP" style="width: 160" >
				<option value="">Select Status</option>
				<option value="A">Active</option>
				<option value="I">Inactive</option>
			</select>
		</td>
	</tr>

 </table>
 <table>
 <br>
	<tr>
		<td><input type="submit" name="submit" value="Update"/> </td>
		<td><input type="button" name="button" value="Cancel"
			 onclick=self.close()> </td> 
	</tr>
</table>
</html:form>	
</table>
</body>
</html>
