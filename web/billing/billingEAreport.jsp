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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

 <%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat" errorPage="errorpage.jsp" %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<jsp:useBean id="documentBean" class="oscar.DocumentBean" scope="request" /> 
<%@ include file="../billing/dbBilling.jsp" %>
<% 
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);
%>
   

<% 
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="",payee="",  account="",  provincecode="", newhin="", hin="", ver="", dob="", billtype="", location="";
String servicedate="", serviceno="",dxcode="",  servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="", strcount="", strtCount="";
String balancefwd="",abf_ca="", abf_ad="",abf_re="",abf_de="";
String transaction="",trans_code="",cheque_indicator="", trans_date="",trans_amount="", trans_message="";
String message="", message_txt="";
String xml_ra="";
int accountno=0, totalsum=0, txFlag=0, recFlag=0, flag=0, payFlag=0, count = 0, tCount=0, amountPaySum=0, amountSubmitSum=0;
 String raNo = "";
String recordLength="", msgType="", filler="", recordImage="";
String techSpec="", MOHoffice="", providerNumber="", groupNumber="", operatorNumber="", specialtyCode="", stationNumber="", claimProcessDate="", referNumber="", facilityNumber="", admitDate="", referLab="";
String eCode1="", eCode2="", eCode3="", eCode4="", eCode5="";
String reCode1="",reCode2="", reCode3="", reCode4="", reCode5="";
String heCode1="",heCode2="", heCode3="", heCode4="", heCode5="";
String regNumber="", patient_last="", patient_first="", patient_sex="", province_code="";
String header1Count="", header2Count="", itemCount="", messageCount="";
String batchNumber="", batchCreateDate="", batchSequenceNumber="", microStart="", microEnd="", microType="", batchProcessDate="", claimNumber="", recordNumber="";
String mailDate="", mailTime="", mailFile="";
String obecHIN="", obecVer="", obecResponse="", obecId="", obecSex="", obecDOB="", obecExpiry="", obecLast="", obecFirst="", obecSecond="", obecMOH="";
%>



      
 
   

<html>
<head>
<link rel="stylesheet" href="../billing/billing.css" >
<title>Billing 
      Reconcilliation</title>
      
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
  }
else{
alert("You have cancel the action!");
}
}
//-->
</SCRIPT>
</head>

<body bgcolor="#EBF4F5" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%
filename = documentBean.getFilename();

 String url=request.getRequestURI();
   
        url = url.substring(1);
        url = url.substring(0,url.indexOf("/")); 

filepath = "/usr/local/OscarDocument/" + url +"/document/";
FileInputStream file = new FileInputStream(filepath + filename);
String ReportName="", ReportFlag="";
if (filename.substring(0,1).compareTo("E")==0) {ReportName="Claims Error Report"; ReportFlag="A";}
if (filename.substring(0,1).compareTo("B")==0){ ReportName="Claim Batch Acknowledgement Report"; ReportFlag="B";}
if (filename.substring(0,1).compareTo("X")==0){ ReportName="Claim File Rejection Report"; ReportFlag="C";}
if (filename.substring(0,1).compareTo("R")==0){ ReportName="EDT OBEC Output Specification"; ReportFlag="D";}
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%"  onLoad="setfocus()" rightmargin="0" topmargin="0" leftmargin="0">
    <tr bgcolor="#486ebd">
     <th align='LEFT'>
		<input type='button' name='print' value='Print' onClick='window.print()'> </th> 
    <th align='CENTER'  ><font face="Arial, Helvetica, sans-serif" color="#FFFFFF"> 
 <%=ReportName%></font></th>
      <th align='RIGHT'><input type='button' name='close' value='Close' onClick='window.close()'></th>
  </tr>
</table>
<%
if (ReportFlag.compareTo("C") == 0){

%>
<pre>M01 | Message Reason         Length     Msg Type   Filler  Record Image</pre>
<pre>M02 | File:    File Name    Date:   Mail Date   Time: Mail Time     Process Date</pre>
<%
InputStreamReader reader = new InputStreamReader(file);
BufferedReader input = new BufferedReader(reader);
String nextline;
while ((nextline=input.readLine())!=null){

  headerCount = nextline.substring(2,3);
   if (headerCount.compareTo("1") == 0){

recordLength=nextline.substring(23,28);
msgType=nextline.substring(28,31);
filler=nextline.substring(32,39);
error=nextline.substring(39,76);
explain=nextline.substring(3,23);
%>
<pre>M01 | <%=explain%>   <%=recordLength%>   <%=msgType%>   <%=filler%>   <%=URLEncoder.encode(error)%></pre>
<%
}
 if (headerCount.compareTo("2") == 0){

mailFile=nextline.substring(8,20);
mailDate=nextline.substring(25,33);
mailTime=nextline.substring(38,44);
batchProcessDate=nextline.substring(50,58);
%>
<pre>M02 | File:   <%=mailFile%>    Date:   <%=mailDate%>   Time: <%=mailTime%>     PDate: <%=batchProcessDate%></pre>
<%
}
}
%>    
<%
} else{
%>


<%
if (ReportFlag.compareTo("B") == 0){

%>
<table width="102%" border="1" cellspacing="2" cellpadding="2" bgcolor="#F1E9FE">
  <tr bgcolor="#CCCCFF"> 
    <td>Batch #</td>
    <td>Oper.#</td>
    <td>Create Date</td>
    <td>Seq#</td>
    <td>Rec Start</td>
    <td>Rec End</td>
    <td>Rec Type</td>
    <td>Group#</td>
    <td>Provider #</td>
    <td>Claims</td>
    <td>Records</td>
        <td>Batch Process Date</td>
    <td>Reject Reason</td>
  </tr>

<%
InputStreamReader reader = new InputStreamReader(file);
BufferedReader input = new BufferedReader(reader);
String nextline;
while ((nextline=input.readLine())!=null){
 headerCount = nextline.substring(2,3);
  if (headerCount.compareTo("1") == 0){

batchNumber=nextline.substring(6,11);
operatorNumber=nextline.substring(11,17);

providerNumber=nextline.substring(56,62); 
groupNumber=nextline.substring(52,56);
batchCreateDate=nextline.substring(17,25);
batchSequenceNumber=nextline.substring(25,29);
microStart=nextline.substring(29,40);
microEnd=nextline.substring(40,45);
microType=nextline.substring(45,52);
claimNumber=nextline.substring(62,67);
recordNumber=nextline.substring(67,73);
batchProcessDate=nextline.substring(73,81);
explain=nextline.substring(81,111);
%>
  <tr bgcolor="#F9F1FE"> 
    <td><%=batchNumber%> &nbsp;</td>
    <td><%=operatorNumber%> &nbsp;</td>
    <td><%=batchCreateDate%> &nbsp;</td>
    <td><%=batchSequenceNumber%> &nbsp;</td>
    <td><%=microStart%>  &nbsp;</td>
    <td><%=microEnd%> &nbsp;</td>
    <td><%=microType%>&nbsp;</td>
    <td><%=groupNumber%> &nbsp;</td>
    <td><%=providerNumber%>&nbsp;</td>
    <td><%=claimNumber%> &nbsp;</td>
    <td><%=recordNumber%> &nbsp;</td>
        <td>  <%=batchProcessDate%>&nbsp;</td>
    <td> <%=explain%>&nbsp;</td>
  </tr>


<%

}
}

%>  </table>  
<%
} else{
%>
<%
if (ReportFlag.compareTo("D") == 0){
// String obecHIN="", obecVer="", obecResponse="", obecId="", obecSex="", obecDOB="", obecExpiry="", obecLast="", obecFirst="", obecSecond="", obecMOH="";

%>
<table width="102%" border="1" cellspacing="2" cellpadding="2" bgcolor="#F1E9FE">
  <tr bgcolor="#CCCCFF"> 
    <td>Health #</td>
    <td>Ver</td>
    <td>Response Code</td>
    <td>Identifier</td>
    <td>Sex</td>
    <td>DOB</td>
    <td>Expiry</td>
    <td>Last Name</td>
    <td>First Name</td>
    <td>Second Name</td>
    <td>Reserved for MOH</td>
      
  </tr>

<%
InputStreamReader reader = new InputStreamReader(file);
BufferedReader input = new BufferedReader(reader);
String nextline;
while ((nextline=input.readLine()) != null){

 if (nextline.length() > 2){

obecHIN=nextline.substring(0,10);
obecVer=nextline.substring(10,12);
obecResponse=nextline.substring(12,14); 

if (nextline.length() > 14){
obecId=nextline.substring(14,18);
obecSex=nextline.substring(18,19);
obecDOB=nextline.substring(19,27);
obecExpiry=nextline.substring(27,35);
obecLast=nextline.substring(35,65);
obecFirst=nextline.substring(65,85);
obecSecond=nextline.substring(85,105);
obecMOH=nextline.substring(105,207);

}
}




%>
  <tr bgcolor="#F9F1FE"> 
    <td><%=obecHIN%> &nbsp;</td>
    <td><%=obecVer%> &nbsp;</td>
    <td><%=obecResponse%> &nbsp;</td>
    <td><%=obecId%> &nbsp;</td>
    <td><%=obecSex%>  &nbsp;</td>
    <td><%=obecDOB%> &nbsp;</td>
    <td><%=obecExpiry%>&nbsp;</td>
    <td><%=obecLast%> &nbsp;</td>
    <td><%=obecFirst%>&nbsp;</td>
    <td><%=obecSecond%> &nbsp;</td>
    <td><%=obecMOH%> &nbsp;</td>

  </tr>


<%

}


%>  </table>  

<%
} else{
%>




<%
if (ReportFlag.compareTo("A") == 0){

%>


<%
InputStreamReader reader = new InputStreamReader(file);
BufferedReader input = new BufferedReader(reader);
String nextline;
while ((nextline=input.readLine())!=null){

  headerCount = nextline.substring(2,3);
   if (headerCount.compareTo("1") == 0){

techSpec=nextline.substring(3,6);
MOHoffice=nextline.substring(6,7);
providerNumber=nextline.substring(27,33); 
groupNumber=nextline.substring(23,27);
operatorNumber=nextline.substring(17,23);
specialtyCode=nextline.substring(33,35);
stationNumber=nextline.substring(35,38);
claimProcessDate=nextline.substring(38,46);

%>
<table width="100%" border="0" cellspacing="2" cellpadding="2" bgcolor="#CCCCFF">
  <tr> 
    <td width="15%"><b>MOH Office: <%=MOHoffice%>  </b></td>
    <td width="15%"><b>Provider #:       <%=providerNumber%>  </b></td>
    <td width="11%"><b>Group #:  <%= groupNumber%>  </b></td>
    <td width="11%"><b>Opr.#: <%=operatorNumber%></b></td>
    <td width="11%"><b>Sp. Code: <%=specialtyCode%></b></td>
    <td width="11%"><b>Spec.#: <%=techSpec%>    </b></td>
    <td width="11%"><b>Station #:    <%=stationNumber%>     </b></td>
     <td width="15%"><b>Clm Date: <%=claimProcessDate%></b></td>
   
 </tr>
</table><table width="100%" border="0" cellspacing="2" cellpadding="2" bgcolor="#F1E9FE">
  <tr> 
    <td width="10%">Health#</td>
    <td width="6%">D.O.B</td>
    <td width="7%">Invoice #</td>
    <td width="3%">Type</td>
    <td width="9%">Ref Phy#</td>
    <td width="7%">Hosp #</td>
    <td width="9%">Admitted</td>
    <td width="5%">Claim Errors</td>
    <td width="5%">Code</td>
    <td width="6%">Fee Unit</td>
    <td width="4%"> Unit</td>
  
  <td width="7%">Date</td>
    <td width="4%">Diag</td>
    <td width="2%">Exp.</td>
    <td width="12%">Code Error</td>

<%
}
%>    
<%
   if (headerCount.compareTo("H") == 0){

hin=nextline.substring(3,13);
ver=nextline.substring(13,15);
dob=nextline.substring(15,23); 
account=nextline.substring(23,31);
billtype=nextline.substring(31,34);
payee=nextline.substring(34,35);
referNumber=nextline.substring(35,41);
facilityNumber=nextline.substring(41,45);
admitDate=nextline.substring(45,53);
referLab = nextline.substring(53,57);
location = nextline.substring(57,61);
heCode1 = nextline.substring(64,67);
heCode2 = nextline.substring(67,70);
heCode3 = nextline.substring(70,73);
heCode4 = nextline.substring(73,76);
heCode5 = nextline.substring(76,79);



%>

<%
}
%>    
<%

   if (headerCount.compareTo("R") == 0){

regNumber=nextline.substring(3,15);
patient_last=nextline.substring(15,24);
patient_first=nextline.substring(24,29); 
patient_sex=nextline.substring(29,30);
province_code=nextline.substring(30,32);
reCode1 = nextline.substring(64,67);
reCode2 = nextline.substring(67,70);
reCode3 = nextline.substring(70,73);
reCode4 = nextline.substring(73,76);
reCode5 = nextline.substring(76,79);



%>
  <tr bgcolor="#F9F1FE"> 
    <td width="23%" colspan="3"><%=patient_last%>, &nbsp;<%=patient_first%> </td>
    <td width="3%"> <%=patient_sex%> </td>
    <td width="9%"><%=province_code%></td>
      <td width="65%" colspan="10"> <%=reCode1%>   &nbsp;<%=reCode2%>   &nbsp;<%=reCode3%>   &nbsp;<%=reCode4%>   &nbsp;<%=reCode5%>&nbsp;</td>
  </tr>
<%
}
%>    


<%

   if (headerCount.compareTo("T") == 0){

servicecode=nextline.substring(3,8);
amountsubmit=nextline.substring(10,16);
serviceno=nextline.substring(16,18); 
servicedate=nextline.substring(18,26);
dxcode=nextline.substring(26,30);
eCode1 = nextline.substring(64,67);
eCode2 = nextline.substring(67,70);
eCode3 = nextline.substring(70,73);
eCode4 = nextline.substring(73,76);
eCode5 = nextline.substring(76,79);



%>
  <tr bgcolor="#F9F1FE"> 
    <td width="10%"><%=hin%> &nbsp; <%=ver%>  </td>
    <td width="6%"><%=dob%></td>
    <td width="7%"><%=account%> </td>
    <td width="3%">    <%=payee%> </td>
    <td width="9%"><%=referNumber%></td>
    <td width="7%">  <%=facilityNumber%>   </td>
    <td width="9%">   <%=admitDate%></td>
    <td width="9%"> <%=heCode1%>  &nbsp;<%=heCode2%>  &nbsp;<%=heCode3%>  &nbsp;<%=heCode4%>  &nbsp;<%=heCode5%>&nbsp;</td>
    <td width="5%"><%=servicecode%></td>
    <td width="6%"><%=amountsubmit%></td>
    <td width="4%"> <%=serviceno%></td>
    <td width="7%">   <%= servicedate%></td>
    <td width="4%">  <%=dxcode%></td>
        <td width="2%"></td>
    <td width="12%"> <%=eCode1%>   &nbsp;<%=eCode2%>   &nbsp;<%=eCode3%>   &nbsp;<%=eCode4%>   &nbsp;<%=eCode5%>&nbsp;</td>
  </tr>
<%
hin="";
ver="";
dob="";
account="";
billtype="";
payee="";
referNumber="";
facilityNumber="";
admitDate="";
referLab = "";
location = "";
heCode1 ="";
heCode2 ="";
heCode3 ="";
heCode4 ="";
heCode5 ="";

}
%>    


<%

   if (headerCount.compareTo("8") == 0){

explain = nextline.substring(3,5);
error = nextline.substring(5,60);



%>
  <tr> 
    <td width="20%"><b>Error/Description</b></td>
 <td width="20%"><%=explain%>    </td>
 <td width="60%"><%=error%> </td>

 </tr>

<%
}
%>    


<%

   if (headerCount.compareTo("9") == 0){

header1Count=nextline.substring(3,10);
header2Count=nextline.substring(10,17);
itemCount=nextline.substring(17,24); 
messageCount=nextline.substring(24,31);



%>
  </tr>

</table>
<table width="100%" border="0" cellspacing="2" cellpadding="2" bgcolor="#CCCCFF">
  <tr> 
    <td width="20%"><b>Record Coutns: [  </b></td>
    <td width="20% "><b>Header 1:   <%=header1Count%>     </b></td>
    <td width="20%"><b>Header 2:   <%=header2Count%></b></td>
    <td width="20%"><b>Item: <%=itemCount%>  </b></td>
    <td width="20%"><b>Message: <%=messageCount%> ]</b></td>

   
 </tr>
</table>
<%
}

%>    

<%
}
}
}
}
}
%>    
</body>
</html>
