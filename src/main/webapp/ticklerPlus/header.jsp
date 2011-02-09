
<%-- Updated by Eugene Petruhin on 20 feb 2009 while fixing check_date() error --%>

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
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
	<title>TicklerPlus</title>

	<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
	<html:base />
	</head>
	<body>
</c:if>

<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<script>
		// date picker variables
		var readOnly = false;
		var win = null;
		var timerId = 0;
		var isDateValid = true;
		var doOnBlur = true;
		var deferSubmit = false;

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
		<th width="30%"></th>
		<th width="30%" align="center">TicklerPlus</th>
		<th width="30%"></th>
		<th width="10%"></th>
	</tr>