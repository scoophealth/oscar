<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.js"></script>
<title>Migrate CAISI Roles to Oscar Roles</title>

<script type="text/javascript">
$("document").ready(function(){
	$("#migrate_step1").click(function(){
		$.getJSON("<%= request.getContextPath() %>/PMmodule/Admin/MigrateCaisiRoles.do?method=preview",
		        function(data,textStatus){
		          for(var x=0;x<data.messages.length;x++) {
					$("#messages").append("<span>" + data.messages[x] + "</span><br/>");
		          }
		          if(data.status == 'ok') {
			          $("#messages").append("<input id=\"migrate\" type=\"button\" class=\"mbttn\" style=\"width: 180px\" value=\"Migrate\" />");
			          $("#migrate_step1").hide();
		          }		         
		        });
	});

	$("#migrate").live("click",function(){		
		$("#messages").html("<span>Please wait.</span>");	
		$.getJSON("<%= request.getContextPath() %>/PMmodule/Admin/MigrateCaisiRoles.do?method=migrate",
		        function(data,textStatus){	        	 
		          for(var x=0;x<data.messages.length;x++) {
					$("#messages").append("<span>" + data.messages[x] + "</span><br/>");
		          }		          	         
		        });
	});
});
</script>

</head>
<body class="BodyStyle" vlink="#0000FF" rightmargin="0" leftmargin="0"
	topmargin="0" marginwidth="0" marginheight="0" bgcolor="#EEEEFF">
<!--  -->
<table width="100%" bgcolor="#EEEEFF">
	<tr bgcolor="#000000">
		<td class="subject" colspan="2">&nbsp;&nbsp;&nbsp;Data Migration</td>
	</tr>
	<tr>
		<td class=heading colspan="2">Migrate CAISI Roles to OSCAR Roles</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
<div id="associationsPane">
	<table id="associations"  width="100%" border="1" cellpadding="0" cellspacing="0">
	</table>
	<br/>
	<div id="messages">
	</div>
	</br>
	<form method="post">
	<input id="migrate_step1" type="button" class="mbttn"
			style="width: 180px" value="Preview" />
	&nbsp;&nbsp;	
	</form>
</div>
</tr>
</table>

</body>
</html:html>
