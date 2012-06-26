<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page
	import="java.util.*,oscar.oscarBilling.ca.bc.data.BillingCodeData,oscar.oscarBilling.ca.bc.pageUtil.*"%>
<%
String serviceCode = request.getParameter("code")==null?"-1":request.getParameter("code");
BillingCodeData data = new BillingCodeData();
data.deleteBillingCode(serviceCode);
response.sendRedirect("billingPrivateCodeAdjust.jsp");
%>
