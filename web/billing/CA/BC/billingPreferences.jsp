
<!--
  /*
  *
  * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
  * This software is published under the GPL GNU General Public License.
  * This program is free software; you can redistribute it and/or
  * modify it under the terms of the GNU General Public License
  * as published by the Free Software Foundation; either version 2
  * of the License, or (at your option) any later version. *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
  * along with this program; if not, write to the Free Software
  * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
  *
  * <OSCAR TEAM>
  *
  * This software was written for the
  * Department of Family Medicine
  * McMaster Unviersity
  * Hamilton
  * Ontario, Canada
  */
-->
<%@page language="java"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="java.util.*,oscar.util.*"%>
<%
oscar.entities.Provider prov = new oscar.entities.Provider();
prov.setProviderNo("0");
List lst = (List)request.getAttribute("providerList");

lst.add(prov);
%>
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<link rel="stylesheet" type="text/css"
	href="../../../oscarEncounter/encounterStyles.css">
<title></title>
</head>
<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1">
	<html:form action="/billing/CA/BC/saveBillingPreferencesAction.do"
		method="POST">
		<html:hidden property="providerNo" />
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn">Billing Preferences</td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td style="text-align: right"><a
						href="javascript:popupStart(300,400,'Help.jsp')"> <bean:message
						key="global.help" /> </a> | <a
						href="javascript:popupStart(300,400,'About.jsp')"> <bean:message
						key="global.about" /> </a> | <a
						href="javascript:popupStart(300,400,'License.jsp')"> <bean:message
						key="global.license" /> </a></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn">&nbsp;</td>
			<td class="MainTableRightColumn"><br>
			<label for="referral"> Select Default Referral Type: <html:select
				styleId="referral" property="referral">
				<html:option value="1">Refer To</html:option>
				<html:option value="2">Refer By</html:option>
				<html:option value="3">Neither</html:option>
			</html:select> </label>
		</tr>
		<tr>
			<td class="MainTableLeftColumn">&nbsp;</td>
			<td class="MainTableRightColumn"><label for="payeeProviderNo">Select
			Default Payee: <html:select property="payeeProviderNo">
				<html:options collection="providerList" property="providerNo"
					labelProperty="fullName" />
			</html:select> </label></td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn"></td>
			<td class="MainTableBottomRowRightColumn"><html:submit
				property="submit" value="Save" /></td>
		</tr>
	</html:form>
</table>
</body>
</html:html>
