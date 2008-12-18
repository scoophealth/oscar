<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.oscarReport.data.RptSearchData,java.util.*"%>

<%
    oscar.oscarReport.data.RptSearchData searchData  = new oscar.oscarReport.data.RptSearchData();
    java.util.ArrayList queryArray = searchData.getQueryTypes();
%>



<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage Saved Demographic Queries</title>
<html:base />

<style type="text/css" media="print">
.MainTable {
	display: none;
}

.hiddenInPrint {
	display: none;
}

//
.header INPUT { //
	display: none;
	//
}

//
//
.header A { //
	display: none;
	//
}
</style>
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
</head>

<body class="BodyStyle" vlink="#0000FF">
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">oscarReport</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Manage Saved Demographic Queries</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')">Help</a> | <a
					href="javascript:popupStart(300,400,'About.jsp')">About</a> | <a
					href="javascript:popupStart(300,400,'License.jsp')">License</a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>

		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn"><html:form
			action="/report/DeleteDemographicReport">
			<ul style="list-style-type: none; padding-left: 3px;">
				<%for (int i =0 ; i < queryArray.size(); i++){
                           RptSearchData.SearchCriteria sc = (RptSearchData.SearchCriteria) queryArray.get(i);
                           String qId = sc.id;
                           String qName = sc.queryName;%>
				<li><input type="checkbox" name="queryFavourite"
					value="<%=qId%>"><%=qName%></input> <%}%>
				
			</ul>
			<input type="submit" value="Delete" />
			<a href="ReportDemographicReport.jsp">cancel</a>
		</html:form></td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>

</body>
</html:html>

