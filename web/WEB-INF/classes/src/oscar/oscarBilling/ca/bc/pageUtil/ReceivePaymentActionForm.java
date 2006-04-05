package oscar.oscarBilling.ca.bc.pageUtil;

import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import oscar.util.StringUtils;

public class ReceivePaymentActionForm
    extends ActionForm {
  private String amountReceived;
  private String payment;
  private String paymentMethod;
  private List paymentMethodList;
  private String billingmasterNo;
  private String billNo;
  private boolean paymentReceived;
  private String isRefund;
  public String getAmountReceived() {
    return amountReceived;
  }

  public void setAmountReceived(String amountReceived) {
    this.amountReceived = amountReceived;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public void setPayment(String payment) {
    this.payment = payment;
  }

  public void setPaymentMethodList(List paymentMethodList) {
    this.paymentMethodList = paymentMethodList;
  }

  public void setBillingmasterNo(String billingmasterNo) {
    this.billingmasterNo = billingmasterNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }

  public void setPaymentReceived(boolean paymentReceived) {
    this.paymentReceived = paymentReceived;
  }

  public void setIsRefund(String isRefund) {

    this.isRefund = isRefund;
  }

  public String getPayment() {
    return payment;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public List getPaymentMethodList() {
    return paymentMethodList;
  }

  public String getBillingmasterNo() {
    return billingmasterNo;
  }

  public String getBillNo() {
    return billNo;
  }

  public boolean isPaymentReceived() {
    return paymentReceived;
  }

  public String getIsRefund() {
    return isRefund;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    ActionErrors errors = new ActionErrors();
    if (!StringUtils.isNumeric(this.getAmountReceived())) {
      errors.add("",
                 new ActionMessage(
                     "oscar.billing.CA.BC.billingBC.receivePayment.error.amtReceived"));
    }
    return errors;
  }
}
