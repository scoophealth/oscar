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
<html:form action="/Lookup/LookupList.do">
<html:hidden property="tableId"/>
<table width="100%" border="0">
<tr><td width="50%"><html:text property="keywordName" style="width:100%;" /></td>
<td width="50%"><html:submit property="method" value="search" /></td></tr>
<tr><td colspan="2">
<display:table class="simple" style="width:100%;" cellspacing="0" cellpadding="0" id="lookup" name="lookupListForm.lookups" export="false" pagesize="0" requestURI="/PMmodule/Reports/QuatroReportList">
    <display:setProperty name="paging.banner.placement" value="bottom" />
    <display:setProperty name="paging.banner.item_name" value="agency" />
    <display:setProperty name="paging.banner.items_name" value="facilities" />
    <display:setProperty name="basic.msg.empty_list" value="No records found." />

    <display:column sortable="false" title="Code">
        <c:out value="${lookup.code}"/>
    </display:column>
    <display:column sortable="false" title="Description">
        <a href='javascript:selectMe("<c:out value="${lookup.code}" />", "<c:out value="${lookup.description}" />", 
        "<c:out value="${lookupListForm.openerFormName}" />", 
        "<c:out value="${lookupListForm.openerCodeElementName}" />", 
        "<c:out value="${lookupListForm.openerDescElementName}" />");'><c:out value="${lookup.description}"/> </a>
    </display:column>

</display:table>
</td></tr>
</table>
</html:form>
</body>
</html>
