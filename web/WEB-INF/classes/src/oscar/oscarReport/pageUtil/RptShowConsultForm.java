package oscar.oscarReport.pageUtil;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class RptShowConsultForm extends ActionForm {
    
    String requestId;
    
    public String getRequestId(){
       return requestId;
    }
    
    public void setRequestId(String id ){
       this.requestId = id;
    }
}
