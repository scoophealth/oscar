/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarRx.pageUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;

public final class RxWriteScriptAction extends DispatchAction {

    private static final Logger logger = MiscUtils.getLogger();

    public void p(String s) {
        System.out.println(s);
    }

    public void p(String s, String s1) {
        System.out.println(s + "=" + s1);
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {
        System.out.println("***===========IN Take RxWriteScriptAction.java");

        RxWriteScriptForm frm = (RxWriteScriptForm) form;
        String fwd = "refresh";
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        //    p("bean.getStashIndex(): " + bean.getStashIndex());
        //  p("**## print out fields in bean==========");
        //    p(bean.getProviderNo());
        //p(Integer.toString(bean.getDemographicNo()));
        // p(bean.getStash());
        //p(Integer.toString(bean.getStashIndex()));
        //    p(bean.getAllergyWarnings(fwd));
        //  p(Integer.toString(bean.getAttributeNames().size()));
        ArrayList attributeNames = new ArrayList();
        attributeNames = bean.getAttributeNames();
        for (int i = 0; i < attributeNames.size(); i++) {
            p(attributeNames.get(i).toString());
        }
        //   p("done print out fields in bean=========");
        //   p("bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
        if (bean == null) {
            response.sendRedirect("error.html");
            return null;
        }

        if (frm.getAction().startsWith("update")) {

            RxDrugData drugData = new RxDrugData();
            p("check bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
            RxPrescriptionData.Prescription rx = bean.getStashItem(bean.getStashIndex());
            RxPrescriptionData prescription = new RxPrescriptionData();

            if (frm.getGCN_SEQNO() != 0) { // not custom
                if (frm.getBrandName().equals(rx.getBrandName()) == false) {
                    rx.setBrandName(frm.getBrandName());
                } else {
                    rx.setGCN_SEQNO(frm.getGCN_SEQNO());
                }
            } else { // custom
                rx.setBrandName(null);
                rx.setGCN_SEQNO(0);
                rx.setCustomName(frm.getCustomName());
            }
            p("frm.getRxDate()", frm.getRxDate());
            p("frm.getWrittenDate()", frm.getWrittenDate());
            p("frm.getTakeMinFloat()", Float.toString(frm.getTakeMinFloat()));
            p("frm.getTakeMaxFloat()", Float.toString(frm.getTakeMaxFloat()));
            p("frm.getFrequencyCode()", frm.getFrequencyCode());
            p("frm.getDuration()", frm.getDuration());
            p("frm.getRepeat()", Integer.toString(frm.getRepeat()));
            p("frm.getLastRefillDate()", frm.getLastRefillDate());
            p("frm.getSpecial()", frm.getSpecial());
            p("frm.getAtcCode()", frm.getAtcCode());
            p("frm.getRegionalIdentifier()", frm.getRegionalIdentifier());
            p("frm.getUnit()", frm.getUnit());
            p("frm.getUnitName()", frm.getUnitName());
            p("frm.getMethod()", frm.getMethod());
            p("frm.getRoute()", frm.getRoute());
            p("frm.getDosage()", frm.getDosage());

            p("rxdate before process", frm.getRxDate());
            p("rxdate after process", RxUtil.StringToDate(frm.getRxDate(), "yyyy-MM-dd").toString());

            rx.setRxDate(RxUtil.StringToDate(frm.getRxDate(), "yyyy-MM-dd"));
            rx.setWrittenDate(RxUtil.StringToDate(frm.getWrittenDate(), "yyyy-MM-dd"));
            rx.setTakeMin(frm.getTakeMinFloat());
            rx.setTakeMax(frm.getTakeMaxFloat());
            rx.setFrequencyCode(frm.getFrequencyCode());
            rx.setDuration(frm.getDuration());
            rx.setDurationUnit(frm.getDurationUnit());
            rx.setQuantity(frm.getQuantity());
            rx.setRepeat(frm.getRepeat());
            rx.setLastRefillDate(RxUtil.StringToDate(frm.getLastRefillDate(), "yyyy-MM-dd"));
            rx.setNosubs(frm.getNosubs());
            rx.setPrn(frm.getPrn());
            rx.setSpecial(frm.getSpecial());
            rx.setAtcCode(frm.getAtcCode());
            rx.setRegionalIdentifier(frm.getRegionalIdentifier());
            rx.setUnit(frm.getUnit());
            rx.setUnitName(frm.getUnitName());
            rx.setMethod(frm.getMethod());
            rx.setRoute(frm.getRoute());
            rx.setCustomInstr(frm.getCustomInstr());
            rx.setDosage(frm.getDosage());
            rx.setOutsideProviderName(frm.getOutsideProviderName());
            rx.setOutsideProviderOhip(frm.getOutsideProviderOhip());
            rx.setLongTerm(frm.getLongTerm());
            rx.setPastMed(frm.getPastMed());
            rx.setPatientCompliance(frm.getPatientComplianceY(), frm.getPatientComplianceN());

            try {
                rx.setDrugForm(drugData.getDrugForm(String.valueOf(frm.getGCN_SEQNO())));
            } catch (Exception e) {
                logger.error("Unable to get DrugForm from drugref");
            }

            System.out.println("SAVING STASH " + rx.getCustomInstr());
            logger.debug("SAVING STASH " + rx.getCustomInstr());
            if (rx.getSpecial() == null) {
                logger.error("Drug.special is null : " + rx.getSpecial() + " : " + frm.getSpecial());
            } else if (rx.getSpecial().length() < 6) {
                logger.warn("Drug.special appears to be empty : " + rx.getSpecial() + " : " + frm.getSpecial());
            }

            String annotation_attrib = request.getParameter("annotation_attrib");
            if (annotation_attrib == null) {
                annotation_attrib = "";
            }
            //      System.out.println("SETTING ANNOTATE NAME '" + annotation_attrib + "'");
            //      System.out.println("SETTING StashIndex '" + "" + bean.getStashIndex() + "'");
            bean.addAttributeName(annotation_attrib, bean.getStashIndex());
            bean.setStashItem(bean.getStashIndex(), rx);
            p("bean.getStashIndex() in take=" + "" + bean.getStashIndex());
            rx = null;

            if (frm.getAction().equals("update")) {
                fwd = "refresh";
            }
            if (frm.getAction().equals("updateAddAnother")) {
                fwd = "addAnother";
            }
            if (frm.getAction().equals("updateAndPrint")) {
                //SAVE THE DRUG
                int i;
                String scriptId = prescription.saveScript(bean);
                @SuppressWarnings("unchecked")
                ArrayList<String> attrib_names = bean.getAttributeNames();
                //      p("attrib_names", attrib_names.toString());
                StringBuffer auditStr = new StringBuffer();
                for (i = 0; i < bean.getStashSize(); i++) {
                    rx = bean.getStashItem(i);
                    // System.out.println("*** before rx.Save(" + scriptId.toString() + ")");
                    rx.Save(scriptId);
                    auditStr.append(rx.getAuditString());
                    auditStr.append("\n");
                    //    p("rx.getAuditString()", rx.getAuditString());
                    /* Save annotation */
                    HttpSession se = request.getSession();
                    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
                    CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
                    String attrib_name = attrib_names.get(i);
                    //   p("attrib_names.get(i)", attrib_names.get(i));
                    if (attrib_name != null) {
                        CaseManagementNote cmn = (CaseManagementNote) se.getAttribute(attrib_name);
                        if (cmn != null) {
                            cmm.saveNoteSimple(cmn);
                            CaseManagementNoteLink cml = new CaseManagementNoteLink();
                            cml.setTableName(CaseManagementNoteLink.DRUGS);
                            cml.setTableId((long) rx.getDrugId());
                            cml.setNoteId(cmn.getId());
                            cmm.saveNoteLink(cml);
                            se.removeAttribute(attrib_name);
                            LogAction.addLog(cmn.getProviderNo(), LogConst.ANNOTATE, CaseManagementNoteLink.DISP_PRESCRIP, scriptId, request.getRemoteAddr(), cmn.getDemographic_no(), cmn.getNote());
                        }
                    }
                    rx = null;
                }
                System.out.println("bean.getStashIndex() after update,print,save=" + bean.getStashIndex());
                fwd = "viewScript";
                String ip = request.getRemoteAddr();
                //    p("ip", ip);
                request.setAttribute("scriptId", scriptId);
                LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_PRESCRIPTION, scriptId, ip, "" + bean.getDemographicNo(), auditStr.toString());
            }
        }
        System.out.println("***===========End of unspecified RxWriteScriptAction.java");
        return mapping.findForward(fwd);
    }

    public ActionForward saveDrug(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {
        System.out.println("==========***### IN saveDrug RxWriteScriptAction.java");
        // Enumeration emm = request.getSession().getAttributeNames();
        //  while (emm.hasMoreElements()) {
        //      p("session attribute=" + emm.nextElement().toString());
        //  }
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");

        //    p("**## print out fields in bean==========");
        //   p(bean.getProviderNo());
        //     p(Integer.toString(bean.getDemographicNo()));
        // p(bean.getStash());
        //    p(Integer.toString(bean.getStashIndex()));
        //    p(bean.getAllergyWarnings(fwd));
        //    p(Integer.toString(bean.getAttributeNames().size()));
        ArrayList attributeNames = new ArrayList();
        attributeNames = bean.getAttributeNames();
        for (int i = 0; i < attributeNames.size(); i++) {
            //       p(attributeNames.get(i).toString());
        }
        //     p("done print out fields in bean=========");
        p("here");
        //     p("bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
        List<String> paramList = new ArrayList();
        Enumeration em = request.getParameterNames();
        List<String> randNum = new ArrayList();
        while (em.hasMoreElements()) {
            //paramList.add(em.nextElement().toString());
            String[] temp = null;
            temp = em.nextElement().toString().split("_");
            if (temp.length > 1) {
                randNum.add(temp[1]);
            }
        }
        p("here2");


        RxDrugData drugData = new RxDrugData();
        RxPrescriptionData.Prescription rx = bean.getStashItem(bean.getStashIndex());
        RxPrescriptionData prescription = new RxPrescriptionData();
        String scriptId = prescription.saveScript(bean);
        StringBuffer auditStr = new StringBuffer();
        ArrayList<String> attrib_names = bean.getAttributeNames();
        p("here3");
        p("bean.getStashSize()", Integer.toString(bean.getStashSize()));
        for (int i = 0; i < bean.getStashSize(); i++) {
            try {
                rx = bean.getStashItem(i);
                p("before saving rx", rx.getGenericName());
                p(rx.getSpecial());
                p(rx.getQuantity());
                p("" + rx.getRepeat());
                p(rx.getAtcCode());
                p(rx.getRegionalIdentifier());
                // System.out.println("*** before rx.Save(" + scriptId.toString() + ")");
                rx.Save(scriptId);
                auditStr.append(rx.getAuditString());
                auditStr.append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            p("rx.getAuditString()", rx.getAuditString());
            /* Save annotation */
            HttpSession se = request.getSession();
            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
            CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
            String attrib_name = attrib_names.get(i);
            p("attrib_names.get(i)", attrib_names.get(i));
            if (attrib_name != null) {
                CaseManagementNote cmn = (CaseManagementNote) se.getAttribute(attrib_name);
                if (cmn != null) {
                    cmm.saveNoteSimple(cmn);
                    CaseManagementNoteLink cml = new CaseManagementNoteLink();
                    cml.setTableName(CaseManagementNoteLink.DRUGS);
                    cml.setTableId((long) rx.getDrugId());
                    cml.setNoteId(cmn.getId());
                    cmm.saveNoteLink(cml);
                    se.removeAttribute(attrib_name);
                    LogAction.addLog(cmn.getProviderNo(), LogConst.ANNOTATE, CaseManagementNoteLink.DISP_PRESCRIP, scriptId, request.getRemoteAddr(), cmn.getDemographic_no(), cmn.getNote());
                }
            }
            rx = null;
        }

        String ip = request.getRemoteAddr();
        //    p("ip", ip);
        request.setAttribute("scriptId", scriptId);
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_PRESCRIPTION, scriptId, ip, "" + bean.getDemographicNo(), auditStr.toString());

        System.out.println("***===========End of saveDrug RxWriteScriptAction.java");
        return mapping.findForward("viewScript");
   }

    public ActionForward createNewRx(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        p("=============Start createNewRx RxWriteScriptAction.java===============");
        //    System.out.println("***IN RxChooseDrugAction.java");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        p("locale=" + locale.toString());
        p("message=" + messages.toString());

        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        if (bean == null) {
            response.sendRedirect("error.html");
            return null;
        }

        try {
            RxPrescriptionData rxData = new RxPrescriptionData();
            RxDrugData drugData = new RxDrugData();

            // create Prescription
            RxPrescriptionData.Prescription rx = rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo());

            String ra = request.getParameter("randomId");
            int randomId = Integer.parseInt(ra);
            rx.setRandomId(randomId);
            String drugId = request.getParameter("drugId");
            String text = request.getParameter("text");

            p("drugId", drugId);
            p("text", text);

            //TODO: Is this to slow to do here?  It's possible to do this in ajax,  as in when this comes back launch an ajax request to fill in.
            RxDrugData.DrugMonograph dmono = drugData.getDrug2(drugId);

            String brandName = text;
            //String genericName = request.getParameter("drugName");

            p("BRAND = " + brandName);
            rx.setGenericName(dmono.name); //TODO: how was this done before?
            rx.setBrandName(brandName);

            rx.setDrugForm(dmono.drugForm);

            //TO DO: cache the most used route from the drugs table.
            //for now, check to see if ORAL present, if yes use that, if not use the first one.
            boolean oral=false;
            for(int i=0;i<dmono.route.size();i++){
                if(((String)dmono.route.get(i)).equalsIgnoreCase("ORAL"))
                    oral=true;
            }
            if(oral)
                rx.setRoute("ORAL");
            else{
                if(dmono.route.size()>0){
                    rx.setRoute((String)dmono.route.get(0));
                }
            }
            //if user specified route in instructions, it'll be changed to the one specified.
            String dosage="";
            String unit="";
            Vector comps=(Vector)dmono.components;
            for (int i=0;i<comps.size();i++){
                RxDrugData.DrugMonograph.DrugComponent drugComp=(RxDrugData.DrugMonograph.DrugComponent)comps.get(i);
                String strength=drugComp.strength;
                unit=drugComp.unit;
                dosage=dosage+" "+strength+" "+unit;//get drug dosage from strength and unit.
            }

            rx.setDosage(dosage);
            rx.setUnit(unit);

            p("set drug form to ",rx.getDrugForm());
            p("set dosage to ",rx.getDosage());
            p("set unit to ",rx.getUnit());
            p("set route to ",rx.getRoute());


            rx.setGCN_SEQNO(Integer.parseInt(drugId));
            rx.setRegionalIdentifier(dmono.regionalIdentifier);
            p("set regional identifier to ", rx.getRegionalIdentifier());
            String atcCode = dmono.atc;
            rx.setAtcCode(atcCode);
            rx.setSpecial("1 OD");
            rx.setRepeat(0);
            rx.setQuantity("30");
            p("set atc code to ", rx.getAtcCode());

            bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
            bean.setStashIndex(bean.addStashItem(rx));
            p("brandName of rx", rx.getBrandName());
            p("stash index it's set to", "" + bean.getStashIndex());

            String today = null;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                today = dateFormat.format(calendar.getTime());
                p("today's date", today);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Date tod = RxUtil.StringToDate(today, "yyyy-MM-dd");
            rx.setRxDate(tod);
            rx.setWrittenDate(tod);
            rx.checkDiscontinued();//check and set if rx was discontinued before.
            List<RxPrescriptionData.Prescription> listRxDrugs=new ArrayList();
            listRxDrugs.add(rx);
            request.setAttribute("listRxDrugs",listRxDrugs);                     
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        p("=============END createNewRx RxWriteScriptAction.java===============");
        return (mapping.findForward("newRx"));
    }

    public ActionForward updateDrug(ActionMapping mapping, ActionForm aform, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        if (bean == null) {
            response.sendRedirect("error.html");
            return null;
        }

        String action = request.getParameter("action");

        if (action != null && action.equals("parseInstructions")) {
            System.out.println("==========***### IN parseInstruction RxWriteScriptAction.java");
          try{  Enumeration emm = request.getParameterNames();
            while (emm.hasMoreElements()) {
                p("request attribute=" + emm.nextElement().toString());
            }
            String randomId = request.getParameter("randomId");
            p("randomId from request",randomId);
            RxPrescriptionData.Prescription rx = bean.getStashItem2(Integer.parseInt(randomId));
            if(rx==null)
                p("rx is null");
            p("IN updateDrug, atc=" + rx.getAtcCode() + "; regionalIdentifier=" + rx.getRegionalIdentifier());

            String instructions = request.getParameter("instruction");
            p("instruction", instructions);

            HashMap retHm=RxUtil.instrucParser(instructions, rx);
            p("before updateDrug parseIntr bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
           // bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
            bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getIndexFromRx(Integer.parseInt(randomId))));
            p("updateDrug parseIntr bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
            // bean.setStashIndex(bean.addStashItem(rx));
            bean.setStashItem(bean.getIndexFromRx(Integer.parseInt(randomId)), rx);
            RxUtil.printStashContent(bean);
            HashMap hm = new HashMap();

            hm.put("method", rx.getMethod());
            hm.put("takeMin", rx.getTakeMin());//should be like 1-2 , min-max
            hm.put("takeMax", rx.getTakeMax());
            hm.put("duration", rx.getDuration());
            hm.put("frequency", rx.getFrequencyCode());
            hm.put("route", rx.getRoute());
            hm.put("durationUnit", rx.getDurationUnit());
            hm.put("prn", rx.getPrn());
            hm.put("calQuantity", rx.getQuantity());
            JSONObject jsonObject = JSONObject.fromObject(hm);
            p("jsonObject", jsonObject.toString());
            response.getOutputStream().write(jsonObject.toString().getBytes());
          }catch(Exception e){
            e.printStackTrace();
          }
            p("===================END parseInstruction RxWriteScriptAction.java======================");
            return null;
        }else if(action != null && action.equals("updateQty")){
            System.out.println("==========***### IN updateQty RxWriteScriptAction.java");
            String quantity = request.getParameter("quantity");
            String randomId = request.getParameter("randomId");
            RxPrescriptionData.Prescription rx = bean.getStashItem2(Integer.parseInt(randomId));
            //get rx from randomId
            if (quantity == null){
                quantity = "";
            }
            //check if quantity is same as rx.getquantity(), if yes, do nothing.
            if(quantity.equals(rx.getQuantity())){
                //do nothing
            }
            else{
                rx.setQuantity(quantity);
                String frequency=rx.getFrequencyCode();
                String takeMin=rx.getTakeMinString();
                String takeMax=rx.getTakeMaxString();
                String durationUnit=rx.getDurationUnit();
                double nPerDay=0d;
                double nDays=0d;
                 if(takeMin.equals("0") || takeMax.equals("0") ||frequency.equals("")){
                }else{
                    if(durationUnit.equals(""))
                        durationUnit="D";
                    if(frequency.equalsIgnoreCase("od"))
                        nPerDay=1;
                    else if(frequency.equalsIgnoreCase("bid"))
                        nPerDay=2;
                    else if(frequency.equalsIgnoreCase("tid"))
                        nPerDay=3;
                    else if(frequency.equalsIgnoreCase("qid"))
                        nPerDay=4;
                    else if(frequency.equalsIgnoreCase("Q1H"))
                        nPerDay=24;
                    else if(frequency.equalsIgnoreCase("Q2H"))
                        nPerDay=12;
                    else if(frequency.equalsIgnoreCase("Q1-2H"))
                        nPerDay=24;
                    else if(frequency.equalsIgnoreCase("Q3-4H"))
                        nPerDay=8;
                    else if(frequency.equalsIgnoreCase("Q4H"))
                        nPerDay=6;
                    else if(frequency.equalsIgnoreCase("Q4-6H"))
                        nPerDay=6;
                    else if(frequency.equalsIgnoreCase("Q6H"))
                        nPerDay=4;
                    else if(frequency.equalsIgnoreCase("Q8H"))
                        nPerDay=3;
                    else if(frequency.equalsIgnoreCase("Q12H"))
                        nPerDay=2;
                    else if(frequency.equalsIgnoreCase("QAM"))
                        nPerDay=1;
                    else if(frequency.equalsIgnoreCase("QPM"))
                        nPerDay=1;
                    else if(frequency.equalsIgnoreCase("QHS"))
                        nPerDay=1;
                    else if(frequency.equalsIgnoreCase("Q1Week"))
                        nPerDay=0.14285714285714285;
                    else if(frequency.equalsIgnoreCase("Q2Week"))
                        nPerDay=0.07142857142857142;
                    else if(frequency.equalsIgnoreCase("Q1Month"))
                        nPerDay=0.03333333333333333;
                    else if(frequency.equalsIgnoreCase("Q3Month"))
                        nPerDay=0.011111111111111112;

                    if(durationUnit.equalsIgnoreCase("D"))
                        nDays=1;
                    else if(durationUnit.equalsIgnoreCase("W"))
                        nDays=7;
                    else if(durationUnit.equalsIgnoreCase("M"))
                        nDays=30;

                    double qtyD=Double.parseDouble(quantity);
                    //quantity=takeMax * nDays * duration * nPerDay
                    double durD=qtyD/((Double.parseDouble(takeMax)) * nPerDay * nDays);
                    int durI=(int)durD;
                    rx.setDuration(Integer.toString(durI));
                    rx.setDurationUnit(durationUnit);
                }
                //duration=quantity divide by no. of pills per duration period.
                //if not, recalculate duration based on frequency if frequency is not empty
                //if there is already a duration uni present, use that duration unit. if not, set duration unit to days, and output duration in days

            }


            p("before updateDrug parseIntr bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
            bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getIndexFromRx(Integer.parseInt(randomId))));
            p("updateDrug parseIntr bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
            bean.setStashItem(bean.getIndexFromRx(Integer.parseInt(randomId)), rx);
            RxUtil.printStashContent(bean);
            HashMap hm = new HashMap();
            hm.put("method", rx.getMethod());
            hm.put("takeMin", rx.getTakeMin());
            hm.put("takeMax", rx.getTakeMax());
            hm.put("duration", rx.getDuration());
            hm.put("frequency", rx.getFrequencyCode());
            hm.put("route", rx.getRoute());
            hm.put("durationUnit", rx.getDurationUnit());
            hm.put("prn", rx.getPrn());
            hm.put("calQuantity", rx.getQuantity());
            JSONObject jsonObject = JSONObject.fromObject(hm);
            p("jsonObject", jsonObject.toString());
            response.getOutputStream().write(jsonObject.toString().getBytes());
            p("===================END updateQty RxWriteScriptAction.java======================");
            return null;
        }else{
        return null;
        }
    }

    public ActionForward updateAllDrugs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {
        System.out.println("==========***### IN updateAllDrugs RxWriteScriptAction.java");
        // Enumeration emm = request.getSession().getAttributeNames();
        //  while (emm.hasMoreElements()) {
        //      p("session attribute=" + emm.nextElement().toString());
        //  }
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");

        p("**## print out fields in bean==========");
        p(bean.getProviderNo());
        p(Integer.toString(bean.getDemographicNo()));
        //p(bean.getStash());
        p(Integer.toString(bean.getStashIndex()));
        // p(bean.getAllergyWarnings(fwd));
        p(Integer.toString(bean.getAttributeNames().size()));
        ArrayList attributeNames = new ArrayList();
        attributeNames = bean.getAttributeNames();
        for (int i = 0; i < attributeNames.size(); i++) {
            p(attributeNames.get(i).toString());
        }
        p("done print out fields in bean=========");

        //     p("bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
        List<String> paramList = new ArrayList();
        Enumeration em = request.getParameterNames();
        List<String> randNum = new ArrayList();
        while (em.hasMoreElements()) {
            String ele = em.nextElement().toString();
            System.out.println("ele>"+ele);
            paramList.add(ele);
            if ( ele.startsWith("drugName_")){
                String rNum = ele.substring(9);
                System.out.println("rNum:"+rNum);
                if(!randNum.contains(rNum)){
                   randNum.add(rNum);
                }
            }
        }
        p("here2 ran num size "+randNum.size());
        p("bean.getStashSize()", Integer.toString(bean.getStashSize()));

        List<Integer> allIndex=new ArrayList();
        for(int i=0;i<bean.getStashSize();i++){
            allIndex.add(i);
        }
        List<Integer> existingIndex=new ArrayList();

            //Iterator uniqueIterator = new UniqueFilterIterator(randNum.iterator());
            for(String num:randNum){

                p("num", num);

                int stashIndex=bean.getIndexFromRx(Integer.parseInt(num));
                existingIndex.add(stashIndex);
                p("stashIndex", Integer.toString(stashIndex));
                RxPrescriptionData.Prescription rx = bean.getStashItem(stashIndex);
                p("***item being updated, stash index",""+stashIndex);
                p("randomId",""+rx.getRandomId());
                p("generic name", rx.getGenericName());
                p("special",rx.getSpecial());
                p("quantity",rx.getQuantity());
                p("repeat=" + rx.getRepeat());
                p("atccode",rx.getAtcCode());
                p("regional identifier",rx.getRegionalIdentifier());
                p("---");

                boolean patientComplianceY = false;
                boolean patientComplianceN = false;
                boolean isOutsideProvider = false;
                boolean isLongTerm=false;
                boolean isPastMed=false;
                try {
                    em = request.getParameterNames();
                    while (em.hasMoreElements()) {
                        String elem = (String) em.nextElement();
                        String val = request.getParameter(elem);
                        System.out.println("paramName=" + elem + ", value=" + val);
                        if (elem.startsWith("drugName_" + num)) {
                           rx.setBrandName(val);
                        } else if (elem.equals("repeats_" + num)) {

                            if (val.equals("") || val == null) {
                                rx.setRepeat(0);
                            } else {
                                rx.setRepeat(Integer.parseInt(val));
                            }

                        } else if (elem.startsWith("instructions_" + num)) {
                            rx.setSpecial(val);
                        } else if (elem.equals("quantity_" + num)) {
                            if (val.equals("") || val == null) {
                                rx.setQuantity("0");
                            } else {
                                rx.setQuantity(val);
                            }
                        } else if (elem.equals("longTerm_" + num)) {
                            if (val.equals("on")) {
                                isLongTerm=true;
                            } else {
                                isLongTerm=false;
                            }
                        } else if (elem.equals("lastRefillDate_" + num)) {
                            rx.setLastRefillDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
                        } else if (elem.equals("outsideProviderName_" + num)) {
                            rx.setOutsideProviderName(val);
                        } else if (elem.equals("rxDate_" + num)) {
                            //     p("paramName is rxDate!!");
                            if ((val == null) || (val.equals(""))) {
                                rx.setRxDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
                            } else {
                                rx.setRxDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
                            }
                        } else if (elem.equals("writtenDate_" + num)) {
                            if (val == null || (val.equals(""))) {
                                p("writtenDate is null");
                                rx.setRxDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
                            } else {
                                rx.setWrittenDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
                            }

                        } else if (elem.equals("outsideProviderName_" + num)) {
                            rx.setOutsideProviderName(val);
                        } else if (elem.equals("outsideProviderOhip_" + num)) {
                            if (val.equals("") || val == null) {
                                rx.setOutsideProviderOhip("0");
                            } else {
                                rx.setOutsideProviderOhip(val);
                            }
                        } else if (elem.equals("rxFreq_" + num)) {
                            rx.setFrequencyCode(val);
                        } else if (elem.equals("rxDuration_" + num)) {
                            if (val.equals("") || val == null) {
                                rx.setDuration("0");
                            } else {
                                rx.setDuration(val);
                            }
                        } else if (elem.equals("rxDurationUnit_" + num)) {
                            rx.setDurationUnit(val);
                        }  else if (elem.equals("ocheck_" + num)) {
                            if (val.equals("on")) {
                                isOutsideProvider = true;
                            } else {
                                isOutsideProvider = false;
                            }
                        } else if (elem.equals("pastMed_" + num)) {
                            if (val.equals("on")) {
                                isPastMed=true;
                            } else {
                                isPastMed=false;
                            }
                        } else if (elem.equals("patientComplianceY_" + num)) {
                            if (val.equals("on")) {
                                patientComplianceY = true;
                            } else {
                                patientComplianceY = false;
                            }
                        } else if (elem.equals("patientComplianceN_" + num)) {
                            if (val.equals("on")) {
                                patientComplianceN = true;
                            } else {
                                patientComplianceN = false;
                            }
                        } else if (elem.equals("rxRoute_" + num)) {
                            rx.setRoute(val);
                        } else if (elem.equals("rxMethod_" + num)) {
                            rx.setMethod(val);
                        } else if (elem.equals("rxAmount_" + num)) {
                            p("amount here", val);
                            if (val.equals("") || val == null) {
                                rx.setTakeMin(0f);
                            } else {
                                rx.setTakeMin(Float.parseFloat(val));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                  p("outside of the try {}");

                try {
                    if (!isOutsideProvider) {
                        rx.setOutsideProviderName("");
                        rx.setOutsideProviderOhip("");
                    }
                    rx.setPastMed(isPastMed);
                    rx.setLongTerm(isLongTerm);
                    String newline = System.getProperty("line.separator");
                    rx.setPatientCompliance(patientComplianceY, patientComplianceN);
                    String special =  rx.getBrandName() + newline + rx.getSpecial()+ newline + "Qty:" + rx.getQuantity() + " Repeats:" + ""+rx.getRepeat() ;
                    //     p("here222");
                    rx.setSpecial(special);
                    System.out.println("SETTING SPECIAL TOO >"+special+"<");
                    //         p("rx.getDuration()", rx.getDuration());
                    int duration;
                    if (rx.getDuration() == null || rx.getDuration().equals("")) {
                        duration = 0;
                    } else {
                        duration = Integer.parseInt(rx.getDuration());
                    }

                    //       p("here1111");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(rx.getRxDate());
                    DateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
                    //       p("cal", ft.format(cal.getTime()));
                    cal.add(Calendar.DATE, duration);
                    String end = ft.format(cal.getTime());
                    //       p("after addition", end);
                    Date endDate = (Date) ft.parse(end);
                    //       p("freqcode as", rx.getFrequencyCode());
                    //       p("here2");
                    //       p("here5");
                    //       p(rx.getGenericName());
                    
                    Long rand = Math.round(Math.random() * 1000000);
                    rx.setRegionalIdentifier(Long.toString(rand));
                    rx.setAtcCode(Long.toString(rand + 1));
                    System.out.println("----"+rx.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            p("the rxDate is in updateDrug", RxUtil.DateToString(rx.getRxDate()));
            p("duration after updated", rx.getDuration());

            bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(stashIndex));
            p("before bean.getStashIndex()", Integer.toString(stashIndex));
            bean.setStashItem(stashIndex, rx);
            p("brand name of updated rx", rx.getBrandName());
            p("stash index of updated rx", Integer.toString(bean.getStashIndex()));

            }
            for(Integer n:existingIndex){
                if(allIndex.contains(n))
                    allIndex.remove(n);
            }
            List<Integer> deletedIndex=allIndex;
            //remove closed Rx from stash

            for(Integer n: deletedIndex){
                p("stash index of closed rx",""+n);
                bean.removeStashItem(n);
                p("bean.getStashIndex()",""+bean.getStashIndex());
                p("bean.getStashSize()",""+bean.getStashSize());
                if(bean.getStashIndex() >= bean.getStashSize()) {
                       bean.setStashIndex(bean.getStashSize() - 1);
                }
                //update rx.setStashId
            }

        System.out.println("***===========End of updateAllDrugs RxWriteScriptAction.java");
        return mapping.findForward("viewScript");
        }
    }
