package oscar.oscarBilling.pageUtil;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;


public final class BillingForm extends ActionForm {
    
    
    String requestId;
    String xml_location;
    String xml_provider;
    String xml_visittype;
    
    
    public String getRequestId(){
        return requestId;
    }
    
    public void setRequestId(String id ){
        this.requestId = id;
    }
    
    public void setXml_location(String id ){
        this.xml_location = id;
    }
    
    public String getXml_location(){
        return xml_location;
    }
    
    
    public void setXml_provider(String id ){
        this.xml_provider = id;
    }
    
    public String getXml_provider(){
        return xml_provider;
    }
    
    public void setXml_visittype(String id ){
        this.xml_visittype = id;
    }
    
    public String getXml_visittype(){
        return xml_visittype;
    }
    
}