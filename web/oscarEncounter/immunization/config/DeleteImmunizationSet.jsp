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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
oscar.encounter.immunization.config.data.EctImmImmunizationSetData immuSets = new oscar.encounter.immunization.config.data.EctImmImmunizationSetData();
immuSets.estImmunizationVecs();

%>

<html:html locale="true">


<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Administrate Immunization Sets</title>
<html:base />

</head>
<script language="javascript">
function BackToOscar()
{
       window.close();
}


</script>


<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onload="window.focus();">
<html:errors />
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<tr>
		<td width="100%"
			style="padding-left: 3; padding-right: 3; padding-top: 2; padding-bottom: 2"
			height="0%" colspan="2">
		<p class="HelpAboutLogout"><span class="FakeLink"><a
			href="Help.htm">Help</a></span> | <span class="FakeLink"><a
			href="About.htm">About</a></span> | <span class="FakeLink"> <a
			href="Disclaimer.htm">Disclaimer</a></span></p>
		</td>
	</tr>
	<tr>
		<td width="10%" height="37" bgcolor="#000000">&nbsp;</td>
		<td width="100%" bgcolor="#000000"
			style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%">
		<p class="ScreenTitle">Administrate Immunization Sets</p>
		</td>
	</tr>
	<tr>
		<td></td>
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle">blah</div>
				</td>
			</tr>
			<tr>
				<td>
				<table>
					<tr>
						<td><html:link
							page="/oscarEncounter/immunization/config/AdministrateImmunizationSets.jsp">
                                        Administrate Immunization Sets
                                    </html:link></td>
						<td><html:link
							page="/oscarEncounter/immunization/config/CreateImmunizationSetInit.jsp">
                                        New Immunization Set
                                    </html:link></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>Current Immunization Sets<br>
				Please select which immunizations you would like to delete</td>
			</tr>
			<tr>
				<td><html:form action="/oscarEncounter/DeleteImmunizationSets">
					<table border=1>
						<tr>
							<th>&nbsp;</th>
							<th>Immunization Set Name</th>
							<th>Date Created</th>
						</tr>
						<%for ( int i = 0; i < immuSets.setNameVec.size();i++){
                                    String name = (String) immuSets.setNameVec.elementAt(i);
                                    String id = (String) immuSets.setIdVec.elementAt(i);
                                    String createDate = (String) immuSets.createDateVec.elementAt(i);
                                %>
						<tr>
							<td><input type="checkbox" name="immuSets" value="<%=id%>" />
							</td>
							<td><a
								href="javascript:popupImmunizationSet(768,1024,'./oscarEncounter/ImmunizationSetDisplay.do?setId=<%=id%>')">
							<%=name%> </a></td>
							<td><%=createDate%></td>
						</tr>
						<%}%>
						<tr>
							<td colspan="3"><input type="submit" value="Delete Sets" />
							</td>
						</tr>
					</table>
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
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC"
			colspan="2"></td>
	</tr>
</table>
</body>
</html:html>


