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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<!DOCTYPE html>
<html>
<head>
<title>Look-Up List Manager</title>
<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" >
	$.fn.bindEvents = function() {

		$(".addLookupListItemButton").unbind("click");
		$(".removeLookupListItem").unbind("click");
		$(".showHideEdit").unbind( "click");
		
		$(".addLookupListItemButton").bind("click", function(){
			var data = new Object();
			data.lookupListId = this.id.split("_")[1];
			data.lookupListItemLabel = $( "#lookupListItemLabel_" + data.lookupListId ).val();
			data.method = "add";
			postData( data, "#lookupListItems_" + data.lookupListId );
		});
	
		$(".removeLookupListItem").bind("click", function(){
			var lookupListId = this.id.split("_")[2];
			var data = new Object();
			data.lookupListItemId = this.id.split("_")[1];
			data.method = "remove";
			postData( data, "#lookupListItems_" + lookupListId );
		});
	
		$(".showHideEdit").bind( "click", function(){		
			var lookupListId = this.id.split("_")[1];
			var showId = "#lookupListItemWrapper_" + lookupListId;
			var cancel = "#cancel_" + lookupListId; 
			var edit = "#edit_" + lookupListId;
	
			$(showId).toggle();
			$(cancel).toggle();
			$(edit).toggle();		
		});
	}
	
	function postData( data, target ) {
		$.ajax({
			method : "POST",
			url : "${ pageContext.request.contextPath }/lookupListManagerAction.do",
			data : data,
			success: function(data) {
				$(".lookupListItemLabel").val("");
				$(target).replaceWith( $(target, data) );
				$().bindEvents();
			}
		});
	}
	
	$(document).ready(function(){	
		$().bindEvents();	
		$(".lookupListItemWrapper").hide();
	});

</script>
<style type="text/css">
html {
	font-size: small;
	font-family: Verdana, Tahoma, Arial, sans-serif;
}

body {
	margin:0px;
	padding:0px;
	top:0;
	left:0;
}

header {
	background-color: black;
	margin-bottom:10px !important;
	padding:5px;
}

header h1 {
	color:white;
	margin:0px;
	padding:0px;
}

h3 {
	margin:0px;
	padding:0px;
}

	div#lookUpListWrapper {
		margin: auto 20px;
	}

div.lookupListItemsWrapper {

	border:red thin solid;
	padding:10px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	background-color: #eaeaea;
	background-image: -webkit-gradient(linear, left top, left bottom, from(#ffffff), to(#eaeaea));
	background-image: -webkit-linear-gradient(top, #ffffff, #eaeaea);
	background-image: -moz-linear-gradient(top, #ffffff, #eaeaea);
	background-image: -ms-linear-gradient(top, #ffffff, #eaeaea);
	background-image: -o-linear-gradient(top, #ffffff, #eaeaea);
	background-image: linear-gradient(top, #ffffff, #eaeaea);
	border: 1px solid #ccc;
	border-bottom-color: #aaa;
	border-radius: 4px;
	clear: both;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#ffffff, endColorstr=#eaeaea);
	margin-top: 15px;
	padding: 10px ;
	
}

div.lookupListTitle {
	padding:5px;
}

ul {
	list-style: none;
	list-style-type: none;
	list-style-position: outside;
	padding-left: 1px;
	margin-left: 1px;
	margin-top: 0px;
	padding-top: 1px;
	margin-bottom: 0px;
	padding-bottom: 0px;
}
	
	li.lookupListItem {
		list-style-type: none;
		padding: 5px;
		margin: 1px;
		background-color: white;
		border: 2px double #CCCCCC;
	}

	
	li.lookupListItem a {
		display: inline-block;
		text-decoration: none;
		color: red;
		font-weight: bold;
		float: right;
		border: red thin solid;
		padding: 0 2px;
	}
	
	li.lookupListItem a:hover {
		border-color: #555;
		color: #555;
	}

div.addLookupListItemTools {
	padding:5px;
	float:left;
	width:100%;
}
	
	div.addLookupListItemTools div.addInput {
	   width:89%;
		float:left;
		overflow: hidden;
		border:#555 thin solid;
		border-radius: 3px;
	}

	div.addLookupListItemTools div.addInputButton {
	   width:10%;
		padding:0xp;
		vertical-align: center;
		text-align: left;
		float:left;
	}
	
	div.addLookupListItemTools input[type="text"] {
		width: 100%;
	  border:none;
	}

.clearfix {
	clear:both;
}

footer {
	height:25px;
	width:100%;
}

</style>
</head>
<body id="lookUpListManager">
<header>
	<h1 class="pageTitle">
		<bean:message key="admin.admin.lookuplists.title" />
	</h1>
</header>

<security:oscarSec roleName="${ sessionScope.userrole },${ sessionScope.user }" 
	objectName="_admin.*" rights="w" reverse="${ true }">
	
	<div id="lookUpListWrapper">
		<c:import url="./manageLookUpLists.jsp" />	
	</div>	
</security:oscarSec>

<footer></footer>

</body>
</html>
