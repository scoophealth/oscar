package oscar.oscarBilling.ca.bc.pageUtil;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

public final class BillingSaveBillingForm extends ActionForm {


  /**
   * Used to reset everything to a null value
   * @param mapping
   * @param request
   */
  public void reset(ActionMapping mapping, HttpServletRequest request){
  //  this.service = null;
   // this.message = null;
   // this.subject = null;

  }






  /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @return fill in later
     */
  //public ActionErrors validate(ActionMapping mapping,
  //                               HttpServletRequest request) {

  //   ActionErrors errors = new ActionErrors();

  //   if (message == null || message.length() == 0){
  //      errors.add("message", new ActionError("error.message.missing"));
  //   }

  //   if (provider == null || provider.length == 0){
  //      errors.add(ActionErrors.GLOBAL_ERROR,
  //              new ActionError("error.provider.missing"));
  //   }

  //   return errors;

  //}

}//CreateMessageForm