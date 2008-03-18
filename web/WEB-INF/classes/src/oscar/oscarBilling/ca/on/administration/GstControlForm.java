

package oscar.oscarBilling.ca.on.administration;

import org.apache.struts.action.ActionForm;

public final class GstControlForm extends ActionForm{

    String gstPercent;

    
    public String getGstPercent(){
        return gstPercent;
    }
    
    public void setGstPercent(String gstPercent){
        this.gstPercent = gstPercent;
    }

}
