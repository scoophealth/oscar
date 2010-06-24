<%@page import="org.oscarehr.util.WebUtils"%>

<%@include file="/layouts/html_top.jspf"%>

<div style="border:solid gray 1px; text-align:center">
	<%=WebUtils.popInfoMessagesAsHtml(session)%>
	<%=WebUtils.popErrorMessagesAsHtml(session)%>
	
	<br />
	<input type="button" value="close" onclick='window.close()' />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="back" onclick='history.go(-1)' />
	<br /><br />
</div>

<%@include file="/layouts/html_bottom.jspf"%>