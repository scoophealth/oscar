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

<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
<link rel="stylesheet" type="text/css" href="dxResearch.css">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>

<title><bean:message
	key="oscarResearch.oscarDxResearch.dxCustomization.selectAssociations" />
</title>

<script type="text/javascript">

function setfocus(){
    window.focus();
    window.resizeTo(800,600);
}

function populateListOfAssociations() {	
	$("#associations tr").remove();

	$('#associations').append('<tr><th colspan="3">Issue List</th><th colspan="3">Disease Registry</th></tr><tr><th>CodeType</th><th>Code</th><th>Description</th><th>CodeType</th><th>Code</th><th>Description</th></tr>');		
	$.getJSON("<%= request.getContextPath() %>/oscarResearch/oscarDxResearch/dxResearchLoadAssociations.do?method=getAllAssociations",
	        function(data,textStatus){
	          for(var x=0;x<data.length;x++) {
		       	$('#associations tr:last').after('<tr><td>'+data[x].codeType+'</td><td>'+data[x].code+'</td><td>'+data[x].description+'</td><td>'+data[x].dxCodeType+'</td><td>'+data[x].dxCode+'</td><td>'+data[x].dxDescription+'</td></tr>');		       	  
	          }
	        });
}

$(document).ready(function() {

	//clear list
	$("#clear_list").click(function(){
		if(confirm('Are you sure you want to delete all associations?')) {
			//$.get("<%= request.getContextPath() %>/oscarResearch/oscarDxResearch/dxResearchLoadAssociations.do?method=clearAssociations");
			$.ajax({
				type: "GET",
				url: "<%= request.getContextPath() %>/oscarResearch/oscarDxResearch/dxResearchLoadAssociations.do?method=clearAssociations",
				async: false,
				success: function() {
					populateListOfAssociations();		
				}
			});
						
		}
	});

	//export
	$("#export").click(function(){
		window.open("<%= request.getContextPath() %>/oscarResearch/oscarDxResearch/dxResearchLoadAssociations.do?method=export");
		
	});

	//automatch
	$("#automatch").click(function(){
		if(confirm('This function will remove and re-generate all entries in the disease registry where the entry was created by an association.\nWould you like to continue?')) {
			$.getJSON("<%= request.getContextPath() %>/oscarResearch/oscarDxResearch/dxResearchLoadAssociations.do?method=autoPopulateAssociations",
			        function(data,textStatus){
			          alert("Automatch generated " + data.recordsAdded + " records.");
			        });
		}		
	});
	
	//populate the current list of associations
	populateListOfAssociations();
});
</script>

	<script>
	$(function() {
	    $( document ).tooltip();
	  });
	</script>
	
</head>
<link rel="stylesheet" type="text/css" href="dxResearch.css">
<body class="BodyStyle" vlink="#0000FF" rightmargin="0" leftmargin="0"
	topmargin="0" marginwidth="0" marginheight="0" onload="setfocus()" bgcolor="#EEEEFF">
<!--  -->
<table width="100%" bgcolor="#EEEEFF">
	<tr bgcolor="#000000">
		<td class="subject" colspan="2">&nbsp;&nbsp;&nbsp;<bean:message
			key="oscarResearch.oscarDxResearch.dxResearch.msgDxResearch" /></td>
	</tr>
	<tr>
		<td class=heading colspan="2">Customize Associations List</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
<div id="associationsPane">
	<table id="associations"  width="100%" border="1" cellpadding="0" cellspacing="0">
	</table>
	<br/>
	<div id="upload_form">
	<h4>Upload CSV file:</h4>
	<html:form action="/oscarResearch/oscarDxResearch/dxResearchLoadAssociations.do?method=uploadFile" method="post" enctype="multipart/form-data">
		<html:file property="file"></html:file>
		<span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../../images/icon_alertsml.gif"/></span></span>
        
		<br/>
		<html:radio property="replace" value="true"/>Replace&nbsp;
		<html:radio property="replace" value="false"/>Append
		<br/>
		<html:submit/>
	</html:form>
	</div>
	<br/>
	<input id="automatch" type="button" class="mbttn"
			style="width: 180px" value="Automatch" />
	&nbsp;&nbsp;	
	<input id="clear_list" type="button" class="mbttn"
			style="width: 180px" value="Clear Associations" />
	&nbsp;&nbsp;		
	<input id="export" type="button" class="mbttn"
			style="width: 180px" value="Export" />
	&nbsp;&nbsp;	
	<input id="close" type="button" class="mbttn"
			style="width: 180px" value="Close" onclick="javascript:window.close();"/>
	<br/>
</div>
</tr>
</table>

</body>
</html:html>
