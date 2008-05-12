    var numNotes;   //How many saved notes do we have?
    var ctx;        //url context
    var providerNo;
    var demographicNo;
    var case_program_id;
    var caisiEnabled = false;
    var passwordEnabled = false;

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
        
        if(varpage!= 'null'){                  
          var page = ctx + "/oscarEncounter/InsertTemplate.do";
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
    
    for( var count = 0; (pos = note.indexOf("<br>",pos+1)) != -1; ++count ) {
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
    var img = "<img id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
        
    
    Element.remove(Event.element(e).id);
    Event.stop(e);
    
    if( error )
        Element.remove("passwdError");
        
    if( frm )
        Element.remove("passwdPara");        
        
    //new Insertion.Top(parent, img);
    Element.observe(parent, 'click', unlockNote);
}

var updatedNoteId = -1;  //used to store id of ajax saved note used below
var selectBoxes = new Object();
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
            ajaxSaveNote(sig,nId,tmp);              
        }
   }
   
   
    //cancel updating of issues
    //IE destroys innerHTML of sig div when calling ajax update
    //so we have to restore it here if the ajax call is aborted
    //this is buggy don't use
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

    Element.remove("notePasswd");

    Element.stopObserving(id, 'keyup', monitorCaseNote);
    Element.stopObserving(id, 'click', getActiveText);   

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
        
    var selectEnc = "encTypeSelect" + nId;
    
    if( $(selectEnc) != null ) {
        var encTypeId = "encType" + nId;
        var content = $F(selectEnc);
        var encType;
        if( content.length > 0 )
            encType = "&quot;" + content + "&quot;";
        else
            encType = "";
        Element.remove(selectEnc);
        $(encTypeId).update(encType); 
        
    }    
    //we can stop listening for add issue here
    Element.stopObserving('asgnIssues', 'click', addIssueFunc);
    if( tmp.length == 0 ) 
        tmp = "&nbsp;";
        
    tmp = tmp.replace(/\n/g,"<br>");
    if( !saving ) {
        if( largeNote(tmp) ) {
            var btmImg = "<img title='Minimize Display' id='bottomQuitImg" + nId + "' alt='Minimize Display' onclick='minView(event)' style='float:right; margin-right:5px; margin-bottom:3px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
            new Insertion.Top(parent, btmImg); 
        }

        //$(txt).style.fontSize = normalFont;

        //if we're not restoring a new note display print img
        //if( nId.substr(0,1) != "0" ) {
        //    img = "<img title='Print' id='print" + nId + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
        //     new Insertion.Top(parent, img);
       // }

        var printImg = "print" + nId;       
        var img = "<img title='Minimize' id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
        var printimg = "<img title='Print' id='" + printImg + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
        var input = "<span id='txt" + nId + "'>" + tmp + "<\/span>";
        
        new Insertion.Top(parent, input);

        if( nId.substr(0,1) != "0" )            
            Element.remove(printImg);
        
        new Insertion.Top(parent, printimg); 
        new Insertion.Top(parent, img);            

        $(parent).style.height = "auto";        
        Element.observe(parent, 'click', editNote);    
    }
    return true;
}

function completeChangeToView(note,newId) {
    //var newId = updatedNoteId;
    var parent = "n" + newId;
    
    var selectEnc = "encTypeSelect" + newId;
    if( $(selectEnc) != null ) {
        var encTypeId = "encType" + newId;
        var content = $F(selectEnc);
        var encType;
        if( content.length > 0 )
            encType = "&quot;" + content + "&quot;";
        else
            encType = "";
        Element.remove(selectEnc);
        $(encTypeId).update(encType); 
       
    } 
    
    note = note.replace(/\n/g,"<br>");
    if( largeNote(note) ) {
        var btmImg = "<img title='Minimize Display' id='bottomQuitImg" + newId + "' alt='Minimize Display' onclick='minView(event)' style='float:right; margin-right:5px; margin-bottom:3px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
        new Insertion.Top(parent, btmImg); 
    }

    var input = "<span id='txt" + newId + "'>" + note + "<\/span>";    
    //$(txt).style.fontSize = normalFont    

    var imgId = "quitImg" + newId;
    var printId = "print" + newId;
    var img = "<img title='Minimize' id='" + imgId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'/>";
    var printimg = "<img title='Print' id='" + printId + "' alt='Toggle Print Note' onclick='togglePrint(" + newId + ", event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
    if( $(printId) != null ) {        
        Element.remove(printId);
    }

    new Insertion.Top(parent, input);
    new Insertion.Top(parent, printimg);        
    new Insertion.Top(parent, img);    

    $(parent).style.height = "auto";        
    Element.observe(parent, 'click', editNote); 

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
    //$(txt).style.height = divHeight;    c
    
    var txtId = "txt" + nId;
    var line = $(txtId).innerHTML.substr(0,100);
    line = line.replace(/<br>/g," ");
    var dateValue = $(dateId) != null ? $(dateId).innerHTML : "";
    line = "<div id='" + date + "' style='float:left; font-size:1.0em; width:25%;'><b>" + dateValue + "<\/b><\/div><div id='" + content + "' style='float:left; font-size:1.0em; width:65%;'>" + line + "<\/div>";
    new Insertion.Top(txt,line);        
    
    
    //img = "<img title='Print' id='print" + nId + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
    //new Insertion.Top(txt, img);

    img = "<img title='Maximize Display' alt='Maximize Display' id='xpImg" + nId + "' onclick='xpandView(event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/triangle_down.gif'>";
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

//this func fires only if maximize button is clicked after fullView
function xpandView(e) {
    var txt = Event.element(e).parentNode.id;   
    var img = Event.element(e).id; 
    var nId = txt.substr(1);
    var content = "c" + nId;
    var date = "d" + nId;    
    
    var imgTag = "<img id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
    
    
    Element.remove(img);
    Element.remove(date);
    Element.remove(content);
    
    $(txt).style.height = 'auto';    
    new Insertion.Top(txt, imgTag);    
    Event.stop(e);

}

function fetchNote(nId) {
    var url = ctx + "/CaseManagementView.do";
    var fullId = "full" + nId;
    var params = "method=viewNote&raw=true&noteId=" + nId;    
    var noteTxtArea = "caseNote_note" + nId;
    
    var ajax = new Ajax.Request (                    
                    url,
                    {
                        method: 'post',
                        postBody: params,
                        evalScripts: true,
                        onSuccess: function(response) {
                            $(noteTxtArea).update(response.responseText);
                            adjustCaseNote();                            
                                $(noteTxtArea).focus();
                            setCaretPosition($(noteTxtArea),$(noteTxtArea).value.length); 
                            origCaseNote = $F(noteTxtArea); 
                            $(fullId).value = "true";
                        }
                    }
               );
 
}

//this func fires only if maximize button is clicked
function fullView(e) {
    var url = ctx + "/CaseManagementView.do";
    var txt = Event.element(e).parentNode.id;   
    var img = Event.element(e).id; 
    var nId = txt.substr(1);
    var fullId = "full" + nId;
    var params = "method=viewNote&raw=false&noteId=" + nId;    
    var noteTxtId = "txt" + nId;
    var btnHtml = "<img title='Minimize Display' id='bottomQuitImg" + nId + "' alt='Minimize Display' onclick='minView(event)' style='float:right; margin-right:5px; margin-bottom:3px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
    
    var ajax = new Ajax.Request (                    
                    url,
                    {
                        method: 'post',
                        postBody: params,
                        evalScripts: true,
                        onSuccess: function(response) {
                            $(noteTxtId).update(response.responseText);
                            if( largeNote(response.responseText) ) {
                                new Insertion.After(noteTxtId,btnHtml);
                            }
                            $(fullId).value = "true";
                        }
                    }
               );
               
    var imgTag = "<img id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
    
    
    Element.remove(img);
    
    
    $(txt).style.height = 'auto';    
    new Insertion.Top(txt, imgTag);    
    Event.stop(e);

}

function resetEdit(e) {
    var txt = Event.element(e).id;
    var nId = txt.substr(1);

    var img = "<img id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
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
    var url = ctx + "/CaseManagementView.do";
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
                                            alert("Session Expired");
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
    var img = "<img id='quitImg" + nId + "' onclick='resetView(true, false, event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
    new Insertion.Top(txt,img);
    var lockForm = "<p id='passwdPara' class='passwd'>Password:&nbsp;<input onkeypress=\"return grabEnter('btnUnlock', event);\" type='password' id='" + passwd + "' size='16'>&nbsp;<input id='btnUnlock' type='button' onclick=\"return unlock_ajax('" + txt + "');\" value='Unlock'><\/p>";
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
    var isFull = "full" + nId;
    var txtId = "txt" + nId;  

    if( $F(isFull) == "true" ) {
        payload = $(txtId).innerHTML;    
        payload = payload.replace(/^\s+|\s+$/g,"");
        payload = payload.replace(/<br>/gi,"\n");
        payload += "\n";
    }
    else
        payload = "";
    Element.remove(txtId);                       
    caseNote = "caseNote_note" + nId;                
    
    var input = "<textarea tabindex='7' cols='84' rows='10' wrap='soft' class='txtArea' style='line-height:1.1em;' name='caseNote_note' id='" + caseNote + "'>" + payload + "<\/textarea>";                    
    new Insertion.Top(txt, input);                 
    var printimg = "<img title='Print' id='print" + nId + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";    
    new Insertion.Top(txt, printimg);

    if( $F(isFull) == "true" ) {    
        //position cursor at end of text  
        adjustCaseNote();
        setCaretPosition($(caseNote),$(caseNote).value.length); 
        $(caseNote).focus();
        origCaseNote = $F(caseNote); 
    }
    else
        fetchNote(nId);

    Element.observe(caseNote, 'keyup', monitorCaseNote);
    Element.observe(caseNote, 'click', getActiveText);
      
    if( passwordEnabled ) {
           input = "<p style='background-color:#CCCCFF; display:none; margin:0px;' id='notePasswd'>Password:&nbsp;<input type='password' name='caseNote.password'/><\/p>";
           new Insertion.Bottom(txt, input);
    }
                            
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
    var issueURL = ctx + "/CaseManagementEntry.do?method=issueList&demographicNo=${demographicNo}&providerNo=" + providerNo;
    issueAutoCompleter = new Ajax.Autocompleter("issueAutocomplete", "issueAutocompleteList", issueURL, {minChars: 4, indicator: 'busy', afterUpdateElement: saveIssueId, onShow: autoCompleteShowMenu, onHide: autoCompleteHideMenu});               
        
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
    //html = $(txt).innerHTML;
    //html = html.replace(/<span>|<\/span>|<pre>|<\/pre>/ig,"");
    //$(txt).innerHTML = html;
    $(txt).style.cursor = "pointer";
    
    Event.observe(txt, 'click', viewNote);
}

function viewNote(e) {    
    var txt = Event.element(e).id; 
    var html;            
    var img = "<img id='quitImg" + txt.substr(1) + "' onclick='collapseView(event)' style='float:right; cursor:pointer;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
        
    $(txt).style.height = "auto";
    //html = $(txt).innerHTML;
    //$(txt).innerHTML = "<pre>" + html + "<\/pre>";        
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
    
    document.forms["caseManagementViewForm"].method.value = "view";
     
    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];
    var caseMgtViewfrm = document.forms["caseManagementViewForm"];
    var url = ctx + "/CaseManagementEntry.do";
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
var strToday;
function validDate() {
    var strDate = $("observationDate").value;
    var day = strDate.substring(0,strDate.indexOf("-"));
    var mnth = strDate.substring(strDate.indexOf("-")+1, strDate.lastIndexOf("-"));
    var year = strDate.substring(strDate.lastIndexOf("-")+1, strDate.indexOf(" "));
    var time = strDate.substr(strDate.indexOf(" ")+1);
    var date = new Date( mnth + " " + day + ", " + year + " " + time);
    var today = new Date(strToday);    
    
    if( date <= today )
        return true;
    else
        return false;
}

function ajaxSaveNote(div,noteId,noteTxt) {
    if( $("observationDate") != undefined && $("observationDate").value.length > 0 && !validDate() ) {
        alert("Observation date must be in the past");
        return false;
    }

    if( caisiEnabled ) {
        if( !issueIsAssigned() ) {
            alert("At least one(1) issue must be assigned to note");
            return false;
        }
    }


    document.forms["caseManagementEntryForm"].method.value = 'ajaxsave';
    
    var idx = 0;
    var issue = "noteIssue" + idx;
    var issueParams = "";
    while($(issue) != null) {        
        issueParams += "&issue" + idx + "=" + $F(issue);
        ++idx;
        issue = "noteIssue" + idx;
    }            
    
    var demoNo = demographicNo;
    
    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];
    var url = ctx + "/CaseManagementEntry.do";
    var params = "method=ajaxsave&nId="+noteId+issueParams+"&demographicNo=" + demographicNo +"&providerNo=" + providerNo + "&numIssues="+idx+"&obsDate="+$F("observationDate")+"&noteTxt="+encodeURI(noteTxt);
    var objAjax = new Ajax.Updater (
                    {success:div},
                    url,
                    {
                        method: 'post',                        
                        evalScripts: true,                         
                        postBody: params,                       
                        onFailure: function(request) {
                            if( request.status == 403 )
                                alert("Session Expired");
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
    
    if( caisiEnabled ) {
        if( !issueIsAssigned() ) {
            alert("At least one(1) issue must be assigned to note");
            return false;
        }
    }
    document.forms["caseManagementEntryForm"].method.value = method;
    document.forms["caseManagementEntryForm"].ajax.value = false;
    document.forms["caseManagementEntryForm"].chain.value = "list";
    document.forms["caseManagementEntryForm"].includeIssue.value = "off";
    
    document.forms["caseManagementViewForm"].method.value = method;
     
    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];
    var frm = document.forms["caseManagementViewForm"];
    var url = ctx + "/CaseManagementView.do";
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
                                alert("Session Expired");
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
    var frm = document.forms["caseManagementEntryForm"];
    frm.method.value = method;    
    frm.ajax.value = true;
    
    var url = ctx + "/CaseManagementEntry.do";
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
var reason;
function newNote(e) {
    Event.stop(e);    
   
    ++newNoteCounter;
    var newNoteIdx = "0" + newNoteCounter;
    var id = "nc" + newNoteIdx;
    var sigId = "sig"+ newNoteIdx;
    var input = "<textarea tabindex='7' cols='84' rows='1' wrap='soft' class='txtArea' style='line-height:1.0em;' name='caseNote_note' id='caseNote_note" + newNoteIdx + "'>" + reason + "<\/textarea>";
    var passwd = "";
    if( passwordEnabled ) {
        passwd = "<p style='background-color:#CCCCFF; display:none; margin:0px;' id='notePasswd'>Password:&nbsp;<input type='password' name='caseNote.password'/><\/p>";
    }                

    var div = "<div id='" + id + "' class='newNote'><input type='hidden' id='signed" + newNoteIdx + "' value='false'><div id='n" + newNoteIdx + "'><input type='hidden' id='full" + newNoteIdx + "value='true'>" +
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
        var issueURL = "/CaseManagementEntry.do?method=issueList&demographicNo=" + demographicNo + "&providerNo=" + providerNo;
        issueAutoCompleter = new Ajax.Autocompleter("issueAutocomplete", "issueAutocompleteList", issueURL, {minChars: 4, indicator: 'busy', afterUpdateElement: saveIssueId, onShow: autoCompleteShowMenu, onHide: autoCompleteHideMenu});        

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

function deleteAutoSave() {
    var url = ctx + "/CaseManagementEntry.do";
    var frm = document.forms["caseManagementEntryForm"];
    frm.method.value = "cancel";
    
    new Ajax.Request( url, {
                                method: 'post',
                                postBody: Form.serialize(frm)
                           }
                    );
}

function autoSave(async) {    
    
    var url = ctx + "/CaseManagementEntry.do";
    var programId = case_program_id;
    var demoNo = demographicNo;
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
    var rnd = Math.round(Math.random() * 1000);
    win = "win" + rnd;
    var url = ctx + "/CaseManagementEntry.do?method=notehistory&noteId=" + noteId;    
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
        var selected = ctx + "/oscarEncounter/graphics/printerGreen.png";
        var unselected = ctx + "/oscarEncounter/graphics/printer.png";
        
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
        var selected = ctx + "/oscarEncounter/graphics/printerGreen.png";
        var unselected = ctx + "/oscarEncounter/graphics/printer.png";
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
            $(imgId).src = selected;
            if( $F("notes2print").length > 0 ) 
                $("notes2print").value += "," + noteId;
            else
               $("notes2print").value = noteId;         
        }
                
        return false;
    }    
    
    var imgPrintgreen = new Image();
    function addPrintQueue(noteId) {    
        var imgId = "print" + noteId;
                
        //$(imgId).src = ctx + "/oscarEncounter/graphics/printerGreen.png"; //imgPrintgreen.src;
        $(imgId).src = imgPrintgreen.src;
        if( $F("notes2print").length > 0 ) 
            $("notes2print").value += "," + noteId;
        else
           $("notes2print").value = noteId;
           
    }
    
    function removePrintQueue(noteId, idx) {
        var unselected = ctx + "/oscarEncounter/graphics/printer.png";
        var imgId = "print" + noteId;
        var tmp = "";
        var idx2;
        
        $(imgId).src = unselected; //imgPrintgrey.src;
            
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
    
    function printDateRange() {
        var sdate = $F("printStartDate");
        var edate = $F("printEndDate");
        if( sdate.length == 0 || edate.length == 0 ) {
            alert("Both start date and end date must be specified");
            return false;
        }

        var tmp = sdate.split("-");
        var formatdate = tmp[1] + " " + tmp[0] + ", " + tmp[2];
        var msbeg = Date.parse(formatdate);

        tmp = edate.split("-");
        formatdate = tmp[1] + " " + tmp[0] + ", " + tmp[2];
        var msend = Date.parse(formatdate);

        if( msbeg > msend ) {
            alert("Beginning date must precede end date");
            return false;
        }  
        
        //cycle through container divs for each note
        var idx;
        var noteId;
        var notesDiv;
        var noteDate = null;
        var msnote;
        var pos;
        
        for( idx = 0; idx < numNotes; ++idx ) {
            notesDiv = $("nc" + idx).down('div');
            noteId = notesDiv.id.substr(1);  //get note id                       
            
            if( $("obs"+noteId) != null ) 
                noteDate = $("obs"+noteId).innerHTML;
            else if( $("observationDate") != null )
                noteDate = $F("observationDate");

            //trim leading and trailing whitespace from date
            noteDate = noteDate.replace(/^\s+|\s+$/g,"");            

            if( noteDate != null ) {
                //grab date and splice off time and format for js date object                
                noteDate = noteDate.substr(0,noteDate.indexOf(" "));
                tmp = noteDate.split("-");
                formatdate = tmp[1] + " " + tmp[0] + ", " + tmp[2];                
                msnote = Date.parse(formatdate);
                pos = noteIsQeued(noteId);
                if( msnote >= msbeg && msnote <= msend ) {                   
                    if( pos == -1 )
                        addPrintQueue(noteId);
                }
                else if( pos >= 0 ) {
                    removePrintQueue(noteId, pos);
                }
            }
        }
        
        return true;
    }
    
    function printSetup(e) {
        if( $F("notes2print").length > 0 ) 
            $("printopSelected").checked = true;
        else
            $("printopAll").checked = true;

        $("printOps").style.right = (pageWidth() - Event.pointerX(e)) + "px";
        $("printOps").style.bottom = (pageHeight() - Event.pointerY(e)) + "px";
        $("printOps").style.display = "block";
        return false;
    }
    
    function printNotes() {
        if( $("printopDates").checked && !printDateRange()) {            
            return false;
        }
        else if( $("printopAll").checked )
            printAll();        
            
        if( $F("notes2print").length == 0 && $F("printCPP") == "false" && $F("printRx") == "false" ) {
            alert("Nothing selected to print");
            return false;
        }
        
        var url = ctx + "/CaseManagementEntry.do";
        var frm = document.forms["caseManagementEntryForm"];

        frm.method.value = "print";
        frm.submit();

        
        return false;
    }
    
    //print today's notes
    function printToday(e) {
        clearAll(e);
        
        var today = $F("serverDate").split(" ");        
        $("printStartDate").value = today[1].substr(0,today[1].indexOf(",")) + "-" + today[0] + "-" + today[2];
        $("printEndDate").value = $F("printStartDate");
        $("printopDates").checked = true;
        
        printNotes();

    }
    
    function clearAll(e) {        
        var idx;
        var noteId;
        var notesDiv;
        var pos;
        var imgId;
        
       Event.stop(e);
        
        //cycle through container divs for each note
        for( idx = 0; idx < numNotes; ++idx ) {
            notesDiv = $("nc" + idx).down('div');
            noteId = notesDiv.id.substr(1);  //get note id
            imgId = "print"+noteId;
            
            //if print img present, add note to print queue if not already there            
            if( $(imgId) != null ) {
                pos = noteIsQeued(noteId);
                if( pos >= 0 )
                    removePrintQueue(noteId, pos);                                      
            }            
        }
        
        if( $F("printCPP") == "true" )
            printInfo("imgPrintCPP","printCPP");
        
        if( $F("printRx") == "true" )
            printInfo("imgPrintRx","printRx");
            
        return false;
            
    }
    
    function printAll() {        
        var idx;
        var noteId;
        var notesDiv;
        var pos;
        
        //$("notes2print").value = "";
        
        //cycle through container divs for each note
        for( idx = 0; idx < numNotes; ++idx ) {
            notesDiv = $("nc" + idx).down('div');
            noteId = notesDiv.id.substr(1);  //get note id
            
            //if print img present, add note to print queue if not already there
            if( $("print"+noteId) != null ) {
                pos = noteIsQeued(noteId);
                if( pos == -1 )
                    addPrintQueue(noteId);                                        
            }            
        }                
    }
    
    function noPrivs(e) {
        Event.stop(e);
        alert("This note has not been signed by owner.  Select new note to add a note.");
    }