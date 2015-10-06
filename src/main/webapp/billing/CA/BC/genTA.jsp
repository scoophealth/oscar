<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat,oscar.oscarBilling.ca.bc.MSP.*,oscar.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<jsp:useBean id="documentBean" class="oscar.DocumentBean" scope="request" />

<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.billing.CA.BC.model.TeleplanS21"%>
<%@ page import="org.oscarehr.billing.CA.BC.dao.TeleplanS21Dao"%>
<%@ page import="org.oscarehr.billing.CA.BC.model.TeleplanS00"%>
<%@ page import="org.oscarehr.billing.CA.BC.dao.TeleplanS00Dao"%>
<%@ page import="org.oscarehr.billing.CA.BC.model.TeleplanS23"%>
<%@ page import="org.oscarehr.billing.CA.BC.dao.TeleplanS23Dao"%>
<%@ page import="org.oscarehr.billing.CA.BC.model.TeleplanS25"%>
<%@ page import="org.oscarehr.billing.CA.BC.dao.TeleplanS25Dao"%>
<%@ page import="org.oscarehr.billing.CA.BC.model.TeleplanS22"%>
<%@ page import="org.oscarehr.billing.CA.BC.dao.TeleplanS22Dao"%>
<%@ page import="org.oscarehr.billing.CA.BC.model.TeleplanC12"%>
<%@ page import="org.oscarehr.billing.CA.BC.dao.TeleplanC12Dao"%>
<%
	TeleplanS21Dao teleplanS21Dao = SpringUtils.getBean(TeleplanS21Dao.class);
	TeleplanS00Dao teleplanS00Dao = SpringUtils.getBean(TeleplanS00Dao.class);
	TeleplanS23Dao teleplanS23Dao = SpringUtils.getBean(TeleplanS23Dao.class);
	TeleplanS25Dao teleplanS25Dao = SpringUtils.getBean(TeleplanS25Dao.class);
	TeleplanS22Dao teleplanS22Dao = SpringUtils.getBean(TeleplanS22Dao.class);
	TeleplanC12Dao teleplanC12Dao = SpringUtils.getBean(TeleplanC12Dao.class);
%>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);
  MSPReconcile mspReconcile = new MSPReconcile();
%>


<%
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", newhin="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="", strcount="", strtCount="";
String amtbilled="", amtpaid="", balancefwd="", chequeamt="", newbalance="";
int accountno=0, totalsum=0, txFlag=0, recFlag=0, flag=0, payFlag=0, count = 0, tCount=0, amountPaySum=0, amountSubmitSum=0;
String raNo = "";
String t_s22type="",t_s23type="",t_s25type="",t_s00type="",t_datacenter="", t_dataseq="", t_payment="", t_linecode="", t_payeeno="", t_mspctlno="", t_payeename="", t_amtbilled="", t_amtpaid="",t_balancefwd="",t_cheque="",t_newbalance="",t_filler="";
String t_practitionerno="",t_msprcddate="", t_initial="", t_surname="", t_phn="", t_phndepno="", t_servicedate="", t_today="", t_billnoservices ="", t_billclafcode="", t_billfeeschedule="";
String t_billamt="", t_paidnoservices="", t_paidclafcode="", t_paidfeeschedule ="", t_paidamt ="", t_officeno="", t_exp1="",  t_exp2="",t_exp3="", t_exp4="",t_exp5="", t_exp6="",t_exp7="";
String t_ajc1="",t_aja1="",t_ajc2="",t_aja2="",t_ajc3="",t_aja3="", t_ajc4="",t_aja4="",t_ajc5="",t_aja5="",t_ajc6="",t_aja6="",t_ajc7="",t_aja7="";
String t_paidrate="", t_planrefno="",t_claimsource="",t_previouspaiddate ="",t_icbcwcb="", t_insurercode="";
String t_ajc ="", t_aji ="", t_ajm ="", t_calcmethod="",  t_rpercent="",  t_opercent="", t_gamount="", t_ramount="",t_oamount="", t_adjmade ="",t_adjoutstanding ="",t_c12type = "" ,t_officefolioclaimno = "";
String t_practitionername="", t_message="" ;
ResultSet rslocal;
filename = documentBean.getFilename();

String forwardPage = "viewReconcileReports.jsp";

filepath = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");

FileInputStream file = new FileInputStream(filepath + filename);
InputStreamReader reader = new InputStreamReader(file);
BufferedReader input = new BufferedReader(reader);
String nextline;

while ((nextline=input.readLine())!=null){
   header = nextline.substring(0,3);
   if (header.compareTo("S21") == 0) {
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

       raNo = "";

       String[] param2 = new String[3];
       param2[0] = filename;
       param2[1] = t_payment;
       param2[2] = t_payeeno;

       for(TeleplanS21 result : teleplanS21Dao.findByFilenamePaymentPayeeNo(filename,t_payment, t_payeeno)) {
    	   raNo = result.getId().toString();
       }
      
       if (raNo.compareTo("") == 0 || raNo == null){
          recFlag = 1;

          TeleplanS21 t = new TeleplanS21();
          t.setFileName(filename);
          t.setDataCentre(t_datacenter);
          t.setDataSeq(t_dataseq);
          t.setPayment(t_payment);
          t.setLineCode(t_linecode.toCharArray()[0]);
          t.setPayeeNo(t_payeeno);
          t.setMspCtlNo(t_mspctlno);
          t.setPayeeName(t_payeename);
          t.setAmountBilled(t_amtbilled);
          t.setAmountPaid(t_amtpaid);
          t.setBalanceForward(t_balancefwd);
          t.setCheque(t_cheque);
          t.setNewBalance(t_newbalance);
          t.setFiller(t_filler);
          t.setStatus('N');

          teleplanS21Dao.persist(t);
          int rowsAffected=1;
          raNo = t.getId().toString();

       }
   }else if (header.compareTo("S01") == 0){
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

        if(recFlag >0){

			TeleplanS00 t = new TeleplanS00();
			t.setS21Id(Integer.parseInt(raNo));
			t.setFileName(filename);
			t.setS00Type(t_s00type);
			t.setDataCentre(t_datacenter);
	        t.setDataSeq(t_dataseq);
	        t.setPayment(t_payment);
	        t.setLineCode(t_linecode.toCharArray()[0]);
	        t.setPayeeNo(t_payeeno);
	        t.setMspCtlNo(t_mspctlno);
	        t.setPractitionerNo(t_practitionerno);
	        t.setMspRcdDate(t_msprcddate);
	      	t.setInitial("");
	      	t.setSurname("");
	      	t.setPhn("");
	      	t.setPhnDepNo("");
	      	t.setServiceDate("");
	      	t.setToday("");
	      	t.setBillNoServices("");
	      	t.setBillClafCode("");
	      	t.setPaidFeeSchedule("");
	      	t.setBillAmount("");
	      	t.setPaidNoServices("");
	      	t.setPaidClafCode("");
	      	t.setPaidFeeSchedule("");
	      	t.setPaidAmount(t_paidamt);
	      	t.setOfficeNo(t_officeno);
	      	t.setExp1("");
	      	t.setExp2("");
	      	t.setExp3("");
	      	t.setExp4("");
	      	t.setExp5("");
	      	t.setExp6("");
	      	t.setExp7("");
	      	t.setAjc1(t_ajc1);
	      	t.setAja1(t_aja1);
	      	t.setAjc2(t_ajc2);
	      	t.setAja2(t_aja2);
	      	t.setAjc3(t_ajc3);
	      	t.setAja3(t_aja3);
	      	t.setAjc4(t_ajc4);
	      	t.setAja4(t_aja4);
	      	t.setAjc5(t_ajc5);
	      	t.setAja5(t_aja5);
	      	t.setAjc6(t_ajc6);
	      	t.setAja6(t_aja6);
	      	t.setAjc7(t_ajc7);
	      	t.setAja7(t_aja7);
			t.setPaidRate(t_paidrate);
			t.setPlanRefNo("");
			t.setClaimSource("");
			t.setPreviousPaidDate("");
			t.setIcBcWcb(t_icbcwcb);
			t.setInsurerCode(t_insurercode);
			t.setFiller(t_filler);
            teleplanS00Dao.persist(t);
            int rowsAffected00 = 1;
            mspReconcile.updateStat(MSPReconcile.SETTLED,Integer.toString(Integer.parseInt(t_officeno)));

            //UPDate Table for settle e
        }


   }else if (header.compareTo("S02") == 0 || header.compareTo("S00") == 0 || header.compareTo("S03") == 0){
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

        if (recFlag >0) {
           recFlag = recFlag +1;

			TeleplanS00 t = new TeleplanS00();
			t.setS21Id(Integer.parseInt(raNo));
			t.setFileName(filename);
			t.setS00Type(t_s00type);
			t.setDataCentre(t_datacenter);
	        t.setDataSeq(t_dataseq);
	        t.setPayment(t_payment);
	        t.setLineCode(t_linecode.toCharArray()[0]);
	        t.setPayeeNo(t_payeeno);
	        t.setMspCtlNo(t_mspctlno);
	        t.setPractitionerNo(t_practitionerno);
	        t.setMspRcdDate(t_msprcddate);
	      	t.setInitial(t_initial);
	      	t.setSurname(t_surname);
	      	t.setPhn(t_phn);
	      	t.setPhnDepNo(t_phndepno);
	      	t.setServiceDate(t_servicedate);
	      	t.setToday(t_today);
	      	t.setBillNoServices(t_billnoservices);
	      	t.setBillClafCode(t_billclafcode);
	      	t.setPaidFeeSchedule(t_paidfeeschedule);
	      	t.setBillAmount(t_billamt);
	      	t.setPaidNoServices(t_paidnoservices);
	      	t.setPaidClafCode(t_paidclafcode);
	      	t.setPaidFeeSchedule(t_paidfeeschedule);
	      	t.setPaidAmount(t_paidamt);
	      	t.setOfficeNo(t_officeno);
	      	t.setExp1(t_exp1);
	      	t.setExp2(t_exp2);
	      	t.setExp3(t_exp3);
	      	t.setExp4(t_exp4);
	      	t.setExp5(t_exp5);
	      	t.setExp6(t_exp6);
	      	t.setExp7(t_exp7);
	      	t.setAjc1(t_ajc1);
	      	t.setAja1(t_aja1);
	      	t.setAjc2(t_ajc2);
	      	t.setAja2(t_aja2);
	      	t.setAjc3(t_ajc3);
	      	t.setAja3(t_aja3);
	      	t.setAjc4(t_ajc4);
	      	t.setAja4(t_aja4);
	      	t.setAjc5(t_ajc5);
	      	t.setAja5(t_aja5);
	      	t.setAjc6(t_ajc6);
	      	t.setAja6(t_aja6);
	      	t.setAjc7(t_ajc7);
	      	t.setAja7(t_aja7);
			t.setPaidRate(t_paidrate);
			t.setPlanRefNo(t_planrefno);
			t.setClaimSource(t_claimsource);
			t.setPreviousPaidDate(t_previouspaiddate);
			t.setIcBcWcb(t_icbcwcb);
			t.setInsurerCode(t_insurercode);
			t.setFiller(t_filler);

			teleplanS00Dao.persist(t);
			int rowsAffected02=1;

           if(header.equals("S02")){ //header.compareTo("S00") == 0 || header.compareTo("S03") == 0){
              mspReconcile.updateStat(MSPReconcile.PAIDWITHEXP,Integer.toString(Integer.parseInt(t_officeno)));
           }else if (header.equals("S03")){
           mspReconcile.updateStat(MSPReconcile.REFUSED,Integer.toString(Integer.parseInt(t_officeno)));
           }else if (header.equals("S00")){
           mspReconcile.updateStat(MSPReconcile.DATACENTERCHANGED,Integer.toString(Integer.parseInt(t_officeno)));
           }
        }
   }else if (header.compareTo("S04") == 0){
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

        if (recFlag >0) {

			TeleplanS00 t = new TeleplanS00();
			t.setS21Id(Integer.parseInt(raNo));
			t.setFileName(filename);
			t.setS00Type(t_s00type);
			t.setDataCentre(t_datacenter);
	        t.setDataSeq(t_dataseq);
	        t.setPayment(t_payment);
	        t.setLineCode(t_linecode.toCharArray()[0]);
	        t.setPayeeNo(t_payeeno);
	        t.setMspCtlNo(t_mspctlno);
	        t.setPractitionerNo(t_practitionerno);
	        t.setMspRcdDate(t_msprcddate);
	      	t.setInitial("");
	      	t.setSurname("");
	      	t.setPhn("");
	      	t.setPhnDepNo("");
	      	t.setServiceDate("");
	      	t.setToday("");
	      	t.setBillNoServices("");
	      	t.setBillClafCode("");
	      	t.setPaidFeeSchedule("");
	      	t.setBillAmount("");
	      	t.setPaidNoServices("");
	      	t.setPaidClafCode("");
	      	t.setPaidFeeSchedule("");
	      	t.setPaidAmount("");
	      	t.setOfficeNo(t_officeno);
	      	t.setExp1(t_exp1);
	      	t.setExp2(t_exp2);
	      	t.setExp3(t_exp3);
	      	t.setExp4(t_exp4);
	      	t.setExp5(t_exp5);
	      	t.setExp6(t_exp6);
	      	t.setExp7(t_exp7);
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
			t.setIcBcWcb(t_icbcwcb);
			t.setInsurerCode(t_insurercode);
			t.setFiller(t_filler);

			teleplanS00Dao.persist(t);
			int rowsAffected04 =1;

           mspReconcile.updateStat(MSPReconcile.HELD,Integer.toString(Integer.parseInt(t_officeno)));
        }
   } else if (header.compareTo("S23") == 0||header.compareTo("S24")==0){


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


        if (recFlag >0) {

        	TeleplanS23 t = new TeleplanS23();
			t.setS21Id(Integer.parseInt(raNo));
			t.setFileName(filename);
			t.setS23Type(t_s23type);
			t.setDataCentre(t_datacenter);
	        t.setDataSeq(t_dataseq);
	        t.setPayment(t_payment);
	        t.setLineCode(t_linecode.toCharArray()[0]);
	        t.setPayeeNo(t_payeeno);
	        t.setMspCtlNo(t_mspctlno);
	       	t.setAjc(t_ajc);
	       	t.setAji(t_aji);
	       	t.setAjm(t_ajm);
	       	t.setCalcMethod(t_calcmethod);
	       	t.setrPercent(t_rpercent);
	       	t.setoPercent(t_opercent);
	        t.setgAmount(t_gamount);
	        t.setrAmount(t_ramount);
	        t.setoAmount(t_oamount);
	       	t.setBalanceForward(t_balancefwd);
	       	t.setAdjMade(t_adjmade);
	        t.setAdjOutstanding(t_adjoutstanding);
	        t.setFiller(t_filler);
	        teleplanS23Dao.persist(t);
            int rowsAffected23 = 1;
        }
   } else if (header.compareTo("S25") == 0){

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


        if (recFlag >0) {

        	TeleplanS25 t = new TeleplanS25();
			t.setS21Id(Integer.parseInt(raNo));
			t.setFileName(filename);
			t.setS25Type(t_s25type);
			t.setDataCentre(t_datacenter);
	        t.setDataSeq(t_dataseq);
	        t.setPayment(t_payment);
	        t.setLineCode(t_linecode.toCharArray()[0]);
	        t.setPayeeNo(t_payeeno);
	        t.setMspCtlNo(t_mspctlno);
	        t.setPractitionerNo(t_practitionerno);
	       	t.setMessage(t_message);
	        t.setFiller(t_filler);

			teleplanS25Dao.persist(t);
            int rowsAffected25 = 1;
        }
   } else if (header.compareTo("S22") == 0){

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


        if (recFlag >0) {

        	TeleplanS22 t = new TeleplanS22();
			t.setS21Id(Integer.parseInt(raNo));
			t.setFileName(filename);
			t.setS22Type(t_s22type);
			t.setDataCentre(t_datacenter);
	        t.setDataSeq(t_dataseq);
	        t.setPayment(t_payment);
	        t.setLineCode(t_linecode.toCharArray()[0]);
	        t.setPayeeNo(t_payeeno);
	        t.setMspCtlNo(t_mspctlno);
	        t.setPractitionerNo(t_practitionerno);
	        t.setPractitionerName(t_practitionername);
	        t.setAmountBilled(t_amtbilled);
	        t.setAmountPaid(t_amtpaid);
	       	t.setFiller(t_filler);

	       	teleplanS22Dao.persist(t);
	        int rowsAffected22 =1;
        }
   }else if (header.compareTo("C12") == 0){
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
                if (raNo.equals("")){
                    
                    for(TeleplanS21 result : teleplanS21Dao.findByFilenamePaymentPayeeNo(filename,t_payment, t_payeeno)) {
                 	   raNo = result.getId().toString();
                    }
                    if (raNo.compareTo("") == 0 || raNo == null){
                        recFlag = 1;

                        TeleplanS21 t = new TeleplanS21();
                        t.setFileName(filename);
                        t.setDataCentre(t_datacenter);
                        t.setDataSeq(t_dataseq);
                        t.setPayment(t_payment);
                        t.setLineCode(t_linecode.toCharArray()[0]);
                        t.setPayeeNo(t_payeeno);
                        t.setMspCtlNo(t_mspctlno);
                        t.setPayeeName(t_payeename);
                        t.setAmountBilled(t_amtbilled);
                        t.setAmountPaid(t_amtpaid);
                        t.setBalanceForward(t_balancefwd);
                        t.setCheque(t_cheque);
                        t.setNewBalance(t_newbalance);
                        t.setFiller(t_filler);
                        t.setStatus('D');

                        teleplanS21Dao.persist(t);
                        raNo = t.getId().toString();
                    }
                }
                if (recFlag > 0){
                		TeleplanC12 t = new TeleplanC12();
                		t.setS21Id(Integer.parseInt(raNo));
                        t.setFileName(filename);
                        t.setDataCentre(t_datacenter);
                        t.setDataSeq(t_dataseq);
                        t.setPayeeNo(t_payeeno);
                        t.setPractitionerNo(t_practitionerno);
            	      	t.setExp1(t_exp1);
            	      	t.setExp2(t_exp2);
            	      	t.setExp3(t_exp3);
            	      	t.setExp4(t_exp4);
            	      	t.setExp5(t_exp5);
            	      	t.setExp6(t_exp6);
            	      	t.setExp7(t_exp7);
            	      	t.setOfficeFolioClaimNo(t_officefolioclaimno);
            	      	t.setFiller(t_filler);
						teleplanC12Dao.persist(t);
                        mspReconcile.updateStat(MSPReconcile.REJECTED,Integer.toString(Integer.parseInt(t_officefolioclaimno )));
                }
                forwardPage = "billStatus.jsp";
        }
//
}


%>

<jsp:forward page="<%=forwardPage%>" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<html:base />
<link rel="stylesheet" href="../../../billing/billing.css">
<title>Billing Reconcilliation</title>

<script language="JavaScript">
<!--
    var remote=null;
    function refresh() {
        history.go(0);
    }
    function rs(n,u,w,h,x) {
      args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
      remote=window.open(u,n,args);
      if (remote != null) {
        if (remote.opener == null)
          remote.opener = self;
      }
      if (x == 1) { return remote; }
    }

    var awnd=null;
    function popPage(url) {
        awnd=rs('',url ,400,200,1);
        awnd.focus();
    }

    function checkReconcile(url){
        if(confirm("You are about to reconcile the file, are you sure?")) {
            location.href=url;
        }else{
            alert("You have cancel the action!");
        }
    }
//-->
</SCRIPT>
</head>

<body bgcolor="#EBF4F5" text="#000000" leftmargin="0" topmargin="0"
	marginwidth="0" marginheight="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF">Billing Reconcilliation </font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>

<table width="100%" border="1" cellspacing="0" cellpadding="0"
	bgcolor="#EFEFEF">
	<form>
	<tr>

		<td width="5%" height="16">Payment Date</td>
		<td width="10%" height="16">Payable</td>
		<td width="10%" height="16">Amount Billed</td>
		<td width="10%" height="16">Amount Paid</td>
		<td width="10%" height="16">Balance Fwd</td>
		<td width="10%" height="16">Cheque Amount</td>
		<td width="10%" height="16">New Balance</td>
		<td width="20%" height="16">Action</td>
		<td width="5%" height="16">Status</td>
	</tr>

	<%
	for(TeleplanS21 result : teleplanS21Dao.search_all_tahd("D")) {
        raNo  = result.getId().toString();
        paymentdate = result.getPayment();
        payable = result.getPayeeName();
        amtbilled= result.getAmountBilled();
        amtpaid = result.getAmountPaid();
        balancefwd = result.getBalanceForward();
        chequeamt= result.getCheque();
        newbalance = result.getNewBalance();
        //total = rsdemo.getString("totalamount");
   %>

	<tr>
		<td><%=paymentdate%></td>
		<td><%=payable%></td>
		<td><%=amtbilled%></td>
		<td><%=amtpaid%></td>
		<td><%=balancefwd%></td>
		<td><%=chequeamt%></td>
		<td><%=newbalance%></td>
		<td><a href="genTAS01.jsp?rano=<%=raNo%>&proNo=" target="_blank">Billed</a>
		| <a href="genTAS00.jsp?rano=<%=raNo%>&proNo=" target="_blank">Detail</a>|
		<a href="genTAS22.jsp?rano=<%=raNo%>&proNo=" target="_blank">Summary</a></td>
		<td><%=result.getStatus()%></td>
	</tr>

	<% }%>

</table>

</body>
</html>
