<%@ page errorPage="error.jsp" %>
<%@ page language="java"%>

<%@ include file="/survey/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    <title>Oscar Forms</title>
    		<style type="text/css">
			/* <![CDATA[ */
			@import "<html:rewrite page="/survey/css/standard.css" />";
			/*  ]]> */
		</style>
<style type="text/css">
<!--
	.message {
		color: red;
		background-color: white;
	}
	.error {
		color: red;
		background-color: white;
	}
-->
</style>
  </head> 
  <body>
  
	 <table border="0" cellspacing="0" cellpadding="18" width="100%">
		<logic:messagesPresent message="true">
			<html:messages id="message" message="true" bundle="survey">
	   			<tr><td colspan="3" class="message"><c:out value="${message}"/></td></tr>
	    	</html:messages>
		</logic:messagesPresent>
		<logic:messagesPresent>
	  	  <html:messages id="error" bundle="survey">
            <tr><td colspan="3" class="error"><c:out value="${error}"/></td></tr>
       	 </html:messages>
		</logic:messagesPresent>
		<tr>
			<td><a href="javascript:history.go(-1)">back</a></td>
		</tr>
	 </table>
	
  </body>
</html:html>
