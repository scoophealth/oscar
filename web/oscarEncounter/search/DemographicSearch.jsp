<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<%@ page import="oscar.oscarEncounter.immunization.data.*, oscar.util.*"%>
<%@ page
	import="oscar.oscarEncounter.immunization.pageUtil.*, java.util.*, org.w3c.dom.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.search.demographicSearch.title" /></title>
</head>
<html:base />

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />





</head>
<script language="javascript">
function BackToOscar()
{
       window.close();
}
</script>
<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1"
	name="<bean:message key="oscarEncounter.search.demographicSearch.msgEncounter"/>">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" NOWRAP><bean:message
			key="oscarEncounter.search.demographicSearch.msgPatSearch" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td></td>
				<td></td>
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



		<td></td>
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!--Start new rows here-->
			<tr>
				<td><html:form action="/oscarEncounter/SearchDemographic">
					<table>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.msgFirstName" />:</td>
							<td><html:text property="firstName" /></td>

							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.msgLastName" />:</td>
							<td><html:text property="lastName" /></td>
							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.msgSex" />:</td>
							<td>
							<table height="100%">
								<tr>
									<td><bean:message
										key="oscarEncounter.search.demographicSearch.formMale" /></td>
									<td><html:radio property="sex" value="M" />
									</td>
									<td><bean:message
										key="oscarEncounter.search.demographicSearch.formFemale" /></td>
                                                                        <td><html:radio property="sex" value="F" />
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.formBirthYear" />:
							</td>
							<td><html:text property="yearOfBirth" /></td>

							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.formBirthMonth" />:
							</td>
							<td><html:text property="monthOfBirth" /></td class="tite4">

							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.formBirthDay" />:</td>
							<td><html:text property="dayOfBirth" /></td>
						</tr>

						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.formAddr" />:</td>
							<td><html:text property="address" /></td>
							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.formCity" />:</td>
							<td colspan="3"><html:text property="city" /></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.formPhone" />:</td>
							<td colspan="5"><html:text property="phone" /></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.formHIN" />:</td>
							<td colspan="5"><html:text property="hin" /></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.search.demographicSearch.formCNumber" />:</td>
							<td colspan="5"><html:text property="chartNumber" /></td>
						</tr>




						<tr>
							<td><input type="submit" name="Submit" value="Submit" /> <input
								type="reset" value="Reset" /></td>
						</tr>
					</table>
				</html:form> <%
                java.util.Vector searchVec = (java.util.Vector) request.getAttribute("searchVector");
                if (searchVec != null){

                    if (searchVec.size() != 0){

                    %>
				<table>
					<tr>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgDemoN" /></th>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgLastName" /></th>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgFirstName" /></th>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgAddr" /></th>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgCity" /></th>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgProvice" /></th>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgSex" /></th>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgHin" /></th>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgDOB" /></th>
						<th class="title"><bean:message
							key="oscarEncounter.search.demographicSearch.msgPhone" /></th>
					</tr>
					<%

                        for ( int i = 0; i < searchVec.size(); i++){
                            oscar.oscarEncounter.search.data.EctDemographicData demo = (oscar.oscarEncounter.search.data.EctDemographicData) searchVec.elementAt(i);
                            String forClass;
                            if ( (i % 2) == 0){ forClass="even"; }else{ forClass="odd"; }
                        %>
					<tr class="<%=forClass%>">
						<td><a
							href="../../IncomingEncounter.do?demographicSearch=true&demographicNo=<%=demo.demographicNo%>">
						<%=demo.demographicNo%> </a></td>
						<td><%=demo.lastName%></td>
						<td><%=demo.firstName%></td>
						<td><%=demo.address%></td>
						<td><%=demo.city%></td>
						<td><%=demo.province%></td>
						<td><%=demo.sex%></td>
						<td><%=demo.hin%></td>
						<td><%=demo.yearOfBirth%>/<%=demo.monthOfBirth%>/<%=demo.dayOfBirth%>
						</td>
						<td><%=demo.phone%></td>
					</tr>

					<%}/*for*/%>
				</table>
				<%}else{%>
				<h3 class="noFound"><bean:message
					key="oscarEncounter.search.demographicSearch.msgNoResult" /></h3>
				<%}
                }%>
				</td>
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


