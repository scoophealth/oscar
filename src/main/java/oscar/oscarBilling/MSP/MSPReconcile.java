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
package oscar.oscarBilling.MSP;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.oscarehr.billing.CA.BC.dao.TeleplanC12Dao;
import org.oscarehr.billing.CA.BC.dao.TeleplanS00Dao;
import org.oscarehr.billing.CA.BC.model.TeleplanC12;
import org.oscarehr.billing.CA.BC.model.TeleplanS00;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.util.ConversionUtils;

public class MSPReconcile {

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

	private BillingmasterDAO billingmasterDao = SpringUtils.getBean(BillingmasterDAO.class);

	public Properties currentC12Records() {
		Properties p = new Properties();
		TeleplanC12Dao dao = SpringUtils.getBean(TeleplanC12Dao.class);

		for (TeleplanC12 t : dao.findCurrent()) {
			int i = Integer.parseInt(t.getOfficeFolioClaimNo()); // this kludge rids leading zeros
			String exp[] = new String[7];
			exp[0] = t.getExp1();
			exp[1] = t.getExp2();
			exp[2] = t.getExp3();
			exp[3] = t.getExp4();
			exp[4] = t.getExp5();
			exp[5] = t.getExp6();
			exp[6] = t.getExp7();
			String def = createCorrectionsString(exp);
			String s = Integer.toString(i);
			p.put(s, def);
		}
		return p;
	}

	//
	public String getS00String(String billingNo) {
		String s = "";
		int i = 0;

		TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);
		for (TeleplanS00 t : dao.findByBillingNo(forwardZero(billingNo, 7))) {
			String exp[] = new String[7];
			exp[0] = t.getExp1();
			exp[1] = t.getExp2();
			exp[2] = t.getExp3();
			exp[3] = t.getExp4();
			exp[4] = t.getExp5();
			exp[5] = t.getExp6();
			exp[6] = t.getExp7();
			s = createCorrectionsString(exp);
			i++;
		}

		if (i > 1) {
			MiscUtils.getLogger().debug(" billingNo " + billingNo + " had " + i + "rows in the table");
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

	public class BillSearch {
		Properties p;
		public ArrayList<Bill> list;
		int count = 0;
		ArrayList<String> justBillingMaster;

		public Properties getCurrentErrorMessages() {
			Properties errorsProps = new Properties();
			if (count <= 0) {
				return errorsProps;
			}

			List<String> billingMasterIds = new ArrayList<String>();
			for (int i = 0; i < justBillingMaster.size(); i++) {
				billingMasterIds.add(forwardZero(justBillingMaster.get(i), 7));
			}

			TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);
			for (TeleplanS00 t : dao.findByOfficeNumbers(billingMasterIds)) {
				int i = Integer.parseInt(t.getOfficeNo()); // this kludge rids leading zeros
				String exp[] = new String[7];
				exp[0] = t.getExp1();
				exp[1] = t.getExp2();
				exp[2] = t.getExp3();
				exp[3] = t.getExp4();
				exp[4] = t.getExp5();
				exp[5] = t.getExp6();
				exp[6] = t.getExp7();
				String def = createCorrectionsString(exp);
				String s = Integer.toString(i);
				errorsProps.put(s, def);

			}

			return errorsProps;
		}
	}

	public ArrayList<String> getSequenceNumbers(String billingNo) {
		ArrayList<String> retval = new ArrayList<String>();

		TeleplanC12Dao dao = SpringUtils.getBean(TeleplanC12Dao.class);
		TeleplanS00Dao sDao = SpringUtils.getBean(TeleplanS00Dao.class);

		for (TeleplanC12 t : dao.findByOfficeClaimNo(forwardZero(billingNo, 7))) {
			retval.add(t.getDataSeq());
		}

		for (TeleplanS00 s : sDao.findByOfficeNumbers(Arrays.asList(new String[] { forwardZero(billingNo, 7) }))) {
			retval.add(s.getDataSeq());
		}
		return retval;
	}

	public BillSearch getBills(String statusType, String providerNo, String startDate, String endDate) {

		BillSearch billSearch = new BillSearch();

		BillingmasterDAO dao = SpringUtils.getBean(BillingmasterDAO.class);

		//String
		billSearch.list = new ArrayList<Bill>();
		billSearch.count = 0;
		billSearch.justBillingMaster = new ArrayList<String>();

		for (Object[] o : dao.getBillingMasterByVariousFields(statusType, providerNo, startDate, endDate)) {
			Bill b = new Bill();

			b.billing_no = getString(o[0]);
			b.apptDoctorNo = getString(o[5]);
			b.apptNo = getString(o[6]);
			b.demoNo = getString(o[1]);
			b.demoName = getString(o[2]);
			b.userno = getString(o[15]);
			b.apptDate = getString(o[7]);
			b.apptTime = getString(o[8]);
			b.reason = getString(o[9]);
			b.billMasterNo = getString(o[18]);
			b.amount = getString(o[10]);
			b.code = getString(o[11]);
			b.dx1 = getString(o[12]);
			b.dx2 = getString(o[13]);
			b.dx3 = getString(o[14]);

			billSearch.justBillingMaster.add(b.billMasterNo);
			billSearch.list.add(b);
			billSearch.count++;
		}

		return billSearch;
	}

	private String getString(Object object) {
		if (object == null) {
			return "";
		}
		return String.valueOf(object);
	}

	public ArrayList<Bill> getBillsMaster(String statusType) {
		BillingDao dao = SpringUtils.getBean(BillingDao.class);

		ArrayList<Bill> list = new ArrayList<Bill>();

		BillingmasterDAO bmDao = SpringUtils.getBean(BillingmasterDAO.class);
		for (Object[] o : bmDao.findByStatus(statusType)) {
			Billing bb = (Billing) o[0];
			Billingmaster bm = (Billingmaster) o[1];
			Bill b = new Bill();
			b.billing_no = "" + bb.getId();
			b.apptDoctorNo = bb.getApptProviderNo();
			b.apptNo = "" + bb.getAppointmentNo();
			b.demoNo = "" + bb.getDemographicNo();
			b.demoName = bb.getDemographicName();
			b.userno = bb.getProviderNo();
			b.apptDate = ConversionUtils.toDateString(bb.getBillingDate());
			b.apptTime = ConversionUtils.toTimeString(bb.getBillingTime());
			b.reason = bb.getStatus();
			b.billMasterNo = "" + bm.getBillingmasterNo();

			b.amount = bm.getBillAmount();
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

		public String code = "";
		public String amount = "";
		public String dx1 = "";
		public String dx2 = "";
		public String dx3 = "";
	}

	public ArrayList<String> getAllC12Records(String billingNo) {
		ArrayList<String> retval = new ArrayList<String>();
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("/home/jay/documents/PEMP/mspEditCodes.properties"));
		} catch (IOException e) {
			//empty
		}

		TeleplanC12Dao dao = SpringUtils.getBean(TeleplanC12Dao.class);
		for (TeleplanC12 t : dao.findByOfficeClaimNo(forwardZero(billingNo, 7))) {
			String exp[] = new String[7];
			String seq = t.getDataSeq();
			exp[0] = t.getExp1();
			exp[1] = t.getExp2();
			exp[2] = t.getExp3();
			exp[3] = t.getExp4();
			exp[4] = t.getExp5();
			exp[5] = t.getExp6();
			exp[6] = t.getExp7();

			for (int i = 0; i < exp.length; i++) {
				if (exp[i].length() != 0) {
					retval.add(seq + "&nbsp;&nbsp;" + exp[i] + "&nbsp;&nbsp;" + p.getProperty(exp[i], ""));
				}
			}
		}
		return retval;
	}

	public ArrayList<String> getAllS00Records(String billingNo) {
		ArrayList<String> retval = new ArrayList<String>();
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("/home/jay/documents/PEMP/mspEditCodes.properties"));
		} catch (IOException e) {
			MiscUtils.getLogger().error("Error", e);
		}

		TeleplanS00Dao dao = SpringUtils.getBean(TeleplanS00Dao.class);
		for (TeleplanS00 t : dao.findByOfficeNumbers(Arrays.asList(new String[] { forwardZero(billingNo, 7) }))) {
			String exp[] = new String[7];
			String seq = t.getDataSeq();
			exp[0] = t.getExp1();
			exp[1] = t.getExp2();
			exp[2] = t.getExp3();
			exp[3] = t.getExp4();
			exp[4] = t.getExp5();
			exp[5] = t.getExp6();
			exp[6] = t.getExp7();
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

	public boolean updateStat(String stat, String billingNo) {
		//get current status of bill
		boolean updated = true;
		String currStat = "";
		String newStat = "";

		BillingmasterDAO dao = SpringUtils.getBean(BillingmasterDAO.class);
		Billingmaster bm = dao.getBillingmaster(billingNo);
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
			Billingmaster b = billingmasterDao.getBillingmaster(billingNo);
			if (b != null) {
				b.setBillingstatus(newStat);
				billingmasterDao.update(b);
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

	class BillRecord {
		String billingmaster_no;
		String billing_no;
		String createdate;
		String billingstatus;
		String demographic_no;
		String appointment_no;
		String claimcode;
		String datacenter;
		String payee_no;
		String practitioner_no;
		String phn;
		String name_verify;
		String dependent_num;
		String billing_unit;
		String clarification_code;
		String anatomical_area;
		String after_hour;
		String new_program;
		String billing_code;
		String bill_amount;
		String payment_mode;
		String service_date;
		String service_to_day;
		String submission_code;
		String extended_submission_code;
		String dx_code1;
		String dx_code2;
		String dx_code3;
		String dx_expansion;
		String service_location;
		String referral_flag1;
		String referral_no1;
		String referral_flag2;
		String referral_no2;
		String time_call;
		String service_start_time;
		String service_end_time;
		String birth_date;
		String office_number;
		String correspondence_code;
		String claim_comment;
		String mva_claim_code;
		String icbc_claim_no;
		String original_claim;
		String facility_no;
		String facility_sub_no;
		String filler_claim;
		String oin_insurer_code;
		String oin_registration_no;
		String oin_birthdate;
		String oin_first_name;
		String oin_second_name;
		String oin_surname;
		String oin_sex_code;
		String oin_address;
		String oin_address2;
		String oin_address3;
		String oin_address4;
		String oin_postalcode;
	}

}
