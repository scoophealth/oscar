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
 * ManageTeleplanAction.java
 *
 * Created on June 22, 2007, 12:10 AM
 *
 */

package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import oscar.OscarProperties;
import oscar.entities.Billactivity;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanAPI;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanCodesManager;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanResponse;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanSequenceDAO;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanService;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanUserPassDAO;
import oscar.oscarBilling.ca.bc.data.BillActivityDAO;
import oscar.oscarBilling.ca.bc.data.BillingCodeData;

/**
 *
 * @author jay
 */
public class ManageTeleplanAction extends DispatchAction {
    
    private static Log log = LogFactory.getLog(ManageTeleplanAction.class);
    
    /** Creates a new instance of ManageTeleplanAction */
    public ManageTeleplanAction() {
    }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           log.debug("UNSPECIFIED ACTION!");
           return mapping.findForward("success");
    }
    
    public ActionForward setUserName(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           log.debug("SET USER NA<E ACTION JACKSON");
           String username = (String) request.getParameter("user");
           String password = (String) request.getParameter("pass");
           
           log.debug("username "+username+" password "+password);
           
           //TODO: validate username - make sure url is not null
           
           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           dao.saveUpdateUsername(username);
           dao.saveUpdatePasssword(password);
           return mapping.findForward("success");
    }
    
    public ActionForward updateBillingCodes(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
        
           System.out.println("UPDATE BILLING CODE WITH PARSING");
           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();
           TeleplanService tService = new TeleplanService();
           TeleplanAPI tAPI = tService.getTeleplanAPI(userpass[0],userpass[1]);
           
           TeleplanResponse tr = tAPI.getAsciiFile("3");
          
           log.debug("real filename "+tr.getRealFilename());
           
           File file = tr.getFile();
           TeleplanCodesManager tcm = new TeleplanCodesManager();
           List list = tcm.parse(file);
           request.setAttribute("codes",list);
           return mapping.findForward("codelist");
    }
    
    
    public ActionForward commitUpdateBillingCodes(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           String[] codes = request.getParameterValues("codes");
           if (codes != null){
               for(String code: codes){
                  System.out.println(code);
                  String[] codeval = code.split("À");   //="<%=h.get("code")%>|<%=h.get("fee")%>|<%=h.get("desc")%>"
                  BillingCodeData bcd = new BillingCodeData();
                  if (bcd.getBillingCodeByCode(codeval[0]) == null){ //NEW CODE
                    bcd.addBillingCode(codeval[0],codeval[2],codeval[1]);
                  }else{ //UPDATE PRICE
                    bcd.updateBillingCodePrice(codeval[0],codeval[1]); 
                  }
               }
           }
           return mapping.findForward("success");
    }
    
    public ActionForward getSequenceNumber(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           log.debug("getSequenceNumber");
           OscarProperties prop = OscarProperties.getInstance();
           String datacenter = prop.getProperty("dataCenterId","");
           if(datacenter.length() != 5){
               //this.addMessages() //TODO:ADD MESSAGE ABOUT DATA CENTER NOT BEING CORRECT 
                log.debug("returning because of datacenter #"+datacenter);
                return mapping.findForward("success");
           }
           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();
           TeleplanService tService = new TeleplanService();
           
           TeleplanAPI tAPI = null;
           try{
                tAPI = tService.getTeleplanAPI(userpass[0],userpass[1]);
           }catch(Exception e){
               log.debug(e.getMessage(),e);
               request.setAttribute("error",e.getMessage());
               return mapping.findForward("success");
           }
           
           
           int sequenceNumber = tService.getSequenceNumber(tAPI,datacenter);
        
           TeleplanSequenceDAO seq = new TeleplanSequenceDAO();
           seq.saveUpdateSequence(sequenceNumber);
           return mapping.findForward("success");
    }
    
    public ActionForward setSequenceNumber(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           log.debug("setSequenceNumber");
           String sequence = (String) request.getParameter("num");
           int sequenceNumber = -1;
           try{
            sequenceNumber = Integer.parseInt(sequence);
           }catch(Exception e){
               //TODO: ADDED ERROR MESSAGE ABOUT THE NUMBER NOT BEING A NUMBER!
               return mapping.findForward("success");
           }
           
           if (sequenceNumber < 0 || sequenceNumber > 9999999){
               //TODO: ADDED ERROR MESSAGE ABOUT NUMBER BEING OUT OF RANGE
               return mapping.findForward("success");
           }
           
           TeleplanSequenceDAO seq = new TeleplanSequenceDAO();
           seq.saveUpdateSequence(sequenceNumber);
           return mapping.findForward("success");
    }
    
    
    public ActionForward sendFile(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           log.debug("sendFile Start");
           String id  = (String) request.getParameter("id");
           
           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();
           TeleplanService tService = new TeleplanService();
           TeleplanAPI tAPI = null;
           try{
                tAPI = tService.getTeleplanAPI(userpass[0],userpass[1]);
           }catch(Exception e){
               log.debug(e.getMessage(),e);
               request.setAttribute("error",e.getMessage());
               return mapping.findForward("submission");
           }
           
           
           
           //TODO: validate username - make sure url is not null
           BillActivityDAO billActDAO = new BillActivityDAO();
           List l = billActDAO.getBillactivityByID(id);
           
           Billactivity b = (Billactivity) l.get(0);
           String filename = b.getOhipfilename();
           
           OscarProperties prop = OscarProperties.getInstance();
           String datacenter = prop.getProperty("HOME_DIR","");
           
           File f = new File(datacenter,filename);
           
           
           if ( f != null && log.isDebugEnabled()){
               log.debug("File is Readable: "+f.canRead());
               log.debug("File exists: "+f.exists());
               log.debug("File Path " +f.getCanonicalPath());
           }
           log.info("sending file "+f.getAbsolutePath());
           
           TeleplanResponse tr = tAPI.putMSPFile(f);
           log.debug("sendFile End"+tr.getResult());
           if(!tr.isSuccess()){
               request.setAttribute("error",tr.getMsgs());
           }else{
               billActDAO.setStatusToSent(b);
           }
           
           
           return mapping.findForward("submission");
    }  
    
    public ActionForward remit(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           
           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();
           TeleplanService tService = new TeleplanService();
           
           TeleplanAPI tAPI = null;
           try{
                tAPI = tService.getTeleplanAPI(userpass[0],userpass[1]);
           }catch(Exception e){
               log.debug(e.getMessage(),e);
               request.setAttribute("error",e.getMessage());
               return mapping.findForward("success");
           }
        
           TeleplanResponse tr = tAPI.getRemittance(true);
           log.debug(tr.toString());
           log.debug("real filename "+tr.getRealFilename());
           request.setAttribute("filename",tr.getRealFilename());
           return mapping.findForward("remit");
    }
            
    public ActionForward changePass(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           String oldpass  = (String) request.getParameter("oldpass");
           String newpass  = (String) request.getParameter("newpass");
           String confpass = (String) request.getParameter("confpass");
           
           //TODO: validate username - make sure url is not null
           
           if (!newpass.equals(confpass)){  
               return mapping.findForward("error");
           }
           //CHECK TELEPLAN
           
           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();
           TeleplanService tService = new TeleplanService();
           
           TeleplanAPI tAPI = new TeleplanAPI();
           try{
              //THIS IS DIFFERENT BECAUSE THE NORMAL TELEPLAN SERVICE WILL THROW AN EXCEPTION BECAUSE IT CHECKS FOR RESULT OF SUCCESS.  
               //IF PASSWORD HAS EXPIRED THE RESULTS is EXPIRED.PASSWORD
              TeleplanResponse tr = tAPI.login(userpass[0],userpass[1]);
              
              
           }catch(Exception e){
               log.debug(e.getMessage(),e);
               request.setAttribute("error",e.getMessage());
               return mapping.findForward("success");
           }
           
           TeleplanResponse tr = tAPI.changePassword(userpass[0],userpass[1],newpass,confpass);
           log.debug("change password "+tr.getResult());
           if(!tr.isSuccess()){
              request.setAttribute("error",tr.getMsgs());
           }else{
              dao.saveUpdatePasssword(newpass);
           }
           return mapping.findForward("success");
    }
    
    
    public ActionForward checkElig(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           log.debug("checkElig");
           OscarProperties prop = OscarProperties.getInstance();
           
           
           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();
           TeleplanService tService = new TeleplanService();
           
           TeleplanAPI tAPI = null;
           try{
                tAPI = tService.getTeleplanAPI(userpass[0],userpass[1]);
           }catch(Exception e){
               log.debug(e.getMessage(),e);
               request.setAttribute("error",e.getMessage());
               return mapping.findForward("success");
           }
           
           String phn = "";
           String dateofbirthyyyy= "";
           String dateofbirthmm= "";
           String dateofbirthdd= "";
           String dateofserviceyyyy= ""; 
           String dateofservicemm= "";
           String dateofservicedd= "";
           boolean patientvisitcharge= true; 
           boolean lasteyeexam=true;
           boolean patientrestriction=true;
           
           //TeleplanResponse tr = tAPI.checkElig(phn,dateofbirthyyyy,dateofbirthmm,dateofbirthdd,dateofserviceyyyy,dateofservicemm,dateofservicedd,patientvisitcharge,lasteyeexam, patientrestriction);
           //request.setAttribute("message",tr.toString());
           return mapping.findForward("success");
    }
    
   
    
    
    
}
