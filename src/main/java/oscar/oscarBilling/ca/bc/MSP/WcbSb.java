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
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.Misc;
import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;

/**
 * @author Jef King
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class WcbSb {
    private static Logger logger=MiscUtils.getLogger(); 

   protected static String file = null;
   private String demographic_no,
   provider_no,
   formCreated,
   formEdited,
   bill_amount,
   visit_type,
   billing_no,
   invoice_no,
   w_reporttype,
   w_fname,
   w_lname,
   w_mname,
   w_gender,
   w_dob,
   w_doi,
   w_address,
   w_city,
   w_postalcode,
   w_area,
   w_phone,
   w_phn,
   w_empname,
   w_emparea,
   w_empphone,
   w_wcbno,
   w_opaddress,
   w_opcity,
   w_rphysician,
   w_duration,
   w_ftreatment,
   w_problem,
   w_servicedate,
   w_diagnosis,
   w_icd9,
   w_bp,
   w_side,
   w_noi,
   w_work,
   w_workdate,
   w_clinicinfo,
   w_capability,
   w_capreason,
   w_estimate,
   w_rehab,
   w_rehabtype,
   w_estimatedate,
   w_tofollow,
   w_payeeno,
   w_pracno,
   w_wcbadvisor,
   w_feeitem,
   w_extrafeeitem,
   w_servicelocation,
   billamountforfeeitem1,
   billamountforfeeitem2,
   formNeeded;
   
   String newLine ="\r\n";
   
   public WcbSb(String billingNo){
       try{
           
           
           String sql = "SELECT *, billingservice.value As `feeitem1` FROM  wcb JOIN billing ON wcb.billing_no=billing.billing_no left join billingservice on wcb.w_feeitem=billingservice.service_code "
            +"WHERE wcb.billing_no='"+ billingNo + "' AND wcb.status='O' AND billing.status IN ('O', 'W') ";
           
           ResultSet rs =
           DBHandler.GetSQL(sql);
           if (rs.next()){
              fillWithRs(rs);
           }
           rs.close();
           //TODO: what to do if the result is empty?? 
       }catch (Exception e){}
   }
   
   private void fillWithRs(java.sql.ResultSet rs){
       try {
         this.bill_amount = rs.getString("wcb.bill_amount");
         this.visit_type = rs.getString("w_servicelocation");
         this.billing_no = getBillingMasterNo(rs.getString("billing_no"));
         this.invoice_no = rs.getString("billing_no");
         this.demographic_no = rs.getString("demographic_no");
         this.provider_no = rs.getString("provider_no");
         this.formCreated = rs.getString("formCreated");
         this.formEdited = rs.getString("formEdited");
         this.w_reporttype = rs.getString("w_reporttype");
         this.w_fname = rs.getString("w_fname");
         this.w_lname = rs.getString("w_lname");
         this.w_mname = rs.getString("w_mname");
         this.w_gender = rs.getString("w_gender");
         this.w_dob = rs.getString("w_dob");
         this.w_doi = rs.getString("w_doi");
         this.w_address = rs.getString("w_address");
         this.w_city = rs.getString("w_city");
         this.w_postalcode = rs.getString("w_postal");
         this.w_area = rs.getString("w_area");
         this.w_phone = rs.getString("w_phone");
         this.w_phn = rs.getString("w_phn");
         this.w_empname = rs.getString("w_empname");
         this.w_emparea = rs.getString("w_emparea");
         this.w_empphone = rs.getString("w_empphone");
         this.w_wcbno = rs.getString("w_wcbno");
         this.w_opaddress = rs.getString("w_opaddress");
         this.w_opcity = rs.getString("w_opcity");
         this.w_rphysician = rs.getString("w_rphysician");
         this.w_duration = rs.getString("w_duration");
         this.w_ftreatment = this.StripLineBreaks(rs.getString("w_ftreatment"));
         this.w_problem = this.StripLineBreaks(rs.getString("w_problem"));
         this.w_servicedate = rs.getString("w_servicedate");
         this.w_diagnosis = this.StripLineBreaks(rs.getString("w_diagnosis"));
         this.w_icd9 = rs.getString("w_icd9");
         this.w_bp = rs.getString("w_bp");
         this.w_side = rs.getString("w_side");
         this.w_noi = rs.getString("w_noi");
         this.w_work = rs.getString("w_work");
         this.w_workdate = rs.getString("w_workdate");
         this.w_clinicinfo = this.StripLineBreaks(rs.getString("w_clinicinfo"));
         this.w_capability = rs.getString("w_capability");
         this.w_capreason = this.StripLineBreaks(rs.getString("w_capreason"));
         this.w_estimate = rs.getString("w_estimate");
         this.w_rehab = rs.getString("w_rehab");
         this.w_rehabtype = rs.getString("w_rehabtype");
         this.w_estimatedate = rs.getString("w_estimatedate");
         this.w_tofollow = rs.getString("w_tofollow");
         this.w_payeeno = rs.getString("w_payeeno");
         this.w_pracno = rs.getString("w_pracno");
         this.w_wcbadvisor = rs.getString("w_wcbadvisor");
         this.w_feeitem = rs.getString("w_feeitem");
         this.w_extrafeeitem = rs.getString("w_extrafeeitem");
         this.w_servicelocation = rs.getString("w_servicelocation");
         this.billamountforfeeitem1 = rs.getString("feeitem1");
         this.formNeeded = rs.getString("formNeeded");
      }
      catch (Exception ex) {
    	  logger.error("WcbSb (Constructor): ",ex);
      }
       
   }
  
   
   
   public String validate(){
        StringBuilder m = new StringBuilder();
        if ( billamountforfeeitem1 == null || billamountforfeeitem1.equalsIgnoreCase("NULL")){
            m.append(": INVALID Fee Code "); 
        }
        
        try {
            Integer.parseInt(this.w_icd9);
        }catch(Exception e){
            m.append(": ICD9 may only contain Numbers ");
        }
        
        try {
            Integer.parseInt(this.w_wcbno);
        }catch(Exception e){
            m.append(": WCB claim # may only contain Numbers ");
        }
        
        if (this.w_reporttype != null && this.w_reporttype.equals("F") ){
            if (this.w_empname != null && this.w_empname.trim().length() == 0 ){
                m.append(": Employer's name can not be empty ");
            }
            
            if (this.w_opaddress != null && this.w_opaddress.trim().length() == 0 ){
                m.append(": Employer's Operation Address can not be empty ");
            }
            
            if (this.w_opcity != null && this.w_opcity.trim().length() == 0 ){
                m.append(": Employer's Operation City can not be empty ");
            }
             
            if (this.w_empphone != null && this.w_empphone.trim().length() == 0 ){
                m.append(": Employer's Phone # can not be empty ");
            }
        }    
        
//        if (this.w_doi != null && this.w_doi.trim().length() == 0 ){
//            m.append(": Date of Injury is missing ");
//        }
//       
//        w_bp        
//        w_noi
        
       
        String ret = "<tr bgcolor='red'><td colspan='11'>"
                + "<a href='#' onClick=\"openBrWindow('billingTeleplanCorrectionWCB.jsp?billing_no="
                + Misc.forwardZero(this.billing_no, 7) 
                + "','','resizable=yes,scrollbars=yes,top=0,left=0,width=900,height=600'); return false;\">"
                + m.toString() + "</a>" + "</td></tr>";
        if ("".equals(m.toString())){
            return "" ;
        }      
        return ret;
   }
   
   public BigDecimal getBillingAmountForFee1BigDecimal(){
     BigDecimal bdFee= null;
     try{
        double dFee = Double.parseDouble(billamountforfeeitem1);
        bdFee = new BigDecimal(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
     }catch(Exception e){
         bdFee = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP);
     }
     return bdFee;
  }
   
   public WcbSb(java.sql.ResultSet rs) {
      fillWithRs(rs);
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
   public String getBillingMasterNo(String invNo){
      String retval = invNo;
      try{
                        
         ResultSet rs = DBHandler.GetSQL("SELECT billingmaster_no FROM billingmaster WHERE billing_no="+ invNo);
         if (rs.next()) {
            retval = rs.getString("billingmaster_no");
         }
      }catch(Exception e){
         retval = invNo;
         MiscUtils.getLogger().debug("PROBLEM SENDING WCB: Could not find billing master no for billing_no:"+invNo+" sending bill with that invNo");
         MiscUtils.getLogger().error("Error", e);         
      }
      return retval;      
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
      (Misc.forwardZero("D1", 4)
      + Misc.backwardSpace(this.w_rphysician, 1)
      + Misc.space(8)
      + Misc.backwardSpace(this.w_capability, 1)
      + Misc.backwardSpace(this.w_rehabtype, 1)
      + Misc.backwardSpace(this.w_estimate, 1)
      + Misc.backwardSpace(this.w_work, 1)
      + Misc.backwardSpace(this.w_tofollow, 1)
      + dateFormat(this.w_estimatedate)
      + dateFormat(this.w_workdate)
      + Misc.backwardSpace(this.w_duration, 1)
      + Misc.backwardSpace(this.w_ftreatment, 25)
      + Misc.backwardSpace(this.w_diagnosis, 120)
      + Misc.backwardSpace(this.w_wcbadvisor, 1)
      + Misc.backwardSpace(this.w_rehab, 1)
      + Misc.forwardZero(Misc.cleanNumber(this.w_area), 3)
      + Misc.forwardZero(Misc.cleanNumber(this.w_phone), 7)
      + Misc.backwardSpace(this.w_address, 25)
      + Misc.backwardSpace(this.w_city, 20)
      + Misc.backwardSpace(this.w_postalcode, 6)
      + Misc.forwardZero(Misc.cleanNumber(this.w_emparea), 3)
      + Misc.forwardZero(Misc.cleanNumber(this.w_empphone), 7)
      + Misc.backwardSpace(this.w_empname, 25)
      + Misc.backwardSpace(this.w_opaddress, 25)
      + Misc.backwardSpace(this.w_opcity, 25)
      + ((this.w_reporttype.compareTo("F") == 0) ? Misc.backwardSpace("Y", 2) : Misc.forwardSpace("Y", 2)) + Misc.space(70)));
   }
   private String Claim2(String logNo) {
      return this.Claim(logNo, "0", "19333", "N");
   }
   private String Note2(String logNo) {
      return this.Note(logNo,
            Misc.backwardSpace( ((this.w_problem.compareTo("") == 0) ? "Intentionally left blank" : this.w_problem),160),Misc.backwardSpace(this.w_capreason, 240));
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
      return this.Note(logNo,Misc.backwardSpace(((length >= 400)? this.w_clinicinfo.substring(400, length): "Clinical Information Complete"),400));
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
   
   
    
    
    
   private String Claim(String logNo, String billedAmount, String feeitem,String correspondenceCode) {
      return "C02"
      + this.ClaimNote1Head(logNo)
      + Misc.forwardZero("",10)
      //+ Misc.zero(10)      //phn
      + "0" //Misc.backwardSpace("", 1).toUpperCase()
      + "0" //Misc.space(1)
      + "00"//Misc.backwardSpace("", 2).toUpperCase()
      + Misc.zero(2)
      + Misc.forwardZero("1", 3)
      + Misc.zero(2 + 2 + 1 + 2)  //clarification
      + Misc.forwardZero(feeitem, 5)
      + Misc.moneyFormatPaddedZeroNoDecimal(billedAmount, 7)
      + Misc.zero(1)
      + dateFormat(this.w_servicedate)
      + Misc.zero(2)
      + "W"//Misc.zero(1) //Submission Code
      + Misc.space(1)
      + Misc.forwardZero(this.w_icd9, 5)
      + Misc.space(5 + 5 + 15)
      + Misc.backwardSpace(this.visit_type, 1)
      + Misc.zero(1 + 5 + 1 + 5 + 4 + 4 + 4 + 8)
      + Misc.forwardZero(this.billing_no, 7)
      + Misc.forwardZero(correspondenceCode, 1) //correspondence code//+ Misc.zero(1)
      + Misc.space(20)
      + "N"
      + Misc.zero(8 + 20 + 5 + 5)
      + Misc.space(58) //Part II of Claim 1
      +"WC"
      + Misc.backwardZero(this.w_phn, 12)
      + dateFormat(this.w_dob)
      + Misc.backwardSpace(this.w_fname, 12)
      + Misc.backwardSpace(this.w_mname, 1)
      + Misc.backwardSpace(this.w_lname, 18)
      + Misc.backwardSpace(this.w_gender, 1)
      + Misc.backwardSpace(dateFormat(this.w_doi), 25)
      + Misc.backwardSpace(Misc.backwardSpace(w_bp, 5) + Misc.backwardSpace(w_side, 2),25)
      + Misc.backwardSpace(this.w_noi, 25)
      + Misc.backwardSpace(this.w_wcbno, 25)
      + Misc.space(6);
   }
   private String ClaimNote1Head(String logNo) {
      return Misc.forwardZero(OscarProperties.getInstance().getProperty("dataCenterId"),5)
      + Misc.forwardZero(String.valueOf(logNo), 7)
      + Misc.forwardZero(this.w_payeeno, 5)
      + Misc.forwardZero(this.w_pracno, 5);
   }
   
   public  String dateFormat(String date){
      return Misc.forwardZero(oscar.Misc.cleanNumber(date), 8);
   }
   
   public String getHtmlLine(){
      return getHtmlLine(this.billing_no,this.invoice_no,this.w_lname+","+this.w_fname,this.w_phn,this.w_servicedate,this.w_feeitem,this.billamountforfeeitem1,this.w_icd9,"","");

      
   }
   public String getHtmlLine(String billingMasterNo,String invNo, String demoName, String phn, String serviceDate,String billingCode,String billAmount,String dx1,String dx2,String dx3){                           
       String htmlContent = 
       "<tr>" +
          "<td class='bodytext'>" +
             "<a href='#' onClick=\"openBrWindow('billingTeleplanCorrectionWCB.jsp?billing_no=" +
                Misc.forwardZero(billingMasterNo, 7) +
                 "','','resizable=yes,scrollbars=yes,top=0,left=0,width=900,height=600'); return false;\">"  +
               invNo + 
             "</a>" + 
          "</td>" +
          "<td class='bodytext'>" + demoName    + "</td>" +
          "<td class='bodytext'>" + w_phn         + "</td>" +
          "<td class='bodytext'>" + dateFormat(serviceDate) + "</td>" +
          "<td class='bodytext'>" + billingCode + "</td>" +
          "<td align='right' class='bodytext'>"+ billAmount +"</td>" +
          "<td align='right' class='bodytext'>"+ Misc.backwardSpace(dx1, 5) + "</td>" +
          "<td align='right' class='bodytext'>"+ Misc.backwardSpace(dx2, 5) + "</td>" +
          "<td align='right' class='bodytext'>"+ Misc.backwardSpace(dx3, 5) + "</td>" +
          "<td class='bodytext'>" + Misc.forwardZero(billingMasterNo, 7)+"</td>" +
          "<td class='bodytext'>&nbsp;</td>" +
       "</tr>";              
       return htmlContent;
    }
   boolean isFormNeeded(){
      boolean retval = true;
      if (formNeeded != null && formNeeded.equals("0")){
         retval = false;
      }      
      return retval;
   }
}
