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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import oscar.service.OscarSuperManager;

public class BillingSaveBillingAction extends Action {
	
	private static final int BC_PHN_CHAR_LENGTH = 10;

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
        String sql = "";

        Date curDate = new Date();
        String billingid = "";
        ArrayList<String> billingIds = new ArrayList<String>();
        String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");
        String billingMasterId = "";

        if (bean.getApptNo() == null || bean.getApptNo().equalsIgnoreCase("null")){
            bean.setApptNo("0");
        }

        ////////////
        OscarSuperManager oscarSuperManager = (OscarSuperManager)SpringUtils.getBean("oscarSuperManager");
        if (bean.getApptNo() != null && !bean.getApptNo().trim().equals("0") &&  !bean.getApptNo().trim().equals("")){
            String apptStatus = "";
            List<Map<String, Object>> resultList  = oscarSuperManager.find("appointmentDao", "search", new Object[]{bean.getApptNo()});
            if (resultList.size() < 1) {
                log.error("LLLOOK: APPT ERROR - APPT ("+bean.getApptNo()+") NOT FOUND - FOR demo:" + bean.getPatientName() +" date " + curDate);
            } else {
                Map m_status = resultList.get(0);
                apptStatus = (String)m_status.get("status");
            }
            String billStatus = as.billStatus(apptStatus);
            ///Update Appointment information
            log.debug("appointment_no: " + bean.getApptNo());
            log.debug("BillStatus:" + billStatus);
            Appointment appt = appointmentDao.find(Integer.parseInt(bean.getApptNo()));
            appointmentArchiveDao.archiveAppointment(appt);
            int rowsAffected = oscarSuperManager.update("appointmentDao", "updatestatusc", new Object[]{billStatus,bean.getCreator(),bean.getApptNo()});

            if (rowsAffected<1) log.error("LLLOOK: APPT ERROR - CANNOT UPDATE APPT ("+bean.getApptNo()+") FOR demo:" + bean.getPatientName() +" date " + curDate);
        }


        char billingAccountStatus = getBillingAccountStatus( bean);

        //String billingSQL = insertIntoBilling(bean, curDate, billingAccountStatus);
        //* moved Billing billing = getBillingObj(bean, curDate, billingAccountStatus);

        ArrayList<oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem> billItem = bean.getBillItem();

        char paymentMode = (bean.getEncounter().equals("E") && !bean.getBillingType().equals("ICBC") && !bean.getBillingType().equals("Pri") && !bean.getBillingType().equals("WCB")) ? 'E' : '0';

        String billedAmount;
        //REALLY should be able to get rid of this since every claim will go thru here.
////        if (bean.getBillingType().equals("MSP") || bean.getBillingType().equals("ICBC") || bean.getBillingType().equals("Pri") || bean.getBillingType().equals("WCB")) {
        for (oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem bItem: billItem){

            Billing billing = getBillingObj(bean, curDate, billingAccountStatus);
            if(request.getParameter("dispPrice+"+bItem.getServiceCode())!= null){
                String updatedPrice = request.getParameter("dispPrice+"+bItem.getServiceCode());
                log.debug(bItem.getServiceCode()+"Original "+bItem.price+ " updated price "+Double.parseDouble(updatedPrice));
                bItem.price = Double.parseDouble(updatedPrice);
                bItem.getLineTotal();
            }

            billingmasterDAO.save(billing);
            billingid = ""+billing.getId(); //getInsertIdFromBilling(billingSQL);
            //log.debug("billing id " + billingid + "   sql " + billingSQL);
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

        // not usefull
        // if (bean.getBillingType().equals("WCB")) {

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
           // MiscUtils.getLogger().debug("WCB BILL!!");


        //}

////        ///}
        //////////////
//        if (bean.getBillingType().equals("WCB")) {
//            //Keep in mind that the first billingId was set way up at the top
//            //NOT ANY MORE
//            billingid = getInsertIdFromBilling(billingSQL);//--
//            billingIds.add(billingid);//--
//
//            String status = new String(new char[]{billingAccountStatus});//--
//            WCBForm wcb = (WCBForm) request.getSession().getAttribute("WCBForm");
//            wcb.setW_demographic(bean.getPatientNo());
//            wcb.setW_providerno(bean.getBillingProvider());
//            String insertBillingMaster = createBillingMasterInsertString(bean,billingid, billingAccountStatus, wcb.getW_payeeno());//--
//            String amnt = getFeeByCode(wcb.getW_feeitem());
//            try {
//
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
//                DBHandler.RunSQL(wcb.SQL(billingid, amnt));
//
//                //If an extra fee item was declared on the WCB form, save it in
//                //The billingmaster table as well
//                if (wcb.getW_extrafeeitem() != null && wcb.getW_extrafeeitem().trim().length() != 0) {
//                    log.debug("Adding Second billing item");
//                    String secondWCBBillingId = null;
//                    String secondBillingAmt = this.getFeeByCode(wcb.getW_extrafeeitem());
//                    //save entry in billing table
//                    DBHandler.RunSQL(billingSQL);
//                    secondWCBBillingId = this.getLastInsertId(db);
//                    billingIds.add(secondWCBBillingId);
//                    //Link new billing record to billing line in billingmaster table
//                    String secondBillingMaster = createBillingMasterInsertString(bean, secondWCBBillingId, billingAccountStatus, wcb.getW_payeeno());
//                    DBHandler.RunSQL(secondBillingMaster);
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
//                    DBHandler.RunSQL(updateWCBSQL);
//                    }
//                    else {
//                    //This form was created from the billing screen
//                    //Store a new WCB entry for the second fee item
//                    DBHandler.RunSQL(wcb.secondSQLItem(secondWCBBillingId, secondBillingAmt));
//                    }**/
//                    DBHandler.RunSQL(wcb.secondSQLItem(secondWCBBillingId, secondBillingAmt));
//
//                    //Update patient echart with the clinical info from the WCB form
//                    updatePatientChartWithWCBInfo(wcb);
//                }
//            } catch (SQLException e) {
//                log.error(e.getMessage(), e);
//                MiscUtils.getLogger().error("Error", e);
//            } finally {
//                if (db != null) {
//                    try {
//                    } catch (SQLException ex) {
//                        log.error(ex.getMessage(), ex);
//MiscUtils.getLogger().error("Error", ex);
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

//    private String getInsertIdFromBilling(final String billingSQL) {
//        String billingId = "";
//        try {
//
//            DBHandler.RunSQL(billingSQL);
//            java.sql.ResultSet rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID()");
//            if (rs.next()) {
//                billingId = rs.getString(1);
//            }
//            rs.close();
//        } catch (SQLException e) {
//            log.error(e.getMessage(), e);
//            MiscUtils.getLogger().error("Error", e);
//        }
//        return billingId;
//    }

    private Billing getBillingObj(final oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean, final Date curDate, final char billingAccountStatus){

    	int apptNo = 0;
        String visitType = parseBillingVisitType(bean.getVisitType());

        try{
            apptNo = Integer.parseInt(bean.getApptNo());
        }catch(Exception e){
        	apptNo = 0;
        }
        
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
        bill.setVisitType(visitType);//bean.getVisitType());
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

//    private String getLastInsertId(DBHandler db) throws SQLException {
//        ResultSet rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID()");
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
        Billingmaster billingMaster= new Billingmaster();

        // Added by Dennis Warren o/a Colcamex Resources 2013 - InsurerCode override for Opted-Out billing.        
        boolean enableOinForm = Boolean.FALSE;       
        String oinRegistrationNo = "0000000000";       
        String providerNumber = bean.getBillingProvider().trim();
        String billingType = bean.getBillingType();
        String billingProvince = bean.getBillRegion().trim();
        String patientProvince = bean.getPatientHCType().trim();
        String patientPostal = bean.getPatientPostal();
        String patientPhn = bean.getPatientPHN();      		
        String dependentNumber = "00";
        String oinInsurerCode = OverrideInsurerCode.getOinInsurerCode(providerNumber, billingType);
        //String visitType =  parseBillingVisitType(bean.getVisitType()); // will be used in the future.
        String anatomicalArea = "00";
        String newProgram = "00";
        String claimCode = "C02";
      
        // Overridden to ensure the strict rules from MSP that this number can only 
        // be 00 or 66.  Hard coding because "doing it right" will cause a lot of changes.
        if( ( bean.getDependent() != null ) && ( bean.getDependent().equalsIgnoreCase("66") ) ) {
        	dependentNumber = bean.getDependent().trim();
        }
        
        if(patientPostal != null) {
        	patientPostal = patientPostal.replaceAll("\\W", "");
        }    

        if (billingType.equals("WCB")) {
        	int wcbId = Integer.parseInt(bean.getWcbId());    	
            if(wcbId > 0) {
            	billingMaster.setWcbId(wcbId);
            }
        }
        
        if(patientPhn != null) {
        	patientPhn = patientPhn.trim();
        }
        
        if( ( patientPhn == null ) || ( patientPhn.length() > BC_PHN_CHAR_LENGTH ) ) {
        	patientPhn = "0000000000";
        }
               
        billingMaster.setBillingNo(Integer.parseInt(billingid));
        billingMaster.setCreatedate(new Date());
        billingMaster.setBillingstatus(billingAccountStatus);
        billingMaster.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
        billingMaster.setAppointmentNo(Integer.parseInt(bean.getApptNo()));
        billingMaster.setClaimcode(claimCode);
        billingMaster.setDatacenter(dataCenterId);
        billingMaster.setPayeeNo(bean.getBillingGroupNo());
        billingMaster.setPractitionerNo(bean.getBillingPracNo());

        billingMaster.setPhn(patientPhn);
        billingMaster.setNameVerify(bean.getPatientFirstName(),bean.getPatientLastName());
        billingMaster.setDependentNum(dependentNumber);
        billingMaster.setBillingUnit(billingUnit); //"" + billItem.getUnit());
        billingMaster.setClarificationCode(bean.getVisitLocation().substring(0, 2));

        billingMaster.setAnatomicalArea(anatomicalArea);
        billingMaster.setAfterHour(bean.getAfterHours());
        billingMaster.setNewProgram(newProgram);
        billingMaster.setBillingCode(serviceCode);//billItem.getServiceCode());
        billingMaster.setBillAmount(billedAmount);
        billingMaster.setPaymentMode(paymentMode);
        billingMaster.setServiceDate(convertDate8Char(bean.getServiceDate()));
        billingMaster.setServiceToDay(bean.getService_to_date());
        billingMaster.setSubmissionCode(bean.getSubmissionCode());
        billingMaster.setExtendedSubmissionCode(" ");
        billingMaster.setDxCode1(bean.getDx1());
        billingMaster.setDxCode2(bean.getDx2());
        billingMaster.setDxCode3(bean.getDx3());
        billingMaster.setDxExpansion(" ");

        billingMaster.setServiceLocation(bean.getVisitType().substring(0, 1));
        billingMaster.setReferralFlag1(bean.getReferType1());
        billingMaster.setReferralNo1(bean.getReferral1());
        billingMaster.setReferralFlag2(bean.getReferType2());
        billingMaster.setReferralNo2(bean.getReferral2());
        billingMaster.setTimeCall(bean.getTimeCall());
        billingMaster.setServiceStartTime(bean.getStartTime());
        billingMaster.setServiceEndTime(bean.getEndTime());
        billingMaster.setBirthDate(convertDate8Char(bean.getPatientDoB()));
        billingMaster.setOfficeNumber("");
        billingMaster.setCorrespondenceCode(bean.getCorrespondenceCode());
        billingMaster.setClaimComment(bean.getShortClaimNote());
        billingMaster.setMvaClaimCode(bean.getMva_claim_code());
        billingMaster.setIcbcClaimNo(bean.getIcbc_claim_no());
        billingMaster.setFacilityNo(bean.getFacilityNum());
        billingMaster.setFacilitySubNo(bean.getFacilitySubNum());
        billingMaster.setPaymentMethod(Integer.parseInt(bean.getPaymentType()));

        
        // Other insurer portion of Teleplan4 C02 claim
        // Covers claim codes for out of province reciprocol: AB, SK, MB ...
        // Institutional: IN, Opted-Out: PP and Work Safe: WC
        // Added by Dennis Warren o/a Colcamex Resources 2013 - InsurerCode override for Opted-Out billing.        
        if(oinInsurerCode != null) {        	 
        	enableOinForm = Boolean.TRUE;
        }
        
        if ( ! patientProvince.equalsIgnoreCase(billingProvince) ) {          	
        	oinInsurerCode = patientProvince;
        	enableOinForm = Boolean.TRUE;
        }
        
        log.info("Part 2 billing form enabled : " + enableOinForm );
        	
        if(enableOinForm) {
 
        	// build oin registration number based on Teleplan rules. 
        	// provincial claims are combinations of PHN and dependent code (00 or 66)
        	if( ( ! oinInsurerCode.equalsIgnoreCase(OverrideInsurerCode.BC_INSTITUTIONAL_CLAIM) ) &&
        			( patientPhn.startsWith("9") ) ) {

            	oinRegistrationNo = patientPhn + dependentNumber;            		

        	} else {
        		oinRegistrationNo = patientPhn;
        	}
 	            
            billingMaster.setOinInsurerCode(oinInsurerCode);
            billingMaster.setOinRegistrationNo(oinRegistrationNo);
            billingMaster.setOinBirthdate(convertDate8Char(bean.getPatientDoB()));
            billingMaster.setOinFirstName(bean.getPatientFirstName());
            billingMaster.setOinSecondName(" ");
            billingMaster.setOinSurname(bean.getPatientLastName());
            billingMaster.setOinSexCode(bean.getPatientSex());
            billingMaster.setOinAddress(bean.getPatientAddress1());
            billingMaster.setOinAddress2(bean.getPatientAddress2());
            billingMaster.setOinAddress3("");
            billingMaster.setOinAddress4("");
            billingMaster.setOinPostalcode(patientPostal);

            billingMaster.setPhn("0000000000");
            billingMaster.setNameVerify("0000");
            billingMaster.setDependentNum("00");
            billingMaster.setBirthDate("00000000");

        }
        
//        if (!bean.getPatientHCType().trim().equals(bean.getBillRegion().trim())) {
//
//            billingMaster.setOinInsurerCode(bean.getPatientHCType());
//            billingMaster.setOinRegistrationNo(bean.getPatientPHN());
//            billingMaster.setOinBirthdate(convertDate8Char(bean.getPatientDoB()));
//            billingMaster.setOinFirstName(bean.getPatientFirstName());
//            billingMaster.setOinSecondName(" ");
//            billingMaster.setOinSurname(bean.getPatientLastName());
//            billingMaster.setOinSexCode(bean.getPatientSex());
//            billingMaster.setOinAddress(bean.getPatientAddress1());
//            billingMaster.setOinAddress2(bean.getPatientAddress2());
//            billingMaster.setOinAddress3("");
//            billingMaster.setOinAddress4("");
//            billingMaster.setOinPostalcode(bean.getPatientPostal());
//
//            billingMaster.setPhn("0000000000");
//            billingMaster.setNameVerify("0000");
//            billingMaster.setDependentNum("00");
//            billingMaster.setBirthDate("00000000");
//
//        }
        
        log.debug("Bill "+billingMaster.getBillingCode()+" "+billingMaster.getBillAmount());
        return billingMaster;
    }
    
    /**
     * Splits off the visit type code from the beginning of a visit type 
     * description. 
     * @param visitType
     * @return
     */
    private static String parseBillingVisitType(String visitType) {
    	
        String[] split = null;
    	if( visitType != null ) { 
	        if( visitType.contains("\\|") ) {
	        	split = visitType.split("\\|", 2);
	        	if(split != null) {
	        		visitType = split[0].trim();
	        	}
	        } else {
	        	visitType = visitType.substring(0, 1); 
	        }
        }
    	
    	return visitType;
    }
}
