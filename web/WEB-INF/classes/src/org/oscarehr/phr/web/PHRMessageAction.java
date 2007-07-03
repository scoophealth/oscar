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

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRMessage;
import org.oscarehr.phr.service.PHRService;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarProvider.data.ProviderMyOscarIdData;

/**
 *
 * @author jay
 */
public class PHRMessageAction extends  DispatchAction {  //IndivoAction{
    
    private static Log log = LogFactory.getLog(PHRMessageAction.class);
    
    
    PHRDocumentDAO phrDocumentDAO;
    PHRService phrService;
    /** Creates a new instance of PHRMessageAction */
    public PHRMessageAction() {
    }
    
    public void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO) {
        this.phrDocumentDAO = phrDocumentDAO;
        
    }
    
     public void setPhrService(PHRService phrService){
        this.phrService = phrService;
     }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           return viewMessages(mapping,form,request,response);
    }
    
    public ActionForward viewMessages(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        PHRAuthentication auth  = (PHRAuthentication) request.getAttribute("INDIVO_AUTH"); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();
        String providerNo = (String) request.getSession().getAttribute("user");
        List docs = phrDocumentDAO.getDocuments("urn:org:indivo:document:classification:message", providerNo);
 
        if(docs  != null){
            request.getSession().setAttribute("indivoMessages", docs);
        } 
        return mapping.findForward("view");
    }
     
    public ActionForward read(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        PHRAuthentication auth  = (PHRAuthentication) request.getAttribute("INDIVO_AUTH"); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();
        String providerNo = (String) request.getSession().getAttribute("user");
        String id = request.getParameter("id");
        PHRDocument doc = phrDocumentDAO.getDocumentById(id);
        PHRMessage msg = new PHRMessage(doc);
        msg.setRead();
        msg.reDocContent();
        phrDocumentDAO.update((PHRDocument) msg);
        
        phrService.sendUpdateMessage(msg);
        
        if (doc == null){
            //FORWARD TO PAGE SAYING DOCUMENT NOT FOUND ON SYSTEM
        }       
        log.debug("ID FOR DOC BEING READ IS "+doc.getId());
        request.setAttribute("message",msg);
        //List refList = phrDocumentDAO.getReferencedMessages(doc);
        //request.setAttribute("refList",refList);
        
        return mapping.findForward("read");
    }
    
    //
    // Reply is a create but displays the message being re 
    // 
    public ActionForward reply(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        PHRAuthentication auth  = (PHRAuthentication) request.getAttribute("INDIVO_AUTH"); 
        log.debug("AUTH "+auth);
        String indivoId   = auth.getUserId();
        String ticket     = auth.getToken();
        String providerNo = (String) request.getSession().getAttribute("user");
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
        request.setAttribute("subject","re:"+msgRe); //TODO: check to see if the string already starts with re:
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
        PHRAuthentication auth  = (PHRAuthentication) request.getAttribute("INDIVO_AUTH"); 
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
        int recipientType    = Integer.parseInt(request.getParameter("recipientType"));
        String recipientPhrId = null;
        
        log.debug("recipientType "+recipientType+" : "+PHRDocument.TYPE_DEMOGRAPHIC+ " : "+PHRDocument.TYPE_PROVIDER);
        if (  recipientType == PHRDocument.TYPE_DEMOGRAPHIC){
            log.debug("finding data from demographic");
            DemographicData demo = new DemographicData();
            recipientPhrId = demo.getDemographic(recipientOscarId).getIndivoId();
        }else if (recipientType == PHRDocument.TYPE_PROVIDER){
            ProviderMyOscarIdData theirId = new ProviderMyOscarIdData(recipientOscarId); 
            recipientPhrId = theirId.getMyOscarId(); 
        }
        
        log.debug("SENDER ID >"+recipientPhrId+"<");
        phrService.sendAddMessage(subject,priorThreadMessage,messageBody,pp,recipientOscarId,recipientType,recipientPhrId);    
        
   // PHRMessage(subject, priorThreadMessage, messageBody, senderOscarId, senderType, 
     //          senderPhrId, recipientOscarId, recipientType, recipientPhrId)
        
        return mapping.findForward("view");
    }
}
