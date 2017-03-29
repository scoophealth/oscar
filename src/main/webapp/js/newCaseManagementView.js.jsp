<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@page contentType="text/javascript"%>
<%@page import="org.oscarehr.casemgmt.common.Colour"%>

	var numNotes = 0;   //How many saved notes do we have?
    var ctx;        //url context
    var providerNo;
    var demographicNo;
    var case_program_id;
    var caisiEnabled = false;
    var passwordEnabled = false;
    var requireIssue = true;
    var requireObsDate = true;
    var makeIssue;
   	var defaultDiv;
   	var changeIssueFunc;
   	var addIssueFunc;
   	var needToReleaseLock = true;

       var X       = 10;
    var small   = 60;
    var normal  = 166;
    var medium  = 272;
    var large   = 378;
    var full    = 649;

    var itemColours = new Object();
        var autoCompleted = new Object();
        var autoCompList = new Array();
        var measurementWindows = new Array();
        var openWindows = new Object();
        var origCaseNote = "";
        var origObservationDate = "";        
        var calendar;
		var reloadWindows = new Object();
		var updateDivTimer = null;
		var reloadDivUrl;
		var reloadDiv;
		
		function checkLengthofObject(o) {
			var c = 0;
			for( var attr in o ) {
				if( o.hasOwnProperty(attr) ) {
					++c;
				}
			}
			
			return c;
		
		}
		
        function popupPage(vheight,vwidth,name,varpage) { //open a new popup window
		  if (varpage == null || varpage == -1) {
		  	return false;
		  }
          if( varpage.indexOf("..") == 0 ) {
            varpage = ctx + varpage.substr(2);
          }
          var page = "" + varpage;
          windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
                //var popup =window.open(page, "<bean:message key="oscarEncounter.Index.popupPageWindow"/>", windowprops);
                openWindows[name] = window.open(page, name, windowprops);

                if (openWindows[name] != null) {
                    if (openWindows[name].opener == null) {
                        openWindows[name].opener = self;
                    }
                    openWindows[name].focus();
                    if( updateDivTimer == null ) {
                    	updateDivTimer = setInterval(
                    		function() {
			
								if( checkLengthofObject(openWindows) > 0 ) {
									for( var name in openWindows ) {
										if( openWindows[name].closed && reloadWindows[name] != undefined ) {
											reloadDivUrl = reloadWindows[name];
											reloadDiv = reloadWindows[name+"div"];
											
											loadDiv(reloadDiv,reloadDivUrl,0);

											delete reloadWindows[name];
											var divName = name + "div";
											delete reloadWindows[divName];
											delete openWindows[name];
										}
																	
									}
			
								}
								if( checkLengthofObject(openWindows) == 0 ) {
									clearInterval(updateDivTimer);
									updateDivTimer = null;
								}
		
							},1000);
                    } 
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

        var okToClose = false;

            function onClosing() {
                 for( var idx = 0; idx < measurementWindows.length; ++idx ) {
                     if( !measurementWindows[idx].closed )
                         measurementWindows[idx].parentChanged = true;
                 }
            
                if( needToReleaseLock ) {								
				//release lock on note
				var url = ctx + "/CaseManagementEntry.do";
				var nId = document.forms['caseManagementEntryForm'].noteId.value;
				var params = "method=releaseNoteLock&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&noteId=" + nId;
				new Ajax.Request (
					url,
					{
						method: 'post',
						postBody: params,
						asynchronous: false						
					}
				);				
                }
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
   var obj = {};
   function listDisplay(Id, threshold) {
            if( threshold == 0 )
                return;

            var saveThreshold = Id + "threshold";
            if( $(saveThreshold) != null )
                $(saveThreshold).value = threshold;

            var listId = Id + "list";
            var list = $(listId);
            var items = list.getElementsByTagName('li');
            items = $A(items);

            var topName = "img"+Id+"0";
            var midName = "img"+Id+(threshold-1);
            var lastName = "img"+Id+(items.length-1);
            var topImage = $(topName);
            var midImage = $(midName);
            var lastImage = $(lastName);
            var expand;
            var expandPath = ctx + "/oscarEncounter/graphics/expand.gif";
            var collapsePath = ctx + "/oscarMessenger/img/collapse.gif";
            var transparentPath = ctx + "/images/clear.gif";

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
        Event.stop(e);
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

    Rounded("div.noteRounded","all","transparent","#CCCCCC","big border #000000");

    //need to set focus after rounded is called
    adjustCaseNote();
    setCaretPosition($(caseNote), $(caseNote).value.length);

    $(caseNote).focus();
}

function setupOneNote(note) {
	if (!NiftyCheck())
		return;

	Rounded("div#nc" + note, "all", "transparent", "#CCCCCC", "big border #000000");
}

var minDelta =  0.93;
var minMain;
var minWin;
function monitorNavBars(e) {
    var win = pageWidth();
    var main = Element.getWidth("body");

    if( e == null ) {
        minMain = Math.round(main * minDelta);
        minWin = Math.round(win * minDelta);
    }

    if( main < minMain ) {
        $("body").style.width = minMain + "px";
    }
    else if( win >= minWin &&  main == minMain ) {
        $("body").style.width = "100%";
    }

}

function scrollDownInnerBar() {
	$("encMainDiv").scrollTop= $("encMainDiv").scrollHeight;
}

function popperup(vheight,vwidth,varpage,pageName) { //open a new popup window
     		var page = varpage;
     		windowprops = "height="+vheight+",width="+vwidth+",status=yes,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=100,left=100";
     		var popup=window.open(varpage, pageName, windowprops);
     		popup.pastewin = opener;
     		popup.focus();
}

var fullChart = "false";
function viewFullChart(displayFullChart) {

	var url = ctx + "/CaseManagementEntry.do";
	var params = assembleMainChartParams(displayFullChart);

	if( displayFullChart ) {
		fullChart = "true";
	}
	else {
		fullChart = "false";
	}

	$("notCPP").update("Loading...");
	var objAjax = new Ajax.Request (
                            url,
                            {
                                method: 'post',
                                postBody: params,
                                evalScripts: true,
                                onSuccess: function(request) {
                                                $("notCPP").update(request.responseText);
												$("notCPP").style.height = "50%";
												if( displayFullChart ) {
													$("quickChart").innerHTML = quickChartMsg;
													$("quickChart").onclick = function() {return viewFullChart(false);}
													scrollDownInnerBar();

												}
												else {
													$("quickChart").innerHTML = fullChartMsg;
													$("quickChart").onclick = function() {return viewFullChart(true);}
													scrollDownInnerBar();
												}
                                           },
                                onFailure: function(request) {
                                                $("notCPP").update("Error: " + request.status + request.responseText);
                                            }
                            }

                      );
	return false;
}
/*
 *Draw the cpp views
 */
var socHistoryLabel;
var medHistoryLabel;
var onGoingLabel;
var remindersLabel;
var oMedsLabel;
var famHistoryLabel;
var riskFactorsLabel;

/*
	Loads issue notes: Social History, Medical History, Ongoing Concerns, Reminders
*/
function showIssueNotes() {
/*
    var issueNoteUrls = {
        divR1I1:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=SocHistory&title=" + socHistoryLabel + "&cmd=divR1I1",
        divR1I2:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=MedHistory&title=" + medHistoryLabel + "&cmd=divR1I2",
        divR2I1:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=Concerns&title=" + onGoingLabel + "&cmd=divR2I1",
        divR2I2:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=Reminders&title=" + remindersLabel + "&cmd=divR2I2"
    };
*/
    var limit = 5;

    for( idx in issueNoteUrls ) {
        loadDiv(idx,issueNoteUrls[idx],limit);
    }
}

var notesOffset = 0;
var notesIncrement = 20;
var notesRetrieveOk = false;
var notesCurrentTop = null;
var notesScrollCheckInterval = null;

function notesIncrementAndLoadMore() {
	if (notesRetrieveOk && $("encMainDiv").scrollTop == 0) {				
		if($("encMainDiv").scrollHeight > $("encMainDiv").getHeight()) {	
			notesOffset += notesIncrement;
			notesRetrieveOk = false;
			notesCurrentTop = $("encMainDiv").children[0].id;
			notesLoader(notesOffset, notesIncrement, demographicNo);
		}
	}
}

/**

	Responsible for loading notes on the eChart
	@param {offset} 
		Offset from the beginning of the notes
	@param {numToReturn}
		Number of notes to load
	@param {demoNo}
		Demographic number to loads notes for

*/
function notesLoader(offset, numToReturn, demoNo) {
	$("notesLoading").style.display = "inline";
	var params = "method=viewNotesOpt&offset=" + offset + "&numToReturn=" + numToReturn + "&demographicNo=" + demoNo;
	var params2 = jQuery("input[name='filter_providers'],input[name='filter_roles'],input[name='issues'],input[name='note_sort']").serialize();
	if(params2.length>0)
		params = params + "&" + params2;
	new Ajax.Updater("encMainDiv",
			ctx + "/CaseManagementView.do",
			{
				method: 'post',
				postBody: params,
				evalScripts: true,
				insertion: Insertion.Top,
				onSuccess: function(data) {
					notesRetrieveOk = (data.responseText.replace(/\s+/g, '').length > 0);
					if (!notesRetrieveOk) clearInterval(scrollCheckInterval);
				},
				onComplete: function() {
					$("notesLoading").style.display = "none";
					if (notesCurrentTop != null) $(notesCurrentTop).scrollIntoView();
				}
			});
}

function navBarLoader() {


   $("leftNavBar").style.height = "660px";
   $("rightNavBar").style.height = "660px";


    this.maxRightNumLines = Math.floor($("rightNavBar").getHeight() / 14);
    this.maxLeftNumLines = Math.floor($("leftNavBar").getHeight() / 14);
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

            var leftNavBar = [
                  ctx + "/oscarEncounter/displayPrevention.do?hC=" + Colour.prevention,
                  ctx + "/oscarEncounter/displayTickler.do?hC=" + Colour.tickler,
                  ctx + "/oscarEncounter/displayDisease.do?hC=" + Colour.disease,
                  ctx + "/oscarEncounter/displayForms.do?hC=" + Colour.forms,
                  ctx + "/oscarEncounter/displayEForms.do?hC=" + Colour.eForms,
                  ctx + "/oscarEncounter/displayDocuments.do?hC=" + Colour.documents,
                  ctx + "/oscarEncounter/displayLabs.do?hC=" + Colour.labs,
                  ctx + "/oscarEncounter/displayMessages.do?hC=" + Colour.messages,
                  ctx + "/oscarEncounter/displayMeasurements.do?hC=" + Colour.measurements,
                  ctx + "/oscarEncounter/displayConsultation.do?hC=" + Colour.consultation,
                  ctx + "/oscarEncounter/displayHRM.do?hC=",
                  ctx + "/oscarEncounter/displayMyOscar.do?hC=",
                  ctx + "/eaaps/displayEctEaaps.do?hC="
              ];

            var leftNavBarTitles = [ "preventions", "tickler", "Dx", "forms", "eforms", "docs","labs", "msgs", "measurements", "consultation","HRM","PHR", "eaaps"];

            var rightNavBar = [
                  ctx + "/oscarEncounter/displayAllergy.do?hC=" + Colour.allergy,
                  ctx + "/oscarEncounter/displayRx.do?hC=" + Colour.rx + "&numToDisplay=12",
                  ctx + "/CaseManagementView.do?hc=" + Colour.omed + "&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=OMeds&title=" + oMedsLabel + "&cmd=OMeds" + "&appointment_no="+appointmentNo,
                  ctx + "/CaseManagementView.do?hc=" + Colour.riskFactors + "&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=RiskFactors&title=" + riskFactorsLabel + "&cmd=RiskFactors"+ "&appointment_no="+appointmentNo,
                  ctx + "/CaseManagementView.do?hc=" + Colour.familyHistory + "&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=FamHistory&title=" + famHistoryLabel + "&cmd=FamHistory"+ "&appointment_no="+appointmentNo,
                  ctx + "/oscarEncounter/displayIssues.do?hC=" + Colour.unresolvedIssues,
                  ctx + "/oscarEncounter/displayResolvedIssues.do?hC=" + Colour.resolvedIssues,
                  ctx + "/oscarEncounter/displayDecisionSupportAlerts.do?providerNo=" + providerNo + "&demographicNo=" + demographicNo,                                       
                  ctx + "/oscarEncounter/displayEpisodes.do?hC=" + Colour.episode,
                  ctx + "/oscarEncounter/displayPregnancies.do?hC="+ Colour.episode,
                  ctx + "/oscarEncounter/displayContacts.do?hC=" + Colour.contacts
              ];

            var rightNavBarTitles = [ "allergies", "Rx", "OMeds", "RiskFactors", "FamHistory", "unresolvedIssues", "resolvedIssues", "Guidelines","episode","pregnancy","contacts"];


          var navbar = "leftNavBar";
          for( var idx = 0; idx < leftNavBar.length; ++idx ) {
                var div = document.createElement("div");
                div.className = "leftBox";
                div.style.display = "block";
                div.style.visiblity = "hidden";
                div.id = leftNavBarTitles[idx];
                $(navbar).appendChild(div);
                this.arrLeftDivs.push(div);

                this.popColumn(leftNavBar[idx],leftNavBarTitles[idx],leftNavBarTitles[idx], navbar, this);

          }

          navbar = "rightNavBar";
          for( var idx = 0; idx < rightNavBar.length; ++idx ) {
                var div = document.createElement("div");
                div.className = "leftBox";
                div.style.display = "block";
                div.id = rightNavBarTitles[idx];
                $(navbar).appendChild(div);
                this.arrRightDivs.push(div);

                this.popColumn(rightNavBar[idx],rightNavBarTitles[idx],rightNavBarTitles[idx], navbar, this);

          }



          /*var URLs = new Array();
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
                div.style.visiblity = "hidden";
                div.id = idx;
                $(navbar).appendChild(div);

                if( navbar == "leftNavBar" )
                    this.arrLeftDivs.push(div);
                if( navbar == "rightNavBar" )
                    this.arrRightDivs.push(div);

                this.popColumn(URLs[j][idx],idx,idx, navbar, this);
            }

        }*/


    };

    //update each ajax div with info from request
    this.popColumn = function (url,div,params, navBar, navBarObj) {
        params = "reloadURL=" + url + "&numToDisplay=6&cmd=" + params;

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
                                                //while( $(div).firstChild )
                                                //    $(div).removeChild($(div).firstChild);
                                                //alert("success " + div);
                                                $(div).update(request.responseText);

                                                if( $("leftColLoader") != null )
                                                    Element.remove("leftColLoader");

                                                if( $("rightColLoader") != null )
                                                    Element.remove("rightColLoader");


                                                //track ajax completions and display divs when last ajax call completes
                                                //navBarObj.display(navBar,div);
                                                notifyDivLoaded($(div).id);
                                           },
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>" + div + "</h3>Error: " + request.status;
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
                divs[idx].style.visibility = "visible";
            }
        };

}

function showIntegratedNote(title, note, location, providerName, obsDate){
	$("integratedNoteTitle").innerHTML = title;
	$("integratedNoteDetails").innerHTML = "Integrated Facility:" + location + " by " + providerName + " on " + obsDate;
	
	$("integratedNoteTxt").value = note;
	
	var coords = null;
    if(document.getElementById("measurements_div") == null) {
    	coords = Position.page($("topContent"));
    } else {
   		coords = Position.positionedOffset($("cppBoxes"));
    }

    var top = Math.max(coords[1], 0);
    var right = Math.round(coords[0]/0.66);

	$("showIntegratedNote").style.right = right + "px";
    $("showIntegratedNote").style.top = top + "px";
    
    $("channel").style.visibility = "hidden";
    $("showEditNote").style.display = "none";
    
	$("showIntegratedNote").style.display = "block";
	
	$("integratedNoteTxt").focus();
}

//display in place editor
function showEdit(e,title, noteId, editors, date, revision, note, url, containerDiv, reloadUrl, noteIssues, noteExts, demoNo) {
    //Event.extend(e);
    //e.stop();

    var limit = containerDiv + "threshold";
    var editElem = "showEditNote";
    var pgHeight = pageHeight();

    var coords = null;
    if(document.getElementById("measurements_div") == null) {
    	 coords = Position.page($("topContent"));
    } else {
   		coords = Position.positionedOffset($("cppBoxes"));
    }

    var top = Math.max(coords[1], 0);
    var right = Math.round(coords[0]/0.66);
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

    var noteIssueUl = "<ul id='issueIdList' style='list-style: none outside none; margin:0px;'>";

    if( noteIssues.length > 0 ) {
        var issueArray = noteIssues.split(";");
        var idx,rows;
	var cppDisplay = "";
        for( idx = 0,rows=0; idx < issueArray.length; idx+=3, ++rows ) {
            if( rows % 2 == 0 )
                noteIssueUl += "<li><input type='checkbox' id='issueId' name='issue_id' checked value='" + issueArray[idx] + "'>" + issueArray[idx+2];
            else
                noteIssueUl += "&nbsp; <input type='checkbox' id='issueId' name='issue_id' checked value='" + issueArray[idx] + "'>" + issueArray[idx+2] + "</li>";

	    if (cppDisplay=="") cppDisplay = getCPP(issueArray[idx+1]);
        }

        if( rows % 2 == 0 )
            noteIssueUl += "</li>";
    }
    noteIssueUl += "</ul>";

    var noteInfo = "<div style='float:right;'><i>Encounter Date:&nbsp;" + date + "&nbsp;rev<a href='#' onclick='return showHistory(\"" + noteId + "\",event);'>"  + revision + "</a></i></div>" +
                    "<div><span style='float:left;'>Editors: </span>" + editorUl + noteIssueUl + "</div><br style='clear:both;'>";

    $("issueNoteInfo").update(noteInfo);
    $("frmIssueNotes").action = url;
    $("reloadUrl").value = reloadUrl;
    $("containerDiv").value = containerDiv;
    $("winTitle").update(title);

    $(editElem).style.right = right + "px";
    $(editElem).style.top = top + "px";
    $("showIntegratedNote").style.display = "none";
    if( Prototype.Browser.IE ) {
        //IE6 bug of showing select box
        $("channel").style.visibility = "hidden";
        $(editElem).style.display = "block";
    }
    else
        $(editElem).style.display = "table";

    //Prepare Annotation Window & Extra Fields
    var now = new Date();
    document.getElementById('annotation_attrib').value = "anno"+now.getTime();
    var obj={};
    Element.observe('anno','click', openAnnotation.bindAsEventListener(obj,noteId,cppDisplay,demoNo));
    prepareExtraFields(cppDisplay,noteExts);

    //Set note position order
    var elementNum = containerDiv + "num";
    var numNotes = $F(elementNum);
    var positionElement = containerDiv + noteId;
    var position;
    if( noteId == "" ) {
        position = 0;
    }
    else {
        position = $F(positionElement);
    }

    var curElem;
    var numOptions = $("position").length;
    var max = numNotes > numOptions ? numNotes : numOptions;
    var optId;
    var option;
    var opttxt;

    for( curElem = 0; curElem < max; ++curElem ) {

        optId = "popt" + curElem;
        if( $(optId) == null ) {
            option = document.createElement("OPTION");
            option.id = optId;
            opttxt = curElem + 1;
            option.text = "" + opttxt;
            option.value = curElem;
            $("position").options.add(option,curElem);
        }

        if( position == curElem ) {
            $(optId).selected = true;
        }
    }

    if( max == numNotes ) {
        optId = "popt" + max;
        if( $(optId) == null ) {
            option = document.createElement("OPTION");
            option.id = optId;
            opttxt = 1 * max + 1;
            option.text = "" + opttxt;
            option.value = max;
            $("position").options.add(option,max);
        }

    }

    for( curElem = max - 1; curElem > 0; --curElem ) {

        optId = "popt" + curElem;
        if( curElem > numNotes ) {
            Element.remove(optId);
        }
    }


    $("noteEditTxt").focus();

    return false;
}

var cppIssues = new Array(7);
var cppNames = new Array(7);
cppIssues[0] = "SocHistory";
cppIssues[1] = "MedHistory";
cppIssues[2] = "FamHistory";
cppIssues[3] = "Concerns";
cppIssues[4] = "RiskFactors";
cppIssues[5] = "Reminders";
cppIssues[6] = "OMeds";
cppNames[0] = "Social History";
cppNames[1] = "Medical History";
cppNames[2] = "Family History";
cppNames[3] = "Ongoing Concerns";
cppNames[4] = "Risk Factors";
cppNames[5] = "Reminders";
cppNames[6] = "Other Meds";

function getCPP(issueCode) {
    for (var i=0; i<cppIssues.length; i++) {
	if (issueCode==cppIssues[i]) {
	    return cppNames[i];
	}
    }
    return "";
}

var exFields = new Array(11);
var exKeys = new Array(11);
exFields[0] = "startdate";
exFields[1] = "resolutiondate";
exFields[2] = "proceduredate";
exFields[3] = "ageatonset";
exFields[4] = "treatment";
exFields[5] = "problemstatus";
exFields[6] = "exposuredetail";
exFields[7] = "relationship";
exFields[8] = "lifestage";
exFields[9] = "hidecpp";
exFields[10] = "problemdescription";
exKeys[0] = "Start Date";
exKeys[1] = "Resolution Date";
exKeys[2] = "Procedure Date";
exKeys[3] = "Age at Onset";
exKeys[4] = "Treatment";
exKeys[5] = "Problem Status";
exKeys[6] = "Exposure Details";
exKeys[7] = "Relationship";
exKeys[8] = "Life Stage";
exKeys[9] = "Hide Cpp";
exKeys[10] = "Problem Description";

function prepareExtraFields(cpp,exts) {
	//commented out..this causes a problem in Firefox
	//console.log("prepare Extra Fields");
    var rowIDs = new Array(10);
    for (var i=2; i<exFields.length; i++) {
	rowIDs[i] = "Item"+exFields[i];
	$(rowIDs[i]).hide();
    }
    if (cpp==cppNames[1]) $(rowIDs[2],rowIDs[4],rowIDs[8],rowIDs[9]).invoke("show");
    if (cpp==cppNames[2]) $(rowIDs[3],rowIDs[4],rowIDs[7],rowIDs[8],rowIDs[9]).invoke("show");
    if (cpp==cppNames[3]) $(rowIDs[5],rowIDs[8],rowIDs[9],rowIDs[10]).invoke("show");
    if (cpp==cppNames[4]) $(rowIDs[3],rowIDs[6],rowIDs[8],rowIDs[9]).invoke("show");

    for (var i=0; i<exFields.length; i++) {
	$(exFields[i]).value = "";
    }

    var extsArr = exts.split(";");
    for (var i=0; i<extsArr.length; i+=2) {
    	for (var j=0; j<exFields.length; j++) {
			if (extsArr[i]==exKeys[j]) {
				$(exFields[j]).value = extsArr[i+1];
				continue;
	    	}
		}
    }
}

function openAnnotation() {
    var atbname = document.getElementById('annotation_attrib').value;
    var data = $A(arguments);
    var addr = ctx+"/annotation/annotation.jsp?atbname="+atbname+"&table_id="+data[1]+"&display="+data[2]+"&demo="+data[3];
    window.open(addr, "anwin", "width=400,height=500");
    Event.stop(data[0]);
}

function updateCPPNote() {
   var url = $("frmIssueNotes").action;
   var reloadUrl = $("reloadUrl").value;
   var div = $("containerDiv").value;

   $('channel').style.visibility ='visible';
   $('showEditNote').style.display='none';

   var curItems = document.forms["frmIssueNotes"].elements["issueId"];
   if( typeof curItems.length != "undefined" ) {
        size = curItems.length;

       for( var idx = 0; idx < size; ++idx ) {
            if( !curItems[idx].checked ) {
                $("issueChange").value = true;
                break;
            }
       }
   }
   else {
        $("issueChange").value = true;
   }

   var params = $("frmIssueNotes").serialize();
   var sigId = "sig" + caseNote.substr(13);
   var objAjax = new Ajax.Request (
                          url,
                            {
                                method: 'post',
                                evalScripts: true,
                                postBody: params,
                                onSuccess: function(request) {
                                                if( request.responseText.length > 0 ) {
                                                    $(div).update(request.responseText);
												}
                                                 if( $("issueChange").value == "true" ) {
                                                 	  ajaxUpdateIssues("edit",sigId);
                                                      $("issueChange").value = false;
                                                 }

												notifyDivLoaded($(div).id);
                                           },
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>" + div + "<\/h3>Error: " + request.status;
                                            }
                            }

                      );
    return false;

}

function clickLoadDiv(e) {
    var data = $A(arguments);
    Event.stop(e);
    data.shift();
    loadDiv(data[0],data[1],0);
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
												notifyDivLoaded($(div).id);

                                           },
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>" + div + "<\/h3>Error: " + request.status + "<br>" + request.responseText;
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
		if(inpu.value.trim().length == 0) {
			inpu.value=inpu.value.trim();
		}
                var ev;
                try {
                    ev = document.createEvent('KeyEvents');
                    ev.initKeyEvent('keypress', true, true, window,false, false, false, false, 0,0);
                }
                catch(e) {
                    ev = document.createEvent("UIEvents");
                    ev.initEvent("keypress",true,true);
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

        //if( $(caseNote).value.length > 0 )
            //$(caseNote).value += "\n\n";

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

     var insertTemplateError;
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
                                            alert(insertTemplateError);
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

function removeLock(id) {
	var regEx = /\d+/;
    var nId = regEx.exec(id);
	var url = ctx + "/CaseManagementEntry.do";
	params = "method=releaseNoteLock&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&noteId=" + nId + "&force=true";
	
	new Ajax.Request(
		url,
		{
			method: 'post',
			postBody: params,
			asynchronous: true
		}
	);	
}


var updatedNoteId = -1;  //used to store id of ajax saved note used below
var selectBoxes = new Object();
var unsavedNoteWarning;
var editLabel;
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
        if( !confirm(unsavedNoteWarning))
            return false;
        else {
       	// Prevent saving of note if the current note isn't properly assigned to a program and role. (note_program_ui_enabled = true)
            if ((typeof jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val() != "undefined") &&
        			(typeof jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val() != "undefined")) {
        		if (jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val().trim().length == 0 ||
        				jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val().trim().length == 0) {
        			// For weird cases where the role id or program number is missing.
        			_missingRoleProgramIdError();
        			return false;
        		} else if (jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val() == "-2" ||
        				jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val() == "-2") {
        			// For the case where you're trying to save a note with no available programs or roles
        			_noVisibleProgramsError();
        			return false;
        		}
        	}        
            saving = true;
            if( ajaxSaveNote(sig,nId,tmp) == false)
                return false;
        }
   }

	//remove lock from note
	removeLock(id);


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

    if( $("notePasswd") != null ) {
        Element.remove("notePasswd");
    }

    Element.stopObserving(id, 'keyup', monitorCaseNote);
    Element.stopObserving(id, 'click', getActiveText);

    Element.remove(id);

    //remove observation date input text box but preserve date if there is one
    if( !saving && $("observationDate") != null ) {
        var observationDate = $("observationDate").value;

		new Insertion.After("observationDate", " <span id='obs" + nId + "'>" + observationDate + "</span>");
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

	if( $("noteIssues-resolved") != null )
		Element.remove("noteIssues-resolved");
		
	if( $("noteIssues-unresolved") != null )
		Element.remove("noteIssues-unresolved");
		
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

    tmp = tmp.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
    tmp = tmp.replace(/\n/g,"<br>");

    if( !saving ) {
        if( largeNote(tmp) ) {
            var btmImg = "<img title='Minimize Display' id='bottomQuitImg" + nId + "' alt='Minimize Display' onclick='minView(event)' style='float:right; margin-right:5px; margin-bottom:3px; ' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
            new Insertion.Before(sig, btmImg);
        }

        //$(txt).style.fontSize = normalFont;

        //if we're not restoring a new note display print img
        //if( nId.substr(0,1) != "0" ) {
        //    img = "<img title='Print' id='print" + nId + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
        //     new Insertion.Top(parent, img);
       // }

        var printImg = "print" + nId;
        var img = "<img title='Minimize' id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
        var printimg = "<img title='Print' id='" + printImg + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
        var input = "<div id='txt" + nId + "'>" + tmp + "<\/div>";

        var func;
        var editWarn = "editWarn" + nId;
        if( $F(editWarn) == "true" ) {
            func = "noPrivs(event);";
        }
        else {
            func = "editNote(event);";
        }

        var editAnchor = "<a title='Edit' id='edit"+ nId + "' href='#' onclick='" + func + " return false;' style='float: right; margin-right: 5px; font-size:8px;'>" + editLabel + "</a>";
        var editAnchor = "<a title='Edit' id='edit"+ nId + "' href='#' onclick='" + func + " return false;' style='float: right; margin-right: 5px; font-size:8px;'>" + editLabel + "</a>";
        var editId = "edit" + nId;

        var attribName = "anno" + (new Date().getTime());
        var attribAnchor = "<input id='anno" + nId + "' height='10px;' width='10px' type='image' src='" + ctx + "/oscarEncounter/graphics/annotation.png' title='" + annotationLabel + "' style='float: right; margin-right: 5px; margin-bottom: 3px;'" +
        	"onclick=\"window.open('" + ctx + "/annotation/annotation.jsp?atbname=" + attribName + "&table_id=" + nId + "&display=EChartNote&demo=" + demographicNo + "','anwin','width=400,height=500');$('annotation_attribname').value='" + attribName + "'; return false;\">";

        new Insertion.Top(parent, editAnchor);
        new Insertion.After(editId, input);
        

         if( nId.substr(0,1) != "0" ) {
            Element.remove(printImg);
            new Insertion.Before(editId, printimg);
            new Insertion.After(editId, attribAnchor);
            new Insertion.Top(parent, img);
        }

        new Insertion.Top(parent, img);

        $(parent).style.height = "auto";

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
    var img = "<img title='Minimize' id='" + imgId + "' onclick='minView(event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'/>";
    var printimg = "<img title='Print' id='" + printId + "' alt='Toggle Print Note' onclick='togglePrint(" + newId + ", event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
    if( $(printId) != null ) {
        Element.remove(printId);
    }

    var func;
    var editWarn = "editWarn" + newId;
    if( $F(editWarn) == "true" ) {
        func = "noPrivs(event);";
    }
    else {
        func = "editNote(event);";
    }

    var anchor = "<a title='Edit' id='edit"+ newId + "' href='#' onclick='" + func + " return false;' style='float: right; margin-right: 5px; font-size:8px;'>" + editLabel + "</a>";

    new Insertion.Top(parent, input);
    new Insertion.Top(parent, anchor);
    new Insertion.Top(parent, printimg);
    new Insertion.Top(parent, img);

    $(parent).style.height = "auto";

}

function minView(e) {
    var divHeight = "1.1em";
    var txt = Event.element(e).parentNode.id;
   //alert(txt);
    var nId = txt.substr(1);
    var img = Event.element(e).id;
    var dateId = "obs" + nId;
    var content = "c" + nId;
    var date = "d" + nId;
    var editAnchor = "edit" + nId;

    Event.stop(e);
    var imgs = $(txt).getElementsBySelector("img");
    for( i = 0; i < imgs.length; ++i ) {
        if( imgs[i].id.indexOf("quitImg") > -1 ) {
            Element.remove(imgs[i]);
            break;
        }
    }

    Element.remove(editAnchor);

    $(txt).style.overflow = "hidden";
    //shrink(txt, 14);
    $(txt).setStyle('height','14px');
    //$(txt).style.height = divHeight;

    var txtId = "txt" + nId;
    var line = $(txtId).innerHTML.substr(0,90);
    line = line.replace(/<br>/g," ");
    var dateValue = $(dateId) != null ? $(dateId).innerHTML : "";
    dateValue = dateValue.substring(0,dateValue.indexOf(" "));
    line = "<div id='" + date + "' style='font-size:1.0em; width:10%;'><b>" + dateValue + "<\/b><\/div><div id='" + content + "' style='float:left; font-size:1.0em; width:70%;'>" + line + "<\/div>";
    $("txt"+nId).hide();
    $("sig"+nId).hide();
    new Insertion.Top(txt,line);


    //img = "<img title='Print' id='print" + nId + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
    //new Insertion.Top(txt, img);

    var print = 'print' + nId;
    var func;
    var editWarn = "editWarn" + nId;
    if( $F(editWarn) == "true" ) {
        func = "noPrivs(event);";
    }
    else {
        func = "editNote(event);";
    }
    var anchor = "<a title='Edit' id='edit"+ nId + "' href='#' onclick='" + func + " return false;' style='float: right; margin-right: 5px; font-size:8px;'>Edit</a>";
    new Insertion.After(print, anchor);


    img = "<img title='Maximize Display' alt='Maximize Display' id='xpImg" + nId + "' name='expandViewTrigger' onclick='xpandView(event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/triangle_down.gif'>";
    new Insertion.Top(txt, img);
    Element.observe(txt, 'click', xpandView);
}

var idHeight;
var curElemHeight;
var shrinkTimer;
function shrink(id, toScale) {
	idHeight = $(id).getHeight();
    curElemHeight = idHeight;
    var delta = Math.ceil(curElemHeight/5);
    $(id).style.height = toScale + "px";
}

//this func fires only if maximize button is clicked after fullView
function xpandView(e) {
    var id = Event.element(e).id;
    xpandViewById(id);
    Event.stop(e);
}

function xpandViewById(id) {
    var regEx = /\d+/;
    var nId = regEx.exec(id);
    var txt = "n" + nId;
    var img = "xpImg" + nId;
    var content = "c" + nId;
    var date = "d" + nId;

    var imgTag = "<img id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";


    Element.remove(img);
    Element.remove(date);
    Element.remove(content);


    $(txt).style.height = 'auto';
    new Insertion.Top(txt, imgTag);
    $("txt"+nId).show();
    $("sig"+nId).show();
    Element.stopObserving(txt, 'click', xpandView);

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

function toggleFullViewForAll(f) {
	jQuery('[name="fullViewTrigger"]').each(function(){
		$(this).click();
	});
	jQuery('[name="expandViewTrigger"]').each(function(){
		$(this).click();
	});
}

//this func fires only if maximize button is clicked
function fullView(e) {
    var id = Event.element(e).id;
    fullViewById(id);
    Event.stop(e);
}

function fullViewById(id) {
	var url = ctx + "/CaseManagementView.do";

    var regEx = /\d+/;
    var nId = regEx.exec(id);
	
    
    var txt = "n" + nId;
    var img = "quitImg" + nId;
    var fullId = "full" + nId;
    var params = "method=viewNote&raw=false&noteId=" + nId;
    var noteTxtId = "txt" + nId; 
    var btnHtml = "<img title='Minimize Display' id='bottomQuitImg" + nId + "' alt='Minimize Display' onclick='minView(event)' style='float:right; margin-right:5px; margin-bottom:3px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
    Element.stopObserving(txt, 'click', fullView);
	
	

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

    var imgTag = "<img id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";


    Element.remove(img);


    $(txt).style.height = 'auto';
    new Insertion.Top(txt, imgTag);
    //Element.stopObserving(txt, 'click', fullView);
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
var sessionExpiredError;
var unlockNoteError;
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
                                            alert(sessionExpiredError);
                                        else
                                            alert(request.status + " " +  unlockNoteError);
                                    }
                   }
            );
    return false;
}

//display unlock note password text field and submit button
var msgPasswd;
var btnMsgUnlock;
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
    var lockForm = "<p id='passwdPara' class='passwd'>" + msgPasswd + ":&nbsp;<input onkeypress=\"return grabEnter('btnUnlock', event);\" type='password' id='" + passwd + "' size='16'>&nbsp;<input id='btnUnlock' type='button' onclick=\"return unlock_ajax('" + txt + "');\" value='" + btnMsgUnlock + "'><\/p>";
    new Insertion.Bottom(txt, lockForm);

    $(txt).style.height = "auto";
    $(passwd).focus();
    Element.stopObserving(txt, 'click', unlockNote);
}

function NoteisLocked(nId) {

	var noteIsLocked = "";
	var url = ctx + "/CaseManagementEntry.do";
	params = "method=isNoteEdited&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&noteId=" + nId;
	
	new Ajax.Request(
		url,
		{
			method: 'post',
			postBody: params,
			evalScripts: true,
			asynchronous: false,
			onSuccess: function(request) {										
					var json = request.responseText.evalJSON();								
					noteIsLocked = json.isNoteEdited;
			}
		}
	);	
	
	return noteIsLocked;
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
    var el = Event.element(e);
    var payload;
    var regEx = /\d+/;
    var nId = regEx.exec(el.id);
    var txt = "n" + nId;
    var xpandId = "xpImg" + nId;
    var sig = "sig" + nId;
    
    var noteLockStatus = NoteisLocked(nId);
    if(noteLockStatus == "user") {
    	var viewEditedNote = confirm("You have started to edit this note in another window.\nDo you wish to continue?");
    	if( viewEditedNote ) {    	
    		var parent = $(caseNote).parentNode.id;
    		var oldNoteId = parent.substr(1);	    		
    		var params = "method=releaseNoteLock&demographicNo=" + demographicNo + "&providerNo=" + providerNo  + "&noteId=" + oldNoteId + "&force=true";
    		jQuery.ajax({
				type: "POST",
				url:  ctx + "/CaseManagementEntry.do",
				data: params
			});
    		    		
    		params = "method=updateNoteLock&demographicNo=" + demographicNo + "&noteId=" + nId;
			jQuery.ajax({
				type: "POST",
				url:  ctx + "/CaseManagementEntry.do",
				data: params
			});
    	}  
    	else {
    		Event.stop(e);
    		return false;
    	}
    }
    else if( noteLockStatus == "other" ) {
    	Event.stop(e);
    	alert("This note is being edited by another user.  Try again later");
    	return false;
    }

    if( $(xpandId) != null ) {
        xpandView(e);
    }
    else {
        Event.stop(e);
    }

    //if we have an edit textarea already open, close it
    if($(caseNote) !=null && $(caseNote).parentNode.id != $(txt).id) {
        if( !changeToView(caseNote) ) {
            $(caseNote).focus();
            return;
        }
    }
    
    // Only works with "note_program_ui_enabled = true" (noteProgram.js)
    if (typeof _setCurrentProgramAndRoleIdForNote == "function") {
    	_setCurrentProgramAndRoleIdForNote(nId);
    }

    //get rid of minimize and print buttons
    var nodes = $(txt).getElementsBySelector('img');
    for(var i = 0; i < nodes.length; ++i ) {
        nodes[i].remove();
    }


    var editAnchor = "edit" + nId;
    var annoAnchor = "anno" + nId;
    var date = "d" + nId;
    var content = "c" + nId;

    //remove edit anchor
    //remove edit anchor
    if ($(editAnchor) != null)
    	Element.remove(editAnchor);

    // Remove annotation anchor
    if ($(annoAnchor) != null)
    	Element.remove(annoAnchor);

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

    var input = "<textarea tabindex='7' cols='84' rows='10' wrap='hard' class='txtArea' style='line-height:1.1em;' name='caseNote_note' id='" + caseNote + "'>" + payload + "<\/textarea>";
    new Insertion.Top(txt, input);
    var printimg = "<img title='Print' id='print" + nId + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";

    var strNid = "" + nId;
    if( strNid.substr(0,1) != "0" )
        new Insertion.Top(txt, printimg);

    if( $F(isFull) == "true" ) {
        //position cursor at end of text
        adjustCaseNote();
        setCaretPosition($(caseNote),$(caseNote).value.length);
        $(caseNote).focus();
        origCaseNote = $F(caseNote);
        
    }
    else {
        fetchNote(nId);
        Element.stopObserving(txt, 'click', fullView);
    }

    Element.observe(caseNote, 'keyup', monitorCaseNote);
    Element.observe(caseNote, 'click', getActiveText);

    if( passwordEnabled ) {
           input = "<p style='background-color:#CCCCFF; display:none; margin:0px;' id='notePasswd'>Password:&nbsp;<input type='password' name='caseNote.password'/><\/p>";
           new Insertion.Bottom(txt, input);
    }

    //we check if we are dealing with a new note or not
    if( strNid.charAt(0) == "0" ) {
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


    //AutoCompleter for Issues
    var issueURL = ctx + "/CaseManagementEntry.do?method=issueList&demographicNo=" + demographicNo + "&providerNo=" + providerNo;
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

    if( showIssue ) {
        $("noteIssues").scrollIntoView(false);
        $("issueAutocomplete").focus();
    }
    else {
        $(caseNote).focus();
    }

    return false;

}

function showHideIssues(e, issueType) {
				
	Event.stop(e);
	//Element.toggle('noteIssues');
	if(issueType=="hide" || issueType=="")
		showIssue = false;
	else
		showIssue = true;
				
	if( showIssue ) {
		if(issueType == "noteIssues-unresolved") {
			Element.toggle('noteIssues-unresolved');
			$("noteIssues-unresolved").scrollIntoView(false);			
		} else if(issueType == "noteIssues-resolved") {
			Element.toggle('noteIssues-resolved');
			$("noteIssues-resolved").scrollIntoView(false);			
		} else if(issueType == "noteIssues") {
			Element.toggle('noteIssues');
			$("noteIssues").scrollIntoView(false);
		}
				
		$("issueAutocomplete").focus();
	} else {
		$(caseNote).focus();
	}
				
	return false;
}
			
function scrollEncDown() {
	//$("encMainDiv").scrollTop= $("encMainDiv").scrollHeight;
	$("noteIssues").scrollIntoView(false);
	var x=document.body.scrollHeight;
	x=x+99999
	window.scrollTo(0,x);
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

var filterError;

function resetInputElements(element) {
	if (Object.prototype.toString.call(element) == "[object NodeList]") {
		var size = element.length;
		for (var i = 0; i < size; i++) {
			element[i].checked = false;
		}
	} else {
		element.checked = false;
	}
}

function filter(reset) {
    var url = ctx + "/CaseManagementEntry.do";
    var params = "ajaxview=ajaxView&fullChart=" + fullChart;
    document.forms["caseManagementEntryForm"].method.value = "edit";
    document.forms["caseManagementEntryForm"].note_edit.value = "new";
    document.forms["caseManagementEntryForm"].noteId.value = "0";
    document.forms["caseManagementEntryForm"].ajax.value = false;
    document.forms["caseManagementEntryForm"].chain.value = "list";

    document.forms["caseManagementViewForm"].method.value = "view";
    document.forms["caseManagementViewForm"].resetFilter.value = reset;

	if (reset) {
		resetInputElements(document.forms["caseManagementViewForm"].filter_providers);
		resetInputElements(document.forms["caseManagementViewForm"].filter_roles);
		resetInputElements(document.forms["caseManagementViewForm"].note_sort);
		resetInputElements(document.forms["caseManagementViewForm"].issues);
	}
	
    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];
    var caseMgtViewfrm = document.forms["caseManagementViewForm"];
    params +=  "&" + Form.serialize(caseMgtEntryfrm);
    params += "&" + Form.serialize(caseMgtViewfrm);

    var objAjax = new Ajax.Request (
                    url,
                    {
                        method: 'post',
                        postBody: params,
                        evalScripts: true,
                        onSuccess: function(request) {
                                                $("notCPP").update(request.responseText);
												$("notCPP").style.height = "50%";
                                           },
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>" + div + "</h3>Error: " + request.status;
                                            }
                            }

                      );
	return false;

}

/*function filter(reset) {
    document.forms["caseManagementEntryForm"].method.value = "edit";
    document.forms["caseManagementEntryForm"].note_edit.value = "new";
    document.forms["caseManagementEntryForm"].noteId.value = "0";
    document.forms["caseManagementEntryForm"].ajax.value = false;
    document.forms["caseManagementEntryForm"].chain.value = "null";

    document.forms["caseManagementViewForm"].method.value = "view";
    document.forms["caseManagementViewForm"].resetFilter.value = reset;

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
                            alert(request.status + " " + filterError);
                        }
                     }
                   );

    return false;
}*/

//find index of month
function getMonthIdx(mnth) {
    var idx;
    var tmp;

    for( idx = 0; idx < month.length; ++idx) {
        tmp = month[idx].toLowerCase();

        if( mnth == tmp ) {
            return idx;
        }
    }

    return -1;
}

//make sure observation date is in the past
var strToday;  //initialized in newCaseManagementView.jsp
function validDate() {
    var strDate = $("observationDate").value;
    var day = strDate.substring(0,strDate.indexOf("-"));
    var mnth = strDate.substring(strDate.indexOf("-")+1, strDate.lastIndexOf("-"));
    mnth = mnth.indexOf(".") != -1 ? mnth.substring(0, mnth.indexOf(".")) : mnth;
    mnth = mnth.toLowerCase();
    var mnthIdx = getMonthIdx(mnth);
    var year = strDate.substring(strDate.lastIndexOf("-")+1, strDate.indexOf(" "));
    var time = strDate.substr(strDate.indexOf(" ")+1);
    var timeArr = time.split(":");

    var date = new Date();
    date.setMonth(mnthIdx);
    date.setDate(day);
    date.setYear(year);
    date.setHours(timeArr[0]);
    date.setMinutes(timeArr[1]);

    var today = new Date();
    today.setHours(23);
    today.setMinutes(59);

    if( date <= today )
        return true;
    else
        return false;
}

var pastObservationDateError;
var assignObservationDateError;
var assignIssueError;
var savingNoteError;
var encTimeError;
var transportationTimeError;
var encMinError;
var encTimeMandatoryMsg;
var encTimeMandatory;
var transportationTimeMandatoryMsg;
var transportationTimeMandatory;

function ajaxSaveNote(div,noteId,noteTxt) {

    if( $("observationDate") != null && $("observationDate").value.length > 0 && !validDate() ) {
        alert(pastObservationDateError);
        return false;
    }


    if( caisiEnabled ) {
        if(requireIssue && !issueIsAssigned() ) {
            alert(assignIssueError);
            return false;
        }
		/* the observationDate could be the default one as today.
        if( requireObsDate && $("observationDate").value.length == 0 ) {
            alert(assignObservationDateError);
            return false;
        }
		*/
        if($("encTypeSelect0") != null && $("encTypeSelect0").options[$("encTypeSelect0").selectedIndex].value.length == 0 ) {
        	alert(assignEncTypeError);
        	return false;
        }
        if(document.getElementById("hourOfEncTransportationTime") != null) {
	        if(isNaN(document.getElementById("hourOfEncTransportationTime").value) ||
	        isNaN(document.getElementById("minuteOfEncTransportationTime").value) ) {
				alert(encTimeError);
				return false;
			}
			if(!isNaN(document.getElementById("minuteOfEncTransportationTime").value) &&
			parseInt(document.getElementById("minuteOfEncTransportationTime").value) > 59) {
				alert(encMinError);
				return false;
			}			
					
		} 
		
		if(document.getElementById("hourOfEncounterTime") != null) { 
			if(isNaN(document.getElementById("hourOfEncounterTime").value) ||
			isNaN(document.getElementById("minuteOfEncounterTime").value) ) {
				alert(encTimeError);
				return false;
			}
			if(!isNaN(document.getElementById("minuteOfEncounterTime").value) &&
			parseInt(document.getElementById("minuteOfEncounterTime").value) > 59) {
				alert(encMinError);
				return false;
			}
		 
		
			if( parseInt(document.getElementById("minuteOfEncounterTime").value) == 0 &&
				parseInt(document.getElementById("hourOfEncounterTime").value) == 0 )  {
		   
		   		if(encTimeMandatory) {
		    		alert(encTimeMandatoryMsg);
		    		return false;
		   		}
			}
		} 
		if (document.getElementById("hourOfEncounterTime") == null || 
			document.getElementById("minuteOfEncounterTime") == null ||
			document.getElementById("hourOfEncounterTime").value == "" ||
			document.getElementById("minuteOfEncounterTime").value == "") {
				if(encTimeMandatory) {
			    	alert(encTimeMandatoryMsg);
			    	return false;
			   	}
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
    var encType = "encTypeSelect" + noteId;
    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];
    var url = ctx + "/CaseManagementEntry.do";
    var params = "nId="+noteId+issueParams+"&demographicNo=" + demographicNo +"&providerNo=" + providerNo + "&numIssues="+idx+"&obsDate="+$F("observationDate")+"&encType="+encodeURI($F(encType))+"&noteTxt="+encodeURI(noteTxt);
    params += "&" + Form.serialize(caseMgtEntryfrm);

    var objAjax = new Ajax.Updater (
                    {success:div},
                    url,
                    {
                        method: 'post',
                        evalScripts: true,
                        postBody: params,
                        onFailure: function(request) {
                            if( request.status == 403 )
                                alert(sessionExpiredError);
                            else
                                alert(savingNoteError + " " + request.status + " " + request.responseText);
                        }
                     }
                   );

    return true;
}

function saveNoteAjax(method, chain) {

	var noteStr;
	noteStr = $F(caseNote);
    /*
    if( noteStr.replace(/^\s+|\s+$/g,"").length == 0 ) {
        alert("Please enter a note before saving");
        return false;
    }
    */

    if( $("observationDate") != undefined && $("observationDate").value.length > 0 && !validDate() ) {
        alert(pastObservationDateError);
        return false;
    }

    if( caisiEnabled ) {
        if( requireIssue && !issueIsAssigned() ) {
            alert(assignIssueError);
            return false;
        }
/* the observationDate could be the default one as today.
        if( requireObsDate && $("observationDate").value.length == 0 ) {
            alert(assignObservationDateError);
            return false;
        }
*/
        if($("encTypeSelect0") != null && $("encTypeSelect0").options[$("encTypeSelect0").selectedIndex].value.length == 0 ) {
        	alert(assignEncTypeError);
        	return false;
        }
 		if(document.getElementById("hourOfEncTransportationTime") != null) {
	        if(isNaN(document.getElementById("hourOfEncTransportationTime").value) ||
	        isNaN(document.getElementById("minuteOfEncTransportationTime").value) ) {
				alert(encTimeError);
				return false;
			}
			if(!isNaN(document.getElementById("minuteOfEncTransportationTime").value) &&
			parseInt(document.getElementById("minuteOfEncTransportationTime").value) > 59) {
				alert(encMinError);
				return false;
			}	
			
		} 
		
		if(document.getElementById("hourOfEncounterTime") != null) {
			if(isNaN(document.getElementById("hourOfEncounterTime").value) ||
			isNaN(document.getElementById("minuteOfEncounterTime").value) ) {
				alert(encTimeError);
				return false;
			}
			if(!isNaN(document.getElementById("minuteOfEncounterTime").value) &&
			parseInt(document.getElementById("minuteOfEncounterTime").value) > 59) {
				alert(encMinError);
				return false;
			}
		 
		
			if( parseInt(document.getElementById("minuteOfEncounterTime").value) == 0 &&
				parseInt(document.getElementById("hourOfEncounterTime").value) == 0 )  {		   
		   		if(encTimeMandatory) {
		    		alert(encTimeMandatoryMsg);
		    		return false;
		   		}
			}
		}
		if (document.getElementById("hourOfEncounterTime") == null || 
			document.getElementById("minuteOfEncounterTime") == null ||
			document.getElementById("EncounterTime").value == "" ||
			document.getElementById("minuteOfEncounterTime").value == "") {
				if(encTimeMandatory) {
			    	alert(encTimeMandatoryMsg);
			    	return false;
			   	}
		}
    }
    document.forms["caseManagementEntryForm"].method.value = method;
    document.forms["caseManagementEntryForm"].ajax.value = false;
    document.forms["caseManagementEntryForm"].chain.value = chain;
    document.forms["caseManagementEntryForm"].includeIssue.value = "off";

    clearAutoSaveTimer();

    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];

	var params = Form.serialize(caseMgtEntryfrm);
    params += "&ajaxview=ajaxView&fullChart=" + fullChart;

    var url = ctx + "/CaseManagementEntry.do";

    $("notCPP").update("Loading...");

	var objAjax = new Ajax.Request (
                            url,
                            {
                                method: 'post',
                                postBody: params,
                                evalScripts: true,
                                onSuccess: function(request) {
                                                $("notCPP").update(request.responseText);
												$("notCPP").style.height = "50%";
												if( fullChart == "true" ) {
													$("quickChart").innerHTML = quickChartMsg;
													$("quickChart").onclick = function() {return viewFullChart(false);}
												}
												else {
													$("quickChart").innerHTML = fullChartMsg;
													$("quickChart").onclick = function() {return viewFullChart(true);}
												}
                                           },
                                onFailure: function(request) {
                                                $("notCPP").update("Error: " + request.status + request.responseText);
                                            }
                            }

                      );
    return false;
}
function cancelResident() {
    jQuery('#resident').trigger("reset");
    jQuery("#residentChain").val("");
    jQuery("#residentMethod").val("");
    if( jQuery(".supervisor").is(":visible") ) {
        jQuery(".supervisor").slideUp(300);                    
    }

    if( jQuery(".reviewer").is(":visible") ) {
        jQuery(".reviewer").slideUp(300);
    }
    
    jQuery('#showResident').fadeOut(2000);
    jQuery('#showResident').css('z-index',300);
    
    return false;

}

function subResident() {
    if( !jQuery("input[name='reviewed']:checked").length ) {
        alert("Please select if the note has been reviewed");
        return false;
    }

    if( (jQuery("input[name='reviewed']:checked").val() == "true" && jQuery("#reviewer").val() == "") ) {
        alert("Please select who you reviewed the note with");
        return false;
    }
    
    else if( (jQuery("input[name='reviewed']:checked").val() == "false" && jQuery("#supervisor").val() == "") ) {
        alert("Please Choose Your Supervisor");
        return false;
    }

    jQuery('<input>').attr({
    type: 'hidden',
    id: 'isAResident',
    name: 'isAResident',
    value: 'true'
}).appendTo('#resident');

    jQuery('<input>').attr({
    type: 'hidden',
    id: 'supervisor',
    name: 'supervisor',
    value: jQuery("#supervisor").val()
}).appendTo("form[name='caseManagementEntryForm']");

    jQuery('<input>').attr({
    type: 'hidden',
    id: 'reviewer',
    name: 'reviewer',
    value: jQuery("#reviewer").val()
}).appendTo("form[name='caseManagementEntryForm']");

    jQuery('<input>').attr({
    type: 'hidden',
    id: 'resident',
    name: 'resident',
    value: 'true'
}).appendTo("form[name='caseManagementEntryForm']");

  jQuery('#showResident').fadeOut(2000);
  jQuery('#showResident').css('z-index',300);  
  savePage(jQuery("#residentMethod").val(), jQuery("#residentChain").val());
  return false;
}


function savePage(method, chain) {
       
        if( typeof jQuery("form[name='resident'] input[name='residentMethod']").val() != "undefined" && 
            jQuery("form[name='resident'] input[name='residentMethod']").val().trim().length == 0 &&
            method.match(/.*[Ee]xit$/g) != null  ) { 
            jQuery("#residentChain").val(chain);
            jQuery("#residentMethod").val(method);
            jQuery("#showResident").css('z-index',1);
            jQuery("#showResident").fadeIn(2000);
            jQuery("#reviewed").focus();
            return false;
        }


	if ((typeof jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val() != "undefined") &&
			(typeof jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val() != "undefined")) {
		if (jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val().trim().length == 0 ||
				jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val().trim().length == 0) {
			// For weird cases where the role id or program number is missing.
			_missingRoleProgramIdError();
			return false;
		} else if (jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val() == "-2" ||
				jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val() == "-2") {
			// For the case where you're trying to save a note with no available programs or roles
			_noVisibleProgramsError();
			return false;
		}
	}


	var noteStr;
	noteStr = $F(caseNote);
    /*
    if( noteStr.replace(/^\s+|\s+$/g,"").length == 0 ) {
        alert("Please enter a note before saving");
        return false;
    }
    */

    if( $("observationDate") != undefined && $("observationDate").value.length > 0 && !validDate() ) {
        alert(pastObservationDateError);
        return false;
    }

    if( caisiEnabled ) {
        if( requireIssue && !issueIsAssigned() ) {
            alert(assignIssueError);
            return false;
        }
		/* the observationDate could be the default one as today.
        if( requireObsDate && $("observationDate").value.length == 0 ) {
            alert(assignObservationDateError);
            return false;
        }
		*/
        if($("encTypeSelect0") != null && $("encTypeSelect0").options[$("encTypeSelect0").selectedIndex].value.length == 0 ) {
        	alert(assignEncTypeError);
        	return false;
        }
        
        if(document.getElementById("hourOfEncTransportationTime") != null) {
	        if(isNaN(document.getElementById("hourOfEncTransportationTime").value) ||
	        isNaN(document.getElementById("minuteOfEncTransportationTime").value) ) {
				alert(encTimeError);
				return false;
			}
			if(!isNaN(document.getElementById("minuteOfEncTransportationTime").value) &&
			parseInt(document.getElementById("minuteOfEncTransportationTime").value) > 59) {
				alert(encMinError);
				return false;
			}	
						
		} 
		
		if(document.getElementById("hourOfEncounterTime") != null) {
			if(isNaN(document.getElementById("hourOfEncounterTime").value) ||
			isNaN(document.getElementById("minuteOfEncounterTime").value) ) {
				alert(encTimeError);
				return false;
			}
			if(!isNaN(document.getElementById("minuteOfEncounterTime").value) &&
			parseInt(document.getElementById("minuteOfEncounterTime").value) > 59) {
				alert(encMinError);
				return false;
			}
		 
		
			if( parseInt(document.getElementById("minuteOfEncounterTime").value) == 0 &&
				parseInt(document.getElementById("hourOfEncounterTime").value) == 0 )  {
		   
		   		if(encTimeMandatory) {
		    		alert(encTimeMandatoryMsg);
		    		return false;
		   		}
			}
		}  
		if (document.getElementById("hourOfEncounterTime") == null || 
			document.getElementById("minuteOfEncounterTime") == null ||
			document.getElementById("hourOfEncounterTime").value == "" ||
			document.getElementById("minuteOfEncounterTime").value == "") {
				if(encTimeMandatory) {
			    	alert(encTimeMandatoryMsg);
			    	return false;
			   	}
		}     
 		
    }

    
    document.forms["caseManagementEntryForm"].method.value = method;
    document.forms["caseManagementEntryForm"].ajax.value = false;
    document.forms["caseManagementEntryForm"].chain.value = chain;
    document.forms["caseManagementEntryForm"].includeIssue.value = "off";

    document.forms["caseManagementViewForm"].method.value = method;

    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];

    clearAutoSaveTimer();

    if( method == "saveAndExit" ) {
    	needToReleaseLock = false;
    }
    else {
    	needToReleaseLock = true;
    }

	origCaseNote = $F(caseNote);
    caseMgtEntryfrm.submit();

	jQuery("span[note_addon]").each(function(i){
		var func = jQuery(this).attr('note_addon');
		eval(func + "()");
	});


	jQuery("[submit_addon]").each(function()
    		   {
    		     jQuery("#"+jQuery(this).attr('submit_addon')).click();
    		   }
    		 );


    /*var frm = document.forms["caseManagementViewForm"];
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
                                alert(sessionExpiredError);
                            else
                                alert(request.status + " " + savingNoteError);
                        }
                     }
                   );
*/
    return false;
}

    var changeIssueMsg;
    function changeDiagnosis(issueId) {
        var methodArg = "ajaxChangeDiagnosis";
        var divIdArg = $("noteIssues").up().id;
        var thisObj = {};
        changeIssueFunc = updateIssues.bindAsEventListener(thisObj, methodArg, divIdArg);

        document.forms['caseManagementEntryForm'].change_diagnosis_id.value=issueId;
        $("asgnIssues").value= changeIssueMsg;

        Element.stopObserving('asgnIssues', 'click', addIssueFunc);
        Element.observe('asgnIssues', 'click', changeIssueFunc);
        $("issueAutocomplete").focus();
        return false;
    }

function changeDiagnosisResolved(issueId) {
	var methodArg = "ajaxChangeDiagnosis";
	var divIdArg = $("noteIssues-resolved").up().id;
	var thisObj = {};
	changeIssueFunc = updateIssues.bindAsEventListener(thisObj, methodArg, divIdArg);
			
	document.forms['caseManagementEntryForm'].change_diagnosis_id.value=issueId;
	$("asgnIssues").value= changeIssueMsg;
		
	Element.stopObserving('asgnIssues', 'click', addIssueFunc);
	Element.observe('asgnIssues', 'click', changeIssueFunc);
	$("issueAutocomplete").focus();
	return false;
}

function changeDiagnosisUnresolved(issueId) {
	var methodArg = "ajaxChangeDiagnosis";
	var divIdArg = $("noteIssues-unresolved").up().id;
	var thisObj = {};
	changeIssueFunc = updateIssues.bindAsEventListener(thisObj, methodArg, divIdArg);
			
	document.forms['caseManagementEntryForm'].change_diagnosis_id.value=issueId;
	$("asgnIssues").value= changeIssueMsg;
				
	Element.stopObserving('asgnIssues', 'click', addIssueFunc);
	Element.observe('asgnIssues', 'click', changeIssueFunc);
	$("issueAutocomplete").focus();
	return false;
}
			
    function toggleNotePasswd() {
        if( passwordEnabled ) {
            Element.toggle('notePasswd');
            if( $('notePasswd').style.display != "none" )
                document.forms['caseManagementEntryForm'].elements['caseNote.password'].focus();
            else
                document.forms['caseManagementEntryForm'].elements[caseNote].focus();
        }
        return false;
    }

    var closeWithoutSaveMsg;
    function closeEnc(e) {
        Event.stop(e);
        if( !lostNoteLock && (origCaseNote != $F(caseNote)  || origObservationDate != $("observationDate").value)) {
            if( confirm(closeWithoutSaveMsg) ) {
                var frm = document.forms["caseManagementEntryForm"];
                origCaseNote = $F(caseNote);
                frm.method.value = "cancel";
                frm.submit();
            }
        }
        else
            window.close();

        return false;
    }

function addIssue2CPP(txtField, listItem) {

   var nodeId = listItem.id;
   var size = 0;
   var found = false;
   var curItems = document.forms["frmIssueNotes"].elements["issueId"];

   if( typeof curItems.length != "undefined" ) {
        size = curItems.length;

       for( var idx = 0; idx < size; ++idx ) {
            if( curItems[idx].value == nodeId ) {
                found = true;
                break;
            }
       }
   }
   else {
        found = curItems.value == nodeId;
   }

   if( !found ) {
       var node = document.createElement("LI");

       var html = "<input type='checkbox' id='issueId' name='issue_id' checked value='" + nodeId + "'>" + listItem.innerHTML;
       new Insertion.Top(node, html);

       $("issueIdList").appendChild(node);
       $("issueAutocompleteCPP").value = "";

   }

   $("issueChange").value = true;
}

function saveIssueId(txtField, listItem) {
    $("newIssueId").value = listItem.id;
    $("newIssueName").value = listItem.innerHTML;

    submitIssues = true;
}

var pickIssueMsg;
var assignIssueMsg;
function updateIssues(e) {
    var args = $A(arguments);
    args.shift();

    if( $("newIssueId").value.length == 0 || $("issueAutocomplete").value != $("newIssueName").value )
        alert(pickIssueMsg);
    else
        ajaxUpdateIssues(args[0], args[1]);

    if( $F("asgnIssues") != assignIssueMsg ) {
        $("asgnIssues").value= assignIssueMsg;
        Element.stopObserving('asgnIssues', 'click', changeIssueFunc);
        Element.observe('asgnIssues', 'click', addIssueFunc);
    }
    Event.stop(e);
    submitIssues = false;
    return false;
}
var ajaxRequest;
var updateIssueError;
function ajaxUpdateIssues(method, div) {
    var frm = document.forms["caseManagementEntryForm"];
    frm.method.value = method;
    frm.ajax.value = true;

    var url = ctx + "/CaseManagementEntry.do";
    ajaxRequest = new Ajax.Updater( {success:div}, url, {
                                        evalScripts: true, parameters: Form.serialize(frm), onSuccess: onIssueUpdate,
                                        onFailure: function(response) {
                                                        alert( response.status + " " + updateIssueError);
                                                    }
                                    } );

    return false;
}

function onIssueUpdate() {

    //this request succeeded so we reset issues
    $("issueAutocomplete").value = "";
    $("newIssueId").value = "";
	//notifyIssueUpdate();
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

function writeNewNote(newReason,txt, encType) {

    var origReason = reason;
    reason = newReason;

    $("encType").value = encType;

    newNote(null);

    $(caseNote).value += txt;
    adjustCaseNote();
    setCaretPosition($(caseNote),$(caseNote).value.length);
    reason = origReason;
    $("encType").value = "";
}

//we insert a new note div with textarea etc
//newNoteIdx guarantees unique id for successive calls to newNote
var newNoteCounter = 0;
var reason;
function newNote(e) {
    if( e != null )
        Event.stop(e);

    ++newNoteCounter;
    var newNoteIdx = "0" + newNoteCounter;
    var id = "nc" + newNoteIdx;
    var sigId = "sig"+ newNoteIdx;
    var input = "<textarea tabindex='7' cols='84' rows='1' wrap='hard' class='txtArea' style='line-height:1.0em;' name='caseNote_note' id='caseNote_note" + newNoteIdx + "'>" + reason + "<\/textarea>";
    var passwd = "";
    if( passwordEnabled ) {
        passwd = "<p style='background-color:#CCCCFF; display:none; margin:0px;' id='notePasswd'>Password:&nbsp;<input type='password' name='caseNote.password'/><\/p>";
    }

    // the extra BR NBSP at the ends are for IE fix for selection box is out of scrolling pane view.
    var div = "<div id='" + id + "' class='newNote'><input type='hidden' id='signed" + newNoteIdx + "' value='false'><input type='hidden' id='editWarn" + newNoteIdx + "' value='false'><div id='n" + newNoteIdx + "'><input type='hidden' id='full" + newNoteIdx + "' value='true'>" +
              "<input type='hidden' id='bgColour" + newNoteIdx + "' value='color:#000000;background-color:#CCCCFF;'>" + input + "<div class='sig' style='display:inline;' id='" + sigId + "'><\/div>" + passwd + "<\/div><\/div><br \/>&nbsp;<br \/>&nbsp;<br \/>&nbsp;<br \/>";


    if( changeToView(caseNote) ) {

        caseNote = "caseNote_note" + newNoteIdx;
        document.forms["caseManagementEntryForm"].note_edit.value = "new";
        document.forms["caseManagementEntryForm"].noteId.value = "0";
        document.forms["caseManagementEntryForm"].newNoteIdx.value = newNoteIdx;
        new Insertion.Bottom("encMainDiv", div);
        $(sigId).addClassName("sig");
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

    //need delay..something else going on
    setTimeout(scrollDownInnerBar,1500);

    return false;
}

function scrollDownInnerBar() {
	$("encMainDiv").scrollTop= $("encMainDiv").scrollHeight;
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
var month=new Array(12);
var msgDraftSaved;
var lostNoteLock = false;
function autoSave(async) {

    var url = ctx + "/CaseManagementEntry.do";
    var programId = case_program_id;
    var demoNo = demographicNo;
    var cmeFrm = document.forms["caseManagementEntryForm"];
    var nId = cmeFrm.noteId.value < 0 ? 0 : cmeFrm.noteId.value;
    var params = "method=autosave&demographicNo=" + demoNo + "&programId=" + programId + "&note_id=" + nId + "&note=" + escape($F(caseNote));

    new Ajax.Request( url, {
                                method: 'post',
                                postBody: params,
                                asynchronous: async,
                                onComplete: function(req) {
                                    if( async == false )
                                        okToClose = true;
				},
                                onSuccess: function(req) {
                                                /*var nId = caseNote.substr(13);
                                                var sig = "sig" + nId;

                                                if( $("autosaveTime") == null )
                                                    new Insertion.Bottom(sig, "<div id='autosaveTime' class='sig' style='text-align:center; margin:0px;'><\/div>");
                                                    */
                                                    
                                                
                                                
                                                var d = new Date();
                                                var min = d.getMinutes();
                                                min = min < 10 ? "0" + min : min;
                                                
                                                var seconds = d.getSeconds();
                                                seconds = seconds < 10 ? "0" + seconds : seconds;

                                                var fmtDate = "<i>" + msgDraftSaved + " " + d.getDate() + "-" + month[d.getMonth()]  + "-" + d.getFullYear() + " " + d.getHours() + ":" + min +  ":" + seconds + "<\/i>";
                                                $("autosaveTime").update(fmtDate);
                                                

                                           },
                                 onFailure: function(req) {
                                 	if( req.status == 403 ) {
                                                	lostNoteLock = true;
                                                	var msg = "<i>Autosave cancelled due to note being edited in another window</i>";
                                                	$("autosaveTime").update(msg);
                                    }
                                 
                                 }
                            }
                     );

}


function backup() {
	
    if(origCaseNote != $(caseNote).value || origObservationDate != $("observationDate").value) {
        autoSave(true);        
    }

	if( !lostNoteLock ) {
    	setTimer();
    }
}

var autoSaveTimer;
function setTimer() {
    autoSaveTimer = setTimeout("backup()", 5000);
}

function clearAutoSaveTimer() {
    clearTimeout(autoSaveTimer);
}

function clearAutoSaveTimer() {
    clearTimeout(autoSaveTimer);
}

var unsavedNoteMsg;
function restore() {
    if(confirm(unsavedNoteMsg)) {
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

/*
 *Pop up window for Showing all notes that have linked to an issue
 */
function showIssueHistory(demoNo, issueIds) {
    var rnd = Math.round(Math.random() * 1000);
    win = "win" + rnd;
    var url = ctx + "/CaseManagementEntry.do?method=issuehistory&demographicNo=" + demoNo + "&issueIds=" + issueIds;
    window.open(url,win,"scrollbars=yes, location=no, width=647, height=600","");
    return false;
}

var caseNote = "";  //contains id of note text area; system permits only 1 text area at a time to be created
var numChars = 0;
function monitorCaseNote(e) {
	
	//if we have lost the lock on the note alert the user
	if( lostNoteLock ) {
		var openAgain = confirm("You have saved/edited this note in another window\nPlease reopen the Chart if you wish to continue editing this note" +
			"\n Do you wish to reopen the chart now?");
			
	    if( openAgain ) {
	    	window.location.reload(true);
	    }
	}

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

function autoCompleteHideMenuCPP(element, update){
    new Effect.Fade(update,{duration:0.15});
    new Effect.Fade($("issueListCPP"), {duration:0.15});

}

function autoCompleteShowMenuCPP(element, update) {
    Effect.Appear($("issueListCPP"), {duration:0.15});
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

    var printDateMsg;
    var printDateOrderMsg;
    function printDateRange() {
        var sdate = $F("printStartDate");
        var edate = $F("printEndDate");
        if( sdate.length == 0 || edate.length == 0 ) {
            alert(printDateMsg);
            return false;
        }

        var tmp = sdate.split("-");
        var formatdate = tmp[1] + " " + tmp[0] + ", " + tmp[2];
        var msbeg = Date.parse(formatdate);

        tmp = edate.split("-");
        formatdate = tmp[1] + " " + tmp[0] + ", " + tmp[2];
        var msend = Date.parse(formatdate);

        if( msbeg > msend ) {
            alert(printDateOrderMsg);
            return false;
        }

        //cycle through container divs for each note
        var idx;
        var noteId;
        var notesDiv;
        var noteDate = null;
        var msnote;
        var pos;
        var imgId;

        for( idx = 1; idx <= maxNcId; ++idx ) {
        	
        	if($("nc"+idx) == null) continue;
        	noteDate = null;
            notesDiv = $("nc" + idx).down('div');
            noteId = notesDiv.id.substr(1);  //get note id
            if(noteId==0) continue;

            imgId = "print" + noteId;
            if( $(imgId) == null ) continue;

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

    var nothing2PrintMsg;
    function printNotes() {
        if( $("printopDates").checked && !printDateRange()) {
            return false;
        }else if( $("printopAll").checked ){
            printAll();
        }

        if( $F("notes2print").length == 0 && $F("printCPP") == "false" && $F("printRx") == "false" && $F("printLabs") == "false" ) {
            alert(nothing2PrintMsg);
            return false;
        }

        var url = ctx + "/CaseManagementEntry.do";
        var frm = document.forms["caseManagementEntryForm"];

        frm.method.value = "print";

        frm.pStartDate.value = $F("printStartDate");
        frm.pEndDate.value = $F("printEndDate");
        frm.submit();

        return false;
    }

    function sendToPhrr() {
        if( $("printopDates").checked && !printDateRange()) {
            return false;
        }
        else if( $("printopAll").checked )
            printAll();

        if( $F("notes2print").length == 0 && $F("printCPP") == "false" && $F("printRx") == "false" ) {
            alert(nothing2PrintMsg);
            return false;
        }

        var url = ctx + "/SendToPhr.do";
        var frm = document.forms["caseManagementEntryForm"];

        if (frm.module == null) {
            var moduleInput = document.createElement('input');
            moduleInput.setAttribute("type", "hidden");
            moduleInput.setAttribute("name", "module");
            moduleInput.setAttribute("value", "echart");
            frm.appendChild(moduleInput);
        }

        frm.method.value = "print";
        var oldurl = frm.action;
        frm.action = url;
        sendToPhrPopup("", "sendtophr");
        frm.target = "sendtophr";
        frm.submit();
        frm.target = "";
        frm.action = oldurl;


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
        for( idx = 1; idx <= maxNcId; ++idx ) {
        
        	if( $("nc" + idx) == null ) continue;
        	
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
        
        $("notes2print").value = "ALL_NOTES";
        
        /*
        //cycle through container divs for each note
        for( idx = 1; idx <= maxNcId; ++idx ) {
        
        	if( $("nc" + idx) == null ) continue;
        
            notesDiv = $("nc" + idx).down('div');
            noteId = notesDiv.id.substr(1);  //get note id
          //if print img present, add note to print queue if not already there
            if( $("print"+noteId) != null ) {
                pos = noteIsQeued(noteId);
                if( pos == -1 )
                    addPrintQueue(noteId);
            }
        }
        */
    }

    var editUnsignedMsg;
    function noPrivs(e) {

        if( confirm(editUnsignedMsg) ) {
            editNote(e);
        }
        else {
            Event.stop(e);
        }
    }

    function copyCppToCurrentNote() {
    	var currentNoteId = jQuery("input[name='noteId']").val();
    	var currentNoteText = jQuery("#caseNote_note"+currentNoteId).val();
    	currentNoteText += "\n";
    	currentNoteText += jQuery("#noteEditTxt").val();
    	jQuery("#caseNote_note"+currentNoteId).val(currentNoteText);
    }
    
  	function selectGroup(programId,demographicNo) {
  			var noteId = document.forms["caseManagementEntryForm"].noteId.value;
		 	var url='groupNoteSelect.jsp?programId='+programId + '&demographicNo='+demographicNo;
	    	popupPage(600,700,'group',url);

	}


  	function assign(programId,demographicNo) {
        if( origCaseNote != $F(caseNote)  || origObservationDate != $("observationDate").value) {
    var sumaryId = "sumary";
    var sumary;
    var saving = false;
            var parent = $(caseNote).parentNode.id;
            var nId = parent.substr(1);
            var tmp = $(caseNote).value;
            var sig = 'sig' + nId;
            //assignNoteAjax('save','list',programId,demographicNo);
                   	// Prevent saving of note if the current note isn't properly assigned to a program and role. (note_program_ui_enabled = true)
                        if ((typeof jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val() != "undefined") &&
                    			(typeof jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val() != "undefined")) {
                    		if (jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val().trim().length == 0 ||
                    				jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val().trim().length == 0) {
                    			// For weird cases where the role id or program number is missing.
                    			_missingRoleProgramIdError();
                    			return false;
                    		} else if (jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val() == "-2" ||
                    				jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val() == "-2") {
                    			// For the case where you're trying to save a note with no available programs or roles
                    			_noVisibleProgramsError();
                    			return false;
                    		}
                    	}
            saving = true;
            ajaxSaveNote(sig,nId,tmp);
                //cancel updating of issues
                //IE destroys innerHTML of sig div when calling ajax update
                //so we have to restore it here if the ajax call is aborted
                //this is buggy don't use
                /*if( ajaxRequest != undefined  && callInProgress(ajaxRequest.transport) ) {
                    ajaxRequest.transport.abort();
                    var siblings = $(caseNote).siblings();
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

                if( $("notePasswd") != null ) {
                    Element.remove("notePasswd");
                }

                Element.stopObserving(caseNote, 'keyup', monitorCaseNote);
                Element.stopObserving(caseNote, 'click', getActiveText);

                Element.remove(caseNote);

                //remove observation date input text box but preserve date if there is one
                if( !saving && $("observationDate") != null ) {
                    var observationDate = $("observationDate").value;

            		new Insertion.After("observationDate", " <span id='obs" + nId + "'>" + observationDate + "</span>");
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

            	if( $("noteIssues-resolved") != null )
            		Element.remove("noteIssues-resolved");

            	if( $("noteIssues-unresolved") != null )
            		Element.remove("noteIssues-unresolved");

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

                tmp = tmp.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
                tmp = tmp.replace(/\n/g,"<br>");

                if( !saving ) {
                    if( largeNote(tmp) ) {
                        var btmImg = "<img title='Minimize Display' id='bottomQuitImg" + nId + "' alt='Minimize Display' onclick='minView(event)' style='float:right; margin-right:5px; margin-bottom:3px; ' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
                        new Insertion.Before(sig, btmImg);
                    }

                    //$(txt).style.fontSize = normalFont;

                    //if we're not restoring a new note display print img
                    //if( nId.substr(0,1) != "0" ) {
                    //    img = "<img title='Print' id='print" + nId + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
                    //     new Insertion.Top(parent, img);
                   // }

                    var printImg = "print" + nId;
                    var img = "<img title='Minimize' id='quitImg" + nId + "' onclick='minView(event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/triangle_up.gif'>";
                    var printimg = "<img title='Print' id='" + printImg + "' alt='Toggle Print Note' onclick='togglePrint(" + nId + ", event)' style='float:right; margin-right:5px; margin-top: 2px;' src='" + ctx + "/oscarEncounter/graphics/printer.png'>";
                    var input = "<div id='txt" + nId + "'>" + tmp + "<\/div>";

                    var func;
                    var editWarn = "editWarn" + nId;
                    if( $F(editWarn) == "true" ) {
                        func = "noPrivs(event);";
                    }
                    else {
                        func = "editNote(event);";
                    }

                    var editAnchor = "<a title='Edit' id='edit"+ nId + "' href='#' onclick='" + func + " return false;' style='float: right; margin-right: 5px; font-size:8px;'>" + editLabel + "</a>";
                    var editAnchor = "<a title='Edit' id='edit"+ nId + "' href='#' onclick='" + func + " return false;' style='float: right; margin-right: 5px; font-size:8px;'>" + editLabel + "</a>";
                    var editId = "edit" + nId;

                    var attribName = "anno" + (new Date().getTime());
                    var attribAnchor = "<input id='anno" + nId + "' height='10px;' width='10px' type='image' src='" + ctx + "/oscarEncounter/graphics/annotation.png' title='" + annotationLabel + "' style='float: right; margin-right: 5px; margin-bottom: 3px;'" +
                    	"onclick=\"window.open('" + ctx + "/annotation/annotation.jsp?atbname=" + attribName + "&table_id=" + nId + "&display=EChartNote&demo=" + demographicNo + "','anwin','width=400,height=500');$('annotation_attribname').value='" + attribName + "'; return false;\">";

                    new Insertion.Top(parent, editAnchor);
                    new Insertion.After(editId, input);


                     if( nId.substr(0,1) != "0" ) {
                        Element.remove(printImg);
                        new Insertion.Before(editId, printimg);
                        new Insertion.After(editId, attribAnchor);
                        new Insertion.Top(parent, img);
                    }

                    new Insertion.Top(parent, img);

                    $(parent).style.height = "auto";

                }
            }//else{
  			var noteId = document.forms["caseManagementEntryForm"].noteId.value;
		 	var url='../PMmodule/ClientSearch2.do?programId='+programId + '&noteId='+noteId+'&method=attachForm&demographicNo='+demographicNo;
	    	popupPage(600,700,'group',url);
            //}
	}

	function setIssueCheckbox(val) {
		jQuery("input[name='issues']").each(function(){
			if(jQuery(this).val() == val)
				jQuery(this).attr("checked","checked");
		});
	}

function assignNoteAjax(method, chain,programId,demographicNo) {

	var noteStr;
	noteStr = $F(caseNote);
    /*
    if( noteStr.replace(/^\s+|\s+$/g,"").length == 0 ) {
        alert("Please enter a note before saving");
        return false;
    }
    */

    if( $("observationDate") != undefined && $("observationDate").value.length > 0 && !validDate() ) {
        alert(pastObservationDateError);
        return false;
    }

    if( caisiEnabled ) {
        if( requireIssue && !issueIsAssigned() ) {
            alert(assignIssueError);
            return false;
        }
/* the observationDate could be the default one as today.
        if( requireObsDate && $("observationDate").value.length == 0 ) {
            alert(assignObservationDateError);
            return false;
        }
*/
        if($("encTypeSelect0") != null && $("encTypeSelect0").options[$("encTypeSelect0").selectedIndex].value.length == 0 ) {
        	alert(assignEncTypeError);
        	return false;
        }
 		if(document.getElementById("hourOfEncounterTime") != null) {
	        if(
	         isNaN(document.getElementById("hourOfEncounterTime").value) ||
	         isNaN(document.getElementById("minuteOfEncounterTime").value) ) {
	        	alert(encTimeError);
	        	return false;
	        }
		   if(
			parseInt(document.getElementById("minuteOfEncounterTime").value) == 0 &&
			parseInt(document.getElementById("hourOfEncounterTime").value) == 0 ) {
		   
		   		if(encTimeMandatory) {
		    		alert(encTimeMandatoryMsg);
		    		return false;
		   		}	
		   	}	
		}
		
		if(document.getElementById("hourOfEncTransportationTime") != null) {
	        if(
	         isNaN(document.getElementById("hourOfEncTransportationTime").value) ||
	         isNaN(document.getElementById("minuteOfEncTransportationTime").value) ) {
	        	alert(transportationTimeError);
	        	return false;
	        }
		   if(
			parseInt(document.getElementById("minuteOfEncTransportationTime").value) == 0 &&
			parseInt(document.getElementById("hourOfEncTransportationTime").value) == 0 ) {
		   
		   		if(transportationTimeMandatory) {
		    		alert(transportationTimeMandatoryMsg);
		    		return false;
		   		}	
		   	}	
		}
    }
    document.forms["caseManagementEntryForm"].method.value = method;
    document.forms["caseManagementEntryForm"].ajax.value = false;
    document.forms["caseManagementEntryForm"].chain.value = chain;
    document.forms["caseManagementEntryForm"].includeIssue.value = "off";

    var caseMgtEntryfrm = document.forms["caseManagementEntryForm"];
    

	var params = Form.serialize(caseMgtEntryfrm);
    params += "&ajaxview=ajaxView&fullChart=" + fullChart;

    var url = ctx + "/CaseManagementEntry.do";

    $("notCPP").update("Loading...");

	var objAjax = new Ajax.Request (
                            url,
                            {
                                method: 'post',
                                postBody: params,
                                evalScripts: true,
                                onSuccess: function(request) {
                                                $("notCPP").update(request.responseText);
												$("notCPP").style.height = "50%";
												if( fullChart == "true" ) {
													$("quickChart").innerHTML = quickChartMsg;
													$("quickChart").onclick = function() {return viewFullChart(false);}
												}
												else {
													$("quickChart").innerHTML = fullChartMsg;
													$("quickChart").onclick = function() {return viewFullChart(true);}
												}
                                           },
                                onFailure: function(request) {
                                                $("notCPP").update("Error: " + request.status + request.responseText);
                                            }
                            }

                      );

}


	
	function doInformedConsent(demographicNo) {
		var checked = jQuery("#informedConsentCheck").attr("checked");
		if(checked) {
			var forSure = window.confirm("Are you sure you would like to indicate that Informed Consent has been collected?");
			if(forSure) {
				        jQuery.getJSON(ctx+"/DemographicExtService.do?method=saveNewValue&demographicNo="+demographicNo + "&key=informedConsent&value=yes",
			                function(data,textStatus){
			                  if(data != undefined && parseInt(data.value) > 0) {
			                  	jQuery("#informedConsentDiv").remove();
			                  }
      				  });
				
			} else {
				jQuery("#informedConsentCheck").attr("checked",false);
			}
		}
	}

	
	function clearTempNotes(demographicNo) {
		jQuery.getJSON(ctx+"/CaseManagementEntry.do?method=clearTempNotes&demographicNo="+demographicNo,
              function(data,textStatus){
              if(data != null && data.success == true) {
              	location.reload();
              }  else {
              	alert(data.error);
              }
 		});
	}
	
window.addEventListener("message", receiveMessage, false);
	
function receiveMessage(event) {
	var data = JSON.parse(event.data);
	if(data.encounterText != null && data.encounterText.length > 0) {
		var x = {};
		x.responseText = data.encounterText;
		writeToEncounterNote(x);
	}
	
}
