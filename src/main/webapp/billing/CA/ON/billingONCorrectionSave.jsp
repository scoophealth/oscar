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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="errorpage.jsp"
	import="java.util.*,java.math.*,java.net.*,java.sql.*,oscar.util.*,oscar.*"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@page import="org.oscarehr.managers.SecurityInfoManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>


<%
		SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

			String billingNo = request.getParameter("xml_billing_no");
			String error = null;
			
			if(billingNo != null && !billingNo.equals("")) {
			
				//String user_no = (String) session.getAttribute("user");
				BillingCorrectionPrep bObj = new BillingCorrectionPrep();
				List lObj = bObj.getBillingClaimHeaderObj(billingNo);
				BillingClaimHeader1Data ch1Obj = (BillingClaimHeader1Data) lObj.get(0);
				boolean bs = bObj.updateBillingClaimHeader(ch1Obj, request);

				// update item objs
				if(lObj.size() > 1 && !"D".equals(ch1Obj.getStatus())) {
					//lObj.remove(0);
					bs = bObj.updateBillingItem(lObj, request);
				}
			
			} else {
				error = "No xml_billing_no parameter passed. Update failed";
			}
			
		%>
<p>
<%if(error == null) { %>
<h1>Successful Update of a billing Record.</h1>
<%} else { %>
<h1 style="color:red"><%=error %></h1>
<%} %>
</p>

<% if(request.getParameter("submit").equals("Submit&Correct Another")) { %>
<center><input type='button' name='back' 
	value='Correct Another'
	onclick='window.location.href="billingONCorrection.jsp?billing_no="' />
</center>
<%} else {
if(error == null) {	
%>
<script LANGUAGE="JavaScript">
	self.close();
</script>
<% } } %>
