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
  <link rel="stylesheet" type="text/css" media="all" href="<c:out value="${ctx}"/>/share/calendar/calendar.css" title="win2k-cold-1">

  <!-- main calendar program -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar.js"></script>

  <!-- language for the calendar -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

  <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar-setup.js"></script>
  
  <!-- js implementation of markdown -->
  <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/showdown.js"/>"></script>
    
    <style type="text/css">
        
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
            margin: 5px 0px 5px 5px;
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
            font-size:0.8em;
        }                
        
        .txtArea {
            font-family:arial,sans-serif; 
            font-size:1.0em;             
            width:99%; 
            rows:10; 
            overflow:hidden; 
            border-style:none; 
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
        /* formatting for navbar */
        .links {
            color: blue;
            text-decoration: underline;
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
            width: 98%;
            overflow:auto;
        }
        
        .rowTwo {
            height: <nested:write name="rowTwoSize"/>px;
            width:98%;
            overflow:auto;
        }
        
    </style>

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
        
   function listDisplay(Id) {           
            
        var eqIdx = Id.indexOf("=");    
        var numId = Id.substr(eqIdx+1) + "num";

        if( $(numId).value > 5 ) {
            var btnName = Id.substr(eqIdx+1) + "topimg";
            var topImage = $(btnName);
            btnName = Id.substr(eqIdx+1) + "midimg";
            var midImage = $(btnName);
            var expand;
            var expandPath = "graphics/expand.gif";
            var collapsePath = "../oscarMessenger/img/collapse.gif";
            var listId = Id.substr(eqIdx+1) + "list";
            var list = $(listId);
            var items = list.getElementsByTagName('li');
            items = $A(items);
            for( var idx = 5; idx < items.length; ++idx ) {
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
                topImage.style.display = 'none';
                midImage.style.display = 'inline';
            }
            else {
                topImage.style.display = 'inline';
                midImage.style.display = 'none';
            }

        }

    
    }  
    function popColumn(url,div,params) {    
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
                                                
                                            listDisplay(params);
                                       }, 
                            onFailure: function(request) {
                                            $(div).innerHTML = "<h3>Error:<\/h3>" + request.status;
                                        }
                        }
                           
                  );
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
    
    </script>
  </head> 
  <body style="margin:0px;" onunload="onClosing()">
                        <div id="header" style="display:block;">
                            <tiles:insert attribute="header" />
                        </div>
                        
                        <div id="leftNavbar" style="display:inline; float:left; width:20%;">
                            <tiles:insert attribute="navigation" />
                        </div>  
                        
                        <div id="content" style="display:inline; float:left; width:80%; background-color:#CCCCFF;">
                            <tiles:insert attribute="body" />
                        </div>
  </body>
</html:html>
