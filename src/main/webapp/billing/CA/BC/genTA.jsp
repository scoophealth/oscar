<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat,oscar.oscarBilling.ca.bc.MSP.*,oscar.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="documentBean" class="oscar.DocumentBean"
	scope="request" />

<%@ include file="dbBilling.jspf"%>

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

       //| s21_id       | int(10)     |      | PRI | NULL    | auto_increment |
       //| filename     | varchar(30) | YES  |     | NULL    |                |
       //| t_datacenter | varchar(5)  | YES  |     | NULL    |                |
       //| t_dataseq    | varchar(7)  | YES  |     | NULL    |                |
       //| t_payment    | varchar(8)  | YES  |     | NULL    |                |
       //| t_linecode   | char(1)     | YES  |     | NULL    |                |
       //| t_payeeno    | varchar(5)  | YES  |     | NULL    |                |
       //| t_mspctlno   | varchar(6)  | YES  |     | NULL    |                |
       //| t_payeename  | varchar(25) | YES  |     | NULL    |                |
       //| t_amtbilled  | varchar(9)  | YES  |     | NULL    |                |
       //| t_amtpaid    | varchar(9)  | YES  |     | NULL    |                |
       //| t_balancefwd | varchar(9)  | YES  |     | NULL    |                |
       //| t_cheque     | varchar(9)  | YES  |     | NULL    |                |
       //| t_newbalance | varchar(9)  | YES  |     | NULL    |                |
       //| t_filler     | varchar(61) | YES  |     | NULL    |                |

       raNo = "";
   
       String[] param2 = new String[3];
       param2[0] = filename;
       param2[1] = t_payment;
       param2[2] = t_payeeno;
             
       ResultSet rsdemo = null;
       rsdemo = apptMainBean.queryResults(param2, "search_tahd");
       while (rsdemo.next()) {   
          raNo = rsdemo.getString("s21_id");
       }
             
       if (raNo.compareTo("") == 0 || raNo == null){           
          recFlag = 1;                     
	  String[] param =new String[15];
	  param[0]=filename;
	  param[1]=t_datacenter;
	  param[2]=t_dataseq; 
	  param[3]=t_payment;
	  param[4]=t_linecode;
	  param[5]=t_payeeno;
	  param[6]=t_mspctlno;
	  param[7]=t_payeename;
	  param[8]=t_amtbilled;
	  param[9]=t_amtpaid;
	  param[10]=t_balancefwd;
	  param[11]= t_cheque;
	  param[12]=t_newbalance;
	  param[13]=t_filler;
	  param[14]="N";

	  int rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_tahd");
          rsdemo = null;
	  rsdemo = apptMainBean.queryResults(param2, "search_tahd");
	  while (rsdemo.next()) {   
	     raNo = rsdemo.getString("s21_id");
	  }
       }
   }else if (header.compareTo("S01") == 0){
   
       //  s00_id int(10) NOT NULL auto_increment,
       //  s21_id int(10) NOT NULL,
       //  filename varchar(30),
       //  t_s00type varchar(3),
       //  t_datacenter varchar(5),
       //  t_dataseq varchar(7),
       //  t_payment varchar(8),
       //  t_linecode varchar(1),
       //  t_payeeno varchar(5),
       //  t_mspctlno varchar(6),
       //  t_practitionerno varchar(5),
       //  t_msprcddate varchar(8),
       //  t_initial varchar(2), 
       //  t_surname varchar(18),
       //  t_phn varchar(10),
       //  t_phndepno varchar(2),
       //  t_servicedate varchar(8),
       //  t_today varchar(2),
       //  t_billnoservices varchar(3),
       //  t_billclafcode varchar(2),
       //  t_billfeeschedule varchar(5),
       //  t_billamt varchar(7),
       //  t_paidnoservices varchar(3),
       //  t_paidclafcode varchar(2),
       //  t_paidfeeschedule varchar(5),
       //  t_paidamt varchar(7),
       //  t_officeno varchar(7),
       //  t_exp1 varchar(2),
       //  t_exp2 varchar(2),
       //  t_exp3 varchar(2),
       //  t_exp4 varchar(2),
       //  t_exp5 varchar(2),
       //  t_exp6 varchar(2),
       //  t_exp7 varchar(2),
       //  t_ajc1 varchar(2),
       //  t_aja1 varchar(7),
       //  t_ajc2 varchar(2),
       //  t_aja2 varchar(7),
       //  t_ajc3 varchar(2),
       //  t_aja3 varchar(7),
       //  t_ajc4 varchar(2),
       //  t_aja4 varchar(7),
       //  t_ajc5 varchar(2),
       //  t_aja5 varchar(7),
       //  t_ajc6 varchar(2),
       //  t_aja6 varchar(7),
       //  t_ajc7 varchar(2),
       //  t_aja7 varchar(7),
       //  t_paidrate varchar(2),
       //  t_planrefno varchar(10),
       //  t_claimsource varchar(10),
       //  t_previouspaiddate varchar(8),
       //  t_icbcwcb varchar(8),
       //  t_insurercode varchar(2),
       //  t_filler varchar(87),

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
            int rowsAffected00 = apptMainBean.queryExecuteUpdate(param00,"save_tadt");
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
           param02[47] =  t_paidrate ; //(2),
           param02[48] =  t_planrefno ; //(10),
           param02[49] =  t_claimsource ; //(10),
           param02[50] =  t_previouspaiddate ; //(8),
           param02[51] =  t_icbcwcb ; //(8),
           param02[52] =  t_insurercode ; //(2),
           param02[53] =  t_filler ; //(87),
           int rowsAffected02 = apptMainBean.queryExecuteUpdate(param02,"save_tadt");
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
           int rowsAffected04 = apptMainBean.queryExecuteUpdate(param04,"save_tadt");
           mspReconcile.updateStat(MSPReconcile.HELD,Integer.toString(Integer.parseInt(t_officeno)));
        }     
   } else if (header.compareTo("S23") == 0||header.compareTo("S24")==0){
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
                int rowsAffected23 = apptMainBean.queryExecuteUpdate(param23,"save_tadt_s23");
        }  
   } else if (header.compareTo("S25") == 0){
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
            int rowsAffected25 = apptMainBean.queryExecuteUpdate(param25,"save_tadt_s25");
        }
   } else if (header.compareTo("S22") == 0){
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
	        int rowsAffected22 = apptMainBean.queryExecuteUpdate(param22,"save_tadt_s22");	       
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
                    String[] param2 = {filename, t_payment, t_payeeno};
                    ResultSet rsdemo = apptMainBean.queryResults(param2, "search_tahd");
                    while (rsdemo.next()){
                            raNo = rsdemo.getString("s21_id");
                    }
                    if (raNo.compareTo("") == 0 || raNo == null){
                        recFlag = 1;
                        String[] param = new String[15];
                        param[0] = filename;
                        param[1] = t_datacenter;
                        param[2] = t_dataseq;
                        param[3] = t_payment;
                        param[4] = t_linecode;
                        param[5] = t_payeeno;
                        param[6] = t_mspctlno;
                        param[7] = t_payeename;
                        param[8] = t_amtbilled;
                        param[9] = t_amtpaid;
                        param[10] = t_balancefwd;
                        param[11] = t_cheque;
                        param[12] = t_newbalance;
                        param[13] = t_filler;
                        param[14] = "D";
                        apptMainBean.queryExecuteUpdate(param, "save_tahd");
                        rsdemo = apptMainBean.queryResults(param2, "search_tahd");
                        while (rsdemo.next()){
                            raNo = rsdemo.getString("s21_id");
                        }
                    }
                }
                if (recFlag > 0){
                        apptMainBean.queryExecuteUpdate(new String[]{raNo,filename,t_datacenter,t_dataseq,t_payeeno,t_practitionerno,t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7,t_officefolioclaimno,t_filler}, "save_tadt_C12");
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
    ResultSet rsdemo;
    rsdemo = null;
    String[] param5 =new String[1];
    param5[0] = "D";
    rsdemo = apptMainBean.queryResults(param5, "search_all_tahd");
    while (rsdemo.next()) {   
        raNo  = rsdemo.getString("s21_id");
        paymentdate = rsdemo.getString("t_payment");
        payable = rsdemo.getString("t_payeename");
        amtbilled= rsdemo.getString("t_amtbilled");
        amtpaid = rsdemo.getString("t_amtpaid");
        balancefwd = rsdemo.getString("t_balancefwd");
        chequeamt= rsdemo.getString("t_cheque");
        newbalance = rsdemo.getString("t_newbalance");
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
		<td><%=rsdemo.getString("status")%></td>
	</tr>

	<% }%>

</table>

</body>
</html>