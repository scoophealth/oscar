<%--

    Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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

<%@ page errorPage="../error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.oscar-emr.com/tags/integration" prefix="i"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.billing&type=_billing");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.io.*,java.util.*, java.sql.*, oscar.*, java.net.*, org.oscarehr.integration.mcedt.web.ActionUtils" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload</title>
<script src="../../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" >
$(window).load(function(){
	$('input[type="checkbox"]').click(function () {
   	 var pass = 5; //5 files at a time
   	 var numOfFiles = $('input[type="checkbox"]:checked').length;
   	 if (numOfFiles == pass) {
        $('input[type="checkbox"]').not(':checked').prop('disabled', true);
   	 } else {
        $('input[type="checkbox"]').not(':checked').prop('disabled', false);
   	 }
   	 if (numOfFiles>0) {
  	  	$("#unSelUpload").prop('disabled', false);
  	  	$("#submitUpload").prop('disabled', false);
   	 	$("#deleteUpload").prop('disabled', false);
    	} else {
   	 	$("#unSelUpload").prop('disabled', true);
   	 	$("#submitUpload").prop('disabled', true);
   	 	$("#deleteUpload").prop('disabled', true);

  	  }
   	 
   	if (numOfFiles>0) {
    	} else {
  	  }
	})
	
	$("#unSelUpload").click(function(){
		$('input[type="checkbox"]').filter(':checked').prop('checked', false);
		$('input[type="checkbox"]').filter(':disabled').prop('disabled', false);
   	 	$("#unSelUpload").prop('disabled', true);
		$("#submitUpload").prop('disabled', true);
	 	$("#deleteUpload").prop('disabled', true);
	})
});

function setForm(control,action) {
	if (action=='deleteUpload') {
		if (!confirm("Please confirm that you would like to delete the selected files")) {
			return false;
		}
		ShowSpin(true);
	}
	
	
	var files = document.getElementsByClassName("fileNames");
	var types = document.getElementsByClassName("fileTypes");
	var filenames="";
	var resourceTypes="";
	for (var i = 0; i < files.length; i++) {
		if (files[i].checked) {
			filenames += (files[i].value+",");
			resourceTypes += (types[i].value+",");
		}
		
	}
	document.getElementById("fileName").value = filenames.replace(/,\s*$/, "");
	document.getElementById("resourceType").value = resourceTypes.replace(/,\s*$/, "");
	return submitFormUpload(action, control);
}

function submitFormUpload(methodType, control){
	if (control) {
		control.disabled = true;
	}
	
	var method = jQuery("#methodUpload");
	method.val(methodType);
	
	var form = jQuery("#formUpload");
	form.submit();
	return true;
}

function add(control) {
	if (control) {
		control.disabled = true;
	}
	window.location.href = '<%= request.getContextPath() %>/mcedt/web/addUpload.jsp';
	return false;
}

</script>
</head>
<body>
<html:form action="/mcedt/upload.do" method="post" styleId="formUpload">
			<jsp:include page="../messages.jsp" />

			<input id="methodUpload" name="method" type="hidden" value="" />
			<html:hidden styleId="description" property="description" value="" />
			<html:hidden styleId="fileName" property="fileName" value='' />					
			<html:hidden property="resourceType" styleId="resourceType" value="" />				
		</html:form>
	<div>
	* You may select a maximum of 5 files at a time to upload to MC-EDT
	<table width="100%" border="0" cellspacing="0" cellpadding="5" style="margin:5px 0 15px;" class="whiteBox">
		<tr class="greenBox">
			<td width="7%" >Select</td>
			<td>File Name</td>
			<td>Type</td>
			<td>Created on</td>
		</tr>
		<%List<File> toEdt = ActionUtils.getUploadList();
		if (toEdt != null) {
			int i=0;
			for (File file : toEdt) {
				i++;
			        %> 
		<tr>
			<td width="7%"><input type="checkbox" class="fileNames" name="choice" id="<%=i %>" value="<%=file.getName() %>"> </input></td>
			<td><%=file.getName() %></td>
			<td><select class="fileTypes">
					<option value="CL" <%= (file.getName().startsWith("H")? "selected":"") %> >Claims</option>
					<option value="OB" <%= (file.getName().startsWith("OBEC")?"selected":"") %> >OBEC</option>
				</select> </td>
			<td><%=new java.util.Date(file.lastModified()) %></td>		
		</tr><% }	     
	    } else { %>
		<tr>
			<td width="7%"> </input></td>
			<td>No Files are available</td>			
		</tr>
		<%} %>		
	</table>
	<table>
		<tr>
			<td>
				<button type="button" id="unSelUpload" class="noBorder blackBox flatLink font12 rightMargin5" disabled="true">Un-Select All</button> 
				<button type="button" id="submitUpload" class="noBorder blackBox flatLink font12 rightMargin5" disabled="true" onclick="ShowSpin(true);return setForm(this,'uploadSubmitToMcedt');">Submit</button>
				<button type="button" id="deleteUpload" class="noBorder blackBox flatLink font12 rightMargin5" disabled="true" onclick="return setForm(this,'deleteUpload');">Delete Selected</button>
				<button type="button" class="noBorder blackBox flatLink font12" onclick="add(this);">Add</button>
			</td>
			<td></td>			
		</tr>
	</table>
	</div>
</body>
</html>
