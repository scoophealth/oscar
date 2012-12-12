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
  String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
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
<title>Invoice List</title>
<link rel="stylesheet" type="text/css" media="all" href="../../../share/calendar/calendar.css" title="win2k-cold-1" />

<!--<script type="text/javascript" src="../../../share/javascript/sorttable.js"></script>-->
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<script type="text/javascript" src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>
">
</script>
<script type="text/javascript" src="../../../share/calendar/calendar-setup.js"></script>

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
if (billTypes == null){
    billTypes = MSPReconcile.REJECTED;
}
if("true".equals(readonly)){
billTypes = "%";
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

	  <input type="radio" name="billTypes" value="?"                              <%=billTypes.equals("?")?"checked":""%>/>
      Fixable Receivables

	  <input type="radio" name="billTypes" value="$"                              <%=billTypes.equals("$")?"checked":""%>/>
	  Paid Bills
      <input type="hidden" name="submitted" value="yes"/>

    </td>
  </tr>
</table>
</form>
<table class="sortable tabular_list" id="table-1" width="100%" border="2"  valign="top">
<thead>
	<tr bgcolor="#CCCCFF">
	<th align="center" class="bHeaderData" title="INVOICE #" >INVOICE # </th>
	<th align="center" class="bHeaderData" title="LINE #" >SEQ # </th>
    <th align="center" class="bHeaderData" title="APP. DATE">APP. DATE</th>
	<th align="center" class="bHeaderData" title="TYPE" >TYPE </th>
	<%
		if(!"true".equals(readonly)){
	%>
    <th align="center" class="bHeaderData" title="PATIENT" >PATIENT</th>
	<%}%>
	 <th align="center" class="bHeaderData" title="PRACT" >PRACT.</th>
	<th align="center" class="bHeaderData" title="Status">STAT</th>


    <th align="center" class="bHeaderDate" title="Fee Code">FEE CODE</th>
    <th align="center" class="bHeaderDate" title="QTY">QTY</th>
    <th align="center" class="bHeaderDate" title="Amount Billed">AMT</th>
    <th align="center" class="bHeaderDate" title="Amount Paid"  >PAID</th>
    <th align="center" class="bHeaderDate" >OWED</th>
    <th align="center" class="bHeaderDate" >DX CODE </th>
    <th align="center" class="bHeaderData" >MSGS</th>
  </tr>
</thead>
   <tbody>
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
	String currentBillingNo = "";
    for (int i = 0; i < list.size(); i++){

      incorrectVal = false;
      paidinCorrectval = false;
      MSPReconcile.Bill b = (MSPReconcile.Bill) list.get(i);

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

  <tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
      <td align="center" class="bCellData">
        <%if("Pri".equals(b.billingtype)){%>
	 	<a href="javascript:popupPage(800,800, '../../../billing/CA/BC/billingView.do?billing_no=<%=b.billing_no%>&receipt=yes')"><%=b.billing_no%>       </a>
		<%}
	 else{
		%>
	 	<%=b.billing_no%>
		<%}%>		    </td>


	<td align="center" class="bCellData" ><a href="javascript: function myFunction() {return false; }" onClick="popupPage2(500,1020,'genTAS00ByOfficeNo.jsp?officeNo=<%=b.billMasterNo%>','RecValues');"> <%=b.seqNum%> </a></td>
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
    <td align="center" class="bCellData" <%=isBadVal(incorrectVal)%> ><%=nf.format(Double.parseDouble(b.amount))%> </td>
    <td align="center" class="bCellData" ><%=nf.format(pAmount)%> </td>
    <%
    double dblAmtOwed = msp.getAmountOwing(b.billMasterNo,b.amount,b.billingtype);
    BigDecimal amtOwed = new BigDecimal(dblAmtOwed).setScale(2, BigDecimal.ROUND_HALF_UP);
    owedTotal = owedTotal.add(amtOwed);

    %>
    <td align="center" class="bCellData" ><%=nf.format(amtOwed)%> </td>
    <td align="center" class="bCellData" ><%=s(b.dx1)%></td>

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
  <tr bgcolor="<%=bodd?"ivory":"white"%>">
    <td colspan="14" align="center" class="bCellData"> No bills </td>
  </tr>
  <% }%>
    </tbody>
  <tfoot>
  <tr class="sortbottom">
    <logic:notEqual parameter="filterPatient" value="true">
      <td class="bCellData" align="center">&nbsp;</td>
    </logic:notEqual>
    <td class="bCellData" align="center">&nbsp;</td>
    <td class="bCellData" align="center">&nbsp;</td>
    <td class="bCellData" align="center">&nbsp;</td>
    <td class="bCellData" align="center">&nbsp;</td>
    <td align="center" class="bCellData" >Count:</td>
    <td align="center" class="bCellData" ><%=list.size()%></td>
    <td align="center" class="bCellData" >&nbsp;</td>
    <td align="center" class="bCellData" >Total:</td>
    <td align="center" class="bCellData" ><%=nf.format(total.doubleValue())%></td>
    <td align="center" class="bCellData" ><%=nf.format(paidTotal.doubleValue())%></td>
    <td align="center" class="bCellData" ><%=nf.format(owedTotal.doubleValue())%></td>
    <td class="bCellData" align="center">&nbsp;</td>
    <td class="bCellData" align="center">&nbsp;</td>

  </tr>
  </tfoot>
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
