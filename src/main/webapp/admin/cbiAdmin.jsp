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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="oscar.util.CBIUtil"%>
<html style="height: 100% !important;">
<head>
<title>CBI Upload Administration Panel</title>

<!-- <script type="text/javascript" src="../js/jquery-1.9.1.js"></script> -->
<script type="text/javascript" language="JavaScript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../js/jquery.treeview.js"></script>
<script type="text/javascript" language="JavaScript" src="../js/jquery-ui-1.8.18.custom.min.js"></script>

<link rel="stylesheet" href="../css/cupertino/jquery-ui-1.8.18.custom.css">
<link rel="stylesheet" href="../css/jqtreeview/jquery.treeview.css" />

<style type="text/css">
body
{
	font-size: 14px;
}

.table_main, .table_main_details, .table_records, .table_form_dtl
{
	border-collapse: collapse;
	border-style: solid;
	border-width: 1px;
	border-color: #CCCCFF;
}
.table_records
{
	font-size: 14px !important;
}
.table_main_details
{
	border-top: none !important;
	border-left: none !important;
	border-right: none !important;
}
.table_main_details td.summary, .table_main_details td.filter
{
	height: 30px;
}
.table_main_details td.summary span, .table_main_details td.filter span 
{
	margin-right: 15px;
}
.table_main_details>tbody>tr>td 
{
	padding-left: 5px;
	border-left: none !important;
	border-right: none !important;
	border-top: none !important;
}
.table_records>tbody>tr:HOVER
{
	background-color: #F0F0F0;
	cursor: pointer;
}
.table_records>thead
{
	background-color: #E3E3EA;
    color: #57627C;
}

.header
{
	font-weight: bold;
	font-size: 16px !important;
	background-color: #CCCCFF;
    color: black;
    height: 25px;
    padding-left: 5px;
}

.td_tree
{
	border-right-style: solid;
	border-right-width: 2px;
}
.td_content
{
	padding-left: 0px !important;
	padding-right: 0px !important;
}
.error
{
	color: red !important;
}

div.ui-dialog
{
	font-size: 13px !important;
}
.ui-widget-overlay {
    background: none !important;
    opacity: .50 !important; /* Make sure to change both of these, as IE only sees the second one */
  	filter: Alpha(Opacity=50) !important;
  	background-color: rgb(50, 50, 50) !important; 
}

.table_form_dtl td.label
{
	font-weight: bold;
}
</style>

<script type="text/javascript">
$(document).ready(function(){
	$(".ul1").treeview({
		/*persist: "location",*/
		collapsed: true,
		unique: true
	});
	window.resizeTo(900, 700);
});
function onclick_tree_item(obj)
{
	var dateStr = $(obj).html();
	//alert("in onclick_tree_item.. dateStr = "+dateStr);
	
	$(".div_content").html('<div align="center" style="margin-top: 100px; font-weight: bold; font-size: 15px;">Loading...</div>');
	$.ajax({
		url: "./cbiAdminSubmissionDtl.jsp?date="+dateStr,
		success: function(data){
			$(".div_content").html(data);
		}
	});
}
function onclick_filter(val)
{
	//alert("val = "+val);
	$(".table_records").find("tr").show();
	
	if(val!='all')
	{
		//hide rows having status other than val
		var cnt = 0;
		$(".table_records").find("tr").each(function(){
		    if(cnt++==0)
		        return;
		    var statusTDObj = $(this).children()[6];
		    var status = $(statusTDObj).html();
		    if(status!=val)
		    {
		        $(this).hide();
		    }
		});
	}
}
function onclick_submission(submissionId)
{
	//alert("submissionId = "+submissionId);
	$( "#dlg_cbi_form_dtl" ).dialog("open");
	$("#dlg_cbi_form_dtl .form_content").html('<div align="center" style="margin-top: 100px; font-weight: bold; font-size: 15px;">Loading...</div>');
	$.ajax({
		url: "./cbiAdminFormDtl.jsp?submissionId="+submissionId,
		success: function(data){
			$("#dlg_cbi_form_dtl .form_content").html(data);
		}
	});
}
$(function() {
	$( "#dlg_cbi_form_dtl" ).dialog({
		height: 450,
		width: 550,
		autoOpen: false,
		modal: true,
		buttons: {
	    	Close : function() {
	        	$(this).dialog("close");
	        }
		}
	});
});
</script>

</head>

<%!
CBIUtil cbiUtil = new CBIUtil();
%>

<%
List<Date> submissionDateList = cbiUtil.getSubmissionDateList();
StringBuffer treeDataStr = cbiUtil.generateTree(submissionDateList);
%>

<body style="height: 100%; margin: 0px 0px 0px 0px;">

<table class="table_main" border="1" width="100%" height="100%" bordercolor="#C0CBDB">
	<tr>
		<td colspan="2" valign="middle" class="header">CBI Upload Administration Panel</td>
	</tr>
	<tr>
		<td width="150px" valign="top" class="td_tree"><div class="div_tree"><%=treeDataStr.toString() %></div></td>
		<td valign="top" class="td_content">
			<div class="div_content">
			</div>
		</td>
	</tr>
</table>

<div id="dlg_cbi_form_dtl" title="CBI Form Details">
	<div class="form_content"></div>
</div>

</body>
</html>
