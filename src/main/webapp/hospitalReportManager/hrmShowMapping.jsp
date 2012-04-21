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
		<td class="MainTableTopRowLeftColumn" width="175"><bean:message
			key="eform.showmyform.msgMyForm" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Show Mappings</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
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
						<td><%=((HRMCategory) curmapping.get("category")).getCategoryName() %></td>
						<td><a href="<%=request.getContextPath() %>/hospitalReportManager/Mapping.do?deleteMappingId=<%=curmapping.get("mappingId") %>">Delete</a></td>
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
