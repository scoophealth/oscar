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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.MyDateFormat;
import oscar.OscarProperties;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.MSP.MSPBillingNote;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingNote;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;

public class BillingSaveBillingAction extends Action {

    private static Logger log = MiscUtils.getLogger();
    AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (request.getSession().getAttribute("user") == null) {
            return (mapping.findForward("Logout"));
        }

        BillingSaveBillingForm frm = (BillingSaveBillingForm) form;

        oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean = (BillingSessionBean) request.getSession().getAttribute("billingSessionBean");
        //  oscar.oscarBilling.data.BillingStoreData bsd = new oscar.oscarBilling.data.BillingStoreDate();
        //  bsd.storeBilling(bean);

        //Get rid of this
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        BillingmasterDAO billingmasterDAO = (BillingmasterDAO) ctx.getBean("BillingmasterDAO");
        MiscUtils.getLogger().debug("appointment_no---: " + bean.getApptNo());
        oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
        
        Date curDate = new Date();
        String billingid = "";
        ArrayList<String> billingIds = new ArrayList<String>();
        String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");
        String billingMasterId = "";

        if (bean.getApptNo() == null || bean.getApptNo().equalsIgnoreCase("null")){
            bean.setApptNo("0");
        }

        ////////////
        if (bean.getApptNo() != null && !bean.getApptNo().trim().equals("0") &&  !bean.getApptNo().trim().equals("")){
            String apptStatus = "";
            Appointment result = appointmentDao.find(Integer.parseInt(bean.getApptNo()));
           if (result == null) {
                log.error("LLLOOK: APPT ERROR - APPT ("+bean.getApptNo()+") NOT FOUND - FOR demo:" + bean.getPatientName() +" date " + curDate);
            } else {
                apptStatus = result.getStatus();
            }
            String billStatus = as.billStatus(apptStatus);
            ///Update Appointment information
            log.debug("appointment_no: " + bean.getApptNo());
            log.debug("BillStatus:" + billStatus);
            Appointment appt = appointmentDao.find(Integer.parseInt(bean.getApptNo()));
            appointmentArchiveDao.archiveAppointment(appt);
            int rowsAffected = 0;
            if(appt != null) {
            	appt.setStatus(billStatus);
            	appt.setLastUpdateUser(bean.getCreator());
            	appointmentDao.merge(appt);
            }
            
            if (rowsAffected<1) log.error("LLLOOK: APPT ERROR - CANNOT UPDATE APPT ("+bean.getApptNo()+") FOR demo:" + bean.getPatientName() +" date " + curDate);
        }


        char billingAccountStatus = getBillingAccountStatus( bean);

       

        ArrayList<oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem> billItem = bean.getBillItem();

        char paymentMode = (bean.getEncounter().equals("E") && !bean.getBillingType().equals("ICBC") && !bean.getBillingType().equals("Pri") && !bean.getBillingType().equals("WCB")) ? 'E' : '0';

        String billedAmount;
      
        for (oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem bItem: billItem){

            Billing billing = getBillingObj(bean, curDate, billingAccountStatus);
            if(request.getParameter("dispPrice+"+bItem.getServiceCode())!= null){
                String updatedPrice = request.getParameter("dispPrice+"+bItem.getServiceCode());
                log.debug(bItem.getServiceCode()+"Original "+bItem.price+ " updated price "+Double.parseDouble(updatedPrice));
                bItem.price = Double.parseDouble(updatedPrice);
                bItem.getLineTotal();
            }

            billingmasterDAO.save(billing);
            billingid = ""+billing.getId(); 
           
            billingIds.add(billingid);
            if (paymentMode == 'E') {
                billedAmount = "0.00";
            } else {
                billedAmount =  bItem.getDispLineTotal();
            }

            Billingmaster billingmaster = saveBill(billingid, "" + billingAccountStatus, dataCenterId, billedAmount, "" + paymentMode, bean, bItem);//billItem.get(i));

            String WCBid = request.getParameter("WCBid");
            MiscUtils.getLogger().debug("WCB:"+WCBid);
            if (bean.getBillingType().equals("WCB")) {
                billingmaster.setWcbId(Integer.parseInt(bean.getWcbId()));
            }
            billingmasterDAO.save(billingmaster);
            billingMasterId = "" + billingmaster.getBillingmasterNo();
            this.createBillArchive(billingMasterId);

            //Changed March 8th to be included side this loop,  before only one billing would get this information.
            if (bean.getCorrespondenceCode().equals("N") || bean.getCorrespondenceCode().equals("B")) {
               
                MSPBillingNote n = new MSPBillingNote();
                n.addNote(billingMasterId, bean.getCreator(), bean.getNotes());
                
            }
            if (bean.getMessageNotes() != null || !bean.getMessageNotes().trim().equals("")) {
                BillingNote n = new BillingNote();
                n.addNote(billingMasterId, bean.getCreator(), bean.getMessageNotes());
            }
        }

        if (bean.getBillingType().equals("WCB")) {

            // HOW TO DO THIS PART
            /* Need to link the id of a WCB for with a bill
                -Continue to put it in the WCB form ?   + no data structure change - not sure how will it work.
                On submission how would this work??  for each bill submission that would look for it's id in the wcb table?
                The problem is that it's not really logical but it would work.  Not every form would have a billing.


                -Add a field to Billingmaster?   + data structure change + data migration + initial reaction
                Data conversion wouldn't be that big of a deal though.  because everything else would be coming over too.
                Most logical

                -Add a separate table ?       + data structure change + data migration + 2nd initial reacion

             */
            MiscUtils.getLogger().debug("WCB BILL!!");


        }


        ActionForward af = mapping.findForward("success");
        if (frm.getSubmit().equals("Another Bill")) {
            bean.setBillForm("GP");   //Todo: is this what this should be?
            af = mapping.findForward("anotherBill");

        } else if (frm.getSubmit().equals("Save & Print Receipt")) {
            StringBuilder stb = new StringBuilder();
            for (String s : billingIds) {
                log.debug("String " + s);
                stb.append("billing_no=" + s + "&");
            }
            log.debug("FULL STRING " + stb.toString());
            af = new ActionForward("/billing/CA/BC/billingView.do?" + stb.toString() + "receipt=yes");
            af.setRedirect(true);
        }
        return af; //(mapping.findForward("success"));
    }


    private Billing getBillingObj(final oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean, final Date curDate, final char billingAccountStatus){

        int apptNo = 0;
        try{
            apptNo = Integer.parseInt(bean.getApptNo());
        }catch(Exception e){apptNo=0;}


        Billing bill = new Billing();
        bill.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
        bill.setProviderNo(bean.getBillingProvider());
        bill.setAppointmentNo(apptNo);
        bill.setDemographicName(bean.getPatientName());
        bill.setHin(bean.getPatientPHN());
        bill.setUpdateDate(curDate);
        bill.setBillingDate(MyDateFormat.getSysDate(bean.getServiceDate()));
        bill.setTotal(bean.getGrandtotal());
        bill.setStatus(""+billingAccountStatus);
        bill.setDob(bean.getPatientDoB());
        bill.setVisitDate(MyDateFormat.getSysDate(bean.getAdmissionDate()));
        bill.setVisitType(bean.getVisitType());
        bill.setProviderOhipNo(bean.getBillingPracNo());
        bill.setApptProviderNo(bean.getApptProviderNo());
        bill.setCreator(bean.getCreator());
        bill.setBillingtype(bean.getBillingType());
        return bill;
    }

    private char getBillingAccountStatus(oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean){
        char billingAccountStatus = 'O';
        if (bean.getBillingType().equals("DONOTBILL")) {
            //bean.setBillingType("MSP"); //RESET this to MSP to get processed
            billingAccountStatus = 'N';
        } else if (bean.getBillingType().equals("WCB")) {
            billingAccountStatus = 'O';
        } else if (MSPReconcile.BILLTYPE_PRI.equals(bean.getBillingType())) {
            billingAccountStatus = 'P';
        }
        return billingAccountStatus;
    }

    public String convertDate8Char(String s) {
         String  sdate = "00000000", syear = "", smonth = "", sday = "";
        log.debug("s=" + s);
        if (s != null) {

            if (s.indexOf("-") != -1) {

                syear = s.substring(0, s.indexOf("-"));
                s = s.substring(s.indexOf("-") + 1);
                smonth = s.substring(0, s.indexOf("-"));
                if (smonth.length() == 1) {
                    smonth = "0" + smonth;
                }
                s = s.substring(s.indexOf("-") + 1);
                sday = s;
                if (sday.length() == 1) {
                    sday = "0" + sday;
                }

                log.debug("Year" + syear + " Month" + smonth + " Day" + sday);
                sdate = syear + smonth + sday;

            } else {
                sdate = s;
            }
            log.debug("sdate:" + sdate);
        } else {
            sdate = "00000000";

        }
        return sdate;
    }


    String moneyFormat(String str) {
        String moneyStr = "0.00";
        try {
            moneyStr = new java.math.BigDecimal(str).movePointLeft(2).toString();
        } catch (Exception moneyException) {
        	MiscUtils.getLogger().warn("warning",moneyException);
        }
        return moneyStr;
    }

    /**
     * Adds a new entry into the billing_history table
     * @param newInvNo String
     */
    private void createBillArchive(String billingMasterNo) {
        BillingHistoryDAO dao = new BillingHistoryDAO();
        dao.createBillingHistoryArchive(billingMasterNo);
    }

    private Billingmaster saveBill(String billingid, String billingAccountStatus, String dataCenterId, String billedAmount, String paymentMode, BillingSessionBean bean, oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem billItem) {
        return saveBill(billingid, billingAccountStatus, dataCenterId,billedAmount , paymentMode, bean,  "" + billItem.getUnit()  ,"" + billItem.getServiceCode());
    }
    private Billingmaster saveBill(String billingid, String billingAccountStatus, String dataCenterId, String billedAmount, String paymentMode, BillingSessionBean bean, String billingUnit,String serviceCode) {
        Billingmaster bill = new Billingmaster();

        bill.setBillingNo(Integer.parseInt(billingid));
        bill.setCreatedate(new Date());
        bill.setBillingstatus(billingAccountStatus);
        bill.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
        bill.setAppointmentNo(Integer.parseInt(bean.getApptNo()));
        bill.setClaimcode("C02");
        bill.setDatacenter(dataCenterId);
        bill.setPayeeNo(bean.getBillingGroupNo());
        bill.setPractitionerNo(bean.getBillingPracNo());
        bill.setPhn(bean.getPatientPHN());



        bill.setNameVerify(bean.getPatientFirstName(),bean.getPatientLastName());
        bill.setDependentNum(bean.getDependent());
        bill.setBillingUnit(billingUnit); //"" + billItem.getUnit());
        bill.setClarificationCode(bean.getVisitLocation().substring(0, 2));

        String anatomicalArea = "00";
        bill.setAnatomicalArea(anatomicalArea);
        bill.setAfterHour(bean.getAfterHours());
        String newProgram = "00";
        bill.setNewProgram(newProgram);
        bill.setBillingCode(serviceCode);//billItem.getServiceCode());
        bill.setBillAmount(billedAmount);
        bill.setPaymentMode(paymentMode);
        bill.setServiceDate(convertDate8Char(bean.getServiceDate()));
        bill.setServiceToDay(bean.getService_to_date());
        bill.setSubmissionCode(bean.getSubmissionCode());
        bill.setExtendedSubmissionCode(" ");
        bill.setDxCode1(bean.getDx1());
        bill.setDxCode2(bean.getDx2());
        bill.setDxCode3(bean.getDx3());
        bill.setDxExpansion(" ");

        bill.setServiceLocation(bean.getVisitType().substring(0, 1));
        bill.setReferralFlag1(bean.getReferType1());
        bill.setReferralNo1(bean.getReferral1());
        bill.setReferralFlag2(bean.getReferType2());
        bill.setReferralNo2(bean.getReferral2());
        bill.setTimeCall(bean.getTimeCall());
        bill.setServiceStartTime(bean.getStartTime());
        bill.setServiceEndTime(bean.getEndTime());
        bill.setBirthDate(convertDate8Char(bean.getPatientDoB()));
        bill.setOfficeNumber("");
        bill.setCorrespondenceCode(bean.getCorrespondenceCode());
        bill.setClaimComment(bean.getShortClaimNote());
        bill.setMvaClaimCode(bean.getMva_claim_code());
        bill.setIcbcClaimNo(bean.getIcbc_claim_no());
        bill.setFacilityNo(bean.getFacilityNum());
        bill.setFacilitySubNo(bean.getFacilitySubNum());
        bill.setPaymentMethod(Integer.parseInt(bean.getPaymentType()));

        if (!bean.getPatientHCType().trim().equals(bean.getBillRegion().trim())) {

            bill.setOinInsurerCode(bean.getPatientHCType());
            bill.setOinRegistrationNo(bean.getPatientPHN());
            bill.setOinBirthdate(convertDate8Char(bean.getPatientDoB()));
            bill.setOinFirstName(bean.getPatientFirstName());
            bill.setOinSecondName(" ");
            bill.setOinSurname(bean.getPatientLastName());
            bill.setOinSexCode(bean.getPatientSex());
            bill.setOinAddress(bean.getPatientAddress1());
            bill.setOinAddress2(bean.getPatientAddress2());
            bill.setOinAddress3("");
            bill.setOinAddress4("");
            bill.setOinPostalcode(bean.getPatientPostal());

            bill.setPhn("0000000000");
            bill.setNameVerify("0000");
            bill.setDependentNum("00");
            bill.setBirthDate("00000000");

        }
        log.debug("Bill "+bill.getBillingCode()+" "+bill.getBillAmount());
        return bill;
    }
}
