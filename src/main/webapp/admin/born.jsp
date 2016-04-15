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
	objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin");%>
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
<title>BORN Mappings</title>
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
function addNewMapping() {
	
	jQuery.getJSON("bornManage.json",{method: "listServices"},
            function(data,textStatus){
				console.log('ok');
				$("#oscarService").empty();
				for(var x=0;x<data.length;x++) {
					var id = data[x].serviceId;
					var name = data[x].serviceDesc;
					
					$("#oscarService").append("<option value=\""+ id + "\">"+name+"</option>");
					
					$('#new-form').dialog('open');
				}
   			 }
	);
}

function deleteMapping(id) {
    jQuery.getJSON("bornManage.json", {
        method: "delete",
        id: id
    },
    function(xml) {
    	if(xml.success)
    		listMappings();
    	else
    		alert(xml.error);
    });
}

function listMappings() {
	 $("#mappingTable tbody").find("tr").remove();
	 
	jQuery.getJSON("bornManage.json",{method: "list"},
           function(data,textStatus){
				for(var x=0;x<data.length;x++) {
					var id = data[x].id;
					var bornPathway = data[x].bornPathway;
					var serviceId = data[x].serviceName;
					
					$('#mappingTable > tbody:last').append('<tr><td>'+bornPathway+'</td><td>'+serviceId+'</td><td><a href="javascript:void(0);" onclick="deleteMapping('+id+');"><img border="0" title="delete" src="../images/Delete16.gif"/></a></td></tr>');
				}
   });
}

$(document).ready(function(){
	listMappings();
	
	$( "#new-form" ).dialog({
		autoOpen: false,
		height: 275,
		width: 450,
		modal: true,
		buttons: {
			"Add Mapping": function() {
				var bm = $("#bornPathway").val();
				var os = $("#oscarService").val();
				 jQuery.getJSON("bornManage.json",
			                {
			                        method: "add",
			                        bornPathway: bm,
			                        oscarService:os,
			                },
			                function(xml){
			                	if(xml.success)
			                		listMappings();
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
<h4>BORN/OSCAR Consultation Service Mapping</h4>
<table id="mappingTable" name="mappingTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th>Early Child Development and Parenting Resource System - Ontario</th>
			<th>OSCAR Consultation Service</th>
		</tr>
	</thead>
	<tbody></tbody>
</table>
<input type="button" class="btn btn-primary" value="Add New" onClick="addNewMapping()"/>	


<div id="new-form" title="Create Mapping">
	<p class="validateTips"></p>
	<form>
		<fieldset>
			<div class="control-group">
				<label class="control-label" for="bornPathway">BORN Pathway:</label>
				<div class="controls">
					<select name="bornPathway" id="bornPathway">
						<option value="Autism Intervention Services" >Autism Intervention Services</option>
						<option value="Blind Low Vision Program">Blind Low Vision Program</option>
						<option value="Child Care">Child Care</option>
						<option value="Child Protection Services">Child Protection Services</option>
						<option value="Children's Mental Health Services">Children's Mental Health Services</option>
						<option value="Children's Treatment Centre">Children's Treatment Centre</option>
						<option value="Community Care Access Centre">Community Care Access Centre</option>
						<option value="Community Parks and Recreation Programs">Community Parks and Recreation Programs</option>
						<option value="Dental Services">Dental Services</option>
						<option value="Family Resource Programs">Family Resource Programs</option>
						<option value="Healthy Babies Healthy Children">Healthy Babies Healthy Children</option>
						<option value="Infant Development Program">Infant Development Program</option>
						<option value="Infant Hearing Program">Infant Hearing Program</option>
						<option value="Ontario Early Years Centre">Ontario Early Years Centre</option>
						<option value="Paediatrician/Developmental Paediatrician">Paediatrician/Developmental Paediatrician</option>
						<option value="Preschool Speech and Language Program">Preschool Speech and Language Program</option>
						<option value="Public Health">Public Health</option>
						<option value="Schools">Schools</option>
						<option value="Services for Physical and Developmental Disabilities">Services for Physical and Developmental Disabilities</option>
						<option value="Services for the Hearing Impaired">Services for the Hearing Impaired</option>
						<option value="Services for the Visually Impaired">Services for the Visually Impaired</option>
						<option value="Specialized Child Care Programming">Specialized Child Care Programming</option>
						<option value="Specialized Medical Services">Specialized Medical Services</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="oscarService">OSCAR Consultation Service:</label>
				<div class="controls">
					<select name="oscarService" id="oscarService">
						
					</select>
				</div>
			</div>
		</fieldset>
	</form>
</div>

</body>
</html:html>
