

package oscar.oscarBilling.ca.on.administration;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class GstControlForm extends ActionForm{

    String gstPercent;

    
    public String getGstPercent(){
        return gstPercent;
    }
    
    public void setGstPercent(String gstPercent){
        this.gstPercent = gstPercent;
    }

}
