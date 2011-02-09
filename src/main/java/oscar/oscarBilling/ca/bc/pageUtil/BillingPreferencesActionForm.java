package oscar.oscarBilling.ca.bc.pageUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class BillingPreferencesActionForm
    extends ActionForm {
  private String providerNo;
  private String referral;
  private String payeeProviderNo;
  public String getProviderNo() {

    return providerNo;
  }

  public void setProviderNo(String providerNo) {

    this.providerNo = providerNo;
  }

  public void setReferral(String referral) {
    this.referral = referral;
  }

  public void setPayeeProviderNo(String payeeProviderNo) {
    this.payeeProviderNo = payeeProviderNo;
  }

  public String getReferral() {
    return referral;
  }

  public String getPayeeProviderNo() {
    return payeeProviderNo;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
      /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
  }
}
