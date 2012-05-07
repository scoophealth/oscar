/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarTickler.pageUtil;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.caisi.dao.TicklerDAO;
import org.caisi.model.Tickler;
import org.caisi.model.TicklerComment;
import org.caisi.model.TicklerUpdate;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.common.dao.TicklerTextSuggestDao;
import org.oscarehr.common.model.TicklerTextSuggest;
import org.apache.commons.mail.EmailException;
import java.io.IOException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.caisi.service.TicklerManager;
import oscar.util.DateUtils;
import oscar.OscarProperties;

public class EditTicklerAction extends DispatchAction{
    
    private static final Logger logger=MiscUtils.getLogger();
    
    public ActionForward editTickler(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        
        ActionMessages errors = this.getErrors(request);
        DynaValidatorForm editForm = (DynaValidatorForm)form;
        
        String ticklerNoStr = request.getParameter("ticklerNo");
        Long ticklerNo = Long.parseLong(ticklerNoStr);
                
        String status = request.getParameter("status"); 
        String priority = request.getParameter("priority");
        String assignedTo = request.getParameter("assignedToProviders");
        String serviceDate = request.getParameter("xml_appointment_date");
        
        if ((ticklerNo == null)
         || (status == null)
         || (priority == null)
         || (serviceDate == null)
         || (assignedTo == null)) 
        {            
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("tickler.ticklerEdit.arg.error"));
            saveErrors(request,errors);
            return mapping.findForward("failure");
        }
        
        TicklerDAO ticklerDao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
        Tickler t = ticklerDao.getTickler(ticklerNo);
        
        if (t == null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("tickler.ticklerEdit.arg.error"));
            saveErrors(request,errors);
            return mapping.findForward("failure");
        }
        
        Date now = new Date();
        
        boolean emailFailed = false;
        boolean isComment = false;        

        boolean enabledTicklerEmail = Boolean.parseBoolean(OscarProperties.getInstance().getProperty("tickler_email_enabled"));
        if (enabledTicklerEmail) {
            boolean  emailDemographic = (Boolean) editForm.get("emailDemographic");

            if (emailDemographic) {

                TicklerManager ticklerMgr = (TicklerManager)SpringUtils.getBean("ticklerManagerT");

                try {
                    ticklerMgr.sendNotification(t);     

                    //add tickler comment noting patient was emailed
                    TicklerComment tc = new TicklerComment();
                    ResourceBundle prop = ResourceBundle.getBundle("oscarResources",request.getLocale());
                    String emailedMsg = "";
                    try {
                        emailedMsg = prop.getString("tickler.ticklerEdit.emailedDemographic");
                    }
                    catch (Exception e) {
                        emailedMsg = "Emailed Demographic";
                    }
                    tc.setMessage(emailedMsg);
                    tc.setTickler_no(ticklerNo);
                    tc.setUpdate_date(now);
                    tc.setProviderNo(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());

                    t.getComments().add(tc);
                    isComment = true;

                }catch (EmailException e) {
                    logger.error("Tickler Email cannot be sent",e);   
                    emailFailed = true;                           
                }catch (IOException e) {
                    logger.error("Tickler Email Template probably can't be found",e);  
                    emailFailed = true;
                }
                catch (Exception e) {
                    logger.error("Unexpected error. Check your tickler email, and general email configuration",e);  
                    emailFailed = true;
                }                                   
            }
        }
        
        String newMessage = request.getParameter("newMessage");
        
        /*
         * Create a new TicklerComment
         */
        if (newMessage != null && !newMessage.isEmpty()) {

            TicklerComment tc = new TicklerComment();
            tc.setMessage(newMessage);
            tc.setTickler_no(ticklerNo);
            tc.setUpdate_date(now);
            tc.setProviderNo(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());

            t.getComments().add(tc);
            isComment = true;
        }

        /*
         * Create a new TicklerUpdate                      
         */
        //back fill the original state of the tickler so we don't lose it  
        TicklerUpdate tuOriginal = new TicklerUpdate();

        if (t.getUpdates().isEmpty()) {                                                              
            tuOriginal.setTickler_no(t.getTickler_no());
            tuOriginal.setProviderNo(t.getCreator());
            tuOriginal.setUpdate_date(t.getUpdate_date());

            tuOriginal.setStatus(t.getStatus());
            tuOriginal.setPriority(t.getPriority());
            tuOriginal.setAssignedTo(t.getTask_assigned_to());                                
            tuOriginal.setServiceDate(t.getService_date()); 
            
            t.getUpdates().add(tuOriginal);
        }

        TicklerUpdate tu = new TicklerUpdate();
        tu.setTickler_no(t.getTickler_no());
        tu.setUpdate_date(now);            
        tu.setProviderNo(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());

        boolean isUpdate = false;                        

        if (!status.equals(String.valueOf(t.getStatus()))){
            tu.setStatus(status.charAt(0));
            t.setStatus(status.charAt(0));
            isUpdate = true;            
        }

        if (!priority.equals(t.getPriority())) {
            tu.setPriority(priority);
            t.setPriority(priority);
            isUpdate = true;
        }


        if (!assignedTo.equals(t.getTask_assigned_to())){                
            tu.setAssignedTo(assignedTo);
            t.setTask_assigned_to(assignedTo);
            isUpdate = true;
        }

        if (!serviceDate.equals(t.getServiceDate())){
           try {
               Date serviceDateAsDate = DateUtils.parseDate(serviceDate, request.getLocale());
               tu.setServiceDate(serviceDateAsDate);
               t.setServiceDate(serviceDate);
               isUpdate = true;
           }
           catch (java.text.ParseException e) {
               logger.error("Service Date cannot be parsed:",e);
               return mapping.findForward("error"); 
           }                
        }

        if (isUpdate){            
            t.getUpdates().add(tu);
        }

        if (isComment || isUpdate) {            
            ticklerDao.saveTickler(t);
        }                                    
                
        if (emailFailed) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("tickler.ticklerEdit.emailFailed.error"));
            saveErrors(request,errors);
            return mapping.findForward("failure");
        } 
            
        return mapping.findForward("close");
        
    }
    
     public ActionForward updateTextSuggest(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
         
         ActionMessages errors = this.getErrors(request);
         DynaValidatorForm tsForm = (DynaValidatorForm)form;
         
         String[] activeText = (String[])tsForm.get("activeText");  
         if(activeText == null){
             errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("tickler.ticklerEdit.arg.error"));
             saveErrors(request,errors);
             return mapping.findForward("failure");
         }
         
         String[] inactiveText = (String[])tsForm.get("inactiveText");   
         if(inactiveText == null){
             errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("tickler.ticklerEdit.arg.error"));
             saveErrors(request,errors);
             return mapping.findForward("failure");
         }
         
         TicklerTextSuggestDao ticklerTextSuggestDao = (TicklerTextSuggestDao) SpringUtils.getBean("ticklerTextSuggestDao");
         
         
         for (String activeTextStr : activeText) {
             Integer textSuggestId = null;
             try {
                 textSuggestId = Integer.parseInt(activeTextStr);
             } catch (NumberFormatException e) {
                 //probably a new text suggestion then
             }
             
             TicklerTextSuggest ts = null;
             if (textSuggestId != null) {
                 ts = ticklerTextSuggestDao.find(textSuggestId);           
             }
             if (ts == null) {
                 ts = new TicklerTextSuggest();
                 ts.setActive(true);
                 ts.setCreateDate(new Date());
                 ts.setCreator(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
                 ts.setSuggestedText(activeTextStr);
                 ticklerTextSuggestDao.persist(ts);
             } else {
                 ts.setActive(true);        
                 ticklerTextSuggestDao.merge(ts);
             }            
         }
         
         
         for (String inactiveTextStr : inactiveText) {
             Integer textSuggestId = null;
             try {
                 textSuggestId = Integer.parseInt(inactiveTextStr);
             } catch (NumberFormatException e) {
                 //probably a new text suggestion then
             }
             
             TicklerTextSuggest ts = null;
             if (textSuggestId != null) {
                 ts = ticklerTextSuggestDao.find(textSuggestId);           
             }
             if (ts == null) {
                 ts = new TicklerTextSuggest();
                 ts.setActive(false);
                 ts.setCreateDate(new Date());
                 ts.setCreator(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
                 ts.setSuggestedText(inactiveTextStr); 
                 ticklerTextSuggestDao.persist(ts);
             } else {
                 ts.setActive(false);
                 ticklerTextSuggestDao.merge(ts);
             }
             
         }

         return mapping.findForward("close");
     }
}
