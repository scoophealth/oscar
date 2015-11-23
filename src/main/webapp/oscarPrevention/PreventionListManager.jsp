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
<%@page import="org.oscarehr.managers.PreventionManager" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="java.util.*" %>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_prevention" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_prevention");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<!DOCTYPE html>
<html>
	<head>
		<title><bean:message key="oscarprevention.preventionlistmanager.title" /></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">

		<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet" media="screen">

		<style>
			.table tbody tr:hover td, .table tbody tr:hover th {
			    background-color: #FFFFAA;
			    cursor: pointer; cursor: hand;
			}
			
			.table tbody td.item-active {
			  background-color: #009900 !important;
			}
		</style>
	</head>

<body>

<div class="container">
<h1><bean:message key="oscarprevention.preventionlistmanager.title" /></h1>
<p class="lead">Customize which prevention items to display on the prevention list.</p>
<p style="margin-top:-20px"><span class="label label-info">Info</span> Any changes made here will affect every provider using the prevention module. To add/remove any item from the prevention list simply click on the item below. Green indicates that the item is available from the prevention module.</p>

<table class="table table-striped table-hover table-bordered">

<thead><tr><th></th><th width="200px">Name</th><th>Description</th></tr></thead>

<tbody>
<%
PreventionManager preventionManager = SpringUtils.getBean(PreventionManager.class);

if(request.getParameter("formAction")!=null && request.getParameter("formAction").equals("update")){
	preventionManager.addCustomPreventionItems(request.getParameter("prevention-bin"));
}

LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
ArrayList<HashMap<String,String>> prevList = preventionManager.getPreventionTypeDescList(loggedInInfo);

String customPreventionItems = "";

boolean propertyExists = preventionManager.isHidePrevItemExist();

if(propertyExists){
	customPreventionItems=preventionManager.getCustomPreventionItems();
}

String prevId = null;
String prevName = null;
String prevDesc = null;
for (int e = 0 ; e < prevList.size(); e++){ 
	HashMap<String,String> h = prevList.get(e);
		prevId = h.get("name").replaceAll("\\s+","");
		prevName = h.get("name");
		prevDesc = h.get("desc");             	
%>
			<tr class="prevention-item" id="<%=prevId%>" prevention-data="<%=prevName%>">
				<td class="item-active" title="Available on master list"></td><td><%=prevName%></td><td><%=prevDesc%></td>
			</tr>
			
<%}%>
</tbody>
</table>

<!-- Button to trigger modal confirmation -->
<button id="btnVoid" class="btn btn-large pull-right" disabled>Save</button>
<a href="#modalConfirm" id="btnConfirm" class="btn btn-large btn-success pull-right" data-toggle="modal" data-backdrop="false" style="display:none">Save</a>

</div><!-- container -->


<form action="PreventionListManager.jsp?formAction=update" method="post">     
    <!-- Modal -->
    <div id="modalConfirm" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true">
      <div class="modal-header" style="background-color:#fbb450;">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <h3 id="modalConfirmLabel">Are you sure?</h3>
      </div>
      <div class="modal-body">
        <p>Please confirm your changes to the prevention list:</p>
        <div class="well" id="modalItems"></div>
      </div>
      <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">No, don't save</button>
        <button type="submit" class="btn btn-danger">Yes, please save </button>
      </div>
    </div>
    
    <!-- property value to be saved: hidden-->
	<input type="hidden" name="prevention-bin" id="prevention-bin">
</form>   


<!-- property value from database: hidden-->
<input type="hidden" name="property-bin" id="property-bin" style="width:1200px" value="<%=customPreventionItems%>">

	
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>

<script>
$(".prevention-item").click(function () {
    var id = $(this).attr("id");
    var name = $(this).attr("prevention-data");
    bin = $("#prevention-bin");
    indicatorDisplay(id, name);
});


function indicatorDisplay(id, name){
	indicator = $('#'+id+' td:first-child');

	if(indicator.hasClass("item-active")){
		indicator.removeClass('item-active');
		indicator.attr("title", "Removed from master list");	    
		addPreventionToBin(name);
	    
	}else{
		indicator.addClass('item-active');
		indicator.attr("title", "Available on master list");
		removePreventionFromBin(name);
	}

	btnSaveDisplay();
}

function indicatorAllDisplay(items){
	preventions = items.split(',');
	n = preventions.length;
	if(n>1){
		for(i=0;i<n;i++){
			id = preventions[i].replace(" ", "");
			indicatorDisplay(id, preventions[i]);
		}
	}else{			
		id = items.replace(" ", "");
		indicatorDisplay(id, items);
	}
}

function addPreventionToBin(name){
	bin = $("#prevention-bin");
	if(bin.val()!=""){
		bin.val(bin.val() + "," + name);
	}else{
		bin.val(name);
	}
}

function removePreventionFromBin(itemToBeRemoved){
	bin = $("#prevention-bin");
	if(bin.val()!=""){
	preventions = bin.val().split(',');
	n = preventions.length;
		if(n>1){
			index = preventions.indexOf(itemToBeRemoved);
			
			if(index > -1){
				preventions.splice(index, 1);
				bin.val(preventions);
			}
			
		}else{
			bin.val("");
		}
	}
}

function btnSaveDisplay() {
	bin = $("#prevention-bin");
	prop = $("#property-bin");
	
	if(bin.val()!="" || prop.val()!=""){
		$("#btnVoid").hide();
		$("#btnConfirm").show();
	}else{
		$("#btnConfirm").hide();
		$("#btnVoid").show();
		$('#modalItems').html('');
	}
}

function setModalItems(){
	$('#modalItems').html('');
	bin = $("#prevention-bin");
	if(bin.val()!=""){
	preventions = bin.val().split(',');
	n = preventions.length;
	
	$('#modalItems').html("<h5>Hide the following prevention(s):</h5>");
		if(n>1){
			for(i=0;i<n;i++){
			
				$('#modalItems').append(preventions[i]+"<br>");
			}
		}else{			
			$('#modalItems').append(bin.val()+"<br>");
		}
	}else{
		$('#modalItems').html("Make all preventions available.");
	}
}



$("#btnConfirm").click(function () {
	$('html, body', window.parent.document).animate({scrollTop:230}, 'slow');
	setModalItems();
});

$( document ).ready(function() {	

if($('#property-bin').val()!=""){
	indicatorAllDisplay($('#property-bin').val());
}	

    parent.parent.resizeIframe($('html').height());	
});
</script>		
</body>
</html>
