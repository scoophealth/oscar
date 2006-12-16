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
			<jsp:include page="/navigation.jsp"/>
		 </td>
		 <td>
		 <div class=body>
		 	<br/><br/>
  			<h4>Authorization Failed</h4>
  			<p>
  				You do not have the proper permissions to perform this action.
  				Please speak to your administrator.
  			</p>
  			<p>
  				<a href="javascript:history.go(-1)">back</a>
  			</p>
  		 </div>
  		 </td>
  		</tr>
  	</table>
  </div>
  
  </body>
</html:html>
