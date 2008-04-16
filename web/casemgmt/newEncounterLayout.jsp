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

<%@ include file="/casemgmt/taglibs.jsp" %>
<%@ page errorPage="/casemgmt/error.jsp" %>
<%@ page language="java"%>

<% if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp"); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%
    oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
    bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean");
    
    pageContext.setAttribute("providerNo",bean.providerNo, pageContext.PAGE_SCOPE);
%>

<nested:define id="rowOneSize" name="caseManagementViewForm" property="ectWin.rowOneSize"/>
<nested:define id="rowTwoSize" name="caseManagementViewForm" property="ectWin.rowTwoSize"/>
<html:html locale="true">
  <head>
      <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
      <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
      <META HTTP-EQUIV="EXPIRES" CONTENT="Wed, 26 Feb 2004 08:21:57 GMT">      

  	<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>	
	<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css" type="text/css">
        <link rel="stylesheet" href="<c:out value="${ctx}"/>/oscarEncounter/encounterStyles.css" type="text/css">         
    <link rel="stylesheet" type="text/css" href="<c:out value="${ctx}"/>/css/print.css" media="print">
    <script src="<c:out value="${ctx}"/>/share/javascript/prototype.js" type="text/javascript"></script>
    <script src="<c:out value="${ctx}"/>/share/javascript/scriptaculous.js" type="text/javascript"></script>    

    <%-- for popup menu of forms --%>
    <script src="<c:out value="${ctx}"/>/share/javascript/popupmenu.js" type="text/javascript"></script>
    <script src="<c:out value="${ctx}"/>/share/javascript/menutility.js" type="text/javascript"></script>

    <!-- library for rounded elements -->
    <link rel="stylesheet" type="text/css" href="<c:out value="${ctx}/share/css/niftyCorners.css" />">
    <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/nifty.js"/>"></script>
    
      <!-- calendar stylesheet -->
  <link rel="stylesheet" type="text/css" media="all" href="<c:out value="${ctx}"/>/share/calendar/calendar-blue.css" title="win2k-cold-1">

  <!-- main calendar program -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar.js"></script>

  <!-- language for the calendar -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

  <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar-setup.js"></script>
  
  <!-- js implementation of markdown -->
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/showdown.js"/>"></script>
  
  <!-- js window size utility funcs since prototype's funcs are buggy in ie6 -->
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/screen.js"/>"></script>
  
  <!-- scriptaculous based select box -->
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/select.js"/>"></script>
    
    <style type="text/css">
        
        /*CPP Format */
        li.cpp {
            color: #000000;         
            font-family:arial,sans-serif;
        }
        
        /*Note format */
        div.newNote {
            color: #000000;         
            font-family:arial,sans-serif;
            font-size:0.8em;
            margin: 5px 0px 5px 5px;
            float:left;  
            width:98%;            
        }
        
        div.newNote pre {
            color: #000000;      
            font-family:arial,sans-serif;
            margin: 0px 3px 0px 3px;          
            width:100%; 
            clear:left;            
        }
        
        div.note {
            color: #000000;         
            font-family:arial,sans-serif;
            margin: 3px 0px 3px 5px;
            float:left;  
            width:98%;
        }
        
        div.note pre {
            color: #000000;      
            font-family:arial,sans-serif;
            margin: 0px 3px 0px 3px;          
            width:100%; 
            clear:left;
        }
       .sig {
            background-color:#CCCCFF;
            color: #000000;  
            width:100%;
            font-size:9px;
        }                
        
        .txtArea {
            font-family:arial,sans-serif; 
            font-size:1.0em;             
            width:99%; 
            rows:10; 
            overflow:hidden; 
            border:none; 
            font-family:arial,sans-serif;             
            margin: 0px 3px 0px 3px;
        }
      
        p.passwd {
            margin:0px 3px; 0px 3px;        
        }
        
        /* span formatting for measurements div found in ajax call */
        span.measureCol1 {            
            float: left;
            width: 50px;            
        }
        
        span.measureCol2 {
            float: left;
            width: 55px;
        }
        
        span.measureCol3 {
            float: left;                        
        }
        
        .topLinks  {
            color: black;
            text-decoration:none;
            font-size:9px;                
        }
        
        .topLinkhover { 
            color: blue;
            text-decoration: underline;
        }
        
        /* formatting for navbar */               
        .links {
            color: blue;
            text-decoration:none;
            font-size:9px;
        }
        
        .linkhover { 
            color: black;
            text-decoration: underline;
        }
        
    /* template styles*/
          
          .enTemplate_name_auto_complete {
            width: 350px;
            background: #fff;
            font-size: 9px;
            text-align:left;
          }
          .enTemplate_name_auto_complete ul {
            border:1px solid #888;
            margin:0;
            padding:0;
            width:100%;
            list-style-type:square;
            list-style-position:inside;
          }
          .enTemplate_name_auto_complete ul li {
            margin:0;
            padding:3px;            
          }
          .enTemplate_name_auto_complete ul li.selected { 
            background-color: #ffb; 
            text-decoration: underline;
          }
          .enTemplate_name_auto_complete ul strong.highlight { 
            color: #800; 
            margin:0;
            padding:0;
          }


        /* CPP textareas */
        .rowOne {
            height: <nested:write name="rowOneSize"/>px;
            width: 97%;
            overflow:auto;
        }
        
        .rowTwo {
            height: <nested:write name="rowTwoSize"/>px;
            width:97%;
            margin-left:4px;            
            overflow:auto;
        }
        
        /* Encounter type select box */
        div.autocomplete {
          position:absolute;
          width:400px;
          background-color:white;
          border:1px solid #ccc;
          margin:0px;
          padding:0px;
          font-size:9px;
          text-align:left;
          max-height:200px;
          overflow:auto;
        }
        div.autocomplete ul {
          list-style-type:none;
          margin:0px;
          padding:0px;
        }
        div.autocomplete ul li.selected { 
          background-color: #EAF2FB;
        }
        div.autocomplete ul li {
          list-style-type:none;
          display:block;
          margin:0;
          padding:2px;
          cursor:pointer;
        }
        
        .encTypeCombo /* look&feel of scriptaculous select box*/
        {
          margin: 0px;/* 5px 10px 0px;*/
          font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;
          font-size:9px;
          width: 200px;          
          text-align:left; 
          vertical-align: middle;
          background: #FFFFFF url('<c:out value="${ctx}"/>/images/downarrow_inv.gif') no-repeat right;
          height:18px;
          cursor: pointer;
          border:1px solid #ccc;
          color: #000000;
          
        }
        
        .printOps {                        
            background-color:#CCCCFF;
            font-size:9px;
                        
            position: absolute;
            display:none;
            z-index:1;
            width:200px;
            right:100px;
            bottom:200px;                                                
        }
        
        .showEdContainer {        
            position: absolute;
            display:none;
            z-index:100;
            right:100px;
            bottom:200px;            
            background-color:transparent;
            font-size:8px;
            /*border: thin ridge black;*/
            text-align: center;
        }
        
        .showEdPosition { 
            display: table-cell;        
            vertical-align: middle;            
        }
        
        .showEdContent {            
            /*border: thin ridge black;*/
            background-color:#CCCCFF;
            font-size:9px;
                        
            position: absolute;
            display:none;
            z-index:100;
            right:100px;
            bottom:200px;                                    
            text-align: center;
        }
        

    </style>
<!--[if IE]>
    <style type=text/css>
    
        .showEdContent { 
            width:450px;
        }
     </style>
 <![endif]-->
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
            
            var saveThreshold = Id + "threshold";
            if( $(saveThreshold) != undefined )
                $(saveThreshold).value = threshold;
                
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
function monitorNavBars(e) {            
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
        
}

function init() {        
    var navBars = new navBarLoader();
    navBars.load();  
    monitorNavBars(null);
    showIssueNotes();
    Element.observe(window, "resize", monitorNavBars);
    new Draggable("showEditNote");
    
    if(!NiftyCheck())
        return;

    Rounded("div.showEdContent","all","transparent","#CCCCCC","big border #000000"); 
    Rounded("div.printOps","all","transparent","#CCCCCC","big border #000000");
    Calendar.setup({ inputField : "printStartDate", ifFormat : "%d-%b-%Y", showsTime :false, button : "printStartDate_cal", singleClick : true, step : 1 });    
    Calendar.setup({ inputField : "printEndDate", ifFormat : "%d-%b-%Y", showsTime :false, button : "printEndDate_cal", singleClick : true, step : 1 });    
    
}

/*
 *Draw the cpp views
 */
function showIssueNotes() {
    var issueNoteUrls = {
        divR1I1:    "<c:out value="${ctx}"/>/CaseManagementView.do?hc=996633&method=listNotes&providerNo=<c:out value="${providerNo}"/>&demographicNo=<c:out value="${demographicNo}"/>&issue_code=SocHistory&title=Social%20History&cmd=divR1I1",
        divR1I2:    "<c:out value="${ctx}"/>/CaseManagementView.do?hc=996633&method=listNotes&providerNo=<c:out value="${providerNo}"/>&demographicNo=<c:out value="${demographicNo}"/>&issue_code=MedHistory&title=Medical%20History&cmd=divR1I2",
        divR2I1:    "<c:out value="${ctx}"/>/CaseManagementView.do?hc=996633&method=listNotes&providerNo=<c:out value="${providerNo}"/>&demographicNo=<c:out value="${demographicNo}"/>&issue_code=Concerns&title=Ongoing%20Concerns&cmd=divR2I1",
        divR2I2:    "<c:out value="${ctx}"/>/CaseManagementView.do?hc=996633&method=listNotes&providerNo=<c:out value="${providerNo}"/>&demographicNo=<c:out value="${demographicNo}"/>&issue_code=Reminders&title=Reminders&cmd=divR2I2"
    };
    var limit = 5;
    
    for( idx in issueNoteUrls ) {
        loadDiv(idx,issueNoteUrls[idx],limit);
    }
}

function navBarLoader() {


   $("leftNavBar").style.height = "660px";
   $("rightNavBar").style.height = "660px";
    
    
    this.maxRightNumLines = Math.floor($("rightNavBar").getHeight() / 12);                    
    this.maxLeftNumLines = Math.floor($("leftNavBar").getHeight() / 12);    
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
                  issues:       "<c:out value="${ctx}"/>/oscarEncounter/displayIssues.do?hC=CC9900",
                  OMeds:        "<c:out value="${ctx}"/>/CaseManagementView.do?hc=CCDDAA&method=listNotes&providerNo=<c:out value="${providerNo}"/>&demographicNo=<c:out value="${demographicNo}"/>&issue_code=OMeds&title=Other%20Meds&cmd=OMeds"
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
                                                    
                                                if( $("leftColLoader") != null )
                                                    Element.remove("leftColLoader");                                                      

                                                //track ajax completions and display divs when last ajax call completes                                                
                                                navBarObj.display(navBar,div);
                                           }, 
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>" + div + "<\/h3>Error: " + request.status;
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
                listDisplay(divs[idx].id, threshold);                  
            }        
        };

}

//display in place editor
function showEdit(e,noteId, editors, date, revision, note, url, containerDiv, reloadUrl) {    
    //Event.extend(e);
    //e.stop();
    
    var limit = containerDiv + "threshold";
    var editElem = "showEditNote";
    var pgHeight = pageHeight();
    var right = Math.round((pageWidth() - $(editElem).getWidth())/2);
    var top = Event.pointerY(e);   
    var height = $("showEditNote").getHeight();    
    var gutterMargin = 150;            
    
    if( right < gutterMargin )
        right = gutterMargin;
                    
               
    $("noteEditTxt").value = note;
    
    var editorUl = "<ul style='list-style: none outside none; margin:0px;'>";
    
    if( editors.length > 0 ) {
        var editorArray = editors.split(";");    
        var idx;
        for( idx = 0; idx < editorArray.length; ++idx ) {
            if( idx % 2 == 0 )
                editorUl += "<li>" + editorArray[idx];
            else
                editorUl += "; " + editorArray[idx] + "</li>";
        }

        if( idx % 2 == 0 )
            editorUl += "</li>";
    }    
    editorUl += "</ul>";
    
    var noteInfo = "<div style='float:right;'><i>Date:&nbsp;" + date + "&nbsp;rev<a href='#' onclick='return showHistory(\"" + noteId + "\",event);'>"  + revision + "</a></i></div>" + 
                    "<div><span style='float:left;'>Editors: </span>" + editorUl  + "</div><br style='clear:both;'>";                                                        
                    
    $("issueNoteInfo").update(noteInfo);
    $("frmIssueNotes").action = url;   
    $("reloadUrl").value = reloadUrl;
    $("containerDiv").value = containerDiv;
       
    $(editElem).style.right = right + "px";
    $(editElem).style.top = top + "px";
    if( Prototype.Browser.IE ) {
        //IE6 bug of showing select box    
        $("channel").style.visibility = "hidden";
        $(editElem).style.display = "block";        
    }
    else
        $(editElem).style.display = "table"; 
        
    $("noteEditTxt").focus();
    
    return false;
}

function updateCPPNote() {
   var url = $("frmIssueNotes").action;
   var reloadUrl = $("reloadUrl").value;
   var div = $("containerDiv").value;
   
   $('channel').style.visibility ='visible';
   $('showEditNote').style.display='none';
   var params = $("frmIssueNotes").serialize();   
   var objAjax = new Ajax.Request (                        
                          url,
                            {
                                method: 'post', 
                                evalScripts: true,
                                postBody: params,
                                onSuccess: function(request) {   
                                                if( request.responseText.length > 0 )
                                                    $(div).update(request.responseText);
                                           }, 
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>" + div + "<\/h3>Error: " + request.status;
                                            }
                            }

                      );
    return false;

}

function loadDiv(div,url,limit) {
    
    var objAjax = new Ajax.Request (                        
                            url,
                            {
                                method: 'post', 
                                evalScripts: true,
                                /*onLoading: function() {                            
                                                $(div).update("<p>Loading ...<\/p>");
                                            },*/
                                onSuccess: function(request) {                            
                                                /*while( $(div).firstChild )
                                                    $(div).removeChild($(div).firstChild);
                                                */
                                                
                                                $(div).update(request.responseText);
                                                //listDisplay(div,100);
                                               
                                           }, 
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>" + div + "<\/h3>Error: " + request.status;
                                            }
                            }

                      );
    return false;

}

/*
 *Manage issues attached to notes
 */
 var expandedIssues = new Array();
 function displayIssue(id) {
        //if issue has been changed/deleted remove it from array and return
        if( $(id) == null ) {
            removeIssue(id);
            return false;
        }
        var idx;
        var parent = $(id).parentNode;
        $(id).toggle();
        if( $(id).style.display != "none" ) {            
            parent.style.backgroundColor = "#dde3eb";
            parent.style.border = "1px solid #464f5a";
            
            if( (idx = expandedIssues.indexOf(id)) == -1 )
                expandedIssues.push(id);
        }
        else {
            parent.style.backgroundColor = "";
            parent.style.border = ""; 
            
            removeIssue(id);
        }
        return false;
   }
   
   function removeIssue(id) {
        var idx;
        
        if( (idx = expandedIssues.indexOf(id)) > -1 )
            expandedIssues.splice(idx,1);
   }
</script>
  </head> 
  <body id="body" style="margin:0px;" onload="init()" onunload="onClosing()">
      <div id="main">          
          <div id="header">
              <tiles:insert attribute="header" />
          </div>
          
          <div id="leftNavBar" style="display:inline; float:left; width:20%;">
              <tiles:insert attribute="leftNavigation" />
          </div>  
          
          <div id="content" style="display:inline; float:left; width:60%; background-color:#CCCCFF;">
              <tiles:insert attribute="body" />
          </div>
          
          <div id="rightNavBar" style="display:inline; float:right; width:20%; margin-left:-3px;">
              <tiles:insert attribute="rightNavigation" />
          </div>  
          
          <!-- hovering divs -->
          <div id="showEditNote" class="showEdContent">
              <form id="frmIssueNotes" action="" method="post" onsubmit="return updateCPPNote();">
                  <input type="hidden" id="reloadUrl" name="reloadUrl" value="">
                  <input type="hidden" id="containerDiv" name="containerDiv" value="">
                  <textarea style="margin:10px;" cols="50" rows="15" id="noteEditTxt" name="value" wrap="soft"></textarea><br>
                  <span style="float:right; margin-right:10px;">
                      <input style="padding-right:10px;" type="image" src="<c:out value="${ctx}/oscarEncounter/graphics/note-save.png"/>" title='<bean:message key="oscarEncounter.Index.btnSignSave"/>'>
                      <input type="image" src="<c:out value="${ctx}/oscarEncounter/graphics/system-log-out.png"/>" onclick="$('channel').style.visibility ='visible';$('showEditNote').style.display='none';return false;" title='<bean:message key="global.btnExit"/>'>
                  </span>
                  <div id="issueNoteInfo" style="clear:both; text-align:left;"></div>
              </form>
          </div>
          <div id="printOps" class="printOps">
              <h3 style="margin-bottom:5px; text-align:center;">Print Dialog</h3>
              <form id="frmPrintOps" action="" onsubmit="return false;">
                   <input type="radio" id="printopSelected" name="printop" checked value="selected">Selected<br>
                   <input type="radio" id="printopAll" name="printop" value="all">All<br>
                   <input type="radio" id="printopDates" name="printop" value="dates">Dates<br>                   
                   <div style="float:left; margin-left:5px; width:30px;">From:</div> <img src="<c:out value="${ctx}/images/cal.gif" />" id="printStartDate_cal" alt="calendar">&nbsp;<input type="text" id="printStartDate" name="printStartDate" ondblclick="this.value='';" style="font-style:italic; border: 1px solid #7682b1; width:125px; background-color:#FFFFFF;" readonly value=""><br>
                   <div style="float:left; margin-left:5px; width:30px;">To:</div> <img src="<c:out value="${ctx}/images/cal.gif" />" id="printEndDate_cal" alt="calendar">&nbsp;<input type="text" id="printEndDate" name="printEndDate" ondblclick="this.value='';" style="font-style:italic; border: 1px solid #7682b1; width:125px; background-color:#FFFFFF;" readonly value=""><br>                   
                   <div style="margin-top:5px; text-align:center">
                       <input type="submit" id="printOp" style="border: 1px solid #7682b1;" value="Print" onclick="return printNotes();">
                       <input type="submit" id="cancelprintOp" style="border: 1px solid #7682b1;" value="Cancel" onclick="$('printOps').style.display='none';">
                   </div>
              </form>              
          </div>
          
      </div>
  </body>
</html:html>
