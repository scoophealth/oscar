/*
 * MsgDisplayDemographicMessagesAction.java
 *
 * Created on May 8, 2005, 12:20 AM
 */

package oscar.oscarMessenger.pageUtil;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import oscar.oscarMessenger.util.MsgDemoMap;

/**
 *
 * @author root
 */
public class MsgDisplayDemographicMessagesAction extends Action {


    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
    // System.out.println("in display message action jackson");
            // Extract attributes we will need
        
            System.out.println("this is displayDemographicMessages.Action");
            Locale locale = getLocale(request);
            MessageResources messages = getResources(request);

            // Setup variables            
            oscar.oscarMessenger.pageUtil.MsgSessionBean bean = null;
            String[] messageNo = ((MsgDisplayDemographicMessagesForm)form).getMessageNo();
            //String unlinkMsg = ((MsgDisplayDemographicMessagesForm)form).getUnlinkMsg();
            String providerNo;
            
            //Initialize forward location
            String findForward = "success";

            if(request.getParameter("providerNo")!=null & request.getParameter("userName")!=null)
            {
                // System.out.println("in display message action jackson4");
                bean = new oscar.oscarMessenger.pageUtil.MsgSessionBean();
                bean.setProviderNo(request.getParameter("providerNo"));
                bean.setUserName(request.getParameter("userName"));
                bean.setDemographic_no(request.getParameter("demographic_no"));
                
                //System.out.println(bean.getDemographic_no());
                request.getSession().setAttribute("msgSessionBean", bean);
                                
            }//if
            else
            {
                bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
            }//else
            
            
            //Unlinked selected messages
            if(messageNo!=null){              
                MsgDemoMap msgDemoMap = new MsgDemoMap();                                              
                for (int i =0 ; i < messageNo.length ; i++){
                    msgDemoMap.unlinkMsg(request.getParameter("demographic_no"), messageNo[i]);                            
                }
                                 
                //Forward to DisplayDemographiMessage.jsp
                findForward = "demoMsg";                
            }
            
    return (mapping.findForward(findForward));
    }
}