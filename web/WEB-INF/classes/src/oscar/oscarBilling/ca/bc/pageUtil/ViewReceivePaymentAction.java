package oscar.oscarBilling.ca.bc.pageUtil;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import oscar.oscarBilling.ca.bc.data.*;

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
    frm.setPaymentMethodList(bean.getPaymentTypes());
    frm.setBillingmasterNo(billingMasterNo);
    frm.setBillNo(billNo);
    return actionMapping.findForward("success");
  }
}
