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

<!-- add by caisi -->
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- add by caisi end<style>* {border:1px solid black;}</style> -->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.oscarProvider.data.*"%>
<%@ page import="oscar.oscarProvider.pageUtil.*"%>


<%
if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");

ProSignatureData sig = new ProSignatureData();
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<html:base />
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">

<title><bean:message key="provider.editSignature.title" /></title>

</head>

<body class="BodyStyle" vlink="#0000FF">
<!-- add by caisi -->
<caisi:isModuleLoad moduleName="caisi">

	<iframe id="hiddenFrame" src="javascript:void(0)" style="display: none"></iframe>
	<script>
function toggleSig(n) {
	var fr=document.getElementById("hiddenFrame");
	var baseURL="/"+"<%=application.getServletContextName()%>";
	fr.src=baseURL+"/infirm.do?action=toggleSig&demoNo="+n;
}
</script>

</caisi:isModuleLoad>
<!-- add by caisi end-->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="provider.editSignature.msgPrefs" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message
					key="provider.editSignature.msgProviderSignature" /></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="signature" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn"><html:form
			action="/EnterSignature.do">
			<%
               ProEditSignatureForm thisForm = ( ProEditSignatureForm ) request.getAttribute("ProEditSignatureForm");
               thisForm.setSignature(sig.getSignature(curUser_no));
               if (sig.hasSignature(curUser_no)) {
            %>
			<bean:message key="provider.editSignature.msgEdit" />
			<br>
			<html:text property="signature" size="40" />
			<br>

			<!-- add by caisi -->
			<caisi:isModuleLoad moduleName="caisi">
				<c:import url="/infirm.do?action=getSig" />

				<INPUT TYPE="checkbox"
					<%= ((Boolean)session.getAttribute("signOnNote")).booleanValue()?"checked":""%>
					onchange="toggleSig('<%= curUser_no %>')">also sign the signature in encounter notes
               </caisi:isModuleLoad>
			<!-- add by caisi end-->

			<input type="submit"
				value="<bean:message key="provider.editSignature.btnUpdate"/>" />
			<% }else{%>
			<bean:message key="provider.editSignature.msgNew" />
			<br>
			<html:text property="signature" size="40" />
			<br>
			<!-- add by caisi -->
			<caisi:isModuleLoad moduleName="caisi">
				<c:import url="/infirm.do?action=getSig" />
				<INPUT TYPE="checkbox"
					<%= ((Boolean)session.getAttribute("signOnNote")).booleanValue()?"checked":""%>
					onchange="toggleSig('<%= curUser_no %>')">also sign the signature in encounter notes
               </caisi:isModuleLoad>
			<!-- add by caisi end-->
			<input type="submit"
				value="<bean:message key="provider.editSignature.btnSubmit"/>" />
			<%}%>
		</html:form></td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
