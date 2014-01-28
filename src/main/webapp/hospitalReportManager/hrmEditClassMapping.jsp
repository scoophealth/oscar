<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="java.util.*, org.oscarehr.hospitalReportManager.*, org.oscarehr.hospitalReportManager.model.HRMCategory, org.oscarehr.hospitalReportManager.dao.HRMCategoryDao, org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%@page import="org.oscarehr.hospitalReportManager.dao.HRMSubClassDao" %>
<%@page import="org.oscarehr.hospitalReportManager.model.HRMSubClass" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%
	
	String deepColor = "#CCCCFF", weakColor = "#EEEEFF";

	String country = request.getLocale().getCountry();
	
	HRMSubClassDao hrmSubClassDao = (HRMSubClassDao)SpringUtils.getBean("HRMSubClassDao");
	HRMCategoryDao categoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");
	
	List<String> sendingFacilityIds = hrmSubClassDao.findAllSendingFacilityIds();
	
	List<HRMCategory> categoryList = categoryDao.findAll();
	
	String id = request.getParameter("id");
	
	String className = "";
	String subClassName = "";
	String subClassMnemonic = "";
	String subClassDescription = "";
	String sendingFacilityId = "";
	String categoryId = "0";
	
	
	if(id != null) {
		HRMSubClass sc = hrmSubClassDao.find(Integer.parseInt(id));
		if(sc != null) {
			className = sc.getClassName();
			subClassName = sc.getSubClassName();
			subClassMnemonic = sc.getSubClassMnemonic();
			subClassDescription = sc.getSubClassDescription();
			sendingFacilityId = sc.getSendingFacilityId();
			HRMCategory c = sc.getHrmCategory();
			if(c != null) {
				categoryId = String.valueOf(c.getId());
			}
			
		}
		
	}
	
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
<title>Show HRM Mappings</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css"
	href="../share/css/eformStyle.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
	
<script type="text/javascript">

var categoryList = new Array();

<%
for(HRMCategory cat:categoryList) {
	%>
	categoryList.push({id:"<%=cat.getId()%>",name:"<%=cat.getCategoryName()%>",sendingFacilityId:"<%=cat.getSendingFacilityId()%>"});
	<%
}%>

function getCategories(sendingFacilityId, defaultVal) {
	var response = "";
	
	for(var x=0;x<categoryList.length;x++) {
		if(categoryList[x].sendingFacilityId == sendingFacilityId) {
			if(defaultVal != undefined &&  categoryList[x].id === defaultVal) {
				response += "<option value='"+categoryList[x].id+"' selected='selected'>"+categoryList[x].name+"</option>";	
			} else {
				response += "<option value='"+categoryList[x].id+"'>"+categoryList[x].name+"</option>";	
			}
		}
	}
	return response;
}

$(document).ready(function() {
	$("#className").val('<%=className%>');
	$("#subclass").val('<%=subClassName%>');
	$("#mnemonic").val('<%=subClassMnemonic%>');
	$("#description").val('<%=StringEscapeUtils.escapeJavaScript(subClassDescription)%>');
	$("#sendingFacilityIdSelect").bind('change',function(){
		$("#category").html(getCategories($("#sendingFacilityIdSelect").val()),null);
	});
	$("#category").html(getCategories('<%=sendingFacilityId%>','<%=categoryId%>'));
});
</script>

</head>

<body class="BodyStyle" vlink="#0000FF">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175">HRM</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Edit HRM Mapping</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">
			</td>
		<td class="MainTableRightColumn" valign="top">
			<form method="post" action="<%=request.getContextPath() %>/hospitalReportManager/Mapping.do">
				<input type="hidden" name="id" value="<%=id%>"/>
				Report class:
				<select name="class" id="className">
					<option value="Medical Records Report">Medical Records Report</option>
					<option value="Diagnostic Imaging Report">Diagnostic Imaging Report</option>
					<option value="Cardio Respiratory Report">Cardio Respiratory Report</option>
				</select>
				<br />
				Sub-class: <input type="text" name="subclass" id ="subclass" /><br />  
				Sub-class mnemonic: <input type="text" name="mnemonic" id="mnemonic" /><br />
				Sub-class description: <input type="text" name="description" id="description"/><br />
				Sending Facility ID : 
				<select name="sendingFacilityIdSelect" id="sendingFacilityIdSelect"> 
					<option value="0">Select Option or New</option>
					<option value="-1">New</option>
				<%for(String tmp:sendingFacilityIds) {
					String selected="";
					if(tmp.equals(sendingFacilityId)) {
						selected="selected=\"selected\"";
					}
				%>
					<option value="<%=tmp%>" <%=selected %>><%=tmp %></option>
				<%} %>
				</select>
				
				or <input type="text" name="sendingFacilityId" placeholder="New Sending Faclity Id"/><br /> 
				Category: <select name="category" id="category">
				
				</select><br /><br />
				
				<input type="submit" name="submit" value="Save" />
				
			</form>
		</td>
	</tr>
</table>

</body>
</html:html>
