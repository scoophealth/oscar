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

<%
  String curUser_no = (String) session.getAttribute("user");
%>
<%@page	import="java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.pageUtil.*"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.BillingServiceDao" %>
<%@page import="org.oscarehr.common.model.BillingService" %>
<%
	BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Summary</title>
<script LANGUAGE="JavaScript">
<!--
<%
      boolean multipage = false;
      String formName = request.getParameter("formName");
      String formElement = request.getParameter("formElement");
      if ( formName != null && !formName.equals("") && formElement != null && !formElement.equals("") ){
         multipage = true;
      }

      if (multipage){%>
function CodeAttach(File0, File1, File2) {

      self.close();
      self.opener.document.<%=formName%>.<%=formElement%>.value = File0;
}
      <%}else{%>
function CodeAttach(File0, File1, File2,dx1,dx2,dx3) {

      self.close();
      self.opener.document.BillingCreateBillingForm.xml_other1.value = File0;
      self.opener.document.BillingCreateBillingForm.xml_other2.value = File1;
      self.opener.document.BillingCreateBillingForm.xml_other3.value = File2;
      self.opener.document.BillingCreateBillingForm.xml_diagnostic_detail1.value =dx1;
      self.opener.document.BillingCreateBillingForm.xml_diagnostic_detail2.value =dx2;
      self.opener.document.BillingCreateBillingForm.xml_diagnostic_detail3.value =dx3;
}
    <%}%>

-->
</script>
</head>
<body>
<%
  if (request.getParameter("update").equals("Confirm")) {
    String temp = "";
    String[] param = new String[10];
    param[0] = "";
    param[1] = "";
    param[2] = "";
    int Count = 0;
    for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
      temp = e.nextElement().toString();
      if (temp.indexOf("code_") == -1)continue;
      param[Count] = temp.substring(5).toUpperCase(); // + " |" + request.getParameter("codedesc_" + temp.substring(5));
      Count = Count + 1;
    }
    if (Count == 1) {
      param[1] = "";
      param[2] = "";
    }
    if (Count == 2) {
      param[2] = "";
    }
    if (Count == 0) {
%>
<p>No input selected</p>
<input type="button" name="back" value="back"
	onClick="javascript:history.go(-1);return false;">
<%
  } else {
    String[] diags = {"","",""};
    BillingAssociationPersistence per = new BillingAssociationPersistence();
    for (int i = 0; i < param.length; i++) {
      ServiceCodeAssociation assoc = per.getServiceCodeAssocByCode(param[i]);
      List dxcodes = assoc.getDxCodes();
      if(!dxcodes.isEmpty()){
        diags[i]=(String)dxcodes.get(0);
      }
    }
%>
<script LANGUAGE="JavaScript">
    <!--
     CodeAttach('<%=param[0]%>','<%=param[1]%>', '<%=param[2]%>', '<%=diags[0]%>', '<%=diags[1]%>', '<%=diags[2]%>' );
    -->

</script>
<%
  }
  } else {
%>
<%
  String code = request.getParameter("update");
  code = code.substring(code.length() - 5);
  int rowsAffected = 0;
	for( BillingService bs:billingServiceDao.findByServiceCode(code)) {
  		bs.setDescription(request.getParameter(code));
  		billingServiceDao.merge(bs);
  		rowsAffected++;
  	}
%>

<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
    history.go(-1);return false;
    self.opener.refresh();
</script>
<%}%>
</body>
</html>
