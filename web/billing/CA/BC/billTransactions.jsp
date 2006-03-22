<%@page import="java.util.*"%>

<%
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
<link href="../../../share/css/oscar.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.ColHead{
	font-weight:bold;
	text-decoration: underline;
	text-align: center;
}
</style>
<table width="100%">
  <tr class="SectionHead">
    <td colspan="2" class="bCellData">Bill Transaction History</td>
  </tr>
  <tr  class="ColHead">
    <td>Bill Status</td>
    <td>Update Date</td>
  </tr>
<%
  for (Iterator iter = billingTransactions.iterator(); iter.hasNext(); ) {
    oscar.entities.BillHistory item = (oscar.entities.BillHistory) iter.next();
%>
  <tr align="center">
    <td><%=rec.getStatusDesc(item.getBillingStatus())%>    </td>
    <td><%=item.getArchiveDate()%>    </td>
  </tr>
<%}%>
</table>
