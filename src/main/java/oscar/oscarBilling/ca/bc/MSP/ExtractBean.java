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
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.Misc;
import oscar.OscarProperties;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilMisc;



public class ExtractBean extends Object implements Serializable {
    private static Logger logger=MiscUtils.getLogger();

    private String ohipRecord;
    private String ohipClaim;
    private String ohipReciprocal;
    private String ohipFilename;
    private String htmlFilename;
    private String value;
    private String query;
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



    public synchronized void dbQuery(){
        String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");
        if (HasBillingItemsToSubmit()) {
           try{

               if (vsFlag == 0) {
                   logNo =  getSequence() ;
                   batchHeader = "VS1" + dataCenterId + Misc.forwardZero(logNo,7) + "V6242" + "OSCAR_MCMASTER           " + "V1.1      " + "20030930" + "OSCAR MCMASTER                          " + "(905) 575-1300 " + Misc.space(25) + Misc.space(57) + "\r";
                   errorMsg = checkData.checkVS1("VS1" , dataCenterId , Misc.forwardZero(logNo,7) , "V6242" , "OSCAR_MCMASTER           " , "V1.1      " , "20030930" , "OSCAR MCMASTER                          " , "(905) 575-1300 " , Misc.space(25) , Misc.space(57));
                   logValue = batchHeader;
                   setLog(logNo, logValue);
                   vsFlag = 1;
               }
               else{
                   batchHeader = "";
               }

               htmlContentHeader = htmlContentHeaderGen(providerNo,output.substring(0,8),errorMsg);
               errorMsg = "";

               value = batchHeader;

               query = "select * from billing where provider_ohip_no='"+ providerNo+"' and (status='O' or status='W') " + dateRange;

               MiscUtils.getLogger().debug("1st billing query "+query);
               ResultSet rs = DBHandler.GetSQL(query);


               if (rs != null){

                   while(rs.next()) {
                     patientCount ++;
                     invNo = rs.getString("billing_no");
                     demoName = rs.getString("demographic_name");
                     String billType =  rs.getString("billingtype");
                     invCount = 0;

                     MiscUtils.getLogger().debug("Bill Type  : "+billType+" pt :"+patientCount);
                        if (billType.equals("MSP")  || billType.equals("ICBC") ) {
                           MiscUtils.getLogger().debug("Going to process a "+billType+" type bill invoice #"+invNo );

                           ResultSet rs2 = DBHandler.GetSQL("select * from billingmaster where billing_no='"+ invNo +"' and billingstatus='O'");
                              while (rs2.next()) {
                                 recordCount ++;

                                 logNo = getSequence();
                                 MiscUtils.getLogger().debug("processing "+invNo);

                                 String dataLine = getClaimDetailRecord(rs2,logNo);
                                 if (dataLine.length() != 424 ){ MiscUtils.getLogger().debug("dataLine2 "+logNo+" Len"+dataLine.length()); }

                                 value += "\n"+dataLine+"\r";
                                 logValue = dataLine;
                                 setLog(logNo,logValue);

                                 if (hasNoteRecord(rs2)){
                                    String noteLogNo = getSequence();
                                    String noteRecordLine = getNoteRecord(rs2,noteLogNo);
                                    value += "\n"+noteRecordLine+"\r";
                                    setLog(noteRecordLine,noteLogNo);
                                 }

                                 dFee = Double.parseDouble(rs2.getString("bill_amount"));
                                 bdFee = new BigDecimal(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
                                 BigTotal = BigTotal.add(bdFee);

                                 if (isMSPInsurer(rs2)){
                                    htmlContent += htmlLine(rs2.getString("billingmaster_no"),invNo,demoName, rs2.getString("phn"), rs2.getString("service_date"),rs2.getString("billing_code") ,rs2.getString("bill_amount"),rs2.getString("dx_code1"),rs2.getString("dx_code2"),rs2.getString("dx_code3"));
                                 }else{
                                    htmlContent += htmlLine(rs2.getString("billingmaster_no"),invNo,demoName, rs2.getString("oin_registration_no"), rs2.getString("service_date"),rs2.getString("billing_code") ,rs2.getString("bill_amount"),rs2.getString("dx_code1"),rs2.getString("dx_code2"),rs2.getString("dx_code3"));
                                 }



                                 errorMsg = checkData.checkC02(rs2.getString("billingmaster_no"), rs2);
                                 htmlContent += errorMsg;

                                 invCount++;
                                 setAsBilledMaster(rs2.getString("billingmaster_no"));


                              }// while
                        }else if (billType.equals("WCB")){
                           ResultSet rs2 =
                           DBHandler.GetSQL("SELECT *, billingservice.value As `feeitem1` FROM billingservice, wcb JOIN billing ON wcb.billing_no=billing.billing_no WHERE wcb.billing_no='"
                           + invNo + "' AND wcb.status='O' AND billing.status IN ('O', 'W') AND billingservice.service_code=wcb.w_feeitem");



                           if (rs2.next()) {

                              WcbSb sb = new WcbSb(rs2);
                              htmlContent += sb.getHtmlLine();
                              htmlContent += checkData.printWarningMsg("");

                              if (sb.isFormNeeded()){
                                 logNo = getSequence();
                                 String lines = sb.Line1(String.valueOf(logNo));
                                 value += "\n"+ lines +"\r";
                                 setLog(logNo, lines);

                                 logNo = getSequence();
                                 lines = sb.Line2(String.valueOf(logNo));
                                 value += "\n"+ lines +"\r";
                                 setLog(logNo, lines);

                                 logNo = getSequence();
                                 lines = sb.Line3(String.valueOf(logNo));
                                 value += "\n"+ lines +"\r";
                                 setLog(logNo, lines);

                                 logNo = getSequence();
                                 lines = sb.Line4(String.valueOf(logNo));
                                 value += "\n"+ lines +"\r";
                                 setLog(logNo, lines);

                                 logNo = getSequence();
                                 lines = sb.Line5(String.valueOf(logNo));
                                 value += "\n"+ lines +"\r";
                                 setLog(logNo, lines);

                                 logNo = getSequence();
                                 lines = sb.Line6(String.valueOf(logNo));
                                 value += "\n"+ lines +"\r";
                                 setLog(logNo, lines);

                                 logNo = getSequence();
                                 lines = sb.Line7(String.valueOf(logNo));
                                 value += "\n"+ lines +"\r";
                                 setLog(logNo, lines);

                                 logNo = getSequence();
                                 lines = sb.Line8(String.valueOf(logNo));
                                 value += "\n"+ lines +"\r";
                                 setLog(logNo, lines);
                              }else{

                                 logNo = getSequence();
                                 String lines = sb.Line9(String.valueOf(logNo));
                                 value += "\n"+ lines +"\r";
                                 setLog(logNo, lines);

                              }
                           }

                           rs2.close();
                           rs2 = DBHandler.GetSQL("SELECT billingmaster_no FROM billingmaster WHERE billing_no="+ invNo);
                           if (rs2.next()) {
                              setAsBilledMaster(rs2.getString("billingmaster_no"));
                           }
                           rs2.close();
                           /////////////////////////////////////
                        }
                        setAsBilled(invNo);
                   }

                   pCount = pCount + patientCount;
                   rCount = rCount + recordCount;

                   htmlFooter =  "<tr>    <td colspan='11' class='bodytext'>&nbsp;</td>  </tr>  <tr>    <td colspan='5' class='bodytext'>Billing No: "+ providerNo + ": "+ pCount +" RECORDS PROCESSED</td>    <td colspan='6' class='bodytext'>TOTAL: " +BigTotal +"</td>  </tr></table></body></html>";
                   htmlCode = htmlContentHeader + htmlContent + htmlFooter;

                   writeHtml(htmlCode);

                   ohipReciprocal = String.valueOf(hcCount);
                   ohipRecord = String.valueOf(rCount);
                   ohipClaim = String.valueOf(pCount);
               }
           }catch (SQLException e) {
               MiscUtils.getLogger().error("Error", e);
           }
        }
    }


    public void setAsBilled(String newInvNo){
      if (eFlag.equals("1")){
         String query30 = "update billing set status='B' where billing_no='" + newInvNo + "'";
         try {

        	 DBHandler.RunSQL(query30);
         }catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
         }
      }
    }

    public void setAsBilledMaster(String newInvNo){
      if (eFlag.equals("1")){
         String query30 = "update billingmaster set billingstatus='B' where billingmaster_no='" + newInvNo + "'";
         try {

        	 DBHandler.RunSQL(query30);
            createBillArchive(newInvNo);
         }catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
         }
      }
    }

    /**
     * Adds a new entry into the billing_history table
     * @param newInvNo String
     */
    private void createBillArchive(String newInvNo) {
      BillingHistoryDAO dao = new BillingHistoryDAO();
      dao.createBillingHistoryArchive(newInvNo);
    }

    public void setLog(String x, String logValue){
      if (eFlag.equals("1")){
         String nsql = "update log_teleplantx set claim='" + UtilMisc.mysqlEscape(logValue) + "' where log_no='"+ x +"'";
         try {

        	 DBHandler.RunSQL(nsql);
         }catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
         }
      }
    }


    public String getSequence(){
      String n="1";
      if (eFlag.equals("1")){
         String nsql ="";
         nsql =  "insert into log_teleplantx (log_no, claim) values ('\\N','" + "New Log" + "')";
         try {

        	 DBHandler.RunSQL(nsql);
            ResultSet  rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID()");
            if (rs.next()){
               n = rs.getString(1);
            }
            rs.close();
         }catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
         }
      }
      return n;
    }

    public void writeFile(String value1){
        try{
            String home_dir = OscarProperties.getInstance().getProperty("HOME_DIR");

            FileOutputStream out = new FileOutputStream(home_dir+ ohipFilename);
            PrintStream p = new PrintStream(out);
            p.println(value1);
            p.close();
        }catch(Exception e) {
            logger.error("Unexpected error", e);
        }
    }

    public void writeHtml(String htmlvalue1){
      if (eFlag.equals("1")) {
        try{
            String home_dir = OscarProperties.getInstance().getProperty("HOME_DIR");
            FileOutputStream out = new FileOutputStream(home_dir+htmlFilename);
            PrintStream p = new PrintStream(out);
            p.println(htmlvalue1);
            p.close();
        }catch(Exception e) {
            logger.error("Unexpected error", e);
        }
      }
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
       }catch(Exception e){
           logger.error("Unexpected error", e);}
       return retval;
    }
    public String getClaimDetailRecord(ResultSet rs2,String LogNo) throws SQLException{
        String dataLine =     Misc.forwardSpace(rs2.getString("claimcode"),3)            //p00   3
                            + Misc.forwardSpace(rs2.getString("datacenter"),5)           //p02   5
                            + Misc.forwardZero(logNo,7)                                  //p04   7
                            + Misc.forwardSpace(rs2.getString("payee_no"),5)             //p06   5
                            + Misc.forwardSpace(rs2.getString("practitioner_no"),5)      //p08   5
                            + Misc.forwardZero(rs2.getString("phn"),10)                  //p14  10
                            + Misc.forwardSpace(rs2.getString("name_verify"),4)          //p16   4
                            + Misc.forwardSpace(rs2.getString("dependent_num"),2)        //p18   2
                            + Misc.forwardZero(roundUp(rs2.getString("billing_unit")),3) //p20   3
                            + Misc.forwardZero(rs2.getString("clarification_code"),2)    //p22   2
                            + Misc.forwardSpace(rs2.getString("anatomical_area"), 2)     //p23   2
                            + Misc.forwardSpace(rs2.getString("after_hour"),1)           //p24   1
                            + Misc.forwardZero(rs2.getString("new_program"),2)           //p25   2
                            + Misc.forwardZero(rs2.getString("billing_code"),5)          //p26   5
                            + Misc.moneyFormatPaddedZeroNoDecimal(rs2.getString("bill_amount"),7)           //p27   7
                            + Misc.forwardZero(rs2.getString("payment_mode"), 1)         //p28   1
                            + Misc.forwardSpace(rs2.getString("service_date"), 8)        //p30   8
                            + Misc.forwardZero(rs2.getString("service_to_day"),2)        //p32   2
                            + Misc.forwardSpace(rs2.getString("submission_code"), 1)     //p34   1
                            + Misc.space(1)                                              //p35   1
                            + Misc.backwardSpace(rs2.getString("dx_code1"), 5)           //p36   5
                            + Misc.backwardSpace(rs2.getString("dx_code2"), 5)           //p37   5
                            + Misc.backwardSpace(rs2.getString("dx_code3"), 5)           //p38   5
                            + Misc.space(15)                                             //p39  15
                            + Misc.forwardSpace(rs2.getString("service_location"), 1)    //p40   1
                            + Misc.forwardZero(rs2.getString("referral_flag1"), 1)       //p41   1
                            + Misc.forwardZero(rs2.getString("referral_no1"),5)          //p42   5
                            + Misc.forwardZero(rs2.getString("referral_flag2"),1)        //p44   1
                            + Misc.forwardZero(rs2.getString("referral_no2"),5)          //p46   5
                            + Misc.forwardZero(rs2.getString("time_call"),4)             //p47   4
                            + Misc.forwardZero(rs2.getString("service_start_time"),4)    //p48   4
                            + Misc.forwardZero(rs2.getString("service_end_time"),4)      //p50   4
                            + Misc.forwardZero(rs2.getString("birth_date"),8)            //p52   8
                            + Misc.forwardZero(rs2.getString("billingmaster_no"), 7)     //p54   7
                            + Misc.forwardSpace(rs2.getString("correspondence_code"), 1) //p56   1
                            //+ misc.space(20)                                             //p58  20
                            + Misc.backwardSpace(rs2.getString("claim_comment"),20)      //p58  20
                            + Misc.forwardSpace(rs2.getString("mva_claim_code"),1)       //p60   1
                            + Misc.forwardZero(rs2.getString("icbc_claim_no"), 8)        //p62   8
                            + Misc.forwardZero(rs2.getString("original_claim"), 20 )     //p64  20
                            + Misc.forwardZero(rs2.getString("facility_no"), 5)          //p70   5
                            + Misc.forwardZero(rs2.getString("facility_sub_no"), 5)      //p72   5
                            + Misc.space(58)                                             //p80  58
                            + Misc.backwardSpace(rs2.getString("oin_insurer_code"),2)    //p100  2
                            + Misc.forwardZero(rs2.getString("oin_registration_no"),12)  //p102 12
                            + Misc.backwardSpace(rs2.getString("oin_birthdate"),8)       //p104  8
                            + Misc.backwardSpace(rs2.getString("oin_first_name"),12)     //p106 12
                            + Misc.backwardSpace(rs2.getString("oin_second_name"),1)     //p108  1
                            + Misc.backwardSpace(rs2.getString("oin_surname"),18)        //p110 18
                            + Misc.backwardSpace(rs2.getString("oin_sex_code"),1)        //p112  1
                            + Misc.backwardSpace(rs2.getString("oin_address"),25)        //p114 25
                            + Misc.backwardSpace(rs2.getString("oin_address2"),25)       //p116 25
                            + Misc.backwardSpace(rs2.getString("oin_address3"),25)       //p118 25
                            + Misc.backwardSpace(rs2.getString("oin_address4"),25)       //p120 25
                            + Misc.backwardSpace(rs2.getString("oin_postalcode"),6);     //p122  6
        return dataLine;
    }

    public boolean hasNoteRecord (ResultSet rs) {
       boolean retval = false;
       try{
          String correspondenceCode = rs.getString("correspondence_code");
          if (correspondenceCode.equals("N") || correspondenceCode.equals("B")){
             retval = true;
          }
       }catch(Exception e){
          retval = false;
          logger.error("Unexpected error", e);
       }
       return retval;
    }

    public String getNoteRecord(ResultSet rs, String seqNo) throws SQLException{
       MSPBillingNote note = new MSPBillingNote();
       return MSPBillingNote.getN01(rs.getString("datacenter"), seqNo, rs.getString("payee_no"), rs.getString("practitioner_no"), "A", note.getNote(rs.getString("billingmaster_no")));
    }

    public String htmlContentHeaderGen(String providerNo,String output,String errorMsg){
      htmlContentHeader = "<html><body><style type='text/css'><!-- .bodytext{  font-family: Tahoma, Arial, Helvetica, sans-serif;  font-size: 12px; font-style: normal;  line-height: normal;  font-weight: normal;  font-variant: normal;  text-transform: none;  color: #003366;  text-decoration: none; --></style>";
      htmlContentHeader +="<table width='100%' border='0' cellspacing='0' cellpadding='0'>";
      htmlContentHeader +="<tr>";
      htmlContentHeader +="<td colspan='4' class='bodytext'>Billing Invoice for Billing No."+ providerNo +"</td>";
      htmlContentHeader +="<td colspan='7' class='bodytext'>Payment date of " + output+ "</td>";
      htmlContentHeader +="</tr>";
      htmlContentHeader +="<tr>";
      htmlContentHeader +="<td width='9%' class='bodytext'>INVOICE</td>";
      htmlContentHeader +="<td width='19%' class='bodytext'>NAME</td>";
      htmlContentHeader +="<td width='12%' class='bodytext'>HEALTH #</td>";
      htmlContentHeader +="<td width='10%' class='bodytext'>BILLDATE</td>";
      htmlContentHeader +="<td width='8%' class='bodytext'>CODE</td>";
      htmlContentHeader +="<td width='14%' align='right' class='bodytext'>BILLED</td>";
      htmlContentHeader +="<td width='4%' align='right' class='bodytext'>DX</td>";
      htmlContentHeader +="<td width='5%' align='right' class='bodytext'>DX2</td>";
      htmlContentHeader +="<td width='6%' align='right' class='bodytext'>DX3</td>";
      htmlContentHeader +="<td width='8%' align='right' class='bodytext'>SEQUENCE</td>";
      htmlContentHeader +="<td width='5%' align='right' class='bodytext'>COMMENT</td>";
      htmlContentHeader +="</tr>";
      htmlContentHeader += errorMsg;
      return htmlContentHeader;
    }

    public String htmlLine(String billingMasterNo,String invNo, String demoName, String phn, String serviceDate,String billingCode,String billAmount,String dx1,String dx2,String dx3){
       String htmlContent =
       "<tr>" +
          "<td class='bodytext'>" +
             "<a href='#' onClick=\"openBrWindow('adjustBill.jsp?billing_no=" +
                Misc.forwardZero(billingMasterNo, 7) +
                 "','','resizable=yes,scrollbars=yes,top=0,left=0,width=900,height=600'); return false;\">"  +
               invNo +
             "</a>" +
          "</td>" +
          "<td class='bodytext'>" + demoName    + "</td>" +
          "<td class='bodytext'>" + phn         + "</td>" +
          "<td class='bodytext'>" + serviceDate + "</td>" +
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


    public static boolean HasBillingItemsToSubmit() {
      boolean tosubmit = false;
      try {

         ResultSet rs =
         DBHandler.GetSQL("SELECT COUNT(billing_no) As `count` FROM billing WHERE status <> 'B' AND billingtype IN ('ICBC', 'WCB', 'MSP')");
         tosubmit = rs.next() && 0 < rs.getInt("count");
         rs.close();
      }catch (Exception ex) {
          logger.error("Unexpected error", ex);
      }
      return tosubmit;
   }

    public boolean isMSPInsurer(ResultSet rs2) throws SQLException{
       boolean retval = true;
       String insurer = rs2.getString("oin_insurer_code");
       if ( insurer != null && (insurer.trim().length() > 0) ){
          retval = false;
       }
       return retval;
    }
}
