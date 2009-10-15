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

    public ActionForward Take(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
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
        System.out.println("***===========End of Take RxWriteScriptAction.java");
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

        /*   while (uniqueIterator.hasNext()) {
        String num = uniqueIterator.next().toString();
        //        p("num", num);
        //get the val of params and save them.
        //        p("bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
        RxDrugData drugData = new RxDrugData();

        RxPrescriptionData rxData = new RxPrescriptionData();
        RxPrescriptionData.Prescription rx = rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo());

        RxPrescriptionData prescription = new RxPrescriptionData();
        boolean patientComplianceY = false;
        boolean patientComplianceN = false;
        boolean isOutsideProvider = false;
        try {
        em = request.getParameterNames();
        while (em.hasMoreElements()) {
        String elem = (String) em.nextElement();
        String val = request.getParameter(elem);
        //     System.out.println("paramName=" + elem + ", value=" + val);
        if (elem.startsWith("drugName_" + num)) {
        rx.setGenericName(val);
        } else if (elem.equals("repeats_" + num)) {

        if (val.equals("")|| val==null) {
        rx.setRepeat(0);
        } else {
        rx.setRepeat(Integer.parseInt(val));
        }

        } else if (elem.startsWith("instructions_" + num)) {
        rx.setSpecial(val);
        } else if (elem.equals("quantity_" + num)) {
        if(val.equals("")||val==null){
        rx.setQuantity("0");
        }
        else {rx.setQuantity(val);}
        } else if (elem.equals("longTerm_" + num)) {
        if (val.equals("on")) {
        rx.setLongTerm(true);
        } else {
        rx.setLongTerm(false);
        }
        } else if (elem.equals("lastRefillDate_" + num)) {
        rx.setLastRefillDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
        } else if (elem.equals("outsideProviderName_" + num)) {
        rx.setOutsideProviderName(val);
        } else if (elem.equals("rxDate_" + num)) {
        //     p("paramName is rxDate!!");
        if ((val == null) || (val.equals(""))) {
        //p("rxDate is null");
        Date d = RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd");
        //p(RxUtil.DateToString(d));
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
        if(val.equals("")||val==null){
        rx.setOutsideProviderOhip("0");
        }
        else {rx.setOutsideProviderOhip(val);}
        } else if (elem.equals("rxFreq_" + num)) {
        rx.setFrequencyCode(val);
        } else if (elem.equals("rxDuration_" + num)) {
        if(val.equals("")||val==null){
        rx.setDuration("0");
        }
        else {rx.setDuration(val);}
        } else if (elem.equals("rxDurationUnit_" + num)) {
        rx.setDurationUnit(val);
        } else if (elem.equals("rxPRN_" + num)) {
        if (val.equals("on")) {
        rx.setPrn(true);
        } else {
        rx.setPrn(false);
        }
        } else if (elem.equals("otext_" + num)) {
        if (val.equals("on")) {
        isOutsideProvider = true;
        } else {
        isOutsideProvider = false;
        }
        } else if (elem.equals("pastMed_" + num)) {
        if (val.equals("on")) {
        rx.setPastMed(true);
        } else {
        rx.setPastMed(false);
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
        //  p("here1");

        try {
        if (!isOutsideProvider) {
        rx.setOutsideProviderName("");
        rx.setOutsideProviderOhip("");
        }
        String newline = System.getProperty("line.separator");
        rx.setPatientCompliance(patientComplianceY, patientComplianceN);
        String special = newline+rx.getGenericName() + newline + rx.getSpecial();
        //     p("here222");
        rx.setSpecial(special);
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
        rx.setBrandName(rx.getGenericName());
        Long rand=Math.round(Math.random()*1000000);
        rx.setRegionalIdentifier(Long.toString(rand));
        rx.setAtcCode(Long.toString(rand+1));
        } catch (Exception e) {
        e.printStackTrace();
        }
        String scriptId = prescription.saveScript(bean);
        //      System.out.println("*** before rx.Save(" + scriptId.toString() + ")");

        try {
        rx.Save(scriptId);
        } catch (NullPointerException e) {
        e.printStackTrace();
        }
        }*/



        //     System.out.println("==========***### end of saveDrug RxWriteScriptAction.java");

    }

    public ActionForward parseInstruction(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {
        System.out.println("==========***### IN parseInstruction RxWriteScriptAction.java");
        String s = request.getParameter("instruction");
        p("instruction", s);
        Enumeration emm = request.getParameterNames();
        while (emm.hasMoreElements()) {
            p("request attribute=" + emm.nextElement().toString());
        }
        StringTokenizer st = new StringTokenizer(s);
        String[] strArray = new String[st.countTokens()];
        //    p(Integer.toString(st.countTokens()));
        int i = 0;
        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            strArray[i] = str;
            i++;
        }
        String amount = "";
        String route = "";
        String frequency = "";
        String form = "";
        String duration = "";
        String method = "";
        char durationUnit = ' ';
        boolean prn = false;

        p(Integer.toString(strArray.length));
        for (int j = 0; j < strArray.length; j++) {
            //      p(Integer.toString(j));
            String sa = strArray[j];

            if (sa.equalsIgnoreCase("take") || sa.equalsIgnoreCase("apply") || sa.equalsIgnoreCase("rub well in")) {
                method = sa;
                //get number after take, use regular expression.
                try {
                    int n = Integer.parseInt(strArray[j + 1]);
                    amount = Integer.toString(n);
                } catch (NumberFormatException e) {
                    //get the number from the string.
                    }
            } else if (sa.equalsIgnoreCase("PO") || sa.equalsIgnoreCase("SL") || sa.equalsIgnoreCase("IM") ||
                    sa.equalsIgnoreCase("TOP.") || sa.equalsIgnoreCase("PATCH") || sa.equalsIgnoreCase("SC") ||
                    sa.equalsIgnoreCase("INH") || sa.equalsIgnoreCase("SUPP") || sa.equalsIgnoreCase("O.D.") ||
                    sa.equalsIgnoreCase("O.S.") || sa.equalsIgnoreCase("O.U.")) //PO|SL|IM|SC|PATCH|TOP.|INH|SUPP|O.D.|O.S.|O.U.
            {
                route = sa;
            } else if (sa.equalsIgnoreCase("OD") || sa.equalsIgnoreCase("BID") || sa.equalsIgnoreCase("TID") || sa.equalsIgnoreCase("QID") ||
                    sa.equalsIgnoreCase("Q1H") || sa.equalsIgnoreCase("Q2H") || sa.equalsIgnoreCase("Q1-2H") || sa.equalsIgnoreCase("Q3-4H") ||
                    sa.equalsIgnoreCase("Q4H") || sa.equalsIgnoreCase("Q4-6H") || sa.equalsIgnoreCase("Q6H") || sa.equalsIgnoreCase("Q8H") ||
                    sa.equalsIgnoreCase("Q12H") || sa.equalsIgnoreCase("QAM") || sa.equalsIgnoreCase("QPM") || sa.equalsIgnoreCase("QHS") ||
                    sa.equalsIgnoreCase("Q1Week") || sa.equalsIgnoreCase("Q2Week") || sa.equalsIgnoreCase("Q1Month") || sa.equalsIgnoreCase("Q3Month")) //(OD|BID|TID|QID|Q1H|Q2H|Q1-2H|Q3-4H|Q4H|Q4-6H|Q6H|Q8H|Q12H|QAM|QPM|QHS|Q1Week|Q2Week|Q1Month|Q3Month)
            {
                frequency = sa;
            } else if (sa.equalsIgnoreCase("PRN")) {
                prn = true;
            } else if (sa.equalsIgnoreCase("days") || sa.equalsIgnoreCase("weeks") || sa.equalsIgnoreCase("months")) {
                char[] cArray = sa.toCharArray();
                durationUnit = cArray[0];
                duration = strArray[j - 1];
            }
        }

        int calQuantity = 0;
        if ((durationUnit == 'd') || (durationUnit == 'D')) {
            calQuantity = Integer.parseInt(duration) * Integer.parseInt(amount);
        } else if (durationUnit == 'w' || durationUnit == 'W') {
            calQuantity = Integer.parseInt(duration) * 7 * Integer.parseInt(amount);
        } else if (durationUnit == 'm' || durationUnit == 'M') {
            calQuantity = Integer.parseInt(duration) * 30 * Integer.parseInt(amount);
        } //assume each month is 30 days, more complicated implementation if exact number needed.


        Hashtable ha = new Hashtable();

        ha.put("method", method);
        ha.put("amount", amount);//should be like 1-2 , min-max
        ha.put("duration", duration);
        ha.put("frequency", frequency);
        ha.put("route", route);
        ha.put("durationUnit", durationUnit);
        ha.put("prn", prn);
        ha.put("calQuantity", calQuantity);
        JSONObject jsonObject = JSONObject.fromObject(ha);
        p("jsonObject", jsonObject.toString());
        response.getOutputStream().write(jsonObject.toString().getBytes());
        return null;
    }

    public ActionForward parseData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {

        HashMap hm = new HashMap();
        Enumeration em = request.getParameterNames();
        p("attr in request");
        while (em.hasMoreElements()) {
            System.out.println(em.nextElement());
        }
        em = request.getParameterNames();

        while (em.hasMoreElements()) {
            String elem = (String) em.nextElement();
            String val = request.getParameter(elem);

            if (elem.startsWith("drugName_")) {
                hm.put("drugName", val);
                hm.put("brandName", val);
            } else if (elem.startsWith("repeats_")) {
                hm.put("repeats", val);
            } else if (elem.startsWith("instructions_")) {
                hm.put("instructions", val);
            } else if (elem.startsWith("quantity_")) {
                hm.put("quantity", val);

            } else if (elem.startsWith("longTerm_")) {
                hm.put("longTerm", val);

            } else if (elem.startsWith("lastRefillDate_")) {
                hm.put("lastRefillDate", val);

            } else if (elem.startsWith("outsideProviderName_")) {
                hm.put("outsideProviderName", val);

            } else if (elem.startsWith("rxDate_")) {
                hm.put("rxDate", val);

            } else if (elem.startsWith("writtenDate_")) {
                hm.put("writtenDate", val);


            } else if (elem.startsWith("outsideProviderName_")) {
                hm.put("outsideProviderName", val);

            } else if (elem.startsWith("outsideProviderOhip_")) {
                hm.put("outsideProviderOhip", val);

            } else if (elem.startsWith("rxFreq_")) {
                hm.put("rxFreq", val);

            } else if (elem.startsWith("rxDuration_")) {
                hm.put("rxDuration", val);

            } else if (elem.startsWith("rxDurationUnit_")) {
                hm.put("rxDurationUnit", val);

            } else if (elem.startsWith("rxPRN_")) {
                hm.put("rxPRN", val);

            } else if (elem.startsWith("otext_")) {
                hm.put("otext", val);

            } else if (elem.startsWith("pastMed_")) {
                hm.put("pastMed", val);

            } else if (elem.startsWith("patientComplianceY_")) {
                hm.put("patientComplianceY", val);

            } else if (elem.startsWith("patientComplianceN_")) {
                hm.put("patientComplianceN", val);

            } else if (elem.startsWith("rxRoute_")) {
                hm.put("rxRoute", val);

            } else if (elem.startsWith("method")) {
                hm.put("rxMethod", val);

            } else if (elem.startsWith("rxAmount_")) {
                hm.put("rxAmount", val);

            }
        }

        JSONObject jsonObject = JSONObject.fromObject(hm);
        p("jsonObject in parseData", jsonObject.toString());
        response.getOutputStream().write(jsonObject.toString().getBytes());

        return null;
    }

    public ActionForward getAllRandomId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        p("=============Start getAllRandomId RxWriteScriptAction.java===============");
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        int[] arRand = new int[bean.getStashSize()];
        RxPrescriptionData.Prescription[] preAr = bean.getStash();
        int i = 0;
        for (RxPrescriptionData.Prescription p : preAr) {
            arRand[i] = (p.getRandomId());
            p("randId", "" + arRand[i]);
            i++;
        }
        HashMap hm = new HashMap();
        hm.put("arRand", arRand);
        JSONObject jsonObject = JSONObject.fromObject(hm);
        p("jsonObject", jsonObject.toString());
        response.getOutputStream().write(jsonObject.toString().getBytes());
        p("=============End getAllRandomId RxWriteScriptAction.java===============");
        return null;
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
            String countPrescribe = request.getParameter("countPrescribe");

            p("BRAND = " + brandName);
            rx.setGenericName(dmono.name); //TODO: how was this done before?
            rx.setBrandName(brandName);

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
            p("countPrescribe", countPrescribe);

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
        p("start bean.getStashIndex()", "" + bean.getStashIndex());
        RxDrugData drugData = new RxDrugData();
        RxPrescriptionData.Prescription rx = bean.getStashItem(bean.getStashIndex());
        RxPrescriptionData prescription = new RxPrescriptionData();

        p("IN updateDrug, atc=" + rx.getAtcCode() + "; regionalIdentifier=" + rx.getRegionalIdentifier());
        String action = request.getParameter("action");

        if (action != null && action.equals("parseInstructions")) {
            System.out.println("==========***### IN parseInstruction RxWriteScriptAction.java");
            String s = request.getParameter("instruction");
            p("instruction", s);
            Enumeration emm = request.getParameterNames();
            while (emm.hasMoreElements()) {
                p("request attribute=" + emm.nextElement().toString());
            }
            StringTokenizer st = new StringTokenizer(s);
            String[] strArray = new String[st.countTokens()];
            //    p(Integer.toString(st.countTokens()));
            int i = 0;
            while (st.hasMoreTokens()) {
                String str = st.nextToken();
                strArray[i] = str;
                i++;
            }
            String amount = "0";
            String route = "";
            String frequency = "";
            String form = "";
            String duration = "0";
            String method = "";
            char durationUnit = ' ';
            boolean prn = false;

            p("array size", Integer.toString(strArray.length));
            for (int j = 0; j < strArray.length; j++) {
                //      p(Integer.toString(j));
                String sa = strArray[j];
                p("sa", sa);

                if (sa.equalsIgnoreCase("take") || sa.equalsIgnoreCase("apply") || sa.equalsIgnoreCase("rub well in")) {
                    method = sa;
                    //get number after take, use regular expression.
                    try {
                        int n = Integer.parseInt(strArray[j + 1]);
                        amount = Integer.toString(n);
                    } catch (NumberFormatException e) {
                        //get the number from the string.
                    }
                } else if (sa.equalsIgnoreCase("PO") || sa.equalsIgnoreCase("SL") || sa.equalsIgnoreCase("IM") ||
                        sa.equalsIgnoreCase("TOP.") || sa.equalsIgnoreCase("PATCH") || sa.equalsIgnoreCase("SC") ||
                        sa.equalsIgnoreCase("INH") || sa.equalsIgnoreCase("SUPP") || sa.equalsIgnoreCase("O.D.") ||
                        sa.equalsIgnoreCase("O.S.") || sa.equalsIgnoreCase("O.U.")) //PO|SL|IM|SC|PATCH|TOP.|INH|SUPP|O.D.|O.S.|O.U.
                {
                    route = sa;
                } else if (sa.equalsIgnoreCase("OD") || sa.equalsIgnoreCase("BID") || sa.equalsIgnoreCase("TID") || sa.equalsIgnoreCase("QID") ||
                        sa.equalsIgnoreCase("Q1H") || sa.equalsIgnoreCase("Q2H") || sa.equalsIgnoreCase("Q1-2H") || sa.equalsIgnoreCase("Q3-4H") ||
                        sa.equalsIgnoreCase("Q4H") || sa.equalsIgnoreCase("Q4-6H") || sa.equalsIgnoreCase("Q6H") || sa.equalsIgnoreCase("Q8H") ||
                        sa.equalsIgnoreCase("Q12H") || sa.equalsIgnoreCase("QAM") || sa.equalsIgnoreCase("QPM") || sa.equalsIgnoreCase("QHS") ||
                        sa.equalsIgnoreCase("Q1Week") || sa.equalsIgnoreCase("Q2Week") || sa.equalsIgnoreCase("Q1Month") || sa.equalsIgnoreCase("Q3Month")) //(OD|BID|TID|QID|Q1H|Q2H|Q1-2H|Q3-4H|Q4H|Q4-6H|Q6H|Q8H|Q12H|QAM|QPM|QHS|Q1Week|Q2Week|Q1Month|Q3Month)
                {
                    frequency = sa;
                } else if (sa.equalsIgnoreCase("PRN")) {
                    prn = true;
                } else if (sa.equalsIgnoreCase("days") || sa.equalsIgnoreCase("weeks") || sa.equalsIgnoreCase("months")) {
                    char[] cArray = sa.toCharArray();
                    durationUnit = Character.toUpperCase(cArray[0]);
                    duration = strArray[j - 1];
                } else if (sa.indexOf("Days") != -1) {
                    int n = sa.indexOf("Days");
                    durationUnit = 'D';
                    duration = sa.substring(0, n);
                } else if (sa.indexOf("Weeks") != -1) {
                    int n = sa.indexOf("Weeks");
                    durationUnit = 'W';
                    duration = sa.substring(0, n);
                } else if (sa.indexOf("Months") != -1) {
                    int n = sa.indexOf("Months");
                    durationUnit = 'M';
                    duration = sa.substring(0, n);
                }
            }
            p("here");
            int calQuantity = 0;
            if ((durationUnit == 'd') || (durationUnit == 'D')) {
                calQuantity = Integer.parseInt(duration) * Integer.parseInt(amount);
            } else if (durationUnit == 'w' || durationUnit == 'W') {
                calQuantity = Integer.parseInt(duration) * 7 * Integer.parseInt(amount);
            } else if (durationUnit == 'm' || durationUnit == 'M') {
                calQuantity = Integer.parseInt(duration) * 30 * Integer.parseInt(amount);
            } //assume each month is 30 days, more complicated implementation if exact number needed.
            p("here2");
            rx.setMethod(method);
            rx.setTakeMin(Integer.parseInt(amount));
            rx.setDuration(duration);
            rx.setFrequencyCode(frequency);
            rx.setRoute(route);
            rx.setDurationUnit(String.valueOf(durationUnit));
            rx.setPrn(prn);
            p("before updateDrug parseIntr bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
            bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
            p("updateDrug parseIntr bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
            // bean.setStashIndex(bean.addStashItem(rx));
            bean.setStashItem(bean.getStashIndex(), rx);
            //   p("after bean.getStashIndex()",Integer.toString(bean.getStashIndex()));

            HashMap hm = new HashMap();

            hm.put("method", method);
            hm.put("amount", amount);//should be like 1-2 , min-max
            hm.put("duration", duration);
            hm.put("frequency", frequency);
            hm.put("route", route);
            hm.put("durationUnit", durationUnit);
            hm.put("prn", prn);
            hm.put("calQuantity", calQuantity);
            JSONObject jsonObject = JSONObject.fromObject(hm);
            p("jsonObject", jsonObject.toString());
            response.getOutputStream().write(jsonObject.toString().getBytes());
            p("===================END parseInstruction RxWriteScriptAction.java======================");
            return null;
        } else {
            System.out.println("                                                                   ============IN updateDrug RxWriteScriptAction.java=============");


            // Extract attributes we will need
            Enumeration em = request.getParameterNames();
            p("attr in request");
            while (em.hasMoreElements()) {
                System.out.println(em.nextElement());
            }
            Locale locale = getLocale(request);
            MessageResources messages = getResources(request);
            p("locale=" + locale.toString());
            p("message=" + messages.toString());
            // Setup variables            

            boolean patientComplianceY = false;
            boolean patientComplianceN = false;
            boolean isOutsideProvider = false;


            try {
                em = request.getParameterNames();
                while (em.hasMoreElements()) {
                    String elem = (String) em.nextElement();
                    String val = request.getParameter(elem);
                    if (elem.startsWith("drugName_")) {
                        rx.setGenericName(val);
                    } else if (elem.startsWith("repeats_")) {

                        if (val.equals("") || val == null) {
                            rx.setRepeat(0);
                        } else {
                            rx.setRepeat(Integer.parseInt(val));
                        }

                    } else if (elem.startsWith("instructions_")) {
                        p("instruction in updateDrugs set to", val);
                        rx.setSpecial(val);
                    } else if (elem.startsWith("quantity_")) {
                        if (val.equals("") || val == null) {
                            rx.setQuantity("0");
                        } else {
                            rx.setQuantity(val);
                        }
                    } else if (elem.startsWith("longTerm_")) {
                        if (val.equals("on")) {
                            rx.setLongTerm(true);
                        } else {
                            rx.setLongTerm(false);
                        }
                    } else if (elem.startsWith("lastRefillDate_")) {
                        rx.setLastRefillDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
                    } else if (elem.startsWith("outsideProviderName_")) {
                        rx.setOutsideProviderName(val);
                    } else if (elem.startsWith("rxDate_")) {
                        //     p("paramName is rxDate!!");
                        if ((val == null) || (val.equals(""))) {
                            //p("rxDate is null");
                            Date d = RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd");
                            //p(RxUtil.DateToString(d));
                            rx.setRxDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
                        } else {
                            rx.setRxDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
                        }
                    } else if (elem.startsWith("writtenDate_")) {
                        if (val == null || (val.equals(""))) {
                            p("writtenDate is null");
                            rx.setRxDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
                        } else {
                            rx.setWrittenDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
                        }

                    } else if (elem.startsWith("outsideProviderName_")) {
                        rx.setOutsideProviderName(val);
                    } else if (elem.startsWith("outsideProviderOhip_")) {
                        if (val.equals("") || val == null) {
                            rx.setOutsideProviderOhip("0");
                        } else {
                            rx.setOutsideProviderOhip(val);
                        }
                    } else if (elem.startsWith("rxFreq_")) {
                        rx.setFrequencyCode(val);
                    } else if (elem.startsWith("rxDuration_")) {
                        if (val.equals("") || val == null || val.equals("null")) {
                            p("duration if case");
                            rx.setDuration("0");
                        } else {
                            p("druation else case");
                            rx.setDuration(val);
                        }
                    } else if (elem.startsWith("rxDurationUnit_")) {
                        rx.setDurationUnit(val);
                    } else if (elem.startsWith("rxPRN_")) {
                        if (val.equals("on")) {
                            rx.setPrn(true);
                        } else {
                            rx.setPrn(false);
                        }
                    } else if (elem.startsWith("otext_")) {
                        if (val.equals("on")) {
                            isOutsideProvider = true;
                        } else {
                            isOutsideProvider = false;
                        }
                    } else if (elem.startsWith("pastMed_")) {
                        if (val.equals("on")) {
                            rx.setPastMed(true);
                        } else {
                            rx.setPastMed(false);
                        }
                    } else if (elem.startsWith("patientComplianceY_")) {
                        if (val.equals("on")) {
                            patientComplianceY = true;
                        } else {
                            patientComplianceY = false;
                        }
                    } else if (elem.startsWith("patientComplianceN_")) {
                        if (val.equals("on")) {
                            patientComplianceN = true;
                        } else {
                            patientComplianceN = false;
                        }
                    } else if (elem.startsWith("rxRoute_")) {
                        rx.setRoute(val);
                    } else if (elem.startsWith("rxMethod_")) {
                        rx.setMethod(val);
                    } else if (elem.startsWith("rxAmount_")) {
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
            p("here1");

            try {
                if (!isOutsideProvider) {
                    rx.setOutsideProviderName("");
                    rx.setOutsideProviderOhip("");
                }
                String newline = System.getProperty("line.separator");
                rx.setPatientCompliance(patientComplianceY, patientComplianceN);
                //add generic name to special
                //  String special = newline + rx.getGenericName() + newline + rx.getSpecial();
                //     p("here222");
                //  rx.setSpecial(special);
                //         p("rx.getDuration()", rx.getDuration());
                int duration;
                if (rx.getDuration() == null || rx.getDuration().equals("")) {
                    duration = 0;
                } else {
                    duration = Integer.parseInt(rx.getDuration());
                }

                rx.setBrandName(rx.getGenericName());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            if (rx.getRxDate() == null) {
                p("SET startdate to TODAY DATE");
                rx.setRxDate(tod);
            }
            if (rx.getWrittenDate() == null) {
                p("SET writtenDate to TODAY DATE");
                rx.setWrittenDate(tod);
            }
            p("the rxDate is in updateDrug", RxUtil.DateToString(rx.getRxDate()));
            p("duration after updated", rx.getDuration());
            bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
            p("before bean.getStashIndex()", Integer.toString(bean.getStashIndex()));
            // bean.setStashIndex(bean.addStashItem(rx));
            bean.setStashItem(bean.getStashIndex(), rx);
            p("brand name of updated rx", rx.getBrandName());
            p("stash index of updated rx", Integer.toString(bean.getStashIndex()));

            System.out.println("==========END updateDrug RxWriteScriptAction.java==========");
            return (mapping.findForward("success"));
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

            //Iterator uniqueIterator = new UniqueFilterIterator(randNum.iterator());
            for(String num:randNum){
           
                p("num", num);

                int stashIndex=bean.getIndexFromRx(Integer.parseInt(num));
                p("stashIndex", Integer.toString(stashIndex));
                RxPrescriptionData.Prescription rx = bean.getStashItem(stashIndex);

                boolean patientComplianceY = false;
                boolean patientComplianceN = false;
                boolean isOutsideProvider = false;
                try {
                    em = request.getParameterNames();
                    while (em.hasMoreElements()) {
                        String elem = (String) em.nextElement();
                        String val = request.getParameter(elem);
                        System.out.println("paramName=" + elem + ", value=" + val);
                        if (elem.startsWith("drugName_" + num)) {
                            rx.setGenericName(val);
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
                                rx.setLongTerm(true);
                            } else {
                                rx.setLongTerm(false);
                            }
                        } else if (elem.equals("lastRefillDate_" + num)) {
                            rx.setLastRefillDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
                        } else if (elem.equals("outsideProviderName_" + num)) {
                            rx.setOutsideProviderName(val);
                        } else if (elem.equals("rxDate_" + num)) {
                            //     p("paramName is rxDate!!");
                            if ((val == null) || (val.equals(""))) {
                                //p("rxDate is null");
                                Date d = RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd");
                                //p(RxUtil.DateToString(d));
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
                        } else if (elem.equals("rxPRN_" + num)) {
                            if (val.equals("on")) {
                                rx.setPrn(true);
                            } else {
                                rx.setPrn(false);
                            }
                        } else if (elem.equals("otext_" + num)) {
                            if (val.equals("on")) {
                                isOutsideProvider = true;
                            } else {
                                isOutsideProvider = false;
                            }
                        } else if (elem.equals("pastMed_" + num)) {
                            if (val.equals("on")) {
                                rx.setPastMed(true);
                            } else {
                                rx.setPastMed(false);
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
                  p("outside of the try {");

                try {
                    if (!isOutsideProvider) {
                        rx.setOutsideProviderName("");
                        rx.setOutsideProviderOhip("");
                    }
                    String newline = System.getProperty("line.separator");
                    rx.setPatientCompliance(patientComplianceY, patientComplianceN);
                    String special = newline + rx.getGenericName() + newline + rx.getSpecial();
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
                    rx.setBrandName(rx.getGenericName());
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
        System.out.println("***===========End of updateAllDrugs RxWriteScriptAction.java");
        return mapping.findForward("viewScript");
        }
    }

  