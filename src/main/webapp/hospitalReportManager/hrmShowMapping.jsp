<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="java.util.*, org.oscarehr.hospitalReportManager.*, org.oscarehr.hospitalReportManager.model.HRMCategory"%>

<%
	
	String deepColor = "#CCCCFF", weakColor = "#EEEEFF";

	String country = request.getLocale().getCountry();
	

%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title>Show Mappings</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css"
	href="../share/css/eformStyle.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>

</head>

<body onunload="updateAjax()" class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175">HRM</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Show Mappings</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">
			</td>
		<td class="MainTableRightColumn" valign="top">
			<% if (request.getAttribute("success") != null ) {
				if ((Boolean) request.getAttribute("success")) { %>
				Successfully added the mapping<br />
			<% } else { %>
				Error encountered while adding the mapping<br />
			<% }
			}%>
			
			<a href="<%=request.getContextPath() %>/hospitalReportManager/hrmAddClassMapping.jsp">+ Add a class mapping</a>
			
			<table class="elements" width="100%">
					<tr bgcolor=<%=deepColor%>>
						
						<th>
							Sending Facility Id
						</th>
						<th>Class Name</th>
						<th>Sub-Class Name</th>
						<th>Mnemonic</th>
						<th>Description</th>
						<th>Category</th>
						<th></th>
						
					</tr>
					<%
						ArrayList<HashMap<String,? extends Object>> hrmmappings;
						
							hrmmappings = HRMUtil.listMappings();
						
						
						for (int i = 0; i < hrmmappings.size(); i++)
						{
							HashMap<String,? extends Object> curmapping = hrmmappings.get(i);
					%>
					<tr bgcolor="<%=((i % 2) == 1)?"#F2F2F2":"white"%>">
						
						<td><%=curmapping.get("id")%></td>
						<td><%=curmapping.get("class")%></td>
						<td align='center'><%=curmapping.get("sub_class")%></td>
						<td><%=curmapping.get("mnemonic") %></td>
						<td><%=curmapping.get("description") %></td>
						<td><%=(curmapping.get("category")!=null) ? ((HRMCategory) curmapping.get("category")).getCategoryName() : "N/A" %></td>
						
						<td><a href="<%=request.getContextPath() %>/hospitalReportManager/hrmEditClassMapping.jsp?id=<%=curmapping.get("mappingId") %>">Edit</a>&nbsp;<a href="<%=request.getContextPath() %>/hospitalReportManager/Mapping.do?deleteMappingId=<%=curmapping.get("mappingId") %>">Delete</a></td>
					</tr>
					<%
						} %>
							
				</table>
				
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
