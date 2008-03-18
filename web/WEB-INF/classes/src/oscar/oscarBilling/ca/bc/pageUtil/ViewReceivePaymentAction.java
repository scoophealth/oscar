package oscar.oscarBilling.ca.bc.pageUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.entities.PaymentType;

public class ViewReceivePaymentAction
    extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {
    ReceivePaymentActionForm frm = (
        ReceivePaymentActionForm) actionForm;
    BillingViewBean bean = new BillingViewBean();
    String billingMasterNo = request.getParameter("lineNo");
    String billNo = request.getParameter("billNo");
    List paymentTypes = bean.getPaymentTypes();
    for (int i = 0; i < paymentTypes.size(); i++) {
      PaymentType tp = (PaymentType) paymentTypes.get(i);
      if ("ELECTRONIC".equals(tp.getPaymentType())) {
        paymentTypes.remove(i);
      }
    }
    frm.setPaymentMethodList(paymentTypes);
    frm.setBillingmasterNo(billingMasterNo);
    frm.setBillNo(billNo);
    return actionMapping.findForward("success");
  }
}
