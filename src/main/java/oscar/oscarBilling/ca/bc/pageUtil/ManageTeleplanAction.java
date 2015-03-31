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


package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DiagnosticCodeDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.entities.Billactivity;
import oscar.oscarBilling.ca.bc.MSP.MspErrorCodes;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanAPI;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanCodesManager;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanResponse;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanSequenceDAO;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanService;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanUserPassDAO;
import oscar.oscarBilling.ca.bc.data.BillActivityDAO;
import oscar.oscarBilling.ca.bc.data.BillingCodeData;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class ManageTeleplanAction extends DispatchAction {

    private static Logger log = MiscUtils.getLogger();

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
            {
           log.debug("SET USER NA<E ACTION JACKSON");
           String username = request.getParameter("user");
           String password = request.getParameter("pass");

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


    public ActionForward updateteleplanICDCodesList(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {

           DiagnosticCodeDao bDx = SpringUtils.getBean(DiagnosticCodeDao.class);

           log.debug("UPDATE ICD  CODE WITH PARSING");
           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();
           TeleplanService tService = new TeleplanService();
           TeleplanAPI tAPI = tService.getTeleplanAPI(userpass[0],userpass[1]);
           TeleplanResponse tr = tAPI.getAsciiFile("2");

           log.debug("real filename "+tr.getRealFilename());

           File file = tr.getFile();
           BufferedReader buff = new BufferedReader(new FileReader(file));

           String line = null;
           Properties dxProp = new Properties();
           while ((line = buff.readLine()) != null) {
               if (!line.startsWith("REM")){
                   log.debug(line.substring(0,5).trim()+"="+line.substring(4).trim());
                   String code = line.substring(0,5).trim();
                   String desc = line.substring(4).trim();

                   if(dxProp.containsKey(code)){//Some of the lines in file double up for a longer desc.
                       String dxDesc = dxProp.getProperty(code);
                       dxDesc += " " +desc;
                       dxProp.setProperty(code, dxDesc);
                   }else{
                       dxProp.put(code, desc);
                   }

               }
           }

           Enumeration dxKeys = dxProp.keys();
           while(dxKeys.hasMoreElements()){
               String code = (String) dxKeys.nextElement();
               String desc = dxProp.getProperty(code);

                   List<DiagnosticCode> dxList = bDx.getByDxCode(code);
                   if (dxList == null || dxList.size() == 0){ //New Code
                	   DiagnosticCode dxCode = new DiagnosticCode();
                        log.debug("Adding new code "+code+" desc : "+desc);
                        dxCode.setDiagnosticCode(code);
                        dxCode.setDescription(desc);
                        dxCode.setRegion("BC");
                        dxCode.setStatus("A");
                        bDx.persist(dxCode);
                   }



           }
           return mapping.findForward("success");
    }

    /*
     *  2 = MSP ICD9 Codes (3 char)
	*      1 = MSP Explanatory Codes List
     */

    public ActionForward updateExplanatoryCodesList(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {


           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();
           TeleplanService tService = new TeleplanService();
           TeleplanAPI tAPI = tService.getTeleplanAPI(userpass[0],userpass[1]);

           TeleplanResponse tr = tAPI.getAsciiFile("1");

           log.debug("real filename "+tr.getRealFilename());

           File file = tr.getFile();
           BufferedReader buff = new BufferedReader(new FileReader(file));

           String line = null;

           boolean start= false;
           StringBuilder sb = new StringBuilder();
           MspErrorCodes errorCodes = new MspErrorCodes();

           while ((line = buff.readLine()) != null) {
               line = line.trim();
               if (line != null && line.startsWith("--")){
                   start = true;
                   continue;
               }
               if (start){
                   if (line.trim().equals("")){
                       String togo = sb.toString();
                       sb = new StringBuilder();
                       if (!togo.equals("")){
                          errorCodes.put(togo.substring(0,2), togo.substring(4));
                       }
                   }else{
                       sb.append(line);
                   }
               }
           }

           if (sb.length() > 0){ //still left in the buffer
               String togo = sb.toString();
               int i = togo.lastIndexOf("#TID");
               if (i != -1){
                  togo = togo.substring(0,i);
               }

               if (!togo.equals("")){
                  errorCodes.put(togo.substring(0,2), togo.substring(4));
               }
           }

           errorCodes.save();

           //...I guess pass the errors back to jsp
           StringBuilder errorStr = new StringBuilder("");
           for (Entry error: errorCodes.entrySet()) {
               errorStr.append("Error codes: \n");
               errorStr.append(error.getKey());
               errorStr.append(" -- ");
               errorStr.append(error.getValue());
               errorStr.append("\n<br/>");
           }
           request.setAttribute("error", errorStr);
           //---------------------------------------------------------------------------

           log.info("Msp error codes "+errorCodes.size());
           log.debug(sb.toString());
           return mapping.findForward("success");
    }


    public ActionForward commitUpdateBillingCodes(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response)
            {
           String[] codes = request.getParameterValues("codes");
           if (codes != null){
               for(String code: codes){
                  String nCode = code.substring(0,5);
                  String fee = code.substring(5,13).trim();
                  String desc = code.substring(13).trim();
                  BillingCodeData bcd = new BillingCodeData();
                  if (bcd.getBillingCodeByCode(nCode) == null){ //NEW CODE
                    bcd.addBillingCode(nCode,desc,fee);
                  }else{ //UPDATE PRICE
                    bcd.updateBillingCodePrice(nCode,fee);
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
            {
           log.debug("setSequenceNumber");
           String sequence = request.getParameter("num");
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
           String id  = request.getParameter("id");

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
            {

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

    public ActionForward setPass(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
            {
           String newpass  = request.getParameter("newpass");
           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           dao.saveUpdatePasssword(newpass);
           return mapping.findForward("success");
    }


    public ActionForward changePass(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response) {

           String newpass  = request.getParameter("newpass");
           String confpass = request.getParameter("confpass");

           //TODO: validate username - make sure url is not null

           if (!newpass.equals(confpass)){
               return mapping.findForward("error");
           }
           //CHECK TELEPLAN

           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();


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
           String demographicNo = request.getParameter("demographic");
           OscarProperties prop = OscarProperties.getInstance();
           DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
           Demographic demo = demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);

           Date billingDate = new Date();

           TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
           String[] userpass = dao.getUsernamePassword();
           TeleplanService tService = new TeleplanService();

           TeleplanAPI tAPI = null;
           try{
                tAPI = tService.getTeleplanAPI(userpass[0],userpass[1]);
           }catch(Exception e){
               log.debug(e.getMessage(),e);
               request.setAttribute("error",e.getMessage());
               return mapping.findForward("checkElig");
           }

           String phn = demo.getHin();
           String dateofbirthyyyy= demo.getYearOfBirth();
           String dateofbirthmm= demo.getMonthOfBirth();
           String dateofbirthdd= demo.getDateOfBirth();
           String dateofserviceyyyy= UtilDateUtilities.justYear(billingDate);
           String dateofservicemm= UtilDateUtilities.justMonth(billingDate);
           String dateofservicedd= UtilDateUtilities.justDay(billingDate);
           boolean patientvisitcharge= true;
           boolean lasteyeexam=true;
           boolean patientrestriction=true;

           TeleplanResponse tr = tAPI.checkElig(phn,dateofbirthyyyy,dateofbirthmm,dateofbirthdd,dateofserviceyyyy,dateofservicemm,dateofservicedd,patientvisitcharge,lasteyeexam, patientrestriction);
           log.debug(tr.getResult());
           log.debug(tr.isSuccess());
           log.debug(tr.toString());
           request.setAttribute("Result",tr.getResult());


           String realFile = tr.getRealFilename();
           if (realFile != null && !realFile.trim().equals("")){
               File file = tr.getFile();
               BufferedReader buff = new BufferedReader(new FileReader(file));
               StringBuilder sb = new StringBuilder();
               String line = null;

               while ((line = buff.readLine()) != null) {

                  if (line != null && line.startsWith("ELIG_ON_DOS:")){
                      String el = line.substring(12).trim();
                      if(el.equalsIgnoreCase("no")){
                        request.setAttribute("Result","Failure");

                        line = "<span style=\"color:red; font-weight:bold;\">"+line+"</span>";
                      }
                  }
                  sb.append(line);
                  sb.append("<br>");
               }
               request.setAttribute("Msgs", sb.toString());//tr.getMsgs());

           }else{
               request.setAttribute("Msgs", tr.getMsgs());

           }

           //request.setAttribute("message",tr.toString());
           return mapping.findForward("checkElig");
    }





}
