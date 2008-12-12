<%@ include file="/taglibs.jsp"%>
<%@ page
	import="org.caisi.model.*,org.oscarehr.PMmodule.model.*,org.springframework.context.*,org.springframework.web.context.support.*"%>
<%@ page import="java.util.Date"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tasks"
	rights="r" reverse="<%=true%>">
	<%response.sendRedirect("noRights.html");%>
</security:oscarSec>

<%
	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
%>
<c:if test="${requestScope.from ne 'CaseMgmt'}">
	<html>
	<head>
	<title>TicklerPlus</title>

	<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
	<html:base />
	</head>
	<body>
</c:if>

<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<script>
		function openBrWindow(theURL,winName,features) { 
		  window.open(theURL,winName,features);
		}
		
		function Check(e) {
			e.checked = true;
		}
		
		function Clear(e) {
			e.checked = false;
		}
		    
		function CheckAll(ml) {
			var len = ml.elements.length;
			for (var i = 0; i < len; i++) {
			    var e = ml.elements[i];
			    if (e.name == "checkbox") {
					Check(e);
			    }
			}
		}
		
		function ClearAll(ml) {
			var len = ml.elements.length;
			for (var i = 0; i < len; i++) {
			    var e = ml.elements[i];
			    if (e.name == "checkbox") {
				Clear(e);
			    }
			}
		}
	</script>
<table border="0" cellspacing="0" cellpadding="1" width="100%"
	bgcolor="#CCCCFF">
	<tr class="subject">
		<th width="20%"></th>
		<th colspan="2" width="80%">TicklerPlus</th>
		<th width="20%" align="right" nowrap></th>
	</tr>