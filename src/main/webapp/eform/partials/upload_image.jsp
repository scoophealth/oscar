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
<!DOCTYPE html>
<%@ page import="oscar.eform.data.*, oscar.OscarProperties, oscar.eform.*, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
String status = (String) request.getAttribute("status");    		
%>
<html:html locale="true">

<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>

<script>
$(function() {
    $( document ).tooltip();
  });
</script>
</head>

<style>
.message div{
font-size: 12px;
display:inline; 
}

h3{
margin:0px; 
display:inline; 
font-weight:normal;
font-size: 12px;
}

.message ul{
margin:0px;
padding:0px;
display:inline;
}

.message li{
list-style: none; 
margin:0px;
display:inline;  
padding-left:6px;
}

</style>
<body>

<%if(status != null){ %>
    <div class="alert alert-success">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <strong>Success!</strong> Your image was uploaded.
    </div>

<script>
window.top.location.href = "<%=request.getContextPath()%>/administration/?show=ImageUpload";
</script>

<%}else{%>

		
		<html:form action="/eform/imageUpload" enctype="multipart/form-data" method="post">
		<bean:message key="eform.uploadimages.msgFileName" /> 
		<span class="text-error message"><html:errors /></span>
		<br>
		<input type="file" name="image" id="image" class="check" size="40"  required>
		<span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../../images/icon_alertsml.gif"/></span></span>
        
		<input type="submit" class="btn upload" name="subm" value="<bean:message key="eform.uploadimages.btnUpload"/>" disabled>

		</html:form>
<%}%>
<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>  

<script>
$( document ).ready(function() {
$(".check").change(validate).keyup(validate);
});

function validate()
{

var id = $(this).attr("id");
var formHtml = $("#image").val();
var file = $('#image')[0].files[0];
//var filename = file.name;
//var fileSize = file.size;

//var inputCheck=checkRow(filename);

if (formHtml!="") {
	    $('.upload').removeAttr("disabled");
		$('.upload').addClass("btn-success");
    }else{
	    $('.upload').attr("disabled", "disabled");
		$('.upload').removeClass("btn-success");
    } 
}

</script>
</body>
</html:html>