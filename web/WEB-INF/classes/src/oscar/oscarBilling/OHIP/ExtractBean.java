// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster
// University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received
// a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
// *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster Unviersity
//* Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarBilling.OHIP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

public class ExtractBean extends Object implements Serializable {
	private String appt;
	private String apptDate;
	private String batchCount = "";
	private String batchHeader;
	private int batchOrder = 0;
	private BigDecimal bdFee = new BigDecimal(0).setScale(2,
			BigDecimal.ROUND_HALF_UP);
	private BigDecimal BigTotal = new BigDecimal(0).setScale(2,
			BigDecimal.ROUND_HALF_UP);
	private String billingUnit;
	private int count = 0;
	private String dateRange = "";
	public String[] dbParam;
	private String demoName;
	private String demoSex = "";
	private double dFee;
	private String diagcode;
	private String dob;
	private String eFlag = "";
	private String fee;
	private int flag = 0;
	private int flagOrder = 0;
	private SimpleDateFormat formatter;
	private String groupNo;
	private String hcCount = "";
	private String hcFirst = "";
	private String hcFlag = "";
	private String hcHin = "";
	private String hcLast = "";
	private String hcType = "";
	private String HE = "HE";
	private int healthcardCount = 0;
	private String hin;
	private String htmlClass = "";
	private String htmlCode = "";
	private String htmlContent = "";
	private String htmlContentHeader = "";
	private String htmlFilename;
	private String htmlFooter = "";
	private String htmlHeader = "";
	private String htmlValue = "";
	private String inPatient;
	private int invCount = 0;
	private String invNo;
	private int it;
	private ArrayList iterator;
	private String m_Flag = "";
	private String m_review = "";
	private String ohipCenter;
	private String ohipClaim;
	private String ohipFilename;
	private String ohipReciprocal;
	private String ohipRecord;
	private String ohipVer;
	private String oscar_home;
	private String outPatient;
	private String outPatientDate;
	private String outPatientDateValue;
	private String output;
	public String password;
	private int patientCount = 0;
	private String patientHeader;
	private String patientHeader2;
	private String pCount = "";
	private BigDecimal percent = new BigDecimal(0).setScale(2,
			BigDecimal.ROUND_HALF_UP);
	private String providerNo;
	private String query;
	private String query2;
	private String query3;
	private String rCount = "";
	private int recordCount = 0;
	private String referral;
	private String referralDoc;
	private String reportCount;
	private String reportGenDate;
	public String sdriver;
	private int secondFlag = 0;
	private String serviceCode;
	private String spec;
	private String specCode;
	private String specialty;
	public String surl;
	private int thirdFlag = 0;
	private java.util.Date today;
	private String totalAmount;
	public String user;
	private String value;
	private String ver;
	private java.sql.Date visitDate;
	private String visitType;
	
	public ExtractBean() {
		formatter = new SimpleDateFormat("yyyyMMdd"); //yyyyMMddHmm");
		today = new java.util.Date();
		output = formatter.format(today);
	}
	
	public void dbQuery(String[] dbP) {
		dbParam = dbP;
		sdriver = dbParam[0];
		surl = dbParam[1];
		user = dbParam[2];
		password = dbParam[3];
		
		try {
			htmlClass = "class='bodytext'";
			batchOrder = 4 - batchCount.length();
			batchHeader = HE + "B" + ohipVer + getOhipCenter()
					+ output.substring(0, 8) + zero(batchOrder) + batchCount
					+ space(6) + groupNo + providerNo + specialty + space(42)
					+ "\r";
			htmlContentHeader = "<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td colspan='4' class='bodytext'>OHIP Invoice for OHIP No."
					+ providerNo
					+ "</td><td colspan='4' class='bodytext'>Payment date of "
					+ output.substring(0, 8) + "</td></tr>";
			htmlContentHeader = htmlContentHeader
					+ "<tr><td class='bodytext'>ACCT NO</td><td class='bodytext'>NAME</td><td class='bodytext'>HEALTH #</td><td class='bodytext'>BILLDATE</td><td class='bodytext'>CODE</td><td align='right' class='bodytext'>BILLED</td><td align='right' class='bodytext'>DX</td><td align='right' class='bodytext'>Comment</td></tr>";
			htmlValue = htmlContentHeader;
			value = batchHeader;
			dbExtract dbExt = new dbExtract();
			dbExt.openConnection(sdriver, surl, user, password);
			query = "select * from billing where provider_ohip_no='"
					+ providerNo + "' and (status='O' or status='W') "
					+ dateRange;
			ResultSet rs = dbExt.executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					patientCount = patientCount + 1;
					invNo = rs.getString("billing_no");
					//   ohipVer = rs.getString("organization_spec_code");
					inPatient = rs.getString("clinic_no");
					hin = rs.getString("hin");
					dob = rs.getString("dob");
					demoName = rs.getString("demographic_name");
					visitType = rs.getString("visittype");
					outPatient = rs.getString("clinic_ref_code");
					// outPatientDate = rs.getDate("visitdate").toString();
					visitDate = rs.getDate("visitdate");
					referral = "";
					referralDoc = "000000";
					hcFlag = "";
					referral = oscar.SxmlMisc.getXmlContent(rs
							.getString("content"), "<xml_referral>",
							"</xml_referral>");
					hcType = oscar.SxmlMisc.getXmlContent(rs
							.getString("content"), "<hctype>", "</hctype>");
					m_review = oscar.SxmlMisc.getXmlContent(rs
							.getString("content"), "<mreview>", "</mreview>");
					if (m_review == null) {
						m_review = space(1);
						m_Flag = "";
					}
					if (m_review.compareTo("checked") == 0) {
						m_review = "Y";
						m_Flag = "M";
					} else {
						m_review = space(1);
						m_Flag = "";
					}
					if (hcType == null || hcType.compareTo("ON") == 0
							|| hcType.compareTo("") == 0) {
						hcFlag = "";
					} else {
						demoSex = oscar.SxmlMisc.getXmlContent(rs
								.getString("content"), "<demosex>",
								"</demosex>");
						hcFlag = "H";
						hcLast = demoName.substring(0, demoName.indexOf(","))
								.toUpperCase();
						hcFirst = demoName.substring(demoName.indexOf(",") + 1)
								.toUpperCase();
						if (hcLast.length() < 9) {
							hcLast = hcLast + space(9 - hcLast.length());
						} else {
							hcLast = hcLast.substring(0, 9);
						}
						if (hcFirst.length() < 5) {
							hcFirst = hcFirst + space(5 - hcFirst.length());
						} else {
							hcFirst = hcFirst.substring(0, 5);
						}
					}
					if (referral == null) {
						referral = "";
					}
					referralDoc = oscar.SxmlMisc.getXmlContent(rs
							.getString("content"), "<rdohip>", "</rdohip>");
					if (referral.compareTo("checked") == 0) {
						if (referralDoc == null
								|| referralDoc.compareTo("000000") == 0) {
							referral = "";
							referralDoc = space(6);
						} else {
							referral = "R";
							//referralDoc = referralDoc;
						}
					} else {
						referral = "";
						referralDoc = space(6);
					}
					if (visitDate == null) {
						outPatientDateValue = space(8);
					} else {
						outPatientDate = visitDate.toString();
						outPatientDateValue = outPatientDate.substring(0, 4)
								+ outPatientDate.substring(5, 7)
								+ outPatientDate.substring(outPatientDate
										.length() - 2);
					}
					specCode = rs.getString("status");
					if (specCode.compareTo("O") == 0) {
						spec = "HCP";
					} else {
						spec = "WCB";
					}
					if (hin.length() < 12) {
						hin = hin + space(12 - hin.length());
					} else {
						hin = hin.substring(0, 12);
					}
					hin = hin.toUpperCase();
					count = invNo.length();
					count = 8 - count;
					hcHin = hin;
					if (hcFlag.compareTo("H") == 0) {
						spec = "RMB";
						healthcardCount = healthcardCount + 1;
						hcHin = hin;
						hin = space(12);
						patientHeader2 = "\n" + HE + "R" + hcHin + hcLast
								+ hcFirst + demoSex + hcType + space(47) + "\r";
					} else {
						patientHeader2 = "";
					}
					if (visitType == null) {
						patientHeader = HE + "H" + hin + dob + zero(count)
								+ invNo + spec + "P" + referralDoc + space(4)
								+ space(8) + space(4) + m_review + inPatient
								+ space(11) + space(6);
					} else {
						if (visitType.compareTo("00") == 0) {
							patientHeader = HE + "H" + hin + dob + zero(count)
									+ invNo + spec + "P" + referralDoc
									+ space(4) + space(8) + space(4) + m_review
									+ inPatient + space(11) + space(6);
						} else {
							patientHeader = HE + "H" + hin + dob + zero(count)
									+ invNo + spec + "P" + referralDoc
									+ outPatient + outPatientDateValue
									+ space(4) + m_review + inPatient
									+ space(11) + space(6);
						}
					}
					value = value + "\n" + patientHeader + "\r"
							+ patientHeader2;
					invCount = 0;
					query2 = "select * from billingdetail where billing_no='"
							+ invNo + "' and status='" + specCode + "'";
					ResultSet rs2 = dbExt.executeQuery2(query2);
					while (rs2.next()) {
						recordCount = recordCount + 1;
						count = 0;
						serviceCode = rs2.getString("service_code");
						fee = rs2.getString("billing_amount");
						diagcode = rs2.getString("diagnostic_code");
						appt = rs2.getDate("appointment_date").toString();
						billingUnit = rs2.getString("billingunit");
						count = 6 - fee.length();
						apptDate = appt.substring(0, 4) + appt.substring(5, 7)
								+ appt.substring(appt.length() - 2);
						dFee = Double.parseDouble(fee);
						bdFee = new BigDecimal(dFee).setScale(2,
								BigDecimal.ROUND_HALF_UP);
						BigTotal = BigTotal.add(bdFee);
						value = value + "\n" + HE + "T" + serviceCode
								+ space(2) + zero(count) + fee + "0"
								+ billingUnit + apptDate + diagcode + space(12)
								+ space(5) + space(2) + space(6) + space(25)
								+ "\r";
						if (invCount == 0) {
							htmlContent = htmlContent
									+ "<tr><td class='bodytext'>"
									+ invNo
									+ "</td><td class='bodytext'>"
									+ demoName
									+ "</td><td class='bodytext'>"
									+ hcHin
									+ "</td><td class='bodytext'>"
									+ apptDate
									+ "</td><td class='bodytext'>"
									+ serviceCode
									+ "</td><td align='right' class='bodytext'>"
									+ fee
									+ "</td><td align='right' class='bodytext'>"
									+ diagcode
									+ "</td><td class='bodytext'> &nbsp; &nbsp;"
									+ referral + hcFlag + m_Flag
									+ " </td></tr>";
						} else {
							htmlContent = htmlContent
									+ "<tr><td class='bodytext'>&nbsp;</td><td class='bodytext'>&nbsp;</td><td class='bodytext'>&nbsp;</td><td class='bodytext'>&nbsp;</td><td class='bodytext'>"
									+ serviceCode
									+ "</td><td align='right' class='bodytext'>"
									+ fee
									+ "</td><td align='right' class='bodytext'>"
									+ diagcode
									+ "</td><td class='bodytext'>&nbsp;</td></tr>";
						}
						invCount = invCount + 1;
					}
					if (eFlag.compareTo("1") == 0) {
						setAsBilled(invNo, sdriver, surl, user, password);
					}
				}
				hcCount = hcCount + healthcardCount;
				pCount = pCount + patientCount;
				rCount = rCount + recordCount;
				flagOrder = 4 - pCount.length();
				secondFlag = 5 - rCount.length();
				thirdFlag = 4 - hcCount.length();
				percent = new BigDecimal(0.01).setScale(2,
						BigDecimal.ROUND_HALF_UP);
				BigTotal = BigTotal.multiply(percent);
				value = value + "\n" + HE + "E" + zero(flagOrder) + pCount
						+ zero(thirdFlag) + hcCount + zero(secondFlag) + rCount
						+ space(63) + "\r";
				htmlContent = htmlContent
						+ "<tr><td colspan='8' class='bodytext'>&nbsp;</td></tr><tr><td colspan='4' class='bodytext'>OHIP No: "
						+ providerNo
						+ ": "
						+ pCount
						+ " RECORDS PROCESSED</td><td colspan='4' class='bodytext'>TOTAL: "
						+ BigTotal.toString().substring(0,
								BigTotal.toString().length() - 2)
						+ "</td></tr>";
				totalAmount = BigTotal.toString();
				//  writeFile(value);
				htmlValue = htmlValue + htmlContent + "</table>";
				htmlHeader = "<html><body><style type='text/css'><!-- .bodytext{  font-family: Arial, Helvetica, sans-serif;  font-size: 12px; font-style: normal;  line-height: normal;  font-weight: normal;  font-variant: normal;  text-transform: none;  color: #003366;  text-decoration: none; --></style>";
				htmlFooter = "</body></html>";
				htmlCode = htmlHeader + htmlValue + htmlFooter;
				// writeHtml(htmlCode);
				ohipReciprocal = String.valueOf(hcCount);
				ohipRecord = String.valueOf(rCount);
				ohipClaim = String.valueOf(pCount);
			}
			dbExt.closeConnection();
		} catch (SQLException e) {
		}
	}
	public String getHtmlCode() {
		return htmlCode;
	}
	public String getHtmlValue() {
		return htmlValue;
	}
	public String getInvNo() {
		return invNo;
	}
	public String getOhipCenter() {
		return ohipCenter;
	}
	public String getOhipClaim() {
		return ohipClaim;
	}
	public String getOhipReciprocal() {
		return ohipReciprocal;
	}
	public String getOhipRecord() {
		return ohipRecord;
	}
	public String getOhipVer() {
		return ohipVer;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public String getValue() {
		return value;
	}
	public void setAsBilled(String newInvNo, String newsdriver, String newsurl,
			String newuser, String newpassword) {
		try {
			dbExtract dbExt1 = new dbExtract();
			dbExt1.openConnection(newsdriver, newsurl, newuser, newpassword);
			String query30 = "update billing set status='B' where billing_no='"
					+ newInvNo + "'";
			dbExt1.setUpdateString(query30);
			dbExt1.executeUpdate();
			dbExt1.closeConnection();
		} catch (SQLException e) {
		}
	}
	// batchCount 1 ???
	public synchronized void setBatchCount(String newBatchCount) {
		batchCount = newBatchCount;
	}
	public synchronized void setDateRange(String newDateRange) {
		dateRange = newDateRange;
	}
	// flag 0 - nothing ??? 1 - set as billed.
	public synchronized void seteFlag(String neweFlag) {
		eFlag = neweFlag;
	}
	public synchronized void setGroupNo(String newGroupNo) {
		groupNo = newGroupNo;
	}
	public synchronized void setHtmlFilename(String newHtmlFilename) {
		htmlFilename = newHtmlFilename;
	}
	public synchronized void setOhipCenter(String newOhipCenter) {
		ohipCenter = newOhipCenter;
	}
	public synchronized void setOhipFilename(String newOhipFilename) {
		ohipFilename = newOhipFilename;
	}
	public synchronized void setOhipVer(String newOhipVer) {
		ohipVer = newOhipVer;
	}
	public synchronized void setOscarHome(String oscarHOME) {
		oscar_home = oscarHOME;
	}
	public synchronized void setProviderNo(String newProviderNo) {
		providerNo = newProviderNo;
	}
	public synchronized void setSpecialty(String newSpecialty) {
		specialty = newSpecialty;
	}
	// write OHIP file to it
	public void writeFile(String value1) {
		try {
			String home_dir;
			String userHomePath = System.getProperty("user.home", "user.dir");
			//System.out.println(userHomePath);
			
			File pFile = new File(userHomePath, oscar_home);
			FileInputStream pStream = new FileInputStream(pFile.getPath());
			Properties ap = new Properties();
			ap.load(pStream);
			home_dir = ap.getProperty("HOME_DIR");
			pStream.close();
			
			FileOutputStream out = new FileOutputStream(home_dir + ohipFilename);
			PrintStream p = new PrintStream(out);
			p.println(value1);
			//System.out.println(sqlE.record);
			p.close();
			out.close();
		} catch (Exception e) {
			System.err.println("Write OHIP File Error");
		}
	}
	// get path from the property file, e.g. OscarDocument/.../billing/download/, and then write to it
	public void writeHtml(String htmlvalue1) {
		try {
			String home_dir1;
			String userHomePath1 = System.getProperty("user.home", "user.dir");
			// System.out.println(userHomePath);
			
			File pFile1 = new File(userHomePath1, oscar_home);
			FileInputStream pStream1 = new FileInputStream(pFile1.getPath());
			Properties ap1 = new Properties();
			ap1.load(pStream1);
			home_dir1 = ap1.getProperty("HOME_DIR");
			pStream1.close();
			
			FileOutputStream out1 = new FileOutputStream(home_dir1 + htmlFilename);
			PrintStream p1 = new PrintStream(out1);
			p1.println(htmlvalue1);
			//System.out.println(sqlE.record);
			p1.close();
			out1.close();
		} catch (Exception e) {
			System.err.println("Write HTML File Error!!!");
		}
	}
	// return i space str, e.g. "    "
	public String space(int i) {
		String returnValue = new String();
		for (int j = 0; j < i; j++) {
			returnValue += " ";
		}
		return returnValue;
	}
	// return x zero str, e.g. 000000
	public String zero(int x) {
		String returnZeroValue = new String();
		for (int y = 0; y < x; y++) {
			returnZeroValue += "0";
		}
		return returnZeroValue;
	}
}
