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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.billing.CA.BC.dao.TeleplanC12Dao;
import org.oscarehr.billing.CA.BC.dao.TeleplanS00Dao;
import org.oscarehr.billing.CA.BC.dao.TeleplanS21Dao;
import org.oscarehr.billing.CA.BC.dao.TeleplanS22Dao;
import org.oscarehr.billing.CA.BC.dao.TeleplanS23Dao;
import org.oscarehr.billing.CA.BC.dao.TeleplanS25Dao;
import org.oscarehr.billing.CA.BC.model.TeleplanC12;
import org.oscarehr.billing.CA.BC.model.TeleplanS00;
import org.oscarehr.billing.CA.BC.model.TeleplanS21;
import org.oscarehr.billing.CA.BC.model.TeleplanS22;
import org.oscarehr.billing.CA.BC.model.TeleplanS23;
import org.oscarehr.billing.CA.BC.model.TeleplanS25;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

/**
 *
 * @author jay
 */
public class GenTaAction  extends Action {
    
	private TeleplanS21Dao s21Dao = SpringUtils.getBean(TeleplanS21Dao.class);
	private TeleplanS00Dao s00Dao = SpringUtils.getBean(TeleplanS00Dao.class);
	private TeleplanS23Dao s23Dao = SpringUtils.getBean(TeleplanS23Dao.class);
	private TeleplanS25Dao s25Dao = SpringUtils.getBean(TeleplanS25Dao.class);
	private TeleplanS22Dao s22Dao = SpringUtils.getBean(TeleplanS22Dao.class);
	private TeleplanC12Dao c12Dao = SpringUtils.getBean(TeleplanC12Dao.class);

	
    /** Creates a new instance of GenTaAction */
    public GenTaAction() {
    }
    
    
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
    throws IOException, ServletException, Exception{
        
        
        MSPReconcile mspReconcile = new MSPReconcile();
        
        
        int recFlag = 0;
        String raNo = "";
        String filename = (String) request.getAttribute("filename");// documentBean.getFilename();
        
        String forwardPage = "S21";
        
        String filepath = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
        
        FileInputStream file = new FileInputStream(filepath + filename);
        BufferedReader input = new BufferedReader(new InputStreamReader(file));
        String nextline;
        
        while ((nextline=input.readLine())!=null){
            String header = nextline.substring(0,3);
            if (header.equals("S21")) {
                S21 s21 = new S21();
                s21.parse(nextline);
                raNo = "";
               
                List<TeleplanS21> rs = s21Dao.findByFilenamePaymentPayeeNo(filename, s21.getT_payment(), s21.getT_payeeno());
                for(TeleplanS21 r:rs) {
                	raNo = String.valueOf(r.getId());
                }
                if (raNo.equals("")  || raNo == null){
                    recFlag = 1;
                    //persist here
                    TeleplanS21 t = new TeleplanS21();
                    t.setFileName(filename);
                    t.setDataCentre(s21.getT_datacenter());
                    t.setDataSeq(s21.getT_dataseq());
                    t.setPayment(s21.getT_payment());
                    t.setLineCode(s21.getT_linecode().toCharArray()[0]);
                    t.setPayeeNo(s21.getT_payeeno());
                    t.setMspCtlNo(s21.getT_mspctlno());
                    t.setPayeeName(s21.getT_payeename());
                    t.setAmountBilled(s21.getT_amtbilled());
                    t.setAmountPaid(s21.getT_amtpaid());
                    t.setBalanceForward(s21.getT_balancefwd());
                    t.setCheque(s21.getT_cheque());
                    t.setNewBalance(s21.getT_newbalance());
                    t.setFiller(s21.getT_filler());
                    t.setStatus('N');
                    s21Dao.persist(t);
                    raNo=t.getId().toString();
                }
            }else if (header.equals("S01")){
                S01 s01 = new S01(nextline);
                if(recFlag >0){
                	TeleplanS00 t = new TeleplanS00();
                	t.setS21Id(Integer.parseInt(raNo));
                	t.setFileName(filename);
                	t.setS00Type(s01.t_s00type);
                	t.setDataCentre(s01.t_datacenter);
                    t.setDataSeq(s01.t_dataseq);
                    t.setPayment(s01.t_payment);
                    t.setLineCode(s01.t_linecode.toCharArray()[0]);
                    t.setPayeeNo(s01.t_payeeno);
                    t.setMspCtlNo(s01.t_mspctlno);
                	t.setPractitionerNo(s01.t_practitionerno);
                	t.setMspRcdDate(s01.t_msprcddate);
                    t.setInitial("");
                    t.setSurname("");
                    t.setPhn("");
                    t.setPhnDepNo("");
                    t.setServiceDate("");
                    t.setToday("");
                    t.setBillNoServices("");
                    t.setBillClafCode("");
                    t.setBillFeeSchedule("");
                    t.setBillAmount("");
                    t.setPaidNoServices("");
                    t.setPaidClafCode("");
                    t.setPaidFeeSchedule("");
                    t.setPaidAmount(s01.t_paidamt);
                    t.setOfficeNo(s01.t_officeno);
                    t.setExp1("");
                    t.setExp2("");
                    t.setExp3("");
                    t.setExp4("");
                    t.setExp5("");
                    t.setExp6("");
                    t.setExp7("");
                    t.setAjc1(s01.t_ajc1);
                    t.setAja1(s01.t_aja1);
                    t.setAjc2(s01.t_ajc2);
                    t.setAja2(s01.t_aja2);
                    t.setAjc3(s01.t_ajc3);
                    t.setAja3(s01.t_aja3);
                    t.setAjc4(s01.t_ajc4);
                    t.setAja4(s01.t_aja4);
                    t.setAjc5(s01.t_ajc5);
                    t.setAja5(s01.t_aja5);
                    t.setAjc6(s01.t_ajc6);
                    t.setAja6(s01.t_aja6);
                    t.setAjc7(s01.t_ajc7);
                    t.setAja7(s01.t_aja7);
                    t.setPaidRate(s01.t_paidrate);
                    t.setPlanRefNo("");
                    t.setClaimSource("");
                    t.setPreviousPaidDate("");
                    t.setIcBcWcb(s01.t_icbcwcb);
                    t.setInsurerCode(s01.t_insurercode);
                    t.setFiller(s01.t_filler);
                    
                    s00Dao.persist(t);
                	
                    mspReconcile.updateStat(MSPReconcile.SETTLED,s01.getBillingMasterNo());
                }
            }else if (header.equals("S02")  || header.equals("S00")  || header.equals("S03") ){
                S02 s02 = new S02(nextline);
                if (recFlag >0) {
                    recFlag = recFlag +1;
                    
                	TeleplanS00 t = new TeleplanS00();
                	t.setS21Id(Integer.parseInt(raNo));
                	t.setFileName(filename);
                	t.setS00Type(s02.t_s00type);
                	t.setDataCentre(s02.t_datacenter);
                    t.setDataSeq(s02.t_dataseq);
                    t.setPayment(s02.t_payment);
                    t.setLineCode(s02.t_linecode.toCharArray()[0]);
                    t.setPayeeNo(s02.t_payeeno);
                    t.setMspCtlNo(s02.t_mspctlno);
                	t.setPractitionerNo(s02.t_practitionerno);
                	t.setMspRcdDate(s02.t_msprcddate);
                    t.setInitial(s02.t_initial);
                    t.setSurname(s02.t_surname);
                    t.setPhn(s02.t_phn);
                    t.setPhnDepNo(s02.t_phndepno);
                    t.setServiceDate(s02.t_servicedate);
                    t.setToday(s02.t_today);
                    t.setBillNoServices(s02.t_billnoservices);
                    t.setBillClafCode(s02.t_billclafcode);
                    t.setBillFeeSchedule(s02.t_billfeeschedule);
                    t.setBillAmount(s02.t_billamt);
                    t.setPaidNoServices(s02.t_paidnoservices);
                    t.setPaidClafCode(s02.t_paidclafcode);
                    t.setPaidFeeSchedule(s02.t_paidfeeschedule);
                    t.setPaidAmount(s02.t_paidamt);
                    t.setOfficeNo(s02.t_officeno);
                    t.setExp1(s02.t_exp1);
                    t.setExp2(s02.t_exp2);
                    t.setExp3(s02.t_exp3);
                    t.setExp4(s02.t_exp4);
                    t.setExp5(s02.t_exp5);
                    t.setExp6(s02.t_exp6);
                    t.setExp7(s02.t_exp7);
                    t.setAjc1(s02.t_ajc1);
                    t.setAja1(s02.t_aja1);
                    t.setAjc2(s02.t_ajc2);
                    t.setAja2(s02.t_aja2);
                    t.setAjc3(s02.t_ajc3);
                    t.setAja3(s02.t_aja3);
                    t.setAjc4(s02.t_ajc4);
                    t.setAja4(s02.t_aja4);
                    t.setAjc5(s02.t_ajc5);
                    t.setAja5(s02.t_aja5);
                    t.setAjc6(s02.t_ajc6);
                    t.setAja6(s02.t_aja6);
                    t.setAjc7(s02.t_ajc7);
                    t.setAja7(s02.t_aja7);
                    t.setPaidRate("");
                    t.setPlanRefNo(s02.t_planrefno);
                    t.setClaimSource(s02.t_claimsource);
                    t.setPreviousPaidDate(s02.t_previouspaiddate);
                    t.setIcBcWcb(s02.t_icbcwcb);
                    t.setInsurerCode(s02.t_insurercode);
                    t.setFiller(s02.t_filler);
                    
                    s00Dao.persist(t);
                   
                  
                    if(header.equals("S02")){ //header.compareTo("S00") == 0 || header.compareTo("S03") == 0){
                        mspReconcile.updateStat(MSPReconcile.PAIDWITHEXP,s02.getBillingMasterNo());
                    }else if (header.equals("S03")){
                        mspReconcile.updateStat(MSPReconcile.REFUSED,s02.getBillingMasterNo());
                    }else if (header.equals("S00")){
                        mspReconcile.updateStat(MSPReconcile.DATACENTERCHANGED,s02.getBillingMasterNo());
                    }
                }
            }else if (header.equals("S04")){
                S04 s04 = new S04(nextline);
                if (recFlag >0) {
                	TeleplanS00 t = new TeleplanS00();
                	t.setS21Id(Integer.parseInt(raNo));
                	t.setFileName(filename);
                	t.setS00Type(s04.t_s00type);
                	t.setDataCentre(s04.t_datacenter);
                    t.setDataSeq(s04.t_dataseq);
                    t.setPayment(s04.t_payment);
                    t.setLineCode(s04.t_linecode.toCharArray()[0]);
                    t.setPayeeNo(s04.t_payeeno);
                    t.setMspCtlNo(s04.t_mspctlno);
                    
                	t.setPractitionerNo("");
                	t.setMspRcdDate("");
                    t.setInitial("");
                    t.setSurname("");
                    t.setPhn("");
                    t.setPhnDepNo("");
                    t.setServiceDate("");
                    t.setToday("");
                    t.setBillNoServices("");
                    t.setBillClafCode("");
                    t.setBillFeeSchedule("");
                    t.setBillAmount("");
                    t.setPaidNoServices("");
                    t.setPaidClafCode("");
                    t.setPaidFeeSchedule("");
                    t.setPaidAmount("");
                    t.setOfficeNo(s04.t_officeno);
                    t.setExp1(s04.t_exp1);
                    t.setExp2(s04.t_exp2);
                    t.setExp3(s04.t_exp3);
                    t.setExp4(s04.t_exp4);
                    t.setExp5(s04.t_exp5);
                    t.setExp6(s04.t_exp6);
                    t.setExp7(s04.t_exp7);
                    t.setAjc1("");
                    t.setAja1("");
                    t.setAjc2("");
                    t.setAja2("");
                    t.setAjc3("");
                    t.setAja3("");
                    t.setAjc4("");
                    t.setAja4("");
                    t.setAjc5("");
                    t.setAja5("");
                    t.setAjc6("");
                    t.setAja6("");
                    t.setAjc7("");
                    t.setAja7("");
                    t.setPaidRate("");
                    t.setPlanRefNo("");
                    t.setClaimSource("");
                    t.setPreviousPaidDate("");
                    t.setIcBcWcb(s04.t_icbcwcb);
                    t.setInsurerCode(s04.t_insurercode);
                    t.setFiller(s04.t_filler);
                    
                    s00Dao.persist(t);
                  
           
                    mspReconcile.updateStat(MSPReconcile.HELD,s04.getBillingMasterNo());
                }
            } else if (header.equals("S23") ||header.equals("S24")){
                S23 s23 = new S23(nextline);
                if (recFlag >0) {
                	TeleplanS23 t = new TeleplanS23();
                	t.setS21Id(Integer.parseInt(raNo));
                	t.setFileName(filename);
                	t.setS23Type(s23.t_s23type);
                	t.setDataCentre(s23.t_datacenter);
                	t.setDataSeq(s23.t_dataseq);
                	t.setPayment(s23.t_payment);
                	t.setLineCode(s23.t_linecode.charAt(0));
                	t.setMspCtlNo(s23.t_mspctlno);
                	t.setAjc(s23.t_ajc);
                	t.setAji(s23.t_aji);
                	t.setAjm(s23.t_ajm);
                	t.setCalcMethod(s23.t_calcmethod);
                	t.setrPercent(s23.t_rpercent);
                	t.setoPercent(s23.t_opercent);
                	t.setgAmount(s23.t_gamount);
                	t.setrAmount(s23.t_ramount);
                	t.setoAmount(s23.t_oamount);
                	t.setBalanceForward(s23.t_balancefwd);
                	t.setAdjMade(s23.t_adjmade);
                	t.setAdjOutstanding(s23.t_adjoutstanding);
                	t.setFiller(s23.t_filler);
                	
                	s23Dao.persist(t);

                }
            } else if (header.equals("S25")){
                S25 s25 = new S25(nextline);
                if (recFlag >0) {
                	TeleplanS25 t = new TeleplanS25();
                	t.setS21Id(Integer.parseInt(raNo));
                	t.setFileName(filename);
                	t.setS25Type(s25.t_s25type);
                	t.setDataCentre(s25.t_datacenter);
                	t.setDataSeq(s25.t_dataseq);
                	t.setPayment(s25.t_payment);
                	t.setLineCode(s25.t_linecode.charAt(0));
                	t.setPayeeNo(s25.t_payeeno);
                	t.setMspCtlNo(s25.t_mspctlno);
                	t.setPractitionerNo(s25.t_practitionerno);
                	t.setMessage(s25.t_message);
                	t.setFiller(s25.t_filler);
                	
                	s25Dao.persist(t);
                }
            } else if (header.equals("S22")){
                S22 s22 = new S22(nextline);
                if (recFlag >0) {
                	TeleplanS22 t = new TeleplanS22();
                	t.setS21Id(Integer.parseInt(raNo));
                	t.setFileName(filename);
                	t.setS22Type(s22.t_s22type);
                	t.setDataCentre(s22.t_datacenter);
                	t.setDataSeq(s22.t_dataseq);
                	t.setPayment(s22.t_payment);
                	t.setLineCode(s22.t_linecode.charAt(0));
                	t.setPayeeNo(s22.t_payeeno);
                	t.setMspCtlNo(s22.t_mspctlno);
                	t.setPractitionerNo(s22.t_practitionerno);
                	t.setPractitionerName(s22.t_practitionername);
                	t.setAmountBilled(s22.t_amtbilled);
                	t.setAmountPaid(s22.t_amtpaid);
                	t.setFiller(s22.t_filler);
                	s22Dao.persist(t);
                }
                
            /*
             *C12 records are error records. There are three ways that the program will come to here
             *1.File with just C12 records (besides all the VCR ones at the top)
             *     one record is added to teleplanS21
             *2.File with C12 records at the top before it gets to a S21 record
             *     two records are added to teleplanS21, one with a status of D ( the one from the C12 records ) and the other with N
             *3.File with C12 records at the bottom.  
             *     one record with a status of N
             *
             */    
            }else if (header.equals("C12")){
                C12 c12 = new C12(nextline);
                if (raNo.equals("")){
                	
                	List<TeleplanS21> rs = s21Dao.findByFilenamePaymentPayeeNo(filename,"","");
                    for(TeleplanS21 r:rs) {
                    	raNo = String.valueOf(r.getId());
                    }
                    
                    if (raNo.compareTo("") == 0 || raNo == null){
                        recFlag = 1;
                        
                        TeleplanS21 t = new TeleplanS21();
                        t.setFileName(filename);
                        t.setDataCentre(c12.getT_datacenter());
                        t.setDataSeq(c12.getT_dataseq());
                        t.setPayment("");
                        t.setPayeeNo(c12.getT_payeeno());
                        t.setMspCtlNo("");
                        t.setPayeeName("");
                        t.setAmountBilled("");
                        t.setAmountPaid("");
                        t.setBalanceForward("");
                        t.setCheque("");
                        t.setNewBalance("");
                        t.setFiller("");
                        t.setStatus('D');
                        s21Dao.persist(t);
                        raNo = t.getId().toString();
                    }
                }  // This will be +1 if the records are at the bottom
                if (recFlag > 0){
                	TeleplanC12 t = new TeleplanC12();
                	t.setS21Id(Integer.parseInt(raNo));
                	t.setFileName(filename);
                	t.setDataCentre(c12.getT_datacenter());
                	t.setDataSeq(c12.getT_dataseq());
                	t.setPayeeNo(c12.getT_payeeno());
                	t.setPractitionerNo(c12.getT_practitionerno());
                	t.setExp1(c12.getT_exp1());
                	t.setExp2(c12.getT_exp2());
                	t.setExp3(c12.getT_exp3());
                	t.setExp4(c12.getT_exp4());
                	t.setExp5(c12.getT_exp5());
                	t.setExp6(c12.getT_exp6());
                	t.setExp7(c12.getT_exp7());
                	t.setOfficeFolioClaimNo(c12.getT_officefolioclaimno());
                	t.setFiller(c12.getT_filler());
                	
                	c12Dao.persist(t);
                	
                    mspReconcile.updateStat(MSPReconcile.REJECTED,c12.getBillingMasterNo());
                }
                forwardPage = "C12";
           
           }else if (header.equals("M01")){
            
           }
            
        }
        
        
        
        
        return mapping.findForward(forwardPage);
    }
}
    
class M01{
   String message;
    void parse(String nextline){
            message = nextline.substring(12);
    }
    
    public M01(String s){
        parse(s);
    }
    
    
    
} 
        
    class S21{
        private String t_datacenter;
        private String t_dataseq ;
        private String t_payment;
        private String t_linecode;
        private String t_payeeno;
        private String t_mspctlno;
        private String t_payeename;
        private String t_amtbilled;
        private String t_amtpaid;
        private String t_balancefwd ;
        private String t_cheque ;
        private String t_newbalance;
        private String t_filler;
        
        public void parse(String nextline){
            
            t_datacenter = nextline.substring(3,8);
            t_dataseq = nextline.substring(8,15);
            t_payment = nextline.substring(15,23);
            t_linecode = nextline.substring(23,24);
            t_payeeno = nextline.substring(24,29);
            t_mspctlno = nextline.substring(29,35);
            t_payeename = nextline.substring(35,60);
            t_amtbilled = nextline.substring(60,69);
            t_amtpaid = nextline.substring(69,78);
            t_balancefwd = nextline.substring(78,87);
            t_cheque = nextline.substring(87,96);
            t_newbalance = nextline.substring(96,105);
            t_filler = nextline.substring(105,165);
            
        }
        
        public String[] getParam(String filename){
            String[] param =new String[15];
            param[0]=filename;
            param[1]=getT_datacenter();
            param[2]=getT_dataseq();
            param[3]=getT_payment();
            param[4]=getT_linecode();
            param[5]=getT_payeeno();
            param[6]=getT_mspctlno();
            param[7]=getT_payeename();
            param[8]=getT_amtbilled();
            param[9]=getT_amtpaid();
            param[10]=getT_balancefwd();
            param[11]= getT_cheque();
            param[12]=getT_newbalance();
            param[13]=getT_filler();
            param[14]="N";
            return param;
            
        }
        
     
        
        public String getT_datacenter() {
            return t_datacenter;
        }
        
        public String getT_dataseq() {
            return t_dataseq;
        }
        
        public String getT_payment() {
            return t_payment;
        }
        
        public String getT_linecode() {
            return t_linecode;
        }
        
        public String getT_payeeno() {
            return t_payeeno;
        }
        
        public String getT_mspctlno() {
            return t_mspctlno;
        }
        
        public String getT_payeename() {
            return t_payeename;
        }
        
        public String getT_amtbilled() {
            return t_amtbilled;
        }
        
        public String getT_amtpaid() {
            return t_amtpaid;
        }
        
        public String getT_balancefwd() {
            return t_balancefwd;
        }
        
        public String getT_cheque() {
            return t_cheque;
        }
        
        public String getT_newbalance() {
            return t_newbalance;
        }
        
        public String getT_filler() {
            return t_filler;
        }
        
        
    }
    
  
    class S01{
        
        public S01(String line){
            parse(line);
        }
        
        String t_s00type ;
        String t_datacenter ;
        String t_dataseq ;
        String t_payment ;
        String t_linecode ;
        String t_payeeno ;
        String t_mspctlno ;
        String t_practitionerno ;
        String t_ajc1;
        String t_aja1;
        String t_ajc2;
        String t_aja2;
        String t_ajc3;
        String t_aja3;
        String t_ajc4;
        String t_aja4;
        String t_ajc5;
        String t_aja5;
        String t_ajc6;
        String t_aja6;
        String t_ajc7;
        String t_aja7;
        String t_officeno;
        String t_paidamt;
        String t_msprcddate;
        String t_paidrate;
        String t_icbcwcb;
        String t_insurercode;
        String t_filler;
        
        void parse(String nextline){
            
            t_s00type = nextline.substring(0,3);
            t_datacenter = nextline.substring(3,8);
            t_dataseq = nextline.substring(8,15);
            t_payment = nextline.substring(15,23);
            t_linecode = nextline.substring(23,24);
            t_payeeno = nextline.substring(24,29);
            t_mspctlno = nextline.substring(29,35);
            t_practitionerno = nextline.substring(35,40);
            t_ajc1= nextline.substring(40,42);
            t_aja1= nextline.substring(42,49);
            t_ajc2= nextline.substring(49,51);
            t_aja2= nextline.substring(51,58);
            t_ajc3= nextline.substring(58,60);
            t_aja3= nextline.substring(60,67);
            t_ajc4= nextline.substring(67,69);
            t_aja4= nextline.substring(69,76);
            t_ajc5= nextline.substring(76,78);
            t_aja5= nextline.substring(78,85);
            t_ajc6= nextline.substring(85,87);
            t_aja6= nextline.substring(87,94);
            t_ajc7= nextline.substring(94,96);
            t_aja7= nextline.substring(96,103);
            t_officeno=nextline.substring(103,110);
            t_paidamt = nextline.substring(110,117);
            t_msprcddate = nextline.substring(117,125);
            t_paidrate = nextline.substring(125,127);
            t_icbcwcb = nextline.substring(127,135);
            t_insurercode = nextline.substring(135,137);
            t_filler = nextline.substring(137,165);
        }
        
        public String getBillingMasterNo(){
            return Integer.toString(Integer.parseInt(t_officeno));
        }
        
        public String[] getParam(String filename,String raNo){
            
            String[] param00 = new String[54];
            param00[0]  =  raNo;
            param00[1]  =  filename ; //(30),
            param00[2]  =  t_s00type ; //(3),
            param00[3]  =  t_datacenter ; //(5),
            param00[4]  =  t_dataseq ; //(7),
            param00[5]  =  t_payment ; //(8),
            param00[6]  =  t_linecode ; //(1),
            param00[7]  =  t_payeeno ; //(5),
            param00[8]  =  t_mspctlno ; //(6),
            param00[9]  =  t_practitionerno ; //(5),
            param00[10] =  t_msprcddate ; //(8),
            param00[11] =  "" ; //(2),
            param00[12] =  "" ; //(18),
            param00[13] =  "" ; //(10),
            param00[14] =  "" ; //(2),
            param00[15] =  "" ; //(8),
            param00[16] =  "" ; //(2),
            param00[17] =  "" ; //(3),
            param00[18] =  "" ; //(2),
            param00[19] =  "" ; //(5),
            param00[20] =  "" ; //(7),
            param00[21] =  "" ; //(3),
            param00[22] =  "" ; //(2),
            param00[23] =  "" ; //(5),
            param00[24] =  t_paidamt ; //(7),
            param00[25] =  t_officeno ; //(7),
            param00[26] =  "" ; //(2),
            param00[27] =  "" ; //(2),
            param00[28] =  "" ; //(2),
            param00[29] =  "" ; //(2),
            param00[30] =  "" ; //(2),
            param00[31] =  "" ; //(2),
            param00[32] =  "" ; //(2),
            param00[33] =  t_ajc1 ; //(2),
            param00[34] =  t_aja1 ; //(7),
            param00[35] =  t_ajc2 ; //(2),
            param00[36] =  t_aja2 ; //(7),
            param00[37] =  t_ajc3 ; //(2),
            param00[38] =  t_aja3 ; //(7),
            param00[39] =  t_ajc4 ; //(2),
            param00[40] =  t_aja4 ; //(7),
            param00[41] =  t_ajc5 ; //(2),
            param00[42] =  t_aja5 ; //(7),
            param00[43] =  t_ajc6 ; //(2),
            param00[44] =  t_aja6 ; //(7),
            param00[45] =  t_ajc7 ; //(2),
            param00[46] =  t_aja7 ; //(7),
            param00[47] =  t_paidrate ; //(2),
            param00[48] =  "" ; //(10),
            param00[49] =  "" ; //(10),
            param00[50] =  "" ; //(8),
            param00[51] =  t_icbcwcb ; //(8),
            param00[52] =  t_insurercode ; //(2),
            param00[53] =  t_filler ; //(87),
            return param00;
        }
    }
    
    
class S02{//{") == 0 || header.compareTo("S00") == 0 || header.compareTo("S03") == 0){
    //if(header.equals("S02")){ //header.compareTo("S00") == 0 || header.compareTo("S03") == 0){
    //}else if (header.equals("S03")){
    //}else if (header.equals("S00")){
    
    public S02(String line){
            parse(line);
        }
    
    String t_s00type ;
    String t_datacenter ;
    String t_dataseq ;
    String t_payment ;
    String t_linecode ;
    String t_payeeno;
    String t_mspctlno;
    String t_practitionerno ;
    String t_msprcddate ;
    String t_initial ;
    String t_surname;
    String t_phn;
    String t_phndepno;
    String t_servicedate;
    String t_today;
    String t_billnoservices;
    String t_billclafcode;
    String t_billfeeschedule;
    String t_billamt ;
    String t_paidnoservices ;
    String t_paidclafcode ;
    String t_paidfeeschedule ;
    String t_paidamt ;
    String t_officeno;
    String t_exp1;
    String t_exp2;
    String t_exp3;
    String t_exp4;
    String t_exp5;
    String t_exp6;
    String t_exp7;
    String t_ajc1;
    String t_aja1;
    String t_ajc2;
    String t_aja2;
    String t_ajc3;
    String t_aja3;
    String t_ajc4;
    String t_aja4;
    String t_ajc5;
    String t_aja5;
    String t_ajc6;
    String t_aja6;
    String t_ajc7;
    String t_aja7;
    String t_planrefno;
    String t_claimsource;
    String t_previouspaiddate;
    String t_insurercode;
    String t_icbcwcb;
    String t_filler;
    
    
    
    void parse(String nextline){
        t_s00type = nextline.substring(0,3);
        t_datacenter = nextline.substring(3,8);
        t_dataseq = nextline.substring(8,15);
        t_payment = nextline.substring(15,23);
        t_linecode = nextline.substring(23,24);
        t_payeeno = nextline.substring(24,29);
        t_mspctlno = nextline.substring(29,35);
        t_practitionerno = nextline.substring(35,40);
        t_msprcddate = nextline.substring(40,48);
        t_initial = nextline.substring(48,50);
        t_surname= nextline.substring(50,68);
        t_phn= nextline.substring(68,78);
        t_phndepno= nextline.substring(78,80);
        t_servicedate= nextline.substring(80,88);
        t_today= nextline.substring(88,90);
        t_billnoservices= nextline.substring(90,93);
        t_billclafcode= nextline.substring(93,95);
        t_billfeeschedule= nextline.substring(95,100);
        t_billamt= nextline.substring(100,107);
        t_paidnoservices= nextline.substring(107,110);
        t_paidclafcode= nextline.substring(110,112);
        t_paidfeeschedule= nextline.substring(112,117);
        t_paidamt= nextline.substring(117,124);
        t_officeno= nextline.substring(124,131);
        t_exp1= nextline.substring(131,133);
        t_exp2= nextline.substring(133,135);
        t_exp3= nextline.substring(135,137);
        t_exp4= nextline.substring(137,139);
        t_exp5= nextline.substring(139,141);
        t_exp6= nextline.substring(141,143);
        t_exp7= nextline.substring(143,145);
        t_ajc1= nextline.substring(145,147);
        t_aja1= nextline.substring(147,154);
        t_ajc2= nextline.substring(154,156);
        t_aja2= nextline.substring(156,163);
        t_ajc3= nextline.substring(163,165);
        t_aja3= nextline.substring(165,172);
        t_ajc4= nextline.substring(172,174);
        t_aja4= nextline.substring(174,181);
        t_ajc5= nextline.substring(181,183);
        t_aja5= nextline.substring(183,190);
        t_ajc6= nextline.substring(190,192);
        t_aja6= nextline.substring(192,199);
        t_ajc7= nextline.substring(199,201);
        t_aja7= nextline.substring(201,208);
        t_planrefno= nextline.substring(208,218);
        t_claimsource= nextline.substring(218,219);
        t_previouspaiddate= nextline.substring(219,227);
        t_insurercode= nextline.substring(227,229);
        t_icbcwcb= nextline.substring(229,237);
        t_filler= nextline.substring(237,267);
    }
    
    public String getBillingMasterNo(){
            return Integer.toString(Integer.parseInt(t_officeno));
        }
    
    public String[] getParam(String filename,String raNo){
        
        
        String[] param02 = new String[54];
        param02[0] =  raNo;
        param02[1] =  filename ; //(30),
        param02[2] =  t_s00type ; //(3),
        param02[3] =  t_datacenter ; //(5),
        param02[4] =  t_dataseq ; //(7),
        param02[5] =  t_payment ; //(8),
        param02[6] =  t_linecode ; //(1),
        param02[7] =  t_payeeno ; //(5),
        param02[8] =  t_mspctlno ; //(6),
        param02[9] =  t_practitionerno ; //(5),
        param02[10] =  t_msprcddate ; //(8),
        param02[11] =  t_initial ; //(2),
        param02[12] =  t_surname ; //(18),
        param02[13] =  t_phn ; //(10),
        param02[14] =  t_phndepno ; //(2),
        param02[15] =  t_servicedate ; //(8),
        param02[16] =  t_today ; //(2),
        param02[17] =  t_billnoservices ; //(3),
        param02[18] =  t_billclafcode ; //(2),
        param02[19] =  t_billfeeschedule ; //(5),
        param02[20] =  t_billamt ; //(7),
        param02[21] =  t_paidnoservices ; //(3),
        param02[22] =  t_paidclafcode ; //(2),
        param02[23] =  t_paidfeeschedule ; //(5),
        param02[24] =  t_paidamt ; //(7),
        param02[25] =  t_officeno ; //(7),
        param02[26] =  t_exp1 ; //(2),
        param02[27] =  t_exp2 ; //(2),
        param02[28] =  t_exp3 ; //(2),
        param02[29] =  t_exp4 ; //(2),
        param02[30] =  t_exp5 ; //(2),
        param02[31] =  t_exp6 ; //(2),
        param02[32] =  t_exp7 ; //(2),
        param02[33] =  t_ajc1 ; //(2),
        param02[34] =  t_aja1 ; //(7),
        param02[35] =  t_ajc2 ; //(2),
        param02[36] =  t_aja2 ; //(7),
        param02[37] =  t_ajc3 ; //(2),
        param02[38] =  t_aja3 ; //(7),
        param02[39] =  t_ajc4 ; //(2),
        param02[40] =  t_aja4 ; //(7),
        param02[41] =  t_ajc5 ; //(2),
        param02[42] =  t_aja5 ; //(7),
        param02[43] =  t_ajc6 ; //(2),
        param02[44] =  t_aja6 ; //(7),
        param02[45] =  t_ajc7 ; //(2),
        param02[46] =  t_aja7 ; //(7),
        param02[47] =  "";//t_paidrate ; //(2),
        param02[48] =  t_planrefno ; //(10),
        param02[49] =  t_claimsource ; //(10),
        param02[50] =  t_previouspaiddate ; //(8),
        param02[51] =  t_icbcwcb ; //(8),
        param02[52] =  t_insurercode ; //(2),
        param02[53] =  t_filler ; //(87),
        
        return param02;
    }
    
    
}


class S04{
    
    public S04(String line){
            parse(line);
        }
    
    String t_s00type ;
    String t_datacenter;
    String t_dataseq ;
    String t_payment ;
    String t_linecode ;
    String t_payeeno ;
    String t_mspctlno ;
    String t_practitionerno ;
    String t_msprcddate;
    String t_officeno;
    String t_exp1;
    String t_exp2;
    String t_exp3;
    String t_exp4;
    String t_exp5;
    String t_exp6;
    String t_exp7;
    String t_icbcwcb;
    String t_insurercode;
    String t_filler;
    
    void parse(String nextline){
        
        t_s00type = nextline.substring(0,3);
        t_datacenter = nextline.substring(3,8);
        t_dataseq = nextline.substring(8,15);
        t_payment = nextline.substring(15,23);
        t_linecode = nextline.substring(23,24);
        t_payeeno = nextline.substring(24,29);
        t_mspctlno = nextline.substring(29,35);
        t_practitionerno = nextline.substring(35,40);
        t_msprcddate = nextline.substring(40,48);
        t_officeno= nextline.substring(48,55);
        t_exp1= nextline.substring(55,57);
        t_exp2= nextline.substring(57,59);
        t_exp3= nextline.substring(59,61);
        t_exp4= nextline.substring(61,63);
        t_exp5= nextline.substring(63,65);
        t_exp6= nextline.substring(65,67);
        t_exp7= nextline.substring(67,69);
        t_icbcwcb= nextline.substring(69,77);
        t_insurercode= nextline.substring(77,79);
        t_filler= nextline.substring(79,165);
        
        MiscUtils.getLogger().debug("held "+MSPReconcile.HELD+" office no "+getBillingMasterNo());
        
    }
    
    public String getBillingMasterNo(){
            return Integer.toString(Integer.parseInt(t_officeno));
        } 
    String[] getParams(String filename, String raNo){
        String[] param04 = new String[54];
        param04[0] =  raNo;
        param04[1] =  filename ; //(30),
        param04[2] =  t_s00type ; //(3),
        param04[3] =  t_datacenter ; //(5),
        param04[4] =  t_dataseq ; //(7),
        param04[5] =  t_payment ; //(8),
        param04[6] =  t_linecode ; //(1),
        param04[7] =  t_payeeno ; //(5),
        param04[8] =  t_mspctlno ; //(6),
        param04[9] =  t_practitionerno ; //(5),
        param04[10] =  t_msprcddate ; //(8),
        param04[11] =  ""; //(2),
        param04[12] =  ""; //(18),
        param04[13] =  ""; //(10),
        param04[14] =  ""; //(2),
        param04[15] =  ""; //(8),
        param04[16] =  ""; //(2),
        param04[17] =  ""; //(3),
        param04[18] =  ""; //(2),
        param04[19] =  ""; //(5),
        param04[20] =  ""; //(7),
        param04[21] =  ""; //(3),
        param04[22] =  ""; //(2),
        param04[23] =  ""; //(5),
        param04[24] =  ""; //(7),
        param04[25] =  t_officeno ; //(7),
        param04[26] =  t_exp1 ; //(2),
        param04[27] =  t_exp2 ; //(2),
        param04[28] =  t_exp3 ; //(2),
        param04[29] =  t_exp4 ; //(2),
        param04[30] =  t_exp5 ; //(2),
        param04[31] =  t_exp6 ; //(2),
        param04[32] =  t_exp7 ; //(2),
        param04[33] =  ""; //(2),
        param04[34] =  ""; //(7),
        param04[35] =  ""; //(2),
        param04[36] =  ""; //(7),
        param04[37] =  ""; //(2),
        param04[38] =  ""; //(7),
        param04[39] =  ""; //(2),
        param04[40] =  ""; //(7),
        param04[41] =  ""; //(2),
        param04[42] =  ""; //(7),
        param04[43] =  ""; //(2),
        param04[44] =  ""; //(7),
        param04[45] =  ""; //(2),
        param04[46] =  ""; //(7),
        param04[47] =  ""; //(2),
        param04[48] =  ""; //(10),
        param04[49] =  ""; //(10),
        param04[50] =  ""; //(8),
        param04[51] =  t_icbcwcb ; //(8),
        param04[52] =  t_insurercode ; //(2),
        param04[53] =  t_filler ; //(87),
        return param04;
    }
    
}

//  s23_id int(10) NOT NULL auto_increment,
//  s21_id int(10) NOT NULL,
//  filename varchar(30),
//  t_s23type varchar(3),
//  t_datacenter varchar(5),
//  t_dataseq varchar(7),
//  t_payment varchar(8),
//  t_linecode varchar(1),
//  t_payeeno varchar(5),
//  t_mspctlno varchar(6),
//  t_ajc varchar(2),
//  t_aji varchar(12),
//  t_ajm varchar(20),
//  t_calcmethod varchar(1),
//  t_rpercent varchar(5),
//  t_opercent varchar(5),
//  t_gamount varchar(9),
//  t_ramount varchar(9),
//  t_oamount varchar(9),
//  t_balancefwd varchar(9),
//  t_adjmade varchar(9),
//  t_adjoutstanding varchar(9),
// t_filler varchar(32),
class S23{ //S24
    
    public S23(String line){
            parse(line);
        }
    
    String t_s23type;
    String t_datacenter;
    String t_dataseq ;
    String t_payment ;
    String t_linecode;
    String t_payeeno ;
    String t_mspctlno;
    String t_ajc ;
    String t_aji ;
    String t_ajm;
    String t_calcmethod;
    String t_rpercent;
    String t_opercent;
    String t_gamount;
    String t_ramount;
    String t_oamount;
    String t_balancefwd;
    String t_adjmade;
    String t_adjoutstanding;
    String t_filler;
    
    void parse(String nextline){
        t_s23type = nextline.substring(0,3);
        t_datacenter = nextline.substring(3,8);
        t_dataseq = nextline.substring(8,15);
        t_payment = nextline.substring(15,23);
        t_linecode = nextline.substring(23,24);
        t_payeeno = nextline.substring(24,29);
        t_mspctlno = nextline.substring(29,35);
        t_ajc = nextline.substring(35,37);
        t_aji = nextline.substring(37,49);
        t_ajm= nextline.substring(49,69);
        t_calcmethod= nextline.substring(69,70);
        t_rpercent=nextline.substring(70,75);
        t_opercent=nextline.substring(75,80);
        t_gamount=nextline.substring(80,89);
        t_ramount=nextline.substring(89,98);
        t_oamount=nextline.substring(98,107);
        t_balancefwd=nextline.substring(107,116);
        t_adjmade=nextline.substring(116,125);
        t_adjoutstanding=nextline.substring(125,134);
        t_filler=nextline.substring(134,165);
    }
    
  
    String[] getParams(String filename, String raNo){
        String[] param23 = new String[22];
        param23[0] =  raNo;
        param23[1] =  filename ; //(30),
        param23[2] =  t_s23type ; //(3),
        param23[3] =  t_datacenter ; //(5),
        param23[4] =  t_dataseq ; //(7),
        param23[5] =  t_payment ; //(8),
        param23[6] =  t_linecode ; //(1),
        param23[7] =  t_payeeno ; //(5),
        param23[8] =  t_mspctlno ; //(6),
        param23[9] =  t_ajc ; //(5),
        param23[10] =  t_aji ; //(8),
        param23[11] =  t_ajm; //(2),
        param23[12] =  t_calcmethod; //(18),
        param23[13] =  t_rpercent; //(10),
        param23[14] =  t_opercent; //(2),
        param23[15] =  t_gamount; //(8),
        param23[16] =  t_ramount; //(2),
        param23[17] =  t_oamount; //(3),
        param23[18] =  t_balancefwd; //(2),
        param23[19] =  t_adjmade; //(5),
        param23[20] =  t_adjoutstanding; //(7),
        param23[21] =  t_filler; //(3),
        return param23;
    }
    
}

//filename varchar(30),
// t_s25type varchar(3),
// t_datacenter varchar(5),
// t_dataseq varchar(7),
// t_payment varchar(8),
// t_linecode varchar(1),
// t_payeeno varchar(5),
// t_mspctlno varchar(6),
// t_practitionerno varchar(5),
// t_message varchar(80),
// t_adjoutstanding varchar(9),
// t_filler varchar(46),
class S25{
    
    public S25(String line){
            parse(line);
        }
    
    String t_s25type  ;
    String t_datacenter ;
    String t_dataseq ;
    String t_payment ;
    String t_linecode ;
    String t_payeeno ;
    String t_mspctlno;
    String t_practitionerno ;
    String t_message ;
    String t_filler ;
    
    
    void parse(String nextline){
        t_s25type = nextline.substring(0,3);
        t_datacenter = nextline.substring(3,8);
        t_dataseq = nextline.substring(8,15);
        t_payment = nextline.substring(15,23);
        t_linecode = nextline.substring(23,24);
        t_payeeno = nextline.substring(24,29);
        t_mspctlno = nextline.substring(29,35);
        t_practitionerno = nextline.substring(35,40);
        t_message = nextline.substring(40,120);
        t_filler = nextline.substring(120,165);
    }
    
    
    String[] getParams(String filename, String raNo){
        
        String[] param25 = new String[12];
        param25[0] =  raNo;
        param25[1] =  filename ; //(30),
        param25[2] =  t_s25type ; //(3),
        param25[3] =  t_datacenter ; //(5),
        param25[4] =  t_dataseq ; //(7),
        param25[5] =  t_payment ; //(8),
        param25[6] =  t_linecode ; //(1),
        param25[7] =  t_payeeno ; //(5),
        param25[8] =  t_mspctlno ; //(6),
        param25[9] =  t_practitionerno ; //(5),
        param25[10] =  t_message ; //(8),
        param25[11] =  t_filler; //(2),
        return param25;
    }
}

//filename varchar(30),
// t_s25type varchar(3),
// t_datacenter varchar(5),
// t_dataseq varchar(7),
// t_payment varchar(8),
// t_linecode varchar(1),
// t_payeeno varchar(5),
// t_mspctlno varchar(6),
// t_practitionerno varchar(5),
// t_message varchar(80),
// t_adjoutstanding varchar(9),
// t_filler varchar(46),
class S22{
    
    public S22(String line){
            parse(line);
        }
    
    String t_s22type ;
    String t_datacenter ;
    String t_dataseq ;
    String t_payment ;
    String t_linecode;
    String t_payeeno ;
    String t_mspctlno;
    String t_practitionerno ;
    String t_practitionername;
    String t_amtbilled ;
    String t_amtpaid ;
    String t_filler ;
    
    
    void parse(String nextline){
        t_s22type = nextline.substring(0,3);
        t_datacenter = nextline.substring(3,8);
        t_dataseq = nextline.substring(8,15);
        t_payment = nextline.substring(15,23);
        t_linecode = nextline.substring(23,24);
        t_payeeno = nextline.substring(24,29);
        t_mspctlno = nextline.substring(29,35);
        t_practitionerno = nextline.substring(35,40);
        t_practitionername =nextline.substring(40,65);
        t_amtbilled = nextline.substring(65,74);
        t_amtpaid = nextline.substring(74,83);
        t_filler = nextline.substring(83,165);
    }
    
   
    String[] getParams(String filename, String raNo){
        String[] param22 = new String[14];
        param22[0] =  raNo;
        param22[1] =  filename ; //(30),
        param22[2] =  t_s22type ; //(3),
        param22[3] =  t_datacenter ; //(5),
        param22[4] =  t_dataseq ; //(7),
        param22[5] =  t_payment ; //(8),
        param22[6] =  t_linecode ; //(1),
        param22[7] =  t_payeeno ; //(5),
        param22[8] =  t_mspctlno ; //(6),
        param22[9] =  t_practitionerno ; //(5),
        param22[10] =  t_practitionername ; //(8),
        param22[11] =  t_amtbilled; //(2),
        param22[12] =  t_amtpaid; //(18),
        param22[13] =  t_filler; //(10),
        return param22;
    }
}
class C12{
    
    public C12(String line){
            parse(line);
        }
    
    private String  t_c12type ;
    private String  t_datacenter ;
    private String  t_dataseq ;
    private String t_payeeno ;
    private String t_practitionerno ;
    private String t_exp1 ;
    private String t_exp2 ;
    private String t_exp3 ;
    private String t_exp4 ;
    private String t_exp5 ;
    private String t_exp6 ;
    private String t_exp7 ;
    private String t_officefolioclaimno ;
    private String  t_filler ;
    
    
    void parse(String nextline){
        t_c12type = nextline.substring(0, 3);
        t_datacenter = nextline.substring(3, 8);
        t_dataseq = nextline.substring(8, 15);
        t_payeeno = nextline.substring(15, 20);
        t_practitionerno = nextline.substring(20, 25);
        t_exp1 = nextline.substring(25, 27);
        t_exp2 = nextline.substring(27, 29);
        t_exp3 = nextline.substring(29, 31);
        t_exp4 = nextline.substring(31, 33);
        t_exp5 = nextline.substring(33, 35);
        t_exp6 = nextline.substring(35, 37);
        t_exp7 = nextline.substring(37, 39);
        t_officefolioclaimno = nextline.substring(39, 46);
        t_filler = nextline.substring(46, 70);
    }
    
     public String getBillingMasterNo(){
            return Integer.toString(Integer.parseInt(t_officefolioclaimno));
        }
   
    
    String[] getParams(String filename,String raNo){
        return new String[]{raNo,filename,t_datacenter,t_dataseq,t_payeeno,t_practitionerno,t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7,t_officefolioclaimno,t_filler};
    }
    
    String[] getParams(String filename){
        
        String[] param = new String[15];
        param[0] = filename;
        param[1] = t_datacenter;
        param[2] = t_dataseq;
        param[3] = "";//t_payment;
        param[4] = "";//t_linecode;
        param[5] = t_payeeno;
        param[6] = "";//t_mspctlno;
        param[7] = "";//t_payeename;
        param[8] = "";//t_amtbilled;
        param[9] = "";//t_amtpaid;
        param[10] ="";// t_balancefwd;
        param[11] ="";// t_cheque;
        param[12] ="";// t_newbalance;
        param[13] ="";// t_filler;
        param[14] = "D";
        return param;
    }
    
    public String getT_c12type() {
        return t_c12type;
    }
    
    public String getT_datacenter() {
        return t_datacenter;
    }
    
    public String getT_dataseq() {
        return t_dataseq;
    }
    
    public String getT_payeeno() {
        return t_payeeno;
    }
    
    public String getT_practitionerno() {
        return t_practitionerno;
    }
    
    public String getT_exp1() {
        return t_exp1;
    }
    
    public String getT_exp2() {
        return t_exp2;
    }
    
    public String getT_exp3() {
        return t_exp3;
    }
    
    public String getT_exp4() {
        return t_exp4;
    }
    
    public String getT_exp5() {
        return t_exp5;
    }
    
    public String getT_exp6() {
        return t_exp6;
    }
    
    public String getT_exp7() {
        return t_exp7;
    }
    
    public String getT_officefolioclaimno() {
        return t_officefolioclaimno;
    }
    
    public String getT_filler() {
        return t_filler;
    }
    
    
}


/////
