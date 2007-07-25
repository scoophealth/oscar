

package oscar.oscarBilling.ca.on.administration;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class GstControlForm extends ActionForm{
    
    String gstFlag;
    String gstPercent;
    
    public String getGstFlag(){
        return gstFlag;
    }
       
    public void setGstFlag(String gstFlag){
        this.gstFlag = gstFlag;
    }
    
    public String getGstPercent(){
        return gstPercent;
    }
    
    public void setGstPercent(String gstPercent){
        this.gstPercent = gstPercent;
    }

}
