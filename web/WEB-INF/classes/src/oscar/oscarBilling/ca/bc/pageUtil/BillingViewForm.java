package oscar.oscarBilling.ca.bc.pageUtil;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;


public final class BillingViewForm extends ActionForm {
    
    
    String requestId;
    
    public String getRequestId(){
        return requestId;
    }
    
    public void setRequestId(String id ){
        this.requestId = id;
    }
    
    
}