<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
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
<table cellspacing="0" cellpadding="2" width="100%" border="0"
	align="center">
	<tr>
		<th>Faturamento</th>
	</tr>
</table>
<p>&nbsp;</p>
<table cellspacing="0" cellpadding="2" width="95%" border="1"
	align="center">
	<tr>
		<th>Hist&oacute;rico do Paciente</th>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="95%" border="1"
	align="center">
	<tr>
		<th class="tableLabel" width="20%" align="center">Data da
		consulta</th>
		<th class="tableLabel" width="20%" align="center">Nr. da consulta</th>
		<th class="tableLabel" width="40%" align="left">M&eacute;dico</th>
		<th class="tableLabel" width="10%" align="left">Situa&ccedil;&atilde;o</th>
	</tr>
	<logic:present name="BILLING">
		<logic:iterate id="consulta" name="BILLING"
			type="oscar.billing.model.Appointment">
			<tr class="body">
				<td>
				<div align="center"><span class="formLabel"> <bean:write
					name="consulta" property="appointmentDate" /> </span></div>
				</td>
				<td>
				<div align="center"><span class="formLabel"> <a
					href="/oscar/oscar/billing/procedimentoRealizado/init.do?appId=<%= consulta.getAppointmentNo() %>"
					title="Preencher fatura"> <bean:write name="consulta"
					property="appointmentNo" /> </a> </span></div>
				</td>
				<td>
				<div align="left"><span class="formLabel"> <bean:write
					name="consulta" property="provider.name" /> </span></div>
				</td>
				<td>
				<div align="left"><span class="formLabel"> <bean:write
					name="consulta" property="descBilling" /> </span></div>
				</td>
			</tr>
		</logic:iterate>
	</logic:present>
</table>
</body>
<p>&nbsp;</p>
<table border="0" cellspacing="0" cellpadding="0" width="100%"
	align="center">
	<tr>
		<td><a href="#" onclick="history.back();"> <img
			src="../../../images/leftarrow.gif" border="0" width="25" height="20"
			align="absmiddle"><bean:message key="global.btnBack" /></a></td>
		<td align="right"><a href="#" onclick="window.close();"><bean:message
			key="global.btnLogout" /><img src="../../../images/rightarrow.gif"
			border="0" width="25" height="20" align="absmiddle"></a></td>
	</tr>
</table>


</html:html>
</html>
