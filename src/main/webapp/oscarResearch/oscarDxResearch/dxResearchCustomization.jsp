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


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarResearch.oscarDxResearch.dxCustomization.title" /></title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<html:base />
<script type="text/javascript">

function setfocus(){
    window.focus();
    window.resizeTo(600,280);
}
</script>
</head>

<link rel="stylesheet" type="text/css" href="dxResearch.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="setfocus();">
<html:errors />
<table width="100%" bgcolor="#EEEEFF">
	<tr bgcolor="#000000">
		<td class="subject" colspan="3">&nbsp;&nbsp;&nbsp;<bean:message
			key="oscarResearch.oscarDxResearch.dxResearch.msgDxResearch" /></td>
	</tr>
	<tr>
		<td class=heading colspan="3"><bean:message
			key="oscarResearch.oscarDxResearch.dxCustomization.title" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td valign="center"><input type="button" class="mbttn"
			style="width: 180px"
			onClick="popupPage(230,600,'dxResearchNewQuickList.jsp')"
			value="<bean:message key="oscarResearch.oscarDxResearch.dxCustomization.addNewQuickList"/>" />
		</td>
		<td valign="center"><input type="button" class="mbttn"
			style="width: 180px"
			onClick="popupPage(230,600,'dxResearchLoadQuickList.do')"
			value="<bean:message key="oscarResearch.oscarDxResearch.dxCustomization.editQuickList"/>" />
		</td>
		<td valign="center"><input type="button" class="mbttn"
			style="width: 180px"
			onClick="popupPage(230,600,'dxResearchSelectAssociations.jsp')"
			value="<bean:message key="oscarResearch.oscarDxResearch.dxCustomization.editAssociations"/>" />
		</td>
	</tr>
	<tr>
		<td></td>
	</tr>
</table>
</body>
</html:html>
