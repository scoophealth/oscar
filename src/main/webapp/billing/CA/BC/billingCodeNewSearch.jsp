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
<%
  String user_no = (String) session.getAttribute("user");
%>
<%@page import="java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.pageUtil.*"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.BillingService" %>
<%@page import="org.oscarehr.common.dao.BillingServiceDao" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>



<%
	BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class);
%>
<%
  String search = "", search2 = "";
  search = request.getParameter("search");
  if (search.compareTo("") == 0) {
    search = "search_service_code";
  }
  String codeName = "", codeName1 = "", codeName2 = "";
  String xcodeName = "", xcodeName1 = "", xcodeName2 = "";
  codeName = request.getParameter("name");
  codeName1 = request.getParameter("name1");
  codeName2 = request.getParameter("name2");
  xcodeName = request.getParameter("name");
  xcodeName1 = request.getParameter("name1");
  xcodeName2 = request.getParameter("name2");
  String formName = request.getParameter("formName");
  String formElement = request.getParameter("formElement");
  if (formName == null || formElement == null) {
    formName = "";
    formElement = "";
  }
  String desc = "", desc1 = "", desc2 = "";
  if (codeName.compareTo("") == 0 || codeName == null) {
    codeName = " ";
    desc = " ";
  }
  else {
    codeName = codeName + "%";
    desc = "%" + codeName + "%";
  }
  if (codeName1.compareTo("") == 0 || codeName1 == null) {
    codeName1 = " ";
    desc1 = " ";
  }
  else {
    codeName1 = codeName1 + "%";
    desc1 = "%" + codeName1 + "%";
  }
  if (codeName2.compareTo("") == 0 || codeName2 == null) {
    codeName2 = " ";
    desc2 = " ";
  }
  else {
    codeName2 = codeName2 + "%";
    desc2 = "%" + codeName2 + "%";
  }
 
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Service Code Search</title>
<script LANGUAGE="JavaScript">
<!--
function CodeAttach(File0,dx1,dx2,dx3) {
 self.close();
      self.opener.document.BillingCreateBillingForm.xml_other1.value = File0;
      self.opener.document.BillingCreateBillingForm.xml_other2.value ='';
      self.opener.document.BillingCreateBillingForm.xml_other3.value ='';
      self.opener.document.BillingCreateBillingForm.xml_diagnostic_detail1.value =dx1;
      self.opener.document.BillingCreateBillingForm.xml_diagnostic_detail2.value =dx2;
      self.opener.document.BillingCreateBillingForm.xml_diagnostic_detail3.value =dx3;
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0"
	rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP bgcolor="#CCCCFF"><font face="Helvetica"
			color="#000000">Service Code Search</font> <font
			face="Arial, Helvetica, sans-serif" color="#FF0000">(Maximum 3
		selections)</font></th>
	</tr>
</table>
<form name="servicecode" id="servicecode" method="post"
	action="billingCodeNewUpdate.jsp"><input type="hidden"
	name="formName" value="<%=formName%>" /> <input type="hidden"
	name="formElement" value="<%=formElement%>" />
<div style="height: 600; overflow: auto">
<table width="800" border="1">
	<tr bgcolor="#CCCCFF">
		<td><b> <font face="Arial, Helvetica, sans-serif" size="2">Code</font>
		</b></td>
		<td><b> <font face="Arial, Helvetica, sans-serif" size="2">Description</font>
		</b></td>
	</tr>
	<%
  
    String color = "";
    int Count = 0;
    int intCount = 0;
    String numCode = "";
    String textCode = "";
    String searchType = "";
    // Retrieving Provider
    String Dcode = "", DcodeDesc = "";
    String dx1 = "";
    String dx2 = "";
    String dx3 = "";
    
    for(BillingService bs : billingServiceDao.search_service_code(codeName, codeName1, codeName2, desc, desc1, desc2)) {
    
      intCount = intCount + 1;
      Dcode = bs.getServiceCode();
      DcodeDesc = bs.getDescription();
      if (Count == 0) {
        Count = 1;
        color = "#FFFFFF";
      }
      else {
        Count = 0;
        color = "#EEEEFF";
      }
      BillingAssociationPersistence per = new BillingAssociationPersistence();
      ServiceCodeAssociation assoc = per.getServiceCodeAssocByCode(Dcode);
      List dxcodes = assoc.getDxCodes();
      for (int i = 0; i < dxcodes.size(); i++) {
        if (i == 0) {
          dx1 = (String) dxcodes.get(i);
        }
        else if (i == 1) {
          dx2 = (String) dxcodes.get(i);
        }
        else if (i == 2) {
          dx3 = (String) dxcodes.get(i);
        }
      }
  %>
	<tr bgcolor="<%=color%>">
		<td><font face="Arial, Helvetica, sans-serif" size="2"> <%if (Dcode.compareTo(xcodeName) == 0 || Dcode.compareTo(xcodeName1) == 0 || Dcode.compareTo(xcodeName2) == 0) {        %>
		<input type="checkbox" name="code_<%=Dcode%>" checked> <%} else {        %>
		<input type="checkbox" name="code_<%=Dcode%>"> <%}        %> <%=Dcode%>
		</font></td>
		<td><font face="Arial, Helvetica, sans-serif" size="2"> <input
			type="hidden" name="codedesc_<%=Dcode%>" value="<%=DcodeDesc%>">
		<input type="text" name="<%=Dcode%>" value="<%=DcodeDesc%>" size="80">
		<input type="submit" name="update" value="update <%=Dcode%>">
		</font></td>
	</tr>
	<%}  %>
	<%if (intCount == 0) {  %>
	<tr bgcolor="<%=color%>">
		<td colspan="2"><font face="Arial, Helvetica, sans-serif"
			size="2"> No match found. <%// =i        %> </font></td>
	</tr>
	<%}%>
	<%if (intCount == 1) {%>
	<script LANGUAGE="JavaScript">
<!--

 CodeAttach('<%=Dcode%>','<%=dx1%>','<%=dx2%>','<%=dx3%>');
-->

</script>
	<%}  %>
</table>
</div>
<input type="submit" name="update" value="Confirm"> <input
	type="button" name="cancel" value="Cancel"
	onclick="javascript:window.close()"></form>
</body>
</html>
