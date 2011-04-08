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
 * PHRRetrieveAsyncAction.java
 *
 * Created on May 28, 2007, 4:39 PM
 *
 */

package org.oscarehr.phr.web;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRMedication;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.util.RxUtil;

/**
 *
 * @author jay
 */
public class PHRExchangeAction extends DispatchAction {
    
    private static Logger log = MiscUtils.getLogger();
    PHRService phrService = null;
    
    /**
     * Creates a new instance of PHRRetrieveAsyncAction
     */
    public PHRExchangeAction() {
    }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        return doPhrExchange(mapping, form ,request, response);
    }

  
    //save selected meds to drugs table.
    public ActionForward saveSelectedPHRMeds(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {

        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        if (bean == null) {
                response.sendRedirect("error.html");
                return null;
        }
        HashMap<Long,PHRMedication> meds=new HashMap<Long,PHRMedication>();
        String unimportedMed=request.getParameter("unimportedMed");

        String selected=request.getParameter("selectedDrugs");
        if(unimportedMed.equalsIgnoreCase("true"))
            meds=bean.getPairPrevViewedPHRMed();
        else
            meds=bean.getPairPHRMed();
        Set<Long> ks=meds.keySet();
        Iterator<Long> it= ks.iterator();
        String [] chosen=selected.split(",");
        List<String> chosenS=Arrays.asList(chosen);
        PHRDocumentDAO  phrDocumentDAO=(PHRDocumentDAO)SpringUtils.getBean("phrDocumentDAO");
        Long key;
        String keyS;
        String indexStr;
        DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
        Drug saveDrug;
        PHRDocument phrDoc;
        while(it.hasNext()){
            key=it.next();
            keyS=key.toString();
            PHRMedication m=meds.get(key);
            indexStr=m.getPhrIndex();
            phrDoc=phrDocumentDAO.getDocumentByIndex(indexStr);
            if(chosenS.contains(keyS)){
                //save it in drug table and update status to recieved and saved in other table in phr documents table
                saveDrug=m.getDrug();
                drugDao.addNewDrug(saveDrug);
                    LogAction.addLog(bean.getProviderNo(),LogConst.ADD,LogConst.CON_DRUGS,""+saveDrug.getId(),
                            request.getRemoteAddr(),""+bean.getDemographicNo(),saveDrug.getAuditString());
                phrDoc.setStatus(PHRDocument.STATUS_RECIEVED_SAVED_IN_OSCAR_TABLE);
                    phrDocumentDAO.update(phrDoc);
                    LogAction.addLog(bean.getProviderNo(),LogConst.UPDATE,PHRDocument.PHR_DOCUMENTS,""+phrDoc.getId() ,
                            request.getRemoteAddr(),""+bean.getDemographicNo(),phrDoc.getDocContent());
            }

        }
        HashMap<String,Boolean> hm=new HashMap<String,Boolean>();
        hm.put("success", Boolean.TRUE);
        JSONObject jsonObject = JSONObject.fromObject(hm);
        response.getOutputStream().write(jsonObject.toString().getBytes());
        return null;
    }

    public ActionForward displayNewMedsFromPhr(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.debug("-----------------displayNewMedsFromPhr has been called -------------");
        String demoId=request.getParameter("demoId");
        String demoPhrId="";
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        if (bean == null) {
                response.sendRedirect("error.html");
                return null;
        }
        if(demoId!=null){
            DemographicDao demographicDao=(DemographicDao)SpringUtils.getBean("demographicDao");
            demoPhrId=demographicDao.getDemographic(demoId).getPin();
            if(demoPhrId!=null && demoPhrId.length()>0){
                PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
                //PrintWriter out = response.getWriter();
                log.error("auth object="+auth+"--"+request.getSession().getAttribute(phrService.SESSION_PHR_EXCHANGE_TIME));
                if (auth != null && request.getSession().getAttribute(phrService.SESSION_PHR_EXCHANGE_TIME) != null){
                //if (auth != null){
                    String providerNo = (String) request.getSession().getAttribute("user");
                    try{
                        request.getSession().setAttribute(phrService.SESSION_PHR_EXCHANGE_TIME, null);
                        long startTime = System.currentTimeMillis();                        
                        
                        List<PHRMedication> listDrugsToDisplay=phrService.retrieveSaveMedToDisplay(auth,providerNo,demoId,demoPhrId);
                        HashMap<Long,PHRMedication> drugsToDisplay =RxUtil.createKeyValPair(listDrugsToDisplay);
                        bean.setPairPHRMed(drugsToDisplay);
                        
                        //phrService.retrieveUploadDocs(auth,providerNo);
                        request.getSession().setAttribute(phrService.SESSION_PHR_EXCHANGE_TIME, getNextExchangeTime());
                        log.info("Time taken to perform doPhrExchangeMedication: " + (System.currentTimeMillis()-startTime) + "ms");
                        //out.print("1");
                    }catch(Exception e){
                        MiscUtils.getLogger().error("Error", e);
                        //out.print("exception thrown");
                        request.getSession().removeAttribute(PHRAuthentication.SESSION_PHR_AUTH);
                    }
                }else{
                    log.error("String Auth object was null or the previous action still executing");
                    return mapping.findForward("phrNotAuthorized");
                }
            }
        }
        return mapping.findForward("success");
    }

    public ActionForward displayPrevViewedMeds(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        String provNo=bean.getProviderNo();
        //need demographic number
        String demoId=request.getParameter("demoId");
        //get all drugs with status = 5 from phr_document table.
        PHRDocumentDAO  phrDocumentDAO=(PHRDocumentDAO)SpringUtils.getBean("phrDocumentDAO");
        List<PHRDocument> phrDocument=phrDocumentDAO.getDocumentsByReceiverSenderStatusClassification(PHRDocument.TYPE_DEMOGRAPHIC,
                PHRDocument.TYPE_PROVIDER ,PHRDocument.CLASSIFICATION_MED,demoId, PHRDocument.STATUS_RECIEVED_NOT_SAVED_IN_OSCAR_TABLE);
        List<PHRMedication> listDrugsToDisplay=new ArrayList<PHRMedication>();
        for(PHRDocument d:phrDocument){
            PHRMedication m=(PHRMedication)d;
            m.initMedication();
            m.createDrugFromPhrMed(provNo);
            listDrugsToDisplay.add(m);
        }
        HashMap<Long,PHRMedication> drugsToDisplay =RxUtil.createKeyValPair(listDrugsToDisplay);
        bean.setPairPrevViewedPHRMed(drugsToDisplay);
        request.setAttribute("unimportedMed", true);
        return mapping.findForward("success");
    }
    public ActionForward doPhrExchange(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("-----------------Indivo Exchange has been called -------------");
        PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
        PrintWriter out = response.getWriter();
        if (auth != null && request.getSession().getAttribute(phrService.SESSION_PHR_EXCHANGE_TIME) != null){
            String providerNo = (String) request.getSession().getAttribute("user");
            try{
                request.getSession().setAttribute(phrService.SESSION_PHR_EXCHANGE_TIME, null);
                long startTime = System.currentTimeMillis();
                phrService.sendQueuedDocuments(auth,providerNo) ;
//                phrService.retrieveDocuments(auth,providerNo);
                //phrService.retrieveUploadDocs(auth,providerNo);
                request.getSession().setAttribute(phrService.SESSION_PHR_EXCHANGE_TIME, getNextExchangeTime());
                log.info("Time taken to perform OSCAR-myOSCAR exchange: " + (System.currentTimeMillis()-startTime) + "ms");
                out.print("1");
            }catch(Exception e){
                MiscUtils.getLogger().error("Error", e);
                out.print("0");
                request.getSession().removeAttribute(PHRAuthentication.SESSION_PHR_AUTH);
            }
        }else{
            log.error("String Auth object was null or the previous action still executing");
        }
        return null;
    }
    
    public void setPhrService(PHRService pServ){
        this.phrService = pServ;
    }
    
    public ActionForward setExchangeTimeNow(ActionMapping mapping, ActionForm  form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getSession().getAttribute(phrService.SESSION_PHR_EXCHANGE_TIME) != null) {
            request.getSession().setAttribute(phrService.SESSION_PHR_EXCHANGE_TIME, Calendar.getInstance().getTime());
            log.debug("set exchange to 0");
        }
        log.debug("finished setting exchange to 0");
        return new ActionRedirect(request.getParameter("forwardto"));
    }
    
        //returns a date that is intervalMinutes from now
    public static Date getNextExchangeTime() {
        int intervalMinutes = Integer.parseInt(OscarProperties.getInstance().getProperty(PHRService.OSCAR_PROPS_EXCHANGE_INTERVAL));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, intervalMinutes);
        return cal.getTime();
    }
    
}
