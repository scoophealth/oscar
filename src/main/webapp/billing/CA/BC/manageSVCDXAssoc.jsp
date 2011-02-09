
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
  * McMaster University
  * Hamilton
  * Ontario, Canada
  */
-->
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page
	import="java.util.*,oscar.oscarBilling.ca.bc.data.BillingCodeData,oscar.oscarBilling.ca.bc.pageUtil.*"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscar.billing.CA.BC.billingBC.manageSVCDXAssoc.title" /></title>
<link rel="stylesheet" type="text/css"
	href="../../../oscarEncounter/encounterStyles.css">
<script type="text/javascript">





</script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body class="BodyStyle" vlink="#0000FF">
<h2><html:errors /></h2>
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr>
		<td class="MainTableRightColumn">
		<table width="100%" border="1" cellspacing="0" cellpadding="0">
			<tr class="TopStatusBar">
				<td>
				<h3><bean:message
					key="oscar.billing.CA.BC.billingBC.manageSVCDXAssoc.title" /></h3>
				</td>
				<td style="text-align: right" colspan="2"><a
					href="javascript:popupStart(300,400,'Help.jsp')"> <bean:message
					key="global.help" /> </a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"> <bean:message
					key="global.about" /> </a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"> <bean:message
					key="global.license" /> </a></td>
			</tr>
			<tr bgcolor="CCCCFF">
				<th><bean:message
					key="oscar.billing.CA.BC.billingBC.manageSVCDXAssoc.svc" /></th>
				<th><bean:message
					key="oscar.billing.CA.BC.billingBC.manageSVCDXAssoc.dx" /></th>
				<th><bean:message
					key="oscar.billing.CA.BC.billingBC.manageSVCDXAssoc.options" /></th>
			</tr>
			<%
          ArrayList lst = (ArrayList) request.getAttribute("assocs");
          for (int i = 0; i < lst.size(); i++) {
            ServiceCodeAssociation assoc = (ServiceCodeAssociation) lst.get(i);
        %>
			<tr align="center" class="assocRow"
				bgcolor="<%=i%2==0?"FFFFFF":"EEEEFF"%>">
				<td><%=assoc.getServiceCode()%></td>
				<td>
				<%
              List dxcodes = assoc.getDxCodes();
              for (int j = 0; j < dxcodes.size(); j++) {
                String s = (String) dxcodes.get(j);
                out.print(s + "<br>");
              }
            %>
				</td>
				<td><a
					href="editServiceCodeAssocAction.do?svcCode=<%=assoc.getServiceCode()%>">Edit</a>
				<br>
				<a
					href="deleteServiceCodeAssoc.do?svcCode=<%=assoc.getServiceCode()%>">Remove</a>
				</td>
			</tr>
			<%}        %>
			<tr bgcolor="CCCCFF">
				<td colspan="3">
				<div align="center"><a href="dxcode_svccode_assoc.jsp">Create
				New Association</a></div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
