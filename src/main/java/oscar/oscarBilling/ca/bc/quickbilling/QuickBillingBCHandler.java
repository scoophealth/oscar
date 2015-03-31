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

/**
 * @author Dennis Warren
 * Company Colcamex Resources
 * Date Jun 4, 2012
 * Filename QuickBillingBCHandler.java
 */
package oscar.oscarBilling.ca.bc.quickbilling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingNote;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager;
import oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem;
import oscar.oscarBilling.ca.bc.pageUtil.BillingSaveBillingAction;
import oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean;


/**
 * @author Dennis Warren
 * @Revised Jun 4, 2012
 * @Comment 
 *
 */
public class QuickBillingBCHandler {
	
	// full logging to be added later. too pressed for time.
	private static Logger log = Logger.getLogger(BillingSaveBillingAction.class);

	// default attributes for MSP billing. 
	// create new attributes for dynamic form input.
	public static final char BILL_ACCOUNT_STATUS = 'O';
	public static final String BILLING_TYPE = "MSP";
	public static final String PAYMENT_TYPE_NAME = "ELECTRONIC";
	public static final String PAYMENT_TYPE = "6";
	public static final Integer APPOINTMENT_NO = 0;
	public static final String PAYMENT_MODE = "0";
	public static final String CLAIM_CODE = "C02";
	public static final String ANATOMICAL_AREA = "00";
	public static final String NEW_PROGRAM = "00";
	public static final String BILL_REGION = "BC";
	public static final String SUBMISSION_CODE = "0";
	public static final String CORRESPONDENCE_CODE = "0";
	public static final String BILLING_PROV = "BC";
	public static final String MVA_CLAIM_CODE = "N";
	public static final String DEPENDENT_CODE = "00";
	public static final String INTERNAL_COMMENT = "MSP Billing done by quick billing method.";
	public static final String AFTERHOUR_CODE = "0";
	public static final String ADMISSION_DATE = "0000-00-00";
	
	public static final String BILLING_UNIT = "1";
	public static final String HALF_BILLING = "0.5";
	
	public final SimpleDateFormat dateformat = new SimpleDateFormat("ddmmyyyy");
	
	private Date today;
	private QuickBillingBCFormBean quickBillingBCFormBean;
	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	private ProviderDataDao providerDao;
	private Properties oscarProperties;
	private BillingBillingManager bmanager;
	private Billing billing;
	private BillingmasterDAO billingmasterDAO;
    private BillingHistoryDAO billingHistoryDAO;
	private Billingmaster billingmaster;
	private int numberSaved;

	/**
	 * Default constructor.
	 */
	public QuickBillingBCHandler(){
		
		this.today = new Date();
    	providerDao = (ProviderDataDao) SpringUtils.getBean("providerDataDao");
    	oscarProperties = OscarProperties.getInstance();
    	bmanager = new BillingBillingManager();
    	billing = new Billing();
		billingmasterDAO = (BillingmasterDAO) SpringUtils.getBean("BillingmasterDAO");
		billingHistoryDAO = new BillingHistoryDAO();
		
	}
	
	/**
	 * Instantiate a new handler with fresh form bean data.
	 */
	public QuickBillingBCHandler(QuickBillingBCFormBean quickBillingBCFormBean) {
		
		this.quickBillingBCFormBean = quickBillingBCFormBean;
		
		this.today = new Date();
    	providerDao = (ProviderDataDao) SpringUtils.getBean("providerDataDao");
    	oscarProperties = OscarProperties.getInstance();
    	bmanager = new BillingBillingManager();
    	billing = new Billing();
		billingmasterDAO = (BillingmasterDAO) SpringUtils.getBean("BillingmasterDAO");
		billingHistoryDAO = new BillingHistoryDAO();
		
	}
	
	

	/**
	 * 
	 * @return Provider Data Access Object
	 */
	public ProviderDataDao getProviderDao() {
		return providerDao;
	}

	/**
	 * 
	 * @return Oscar Properties Object
	 */
	public Properties getOscarProperties() {
		return oscarProperties;
	}
	
	
	/**
	 * The number of invoices saved in the last
	 * session.
	 * @return int
	 */
	public int getNumberSaved() {
		return numberSaved;
	}

	/**
	 * set the number of invoices saved in this session.
	 * @param numberSaved
	 */
	private void setNumberSaved(int numberSaved) {
		this.numberSaved = numberSaved;
	}

	/**
	 * Reset the form and session objects for 
	 * fresh entries.
	 */
	public void reset() {
    	quickBillingBCFormBean.setIsHeaderSet(false);
    	quickBillingBCFormBean.setBillingProvider("");
    	quickBillingBCFormBean.setBillingProviderNo("");
    	quickBillingBCFormBean.setServiceDate("");
    	quickBillingBCFormBean.setVisitLocation("");
    	quickBillingBCFormBean.setBillingData(new ArrayList<BillingSessionBean>());

	}
	
	/**
	 * Set the header data for this group of billings.
	 * Header consists of a provider, service location, and service date and
	 * is the header for a group of individual patients with the header data
	 * in common.
	 * 
	 */
	public void setHeader(JSONObject billingEntry) {
		
		String visitLocation = billingEntry.getString("visitLocation");		
		String visitDate = billingEntry.getString("visitDate");		
		String providerNo = billingEntry.getString("provider");
		String creator = billingEntry.getString("creator");

    	// if any of the variables are empty
		if( (!providerNo.equals("empty"))||
				(!visitLocation.equals("empty")) ||
				(!visitDate.equals(""))
		) {
			
	
    		// set the header data in the quickBillingbean. Only happens on the first add.
			// Then they are set and used during the entire session.
			this.quickBillingBCFormBean.setBillingProviderNo(providerNo);
    		this.quickBillingBCFormBean.setBillingProvider(
    				providerDao.findByProviderNo(providerNo).getLastName() + 
    				", " + 
    				providerDao.findByProviderNo(providerNo).getFirstName()
    		);
    		
    		this.quickBillingBCFormBean.setVisitLocation(visitLocation);
    		this.quickBillingBCFormBean.setServiceDate(visitDate);
    		this.quickBillingBCFormBean.setCreator(creator);
    		    			
		} 
	}

	/**
     * Create a new billing object to be stored into an array 
     * for later processing.
     * Lots of data here is hard coded.
     */
	public boolean addBill(LoggedInInfo loggedInInfo, JSONObject billingEntry) {

		String providerNo = this.quickBillingBCFormBean.getBillingProviderNo();
		BillingSessionBean bean = new BillingSessionBean();
		String unit = BILLING_UNIT;
		ProviderData provider = null;
		Demographic demographic = null;
		String demographicNo = "";
		String billingCode = "";
		String dxCode = "";
		
		if(billingEntry.containsKey("ptNumber")) {
			demographicNo = billingEntry.getString("ptNumber");
		}
		
		if(billingEntry.containsKey("billingCode")) {
			billingCode = billingEntry.getString("billingCode");
		}
		
		if(billingEntry.containsKey("dxCode1")) {
			dxCode = billingEntry.getString("dxCode1");
		}
		
		if(!demographicNo.isEmpty()) {
			demographic = this.demographicManager.getDemographic(loggedInInfo, demographicNo);
		}
		
		if(!providerNo.isEmpty()) {
			provider = this.providerDao.findByProviderNo(providerNo);
		}
		
		if((billingEntry.containsKey("halfBilling"))&&
				(billingEntry.getString("halfBilling") != "") 
		){
			// unit is set as 1.0 in default.
			unit = billingEntry.getString("halfBilling");
		}
	
		// first take care of the service codes.
		String[] service = new String[0]; // not sure what this does, but pressed for time soooo...
		
		ArrayList<BillingItem> billItem = bmanager.getDups2(
				service,
				billingCode,
				"",
				"",
				unit,
				"",
				""
		);
			
		// diagnostic codes.
		bean.setDx1(dxCode);
		bean.setDx2(""); //(!billingEntry.getString("dxCode2").isEmpty()) ? billingEntry.getString("dxCode2") : "");
		bean.setDx3(""); //(!billingEntry.getString("dxCode3").isEmpty()) ? billingEntry.getString("dxCode3") : "");
		
		// billing codes.
		bean.setGrandtotal(bmanager.getGrandTotal(billItem));
		bean.setBillItem(billItem);
		bean.setSubmissionCode(SUBMISSION_CODE);
		//bean.setFacilityNum(null);
		//bean.setFacilitySubNum(null);
		bean.setPaymentTypeName(PAYMENT_TYPE_NAME);
		bean.setApptNo(APPOINTMENT_NO.toString());
		bean.setBillRegion(BILL_REGION);
				
		// demographic data
		bean.setPatientNo(demographicNo);
		bean.setPatientLastName(demographic.getLastName());
		bean.setPatientFirstName(demographic.getFirstName());
		bean.setPatientName(demographic.getLastName()+", "+demographic.getFirstName());
		bean.setPatientDoB(convertDate8Char(demographic.getFormattedDob()));
		bean.setPatientAddress1(demographic.getAddress());
		bean.setPatientAddress2(demographic.getCity());
		bean.setPatientPostal(demographic.getPostal());
		bean.setPatientSex(demographic.getSex());
		bean.setPatientPHN(demographic.getHin());
		bean.setPatientHCType(demographic.getHcType());
		bean.setPatientAge(demographic.getAge());
		
		// billing settings
		bean.setBillingType(BILLING_TYPE); // billing account status = O
		bean.setPaymentType(PAYMENT_TYPE);
		bean.setEncounter(PAYMENT_MODE);
		//bean.setWcbId(null);
		
		// visit information
		bean.setVisitType(quickBillingBCFormBean.getVisitLocation());
		bean.setVisitLocation(oscarProperties.getProperty("visitlocation")); //global location also sets the clarification code.
		bean.setServiceDate(convertDate8Char(quickBillingBCFormBean.getServiceDate()));
		//bean.setStartTimeHr(null);
		//bean.setStartTimeMin(null);
		//bean.setEndTimeHr(null);
		//bean.setEndTimeMin(null);
		bean.setAdmissionDate(ADMISSION_DATE);

		// provider data for billing
		// aka: ohip number, billing number, practitioner number, payee...
		bean.setBillingProvider(provider.getBillingNo());
		bean.setBillingPracNo(provider.getOhipNo());		
		bean.setBillingGroupNo(null);
		
		bean.setCreator(quickBillingBCFormBean.getCreator());	
		bean.setApptProviderNo(provider.getId());
		bean.setReferral1("");
		bean.setReferral2("");
		bean.setReferType1("");
		bean.setReferType2("");
		
		// codes are 0=no notes n=external c=internal b=both 
		bean.setCorrespondenceCode(CORRESPONDENCE_CODE);

		// notes which are sent to msp and seen by recipient.
		//bean.setNotes(null);
		bean.setDependent(DEPENDENT_CODE);
		//bean.setAfterHours(null);
		//bean.setTimeCall(null);
		//bean.setShortClaimNote(null);
		//bean.setService_to_date(null);
		//bean.setIcbc_claim_no(null);
		bean.setMva_claim_code(MVA_CLAIM_CODE);

		// notes which are internal - NOT seen by recipient.
		bean.setMessageNotes(INTERNAL_COMMENT);	

		return (this.quickBillingBCFormBean.getBillingData()).add(bean);
	}
	
	/**
	 * Triggers exsisting class: BillingSaveBillingAction to recursivley save the bills array list.
	 * @throws IOException
	 * @throws ServletException
	 */
	public boolean saveBills() {
	
		String dataCenterId = oscarProperties.getProperty("dataCenterId");		
		ArrayList<BillingSessionBean> billingSessionBeans = quickBillingBCFormBean.getBillingData();
		ListIterator<BillingSessionBean> it = billingSessionBeans.listIterator();
		int invoiceCount = billingSessionBeans.size();

		while(it.hasNext()) {
			BillingSessionBean billingSessionBean = it.next();

			try {
	            billing = getBillingObj(billingSessionBean, BILL_ACCOUNT_STATUS);
            } catch (ParseException e) {
	            log.error("There was an error extracting the billing object: ", e);
            }

			ArrayList<BillingItem> billItems = billingSessionBean.getBillItem();

			for(BillingItem item : billItems) {
				
				// save indivdual bills.
	            //billing.setBillingNo(0);
	            billingmasterDAO.save(billing);
	            
	            billingmaster = saveBill(
	            		billing.getId(),
	            		billing.getStatus(),
	            		dataCenterId,
	            		item.getDispLineTotal(),
	            		""+PAYMENT_MODE,
	            		billingSessionBean,
	            		"" + item.getUnit()  ,
	            		"" + item.getServiceCode()
	            );
	            
	            billingmasterDAO.save(billingmaster);
	            
	            // billing history entry
	            String billingMasterNo = "" + billingmaster.getBillingmasterNo();
	            
	            billingHistoryDAO.createBillingHistoryArchive(billingMasterNo);
	            BillingNote billingNote = new BillingNote();
                
				billingNote.addNote(billingMasterNo, billingSessionBean.getCreator(), billingSessionBean.getMessageNotes());
				 
			}

		}
		setNumberSaved(invoiceCount);
		return true;
		
	}
	
	/**
	 * remove selected bill from the bill arraylist stored in the quickBillingBCFormBean
	 * @param bill
	 */
	public boolean removeBill(String bill) {
		
		ArrayList<BillingSessionBean> billingSessionBeans = quickBillingBCFormBean.getBillingData();
		billingSessionBeans.remove(Integer.parseInt(bill));
		return true;
	}
	
	/**
	 * Class borrowed from BillingSaveBillingAction
	 * @param bean
	 * @param curDate
	 * @param billingAccountStatus
	 * @return
	 * @throws ParseException 
	 */
    private Billing getBillingObj(BillingSessionBean bean, char billingAccountStatus) throws ParseException{

        Billing bill = new Billing();
        
        bill.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
        bill.setProviderNo(bean.getApptProviderNo());
        bill.setAppointmentNo(APPOINTMENT_NO);
        bill.setDemographicName(bean.getPatientName());
        bill.setHin(bean.getPatientPHN());
        bill.setUpdateDate(today);
        bill.setBillingDate(dateformat.parse(bean.getServiceDate()));
        bill.setTotal(bean.getGrandtotal());
        bill.setStatus(""+billingAccountStatus);
        bill.setDob(bean.getPatientDoB());
        bill.setVisitDate(dateformat.parse(bean.getAdmissionDate()));
        bill.setVisitType(bean.getVisitType());
        bill.setProviderOhipNo(bean.getBillingPracNo());
        bill.setApptProviderNo(bean.getApptProviderNo());
        bill.setCreator(bean.getCreator());
        bill.setBillingtype(bean.getBillingType());
        
        return bill;
    }
    
    /**
     * Again method borrowed from BillingSaveBillingAction.
     * 
     * @param billingid
     * @param billingAccountStatus
     * @param dataCenterId
     * @param billedAmount
     * @param paymentMode
     * @param bean
     * @param billingUnit
     * @param serviceCode
     * @return
     */
     private Billingmaster saveBill(
    		int billingid, 
    		String billingAccountStatus, 
    		String dataCenterId, 
    		String billedAmount, 
    		String paymentMode, 
    		BillingSessionBean bean, 
    		String billingUnit,
    		String serviceCode
    ) {    
        Billingmaster bill = new Billingmaster();
 
        bill.setBillingNo(billingid);
        bill.setCreatedate(today);
        bill.setBillingstatus(billingAccountStatus);
        bill.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
        bill.setAppointmentNo(Integer.parseInt(bean.getApptNo()));
        bill.setClaimcode(CLAIM_CODE);
        bill.setDatacenter(dataCenterId);
        bill.setPayeeNo(bean.getBillingProvider());
        bill.setPractitionerNo(bean.getBillingPracNo());
        bill.setPhn(bean.getPatientPHN());

        bill.setNameVerify(bean.getPatientFirstName(),bean.getPatientLastName());
        bill.setDependentNum(bean.getDependent());
        bill.setBillingUnit(billingUnit); //"" + billItem.getUnit());
        bill.setClarificationCode(bean.getVisitLocation().substring(0, 2));
    
        bill.setAnatomicalArea(ANATOMICAL_AREA);
        bill.setAfterHour(AFTERHOUR_CODE);
       
        bill.setNewProgram(NEW_PROGRAM);        
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

        return bill;
    }
    
    
    /**
     * UTILITY METHOD -REALLY SHOULDN'T BE IN HERE...
     * But I made it static - unlike others.
     * @param s
     * @return
     */
    public static String convertDate8Char(String s) {
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
	
	
}
