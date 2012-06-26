<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Faturamento</title>
<link rel="stylesheet" href="../../../web_fat.css">
</head>

<body background="../../../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table cellspacing="0" cellpadding="2" width="95%" border="1"
	align="center">
	<tr>
		<th>Faturamento realizado com sucesso</th>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="#" onclick="window.close();"> <img
			src="../../../images/leftarrow.gif" border="0" width="25" height="20"
			align="absmiddle"><bean:message key="global.btnBack" /></a></td>
		<td align="right"><a href="#" onclick="window.close();"><bean:message
			key="global.btnLogout" /><img src="../../../images/rightarrow.gif"
			border="0" width="25" height="20" align="absmiddle"></a></td>
	</tr>
</table>
</body>
</html:html>
</html>
