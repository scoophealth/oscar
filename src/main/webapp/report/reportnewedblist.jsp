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

<%
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";

  String strLimit1="0";
  String strLimit2="15000";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");

  String startDate =null, endDate=null;
  if(request.getParameter("startDate")!=null) startDate = request.getParameter("startDate");
  if(request.getParameter("endDate")!=null) endDate = request.getParameter("endDate");
%>
<%@ page import="java.util.*, java.sql.*" errorPage="../errorpage.jsp"%>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean"
	scope="page" />
<jsp:useBean id="providerNameBean" class="java.util.Properties"
	scope="page" />

<%
  String [][] dbQueries=new String[][] {
{"select_maxformar_id", "select max(ID) from formONAREnhanced where c_finalEDB >= ? and c_finalEDB <= ? group by demographic_no" },
{"select_formar", "select ID, demographic_no, c_finalEDB, concat(c_lastName,', ',c_firstName) as c_pName, pg1_age, c_gravida, c_term, pg1_homePhone, provider_no from formONAREnhanced where c_finalEDB >= ? and c_finalEDB <= ? order by c_finalEDB desc limit ? offset ?"  },
{"search_provider", "select provider_no, last_name, first_name from provider order by last_name"},
{"select_patientStatus", "select patient_status, provider_no from demographic where demographic_no = ?"  },
  };
  reportMainBean.doConfigure(dbQueries);
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="report.reportnewdblist.title" /></title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css">

<script language="JavaScript">
<!--
<%-- var SORT_COLUMN_INDEX;

function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {        
        thisTbl = tbls[ti];
        if (((' '+thisTbl.className+' ').indexOf("sortable") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            alert("sortable!");
            ts_makeSortable(thisTbl);
        }
    }
}

function ts_makeSortable(table) {
    if (table.rows && table.rows.lengtph > 0) {
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
--%>


function loadPage() {
    //sortables_init();
    setfocus();
}
//-->
</SCRIPT>
<!--base target="pt_srch_main"-->
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="loadPage()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="<%=deepcolor%>">
		<th><font face="Helvetica"><bean:message
			key="report.reportnewdblist.msgEDBList" /></font></th>
	</tr>
	<tr>
		<td align="right"><input type="button" name="Button"
			value="<bean:message key="global.btnPrint"/>"
			onClick="window.print()"> <input type="button" name="Button"
			value="<bean:message key="global.btnCancel" />"
			onClick="window.close()">
		</th>
	</tr>
</table>
<script type="text/javascript"
	src="../commons/scripts/sort_table/css.js"></script>
<script type="text/javascript"
	src="../commons/scripts/sort_table/common.js"></script>
<script type="text/javascript"
	src="../commons/scripts/sort_table/standardista-table-sorting.js"></script>
<CENTER>
<table class="sortable" id="table-1" width="100%" border="0"
	bgcolor="silver" cellspacing="2" cellpadding="2">
	<thead>
		<tr bgcolor='<%=deepcolor%>'>
			<th style="text-decoration: bold">#</th>
			<th style="text-decoration: bold" width="10%" nowrap><bean:message
				key="report.reportnewdblist.msgEDB" /></th>
			<TH style="text-decoration: bold" align="center" width="30%"><bean:message
				key="report.reportnewdblist.msgName" /></TH>
			<!--TH align="center" width="20%"><b>Demog' No </b></TH-->
			<TH style="text-decoration: bold" align="center" width="5%"><bean:message
				key="report.reportnewdblist.msgAge" /></TH>
			<TH style="text-decoration: bold" align="center" width="5%"><bean:message
				key="report.reportnewdblist.msgGravida" /></TH>
			<TH style="text-decoration: bold" align="center" width="10%"><bean:message
				key="report.reportnewdblist.msgTerm" /></TH>
			<TH style="text-decoration: bold" align="center" width="10%"><bean:message
				key="report.reportnewdblist.msgPhone" /></TH>
			<TH style="text-decoration: bold" align="center"><bean:message
				key="report.reportnewdblist.msGP" /></TH>
			<TH style="text-decoration: bold" align="center"><bean:message
				key="report.reportnewdblist.msProvider" /></TH>

		</tr>
	</thead>
	<tfoot></tfoot>
	<tbody>
		<%
        ResultSet rs=null ;
        rs = reportMainBean.queryResults("search_provider");
        while (rs.next()) {
        providerNameBean.setProperty(reportMainBean.getString(rs,"provider_no"), new String( reportMainBean.getString(rs,"last_name")+","+reportMainBean.getString(rs,"first_name") ));
        }
        Properties arMaxId = new Properties();
        String[] paramI =new String[2];
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(startDate.substring(0,4)), Integer.parseInt(startDate.substring(5,startDate.lastIndexOf('-'))) , Integer.parseInt(startDate.substring(startDate.lastIndexOf('-')+1)) );
        cal.add(Calendar.YEAR,-1);
        paramI[0]=sdf.format(cal.getTime());
        cal.set(Integer.parseInt(endDate.substring(0,4)), Integer.parseInt(endDate.substring(5,endDate.lastIndexOf('-'))) , Integer.parseInt(endDate.substring(endDate.lastIndexOf('-')+1)) );
        cal.add(Calendar.YEAR, 1);
        paramI[1]=sdf.format(cal.getTime());
        rs = reportMainBean.queryResults(paramI, "select_maxformar_id");
        while (rs.next()) {
        arMaxId.setProperty(""+rs.getInt("max(ID)"), "1");
        }
        
        Properties demoProp = new Properties();
        
        String[] param =new String[2];
        param[0]=startDate; 
        param[1]=endDate; 
        String[] paramb = new String[4];
        paramb[0]=startDate; 
        paramb[1]=endDate; 
        paramb[2]=startDate; 
        paramb[3]=endDate; 
        int[] itemp1 = new int[2];
        itemp1[1] = Integer.parseInt(strLimit1);
        itemp1[0] = Integer.parseInt(strLimit2);
        boolean bodd=false;
        int nItems=0;
	rs = reportMainBean.queryResults(param,itemp1, "select_formar");
        while (rs.next()) {
        if (!arMaxId.containsKey(""+rs.getInt("ID")) ) continue;
        if (demoProp.containsKey(reportMainBean.getString(rs,"demographic_no")) ) continue;
        else demoProp.setProperty(reportMainBean.getString(rs,"demographic_no"), "1");
        
        // filter the "IN" patient from the list.
        String providerNo = "0";
        ResultSet rs1=reportMainBean.queryResults(reportMainBean.getString(rs,"demographic_no"), "select_patientStatus");
        if (rs1.next()) {
        if(rs1.getString("patient_status").equals("IN")) continue;
        providerNo = rs1.getString("provider_no");
        }
        
        bodd=bodd?false:true; //for the color of rows
        nItems++;
        
        %>
		<tr bgcolor="<%=bodd?weakcolor:"white"%>">
			<td><%=nItems%></td>
			<td align="center" nowrap><%=reportMainBean.getString(rs,"c_finalEDB")!=null?reportMainBean.getString(rs,"c_finalEDB").replace('-','/'):"----/--/--"%></td>
			<td><%=reportMainBean.getString(rs,"c_pName")%></td>
			<!--td align="center" ><%=reportMainBean.getString(rs,"demographic_no")%> </td-->
			<td><%=reportMainBean.getString(rs,"pg1_age")%></td>
			<td><%=reportMainBean.getString(rs,"c_gravida")%></td>
			<td><%=reportMainBean.getString(rs,"c_term")%></td>
			<td nowrap><%=reportMainBean.getString(rs,"pg1_homePhone")%></td>
			<td><%=providerNameBean.getProperty(providerNo, "")%></td>
			<td><%=providerNameBean.getProperty(reportMainBean.getString(rs,"provider_no"), "")%></td>
		</tr>
		<% 
        }
        %>

	</tbody>
</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="reportnewedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"><bean:message
	key="report.reportnewdblist.msgLastPage" /></a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="reportnewedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
<bean:message key="report.reportnewdblist.msgNextPage" /></a> <%
}
%>

</body>
</html:html>