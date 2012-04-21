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

<%@ page
	import="java.util.*, oscar.dms.data.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
HashMap<String,String> doctypeerrors = new HashMap<String,String>();
if (request.getAttribute("doctypeerrors") != null) {
	doctypeerrors = (HashMap<String,String>) request.getAttribute("doctypeerrors");
}
%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
<script> 

function submitUpload(object) {
    object.Submit.disabled = true;
    
    return true;
}
</script>
<title>Add New Document Type</title>
</head>
<body>
<div>
<% Iterator iter = doctypeerrors.keySet().iterator();
while (iter.hasNext()){%>
<font class="warning">Error: <bean:message
	key="<%= doctypeerrors.get(iter.next())%>" /></font><br />
<% } %> 
</div>

<table class="MainTable" id="scrollNumber1" name="documentCategoryTable" style="margin: 0px;">
<tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Document Types</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Add New Document Type</td>
                        </tr>
                    </table>
                </td>
            </tr>
<html:form action="/dms/addDocumentType" method="POST"
	enctype="multipart/form-data" styleClass="forms"
	onsubmit="return submitUpload(this)">
<table>
	<tr>
		<td><b>Select module name: </b></td>
	
		<td >
			<input <% if (doctypeerrors.containsKey("modulemissing")) {%>
				class="warning" <%}%> id="function" type="radio" name="function" value="Demographic"> Demographic</td>
		<td >
			<input <% if (doctypeerrors.containsKey("modulemissing")) {%>
				class="warning" <%}%> id="function" type="radio" name="function" value="Provider"> Provider</td>
	
	</tr>
	
	<tr>
	<td><b>Enter document Type: </b></td>
	<td >
		<input <% if (doctypeerrors.containsKey("doctypemissing")) {%>
				class="warning" <%}%> id="docType" type="text" name="docType" value=""> <br></td>
	</tr>
	
	

	<tr>
		<td><input type="submit" name="submit" value="Submit"/> </td> 
		<td><input type="button" name="button" value="Cancel" onclick=self.close()> </td>
	</tr>
</table>
</html:form>
</table>
</body>
</html>
