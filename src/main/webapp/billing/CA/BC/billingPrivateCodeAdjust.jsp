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
<%@page
	import="java.util.*,oscar.oscarBilling.ca.bc.data.BillingCodeData,oscar.oscarBilling.ca.bc.pageUtil.*"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Adjust Private Billing Codes</title>
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
				<td>Adjust Private Billing Codes</td>
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
			href="billingAddPrivateCode.jsp?addNew=true">Add Code</a></td>
		<td class="MainTableRightColumn">
		<form action="billingPrivateCodeAdjust.jsp" method="get">
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
				<td></td>
			</tr>
		</table>
		</form>
		<%

      String sortOrder = request.getParameter("sortOrder")!=null?request.getParameter("sortOrder"):"";
        BillingCodeData bcds = new BillingCodeData();
        ArrayList list = (ArrayList) bcds.findBillingCodesByCode("A",sortOrder.equals("desc")?1:0);
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
      %> <% request.setAttribute( "test", list); %> <display:table
			name="test" defaultsort="1" defaultorder="descending"
			decorator="oscar.oscarBilling.ca.bc.pageUtil.BillCodesTableWrapper">
			<display:column property="serviceCode" title="Service Code"
				sortable="true" headerClass="sortable" />
			<display:column property="description" title="Description"
				sortable="true" headerClass="sortable" />
			<display:column property="value" title="Price" sortable="true"
				headerClass="sortable" />
			<display:column property="billingserviceNo" title="Options" />
		</display:table> <%}%>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
