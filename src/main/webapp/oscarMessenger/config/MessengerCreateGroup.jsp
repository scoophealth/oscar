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

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarMessenger.config.MessengerCreateGroup.title" /></title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<script language="javascript">
function BackToOscar()
{
    if (opener.callRefreshTabAlerts) {
	opener.callRefreshTabAlerts("oscar_new_msg");
        setTimeout("window.close()", 100);
    } else {
        window.close();
    }
}
</script>





<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF">



<html:errors />
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<tr>
		<td width="100%"
			style="padding-left: 3; padding-right: 3; padding-top: 2; padding-bottom: 2"
			height="0%" colspan="2">
		<p class="HelpAboutLogout"><span class="FakeLink"><a
			href="Help.htm">Help</a></span> | <span class="FakeLink"><a
			href="About.htm">About</a></span> | <span class="FakeLink"> <a
			href="Disclaimer.htm">Disclaimer</a></span></p>
		</td>
	</tr>
	<tr>
		<td width="10%" height="37" bgcolor="#000000">&nbsp;</td>
		<td width="100%" bgcolor="#000000"
			style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%">
		<p class="ScreenTitle"><bean:message key="application.title.admin" /></p>
		</td>
	</tr>
	<tr>
		<td></td>
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle">
				<% int style=0;
                                    String levelOrGroup = new String();
                                if (request.getParameter("Level") != null){
                                      style=1;
                                      levelOrGroup = request.getParameter("Level");
                                %> <bean:message
					key="oscarMessenger.config.MessengerCreateGroup.msgAddGroup" /> <%}else if(request.getParameter("Group") != null){
                                      style =2;
                                      levelOrGroup = request.getParameter("Group");
                                %> <bean:message
					key="oscarMessenger.config.MessengerCreateGroup.msgRenameGroup" />
				<%}else{
                                      style=3;
                                %> MR HAcker go away <%}%>
				</div>
				</td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead"><a
					href="MessengerAdmin.jsp?groupNo=<%=levelOrGroup%>"><bean:message
					key="oscarMessenger.config.MessengerCreateGroup.cancel" /></a></div>
				</td>
			</tr>
			<tr>
				<td><br>





				<html:form action="/oscarMessenger/AddGroup">
					<br>
					<br>
					<% if (style == 1){%>
					<bean:message
						key="oscarMessenger.config.MessengerCreateGroup.newGroupsName" />
					<%}else{%>
					<bean:message
						key="oscarMessenger.config.MessengerCreateGroup.changeGroupsName" />
					<%}%>
					<br>
					<input type="text" name="groupName">
					<html:hidden property="type2" value="<%=Integer.toString(style)%>" />
					<input type="hidden" name="parentID" value="<%=levelOrGroup%>">

					<input type="submit" name="Submit" class="ControlPushButton"
						value="<bean:message key="oscarMessenger.config.MessengerCreateGroup.btnSubmit"/>">
					<input type="reset" class="ControlPushButton"
						value="<bean:message key="oscarMessenger.config.MessengerCreateGroup.btnReset"/>">
				</html:form> <br>
				<br>
				<br>
				<br>
				<br>
				</td>
			</tr>
			<!----End new rows here-->

			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC"
			colspan="2"></td>
	</tr>
</table>
</body>
</html:html>
