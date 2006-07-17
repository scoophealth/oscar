<%@ page errorPage="error.jsp" %>
<%@ page language="java"%>

<%@ include file="/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    <title>Program Management Module</title>
    		<style type="text/css">
			/* <![CDATA[ */
			@import "<html:rewrite page="/css/tigris.css" />";
			/*  ]]> */
		</style>
  </head> 
  <body>
  
  
  <div  class="composite">
	 <table border="0" cellspacing="0" cellpadding="18" width="100%">
	   <tr valign="top">
	   	 <td id="leftcol" width="20%">
			<tiles:insert attribute="navigation"/>
		 </td>
		 <td>
		 <div class=body>
  			<tiles:insert attribute="body" />
  		 </div>
  		 </td>
  		</tr>
  	</table>
  </div>
  
  </body>
</html:html>
