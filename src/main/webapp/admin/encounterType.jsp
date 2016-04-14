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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.encounter" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin&type=_admin.encounter");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Customize Encounter Types</title>
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script>   
<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

<script>
function addNew() {
	$("#newEncounterType").val('');
	$('#new-form').dialog('open');
}

function deleteIt(id) {
    jQuery.getJSON("encounterTypeManage.json", {
        method: "delete",
        id: id
    },
    function(xml) {
    	if(xml.success)
    		list();
    	else
    		alert(xml.error);
    });
}

function list() {
	 $("#mappingTable tbody").find("tr").remove();
	 
	jQuery.getJSON("encounterTypeManage.json",{method: "list"},
           function(data,textStatus){
				for(var x=0;x<data.length;x++) {
					var id = data[x].id;
					var name = data[x].value;
					
					$('#mappingTable > tbody:last').append('<tr><td>'+name+'</td><td><a href="javascript:void(0);" onclick="deleteIt('+id+');"><img border="0" title="delete" src="../images/Delete16.gif"/></a></td></tr>');
				}
   });
}

$(document).ready(function(){
	list();
	
	$( "#new-form" ).dialog({
		autoOpen: false,
		height: 275,
		width: 450,
		modal: true,
		buttons: {
			"Add": function() {
				var bm = $("#newEncounterType").val();
				jQuery.getJSON("encounterTypeManage.json",
			                {
			                        method: "add",
			                        name: bm,
			                },
			                function(xml){
			                	if(xml.success)
			                		list();
			                	else
			                		alert(xml.error);
			                });
				 
				$( this ).dialog( "close" );
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			
		}
	});
	
});

</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>Customize Encounter Types</h4>
<table id="mappingTable" name="mappingTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th>Name</th>
			<th></th>
		</tr>
	</thead>
	<tbody></tbody>
</table>
<input type="button" class="btn btn-primary" value="Add" onClick="addNew()"/>	


<div id="new-form" title="Create Encounter Type">
	<p class="validateTips"></p>
	<form>
		<fieldset>
			<div class="control-group">
				<label class="control-label" for="newEncounterType">Encounter Type:</label>
				<div class="controls">
					<input type="text" name="newEncounterType" id="newEncounterType" value=""/>
				</div>
			</div>
		</fieldset>
	</form>
</div>

</body>
</html:html>
