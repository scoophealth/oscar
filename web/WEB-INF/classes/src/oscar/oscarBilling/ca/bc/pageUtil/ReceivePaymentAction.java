package oscar.oscarBilling.ca.bc.pageUtil;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.Action;
import oscar.oscarBilling.ca.bc.data.PrivateBillTransactionsDAO;

public class ReceivePaymentAction
    extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
    ReceivePaymentActionForm frm = (
        ReceivePaymentActionForm) actionForm;
    int billingmasterNo = new Integer(frm.getBillingmasterNo()).intValue();
    double amount = new Double(frm.getAmountReceived()).doubleValue();
    if("true".equals(frm.getIsRefund())){
    frm.setAmountReceived(String.valueOf(amount*-1.0));
    }
    this.receivePayment(billingmasterNo,amount);
    frm.setPaymentReceived(true);
    return actionMapping.findForward("success");
  }

  public void receivePayment(int billingMasterNo, double amount) {

   PrivateBillTransactionsDAO dao = new PrivateBillTransactionsDAO();
   dao.savePrivateBillTransaction(billingMasterNo,amount);
 }

}
