<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%  
if(session.getValue("user") == null) response.sendRedirect("../../../logout.jsp");
%>

<%@ page
	import="java.io.*, java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbBilling.jspf"%>

<% 
String raNo = "", note="", htmlContent="", transaction="", messages="";
raNo = request.getParameter("rano");
//note = request.getParameter("note");
String filepath="", filename = "", header="", headerCount="", total="", new_total="",other_total="",local_total="",co_total="", ob_total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", newhin="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="", strcount="", strtCount="";
String balancefwd="",abf_ca="", abf_ad="",abf_re="",abf_de="";
String trans_code="",cheque_indicator="", trans_date="",trans_amount="", trans_message="";
String message="", message_txt="";
String xml_ra="", HTMLtransaction="";
int accountno=0, totalsum=0, txFlag=0, recFlag=0, flag=0, payFlag=0, count = 0, tCount=0, amountPaySum=0, amountSubmitSum=0;

ResultSet rslocal = apptMainBean.queryResults(raNo, "search_rahd_content");
while(rslocal.next()){
	filename=rslocal.getString("filename");
	HTMLtransaction= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_transaction>","</xml_transaction>");
	htmlContent= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_balancefwd>","</xml_balancefwd>");
	new_total = SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_total>","</xml_total>");
	local_total= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_local>","</xml_local>");
	other_total= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_other_total>","</xml_other_total>");
	ob_total= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_ob_total>","</xml_ob_total>");
	co_total= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_co_total>","</xml_co_total>");
}

filepath = oscarVariables.getProperty("DOCUMENT_DIR").trim();
FileInputStream file = new FileInputStream(filepath + filename);
InputStreamReader reader = new InputStreamReader(file);
BufferedReader input = new BufferedReader(reader);
String nextline;
while ((nextline=input.readLine())!=null){
	header = nextline.substring(0,1);
	if (header.compareTo("H") == 0) { 
		headerCount = nextline.substring(2,3);

		if (headerCount.compareTo("1") == 0){
			paymentdate = nextline.substring(21,29);
			payable = nextline.substring(29,59);
			total = nextline.substring(59,68);
			totalStatus = nextline.substring(68,69);
			deposit = nextline.substring(69,77);

			totalsum = Integer.parseInt(total);
			if (totalsum == 0){ 
				total = "0.00"; 
			}else{
				total = String.valueOf(totalsum);
				total = total.substring(0, total.length()-2) + "." + total.substring(total.length()-2) + totalStatus;      
			}
		}

		if (headerCount.compareTo("6") == 0){
			// balancefwd = "<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td colspan='4'>Balance Forward Record - Amount Brought Forward (ABF)</td></tr><tr><td>Claims Adjustment</td><td>Advances</td><td>Reductions</td><td>Deductions</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr></table>";
			abf_ca = nextline.substring(3,10)+"."+nextline.substring(10,13);
			abf_ad = nextline.substring(13,20)+"."+nextline.substring(20,23);
			abf_re = nextline.substring(23,30)+"."+nextline.substring(30,33);
			abf_de = nextline.substring(33,40)+"."+nextline.substring(40,43);
		}


		if (headerCount.compareTo("7") == 0){
			trans_code = nextline.substring(3,5);
			if (trans_code.compareTo("10")==0) trans_code="Advance";
			if (trans_code.compareTo("20")==0) trans_code="Reduction";
			if (trans_code.compareTo("30")==0) trans_code="Unused";
			if (trans_code.compareTo("40")==0) trans_code="Advance repayment";
			if (trans_code.compareTo("50")==0) trans_code="Accounting adjustment";
			if (trans_code.compareTo("70")==0) trans_code="Attachments";
			cheque_indicator = nextline.substring(5,6);
			if (cheque_indicator.compareTo("M")==0) cheque_indicator="Manual Cheque issued";
			if (cheque_indicator.compareTo("C")==0) cheque_indicator="Computer Cheque issued";
			if (cheque_indicator.compareTo("I")==0) cheque_indicator="Interim payment Cheque/Direct Bank Deposit issued";
			if (cheque_indicator.compareTo(" ")==0 || cheque_indicator.compareTo("N")==0) cheque_indicator="No Cheque issued";
			trans_date = nextline.substring(6,14);
			trans_amount= nextline.substring(14,20)+"."+nextline.substring(20,23);
			trans_message = nextline.substring(23,73);

			transaction = transaction + "<tr><td width='14%'>"+trans_code+"</td><td width='12%'>"+trans_date+"</td><td width='17%'>"+cheque_indicator+"</td><td width='13%'>"+trans_amount+"</td><td width='44%'>"+trans_message+"</td></tr>";
		} 

		if (headerCount.compareTo("4") == 0){
			count = count + 1;
		}

		if (headerCount.compareTo("5") == 0){
			tCount = tCount +1;
		}

		if (headerCount.compareTo("8") == 0){
			message_txt = message_txt + nextline.substring(3,73)+"<br>";
		}
	}
}

if (transaction.compareTo("") != 0){
	transaction = "<xml_transaction><table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td colspan='5'>Accounting Transaction Record</td></tr><tr><td width='14%'>Transaction</td><td width='12%'>Transaction Date</td><td width='17%'>Cheque Issued</td><td width='13%'>Amount</td><td width='44%'>Message</td></tr>"+ transaction + "</table></xml_transaction>";
}

balancefwd = "<xml_balancefwd><table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td colspan='4'>Balance Forward Record - Amount Brought Forward (ABF)</td></tr><tr><td>Claims Adjustment</td><td>Advances</td><td>Reductions</td><td>Deductions</td></tr><tr><td>"+abf_ca+"</td><td>"+abf_ad+"</td><td>"+abf_re+"</td><td>"+abf_de+"</td></tr></table></xml_balancefwd>";

xml_ra = transaction + balancefwd +"<xml_local>" + local_total + "</xml_local>" +"<xml_cheque>"+total+"</xml_cheque>" + "<xml_total>"+new_total+"</xml_total>" + "<xml_other_total>"+other_total+"</xml_other_total>" + "<xml_ob_total>"+ob_total+"</xml_ob_total>" +
"<xml_co_total>"+co_total+"</xml_co_total>";	 

String[] param3 =new String[6];
param3[0]=total;
param3[1]=String.valueOf(count);
param3[2]=String.valueOf(tCount);
param3[3]=xml_ra;
param3[4]=paymentdate;
param3[5]=filename;
int rowsAffected1 = apptMainBean.queryExecuteUpdate(param3,"update_rahd");

rslocal = apptMainBean.queryResults(raNo, "search_rahd_content");
while(rslocal.next()){
	filename=rslocal.getString("filename");
	HTMLtransaction= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_transaction>","</xml_transaction>");
	htmlContent= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_balancefwd>","</xml_balancefwd>");
	new_total = SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_total>","</xml_total>");
	other_total= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_other_total>","</xml_other_total>");
	local_total= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_local>","</xml_local>");
	ob_total= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_ob_total>","</xml_ob_total>");
	co_total= SxmlMisc.getXmlContent(rslocal.getString("content"),"<xml_co_total>","</xml_co_total>");
}

file.close();
reader.close();
input.close();
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR Project</title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
<!--



//-->
</script>
</head>

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="left">
		<form><input type="button" onclick="window.print()"
			value="Print"></form>
		</th>
		<th align="center"><font face="Helvetica" color="#FFFFFF">
		Reconcillation Report </font></th>
		<th align="right">
		<form><input type="button"
			onClick="popupPage(700,600,'billingClipboard.jsp')" value="Clipboard"></form>
		</th>
	</tr>
</table>

Cheque amount:
<%=total%>
<br>
<%="Local clinic "%>:
<%=local_total%>
<br>
Other clinic :
<%=other_total%><br>

OB Total :
<%=ob_total%><br>
Colposcopy Total :
<%=co_total%><br>

<br>
<br>
<table bgcolor="#EEEEEE" bordercolor="#666666" border="1">
	<%=htmlContent%>
</table>
<br>
<table bgcolor="#EEEEFF" bordercolor="#666666" border="1">
	<%=transaction%>
</table>
<pre><%=message_txt%></pre>

</body>
</html>
