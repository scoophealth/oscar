<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<%@ page import="oscar.oscarRx.data.*,oscar.oscarProvider.data.ProviderMyOscarIdData,oscar.oscarDemographic.data.DemographicData,oscar.OscarProperties,oscar.log.*"%>
<%@ page import="org.oscarehr.common.model.OscarAnnotation" %>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.*" %>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.casemgmt.web.PrescriptDrug"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.ArrayList"%>
<bean:define id="patient" type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />

<%
        if (session.getAttribute("userrole") == null) response.sendRedirect("../logout.jsp");
        String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r"
                   reverse="<%=true%>">
    <%
    //LogAction.addLog((String) session.getAttribute("user"), LogConst.NORIGHT+LogConst.READ,  LogConst.CON_PRESCRIPTION, demographic$, request.getRemoteAddr(),demographic$);

            response.sendRedirect("../noRights.html");
    %>
</security:oscarSec>

<%
            response.setHeader("Cache-Control", "no-cache");
%>
<logic:notPresent name="RxSessionBean" scope="session">
    <logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
    <bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
                 name="RxSessionBean" scope="session" />
    <logic:equal name="bean" property="valid" value="false">
        <logic:redirect href="error.html" />
    </logic:equal>
</logic:present>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
            oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) pageContext.findAttribute("bean");
%>

<%
            String usefav=request.getParameter("usefav");
            String favid=request.getParameter("favid");
            String reRxDrugId=request.getParameter("reRxDrugId");
            HashMap hm=(HashMap)session.getAttribute("profileViewSpec");
            boolean show_current=true;
            boolean show_all=true;
            boolean active=true;
            boolean inactive=true;
            boolean all=true;
            boolean longterm_acute=true;
            boolean longterm_acute_inactive=true;
            if(hm==null) {System.out.println("hm is null");}
            else{
             show_current=(Boolean)hm.get("show_current");
             show_all=(Boolean)hm.get("show_all");
             active=(Boolean)hm.get("active");
             inactive=(Boolean)hm.get("inactive");
             all=(Boolean)hm.get("all");
             longterm_acute=(Boolean)hm.get("longterm_acute");
             longterm_acute_inactive=(Boolean)hm.get("longterm_acute_inactive");
            }

            //System.out.println("usefav="+usefav);
            //System.out.println("reRxDrugId="+reRxDrugId);
            RxPharmacyData pharmacyData = new RxPharmacyData();
            RxPharmacyData.Pharmacy pharmacy;
            pharmacy = pharmacyData.getPharmacyFromDemographic(Integer.toString(bean.getDemographicNo()));
            String prefPharmacy = "";
            if (pharmacy != null) {
                prefPharmacy = pharmacy.name;
            }

            String drugref_route = OscarProperties.getInstance().getProperty("drugref_route");
            if (drugref_route == null) {
                drugref_route = "";
            }
            String[] d_route = ("Oral," + drugref_route).split(",");

            String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_PRESCRIP;

            oscar.oscarRx.data.RxPrescriptionData.Prescription[] prescribedDrugs;
                        prescribedDrugs = patient.getPrescribedDrugScripts(); //this function only returns drugs which have an entry in prescription and drugs table
                        String script_no = "";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
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


<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>

        <title><bean:message key="SearchDrug.title" /></title>
        <link rel="stylesheet" type="text/css" href="styles.css">

        <html:base />

        <link rel="stylesheet" href="<c:out value="${ctx}/share/lightwindow/css/lightwindow.css"/>" type="text/css" media="screen" />
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  ></link>
        <!--link rel="stylesheet" type="text/css" href="modaldbox.css"  /-->
        <script type="text/javascript" src="<c:out value="${ctx}/phr/phr.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/screen.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/rx.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/scriptaculous.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/effects.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/controls.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/lightwindow/javascript/lightwindow.js"/>"></script>
        <!--script type="text/javascript" src="<%--c:out value="modaldbox.js"/--%>"></script-->



        <link rel="stylesheet" type="text/css" href="<c:out value="${ctx}/share/yui/css/fonts-min.css"/>" >
        <link rel="stylesheet" type="text/css" href="<c:out value="${ctx}/share/yui/css/autocomplete.css"/>" >
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/connection-min.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/animation-min.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/datasource-min.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/autocomplete-min.js"/>"></script>


        <script type="text/javascript">
            function onPrint(cfgPage) {
                var docF = $('printFormDD');
                oscarLog('in onPrint'+docF);

                docF.action = "../form/createpdf?__title=Rx&__cfgfile=" + cfgPage + "&__template=a6blank";
                docF.target="_blank";
                docF.submit();
               return true;
            }

            function buildRoute() {

                pickRoute = "";
                <oscar:oscarPropertiesCheck property="drugref_route_search" value="on">
                <%for (int i = 0; i < d_route.length; i++) {%>
                        if (document.forms[2].route<%=i%>.checked) pickRoute += " "+document.forms[2].route<%=i%>.value;
                <%}%>
                        document.forms[2].searchRoute.value = pickRoute;
                </oscar:oscarPropertiesCheck>
            }



           function popupRxSearchWindow(){
               var winX = (document.all)?window.screenLeft:window.screenX;
               var winY = (document.all)?window.screenTop:window.screenY;

               var top = winY+70;
               var left = winX+110;
               var url = "searchDrug.do?rx2=true&searchString="+$('searchString').value;
               popup2(600, 800, top, left, url, 'windowNameRxSearch<%=bean.getDemographicNo()%>');

           }

           var highlightMatch = function(full, snippet, matchindex) {
            return full.substring(0, matchindex) +
                "<span class=match>" +
                full.substr(matchindex, snippet.length) +
                "</span>" +
                full.substring(matchindex + snippet.length);
           };

           var resultFormatter = function(oResultData, sQuery, sResultMatch) {
               var query = sQuery.toUpperCase();
               var drugName = oResultData[0];

               var mIndex = drugName.toUpperCase().indexOf(query);
               var display = '';

               if(mIndex > -1){
                   display = highlightMatch(drugName,query,mIndex);
               }else{
                   display = drugName;
               }
               return  display;
           };
        </script>

        <script type="text/javascript">
addEvent(window, "load", sortables_init);

var SORT_COLUMN_INDEX;

function sortables_init() {
    // Find all tables with class sortable and make them sortable
    oscarLog("in here");
    if (!document.getElementsByTagName) return;
    oscarLog("Made it pat getElementsByTagName");
    tbls = document.getElementsByTagName("table");
    oscarLog("able "+tbls.length);
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        oscarLog(thisTbl.className);
        if (((' '+thisTbl.className+' ').indexOf("sortable") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}

function ts_makeSortable(table) {
    oscarLog('making '+table+' sortable');
    if (table.rows && table.rows.length > 0) {
        var firstRow = table.rows[0];
    }
    if (!firstRow) return;
    oscarLog('Gets past here');

    // We have a first row: assume it's the header, and make its contents clickable links
    for (var i=0;i<firstRow.cells.length;i++) {
        var cell = firstRow.cells[i];
        var txt = ts_getInnerText(cell);
        cell.innerHTML = '<a href="#"  class="sortheader" '+
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
    if (itm.match(/^[£$]/)) sortfn = ts_sort_currency;
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
function checkFav(){
    //oscarLog("****** in checkFav");
    var usefav='<%=usefav%>';
    var favid='<%=favid%>';
    if(usefav=="true" && favid!=null && favid!='null'){
        //oscarLog("****** favid "+favid);
        useFav2(favid);
    }else{}
}
function checkRePrescribe(){
    var drugId='<%=reRxDrugId%>';
    oscarLog("drugId in checkrePrescribe: "+drugId);
    if(drugId!=null && (drugId!='null')){
              represcribeOnLoad(drugId);
    }else{}
}

     //represcribe a drug
    function represcribeOnLoad(drugId){
        var data="drugId="+drugId;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/rePrescribe2.do?method=represcribe2";
        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:false,evalScripts:true,insertion: Insertion.Bottom});

    }
</script>




               <style type="text/css" media="print">
                   noprint{
                       display:none;
                   }
                   justforprint{
                       float:left;
                   }
               </style>

        <style type="text/css">
            .match{
                font-weight:bold;
            }
#myAutoComplete {
    width:25em; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}

body {
	margin:0;
	padding:0;
}

<%--
/*THEMES*/

     .currentDrug{
        color:red;
     }
     .archivedDrug{
        text-decoration: line-through;
     }
     .expireInReference{
         color:orange;
         font-weight:bold;
     }
     .expiredDrug{

     }

     .longTermMed{

     }

     .discontinued{

     }

--%>
/*THEME 2*/

     .currentDrug{
        font-weight:bold;
     }
     .archivedDrug{
        text-decoration: line-through;
     }
     .expireInReference{
         color:orange;
         font-weight:bold;
     }
     .expiredDrug{
         color:gray;
     }

     .longTermMed{
        font-style:italic;
     }

     .discontinued{
         text-decoration: line-through;

     }

     .deleted{
         text-decoration: line-through;

     }


     .sortheader{
         text-decoration: none;
         color:black;

     }
</style>

    </head>

    <%
        boolean showall = false;

		if (request.getParameter("show") != null) if (request.getParameter("show").equals("all")) showall = true;
    %>



    <body  vlink="#0000FF" onload="checkFav();checkRePrescribe();iterateStash();rxPageSizeSelect();<%-- initmb(); --%>load()" class="yui-skin-sam">
        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
            <%@ include file="TopLinks2.jsp" %><!-- Row One included here-->
            <tr>
                <%@ include file="SideLinksEditFavorites2.jsp"%><%-- <td></td>Side Bar File --%>
                <td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%" valign="top"><!--Column Two Row Two-->
                    <table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">

                        <tr>
                            <td>
                                <%-- div class="DivContentSectionHead"><bean:message key="SearchDrug.section3Title" /></div --%>
                                <html:form action="/oscarRx/searchDrug"  onsubmit="return checkEnterSendRx();" styleId="drugForm">
                                    <div id="rxText" style="float:left;"></div><div id="interactionsRxMyD" style="float:right;"></div><br style="clear:left;">

                                    <html:hidden property="demographicNo" value="<%=new Integer(patient.getDemographicNo()).toString()%>" />
                                    <table border="0">
                                        <tr valign="top">
                                            <td style="width:350px;"><bean:message key="SearchDrug.drugSearchTextBox"  />
                                                <html:text styleId="searchString" property="searchString"   size="16" maxlength="16" style="width:248px;\" autocomplete=\"off"  />
                                                <div id="autocomplete_choices"></div>
                                                <span id="indicator1" style="display: none"> <!--img src="/images/spinner.gif" alt="Working..." --></span>
                                            </td>
                                            <td >
                                                <a href="javascript:void(0);" onclick="callTreatments('searchString','treatmentsMyD')"><bean:message key="SearchDrug.msgDrugOfChoice" /></a>
                                                <a href=#  title="Set most frequently used prescription quantity" onClick ="popupPage(230,860,'../setProviderStaleDate.do?method=viewDefaultQuantity');return false;" style="font-style:normal;color:#6699CC"><bean:message key="provider.SetDefaultPrescriptionQuantity"/></a>
                                                <%-- <input type="button" name="search" class="ControlPushButton" value="<bean:message key="SearchDrug.msgSearch"/>" onclick="popupRxSearchWindow();">  --%>
                                                <%if (OscarProperties.getInstance().hasProperty("ONTARIO_MD_INCOMINGREQUESTOR")) {%>
                                                <a href="javascript:goOMD();"><bean:message key="SearchDrug.msgOMDLookup"/></a>
                                                <%}%>
                                                <div class="buttonrow">
                                                    <input id="saveButton" type="button"  onclick="updateSaveAllDrugs();" value="<bean:message key="SearchDrug.msgSaveAndPrescribe"/>" />
                                                    <input id="customDrug" type="button"  onclick="customWarning2();" value="<bean:message key="SearchDrug.msgCustomDrug"/>" />
                                                    <input id="reset" type="button" title="Clear pending prescriptions" onclick="resetStash();" value="<bean:message key="SearchDrug.msgResetPrescription"/>" />
                                                </div>

                                            </td>

                                            <td><oscar:oscarPropertiesCheck property="drugref_route_search" value="on">
                                                    <bean:message key="SearchDrug.drugSearchRouteLabel" />
                                                    <br>
                                                    <%for (int i = 0; i < d_route.length; i++) {%>
                                                    <input type="checkbox" name="route" <%=i%> value="<%=d_route[i].trim()%>"><%=d_route[i].trim()%> &nbsp;</input>
                                                    <%}%>
                                                    <html:hidden property="searchRoute" />
                                                </oscar:oscarPropertiesCheck></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3">
                                                <%-- input type="button" class="ControlPushButton" onclick="customWarning();" value="<bean:message key="SearchDrug.msgCustomDrug"/>" / --%>
                                            </td>
                                        </tr>
                                    </table>
                                </html:form>


<div id="previewForm" style="display:none;"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div>
                                    <table width="100%">
                                        <tr>
                                            <td>
                                                <div class="DivContentSectionHead">
                                                    <bean:message key="SearchDrug.section2Title" />
                                                    &nbsp;&nbsp;
                                                    <a href="javascript:popupWindow(720,700,'PrintDrugProfile2.jsp','PrintDrugProfile')"><bean:message key="SearchDrug.Print"/></a>
                                                    &nbsp;&nbsp;
                                                    <a href="#" onclick="$('reprint').toggle();return false;"><bean:message key="SearchDrug.Reprint"/></a>
                                                    &nbsp;&nbsp;
                                                    <a href="javascript:void(0);"name="cmdRePrescribe"  onclick="javascript:RePrescribeLongTerm();" style="width: 200px" ><bean:message key="SearchDrug.msgReprescribeLongTermMed"/></a>
                                                    &nbsp;&nbsp;
                                                    <a href="javascript:popupWindow(720,920,'chartDrugProfile.jsp?demographic_no=<%=bean.getDemographicNo()%>','PrintDrugProfile2')">Timeline Drug Profile</a>
                                                    &nbsp;&nbsp;
                                                    <a href="javascript: void(0);" onclick="callReplacementWebService('GetmyDrugrefInfo.do?method=view','interactionsRxMyD');" >DS run</a>
                                                </div>

                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div style="height: 100px; overflow: auto; background-color: #DCDCDC; border: thin solid green; display: none;" id="reprint">
                                                    <%


                        for (int i = 0; i < prescribedDrugs.length; i++) {
                            oscar.oscarRx.data.RxPrescriptionData.Prescription drug = prescribedDrugs[i];
                            if (drug.getScript_no() != null && script_no.equals(drug.getScript_no())) {
                                                    %>
                                                    <br>
                                                    <div style="float: left; width: 24%; padding-left: 40px;">&nbsp;</div>
                                                    <a style="float: left;" href="javascript:void(0);" onclick="reprint2('<%=drug.getScript_no()%>')"><%=drug.getRxDisplay()%></a>
                                                    <%
                            } else {
                                                    %>
                                                    <%=i > 0 ? "<br style='clear:both;'><br style='clear:both;'>" : ""%><div style="float: left; width: 12%; padding-left: 20px;"><%=drug.getRxDate()%></div>
                                                    <div style="float: left; width: 12%; padding-left: 20px;"><%=drug.getNumPrints()%>&nbsp;Prints</div>
                                                    <a style="float: left;" href="javascript:void(0);" onclick="reprint2('<%=drug.getScript_no()%>')"><%=drug.getRxDisplay()%></a>
                                                    <%
                            }
                            script_no = drug.getScript_no() == null ? "" : drug.getScript_no();
                        }
                                                    %>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table border="0">
                                                    <tr>
                                                        <td >
                                                            <div style="margin-top: 10px;  width: 100%">
                                                                <table width="100%" cellspacing="0" cellpadding="0">
                                                                    <tr>
                                                                        <td align="left">
                                                                            <a href="javascript:void(0);" title="View drug profile legend" onclick="ThemeViewer();" style="font-style:normal;color:#000000" ><bean:message key="SearchDrug.msgProfileLegend"/></a>
                                                                            <a href="javascript:void(0);" title="Change what kind of prescriptions to see in this drug profile" onclick="popupPage(230,860,'../setProviderStaleDate.do?method=viewRxProfileView');return false;" style="font-style:italic;color:#000000" ><bean:message key="provider.rxChangeProfileView"/></a>
                                                                            <%if(show_current){%>
                                                                            <a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp','drugProfile');"><bean:message key="SearchDrug.msgShowCurrent"/></a>
                                                                            <%}if(show_all){%>
                                                                            <a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?show=all','drugProfile');"><bean:message key="SearchDrug.msgShowAll"/></a>
                                                                            <%}if(active){%><a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?status=active','drugProfile');"><bean:message key="SearchDrug.msgActive"/></a>
                                                                            <%}if(inactive){%><a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?status=inactive','drugProfile');"><bean:message key="SearchDrug.msgInactive"/></a>
                                                                            <%}if(all){%><a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?status=all','drugProfile');"><bean:message key="SearchDrug.msgAll"/></a>
                                                                            <%}if(longterm_acute){%><a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?longTermOnly=true&heading=Long Term Meds','drugProfile'); callAdditionWebService('ListDrugs.jsp?longTermOnly=acute&heading=Acute','drugProfile')">Longterm /Acute</a>
                                                                            <%}if(longterm_acute_inactive){%><a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?longTermOnly=true&heading=Long Term Meds','drugProfile'); callAdditionWebService('ListDrugs.jsp?longTermOnly=acute&heading=Acute&status=active','drugProfile');callAdditionWebService('ListDrugs.jsp?longTermOnly=acute&heading=Inactive&status=inactive','drugProfile')">Longterm /Acute/Inactive</a>
                                                                            <%}%>
                                                                        </td>
                                                                        <td align="right">

                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <div id="drugProfile" ></div>

                                                            <html:form action="/oscarRx/rePrescribe">
                                                                <html:hidden property="drugList" />
                                                                <input type="hidden" name="method">
                                                            </html:form> <br>
                                                            <html:form action="/oscarRx/deleteRx">
                                                                <html:hidden property="drugList" />
                                                            </html:form></td>

                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </td>
                        </tr>


                       <%--  <logic:notEqual name="bean" property="stashSize" value="0">
                            <tr>
                                <td> <html:form action="/oscarRx/stash">
                                        <html:hidden property="action" />
                                        <html:hidden property="stashId" />
                                    </html:form>

                                    <div class="DivContentSectionHead"><bean:message key="WriteScript.section5Title" /></div>
                                </td>
                            </tr>
                           <tr>
                                <td>
                            <element>
                                <table cellspacing="0" cellpadding="5">
                                    <%int i = 0;%>
                                    <logic:iterate id="rx" name="bean" property="stash" length="stashSize">
                                        <tr>
                                            <td><a href="javascript:submitPending(<%=i%>, 'edit');"><bean:message key="SearchDrug.msgEdit"/></a></td>
                                            <td><a href="javascript:submitPending(<%=i%>, 'delete');"><bean:message key="SearchDrug.msgDelete"/></a></td>
                                            <td><a href="javascript:submitPending(<%=i%>, 'edit');"> <bean:write name="rx" property="rxDisplay" /> </a></td>
                                            <td><a href="javascript:ShowDrugInfo('<%=((oscar.oscarRx.data.RxPrescriptionData.Prescription) rx).getGenericName()%>');"><bean:message key="SearchDrug.msgInfo"/></a></td>
                                        </tr>
                                        <% i++;%>
                                    </logic:iterate>
                                </table></element>
                            <br>

                        <input type="button" class="ControlPushButton" onclick="javascript:window.location.href='viewScript.do';" value="<bean:message key="SearchDrug.msgSaveAndPrint"/>" /></td>
                </tr>
            </logic:notEqual>


            <tr height="100%">
                <td></td>
            </tr>--%>
        </table>
    </td>
</tr>

<tr>
    <td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
    <td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
</tr>

<tr>
    <td width="100%" height="0%" colspan="2">&nbsp;
        <%-- pre>
            Things that need to work
            JAC-Need data saving in the session.%
            JAC-Represcribe
            JAC-*REPRESCRIBE ALL long term meds
            JAC-Need delete to work
            -*discontinue!

            JAC-format "more" section
            +Adjust search to have required info returned. (What is the required info?  ATC, DIN, Route Form??
            -*How to limit on Route and Form
            -+*TREATMENTS%  *has bug if something is not found

            +ajax ds.%

        </pre
--%>
    </td>
</tr>

<tr>
    <td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2">

    </td>
</tr>

</table>



<div id="treatmentsMyD" style="position: absolute; left: 1px; top: 1px; width: 800px; height: 600px; visibility: hidden; z-index: 1">
       <a href="javascript: function myFunction() {return false; }" onclick="hidepic('treatmentsMyD');" style="text-decoration: none;">X</a>
</div>



    <div id="discontinueUI" style="position: absolute;display:none; width:500px;height:200px;background-color:white;padding:20px;border:1px solid grey">
        <h3>Discontinue :<span id="disDrug"></span></h3>
        <input type="hidden" name="disDrugId" id="disDrugId"/>
        Reason: <select name="disReason" id="disReason">
            <option value="doseChange">Dose change
            <option value="adverseReaction">Adverse reaction</option>
            <option value="allergy">Allergy</option>
            <option value="ineffectiveTreatment">Ineffective treatment</option>
            <option value="prescribingError">Prescribing error</option>
            <option value="noLongerNecessary">No longer necessary</option>
            <option value="simplifyingTreatment">Simplifying treatment</option>
            <option value="patientRequest">Patient request</option>
            <option value="newScientificEvidence">New scientific evidence</option>
            <option value="increasedRiskBenefitRatio">Increased risk:benefit ratio</option>
            <option value="discontinuedByAnotherPhysician">Discontinued by another physician</option>
            <option value="cost">cost</option>
            <option value="drugInteraction">Drug interaction</option>
        </select>


        <br/>
        Comment:<br/>
        <textarea id="disComment" rows="3" cols="60"></textarea><br/>
        <input type="button" onclick="$('discontinueUI').hide();" value="Cancel"/>
        <input type="button" onclick="Discontinue2($('disDrugId').value,$('disReason').value,$('disComment').value);" value="Discontinue"/>

    </div>

    <div id="themeLegend" style="position: absolute;display:none; width:500px;height:200px;background-color:white;padding:20px;border:1px solid grey">
        <a href="javascript:void(0);" class="currentDrug">Drug that is current</a><br/>
        <a href="javascript:void(0);" class="archivedDrug">Drug that is archived</a><br/>
        <a href="javascript:void(0);" class="expireInReference">Drug the is current but will expire within the reference range</a><br/>
        <a href="javascript:void(0);" class="expiredDrug">Drug that is expired</a><br/>
        <a href="javascript:void(0);" class="longTermMed">Long Term Med Drug</a><br/>
        <a href="javascript:void(0);" class="discontinued">Discontinued Drug</a><br/><br/><br/><br/>
        <a href="javascript:void(0);" onclick="$('themeLegend').hide()">Close</a>
    </div>

<%
                        if (pharmacy != null) {
%>
<div id="Layer1" style="position: absolute; left: 1px; top: 1px; width: 350px; height: 311px; visibility: hidden; z-index: 1; background-color: white;"><!--  This should be changed to automagically fill if this changes often -->

    <table border="0" cellspacing="1" cellpadding="1" align="center" class="hiddenLayer">
        <tr class="LightBG">
            <td class="wcblayerTitle">&nbsp;</td>
            <td class="wcblayerTitle">&nbsp;</td>
            <td class="wcblayerTitle" align="right"><a href="javascript: function myFunction() {return false; }" onclick="hidepic('Layer1');" style="text-decoration: none;">X</a></td>
        </tr>

        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgName"/></td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.name%></td>
        </tr>

        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgAddress"/></td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.address%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgCity"/></td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.city%></td>
        </tr>

        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgProvince"/></td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.province%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgPostalCode"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.postalCode%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgPhone1"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.phone1%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgPhone2"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.phone2%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgFax"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.fax%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgEmail"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.email%></td>
        </tr>
        <tr class="LightBG">
            <td colspan="3" class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgNotes"/> :</td>
        </tr>
        <tr class="LightBG">
            <td colspan="3"><%=pharmacy.notes%></td>
        </tr>

    </table>

</div>
<%
                        }
%>
<script type="text/javascript">
    function resetStash(){
               var url="<c:out value="${ctx}"/>" + "/oscarRx/deleteRx.do?parameterValue=clearStash";
               var data = "";
               new Ajax.Request(url, {method: 'post',parameters:data});
               $('rxText').innerHTML="";//make pending prescriptions disappear.
               $("searchString").focus();
    }
    function iterateStash(){
                var url="<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?parameterValue=iterateStash";
                var data="";
                new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom});
    }
    function rxPageSizeSelect(){
               var ran_number=Math.round(Math.random()*1000000);
               var url="GetRxPageSizeInfo.do?method=view";
               var params = "demographicNo=<%=bean.getDemographicNo()%>&rand="+ran_number;  //hack to get around ie caching the page
               new Ajax.Request(url, {method: 'post',parameters:params});
    }
    function reprint2(scriptNo){
        var data="scriptNo="+scriptNo;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/rePrescribe2.do?method=reprint2";
       new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){
                oscarLog("successfully sent data "+url);
                popForm2();
            }});
        return false;
    }


    function deletePrescribe(randomId){
        var data="randomId="+randomId;
        var url="<c:out value="${ctx}"/>" + "/oscarRx/rxStashDelete.do?parameterValue=deletePrescribe";
        new Ajax.Request(url, {method: 'get',parameters:data});

    }

    function ThemeViewer(){

       var xy = Position.page($('drugProfile'));
       var x = (xy[0]+200)+'px';
       var y = xy[1]+'px';
       var wid = ($('drugProfile').getWidth()-300)+'px';
       var styleStr= {left: x, top: y,width: wid};

       $('themeLegend').setStyle(styleStr);
       $('themeLegend').show();
    }

    function useFav2(favoriteId){
        var randomId=Math.round(Math.random()*1000000);
        var data="favoriteId="+favoriteId+"&randomId="+randomId;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/useFavorite.do?parameterValue=useFav2";
        //oscarLog("---"+url);
        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom});
    }

   function Delete2(element){
        oscarLog(element.id);

        if(confirm('Are you sure you wish to delete the selected prescriptions?')==true){
             var id_str=(element.id).split("_");
             var id=id_str[1];
             //var id=element.id;
             var rxDate="rxDate_"+ id;
             var reRx="reRx_"+ id;
             var del="del_"+ id;
             var discont="discont_"+ id;
             var prescrip="prescrip_"+id;

           //  oscarLog(document.getElementsByName(caonima)[0]);
           //  oscarLog(document.getElementById(id));
             var url="<c:out value="${ctx}"/>" + "/oscarRx/deleteRx.do?parameterValue=Delete2"  ;
             var data="deleteRxId="+element.id;
            new Ajax.Request(url,{method: 'post',postBody:data,onSuccess:function(transport){
                  oscarLog("here");
                  $(rxDate).style.textDecoration='line-through';
                  $(reRx).style.textDecoration='line-through';
                  $(del).style.textDecoration='line-through';
                  $(discont).style.textDecoration='line-through';
                  $(prescrip).style.textDecoration='line-through';
                  oscarLog("here2");
            }});
        }
        return false;
    }

   function checkAllergy(id,atcCode){
         var url="<c:out value="${ctx}"/>" + "/oscarRx/getAllergyData.jsp"  ;
         var data="atcCode="+atcCode+"&id="+id;
         new Ajax.Request(url,{method: 'post',postBody:data,onSuccess:function(transport){
                 oscarLog("here");
                 var json=transport.responseText.evalJSON();

                  oscarLog(json);
                  oscarLog("here2 -- " +$('alleg_'+json.id));


                <%--  //Severity:</b>
                //<%=severityOfReaction(allerg[i].getAllergy().getSeverityOfReaction())%>
		//			<b>Onset of Reaction:</b> <%=onSetOfReaction(allerg[i].getAllergy().getOnSetOfReaction())%>
                --%>
                  var str = "Allergy: "+ json.alleg.DESCRIPTION + " Reaction: "+json.alleg.reaction;
                  $('alleg_'+json.id).innerHTML = str;
                  oscarLog("-- "+ $('alleg_'+json.id).innerHTML);
            }});
   }
   function checkIfInactive(id,dinNumber){
        var url="<c:out value="${ctx}"/>" + "/oscarRx/getInactiveDate.jsp"  ;
         var data="din="+dinNumber+"&id="+id;
         new Ajax.Request(url,{method: 'post',postBody:data,onSuccess:function(transport){
                 oscarLog("here");
                 var json=transport.responseText.evalJSON();

                  oscarLog(new Date(json.vec[0].time));
                  oscarLog("here inactive check 2 -- " +$('inactive_'+json.id));



                  var str = "Inactive Drug Since: "+new Date(json.vec[0].time).toDateString();
                  $('inactive_'+json.id).innerHTML = str;
                  oscarLog("-- "+ $('inactive_'+json.id).innerHTML);
            }});
   }


    function Discontinue(event,element){
       var id_str=(element.id).split("_");
       var id=id_str[1];

       var xy = Position.page($('drugProfile'));
       var x = (xy[0]+200)+'px';
       var y = xy[1]+'px';
       var wid = ($('drugProfile').getWidth()-300)+'px';
       var styleStr= {left: x, top: y,width: wid};
       oscarLog("styleStr="+styleStr);

        var drugName = $('prescrip_'+id).innerHTML;
        oscarLog("drugName="+drugName);
       $('discontinueUI').setStyle(styleStr);
       $('disDrug').innerHTML = drugName;
       $('discontinueUI').show();
       $('disDrugId').value=id;


    }

    function Discontinue2(id,reason,comment){
        var url="<c:out value="${ctx}"/>" + "/oscarRx/deleteRx.do?parameterValue=Discontinue"  ;
        var demoNo='<%=patient.getDemographicNo()%>';
        var data="drugId="+id+"&reason="+reason+"&comment="+comment+"&demoNo="+demoNo;
            new Ajax.Request(url,{method: 'post',postBody:data,onSuccess:function(transport){
                  oscarLog("Drug is now discontinued>"+transport.responseText);
                  var json=transport.responseText.evalJSON();
                  $('discontinueUI').hide();
                  $('rxDate_'+json.id).style.textDecoration='line-through';
                  $('reRx_'+json.id).style.textDecoration='line-through';
                  $('del_'+json.id).style.textDecoration='line-through';
                  $('discont_'+json.id).innerHTML = json.reason;
                  $('prescrip_'+json.id).style.textDecoration='line-through';
                  oscarLog("here2");
            }});

    }


//represcribe long term meds
    function RePrescribeLongTerm(){
        //var longTermDrugs=$(longTermDrugList).value;
       // var data="drugIdList="+longTermDrugs;
       var demoNo='<%=patient.getDemographicNo()%>';
              oscarLog("demoNo="+demoNo);
        var data="demoNo="+demoNo+"&showall=<%=showall%>";
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/rePrescribe2.do?method=repcbAllLongTerm";

        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom})
        return false;
    }

     function createNewRx(element){
       oscarLog("createNewRx called");
        var data="drugName="+element.value;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?parameterValue=createNewRx";
        new Ajax.Request(url, {method: 'get',parameters:data})
        return false;
    }

function customWarning2(){
    if (confirm('This feature will allow you to manually enter a drug.'
	+ '\nWarning: Only use this feature if absolutely necessary, as you will lose the following functionality:'
	+ '\n  *  Known Dosage Forms / Routes'
	+ '\n  *  Drug Allergy Information'
	+ '\n  *  Drug-Drug Interaction Information'
	+ '\n  *  Drug Information'
	+ '\n\nAre you sure you wish to use this feature?')==true) {
	//call another function to bring up prescribe.jsp
        var randomId=Math.round(Math.random()*1000000);
        var url="<c:out value="${ctx}"/>"+ "/oscarRx/WriteScript.do?parameterValue=newCustomDrug";
        var data="randomId="+randomId;
        new Ajax.Updater('rxText',url,{method:'get',parameters:data,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom})
    }

}
function saveCustomName(element){
    var elemId=element.id;
    var ar=elemId.split("_");
    var rand=ar[1];
    var url="<c:out value="${ctx}"/>"+"/oscarRx/WriteScript.do?parameterValue=saveCustomName";
    var data="customName="+element.value+"&randomId="+rand;
    var instruction="instructions_"+rand;
    var quantity="quantity_"+rand;
    var repeat="repeats_"+rand;
    new Ajax.Request(url, {method: 'get',parameters:data, onSuccess:function(transport){
            //output default instructions
            var json=transport.responseText.evalJSON();oscarLog("json: "+json.instructions);
                $(instruction).value=json.instructions;
                $(quantity).value=json.quantity;
                $(repeat).value=json.repeat;
            }});
}
function popForm2(){
        try{
            oscarLog("popForm2 called");
           // var url= "<c:out value="${ctx}"/>" + "/oscarRx/Preview2.jsp";
            var url= "<c:out value="${ctx}"/>" + "/oscarRx/ViewScript2.jsp";
                    oscarLog( "preview2 done");
                    myLightWindow.activateWindow({
                        href: url,
                        width: 660
                    });

        }
        catch(er){alert(er);}
        oscarLog("bottom of popForm");
    }

     function callTreatments(textId,id){
         var ele = $(textId);
         var url = "TreatmentMyD.jsp"
         var ran_number=Math.round(Math.random()*1000000);
         var params = "demographicNo=<%=bean.getDemographicNo()%>&cond="+ele.value+"&rand="+ran_number;  //hack to get around ie caching the page
         new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true});
         showpic('treatmentsMyD');

     }

     function callAdditionWebService(url,id){
         var ran_number=Math.round(Math.random()*1000000);
         var params = "demographicNo=<%=bean.getDemographicNo()%>&rand="+ran_number;  //hack to get around ie caching the page
         var updater=new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:false,insertion: Insertion.Bottom,evalScripts:true});
     }

     function callReplacementWebService(url,id){
               var ran_number=Math.round(Math.random()*1000000);
               var params = "demographicNo=<%=bean.getDemographicNo()%>&rand="+ran_number;  //hack to get around ie caching the page
               var updater=new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:false,evalScripts:true});
         }
          //callReplacementWebService("InteractionDisplay.jsp",'interactionsRx');
      <%--    <oscar:oscarPropertiesCheck property="MYDRUGREF_DS" value="yes">
          callReplacementWebService("GetmyDrugrefInfo.do?method=view",'interactionsRxMyD');
          </oscar:oscarPropertiesCheck>
      --%>
          callReplacementWebService("ListDrugs.jsp",'drugProfile');

    <%--
    new Ajax.Autocompleter("searchString", "autocomplete_choices", "search2.jsp", {paramName: "searchString",
       minChars: 2,
       //afterUpdateElement : getSelectionId,
       updateElement : upElement
});

function getSelectionId(text, li) {
    oscarLog('In selection id');
    var url = "prescribe.jsp";
    var ran_number=Math.round(Math.random()*1000000);
    var params = "demographicNo=<%=bean.getDemographicNo()%>&id="+li.id+"&text="+text+"&rand="+ran_number;  //hack to get around ie caching the page
    new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom
});

}

function upElement(li){
    //oscarLog('In up Element '+li);
    //alert($(li).innerHTML);
    getSelectionId($(li).innerHTML,li);
    $('searchString').value = "";
}

--%>


 //used in autocomplete
    function getSelectionId(text, li) {
       var ran_number=Math.round(Math.random()*1000000);
        oscarLog('In selection id');
        var url1=  "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?parameterValue=createNewRx";
        var data1="randomId="+ran_number+"&drugId="+li.id+"&text="+text;
        new Ajax.Updater('rxText',url1, {method:'get',parameters:data1,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom})
    }

YAHOO.example.BasicRemote = function() {
    //var oDS = new YAHOO.util.XHRDataSource("http://localhost:8080/drugref2/test4.jsp");
    var url = "<c:out value="${ctx}"/>" + "/oscarRx/searchDrug.do?method=jsonSearch";
    var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ingoreStaleResponse'});
    oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
    // Define the schema of the delimited results
    oDS.responseSchema = {
        resultsList : "results",
        fields : ["name", "id"]
    };
    // Enable caching
    oDS.maxCacheEntries = 500;

    oDS.connXhrMode ="cancelStaleRequests";
                        /*  Not sure which one of these to use
                        cancelStaleRequests
                            If a request is already in progress, cancel it before sending the next request.
                        ignoreStaleResponses
                            Send all requests, but handle only the response for the most recently sent request.
                        */
    // Instantiate the AutoComplete
    var oAC = new YAHOO.widget.AutoComplete("searchString", "autocomplete_choices", oDS);
    oAC.queryMatchSubset = true;
    oAC.minQueryLength = 3;
    oAC.maxResultsDisplayed = 25;
    oAC.formatResult = resultFormatter;
    //oAC.typeAhead = true;
    //oAC.queryMatchContains = true;
    oAC.itemSelectEvent.subscribe(function(type, args) {
 		oscarLog(type+" :: "+args);
                oscarLog(args[2]);
                arr = args[2];
                oscarLog('In yahoo----'+arr[1]);oscarLog('In yahoo----'+arr[0]);
                var url = "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?parameterValue=createNewRx"; //"prescribe.jsp";
                var ran_number=Math.round(Math.random()*1000000);
                var name=encodeURIComponent(arr[0]);
                var params = "demographicNo=<%=bean.getDemographicNo()%>&drugId="+arr[1]+"&text="+name+"&randomId="+ran_number;  //hack to get around ie caching the page
                new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom});
                $('searchString').value = "";
    });
<%--
    /* when this was enabled nothing happened when you selected the event
     oAC.itemSelectEvent = function(acinst,ele,item){
        oscarLog(acinst+" :: "+ele+" -- "+item);
        return this;
    }
        */
    /*
     oAC.applyLocalFilter = true; // pass results thru filter
    oAC.filterResults = function(sQuery, oFullResponse,oParsedResponse, oCallback) {
        oscarLog("inhere"+sQuery);
        var matches = [], matchee;
        for(var i=0; i<oParsedResponse.results.length; i++) {
            if(oParsedResponse.results[i].someValue > 0) {
                matches[matches.length] =oParsedResponse.results[i]
            }
        }
        //oParsedResponse.results = matches;
        return oParsedResponse;
    }
*/
--%>

    return {
        oDS: oDS,
        oAC: oAC
    };
}();

function addFav(randomId,brandName){
    var favoriteName = window.prompt('Please enter a name for the Favorite:',  brandName);

   if (favoriteName.length > 0){
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/addFavorite2.do?parameterValue=addFav2";
        var data="randomId="+randomId+"&favoriteName="+favoriteName;
        new Ajax.Request(url, {method: 'get',parameters:data, onSuccess:function(transport){
              window.location.href="SearchDrug3.jsp";
        }})
   }
}
    var resHidden2 = 0;
    function showHiddenRes(){
        var list = $$('div.hiddenResource');

        if(resHidden2 == 0){
          list.invoke('show');
          resHidden2 = 1;
          $('showHiddenResWord').update('hide');
        }else{
          $('showHiddenResWord').update('show');
          list.invoke('hide');
          resHidden2 = 0;
        }
    }

   function HideW(id,resourceId,updated){
        var url = 'GetmyDrugrefInfo.do?method=setWarningToHide';
        //callReplacementWebService("GetmyDrugrefInfo.do?method=setWarningToHide",'interactionsRxMyD');function callReplacementWebService(url,id){
        var ran_number=Math.round(Math.random()*1000000);
        var params = "resId="+resourceId+"&updatedat="+updated+"&rand="+ran_number;  //hack to get around ie caching the page

        //console.log("params: "+params);
        new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true});

    }


function setSearchedDrug(drugId,name){

    var url = "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?parameterValue=createNewRx";
    var ran_number=Math.round(Math.random()*1000000);
    oscarLog("name in setSearchedDrug: "+name);
    oscarLog("encodeURIComponent name in setSearchedDrug: "+encodeURIComponent(name));
    name=encodeURIComponent(name);
    var params = "demographicNo=<%=bean.getDemographicNo()%>&drugId="+drugId+"&text="+name+"&randomId="+ran_number;  //hack to get around ie caching the page
    oscarLog(params);
    new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom});

    $('searchString').value = "";
}
 //represcribe a drug
    function represcribe(element){
        var elemId=element.id;
        var ar=elemId.split("_");
        var drugId=ar[1];
        var data="drugId="+drugId;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/rePrescribe2.do?method=represcribe2";
        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:false,evalScripts:true,insertion: Insertion.Bottom});

    }

   //not used now.
   function checkQuantity(element){
        var elemId=element.id;
        var ar=elemId.split("_");
        var rand=ar[1];
        var calQ="calQuantity_"+rand;
        var warning=" WARNING: quantities don't match";
        //oscarLog($(calQ).value);
        //oscarLog(element.value);
        if(element.value==$(calQ).value){
            //if it contains warning, remove warning
            if(($("rxString_"+rand).innerHTML).include(warning)){
                var s=$("rxString_"+rand).innerHTML;
                $("rxString_"+rand).innerHTML=s.replace(/WARNING: quantities don't match/,"");
            }else{}
            //if it doesn't contain warning , do ntohing
            oscarLog("quantity entered equals to quantity calculated form instruction="+element.value);
        }else{
            //if it contains WARNING,do nothing
            if(($("rxString_"+rand).innerHTML).include(warning)){

            }else{
                //if it doesn't contain warning, add
                $("rxString_"+rand).innerHTML=$("rxString_"+rand).innerHTML + warning;
            }
            oscarLog("quantity entered does not match quantity calculated form instructions.");
        }
    }
function updateQty(element){
        var elemId=element.id;
        var ar=elemId.split("_");
        var rand=ar[1];
        var data="randomId="+rand+"&action=updateQty&quantity="+element.value;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?parameterValue=updateDrug";

        var rxMethod="rxMethod_"+rand;
        var rxRoute="rxRoute_"+rand;
        var rxFreq="rxFreq_"+rand;
        var rxDrugForm="rxDrugForm_"+rand;
        var rxDuration="rxDuration_"+rand;
        var rxDurationUnit="rxDurationUnit_"+rand;
        var rxAmt="rxAmount_"+rand;
        var str;
        var rxString="rxString_"+rand;

        new Ajax.Request(url, {method: 'get',parameters:data, onSuccess:function(transport){
                var json=transport.responseText.evalJSON();
                str="Method:"+json.method+"; Route:"+json.route+"; Frequency:"+json.frequency+"; Min:"+json.takeMin+"; Max:"
                    +json.takeMax +"; Duration:"+json.duration+"; DurationUnit:"+json.durationUnit+"; Quantity:"+json.calQuantity;
                oscarLog("json.duration="+json.duration);

                if(json.prn){
                    str=str+" prn";
                } else{ }
                oscarLog("str="+str);
                $(rxString).innerHTML=str;//display parsed string below instruction.
            }});
        return true;
}
    function parseIntr(element){
        var instruction="instruction="+element.value+"&action=parseInstructions";
        //oscarLog(instruction);
        var elemId=element.id;
        var ar=elemId.split("_");
        var rand=ar[1];
        var instruction="instruction="+element.value+"&action=parseInstructions&randomId="+rand;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/UpdateScript.do?parameterValue=updateDrug";
        /*var rxMethod="rxMethod_"+rand;
        var rxRoute="rxRoute_"+rand;
        var rxFreq="rxFreq_"+rand;
        var rxDrugForm="rxDrugForm_"+rand;
        var rxDuration="rxDuration_"+rand;
        var rxDurationUnit="rxDurationUnit_"+rand;*/
        var quantity="quantity_"+rand;
        var str;
        var rxString="rxString_"+rand;

        new Ajax.Request(url, {method: 'get',parameters:instruction, onSuccess:function(transport){
                var json=transport.responseText.evalJSON();
                str="Method:"+json.method+"; Route:"+json.route+"; Frequency:"+json.frequency+"; Min:"+json.takeMin+"; Max:"
                    +json.takeMax +"; Duration:"+json.duration+"; DurationUnit:"+json.durationUnit+"; Quantity:"+json.calQuantity;
                oscarLog("json.duration="+json.duration);
                if(json.prn){
                    str=str+" prn";
                } else{ }
                oscarLog("str="+str);
                $(rxString).innerHTML=str;//display parsed string below instruction.
                $(quantity).value=json.calQuantity;
            }});
        return true;
    }

    function addLuCode(eleId,luCode){
        $(eleId).value = $(eleId).value +" LU Code: "+luCode;
    }

         function getRenalDosingInformation(divId,atcCode){
               var url = "RenalDosing.jsp";
               var ran_number=Math.round(Math.random()*1000000);
               var params = "demographicNo=<%=bean.getDemographicNo()%>&atcCode="+atcCode+"&divId="+divId+"&rand="+ran_number;  //hack to get around ie caching the page
               //alert(params);
               new Ajax.Updater(divId,url, {method:'get',parameters:params,asynchronous:true});
               //alert(origRequest.responseText);
         }


    function updateSaveAllDrugs(){
        var data=Form.serialize($('drugForm'));
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?parameterValue=updateSaveAllDrugs";
        new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){
                oscarLog("successfully sent data "+url);
                popForm2();
            }});
        return false;
    }
<%if (request.getParameter("ltm") != null && request.getParameter("ltm").equals("true")){%>

RePrescribeLongTerm();
<%}%>


function checkEnterSendRx(){
        popupRxSearchWindow();
        return false;
}



$("searchString").focus();



</script>
<script language="javascript" src="../commons/scripts/sort_table/css.js"></script>
<script language="javascript" src="../commons/scripts/sort_table/common.js"></script>
<script language="javascript" src="../commons/scripts/sort_table/standardista-table-sorting.js"></script>

</body>
</html:html>








