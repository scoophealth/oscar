/*
 * BillingUpdateBillingAction.java
 *
 * Created on August 30, 2004, 1:52 PM
 */

package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarBilling.ca.bc.MSP.*;
import oscar.oscarBilling.ca.bc.data.*;

/**
 *
 * @author  root
 */
public final class BillingUpdateBillingAction extends Action {
    
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
       
       String creator = (String) request.getSession().getAttribute("user");
       
       BillingUpdateBillingForm frm = (BillingUpdateBillingForm) form;              
       
       String messageNotes = frm.getMessageNotes();
       String status       = frm.getStatus();
       String billingId    = frm.getBillingNo();
       
       MSPReconcile msprec = new MSPReconcile();
       msprec.updateBillingStatus(billingId,status);
              
       BillingNote n = new BillingNote();
       try{
          n.addNoteFromBillingNo(billingId,creator,messageNotes);
       }catch(Exception e){          
          e.printStackTrace();
       }          
       return mapping.findForward("success");       
    }
   
   /** Creates a new instance of BillingUpdateBillingAction */
   public BillingUpdateBillingAction() {
   }
   
}
