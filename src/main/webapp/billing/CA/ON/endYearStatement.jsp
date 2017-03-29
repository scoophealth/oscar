<!DOCTYPE html>
<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>    
    
<html>
<head>
<title><bean:message key="admin.admin.endYearStatement"/></title>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">

<script type="text/javascript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
	  var page = "" + varpage;
	  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
	  window.location = page;
}

function demographicSearch() {
	var search_param =$('#nameForlooksOnly').val();
	var url = '../../../demographic/demographicsearch2reportresults.jsp';
	url += '?originalpage='+escape('../billing/CA/ON/endYearStatement.do?demosearch=true');
	url += '&search_mode=search_name';
	url += '&orderby=last_name, first_name';
	url += '&limit1=0&limit2=5';
	url += '&keyword='+search_param;
	popupPage(700,1000,url,'master');
	return false;	
}



function calToday(field) {
	var calDate=new Date();
	varMonth = calDate.getMonth()+1;
	varMonth = varMonth>9? varMonth : ("0"+varMonth);
	varDate = calDate.getDate()>9? calDate.getDate(): ("0"+calDate.getDate());
	field.value = calDate.getFullYear() + '-' + (varMonth) + '-' + varDate;
}

function validateFields() {
	if($('#nameForlooksOnly').val() == '') {
		alert('Please select a valid patient for this report.');
		return false;
	}
	return true;
	
}
//-->


</script>

<style>
.well{padding-left:8px; padding-right:8px;}
</style>
</head>

<%
String name="";
if(request.getParameter("firstNameParam")!=null && request.getParameter("lastNameParam")!=null){
name=request.getParameter("firstNameParam")+" "+request.getParameter("lastNameParam");
}
%>
<body>
<h3><bean:message key="admin.admin.endYearStatement"/></h3>

<div class="container-fluid">

<div class="row well">
	<html:form action="billing/CA/ON/endYearStatement">
		<html:hidden property="demographicNoParam"/>
		
		<div class="span5">  
		Patient Name: <br>
		<div class="input-append">
		<input class="span4" id="nameForlooksOnly" type="text" value="<%=name%>">
		<button class="btn btn-primary" type="button" value="Search" onclick="demographicSearch()"><i class="icon icon-search"></i></button>
		</div>
		</div>

		<html:hidden property="firstNameParam" styleId="fname"></html:hidden> 
		<html:hidden property="lastNameParam" styleId="lname"></html:hidden> 


		<div class="span2">
		<label>Start Date:</label>
		<div class="input-append">
			<input type="text" style="width:90px" name="fromDateParam" id="fromDateParam" value="<%= request.getAttribute("fromDateParam") != null ? request.getAttribute("fromDateParam") : "" %>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
			<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
		</div>
		
	
		
		<div class="span2">
		<label>End Date:</label>
		<div class="input-append">
			<input type="text" style="width:90px" name="toDateParam" id="toDateParam" value="<%= request.getAttribute("toDateParam") != null ? request.getAttribute("toDateParam") : "" %>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
			<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
		</div>
		
		<div class="span10">
		<input class="btn" type="submit" name="search" value="Create Statement" onclick="return validateFields();"> 

		<input class="btn" type="submit" name="pdf" value="Print PDF" <logic:empty name="result">disabled="disabled"</logic:empty> >
		</div>
	</html:form>
</div>

<div class="row">

<div style="color"red"><html:errors/></div>

<logic:notEmpty name="summary">
<table class="table table-striped  table-condensed">
	<tr>
		<td width="50px">&nbsp;</td>
		<td align="left" colspan="2">Patient Information</td>
	</tr>
	<tr>
		<td width="50px">&nbsp;</td>
		<td width="100px">Patient:</td>
		<td>
			<bean:write name="summary" property="patientNo" />&nbsp;&nbsp;
			<bean:write name="summary" property="patientName" />&nbsp;&nbsp;
			<bean:write name="summary" property="hin" />&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td width="50px">&nbsp;</td>
		<td width="100px">Address :</td>
		<td>
			<bean:write name="summary" property="address" />
		</td>
	</tr>
	<tr>
		<td width="50px">&nbsp;</td>
		<td width="100px">Phone :</td>
		<td>
			<bean:write name="summary" property="phone" />
		</td>
	</tr>
</table>
</logic:notEmpty>

<br/>

<logic:notEmpty name="result">
<table class="table table-striped  table-condensed">
				<tr bgcolor=#ccffcc>
					<th>INVOICE NUMBER</th>
					<th>INVOICE DATE</th>
					<th>SERVICE CODE</th>
					<th>INVOICED</th>
					<th>PAID</th>
				</tr>
<logic:iterate id="row" name="result" indexId="counter"> 
				<tr bgcolor="#CEF6CE" >
					<td><bean:write name="row" property="invoiceNo" /></td>
					<td><bean:write name="row" property="invoiceDate" /></td>
					<td>&nbsp;</td>
					<td><bean:write name="row" property="invoiced" /></td>
					<td><bean:write name="row" property="paid" /></td>
				</tr>
<logic:iterate id="service" name="row" property="services" indexId="counterService"> 
				<tr bgcolor="<%= counterService.intValue()%2 == 0 ? "ivory" : "#EEEEFF" %>" >
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><bean:write name="service" property="code" /></td>
					<td><bean:write name="service" property="fee" /></td>
					<td>&nbsp;</td>
				</tr>
</logic:iterate>
</logic:iterate>
				<tr height="10">
					<td colspan="5" style="border:collapse">&nbsp;</td>
				</tr>
				<tr bgcolor="#99FF66" >
					<td>Count: &nbsp;&nbsp;&nbsp; <logic:notEmpty name="summary"><bean:write name="summary" property="count" /></logic:notEmpty></td>
					<td>&nbsp;</td>
					<td align="center">Total:</td>
					<td><logic:notEmpty name="summary"><bean:write name="summary" property="invoiced" /></logic:notEmpty></td>
					<td><logic:notEmpty name="summary"><bean:write name="summary" property="paid" /></logic:notEmpty></td>
				</tr>
</table>
</logic:notEmpty>
</div><!--row-->


</div><!--container-->
</body>
<script type="text/javascript">
	var startDate = $("#fromDateParam").datepicker({format : "yyyy-mm-dd"});
	var endDate = $("#toDateParam").datepicker({format : "yyyy-mm-dd"});

</script>
</html>
