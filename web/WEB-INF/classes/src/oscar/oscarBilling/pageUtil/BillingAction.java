package oscar.oscarBilling.pageUtil;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

public final class BillingAction extends Action {
    
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources();
        
        if(request.getSession().getAttribute("user") == null  ){
            return (mapping.findForward("Logout"));
        }
        
        // Setup variables
        ActionErrors errors = new ActionErrors();
        oscar.oscarBilling.pageUtil.BillingSessionBean bean = null;
        if(request.getParameter("billRegion").equals("ON")){                        
            String newURL = mapping.findForward("ON").getPath();
            newURL = newURL + "?"+request.getQueryString();                            
            //return (new ActionForward(newURL));                
            return (mapping.findForward(newURL));                
        }else{
                
            if(request.getParameter("demographic_no")!=null & request.getParameter("appointment_no")!=null) {

                //				 ((BillingForm)form).setXml_location("P1|PEMBERTON");
                bean = new oscar.oscarBilling.pageUtil.BillingSessionBean();

                bean.setApptProviderNo(request.getParameter("apptProvider_no"));
                bean.setPatientName(request.getParameter("demographic_name"));
                bean.setProviderView(request.getParameter("providerview"));
                bean.setBillRegion(request.getParameter("billRegion"));
                bean.setBillForm(request.getParameter("billForm"));
                bean.setCreator(request.getParameter("user_no"));
                bean.setPatientNo(request.getParameter("demographic_no"));
                bean.setApptNo(request.getParameter("appointment_no"));
                bean.setApptDate(request.getParameter("appointment_date"));
                bean.setApptStart(request.getParameter("start_time"));
                bean.setApptStatus(request.getParameter("status"));
                request.getSession().setAttribute("billingSessionBean", bean);

                System.out.println("PatientName is:" + bean.getPatientName());
            }else{
                bean = (oscar.oscarBilling.pageUtil.BillingSessionBean)request.getSession().getAttribute("billingSessionBean");
            }
        
        }
        
        return (mapping.findForward(request.getParameter("billRegion")));
    }
    
}
