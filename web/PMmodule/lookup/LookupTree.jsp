<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts-menu.sf.net/tag" prefix="menu" %>
<%@ taglib uri="http://struts-menu.sf.net/tag-el" prefix="menu-el" %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Lookup</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<script type="text/javascript" src='<c:out value="${ctx}"/>/js/quatroReport.js'></script>
<script type="text/javascript" src='<c:out value="${ctx}"/>/js/quatroLookup.js'></script>
<script type="text/javascript" src='<c:out value="${ctx}"/>/js/menuExpandable.js'></script>
<style type="text/css">
    @import "<html:rewrite page="/css/menuExpandable.css" />";
</style>
<bean:define id="tree" name="lookupTreeForm" property="tree" type="net.sf.navigator.menu.MenuRepository" />
</head>
<body>

<html:form action="/Lookup/LookupTree.do">
<html:hidden property="tableId"/>
<html:hidden property="openerForm"/>
<html:hidden property="codeName"/>
<html:hidden property="descName"/>
<table width="100%" border="0">
<tr><td width="80%">Description: <html:text property="keywordName" style="width:100%;" maxlength="80" /></td>
<td width="20%"><html:submit property="method" value="search" /></td></tr>
<tr><td colspan="2">
<menu:useMenuDisplayer name="ListMenu" repository="tree"> 
    <c:forEach var="menu" items="${tree.topMenus}"> 
        <menu-el:displayMenu name="${menu.name}" /> 
    </c:forEach> 
</menu:useMenuDisplayer>
</td></tr>
</table>

</html:form>
</body>
<script type="text/javascript">initializeMenus();</script>
</html>
