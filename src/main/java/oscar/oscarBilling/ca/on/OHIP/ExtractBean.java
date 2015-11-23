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

package oscar.oscarBilling.ca.on.OHIP;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.dao.BillingDetailDao;
import org.oscarehr.billing.CA.model.BillingDetail;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.DateRange;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

public class ExtractBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
	private static Logger logger=MiscUtils.getLogger(); 
    private  BillingDao billingDao = SpringUtils.getBean(BillingDao.class);

    private String apptDate;
    private String batchCount = "";
    private String batchHeader;
    private int batchOrder = 0;
    private BigDecimal bdFee = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal BigTotal = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
    private String billingUnit;
    private String content;
    private int count = 0;
    private DateRange dateRange = null;
    public String[] dbParam;
    private String demoName;
    private String demoSex = "";
    private double dFee;
    private String diagcode;
    private String dob;
    private String eFlag = "";
    public String errorMsg = "";
    public String errorPartMsg = "";
    private String fee;
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
    private String htmlCode = "";
    private String htmlContent = "";
    private String htmlFilename;
    private String htmlFooter = "";
    private String htmlHeader = "";
    private String htmlValue = "";
    private String inPatient;
    private int invCount = 0;
    private String invNo;
    private String m_Flag = "";
    private String m_review = "";
    private String ohipCenter;
    private String ohipClaim;
    private String ohipFilename;
    private String ohipReciprocal;
    private String ohipRecord;
    private String ohipVer;
    private String outPatient;
    private String outPatientDateValue;
    private String output;
    private int patientCount = 0;
    private String patientHeader;
    private String patientHeader2;
    private String pCount = "";
    private BigDecimal percent = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
    private String providerNo;
    private String rCount = "";
    private int recordCount = 0;
    private String referral;
    private String referralDoc;
    private int secondFlag = 0;
    private String serviceCode;
    private String spec;
    private String specCode;
    private String specialty;
    private int thirdFlag = 0;
    private java.util.Date today;
    private String totalAmount;
    private String value;
    private java.sql.Date visitDate;
    private String visitType;

    public ExtractBean() {
        formatter = new SimpleDateFormat("yyyyMMdd"); //yyyyMMddHmm");
        today = new java.util.Date();
        output = formatter.format(today);
    }

    private String buildBatchHeader() {
        return (HE + "B" + ohipVer + ohipCenter + output + zero(batchOrder) + batchCount + space(6) + groupNo
                + providerNo + specialty + space(42) + "\r");
    }

    private String buildHeader1() {
        referralDoc = "000000";
        hcFlag = ""; // for html content
        m_Flag = ""; // for html content
        referral = oscar.SxmlMisc.getXmlContent(content, "<xml_referral>", "</xml_referral>");
        referralDoc = oscar.SxmlMisc.getXmlContent(content, "<rdohip>", "</rdohip>");
        hcType = oscar.SxmlMisc.getXmlContent(content, "<hctype>", "</hctype>");
        m_review = oscar.SxmlMisc.getXmlContent(content, "<mreview>", "</mreview>");
        m_review = (m_review != null && m_review.equals("checked")) ? "Y" : space(1);
        m_Flag = m_review.equals("Y") ? "M" : "";
        hcFlag = (hcType == null || hcType.compareTo("ON") == 0 || hcType.compareTo("") == 0) ? "" : "H";
        if (referral != null && referral.compareTo("checked") == 0) {
            if (referralDoc == null || referralDoc.compareTo("000000") == 0) {
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
        outPatientDateValue = (visitDate == null) ? space(8) : UtilDateUtilities.DateToString(visitDate, "yyyyMMdd");
        spec = (specCode != null && specCode.equals("O")) ? "HCP" : "WCB";
        hin = (hin.length() < 12) ? (hin + space(12 - hin.length())) : hin.substring(0, 12);
        hin = hin.toUpperCase();
        count = invNo.length();
        count = 8 - count;
        hcHin = hin; // need for html content

        if (hcFlag.compareTo("H") == 0) {
            patientHeader2 = buildHeader2();
            hin = space(12);
        } else {
            patientHeader2 = "";
        }
        checkHeader1();
        if (visitType == null || visitType.compareTo("00") == 0) {
            patientHeader = HE + "H" + hin + dob + zero(count) + invNo + spec + "P" + referralDoc + space(4)
                    + space(8) + space(4) + m_review + inPatient + space(11) + space(6);
        } else {
            patientHeader = HE + "H" + hin + dob + zero(count) + invNo + spec + "P" + referralDoc + outPatient
                    + outPatientDateValue + space(4) + m_review + inPatient + space(11) + space(6);
        }
        return ("\n" + patientHeader + "\r" + patientHeader2);
    }

    private String buildHeader2() {
        spec = "RMB";
        hcFlag = "H";
        healthcardCount++;
        demoSex = oscar.SxmlMisc.getXmlContent(content, "<demosex>", "</demosex>");
        hcLast = demoName.substring(0, demoName.indexOf(",")).toUpperCase();
        hcFirst = demoName.substring(demoName.indexOf(",") + 1).toUpperCase();
        hcLast = hcLast.length() < 9 ? (hcLast + space(9 - hcLast.length())) : (hcLast.substring(0, 9));
        hcFirst = hcFirst.length() < 5 ? (hcFirst + space(5 - hcFirst.length())) : (hcFirst.substring(0, 5));
        hcLast.replaceAll("\\W", "");
        hcFirst.replaceAll("\\W", "");
        checkHeader2();
        //patientHeader2 = "\n" + HE + "R" + hcHin + hcLast + hcFirst + demoSex + hcType +
        // space(47) + "\r";
        return ("\n" + HE + "R" + hcHin + hcLast + hcFirst + demoSex + hcType + space(47) + "\r");
    }

    private String buildHTMLContentHeader() {
        String ret = null;
        ret = "\n<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"
                + "<tr><td colspan='4' class='bodytext'>OHIP Invoice for OHIP No." + providerNo
                + "</td><td colspan='4' class='bodytext'>Payment date of " + output + "\n</td></tr>";
        ret += "\n<tr><td class='bodytext'>ACCT NO</td>"
                + "<td class='bodytext'>NAME</td><td class='bodytext'>HEALTH #</td>"
                + "<td class='bodytext'>BILLDATE</td><td class='bodytext'>CODE</td>"
                + "<td align='right' class='bodytext'>BILLED</td>"
                + "<td align='right' class='bodytext'>DX</td><td align='right' class='bodytext'>Comment</td></tr>";
        return ret;
    }

    private String buildHTMLContentRecord(int invCount) {
        String ret = null;
        if (invCount == 0) {
            ret = "\n<tr><td class='bodytext'>" + invNo + "</td><td class='bodytext'>" + demoName
                    + "</td><td class='bodytext'>" + hcHin + "</td><td class='bodytext'>" + apptDate
                    + "</td><td class='bodytext'>" + serviceCode + "</td><td align='right' class='bodytext'>" + fee
                    + "</td><td align='right' class='bodytext'>" + diagcode
                    + "</td><td class='bodytext'> &nbsp; &nbsp;" + referral + hcFlag + m_Flag + " </td></tr>";
        } else {
            ret = "\n<tr><td class='bodytext'>&nbsp;</td> <td class='bodytext'>&nbsp;</td>"
                    + "<td class='bodytext'>&nbsp;</td> <td class='bodytext'>&nbsp;</td>" + "<td class='bodytext'>"
                    + serviceCode + "</td><td align='right' class='bodytext'>" + fee
                    + "</td><td align='right' class='bodytext'>" + diagcode
                    + "</td><td class='bodytext'>&nbsp;</td></tr>";
        }
        return ret;
    }

    private String buildHTMLContentTrailer() {
        htmlContent += "\n<tr><td colspan='8' class='bodytext'>&nbsp;</td></tr><tr><td colspan='4' class='bodytext'>OHIP No: "
                + providerNo
                + ": "
                + pCount
                + " RECORDS PROCESSED</td><td colspan='4' class='bodytext'>TOTAL: "
                + BigTotal.toString().substring(0, BigTotal.toString().length() - 2) + "\n</td></tr>" + "\n</table>";
        //  writeFile(value);
        String checkSummary = errorMsg.equals("") ? "\n<table border='0' width='100%' bgcolor='green'><tr><td>Pass</td></tr></table>"
                : "\n<table border='0' width='100%' bgcolor='orange'><tr><td>Please correct the errors and run this simulation again!</td></tr></table>";
        htmlValue += htmlContent + checkSummary;
        htmlHeader = "<html><body><style type='text/css'><!-- .bodytext{  font-family: Arial, Helvetica, sans-serif;  font-size: 12px; font-style: normal;  line-height: normal;  font-weight: normal;  font-variant: normal;  text-transform: none;  color: #003366;  text-decoration: none; --></style>";
        htmlFooter = "</body></html>";
        htmlCode = htmlHeader + htmlValue + htmlFooter;
        return htmlCode;
    }

    private String buildItem() {
        //return ("\n" + HE + "T" + serviceCode + space(2) + zero(count) + fee + "0" + billingUnit
        // + apptDate + diagcode + space(12)+ space(5) + space(2) + space(6) + space(25) + "\r");
        return ("\n" + HE + "T" + serviceCode + space(2) + zero(count) + fee + forwardZero(billingUnit, 2) + apptDate
                + diagcode + space(12) + space(5) + space(2) + space(6) + space(25) + "\r");

    }

    private String buildTrailer() {
        return ("\n" + HE + "E" + zero(flagOrder) + pCount + zero(thirdFlag) + hcCount + zero(secondFlag) + rCount
                + space(63) + "\r");
    }

    private void checkBatchHeader() {
        if (ohipVer.length() != 3) {
            errorPartMsg = "Batch Header: Ver. wrong!<br>";
        }
        if (ohipCenter.length() != 1) {
            errorPartMsg += "Batch Header: Health Office Code wrong!<br>";
        }
        if (groupNo.length() != 4) {
            errorPartMsg += "Batch Header: GroupNo. wrong!<br>";
        }
        if (providerNo.length() != 6) {
            errorPartMsg += "Batch Header: Provider OHIP No. wrong!<br>";
        }
        if (specialty.length() != 2) {
            errorPartMsg += "Batch Header: Specialty Code wrong!<br>";
        }
        errorMsg += errorPartMsg;
    }

    private void checkHeader1() {
        if (referralDoc != null && referralDoc.length() != 6)
            errorPartMsg = "Header1: Referral Doc. No. wrong!<br>";
        if (visitType != null && visitType.compareTo("00") != 0) {
            if ((outPatient != null && outPatient.length() != 4) || outPatient == null) {
                errorPartMsg = "Header1: outPatient Visit. wrong!<br>";
            }
        }
        errorMsg += errorPartMsg;
    }

    private void checkHeader2() {
        if (hcHin.length() == 0 || hcHin.length() > 12)
            errorPartMsg += "Header2: Reg. No. wrong!<br>";
        if (hcLast.length() == 0)
            errorPartMsg += "Header2: Patient's Lastname wrong!<br>";
        if (hcFirst.length() == 0)
            errorPartMsg += "Header2: Patient's Firstname wrong!<br>";
        if (!(demoSex.equals("1") || demoSex.equals("2")))
            errorPartMsg += "Header2: Patient's Sex Code wrong! (1 or 2)<br>";
        if (hcType.length() != 2
                || !(hcType.equals("AB") || hcType.equals("BC") || hcType.equals("MB") || hcType.equals("NL")
                        || hcType.equals("NB") || hcType.equals("NT") || hcType.equals("NS") || hcType.equals("PE")
                        || hcType.equals("SK") || hcType.equals("YT")))
            errorPartMsg += "Header2: Patient's Province Code wrong!<br>";
        errorMsg += errorPartMsg;
    }

    private void checkItem() {
        if (serviceCode.trim().length() != 5)
            errorPartMsg = "Item: Service Code wrong!<br>";
        errorMsg += errorPartMsg;
    }

    private void checkNoDetailRecord(int invCount) {
        if (invCount == 0)
            errorPartMsg = "The billing no:" + invNo + " should be marked as 'Delete'.<br>";
        errorMsg += errorPartMsg;
    }

    private String printErrorPartMsg() {
        String ret = "";
        ret = errorPartMsg.length() > 0 ? ("\n<tr bgcolor='yellow'><td colspan='8'><font color='red'>" + errorPartMsg + "</font></td></tr>")
                : "";
        errorPartMsg = "";
        return ret;
    }

    public void dbQuery() {
        try {
            batchOrder = 4 - batchCount.length();
            // check length
            checkBatchHeader();
            batchHeader = buildBatchHeader();
            htmlValue = buildHTMLContentHeader();
            // start here
            value = batchHeader;

            BillingDao dao = SpringUtils.getBean(BillingDao.class);
            BillingDetailDao bdDao = SpringUtils.getBean(BillingDetailDao.class); 
            for(Billing b : dao.findByProviderStatusAndDates(providerNo, Arrays.asList(new String[] {"O", "W"}), dateRange)) {
                patientCount++;
                invNo = "" + b.getId();
                //   ohipVer = b.getorganization_spec_code");
                inPatient = "" + b.getClinicNo();
                // if there is no clinic no for a clinic, it should be an empty str
                inPatient = "0".equals(inPatient) ? "    " : inPatient;
                demoName = b.getDemographicName();
                hin = b.getHin();
                dob = b.getDob();
                visitDate = new Date(b.getVisitDate().getTime());
                visitType = b.getVisitType();
                outPatient = b.getClinicRefCode();
                specCode = b.getStatus();
                content = b.getContent();
                value += buildHeader1();
                htmlContent += printErrorPartMsg();
                // build billing detail
                invCount = 0;
                for(BillingDetail bd : bdDao.findByBillingNoAndStatus(ConversionUtils.fromIntString(invNo), specCode)) {
                    recordCount++;
                    count = 0;
                    serviceCode = bd.getServiceCode();
                    fee = bd.getBillingAmount();
                    diagcode = bd.getDiagnosticCode();
                    // changed the following line for the no need of diagcode 
                    diagcode = ":::".equals(diagcode)? "   " : diagcode; 
                    //appt = rs2.getDate("appointment_date").toString();
                    billingUnit = bd.getBillingUnit();
                    count = 6 - fee.length();
                    apptDate = UtilDateUtilities.DateToString(bd.getAppointmentDate(), "yyyyMMdd");
                    dFee = Double.parseDouble(fee);
                    bdFee = BigDecimal.valueOf(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigTotal = BigTotal.add(bdFee);
                    checkItem();
                    value += buildItem();
                    htmlContent += buildHTMLContentRecord(invCount);
                    htmlContent += printErrorPartMsg();
                    invCount++;
                }
                checkNoDetailRecord(invCount);
                htmlContent += printErrorPartMsg();
                if (eFlag.compareTo("1") == 0) {
                    setAsBilled(invNo);
                }
            }
            hcCount = hcCount + healthcardCount;
            pCount = pCount + patientCount;
            rCount = rCount + recordCount;
            flagOrder = 4 - pCount.length();
            secondFlag = 5 - rCount.length();
            thirdFlag = 4 - hcCount.length();
            percent = BigDecimal.valueOf(0.01).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigTotal = BigTotal.multiply(percent);
            value += buildTrailer();
            htmlCode = buildHTMLContentTrailer();
            // writeHtml(htmlCode);
            ohipReciprocal = String.valueOf(hcCount);
            ohipRecord = String.valueOf(rCount);
            ohipClaim = String.valueOf(pCount);
            totalAmount = BigTotal.toString();
            //}
            //dbExt.closeConnection();
        } catch (Exception e) {
        	MiscUtils.getLogger().error("error",e);
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

    public void setAsBilled(String newInvNo) {
    	Billing b = billingDao.find(Integer.parseInt(newInvNo));
    	if(b != null) {
    		b.setStatus("B");
    		billingDao.merge(b);
    	}
    }

    // batchCount 1 ???
    public synchronized void setBatchCount(String newBatchCount) {
        batchCount = newBatchCount;
    }

    public synchronized void setDateRange(DateRange newDateRange) {
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
    	//empty.potential problem here.
    }

    public synchronized void setProviderNo(String newProviderNo) {
        providerNo = newProviderNo;
    }

    public synchronized void setSpecialty(String newSpecialty) {
        specialty = newSpecialty;
    }

    // return i space str, e.g. " "
    public String space(int i) {
        String returnValue = new String();
        for (int j = 0; j < i; j++) {
            returnValue += " ";
        }
        return returnValue;
    }

    // write OHIP file to it
    public void writeFile(String value1) {
        try {
            String home_dir;
            home_dir = OscarProperties.getInstance().getProperty("HOME_DIR");
            FileOutputStream out = new FileOutputStream(home_dir + ohipFilename);
            PrintStream p = new PrintStream(out);
            p.println(value1);

            p.close();
            out.close();
        } catch (Exception e) {
            logger.error("Write OHIP File Error", e);
        }
    }

    // get path from the property file, e.g.
    // OscarDocument/.../billing/download/, and then write to it
    public void writeHtml(String htmlvalue1) {
        try {
            String home_dir1;
            home_dir1 = OscarProperties.getInstance().getProperty("HOME_DIR");

            FileOutputStream out1 = new FileOutputStream(home_dir1 + htmlFilename);
            PrintStream p1 = new PrintStream(out1);
            p1.println(htmlvalue1);

            p1.close();
            out1.close();
        } catch (Exception e) {
            logger.error("Write HTML File Error!!!", e);
        }
    }

    // return x zero str, e.g. 000000
    public String zero(int x) {
        String returnZeroValue = new String();
        for (int y = 0; y < x; y++) {
            returnZeroValue += "0";
        }
        return returnZeroValue;
    }

    // return x length string with zero str, e.g. 0018
    public String forwardZero(String y, int x) {
        // x must >= y.length()
        String returnZeroValue = "";
        for (int i = y.length(); i < x; i++) {
            returnZeroValue += "0";
        }

        return (returnZeroValue + y);
    }
}
