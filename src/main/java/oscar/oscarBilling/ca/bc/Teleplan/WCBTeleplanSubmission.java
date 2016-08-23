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


package oscar.oscarBilling.ca.bc.Teleplan;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.Misc;
import oscar.OscarProperties;
import oscar.entities.Billingmaster;
import oscar.entities.WCB;
import oscar.oscarBilling.ca.bc.MSP.TeleplanFileWriter;
/**
 *
 * @author jaygallagher
 */
public class WCBTeleplanSubmission {
    private static Logger log = MiscUtils.getLogger();
    
    private DemographicManager demographicManager = null;
    
    //Misc misc = new Misc();
    public String getHtmlLine(WCB wcb,Billingmaster bm) {
        log.debug("WCB "+wcb+ " BM "+bm);
        return getHtmlLine(""+bm.getBillingmasterNo(),""+bm.getBillingNo(), wcb.getW_lname() + "," + wcb.getW_fname(), wcb.getW_phn(),dateFormat(wcb.getW_servicedate()), bm.getBillingCode(), bm.getBillAmount(), bm.getDxCode1(), "", "");


    }

    public String getHtmlLine(String billingMasterNo, String invNo, String demoName, String phn, String serviceDate, String billingCode, String billAmount, String dx1, String dx2, String dx3) {
        String htmlContent =
                "<tr>" +
                "<td class='bodytext'>" +
                "<a href='#' onClick=\"openBrWindow('adjustBill.jsp?billing_no=" +
                Misc.forwardZero(billingMasterNo, 7) +
                "','','resizable=yes,scrollbars=yes,top=0,left=0,width=900,height=600'); return false;\">" +
                invNo +
                "</a>" +
                "</td>" +
                "<td class='bodytext'>" + demoName + "</td>" +
                "<td class='bodytext'>" + phn + "</td>" +
                "<td class='bodytext'>" + dateFormat(serviceDate) + "</td>" +
                "<td class='bodytext'>" + billingCode + "</td>" +
                "<td align='right' class='bodytext'>" + billAmount + "</td>" +
                "<td align='right' class='bodytext'>" + Misc.backwardSpace(dx1, 5) + "</td>" +
                "<td align='right' class='bodytext'>" + Misc.backwardSpace(dx2, 5) + "</td>" +
                "<td align='right' class='bodytext'>" + Misc.backwardSpace(dx3, 5) + "</td>" +
                "<td class='bodytext'>" + Misc.forwardZero(billingMasterNo, 7) + "</td>" +
                "<td class='bodytext'>&nbsp;</td>" +
                "</tr>";
        return htmlContent;
    }
    

    
    
    public boolean isFormNeeded(Billingmaster bm){
       return WCBCodes.getInstance().isFormNeeded(bm.getBillingCode());
    }
    
    public  String dateFormat(String date){
       return Misc.forwardZero(oscar.Misc.cleanNumber(date), 8);
    }
    
    
    public String validate(WCB wcb,Billingmaster bm){
        StringBuilder m = new StringBuilder();
        
        try {
            Integer.parseInt(bm.getDxCode1() );
        }catch(Exception e){
            m.append(": ICD9 may only contain Numbers ");
        }


        if (wcb.getW_wcbno() != null && !wcb.getW_wcbno().trim().equals("")){
            try {
                Integer.parseInt(wcb.getW_wcbno());
            }catch(Exception e){
                m.append(": WCB claim # may only contain Numbers ");
            }
        }
        
        if (wcb.getW_reporttype() != null && wcb.getW_reporttype().equals("F") ){
            if (wcb.getW_empname() != null && wcb.getW_empname().trim().length() == 0 ){
                m.append(": Employer's name can not be empty ");
            }
            
            if (wcb.getW_opaddress() != null && wcb.getW_opaddress().trim().length() == 0 ){
                m.append(": Employer's Operation Address can not be empty ");
            }
            
            if (wcb.getW_opcity() != null && wcb.getW_opcity().trim().length() == 0 ){
                m.append(": Employer's Operation City can not be empty ");
            }
             
            if (wcb.getW_empphone() != null && wcb.getW_empphone().trim().length() == 0 ){
                m.append(": Employer's Phone # can not be empty ");
            }
        }    
        

       
        String ret = "<tr bgcolor='red'><td colspan='11'>"
                + "<a href='#' onClick=\"openBrWindow('adjustBill.jsp?billing_no="
                + Misc.forwardZero(""+bm.getBillingmasterNo(), 7)
                + "','','resizable=yes,scrollbars=yes,top=0,left=0,width=900,height=600'); return false;\">"
                + m.toString() + "</a>" + "</td></tr>";
        if ("".equals(m.toString())){
            return "" ;
        }      
        return ret;
   }
    
 
   
    public String Line1(LoggedInInfo loggedInInfo, String dsn,Billingmaster bm,WCB wcb) {
      return this.Claim1(loggedInInfo, dsn, bm, wcb);
   }
   public String Line2(String dsn,Billingmaster bm,WCB wcb) {
      return this.Note1(dsn, bm, wcb);
   }
   public String Line3(LoggedInInfo loggedInInfo, String dsn,Billingmaster bm,WCB wcb) {
      return this.Claim2(loggedInInfo, dsn, bm, wcb);
   }
   public String Line4(String dsn,Billingmaster bm,WCB wcb) {
      return this.Note2(dsn, bm, wcb);
   }
   public String Line5(LoggedInInfo loggedInInfo, String dsn,Billingmaster bm,WCB wcb) {
      return this.Claim3(loggedInInfo, dsn, bm, wcb);
   }
   public String Line6(String dsn,Billingmaster bm,WCB wcb) {
      return this.Note3(dsn, bm, wcb);
   }
   public String Line7(LoggedInInfo loggedInInfo, String dsn,Billingmaster bm,WCB wcb) {
      return this.Claim4(loggedInInfo, dsn, bm, wcb);
   }
   public String Line8(String dsn,Billingmaster bm,WCB wcb) {
      return this.Note4(dsn, bm, wcb);
   }
   public String Line9(LoggedInInfo loggedInInfo, String dsn,Billingmaster bm,WCB wcb) {
      return this.Claim5(loggedInInfo, dsn, bm, wcb);
   }
   private String Claim1(LoggedInInfo loggedInInfo, String logNo,Billingmaster bm,WCB wcb) {
      return this.Claim(loggedInInfo, logNo, bm.getBillAmount(), bm.getBillingCode(),TeleplanFileWriter.roundUp(bm.getBillingUnit()), "N", bm, wcb);
   }


  private String replaceExtendedAskiiValues(String s){
       log.debug("s "+s.length());
       StringBuilder sb = new StringBuilder();
       for (int i =0; i < s.length(); i++){
           char c = s.charAt(i);
           int j = c;
           MiscUtils.getLogger().debug(j+" : "+c);
           if(j < 32 || j > 126){
              c = '?';
           }
           sb.append(c);

       }
       log.debug("sb "+sb.toString().length());
       return sb.toString();
   }

     
   private String Note1(String logNo,Billingmaster bm,WCB wcb) {
      
      return this.Note(
      logNo,
      (Misc.forwardZero("D1", 4)
      + Misc.backwardSpace(wcb.getW_rphysician(), 1)
      + Misc.space(8)
      + Misc.backwardSpace(wcb.getW_capability(), 1)
      + Misc.backwardSpace(wcb.getW_rehabtype(), 1)
      + Misc.backwardSpace(wcb.getW_estimate(), 1)
      + Misc.backwardSpace(wcb.getW_work(), 1)
      + Misc.backwardSpace(wcb.getW_tofollow(), 1)
      + dateFormat(wcb.getW_estimatedate())
      + dateFormat(wcb.getW_workdate())
      + Misc.backwardSpace(""+wcb.getW_duration(), 1)
      + Misc.backwardSpace(wcb.getW_ftreatment(), 25)
      + Misc.backwardSpace(wcb.getW_diagnosis(), 120)
      + Misc.backwardSpace(wcb.getW_wcbadvisor(), 1)
      + Misc.backwardSpace(wcb.getW_rehab(), 1)
      + Misc.forwardZero(Misc.cleanNumber(wcb.getW_area()), 3)
      + Misc.forwardZero(Misc.cleanNumber(wcb.getW_phone()), 7)
      + Misc.backwardSpace(wcb.getW_address(), 25)
      + Misc.backwardSpace(wcb.getW_city(), 20)
      + Misc.backwardSpace(wcb.getW_postal(), 6)
      + Misc.forwardZero(Misc.cleanNumber(wcb.getW_emparea()), 3)
      + Misc.forwardZero(Misc.cleanNumber(wcb.getW_empphone()), 7)
      + Misc.backwardSpace(wcb.getW_empname(), 25)
      + Misc.backwardSpace(wcb.getW_opaddress(), 25)
      + Misc.backwardSpace(wcb.getW_opcity(), 25)
      + ((wcb.getW_reporttype().compareTo("F") == 0) ? Misc.backwardSpace("Y", 2) : Misc.forwardSpace("Y", 2)) + Misc.space(70)),bm,wcb);
   }
   private String Claim2(LoggedInInfo loggedInInfo, String logNo,Billingmaster bm,WCB wcb) {
      return this.Claim(loggedInInfo, logNo, "0", "19333", "N",bm,wcb);
   }
   private String Note2(String logNo,Billingmaster bm,WCB wcb) {
      return this.Note(logNo,
            Misc.backwardSpace( ((wcb.getW_problem().compareTo("") == 0) ? "Intentionally left blank" : wcb.getW_problem()),160),Misc.backwardSpace(wcb.getW_capreason(), 240),bm,wcb);
   }
   private String Claim3(LoggedInInfo loggedInInfo, String logNo,Billingmaster bm,WCB wcb) {
      return this.Claim(loggedInInfo, logNo, "0", "19334", "N",bm,wcb);
   }
   private String Note3(String logNo,Billingmaster bm,WCB wcb) {
      return this.Note(logNo, Misc.backwardSpace(wcb.getW_clinicinfo(), 400),bm,wcb);
   }
   private String Claim4(LoggedInInfo loggedInInfo, String logNo,Billingmaster bm,WCB wcb) {
      return this.Claim(loggedInInfo, logNo, "0", "19335", "N",bm,wcb);
   }
   private String Note4(String logNo,Billingmaster bm,WCB wcb) {
      int length = wcb.getW_clinicinfo().length();
      return this.Note(logNo,Misc.backwardSpace(((length >= 400)? wcb.getW_clinicinfo().substring(400, length): "Clinical Information Complete"),400),bm,wcb);
   }
   private String Claim5(LoggedInInfo loggedInInfo, String logNo,Billingmaster bm,WCB wcb) {      
      return this.Claim(loggedInInfo,logNo, bm.getBillAmount(), bm.getBillingCode() ,TeleplanFileWriter.roundUp(bm.getBillingUnit()), "0",bm,wcb);
   }
   private String Note(String logNo, String a,Billingmaster bm,WCB wcb) {
      return this.Note(logNo, a, "",bm,wcb);
   }
   private String Note(String logNo, String a, String b,Billingmaster bm,WCB wcb) {
      return "N01" + this.ClaimNote1Head(logNo,bm.getPayeeNo(),bm.getPractitionerNo()) + "W" + replaceExtendedAskiiValues(a) + replaceExtendedAskiiValues(b);
   }
   
   
   String dateFormat(Date date){
      Format formatterDate = new SimpleDateFormat("yyyyMMdd");
      log.debug("DATE try to convert "+date);
      if (date == null){
          log.error("DATE CAN NOT BE NULL FOR WCB CLAIM");
          return "00000000";
      }
      return formatterDate.format(date);
   }

   
   private String Claim(LoggedInInfo loggedInInfo, String logNo, String billedAmount, String feeitem,String correspondenceCode,Billingmaster bm,WCB wcb) {
	   String billingUnit = "1";
	   return Claim(loggedInInfo, logNo, billedAmount,  feeitem, billingUnit,correspondenceCode, bm,wcb) ;
   }
   private String Claim(LoggedInInfo loggedInInfo, String logNo, String billedAmount, String feeitem,String billingUnit,String correspondenceCode,Billingmaster bm,WCB wcb) {
      StringBuilder dLine = new StringBuilder();
     
      Demographic d = demographicManager.getDemographic(loggedInInfo, ""+bm.getDemographicNo()); 
      
      dLine.append("C02");
      dLine.append( this.ClaimNote1Head(logNo,bm.getPayeeNo(),bm.getPractitionerNo()) );
      dLine.append( Misc.forwardZero("",10));
      //+ Misc.zero(10)      //phn
      dLine.append( "0" );//Misc.backwardSpace("", 1).toUpperCase()
      dLine.append( "0" );//Misc.space(1)
      dLine.append( "00");//Misc.backwardSpace("", 2).toUpperCase()
      dLine.append( Misc.zero(2));
      dLine.append( Misc.forwardZero(billingUnit, 3)); // SR#: 7153 v1.9 Section 6.1.1 SEQ P20
      dLine.append( Misc.zero(2 + 2 + 1 + 2) ); //clarification
      dLine.append( Misc.forwardZero(feeitem, 5));
      dLine.append( Misc.moneyFormatPaddedZeroNoDecimal(billedAmount, 7));
      dLine.append( Misc.zero(1));
      dLine.append( dateFormat(bm.getServiceDate()));
      dLine.append( Misc.zero(2));
      dLine.append( "W");//Misc.zero(1) //Submission Code
      dLine.append( Misc.space(1));
      dLine.append( Misc.forwardZero(bm.getDxCode1(), 5));
      dLine.append( Misc.space(5 + 5 + 15));
      dLine.append( Misc.backwardSpace(bm.getServiceLocation(),1));// wcb.getW_servicelocation(), 1)

      dLine.append(Misc.forwardZero(bm.getReferralFlag1(), 1));                   //p41   1
      dLine.append(Misc.forwardZero(bm.getReferralNo1(),5));                      //p42   5
      dLine.append(Misc.forwardZero(bm.getReferralFlag2(),1));                    //p44   1
      dLine.append(Misc.forwardZero(bm.getReferralNo2(),5));                      //p46   5
      dLine.append(Misc.forwardZero(bm.getTimeCall(),4));                         //p47   4
      dLine.append(Misc.forwardZero(bm.getServiceStartTime(),4));                 //p48   4
      dLine.append(Misc.forwardZero(bm.getServiceEndTime(),4));



      dLine.append( Misc.zero(8));  //
      dLine.append( Misc.forwardZero(""+bm.getBillingmasterNo(), 7));
      dLine.append( Misc.forwardZero(correspondenceCode, 1)); //correspondence code//+ Misc.zero(1)
      dLine.append( Misc.space(20));
      dLine.append( "N");
      dLine.append( Misc.zero(8 + 20 + 5 + 5));
      dLine.append( Misc.space(58) );//Part II of Claim 1
      dLine.append("WC");
      //+ Misc.backwardZero(wcb.getW_phn(), 12)
      dLine.append( Misc.backwardZero(d.getHin(), 12));
      dLine.append( Misc.forwardZero(bm.getBirthDate(),8));
      //+ dateFormat(wcb.getW_dob())
      //+ Misc.backwardSpace(wcb.getW_fname(), 12)
      dLine.append( Misc.backwardSpace(d.getFirstName(), 12));
      dLine.append( Misc.backwardSpace(" ", 1));  // we don't store the middle name,  should i reference it from the WCB form??? or just send it as blank
      //+ Misc.backwardSpace(wcb.getW_mname(), 1)
      //+ Misc.backwardSpace(wcb.getW_lname(), 18)
      dLine.append( Misc.backwardSpace(d.getLastName(), 18));
              
      
      dLine.append( Misc.backwardSpace(d.getSex(), 1) );    //WHAT TO DO ABOUT TRANSGENDERED!!??
              
      //+ Misc.backwardSpace(wcb.getW_gender(), 1)
      dLine.append( Misc.backwardSpace(dateFormat(wcb.getW_doi()), 25));
      dLine.append( Misc.backwardSpace(Misc.backwardSpace(wcb.getW_bp(), 5) + Misc.backwardSpace(wcb.getW_side(), 2),25));
      dLine.append( Misc.backwardSpace(wcb.getW_noi(), 25));
      dLine.append( Misc.backwardSpace(wcb.getW_wcbno(), 25));
      dLine.append( Misc.space(6));
      return dLine.toString();
   }
   private String ClaimNote1Head(String logNo,String w_payeeno, String w_pracno) {
      return Misc.forwardZero(OscarProperties.getInstance().getProperty("dataCenterId"),5)
      + Misc.forwardZero(String.valueOf(logNo), 7)
      + Misc.forwardZero(w_payeeno, 5)
      + Misc.forwardZero(w_pracno, 5);
   } 
   
   
   public void setDemographicManager(
		   DemographicManager demographicManager) {
	   	this.demographicManager = demographicManager;
    }
  
   
    
}
