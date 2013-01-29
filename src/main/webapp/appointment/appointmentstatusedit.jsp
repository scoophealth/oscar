<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.userAdmin,_admin.schedule" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.appt.status.mgr.title" /></title>
<link href="../css/jquery.ui.colorPicker.css" rel="stylesheet" type="text/css" />
<script src="../js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="../js/jquery.ui.colorPicker.min.js" type="text/javascript"></script>
<oscar:customInterface section="apptStatusEdit"/>
</head>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<body>
<script type="text/javascript">
	$(document).ready(function(){
	    $('#apptColor').colorPicker({
	    	format:'hex',
	        colorChange: function(e, ui) {
	        	$('#apptColor').val(ui.color);
	        }
	      });
	    
	   $('#colorpicker').colorPicker('setColor',$('#old_color').val());

	});
</script>

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER" NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.appt.status.mgr.title" /></font></th>
	</tr>
</table>


<html:form action="/appointment/apptStatusSetting">
	<table>
		<tr>
			<td class="tdLabel"><bean:message
				key="admin.appt.status.mgr.label.status" />:</td>
			<td><html:text readonly="true" property="apptStatus" size="40" /></td>
		</tr>
		<tr>
			<td class="tdLabel"><bean:message
				key="admin.appt.status.mgr.label.desc" />:</td>
			<td><html:text property="apptDesc" size="40" /></td>
		</tr>
		<tr>
			<td class="tdLabel"><bean:message
				key="admin.appt.status.mgr.label.oldcolor" />:</td>
			<td><html:text readonly="true" styleId="old_color" property="apptOldColor" size="40" />
			</td>
		</tr>
		<tr>
			<td class="tdLabel"><bean:message
				key="admin.appt.status.mgr.label.newcolor" />:</td>
			<td>
				<input id="apptColor" name="apptColor" value="" size="20" />
				
			</td>
		</tr>

		<div id="list_entries"></div>
		<tr>
			<td colspan="2"><html:hidden property="ID" /> <input
				type="hidden" name="dispatch" value="update" /> <br />
			<input type="submit"
				value="<bean:message key="oscar.appt.status.mgr.label.submit"/>" />
			</td>
		</tr>
	</table>
</html:form>
</body>
</html>
