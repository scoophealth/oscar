/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * PHRMessageAction.java
 *
 * Created on June 4, 2007, 4:51 PM
 *
 */

package org.oscarehr.phr.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRMessage;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarProvider.data.ProviderMyOscarIdData;


/**
 *
 * @author jay
 */
public class PHRMessageAction extends DispatchAction {  
    
    private static Logger log = MiscUtils.getLogger();
    
    PHRDocumentDAO phrDocumentDAO;
    PHRActionDAO phrActionDAO;
    PHRService phrService;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       return super.execute(mapping, form, request, response);
    }
    
    /** Creates a new instance of PHRMessageAction */
    public PHRMessageAction() {
    }
    
    public void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO) {
        this.phrDocumentDAO = phrDocumentDAO;
    }
    
    public void setPhrActionDAO(PHRActionDAO phrActionDAO) {
        this.phrActionDAO = phrActionDAO;
    }
    
    public void setPhrService(PHRService phrService) {
      this.phrService = phrService;
    }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           return viewMessages(mapping,form,request,response);
    }
    
    public ActionForward viewMessages(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        clearSessionVariables(request);
        String providerNo = (String) request.getSession().getAttribute("user");
        List docs = phrDocumentDAO.getDocumentsReceived(PHRConstants.DOCTYPE_MESSAGE(), providerNo);
        ArrayList<PHRMessage> messages = null;
        List<PHRAction> actionsPendingApproval = phrActionDAO.getActionsByStatus(PHRAction.STATUS_APPROVAL_PENDING, providerNo);
        if(docs != null) {
            messages = new ArrayList(docs.size());            
            for( int idx = 0; idx < docs.size(); ++idx ) {
                PHRDocument doc = (PHRDocument)docs.get(idx);
                PHRMessage msg = new PHRMessage(doc);
                messages.add(msg);
            }
        }
        request.getSession().setAttribute("indivoMessages", docs);
        request.getSession().setAttribute("indivoMessageBodies", messages);

        if (actionsPendingApproval != null) {
            request.getSession().setAttribute("actionsPendingApproval", actionsPendingApproval);
        }
            
        return mapping.findForward("view");
    }
    
    public ActionForward viewSentMessages(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        clearSessionVariables(request);
        String providerNo = (String) request.getSession().getAttribute("user");
        List docs = phrDocumentDAO.getDocumentsSent(PHRConstants.DOCTYPE_MESSAGE(), providerNo);
        
        List messageActions = phrActionDAO.getPendingActionsByProvider(PHRConstants.DOCTYPE_MESSAGE(), PHRAction.ACTION_ADD, providerNo);
        List otherActions = phrActionDAO.getPendingActionsByProvider(PHRConstants.DOCTYPE_MEDICATION(), -1, providerNo);
        otherActions.addAll(phrActionDAO.getPendingActionsByProvider(PHRConstants.DOCTYPE_BINARYDATA(), -1, providerNo));
        ArrayList<Integer> statusList = new ArrayList();
        statusList.add(PHRAction.STATUS_ON_HOLD);
        statusList.add(PHRAction.STATUS_NOT_AUTHORIZED);
        statusList.add(PHRAction.STATUS_SEND_PENDING);
        statusList.add(PHRAction.STATUS_OTHER_ERROR);
        otherActions.addAll(phrActionDAO.getActionsByStatus(statusList, providerNo, PHRConstants.DOCTYPE_ACCESSPOLICIES()));
        request.getSession().setAttribute("indivoSentMessages", docs);
        request.getSession().setAttribute("indivoMessageActions", messageActions);
        request.getSession().setAttribute("indivoOtherActions", otherActions);
        
        return mapping.findForward("view");
    }
    
    public ActionForward viewArchivedMessages(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        clearSessionVariables(request);
        String providerNo = (String) request.getSession().getAttribute("user");
        List docs = phrDocumentDAO.getDocumentsArchived(PHRConstants.DOCTYPE_MESSAGE(), providerNo);
        request.getSession().setAttribute("indivoArchivedMessages", docs);
        
        return mapping.findForward("view");
    }
    
    public ActionForward read(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        
        String id = request.getParameter("id");
        String source = request.getParameter("source");
        if (source == null) source = "";
        PHRMessage msg = new PHRMessage();
        if ((source != null) && source.equals("actions")) {
            PHRAction phrAction = phrActionDAO.getActionById(id);
            if (phrAction == null) {
                return mapping.findForward("document_not_found");
            }
            msg = phrAction.getPhrMessage();
        } else {
            PHRDocument doc = phrDocumentDAO.getDocumentById(id);
            if (doc == null) {
                return mapping.findForward("document_not_found");
            }
            msg = new PHRMessage(doc);
        }
        if (!msg.isRead() && !source.equals("actions")) {
            msg.setRead();
            msg.reDocContent();
            phrDocumentDAO.update((PHRDocument) msg);

            phrService.sendUpdateMessage(msg);
        }
        
        log.debug("ID FOR DOC BEING READ IS "+msg.getId());
        request.setAttribute("noreply", request.getParameter("noreply"));
        request.setAttribute("comingfrom", request.getParameter("comingfrom"));
        request.setAttribute("message",msg);
        //List refList = phrDocumentDAO.getReferencedMessages(doc);
        //request.setAttribute("refList",refList);
        
        return mapping.findForward("read");
    }
    
    //
    // Reply is a create but displays the message being re 
    // 
    public ActionForward reply(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        
        String id = request.getParameter("id");
        
        log.debug(id);
        PHRDocument doc = phrDocumentDAO.getDocumentById(id);
        
        if (doc == null){
            
            log.debug("DOC WAS NULL");
            //FORWARD TO PAGE SAYING DOCUMENT NOT FOUND ON SYSTEM
        }       
        request.setAttribute("message",new PHRMessage(doc));
        String msgRe = doc.getDocSubject();
        if (msgRe != null && !msgRe.startsWith("Re:")){
            msgRe = "Re: "+msgRe;
        }
        request.setAttribute("subject",msgRe); //TODO: check to see if the string already starts with re:
        //List refList = phrDocumentDAO.getReferencedMessages(doc);
        //request.setAttribute("refList",refList);
        
        String toName ="";
        String toId   = "";
        String toType ="";
        
        if ( doc.getSenderType() == PHRDocument.TYPE_DEMOGRAPHIC){
            
           toId   =  doc.getSenderOscar();
           DemographicData dd = new DemographicData();
           DemographicData.Demographic d = dd.getDemographic(toId);
           toName = d.getFirstName()+ " "+d.getLastName();
           toType = ""+doc.getSenderType();
        
        }
        
        String provNo = (String) request.getSession().getAttribute("user");
        ProviderData pp = new ProviderData();
        String providerName = pp.getProviderName(provNo);
        
        String fromName = providerName;
        String fromId   = provNo;
        String fromType = ""+PHRDocument.TYPE_PROVIDER;   //ONLY PROVIDERS WILL BE SENDING FROM OSCAR
        
        request.setAttribute("toName",toName);
        request.setAttribute("toId",toId);
        request.setAttribute("toType",toType);
     
        request.setAttribute("fromName",fromName);
        request.setAttribute("fromId",fromId);
        request.setAttribute("fromType",fromType);
        
        
        return mapping.findForward("create");
    }
    
    public ActionForward createMessage(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        //PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        String demographicNo = request.getParameter("demographicNo");
        String provNo = (String) request.getSession().getAttribute("user");
        DemographicData dd = new DemographicData();
        DemographicData.Demographic d = dd.getDemographic(demographicNo);
        ProviderData pp = new ProviderData();
        String providerName = pp.getProviderName(provNo);
        
        String toName = d.getFirstName()+ " "+d.getLastName();
        String toId   = demographicNo;
        String toType = ""+PHRDocument.TYPE_DEMOGRAPHIC;
        
        String fromName = providerName;
        String fromId   = provNo;
        String fromType = ""+PHRDocument.TYPE_PROVIDER;
        
        request.setAttribute("toName",toName);
        request.setAttribute("toId",toId);
        request.setAttribute("toType",toType);
     
        request.setAttribute("fromName",fromName);
        request.setAttribute("fromId",fromId);
        request.setAttribute("fromType",fromType);
        
        return mapping.findForward("create");
    }
    
    public ActionForward send(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        String provNo = (String) request.getSession().getAttribute("user");
//    
        String subject = request.getParameter("subject");
        String messageBody = request.getParameter("body");
        
        String priorThreadMessage = null ;
        String priorMessageId = request.getParameter("priorMessageId");
        if (priorMessageId != null){
           PHRDocument doc = phrDocumentDAO.getDocumentById(priorMessageId);
           log.debug("Setting replied on priormessageId "+priorMessageId);
           priorThreadMessage = doc.getPhrIndex();
                  
           PHRMessage msg = new PHRMessage(doc);
           msg.setReplied();
           msg.reDocContent();
           phrDocumentDAO.update((PHRDocument) msg);
           phrService.sendUpdateMessage(msg);
        }      
                
        
        
        ProviderData pp = new ProviderData();
        pp.getProvider(provNo);
        //ProviderMyOscarIdData myId = new ProviderMyOscarIdData(provNo); 
        
        //String senderOscarId = provNo; 
        ///String senderType = ""+PHRDocument.TYPE_PROVIDER;  
       // String senderPhrId = myId.getMyOscarId(); 
                
                
        String recipientOscarId = request.getParameter("recipientOscarId");
        if (request.getParameter("recipientType") == null) return mapping.findForward("view");
        int recipientType    = Integer.parseInt(request.getParameter("recipientType"));
        String recipientPhrId = null;
        
        log.debug("recipientType "+recipientType+" : "+PHRDocument.TYPE_DEMOGRAPHIC+ " : "+PHRDocument.TYPE_PROVIDER);
        if (  recipientType == PHRDocument.TYPE_DEMOGRAPHIC){
            log.debug("finding data from demographic");
            DemographicData demo = new DemographicData();
            recipientPhrId = demo.getDemographic(recipientOscarId).getIndivoId();
        }else if (recipientType == PHRDocument.TYPE_PROVIDER){
            recipientPhrId = ProviderMyOscarIdData.getMyOscarId(recipientOscarId); 
        }
        
        log.debug("SENDER ID >"+recipientPhrId+"<");
        phrService.sendAddMessage(subject,priorThreadMessage,messageBody,pp,recipientOscarId,recipientType,recipientPhrId);    
        
   // PHRMessage(subject, priorThreadMessage, messageBody, senderOscarId, senderType, 
     //          senderPhrId, recipientOscarId, recipientType, recipientPhrId)
        
        return mapping.findForward("view");
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        
        String id = request.getParameter("id");
        if (id == null) return viewSentMessages(mapping, form, request, response);
        log.debug("Id to delete:" + id);
        PHRAction action = phrActionDAO.getActionById(id);
        if (action.getStatus() != PHRAction.STATUS_SENT) {
            action.setStatus(PHRAction.STATUS_NOT_SENT_DELETED);
            phrActionDAO.update(action);
        }
        
        return viewSentMessages(mapping, form, request, response);
    }
    
    public ActionForward resend(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        
        String id = request.getParameter("id");
        if (id != null) {
            PHRAction action = phrActionDAO.getActionById(id);
            if (action.getStatus() != PHRAction.STATUS_SENT) {
                action.setStatus(PHRAction.STATUS_SEND_PENDING);
                phrActionDAO.update(action);
            }
        }
        return viewSentMessages(mapping, form, request, response);
    }
    
    public ActionForward archive(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        
        String id = request.getParameter("id");
        if (id == null) return null;
        PHRDocument doc = phrDocumentDAO.getDocumentById(id);
        PHRMessage msg = new PHRMessage(doc);
        if (!msg.isArchived()) {
            msg.addStatus(PHRMessage.STATUS_ARCHIVED);
            phrDocumentDAO.update(msg);
        }
        return viewMessages(mapping, form, request, response);
    }        
    public ActionForward unarchive(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();*/
        
        String id = request.getParameter("id");
        if (id == null) return null;
        PHRDocument doc = phrDocumentDAO.getDocumentById(id);
        PHRMessage msg = new PHRMessage(doc);
        if (msg.isArchived()) {
            msg.addStatus(-PHRMessage.STATUS_ARCHIVED);
            phrDocumentDAO.update(msg);
        }
        return viewArchivedMessages(mapping, form, request, response);
    } 
    
    private void clearSessionVariables(HttpServletRequest request) {
        request.getSession().setAttribute("indivoMessages", null);
        request.getSession().setAttribute("indivoSentMessages", null);
        request.getSession().setAttribute("indivoMessageActions", null);
        request.getSession().setAttribute("indivoArchivedMessages", null);
        request.getSession().setAttribute("indivoOtherActions", null);
        
    }
}
