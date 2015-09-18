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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarLab.ca.all.pageUtil.SendOruR01UIBean"%>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.Gender"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html>
<head>
	<title>Send eData</title>
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
	<script type="text/javascript">
		function checkRequiredFields() {
			if (jQuery("#professionalSpecialistId").val().length==0) {
				alert('Select a provider / specialist to send to.');
				return(false);
			}			
			if (jQuery("#clientFirstName").val().length==0 || jQuery("#clientLastName").val().length==0) {
				alert('The clients first and last name is required.');
				return(false);
			}	
			if (jQuery("#subject").val().length==0) {
				alert('The data name is required.');
				return(false);
			}	
			if (jQuery("#textMessage").val().length==0 && jQuery("#uploadFile").val().length==0) {
				alert('Either Text Data or an Upload File is required.');
				return(false);
			}	
			return(true);
		}
	</script>
</head>
<body>
	<h4>Send eData <span style="font-size:9px">(ORU_R01 : Unsolicited Observation Message)</span></h4>
<%--
This jsp accepts parameters with the same name as
the fields in the SendOruR01UIBean. All parameters are optional
for pre-populating data.
--%>
<%
	SendOruR01UIBean sendOruR01UIBean=new SendOruR01UIBean(request);
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<form method="post" enctype="multipart/form-data" action="oruR01Upload.do" onsubmit="return checkRequiredFields()" class="well form-horizontal">
	<fieldset>
		<div class="control-group">
			<label class="control-label">From Provider:</label>
			<div class="controls">
				<%=SendOruR01UIBean.getLoggedInProviderDisplayLine(loggedInInfo)%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">To Provider / Specialist:</label>
			<div class="controls">
				<select name="professionalSpecialistId" id="professionalSpecialistId">
					<option value="">--- none selected ---</option>
					<%
						for (ProfessionalSpecialist professionalSpecialist : SendOruR01UIBean.getRemoteCapableProfessionalSpecialists()) 						{
					%>
					<option value="<%=professionalSpecialist.getId()%>" <%=sendOruR01UIBean.renderSelectedProfessionalSpecialistOption(professionalSpecialist.getId())%> ><%=SendOruR01UIBean.getProfessionalSpecialistDisplayString(professionalSpecialist)%></option>
					<%
						}
					%>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><strong>For Client</strong></label>
			<div class="controls">&nbsp;</div>
		</div>
		<div class="control-group">
			<label class="control-label">First Name</label>
			<div class="controls">
				<input type="text" id="clientFirstName" name="clientFirstName" value="<%=sendOruR01UIBean.getClientFirstName()%>" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Last Name</label>
			<div class="controls">
				<input type="text" id="clientLastName" name="clientLastName" value="<%=sendOruR01UIBean.getClientLastName()%>" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Health Number<br />(excluding version code)</label>
			<div class="controls">
				<input type="text" name="clientHealthNumber" value="<%=sendOruR01UIBean.getClientHin()%>" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">BirthDay</label>
			<div class="controls">
				<input type="text" id="clientBirthDay" name="clientBirthDay" value="<%=sendOruR01UIBean.getClientBirthDate()%>" />
				<script type="text/javascript">
					jQuery(document).ready(function() {
						Date.format='yy-mm-dd';
						jQuery("#clientBirthDay").datepicker({dateFormat: 'yy-mm-dd'});
					});
				</script>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Gender</label>
			<div class="controls">
				<select name="clientGender">
					<option value="">--- none selected ---</option>
					<%
						for (Gender gender : Gender.values()) {
					%>
						<option value="<%=gender.name()%>" <%=sendOruR01UIBean.renderSelectedGenderOption(gender)%> ><%=gender.getText()%></option>
					<%
						}
					%>
				</select>
			</div>
		</div>
		<hr/>
		<div class="control-group">
			<label class="control-label">Subject</label>
			<div class="controls">
				<input type="text" id="subject" name="subject" value="<%=sendOruR01UIBean.getSubject()%>" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Text Message</label>
			<div class="controls">
				<textarea id="textMessage" name="textMessage" style="width:40em;height:8em" ><%=sendOruR01UIBean.getTextMessage()%></textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Upload File</label>
			<div class="controls">
				<input type="file" id="uploadFile" name="uploadFile" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">&nbsp;</label>
			<div class="controls">
				<input type="submit" class="btn btn-primary" value="Electronically Send Data" />&nbsp;<input type="button" class="btn" value="close" onclick='window.close()' />
			</div>
		</div>
	</fieldset>
</form>
</body>
</html>