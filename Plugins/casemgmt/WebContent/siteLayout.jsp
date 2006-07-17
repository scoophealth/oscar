
<%@ page language="java"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>

    <html:base />
    <title><tiles:getAsString name="title" /></title>
  </head> 
  <body>
  <table border="1" width="600" cellspacing="5">
  	<tbody>
  		<tr><td colspan="2"><tiles:insert attribute="header" /></td></tr>
  		<tr>
  			<td width="200">navigation</td>
  			<td width="400"><tiles:insert attribute="body" /></td>
  		</tr>
  		<tr><td colspan="2"><tiles:insert attribute="footer" /></td></tr>
  	</tbody>
  </table>
  </body>
</html:html>
