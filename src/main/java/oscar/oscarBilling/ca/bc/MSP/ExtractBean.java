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
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.BC.dao.LogTeleplanTxDao;
import org.oscarehr.billing.CA.BC.model.LogTeleplanTx;
import org.oscarehr.billing.CA.BC.model.Wcb;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.DateRange;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.Misc;
import oscar.OscarProperties;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.util.ConversionUtils;



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
    private DateRange dateRange = null;
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
               
               BillingDao dao = SpringUtils.getBean(BillingDao.class);
               BillingmasterDAO bmDao = SpringUtils.getBean(BillingmasterDAO.class);

            	   for(Billing b : dao.findByProviderStatusAndDates(providerNo, Arrays.asList(new String[] {"O", "W"}), dateRange)) {
                     patientCount ++;
                     invNo = b.getId().toString();
                     demoName = b.getDemographicName();
                     String billType =  b.getBillingtype();
                     invCount = 0;

                     if (billType.equals("MSP")  || billType.equals("ICBC") ) {
                           
                              for(Billingmaster bm : bmDao.getBillingMasterWithStatus(invNo, "O")) {
                                 recordCount ++;
                                 logNo = getSequence();
                                 
                                 String dataLine = getClaimDetailRecord(bm,logNo);
                                 if (dataLine.length() != 424 ){ MiscUtils.getLogger().debug("dataLine2 "+logNo+" Len"+dataLine.length()); }

                                 value += "\n"+dataLine+"\r";
                                 logValue = dataLine;
                                 setLog(logNo,logValue);

                                 if (hasNoteRecord(bm)){
                                    String noteLogNo = getSequence();
                                    String noteRecordLine = getNoteRecord(bm,noteLogNo);
                                    value += "\n"+noteRecordLine+"\r";
                                    setLog(noteRecordLine,noteLogNo);
                                 }

                                 dFee = bm.getBillAmountAsDouble();
                                 bdFee = BigDecimal.valueOf(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
                                 BigTotal = BigTotal.add(bdFee);

                                 if (isMSPInsurer(bm)){
                                    htmlContent += htmlLine("" + bm.getBillingmasterNo(),invNo,demoName, bm.getPhn(), bm.getServiceDate(),
                                    		bm.getBillingCode() ,bm.getBillAmount(),bm.getDxCode1(),
                                    		bm.getDxCode2(),bm.getDxCode3());
                                 }else{
                                    htmlContent += htmlLine("" + bm.getBillingmasterNo(), invNo, demoName, 
                                    		bm.getOinRegistrationNo(), 
                                    		bm.getServiceDate(),
                                    		bm.getBillingCode(),
                                    		bm.getBillAmount(),
                                    		bm.getDxCode1(),
                                    		bm.getDxCode2(),
                                    		bm.getDxCode3());
                                 }



                                 errorMsg = checkData.checkC02("" + bm.getBillingmasterNo(), bm);
                                 htmlContent += errorMsg;

                                 invCount++;
                                 setAsBilledMaster("" + bm.getBillingmasterNo());


                              }// while
                        }else if (billType.equals("WCB")){
                           BillingServiceDao bsDao = SpringUtils.getBean(BillingServiceDao.class);

                        	for(Object[] o : bsDao.findSomethingByBillingId(ConversionUtils.fromIntString(invNo))) {
                        		
                              WcbSb sb = new WcbSb((Wcb) o[1]);
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

                           Billingmaster bm = bmDao.getBillingmaster(ConversionUtils.fromIntString(invNo));
                           if (bm != null) {
                              setAsBilledMaster("" + bm.getBillingmasterNo());
                           }
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
    	  LogTeleplanTx l = this.logTeleplanTxDao.find(Integer.parseInt(x));
    	  if(l != null) {
    		  l.setClaim(logValue.getBytes());
    		  logTeleplanTxDao.merge(l);
    	  }
      }
    }


    public String getSequence(){
      String n="1";
      if (eFlag.equals("1")){
         LogTeleplanTx l = new LogTeleplanTx();
         l.setClaim("New Log".getBytes());
         logTeleplanTxDao.persist(l);
         return l.getId().toString();
         
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
    public synchronized void setDateRange(DateRange newDateRange){
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
    
    public String getClaimDetailRecord(Billingmaster bm, String LogNo) {
    	String dataLine =     Misc.forwardSpace(bm.getClaimcode(), 3)            //p00   3
                + Misc.forwardSpace(bm.getDatacenter(), 5)           //p02   5
                + Misc.forwardZero(logNo,7)                                  //p04   7
                + Misc.forwardSpace(bm.getPayeeNo(), 5)             //p06   5
                + Misc.forwardSpace(bm.getPractitionerNo(), 5)      //p08   5
                + Misc.forwardZero(bm.getPhn(), 10)                  //p14  10
                + Misc.forwardSpace(bm.getNameVerify(), 4)          //p16   4
                + Misc.forwardSpace(bm.getDependentNum(), 2)        //p18   2
                + Misc.forwardZero(roundUp(bm.getBillingUnit()),3) //p20   3
                + Misc.forwardZero(bm.getClarificationCode(), 2)    //p22   2
                + Misc.forwardSpace(bm.getAnatomicalArea(),  2)     //p23   2
                + Misc.forwardSpace(bm.getAfterHour(), 1)           //p24   1
                + Misc.forwardZero(bm.getNewProgram(), 2)           //p25   2
                + Misc.forwardZero(bm.getBillingCode(), 5)          //p26   5
                + Misc.moneyFormatPaddedZeroNoDecimal(bm.getBillAmount(), 7)           //p27   7
                + Misc.forwardZero(bm.getPaymentMode(),  1)         //p28   1
                + Misc.forwardSpace(bm.getServiceDate(),  8)        //p30   8
                + Misc.forwardZero(bm.getServiceToDay(), 2)        //p32   2
                + Misc.forwardSpace(bm.getSubmissionCode(),  1)     //p34   1
                + Misc.space(1)                                              //p35   1
                + Misc.backwardSpace(bm.getDxCode1(),  5)           //p36   5
                + Misc.backwardSpace(bm.getDxCode2(),  5)           //p37   5
                + Misc.backwardSpace(bm.getDxCode3(),  5)           //p38   5
                + Misc.space(15)                                             //p39  15
                + Misc.forwardSpace(bm.getServiceLocation(),  1)    //p40   1
                + Misc.forwardZero(bm.getReferralFlag1(),  1)       //p41   1
                + Misc.forwardZero(bm.getReferralNo1(), 5)          //p42   5
                + Misc.forwardZero(bm.getReferralFlag2(), 1)        //p44   1
                + Misc.forwardZero(bm.getReferralNo2(), 5)          //p46   5
                + Misc.forwardZero(bm.getTimeCall(), 4)             //p47   4
                + Misc.forwardZero(bm.getServiceStartTime(), 4)    //p48   4
                + Misc.forwardZero(bm.getServiceEndTime(), 4)      //p50   4
                + Misc.forwardZero(bm.getBirthDate(), 8)            //p52   8
                + Misc.forwardZero("" + bm.getBillingmasterNo(),  7)     //p54   7
                + Misc.forwardSpace(bm.getCorrespondenceCode(),  1) //p56   1
                //+ misc.space(20)                                             //p58  20
                + Misc.backwardSpace(bm.getClaimComment(), 20)      //p58  20
                + Misc.forwardSpace(bm.getMvaClaimCode(), 1)       //p60   1
                + Misc.forwardZero(bm.getIcbcClaimNo(),  8)        //p62   8
                + Misc.forwardZero(bm.getOriginalClaim(),  20 )     //p64  20
                + Misc.forwardZero(bm.getFacilityNo(),  5)          //p70   5
                + Misc.forwardZero(bm.getFacilitySubNo(),  5)      //p72   5
                + Misc.space(58)                                             //p80  58
                + Misc.backwardSpace(bm.getOinInsurerCode(), 2)    //p100  2
                + Misc.forwardZero(bm.getOinRegistrationNo(), 12)  //p102 12
                + Misc.backwardSpace(bm.getOinBirthdate(), 8)       //p104  8
                + Misc.backwardSpace(bm.getOinFirstName(), 12)     //p106 12
                + Misc.backwardSpace(bm.getOinSecondName(), 1)     //p108  1
                + Misc.backwardSpace(bm.getOinSurname(), 18)        //p110 18
                + Misc.backwardSpace(bm.getOinSexCode(), 1)        //p112  1
                + Misc.backwardSpace(bm.getOinAddress(), 25)        //p114 25
                + Misc.backwardSpace(bm.getOinAddress2(), 25)       //p116 25
                + Misc.backwardSpace(bm.getOinAddress3(), 25)       //p118 25
                + Misc.backwardSpace(bm.getOinAddress4(), 25)       //p120 25
                + Misc.backwardSpace(bm.getOinPostalcode(), 6);     //p122  6
    	return dataLine;
    }

    public boolean hasNoteRecord (Billingmaster bm) {
       boolean retval = false;
       try{
          String correspondenceCode = bm.getCorrespondenceCode();
          if (correspondenceCode.equals("N") || correspondenceCode.equals("B")){
             retval = true;
          }
       }catch(Exception e){
          retval = false;
          logger.error("Unexpected error", e);
       }
       return retval;
    }

    public String getNoteRecord(Billingmaster bm, String seqNo) {
       MSPBillingNote note = new MSPBillingNote();
       return MSPBillingNote.getN01(bm.getDatacenter(), seqNo, bm.getPayeeNo(), bm.getPractitionerNo(), "A", note.getNote("" + bm.getBillingmasterNo()));
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
      BillingDao dao = SpringUtils.getBean(BillingDao.class);
      return !dao.getMyMagicBillings().isEmpty();
   }

    public boolean isMSPInsurer(Billingmaster bm) {
       boolean retval = true;
       String insurer = bm.getOinInsurerCode();
       if ( insurer != null && (insurer.trim().length() > 0) ){
          retval = false;
       }
       return retval;
    }
}
