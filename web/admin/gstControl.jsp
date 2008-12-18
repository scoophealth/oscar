<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ page language="java"%>
<%@ page
	import="java.util.*,oscar.oscarReport.data.*, java.util.Properties, oscar.oscarBilling.ca.on.administration.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">

<%

Properties props = new Properties();
GstControlAction db = new GstControlAction();
props = db.readDatabase();
String percent = props.getProperty("gstPercent");

%>

<script type="text/javascript">
    function submitcheck(){
            document.getElementById("gstPercent").value = extractNums(document.getElementById("gstPercent").value);
    }
    function extractNums(str){
        return str.replace(/\D/g, "");
    }



</script>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit GST</title>
</head>
<body
	style="font-family: arial, helvetica, sans-serif; font-size: 13px;"
	onload="loadData()">
<html:form action="/admin/GstControl">
	<TABLE>
		<TR>
			<TD>GST :</TD>
			<TD align="right"><input type="text" size="3" maxlength="3"
				id="gstPercent" name="gstPercent" value="<%=percent%>" />%</TD>
		</TR>
	</TABLE>
	<input type="submit" value="Submit" onclick="submitcheck()" />
</html:form>
</body>
</html:html>
