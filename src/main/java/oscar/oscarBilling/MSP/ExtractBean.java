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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.BC.dao.LogTeleplanTxDao;
import org.oscarehr.billing.CA.BC.model.LogTeleplanTx;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;


public class ExtractBean extends Object implements Serializable {
    private static Logger logger=MiscUtils.getLogger(); 
    private LogTeleplanTxDao logTeleplanTxDao = SpringUtils.getBean(LogTeleplanTxDao.class);
    private BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
    private BillingmasterDAO billingmasterDao =  SpringUtils.getBean(BillingmasterDAO.class);

    
    private String ohipRecord;
    private String ohipClaim;
    private String ohipReciprocal;
    private String ohipFilename;
    private String htmlFilename;
    private String value;
    private String query;
    private String query2;
    private String providerNo;
    private String ohipVer;
    private String ohipCenter;
    private String demoName;
    private String invNo;
    private String batchHeader;
    private String totalAmount;
    private int invCount = 0;
    private java.util.Date today;
    private String output;
    private SimpleDateFormat formatter;
    private int recordCount=0;
    private int patientCount=0;
    private String rCount="";
    private String pCount="";
    private String hcCount = "";
    private String htmlCode="";
    private String htmlContent="";
    private String htmlContentHeader="";
    private String htmlFooter="";
    private String htmlValue="";
    private double dFee;
    private BigDecimal BigTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal bdFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    private String dateRange="";
    private String eFlag="";
    private String oscar_home;
    private int vsFlag=0;
    private String logNo = "";
    private String logValue="";
    public String[] dbParam;
    public String errorMsg;
    
    public CheckBillingData checkData = new CheckBillingData();
    
    
    
    public ExtractBean() {                
        formatter = new SimpleDateFormat("yyyyMMddHmm");
        today = new java.util.Date();
        output = formatter.format(today);
    }
    
    public void dbQuery(){
        String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");
        try{
                        
            if (vsFlag == 0) {
                logNo = eFlag.equals("1") ? getSequence() : "1";
                batchHeader = "VS1" + dataCenterId + forwardZero(logNo,7) + "V6242" + "OSCAR_MCMASTER           " + "V1.1      " + "20030930" + "OSCAR MCMASTER                          " + "(905) 575-1300 " + space(25) + space(57) + "\r";
                errorMsg = checkData.checkVS1("VS1" , dataCenterId , forwardZero(logNo,7) , "V6242" , "OSCAR_MCMASTER           " , "V1.1      " , "20030930" , "OSCAR MCMASTER                          " , "(905) 575-1300 " , space(25) , space(57));
                
                logValue = batchHeader;
                if (eFlag.equals("1")) setLog(logNo, logValue);
                vsFlag = 1;
            }
            else{
                batchHeader = "";
            }
            
            htmlContentHeader = "<html><body><style type='text/css'><!-- .bodytext{  font-family: Tahoma, Arial, Helvetica, sans-serif;  font-size: 12px; font-style: normal;  line-height: normal;  font-weight: normal;  font-variant: normal;  text-transform: none;  color: #003366;  text-decoration: none; --></style>";
            htmlContentHeader = htmlContentHeader + "<table width='100%' border='0' cellspacing='0' cellpadding='0'>";
            htmlContentHeader = htmlContentHeader + "<tr>";
            htmlContentHeader = htmlContentHeader + "<td colspan='4' class='bodytext'>Billing Invoice for Billing No."+ providerNo +"</td>";
            htmlContentHeader = htmlContentHeader + "<td colspan='7' class='bodytext'>Payment date of " + output.substring(0,8) + "</td>";
            htmlContentHeader = htmlContentHeader + "</tr>";
            htmlContentHeader = htmlContentHeader + "<tr>";
            htmlContentHeader = htmlContentHeader + "<td width='9%' class='bodytext'>INVOICE</td>";
            htmlContentHeader = htmlContentHeader + "<td width='19%' class='bodytext'>NAME</td>";
            htmlContentHeader = htmlContentHeader + "<td width='12%' class='bodytext'>HEALTH #</td>";
            htmlContentHeader = htmlContentHeader + "<td width='10%' class='bodytext'>BILLDATE</td>";
            htmlContentHeader = htmlContentHeader + "<td width='8%' class='bodytext'>CODE</td>";
            htmlContentHeader = htmlContentHeader + "<td width='14%' align='right' class='bodytext'>BILLED</td>";
            htmlContentHeader = htmlContentHeader + "<td width='4%' align='right' class='bodytext'>DX</td>";
            htmlContentHeader = htmlContentHeader + "<td width='5%' align='right' class='bodytext'>DX2</td>";
            htmlContentHeader = htmlContentHeader + "<td width='6%' align='right' class='bodytext'>DX3</td>";
            htmlContentHeader = htmlContentHeader + "<td width='8%' align='right' class='bodytext'>SEQUENCE</td>";
            htmlContentHeader = htmlContentHeader + "<td width='5%' align='right' class='bodytext'>COMMENT</td>";
            htmlContentHeader = htmlContentHeader + "</tr>";
            
            htmlContentHeader += errorMsg;
            errorMsg = "";
            
            // htmlFooter =  "<tr>    <td colspan='11' class='bodytext'>&nbsp;</td>  </tr>  <tr>    <td colspan='5' class='bodytext'>Billing No: 016096: 173 RECORDS PROCESSED</td>    <td colspan='6' class='bodytext'>TOTAL: 11277.32</td>  </tr></table></body></html>";
            // htmlContentHeader = "<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td colspan='4' class='bodytext'>Billing Invoice for Billing No." + providerNo +"</td><td colspan='4' class='bodytext'>Payment date of " + output.substring(0,8) + "</td></tr>";
            // htmlContentHeader = htmlContentHeader + "<tr><td class='bodytext'>ACCT NO</td><td class='bodytext'>NAME</td><td class='bodytext'>HEALTH #</td><td class='bodytext'>BILLDATE</td><td class='bodytext'>CODE</td><td align='right' class='bodytext'>BILLED</td><td align='right' class='bodytext'>DX</td><td align='right' class='bodytext'>Comment</td></tr>";
            // htmlValue = htmlContentHeader;
            value = batchHeader;
            dbExtract dbExt = new dbExtract();
            MiscUtils.getLogger().debug("1st billing query d");
            
            dbExt.openConnection();
            query = "select * from billing where provider_ohip_no='"+ providerNo+"' and (status='O' or status='W') " + dateRange;
            MiscUtils.getLogger().debug("1st billing query "+query);
            ResultSet rs = dbExt.executeQuery(query);
            if (rs != null){
                while(rs.next()) {
                    patientCount = patientCount + 1;
                    invNo = rs.getString("billing_no");
                    //  ohipVer = rs.getString("organization_spec_code");
                    //  inPatient = rs.getString("clinic_no");
                    //  hin = rs.getString("hin");
                    //  dob = rs.getString("dob");
                    demoName = rs.getString("demographic_name");
                    //  	      visitType = rs.getString("visittype");
                    // outPatient = rs.getString("clinic_ref_code");
                    // outPatientDate = rs.getDate("visitdate").toString();
                    //    visitDate = rs.getDate("visitdate");
                    //    referral = "";
                    //    referralDoc = "000000";
                    //    hcFlag = "";
                    //    referral = oscar.SxmlMisc.getXmlContent(rs.getString("content"),"<xml_referral>","</xml_referral>");
                    //    hcType = oscar.SxmlMisc.getXmlContent(rs.getString("content"),"<hctype>","</hctype>");
                    //    m_review = oscar.SxmlMisc.getXmlContent(rs.getString("content"),"<mreview>","</mreview>");
                    //      if (m_review == null){  m_review = space(1);
                    //		  m_Flag = "";}
                    //    if (m_review.compareTo("checked") == 0){
                    //		  m_review = "Y";
                    //		  m_Flag = "M";
                    //	  } else {
                    //		  m_review = space(1);
                    //		  m_Flag = "";
                    //	  }
                    //    if (hcType == null || hcType.compareTo("ON")==0 || hcType.compareTo("") == 0 ){hcFlag = "";}
                    //   else{
                    //		 demoSex = oscar.SxmlMisc.getXmlContent(rs.getString("content"),"<demosex>","</demosex>");
                    //		 hcFlag = "H";
                    //       hcLast = demoName.substring(0, demoName.indexOf(",")).toUpperCase();
                    //       hcFirst = demoName.substring(demoName.indexOf(",")+1).toUpperCase();
                    //       if (hcLast.length() <9) {hcLast = hcLast + space(9-hcLast.length()); } else{ hcLast = hcLast.substring(0,9);}
                    //       if (hcFirst.length() <5) {hcFirst = hcFirst + space(5-hcFirst.length()); } else{ hcFirst = hcFirst.substring(0,5);}
                    //
                    //	 }
                    //    if (referral == null){referral = "";}
                    //    referralDoc = oscar.SxmlMisc.getXmlContent(rs.getString("content"),"<rdohip>","</rdohip>");
                    //    if (referral.compareTo("checked")==0 ){
                    //    if (referralDoc == null || referralDoc.compareTo("000000")==0){
                    //		 referral = "";
                    //		 referralDoc = space(6);
                    //	  }else{
                    //		referral = "R";
                    //		referralDoc = referralDoc + space(6-referralDoc.length());
                    //	  }
                    //  }
                    //	  else{
                    //		  referral = "";
                    //		  	 referralDoc = space(6);
                    //	  }
                    //      if (visitDate == null) {
                    //      	outPatientDateValue = space(8);
                    //    } else {
                    //    	outPatientDate = visitDate.toString();
                    //     outPatientDateValue = outPatientDate.substring(0,4) + outPatientDate.substring(5,7) + outPatientDate.substring(outPatientDate.length()-2);
                    //
                    //    	}
                    //    specCode = rs.getString("status");
                    
                    //    if (specCode.compareTo("O") == 0){
                    //    	spec = "HCP";
                    //    }
                    //    else
                    //    {
                    //          spec = "WCB";
                    //    }
                    //    if (hin.length() < 12) {
                    //    	hin = hin + space(12-hin.length());
                    //    }
                    //    else
                    //    {
                    //    	hin = hin.substring(0,12);
                    //    }
                    //    hin = hin.toUpperCase();
                    //    count = invNo.length();
                    //    count = 8 - count;
                    //    hcHin =hin;
                    //    if (hcFlag.compareTo("H")==0){ spec="RMB"; healthcardCount = healthcardCount + 1;hcHin =hin; hin = space(12); patientHeader2 = "\n" + HE + "R" + hcHin + hcLast + hcFirst + demoSex + hcType + space(47) + "\r"; }else{patientHeader2 = ""; }
                    //    if (visitType == null){
                    //    	 patientHeader = HE + "H" + hin + dob + zero(count) + invNo + spec + "P" + referralDoc + space(4) + space(8) + space(4) + m_review + inPatient + space(11) + space(6);
                    //} else{
                    //    if (visitType.compareTo("00") == 0){
                    //    patientHeader = HE + "H" + hin + dob + zero(count) + invNo + spec + "P" + referralDoc + space(4) + space(8) + space(4) + m_review + inPatient + space(11) + space(6);
                    //}
                    //else{
                    //	  patientHeader = HE + "H" + hin + dob + zero(count) + invNo + spec + "P" +referralDoc + outPatient + outPatientDateValue + space(4) + m_review + inPatient + space(11) + space(6);
                    //}
                    //}
                    
                    
                    //    value = value + "\n" + patientHeader + "\r" + patientHeader2;
                    
                    
                    
                    invCount = 0;
                    query2 = "select * from billingmaster where billing_no='"+ invNo +"' and billingstatus='O'";

                    ResultSet rs2 = dbExt.executeQuery2(query2);
                    while (rs2.next()) {
                        recordCount = recordCount + 1;                        

                        logNo = eFlag.equals("1") ? getSequence() : "1";
                                              
                        //String dataLine =     rs2.getString("claimcode") + rs2.getString("datacenter") + forwardZero(logNo,7) + rs2.getString("payee_no") + rs2.getString("practitioner_no") +     forwardZero(rs2.getString("phn"),10)    + rs2.getString("name_verify") + rs2.getString("dependent_num") + forwardZero(rs2.getString("billing_unit"),3) + rs2.getString("clarification_code") + rs2.getString("anatomical_area") + rs2.getString("after_hour") + rs2.getString("new_program") + rs2.getString("billing_code") + moneyFormat(rs2.getString("bill_amount"),7) + rs2.getString("payment_mode") + rs2.getString("service_date") +rs2.getString("service_to_day") + rs2.getString("submission_code") + space(1) + backwardSpace(rs2.getString("dx_code1"), 5) + backwardSpace(rs2.getString("dx_code2"), 5) + backwardSpace(rs2.getString("dx_code3"), 5)+ space(15) + rs2.getString("service_location")+ forwardZero(rs2.getString("referral_flag1"), 1) + forwardZero(rs2.getString("referral_no1"),5)+ forwardZero(rs2.getString("referral_flag2"),1) + forwardZero(rs2.getString("referral_no2"),5) + forwardZero(rs2.getString("time_call"),4) + zero(4) + zero(4)+ forwardZero(rs2.getString("birth_date"),8) + forwardZero(rs2.getString("billingmaster_no"), 7) +rs2.getString("correspondence_code")+ space(20) + rs2.getString("mva_claim_code") + rs2.getString("icbc_claim_no") + rs2.getString("original_claim") + rs2.getString("facility_no")+ rs2.getString("facility_sub_no")+ space(58) + backwardSpace(rs2.getString("oin_insurer_code"),2) + backwardSpace(rs2.getString("oin_registration_no"),12)+ backwardSpace(rs2.getString("oin_birthdate"),8)+backwardSpace(rs2.getString("oin_first_name"),12) +backwardSpace(rs2.getString("oin_second_name"),1) + backwardSpace(rs2.getString("oin_surname"),18)+ backwardSpace(rs2.getString("oin_sex_code"),1) + backwardSpace(rs2.getString("oin_address"),25) + backwardSpace(rs2.getString("oin_address2"),25) + backwardSpace(rs2.getString("oin_address3"),25)+ backwardSpace(rs2.getString("oin_address4"),25)+ backwardSpace(rs2.getString("oin_postalcode"),6);
                        String dataLine = getClaimDetailRecord(rs2,logNo);
                        if (dataLine.length() != 424 ){
                            MiscUtils.getLogger().debug("dataLine2 "+logNo+" Len"+dataLine.length());
                        }                        
                        value = value + "\n"+dataLine+"\r";
                        logValue =  dataLine;
                        if (eFlag.equals("1")) setLog(logNo,logValue);
                        dFee = Double.parseDouble(rs2.getString("bill_amount"));
                        bdFee = BigDecimal.valueOf(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
                        BigTotal = BigTotal.add(bdFee);
                        
                        if (invCount == 0) {                            
                            htmlContent = htmlContent + "<tr><td class='bodytext'>" + "<a href='#' onClick=\"openBrWindow('adjustBill.jsp?billing_no="
                            + forwardZero(rs2.getString("billingmaster_no"), 7)
                            + "','','resizable=yes,scrollbars=yes,top=0,left=0,width=900,height=600'); return false;\">" + invNo + "</a>" + "</td><td class='bodytext'>" + demoName + "</td><td class='bodytext'>" +rs2.getString("phn")+ "</td><td class='bodytext'>" +rs2.getString("service_date")+ "</td><td class='bodytext'>"+rs2.getString("billing_code") +"</td><td align='right' class='bodytext'>"+ rs2.getString("bill_amount")+"</td><td align='right' class='bodytext'>"+ backwardSpace(rs2.getString("dx_code1"), 5)+"</td><td align='right' class='bodytext'>"+ backwardSpace(rs2.getString("dx_code2"), 5) +"</td><td align='right' class='bodytext'>"+ backwardSpace(rs2.getString("dx_code3"), 5)+"</td><td class='bodytext'>"+forwardZero(rs2.getString("billingmaster_no"), 7)+"</td><td class='bodytext'>&nbsp;</td></tr>";
                        }else{
                            htmlContent = htmlContent + "<tr><td class='bodytext'></td><td class='bodytext'></td><td class='bodytext'></td><td class='bodytext'></td><td class='bodytext'>"+rs2.getString("billing_code") +"</td><td align='right' class='bodytext'>"+ rs2.getString("bill_amount")+"</td><td align='right' class='bodytext'>"+ backwardSpace(rs2.getString("dx_code1"), 5)+"</td><td align='right' class='bodytext'>"+ backwardSpace(rs2.getString("dx_code2"), 5) +"</td><td align='right' class='bodytext'>"+ backwardSpace(rs2.getString("dx_code3"), 5)+"</td><td class='bodytext'>"+forwardZero(rs2.getString("billingmaster_no"), 7)+"</td><td class='bodytext'>&nbsp;</td></tr>";
                        }
                        
                        errorMsg = checkData.checkC02(rs2.getString("billingmaster_no"), rs2); 
                        htmlContent += errorMsg;
                            
                        invCount = invCount + 1;    
                        if (eFlag.compareTo("1") == 0){
                            setAsBilledMaster(rs2.getString("billingmaster_no"));
                        }
                    }                                        
                    if (eFlag.compareTo("1") == 0) {
                        setAsBilled(invNo);
                    }                                                                                
                }
                //      hcCount = hcCount + healthcardCount;
                pCount = pCount + patientCount;
                rCount = rCount + recordCount;
                //      flagOrder = 4 - pCount.length();
                //      secondFlag = 5 - rCount.length();
                //      thirdFlag= 4 - hcCount.length();
                //      percent = new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_HALF_UP);
                //      BigTotal = BigTotal.multiply(percent);
                //      value = value + "\n" + HE + "E" + zero(flagOrder) + pCount + zero(thirdFlag) +hcCount + zero(secondFlag) + rCount + space(63) + "\r";
                // htmlContent = htmlContent + "<tr><td colspan='8' class='bodytext'>&nbsp;</td></tr><tr><td colspan='4' class='bodytext'>Billing No: "+ providerNo+": "+ pCount+" RECORDS PROCESSED</td><td colspan='4' class='bodytext'>TOTAL: " + BigTotal.toString().substring(0,BigTotal.toString().length() -2)  +"</td></tr>";
                // totalAmount = BigTotal.toString();
                //  writeFile(value);
                //      htmlValue = htmlValue + htmlContent + "</table>";
                //      htmlHeader = "<html><body><style type='text/css'><!-- .bodytext{  font-family: Arial, Helvetica, sans-serif;  font-size: 12px; font-style: normal;  line-height: normal;  font-weight: normal;  font-variant: normal;  text-transform: none;  color: #003366;  text-decoration: none; --></style>";
                
                htmlFooter =  "<tr>    <td colspan='11' class='bodytext'>&nbsp;</td>  </tr>  <tr>    <td colspan='5' class='bodytext'>Billing No: "+ providerNo + ": "+ pCount +" RECORDS PROCESSED</td>    <td colspan='6' class='bodytext'>TOTAL: " +BigTotal +"</td>  </tr></table></body></html>";
                htmlCode = htmlContentHeader + htmlContent + htmlFooter;
                
                if (eFlag.compareTo("1") == 0) {
                    	writeHtml(htmlCode);
                }
                
                ohipReciprocal = String.valueOf(hcCount);
                ohipRecord = String.valueOf(rCount);
                ohipClaim = String.valueOf(pCount);
            }

        }
        catch (SQLException e) {            
            MiscUtils.getLogger().error("Error", e);
        }
        
    }
    

    public void setAsBilled(String newInvNo){
        if (eFlag.equals("1")){
      	  Billing b = billingDao.find(Integer.parseInt(newInvNo));
      	  if(b != null) {
      		  b.setStatus("B");
      		  billingDao.merge(b);
      	  }
        }
      }

      public void setAsBilledMaster(String newInvNo){
        if (eFlag.equals("1")){
      	  Billingmaster b = billingmasterDao.getBillingmaster(Integer.parseInt(newInvNo));
      	  if(b != null) {
      		  b.setBillingstatus("B");
      		  billingmasterDao.update(b);
      	  }
        }
      }

    
    public void setLog(String x, String logValue){
        if (eFlag.equals("1")){
      	  LogTeleplanTx l = this.logTeleplanTxDao.find(Integer.parseInt(x));
      	  if(l != null) {
      		  l.setClaim(logValue.getBytes());
      		  logTeleplanTxDao.merge(l);
      	  }
        }
      }
    
    public String getSequence(){
       LogTeleplanTx l = new LogTeleplanTx();
       l.setClaim("New Log".getBytes());
       logTeleplanTxDao.persist(l);
       return l.getId().toString();      
      }
    

    public void writeFile(String value1){                       
        try{
            String home_dir;
            String userHomePath = System.getProperty("user.home", "user.dir");

            File pFile = new File(userHomePath, oscar_home);
            FileInputStream pStream = new FileInputStream(pFile.getPath());
            
            Properties ap = new Properties();
            ap.load(pStream);
                        
            home_dir = ap.getProperty("HOME_DIR");
            pStream.close();
                        
            FileOutputStream out;

            out = new FileOutputStream(home_dir+ ohipFilename);
            PrintStream p;
            p = new PrintStream(out);                        
            p.println(value1);            

            p.close();
        }
        catch(Exception e) {
        	logger.error("Unexpected error", e);
        }        
    }
    
    public void writeHtml(String htmlvalue1){                        
        try{
            String home_dir1;
            String userHomePath1 = System.getProperty("user.home", "user.dir");

            File pFile1 = new File(userHomePath1, oscar_home);
            FileInputStream pStream1 = new FileInputStream(pFile1.getPath());
            
            Properties ap1 = new Properties();
            ap1.load(pStream1);                        
            home_dir1 = ap1.getProperty("HOME_DIR");
            pStream1.close();
            
            
            FileOutputStream out1;
            out1 = new FileOutputStream(home_dir1+htmlFilename);
            PrintStream p1;
            p1 = new PrintStream(out1);
            
            
            p1.println(htmlvalue1);

            p1.close();
        }
        catch(Exception e) {
        	logger.error("Unexpected error", e);
        }
        
    }
    
    public String space(int i) {
        String returnValue = new String();
        for(int j=0; j < i; j++) {
            returnValue += " ";
        }
        return returnValue;
    }
    
    public String backwardSpace(String y,int i) {
        String returnValue = new String();
        for(int j=y.length(); j < i; j++) {
            returnValue += " ";
        }
        return cutBackString(y+returnValue,i);
    }
    
    
    public String zero(int x) {
        String returnZeroValue = new String();
        for(int y=0; y < x; y++) {
            returnZeroValue += "0";
        }
        return returnZeroValue;
    }
    public String forwardZero(String y, int x) {
        String returnZeroValue = new String();
        for(int i=y.length(); i < x; i++) {
            returnZeroValue += "0";
        }
        
        return cutFrontString(returnZeroValue+y,x);
    }
    public String cutFrontString(String str,int len){
        return str.substring(str.length() - len, str.length());
    }
    public String cutBackString(String str,int len){
        return str.substring(0,len);
    }
    
    public String forwardSpace(String y, int x) {
        String returnZeroValue = new String();
        for(int i=y.length(); i < x; i++) {
            returnZeroValue += " ";
        }        
        return cutFrontString(returnZeroValue+y,x);
    }
    
    public String moneyFormat(String y, int x) {
        String returnZeroValue = forwardZero(y.replaceAll("\\.",""),x);
        return cutFrontString(returnZeroValue,x);                
    }
    
    public String getOhipReciprocal() {
        return ohipReciprocal;
    }
    
    
    public String getOhipRecord() {
        return ohipRecord;
    }
    public String getOhipClaim() {
        return ohipClaim;
    }
    
    public String getTotalAmount() {
        return totalAmount;
    }
    public String getHtmlValue() {
        return htmlValue;
    }
    public String getHtmlCode() {
        return htmlCode;
    }
    public String getValue() {
        return value;
    }
    
    public String getOhipVer() {
        return ohipVer;
    }
    
    public synchronized void setOhipVer(String newOhipVer) {
        ohipVer = newOhipVer;
    }
    
    public synchronized void setGroupNo(String newGroupNo) {
    }
    public synchronized void setSpecialty(String newSpecialty) {
    }
    public String getOhipCenter() {
        return ohipCenter;
    }
    public synchronized void setBatchCount(String newBatchCount) {
    }
    public synchronized void setOhipCenter(String newOhipCenter) {
        ohipCenter = newOhipCenter;
    }
    
    public synchronized void setProviderNo(String newProviderNo) {
        providerNo = newProviderNo;
    }
    
    public synchronized void setOhipFilename(String newOhipFilename) {
        ohipFilename = newOhipFilename;
    }
    
    public synchronized void setHtmlFilename(String newHtmlFilename) {
        htmlFilename = newHtmlFilename;
    }
    public String getInvNo(){
        return invNo;
    }
    
    
    public synchronized void setOscarHome(String oscarHOME){
        oscar_home = oscarHOME;
    }
    
    public synchronized void seteFlag(String neweFlag){
        eFlag = neweFlag;
    }
    
    public synchronized void setVSFlag(int newVSFlag){
        vsFlag = newVSFlag;
    }
    public synchronized void setDateRange(String newDateRange){
        dateRange = newDateRange;
    }
    
    
    public String  roundUp (String str){
       String retval = "1";
       try{
          retval = new java.math.BigDecimal(str).setScale(0,BigDecimal.ROUND_UP).toString();               
       }catch(Exception e){ MiscUtils.getLogger().error("Error", e);}
       return retval;
    }
    
    public String getClaimDetailRecord(ResultSet rs2,String LogNo) throws SQLException{
        String dataLine =     forwardSpace(rs2.getString("claimcode"),3)            //p00   3
                            + forwardSpace(rs2.getString("datacenter"),5)           //p02   5
                            + forwardZero(logNo,7)                                  //p04   7
                            + forwardSpace(rs2.getString("payee_no"),5)             //p06   5
                            + forwardSpace(rs2.getString("practitioner_no"),5)      //p08   5
                            + forwardZero(rs2.getString("phn"),10)                  //p14  10
                            + forwardSpace(rs2.getString("name_verify"),4)          //p16   4
                            + forwardSpace(rs2.getString("dependent_num"),2)        //p18   2
                            + forwardZero(roundUp(rs2.getString("billing_unit")),3)          //p20   3
                            + forwardZero(rs2.getString("clarification_code"),2)    //p22   2
                            + forwardSpace(rs2.getString("anatomical_area"), 2)     //p23   2
                            + forwardSpace(rs2.getString("after_hour"),1)           //p24   1
                            + forwardZero(rs2.getString("new_program"),2)           //p25   2
                            + forwardZero(rs2.getString("billing_code"),5)          //p26   5                      
                            + moneyFormat(rs2.getString("bill_amount"),7)           //p27   7
                            + forwardZero(rs2.getString("payment_mode"), 1)         //p28   1
                            + forwardSpace(rs2.getString("service_date"), 8)        //p30   8
                            + forwardZero(rs2.getString("service_to_day"),2)        //p32   2
                            + forwardSpace(rs2.getString("submission_code"), 1)     //p34   1
                            + space(1)                                              //p35   1
                            + backwardSpace(rs2.getString("dx_code1"), 5)           //p36   5
                            + backwardSpace(rs2.getString("dx_code2"), 5)           //p37   5
                            + backwardSpace(rs2.getString("dx_code3"), 5)           //p38   5
                            + space(15)                                             //p39  15
                            + forwardSpace(rs2.getString("service_location"), 1)    //p40   1        
                            + forwardZero(rs2.getString("referral_flag1"), 1)       //p41   1
                            + forwardZero(rs2.getString("referral_no1"),5)          //p42   5
                            + forwardZero(rs2.getString("referral_flag2"),1)        //p44   1
                            + forwardZero(rs2.getString("referral_no2"),5)          //p46   5
                            + forwardZero(rs2.getString("time_call"),4)             //p47   4
                            + forwardZero(rs2.getString("service_start_time"),4)    //p48   4 
                            + forwardZero(rs2.getString("service_end_time"),4)      //p50   4
                            + forwardZero(rs2.getString("birth_date"),8)            //p52   8
                            + forwardZero(rs2.getString("billingmaster_no"), 7)     //p54   7
                            + forwardSpace(rs2.getString("correspondence_code"), 1) //p56   1
                            + space(20)                                             //p58  20 
                            + forwardSpace(rs2.getString("mva_claim_code"),1)       //p60   1                      
                            + forwardZero(rs2.getString("icbc_claim_no"), 8)        //p62   8   
                            + forwardZero(rs2.getString("original_claim"), 20 )     //p64  20
                            + forwardZero(rs2.getString("facility_no"), 5)          //p70   5
                            + forwardZero(rs2.getString("facility_sub_no"), 5)      //p72   5
                            + space(58)                                             //p80  58
                            + backwardSpace(rs2.getString("oin_insurer_code"),2)    //p100  2
                            + backwardSpace(rs2.getString("oin_registration_no"),12)//p102 12 
                            + backwardSpace(rs2.getString("oin_birthdate"),8)       //p104  8
                            + backwardSpace(rs2.getString("oin_first_name"),12)     //p106 12
                            + backwardSpace(rs2.getString("oin_second_name"),1)     //p108  1
                            + backwardSpace(rs2.getString("oin_surname"),18)        //p110 18
                            + backwardSpace(rs2.getString("oin_sex_code"),1)        //p112  1
                            + backwardSpace(rs2.getString("oin_address"),25)        //p114 25
                            + backwardSpace(rs2.getString("oin_address2"),25)       //p116 25
                            + backwardSpace(rs2.getString("oin_address3"),25)       //p118 25
                            + backwardSpace(rs2.getString("oin_address4"),25)       //p120 25
                            + backwardSpace(rs2.getString("oin_postalcode"),6);     //p122  6
        return dataLine;
    }
}
