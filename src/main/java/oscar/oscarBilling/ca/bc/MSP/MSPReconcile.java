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
package oscar.oscarBilling.ca.bc.MSP;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.billing.CA.BC.dao.BillRecipientsDao;
import org.oscarehr.billing.CA.BC.dao.BillingHistoryDao;
import org.oscarehr.billing.CA.BC.dao.TeleplanAdjCodesDao;
import org.oscarehr.billing.CA.BC.dao.TeleplanC12Dao;
import org.oscarehr.billing.CA.BC.dao.TeleplanRefusalCodeDao;
import org.oscarehr.billing.CA.BC.dao.TeleplanS00Dao;
import org.oscarehr.billing.CA.BC.dao.TeleplanS21Dao;
import org.oscarehr.billing.CA.BC.model.BillRecipients;
import org.oscarehr.billing.CA.BC.model.TeleplanAdjCodes;
import org.oscarehr.billing.CA.BC.model.TeleplanC12;
import org.oscarehr.billing.CA.BC.model.TeleplanRefusalCode;
import org.oscarehr.billing.CA.BC.model.TeleplanS00;
import org.oscarehr.billing.CA.BC.model.TeleplanS21;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.dao.BillingPaymentTypeDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.common.model.BillingPaymentType;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.entities.Billingmaster;
import oscar.entities.MSPBill;
import oscar.entities.Provider;
import oscar.oscarBilling.ca.bc.data.BillRecipient;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarDB.DBHandler;
import oscar.util.BeanUtilHlp;
import oscar.util.ConversionUtils;
import oscar.util.SqlUtils;
import oscar.util.StringUtils;
import oscar.util.UtilMisc;

public class MSPReconcile {
	private static Logger log = MiscUtils.getLogger();
	private BillRecipientsDao billRecipientDao = SpringUtils.getBean(BillRecipientsDao.class);

	private BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	private BillingmasterDAO billingmasterDao = SpringUtils.getBean(BillingmasterDAO.class);

	//Accounting Report Types
	public static final String REP_INVOICE = "REP_INVOICE";
	public static final String REP_PAYREF = "REP_PAYREF";
	public static final String REP_PAYREF_SUM = "REP_PAYREF_SUM";
	public static final String REP_ACCOUNT_REC = "REP_ACCOUNT_REC";
	public static final String REP_REJ = "REP_REJ";
	public static final String REP_WO = "REP_WO";
	public static final String REP_MSPREM = "REP_MSPREM";
	public static final String REP_MSPREMSUM = "REP_MSPREMSUM";
	public static final String REP_MSPREMSUM_PRACTSUM = "REP_MSPREMSUM_PRACTSUM";
	public static final String REP_MSPREMSUM_S23 = "REP_MSPREMSUM_S23";
	public static final String REP_MSPREMSUM_S23_ORPHAN = "REP_MSPREMSUM_S23_ORPHAN";

	//MSP Bill Status Types
	public static final String REJECTED = "R";
	public static final String NOTSUBMITTED = "O";
	public static final String SUBMITTED = "B";
	public static final String SETTLED = "S";
	public static final String DELETED = "D";
	public static final String HELD = "Z";
	public static final String DATACENTERCHANGED = "C";
	public static final String PAIDWITHEXP = "E";
	public static final String REFUSED = "F";
	public static final String BADDEBT = "X";
	public static final String WCB = "W";
	public static final String CAPITATED = "H";
	public static final String DONOTBILL = "N";
	public static final String BILLPATIENT = "P";
	public static final String COLLECTION = "T";
	public static final String PAIDPRIVATE = "A";

	private static Properties negValues = new Properties();
	public static final String BILLTYPE_PRI = "Pri";
	public static final String BILLTYPE_MSP = "MSP";
	public static final String BILLTYPE_ICBC = "ICBC";
	public static final String BILLTYPE_WCB = "WCB";

	public static final String PAYTYPE_CASH = "1";
	public static final String PAYTYPE_CHEQUE = "2";
	public static final String PAYTYPE_VISA = "3";
	public static final String PAYTYPE_MC = "4";
	public static final String PAYTYPE_AMEX = "5";
	public static final String PAYTYPE_ELECTRONIC = "6";
	public static final String PAYTYPE_DEBIT = "7";
	public static final String PAYTYPE_OTHER = "8";
	public static final String PAYTYPE_NA = "9";
	/**Not truly a type of payment **/
	public static final String PAYTYPE_IA = "10";

	public static final String DATE_FORMAT = "yyyyMMdd";
	private SimpleDateFormat fmt = null;
	private BeanUtilHlp beanut = new BeanUtilHlp();
	private BillingHistoryDAO dao = new BillingHistoryDAO();

	public MSPReconcile() {
		initTeleplanMonetarySuffixes();
		fmt = new SimpleDateFormat(DATE_FORMAT);
		//if (!patchApplied()) {
		//  migratePrivateTransactions();
		//  updatePrivateBillState();
		//  setPatched();
		//}
	}

	String propFile = "patch.properties";
	String key = "patched";
	String value = "true";

//	@Deprecated
//	private void setPatched() {
//		Properties patchInd = new Properties();
//		patchInd.setProperty(key, value);
//		try {
//			patchInd.store(new FileOutputStream(propFile), null);
//		} catch (IOException e) {
//			MiscUtils.getLogger().error("Error", e);
//		}
//	}
//
//	@Deprecated
//	private boolean patchApplied() {
//		boolean ret = false;
//		Properties patchInd = new Properties();
//		try {
//			patchInd.load(new FileInputStream(propFile));
//			String prop = patchInd.getProperty(key);
//			if (prop != null && value.equals(prop)) {
//				ret = true;
//			}
//		} catch (IOException e) {
//			MiscUtils.getLogger().error("Error", e);
//		}
//		return ret;
//	}

	/**
	 * Initializes the Teleplan Payment suffixes.</P?
	 * Teleplan employs a (COBOL??!) type numeric system wherein negative values
	 * are represented with a non-numeric suffix, indicating the last numeral of the value.
	 *
	 * <p>The suffixes are as follows:</p>
	 * } = 0<b/>
	 * J = 1<b/>
	 * K = 2<b/>
	 * L = 3<b/>
	 * M = 4<b/>
	 * N = 5<b/>
	 * O = 6<b/>
	 * P = 7<b/>
	 * Q = 8<b/>
	 * R = 9<b/>
	 *
	 */
	private void initTeleplanMonetarySuffixes() {
		negValues.setProperty("}", "0");
		negValues.setProperty("J", "1");
		negValues.setProperty("K", "2");
		negValues.setProperty("L", "3");
		negValues.setProperty("M", "4");
		negValues.setProperty("N", "5");
		negValues.setProperty("O", "6");
		negValues.setProperty("P", "7");
		negValues.setProperty("Q", "8");
		negValues.setProperty("R", "9");
	}

	/**
	 * @todo This needs to go , the data is defined in the database
	 * @param stat String
	 * @return String
	 */
	public String getStatusDesc(String stat) {
		String statusDesc = "";
		if (stat.equals(REJECTED)) {
			statusDesc = "REJ";
		} else if (stat.equals(NOTSUBMITTED)) {
			statusDesc = "NOSUB";
		} else if (stat.equals(SUBMITTED)) {
			statusDesc = "SUB";
		} else if (stat.equals(SETTLED)) {
			statusDesc = "SET";
		} else if (stat.equals(DELETED)) {
			statusDesc = "DEL";
		} else if (stat.equals(HELD)) {
			statusDesc = "HELD";
		} else if (stat.equals(DATACENTERCHANGED)) {
			statusDesc = "DCC";
		} else if (stat.equals(PAIDWITHEXP)) {
			statusDesc = "PWE";
		} else if (stat.equals(REFUSED)) {
			statusDesc = "REF";
		} else if (stat.equals(BADDEBT)) {
			statusDesc = "BAD";
		} else if (stat.equals(WCB)) {
			statusDesc = "WCB";
		} else if (stat.equals(CAPITATED)) {
			statusDesc = "CAP";
		} else if (stat.equals(DONOTBILL)) {
			statusDesc = "DNB";
		} else if (stat.equals(BILLPATIENT)) {
			statusDesc = "BP";
		} else if (stat.equals(COLLECTION)) {
			statusDesc = "COL";
		} else if (stat.equals(PAIDPRIVATE)) {
			statusDesc = "PRIV";
		}

		return statusDesc;
	}

	private HashMap<String, Vector<String>> getRejectionDetails() {
		
		TeleplanC12Dao dao = SpringUtils.getBean(TeleplanC12Dao.class);
		HashMap<String, Vector<String>> map = new HashMap<String, Vector<String>>();
		List<Object[]> rejectedList = dao.findRejected();
		
		for ( Object[] rejectedArray : rejectedList ) {
			
			TeleplanC12 tc = null;
			TeleplanS21 ts = null;
			String officeFolioClaimNo = "";

			if( rejectedArray == null ) {
				continue;
			}
			
			Vector<String> exp = new Vector<String>();
			
			if( rejectedArray.length > 0 ) {
				tc = (TeleplanC12) rejectedArray[1];
			}
			
			if( rejectedArray.length > 1 ) {
				ts = (TeleplanS21) rejectedArray[2];
			}

			if( tc != null ) {				
				exp.addAll(Arrays.asList(tc.getExps()));
				officeFolioClaimNo = tc.getOfficeFolioClaimNo();
				officeFolioClaimNo = officeFolioClaimNo.replaceFirst("^0+(?!$)", "").trim();
			}
			
			if( ts != null ) {
				exp.add( ts.getPayment() );
			}
			
			map.put( officeFolioClaimNo, exp );
			
		}
		
		return map;
	}

	public Properties currentC12Records() {
		Properties p = new Properties();
		TeleplanC12Dao dao = SpringUtils.getBean(TeleplanC12Dao.class);

		for (TeleplanC12 tc : dao.findCurrent()) {

			try {
				int i = Integer.parseInt(tc.getOfficeFolioClaimNo()); // this kludge rids leading zeros
				String exp[] = tc.getExps();
				String def = createCorrectionsString(exp);
				String s = Integer.toString(i);
				p.put(s, def);
			} catch (NumberFormatException intEx) {
				MiscUtils.getLogger().error("Error", intEx);
				MiscUtils.getLogger().debug("Had trouble Parsing int from " + tc.getOfficeFolioClaimNo());
			}
		}
		return p;
	}

	public String getS00String(String billingMasterNo) {
		String s = "";
		TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);
		for (TeleplanS00 ts : dao.findByOfficeNumber(forwardZero(billingMasterNo, 7))) {
			String exp[] = ts.getExps();
			s = createCorrectionsString(exp);
		}
		return s;
	}

	private String createCorrectionsString(String[] exp) {
		String retval = "";
		for (int i = 0; i < exp.length; i++) {
			if (exp[i].length() != 0) {
				retval += exp[i] + " ";
			}
		}
		return retval;
	}

	/**
	 * MSPReconcile Nested Class BillSearch.
	 */
	public class BillSearch {
		Properties p;
		public List<Object> list;
		int count = 0;
		ArrayList<String> justBillingMaster;

		public Properties getCurrentErrorMessages() {
			Properties errorsProps = new Properties();
			if (count > 0) {
				TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);
				List<String> officeNumbers = new ArrayList<String>();
				for (int i = 0; i < justBillingMaster.size(); i++) {
					officeNumbers.add(forwardZero(justBillingMaster.get(i), 7));
				}

				for (TeleplanS00 ts : dao.findByOfficeNumbers(justBillingMaster)) {
					try {
						int i = Integer.parseInt(ts.getOfficeNo()); // this kludge rids leading zeros
						String exp[] = ts.getExps();
						String def = createCorrectionsString(exp);
						String s = Integer.toString(i);
						errorsProps.put(s, def);
					} catch (NumberFormatException intEx) {
						MiscUtils.getLogger().error("Error", intEx);
						MiscUtils.getLogger().debug("Had trouble Parsing int from " + ts.getOfficeNo());
					}
				}

			}
			MiscUtils.getLogger().debug("errorsProps=" + errorsProps.isEmpty());
			return errorsProps;
		}
	}

	public String getMaxSeqNum(String billingMasterNo) {
		String maxNum = "";
		ArrayList<String> seqNums = getSequenceNumbers(billingMasterNo);
		if (!seqNums.isEmpty()) {
			Arrays.sort(seqNums.toArray());
			maxNum = seqNums.get(seqNums.size() - 1);
		}
		return maxNum;
	}

	public ArrayList<String> getSequenceNumbers(String billingNo) {
		ArrayList<String> retval = new ArrayList<String>();
		TeleplanC12Dao cDao = SpringUtils.getBean(TeleplanC12Dao.class);
		for (TeleplanC12 c : cDao.findByOfficeClaimNo(forwardZero(billingNo, 7))) {
			retval.add(c.getDataSeq());
		}
		TeleplanS00Dao sDao = SpringUtils.getBean(TeleplanS00Dao.class);
		for (TeleplanS00 s : sDao.findByOfficeNumber(forwardZero(billingNo, 7))) {
			retval.add(s.getDataSeq());
		}
		return retval;
	}

	/**
	 * Returns a map of values from the teleplanS00 table for quick lookup of
	 * teleplan data(solves a performance problem related to joining the teleplanS00 table with the billingmaster table
	 * due to the fact that related fields teleplanS00.t_officeno and billingmaster.billingmaster_no are of a different type
	 * The resultant map currently returns a key/value pair containing the t_officeno and t_dataseq respectiveley
	 * @return Map
	 */
	public Map<String, String> getS00Map() {
		HashMap<String, String> map = new HashMap<String, String>();
		TeleplanS00Dao sDao = SpringUtils.getBean(TeleplanS00Dao.class);
		for (TeleplanS00 s : sDao.findAll()) {
			String value = s.getDataSeq() != null ? s.getDataSeq() : "";
			String key = s.getOfficeNo();
			map.put(key, value);
		}
		return map;
	}

	public BillSearch getBills(String statusType, String providerNo, String startDate, String endDate) {
		return getBills(statusType, providerNo, startDate, endDate, null, false, false, false, false);
	}

	public BillSearch getBills(String statusType, String providerNo, String startDate, String endDate, String demoNo) {
		return getBills(statusType, providerNo, startDate, endDate, demoNo, false, false, false, false);
	}

	public BillSearch getBills(String statusType, String providerNo, String startDate, String endDate, String demoNo, boolean excludeWCB, boolean excludeMSP, boolean excludePrivate, boolean exludeICBC) {

		BillSearch billSearch = new BillSearch();
		billSearch.list = new ArrayList<Object>();
		billSearch.count = 0;
		billSearch.justBillingMaster = new ArrayList<String>();

		BillingDao dao = SpringUtils.getBean(BillingDao.class);
		for (Object[] o : dao.findByManyThings(statusType, providerNo, startDate, endDate, demoNo, excludeWCB, excludeMSP, excludePrivate, exludeICBC)) {
			Bill b = new Bill();
			b.billing_no = "" + o[0];
			b.apptDoctorNo = "" + o[16];
			b.apptNo = "" + o[7];
			b.demoNo = "" + o[1];
			b.demoName = "" + o[2];
			b.userno = "" + o[16];
			b.apptDate = "" + o[8];
			b.apptTime = "" + o[9];
			b.reason = "" + o[10];
			b.billMasterNo = "" + o[19];
			b.billingtype = "" + o[4];
			b.amount = "" + o[11];
			b.code = "" + o[12];
			b.dx1 = "" + o[13];
			b.dx2 = "" + o[14];
			b.dx3 = "" + o[15];
			b.providerFirstName = "" + o[20];
			b.providerLastName = "" + o[21];
			b.quantity = "" + o[22];

			Object[] seqNumsArray = getSequenceNumbers(b.billMasterNo).toArray();
			Arrays.sort(seqNumsArray);
			//Assign the geatest sequence number to this field
			b.seqNum = "";
			if (seqNumsArray.length > 0) {
				b.seqNum = seqNumsArray[seqNumsArray.length - 1].toString();
			}
			billSearch.justBillingMaster.add(b.billMasterNo);
			billSearch.list.add(b);
			billSearch.count++;
		}

		return billSearch;
	}

	public ArrayList<MSPBill> getBillsMaster(String statusType) {
		BillingDao dao = SpringUtils.getBean(BillingDao.class);
		ArrayList<MSPBill> list = new ArrayList<MSPBill>();

		for (Object[] o : dao.findBillingsByStatus(statusType)) {
			Billing bi = (Billing) o[0];
			Billingmaster bm = (Billingmaster) o[1];

			MSPBill b = new MSPBill();
			b.billing_no = "" + bi.getId();
			b.apptDoctorNo = bi.getApptProviderNo();
			b.apptNo = "" + bi.getAppointmentNo();
			b.demoNo = "" + bi.getDemographicNo();
			b.demoName = bi.getDemographicName();
			b.userno = bi.getProviderNo();
			b.apptDate = ConversionUtils.toDateString(bi.getBillingDate());
			b.apptTime = ConversionUtils.toTimeString(bi.getBillingTime());
			b.reason = bm.getBillingstatus();
			b.billMasterNo = "" + bm.getBillingmasterNo();

			b.amount = "" + bm.getBillAmount();
			b.code = bm.getBillingCode();
			b.dx1 = bm.getDxCode1();
			b.dx2 = bm.getDxCode2();
			b.dx3 = bm.getDxCode3();
			list.add(b);
		}

		return list;
	}

	public String getApptStyle(String s, String userno) {
		String retval = "";
		if (s.equals("none")) {
			retval = "No Appt / INR";
		} else {
			if (s.equals(userno)) {
				retval = "With Appt. Doctor";
			} else {
				retval = "Unmatched Appt. Doctor";
			}
		}

		return retval;
	}

	public class Bill {
		public String billing_no = "";
		public String apptDoctorNo = "";
		public String apptNo = "";
		public String demoNo = "";
		public String demoName = "";
		public String userno = "";
		public String apptDate = "";
		public String apptTime = "";
		public String reason = "";
		public String billMasterNo = "";
		public String billingtype = "";

		public String code = "";
		public String amount = "";
		public String dx1 = "";
		public String dx2 = "";
		public String dx3 = "";
		public String providerFirstName = "";
		public String providerLastName = "";
		public String quantity = "";

		public double amountPaid;
		public String seqNum;

		public void setAmountPaid(String paid) {
			try {
				Double dbl = new Double(paid);
				this.amountPaid = dbl.doubleValue();
			} catch (Exception ex) {
				MiscUtils.getLogger().error("Error", ex);
			}
		}

		public double getAmountPaid() {
			return this.amountPaid;
		}

		public boolean isWCB() {
			boolean retval = false;
			if (billingtype.equals("WCB")) {
				retval = true;
			}
			return retval;
		}

		public double getAmount() {
			double ret = 0;
			try {
				Double dbl = new Double(this.amount);
				ret = dbl.doubleValue();
			} catch (Exception ex) {
				MiscUtils.getLogger().error("Error", ex);
			}
			return ret;

		}

		public String getApptDate() {
			return apptDate;
		}

		public String getApptDoctorNo() {
			return apptDoctorNo;
		}

		public String getApptNo() {
			return apptNo;
		}

		public String getApptTime() {
			return apptTime;
		}

		public String getUserno() {
			return userno;
		}

		public String getReason() {
			return reason;
		}

		public String getQuantity() {
			return quantity;
		}

		public String getProviderLastName() {
			return providerLastName;
		}

		public String getProviderFirstName() {
			return providerFirstName;
		}

		public String getDx3() {
			return dx3;
		}

		public String getDx2() {
			return dx2;
		}

		public String getDx1() {
			return dx1;
		}

		public String getDemoNo() {
			return demoNo;
		}

		public String getDemoName() {
			return demoName;
		}

		public String getCode() {
			return code;
		}

		public String getBillMasterNo() {
			return billMasterNo;
		}

		public String getBillingtype() {
			return billingtype;
		}

		public String getBilling_no() {
			return billing_no;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public void setApptDate(String apptDate) {
			this.apptDate = apptDate;
		}

		public void setApptDoctorNo(String apptDoctorNo) {
			this.apptDoctorNo = apptDoctorNo;
		}

		public void setApptNo(String apptNo) {
			this.apptNo = apptNo;
		}

		public void setApptTime(String apptTime) {
			this.apptTime = apptTime;
		}

		public void setBilling_no(String billing_no) {
			this.billing_no = billing_no;
		}

		public void setBillingtype(String billingtype) {
			this.billingtype = billingtype;
		}

		public void setBillMasterNo(String billMasterNo) {
			this.billMasterNo = billMasterNo;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public void setDemoName(String demoName) {
			this.demoName = demoName;
		}

		public void setDemoNo(String demoNo) {
			this.demoNo = demoNo;
		}

		public void setDx1(String dx1) {
			this.dx1 = dx1;
		}

		public void setDx2(String dx2) {
			this.dx2 = dx2;
		}

		public void setDx3(String dx3) {
			this.dx3 = dx3;
		}

		public void setProviderFirstName(String providerFirstName) {
			this.providerFirstName = providerFirstName;
		}

		public void setProviderLastName(String providerLastName) {
			this.providerLastName = providerLastName;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public void setUserno(String userno) {
			this.userno = userno;
		}
	}

	public ArrayList<String> getAllC12Records(String billingNo) {
		ArrayList<String> retval = new ArrayList<String>();
		Properties p = new MspErrorCodes();
		TeleplanC12Dao dao = SpringUtils.getBean(TeleplanC12Dao.class);

		for (TeleplanC12 t : dao.findByOfficeClaimNo(forwardZero(billingNo, 7))) {
			String exp[] = t.getExps();
			String seq = t.getDataSeq();

			for (int i = 0; i < exp.length; i++) {
				if (exp[i].length() != 0) {
					retval.add(seq + "&nbsp;&nbsp;" + exp[i] + "&nbsp;&nbsp;" + p.getProperty(exp[i], ""));
				}
			}
		}
		return retval;
	}

	/**
	 * Returns the amount paid to a specific line item(billingmaster_no) in a bill
	 * Amounts are derived from two potential sources
	 *  1.)teleplanS00 table where msp payments are stored
	 *  2.)billing_history table where internal adjustments are stored
	 *
	 * @param billingmaster_no String
	 * @param billType String
	 * @return double the amount paid
	 */
	public double getAmountPaid(String billingmaster_no, String billType) {
		double retval = 0.0;
		//for msp,icbc,wcb payments
		if (!MSPReconcile.BILLTYPE_PRI.equalsIgnoreCase(billType)) {
			retval = getTotalPaidFromS00(billingmaster_no);
		} else {
			retval = getTotalPaidFromHistory(billingmaster_no, true);
		}
		return retval;
	}

	/**
	 * Returns the sum total of payments that were received for the specified billingmaster record
	 * from the teleplanS00 table.
	 * @param billingmaster_no String
	 * @return double
	 */
	private double getTotalPaidFromS00(String billingmaster_no) {
		double retval = 0.0;
		TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);
		for(TeleplanS00 s : dao.findByOfficeNumber(forwardZero(billingmaster_no, 7))) {
			//this line fixes a bug where the amounts weren't calculating negative values
			String strAmount = convCurValue(s.getPaidAmount());
			double amount = new Double(strAmount).doubleValue();
			retval += amount;			
		}
		return retval;
	}

	/**
	 * Returns the sum total of payments that were received for the specified billingmaster record
	 * from the billinghistory table.
	 * @param billingmaster_no String - The uid of the billingmaster record in question
	 * @param ignoreIA - Flag to ignore Internal Adjustments if set to true
	 * @return double
	 */
	private double getTotalPaidFromHistory(String billingmaster_no, boolean ignoreIA) {
		BillingHistoryDao dao = SpringUtils.getBean(BillingHistoryDao.class);
		return dao.getTotalPaidFromHistory(ConversionUtils.fromIntString(billingmaster_no), ignoreIA);
	}

	public ArrayList<String> getAllS00Records(String billingNo) {
		ArrayList<String> retval = new ArrayList<String>();
		Properties p = new MspErrorCodes();

		TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);
		for (TeleplanS00 s : dao.findByOfficeNumber(forwardZero(billingNo, 7))) {
			String exp[] = s.getExps();
			String seq = s.getDataSeq();

			for (int i = 0; i < exp.length; i++) {
				if (exp[i].length() != 0) {
					retval.add(seq + "&nbsp;&nbsp;" + exp[i] + "&nbsp;&nbsp;" + p.getProperty(exp[i], ""));
				}
			}
		}
		return retval;
	}

	public Properties getBillingMasterRecord(String billingNo) {
		Properties p = null;
		String name = null;
		String value = null;

		BillingmasterDAO dao = SpringUtils.getBean(BillingmasterDAO.class);
		Billingmaster bm = dao.getBillingmaster(billingNo);
		if (bm != null) {
			p = new Properties();
			try {
				BeanInfo bi = Introspector.getBeanInfo(bm.getClass());
				for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
					name = pd.getName();
					Object valueAsObj = pd.getReadMethod().invoke(bm, new Object[] {});
					value = (valueAsObj == null) ? "" : valueAsObj.toString();

					p.setProperty(name, value);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().debug("name: " + name + " value: " + value);
				MiscUtils.getLogger().error("Error", e);
			}

		}

		return p;
	}

	/**
	 * Saves a BillRecipient to database. If a record with the specified  billing number exists, an update is performed<p>
	 * otherwise, a new record is inserted
	 *
	 * @param recip BillReceipient - The BillRecipient instance to be persisted
	 */
	public void saveOrUpdateBillRecipient(BillRecipient recip) {
		List<BillRecipients> bs = billRecipientDao.findByBillingNo(Integer.parseInt(recip.getBillingNo()));
		boolean existingBill = false;
		if (bs.size() > 0) existingBill = true;

		if (existingBill) {
			for (BillRecipients b : bs) {
				b.setName(recip.getName());
				b.setAddress(recip.getAddress());
				b.setCity(recip.getCity());
				b.setProvince(recip.getProvince());
				// remove spaces from postal
				String postal = removeSpaces( recip.getPostal() );
				postal = postal.toUpperCase();
				b.setPostal( postal );
				b.setUpdateTime(new Date());
				billRecipientDao.merge(b);
			}
		} else {
			BillRecipients b = new BillRecipients();
			b.setName(recip.getName());
			b.setAddress(recip.getAddress());
			b.setCity(recip.getCity());
			b.setProvince(recip.getProvince());
			// remove spaces from postal
			String postal = removeSpaces( recip.getPostal() );
			postal = postal.toUpperCase();
			b.setPostal( postal );
			b.setCreationTime(new Date());
			b.setUpdateTime(new Date());
			b.setBillingNo(Integer.parseInt(recip.getBillingNo()));
			billRecipientDao.persist(b);
		}

	}
	
	private static String removeSpaces(String str) {
		String newstr = "";
		if( str != null && ! str.isEmpty() ) {
			newstr = str.replaceAll("\\s","");			
		}
		return newstr;
	}

	/**
	 * @Deprecated use method signature updateBillingStatusWCB(String stat, String billingMasterNo
	 */
	@Deprecated
	public void updateBillingStatusWCB(@SuppressWarnings("unused") String billingNo, String stat, String billingMasterNo) {
		updateBillingStatusHlp2(billingMasterNo, stat);
	}
	
	/**
	 * Updates the status of a bill but doesn't change it's type.Created because a WCB bill can not be
	 * a WCB type Bill and a PRIVATE bill at the same time.
	 */
	public void updateBillingStatusWCB(String stat, String billingMasterNo) {
		updateBillingStatusHlp2(billingMasterNo, stat);
	}

	/**
	 * Updates the status of a the specified bill and adjusts the state of
	 * of associated bill parameters including: bill type, payment method
	 * e.g if the status of the bill is changed to 'A'(billpatient)
	 * the corresponding billingtype is changed to 'Pri' to reflect this update
	 * @param billingNo String
	 * @param stat String
	 */
	public void updateBillingStatus(String billingNo, String stat, String billingMasterNo) {
		updateBillingStatusHlp2(billingMasterNo, stat);
		String paymentMethod = MSPReconcile.PAYTYPE_ELECTRONIC;
		if (MSPReconcile.BILLPATIENT.equals(stat) || MSPReconcile.PAIDPRIVATE.equals(stat)) {
			this.updateBillTypeHlp(billingNo, BILLTYPE_PRI);
			paymentMethod = MSPReconcile.PAYTYPE_NA;
		} else if (MSPReconcile.WCB.equals(stat)) {
			this.updateBillTypeHlp(billingNo, BILLTYPE_WCB);
		} else if (MSPReconcile.NOTSUBMITTED.equals(stat)) {
			this.updateBillTypeHlp(billingNo, BILLTYPE_MSP);
		}

		updatePaymentMethodHlp(billingMasterNo, paymentMethod);
	}

	//Only updates only the billingmaster status
	private void updateBillingStatusHlp2(String billingNo, String stat) {
		Billingmaster b = billingmasterDao.getBillingmaster(Integer.parseInt(billingNo));
		if (b != null) {
			b.setBillingstatus(stat);
			billingmasterDao.update(b);
		}
	}

	private void updateBillingStatusHlp(String billingNo, String stat) {
		Billingmaster b = billingmasterDao.getBillingmaster(Integer.parseInt(billingNo));
		if (b != null) {
			b.setBillingstatus(stat);
			billingmasterDao.update(b);
		}

		Billing bb = billingDao.find(Integer.parseInt(billingNo));
		if (bb != null) {
			bb.setStatus(stat);
			billingDao.merge(bb);
		}

	}


//	@Deprecated
//	private void updatePaymentMethod(String billingNo, String paymentMethod) {
//		updatePaymentMethodHlp(billingNo, paymentMethod);
//		//if this is a private bill, update the status to bill patient
//		if (!MSPReconcile.PAYTYPE_ELECTRONIC.equals(paymentMethod)) {
//			this.updateBillingStatusHlp(billingNo, MSPReconcile.BILLPATIENT);
//		}
//	}

	/**
	 * Updates the paymentMethod of the specified bill with the supplied paymentMethod code
	 * @param billingNo String - The uid of the bill to be updated
	 * @param paymentMethod String - The paymentMethod code
	 */
	private void updatePaymentMethodHlp(String billingMasterNo, String paymentMethod) {
		Billingmaster b = billingmasterDao.getBillingmaster(Integer.parseInt(billingMasterNo));
		if (b != null) {
			b.setPaymentMethod(Integer.parseInt(paymentMethod));
			billingmasterDao.update(b);
		}
	}

	/**
	 * Updates the specified bill with the
	 * @param billingNo String
	 * @param type String
	 */
	public void updateBillType(String billingNo, String type) {
		updateBillTypeHlp(billingNo, type);
		String paymentMethod = MSPReconcile.PAYTYPE_ELECTRONIC;
		if (MSPReconcile.BILLTYPE_PRI.equals(type)) {
			this.updateBillingStatusHlp(billingNo, MSPReconcile.BILLPATIENT);
			paymentMethod = MSPReconcile.PAYTYPE_NA;
		} else if (MSPReconcile.BILLTYPE_WCB.equals(type)) {
			this.updateBillingStatusHlp(billingNo, MSPReconcile.WCB);
		}
		this.updatePaymentMethodHlp(billingNo, paymentMethod);
	}

	/**
	 * Updates the billingtype of the specified billing record
	 * @param billingNo String - The uid of the record to be updated
	 * @param billType String - The type of bill
	 */
	private void updateBillTypeHlp(String billingNo, String billType) {
		Billing b = billingDao.find(Integer.parseInt(billingNo));
		if (b != null) {
			b.setBillingtype(billType);
			billingDao.merge(b);
		}
	}

	/**
	 * Updates the status of the specified billingmaster record
	 * @param billingMasterNo String
	 * @param stat String
	 */
	public void updateBillingMasterStatus(String billingMasterNo, String stat) {
		log.debug("setting billingmaster_no " + billingMasterNo + " to " + stat);
		Billingmaster b = billingmasterDao.getBillingmaster(Integer.parseInt(billingMasterNo));
		if (b != null) {
			b.setBillingstatus(stat);
			billingmasterDao.update(b);
		}

	}

	public boolean updateStat(String stat, String billingNo) {
		//get current status of bill
		boolean updated = true;
		String currStat = "";
		String newStat = "";
		
		BillingmasterDAO bmDao = SpringUtils.getBean(BillingmasterDAO.class);
		Billingmaster bm = bmDao.getBillingmaster(ConversionUtils.fromIntString(billingNo));

		if (bm != null) {
			currStat = bm.getBillingstatus();
		}
		
		if (!currStat.equals(SETTLED)) {
			if (stat.equals(REJECTED)) {
				newStat = REJECTED;
			} else if (stat.equals(NOTSUBMITTED)) {
				newStat = NOTSUBMITTED;
			} else if (stat.equals(SUBMITTED)) {
				newStat = SUBMITTED;
			} else if (stat.equals(SETTLED)) {
				newStat = SETTLED;
			} else if (stat.equals(DELETED)) {
				newStat = DELETED;
			} else if (stat.equals(HELD)) {
				newStat = HELD;
			} else if (stat.equals(DATACENTERCHANGED)) {
				newStat = DATACENTERCHANGED;
			} else if (stat.equals(PAIDWITHEXP)) {
				newStat = PAIDWITHEXP;
			} else if (stat.equals(BADDEBT)) {
				newStat = BADDEBT;
			} else if (stat.equals(WCB)) {
				newStat = WCB;
			} else if (stat.equals(CAPITATED)) {
				newStat = CAPITATED;
			} else if (stat.equals(DONOTBILL)) {
				newStat = DONOTBILL;
			} else if (stat.equals(BILLPATIENT)) {
				newStat = BILLPATIENT;
			}
		} else {
			updated = false;
			MiscUtils.getLogger().debug("billing No " + billingNo + " is settled, will not be updated");
		}
		if (updated) {
			try {

				Billingmaster b = billingmasterDao.getBillingmaster(Integer.parseInt(billingNo));
				if (b != null) {
					b.setBillingstatus(newStat);
					billingmasterDao.update(b);
				}

				dao.createBillingHistoryArchive(billingNo);
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}
		return updated;
	}

	public String forwardZero(String y, int x) {
		String returnZeroValue = new String();
		for (int i = y.length(); i < x; i++) {
			returnZeroValue += "0";
		}
		return returnZeroValue + y;
	}

	/**
	 * * Returns a BillSearch object containing a list of Bills according to the specified criteria
	 *
	 *
	 * @param account String
	 * @param payeeNo String - The Payee responsible for the bill
	 * @param providerNo String - The practitioner whom provided the billable service
	 * @param startDate String - The lower limit of the specified date range
	 * @param endDate String - The upper limit of the specified date range
	 * @param excludeWCB boolean - Indicates whether to search for WCB insurer
	 * @param excludeMSP boolean - Indicates whether to search for MSP insurer
	 * @param excludePrivate boolean - Indicates whether to search for Private insurer
	 * @param exludeICBC boolean - Indicates whether to search for ICBC insurer
	 * @param type String
	 * @return BillSearch
	 */
	public MSPReconcile.BillSearch getBillsByType(String account, String payeeNo, String providerNo, String startDate, String endDate, boolean excludeWCB, 
			boolean excludeMSP, boolean excludePrivate, boolean exludeICBC, String type) {
		BillSearch billSearch = new BillSearch();
		HashMap<String, Vector<String>> rejDetails = null;
		boolean skipBill = false;
		String criteriaQry = createCriteriaString(account, payeeNo, providerNo, startDate, endDate, excludeWCB, excludeMSP, excludePrivate, exludeICBC, type, "");
		Properties c12 = new Properties();
		String orderByClause = "order by billingstatus";

		if (REP_ACCOUNT_REC.equals(type)) {
			orderByClause = "order by bs.sortOrder,bm.paymentMethod,b.demographic_name,bm.service_date";
		} else if (MSPReconcile.REP_INVOICE.equals(type)) {
			orderByClause = "order by b.provider_no,bt.sortOrder,bm.service_date,b.demographic_name";
			c12 = currentC12Records();
		}
		String p = "select provider.first_name,provider.last_name,b.billingtype, b.update_date, bm.billingmaster_no,b.billing_no, " + " b.demographic_name,b.demographic_no,bm.billing_unit,bm.billing_code,bm.bill_amount,bm.billingstatus,bm.mva_claim_code,bm.service_location," + " bm.phn,bm.service_end_time,service_start_time,bm.service_to_day,bm.service_date,bm.oin_sex_code,b.dob,dx_code1,b.provider_no,apptProvider_no,bt.sortOrder "
		        + " from demographic,provider,billing as b left join billingtypes bt on b.billingtype = bt.billingtype ,billingmaster as bm left join billingstatus_types bs on bm.billingstatus = bs.billingstatus" + " where bm.billing_no=b.billing_no " + " and b.provider_no = provider.provider_no " + " and demographic.demographic_no = b.demographic_no " + criteriaQry + " " + orderByClause;

		if (type.equals(REP_REJ)) {
			rejDetails = this.getRejectionDetails();
		}

		billSearch.list = new ArrayList<Object>();
		billSearch.count = 0;
		billSearch.justBillingMaster = new ArrayList<String>();

		ResultSet rs = null;
		MiscUtils.getLogger().debug("p=" + p);
		try {

			rs = DBHandler.GetSQL(p);

			while (rs.next()) {
				MSPBill b = new MSPBill();
				b.billingtype = rs.getString("b.billingtype");
				b.billing_no = rs.getString("billing_no");
				b.demoNo = rs.getString("demographic_no");
				b.billingUnit = rs.getString("billing_unit");
				b.demoName = rs.getString("demographic_name");
				b.apptDate = rs.getString("update_date");
				b.reason = rs.getString("billingstatus");
				b.serviceEndTime = rs.getString("service_end_time");
				b.serviceStartTime = rs.getString("service_start_time");
				b.serviceToDate = rs.getString("service_to_day");
				b.status = b.reason;
				b.billMasterNo = rs.getString("billingmaster_no");
				String expStr = getS00String(b.billMasterNo);
				b.expString = "".equals(expStr) ? expStr : "(" + expStr + ")";
				b.reason = this.getStatusDesc(b.reason);
				b.amount = rs.getString("bill_amount");
				b.code = rs.getString("billing_code");
				b.dx1 = rs.getString("dx_code1");	
				b.serviceDate = rs.getString("service_date").equals("") ? "00000000" : rs.getString("service_date");
				b.mvaCode = rs.getString("mva_claim_code");
				b.hin = rs.getString("phn");
				b.serviceLocation = rs.getString("service_location");
				b.demoDOB = rs.getString("dob");
				b.demoSex = rs.getString("oin_sex_code");
				b.apptDoctorNo = rs.getString("apptProvider_no");
				b.accountNo = rs.getString("b.provider_no");
				b.updateDate = rs.getString("update_date");
				oscar.entities.Provider accountProvider = this.getProvider(b.accountNo, 0);
				b.accountName = accountProvider.getFullName();
				b.payeeName = accountProvider.getInitials();			
				b.providerFirstName = rs.getString("first_name");
				b.providerLastName = rs.getString("last_name");			
				b.provName = this.getProvider(b.apptDoctorNo, 1).getInitials();

				// WCB SECTION ---------------------------------------------------------
				if (b.isWCB()) {
					String wcbQry = "select bill_amount,w_feeitem,w_icd9 from wcb where billing_no = '" + b.billing_no + "'";
					String[] wcbRow = SqlUtils.getRow(wcbQry);
					if (wcbRow != null) {
						b.amount = wcbRow[0];
						b.code = wcbRow[1];
						b.dx1 = wcbRow[2];
					}
				}
				// REJECTED SECTION ---------------------------------------------------------
				if (type.equals(REP_REJ)) {
					if (rejDetails.containsKey(b.billMasterNo)) {
						Vector<String> dets = rejDetails.get(b.billMasterNo);
						String[] exps = new String[7];
						for (int i = 0; i < exps.length; i++) {
							exps[i] = dets.get(i);
						}
						b.expString = this.createCorrectionsString(exps);
						Hashtable<String, String> explCodes = new Hashtable<String, String>();
						for (int i = 0; i < exps.length; i++) {
							String code = exps[i];
							String desc = this.getC12Description(code);
							explCodes.put(code, desc);
						}
						b.explanations = explCodes;
						b.rejectionDate = dets.get(7);
						if (b.rejectionDate == null || b.rejectionDate.equals("")) {
							b.rejectionDate = "00000000";
						}
					}

					ResultSet rsDemo = DBHandler.GetSQL("select phone,phone2 from demographic where demographic_no = " + b.demoNo);
					if (rsDemo.next()) {
						b.demoPhone = rsDemo.getString("phone");
						b.demoPhone2 = rsDemo.getString("phone2");
					}
				}

				else if (MSPReconcile.REP_INVOICE.equals(type)) {
					double dblAmtOwing = this.getAmountOwing(b.billMasterNo, b.amount, b.billingtype);
					b.amtOwing = String.valueOf(dblAmtOwing);
					//append the explanatory code to end of reason field
					String expString = c12.getProperty(b.billing_no) != null ? c12.getProperty(b.billing_no) : "";
					b.reason += " " + expString;
					if ("E".equals(b.status)) {
						b.adjustmentCode = b.expString;
					} else {
						b.adjustmentCode = "";
					}
				}

				// AR SECTION ---------------------------------------------------------
				/**
				 * If the report is of type AR and it was paid with an explanation or is private
				 * we need to get the difference between what was billed and what was paid
				 **/
				if (type.equals(MSPReconcile.REP_ACCOUNT_REC)) {
					double dblAmtOwing = this.getAmountOwing(b.billMasterNo, b.amount, b.billingtype);
					b.amtOwing = String.valueOf(dblAmtOwing);
					skipBill = new Double(b.amtOwing).intValue() == 0;
				}

				if (!skipBill) {
					billSearch.justBillingMaster.add(b.billMasterNo);
					billSearch.list.add(b);
					billSearch.count++;
				} else {
					skipBill = false;
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex1) {
				MiscUtils.getLogger().error("Error", ex1);
			}
		}
		return billSearch;
	}

	/**
	 * Returns the dollar amount owing on a specific bill number
	 * If the specified bill has an explanation code of 'HS'(Already paid) the amount is set to zero
	 * If the bill is not private,"Internal Adjustments" are deducted from the total amount owing.
	 * @param billingMasterNo String - The UID of the bill in question
	 * @param amountBilled String - The total amount of the bill
	 * @return String
	 */
	public double getAmountOwing(String billingMasterNo, String amountBilled, String billingType) {

		amountBilled = (amountBilled != null && !amountBilled.equals("")) ? amountBilled : "0.0";
		double dbltBilled = new Double(amountBilled).doubleValue();
		//Gets the total 'paid' or adjusted for any type of bill from billinghistory
		double totalPaidFromHistory = getTotalPaidFromHistory(billingMasterNo, false);
		double totalPaidFromS00 = 0.0;
		if (!MSPReconcile.BILLTYPE_PRI.equalsIgnoreCase(billingType)) {
			//bills of type msp,icbc,wcb
			TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);
			for (TeleplanS00 s : dao.findByOfficeNumber(forwardZero(billingMasterNo, 7))) {
				if ("HS".equals(s.getExp1())) {
					totalPaidFromS00 = Double.parseDouble(amountBilled);
					log.debug("Bill has HS setting the totalPaid to amountBilled  " + amountBilled);
					break;
				}
				String paidAmount = s.getPaidAmount();
				paidAmount = MSPReconcile.convCurValue(paidAmount);
				Double dblAmtPaid = new Double(paidAmount);
				totalPaidFromS00 += dblAmtPaid.doubleValue();
				log.debug("paidAmount " + paidAmount);
			}
		}
		log.debug("amtPaid = totalPaidFromHistory + totalPaidFromS00; " + totalPaidFromHistory + "+" + totalPaidFromS00);
		double amtPaid = totalPaidFromHistory + totalPaidFromS00;
		log.debug("amtPaid " + amtPaid);
		double dblAmountOwing = amtPaid < 0 ? dbltBilled + amtPaid : dbltBilled - amtPaid;
		dblAmountOwing = UtilMisc.toCurrencyDouble(dblAmountOwing);
		return dblAmountOwing;
	}

	/**
	 * String qry = "SELECT teleplanS00.t_exp1 FROM teleplanS00, billingmaster " + 
	 * "where t_officeno = billingmaster_no " + "and billingmaster_no  = " + billNo;
	 */
	public String getAdjustmentCodeByBillNo(String billNo) {		
		TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);
		for(TeleplanS00 s : dao.findByOfficeNumber(billNo)) {
			return s.getExp1();
		}
		return "";
	}

	/**
	 * getC12Description
	 *
	 * @param code String
	 * @return String
	 */
	private String getC12Description(String code) {
		TeleplanRefusalCodeDao dao = SpringUtils.getBean(TeleplanRefusalCodeDao.class);
		List<TeleplanRefusalCode> codes = dao.findByCode(code);
		if (codes.isEmpty()) {
			return "";
		}
		return codes.get(0).getDescription();			
	}

	/**
	 * Retrieves a list of all bills that were Paid by MSP
	 * 
	 * @param account
	 * @param payeeNo
	 * @param providerNo
	 * @param startDate
	 * @param endDate
	 * @param excludeWCB
	 * @param excludeMSP
	 * @param excludePrivate
	 * @param exludeICBC
	 * @return BillSearch
	 */
	public MSPReconcile.BillSearch getPayments(String account, String payeeNo, String providerNo, String startDate, String endDate, boolean excludeWCB, boolean excludeMSP, boolean excludePrivate, boolean exludeICBC) {
		BillSearch billSearch = new BillSearch();
		String criteriaQry = createCriteriaString(account, payeeNo, providerNo, UtilMisc.replace(startDate, "-", ""), UtilMisc.replace(endDate, "-", ""), excludeWCB, excludeMSP, excludePrivate, exludeICBC, MSPReconcile.REP_PAYREF, "");
		String p = "SELECT teleplanS00.t_payment,b.billingtype,b.demographic_name,apptProvider_no,provider_no,payee_no,b.demographic_no,teleplanS00.t_paidamt,t_exp1,t_exp2,t_dataseq,bm.service_date,bm.paymentMethod,teleplanS00.t_ajc1," + " teleplanS00.t_aja1,teleplanS00.t_aja2,teleplanS00.t_aja3,teleplanS00.t_aja4,teleplanS00.t_aja5,teleplanS00.t_aja6,teleplanS00.t_aja7,bm.billingmaster_no,teleplanS00.t_practitionerno"
		        + " FROM teleplanS00 left join billingmaster as bm on teleplanS00.t_officeno = bm.billingmaster_no,billing as b" + " where b.billing_no = bm.billing_no" + criteriaQry + " and bm.billingstatus != 'D'" + " order by t_payment";

		billSearch.list = new ArrayList<Object>();
		billSearch.count = 0;
		billSearch.justBillingMaster = new ArrayList<String>();

		ResultSet rs = null;
		try {

			rs = DBHandler.GetSQL(p);
			while (rs.next()) {
				MSPBill b = new MSPBill();
				b.billingtype = rs.getString("b.billingtype");
				b.paymentDate = rs.getString("t_payment");
				b.paymentMethod = rs.getString("paymentMethod");
				b.setPaymentMethodName(this.getPaymentMethodDesc(b.paymentMethod));
				b.demoNo = rs.getString("demographic_no");
				b.demoName = rs.getString("demographic_name");

				if (!MSPReconcile.BILLTYPE_PRI.equalsIgnoreCase(b.billingtype)) {
					b.amount = MSPReconcile.convCurValue(rs.getString("t_paidamt"));
				} else {
					b.amount = String.valueOf(getAmountPaid(rs.getString("bm.billingmaster_no"), MSPReconcile.BILLTYPE_PRI));
					if (!StringUtils.isNumeric(b.amount)) {
						throw new RuntimeException("Amount not a number");
						//     b.amount = this.convCurValue(rs.getString("t_paidamt"));
					}
				}
				b.status = b.reason;
				b.serviceDate = rs.getString("service_date");
				b.seqNum = rs.getString("t_dataseq");
				b.exp1 = rs.getString("t_exp1");
				b.exp2 = rs.getString("t_exp2");
				b.apptDoctorNo = rs.getString("t_practitionerno");
				b.userno = rs.getString("provider_no");
				b.payeeNo = rs.getString("payee_no");
				b.adjustmentCode = rs.getString("teleplanS00.t_ajc1");

				//should be empty string if there is no adjustment
				b.adjustmentCodeAmt = "";
				b.adjustmentCode = b.adjustmentCode == null ? "" : b.adjustmentCode;
				b.adjustmentCodeDesc = "";
				if (!"".equals(b.adjustmentCode)) {
					String adjCode1amt1Str = convCurValue(rs.getString("teleplanS00.t_aja1"));
					String adjCode1amt2Str = convCurValue(rs.getString("teleplanS00.t_aja2"));
					String adjCode1amt3Str = convCurValue(rs.getString("teleplanS00.t_aja3"));
					String adjCode1amt4Str = convCurValue(rs.getString("teleplanS00.t_aja4"));
					String adjCode1amt5Str = convCurValue(rs.getString("teleplanS00.t_aja5"));
					String adjCode1amt6Str = convCurValue(rs.getString("teleplanS00.t_aja6"));
					String adjCode1amt7Str = convCurValue(rs.getString("teleplanS00.t_aja7"));

					double adjCode1amt1 = Double.parseDouble(adjCode1amt1Str);
					double adjCode1amt2 = Double.parseDouble(adjCode1amt2Str);
					double adjCode1amt3 = Double.parseDouble(adjCode1amt3Str);
					double adjCode1amt4 = Double.parseDouble(adjCode1amt4Str);
					double adjCode1amt5 = Double.parseDouble(adjCode1amt5Str);
					double adjCode1amt6 = Double.parseDouble(adjCode1amt6Str);
					double adjCode1amt7 = Double.parseDouble(adjCode1amt7Str);

					String adjCodeAmt = String.valueOf(adjCode1amt1 + adjCode1amt2 + adjCode1amt3 + adjCode1amt4 + adjCode1amt5 + adjCode1amt6 + adjCode1amt7);
					b.adjustmentCodeAmt = adjCodeAmt;
					b.adjustmentCodeDesc = getAdjustmentCodeDesc(b.adjustmentCode) + "(" + b.adjustmentCode + ")";
					if (b.adjustmentCodeDesc.equals("")) {
						MiscUtils.getLogger().debug("no description for b.adjustmentCode:" + b.adjustmentCode);
					}
				}

				b.accountName = this.getProvider(b.userno, 0).getFullName();
				b.acctInit = this.getProvider(b.userno, 0).getInitials();
				b.payeeName = this.getProvider(b.payeeNo, 1).getInitials();
				b.provName = this.getProvider(b.apptDoctorNo, 1).getInitials();

				double dblAmount = new Double(b.amount).doubleValue();
				/**
				 * @todo Get rid of hard coded strings
				 */
				b.type = dblAmount > 0 ? "PMT" : "RFD";
				/**
				 * Ignore bill If the amount is 0
				 */
				if ((int)dblAmount != 0) {
					billSearch.justBillingMaster.add(b.billMasterNo);
					billSearch.list.add(b);
					billSearch.count++;
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				MiscUtils.getLogger().error("Error", ex);
			}

		}

		//Now we need to get the Private Payments
		if (!excludePrivate) {
			MSPReconcile.BillSearch privatePayments = getPrivatePayments(account, payeeNo, providerNo, startDate, endDate, true);
			List<Object> privatePaymentsList = privatePayments.list;
			if (privatePaymentsList != null && !privatePaymentsList.isEmpty()) {
				billSearch.list.addAll(privatePaymentsList);
			}
		}
		return billSearch;
	}

	/**
	 * Retrieves a list of all bills that were Paid by Privately
	 * 
	 * @param account
	 * @param payeeNo
	 * @param providerNo
	 * @param startDate
	 * @param endDate
	 * @param excludePrivate
	 * @return BillSearch
	 */
	public MSPReconcile.BillSearch getPrivatePayments(String account, String payeeNo, String providerNo, String startDate, String endDate) {

		startDate = UtilMisc.replace(startDate, "-", "");

		endDate = UtilMisc.replace(endDate, "-", "");
		BillSearch billSearch = new BillSearch();
		String criteriaQry = createCriteriaString(account, payeeNo, providerNo, startDate, endDate, true, true, false, true, "", "creation_date");
		String p = "SELECT b.billingtype,bm.billingmaster_no,b.demographic_no,b.demographic_name,bm.service_date,b.apptProvider_no ,b.provider_no,bm.payee_no," + " bh.creation_date,bh.amount_received,payment_type_id" + " FROM billing_history bh left join billingmaster bm on bh.billingmaster_no = bm.billingmaster_no ,billing b" + " where bm.billing_no = b.billing_no " + " and bh.payment_type_id != " + MSPReconcile.PAYTYPE_IA + " " + criteriaQry + " and bm.billingstatus != '" + MSPReconcile.DELETED + "'";
		MiscUtils.getLogger().debug(p);
		billSearch.list = new ArrayList<Object>();

		ResultSet rs = null;
		try {

			rs = DBHandler.GetSQL(p);
			while (rs.next()) {
				MSPBill b = new MSPBill();
				b.billMasterNo = rs.getString("bm.billingmaster_no");
				b.billingtype = rs.getString("b.billingtype");
				b.demoNo = rs.getString("demographic_no");
				b.demoName = rs.getString("demographic_name");
				b.status = b.reason;
				b.serviceDate = rs.getString("service_date");
				b.apptDoctorNo = rs.getString("apptProvider_no");
				b.userno = rs.getString("provider_no");
				b.payeeNo = rs.getString("payee_no");

				Provider actProv = this.getProvider(b.userno, 0);
				b.accountName = actProv.getFullName();
				b.acctInit = actProv.getInitials();

				b.payeeName = this.getProvider(b.payeeNo, 1).getInitials();
				b.provName = this.getProvider(b.apptDoctorNo, 0).getInitials();

				b.amount = rs.getString("amount_received");
				b.paymentDate = this.fmt.format(rs.getDate("creation_date"));
				b.paymentMethod = rs.getString("payment_type_id");
				b.setPaymentMethodName(this.getPaymentMethodDesc(b.paymentMethod));
				double dblAmount = UtilMisc.safeParseDouble(b.amount);
				b.type = dblAmount > 0 ? "PMT" : "RFD";
				if (dblAmount != 0) {
					billSearch.list.add(b);
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				MiscUtils.getLogger().error("Error", ex);
			}

		}
		return billSearch;
	}
	
	/**
	 * @Deprecated use method signature getPrivatePayments(String account, String payeeNo, String providerNo, String startDate, String endDate)
	 */
	@Deprecated
	public MSPReconcile.BillSearch getPrivatePayments(String account, String payeeNo, String providerNo, 
			String startDate, String endDate, @SuppressWarnings("unused") boolean excludePrivate) {

		startDate = UtilMisc.replace(startDate, "-", "");

		endDate = UtilMisc.replace(endDate, "-", "");
		BillSearch billSearch = new BillSearch();
		String criteriaQry = createCriteriaString(account, payeeNo, providerNo, startDate, endDate, true, true, false, true, "", "creation_date");
		String p = "SELECT b.billingtype,bm.billingmaster_no,b.demographic_no,b.demographic_name,bm.service_date,b.apptProvider_no ,b.provider_no,bm.payee_no," + " bh.creation_date,bh.amount_received,payment_type_id" + " FROM billing_history bh left join billingmaster bm on bh.billingmaster_no = bm.billingmaster_no ,billing b" + " where bm.billing_no = b.billing_no " + " and bh.payment_type_id != " + MSPReconcile.PAYTYPE_IA + " " + criteriaQry + " and bm.billingstatus != '" + MSPReconcile.DELETED + "'";
		MiscUtils.getLogger().debug(p);
		billSearch.list = new ArrayList<Object>();

		ResultSet rs = null;
		try {

			rs = DBHandler.GetSQL(p);
			while (rs.next()) {
				MSPBill b = new MSPBill();
				b.billMasterNo = rs.getString("bm.billingmaster_no");
				b.billingtype = rs.getString("b.billingtype");
				b.demoNo = rs.getString("demographic_no");
				b.demoName = rs.getString("demographic_name");
				b.status = b.reason;
				b.serviceDate = rs.getString("service_date");
				b.apptDoctorNo = rs.getString("apptProvider_no");
				b.userno = rs.getString("provider_no");
				b.payeeNo = rs.getString("payee_no");

				Provider actProv = this.getProvider(b.userno, 0);
				b.accountName = actProv.getFullName();
				b.acctInit = actProv.getInitials();

				b.payeeName = this.getProvider(b.payeeNo, 1).getInitials();
				b.provName = this.getProvider(b.apptDoctorNo, 0).getInitials();

				b.amount = rs.getString("amount_received");
				b.paymentDate = this.fmt.format(rs.getDate("creation_date"));
				b.paymentMethod = rs.getString("payment_type_id");
				b.setPaymentMethodName(this.getPaymentMethodDesc(b.paymentMethod));
				double dblAmount = UtilMisc.safeParseDouble(b.amount);
				b.type = dblAmount > 0 ? "PMT" : "RFD";
				if ((int)dblAmount != 0) {
					billSearch.list.add(b);
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				MiscUtils.getLogger().error("Error", ex);
			}

		}
		return billSearch;
	}

	/**
	 * Returns a string description of a billing payment method
	 * @todo This should actually be a cached lookup map to improve performance
	 * @param string String
	 * @return String
	 */
	private String getPaymentMethodDesc(String id) {
		BillingPaymentTypeDao dao = SpringUtils.getBean(BillingPaymentTypeDao.class);
		BillingPaymentType bpt = dao.find(ConversionUtils.fromIntString(id));
		if (bpt != null) {
			return bpt.getPaymentType();
		}
		return "";
	}

	/**
	 * Creates an SQL fragment that is used as the criteria(WHERE Clause) in the retrieval
	 * of MSP Bills
	 * @param account String
	 * @param payeeNo String
	 * @param providerNo String
	 * @param startDate String
	 * @param endDate String
	 * @param excludeWCB boolean
	 * @param excludeMSP boolean
	 * @param excludePrivate boolean
	 * @param exludeICBC boolean
	 * @param status String
	 * @return String
	 */
	private String createCriteriaString(String account, String payeeNo, String providerNo, String startDate, String endDate, boolean excludeWCB, boolean excludeMSP, boolean excludePrivate, boolean exludeICBC, String repType, String dateFieldOption) {
		String criteriaQry = "";
		String dateField = MSPReconcile.REP_PAYREF.equals(repType) ? "t_payment" : "service_date";

		//This class in need of significant refactoring,(especially this gawd-aweful section which was extracted from the getBills method purely to avoid code duplication

		if ("creation_date".equals(dateFieldOption)) {
			dateField = "creation_date";
		}
		if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
			if (MSPReconcile.REP_PAYREF.equals(repType)) {
				String[] row = SqlUtils.getRow("select ohip_no from provider where provider_no = " + providerNo);
				if (row != null && row.length > 0) {
					String ohip_no = row[0];
					criteriaQry += " and t_practitionerno = '" + ohip_no + "'";
				} else {
					throw new RuntimeException("Provider must have ohip no!");
				}
			} else {
				criteriaQry += " and b.apptProvider_no = '" + providerNo + "'";
			}
		}

		if (payeeNo != null && !payeeNo.trim().equalsIgnoreCase("all")) {
			criteriaQry += " and bm.payee_no = '" + payeeNo + "'";
		}
		if (account != null && !account.trim().equalsIgnoreCase("all")) {
			criteriaQry += " and b.provider_no = '" + account + "'";
		}
		if (startDate != null && !startDate.trim().equalsIgnoreCase("")) {
			criteriaQry += " and ( to_days(" + dateField + ") >= to_days('" + startDate + "')) ";
		}

		if (endDate != null && !endDate.trim().equalsIgnoreCase("")) {
			criteriaQry += " and ( to_days(" + dateField + ") <= to_days('" + endDate + "')) ";
		}
		//put this crap in a Map and use an 'in' clause instead
		if (excludeWCB) {
			criteriaQry += " and b.billingType != 'WCB' ";
		}

		if (excludeMSP) {
			criteriaQry += " and b.billingType != 'MSP' ";
		}

		if (excludePrivate) {
			criteriaQry += " and b.billingType != 'Pri' ";
		}

		if (exludeICBC) {
			criteriaQry += " and b.billingType != 'ICBC' ";
		}

		if (repType.equals(MSPReconcile.REP_REJ)) {
			criteriaQry += " and bm.billingstatus = '" + MSPReconcile.REJECTED + "'";
		} else if (repType.equals(MSPReconcile.REP_INVOICE)) {
			criteriaQry += " and bm.billingstatus != '" + MSPReconcile.DELETED + "'";
		} else if (repType.equals(MSPReconcile.REP_ACCOUNT_REC)) {
			criteriaQry += " and bm.billingstatus not in('" + MSPReconcile.DELETED + "','" + MSPReconcile.BADDEBT + "')";
		}

		else if (repType.equals(MSPReconcile.REP_WO)) {
			criteriaQry += " and bm.billingstatus = '" + MSPReconcile.BADDEBT + "'";
		}
		return criteriaQry;
	}

	/**
	 * Returns the count of distinct values for the specified Bill field
	 * Really just a convenience method for selecting distinct values without hitting the database multiple times
	 * @todo This method should be generalized to count the fields of a collection of arbitrary beans
	 * @param bills List
	 * @param fieldName String
	 * @return int
	 */
	public int getDistinctFieldCount(List<Object> bills, String fieldName) {
		ArrayList<String> fieldValueList = new ArrayList<String>(); //a lookup list containing all values that have been counted
		int colSize = bills.size();
		for (int i = 0; i < colSize; i++) {
			MSPBill bill = (MSPBill) bills.get(i);
			String propValue = beanut.getPropertyValue(bill, fieldName);
			//disgregard previously counted field value
			if (!fieldValueList.contains(propValue)) {
				fieldValueList.add(propValue);
			}
		}
		return fieldValueList.size();
	}

	/**
	 * Returns the total paid for a specific set of bill types
	 * @param bills List
	 * @param status String
	 * @return Double
	 */
	public Double getTotalPaidByStatus(List<Object> bills, String status) {
		int colSize = bills.size();
		double amt = 0.0;
		for (int i = 0; i < colSize; i++) {
			MSPBill bill = (MSPBill) bills.get(i);
			String beanStatus = beanut.getPropertyValue(bill, "status");

			if (beanStatus.equals(status)) {
				amt += new Double(bill.getAmount()).doubleValue();
			}
		}
		return new Double(amt);
	}

	/**
	 * Returns the count of distinct values for the specified Bill field
	 * Really just a convenience method for selecting distinct values without hitting the database multiple times
	 * @todo This method should be generalized to count the fields of a collection of arbitrary beans
	 * @param bills List
	 * @param status String
	 * @return int
	 */
	public Integer getCountByStatus(List<MSPBill> bills, String status) {
		int colSize = bills.size();
		int cnt = 0;
		for (int i = 0; i < colSize; i++) {
			MSPBill bill = bills.get(i);
			String beanStatus = beanut.getPropertyValue(bill, "status");
			if (beanStatus.equals(status)) {
				cnt++;
			}
		}
		return new Integer(cnt);
	}

	/**
	 * Returns a String value representing an SQL query used in the rertrieval of MSP remittance data.
	 *
	 * @param payeeNo String
	 * @return ResultSet
	 */
	public ResultSet getMSPRemittanceQuery(String payeeNo, String s21Id) {
		MiscUtils.getLogger().debug(new java.util.Date() + ":MSPReconcile.getMSPRemittanceQuery(payeeNo, s21Id)");
		String qry = "SELECT billing_code,provider.first_name,provider.last_name,t_practitionerno,t_s00type,billingmaster.service_date as 't_servicedate',t_payment," + "t_datacenter,billing.demographic_name,billing.demographic_no,teleplanS00.t_paidamt,t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_dataseq " + " from teleplanS00,billing,billingmaster,provider " + " where teleplanS00.t_officeno = billingmaster.billingmaster_no " + " and teleplanS00.s21_id = " + s21Id
		        + " and billingmaster.billing_no = billing.billing_no " + " and provider.ohip_no= teleplanS00.t_practitionerno " + " and teleplanS00.t_payeeno = " + payeeNo + " order by provider.first_name,t_servicedate,billing.demographic_name";

		ResultSet rs = null;
		try {

			rs = DBHandler.GetSQL(qry);
		} catch (SQLException ex) {
			MiscUtils.getLogger().error("Error", ex);
		}
		return rs;
	}

	/**
	 * Returns a Provider instance according to the supplied provider number
	 * @todo This method belongs in a ProviderDAO type class
	 * @param providerNo String - The UID of the provider in question
	 * @param criteria int - If criteria == 1, retrieve Provider by ohip_no else by provider_no
	 * @return Provider
	 */
	public oscar.entities.Provider getProvider(String providerNo, int criteria) {
		oscar.entities.Provider prov = new oscar.entities.Provider();
		
		// provider numbers are varchars (strings) 
		if( providerNo == null || providerNo.isEmpty() ) {
			prov.setFirstName("");
			prov.setLastName("");
			return prov;
		}

		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		org.oscarehr.common.model.Provider provider = null;
		boolean isSearchingProvidersByOhipNumber = criteria == 1;
		
		if (isSearchingProvidersByOhipNumber) {			
			List<org.oscarehr.common.model.Provider> providersByOhip = dao.getBillableProvidersByOHIPNo( providerNo );
			
			if( providersByOhip != null ) {
				provider = providersByOhip.get(0); // get the first entry only. 
			}
			
		} else {
			provider = dao.getProvider(providerNo);
		}
		
		if (provider != null) {			
			prov.setFirstName(provider.getFirstName());
			prov.setLastName(provider.getLastName());
		}

		return prov;
	}

	/**
	 * Returns an ArrayList of all Provider instances with a provider_type == 'doctor'
	 * @todo This belongs in a ProviderDAO class
	 * @return ArrayList
	 */
	public List<oscar.entities.Provider> getAllProviders() {
		ArrayList<oscar.entities.Provider> list = new ArrayList<oscar.entities.Provider>();		
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		for(org.oscarehr.common.model.Provider p : dao.getProvidersByType("doctor")) {
			oscar.entities.Provider prov = new oscar.entities.Provider();
			prov.setFirstName(p.getFirstName());
			prov.setLastName(p.getLastName());
			prov.setProviderNo(p.getProviderNo());
			list.add(prov);
		}

		return list;
	}

	/**
	 * Returns a String description of the specified adjustment code
	 * @param code String
	 * @return String
	 */
	public String getAdjustmentCodeDesc(String code) {
		TeleplanAdjCodesDao dao = SpringUtils.getBean(TeleplanAdjCodesDao.class);
		for(TeleplanAdjCodes c : dao.findByCode(code)) {
			return c.getAdjDesc();
		}
		return "";
	}

	public oscar.entities.S21 getS21Record(String s21id) {
		TeleplanS21Dao dao = SpringUtils.getBean(TeleplanS21Dao.class);
		TeleplanS21 t = dao.find(ConversionUtils.fromIntString(s21id));
		if (t.getStatus() == 'D') {
			t = null;
		}

		oscar.entities.S21 s21 = new oscar.entities.S21();
		if (t != null) {
			s21.setPaymentDate(t.getPayment());
			s21.setPayeeNo(t.getPayeeNo());
			s21.setPayeeName(t.getPayeeName());
			s21.setAmtBilled(MSPReconcile.convCurValue(t.getAmountBilled()));
			s21.setAmtPaid(MSPReconcile.convCurValue(t.getAmountPaid()));
			s21.setCheque(MSPReconcile.convCurValue(t.getCheque()));
		}

		return s21;
	}

	/**
	 * Returns a properly formed negative numeric value
	 * If the supplied value doesn't represent a negative number it is simply returned
	 * @param value String
	 * @return String
	 * @todo complete documentation
	 *oscar.oscarBilling.ca.bc.MSP.MSPReconcile.convCurValue(
	 */
	public static String convCurValue(String value) {
		BigDecimal curValue = new BigDecimal("0");
		if (value == null || value.equals("")) {
			return "0.0";
		}
		try {
			boolean isNeg = false;
			String ret = value;
			String lastDigit = ret.substring(ret.length() - 1, ret.length());
			String preDigits = ret.substring(0, ret.length() - 1);
			//If string isn't negative(negative values contain alphabetic last char)
			if (negValues.containsKey(lastDigit)) {
				lastDigit = negValues.getProperty(lastDigit);
				isNeg = true;
				ret = preDigits + lastDigit;
			}
			int dblValue = new Double(ret).intValue();
			if (isNeg) {
				dblValue = dblValue * -1;
			}

			double newDouble = dblValue * .01;
			curValue = BigDecimal.valueOf(newDouble).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			return curValue.toString();
		}
		return curValue.toString();
	}

	/**
	 * Returns true if the specified demographic has any private bill where the amount paid is less than the bill amount
	 * @param demographicNo String - The demographic number
	 * @return boolean - Tru
	 */
	public boolean patientHasOutstandingPrivateBill(String demographicNo) {
		BillingDao dao = SpringUtils.getBean(BillingDao.class);
		boolean ret = false;
		for(Object[] o : dao.findOutstandingBills(ConversionUtils.fromIntString(demographicNo), 
				MSPReconcile.BILLTYPE_PRI, Arrays.asList(new String[] {"S","D","A"}))) {
			Billingmaster bm = (Billingmaster) o[0];
			String billingmaster_no = "" + bm.getBillingmasterNo();
			double amount = Double.parseDouble(bm.getBillAmount());
			return isPrivateBillItemOutstanding(billingmaster_no, amount);
		}
		
		return ret;
	}

	/**
	 * Returns true if the specified bill item(billingmaster record) has an amount owing;
	 * @param billingmaster_no String
	 * @return boolean
	 */
	public boolean isPrivateBillItemOutstanding(String billingmaster_no, double amount) {
		double amountPaid = new Double(getAmountPaid(billingmaster_no, BILLTYPE_PRI)).doubleValue();
		return amountPaid < amount;
	}

	/**
	 * Sets the billmaster record status to SETTLED if an amount isn't owing and the bill.
	 * NOTE: Private bills are set to PAIDPRIVATE
	 *
	 * @param billingmasterNo String
	 */
	public void settleIfBalanced(String billingmasterNo) {
		BillingDao dao = SpringUtils.getBean(BillingDao.class);
		for(Object[] o : dao.findByBillingMasterNo(ConversionUtils.fromIntString(billingmasterNo))) {
			Billingmaster b = (Billingmaster) o[0];
			Billing b1 = (Billing) o[1];
			
			double dblAmtOwing = this.getAmountOwing(billingmasterNo, b.getBillAmount(), b1.getBillingtype());
			String strOwing = String.valueOf(dblAmtOwing);
			double amountOwing = Double.parseDouble(strOwing);
			if (amountOwing <= 0) {
				if (MSPReconcile.BILLTYPE_PRI.equals(b.getBillAmount())) { // Warning: This was probably a bug, as row[0] referred to bill amount...
					this.updateBillingMasterStatus(billingmasterNo, MSPReconcile.PAIDPRIVATE);
				} else {
					this.updateBillingMasterStatus(billingmasterNo, MSPReconcile.SETTLED);
				}
			} else {
				log.debug("amount owing is less than or equal to 0");
			}
		}
	}

	/**
	 * Settles all bills paid by MSP that have an explanatory code of 'BG'
	 * Bills of this type were paid an amount that was different from the original bill amount.
	 * Therefore, an 'Internal Adjustment' is applied to the difference in order to ensure that the
	 * amount owing is $0.00.
	 */
	public void settleBGBills() {
		BillingHistoryDAO dao = new BillingHistoryDAO();
		//get all bills with explanation of type 'BG'
		TeleplanS00Dao sDao = SpringUtils.getBean(TeleplanS00Dao.class);
		//for each bill, get amount owing
		for (TeleplanS00 s : sDao.findBgs()) {
			int billingmaster_no = UtilMisc.safeParseInt(s.getOfficeNo());
			//if billingmaster_no = 0 indicates corrupt record id
			if (billingmaster_no != 0) {
				String strBmNo = String.valueOf(billingmaster_no);
				double parseAmt = UtilMisc.safeParseDouble(s.getBillAmount()) * .01;//ensure correct decimal position
				double amountOwing = this.getAmountOwing(strBmNo, String.valueOf(parseAmt), "");
				//if the the amount owing is not zero
				if ((int)amountOwing != 0) {
					//then apply an adjustment that is equal to the difference
					dao.createBillingHistoryArchive(strBmNo, amountOwing, MSPReconcile.PAYTYPE_IA);
					settleIfBalanced(strBmNo);
				}
			}
		}
	}
}
