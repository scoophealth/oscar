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


package oscar.oscarBilling.pageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarBilling.pageUtil.BillingBillingManager.BillingItem;
import oscar.util.ConversionUtils;

public class BillingSaveBillingAction extends Action {

	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
    private BillingDao billingdao = SpringUtils.getBean(BillingDao.class);
    private BillingmasterDAO billingmasterDao = SpringUtils.getBean(BillingmasterDAO.class);
    

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {

    if (request.getSession().getAttribute("user") == null) {
      return (mapping.findForward("Logout"));
    }

    oscar.oscarBilling.pageUtil.BillingSessionBean bean;
    bean = (oscar.oscarBilling.pageUtil.BillingSessionBean) request.getSession().
        getAttribute("billingSessionBean");
    //  oscar.oscarBilling.data.BillingStoreData bsd = new oscar.oscarBilling.data.BillingStoreDate();
    //  bsd.storeBilling(bean);
    oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
    String billStatus = as.billStatus(bean.getApptStatus());
    
    String billingid = "";
    String dataCenterId = OscarProperties.getInstance().getProperty(
        "dataCenterId");

    //change appointment status
    MiscUtils.getLogger().debug("appointment_no: " + bean.getApptNo());
    MiscUtils.getLogger().debug("BillStatus:" + billStatus);
    Appointment appt = appointmentDao.find(Integer.parseInt(bean.getApptNo()));
    appointmentArchiveDao.archiveAppointment(appt);
    if(appt != null) {
    	appt.setStatus(billStatus);
    	appt.setLastUpdateUser(bean.getCreator());
    	appointmentDao.merge(appt);
    }
    
    Billing b= new Billing();
    b.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
    b.setProviderNo(bean.getBillingProvider());
    b.setAppointmentNo(Integer.parseInt(bean.getApptNo()));
    b.setDemographicName(bean.getPatientName());
    b.setHin(bean.getPatientPHN());
    b.setUpdateDate(new Date());
    b.setBillingDate(ConversionUtils.fromDateString(bean.getServiceDate()));
    b.setTotal(bean.getGrandtotal());
    b.setStatus("0");
    b.setDob(bean.getPatientDoB());
    b.setVisitDate(ConversionUtils.fromDateString(bean.getAdmissionDate()));
    b.setVisitType(bean.getVisitType());
    b.setProviderOhipNo(bean.getBillingPracNo());
    b.setApptProviderNo(bean.getApptProviderNo());
    b.setCreator(bean.getCreator());
    
    billingdao.persist(b);

    billingid = b.getId().toString();
        
    ArrayList<BillingItem> billItem = bean.getBillItem();
    for (int i = 0; i < billItem.size(); i++) {
      if (bean.getBillingType().compareTo("MSP") == 0) {
        if (bean.getPatientHCType().trim().compareTo(bean.getBillRegion().trim()) == 0) {
        	Billingmaster bm = new Billingmaster();
        	bm.setBillingNo(Integer.parseInt(billingid));
        	bm.setCreatedate(new Date());
        	bm.setBillingstatus("0");
        	bm.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
        	bm.setAppointmentNo(Integer.parseInt(bean.getApptNo()));
        	bm.setClaimcode("C02");
        	bm.setDatacenter(dataCenterId);
        	bm.setPayeeNo(bean.getBillingGroupNo());
        	bm.setPractitionerNo(bean.getBillingPracNo());
        	bm.setPhn(bean.getPatientPHN());
        	bm.setNameVerify(bean.getPatientFirstName().substring(0, 1) + " " + bean.getPatientLastName().substring(0, 2));
        	bm.setDependentNum("00");
        	bm.setBillingUnit(String.valueOf((billItem.get(i)).getUnit()));
        	bm.setClarificationCode(bean.getVisitLocation().substring(0, 2));
        	bm.setAnatomicalArea("00");
        	bm.setAfterHour("0");
        	bm.setNewProgram("00");
        	bm.setBillingCode((billItem.get(i)).getServiceCode());
        	bm.setBillAmount((billItem.get(i)).getDispLineTotal());
        	bm.setPaymentMode("0");
        	bm.setServiceDate(convertDate8Char(bean.getServiceDate()));
        	bm.setServiceToDay("00");
        	bm.setSubmissionCode("0");
        	bm.setExtendedSubmissionCode(" ");
        	bm.setDxCode1(bean.getDx1());
        	bm.setDxCode2(bean.getDx2());
        	bm.setDxCode3(bean.getDx3());
        	bm.setDxExpansion(" ");
        	bm.setServiceLocation(bean.getVisitType().substring(0, 1));
        	bm.setReferralFlag1(bean.getReferType1());
        	bm.setReferralNo1(bean.getReferral1());
        	bm.setReferralFlag2(bean.getReferType2());
        	bm.setReferralNo2(bean.getReferral2());
        	bm.setTimeCall("0000");
        	bm.setServiceStartTime(bean.getStartTime());
        	bm.setServiceEndTime(bean.getEndTime());
        	bm.setBirthDate(convertDate8Char(bean.getPatientDoB()));
        	bm.setOfficeNumber("");
        	bm.setCorrespondenceCode("0");
        	bm.setClaimComment("");
        	
        	billingmasterDao.save(bm);

        	int billingMasterNo = bm.getBillingmasterNo();
         
            //ensure that this insert action is audited
            this.createBillArchive(billingMasterNo++, "O");
          
        }
        else {
          	Billingmaster bm = new Billingmaster();
        	bm.setBillingNo(Integer.parseInt(billingid));
        	bm.setCreatedate(new Date());
        	bm.setBillingstatus("0");
        	bm.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
        	bm.setAppointmentNo(Integer.parseInt(bean.getApptNo()));
        	bm.setClaimcode("C02");
        	bm.setDatacenter(dataCenterId);
        	bm.setPayeeNo(bean.getBillingGroupNo());
        	bm.setPractitionerNo(bean.getBillingPracNo());
        	bm.setPhn("0000000000");
        	bm.setNameVerify("0000");
        	bm.setDependentNum("00");
        	bm.setBillingUnit(String.valueOf((billItem.get(i)).getUnit()));
        	bm.setClarificationCode(bean.getVisitLocation().substring(0, 2));
        	bm.setAnatomicalArea("00");
        	bm.setAfterHour("0");
        	bm.setNewProgram("00");
        	bm.setBillingCode((billItem.get(i)).getServiceCode());
        	bm.setBillAmount((billItem.get(i)).getDispLineTotal());
        	bm.setPaymentMode("0");
        	bm.setServiceDate(convertDate8Char(bean.getServiceDate()));
        	bm.setServiceToDay("00");
        	bm.setSubmissionCode("0");
        	bm.setExtendedSubmissionCode(" ");
        	bm.setDxCode1(bean.getDx1());
        	bm.setDxCode2(bean.getDx2());
        	bm.setDxCode3(bean.getDx3());
        	bm.setDxExpansion(" ");
        	bm.setServiceLocation(bean.getVisitType().substring(0, 1));
        	bm.setReferralFlag1(bean.getReferType1());
        	bm.setReferralNo1(bean.getReferral1());
        	bm.setReferralFlag2(bean.getReferType2());
        	bm.setReferralNo2(bean.getReferral2());
        	bm.setTimeCall("0000");
        	bm.setServiceStartTime(bean.getStartTime());
        	bm.setServiceEndTime(bean.getEndTime());
        	bm.setBirthDate("00000000");
        	bm.setOfficeNumber("");
        	bm.setCorrespondenceCode("0");
        	bm.setClaimComment("");
        	bm.setOinInsurerCode(bean.getPatientHCType());
        	bm.setOinRegistrationNo(bean.getPatientPHN());
        	bm.setOinBirthdate(convertDate8Char(bean.getPatientDoB()));
        	bm.setOinFirstName(bean.getPatientFirstName());
        	bm.setOinSecondName("");
        	bm.setOinSurname( bean.getPatientLastName());
        	bm.setOinSexCode( bean.getPatientSex());
        	bm.setOinAddress(bean.getPatientAddress1());
        	bm.setOinAddress2(bean.getPatientAddress2());
        	bm.setOinAddress3("");
        	bm.setOinAddress4("");
        	bm.setOinPostalcode(bean.getPatientPostal());
        	
        	billingmasterDao.save(bm);

        
            int billingMasterNo = bm.getBillingmasterNo();
            
            //ensure that this insert action is audited
            this.createBillArchive(billingMasterNo++, "O");
        }
      }
    }

    return (mapping.findForward("success"));
  }

  public String convertDate8Char(String s) {
    String sdate = "00000000", syear = "", smonth = "", sday = "";
    MiscUtils.getLogger().debug("s=" + s);
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

        MiscUtils.getLogger().debug("Year" + syear + " Month" + smonth + " Day" + sday);
        sdate = syear + smonth + sday;

      }
      else {
        sdate = s;
      }
      MiscUtils.getLogger().debug("sdate:" + sdate);
    }
    else {
      sdate = "00000000";

    }
    return sdate;
  }

  /**
   * Adds a new entry into the billing_history table
   * @param newInvNo String
   */
  private void createBillArchive(int billingMasterNo, String status) {
    BillingHistoryDAO dao = new BillingHistoryDAO();
    dao.createBillingHistoryArchive(String.valueOf(billingMasterNo));
  }

}
