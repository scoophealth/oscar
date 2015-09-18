<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>


<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css" href="dxResearch.css">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarResearch.oscarDxResearch.dxCustomization.selectQuickList" />
</title>

<script type="text/javascript">

function setfocus(){
    window.focus();
    window.resizeTo(450,220);
}
</script>

</head>

<body class="BodyStyle" vlink="#0000FF" rightmargin="0" leftmargin="0"
	topmargin="0" marginwidth="0" marginheight="0" onload="setfocus()">
<!--  -->
<html:form
	action="/oscarResearch/oscarDxResearch/dxResearchLoadQuickListItems.do">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr bgcolor="#000000">
					<td class="subject" colspan="2">&nbsp;&nbsp;&nbsp;<bean:message
						key="oscarResearch.oscarDxResearch.dxResearch.msgDxResearch" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="1"
				bgcolor="#EEEEFF" height="125">
				<tr>
					<td align="left"><bean:message
						key="oscarResearch.oscarDxResearch.dxCustomization.pleaseSelectAQuickList" />
					<html:select property="quickListName" style="width:200px">
						<logic:iterate id="quickLists" name="allQuickLists"
							property="dxQuickListBeanVector">
							<option
								value="<bean:write name="quickLists" property="quickListName" />"
								<bean:write name="quickLists" property="lastUsed" />><bean:write
								name="quickLists" property="quickListName" /></option>
						</logic:iterate>
					</html:select></td>
				</tr>
				<tr>
					<td>
					<table>
						<tr>
							<input type="hidden" name="forward" value="error" />
							<td><input type="button" class="mbttn" name="Button"
								value="<bean:message key="global.btnClose"/>"
								onClick="window.close()"></td>
							<td><input type="submit" class="mbttn" name="Button"
								value="Continue" /></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
