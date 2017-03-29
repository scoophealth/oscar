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

<%--@ page errorPage="../error.jsp"--%>

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

<%@ page import="java.io.*,java.util.*, java.sql.*, oscar.*, java.net.*, org.oscarehr.integration.mcedt.web.ActionUtils, java.math.BigInteger,ca.ontario.health.edt.ResponseResult" %>
<% 

List<File> toEdt = ActionUtils.getUploadList();
BigInteger resourceId = (BigInteger) ((session.getAttribute("uploadResourceId") != null )? session.getAttribute("uploadResourceId"):new BigInteger("0"));
String fileUpload = (String) ((session.getAttribute("uploadFileName") != null )? session.getAttribute("uploadFileName"):"");
List<ResponseResult> uploadResults = (List<ResponseResult>) session.getAttribute("uploadResponseResult");
List<ResponseResult> submitResults = (List<ResponseResult>) session.getAttribute("submitResponseResult");
String connection = ((request.getParameter("connection")==null ||request.getParameter("connection").equals(""))?"":request.getParameter("connection"));
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Uploading Claims from Oscar to MCEDT</title>
	<script src="../js/jquery-1.7.1.min.js"></script>
	<link href="web/css/kai_mcedt.css" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700,900" rel="stylesheet" type="text/css">

	<script type="text/javascript" >

							
		var responseUploadFileNames= [];
		var responseUploadMsg= [];
		var responseUploadCode= [];
		var responseSubmitFileNames= [];
		var responseSubmitMsg= [];
		var responseSubmitCode= [];
		
		<% if ( uploadResults!= null) {
			for (int i = 0; i<uploadResults.size();i++) {
				%>
				responseUploadFileNames.push('<%=uploadResults.get(i).getDescription()%>');
				responseUploadMsg.push('<%=uploadResults.get(i).getResult().getMsg()%>');
				responseUploadCode.push('<%=uploadResults.get(i).getResult().getCode()%>');
				<%
			}
		}
		
		%>
		<% if ( submitResults!= null) {
			for (int i = 0; i<submitResults.size();i++) {
				%>
				responseSubmitFileNames.push('<%=submitResults.get(i).getDescription()%>');
				responseSubmitMsg.push('<%=submitResults.get(i).getResult().getMsg()%>');
				responseSubmitCode.push('<%=submitResults.get(i).getResult().getCode()%>');
				<%
			}
		}
		
		%>
		function setForm() {
			var files = document.getElementsByClassName("fileNames");
			var progress = document.getElementsByClassName("progress");
			var status = document.getElementsByClassName("status");
			var connection = '<%=connection %>';
			if (connection!="failed") {
				for (var i = 0; i < files.length; i++) {
					// Step 1 set all the available statuses
					for (var j = 0; j<responseUploadFileNames.length; j++) {
						if (files[i].value==responseUploadFileNames[j]) {
							setStatus(progress[i], status[i], responseUploadMsg[j], responseUploadCode[j], "Upload");
						}
					}
					for (var j = 0; j<responseSubmitFileNames.length; j++) {
						if (files[i].value==responseSubmitFileNames[j]) {
							setStatus(progress[i], status[i], responseSubmitMsg[j], responseSubmitCode[j], "Submit");
						}
					}
					//step 2 uploads
					if (<%= resourceId %> == -1) { //if resource id==-1 (Upload phase)
						if ((i>0 && files[i-1].value==responseUploadFileNames[responseUploadFileNames.length-1]) || responseUploadFileNames.length==0) {
							//if previous file is in session or this is the 1st file in the list, fill form and upload
							document.getElementById("fileName").value = files[i].value;
							if (files[i].value.substring(0,1) == "H") document.getElementById("resourceType").value = "CL";
							if (files[i].value.substring(0,4) == "OBEC") document.getElementById("resourceType").value = "OB";
							document.getElementById("description").value = document.getElementById("fileName").value;
							progress[i].value="Uploading...";
							
							// upload the form sets the file in session
							submitForm('uploadToMcedt', progress[i]);
							return;
							
						}
					} else { //if resourceid is not -1 submission phase
						if ( files[i].value=='<%=fileUpload%>' ) {
							// if this is the file intended for submission
							if (<%= resourceId %> != -2) {
								progress[i].value="Submitting...";								
							}
							submitForm('submitToMcedt', progress[i]);
							return;
							
						}
						
						
					}

				}
				//if we are done
				if (files.length==responseSubmitFileNames.length && files.length>0 ) ShowDoneDialog(true);
			}
			
			//enable the finish button
			document.getElementById("return").disabled = false;
		}
		
		function submitForm(methodType, control){
			ShowSpin(true);			
			var method = jQuery("#method");
			method.val(methodType);
			var url ="<%= request.getContextPath()%>/mcedt/autoUpload.do";
			var form = jQuery("#form");
			form.attr('action',url);
			form.submit();
			return true;
		}
		
		  window.onload = function () {
			setForm();
		} 
		 
		 function setStatus(progessControl, statusControl, resMsg, resCode, process){ //process is Upload or Submit
			 if (resCode=="IEDTS0001") progessControl.value= "Successful " + process;
			 else progessControl.value="Failed " + process;
		 statusControl.value = resMsg;
		 }
		 
		 // Done-div actions
		 //the click on dialog button
		 $(document).ready(function ()
        {
            $("#btnDone").click(function (e)
            {
            	return submitForm('cancelUpload', null);
                //ShowDoneDialog(true);
                //e.preventDefault();
            });
        });
		 // The functions to show/hide dialog:
			 function ShowDoneDialog(modal)
        {
            $("#donescreen").show();
            $("#donedialog").fadeIn(300);

            if (modal)
            {
                $("#donescreen").unbind("click");
            }
            else
            {
                $("#donescreen").click(function (e)
                {
                    HideDialog();
                });
            }
        }

        function HideDoneDialog()
        {
            $("#donescreen").hide();
            $("#donedialog").fadeOut(300);
        }
		 
	</script>
</head>
<body>
	<!-- <div class="show">
	    <img class="logo" src="web/img/kai.png"/>
	</div> -->
    <div class="greyBox">    
		<div class="center">
			<h1>Uploading Claims from Oscar to MCEDT</h1>
			<div>
				<%-- <html:form action="<%=request.getContextPath() %>/mcedt/kaichpass.do" method="POST"> --%>
				<%-- <html:form action="/mcedt/kaichpass" method="post" styleId="form">
				<input id="method" name="method" type="hidden" value="" />
				</html:form> --%>
				<div>
					<html:form action="/mcedt/autoUpload.do" method="post" styleId="form">
						<jsp:include page="../messages.jsp" />
						<jsp:include page="spinner.jsp" flush="true"/>			
						<input id="method" name="method" type="hidden" value="" />
						<html:hidden styleId="description" property="description" value="" />
						<html:hidden styleId="fileName" property="fileName" value='<%=fileUpload %>' />					
						<html:hidden property="resourceType" styleId="resourceType" value="" />					
						<html:hidden styleId="resourceId" property="resourceId" value='<%=resourceId.toString() %>' />					
					</html:form>
				
					<table class="whiteBox" width="100%" border="0" cellspacing="0" cellpadding="5" style="margin:5px 0 15px;">
						<tr class="greenBox">
							<td width="20%">File Name</td>
							<td>Created on</td>
							<td>Progress</td>
							<td>Status</td>			
						</tr>
						<% 
						if (toEdt != null) {
							int i=0;
							for (File file : toEdt) {
								i++;
								%> <tr>
							<td><input style="border: 0;width: 95%;" class="fileNames" type="text" value="<%=file.getName() %>" /></td>
							</td>
							<td><%=new java.util.Date(file.lastModified()) %></td>
							<td><input style="border: 0;width: 95%;" class="progress" id="prog<%=i %>" type="text" value="" /></td>
							<td><input style="border: 0;width: 95%;" class="status" type="text" value="" /></td>	
						</tr><% 
						}
				
					     
					    } else { %>
						<tr>
							<td>No Files are available</td>			
						</tr>
						<%} %>		
					</table>
				</div>
				<div>
					<button type="button" id="return" class="noBorder blackBox flatLink font12" onclick="return submitForm('cancelUpload', null);" >Return</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- The done popup divs -->
	<div id="donescreen" class="screen"></div>
	<div id="donedialog" class="spinner" style="display:none; background-color:#ffffff;border: 1px solid #191919;">
		<table style="width: 100%; border: 0px;" cellpadding="5" cellspacing="0">
			<tr>
				<td>Files Done uploading! Click below to remove them from Queue:</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;">
            		<button id="btnDone" type="button" class="noBorder blackBox flatLink">Done</button>
            	</td>
        	</tr>
    	</table> 
	</div>
</body>
</html>
