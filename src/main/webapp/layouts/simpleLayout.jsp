<%@ include file="/taglibs.jsp"%>

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><tiles:getAsString name="title" /></title>
<tiles:get name="meta" />
<tiles:get name="stylesheet" />
<tiles:get name="javascript" />
</head>
<body bgcolor="#FFFFFF" text="#000000" onLoad="BodyOnLoad();"
	onClick="BodyOnClick();">
<table border="0" cellpadding="0" cellspacing="0" width="100%"
	height="5%">
	<tr>
		<td align="center" valign="top" height="1%"><tiles:get
			name="header" /></td>
	</tr>
</table>
<br />
<tiles:get name="body" />
<br />
<table border="0" cellpadding="0" cellspacing="0" width="100%"
	height="5%">
	<tr>
		<td align="center" valign="bottom" height="1%"><tiles:get
			name="footer" /></td>
	</tr>
</table>
</body>
</html:html>

