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
<%@ page import="java.math.*,java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.MSP.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="../../../admin/dbconnection.jsp" %>
<%@ include file="dbBilling.jsp" %>


<%      
  if(session.getValue("user") == null)
    response.sendRedirect("../../../logout.jsp");
  String user_no; 
  user_no = (String) session.getAttribute("user");
  int  nItems=0;    
  String strLimit1="0"; 
  String strLimit2="5";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;

  MSPReconcile msp = new MSPReconcile();

  GregorianCalendar now=new GregorianCalendar();
     int curYear = now.get(Calendar.YEAR);
     int curMonth = (now.get(Calendar.MONTH)+1);
     int curDay = now.get(Calendar.DAY_OF_MONTH);

     int flag = 0, rowCount=0;
     //String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
     String xml_vdate            = request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
     String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>

<html>
<head> 
<html:base/>
<title>Billing Report</title>

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
        td.bCellData{ font-weight:bold; font-family: Arial,Helvetica,sans-serif; }
        th.bHeaderData{ font-weight:bold; font-family: Arial,Helvetica,sans-serif; }
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
//-->
</script>

 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0" topmargin="10">
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr bgcolor="#FFFFFF"> 
        <div align="right"><a href="javascript: function myFunction() {return false; }" onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')"><font face="Arial, Helvetica, sans-serif" size="1">Manage Provider List </font></a></div>
      </tr>
   </table>
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr bgcolor="#000000"> 
        <td height="40" width="10%"></td>
        <td width="90%" align="left"> 
          <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4">oscar<font size="3">Billing</font></font></b></font> 
          </p>
        </td>
      </tr>
   </table>

<table width="100%" border="0" bgcolor="#EEEEFF">
  <form name="serviceform" method="get" action="billStatus.jsp">
    <tr>  
      <td colspan=2>         
        <div align="center">
            <font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b>Select provider </b></font> 
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
                <input type="hidden" name="verCode" value="V03">
                <input type="submit" name="Submit" value="Create Report">
            </font>
        </div>
      </td>
    </tr>
    <tr>          
      <td class="bCellData" > 
        <div align="center">
          <font color="#333333">Service Date-Range</font>            
          &nbsp;&nbsp;
          <font size="1" face="Arial, Helvetica, sans-serif">
          <a href="javascript: function myFunction() {return false; }" onClick="openBrWindow('billingCalendarPopup.jsp?type=&returnItem=xml_vdate&returnForm=serviceform&year=<%=curYear%>&month=<%=curMonth%>','','width=300,height=300')">Begin:</a>
          </font> 
          <input type="text" name="xml_vdate" value="<%=xml_vdate%>">          
        
       
          <font size="1" face="Arial, Helvetica, sans-serif">
          <a href="javascript: function myFunction() {return false; }" onClick="openBrWindow('billingCalendarPopup.jsp?type=&returnItem=xml_appointment_date&returnForm=serviceform&year=<%=curYear%>&month=<%=curMonth%>','','width=300,height=300')">End:</a>
          </font> 
          <input type="text" name="xml_appointment_date" value="<%=xml_appointment_date%>">        
        </div>
      </td>
      <td class="bCellData"> 
        <div align="right"><input type='button' name='print' value='Print' onClick='window.print()'>                      
        </div>
      </td>
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
      <td>
        <input type="radio" name="billTypes" value="<%=MSPReconcile.REJECTED%>"     <%=billTypes.equals(MSPReconcile.REJECTED)?"checked":""%>/> Rejected
        <input type="radio" name="billTypes" value="<%=MSPReconcile.NOTSUBMITTED%>" <%=billTypes.equals(MSPReconcile.NOTSUBMITTED)?"checked":""%>/> Not Submitted
        <input type="radio" name="billTypes" value="<%=MSPReconcile.SUBMITTED%>"    <%=billTypes.equals(MSPReconcile.SUBMITTED)?"checked":""%>/> Submitted
        <input type="radio" name="billTypes" value="<%=MSPReconcile.SETTLED%>"      <%=billTypes.equals(MSPReconcile.SETTLED)?"checked":""%>/> Settled
        <input type="radio" name="billTypes" value="<%=MSPReconcile.DELETED%>"      <%=billTypes.equals(MSPReconcile.DELETED)?"checked":""%>/> Deleted
        <input type="radio" name="billTypes" value="<%=MSPReconcile.HELD%>"         <%=billTypes.equals(MSPReconcile.HELD)?"checked":""%>/> Held
        <input type="radio" name="billTypes" value="<%=MSPReconcile.DATACENTERCHANGED%>" title="Data Center Changed" <%=billTypes.equals(MSPReconcile.DATACENTERCHANGED)?"checked":""%>/> DCC
        <input type="radio" name="billTypes" value="<%=MSPReconcile.PAIDWITHEXP%>" title="Paid with Explanation"     <%=billTypes.equals(MSPReconcile.PAIDWITHEXP)?"checked":""%>/> PwE
        <input type="radio" name="billTypes" value="<%=MSPReconcile.BADDEBT%>"      <%=billTypes.equals(MSPReconcile.BADDEBT)?"checked":""%>/> Bad Debt
        <input type="radio" name="billTypes" value="<%=MSPReconcile.WCB%>"          <%=billTypes.equals(MSPReconcile.WCB)?"checked":""%>/> WCB
        <input type="radio" name="billTypes" value="<%=MSPReconcile.CAPITATED%>"    <%=billTypes.equals(MSPReconcile.CAPITATED)?"checked":""%>/> Capitated
        <input type="radio" name="billTypes" value="<%=MSPReconcile.DONOTBILL%>"    <%=billTypes.equals(MSPReconcile.DONOTBILL)?"checked":""%>/> Do Not Bill
        <input type="radio" name="billTypes" value="<%=MSPReconcile.BILLPATIENT%>"  <%=billTypes.equals(MSPReconcile.BILLPATIENT)?"checked":""%>/> Bill Patient

      </td>
    </tr>
</table>
    </form>
	
<table width="100%" border="2"  valign="top">
 
  <tr bgcolor="#CCCCFF"> 
    <TH align="center" class="bHeaderData" >SERVICE DATE</TH>
    <TH align="center" class="bHeaderData" >TIME</TH>
    <TH align="center" class="bHeaderData" >PATIENT</TH>
    <TH align="center" class="bHeaderData" >DESCRIPTION</TH>
    <TH align="center" class="bHeaderData" >ACCOUNT</TH>
    <TH align="center" class="bHeaderData" >MESSAGES</TH>
  </tr>
  <%

    String dateBegin = request.getParameter("xml_vdate");
    String dateEnd = request.getParameter("xml_appointment_date");
    
    ArrayList list = msp.getBillsMaster(billTypes);
    MSPReconcile.BillSearch bSearch = msp.getBills(billTypes, providerview, dateBegin ,dateEnd);
    list = bSearch.list;
    Properties p2 = bSearch.getCurrentErrorMessages();
    Properties p = msp.currentC12Records();    
    boolean bodd = true;
    System.out.println(list.size()+" in this billing list");
    for (int i = 0; i < list.size(); i++){     
        MSPReconcile.Bill b = (MSPReconcile.Bill) list.get(i);
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records                           
      String rejected = isRejected(b.billMasterNo,p);      
      if (rejected == null){     
        //rejected  = msp.getS00String(b.billMasterNo);
        rejected  = isRejected(b.billMasterNo,p2);
      }
   %>
  <tr bgcolor="<%=bodd?"#EEEEFF":"white"%>"> 
    <TD align="center" class="bCellData" ><%=b.apptDate%></TD>
    <TD align="center" class="bCellData" ><%=b.apptTime==null?"00:00:00":b.apptTime%></TD>
    <TD align="center" class="bCellData" ><%=b.demoName%></TD>
    <TD align="center" class="bCellData" ><%=b.reason%></TD>
    <TD align="center" class="bCellData" >
        <a href="javascript: function myFunction() {return false; }" onClick='popupPage(700,720, "../../../billing/CA/BC/billingView.do?billing_no=<%=b.billing_no%>&dboperation=search_bill&hotclick=0")' title="<%=b.reason%>">
            <%=b.billing_no%>
        </a>
    </TD>                                 
    <td><%=rejected%></td>
  </tr>
  <%  //}
    rowCount = rowCount + 1;    
    }
    if (rowCount == 0) {
    %>
  <tr bgcolor="<%=bodd?"ivory":"white"%>"> 
    <td colspan="5" align="center" class="bCellData">
        No unbill items
    </td>
  </tr>
 <% }%>
</table>
</body>
</html>
<%!
String getReasonEx(String reason){
    if (reason.equals("N")) reason="Do Not Bill ";
    if (reason.equals("O")) reason="Bill MSP ";
    if (reason.equals("W")) reason="Bill WSIB ";
    if (reason.equals("H")) reason="Capitated Bill ";
    if (reason.equals("P")) reason="Bill Patient";
    return reason;
}

String isRejected(String billingNo,Properties p){
    String s = "&nbsp;";
    if (p.containsKey(billingNo)){
        s = "<a href=\"javascript: popupPage(700,700,'adjustBill.jsp?billing_no="+billingNo+"')\" >"+p.getProperty(billingNo)+"</a>";
    }    
    return s;
}
%>