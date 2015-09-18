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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%
String outcome = (String) request.getAttribute("outcome");
%>

<html>
<head>

<title><bean:message key="lab.ca.all.testUploader.labUploadUtility" /></title>

<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">

<script type="text/javascript">
            function selectOther(){                
                if (document.UPLOAD.type.value == "OTHER")
                    document.getElementById('OTHER').style.display = "block";
                else
                    document.getElementById('OTHER').style.display = "none";                
            }
</script>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script> 

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>

<script>
$(function() {
    $( document ).tooltip();
  });
</script>

</head>

<body>

<h3>HL7 Lab Upload</h3>
<div class="well">

    <div class="alert" style="display:none;">
    <button type="button" class="close" data-dismiss="alert">&times;</button>  
    <div id="errorMsg">
		
	</div>
    </div>

<form method='POST' name="UPLOAD" id="uploadForm" enctype="multipart/form-data"	action='${ctx}/lab/CA/ALL/insideLabUpload.do'>
						
<bean:message key="lab.ca.all.testUploader.pleaseSelectTheLabfile" />: <i class="icon-question-sign"></i> <oscar:help keywords="lab" key="app.top1"/> <br />
				
<div style="position:relative;">
<a class='btn' href='javascript:;'>
    Choose File...
    <input type="file" name="importFile" id="importFile" style='position:absolute;z-index:2;top:0;left:0;filter: alpha(opacity=0);-ms-filter:"progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";opacity:0;background-color:transparent;color:transparent;' name="file_source" size="40"  onchange='$("#upload-file-info").html($(this).val());'>
</a>
</span>
     
&nbsp;
<span class='label label-success' id="upload-file-info"></span>

</div>
	<span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../../../images/icon_alertsml.gif"/></span>
  
	<br /><br />
				<bean:message key="lab.ca.all.testUploader.labType" /><br />
				<select name="type" id="type" onchange="selectOther()">
					<option value="0">Select Lab Type:</option>
					<%@ include file="labOptions.jspf"%>
				</select>
			<br />
			<div id="OTHER" style="display: none;">
				<bean:message key="lab.ca.all.testUploader.pleaseSpecifyTheOtherLabType" />:<br />
				<input type="text" name="otherType">
			</div>
			
			<br />
			<button type="submit" class="btn btn-primary"><i class="icon-upload"></i> Upload</button>

</form>
</div>
</body>

<script>
var pageTitle = $(document).attr('title');
$(document).attr('title', 'Administration Panel | <bean:message key="lab.ca.all.testUploader.labUploadUtility" />');

$("#uploadForm").submit(function() {
	
	var lab = $('#importFile').val();
	var ext = lab.substring((lab.length - 3), lab.length);

	var type=$('#type').val();
	var other = $('input[name=otherType]').val();

		
	if (lab==""){
        $('.alert').removeClass('alert-success');
        $('.alert').addClass('alert-error');
        $('.alert').show();
         
        $('#errorMsg').html("<strong>Error!</strong> Please select a lab for upload.");

        
        return false;
        
	}else if(ext != 'hl7' && ext != 'xml'){

	        $('.alert').removeClass('alert-success');
	        $('.alert').addClass('alert-error');
	        $('.alert').show();
	         
	        $('#errorMsg').html("<strong>Error!</strong> The lab must be either a .xml or .hl7 file.");

	       
	        return false;
	}else if(type=="0" || type==""){
	    $('.alert').removeClass('alert-success');
        $('.alert').addClass('alert-error');
        $('.alert').show();
         
        $('#errorMsg').html("<strong>Error!</strong> Please specify a lab type.");
        return false;
	}else if(type=="OTHER" && other==""){

		    $('.alert').removeClass('alert-success');
	        $('.alert').addClass('alert-error');
	        $('.alert').show();
	         
	        $('#errorMsg').html("<strong>Error!</strong> Please specify the <strong>other</strong> lab type.");
	        return false; 
	}
	

});

$( document ).ready(function( $ ) {
<%
if(outcome != null){
	   if(outcome.equals("success")){
	%>
    $('.alert').removeClass('alert-error');
    $('.alert').addClass('alert-success');
	$('#errorMsg').html("Lab uploaded successfully");
	$('.alert').show();
	<%
	    }else if(outcome.equals("uploaded previously")){
	%>
     $('.alert').removeClass('alert-success');
     $('.alert').addClass('alert-error');
	 $('#errorMsg').html("Lab has already been uploaded");
	 $('.alert').show();
	<%    
	    }else if(outcome.equals("exception")){
	%>
	  $('.alert').removeClass('alert-success');
      $('.alert').addClass('alert-error');  	
	  $('#errorMsg').html("Exception uploading the lab");
	  $('.alert').show();
	<%
	    }else{
	%>
	  $('.alert').removeClass('alert-success');
      $('.alert').addClass('alert-error');
	  $('#errorMsg').html("Failed to upload lab");
	  $('.alert').show();
	
	<%
	    }
	}
	%>
	
});

</script>
</html>
