package oscar.oscarBilling.ca.bc.pageUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

public class BillingPreferencesActionForm
    extends ActionForm {
  private String providerNo;
  private String referral;
  public String getProviderNo() {
    return providerNo;
  }

  public void setProviderNo(String providerNo) {
    this.providerNo = providerNo;
  }

  public void setReferral(String referral) {
    this.referral = referral;
  }

  public String getReferral() {
    return referral;
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
