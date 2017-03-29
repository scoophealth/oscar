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
	import="java.util.*, oscar.oscarEncounter.oscarMeasurements.data.MeasurementMapConfig, oscar.OscarProperties"%>

<%

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
            
            function reloadPage(){
                document.CONFIG.action = 'removeMeasurementMap.jsp';
                return true;
            }
            
            function deleteMapping(id){
                var answer = confirm ("Are you sure you want to delete the mapping?")
                if (answer){
                    document.CONFIG.id.value=id;
                    return true;
                }else{
                    return false;
                }
                
            }
            
            function remap(id, identifier, name, type){
                popupStart(300, 1000, 'remapMeasurementMap.jsp?id='+id+'&identifier='+identifier+'&name='+name+'&type='+type, 'Remap Measurement')
            }
            
            <%String outcome = request.getParameter("outcome");
            if (outcome != null){
                if (outcome.equals("success")){
                    %>
                      alert("Successfully deleted the mapping");
                    <%
                }else{    
                    %>
                      alert("Failed to delete the mapping");
                    <%
                }   
            }%>

        </script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body>
<form method="post" name="CONFIG" action="RemoveMeasurementMap.do">
<input type="hidden" name="id" value=""> <input type="hidden"
	name="identifier" value=""> <input type="hidden" name="name"
	value=""> <input type="hidden" name="type" value=""> <input
	type="hidden" name="provider_no"
	value="<%= session.getValue("user") %>">
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
		<table width="100%">
			<tr>
				<td colspan="6" valign="bottom" class="Header">Measurement
				Mapping Table</td>
			</tr>
			<tr>
				<td class="Cell" colspan="6">
				<%String searchstring = request.getParameter("searchstring");
                                        if (searchstring == null)
                                            searchstring = "";%> Search
				table for name: <input type="text" size="30" name="searchstring"
					value="<%= searchstring %>" /> <input type="submit" value="Search"
					onclick="return reloadPage()" /></td>
			<tr>
			<tr>
				<td><br />
				</td>
			</tr>
			<tr>
				<th width="5%"></th>
				<th width="5%"></th>
				<th class="HeaderCell" width="15%">Identifier</th>
				<th class="HeaderCell" width="15%">Loinc Code</th>
				<th class="HeaderCell" width="45%">Name</th>
				<th class="HeaderCell" width="15%">Lab Type</th>
			</tr>
			<%MeasurementMapConfig mmc = new MeasurementMapConfig();
                                ArrayList mappings = mmc.getMeasurementMap(searchstring);
                                for (int i=0; i < mappings.size(); i++){
                                    Hashtable ht = (Hashtable) mappings.get(i);%>
			<tr>
				<td class="ButtonCell"><input type="submit" value="DELETE"
					onclick="deleteMapping(<%= (String) ht.get("id") %>)"></td>
				<td class="ButtonCell"><input type="button" value="REMAP"
					onclick="remap(<%= "'"+ (String) ht.get("id") +"','"+ (String) ht.get("ident_code") +"','"+ (String) ht.get("name") +"','"+ (String) ht.get("lab_type")+"'" %>)"></td>
				<td class="TableCell"><%= (String) ht.get("ident_code") %></td>
				<td class="TableCell"><%= (String) ht.get("loinc_code") %></td>
				<td class="TableCell"><%= (String) ht.get("name") %></td>
				<td class="TableCell"><%= (String) ht.get("lab_type") %></td>
			</tr>
			<%}%>

		</table>
		</center>
		</td>
	</tr>
</table>
</form>
</body>
</html>
