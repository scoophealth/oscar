package oscar.oscarBilling.ca.bc.pageUtil;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import oscar.oscarBilling.ca.bc.data.*;
import java.util.*;
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
