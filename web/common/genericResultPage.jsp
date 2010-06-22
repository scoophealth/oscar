
<%@page import="org.oscarehr.util.WebUtils"%><%@include file="/layouts/html_top.jspf"%>

<%=WebUtils.popInfoMessagesAsHtml(session)%>
<%=WebUtils.popErrorMessagesAsHtml(session)%>

<br /><br />
<input type="button" value="close" onclick='window.close()' />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type="button" value="back" onclick='history.go(-1)' />

<%@include file="/layouts/html_bottom.jspf"%>