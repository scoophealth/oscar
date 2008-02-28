<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html>
<head>
<title>Lookup</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<script type="text/javascript" src='<c:out value="${ctx}"/>/js/quatroReport.js'></script>
<script type="text/javascript" src='<c:out value="${ctx}"/>/js/quatroLookup.js'></script>
<style type="text/css">
	@import "<html:rewrite page="/css/displaytag.css" />";
</style>
</head>
<body>
<html:form action="/Lookup/LookupCodeEdit.do">
 <table cellpadding="3" cellspacing="0" border="0" width="100%">
     <tr>
         <td style="color: white;font-family:Tahoma,Verdana,Arial;font-size: 14px;padding-left:8px;padding-right:8px;padding-top:4px;padding-bottom:4px; font-weight: bold" background="../images/TitleBar2.png" align="center">
			Code List</td>
     </tr>
     <tr>
     <td style="font-family:Tahoma,Verdana,Arial;font-size: 14px;padding-left:8px;padding-right:8px;padding-top:4px;padding-bottom:4px;" background="../images/ButtonBar2.png"  align="left">
		<html:link  action="/Lookup/LookupCodeEdit.do" paramName="lookupCodeListForm" paramProperty="tableDef.tableId" paramId="id">
		<img src="../images/New16.png" border="0"/> Add</html:link>&nbsp;|&nbsp;
		<html:link action="/Lookup/LookupTableList.do"> <img src="../images/Back16.png" border="0"/> Back to Lookup Fields</html:link>
</td></tr>
     
     
     
     
 </table>

<table>
<tr>
     <th>Category: </th>
     <th align="left"><bean:write name="lookupCodeListForm" property="tableDef.moduleName" /></th>
</tr>
<tr>
     <th>Field: </th>
     <th alighn="left"><bean:write name="lookupCodeListForm" property="tableDef.description"/></th>
</tr>
<tr><td colspn="2">&nbsp;</td>
</tr>
<tr>
	<th>Id</th><th>Description</th>
</tr>

<logic:iterate id="lkCode" name="lookupCodeListForm" property="codes">
<tr>
	<td>
		<html:link action="/Lookup/LookupCodeEdit.do" paramId="id" paramProperty="codeId" paramName="lkCode">
			<bean:write name="lkCode" property="code" /> 
 		</html:link>
 	</td>
 	<td>
		<html:link action="/Lookup/LookupCodeEdit.do" paramId="id" paramProperty="codeId" paramName="lkCode">
 			<bean:write name="lkCode" property="description" />
	 	</html:link>
 	</td>
</tr> 	
	</logic:iterate>

<tr><td colspan="2">

</td>

</table>



</html:form>
</body>
</html>
