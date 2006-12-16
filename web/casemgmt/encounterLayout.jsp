<%@ include file="/casemgmt/taglibs.jsp" %>
<%@ page errorPage="/casemgmt/error.jsp" %>
<%@ page language="java"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
  	<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>	
	<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="<c:out value="${ctx}"/>/css/print.css" media="print" />
    <html:base />
    <title>Case Management</title>
    <meta http-equiv="Cache-Control" content="no-cache">
    <script type="text/javascript" language=javascript>
    </script>
  </head> 
  <body>
  <table border="0" width="100%" cellspacing="5">
  	<tbody>
  		<tr>
  		
  		
  			<td width="22%" valign="top"><tiles:insert attribute="navigation" /></td>
  		  	<td width="78%" valign="top"><tiles:insert attribute="body" /></td>
  		  	
  		</tr>
  	</tbody>
  </table>
  </body>
</html:html>
