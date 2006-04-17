<%
  if(session.getValue("user") == null)
    response.sendRedirect("../../../logout.jsp");
%>
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
<%@ page language="java" contentType="text/html" %>
<%@ page import="java.math.*,java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.MSP.*,oscar.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /><jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" /><%@ include file="../../../admin/dbconnection.jsp" %>
<%@ include file="dbBilling.jsp" %>
<%
  String user_no;
  user_no = (String) session.getAttribute("user");
  int  nItems=0;
  String strLimit1="0";
  String strLimit2="5";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
  BigDecimal total = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  BigDecimal paidTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

  MSPReconcile msp = new MSPReconcile();

  GregorianCalendar now=new GregorianCalendar();
     int curYear = now.get(Calendar.YEAR);
     int curMonth = (now.get(Calendar.MONTH)+1);
     int curDay = now.get(Calendar.DAY_OF_MONTH);

     int flag = 0, rowCount=0;
     //String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
     String xml_vdate            = request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
     String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
     String xml_demoNo           = request.getParameter("demographicNo")==null?"":request.getParameter("demographicNo");

     boolean defaultShow = request.getParameter("submitted")==null?true:false;

     boolean showMSP              = request.getParameter("showMSP")==null?defaultShow:!defaultShow;  //request.getParameter("showMSP");
     boolean showWCB              = request.getParameter("showWCB")==null?defaultShow:!defaultShow;  //request.getParameter("showWCB");
     boolean showPRIV             = request.getParameter("showPRIV")==null?defaultShow:!defaultShow;  //request.getParameter("showPRIV");
     boolean showICBC             = request.getParameter("showICBC")==null?defaultShow:!defaultShow;  //request.getParameter("showPRIV");

String readonly = request.getParameter("filterPatient");
String firstName = request.getParameter("firstName");
String lastName = request.getParameter("lastName");
String demographicNo = request.getParameter("demographicNo")!=null?request.getParameter("demographicNo"):"";
%>
<html>
<head>
<html:base/>
<title>Invoice List</title>
<link rel="stylesheet" type="text/css" media="all" href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<script type="text/javascript" src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>
">
</script>
<script type="text/javascript" src="../../../share/calendar/calendar-setup.js"></script>
<style type="text/css">
	<!--
	BODY                  {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD                    {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000                                                    }
	TD.black              {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	TD.lilac              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.boldlilac          {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:hover      {font-weight: normal;                                                                            color: #000000; background-color: #CDCFFF  ;}
	TD             {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; }
	TD.heading            {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	H2                    {font-weight: bold  ; font-size: 12pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H3                    {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H4                    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H6                    {font-weight: bold  ; font-size: 7pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	A:link                {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; }
	A:visited             {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; }
 	A:hover               {                                                                            color: red; }
	TD.cost               {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
	TD A:link       {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; }
	TD A:visited    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; }
	TD A:hover      {                                                                            color: #FDCB03; }
	TD.title              {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:hover      {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #CDCFFF  ;}
	#navbar               {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	SPAN.navbar A:link    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	SPAN.navbar A:visited {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #EFEFEF; background-color: #666699   ;}
	SPAN.navbar A:hover   {                                                                            color: #FDCB03; background-color: #666699   ;}
	SPAN.bold             {font-weight: bold  ;                                                                        background-color: #666699   ;}
	-->
        td.bCellData{ font-family: Arial,Helvetica,sans-serif; }
        a.billType{ font-family: Arial,Helvetica,sans-serif; text-decoration: none;}
        th.bHeaderData{ font-weight:bold; font-family: Arial,Helvetica,sans-serif; }

.tabular_list th {
	border:1px solid;
	border-color:#ddd #999 #888 #ddd;
	padding:2px;
	padding-bottom:0px;
	font-size:10pt;
}
.tabular_list td {
	border:1px solid;
	border-color:#fff #bbb #bbb #fff;
	padding:1px;
	font-size:9pt;
}
</style>
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
<script language="JavaScript">
<!--
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "attachment", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
   popup.focus();
  }
}

function popupPage2(vheight,vwidth,varpage,pagename) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, pagename, windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
   popup.focus();
  }
}
function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}
function setfocus() {
  this.focus();
}
function refresh() {
      history.go(0);

}

function fillEndDate(d){
    document.serviceform.xml_appointment_date.value= d;

}
function setDemographic(demoNo){
    document.serviceform.demographicNo.value = demoNo;
}


//-->

function billTypeOnly(showEle){
   document.serviceform.showMSP.checked = false;
   document.serviceform.showWCB.checked = false;
   document.serviceform.showPRIV.checked = false;
   document.serviceform.showICBC.checked = false;
   document.serviceform.elements[showEle].checked = true;
}
</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0" topmargin="10">

<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr bgcolor="#FFFFFF">
    <div align="right"><a href="javascript: function myFunction() {return false; }" onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')"><font face="Arial, Helvetica, sans-serif" size="1">Manage Provider List </font></a></div>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000">
    <td height="40" width="10%"></td>
    <td width="90%" align="left"><p><font color="#FFFFFF" size="4" face="Arial, Helvetica, sans-serif"><b>oscar<font size="3">Billing - Invoice List</font></b></font> </p></td>
    <td nowrap valign="bottom"><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><%=DateUtils.sumDate("yyyy-M-d","0")%></b></font> </td>
  </tr>
</table>

<%
if("true".equals(readonly)){
%>
<div>
 <i>Results for Demographic</i>
      :
<%=request.getParameter("lastName")%>      ,
<%=request.getParameter("firstName")%>      (
<%=request.getParameter("demographicNo")%>      )
</div><%}%>
<table width="100%" border="0" bgcolor="#EEEEFF">
  <form name="serviceform" method="get" action="billStatus.jsp">
  <input type="hidden" name="filterPatient" value="<%=readonly%>"/>
  <input type="hidden" name="lastName" value="<%=request.getParameter("lastName")%>"/>
  <input type="hidden" name="firstName" value="<%=request.getParameter("firstName")%>"/>
 <!--<input type="hidden" name="demographicNo" value="<%=demographicNo%>"/>-->
  <tr>
    <td rowspan="2"><table>
        <tr>
          <td class="bCellData" ><input type="checkbox" name="showMSP" value="show"  <%=showMSP?"checked":""%> />
            <a   onclick="billTypeOnly('showMSP')">MSP</a> </td>
        </tr>
        <tr>
          <td class="bCellData" ><input type="checkbox" name="showWCB" value="show"  <%=showWCB?"checked":""%> />
            <a   onclick="billTypeOnly('showWCB')">WCB</a> </td>
        </tr>
        <tr>
          <td class="bCellData" ><input type="checkbox" name="showPRIV" value="show" <%=showPRIV?"checked":""%>  />
            <a onClick="billTypeOnly('showPRIV')">Private</a> </td>
        </tr>
        <tr>
          <td class="bCellData" ><input type="checkbox" name="showICBC" value="show" <%=showICBC?"checked":""%>  />
            <a onClick="billTypeOnly('showICBC')">ICBC</a> </td>
        </tr>
      </table></td>
    <td colspan=2><div align="center"> <font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b>Select provider </b></font>
        <select name="providerview">
          <option value="all">All Providers</option>
          <%  String proFirst="";
                        String proLast="";
                        String proOHIP="";
                        String specialty_code;
                        String billinggroup_no;
                        int Count = 0;
                        ResultSet rslocal;
                        rslocal = null;
                        rslocal = apptMainBean.queryResults("billingreport", "search_reportprovider");
                        while(rslocal.next()){
                            proFirst = rslocal.getString("first_name");
                            proLast = rslocal.getString("last_name");
                            proOHIP = rslocal.getString("provider_no");
                    %>
          <option value="<%=proOHIP%>" <%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>, <%=proFirst%></option>
          <%  } %>
        </select>
        <font color="#333333" size="2" face="Verdana, Arial, Helvetica, sans-serif">
        <input type="hidden" name="verCode" value="V03"/>
        <input type="submit" name="Submit" value="Create Report">
        </font> </div></td>
  </tr>
  <tr>
    <td class="bCellData" ><div align="center"> <font color="#333333">Service Date-Range</font> &nbsp;&nbsp; <font size="1" face="Arial, Helvetica, sans-serif"> <a href="javascript: function myFunction() {return false; }" id="hlSDate">Begin:</a> </font>
        <input type="text" name="xml_vdate" id="xml_vdate" value="<%=xml_vdate%>">
        <font size="1" face="Arial, Helvetica, sans-serif"> <a href="javascript: function myFunction() {return false; }" id="hlADate" >End:</a> </font>
        <input type="text" name="xml_appointment_date" id="xml_appointment_date" value="<%=xml_appointment_date%>">
        <a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-M-d","-30")%>')" >30</a>&nbsp; <a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-M-d","-60")%>')" >60</a>&nbsp; <a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-M-d","-90")%>')" >90</a>&nbsp;

        Demographic:
		<%
			String readonlyStr = "true".equals(readonly)?"readonly":"";
		%>
        <input type="text" name="demographicNo" size="5" value="<%=xml_demoNo%>" <%=readonlyStr%>"/>
      </div></td>
    <td class="bCellData"><div align="right">
        <input type='button' name='print' value='Print' onClick='window.print()'>
      </div></td>
  </tr>
</table>
<% String billTypes = request.getParameter("billTypes");
System.out.println("billTypes" + billTypes);
if (billTypes == null){
    billTypes = MSPReconcile.REJECTED;
}

%>
<table>
  <tr>
    <td><input type="radio" name="billTypes" value="<%=MSPReconcile.REJECTED%>"     <%=billTypes.equals(MSPReconcile.REJECTED)?"checked":""%>/>
      Rejected
      <input type="radio" name="billTypes" value="<%=MSPReconcile.NOTSUBMITTED%>" <%=billTypes.equals(MSPReconcile.NOTSUBMITTED)?"checked":""%>/>
      Not Submitted
      <input type="radio" name="billTypes" value="<%=MSPReconcile.SUBMITTED%>"    <%=billTypes.equals(MSPReconcile.SUBMITTED)?"checked":""%>/>
      Submitted
      <input type="radio" name="billTypes" value="<%=MSPReconcile.SETTLED%>"      <%=billTypes.equals(MSPReconcile.SETTLED)?"checked":""%>/>
      Settled
      <input type="radio" name="billTypes" value="<%=MSPReconcile.DELETED%>"      <%=billTypes.equals(MSPReconcile.DELETED)?"checked":""%>/>
      Deleted
      <input type="radio" name="billTypes" value="<%=MSPReconcile.HELD%>"         <%=billTypes.equals(MSPReconcile.HELD)?"checked":""%>/>
      Held
      <input type="radio" name="billTypes" value="<%=MSPReconcile.DATACENTERCHANGED%>" title="Data Center Changed" <%=billTypes.equals(MSPReconcile.DATACENTERCHANGED)?"checked":""%>/>
      DCC
      <input type="radio" name="billTypes" value="<%=MSPReconcile.PAIDWITHEXP%>" title="Paid with Explanation"     <%=billTypes.equals(MSPReconcile.PAIDWITHEXP)?"checked":""%>/>
      PwE
      <input type="radio" name="billTypes" value="<%=MSPReconcile.BADDEBT%>"      <%=billTypes.equals(MSPReconcile.BADDEBT)?"checked":""%>/>
      Bad Debt
      <input type="radio" name="billTypes" value="<%=MSPReconcile.REFUSED%>"      <%=billTypes.equals(MSPReconcile.REFUSED)?"checked":""%>/>
      Refused
      <!--<input type="radio" name="billTypes" value="<%=MSPReconcile.WCB%>"          <%=billTypes.equals(MSPReconcile.WCB)?"checked":""%>/> WCB-->
      <input type="radio" name="billTypes" value="<%=MSPReconcile.CAPITATED%>"   title="Capitated"   <%=billTypes.equals(MSPReconcile.CAPITATED)?"checked":""%>/>
      Cap
      <input type="radio" name="billTypes" value="<%=MSPReconcile.DONOTBILL%>"   title="Do Not Bill"    <%=billTypes.equals(MSPReconcile.DONOTBILL)?"checked":""%>/>
      DNBill
      <input type="radio" name="billTypes" value="<%=MSPReconcile.BILLPATIENT%>"  <%=billTypes.equals(MSPReconcile.BILLPATIENT)?"checked":""%>/>
      Bill Patient
      <input type="radio" name="billTypes" value="<%=MSPReconcile.PAIDPRIVATE%>" title="Paid Private"  <%=billTypes.equals(MSPReconcile.PAIDPRIVATE)?"checked":""%>/>
      PPrivate
      <input type="radio" name="billTypes" value="<%=MSPReconcile.COLLECTION%>"  title="Transfered to Collection"<%=billTypes.equals(MSPReconcile.COLLECTION)?"checked":""%>/>
      Collection
      <input type="radio" name="billTypes" value="%"                              <%=billTypes.equals("%")?"checked":""%>/>
      All
      <input type="hidden" name="submitted" value="yes"/>
    </td>
  </tr>
</table>
</form>
<table class="tabular_list" width="100%" border="2"  valign="top">
<thead>
	<tr bgcolor="#CCCCFF">
	<th align="center" class="bHeaderData" title="INVOICE #" >INVOICE # </th>
	<th align="center" class="bHeaderData" title="LINE #" >LINE # </th>
    <th align="center" class="bHeaderData" title="APP. DATE">APP. DATE</th>
	<th align="center" class="bHeaderData" title="TYPE" >TYPE </th>
	<%
		if(!"true".equals(readonly)){
		System.out.println("READ:"+readonly);
	%>
    <th align="center" class="bHeaderData" title="PATIENT" >PATIENT</th>
	<%}%>
	 <th align="center" class="bHeaderData" title="PRACT" >PRACT.</th>
	<th align="center" class="bHeaderData" title="Status">STAT</th>


    <th align="center" class="bHeaderDate" title="Fee Code">FEE CODE</th>
    <th align="center" class="bHeaderDate" title="QTY">QTY</th>
    <th align="center" class="bHeaderDate" title="Amount Billed">AMT</th>
    <th align="center" class="bHeaderDate" title="Amount Paid"  >PAID</th>
    <th align="center" class="bHeaderDate" >DX CODE </th>
    <th align="center" class="bHeaderData" >MSGS</th>
  </tr>
</thead>

  <%

    String dateBegin = request.getParameter("xml_vdate");
    String dateEnd = request.getParameter("xml_appointment_date");
    String demoNo = request.getParameter("demographicNo");

    ArrayList list;

    MSPReconcile.BillSearch bSearch = msp.getBills(billTypes, providerview, dateBegin ,dateEnd,demoNo,!showWCB,!showMSP,!showPRIV,!showICBC);
    list = bSearch.list;
    Properties p2 = bSearch.getCurrentErrorMessages();
    Properties p = msp.currentC12Records();
    boolean bodd = true;
    boolean incorrectVal = false;
    boolean paidinCorrectval = false;
    System.out.println(list.size()+" in this billing list");
	String currentBillingNo = "";
    for (int i = 0; i < list.size(); i++){

      incorrectVal = false;
      paidinCorrectval = false;
      MSPReconcile.Bill b = (MSPReconcile.Bill) list.get(i);


	  /** Getting the number of lines per bill **/
	   ResultSet rsbillingmaster_count = apptMainBean.queryResults(new String[] {b.billing_no}, "search_billingmaster_count");
     int billingmaster_count = 0;
     if (rsbillingmaster_count.next()) {
          billingmaster_count = rsbillingmaster_count.getInt(1);
     }
     rsbillingmaster_count.close();


      bodd=currentBillingNo.equals(b.billing_no) ? !bodd : bodd; //for the color of rows
      nItems++; //to calculate if it is the end of records
      String rejected = isRejected(b.billMasterNo,p,b.isWCB());
      String rejected2  = isRejected(b.billMasterNo,p2,b.isWCB());
        BigDecimal valueToAdd = new BigDecimal("0.00");
      try{
        valueToAdd = new BigDecimal(b.amount).setScale(2, BigDecimal.ROUND_HALF_UP);
      }catch(Exception badValueException){
        System.out.println(" Error calculating value for "+b.billMasterNo);
        incorrectVal = true;
      }
      total = total.add(valueToAdd);
      String pAmount = msp.getAmountPaid(b.billMasterNo,b.billingtype);
        BigDecimal valueToPaidAdd = new BigDecimal("0.00");
      try{
        valueToPaidAdd = new BigDecimal(pAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
      }catch(Exception badValueException){
        System.out.println(" Error calculating paid value for "+b.billMasterNo);
        paidinCorrectval = true;
      }
      paidTotal = paidTotal.add(valueToPaidAdd);

   %>
  <tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">

    <%if (!currentBillingNo.equals(b.billing_no)) {    %>
      <td align="center" class="bCellData" rowspan="<%=billingmaster_count%>">
        <%if("Pri".equals(b.billingtype)){%>
	 	<a href="javascript:popupPage(800,800, '../../../billing/CA/BC/billingView.do?billing_no=<%=b.billing_no%>&receipt=yes')"><%=b.billing_no%>       </a>
		<%}
	 else{
		%>
	 	<%=b.billing_no%>
		<%}%>		    </td>
		<%}%>

	<td align="center" class="bCellData" ><%=b.billMasterNo%></td>
	<td align="center" class="bCellData" ><%=b.apptDate%></td>
	<td align="center" class="bCellData" ><%=b.billingtype%></td>
	<%
		if(!"true".equals(readonly)){
	%>
   <td align="center" class="bCellData" ><a href="javascript: setDemographic('<%=b.demoNo%>');"><%=b.demoName%></a></td>
	<%}%>
	<td align="center" class="bCellData" ><%=b.providerLastName%>,<%=b.providerFirstName%></td>
	 <td align="center" class="bCellData" title="<%=msp.getStatusDesc(b.reason)%>" ><%=msp.getStatusDesc(b.reason)==null?"&nbsp":msp.getStatusDesc(b.reason)%></td>


    <td align="center" class="bCellData" ><%=b.code%></td>
    <td align="center" class="bCellData" <%=isBadVal(incorrectVal)%> ><%=b.quantity%></td>
    <td align="center" class="bCellData" <%=isBadVal(incorrectVal)%> ><%=b.amount%> </td>
    <td align="center" class="bCellData" ><a href="javascript: function myFunction() {return false; }" onClick="popupPage2(500,1020,'genTAS00ByOfficeNo.jsp?officeNo=<%=b.billMasterNo%>','RecValues');"> <%=pAmount%> </a> </td>
    <td align="center" class="bCellData" ><%=s(b.dx1)%></td>

    <td><%if (!b.isWCB()){%>
      <a href="javascript: popupPage(700,700,'adjustBill.jsp?billing_no=<%=b.billMasterNo%>')" >Edit </a>
      <%}else{%>
      <a href="javascript: popupPage(700,700,'billingTeleplanCorrectionWCB.jsp?billing_no=<%=b.billMasterNo%>')" >Edit </a>
      <%}%>
      <%=rejected%><%=rejected2%> </td>
  </tr>
  <%  //}
    rowCount = rowCount + 1;
     currentBillingNo = b.billing_no;
	 bodd=!bodd;
    }
    if (rowCount == 0) {
    %>
  <tr bgcolor="<%=bodd?"ivory":"white"%>">
    <td colspan="13" align="center" class="bCellData"> No bills </td>
  </tr>
  <% }%>
  <tr>
  <%
	String colspan = !"true".equals(readonly)?"4":"3";
 %>
  <td colspan="<%=colspan%>" align="center" class="bCellData">&nbsp;</td>
    <td align="center" class="bCellData" >Count:</td>

    <td align="center" class="bCellData" ><%=list.size()%></td>
    <td align="center" class="bCellData" >&nbsp;</td>
    <td align="center" class="bCellData" >Total:</td>
    <td align="center" class="bCellData" ><%=total.toString()%></td>
    <td align="center" class="bCellData" ><%=paidTotal.toString()%></td>
    <td align="center" class="bCellData" >&nbsp;</td>
  </tr>
</table>
<script language='javascript'>
       Calendar.setup({inputField:"xml_vdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});
       Calendar.setup({inputField:"xml_appointment_date",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlADate",singleClick:true,step:1});
</script>
<script language="javascript" src="../../../commons/scripts/sort_table/css.js">
<script language="javascript" src="../../../commons/scripts/sort_table/common.js">
<script language="javascript" src="../../../commons/scripts/sort_table/standardista-table-sorting.js">

</body>
</html>
<%!
String getReasonEx(String reason){
    if (reason.equals("N")) reason="Do Not Bill ";
    if (reason.equals("O")) reason="Bill MSP ";
    if (reason.equals("W")) reason="Bill WCB ";
    if (reason.equals("H")) reason="Capitated Bill ";
    if (reason.equals("P")) reason="Bill Patient";
    return reason;
}

String isRejected(String billingNo,Properties p,boolean wcb){
    String s = "&nbsp;";
    if (p.containsKey(billingNo)){
        if (!wcb){
        s = "<a href=\"javascript: popupPage(700,700,'adjustBill.jsp?billing_no="+billingNo+"')\" > "+p.getProperty(billingNo)+"</a>";
        }else{
        s = "<a href=\"javascript: popupPage(700,700,'billingTeleplanCorrectionWCB.jsp?billing_no="+billingNo+"')\" > "+p.getProperty(billingNo)+"</a>";
        }
    }
    return s;
}

    String moneyFormat(String str){
        String moneyStr = "0.00";
        try{
            moneyStr = new java.math.BigDecimal(str).movePointLeft(2).toString();
        }catch (Exception moneyException) {}
    return moneyStr;
    }

    String s(String str){
        if (str == null || str.length() == 0 ){
            str = "&nbsp;";
        }
        return str;
    }

    String isBadVal(boolean valBad){
        String retval = "";
        if (valBad){
            retval = "style=\"background-color: red\" title=\"Unprocessable Value: value will not be included in total\"";
        }
        return retval;
    }
%>
