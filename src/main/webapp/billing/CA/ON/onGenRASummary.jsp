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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%! boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>


<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, java.net.*,oscar.*, oscar.util.*, oscar.MyDateFormat" errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>

<jsp:useBean id="billingLocalInvNoBean" class="java.util.Properties" scope="page" />

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.RaHeader" %>
<%@page import="org.oscarehr.common.dao.RaHeaderDao" %>
<%
	RaHeaderDao dao = SpringUtils.getBean(RaHeaderDao.class);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/tablefilter_all_min.js"></script>
<link rel="stylesheet" type="text/css" href="billingON.css" />
<title>Billing Reconcilliation</title>

<style>
<% if (bMultisites) { %>
	.positionFilter {position:absolute;top:2px;right:350px;display:block;}
<% } else { %>
	.positionFilter {display:none;}
<% } %>
</style>

</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<% 
String nowDate = UtilDateUtilities.DateToString(new java.util.Date(), "yyyy/MM/dd"); 

String raNo = "", flag="", plast="", pfirst="", pohipno="", proNo="";
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="", proName="";
String sqlRACO="",sqlRAOB="", OBflag="0",COflag="0", amountOB="", amountCO="";
String demo_name ="",demo_hin="", demo_docname="";
int accountno=0 ;

raNo = request.getParameter("rano");

BillingRAPrep obj = new BillingRAPrep();
String obCodes = "'P006A','P020A','P022A','P028A','P023A','P007A','P009A','P011A','P008B','P018B','E502A','C989A','E409A','E410A','E411A','H001A'";	
String colposcopyCodes = "'A004A','A005A','Z731A','Z666A','Z730A','Z720A'";
List<String> OBbilling_no = obj.getRABillingNo4Code(raNo, obCodes);
List<String> CObilling_no = obj.getRABillingNo4Code(raNo, colposcopyCodes);

Hashtable map = new Hashtable();
BigDecimal bdCFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);     	     
BigDecimal bdPFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);     	     
BigDecimal bdOFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);     	     
BigDecimal bdCOFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);     	     

BigDecimal bdFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal bdHFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigCTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigPTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigOTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigCOTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigLTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigHTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal bdOBFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigOBTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
double dHFee = 0.00;        
double dFee = 0.00;
double dCOFee = 0.00; 
double dOBFee = 0.00; 
double dCFee = 0.00;       	
double dPFee = 0.00;       	       	
double dOFee = 0.00;

double dLocalHFee = 0.00;        
BigDecimal bdLocalHFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigLocalHTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
String localServiceDate = "";
       	
proNo = request.getParameter("proNo");
//raNo = request.getParameter("rano");
if (raNo.compareTo("") == 0 || raNo == null){
	flag = "0";
	return;
} else {
%>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<form action="onGenRASummary.jsp">
	<tr class="myDarkGreen">
		<th align='LEFT'><font color="#FFFFFF"> Billing
		Reconcilliation - Summary Report</font></th>
		<th align='RIGHT'>
		<select id="loadingMsg" class="positionFilter"><option>Loading filters...</option></select>
		<select name="proNo">
			<option value="all" <%=proNo.equals("all")?"selected":""%>>All
			Providers</option>

			<%   
//
List aL = obj.getProviderListFromRAReport(raNo);
for(int i=0; i<aL.size(); i++) {
	Properties prop = (Properties) aL.get(i);
	pohipno = prop.getProperty("providerohip_no", "");
	plast = prop.getProperty("last_name", "");
	pfirst = prop.getProperty("first_name", "");
%>
			<option value="<%=pohipno%>" <%=proNo.equals(pohipno)?"selected":""%>><%=plast%>,<%=pfirst%></option>
			<%
}
%>
		</select><input type='submit' name='submit' value='Generate'> <input
			type="hidden" name="rano" value="<%=raNo%>"> <input
			type='button' name='print' value='Print' onClick='window.print()'>
		<input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
	</form>
</table>


<% 
	if (proNo == null || proNo.compareTo("") == 0 || proNo.compareTo("all") == 0){ 
%>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	class="myIvory">
	<tr class="myYellow">
		<td width="7%" height="16">Billing No</td>
		<td width="7%" height="16">Provider</td>
		<td width="15%" height="16">Patient</td>
		<td width="7%" height="16">HIN</td>
		<td width="10%" height="16">Service Date</td>
		<td width="7%" height="16">Service Code</td>
		<!-- <td width="8%" height="16">Count</td> -->
		<td width="7%" height="16" align=right>Invoiced</td>
		<td width="7%" height="16" align=right>Paid</td>
		<td width="7%" height="16" align=right>Clinic Pay</td>
		<td width="7%" height="16" align=right>Hospital Pay</td>
		<td width="7%" height="16" align=right>OB</td>
		<td width="5%" height="16" align=right>Error</td>
	</tr>

	<%

%>

	<tr>
		<td height="16"><%=account%></td>
		<td height="16"><%=demo_docname%></td>
		<td height="16"><%=demo_name%></td>
		<td height="16"><%=demo_hin%></td>
		<td height="16"><%=servicedate%></td>
		<td height="16"><%=servicecode%></td>
		<!-- <td width="8%" height="16"><%=serviceno%></td>-->
		<td height="16" align=right><%=amountsubmit%></td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right>N/A</td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right><%=amountOB%></td>
		<td height="16" align=right><%=explain%></td>
	</tr>


	<tr>
		<td height="16"><%=account%></td>
		<td height="16"><%=demo_docname%></td>
		<td height="16"><%=demo_name%></td>
		<td height="16"><%=demo_hin%></td>
		<td height="16"><%=servicedate%></td>
		<td height="16"><%=servicecode%></td>
		<!-- <td width="8%" height="16"><%=serviceno%></td>-->
		<td height="16" align=right><%=amountsubmit%></td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right>N/A</td>
		<td height="16" align=right><%=amountOB%></td>
		<td height="16" align=right><%=explain%></td>
	</tr>

	<%/*
			} else { // other fee
				dOFee = Double.parseDouble(amountpay);
				bdOFee = new BigDecimal(dOFee).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigOTotal = BigOTotal.add(bdOFee);
*/
%>
	<tr>
		<td height="16"><%=account%></td>
		<td height="16"><%=demo_docname%></td>
		<td height="16"><%=demo_name%></td>
		<td height="16"><%=demo_hin%></td>
		<td height="16"><%=servicedate%></td>
		<td height="16"><%=servicecode%></td>
		<!-- <td width="8%" height="16"><%=serviceno%></td>-->
		<td height="16" align=right><%=amountsubmit%></td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right>N/A</td>
		<td height="16" align=right>N/A</td>
		<td height="16" align=right><%=amountOB%></td>
		<td height="16" align=right><%=explain%></td>
	</tr>
	<%
//			}
//		}
//	}	 
} else { // raNo for all providers
%>

	<table id="ra_table" width="100%" border="0" cellspacing="1" cellpadding="0"
		class="myIvory">
		<tr class="myYellow">
			<th width="6%">Billing No</th>
			<td width="7%">Claim No</td>
			<!--  th width="14%">Provider </th -->
			<th width="14%">Patient</th>
			<th>Fam Doc</th>
			<th width="10%">HIN</th>
			<th width="9%">Service Date</th>
			<th width="8%">Service Code</th>
			<!-- <th width="8%">Count</th> -->
			<th width="7%" align=right>Invoiced</th>
			<th width="7%" align=right>Paid</th>
			<th width="7%" align=right>Clinic Pay</th>
			<th width="7%" align=right>Hospital Pay</th>
			<th width="7%" align=right>OB</th>
			<th align=right>Error</th>
			<th width="0" align=right style="display:none">Site</th>			
		</tr>

		<%
	aL = obj.getRASummary(raNo, proNo, OBbilling_no, CObilling_no,map);
	for(int i=0; i<aL.size()-1; i++) { //to use table-filter js to generate the sum - so the total-1
		Properties prop = (Properties) aL.get(i);
		String color = i%2==0? "class='myGreen'":"";
		color = i == (aL.size()-1) ? "class='myYellow'" : color;
%>
		<tr <%=color %>>
			<td align="center"><%=prop.getProperty("account", "&nbsp;")%></td>
			<td align="center"><%=prop.getProperty("claimNo", "&nbsp;")%></td>
			<!--  >td><%=prop.getProperty("demo_docname", "&nbsp;")%></td -->
			<td><%=prop.getProperty("demo_name", "&nbsp;")%></td>
			<td align="center"><%=prop.getProperty("demo_doc", "&nbsp;")%></td>
			<td align="center"><%=prop.getProperty("demo_hin", "&nbsp;")%></td>
			<td align="center"><%=prop.getProperty("servicedate", "&nbsp;")%></td>
			<td align="center"><%=prop.getProperty("servicecode", "&nbsp;")%></td>
			<!--<td width="8%"><%=serviceno%></td>-->
			<td align=right><%=prop.getProperty("amountsubmit", "&nbsp;")%></td>
			<td align=right><%=prop.getProperty("amountpay", "&nbsp;")%></td>
			<td align=right><%=prop.getProperty("clinicPay", "&nbsp;")%></td>
			<td align=right><%=prop.getProperty("hospitalPay", "&nbsp;")%></td>
			<td align=right><%=prop.getProperty("obPay", "&nbsp;")%></td>
			<td align=right><%=prop.getProperty("explain", "&nbsp;")%></td>
			<td width="0" style="display:none"><%=prop.getProperty("site", "")%></td>			
		</tr>

		<% } }
}
%>
<!-- added another TR for table-filter js to automatically calculate totals based on filters -->
<tr class="myYellow">
                        <td align="center"></td>
                        <td></td>
                        <td align="center"></td>
                        <td align="center"></td>
                        <td align="center"></td>
                         <td align="center"></td>
                        <td align="center">Total:</td>
                        <td id="amountSubmit" align=right></td>
                        <td id="amountPay" align=right></td>
                        <td id="clinicPay" align=right></td>
                        <td id="hospitalPay" align=right></td>
                        <td id="OBPay" align=right></td>
                        <td align=right>&nbsp;</td>
                        <td align=right  width="0" style="display:none" >&nbsp;</td>


</tr>

		<%

String transaction="", content="", balancefwd="", xtotal="", other_total="", ob_total=""; 
RaHeader rh = dao.find(Integer.parseInt(raNo));
if(rh != null) {
	transaction= SxmlMisc.getXmlContent(rh.getContent(),"<xml_transaction>","</xml_transaction>");
	balancefwd= SxmlMisc.getXmlContent(rh.getContent(),"<xml_balancefwd>","</xml_balancefwd>");
}


if(!map.isEmpty()){
    BigLTotal = (BigDecimal) map.get("xml_local");
    BigPTotal = (BigDecimal) map.get("xml_total"); 
    BigOTotal = (BigDecimal) map.get("xml_other_total"); 
    BigOBTotal= (BigDecimal) map.get("xml_ob_total"); 
    BigCOTotal= (BigDecimal) map.get("xml_co_total");
}


content = content + "<xml_transaction>" + transaction + "</xml_transaction>" + "<xml_balancefwd>" + balancefwd + "</xml_balancefwd>";
content = content + "<xml_local>" + BigLTotal + "</xml_local>"+ "<xml_total>" + BigPTotal + "</xml_total>" + "<xml_other_total>" + BigOTotal + "</xml_other_total>" + "<xml_ob_total>" + BigOBTotal + "</xml_ob_total>" + "<xml_co_total>" + BigCOTotal + "</xml_co_total>";

int recordAffected=0;
RaHeader raHeader = dao.find(Integer.parseInt(raNo));
if(raHeader != null) {
	 raHeader.setContent(content);
	 dao.merge(raHeader);
	recordAffected++;
}

%>
<script language="javascript" type="text/javascript">
        document.getElementById('loadingMsg').style.display='none';
        var totRowIndex = tf_Tag(tf_Id('ra_table'),"tr").length;
        var table_Props =       {
                                        col_0: "none",
                                        col_1: "none",
                                        col_2: "none",
                                        col_3: "none",
                                        col_4: "none",
                                        col_5: "none",
                                        col_6: "none",
                                        col_7: "none",
                                        col_8: "none",
                                        col_9: "none",
                                        col_10: "none",
                                        col_11: "none",
                                        col_12: "select",
                                        display_all_text: " [ Show all clinics ] ",
                                        flts_row_css_class: "dummy",
                                        flt_css_class: "positionFilter",
                                        sort_select: true,
                                        rows_always_visible: [totRowIndex],
                                        col_operation: {
                                                                id: ["amountSubmit","amountPay","clinicPay","hospitalPay","OBPay"],
                                                                col: [7,8,9,10,11],
                                                                operation: ["sum","sum","sum","sum","sum"],
                                                                write_method: ["innerHTML","innerHTML","innerHTML","innerHTML","innerHTML"],
                                                                exclude_row: [totRowIndex],
                                                                decimal_precision: [2,2,2,2,2],
                                                                tot_row_index: [totRowIndex]
                                                        }
                                };
        var tf = setFilterGrid( "ra_table",table_Props );

</script>
	
</body>
</html>
