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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.InstitutionDao" %>
<%@page import="org.oscarehr.common.model.Institution" %>
<%@page import="java.util.List" %>

<%
	InstitutionDao institutionDao = SpringUtils.getBean(InstitutionDao.class);
    		
    List<Institution> institutions = institutionDao.findAll();
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Edit Institutions</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<script language="javascript">
function BackToOscar()
{
       window.close();
}
</script>
<link rel="stylesheet" type="text/css" href="../../encounterStyles.css">
<body class="BodyStyle" vlink="#0000FF">

<html:errors />
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Consultation</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td class="Header">Edit Institutions
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr style="vertical-align: top">
		<td class="MainTableLeftColumn">
		<%oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar titlebar = new oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar(request);
                  out.print(titlebar.estBar(request));
                  %>
		</td>
		<td class="MainTableRightColumn">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!----Start new rows here-->
			<tr>
                            <td><%--bean:message
					key="oscarEncounter.oscarConsultationRequest.config.EditSpecialists.msgCheckOff" /--%><br>
				<bean:message
					key="oscarEncounter.oscarConsultationRequest.config.EditInstitutions.msgClickOn" /><br>


				</td>
			</tr>
			<tr>
				<td><html:form action="/oscarEncounter/EditSpecialists">
					<%-- input type="submit" name="delete"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.config.EditSpecialists.btnDeleteSpecialist"/>"--%>
					<div class="ChooseRecipientsBox1">
					<table>
						<tr>
							<th>&nbsp;</th>
							<th>Name</th>
							<th>Phone</th>
							<th>Fax</th>

						</tr>
						<tr>
							<td><!--<div class="ChooseRecipientsBox1">--> 
							<%
                                 for(Institution i:institutions){
							%>
							
							<tr>
								<td><input type="checkbox" name="specialists"
									value="<%=i.getId()%>"></td>
								<td>
								<%
	                                      out.print("<a href=\"../../EditInstitutions.do?id="+i.getId()+"\"/>");
	                                      out.print(i.getName());
	                                      out.print("</a>");
	                                    %>
								</td>
								
								<td><%=i.getPhone()%></td>
								<td><%=i.getFax()%></td>
							</tr>
						<% }%>
						</td>
						</tr>
					</table>
					</div>
				</html:form></td>
			</tr>
			<!----End new rows here-->

			<tr height="100%">
				<td></td>
			</tr>
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
