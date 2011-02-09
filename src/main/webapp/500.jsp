<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.util.Enumeration"%>

<%@page isErrorPage="true" %>
<%@include file="/layouts/caisi_html_top.jspf"%>

An unexpected system error occurred.<br />
Please contact support with the date and time of this error.<br />

<input type="button" value="Back" onclick="history.go(-1);" />

<%@include file="/layouts/caisi_html_bottom.jspf"%>

<%@include file="/log_error.jspf"%>
