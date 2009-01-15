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

<%-- Updated by Eugene Petruhin on 16 dec 2008 while fixing #2434234 --%>

<%@ include file="/casemgmt/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>

<%@page import="java.util.Arrays, java.util.Properties, java.util.List, java.util.Set, java.util.ArrayList, java.util.Iterator, java.text.SimpleDateFormat, java.util.Date, java.text.ParseException"%>
<%@page import="org.oscarehr.casemgmt.model.*" %>
<%@page import="org.oscarehr.casemgmt.web.formbeans.*" %>
<%@page import="org.oscarehr.PMmodule.model.*" %>
<%@page import="oscar.util.DateUtils" %>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.model.Role"%>

<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<%
    oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
    if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
        response.sendRedirect("error.jsp");
        return;
    }
    
    String provNo = bean.providerNo;
    //Properties windowSizes = oscar.oscarEncounter.pageUtil.EctWindowSizes.getWindowSizes(provNo); 
    
    String pId=(String)session.getAttribute("case_program_id");
    if (pId==null) pId="";
    
    String dateFormat = "dd-MMM-yyyy H:mm";    
    long savedId = 0;
    boolean found = false;
    ArrayList lockedNotes = new ArrayList();
    ArrayList unLockedNotes = new ArrayList();
    ArrayList unEditableNotes = new ArrayList();
    
    java.util.List noteList=(java.util.List)request.getAttribute("Notes");
    int noteSize = noteList != null ? noteList.size() : 0;
%>
<script type="text/javascript">   
    var X       = 10;    
    var small   = 60;
    var normal  = 166;
    var medium  = 272;
    var large   = 378;
    var full    = 649;
    
    function reset() {
        rowOneSmall();
        rowTwoSmall();
    }
    
    function rowOneX(){
        $("cpp.socialHistory").style.overflow="auto";
        $("cpp.familyHistory").style.overflow="auto";
        $("cpp.medicalHistory").style.overflow="auto";
        $("cpp.socialHistory").style.height=X;
        $("cpp.familyHistory").style.height=X;
        $("cpp.medicalHistory").style.height=X;
        $("rowOneSize").value=X;
    }
    
    function rowOneSmall(){
        $("cpp.socialHistory").style.overflow="auto";
        $("cpp.familyHistory").style.overflow="auto";
        $("cpp.medicalHistory").style.overflow="auto";
        $("cpp.socialHistory").style.height=small;
        $("cpp.familyHistory").style.height=small;
        $("cpp.medicalHistory").style.height=small;
        $("rowOneSize").value=small;
    }
    
    function rowOneNormal(){
        $("cpp.socialHistory").style.overflow="auto";
        $("cpp.familyHistory").style.overflow="auto";
        $("cpp.medicalHistory").style.overflow="auto";
        $("cpp.socialHistory").style.height=normal;
        $("cpp.familyHistory").style.height=normal;
        $("cpp.medicalHistory").style.height=normal;
        $("rowOneSize").value=normal;
    }
    
    function rowOneLarge(){
        $("cpp.socialHistory").style.overflow="auto";
        $("cpp.familyHistory").style.overflow="auto";
        $("cpp.medicalHistory").style.overflow="auto";
        $("cpp.socialHistory").style.height=large;
        $("cpp.familyHistory").style.height=large;
        $("cpp.medicalHistory").style.height=large;        
        $("rowOneSize").value=large;
    }
    function rowOneFull(){
        $("cpp.socialHistory").style.overflow="auto";
        $("cpp.familyHistory").style.overflow="auto";
        $("cpp.medicalHistory").style.overflow="auto";
        $("cpp.socialHistory").style.height=full;
        $("cpp.familyHistory").style.height=full;
        $("cpp.medicalHistory").style.height=full;        
        $("rowOneSize").value=full;
    }
    function rowTwoX(){
        $("cpp.ongoingConcerns").style.overflow="auto";
        $("cpp.reminders").style.overflow="auto";
        $("cpp.ongoingConcerns").style.height=X;
        $("cpp.reminders").style.height=X;
        $("rowTwoSize").value=X;
    }
    function rowTwoSmall(){
        $("cpp.ongoingConcerns").style.overflow="auto";
        $("cpp.reminders").style.overflow="auto";
        $("cpp.ongoingConcerns").style.height=small;
        $("cpp.reminders").style.height=small;
        $("rowTwoSize").value=small;
    }
    function rowTwoNormal(){
        $("cpp.ongoingConcerns").style.overflow="auto";
        $("cpp.reminders").style.overflow="auto";
        $("cpp.ongoingConcerns").style.height=normal;
        $("cpp.reminders").style.height=normal;
        $("rowTwoSize").value=normal;
    }
    function rowTwoLarge(){
        $("cpp.ongoingConcerns").style.overflow="auto";
        $("cpp.reminders").style.overflow="auto";
        $("cpp.ongoingConcerns").style.height=large;
        $("cpp.reminders").style.height=large;
        $("rowTwoSize").value=large;
    }
    function rowTwoFull(){
        $("cpp.ongoingConcerns").style.overflow="auto";
        $("cpp.reminders").style.overflow="auto";
        $("cpp.ongoingConcerns").style.height=full;
        $("cpp.reminders").style.height=full;
        $("rowTwoSize").value=full;
    }

    function getActiveText(e) {      
         if(document.all) {
            
            text = document.selection.createRange().text;
            if(text != "" && $F("keyword") == "") {
              $("keyword").value += text;
            }
            if(text != "" && $F("keyword") != "") {
              $("keyword").value = text;
            }
          } else {
            text = window.getSelection();

            if (text.toString().length == 0){  //for firefox
               var txtarea = $(caseNote);
               var selLength = txtarea.textLength;
               var selStart = txtarea.selectionStart;
               var selEnd = txtarea.selectionEnd;
               if (selEnd==1 || selEnd==2) selEnd=selLength;
               text = (txtarea.value).substring(selStart, selEnd);
            }
            //
            $("keyword").value = text;
          }
          
          return true;
    }
    
    function setCaretPosition(inpu, pos){

	if(inpu.setSelectionRange){
		inpu.focus();
		inpu.setSelectionRange(pos,pos);                
         
                var ev;
                try {
                    ev = document.createEvent('KeyEvents');
                    ev.initKeyEvent('keypress', true, true, window,false, false, false, false, 0,0);                   
                }
                catch(e) {                
                    ev = document.createEvent("UIEvents");                    

                    /*
                    Safari doesn't support these funcs but seems to scroll without them
                    ev.initUIEvent( 'keypress', true, true, window, 1 );                    
                    ev.keyCode = inpu.value.charCodeAt(pos-1);                    
                    */
                    
                }
                
                inpu.dispatchEvent(ev); // causes the scrolling                                      
                
	}else if (inpu.createTextRange) {                
		var range = inpu.createTextRange();
		range.collapse(true);
		range.moveEnd('character', pos);
		range.moveStart('character', pos);
		range.select();   
                
                var ev = document.createEventObject();
                ev.keyCode = inpu.value.charCodeAt(pos-1);
                inpu.fireEvent("onkeydown", ev);
	}                     
        
    }
    
    function pasteToEncounterNote(txt) {        
        $(caseNote).value += "\n" + txt;
        adjustCaseNote();
        setCaretPosition($(caseNote),$(caseNote).value.length);    
    }

    function writeToEncounterNote(request) {
        
        //$("templatejs").update(request.responseText);
        var text = request.responseText;        
        text = text.replace(/\\u000A/g, "\u000A");
        text = text.replace(/\\u000D/g, "");
        text = text.replace(/\\u003E/g, "\u003E");
        text = text.replace(/\\u003C/g, "\u003C");
        text = text.replace(/\\u005C/g, "\u005C");
        text = text.replace(/\\u0022/g, "\u0022");
        text = text.replace(/\\u0027/g, "\u0027");

        if( $(caseNote).value.length > 0 )
            $(caseNote).value += "\n\n";
            
        var curPos = $(caseNote).value.length;
        //subtract \r chars from total length for IE
        if( document.all ) {                                 
            var newLines = $(caseNote).value.match(/.*\n.*/g);  
            if( newLines != null ) {
                curPos -= newLines.length;
            }
        }
        ++curPos;            
        
        //if insert text begins with a new line char jump to second new line        
        var newlinePos;
        if( (newlinePos = text.indexOf('\n')) == 0 ) {
            ++newlinePos;            
            var subtxt = text.substr(newlinePos);
            curPos += subtxt.indexOf('\n');            
        }
                
        $(caseNote).value += text;                                
        
        //setTimeout("$(caseNote).scrollTop="+scrollHeight, 0);  // setTimeout is needed to allow browser to realize that text field has been updated 
        $(caseNote).focus();
        adjustCaseNote();
        setCaretPosition($(caseNote),curPos);
    }
    
     function ajaxInsertTemplate(varpage) { //fetch template
        <c:url value="/oscarEncounter/InsertTemplate.do" var="templateURL" />
        
        if(varpage!= 'null'){                  
          var page = "<c:out value="${templateURL}"/>";          
          var params = "templateName=" + varpage + "&version=2";
          new Ajax.Request( page, {
                                    method: 'post',
                                    postBody: params,
                                    evalScripts: true, 
                                    onSuccess:writeToEncounterNote,
                                    onFailure: function() {
                                            alert("Inserting template " + varpage + " failed");
                                        }
                                  }
                            );                    
        }
          
    }
    
    function menuAction(){
        var name = document.getElementById('enTemplate').value;
        var func = autoCompleted[name];
        eval(func);     
    }   
    
function grabEnterGetTemplate(event){

  
  if(window.event && window.event.keyCode == 13){          
      return false;
  }else if (event && event.which == 13){     
      return false;
  }
}

function largeNote(note) {
    var THRESHOLD = 10;
    var isLarge = false;       
    var pos = -1;
        
    for( var count = 0; (pos = note.indexOf("\n",pos+1)) != -1; ++count ) {
        if( count == THRESHOLD ) {
            isLarge = true;
            break;
        }
    }        
        return isLarge;
}

//Return display of Locked Note to normal 
function resetView(frm, error, e) {
    var parent = Event.element(e).parentNode.id;
    var nId = parent.substr(1);
    var img = "<img id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>";
        
    
    Element.remove(Event.element(e).id);
    Event.stop(e);
    
    if( error )
        Element.remove("passwdError");
        
    if( frm )
        Element.remove("passwdPara");        
        
    //new Insertion.Top(parent, img);
    Element.observe(parent, 'click', unlockNote);
}

function changeToView(id) {
    var parent = $(id).parentNode.id;
    var nId = parent.substr(1);
        
    var tmp = $(id).value;
    var saving = false;
    var sumaryId = "sumary";
    var sumary;
    
    var sig = 'sig' + nId;
        
    //check if case note has been changed
    //if so, warn user that changes will be lost if not saved    
    if( origCaseNote != $F(id)  || origObservationDate != $("observationDate").value) {
        if( !confirm("Your changes to the current note have not been saved. Select Ok to save and continue or Cancel to continue editing current note"))
            return false;
        else {
            saving = true;
            ajaxSaveNote(sig);
        }
   }
   
   
    //cancel updating of issues
    //IE destroys innerHTML of sig div when calling ajax update
    //so we have to restore it here if the ajax call is aborted
    /*if( ajaxRequest != undefined  && callInProgress(ajaxRequest.transport) ) {
        ajaxRequest.transport.abort();
        var siblings = $(id).siblings();
        var pos;
        
        for( var idx = 0; idx < siblings.length; ++idx ) {
            if( (pos = siblings[idx].id.indexOf("sig")) != -1 ) {
                nId = siblings[idx].id.substr(pos+3);
                sumaryId += nId;
                if( $(sumaryId) == null ) {                    
                    siblings[idx].innerHTML = sigCache;                
                }
                break;
            }
        }
    } */                                                                                           
    
    //clear auto save
    clearTimeout(autoSaveTimer);
    deleteAutoSave();
    
    //ajaxSave changes id to represent saved note id so we must update our vars here
    parent = $(id).parentNode.id;
    nId = parent.substr(1);
    sig = 'sig' + nId;       
    
    Element.remove("notePasswd");
    Element.stopObserving(id, 'keyup', monitorCaseNote);
    Element.stopObserving(id, 'click', getActiveText);
    nId = parent.substr(1);
    Element.remove(id);    
    
    //remove observation date input text box but preserve date if there is one
    if( !saving && $("observationDate") != null ) {
        var observationDate = $("observationDate").value;
       
        Element.remove("observationDate");
        Element.remove("observationDate_cal");
                    
        var observationId = "observation" + nId;
        
        var html = $(observationId).innerHTML;
        
        html = html.substr(0,html.indexOf(":")+1) + " <span id='obs" + nId + "'>" + observationDate + "<\/span>" + html.substr(html.indexOf(":")+1);
        
        $(observationId).update(html);
                
    }
    
    if( $("autosaveTime") != null )
        Element.remove("autosaveTime");
    
    if( $("noteIssues") != null )
        Element.remove("noteIssues"); 
        
    if( $("encType") != null ) {
        var encTypeId = "encType" + nId;
        var content = $F("encType");
        var encType;
        if( content.length > 0 )
            encType = "&quot;" + content + "&quot;";
        else
            encType = "";
        Element.remove("encType");
        $(encTypeId).update(encType);        
    }    
    //we can stop listening for add issue here
    Element.stopObserving('asgnIssues', 'click', addIssueFunc);
    if( tmp.length == 0 ) 
        tmp = "&nbsp;";
        
    if( largeNote(tmp) ) {
        var btmImg = "<img title='Minimize Display' id='bottomQuitImg" + nId + "' alt='Minimize Display' onclick='minView(event)' style='float:right; margin-right:5px; margin-bottom:3px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>";
        new Insertion.Top(parent, btmImg); 
    }
    var input = "<pre>" + tmp + "<\/pre>";
    new Insertion.Top(parent, input);
    //$(txt).style.fontSize = normalFont;
    
    //if we're not restoring a new note display print img
    if( nId.substr(0,1) != "0" ) {
        img = "<img title='Print' id='print" + nId + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png'/>";
        new Insertion.Top(parent, img);
    }
    
    var img = "<img title='Minimize' id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>";
    new Insertion.Top(parent, img);    
    
    $(parent).style.height = "auto";        
    Element.observe(parent, 'click', editNote);    
    return true;
}

function minView(e) {          
    var divHeight = "1.1em";
    var txt = Event.element(e).parentNode.id;   
    var nId = txt.substr(1);
    var img = Event.element(e).id;    
    var dateId = "obs" + nId;
    var content = "c" + nId;
    var date = "d" + nId;    
    
    Event.stop(e);
    var imgs = $(txt).getElementsBySelector("img");
    for( i = 0; i < imgs.length; ++i ) {    
        if( imgs[i].id.indexOf("quitImg") > -1 ) {
            Element.remove(imgs[i]);
            break;
        }
    }
    
    $(txt).style.overflow = "hidden";
    shrink(txt, 14);
    //$(txt).style.height = divHeight;    
    
    var nodes = $(txt).getElementsBySelector('pre')
    if( nodes.length > 0 ) {
        var line = nodes[0].innerHTML;
        var dateValue = $(dateId) != null ? $(dateId).innerHTML : "";
        line = "<div id='" + date + "' style='float:left; font-size:1.0em; width:25%;'><b>" + dateValue + "<\/b><\/div><div id='" + content + "' style='float:left; font-size:1.0em; width:65%;'>" + line + "<\/div>";
        new Insertion.Top(txt,line);        
    }
    
    //img = "<img title='Print' id='print" + nId + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png'/>";
    //new Insertion.Top(txt, img);

    img = "<img title='Maximize Display' alt='Maximize Display' id='xpImg" + nId + "' onclick='xpandView(event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_down.gif'/>";
    new Insertion.Top(txt, img);
}

var idHeight;
var curElemHeight;
var shrinkTimer;
function shrink(id, toScale) {
    idHeight = $(id).getHeight();
    curElemHeight = idHeight;
    var delta = Math.ceil(curElemHeight/5);
    shrinkTimer = self.setInterval("shrinkImpl("+id+", " + toScale+", "+delta+")",1);
}
function shrinkImpl(id, minHeight, delta) {
    curElemHeight -= delta;
    if( curElemHeight <= minHeight ) {
        $(id).style.height = minHeight;
        window.clearInterval(shrinkTimer);
        return;
    }    
    
    $(id).style.height = curElemHeight;
}
//restore note to full display within pre tags
//this func fires only if maximize button is clicked
function xpandView(e) {
    var txt = Event.element(e).parentNode.id;   
    var img = Event.element(e).id; 
    var nId = txt.substr(1);
    var content = "c" + nId;
    var date = "d" + nId;    
    
    var imgTag = "<img id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>";
    
    
    Element.remove(img);
    Element.remove(date);
    Element.remove(content);
    
    $(txt).style.height = 'auto';    
    new Insertion.Top(txt, imgTag);    
    Event.stop(e);

}

function resetEdit(e) {
    var txt = Event.element(e).id;
    var nId = txt.substr(1);

    var img = "<img id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>";
    var divHeight = 14;    
    var divSize = "size";       
    
    var payload;
    
    //if exit button fires func, we need to get id of textarea, which is grandfather of button
    if( txt == "" ) txt = Event.element(e).parentNode.parentNode.id;
    
    payload = $(caseNote).value;
    Element.remove("notePasswd");
    Element.remove(caseNote);
    
    payload = payload.replace(/^\s+|\s+$/g,"");
    var input = "<pre>" + payload + "\n<\/pre>";    
    new Insertion.Top(txt, input);
    new Insertion.Top(txt, img);
    
    //$(txt).style.height = divHeight;
    Element.observe(txt, 'click', editNote);

}

//send password to server for auth to display locked Note
function unlock_ajax(id) {
    <c:url value="/CaseManagementView.do" var="lockedURL" />
    var url = "<c:out value="${lockedURL}" />";
    var noteId = id.substr(1);  
    var params = "method=do_unlock_ajax&noteId=" + noteId + "&password=" + $F("passwd");

    var objAjax = new Ajax.Request (
                    url,
                    {
                        method: 'post',
                        postBody: params,
                        evalScripts: true,
                        onSuccess: function(request) {                                    
                                    var html = request.responseText;
                                    //if( navigator.userAgent.indexOf("AppleWebKit") > -1 )
                                    //    $(id).updateSafari(html);
                                    //else
                                        $(id).update(html);
                                                                            
                                    },
                        onFailure: function(request) {
                                        if( request.status == 403 )
                                            alert("<bean:message key="oscarEncounter.error.msgExpired"/>");
                                        else
                                            alert("Error " + request.status + "\nAn error occurred while unlocking note");
                                    }
                   }
            );
    return false;
}

//display unlock note password text field and submit button
function unlockNote(e) {      
   var txt;
   var el;
   
    el = Event.element(e);
    
    //get id for parent div
    if( el.id.search(/^n/) > -1 )
        txt = el.id;
    else {
        var level = 0;        
        while( $(el).up('div',level).id.search(/^n/) == -1 )
            ++level;
        
        txt = $(el).up('div',level).id;        
    } 
      
    var passwd = "passwd"; 
    var nId = txt.substr(1);
    var img = "<img id='quitImg" + nId + "' onclick='resetView(true, false, event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>";
    new Insertion.Top(txt,img);
    var lockForm = "<p id='passwdPara' class='passwd'>Password:&nbsp;<input onkeypress=\"return grabEnter('btnUnlock', event);\" type='password' id='" + passwd + "' size='16'>&nbsp;<input id='btnUnlock' type='button' onclick=\"return unlock_ajax('" + txt + "');\" value='<bean:message key="oscarEncounter.Index.btnUnLock"/>'><\/p>";
    new Insertion.Bottom(txt, lockForm);
    
    $(txt).style.height = "auto";
    $(passwd).focus();
    Element.stopObserving(txt, 'click', unlockNote);
}

var sigCache = "";
//place Note text in textarea for editing and add save, sign etc buttons for this note
function editNote(e) {        
    var divHeight = 14;
    var normalFont = 12;
    var lineHeight = 1.2;
    var noteHeight;
    var largeFont = 16;        
    var quit = "quitImg";
    var txt; 
    var payload;
    var el;
   
    el = Event.element(e);
    
    //get id for parent div
    if( el.id.search(/^n/) > -1 )
        txt = el.id;
    else {
        var level = 0;        
        while( $(el).up('div',level).id.search(/^n/) == -1 )
            ++level;
        
        txt = $(el).up('div',level).id;        
    }                              
    
    //if we have an edit textarea already open, close it
    if($(caseNote) !=null && $(caseNote).parentNode.id != $(txt).id) {
        if( !changeToView(caseNote) ) {
            $(caseNote).focus();
            return;
        }
    }
            
    //get rid of minimize and print buttons
    var nodes = $(txt).getElementsBySelector('img');    
    for(var i = 0; i < nodes.length; ++i ) {
        nodes[i].remove();        
    }              
    
    var nId = txt.substr(1); 
    var date = 'd' + nId;
    var content = 'c' + nId;
    
    //check for line item displayed when note is minimized
    if( $(date) != null ) {
        Element.remove(date);
        Element.remove(content);
    }
    
    //place text in textarea for editing
    nodes = $(txt).getElementsBySelector('pre');  
    if( nodes.length > 0 ) {        
        payload = nodes[0].innerHTML;    
        nodes[0].remove();
    }    
    
    payload = payload.replace(/^\s+|\s+$/g,"");
    payload += "\n";
                           
    caseNote = "caseNote_note" + nId;                
    
    var input = "<textarea tabindex='7' cols='84' rows='10' wrap='hard' class='txtArea' style='line-height:1.1em;' name='caseNote_note' id='" + caseNote + "'>" + payload + "<\/textarea>";                    
    new Insertion.Top(txt, input);                 
    
    Element.observe(caseNote, 'keyup', monitorCaseNote);
    Element.observe(caseNote, 'click', getActiveText);
      
    <c:if test="${sessionScope.passwordEnabled=='true'}">
           input = "<p style='background-color:#CCCCFF; display:none; margin:0px;' id='notePasswd'>Password:&nbsp;<input type='password' name='caseNote.password'/><\/p>";
           new Insertion.Bottom(txt, input);
    </c:if>
                            
    //we check if we are dealing with a new note or not
    if( nId.charAt(0) == "0" ) {
        document.forms["caseManagementEntryForm"].noteId.value = "0";
        document.forms["caseManagementEntryForm"].newNoteIdx.value = nId;
        document.forms["caseManagementEntryForm"].note_edit.value = "new";
    }
    else {
        document.forms["caseManagementEntryForm"].noteId.value = nId;
        document.forms["caseManagementEntryForm"].note_edit.value = "existing";
    }
        
    
    //we want to make sure update issue ajax call doesn't retrieve anything from autosave table
    document.forms["caseManagementEntryForm"].forceNote.value = "true";
    
    var divId = "sig" + nId;    
    //cache existing signature so we can recreate it if ajax call aborted
    sigCache = $(divId).innerHTML;    
    ajaxUpdateIssues('edit', divId); 
    addIssueFunc = updateIssues.bindAsEventListener(obj, makeIssue, divId);
    Element.observe('asgnIssues', 'click', addIssueFunc);
    
    $(txt).style.height = "auto";          
    
    //we don't want to capture clicks as user inputs text
    Element.stopObserving(txt, 'click', editNote);
    
    //AutoCompleter for Issues
    <c:url value="/CaseManagementEntry.do?method=issueList&demographicNo=${demographicNo}&providerNo=${param.providerNo}" var="issueURL" />
    issueAutoCompleter = new Ajax.Autocompleter("issueAutocomplete", "issueAutocompleteList", "<c:out value="${issueURL}"/>", {minChars: 4, indicator: 'busy', afterUpdateElement: saveIssueId, onShow: autoCompleteShowMenu, onHide: autoCompleteHideMenu});               
    
    //position cursor at end of text            
    adjustCaseNote();
    setCaretPosition($(caseNote),$(caseNote).value.length); 
    $(caseNote).focus();
    origCaseNote = $F(caseNote); 
    
    //if note is already signed, remove save button to force edits to be signed
    var sign = "signed" + nId;
    if( $F(sign) == "true" )
        $("saveImg").style.visibility = "hidden";
    else
        $("saveImg").style.visibility = "visible";
        
    //start AutoSave
    setTimer();
}

function collapseView(e) {
    var html;
    var divHeight = 14;
    var txt = Event.element(e).parentNode.id;
    var img = Event.element(e).id;
    
    Element.remove(img);
    
    $(txt).style.height = divHeight;
    html = $(txt).innerHTML;
    html = html.replace(/<span>|<\/span>|<pre>|<\/pre>/ig,"");
    $(txt).innerHTML = html;
    $(txt).style.cursor = "pointer";
    
    Event.observe(txt, 'click', viewNote);
}

function viewNote(e) {    
    var txt = Event.element(e).id; 
    var html;            
    var img = "<img id='quitImg" + txt.substr(1) + "' onclick='collapseView(event)' style='float:right; cursor:pointer;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>";    
        
    $(txt).style.height = "auto";
    html = $(txt).innerHTML;
    $(txt).innerHTML = "<pre>" + html + "<\/pre>";        
    $(txt).style.cursor = "text";
        
    new Insertion.Top(txt,img);
    Event.stopObserving(txt, 'click', viewNote);           
}
var showIssue = false;
var expandedIssues = new Array();
function showIssues(e) {    
        
    Event.stop(e);
    Element.toggle('noteIssues');
    showIssue = !showIssue;
    
    if( showIssue )
        $("issueAutocomplete").focus();
    else
        $(caseNote).focus();
        
    return false;

}

function issueIsAssigned() {
    var prefix = "noteIssue";
    var idx = 0;
    var id = prefix + idx;
    
    while( $(id) != undefined ) {
        if( $(id).checked )
            return true;
            
        ++idx;
        id = prefix + idx;
    }
    
    return false;
}

function filter() {
    document.forms["caseManagementEntryForm"].method.value = "edit";
    document.forms["caseManagementEntryForm"].note_edit.value = "new";
    document.forms["caseManagementEntryForm"].noteId.value = "0";
    document.forms["caseManagementEntryForm"].ajax.value = false;
    document.forms["caseManagementEntryForm"].chain.value = "null";
    
    <c:url value="/CaseManagementEntry.do" var="entryURL" />
    document.forms["caseManagementViewForm"].method.value = "view";
     
    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];
    var caseMgtViewfrm = document.forms["caseManagementViewForm"];
    var url = "<c:out value="${entryURL}" />";
    var objAjax = new Ajax.Request (
                    url,
                    {
                        method: 'post',                        
                        postBody: Form.serialize(caseMgtEntryfrm),
                        onSuccess: function(request) {                              
                            caseMgtViewfrm.submit(); 
                        },
                        onFailure: function(request) {
                            alert("Filtering Failed "+ request.status);
                        }
                     }
                   );          
    
    return false;
}

//make sure observation date is in the past
function validDate() {
    var strDate = $("observationDate").value;
    var day = strDate.substring(0,strDate.indexOf("-"));
    var mnth = strDate.substring(strDate.indexOf("-")+1, strDate.lastIndexOf("-"));
    var year = strDate.substring(strDate.lastIndexOf("-")+1, strDate.indexOf(" "));
    var time = strDate.substr(strDate.indexOf(" ")+1);
    var date = new Date( mnth + " " + day + ", " + year + " " + time);
    var now = new Date();
    
    if( date <= now )
        return true;
    else
        return false;
}

function ajaxSaveNote(div) {
    if( $("observationDate") != undefined && $("observationDate").value.length > 0 && !validDate() ) {
        alert("Observation date must be in the past");
        return false;
    }

<caisi:isModuleLoad moduleName="caisi">
    if( !issueIsAssigned() ) {
        alert("At least one(1) issue must be assigned to note");
        return false;
    }
</caisi:isModuleLoad>

    document.forms["caseManagementEntryForm"].method.value = 'ajaxsave';
    
    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];
    <c:url value="/CaseManagementEntry.do" var="saveAjaxURL" />
    var url = "<c:out value="${saveAjaxURL}" />";

    var objAjax = new Ajax.Updater (
                    {success:div},
                    url,
                    {
                        method: 'post',                        
                        evalScripts: true,                         
                        parameters: Form.serialize(caseMgtEntryfrm),
                        asynchronous: false,
                        onFailure: function(request) {
                            if( request.status == 403 )
                                alert("<bean:message key="oscarEncounter.error.msgExpired"/>");
                            else
                                alert("Error saving note " + request.status);
                        }                        
                     }
                   );          
    return false;
}

function savePage(method) {
    
    if( $("observationDate") != undefined && $("observationDate").value.length > 0 && !validDate() ) {
        alert("Observation date must be in the past");
        return false;
    }
    
    /*if( $F(caseNote).replace(/^\s+|\s+$/g,"").length == 0 ) {
        alert("Please enter a note before saving");
        return false;
    }*/
    
<caisi:isModuleLoad moduleName="caisi">
    if( !issueIsAssigned() ) {
        alert("At least one(1) issue must be assigned to note");
        return false;
    }
</caisi:isModuleLoad>
    document.forms["caseManagementEntryForm"].method.value = method;
    document.forms["caseManagementEntryForm"].ajax.value = false;
    document.forms["caseManagementEntryForm"].chain.value = "list";
    document.forms["caseManagementEntryForm"].includeIssue.value = "off";
    
    <c:url value="/CaseManagementView.do" var="entryURL" />
    document.forms["caseManagementViewForm"].method.value = method;
     
    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];
    var frm = document.forms["caseManagementViewForm"];
    var url = "<c:out value="${entryURL}" />";
    var objAjax = new Ajax.Request (
                    url,
                    {
                        method: 'post',                        
                        postBody: Form.serialize(frm),
                        onSuccess: function(request) {  
                            tmpSaveNeeded = false;
                            caseMgtEntryfrm.submit(); 
                        },
                        onFailure: function(request) {
                            if( request.status == 403 )
                                alert("<bean:message key="oscarEncounter.error.msgExpired"/>");
                            else
                                alert("Error saving form " + request.status);
                        }
                     }
                   );          
    return false;
}
    
    
    function changeDiagnosis(issueId) {
        var methodArg = "ajaxChangeDiagnosis"; 
        var divIdArg = $("noteIssues").up().id;
        var thisObj = {};
        changeIssueFunc = updateIssues.bindAsEventListener(thisObj, methodArg, divIdArg);
        
        document.forms['caseManagementEntryForm'].change_diagnosis_id.value=issueId;
        $("asgnIssues").value="Change";       
                        
        Element.stopObserving('asgnIssues', 'click', addIssueFunc);
        Element.observe('asgnIssues', 'click', changeIssueFunc);
        $("issueAutocomplete").focus();        
        return false;
    }
    
    function toggleNotePasswd() {
        <c:if test="${sessionScope.passwordEnabled=='true'}">
            Element.toggle('notePasswd');
            if( $('notePasswd').style.display != "none" )
                document.forms['caseManagementEntryForm'].elements['caseNote.password'].focus();
            else
                document.forms['caseManagementEntryForm'].elements[caseNote].focus();
        </c:if>
        return false;    
    }
    
    function closeEnc(e) {
        Event.stop(e);
        if( origCaseNote != $F(caseNote)  || origObservationDate != $("observationDate").value) {
            if( confirm("Are you sure you wish to close the encounter? Any unsaved data WILL BE LOST") ) {
                var frm = document.forms["caseManagementEntryForm"];
                tmpSaveNeeded = false;
                frm.method.value = "cancel";
                frm.submit();            
            }           
        }
        else
            window.close();
        
        return false;
    }
    

function saveIssueId(txtField, listItem) {       
    $("newIssueId").value = listItem.id;
    $("newIssueName").value = listItem.innerHTML;
    
    submitIssues = true;
}

function updateIssues(e) {
    var args = $A(arguments);
    args.shift();    
   
    if( $("newIssueId").value.length == 0 || $("issueAutocomplete").value != $("newIssueName").value )         
        alert("Please select a full issue name from the auto completion menu");
    else
        ajaxUpdateIssues(args[0], args[1]);
    
    if( $F("asgnIssues") != "Assign Issues" ) {
        $("asgnIssues").value="Assign Issues";
        Element.stopObserving('asgnIssues', 'click', changeIssueFunc);
        Element.observe('asgnIssues', 'click', addIssueFunc);
    }
    Event.stop(e);
    submitIssues = false;
    return false;
}
var ajaxRequest; 
function ajaxUpdateIssues(method, div) {
    <c:url value="/CaseManagementEntry.do" var="issueURL" />

    var frm = document.forms["caseManagementEntryForm"];
    frm.method.value = method;    
    frm.ajax.value = true;
    
    var url = "<c:out value="${issueURL}"/>";
    ajaxRequest = new Ajax.Updater( {success:div}, url, { 
                                        evalScripts: true, parameters: Form.serialize(frm), onSuccess: onIssueUpdate,
                                        onFailure: function(response) { 
                                                        alert( "Error " + response.status + "\nMost likely your session has expired.  Login again.\n" +
                                                                "If problem persists contact support");
                                                    }
                                    } );           
    
    return false;
}

function onIssueUpdate() {        

    //this request succeeded so we reset issues
    $("issueAutocomplete").value = "";
    $("newIssueId").value = "";

}

function submitIssue(event) {
    var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
    if (keyCode == 13 ) {
        if( submitIssues) 
            $("asgnIssues").click();
        
        return false;
    }
}



var filterShows = false;
function showFilter() {

    if( filterShows )
        new Effect.BlindUp('filter');
    else
        new Effect.BlindDown('filter');
        
    filterShows = !filterShows;
}

function filterCheckBox(checkbox) {
    var checks = document.getElementsByName(checkbox.name);
    
    if( checkbox.value == "a" && checkbox.checked ) {        
    
        for( var idx = 0; idx < checks.length; ++idx ) {
            if( checks[idx] != checkbox )
                checks[idx].checked = false;            
        }
    }
    else {
        for( var idx = 0; idx < checks.length; ++idx ) {
            if( checks[idx].value == "a" ) {
                if( checks[idx].checked )
                    checks[idx].checked = false;
                    
                break;
            }
        }
    }
     
}

//we insert a new note div with textarea etc
//newNoteIdx guarantees unique id for successive calls to newNote
var newNoteCounter = 0;
function newNote(e) {
    Event.stop(e);
    var id = "new" + newNoteCounter;
    var reason = "<%=insertReason(request)%>";    //function defined bottom of file
    ++newNoteCounter;
    var newNoteIdx = "0" + newNoteCounter;
    var sigId = "sig"+ newNoteIdx;
    var input = "<textarea tabindex='7' cols='84' rows='1' wrap='hard' class='txtArea' style='line-height:1.0em;' name='caseNote_note' id='caseNote_note" + newNoteIdx + "'>" + reason + "<\/textarea>";
    var passwd  = 
                <c:if test="${sessionScope.passwordEnabled=='true'}">
                        "<p style='background-color:#CCCCFF; display:none; margin:0px;' id='notePasswd'>Password:&nbsp;<input type='password' name='caseNote.password'/><\/p>" +		
                </c:if>
                "";
                
    var div = "<div id='" + id + "' class='newNote'><input type='hidden' id='signed" + newNoteIdx + "' value='false'><div id='n" + newNoteIdx + "'>" +
              input + "<div class='sig' style='display:inline;' id='" + sigId + "'><\/div>" + passwd + "<\/div><\/div>";
              
    if( changeToView(caseNote) ) {
        
        caseNote = "caseNote_note" + newNoteIdx;
        document.forms["caseManagementEntryForm"].note_edit.value = "new";
        document.forms["caseManagementEntryForm"].noteId.value = "0";
        document.forms["caseManagementEntryForm"].newNoteIdx.value = newNoteIdx;
        new Insertion.Bottom("encMainDiv", div);
        Rounded("div#"+id,"all","transparent","#CCCCCC","big border #000000");
        $(caseNote).focus();   
        adjustCaseNote();
        if( reason.length > 0 ) 
            setCaretPosition($(caseNote),$(caseNote).value.length);
            
        Element.observe(caseNote, 'keyup', monitorCaseNote);
        Element.observe(caseNote, 'click', getActiveText);

        origCaseNote = $F(caseNote);        
        ajaxUpdateIssues("edit", sigId); 
        addIssueFunc = updateIssues.bindAsEventListener(obj, makeIssue, sigId);
        Element.observe('asgnIssues', 'click', addIssueFunc);        

        //AutoCompleter for Issues
        <c:url value="/CaseManagementEntry.do?method=issueList&demographicNo=${demographicNo}&providerNo=${param.providerNo}" var="issueURL" />
        issueAutoCompleter = new Ajax.Autocompleter("issueAutocomplete", "issueAutocompleteList", "<c:out value="${issueURL}"/>", {minChars: 4, indicator: 'busy', afterUpdateElement: saveIssueId, onShow: autoCompleteShowMenu, onHide: autoCompleteHideMenu});        

        //hide new note button
        //$("newNoteImg").hide();
        
        //enable saving of notes
        $("saveImg").style.visibility = "visible";
        
        //start AutoSave
        setTimer();
    }
    else
        $(caseNote).focus();
        
    return false;
}

<c:url value="/CaseManagementEntry.do" var="autoSaveURL"/>
function deleteAutoSave() {
    var url = "<c:out value="${autoSaveURL}"/>";
    var frm = document.forms["caseManagementEntryForm"];
    frm.method.value = "cancel";
    
    new Ajax.Request( url, {
                                method: 'post',
                                postBody: Form.serialize(frm)
                           }
                    );
}

function autoSave(async) {    
    
    var url = "<c:out value="${autoSaveURL}"/>";
    var programId = "<c:out value="${case_program_id}"/>";
    var demoNo = "<c:out value="${demographicNo}"/>";
    var nId = document.forms["caseManagementEntryForm"].noteId.value < 0 ? 0 : document.forms["caseManagementEntryForm"].noteId.value;
    var params = "method=autosave&demographicNo=" + demoNo + "&programId=" + programId + "&note_id=" + nId + "&note=" + escape($F(caseNote));

    new Ajax.Request( url, {
                                method: 'post',
                                postBody: params,
                                asynchronous: async,
                                onSuccess: function(req) {     
                                                var nId = caseNote.substr(13);
                                                var sig = "sig" + nId;                                                
                                                var month=new Array(12)
                                                
                                                month[0]="Jan"
                                                month[1]="Feb"
                                                month[2]="Mar"
                                                month[3]="Apr"
                                                month[4]="May"
                                                month[5]="Jun"
                                                month[6]="Jul"
                                                month[7]="Aug"
                                                month[8]="Sep"
                                                month[9]="Oct"
                                                month[10]="Nov"
                                                month[11]="Dec"
                                                
                                                if( $("autosaveTime") == null ) 
                                                    new Insertion.Bottom(sig, "<div id='autosaveTime' class='sig' style='text-align:center; margin:0px;'><\/div>");
                                                    
                                                var d = new Date();
                                                var min = d.getMinutes();
                                                min = min < 10 ? "0" + min : min;
                                                var fmtDate = "<i>Draft Saved " + d.getDate() + "-" + month[d.getMonth()] + "-" + d.getFullYear() + " " + d.getHours() + ":" + min + "<\/i>";
                                                $("autosaveTime").update(fmtDate);
                                                
                                           }
                            }
                     );
                     
    
                                
}

function backup() {
    
    if(origCaseNote != $(caseNote).value || origObservationDate != $("observationDate").value) {
        autoSave(true);
    }
    
    setTimer();
}

var autoSaveTimer;
function setTimer() {
    autoSaveTimer = setTimeout("backup()", 60000);
}

function restore() {
    if(confirm('There is an unsaved note for this client.  Click Ok to edit it.')) {
        document.caseManagementEntryForm.method.value='restore';
        document.caseManagementEntryForm.chain.value = 'list';
	document.caseManagementEntryForm.submit();			
    }		
}

function showHistory(noteId, event) {
    Event.stop(event);
    <c:url value="/CaseManagementEntry.do" var="historyURL"/>
    var rnd = Math.round(Math.random() * 1000);
    win = "win" + rnd;
    var url = "<c:out value="${historyURL}"/>?method=notehistory&noteId=" + noteId;    
    window.open(url,win,"scrollbars=yes, location=no, width=647, height=600","");
    return false;
}

var caseNote = "";  //contains id of note text area; system permits only 1 text area at a time to be created
var numChars = 0;
function monitorCaseNote(e) {
    var MAXCHARS = 78;
    var MINCHARS = -10;
    var newChars = $(caseNote).value.length - numChars;
    var newline = false;
    
    if( e.keyCode == 13)       
      newline = true;    
   
    if( newline ) {
	adjustCaseNote();
    }	
    else if( newChars >= MAXCHARS ) {
        adjustCaseNote();
    }
    else if( newChars <= MINCHARS ) {
        adjustCaseNote();
    }

}

//resize case note text area to contain all text
function adjustCaseNote() {
    var MAXCHARS = 78;
    var payload = $(caseNote).value;
    var numLines = 0;
    var spacing = Prototype.Browser.IE == true ? 1.08 : Prototype.Browser.Gecko == true ? 1.11 : 1.2;
    var fontSize = $(caseNote).getStyle('font-size');
    var lHeight = $(caseNote).getStyle('line-height');
    var lineHeight = lHeight.substr(0,lHeight.indexOf('e'));
    var arrLines = payload.split("\n");    

    //we count each new line char and add a line for lines longer than max length
    for( var idx = 0; idx < arrLines.length; ++idx ) {

	if( arrLines[idx].length >= MAXCHARS ) {
	   numLines += Math.ceil( arrLines[idx].length / MAXCHARS );
	}
	else
	   ++numLines;
	    
    }
    //add a buffer
    numLines += 2;
    var noteHeight = Math.ceil(lineHeight * numLines);    
    noteHeight += 'em';
    $(caseNote).style.height = noteHeight;    

    numChars = $(caseNote).value.length;
}

function autoCompleteHideMenu(element, update){ 
    new Effect.Fade(update,{duration:0.15});
    new Effect.Fade($("issueTable"),{duration:0.15});
    new Effect.Fade($("issueList"),{duration:0.15});
}

function autoCompleteShowMenu(element, update){ 

    $("issueList").style.left = $("mainContent").style.left;    
    $("issueList").style.top = $("mainContent").style.top;
    $("issueList").style.width = $("issueAutocompleteList").style.width;    
    
    Effect.Appear($("issueList"), {duration:0.15});
    Effect.Appear($("issueTable"), {duration:0.15});    
    Effect.Appear(update,{duration:0.15});

}
    
    function callInProgress(xmlhttp) {
        switch (xmlhttp.readyState) {
            case 1: case 2: case 3:
                return true;
        // Case 4 and 0
            default:
                return false;
        } 
    }
    
    function printInfo(img,item) {
        var selected = "<c:out value="${ctx}"/>/oscarEncounter/graphics/printerGreen.png";
        var unselected = "<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png";
        
        if( $F(item) == "true" ) {
            $(img).src = unselected;
            $(item).value = "false";
        }
        else {
            $(img).src = selected;
            $(item).value = "true";                
        }
            
        return false;
    }
    
    function noteIsQeued(noteId) {    
        var foundIdx = -1;
        var curpos = 0;
        var arrNoteIds = $F("notes2print").split(",");
        
        for( var idx = 0; idx < arrNoteIds.length; ++idx ) {
            if( arrNoteIds[idx] == noteId ) {
                foundIdx = curpos;
                break;
            }
            curpos += arrNoteIds[idx].length+1;
        }
        
        
    
        return foundIdx;
    }
    
    function togglePrint(noteId,e) {    
        var selected = "<c:out value="${ctx}"/>/oscarEncounter/graphics/printerGreen.png";
        var unselected = "<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png";
        var imgId = "print" + noteId;
        var idx;
        var idx2;
        var tmp = "";
        
        //see whether we're called in a click event or not
        if( e != null )
            Event.stop(e);
            
        //if selected note has been inserted into print queue, remove it and update image src
        //else insert note into print queue        
        idx = noteIsQeued(noteId);
        if( idx  >= 0 ) {
            $(imgId).src = unselected;
            
            //if we're slicing first note off list
            if( idx == 0 ) {
                idx2 = $F("notes2print").indexOf(",");
                if( idx2 > 0 )
                    tmp = $F("notes2print").substring(idx2+1);
            }
            //or we're slicing after first element
            else {                
                idx2 = $F("notes2print").indexOf(",",idx);
                //are we in the middle of the list?
                if( idx2 > 0 ) {
                    tmp = $F("notes2print").substring(0,idx);
                    tmp += $F("notes2print").substring(idx2+1);
                }
                //or are we at the end of the list; don't copy comma
                else
                    tmp = $F("notes2print").substring(0,idx-1);
                    
           }
            
            $("notes2print").value = tmp;
        }
        else {
            $(imgId).src = selected
            if( $F("notes2print").length > 0 ) 
                $("notes2print").value += "," + noteId;
            else
               $("notes2print").value = noteId;         
        }
                
        return false;
    }
    
    function printNotes() {
        <c:url value="/CaseManagementEntry.do" var="printURL" />
        var empty = true;
        
        if( $F("notes2print").length == 0 ) {
            if( confirm("No notes are selected.  Do you wish to print all of them?") ) {
                printAll();
                empty = false;
            }
        }
        else
            empty = false;
            
        if( empty && $F("printCPP") == "false" && $F("printRx") == "false" ) {
            alert("Nothing selected to print");
            return false;
        }
        
        var url = "<c:out value="${printURL}" />";
        var frm = document.forms["caseManagementEntryForm"];

        frm.method.value = "print";
        frm.submit();

        
        return false;
    }
    
    function printAll() {
        var numNotes = <%=noteSize%>;
        var idx;
        var noteId;
        var notesDiv;
        
        //$("notes2print").value = "";
        
        //cycle through container divs for each note
        for( idx = 0; idx < numNotes; ++idx ) {
            notesDiv = $("nc" + idx).down('div');
            noteId = notesDiv.id.substr(1);  //get note id
            
            //if print img present, add note to print queue if not already there
            if( $("print"+noteId) != undefined ) {
                togglePrint(noteId,null);                              
            }            
        }                
    }
    
    function noPrivs(e) {
        Event.stop(e);
        alert("This note has not been signed by owner.  Select new note to add a note.");
    }
</script>

<%-- <div class="tabs" id="tabs">

<%
	String selectedTab = request.getParameter("tab");
	if(selectedTab==null || selectedTab.trim().equals("")) {
		selectedTab=CaseManagementViewFormBean.tabs[0];
	}
	pageContext.setAttribute("selectedTab",selectedTab);
		
	java.util.List aList=(java.util.List)request.getAttribute("Allergies"); 
	boolean allergies=false;
	if (aList!=null){
		allergies = aList.size() > 0;
	}
	
	boolean reminders = false;
	CaseManagementCPP cpp = (CaseManagementCPP)request.getAttribute("cpp");
	if(cpp!=null){
		reminders = cpp.getReminders().length() > 0;
	}
	//get programId
	String pId=(String)session.getAttribute("case_program_id");
	if (pId==null) pId="";
        System.out.println("case_program_id " + pId);
        System.out.println("Demo No " + request.getParameter("demographicNo"));
        System.out.println("Provider " + request.getParameter("providerNo"));
%>
<table>
<tr>
<th width="8%"></th><th style="font-size: 20" colspan="2" width="80%"><b>Case Management Encounter</b></th>
<%
WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
%>
<th width="12%" align="right" nowrap></th>
</tr>

</table>
<table cellpadding="0" cellspacing="0" border="0">

	<tr>
		<% for(int x=0;x<CaseManagementViewFormBean.tabs.length;x++) {%>
			<%
				String extra = "";
				if((allergies && CaseManagementViewFormBean.tabs[x].equals("Allergies"))||(reminders && CaseManagementViewFormBean.tabs[x].equals("Reminders")) ) {
					extra="color:red;";
				}
				
			%>
			<%if (CaseManagementViewFormBean.tabs[x].equals("Allergies") || CaseManagementViewFormBean.tabs[x].equals("Prescriptions")){%>
			<caisirole:SecurityAccess accessName="prescription Read" accessType="access" providerNo='<%=request.getParameter("providerNo")%>' demoNo='<%=request.getParameter("demographicNo")%>' programId="<%=pId%>">
			<%if(CaseManagementViewFormBean.tabs[x].equals(selectedTab)) { %>
				<td style="background-color: #555;<%=extra%>"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>'); return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<%} else { %>
				<td><a style="<%=extra %>" href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>');return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<% } %>
			</caisirole:SecurityAccess>
			<%}else{ %>
			<%if(CaseManagementViewFormBean.tabs[x].equals(selectedTab)) { %>
				<td style="background-color: #555;<%=extra%>"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>'); return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<%} else { %>
				<td><a style="<%=extra %>" href="javascript:void(0)" onclick="javascript:clickTab('<%=CaseManagementViewFormBean.tabs[x] %>');return false;"><%=CaseManagementViewFormBean.tabs[x] %></a></td>
			<% } %>
			<%} %>
		<% } %>
	</tr>
</table>
</div>
<br/> 

<table width="100%">
<tr>
<td width="75%">
<table cellspacing="1" cellpadding="1">
<tr>
	<td align="right" valign="top" nowrap><b>Client Name:</b></td><td><c:out value="${requestScope.casemgmt_demoName}" /></td>
</tr>
<tr>
	<td align="right"  valign="top" nowrap><b>Age:</b></td><td><c:out value="${requestScope.casemgmt_demoAge}" /></td>
</tr>
<tr>
	<td align="right"  valign="top" nowrap><b>DOB:</b></td><td><c:out value="${requestScope.casemgmt_demoDOB}" /></td>
</tr>
<tr>
	<td align="right"  valign="top" nowrap><b>Team:</b></td><td><c:out value="${requestScope.teamName}" /></td>
</tr>
<tr>	
	<td align="right"  valign="top" nowrap></td>
	<td><c:forEach var="tm" items="${teamMembers}">
		<c:out value="${tm}" />&nbsp;&nbsp;&nbsp;
	</c:forEach></td>	
</tr>
<tr>
	<td align="right"  valign="top" nowrap><b>Primary Health Care Provider:</b></td><td><c:out value="${requestScope.cpp.primaryPhysician}" /></td>
</tr>
<tr>
	<td align="right" valign="top" nowrap><b>Primary Counsellor/Caseworker:</b></td><td><c:out value="${requestScope.cpp.primaryCounsellor}" /></td>	
</tr>
</table>
</td>
<td>

	<%String demo=request.getParameter("demographicNo");%>
	<c:choose>
		<c:when test="${not empty requestScope.image_filename}">
			<img style="cursor: pointer;" id="ci" src="<c:out value="${ctx}"/>/images/default_img.jpg" alt="id_photo"  height="100" title="Click to upload new photo." OnMouseOver="document.getElementById('ci').src='<c:out value="${ctx}"/>/images/<c:out value="${requestScope.image_filename}"/>'" OnMouseOut="delay(5000)" window.status='Click to upload new photo'; return true;" onClick="popupUploadPage('uploadimage.jsp',<%=demo%>);return false;"/>	
		</c:when>
		<c:otherwise>
			<img style="cursor: pointer;" src="<c:out value="${ctx}"/>/images/defaultR_img.jpg" alt="No_Id_Photo" height="100" title="Click to upload new photo." OnMouseOver="window.status='Click to upload new photo';return true" onClick="popupUploadPage('uploadimage.jsp',<%=demo%>);return false;"/>
		</c:otherwise>
	</c:choose>
	
</td>

</tr>
</table>
 
<jsp:include page='<%="/casemgmt/"+selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>'/>
--%>

        <html:form action="/CaseManagementView" method="post">        
            <html:hidden property="demographicNo"/>
            <html:hidden property="providerNo" value="<%=provNo%>" />
            <html:hidden property="tab" value="Current Issues"/>
            <html:hidden property="hideActiveIssue"/>
            <html:hidden property="ectWin.rowOneSize" styleId="rowOneSize"/>
            <html:hidden property="ectWin.rowTwoSize" styleId="rowTwoSize"/>
            <input type="hidden" name="chain" value="list" /> 
            <input type="hidden" name="method" value="view"/>
            <input type="hidden" id="check_issue" name="check_issue">
            <!--Row One Headers -->
            
                <div style="float:left; width:34%; background-color:#CCCCFF;" class="RowTop" >&nbsp;<bean:message key="oscarEncounter.Index.socialFamHist"/>:</div><input type="hidden" name="shInput"/>
                <div style="float:left; width:33%; border-width:0px; background-color:#CCCCFF;" class="RowTop" >
                    <% if(oscarVariables.getProperty("otherMedications", "").length() > 1) {
                    out.print(oscarVariables.getProperty("otherMedications", ""));
                    %>
                    <% } else { %>
                    <bean:message key="oscarEncounter.Index.otherMed"/>:
                    <% } %>
                </div>
                <div style="float:left; width:18%; margin-right:-4px; border-width:0px; background-color:#CCCCFF;" class="RowTop" >
                    <% if(oscarVariables.getProperty("medicalHistory", "").length() > 1) {
                    out.print(oscarVariables.getProperty("medicalHistory", ""));
                    %>
                    <% } else { %>
                    <bean:message key="oscarEncounter.Index.medHist"/>:
                    <% } %>
                </div>  
                
                <div class="RowTop" style="clear:right; float:right; width:15%; text-align:right;vertical-align:bottom; background-color:#CCCCFF;">
                    <a onMouseOver="javascript:window.status='Minimize'; return true;" href="javascript:rowOneX();" title="<bean:message key="oscarEncounter.Index.tooltipClose"/>">
                    <bean:message key="oscarEncounter.Index.x"/></a> |
                    <a onMouseOver="javascript:window.status='Small Size'; return true;" href="javascript:rowOneSmall();" title="<bean:message key="oscarEncounter.Index.tooltipSmall"/>">
                    <bean:message key="oscarEncounter.Index.s"/></a> |
                    <a onMouseOver="javascript:window.status='Medium Size'; return true;" href="javascript:rowOneNormal();" title="<bean:message key="oscarEncounter.Index.tooltipNormal"/>">
                    <bean:message key="oscarEncounter.Index.n"/></a> |
                    <a onMouseOver="javascript:window.status='Large Size'; return true;" href="javascript:rowOneLarge();" title="<bean:message key="oscarEncounter.Index.tooltipLarge"/>">
                    <bean:message key="oscarEncounter.Index.l"/></a> |
                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:rowOneFull();" title="<bean:message key="oscarEncounter.Index.tooltipFull"/>">
                    <bean:message key="oscarEncounter.Index.f"/></a> |
                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:reset();" title="<bean:message key="oscarEncounter.Index.tooltipReset"/>">
                    <bean:message key="oscarEncounter.Index.r"/></a>
                </div>
                
                <!-- Creating the table tag within the script allows you to adjust all table sizes at once, by changing the value of leftCol -->
                <div id="divR1" style="float:left; width:34%; border-width:0px; background-color:#CCCCFF;">
                    &nbsp;<html:textarea styleId="cpp.socialHistory" property="cpp.socialHistory" tabindex="1" styleClass="rowOne" rows="4" cols="28"/>
                </div>
                
                <!-- This is the Family History cell ...fh...-->
                <div style="float:left; width:33%; border-width:0px; background-color:#CCCCFF;">
                    <html:textarea styleId="cpp.familyHistory" property="cpp.familyHistory" tabindex="2" styleClass="rowOne"  rows="4" cols="28"/>
                </div>
                
                <!-- This is the Medical History cell ...mh...-->
                <div style="clear:right; float:left; width:33%;  margin-right:-4px; border-width:0px; background-color:#CCCCFF;">
                    <html:textarea styleId="cpp.medicalHistory" property="cpp.medicalHistory" tabindex="3" styleClass="rowOne"  rows="4" cols="28"/>
                </div>
                
                <!--2nd row headers -->
                <div style="float:left; width:50%; background-color:#CCCCFF;" class="RowTop" >
                    &nbsp;
                    <% if(oscarVariables.getProperty("ongoingConcerns", "").length() > 1) {
                    out.print(oscarVariables.getProperty("ongoingConcerns", ""));
                    %>
                    <% } else { %>
                    <bean:message key="oscarEncounter.Index.msgConcerns"/>:
                    <% } %>
                </div><input type="hidden" name="ocInput"/>
                
                <div style="float:left; width:35%; margin-right:-4px; background-color:#CCCCFF;" class="RowTop" ><bean:message key="oscarEncounter.Index.msgReminders"/>:</div>   
                
                <div class="RowTop" style="clear:right; float:right; width:15%; text-align:right; vertical-align:bottom; background-color:#CCCCFF;">
                    <a onMouseOver="javascript:window.status='Minimize'; return true;" href="javascript:rowTwoX();" title="<bean:message key="oscarEncounter.Index.tooltipClose"/>">
                    <bean:message key="oscarEncounter.Index.x"/></a> |
                    <a onMouseOver="javascript:window.status='Small Size'; return true;" href="javascript:rowTwoSmall();" title="<bean:message key="oscarEncounter.Index.tooltipSmall"/>">
                    <bean:message key="oscarEncounter.Index.s"/></a> |
                    <a onMouseOver="javascript:window.status='Medium Size'; return true;" href="javascript:rowTwoNormal();" title="<bean:message key="oscarEncounter.Index.tooltipNormal"/>">
                    <bean:message key="oscarEncounter.Index.n"/></a> |
                    <a onMouseOver="javascript:window.status='Large Size'; return true;" href="javascript:rowTwoLarge();" title="<bean:message key="oscarEncounter.Index.tooltipLarge"/>">
                    <bean:message key="oscarEncounter.Index.l"/></a> |
                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:rowTwoFull();" title="<bean:message key="oscarEncounter.Index.tooltipFull"/>">
                    <bean:message key="oscarEncounter.Index.f"/></a> |
                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:reset();" title="<bean:message key="oscarEncounter.Index.tooltipReset"/>">
                    <bean:message key="oscarEncounter.Index.r"/></a>
                </div>
                
                <!--Ongoing Concerns cell -->
                <div style="float:left; width:50%; background-color:#CCCCFF;">
                    
                    <html:textarea styleId="cpp.ongoingConcerns" property="cpp.ongoingConcerns" tabindex="4" styleClass="rowTwo"  rows="4" cols="44"/>
                </div>     
                
                <!--Reminders cell -->
                <div style="clear:right; float:left; width:50%; margin-right:-4px; background-color:#CCCCFF;">
                    <html:textarea styleId="cpp.reminders" property="cpp.reminders" tabindex="5" styleClass="rowTwo"  rows="4" cols="44"/>
                </div> 
            
        <div id="notCPP" style="height:70%; margin-left:2px; background-color:#FFFFFF;">
        <div id="rightNavBarColumn" style="position:absolute; top:30px; left:205px; width:20%; z-index:10; background-color:transparent;"><jsp:include page="rightColumn.jsp" /></div>
        <div id="topContent" style="float:left; width:100%; margin-right:-2px; padding-bottom:10px; background-color:#CCCCFF; font-size:10px;">
            <span style="cursor:pointer; text-decoration:underline;" onclick="showFilter();">View Filter</span>
            <div id="filter" style="display:none;">
                <div style="height:150px; width:auto; overflow:auto; float:left; position:relative; left:10%;">
                    Provider:
                    <ul style="margin-left:0px; margin-top:1px; list-style: none inside none;">
                        <li><html:multibox property="filter_providers" value="a" onclick="filterCheckBox(this)"></html:multibox>All</li>
                        <%
                        Set providers = (Set)request.getAttribute("providers"); 
                        Object[] arrProv = providers.toArray();
                        if( arrProv.length > 1 ) {
                        Provider.ComparatorName comp = ((Provider)arrProv[0]).ComparatorName();
                        Arrays.sort(arrProv, comp);
                        }
                        
                        String providerNo;
                        Provider prov;
                        
                        for(int idx = 0; idx < arrProv.length; ++idx ) {
                        prov = (Provider)arrProv[idx];
                        providerNo = prov.getProviderNo();
                        %>
                        <li><html:multibox property="filter_providers" value="<%=providerNo%>" onclick="filterCheckBox(this)"></html:multibox><%=prov.getFormattedName()%></li>
                        <%                        
                        }
                        %>
                    </ul>
                </div>
                
                <div style="height:150px; width:auto; overflow:auto; float:left; position:relative; left:20%;">
                    Role:
                    <ul style="margin-left:0px; margin-top:1px; list-style: none inside none;">
                        <li><html:multibox property="filter_roles" value="a" onclick="filterCheckBox(this)"></html:multibox>All</li>
                        <%
                        List roles = (List)request.getAttribute("roles");
                        for( int num = 0; num < roles.size(); ++num ) {
                        Role role = (Role)roles.get(num);
                        %>
                        <li><html:multibox property="filter_roles" value="<%=String.valueOf(role.getId())%>" onclick="filterCheckBox(this)"></html:multibox><%=role.getName()%></li>
                        <%
                        }
                        %>
                    </ul>
                </div>
                
                <div style="float:left; position:relative; left:30%;">
                    Sort:
                    <ul style="margin-left:0px; margin-top:1px; list-style: none inside none;">
                        <li><html:radio property="note_sort" value="observation_date_asc">Observation Date Asc</html:radio></li>
                        <li><html:radio property="note_sort" value="observation_date">Observation Date Desc</html:radio></li>
                        <li><html:radio property="note_sort" value="providerName">Provider</html:radio></li>
                        <li><html:radio property="note_sort" value="programName">Program</html:radio></li>
                        <li><html:radio property="note_sort" value="roleName">Role</html:radio></li>                        
                    </ul>
                </div>
                
                <div style="float:left; clear:both; cursor:pointer; text-decoration:underline;" onclick="return filter();">
                    Show View
                </div>                
            </div>            
            <div style="float:left; clear:both; margin-bottom:5px; width:100%; text-align:center;">                
                <input id="enTemplate" tabindex="6" size="16" type="text" value="" onkeypress="return grabEnterGetTemplate(event)" />
                <div class="enTemplate_name_auto_complete" id="enTemplate_list" style="z-index:1001; display:none"></div>  
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <select id="channel" >
                    <option value="http://resource.oscarmcmaster.org/oscarResource/OSCAR_search/OSCAR_search_results?title="><bean:message key="oscarEncounter.Index.oscarSearch"/></option>
                    <option value="http://www.google.com/search?q="><bean:message key="global.google"/></option>
                    <option value="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?SUBMIT=y&amp;CDM=Search&amp;DB=PubMed&amp;term="><bean:message key="global.pubmed"/></option>
                    <option value="http://search.nlm.nih.gov/medlineplus/query?DISAMBIGUATION=true&amp;FUNCTION=search&amp;SERVER2=server2&amp;SERVER1=server1&amp;PARAMETER="><bean:message key="global.medlineplus"/></option>
                    <option value="http://www.bnf.org/bnf/bnf/current/noframes/search.htm?n=50&amp;searchButton=Search&amp;q="><bean:message key="global.BNF"/></option>
                </select>
                <input type="text" id="keyword" name="keyword"  value=""  onkeypress="return grabEnter('searchButton',event)"/>
                <input type="button" id="searchButton" name="button"  value="Search" onClick="popupPage(600,800,'<bean:message key="oscarEncounter.Index.popupSearchPageWindow"/>',$('channel').options[$('channel').selectedIndex].value+urlencode($F('keyword')) ); return false;">       
                
            </div>
            <div style="text-align:right">
                <img title="Print" id='imgPrintCPP' alt="Toggle Print CPP" onclick="return printInfo(this,'printCPP');"  src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png'/>&nbsp;CPP
                &nbsp;&nbsp;
                <img title="Print" id='imgPrintRx' alt="Toggle Print Rx" onclick="return printInfo(this, 'printRx');"  src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png'/>&nbsp;Rx
                &nbsp;&nbsp;
            </div>
        </div>
    </html:form>          
    
    <nested:form action="/CaseManagementEntry" style="display:inline; margin-top:0; margin-bottom:0;">
        <html:hidden property="demographicNo"/>
        <html:hidden property="providerNo"/>
        <html:hidden property="includeIssue" value="off"/>
        <input type="hidden" name="deleteId" value="0">
        <input type="hidden" name="lineId" value="0">
        <input type="hidden" name="from" value="casemgmt">
        <input type="hidden" name="method" value="save">
        <input type="hidden" name="change_diagnosis" value="<c:out value="${change_diagnosis}"/>"/>
        <input type="hidden" name="change_diagnosis_id" value="<c:out value="${change_diagnosis_id}"/>"/>                                     
        <input type="hidden" name="newIssueId" id="newIssueId">
        <input type="hidden" name="newIssueName" id="newIssueName">
        <input type="hidden" name="ajax" value="false">
        <input type="hidden" name="chain" value="">
        <input type="hidden" name="caseNote.program_no" value="<%=pId%>">
        <input type="hidden" name="noteId" value="0">
        <input type="hidden" name="note_edit" value="new">
        <input type="hidden" name="sign" value="off">
        <input type="hidden" name="verify" value="off">
        <input type="hidden" name="forceNote" value="false">
        <input type="hidden" name="newNoteIdx" value="">
        <input type="hidden" name="notes2print" id="notes2print" value="">
        <input type="hidden" name="printCPP" id="printCPP" value="false">
        <input type="hidden" name="printRx" id="printRx" value="false">
        <div id="mainContent" style="background-color:#FFFFFF; width:100%; margin-right:-2px; display:inline; float:left;"> 
            <span id="issueList" style="background-color:#FFFFFF; height:440px; width:350px; position:absolute; z-index:1001; display:none; overflow:auto;">
                <table id="issueTable" class="enTemplate_name_auto_complete" style="position:relative; left:0px; display:none;">
                    <tr>
                        <td style="height:430px; vertical-align: bottom;"> 
                            <div class="enTemplate_name_auto_complete" id="issueAutocompleteList" name="issueAutocompleteList" style="position:relative; left:0px; display:none;"></div>
                        </td>
                    </tr>
                </table> 
            </span>
            <div id="encMainDiv" style="width:99%; border-top: thin groove #000000; border-right: thin groove #000000; border-left: thin groove #000000; background-color:#FFFFFF; height:420px; overflow:auto; margin-left:2px;">
                
                
                <c:if test="${not empty Notes}">                        
                    
                    <%
                    //java.util.List noteList=(java.util.List)request.getAttribute("Notes");
                    int idx = 0;
                    //int noteSize = noteList.size();            
                    
                    //Notes list will contain all notes including most recently saved
                    //we need to skip this one when displaying
                    CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean)session.getAttribute("caseManagementEntryForm");
                    
                    //if we're editing a note, display it
                    //else check for last unsigned note and use it if present
                    if( cform.getCaseNote().getId() != null ) {
                    savedId = cform.getCaseNote().getId();                
                    }                              
                    
                    for( idx = 0; idx < noteSize; ++idx ) {
                    
                    CaseManagementNote note = (CaseManagementNote)noteList.get(idx);
                    String noteStr = note.getNote();
                    %>                               
                    <div id="nc<%=idx%>" class="note"> 
                        <input type="hidden" id="signed<%=note.getId()%>" value="<%=note.isSigned()%>">
                        <div id="n<%=note.getId()%>">
                            <%
                            //display last saved note for editing
                            if( note.getId() == savedId ) {    
                            found = true;
                            %>    
                            <textarea  tabindex="7" cols="84" rows="10" wrap='hard' class="txtArea" style="line-height:1.1em;" name="caseNote_note" id="caseNote_note<%=savedId%>"><nested:write property="caseNote.note"/></textarea>
                            <div class="sig" style="display:inline;" id="sig<%=note.getId()%>">
                                <%@ include file="noteIssueList.jsp" %>
                            </div>
                            
                            <c:if test="${sessionScope.passwordEnabled=='true'}">
                                <p style='background-color:#CCCCFF; display:none; margin:0px;' id='notePasswd'>Password:&nbsp;<html:password property="caseNote.password"/></p>
                            </c:if>                                 
                            
                            <%    
                            }
                            //else display contents of note for viewing
                            else {
                                                                                                                                            
                            if( note.isLocked() ) {
                            %>
                            <pre><bean:message key="oscarEncounter.Index.msgLocked" /> <%=DateUtils.getDate(note.getUpdate_date(),dateFormat) + " " + note.getProviderName()%></pre>                                  
                            <%
                            }
                            else {                                      
                            
                            String rev = note.getRevision();
                            %>
                            <img title="Minimize Display" id='quitImg<%=note.getId()%>' alt="Minimize Display" onclick="minView(event)" style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>
                            <img title="Print" id='print<%=note.getId()%>' alt="Toggle Print Note" onclick="togglePrint(<%=note.getId()%>, event)" style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/printer.png'/>
                            <pre><%=noteStr%></pre>
                            <%
                            if( largeNote(noteStr) ) {
                            %>                                       
                            <img title="Minimize Display" id='bottomQuitImg<%=note.getId()%>' alt="Minimize Display" onclick="minView(event)" style='float:right; margin-right:5px; margin-bottom:3px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>
                            <%
                            }
                            %>                                      
                            <div id="sig<%=note.getId()%>" style="clear:both;"><div class="sig" id="sumary<%=note.getId()%>">
                                    <div id="observation<%=note.getId()%>" style="float:right;margin-right:3px;"><i>Date:&nbsp;<span id="obs<%=note.getId()%>"><%=DateUtils.getDate(note.getObservation_date(),dateFormat)%></span>&nbsp;rev<a href="#" onclick="return showHistory('<%=note.getId()%>', event);"><%=rev%></a></i></div>
                                    <div><span style="float:left;">Editors:</span>
                                        <ul style="list-style: none inside none; margin:0px;">    
                                            <%  
                                            List editors = note.getEditors();
                                            Iterator<Provider> it = editors.iterator(); 
                                            int count = 0;
                                            int MAXLINE = 2;
                                            while( it.hasNext() ) {
                                            Provider p = it.next();
                                            
                                            if( count % MAXLINE == 0 ) {
                                            out.print("<li>" + p.getFormattedName() + "; ");
                                            }
                                            else {
                                            out.print(p.getFormattedName() + "</li>");
                                            }
                                            ++count;
                                            }
                                            if( count % MAXLINE == 0 )
                                            out.print("</li>");
                                            %>
                                            
                                        </ul>                                        
                                    </div>
                                    <div style="clear:right;margin-right:3px;float:right;">
                                        Enc Type:&nbsp;<span id="encType<%=note.getId()%>"><%=note.getEncounter_type().equals("")?"":"&quot;"+note.getEncounter_type()+"&quot;"%></span>
                                    </div>
                                    <%
                                    Set issSet = note.getIssues();
                                    if( issSet.isEmpty() ) {
                                    %>
                                    <div>&nbsp;</div>
                                    <%
                                    }
                                    if( issSet.size() > 0 ) {
                                    %>
                                    
                                    Assigned Issues
                                    <ul style="list-style: circle inside none; margin:0px;">                            
                                        <% 
                                        Iterator i = issSet.iterator();
                                        while( i.hasNext() ) {
                                        CaseManagementIssue iss = (CaseManagementIssue)i.next();
                                        %>
                                        <li><%=iss.getIssue().getDescription().trim()%></li>
                                        <%
                                        }
                                    %></ul>                                     
                                    <%
                                    }
                                    %>
                                </div>
                            </div>
                            <%
                            }
                            }
                            //System.out.println("READONLY SESSION " + session.getAttribute("readonly").equals(false));                                  
                            %>
                        </div>
                    </div>
                    <%
                    //if we are not editing note, remember note ids for setting event listeners
                    //Internet Explorer does not play nice with inserting javascript between divs
                    //so we store the ids here and list the event listeners at the end of this script
                    if( note.getId() != savedId ) {
                        
                        if( note.isSigned() || (!note.isSigned() && note.getProviderNo().equals(provNo))) {
                            if( note.isLocked() ) { 
                                lockedNotes.add(note.getId());
                            }
                            else {
                                unLockedNotes.add(note.getId());
                            }
                        }
                        else {
                            unEditableNotes.add(note.getId());
                        }
                    }    
                    
                    } //end for 
                    %>
                    
                </c:if>        
                
                <%
                if( !found ) {
                savedId = 0;
                %>    
                <div class="note">
                    <input type="hidden" id="signed<%=savedId%>" value="false">
                    <div id="n<%=savedId%>" style="line-height:1.1em;">                                     
                        <textarea  tabindex="7" cols="84" rows="10" wrap='hard' class="txtArea" style="line-height:1.1em;" name="caseNote_note" id="caseNote_note<%=savedId%>"><nested:write property="caseNote_note"/></textarea>
                        <div id="sig0">
                            <%@ include file="noteIssueList.jsp" %>                                        
                        </div>
                        
                        <c:if test="${sessionScope.passwordEnabled=='true'}">
                            <p style='display:none;' id='notePasswd'>Password:&nbsp;<html:password property="caseNote.password"/></p>
                        </c:if>
                        
                        
                    </div>
                </div>
                <%
                }
                %>
                
            </div>
            <div id='save' style="width:99%; background-color:#CCCCFF; padding-top:5px; margin-left:2px; border-left: thin solid #000000; border-right: thin solid #000000; border-bottom: thin solid #000000;">  
                <span style="float:right; margin-right:5px;">                                
                    <input tabindex="10" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/media-floppy.png"/>" id="saveImg" onclick="Event.stop(event);return savePage('save');" title='<bean:message key="oscarEncounter.Index.btnSave"/>'>&nbsp;                              
                    <input tabindex="11" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/document-new.png"/>" id="newNoteImg" onclick="newNote(event); return false;" title='<bean:message key="oscarEncounter.Index.btnNew"/>'>&nbsp;                              
                    <input tabindex="12" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/note-save.png"/>" onclick="document.forms['caseManagementEntryForm'].sign.value='on';Event.stop(event);return savePage('saveAndExit');" title='<bean:message key="oscarEncounter.Index.btnSignSave"/>'>&nbsp;
                    <input tabindex="13" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/verify-sign.png"/>" onclick="document.forms['caseManagementEntryForm'].sign.value='on';document.forms['caseManagementEntryForm'].verify.value='on';Event.stop(event);return savePage('saveAndExit');" title='<bean:message key="oscarEncounter.Index.btnSign"/>'>&nbsp;
                    <input tabindex="14" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/lock-note.png"/>" onclick="return toggleNotePasswd();" title='<bean:message key="oscarEncounter.Index.btnLock"/>'>&nbsp;
                    <input tabindex="15" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/system-log-out.png"/>" onclick='closeEnc(event);return false;' title='<bean:message key="global.btnExit"/>'>&nbsp;
                    <input tabindex="16" type='image' src="<c:out value="${ctx}/oscarEncounter/graphics/document-print.png"/>" onclick="return printNotes();" title='<bean:message key="oscarEncounter.Index.btnPrint"/>'>                              
                </span>
                <input type='image' id='toggleIssue' onclick="return showIssues(event);" src="<c:out value="${ctx}/oscarEncounter/graphics/issues.png"/>" title='Display Issues'>&nbsp;
                <input  tabindex="8" type="text" id="issueAutocomplete" name="issueSearch" style="z-index: 2;" onkeypress="return submitIssue(event);" size="25">&nbsp;
                <input  tabindex="9" type="button" id="asgnIssues" value="Assign Issues">
                <span id="busy" style="display: none"><img style="position:absolute;" src="<c:out value="${ctx}/oscarEncounter/graphics/busy.gif"/>" alt="Working..." /></span>
                
                
            </div>
        </div>              
    </nested:form>          
    </div>
          
<script type="text/javascript">                         
    document.forms["caseManagementEntryForm"].noteId.value = "<%=savedId%>";
    
        
    caseNote = "caseNote_note" + "<%=savedId%>"; 
    setupNotes();
    Element.observe(caseNote, "keyup", monitorCaseNote); 
    Element.observe(caseNote, 'click', getActiveText);
    <%
        Long num;
        Iterator iterator = lockedNotes.iterator();
        while( iterator.hasNext() ) {
            num = (Long)iterator.next();
    %>
            Element.observe('n<%=num%>', 'click', unlockNote);
    <%
        }
        
        iterator = unLockedNotes.iterator();
        while( iterator.hasNext() ) {
            num = (Long)iterator.next();
    %>
            Element.observe('n<%=num%>', 'click', editNote);
    <%
        }      
    
        iterator = unEditableNotes.iterator();
        while( iterator.hasNext() ) {
            num = (Long)iterator.next();
    %>
            Element.observe('n<%=num%>', 'click', noPrivs);
    <%
        }
    %>   
   
    
    //flag for determining if we want to submit case management entry form with enter key pressed in auto completer text box
    var submitIssues = false;
   //AutoCompleter for Issues
    <c:url value="/CaseManagementEntry.do?method=issueList&demographicNo=${demographicNo}&providerNo=${param.providerNo}" var="issueURL" />
    var issueAutoCompleter = new Ajax.Autocompleter("issueAutocomplete", "issueAutocompleteList", "<c:out value="${issueURL}"/>", {minChars: 4, indicator: 'busy', afterUpdateElement: saveIssueId, onShow: autoCompleteShowMenu, onHide: autoCompleteHideMenu});

    <%
    int MaxLen = 20;
    int TruncLen = 17;
    String ellipses = "...";
    for(int j=0; j<bean.templateNames.size(); j++) {
     String encounterTmp = (String)bean.templateNames.get(j);
     encounterTmp = oscar.util.StringUtils.maxLenString(encounterTmp, MaxLen, TruncLen, ellipses);
     encounterTmp = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(encounterTmp);
   %>
     autoCompleted["<%=encounterTmp%>"] = "ajaxInsertTemplate('<%=encounterTmp%>')";
     autoCompList.push("<%=encounterTmp%>");
     itemColours["<%=encounterTmp%>"] = "99CCCC";
   <%
    }
   %>      
   //set default event for assigning issues
   //we do this here so we can change event listener when changing diagnosis
   var obj = { };
   var makeIssue = "makeIssue";
   var defaultDiv = "sig<%=savedId%>";
   var changeIssueFunc;  //set in changeDiagnosis function above
   var addIssueFunc = updateIssues.bindAsEventListener(obj, makeIssue, defaultDiv);
   Element.observe('asgnIssues', 'click', addIssueFunc);
   new Autocompleter.Local('enTemplate', 'enTemplate_list', autoCompList, { colours: itemColours, afterUpdateElement: menuAction }  );      
   
   //start timer for autosave
   setTimer();  
   
   //are we editing existing note?  if not init newNoteIdx as we are dealing with a new note
   //save initial note to determine whether save is necessary
   origCaseNote = $F(caseNote);
   <% if( found != true ) { %>
        document.forms["caseManagementEntryForm"].newNoteIdx.value = <%=savedId%>;
   <%}%>
                          
    //$("encMainDiv").scrollTop = $("n<%=savedId%>").offsetTop - $("encMainDiv").offsetTop;
   </script>
   
<%!
  
    /*
     *Insert encounter reason for new note
     */
    protected String insertReason(HttpServletRequest request) {
        oscar.oscarEncounter.pageUtil.EctSessionBean bean = (oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("casemgmt_oscar_bean");
        String encounterText = "";
        if( bean != null ) {            
            String apptDate = convertDateFmt(bean.appointmentDate);
            if(bean.eChartTimeStamp==null){
                  encounterText ="\n["+oscar.util.UtilDateUtilities.DateToString(bean.currentDate, "dd-MMM-yyyy",request.getLocale())+" .: "+bean.reason+"] \n";
                  //encounterText +="\n["+bean.appointmentDate+" .: "+bean.reason+"] \n";
            }else { //if(bean.currentDate.compareTo(bean.eChartTimeStamp)>0){
                   //System.out.println("2curr Date "+ oscar.util.UtilDateUtilities.DateToString(oscar.util.UtilDateUtilities.now(),"yyyy",java.util.Locale.CANADA) );
                  //encounterText +="\n__________________________________________________\n["+dateConvert.DateToString(bean.currentDate)+" .: "+bean.reason+"]\n";
                   encounterText ="\n["+("".equals(bean.appointmentDate)?oscar.util.UtilDateUtilities.getToday("dd-MMM-yyyy"):apptDate)+" .: "+bean.reason+"]\n";
            }/*else {//if((bean.currentDate.compareTo(bean.eChartTimeStamp) == 0) && (bean.reason != null || bean.subject != null ) && !bean.reason.equals(bean.subject) ){
                   //encounterText +="\n__________________________________________________\n["+dateConvert.DateToString(bean.currentDate)+" .: "+bean.reason+"]\n";
                   encounterText ="\n["+apptDate+" .: "+bean.reason+"]\n";
            }*/
           //System.out.println("eChartTimeStamp" + bean.eChartTimeStamp+"  bean.currentDate " + dateConvert.DateToString(bean.currentDate));//" diff "+bean.currentDate.compareTo(bean.eChartTimeStamp));
           if(!bean.oscarMsg.equals("")){
              encounterText +="\n\n"+bean.oscarMsg;
           }

        }
        encounterText = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(encounterText);
        return encounterText;
    }
    
    protected String convertDateFmt(String strOldDate) {
        String strNewDate = "";
        if( strOldDate != null && strOldDate.length() > 0 ) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            try {
                
                Date tempDate = fmt.parse(strOldDate);
                strNewDate = new SimpleDateFormat("dd-MMM-yyyy").format(tempDate);
                
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        
        return strNewDate;
    }
    
    protected boolean largeNote(String note) {
        final int THRESHOLD = 10;
        boolean isLarge = false;       
        int pos = -1;
        
        for( int count = 0; (pos = note.indexOf("\n",pos+1)) != -1; ++count ) {
            if( count == THRESHOLD ) {
                isLarge = true;
                break;
            }
        }
        
        return isLarge;
    }

%>
