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

<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page	import="java.util.*,oscar.oscarBilling.ca.bc.data.BillingCodeData,oscar.oscarBilling.ca.bc.pageUtil.*"%>
<%@ page import="org.oscarehr.common.dao.BillingServiceDao,org.oscarehr.util.SpringUtils,org.oscarehr.common.model.*" %>
<%BillingServiceDao billingServiceDao = (BillingServiceDao) SpringUtils.getBean("billingServiceDao"); %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Adjust Billing Codes</title>
<link rel="stylesheet" type="text/css"
	href="../../../oscarEncounter/encounterStyles.css">
<script type="text/javascript">





</script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body class="BodyStyle" vlink="#0000FF" onLoad="setValues()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td width="10%" class="MainTableTopRowLeftColumn">Billing</td>
		<td width="88%" class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Adjust Billing Codes</td>
				<td>&nbsp;</td>
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
		<td class="MainTableLeftColumn" valign="top">&nbsp; <a
			href="billingAddCode.jsp?addNew=true">Add Code</a></td>
		<td class="MainTableRightColumn">
		<form action="billingCodeAdjust.jsp" method="get">
		<%if (request.getAttribute("returnMessage") != null) {      %>
		<table>
			<tr>
				<td style="font-color: red;"><%=request.getAttribute("returnMessage")%>
				</td>
			</tr>
		</table>
		<%}      %>
		<table>
			<tr>
				<td>Billing Code <input type="text" name="bCode" maxlength="5" />
				<input type="submit" name="Search" /></td>
			</tr>
		</table>
		</form>
		<%

      String sortOrder = request.getParameter("sortOrder")!=null?request.getParameter("sortOrder"):"";

        String bCode = request.getParameter("bCode");
        List list = null;
        if (bCode != null){
           list = (List) billingServiceDao.findBillingCodesByCode(bCode,billingServiceDao.BC,sortOrder.equals("desc")?1:0);
        }
        if (list != null) {
          String arrow = "";
          String newOrder =  "";
          if(sortOrder.equals("desc")){
            newOrder = "";
            arrow = "&uarr;";
          }
          else if(sortOrder.equals("")){
            newOrder = "desc";
             arrow = "&darr;";
          }
      %>
		<table border=1 width="80%">
			<tr>
				<th>Service Code<a
					href="billingCodeAdjust.jsp?sortOrder=<%=newOrder%>"><%=arrow%></a></th>
				<th>Description</th>
				<th>Price</th>
				<th>Options</th>
			</tr>
			<%
          for (int i = 0; i < list.size(); i++) {
            org.oscarehr.common.model.BillingService bcd = (org.oscarehr.common.model.BillingService) list.get(i);
        %>
			<tr align="center">
				<td><strong><%=bcd.getServiceCode()%> </strong></td>
				<td><%=bcd.getDescription()%></td>
				<td><%=bcd.getValue()%></td>
				<td><a
					href="billingEditCode.jsp?codeId=<%=bcd.getBillingserviceNo()%>&code=<%=bcd.getServiceCode()%>&desc=<%=bcd.getDescription()%>&value=<%=bcd.getValue()%>">Edit</a>
				<br>
				<a href="deletePrivateCode.jsp?code=<%=bcd.getBillingserviceNo()%>">Delete</a></td>
			</tr>
			<%}        %>
		</table>
		<%}      %>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
