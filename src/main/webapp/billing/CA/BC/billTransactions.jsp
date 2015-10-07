<%@page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  java.text.NumberFormat nf = java.text.NumberFormat.getCurrencyInstance();
  String billNo = request.getParameter("billNo");
  String billMasterNo = request.getParameter("billMasterNo");
  oscar.oscarBilling.ca.bc.MSP.MSPReconcile rec= new oscar.oscarBilling.ca.bc.MSP.MSPReconcile();
  oscar.oscarBilling.ca.bc.data.BillingHistoryDAO dao = new oscar.oscarBilling.ca.bc.data.BillingHistoryDAO();
  List billingTransactions = new ArrayList();
  if(billNo!=null){
  	 billingTransactions = dao.getBillHistoryByBillNo(billNo);
  }
  else if(billMasterNo!=null){
    billingTransactions = dao.getBillHistory(billMasterNo);
  }
%>
<link href="../../../share/css/oscar.css" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<table width="100%">
	<tr class="SectionHead">
		<td colspan="8" class="bCellData">Bill Transaction History</td>
	</tr>
	<tr class="ColHead">
		<td>STAT</td>
		<td>SEQ #</td>
		<td>INS</td>
		<td>PRACT</td>
		<td>BILL AMT</td>
		<td>TYPE</td>
		<td>AMT ADJ.</td>
		<td>UPDATED</td>
	</tr>
	<%
  for (Iterator iter = billingTransactions.iterator(); iter.hasNext(); ) {
    oscar.entities.BillHistory item = (oscar.entities.BillHistory) iter.next();
    oscar.entities.Provider provider= rec.getProvider(item.getPractitioner_no(), 0);
%>
	<tr align="center">
		<td><%=rec.getStatusDesc(item.getBillingStatus())%></td>
		<td><%=item.getSeqNum()%></td>
		<td><%=item.getBillingtype()%></td>
		<td><%=provider.getInitials()%></td>
		<%
    //for display purposes need to negate value so that
    //a postive internal adjustment appears as a "debit"
    if("10".equals(item.getPaymentTypeId())){
      item.setAmountReceived(item.getAmountReceived()*-1);
    }
    %>
		<td><%=nf.format(item.getAmount())%></td>
		<td><%=item.getPaymentTypeDesc()%></td>
		<td><%=nf.format(item.getAmountReceived())%></td>
		<td><%=item.getArchiveDate()%></td>
	</tr>
	<%}%>
</table>
