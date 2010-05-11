<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/casemgmt/taglibs.jsp"%>
<%@ page errorPage="/casemgmt/error.jsp"%>
<%@ page language="java"%>

<% if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp"); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<nested:define id="rowOneSize" name="caseManagementViewForm"
	property="ectWin.rowOneSize" />
<nested:define id="rowTwoSize" name="caseManagementViewForm"
	property="ectWin.rowTwoSize" />
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META HTTP-EQUIV="EXPIRES" CONTENT="Wed, 26 Feb 2004 08:21:57 GMT">

<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css"
	type="text/css">
<link rel="stylesheet"
	href="<c:out value="${ctx}"/>/oscarEncounter/encounterStyles.css"
	type="text/css">
<link rel="stylesheet" type="text/css"
	href="<c:out value="${ctx}"/>/css/print.css" media="print">
<script src="<c:out value="${ctx}"/>/share/javascript/prototype.js"
	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/scriptaculous.js"
	type="text/javascript"></script>

<%-- for popup menu of forms --%>
<script src="<c:out value="${ctx}"/>/share/javascript/popupmenu.js"
	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/menutility.js"
	type="text/javascript"></script>

<!-- library for rounded elements -->
<link rel="stylesheet" type="text/css"
	href="<c:out value="${ctx}/share/css/niftyCorners.css" />">
<script type="text/javascript"
	src="<c:out value="${ctx}/share/javascript/nifty.js"/>"></script>

<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="<c:out value="${ctx}"/>/share/calendar/calendar.css"
	title="win2k-cold-1">

<!-- main calendar program -->
<script type="text/javascript"
	src="<c:out value="${ctx}"/>/share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="<c:out value="${ctx}"/>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript"
	src="<c:out value="${ctx}"/>/share/calendar/calendar-setup.js"></script>

<!-- js implementation of markdown -->
<script type="text/javascript"
	src="<c:out value="${ctx}/share/javascript/showdown.js"/>"></script>

<!-- js window size utility funcs since prototype's funcs are buggy in ie6 -->
<script type="text/javascript"
	src="<c:out value="${ctx}/share/javascript/screen.js"/>"></script>

<!-- scriptaculous based select box -->
<script type="text/javascript"
	src="<c:out value="${ctx}/share/javascript/select.js"/>"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<html:base />
<title>Case Management</title>
<meta http-equiv="Cache-Control" content="no-cache">
<script type="text/javascript">
        var itemColours = new Object();
        var autoCompleted = new Object();
        var autoCompList = new Array();
        var measurementWindows = new Array();
        var openWindows = new Object();
        var origCaseNote = "";
        var origObservationDate = "";
        var tmpSaveNeeded = true;
        var calendar;
        
        function popupPage(vheight,vwidth,name,varpage) { //open a new popup window
          var page = "" + varpage;
          windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
                //var popup =window.open(page, "<bean:message key="oscarEncounter.Index.popupPageWindow"/>", windowprops);
                openWindows[name] = window.open(page, name, windowprops);

                if (openWindows[name] != null) {        
                    if (openWindows[name].opener == null) {
                        openWindows[name].opener = self;
                        alert("<bean:message key="oscarEncounter.Index.popupPageAlert"/>");
                    }
                    openWindows[name].focus();
                }     
                
        } 
        
        function urlencode(str) {
            var ns = (navigator.appName=="Netscape") ? 1 : 0;
            if (ns) { return escape(str); }
            var ms = "%25#23 20+2B?3F<3C>3E{7B}7D[5B]5D|7C^5E~7E`60";
            var msi = 0;
            var i,c,rs,ts ;
            while (msi < ms.length) {
                c = ms.charAt(msi);
                rs = ms.substring(++msi, msi +2);
                msi += 2;
                i = 0;
                while (true)	{
                    i = str.indexOf(c, i);
                    if (i == -1) break;
                    ts = str.substring(0, i);
                    str = ts + "%" + rs + str.substring(++i, str.length);
                }
            }
            return str;
        }
    
        function measurementLoaded(name) {
            measurementWindows.push(openWindows[name]);
        }
        
        function onClosing() {    
            for( var idx = 0; idx < measurementWindows.length; ++idx ) {
                if( !measurementWindows[idx].closed )
                    measurementWindows[idx].parentChanged = true;
            }
            
            //check to see if we need to back up case note
            if( tmpSaveNeeded && (origCaseNote != $(caseNote).value || origObservationDate != $("observationDate").value) )
                autoSave(false);
        }
        
        var numMenus = 3;
        function showMenu(menuNumber, eventObj) {        
            var menuId = 'menu' + menuNumber;
            return showPopup(menuId, eventObj);
        }
        
   /*
    *Set expand and collapse images for navbar divs and show/hide lines above threshold
    *Store function event listeners so we start/stop listening
    */
   var imgfunc = new Object();
   function listDisplay(Id, threshold) {
            if( threshold == 0 )
                return;
                
            var listId = Id + "list";            
            var list = $(listId);
            var items = list.getElementsByTagName('li');
            items = $A(items);
            var obj = {};
            
            var topName = "img"+Id+"0";
            var midName = "img"+Id+(threshold-1);
            var lastName = "img"+Id+(items.length-1);
            var topImage = $(topName);            
            var midImage = $(midName);
            var lastImage = $(lastName);
            var expand;
            var expandPath = "<c:out value="${ctx}"/>/oscarEncounter/graphics/expand.gif";
            var collapsePath = "<c:out value="${ctx}"/>/oscarMessenger/img/collapse.gif";
            var transparentPath = "<c:out value="${ctx}"/>/images/clear.gif";
            
            for( var idx = threshold; idx < items.length; ++idx ) {
                if( items[idx].style.display == 'block' ) {
                    items[idx].style.display = 'none';
                    expand = true;
                }
                else {
                    items[idx].style.display = 'block';
                    expand = false;
                }
            }

            if( expand ) {
                topImage.src = transparentPath;
                lastImage.src = transparentPath;
                midImage.src = expandPath;
                midImage.title = (items.length - threshold) + " items more";

                Element.stopObserving(topImage, "click", imgfunc[topName]);
                Element.stopObserving(lastImage, "click", imgfunc[lastName]);
                
                imgfunc[midName] = clickListDisplay.bindAsEventListener(obj,Id,threshold);
                Element.observe(midImage, "click", imgfunc[midName]);
            }
            else {
                topImage.src = collapsePath;
                lastImage.src = collapsePath;
                midImage.src = transparentPath;
                midImage.title = "";

                Element.stopObserving(midImage, "click", imgfunc[midName]);
                
                imgfunc[topName] = clickListDisplay.bindAsEventListener(obj,Id,threshold);
                Element.observe(topImage, "click", imgfunc[topName]);
                
                imgfunc[lastName] = clickListDisplay.bindAsEventListener(obj,Id,threshold);
                Element.observe(lastImage, "click", imgfunc[lastName]);                
            }        
    
    }  
    
    function clickListDisplay(e) {
        var data = $A(arguments);
        data.shift();
        listDisplay(data[0],data[1]);
    }
      
    
function grabEnter(id, event) {
    var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
    if (keyCode == 13) {
        $(id).click();        
        return false;
    }            
    
    return true;
}
function setupNotes(){
if(!NiftyCheck())
    return;
    
    Rounded("div.note","all","transparent","#CCCCCC","big border #000000");
    
    //need to set focus after rounded is called
    adjustCaseNote();    
    setCaretPosition($(caseNote), $(caseNote).value.length);
    $(caseNote).focus();
    
}
var minDelta =  0.93;
var minMain;
var minWin;
function monitorWinSize(e) {            
    var win = pageWidth();
    var main = Element.getWidth("main");    
    
    if( e == null ) {
        minMain = Math.round(main * minDelta);
        minWin = Math.round(win * minDelta);
    }

    if( main < minMain ) {        
        $("main").style.width = minMain + "px";                
    }
    else if( win >= minWin &&  main == minMain ) {
        $("main").style.width = "100%";       
    }
    
    var ht = $("mainContent").getHeight() + $("topContent").getHeight();
    //$("rightNavBar").style.height = ht + "px";  //make sure navbar covers complete height regardless of content
}

var leftColumnFunc;
var rightColumnFunc;
function init() {        
    var navBars = new navBarLoader();
    navBars.load();  
    monitorWinSize(null);
    Element.observe(window, "resize", monitorWinSize);
    
    new Draggable("leftNavBarColumn");
    leftColumnFunc = hideNavBar.bindAsEventListener({},"leftNavBar");
    Element.observe("leftNavBarColumn","dblclick",leftColumnFunc);
       
    new Draggable("rightNavBarColumn");
    rightColumnFunc = hideNavBar.bindAsEventListener({},"rightNavBar");
    Element.observe("rightNavBarColumn","dblclick",rightColumnFunc);
 
}

function hideNavBar(e) {
    var args = $A(arguments);
    args.shift();
    var divId = args[0];
    var column = divId + "Column";    
    
    new Effect.BlindUp(divId);
    
    if( divId.indexOf("left") > -1 ) {
        Element.stopObserving(column,"dblclick",leftColumnFunc);
        leftColumnFunc = showNavBar.bindAsEventListener({},divId);
        Element.observe(column,"dblclick",leftColumnFunc);    
    }    
    else {
        Element.stopObserving(column,"dblclick",rightColumnFunc);
        rightColumnFunc = showNavBar.bindAsEventListener({},divId);
        Element.observe(column,"dblclick",rightColumnFunc); 
   }        
    
}

function showNavBar(e) {
    var args = $A(arguments);
    args.shift();
    var divId = args[0];
    var column = divId + "Column"; 
    
    new Effect.BlindDown(divId);
    
   if( divId.indexOf("left") > -1 ) {
        Element.stopObserving(column,"dblclick",leftColumnFunc);
        leftColumnFunc = hideNavBar.bindAsEventListener({},divId);
        Element.observe(column,"dblclick",leftColumnFunc);    
    }    
    else {
        Element.stopObserving(column,"dblclick",rightColumnFunc);
        rightColumnFunc = hideNavBar.bindAsEventListener({},divId);
        Element.observe(column,"dblclick",rightColumnFunc); 
   }
}

function navBarLoader() {
    //$("leftNavBar").style.height = $("content").getHeight();
    
    /*
     *is right navbar present?
     *if so work with it
     *if not, set max lines to 0
     */
    if( $("rightNavBar") != undefined ) {        
        this.maxRightNumLines = Math.floor($("content").getHeight() / 12);        
    }
    else
        this.maxRightNumLines = 0;
        
    this.maxLeftNumLines = Math.floor($("content").getHeight() / 12);    
    this.arrLeftDivs = new Array();
    this.arrRightDivs = new Array();
    this.rightTotal = 0;
    this.leftTotal = 0;
    this.leftDivs = 10;
    this.rightDivs = 3;
    this.leftReported = 0;
    this.rightReported = 0;
    
    //init ajax calls for all sections of the navbars and create a div for each ajax request
    this.load = function() {
             
            var leftNavBar = { 
                  preventions:  "<c:out value="${ctx}"/>/oscarEncounter/displayPrevention.do?hC=009999",
                  tickler:      "<c:out value="${ctx}"/>/oscarEncounter/displayTickler.do?hC=FF6600",
                  Dx:           "<c:out value="${ctx}"/>/oscarEncounter/displayDisease.do?hC=5A5A5A",
                  forms:        "<c:out value="${ctx}"/>/oscarEncounter/displayForms.do?hC=917611",
                  eforms:       "<c:out value="${ctx}"/>/oscarEncounter/displayEForms.do?hC=11CC00",<%/*  88E900 */%> 
                  docs:         "<c:out value="${ctx}"/>/oscarEncounter/displayDocuments.do?hC=476BB3",
                  labs:         "<c:out value="${ctx}"/>/oscarEncounter/displayLabs.do?hC=A0509C", <%/* 550066   */%>                         
                  msgs:         "<c:out value="${ctx}"/>/oscarEncounter/displayMessages.do?hC=DDDD00", <% /* FF33CC */ %>
                  measurements: "<c:out value="${ctx}"/>/oscarEncounter/displayMeasurements.do?hC=344887",
                  consultation: "<c:out value="${ctx}"/>/oscarEncounter/displayConsultation.do"
              };
              
            var rightNavBar = {
                  allergies:    "<c:out value="${ctx}"/>/oscarEncounter/displayAllergy.do?hC=FF9933",
                  Rx:           "<c:out value="${ctx}"/>/oscarEncounter/displayRx.do?hC=C3C3C3",
                  issues:       "<c:out value="${ctx}"/>/oscarEncounter/displayIssues.do?hC=CC9900"
              };
          var URLs = new Array();
          URLs.push(leftNavBar);
          URLs.push(rightNavBar);
              
        for( var j = 0; j < URLs.length; ++j ) {                                    
            
            var navbar;
            if( j == 0 )                
                navbar = "leftNavBar";            
            else if( j == 1)                            
                navbar = "rightNavBar";            
            
            for( idx in URLs[j] ) {                                
                var div = document.createElement("div");            
                div.className = "leftBox";
                div.style.display = "block";
                div.id = idx;
                $(navbar).appendChild(div); 
                
                if( navbar == "leftNavBar" )
                    this.arrLeftDivs.push(div);
                if( navbar == "rightNavBar" )
                    this.arrRightDivs.push(div); 
                    
                this.popColumn(URLs[j][idx],idx,idx, navbar, this);
            }
            
        }
    
    
    };
    
    //update each ajax div with info from request
    this.popColumn = function (url,div,params, navBar, navBarObj) {    
        params = "cmd=" + params;
        
        var objAjax = new Ajax.Request (                        
                            url,
                            {
                                method: 'post', 
                                postBody: params,
                                evalScripts: true,
                                /*onLoading: function() {                            
                                                $(div).update("<p>Loading ...<\/p>");
                                            }, */                            
                                onSuccess: function(request) {                            
                                                while( $(div).firstChild )
                                                    $(div).removeChild($(div).firstChild);
                                                
                                                $(div).update(request.responseText);  

                                                //track ajax completions and display divs when last ajax call completes                                                
                                                navBarObj.display(navBar,div);
                                           }, 
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>Error:<\/h3>" + request.status;
                                            }
                            }

                      );
        };
        
        //format display and show divs in navbars
        this.display = function(navBar,div) {          
             
            //add number of items plus header to total
            var reported = 0;
            var numDivs = 0;
            var arrDivs;
            if( navBar == "leftNavBar" ) {
                this.leftTotal += parseInt($F(div+"num")) + 1;                                
                reported = ++this.leftReported;
                numDivs = this.leftDivs;
                arrDivs = this.arrLeftDivs;
            }
            else if( navBar == "rightNavBar" ) {
                this.rightTotal += parseInt($F(div+"num")) + 1;
                reported = ++this.rightReported;
                numDivs = this.rightDivs;
                arrDivs = this.arrRightDivs;
            }
            
            if( reported == numDivs ) {
                                
                /*
                 * do we have more lines than permitted?
                 * if so we need to reduce display
                 */
                var overflow = this.leftTotal - this.maxLeftNumLines;                
                if( navBar == "leftNavBar" && overflow > 0 )                    
                    this.adjust(this.arrLeftDivs, this.leftTotal, overflow);                                    
                    
                overflow = this.rightTotal - this.maxRightNumLines;                
                if( navBar == "rightNavBar" && overflow > 0 )
                    this.adjust(this.arrRightDivs, this.rightTotal, overflow);
            
            } //end if
        };
        
        this.adjust = function(divs, total, overflow) {
            //spread reduction across all divs weighted according to number of lines each div has            
            var num2reduce;
            var numLines;
            var threshold;
            for( var idx = 0; idx < divs.length; ++idx ) {
                numLines = parseInt($F(divs[idx].id + "num"));
                num2reduce = Math.ceil(overflow * (numLines/total));
                if( num2reduce == numLines && num2reduce > 0 ) 
                    --num2reduce;
                
                threshold = numLines - num2reduce;
               // console.log(idx + " threshold " + threshold);
                listDisplay(divs[idx].id, threshold);                
            }        
        };

}

</script>
</head>
<body id="body" style="margin: 0px;" onload="init()"
	onunload="onClosing()">
<div id="main">
<div id="header" style="background-color: #CCCCFF;"><tiles:insert
	attribute="header" /></div>

<div id="leftNavBarColumn"
	style="position: absolute; z-index: 10; top: 30px; left: 10px; width: 20%; background-color: transparent;">
<tiles:insert attribute="navigation" /></div>

<div id="content" style="float: left; background-color: #CCCCFF;">
<tiles:insert attribute="body" /></div>
</div>
</body>
</html:html>
