<%@ page errorPage="error.jsp" %>
<%@ page language="java"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    <title>Case Management</title>
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
