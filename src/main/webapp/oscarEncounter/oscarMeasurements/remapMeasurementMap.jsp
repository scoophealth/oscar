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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.measurements" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin.measurements");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.*, oscar.oscarEncounter.oscarMeasurements.data.MeasurementMapConfig, oscar.OscarProperties, oscar.util.StringUtils"%>

<%

MeasurementMapConfig mmc = new MeasurementMapConfig();
String id = request.getParameter("id");
String identifier = request.getParameter("identifier");
String name = request.getParameter("name");
String type = request.getParameter("type");

if (identifier == null) identifier = "";
if (name == null) name = "";
if (type == null) type = "";

%>

<link rel="stylesheet" type="text/css"
	href="../../oscarMDS/encounterStyles.css">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Measurement Mapping Configuration</title>

<script type="text/javascript" language=javascript>
            
            function popupStart(vheight,vwidth,varpage,windowname) {
                var page = varpage;
                windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
                var popup=window.open(varpage, windowname, windowprops);
            }
            
            function newWindow(varpage, windowname){
                var page = varpage;
                windowprops = "fullscreen=yes,toolbar=yes,directories=no,resizable=yes,dependent=yes,scrollbars=yes,location=yes,status=yes,menubar=yes";
                var popup=window.open(varpage, windowname, windowprops);
            }
            
            function reloadPage(){
                document.CONFIG.action = 'remapMeasurementMap.jsp';
                return true;
            }
            
            
            <%String outcome = request.getParameter("outcome");
            if (outcome != null){
                if (outcome.equals("success")){
                    %>
                      alert("Successfully remapped the measurement");
                      window.opener.location.reload()
                      window.close();
                    <%
                }else if (outcome.equals("failedcheck")){
                    %>
                      alert("Unable to remap the measurement: A message is already mapped to the specified code for that message type");
                    <%
                }else{    
                    %>
                      alert("Failed to remap the measurement");
                    <%
                }   
            }%>

        </script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body>
<form method="post" name="CONFIG" action="RemapMeasurementMap.do">
<input type="hidden" name="id" value="<%= id %>"> <input
	type="hidden" name="identifier" value="<%= identifier %>"> <input
	type="hidden" name="name" value="<%= name %>"> <input
	type="hidden" name="type" value="<%= type %>">
<table width="100%" height="100%" border="0">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRow" colspan="9" align="left">
		<table width="100%">
			<tr>
				<td align="left"><input type="button"
					value=" <bean:message key="global.btnClose"/> "
					onClick="window.close()"></td>
				<td align="right"><oscar:help keywords="measurement" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'../About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'../License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td valign="middle">
		<center>
		<table width="80%">
			<tr>
				<td colspan="2" valign="bottom" class="Header">Remap Identifier
				Code</td>
			</tr>
			<tr>
				<td class="Cell" width="20%">Identifier:</td>
				<td class="Cell" width="80%"><%= identifier %></td>
			</tr>
			<tr>
				<td class="Cell" width="20%">Name:</td>
				<td class="Cell" width="80%"><%= name %></td>
			</tr>
			<tr>
				<td class="Cell" width="20%">Lab Type:</td>
				<td class="Cell" width="80%"><%= type %></td>
			</tr>
			<tr>
				<td class="Cell" width="20%">Search codes by name:</td>
				<td class="Cell" width="80%">
				<%String searchstring = request.getParameter("searchstring");
                                                if (searchstring == null)
                                                    searchstring = "";%>
				<input type="text" size="30" name="searchstring"
					value="<%= searchstring %>" /> <input type="submit" value="Search"
					onclick="return reloadPage()" /></td>
			<tr>
				<td class="Cell" width="20%">Select code to map to:</td>
				<td class="Cell" width="80%"><select name="loinc_code">
					<option value="0">None Selected</option>
					<%ArrayList loincCodes = mmc.getLoincCodes(searchstring);
                                                for (int i=0; i < loincCodes.size(); i++) { 
                                                Hashtable ht = (Hashtable) loincCodes.get(i);%>
					<option value="<%= (String) ht.get("code") %>"><%= (String) ht.get("code")+" - "+((String) ht.get("name")).trim()%></option>
					<% }%>
				</select></td>
			</tr>
			<tr>
				<td colspan="2" class="Cell" align="center"><input
					type="submit" value=" Remap Measurement "> <input
					type="button" value=" Add New Loinc Code "
					onclick="javascript:popupStart('300','600','newMeasurementMap.jsp','Add New Loinc Code')">
				</td>
			</tr>
			<tr>
				<td colspan="2" class="Cell" align="center">NOTE: <a
					href="javascript:newWindow('http://www.regenstrief.org/medinformatics/loinc/relma','RELMA')">It
				is suggested that you use the RELMA application to help determine
				correct loinc codes.</a></td>
			</tr>
		</table>
		</center>
		</td>
	</tr>
</table>
</form>
</body>
</html>
