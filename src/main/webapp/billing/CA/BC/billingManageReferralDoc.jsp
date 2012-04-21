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
<%@ page
	import="java.util.*,oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Billingreferral" %>
<%@page import="org.oscarehr.common.dao.BillingreferralDao" %>
<%
	BillingreferralDao billingReferralDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDAO");
%>
<html:html locale="true">



<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage Referral Docs</title>
<link rel="stylesheet" type="text/css"
	href="../../../oscarEncounter/encounterStyles.css">
<script type="text/javascript">

function isNumeric(strString){
    var validNums = "0123456789.";
    var strChar;
    var retval = true;

    for (i = 0; i < strString.length && retval == true; i++){
       strChar = strString.charAt(i);
       if (validNums.indexOf(strChar) == -1){
          retval = false;
       }
    }
     return retval;
}

function checkUnits(){
	if  (!isNumeric(document.BillingAddCodeForm.value.value)){
		alert("Price has to be a numeric value");
	        document.BillingAddCodeForm.value.focus();
		return false;
	}
	return true;
}



</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF" onLoad="setValues()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">billing</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Manage Referral Billing</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp; &nbsp; <a
			href="billingAddReferralDoc.jsp">Add Doc</a></td>
		<td class="MainTableRightColumn">
		<%
              String limit = request.getParameter("limit");
              String lastname = request.getParameter("lastname");
            %>
		<form action="billingManageReferralDoc.jsp">Last Name: <input
			type="text" name="lastname"
			value="<%= (lastname == null)?"":lastname%>" /> <select name="limit">
			<option value="10" <%=selected(limit,"10")%>>10</option>
			<option value="50" <%=selected(limit,"50")%>>50</option>
			<option value="100" <%=selected(limit,"100")%>>100</option>
		</select> <input type="submit" value="Search" /></form>
		<table class="ele">
			<tr>
				<!--th>id</th-->
				<th>Billing #</th>
				<th>Last Name</th>
				<th>First Name</th>
				<th>Specialty</th>
				<th>Address 1</th>
				<th>Address 2</th>
				<th>City</th>
				<th>Province</th>
				<th>Postal</th>
				<th>Phone</th>
				<th>Fax</th>
			</tr>
			<%
			//ReferralBillingData rbd = new ReferralBillingData();
               if (limit == null) limit = "10";
               if (lastname == null) lastname = "%";
               List<Billingreferral> alist = billingReferralDao.getBillingreferralByLastName(lastname);
               for ( int i =0 ; i < alist.size() ; i++ ){
                  Billingreferral billingReferral = alist.get(i);
            %>
			<tr>
				<!--td><%=billingReferral.getBillingreferralNo()%></td-->
				<td><a
					href="billingAddReferralDoc.jsp?id=<%=billingReferral.getBillingreferralNo()%>"><%=billingReferral.getReferralNo()%></a></td>
				<td><%=billingReferral.getLastName()%></td>
				<td><%=billingReferral.getFirstName()%></td>
				<td><%=billingReferral.getSpecialty()%></td>
				<td><%=billingReferral.getAddress1()%></td>
				<td><%=billingReferral.getAddress2()%></td>
				<td><%=billingReferral.getCity()%></td>
				<td><%=billingReferral.getProvince()%></td>
				<td><%=billingReferral.getPostal()%></td>
				<td><%=billingReferral.getPhone()%></td>
				<td><%=billingReferral.getFax()%></td>
			</tr>


			<%}%>
		</table>

		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
<%!
 String selected(String var,String constant){
     if (var != null && var.equals(constant)){
         return "selected";
     }else{
         return "";
     }
 }
%>
