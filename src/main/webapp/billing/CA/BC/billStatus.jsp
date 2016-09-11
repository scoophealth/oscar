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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.math.*,java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.MSP.*,oscar.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%@page import="org.oscarehr.common.model.ReportProvider" %>
<%@page import="org.oscarehr.common.model.Provider" %>


<%
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
%>

<%
  java.text.NumberFormat nf = java.text.NumberFormat.getCurrencyInstance();
  String user_no;
  user_no = (String) session.getAttribute("user");
  int  nItems=0;
  String strLimit1="0";
  String strLimit2="5";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"ALL":request.getParameter("providerview") ;
  BigDecimal total = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  BigDecimal paidTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  BigDecimal owedTotal = new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_UP);
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

boolean adminAccess = false;
%>


<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.billing" rights="r" reverse="<%=false%>" >
    <% adminAccess = true; %>
</security:oscarSec>


<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
<html:base/>
<title><bean:message key="admin.admin.editInvoices"/></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>

<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">

<script>
addEvent(window, "load", sortables_init);

var SORT_COLUMN_INDEX;

function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.className+' ').indexOf("sortable") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}

function ts_makeSortable(table) {
    if (table.rows && table.rows.length > 0) {
        var firstRow = table.rows[0];
    }
    if (!firstRow) return;

    // We have a first row: assume it's the header, and make its contents clickable links
    for (var i=0;i<firstRow.cells.length;i++) {
        var cell = firstRow.cells[i];
        var txt = ts_getInnerText(cell);
        cell.innerHTML = '<a href="#" class="sortheader" '+
        'onclick="ts_resortTable(this, '+i+');return false;">' +
        txt+'<span class="sortarrow">&nbsp;&nbsp;&nbsp;</span></a>';
    }
}

function ts_getInnerText(el) {
	if (typeof el == "string") return el;
	if (typeof el == "undefined") { return el };
	if (el.innerText) return el.innerText;	//Not needed but it is faster
	var str = "";

	var cs = el.childNodes;
	var l = cs.length;
	for (var i = 0; i < l; i++) {
		switch (cs[i].nodeType) {
			case 1: //ELEMENT_NODE
				str += ts_getInnerText(cs[i]);
				break;
			case 3:	//TEXT_NODE
				str += cs[i].nodeValue;
				break;
		}
	}
	return str;
}

function ts_resortTable(lnk,clid) {
    // get the span
    var span;
    for (var ci=0;ci<lnk.childNodes.length;ci++) {
        if (lnk.childNodes[ci].tagName && lnk.childNodes[ci].tagName.toLowerCase() == 'span') span = lnk.childNodes[ci];
    }
    var spantext = ts_getInnerText(span);
    var td = lnk.parentNode;
    var column = clid;
    var table = getParent(td,'TABLE');

    // Work out a type for the column
    if (table.rows.length <= 1) return;


    var itm = ts_getInnerText(table.rows[1].cells[column]);
    sortfn = ts_sort_caseinsensitive;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^[�$]/)) sortfn = ts_sort_currency;
    if (itm.match(/^[\d\.]+$/)) sortfn = ts_sort_numeric;
    SORT_COLUMN_INDEX = column;
    var firstRow = new Array();
    var newRows = new Array();
    for (i=0;i<table.rows[0].length;i++) { firstRow[i] = table.rows[0][i]; }
    for (j=1;j<table.rows.length;j++) { newRows[j-1] = table.rows[j]; }

    newRows.sort(sortfn);

    if (span.getAttribute("sortdir") == 'down') {
        ARROW = '&nbsp;&nbsp;&uarr;';
        newRows.reverse();
        span.setAttribute('sortdir','up');
    } else {
        ARROW = '&nbsp;&nbsp;&darr;';
        span.setAttribute('sortdir','down');
    }

    // We appendChild rows that already exist to the tbody, so it moves them rather than creating new ones
    // don't do sortbottom rows
    for (i=0;i<newRows.length;i++) { if (!newRows[i].className || (newRows[i].className && (newRows[i].className.indexOf('sortbottom') == -1))) table.tBodies[0].appendChild(newRows[i]);}
    // do sortbottom rows only
    for (i=0;i<newRows.length;i++) { if (newRows[i].className && (newRows[i].className.indexOf('sortbottom') != -1)) table.tBodies[0].appendChild(newRows[i]);}

    // Delete any other arrows there may be showing
    var allspans = document.getElementsByTagName("span");
    for (var ci=0;ci<allspans.length;ci++) {
        if (allspans[ci].className == 'sortarrow') {
            if (getParent(allspans[ci],"table") == getParent(lnk,"table")) { // in the same table as us?
                allspans[ci].innerHTML = '&nbsp;&nbsp;&nbsp;';
            }
        }
    }

    span.innerHTML = ARROW;
}

function getParent(el, pTagName) {
	if (el == null) return null;
	else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())	// Gecko bug, supposed to be uppercase
		return el;
	else
		return getParent(el.parentNode, pTagName);
}
function ts_sort_date(a,b) {
    // y2k notes: two digit years less than 50 are treated as 20XX, greater than 50 are treated as 19XX
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa.length == 10) {
        dt1 = aa.substr(6,4)+aa.substr(3,2)+aa.substr(0,2);
    } else {
        yr = aa.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt1 = yr+aa.substr(3,2)+aa.substr(0,2);
    }
    if (bb.length == 10) {
        dt2 = bb.substr(6,4)+bb.substr(3,2)+bb.substr(0,2);
    } else {
        yr = bb.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt2 = yr+bb.substr(3,2)+bb.substr(0,2);
    }
    if (dt1==dt2) return 0;
    if (dt1<dt2) return -1;
    return 1;
}

function ts_sort_currency(a,b) {
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    return parseFloat(aa) - parseFloat(bb);
}

function ts_sort_numeric(a,b) {
    aa = parseFloat(ts_getInnerText(a.cells[SORT_COLUMN_INDEX]));
    if (isNaN(aa)) aa = 0;
    bb = parseFloat(ts_getInnerText(b.cells[SORT_COLUMN_INDEX]));
    if (isNaN(bb)) bb = 0;
    return aa-bb;
}

function ts_sort_caseinsensitive(a,b) {
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).toLowerCase();
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).toLowerCase();
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}

function ts_sort_default(a,b) {
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}


function addEvent(elm, evType, fn, useCapture)
// addEvent and removeEvent
// cross-browser event handling for IE5+,  NS6 and Mozilla
// By Scott Andrew
{
  if (elm.addEventListener){
    elm.addEventListener(evType, fn, useCapture);
    return true;
  } else if (elm.attachEvent){
    var r = elm.attachEvent("on"+evType, fn);
    return r;
  } else {
    alert("Handler could not be removed");
  }
}

</script>
<script language="JavaScript">


function billTypeOnly(showEle){
   document.serviceform.showMSP.checked = false;
   document.serviceform.showWCB.checked = false;
   document.serviceform.showPRIV.checked = false;
   document.serviceform.showICBC.checked = false;
   document.serviceform.elements[showEle].checked = true;
}
</script>

<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>

<style>
	input[type="text"] {
		height: 30px;
		width: 100px;
	}

@media print {

  .hidden-print {
    display: none !important;
  }

  
  /*this is so the link locatons don't display*/
  a:link:after, a:visited:after {
    content: "";
  }
}
</style>

</head>

<body>
<div class="container-fluid">
<h3><bean:message key="admin.admin.editInvoices"/></h3>

<div class="row well hidden-print">

<div align="right"><a href="javascript: function myFunction() {return false; }" onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')">Manage Provider List</a></div>

<div align="right"><%=DateUtils.sumDate("yyyy-M-d","0")%></div>


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


<form name="serviceform" method="get" action="billStatus.jsp" class="form-inline">
	<input type="hidden" name="filterPatient" value="<%=readonly%>"/>
	<input type="hidden" name="lastName" value="<%=request.getParameter("lastName")%>"/>
	<input type="hidden" name="firstName" value="<%=request.getParameter("firstName")%>"/>
 	<!--<input type="hidden" name="demographicNo" value="<%=demographicNo%>"/>-->


    <input type="checkbox" name="showMSP" value="show"  <%=showMSP?"checked":""%>/><a onclick="billTypeOnly('showMSP')">MSP</a>
    <input type="checkbox" name="showWCB" value="show"  <%=showWCB?"checked":""%>/><a onclick="billTypeOnly('showWCB')">WCB</a>
    <input type="checkbox" name="showPRIV" value="show" <%=showPRIV?"checked":""%>/><a onClick="billTypeOnly('showPRIV')">Private</a>
    <input type="checkbox" name="showICBC" value="show" <%=showICBC?"checked":""%>/><a onClick="billTypeOnly('showICBC')">ICBC</a><br><br>

<div class="span3">
    Select provider<br>
    <select name="providerview">
      <option value="ALL">All Providers</option>
      <%  String proFirst="";
                    String proLast="";
                    String proOHIP="";
                    String specialty_code;
                    String billinggroup_no;
                    int Count = 0;
                    for(Object[] result:reportProviderDao.search_reportprovider("billingreport")) {
                   		ReportProvider rp = (ReportProvider)result[0];
                   		Provider p = (Provider)result[1];
                        proFirst = p.getFirstName();
                        proLast = p.getLastName();
                        proOHIP =p.getProviderNo();
                %>
      <option value="<%=proOHIP%>" <%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>, <%=proFirst%></option>
      <%  } %>
    </select>
</div>

    <input type="hidden" name="verCode" value="V03"/>
    
<div class="span2">		
	Service Start Date:<br>
	<div class="input-append">
		<input type="text" name="xml_vdate" id="xml_vdate" value="<%=xml_vdate%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
		<span class="add-on"><i class="icon-calendar"></i></span>
	</div>
</div><!--span2-->
	
<div class="span3">		
	Service End Date: 	<a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-M-d","-30")%>')" >30</a>&nbsp; <a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-M-d","-60")%>')" >60</a>&nbsp; <a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-M-d","-90")%>')" >90</a>&nbsp;
<br>
	<div class="input-append">
		<input type="text" name="xml_appointment_date" id="xml_appointment_date" value="<%=xml_appointment_date%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
		<span class="add-on"><i class="icon-calendar"></i></span>
	</div>
</div><!--span3-->

<div class="span2">
    Demographic:<br>
	<%
		String readonlyStr = "true".equals(readonly)?"readonly":"";
	%>
    <input type="text" name="demographicNo" size="5" value="<%=xml_demoNo%>" <%=readonlyStr%>"/>
</div><!-- span3-->	


<div class="span10">
<br>
<% String billTypes = request.getParameter("billTypes");
if (billTypes == null){
    billTypes = MSPReconcile.REJECTED;
}
if("true".equals(readonly)){
billTypes = "%";
}

%>
	  <input type="radio" name="billTypes" value="<%=MSPReconcile.REJECTED%>"     <%=billTypes.equals(MSPReconcile.REJECTED)?"checked":""%>/>
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

	  <input type="radio" name="billTypes" value="?"                              <%=billTypes.equals("?")?"checked":""%>/>
      Fixable Receivables

	  <input type="radio" name="billTypes" value="$"                              <%=billTypes.equals("$")?"checked":""%>/>
	  Paid Bills
</div><!-- span10 -->

<div class="span10">
<br>
<input type="hidden" name="submitted" value="yes"/>
<input class="btn btn-primary" type="submit" name="Submit" value="Create Report">
</div><!-- span10 -->

</form>
</div><!-- row well-->

<div class="row">
<input class="btn pull-right hidden-print" type='button' name='print' value='Print' onClick='window.print()'>
<table class="table table-striped  table-condensed">
<thead>
	<tr>
	<th align="center" title="INVOICE #" >INVOICE # </th>
	<th align="center" title="LINE #" >SEQ # </th>
    <th align="center" title="APP. DATE">APP. DATE</th>
	<th align="center" title="TYPE" >TYPE </th>
	<%
		if(!"true".equals(readonly)){
	%>
    <th align="center" title="PATIENT" >PATIENT</th>
	<%}%>
	 <th align="center" title="PRACT" >PRACT.</th>
	<th align="center" title="Status">STAT</th>


    <th align="center" title="Fee Code">FEE CODE</th>
    <th align="center" title="QTY">QTY</th>
    <th align="center" title="Amount Billed">AMT</th>
    <th align="center" title="Amount Paid"  >PAID</th>
    <th align="center">OWED</th>
    <th align="center">DX CODE </th>
    <th align="center">MSGS</th>
  </tr>
</thead>
   <tbody>
  <%

    String dateBegin = request.getParameter("xml_vdate");
    String dateEnd = request.getParameter("xml_appointment_date");
    String demoNo = request.getParameter("demographicNo");

    MSPReconcile.BillSearch bSearch = msp.getBills(billTypes, providerview, dateBegin ,dateEnd,demoNo,!showWCB,!showMSP,!showPRIV,!showICBC);

    Properties p2 = bSearch.getCurrentErrorMessages();
    Properties p = msp.currentC12Records();
    boolean bodd = true;
    boolean incorrectVal = false;
    boolean paidinCorrectval = false;
	String currentBillingNo = "";
    for (int i = 0; i < bSearch.list.size(); i++){

      incorrectVal = false;
      paidinCorrectval = false;
      MSPReconcile.Bill b = (MSPReconcile.Bill) bSearch.list.get(i);

      bodd=currentBillingNo.equals(b.billing_no) ? !bodd : bodd; //for the color of rows
      nItems++; //to calculate if it is the end of records
      String rejected = isRejected(b.billMasterNo,p,b.isWCB());
      String rejected2  = isRejected(b.billMasterNo,p2,b.isWCB());
        BigDecimal valueToAdd = new BigDecimal("0.00");
      try{
        valueToAdd = new BigDecimal(b.amount).setScale(2, BigDecimal.ROUND_HALF_UP);
      }catch(Exception badValueException){
        MiscUtils.getLogger().error(" Error calculating value for "+b.billMasterNo);
        incorrectVal = true;
      }
      total = total.add(valueToAdd);
      double pAmount = msp.getAmountPaid(b.billMasterNo,b.billingtype);
        BigDecimal valueToPaidAdd = new BigDecimal("0.00");
      try{
        valueToPaidAdd = new BigDecimal(pAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
      }catch(Exception badValueException){
		MiscUtils.getLogger().error(" Error calculating paid value for "+b.billMasterNo);
        paidinCorrectval = true;
      }
      paidTotal = paidTotal.add(valueToPaidAdd);

   %>

  <tr>
      <td align="center">
        <%if("Pri".equals(b.billingtype)){%>
	 	<a href="javascript:popupPage(800,800, '../../../billing/CA/BC/billingView.do?billing_no=<%=b.billing_no%>&receipt=yes')"><%=b.billing_no%>       </a>
		<%}
	 else{
		%>
	 	<%=b.billing_no%>
		<%}%>		    </td>


	<td align="center"><a href="javascript: function myFunction() {return false; }" onClick="popupPage2(500,1020,'genTAS00ByOfficeNo.jsp?officeNo=<%=b.billMasterNo%>','RecValues');"> <%=b.seqNum%> </a></td>
	<td align="center"><%=b.apptDate%></td>
	<td align="center"><%=b.billingtype%></td>
	<%
		if(!"true".equals(readonly)){
	%>
   <td align="center"><a href="javascript: setDemographic('<%=b.demoNo%>');"><%=b.demoName%></a></td>
	<%}%>
	<td align="center"><%=b.providerLastName%>,<%=b.providerFirstName%></td>
	 <td align="center" title="<%=msp.getStatusDesc(b.reason)%>" ><%=msp.getStatusDesc(b.reason)==null?"&nbsp":msp.getStatusDesc(b.reason)%></td>


    <td align="center"><%=b.code%></td>
    <td align="center" <%=isBadVal(incorrectVal)%> ><%=b.quantity%></td>
    <td align="center" <%=isBadVal(incorrectVal)%> ><%=nf.format(Double.parseDouble(b.amount))%> </td>
    <td align="center"><%=nf.format(pAmount)%> </td>
    <%
    double dblAmtOwed = msp.getAmountOwing(b.billMasterNo,b.amount,b.billingtype);
    BigDecimal amtOwed = new BigDecimal(dblAmtOwed).setScale(2, BigDecimal.ROUND_HALF_UP);
    owedTotal = owedTotal.add(amtOwed);

    %>
    <td align="center"><%=nf.format(amtOwed)%> </td>
    <td align="center"><%=s(b.dx1)%></td>

    <td>
      <% if (adminAccess){ %>  
          <a href="javascript: popupPage(700,1000,'adjustBill.jsp?billing_no=<%=b.billMasterNo%>&invoiceNo=<%=b.billing_no%>')" >Edit </a>
      <% } %>
      
      
      <%=rejected%><%=rejected2%> </td>
  </tr>

  <%  //}
    rowCount = rowCount + 1;
     currentBillingNo = b.billing_no;
	 bodd=!bodd;
    }
    if (rowCount == 0) {
    %>
  <tr>
    <td colspan="14" align="center"> No bills </td>
  </tr>
  <% }%>
    </tbody>
  <tfoot>
  <tr>
    <logic:notEqual parameter="filterPatient" value="true">
      <td align="center">&nbsp;</td>
    </logic:notEqual>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">Count:</td>
    <td align="center"><%=bSearch.list.size()%></td>
    <td align="center">&nbsp;</td>
    <td align="center">Total:</td>
    <td align="center"><%=nf.format(total.doubleValue())%></td>
    <td align="center"><%=nf.format(paidTotal.doubleValue())%></td>
    <td align="center"><%=nf.format(owedTotal.doubleValue())%></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>

  </tr>
  </tfoot>
</table>
</div><!--row-->

<script language='javascript'>
	var startDate = $("#xml_vdate").datepicker({
		format : "yyyy-mm-dd"
	});
	
	var endDate = $("#xml_appointment_date").datepicker({
		format : "yyyy-mm-dd"
	});
</script>
<script language="javascript" src="../../../commons/scripts/sort_table/css.js"/>
<script language="javascript" src="../../../commons/scripts/sort_table/common.js"/>
<script language="javascript" src="../../../commons/scripts/sort_table/standardista-table-sorting.js"/>
</div><!--container-->
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
        s = "<a href=\"javascript: popupPage(700,1000,'adjustBill.jsp?billing_no="+billingNo+"')\" > "+p.getProperty(billingNo)+"</a>";
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
