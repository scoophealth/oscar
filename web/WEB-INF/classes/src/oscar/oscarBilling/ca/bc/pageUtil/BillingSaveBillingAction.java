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
package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.caisi.model.Appointment;
import org.oscarehr.casemgmt.dao.ApptDAO;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.entities.Billing;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.MSP.MSPBillingNote;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingNote;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class BillingSaveBillingAction extends Action {

    private static Log log = LogFactory.getLog(BillingSaveBillingAction.class);

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
        ApptDAO apptDAO = (ApptDAO) ctx.getBean("ApptDAO");
        System.out.println("appointment_no---: " + bean.getApptNo());
        oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
        String sql = "";

        Date curDate = new Date();
        String billingid = "";
        ArrayList<String> billingIds = new ArrayList();
        String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");
        String billingMasterId = "";

        if (bean.getApptNo() == null || bean.getApptNo().equalsIgnoreCase("null")){
            bean.setApptNo("0");
        }

        ////////////
        if (bean.getApptNo() != null && !bean.getApptNo().trim().equals("0") &&  !bean.getApptNo().trim().equals("")){
            Appointment appt = apptDAO.getAppt(""+bean.getApptNo());
            String billStatus = as.billStatus(appt.getStatus());
            ///Update Appointment information
            log.debug("appointment_no: " + bean.getApptNo());
            log.debug("BillStatus:" + billStatus);
            sql = "update appointment set status='" + billStatus + "' where appointment_no='" + bean.getApptNo() + "'";

            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                db.RunSQL(sql);

            }catch (SQLException e) {
                log.error(e.getMessage(),e);
                log.error("LLLOOK: APPT ERROR FOR demo:" + bean.getPatientName() +" date " + curDate);
                e.printStackTrace();
            }
        }
        ////////////
       

        char billingAccountStatus = getBillingAccountStatus( bean);
        
        //String billingSQL = insertIntoBilling(bean, curDate, billingAccountStatus);
        Billing billing = getBillingObj(bean, curDate, billingAccountStatus);

        ArrayList<oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem> billItem = bean.getBillItem();

        char paymentMode = (bean.getEncounter().equals("E") && !bean.getBillingType().equals("ICBC") && !bean.getBillingType().equals("Pri") && !bean.getBillingType().equals("WCB")) ? 'E' : '0';

        String billedAmount;
        //REALLY should be able to get rid of this since every claim will go thru here.
////        if (bean.getBillingType().equals("MSP") || bean.getBillingType().equals("ICBC") || bean.getBillingType().equals("Pri") || bean.getBillingType().equals("WCB")) {  
        for (oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem bItem: billItem){

            if(request.getParameter("dispPrice+"+bItem.getServiceCode())!= null){
                String updatedPrice = request.getParameter("dispPrice+"+bItem.getServiceCode());
                log.debug(bItem.getServiceCode()+"Original "+bItem.price+ " updated price "+Double.parseDouble(updatedPrice));
                bItem.price = Double.parseDouble(updatedPrice);
                bItem.getLineTotal();
            }

            billing.setBillingNo(0);
            billingmasterDAO.save(billing);
            billingid = ""+billing.getBillingNo(); //getInsertIdFromBilling(billingSQL);
            //log.debug("billing id " + billingid + "   sql " + billingSQL);
            billingIds.add(billingid);
            if (paymentMode == 'E') {
                billedAmount = "0.00";
            } else {
                billedAmount =  bItem.getDispLineTotal();
            }

            Billingmaster billingmaster = saveBill(billingid, "" + billingAccountStatus, dataCenterId, billedAmount, "" + paymentMode, bean, bItem);//billItem.get(i));
            
            String WCBid = request.getParameter("WCBid");
            System.out.println("WCB:"+WCBid);
            if (bean.getBillingType().equals("WCB")) {
                billingmaster.setWcbId(Integer.parseInt(bean.getWcbId()));
            }
            billingmasterDAO.save(billingmaster);
            billingMasterId = "" + billingmaster.getBillingmasterNo();
            this.createBillArchive(billingMasterId);
        
            //Changed March 8th to be included side this loop,  before only one billing would get this information.
            if (bean.getCorrespondenceCode().equals("N") || bean.getCorrespondenceCode().equals("B")) {
                try {
                    MSPBillingNote n = new MSPBillingNote();
                    n.addNote(billingMasterId, bean.getCreator(), bean.getNotes());
                } catch (SQLException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (bean.getMessageNotes() != null || !bean.getMessageNotes().trim().equals("")) {
                try {
                    BillingNote n = new BillingNote();
                    n.addNote(billingMasterId, bean.getCreator(), bean.getMessageNotes());
                } catch (SQLException e) {
                    log.error(e.getMessage(), e);
                }

            }
        }
        
        if (bean.getBillingType().equals("WCB")) {
            
            // HOW TO DO THIS PART
            /* Need to link the id of a WCB for with a bill
                -Continue to put it in the WCB form ?   + no data structure change - not sure how will it work.
                ===============================================================================================
                On submission how would this work??  for each bill submission that would look for it's id in the wcb table?
                The problem is that it's not really logical but it would work.  Not every form would have a billing. 
                
             
                -Add a field to Billingmaster?   + data structure change + data migration + initial reaction
                ===============================================================================================
                Data conversion wouldn't be that big of a deal though.  because everything else would be coming over too.
                Most logical
             
                -Add a separate table ?       + data structure change + data migration + 2nd initial reacion
                ===============================================================================================
             
             */ 
            System.out.println("WCB BILL!!");
            
            
        }

////        ///}
        //////////////
//        if (bean.getBillingType().equals("WCB")) {
//            //Keep in mind that the first billingId was set way up at the top
//            //NOT ANY MORE  
//            billingid = getInsertIdFromBilling(billingSQL);//--
//            billingIds.add(billingid);//--
//            DBHandler db = null;
//            String status = new String(new char[]{billingAccountStatus});//--
//            WCBForm wcb = (WCBForm) request.getSession().getAttribute("WCBForm");
//            wcb.setW_demographic(bean.getPatientNo());
//            wcb.setW_providerno(bean.getBillingProvider());
//            String insertBillingMaster = createBillingMasterInsertString(bean,billingid, billingAccountStatus, wcb.getW_payeeno());//--
//            String amnt = getFeeByCode(wcb.getW_feeitem());
//            try {
//                db = new DBHandler(DBHandler.OSCAR_DATA);
//                
//                Billingmaster billingmaster = saveBill(billingid, "" + billingAccountStatus, dataCenterId, amnt, "" + paymentMode, bean, "1",wcb.getW_feeitem()) ;
//                billingmasterDAO.save(billingmaster);
//                billingMasterId = "" + billingmaster.getBillingmasterNo();
//                this.createBillArchive(billingMasterId);
//                
//           
//              
//                //for some bizarre reason billing table stores location with trailing '|' e.g 'A|'
//                //whereas WCB table stores it as single char.
//                String serviceLocation = bean.getVisitType().substring(0);
//                wcb.setW_servicelocation(serviceLocation);
//                db.RunSQL(wcb.SQL(billingid, amnt));
//
//                //If an extra fee item was declared on the WCB form, save it in
//                //The billingmaster table as well
//                if (wcb.getW_extrafeeitem() != null && wcb.getW_extrafeeitem().trim().length() != 0) {
//                    log.debug("Adding Second billing item");
//                    String secondWCBBillingId = null;
//                    String secondBillingAmt = this.getFeeByCode(wcb.getW_extrafeeitem());
//                    //save entry in billing table
//                    db.RunSQL(billingSQL);
//                    secondWCBBillingId = this.getLastInsertId(db);
//                    billingIds.add(secondWCBBillingId);
//                    //Link new billing record to billing line in billingmaster table
//                    String secondBillingMaster = createBillingMasterInsertString(bean, secondWCBBillingId, billingAccountStatus, wcb.getW_payeeno());
//                    db.RunSQL(secondBillingMaster);
//                    //get most recent billingmaster id
//                    billingMasterId = getLastInsertId(db);
//
//                    //Store a record of this billingmaster Transaction
//                    status = new String(new char[]{billingAccountStatus});
//                    this.createBillArchive(billingMasterId);
//                    //this.createBillArchive(billingMasterId, status);
//
//                    //save extra fee item entry in wcb table
//                    /**
//                    if (wcb.isNotBilled()) {
//                    //if processing an existing WCB form, update values for second fee item
//                    String updateWCBSQL = createWCBUpdateSQL(secondWCBBillingId,
//                    secondBillingAmt, wcb.getWcbFormId());
//                    db.RunSQL(updateWCBSQL);
//                    }
//                    else {
//                    //This form was created from the billing screen
//                    //Store a new WCB entry for the second fee item
//                    db.RunSQL(wcb.secondSQLItem(secondWCBBillingId, secondBillingAmt));
//                    }**/
//                    db.RunSQL(wcb.secondSQLItem(secondWCBBillingId, secondBillingAmt));
//
//                    //Update patient echart with the clinical info from the WCB form
//                    updatePatientChartWithWCBInfo(wcb);
//                }
//            } catch (SQLException e) {
//                log.error(e.getMessage(), e);
//                e.printStackTrace();
//            } finally {
//                if (db != null) {
//                    try {
//                    } catch (SQLException ex) {
//                        log.error(ex.getMessage(), ex);
//                        ex.printStackTrace();
//                    }
//                }
//            }
//            request.getSession().setAttribute("WCBForm", null);
//        }

        ////////////////////
        //      log.debug("Service count : "+ billItem.size());
        ActionForward af = mapping.findForward("success");
        if (frm.getSubmit().equals("Another Bill")) {
            bean.setBillForm("GP");   //Todo: is this what this should be?
            af = mapping.findForward("anotherBill");

        } else if (frm.getSubmit().equals("Save & Print Receipt")) {
            StringBuffer stb = new StringBuffer();
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

//    private String getInsertIdFromBilling(final String billingSQL) {
//        String billingId = "";
//        try {
//            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
//            db.RunSQL(billingSQL);
//            java.sql.ResultSet rs = db.GetSQL("SELECT LAST_INSERT_ID()");
//            if (rs.next()) {
//                billingId = rs.getString(1);
//            }
//            rs.close();
//        } catch (SQLException e) {
//            log.error(e.getMessage(), e);
//            e.printStackTrace();
//        }
//        return billingId;
//    }

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
        bill.setUpdateDate(UtilDateUtilities.DateToString(curDate));
        bill.setBillingDate(bean.getServiceDate());
        bill.setTotal(bean.getGrandtotal());
        bill.setStatus(""+billingAccountStatus);
        bill.setDob(bean.getPatientDoB());
        bill.setVisitdate(bean.getAdmissionDate());
        bill.setVisittype(bean.getVisitType());
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
    
    

    
//    private String insertIntoBilling(final oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean, final String curDate, final char billingAccountStatus) {
//
//        //TODO STILL NEED TO ADD EXTRA FIELDS for dotes and bill type
//        String billingSQL = "insert into billing (demographic_no, provider_no,appointment_no, demographic_name,hin,update_date, billing_date, total, status, dob, visitdate, visittype, provider_ohip_no, apptProvider_no, creator,billingtype)" + " values(" +
//                "'" + bean.getPatientNo() + "'," +
//                "'" + bean.getBillingProvider() + "', " +
//                "'" + bean.getApptNo() + "'," +
//                "'" + oscar.util.UtilMisc.mysqlEscape(bean.getPatientName()) + "'," +
//                "'" + bean.getPatientPHN() + "'," +
//                "'" + curDate + "'," +
//                "'" + bean.getServiceDate() + "'," +
//                "'" + bean.getGrandtotal() + "'," +
//                "'" + billingAccountStatus + "'," + //status
//                "'" + bean.getPatientDoB() + "'," +
//                "'" + bean.getAdmissionDate() + "'," +
//                "'" + oscar.util.UtilMisc.mysqlEscape(bean.getVisitType()) + "'," +
//                "'" + bean.getBillingPracNo() + "'," +
//                "'" + bean.getApptProviderNo() + "'," +
//                "'" + bean.getCreator() + "'," +
//                "'" + bean.getBillingType() + "')";
//        return billingSQL;
//    }

//    private String createBillingMasterInsertString(BillingSessionBean bean,
//            String billingid,
//            char billingAccountStatus,
//            String payeeNo) {
//        String insertBillingMaster =
//                " INSERT INTO billingmaster (billing_no, createdate, payee_no, billingstatus, demographic_no, appointment_no,service_date) " +
//                " VALUES ('" + billingid + "',NOW(),'" + payeeNo + "','" + billingAccountStatus + "','" + bean.getPatientNo() + "','" + bean.getApptNo() + "','" + convertDate8Char(bean.getServiceDate()) + "')";
//        return insertBillingMaster;
//    }

    private String getFeeByCode(String feeitem) {
        ResultSet rs = null;
        DBHandler db = null;
        String amnt = "0.00";
        try {
            db = new DBHandler(DBHandler.OSCAR_DATA);
            rs = db.GetSQL("SELECT value FROM billingservice WHERE service_code='" +
                    feeitem + "'");
            if (rs.next()) {
                amnt = rs.getString("value");
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex2) {
                    log.error(ex2.getMessage(), ex2);
                    ex2.printStackTrace();
                }
            }
        }

        return amnt;
    }

    private String updateAppt(String billStatus, BillingSessionBean bean, Date curDate) {
        String sql = "update appointment set status='" + billStatus + "' where appointment_no='" + bean.getApptNo() + "'";

        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            db.RunSQL(sql);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            log.error("LLLOOK: APPT ERROR FOR demo:" + bean.getPatientName() + " date " + curDate);
            e.printStackTrace();
        }
        ////End of updating appt information
        return sql;
    }

//    private void updatePatientChartWithWCBInfo(
//            WCBForm wcb) {
//        EChartDAO dao = new EChartDAO();
//        Echart echart = dao.getMostRecentEchart(wcb.getDemographic());
//
//        //if the patient doesn't have an echart than create  a new one
//        if (echart == null) {
//            echart = new Echart();
//            echart.setDemographicNo(wcb.getDemographic());
//            echart.setProviderNo(wcb.getW_pracno());
//        }
//
//        String wcbEchartEntry = "\n\n[WCB Clinical Info - CLAIM# - " + wcb.getW_wcbno() + " @ " + echart.getTimeStampToString() + "]\n" + wcb.getW_clinicinfo();
//        echart.setEncounter(wcbEchartEntry);
//        log.debug("wcbEchartEntry=" + wcbEchartEntry);
//        dao.addEchartEntry(echart);
//    }

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

//    private String getLastInsertId(DBHandler db) throws SQLException {
//        ResultSet rs = db.GetSQL("SELECT LAST_INSERT_ID()");
//        String id = null;
//        if (rs.next()) {
//            id = rs.getString(1);
//        }
//        rs.close();
//        return id;
//    }

    String moneyFormat(String str) {
        String moneyStr = "0.00";
        try {
            moneyStr = new java.math.BigDecimal(str).movePointLeft(2).toString();
        } catch (Exception moneyException) {
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
