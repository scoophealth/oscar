<%@ page errorPage="error.jsp"%>
<%@ page language="java"%>
<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
	<head>
		<html:base />
		<title>Program Management Module</title>
		<style type="text/css">
			@import "<html:rewrite page="/css/tigris.css" />";
			@import "<html:rewrite page="/css/displaytag.css" />";
			@import "<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />";
		</style>
		<script type="text/javascript" src="<html:rewrite page="/jsCalendar/calendar.js" />"></script>
		<script type="text/javascript" src="<html:rewrite page="/jsCalendar/lang/calendar-en.js" />"></script>
		<script type="text/javascript" src="<html:rewrite page="/jsCalendar/calendar-setup.js" />"></script>
				
		<script type="text/javascript">
			var djConfig = { 
				isDebug: false
			};
		</script>
	    
		<script type="text/javascript" src="<html:rewrite page="/dojoAjax/dojo.js" />"></script>
		
		<script type="text/javascript" language="JavaScript">
			// require statements
			dojo.require("dojo.widget.*");
			dojo.require("dojo.validate.*");
		</script>
	</head>
	<body>
		<div class="composite">
			<table border="0" cellspacing="0" cellpadding="18" width="100%">
				<tr valign="top">
					<td id="leftcol" width="20%">
						<tiles:insert attribute="navigation" />
					</td>
					<td>
						<div class="body">
							<tiles:insert attribute="body" />
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html:html>