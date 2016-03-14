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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.Misc;
import oscar.entities.Billingmaster;
import oscar.entities.WCB;
import oscar.oscarBilling.ca.bc.Teleplan.TeleplanSequenceDAO;
import oscar.oscarBilling.ca.bc.Teleplan.WCBTeleplanSubmission;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author jay
 */
public class TeleplanFileWriter {
    
    
    private static Logger log = MiscUtils.getLogger();
    
    
    StringBuilder mspFileStr = null;
    StringBuilder mspHtmlStr = null;
    int sequenceNum = 0;
    ArrayList<String> billingToBeMarkedAsBilled = null;
    ArrayList billingmasterToBeMarkedAsBilled = null;
    private BigDecimal bigTotal = null;
    ArrayList logList = null;
    int totalClaims = 0;
    
    private BillingmasterDAO billingmasterDAO = null;
    private DemographicManager demographicManager = null;
    
    public CheckBillingData checkData = new CheckBillingData();
    
    /** Creates a new instance of TeleplanFileWriter */
    public TeleplanFileWriter() {
        mspFileStr = new StringBuilder();
        mspHtmlStr = new StringBuilder();
        sequenceNum = getLastSequenceNumber();
        billingToBeMarkedAsBilled = new ArrayList<String>();
        billingmasterToBeMarkedAsBilled = new ArrayList();
        bigTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        logList = new ArrayList();
    }
    
    private void addToTotal(BigDecimal bd){
        bigTotal = bigTotal.add(bd);
    }
    
    private void addToMarkBillingmasterList(String billingmasterNo){
        billingmasterToBeMarkedAsBilled.add(billingmasterNo);
    }
    
    private void addToMarkBillingList(String billingNo){
        billingToBeMarkedAsBilled.add(billingNo);
    }
    /*
    private void increaseClaims(){
        totalClaims++;
    }*/

    private void increaseClaims(int numClaims){
        totalClaims += numClaims;
    }
    
    private int getLastSequenceNumber(){
        TeleplanSequenceDAO seqDAO = new TeleplanSequenceDAO();
        return seqDAO.getLastSequenceNumber();   
    }
    
    private int getCurrentSequenceNumber(){
        return sequenceNum;
    }
    
    /*
     * After the sequence gets to 9999999  (7 9's) it rolls over to one again
        Conditions. 
     *    Last line is 9999999 
     *    9999999 is hit in the middle of a file generation
     */
    private String getNextSequenceNumber(){
        sequenceNum++;
        if ( sequenceNum > 9999999){
            sequenceNum = 1;
        }
        return ""+sequenceNum;
    }
    private void appendToFile(String str){
        mspFileStr.append(str);
    }
    
    private void appendToHTML(String str){
        mspHtmlStr.append(str);
    }
    
    public TeleplanSubmission getSubmission(LoggedInInfo loggedInInfo, boolean testRun,ProviderData[] providers,String dataCenterId ) {
        log.debug("Start getSubmission");
        
        String logNo =  getNextSequenceNumber() ;
        log.debug("LogNo :"+logNo);
        String headerLine = "VS1" + dataCenterId + Misc.forwardZero(logNo,7) + "V6242" + "OSCAR_MCMASTER           " + "V1.1      " + "20030930" + "OSCAR MCMASTER                          " + "(905) 575-1300 " + Misc.space(25) + Misc.space(57) + "\r";
        String errorMsg = checkData.checkVS1("VS1" , dataCenterId , Misc.forwardZero(logNo,7) , "V6242" , "OSCAR_MCMASTER           " , "V1.1      " , "20030930" , "OSCAR MCMASTER                          " , "(905) 575-1300 " , Misc.space(25) , Misc.space(57));
        setLog(logNo, headerLine);
        
        appendToHTML(HtmlTeleplanHelper.htmlHeaderGen(errorMsg));
        appendToFile(headerLine);
        errorMsg = "";
        
        
        List<String> providerBillingNumbers = new ArrayList();
        for (int p = 0; p < providers.length; p++){
            if (!providerBillingNumbers.contains(providers[p].getOhip_no())){
                providerBillingNumbers.add(providers[p].getOhip_no());
            }
        }
        
        for(String providerBillingNumber: providerBillingNumbers){    
           appendToHTML(HtmlTeleplanHelper.htmlNewProviderSection(providerBillingNumber,new Date()));  
           log.debug("For Provider  :"+providerBillingNumber);
           List list = getBilling(providerBillingNumber,null,null); // null,null because date range doesn't do anything 
           //Get All The Bills for this provider
           
           log.debug("Billing List Size? "+list.size());
           int providerClaimsCount = 0;
           BigDecimal providerTotals = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
           for (int i = 0; i < list.size(); i++){
                log.debug("Start loop, Interation "+i);
                HashMap map = (HashMap) list.get(i);
                String billType = (String) map.get("billingtype");
                String billing_no = (String) map.get("billing_no");
                String demoName = (String) map.get("demographic_name");
                Claims c = null;
                if (billType.equals("MSP")  || billType.equals("ICBC") ) {
                    log.debug("Billing # :"+billing_no+" Data Center :"+dataCenterId+ " ICBC / MSP BILL");
                    c = createMSPICBCLines(billing_no,dataCenterId,demoName);   
                }else if(billType.equals("WCB")){
                    //TODO:Should pass dataCenterId to WCB but it looks it up in the properties currently, fix in the future
                    log.debug("Billing # :"+billing_no+" Data Center :"+dataCenterId+ " WCB BILL");
                    c = createWCB2(loggedInInfo, billing_no);            
                }
                
                if(c == null){
                    log.error("Billing # "+billing_no+ " has no associated WCB record" );
                    continue;  // Not sure if this is great but at least it contines
                }
                
                providerClaimsCount += c.getNumClaims();
                providerTotals = providerTotals.add(c.getClaimTotal());
                
                log.debug("line Claims :"+c.getNumClaims());
                log.debug("To Claims to this point :"+providerClaimsCount);
                log.debug("Claim Total :"+c.getClaimTotal());
                log.debug("Provider Total  :"+providerTotals);
                
                addToMarkBillingList(billing_no);
                log.debug("End loop, added billing no "+ billing_no+" to list");
           } 
           //Add to Providers Totals to the  submission
           addToTotal(providerTotals);
           increaseClaims(providerClaimsCount);
           appendToHTML(HtmlTeleplanHelper.htmlFooter(providerBillingNumber,providerClaimsCount,providerTotals)); 
        }
        appendToHTML(HtmlTeleplanHelper.htmlFooter("",totalClaims,bigTotal)); 
        appendToHTML(HtmlTeleplanHelper.htmlBottom());
        
        TeleplanSubmission submission = new  TeleplanSubmission(mspFileStr.toString(),
                                                                mspHtmlStr.toString(),
                                                                getCurrentSequenceNumber(),
                                                                billingToBeMarkedAsBilled, 
                                                                billingmasterToBeMarkedAsBilled,
                                                                bigTotal,
                                                                logList,
                                                                totalClaims);
        return submission;
    }
    
    private Claims createWCB2(LoggedInInfo loggedInInfo, String billing_no){
        
        
            //setMasDAO(new BillingmasterDAO());
           //log.debug
            MiscUtils.getLogger().debug("creating WCB teleplan record for claim "+billing_no);
           List billMasterList = billingmasterDAO.getBillingMasterByBillingNo(billing_no);
           Billingmaster bm = (Billingmaster) billMasterList.get(0);
           
           
           //Billingmaster bm = billingmasterDAO.getBillingMasterByBillingMasterNo(billing_no);
           
            
           WCB wcbForm = billingmasterDAO.getWCBForm(""+bm.getWcbId());
               
           MiscUtils.getLogger().debug("BM "+bm+" WCB "+wcbForm + " for "+billing_no);
           
           WCBTeleplanSubmission wcbSub = new WCBTeleplanSubmission();
           wcbSub.setDemographicManager(demographicManager);
           //WcbSb sb = new WcbSb(billing_no);
           appendToHTML(wcbSub.getHtmlLine(wcbForm,bm)); //sb.getHtmlLine());
           appendToHTML(wcbSub.validate(wcbForm,bm)); //sb.validate());
           //TODO: DOES THIS DO ANYTHING appendToHTML(checkData.printWarningMsg(""))
           
           Claims claims = new Claims();
           claims.increaseClaims();
           
           claims.addToTotal(bm.getBillingAmountBigDecimal());        
           
           
           MiscUtils.getLogger().debug("FORM NEEDED ?"+wcbSub.isFormNeeded(bm ));
           if(wcbSub.isFormNeeded(bm)){
               
                String logNo = getNextSequenceNumber();
                String lines = wcbSub.Line1(loggedInInfo, String.valueOf(logNo),bm,wcbForm);
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = wcbSub.Line2(String.valueOf(logNo),bm,wcbForm);
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = wcbSub.Line3(loggedInInfo, String.valueOf(logNo),bm,wcbForm);
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = wcbSub.Line4(String.valueOf(logNo),bm,wcbForm);
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = wcbSub.Line5(loggedInInfo, String.valueOf(logNo),bm,wcbForm);
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = wcbSub.Line6(String.valueOf(logNo),bm,wcbForm);
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = wcbSub.Line7(loggedInInfo, String.valueOf(logNo),bm,wcbForm);
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = wcbSub.Line8(String.valueOf(logNo),bm,wcbForm);
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());
           }else{
                String logNo = getNextSequenceNumber();
                String lines = wcbSub.Line9(loggedInInfo, String.valueOf(logNo),bm,wcbForm);
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());
           }
           addToMarkBillingmasterList(""+bm.getBillingmasterNo());
           return claims;
    }
    
    
    private Claims createWCB(String billing_no){
  
           WcbSb sb = new WcbSb(billing_no);
           appendToHTML(sb.getHtmlLine());
           appendToHTML(sb.validate());
           //TODO: DOES THIS DO ANYTHING appendToHTML(checkData.printWarningMsg(""))
           
           Claims claims = new Claims();
           claims.increaseClaims();
           claims.addToTotal(sb.getBillingAmountForFee1BigDecimal());
           BillingmasterDAO masDAO = SpringUtils.getBean(BillingmasterDAO.class);
        
           List billMasterList = masDAO.getBillingMasterByBillingNo(billing_no);
           Billingmaster bm = (Billingmaster) billMasterList.get(0);
           
           MiscUtils.getLogger().debug("FORM NEEDED ?"+sb.isFormNeeded());
           if(sb.isFormNeeded()){
               
                String logNo = getNextSequenceNumber();
                String lines = sb.Line1(String.valueOf(logNo));
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = sb.Line2(String.valueOf(logNo));
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = sb.Line3(String.valueOf(logNo));
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = sb.Line4(String.valueOf(logNo));
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = sb.Line5(String.valueOf(logNo));
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = sb.Line6(String.valueOf(logNo));
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = sb.Line7(String.valueOf(logNo));
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());

                logNo = getNextSequenceNumber();
                lines = sb.Line8(String.valueOf(logNo));
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());
           }else{
                String logNo = getNextSequenceNumber();
                String lines = sb.Line9(String.valueOf(logNo));
                appendToFile("\n"+ lines +"\r");
                setLog(logNo, lines,""+bm.getBillingmasterNo());
           }
           addToMarkBillingmasterList(""+bm.getBillingmasterNo());
           return claims;
    }
    
    //This needs to handle having multiple billingmaster line per billing but from now 
    private Claims createMSPICBCLines(String billing_no,String dataCenterId,String demoName){
        log.debug("createMSPICBCLines Start");
        
        List billMasterList = billingmasterDAO.getBillingMasterWithStatus(billing_no,"O");
        Claims claims = new Claims();
        for (int i= 0; i < billMasterList.size(); i++){
            Billingmaster bm = (Billingmaster) billMasterList.get(i);
            bm.setDatacenter(dataCenterId);

            claims.increaseClaims();
            String logNo = getNextSequenceNumber();  
            String dataLine = getClaimDetailRecord(bm,logNo);  //NEED TO IMPLEMENT this method
            appendToFile("\n"+dataLine+"\r");
            setLog(logNo,dataLine,bm.getBillingmasterNo());
            
            if (bm.hasNoteRecord()){
              String noteLogNo = getNextSequenceNumber();
              String noteRecordLine = getNoteRecord(bm,noteLogNo);
              appendToFile("\n"+noteRecordLine+"\r");
              setLog(noteLogNo,noteRecordLine,bm.getBillingmasterNo());
            }   
            claims.addToTotal(bm.getBillingAmountBigDecimal());
                                            //?this null is supposed to be the demographic name
            appendToHTML( HtmlTeleplanHelper.htmlLine(""+bm.getBillingmasterNo(),billing_no,demoName, getHinForHTML(bm), bm.getServiceDate() ,bm.getBillingCode(),bm.getBillAmount(),bm.getDxCode1(),bm.getDxCode2(),bm.getDxCode3() ) ) ;
            appendToHTML(checkData.checkC02(""+bm.getBillingmasterNo(), bm));

            addToMarkBillingmasterList(""+bm.getBillingmasterNo());
        }
        log.debug("createMSPICBCLines End");
        return claims;
    }  

    private String getHinForHTML(Billingmaster bm){
        String hin = bm.getPhn();
        if (bm.getOinInsurerCode() != null && (bm.getOinInsurerCode().trim().length() > 0) ){
         hin = bm.getOinRegistrationNo();    
        }
        return hin;
    }

    
    //TODO: DATA CENTER NUMBER IS HERE?? should that be from property?
    public String getNoteRecord(Billingmaster bm, String seqNo) {
       MSPBillingNote note = new MSPBillingNote();
       return MSPBillingNote.getN01(bm.getDatacenter(),seqNo,bm.getPayeeNo(),bm.getPractitionerNo(), "A", note.getNote(""+bm.getBillingmasterNo()));
    }


    private void setLog(String logNo, String value) { 
        logList.add(new TeleplanLog(logNo,value));
    }
    
    private void setLog(String logNo,String value, String billingmaster) {
        logList.add(new TeleplanLog(logNo,value,billingmaster));
    }
    
    private void setLog(String logNo,String value, int billingmaster) {
        logList.add(new TeleplanLog(logNo,value,""+billingmaster));
    }
    
    //Date Range not implemented
    //This should be moved out of this class
    private List<Map<String, String>> getBilling(String providerInsNo,Date startDate, Date endDate) {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();              
        BillingDao dao = SpringUtils.getBean(BillingDao.class);
        for(Billing b : dao.findByProviderStatusForTeleplanFileWriter(providerInsNo)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("billing_no",b.getId().toString());
            map.put("demographic_name",b.getDemographicName());
            map.put("billingtype",b.getBillingtype());   
            list.add(map);
        }     
        return list;
    }
    
    public static String  roundUp (String str){
       String retval = "1";
       try{
          retval = new java.math.BigDecimal(str).setScale(0,BigDecimal.ROUND_UP).toString();
       }catch(Exception e){ MiscUtils.getLogger().error("Error", e);}
       return retval;
    }
    
    public String getClaimDetailRecord(Billingmaster bm ,String logNo) {
        StringBuilder dLine = new StringBuilder(); 
            dLine.append(Misc.forwardSpace(bm.getClaimcode(),3));                       //p00   3
            dLine.append(Misc.forwardSpace(bm.getDatacenter(),5));                      //p02   5
            dLine.append(Misc.forwardZero(logNo,7));                                    //p04   7
            dLine.append(Misc.forwardSpace(bm.getPayeeNo(),5));                         //p06   5
            dLine.append(Misc.forwardSpace(bm.getPractitionerNo(),5));                  //p08   5
            dLine.append(Misc.forwardZero(bm.getPhn(),10));                             //p14  10
            dLine.append(Misc.forwardSpace(bm.getNameVerify(),4));                      //p16   4
            dLine.append(Misc.forwardSpace(bm.getDependentNum(),2));                    //p18   2
            dLine.append(Misc.forwardZero(roundUp(bm.getBillingUnit()),3));             //p20   3
            dLine.append(Misc.forwardZero(bm.getClarificationCode() ,2));               //p22   2
            dLine.append(Misc.forwardSpace(bm.getAnatomicalArea(), 2));                 //p23   2
            dLine.append(Misc.forwardSpace(bm.getAfterHour(),1));                       //p24   1
            dLine.append(Misc.forwardZero(bm.getNewProgram(),2));                       //p25   2
            dLine.append(Misc.forwardZero(bm.getBillingCode(),5));                      //p26   5
            dLine.append(Misc.moneyFormatPaddedZeroNoDecimal(bm.getBillAmount(),7));    //p27   7
            dLine.append(Misc.forwardZero(bm.getPaymentMode(), 1));                     //p28   1
            dLine.append(Misc.forwardSpace(bm.getServiceDate(), 8));                    //p30   8
            dLine.append(Misc.forwardZero(bm.getServiceToDay(),2));                     //p32   2
            dLine.append(Misc.forwardSpace(bm.getSubmissionCode(), 1));                 //p34   1
            dLine.append(Misc.space(1));                                                //p35   1
            dLine.append(Misc.backwardSpace(bm.getDxCode1(), 5));                       //p36   5
            dLine.append(Misc.backwardSpace(bm.getDxCode2(), 5));                       //p37   5
            dLine.append(Misc.backwardSpace(bm.getDxCode3(), 5));                       //p38   5
            dLine.append(Misc.space(15));                                               //p39  15
            dLine.append(Misc.forwardSpace(bm.getServiceLocation(), 1));                //p40   1
            dLine.append(Misc.forwardZero(bm.getReferralFlag1(), 1));                   //p41   1
            dLine.append(Misc.forwardZero(bm.getReferralNo1(),5));                      //p42   5
            dLine.append(Misc.forwardZero(bm.getReferralFlag2(),1));                    //p44   1
            dLine.append(Misc.forwardZero(bm.getReferralNo2(),5));                      //p46   5
            dLine.append(Misc.forwardZero(bm.getTimeCall(),4));                         //p47   4
            dLine.append(Misc.forwardZero(bm.getServiceStartTime(),4));                 //p48   4
            dLine.append(Misc.forwardZero(bm.getServiceEndTime(),4));                   //p50   4
            dLine.append(Misc.forwardZero(bm.getBirthDate(),8));                        //p52   8
            dLine.append(Misc.forwardZero(""+bm.getBillingmasterNo(), 7));              //p54   7
            dLine.append(Misc.forwardSpace(bm.getCorrespondenceCode(), 1));             //p56   1
            //dLine.append(Misc.space(20));                                             //p58  20
            dLine.append(Misc.backwardSpace(bm.getClaimComment(),20));        //p58  20
            dLine.append(Misc.forwardSpace(bm.getMvaClaimCode(),1));                    //p60   1
            dLine.append(Misc.forwardZero(bm.getIcbcClaimNo(), 8));                     //p62   8
            dLine.append(Misc.forwardZero(bm.getOriginalClaim(), 20 ));                 //p64  20
            dLine.append(Misc.forwardZero(bm.getFacilityNo(), 5));                      //p70   5
            dLine.append(Misc.forwardZero(bm.getFacilitySubNo(), 5));                   //p72   5
            dLine.append(Misc.space(58));                                               //p80  58
            dLine.append(Misc.backwardSpace(bm.getOinInsurerCode(),2));                 //p100  2
            dLine.append(Misc.forwardZero(bm.getOinRegistrationNo(),12));               //p102 12
            dLine.append(Misc.backwardSpace(bm.getOinBirthdate(),8));                   //p104  8
            dLine.append(Misc.backwardSpace(bm.getOinFirstName(),12));                  //p106 12
            dLine.append(Misc.backwardSpace(bm.getOinSecondName(),1));                  //p108  1
            dLine.append(Misc.backwardSpace(bm.getOinSurname(),18));                    //p110 18
            dLine.append(Misc.backwardSpace(bm.getOinSexCode(),1));                     //p112  1
            dLine.append(Misc.backwardSpace(bm.getOinAddress(),25));                    //p114 25
            dLine.append(Misc.backwardSpace(bm.getOinAddress2(),25));                   //p116 25
            dLine.append(Misc.backwardSpace(bm.getOinAddress3(),25));                   //p118 25
            dLine.append(Misc.backwardSpace(bm.getOinAddress4(),25));                   //p120 25
            dLine.append(Misc.backwardSpace(bm.getOinPostalcode(),6));                  //p122  6
        return dLine.toString();
    }

    public void setBillingmasterDAO(BillingmasterDAO masDAO) {
        this.billingmasterDAO = masDAO;
    }
   
   
    public void setDemographicManager(DemographicManager demographicManager) {
        this.demographicManager = demographicManager;
    }
   
    
    class Claims{
        BigDecimal claimTotal = null;
        int numClaims = 0;
    
        public Claims(){
            claimTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        public void addToTotal(BigDecimal bd){
            bigTotal = bigTotal.add(bd);
        }
        
        public void increaseClaims(){
            numClaims++;
        }
        
        public int getNumClaims(){
            return numClaims;
        }
        
        public BigDecimal getClaimTotal(){
            return claimTotal;
        }
    
    }
    
}
