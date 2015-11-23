/**
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
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
 * This software was written for
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 */

package oscar.oscarBilling.ca.bc.MSP;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.BC.model.Wcb;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.Misc;
import oscar.OscarProperties;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.util.ConversionUtils;

/**
 * @author Jef King
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class WcbSb {
	private static Logger logger = MiscUtils.getLogger();

	protected static String file = null;
	
	private String demographic_no;
	private String provider_no;
	private String formCreated;
	private String formEdited;
	private String bill_amount;
	private String visit_type;
	private String billing_no;
	private String invoice_no;
	private String w_reporttype;
	private String w_fname;
	private String w_lname;
	private String w_mname;
	private String w_gender;
	private String w_dob;
	private String w_doi;
	private String w_address;
	private String w_city;
	private String w_postalcode;
	private String w_area;
	private String w_phone;
	private String w_phn;
	private String w_empname;
	private String w_emparea;
	private String w_empphone;
	private String w_wcbno;
	private String w_opaddress;
	private String w_opcity;
	private String w_rphysician;
	private String w_duration;
	private String w_ftreatment;
	private String w_problem;
	private String w_servicedate;
	private String w_diagnosis;
	private String w_icd9;
	private String w_bp;
	private String w_side;
	private String w_noi;
	private String w_work;
	private String w_workdate;
	private String w_clinicinfo;
	private String w_capability;
	private String w_capreason;
	private String w_estimate;
	private String w_rehab;
	private String w_rehabtype;
	private String w_estimatedate;
	private String w_tofollow;
	private String w_payeeno;
	private String w_pracno;
	private String w_wcbadvisor;
	private String w_feeitem;
	private String w_extrafeeitem;
	private String w_servicelocation;
	private String billamountforfeeitem1;
	private String billamountforfeeitem2;
	private String formNeeded;

	String newLine = "\r\n";

	public WcbSb(String billingNo) {
		BillingServiceDao bsDao = SpringUtils.getBean(BillingServiceDao.class);

		for(Object[] o : bsDao.findSomethingByBillingId(ConversionUtils.fromIntString(billingNo))) {
			fillWithRs((Wcb) o[1]);
			return;
		}
	}

	private void fillWithRs(Wcb w) {
		this.bill_amount = w.getBillAmount();
		this.visit_type = w.getServiceLocation();
		this.billing_no = getBillingMasterNo("" + w.getBillingNo());
		this.invoice_no = "" + w.getBillingNo();
		this.demographic_no = "" + w.getDemographicNo();
		this.provider_no = "" + w.getProviderNo();
		this.formCreated = ConversionUtils.toDateString(w.getFormCreated());
		this.formEdited = ConversionUtils.toDateString(w.getFormEdited());
		this.w_reporttype = w.getReportType();
		this.w_fname = w.getfName();
		this.w_lname = w.getlName();
		this.w_mname = w.getmName();
		this.w_gender = w.getGender();
		this.w_dob = ConversionUtils.toDateString(w.getDob());
		this.w_doi = ConversionUtils.toDateString(w.getDoi());
		this.w_address = w.getAddress();
		this.w_city = w.getCity();
		this.w_postalcode = w.getPostal();
		this.w_area = w.getArea();
		this.w_phone = w.getPhone();
		this.w_phn = w.getPhn();
		this.w_empname = w.getEmpName();
		this.w_emparea = w.getEmpArea();
		this.w_empphone = w.getEmpPhone();
		this.w_wcbno = w.getWcbNo();
		this.w_opaddress = w.getOpAddress();
		this.w_opcity = w.getOpCity();
		this.w_rphysician = w.getrPhysician();
		this.w_duration = "" + w.getDuration();
		this.w_ftreatment = this.StripLineBreaks(w.getfTreatment());
		this.w_problem = this.StripLineBreaks(w.getProblem());
		this.w_servicedate = ConversionUtils.toDateString(w.getServiceDate());
		this.w_diagnosis = this.StripLineBreaks(w.getDiagnosis());
		this.w_icd9 = w.getIcd9();
		this.w_bp = w.getBp();
		this.w_side = w.getSide();
		this.w_noi = w.getNoi();
		this.w_work = w.getWork();
		this.w_workdate = ConversionUtils.toDateString(w.getWorkDate());
		this.w_clinicinfo = this.StripLineBreaks(w.getClinicInfo());
		this.w_capability = w.getCapability();
		this.w_capreason = this.StripLineBreaks(w.getCapReason());
		this.w_estimate = w.getEstimate();
		this.w_rehab = w.getRehab();
		this.w_rehabtype = w.getRehabType();
		this.w_estimatedate = ConversionUtils.toDateString(w.getEstimateDate());
		this.w_tofollow = w.getToFollow();
		this.w_payeeno = w.getPayeeNo();
		this.w_pracno = w.getPracNo();
		this.w_wcbadvisor = w.getWcbAdbvisor();
		this.w_feeitem = w.getFeeItem();
		this.w_extrafeeitem = w.getExtraFeeItem();
		this.w_servicelocation = w.getServiceLocation();
		this.billamountforfeeitem1 = w.getFeeItem();
		this.formNeeded = "" + w.getFormNeeded();
	}

	public String validate() {
		StringBuilder m = new StringBuilder();
		if (billamountforfeeitem1 == null || billamountforfeeitem1.equalsIgnoreCase("NULL")) {
			m.append(": INVALID Fee Code ");
		}

		try {
			Integer.parseInt(this.w_icd9);
		} catch (Exception e) {
			m.append(": ICD9 may only contain Numbers ");
		}

		try {
			Integer.parseInt(this.w_wcbno);
		} catch (Exception e) {
			m.append(": WCB claim # may only contain Numbers ");
		}

		if (this.w_reporttype != null && this.w_reporttype.equals("F")) {
			if (this.w_empname != null && this.w_empname.trim().length() == 0) {
				m.append(": Employer's name can not be empty ");
			}

			if (this.w_opaddress != null && this.w_opaddress.trim().length() == 0) {
				m.append(": Employer's Operation Address can not be empty ");
			}

			if (this.w_opcity != null && this.w_opcity.trim().length() == 0) {
				m.append(": Employer's Operation City can not be empty ");
			}

			if (this.w_empphone != null && this.w_empphone.trim().length() == 0) {
				m.append(": Employer's Phone # can not be empty ");
			}
		}

		String ret = "<tr bgcolor='red'><td colspan='11'>" + "<a href='#' onClick=\"openBrWindow('billingTeleplanCorrectionWCB.jsp?billing_no=" + Misc.forwardZero(this.billing_no, 7) + "','','resizable=yes,scrollbars=yes,top=0,left=0,width=900,height=600'); return false;\">" + m.toString() + "</a>" + "</td></tr>";
		if ("".equals(m.toString())) {
			return "";
		}
		return ret;
	}

	public BigDecimal getBillingAmountForFee1BigDecimal() {
		BigDecimal bdFee = null;
		try {
			bdFee = new BigDecimal(billamountforfeeitem1).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			bdFee = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return bdFee;
	}

	public WcbSb(Wcb w) {
		fillWithRs(w);
	}

	private String StripLineBreaks(String input) {
		if (input != null) {
			input = input.replaceAll("\\n", " ").replaceAll("\\r", "");
		}
		return input;
	}

	private static String AppendString(String str) {
		return str;
	}

	public String getBillingMasterNo(String invNo) {
		BillingmasterDAO dao = SpringUtils.getBean(BillingmasterDAO.class);
		Billingmaster bm = dao.getBillingmaster(invNo);
		if (bm != null) {
			return "" + bm.getBillingmasterNo();
		}
		return invNo;
	}

	public String Line1(String dsn) {
		return AppendString(this.Claim1(dsn));
	}

	public String Line2(String dsn) {
		return AppendString(this.Note1(dsn));
	}

	public String Line3(String dsn) {
		return AppendString(this.Claim2(dsn));
	}

	public String Line4(String dsn) {
		return AppendString(this.Note2(dsn));
	}

	public String Line5(String dsn) {
		return AppendString(this.Claim3(dsn));
	}

	public String Line6(String dsn) {
		return AppendString(this.Note3(dsn));
	}

	public String Line7(String dsn) {
		return AppendString(this.Claim4(dsn));
	}

	public String Line8(String dsn) {
		return AppendString(this.Note4(dsn));
	}

	public String Line9(String dsn) {
		return AppendString(this.Claim5(dsn));
	}

	public String ForDatabase() {
		return "";
	}

	public boolean HasSecondFeeItem() {
		return (null != this.w_extrafeeitem && 0 != this.w_extrafeeitem.compareTo("") && 0 != this.w_extrafeeitem.compareTo("00000"));
	}

	public String getW_extrafeeitem() {
		return this.w_extrafeeitem;
	}

	public void SetSecondFeeAmount(String sfa) {
		this.billamountforfeeitem2 = sfa;
	}

	public String ForFile() {
		return "";
	}

	private String Claim1(String logNo) {
		return this.Claim(logNo, this.billamountforfeeitem1, this.w_feeitem, "N");
	}

	private String Note1(String logNo) {

		return this.Note(
		        logNo,
		        (Misc.forwardZero("D1", 4) + Misc.backwardSpace(this.w_rphysician, 1) + Misc.space(8) + Misc.backwardSpace(this.w_capability, 1) + Misc.backwardSpace(this.w_rehabtype, 1) + Misc.backwardSpace(this.w_estimate, 1) + Misc.backwardSpace(this.w_work, 1) + Misc.backwardSpace(this.w_tofollow, 1) + dateFormat(this.w_estimatedate) + dateFormat(this.w_workdate) + Misc.backwardSpace(this.w_duration, 1) + Misc.backwardSpace(this.w_ftreatment, 25) + Misc.backwardSpace(this.w_diagnosis, 120)
		                + Misc.backwardSpace(this.w_wcbadvisor, 1) + Misc.backwardSpace(this.w_rehab, 1) + Misc.forwardZero(Misc.cleanNumber(this.w_area), 3) + Misc.forwardZero(Misc.cleanNumber(this.w_phone), 7) + Misc.backwardSpace(this.w_address, 25) + Misc.backwardSpace(this.w_city, 20) + Misc.backwardSpace(this.w_postalcode, 6) + Misc.forwardZero(Misc.cleanNumber(this.w_emparea), 3) + Misc.forwardZero(Misc.cleanNumber(this.w_empphone), 7) + Misc.backwardSpace(this.w_empname, 25)
		                + Misc.backwardSpace(this.w_opaddress, 25) + Misc.backwardSpace(this.w_opcity, 25) + ((this.w_reporttype.compareTo("F") == 0) ? Misc.backwardSpace("Y", 2) : Misc.forwardSpace("Y", 2)) + Misc.space(70)));
	}

	private String Claim2(String logNo) {
		return this.Claim(logNo, "0", "19333", "N");
	}

	private String Note2(String logNo) {
		return this.Note(logNo, Misc.backwardSpace(((this.w_problem.compareTo("") == 0) ? "Intentionally left blank" : this.w_problem), 160), Misc.backwardSpace(this.w_capreason, 240));
	}

	private String Claim3(String logNo) {
		return this.Claim(logNo, "0", "19334", "N");
	}

	private String Note3(String logNo) {
		return this.Note(logNo, Misc.backwardSpace(this.w_clinicinfo, 400));
	}

	private String Claim4(String logNo) {
		return this.Claim(logNo, "0", "19335", "N");
	}

	private String Note4(String logNo) {
		int length = this.w_clinicinfo.length();
		return this.Note(logNo, Misc.backwardSpace(((length >= 400) ? this.w_clinicinfo.substring(400, length) : "Clinical Information Complete"), 400));
	}

	private String Claim5(String logNo) {
		return this.Claim(logNo, this.billamountforfeeitem1, this.w_feeitem, "0");
	}

	private String Note(String logNo, String a) {
		return this.Note(logNo, a, "");
	}

	private String Note(String logNo, String a, String b) {
		return "N01" + this.ClaimNote1Head(logNo) + "W" + a + b;
	}

	private String Claim(String logNo, String billedAmount, String feeitem, String correspondenceCode) {
		return "C02" + this.ClaimNote1Head(logNo) + Misc.forwardZero("", 10)
		//+ Misc.zero(10)      //phn
		        + "0" //Misc.backwardSpace("", 1).toUpperCase()
		        + "0" //Misc.space(1)
		        + "00"//Misc.backwardSpace("", 2).toUpperCase()
		        + Misc.zero(2) + Misc.forwardZero("1", 3) + Misc.zero(2 + 2 + 1 + 2) //clarification
		        + Misc.forwardZero(feeitem, 5) + Misc.moneyFormatPaddedZeroNoDecimal(billedAmount, 7) + Misc.zero(1) + dateFormat(this.w_servicedate) + Misc.zero(2) + "W"//Misc.zero(1) //Submission Code
		        + Misc.space(1) + Misc.forwardZero(this.w_icd9, 5) + Misc.space(5 + 5 + 15) + Misc.backwardSpace(this.visit_type, 1) + Misc.zero(1 + 5 + 1 + 5 + 4 + 4 + 4 + 8) + Misc.forwardZero(this.billing_no, 7) + Misc.forwardZero(correspondenceCode, 1) //correspondence code//+ Misc.zero(1)
		        + Misc.space(20) + "N" + Misc.zero(8 + 20 + 5 + 5) + Misc.space(58) //Part II of Claim 1
		        + "WC" + Misc.backwardZero(this.w_phn, 12) + dateFormat(this.w_dob) + Misc.backwardSpace(this.w_fname, 12) + Misc.backwardSpace(this.w_mname, 1) + Misc.backwardSpace(this.w_lname, 18) + Misc.backwardSpace(this.w_gender, 1) + Misc.backwardSpace(dateFormat(this.w_doi), 25) + Misc.backwardSpace(Misc.backwardSpace(w_bp, 5) + Misc.backwardSpace(w_side, 2), 25) + Misc.backwardSpace(this.w_noi, 25) + Misc.backwardSpace(this.w_wcbno, 25) + Misc.space(6);
	}

	private String ClaimNote1Head(String logNo) {
		return Misc.forwardZero(OscarProperties.getInstance().getProperty("dataCenterId"), 5) + Misc.forwardZero(String.valueOf(logNo), 7) + Misc.forwardZero(this.w_payeeno, 5) + Misc.forwardZero(this.w_pracno, 5);
	}

	public String dateFormat(String date) {
		return Misc.forwardZero(oscar.Misc.cleanNumber(date), 8);
	}

	public String getHtmlLine() {
		return getHtmlLine(this.billing_no, this.invoice_no, this.w_lname + "," + this.w_fname, this.w_phn, this.w_servicedate, this.w_feeitem, this.billamountforfeeitem1, this.w_icd9, "", "");

	}

	public String getHtmlLine(String billingMasterNo, String invNo, String demoName, String phn, String serviceDate, String billingCode, String billAmount, String dx1, String dx2, String dx3) {
		String htmlContent = "<tr>" + "<td class='bodytext'>" + "<a href='#' onClick=\"openBrWindow('billingTeleplanCorrectionWCB.jsp?billing_no=" + Misc.forwardZero(billingMasterNo, 7) + "','','resizable=yes,scrollbars=yes,top=0,left=0,width=900,height=600'); return false;\">" + invNo + "</a>" + "</td>" + "<td class='bodytext'>" + demoName + "</td>" + "<td class='bodytext'>" + w_phn + "</td>" + "<td class='bodytext'>" + dateFormat(serviceDate) + "</td>" + "<td class='bodytext'>" + billingCode + "</td>"
		        + "<td align='right' class='bodytext'>" + billAmount + "</td>" + "<td align='right' class='bodytext'>" + Misc.backwardSpace(dx1, 5) + "</td>" + "<td align='right' class='bodytext'>" + Misc.backwardSpace(dx2, 5) + "</td>" + "<td align='right' class='bodytext'>" + Misc.backwardSpace(dx3, 5) + "</td>" + "<td class='bodytext'>" + Misc.forwardZero(billingMasterNo, 7) + "</td>" + "<td class='bodytext'>&nbsp;</td>" + "</tr>";
		return htmlContent;
	}

	boolean isFormNeeded() {
		boolean retval = true;
		if (formNeeded != null && formNeeded.equals("0")) {
			retval = false;
		}
		return retval;
	}
}
