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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.managers.ProgramManager2"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="oscar.OscarProperties"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

	String curProvider_no = (String) session.getAttribute("user");
	String demographic_no = request.getParameter("demographic_no") ;
	String apptProvider = request.getParameter("apptProvider");
	String appointment = request.getParameter("appointment");
	String userfirstname = (String) session.getAttribute("userfirstname");
	String userlastname = (String) session.getAttribute("userlastname");
	String currentProgram="";

	ResourceBundle oscarResources = ResourceBundle.getBundle("oscarResources", request.getLocale());
    String noteReason = oscarResources.getString("oscarEncounter.noteReason.TelProgress");

    if (OscarProperties.getInstance().getProperty("disableTelProgressNoteTitleInEncouterNotes") != null 
			&& OscarProperties.getInstance().getProperty("disableTelProgressNoteTitleInEncouterNotes").equals("yes")) {
		noteReason = "";
	}
    
 	String programId = (String)session.getAttribute(org.oscarehr.util.SessionConstants.CURRENT_PROGRAM_ID);
 	if(programId != null && programId.length()>0) {
 		Integer prId = null;
 		try {
 			prId = Integer.parseInt(programId);
 		} catch(NumberFormatException e) {
 			//do nothing
 		}
 		if(prId != null) {
 			ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
 			Program p = programManager.getProgram(loggedInInfo, prId);
 			if(p != null) {
 				currentProgram = p.getName();
 			}
 		}
 	}

   GregorianCalendar now=new GregorianCalendar();
   int curYear = now.get(Calendar.YEAR);
   int curMonth = (now.get(Calendar.MONTH)+1);
   int curDay = now.get(Calendar.DAY_OF_MONTH);
%>

function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=360,left=30";
  remote=window.open(u,n,args);
  if (remote != null) {
    if (remote.opener == null)
      remote.opener = self;
  }
  if (x == 1) { return remote; }
}

var awnd=null;
function ScriptAttach() {
  awnd=rs('swipe','zdemographicswipe.htm',600,600,1);
  awnd.focus();
}

function setfocus() {
  this.focus();
  document.titlesearch.keyword.focus();
  document.titlesearch.keyword.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
  var popup=window.open(page, "demodetail", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}


function popupEChart(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
  var popup=window.open(page, "encounter", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}
function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarRx", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}
function popupS(varpage) {
	if (! window.focus)return true;
	var href;
	if (typeof(varpage) == 'string')
	   href=varpage;
	else
	   href=varpage.href;
	window.open(href, "fullwin", ',type=fullWindow,fullscreen,scrollbars=yes');
	return false;
}
function checkRosterStatus() {
	if (rosterStatusChangedNotBlank()) {
		if (document.updatedelete.roster_status.value=="RO") { //Patient rostered
			if (!rosterStatusDateValid(false)) return false;
		}
		else {
			if (!rosterStatusTerminationDateValid(false)) return false;
			if (!rosterStatusTerminationReasonNotBlank()) return false;
		}
	}

	if (rosterStatusDateAllowed()) {
		if (document.updatedelete.roster_status.value=="RO") { //Patient rostered
			if (!rosterStatusDateValid(false)) return false;
		}
		else {
			if (!rosterStatusTerminationDateValid(true)) return false;
		}
	} else {
		return false;
	}
	if (!rosterStatusDateValid(true)) return false;
	if (!rosterStatusTerminationDateValid(true)) return false;
	return true;
}

function rosterStatusChanged() {
	return (document.updatedelete.initial_rosterstatus.value!=document.updatedelete.roster_status.value);
}
function checkPatientStatus() {
	if (patientStatusChanged()) {
		return patientStatusDateValid(false);
	}
	return patientStatusDateValid(true);
}

function patientStatusChanged() {
	return (document.updatedelete.initial_patientstatus.value!=document.updatedelete.patient_status.value);
}
function checkSex() {
	var sex = document.updatedelete.sex.value;
	
	if(sex.length == 0)
	{
		alert ("You must select a Gender.");
		return(false);
	}

	return(true);
}

function checkCaisiRecipientLocation(){
	    var rs = document.updatedelete.recipientLocation.value;
	    if(rs.length == 0) {	    	
	        alert("you must choose a Recipient Location");
	     	return(false);
	    }
	    return(true);
}
	
function checkCaisiLHIN(){
	    var rs = document.updatedelete.lhinConsumerResides.value;
	    if(rs.length == 0) {	    
	        alert("you must choose a LHIN Consumer Resides");
	     	return(false);
	    }
	    return(true);
}
	
function checkTypeInEdit() {
  if ( !checkName() ) return false;
  if ( !checkDob() ) return false;
  if ( !checkHin() ) return false;
  if ( !checkSex() ) return false;
  
  <% if("on".equals(OscarProperties.getInstance().getProperty("caisi","off"))) { %>
  
	if( !checkCaisiRecipientLocation() ) 
		return false;	
		
	if( !checkCaisiLHIN() ) 
		return false;
  	
  <%} %>
  
  <% if("false".equals(OscarProperties.getInstance().getProperty("skip_postal_code_validation","false"))) { %>
  if ( !isPostalCode() ) return false;
  <% } %>
  if ( !checkRosterStatus() ) return false;
  if ( !checkPatientStatus() ) return false;
     
  return true;
}

function formatPhoneNum() {
    if (document.updatedelete.phone.value.length == 10) {
        document.updatedelete.phone.value = document.updatedelete.phone.value.substring(0,3) + "-" + document.updatedelete.phone.value.substring(3,6) + "-" + document.updatedelete.phone.value.substring(6);
        }
    if (document.updatedelete.phone.value.length == 11 && document.updatedelete.phone.value.charAt(3) == '-') {
        document.updatedelete.phone.value = document.updatedelete.phone.value.substring(0,3) + "-" + document.updatedelete.phone.value.substring(4,7) + "-" + document.updatedelete.phone.value.substring(7);
    }
    if (document.updatedelete.phone2.value.length == 10) {
        document.updatedelete.phone2.value = document.updatedelete.phone2.value.substring(0,3) + "-" + document.updatedelete.phone2.value.substring(3,6) + "-" + document.updatedelete.phone2.value.substring(6);
        }
    if (document.updatedelete.phone2.value.length == 11 && document.updatedelete.phone2.value.charAt(3) == '-') {
        document.updatedelete.phone2.value = document.updatedelete.phone2.value.substring(0,3) + "-" + document.updatedelete.phone2.value.substring(4,7) + "-" + document.updatedelete.phone2.value.substring(7);
    }
}

//
function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
}
function referralScriptAttach2(elementName, name2) {
     var d = elementName;
     t0 = escape("document.forms[1].elements[\'"+d+"\'].value");
     t1 = escape("document.forms[1].elements[\'"+name2+"\'].value");
     rs('att',('../billing/CA/ON/searchRefDoc.jsp?param='+t0+'&param2='+t1),600,600,1);
}
function removeAccents(s){
    var r=s.toLowerCase();
    r = r.replace(new RegExp("\\s", 'g'),"");
    r = r.replace(new RegExp("[ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½]", 'g'),"a");
    r = r.replace(new RegExp("ï¿½", 'g'),"ae");
    r = r.replace(new RegExp("ï¿½", 'g'),"c");
    r = r.replace(new RegExp("[ï¿½ï¿½ï¿½ï¿½]", 'g'),"e");
    r = r.replace(new RegExp("[ï¿½ï¿½ï¿½ï¿½]", 'g'),"i");
    r = r.replace(new RegExp("ï¿½", 'g'),"n");
    r = r.replace(new RegExp("[ï¿½ï¿½ï¿½ï¿½ï¿½]", 'g'),"o");
    r = r.replace(new RegExp("?", 'g'),"oe");
    r = r.replace(new RegExp("[ï¿½ï¿½ï¿½ï¿½]", 'g'),"u");
    r = r.replace(new RegExp("[ï¿½ï¿½]", 'g'),"y");
    r = r.replace(new RegExp("\\W", 'g'),"");
    return r;
}

function isPostalCode()
{
    if(isCanadian()){
         e = document.updatedelete.postal;
         postalcode = e.value;
        	
         rePC = new RegExp(/(^s*([a-z](\s)?\d(\s)?){3}$)s*/i);
    
         if (!rePC.test(postalcode)) {
              e.focus();
              alert("The entered Postal Code is not valid");
              return false;
         }
    }//end cdn check

return true;
}

function isCanadian(){
	e = document.updatedelete.province;
    var province = e.options[e.selectedIndex].value;
    
    if ( province.indexOf("US")>-1 || province=="OT"){ //if not canadian
            return false;
    }
    return true;
}

function checkTypeIn() {
  var dob = document.titlesearch.keyword; typeInOK = false;

  if (dob.value.indexOf('%b610054') == 0 && dob.value.length > 18){
     document.titlesearch.keyword.value = dob.value.substring(8,18);
     document.titlesearch.search_mode[4].checked = true;
  }

  if(document.titlesearch.search_mode[2].checked) {
    if(dob.value.length==8) {
      dob.value = dob.value.substring(0, 4)+"-"+dob.value.substring(4, 6)+"-"+dob.value.substring(6, 8);
      //alert(dob.value.length);
      typeInOK = true;
    }
    if(dob.value.length != 10) {
      alert("<bean:message key="demographic.search.msgWrongDOB"/>");
      typeInOK = false;
    }

    return typeInOK ;
  } else {
    return true;
  }
}

function checkName() {
	var typeInOK = false;
	if(document.updatedelete.last_name.value!="" && document.updatedelete.first_name.value!="" && document.updatedelete.last_name.value!=" " && document.updatedelete.first_name.value!=" ") {
	    typeInOK = true;
	} else {
		alert ("<bean:message key="demographic.demographiceditdemographic.msgNameRequired"/>");
    }
	return typeInOK;
}
function checkDate(yyyy,mm,dd,err_msg) {

	var typeInOK = false;

	if(checkTypeNum(yyyy) && checkTypeNum(mm) && checkTypeNum(dd) ){
        var check_date = new Date(yyyy,(mm-1),dd);
		var now = new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var date=now.getDate();
		//alert(yyyy + " | " + mm + " | " + dd + " " + year + " " + month + " " +date);

		var young = new Date(year,month,date);
		var old = new Date(1800,01,01);
		//alert(check_date.getTime() + " | " + young.getTime() + " | " + old.getTime());
		if (check_date.getTime() <= young.getTime() && check_date.getTime() >= old.getTime() && yyyy.length==4) {
		    typeInOK = true;
		}
		if ( yyyy == "0000"){
                    typeInOK = false;
                }
        }

	if (!isValidDate(dd,mm,yyyy) || !typeInOK){
            alert (err_msg+"\n<bean:message key="demographic.demographiceditdemographic.msgWrongDate"/>");
            typeInOK = false;
        }

	return typeInOK;
}
function checkDob() {
	var yyyy = document.updatedelete.year_of_birth.value;
	var mm = document.updatedelete.month_of_birth.value;
	var dd = document.updatedelete.date_of_birth.value;

      return checkDate(yyyy,mm,dd,"<bean:message key="demographic.search.msgWrongDOB"/>");
}

function isValidDate(day,month,year){
   month = ( month - 1 );
   dteDate=new Date(year,month,day);
   //alert(dteDate);
   return ((day==dteDate.getDate()) && (month==dteDate.getMonth()) && (year==dteDate.getFullYear()));
}

function checkHin() {
	var hin = document.updatedelete.hin.value;
	var province = document.updatedelete.hc_type.value;

	if (!isValidHin(hin, province))
	{
		alert ("<bean:message key="demographic.demographiceditdemographic.msgWrongHIN"/>");
		return(false);
	}

	return(true);
}



function rosterStatusChangedNotBlank() {
	if (rosterStatusChanged()) {
		if (document.updatedelete.roster_status.value=="") {
			alert ("<bean:message key="demographic.demographiceditdemographic.msgBlankRoster"/>");
			document.updatedelete.roster_status.focus();
			return false;
		}
		return true;
	}
	return false;
}

function rosterStatusDateAllowed() {
	if (document.updatedelete.roster_status.value=="") {
	    yyyy = document.updatedelete.roster_date_year.value.trim();
	    mm = document.updatedelete.roster_date_month.value.trim();
	    dd = document.updatedelete.roster_date_day.value.trim();

	    if (yyyy!="" || mm!="" || dd!="") {
	    	alert ("<bean:message key="demographic.search.msgForbiddenRosterDate"/>");
	    	return false;
	    }
	    return true;
	}
	return true;
}

function rosterStatusDateValid(trueIfBlank) {
    yyyy = document.updatedelete.roster_date_year.value.trim();
    mm = document.updatedelete.roster_date_month.value.trim();
    dd = document.updatedelete.roster_date_day.value.trim();
    var errMsg = "<bean:message key="demographic.search.msgWrongRosterDate"/>";

    if (trueIfBlank) {
    	errMsg += "\n<bean:message key="demographic.search.msgLeaveBlank"/>";
    	if (yyyy=="" && mm=="" && dd=="") return true;
    }
    return checkDate(yyyy,mm,dd,errMsg);
}

function rosterStatusTerminationDateValid(trueIfBlank) {
    yyyy = document.updatedelete.roster_termination_date_year.value.trim();
    mm = document.updatedelete.roster_termination_date_month.value.trim();
    dd = document.updatedelete.roster_termination_date_day.value.trim();
    var errMsg = "<bean:message key="demographic.search.msgWrongRosterTerminationDate"/>";

    if (trueIfBlank) {
    	errMsg += "\n<bean:message key="demographic.search.msgLeaveBlank"/>";
    	if (yyyy=="" && mm=="" && dd=="") return true;
    }
    return checkDate(yyyy,mm,dd,errMsg);
}

function rosterStatusTerminationReasonNotBlank() {
	if (document.updatedelete.roster_termination_reason.value=="") {
		alert ("<bean:message key="demographic.demographiceditdemographic.msgNoTerminationReason"/>");
		return false;
	}
	return true;
}


function patientStatusDateValid(trueIfBlank) {
    var yyyy = document.updatedelete.patientstatus_date_year.value.trim();
    var mm = document.updatedelete.patientstatus_date_month.value.trim();
    var dd = document.updatedelete.patientstatus_date_day.value.trim();

    if (trueIfBlank) {
    	if (yyyy=="" && mm=="" && dd=="") return true;
    }
    return checkDate(yyyy,mm,dd,"<bean:message key="demographic.search.msgWrongPatientStatusDate"/>");
}




function checkONReferralNo() {
	<%
		String skip = oscar.OscarProperties.getInstance().getProperty("SKIP_REFERRAL_NO_CHECK","false");
		if(!skip.equals("true")) {
	%>
  var referralNo = document.updatedelete.r_doctor_ohip.value ;
  if (document.updatedelete.hc_type.value == 'ON' && referralNo.length > 0 && referralNo.length != 6) {
    alert("<bean:message key="demographic.demographiceditdemographic.msgWrongReferral"/>") ;
  }

  <% } %>
}


function newStatus() {
    newOpt = prompt("<bean:message key="demographic.demographiceditdemographic.msgPromptStatus"/>:", "");
    if (newOpt == null) {
    	return;
    } else if(newOpt != "") {
        document.updatedelete.patient_status.options[document.updatedelete.patient_status.length] = new Option(newOpt, newOpt);
        document.updatedelete.patient_status.options[document.updatedelete.patient_status.length-1].selected = true;
    } else {
        alert("<bean:message key="demographic.demographiceditdemographic.msgInvalidEntry"/>");
    }
}

function newStatus1() {
    newOpt = prompt("<bean:message key="demographic.demographiceditdemographic.msgPromptStatus"/>:", "");
    if (newOpt == null) {
    	return;
    } else if(newOpt != "") {
        document.updatedelete.roster_status.options[document.updatedelete.roster_status.length] = new Option(newOpt, newOpt);
        document.updatedelete.roster_status.options[document.updatedelete.roster_status.length-1].selected = true;
    } else {
        alert("<bean:message key="demographic.demographiceditdemographic.msgInvalidEntry"/>");
    }
}

function showEdit(){
    document.getElementById('editDemographic').style.display = 'block';
    document.getElementById('viewDemographics2').style.display = 'none';
    document.getElementById('updateButton').style.display = 'block';
    document.getElementById('swipeButton').style.display = 'block';
    document.getElementById('editBtn').style.display = 'none';
    document.getElementById('closeBtn').style.display = 'inline';
}

function showHideDetail(){
    showHideItem('editDemographic');
    showHideItem('viewDemographics2');
    showHideItem('updateButton');
    showHideItem('swipeButton');

    showHideBtn('editBtn');
    showHideBtn('closeBtn');
   
}

// Used to display demographic sections, where sections is an array of id's for
// div elements with class "demographicSection"
function showHideMobileSections(sections) {
    showHideItem('mobileDetailSections');
    for (var i = 0; i < sections.length; i++) {
        showHideItem(sections[i]);
    }
    // Change behaviour of cancel button
    var cancelValue = "<bean:message key="global.btnCancel" />";
    var backValue = "<bean:message key="global.btnBack" />";
    var cancelBtn = document.getElementById('cancelButton');
    if (cancelBtn.value == cancelValue) {
        cancelBtn.value = backValue;
        cancelBtn.onclick = function() { showHideMobileSections(sections); };
    } else {
        cancelBtn.value = cancelValue;
        cancelBtn.onclick = function() { self.close(); };
    }
}

function showHideItem(id){
    if(document.getElementById(id).style.display == 'inline' || document.getElementById(id).style.display == 'block')
        document.getElementById(id).style.display = 'none';
    else
        document.getElementById(id).style.display = 'block';
}

function showHideBtn(id){
    if(document.getElementById(id).style.display == 'none')
        document.getElementById(id).style.display = 'inline';
    else
        document.getElementById(id).style.display = 'none';
}


function showItem(id){
        document.getElementById(id).style.display = 'inline';
}

function hideItem(id){
        document.getElementById(id).style.display = 'none';
}

<security:oscarSec roleName="<%= roleName$ %>" objectName="_eChart" rights="r" reverse="<%= false %>" >
var numMenus = 1;
var encURL = "..<c:out value="${ctx}"/>/oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo="+demographicNo+"&curProviderNo=&reason=<%=URLEncoder.encode(noteReason)%>&encType=<%=URLEncoder.encode("telephone encounter with client")%>&userName=<%=URLEncoder.encode( userfirstname+" "+userlastname) %>&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=";
function showMenu(menuNumber, eventObj) {
    var menuId = 'menu' + menuNumber;
    return showPopup(menuId, eventObj);
}

<%if (OscarProperties.getInstance().getProperty("workflow_enhance")!=null && OscarProperties.getInstance().getProperty("workflow_enhance").equals("true")) {%>

function showAppt (targetAppt, eventObj) {
    if(eventObj) {
	targetObjectId = 'menu' + targetAppt;
	hideCurrentPopup();
	eventObj.cancelBubble = true;
	moveObject(targetObjectId, 300, 200);
	if( changeObjectVisibility(targetObjectId, 'visible') ) {
	    window.currentlyVisiblePopup = targetObjectId;
	    return true;
	} else {
	    return false;
	}
    } else {
	return false;
    }
} // showPopup

function closeApptBox(e) {
	if (!e) var e = window.event;
	var tg = (window.event) ? e.srcElement : e.target;
	if (tg.nodeName != 'DIV') return;
	var reltg = (e.relatedTarget) ? e.relatedTarget : e.toElement;
	while (reltg != tg && reltg.nodeName != 'BODY')
		reltg= reltg.parentNode;
	if (reltg== tg) return;

	// Mouseout took place when mouse actually left layer
	// Handle event
	hideCurrentPopup();
}
<%}%>

function add2url(txt) {
    var reasonLabel = "reason=";
    var encTypeLabel = "encType=";
    var beg = encURL.indexOf(reasonLabel);
    beg+= reasonLabel.length;
    var end = encURL.indexOf("&", beg);
    var part1 = encURL.substring(0,beg);
    var part2 = encURL.substr(end);
    encURL = part1 + encodeURI(txt) + part2;
    beg = encURL.indexOf(encTypeLabel);
    beg += encTypeLabel.length;
    end = encURL.indexOf("&", beg);
    part1 = encURL.substring(0,beg);
    part2 = encURL.substr(end);
    encURL = part1 + encodeURI(txt) + part2;
    popupEChart(710, 1024,encURL);
    return false;
}

function customReason() {
    var txtInput;
    var list = document.getElementById("listCustom");
    if( list.style.display == "block" )
        list.style.display = "none";
    else {
        list.style.display = "block";
        txtInput = document.getElementById("txtCustom");
        txtInput.focus();
    }

    return false;
}

function grabEnterCustomReason(event){

  var txtInput = document.getElementById("txtCustom");
  if(window.event && window.event.keyCode == 13){
      add2url(txtInput.value);
  }else if (event && event.which == 13){
      add2url(txtInput.value);
  }

  return true;
}

function addToPatientSet(demoNo, patientSet) {
    if (patientSet=="-") return;
    window.open("addDemoToPatientSet.jsp?demoNo="+demoNo+"&patientSet="+patientSet, "addpsetwin", "width=50,height=50");
}
</security:oscarSec>



function checkRosterStatus2(){
	<oscar:oscarPropertiesCheck property="FORCED_ROSTER_INTEGRATOR_LOCAL_STORE" value="yes">
	var rosterSelect = document.getElementById("roster_status");
	if(rosterSelect.getValue() == "RO"){
		var primaryEmr = document.getElementById("primaryEMR");
		primaryEmr.value = "1";
		primaryEmr.disable(true);
	}
	</oscar:oscarPropertiesCheck>
	return true;
}

jQuery(document).ready(function($) {
	jQuery("a.popup").click(function() {
		var $me = jQuery(this);
		var name = $me.attr("title");
		var rel = $me.attr("rel");
		var content = jQuery("#" + rel).html();
		var win = window.open(null, name, "height=250,width=600,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes");
		jQuery(win.document.body).html(content);
		return false;
	});

});


function showCbiReminder()
{
  alert('<bean:message key="demographic.demographiceditdemographic.updateCBIReminder"/>');
}



var addressHistory = "";
var homePhoneHistory="";
var workPhoneHistory="";
var cellPhoneHistory="";

function generateMarkup(addresses,type,header) {
	 var markup = '<table border="0" cellpadding="2" cellspacing="2" width="100%">';
     markup += '<tr><th><b>Date/Provider</b></th><th><b>'+header+'</b></th></tr>';
     for(var x=0;x<addresses.length;x++) {
     	if(addresses[x].type == type) {
     		markup += '<tr><td>'+addresses[x].dateSeen+'<br/>'+addresses[x].provider+'</td><td>'+addresses[x].name+'</td></tr>';
     	}
     }
     markup += "</table>";
     return markup;
}

function updatePaperArchive() {
	var val = jQuery("#paper_chart_archived").val();
	if(val == '' || val == 'NO') {
		jQuery("#paper_chart_archived_date").val('');
		jQuery("#paper_chart_archived_program").val('');
	}
	if(val == 'YES') {
		jQuery("#paper_chart_archived_program").val('<%=currentProgram%>');
	}
}


function updateDOBFromCal(cal) {
	var selectedDate = document.getElementById('dob_date1').value;
	var parts = selectedDate.split("-");
	if(parts.length == 3) {
		document.getElementById('year_of_birth').value = parts[0];
		document.getElementById('month_of_birth').value = parts[1];
		document.getElementById('date_of_birth').value = parts[2];
	} else {
		alert('Error updating DOB, enter manually');
	}
}


function updateDOBFromCalField() {
	var dt = document.getElementById('year_of_birth').value + "-" + document.getElementById('month_of_birth').value + "-" + document.getElementById('date_of_birth').value;
	document.getElementById('dob_date1').value = dt;
}

jQuery(document).ready(function() {
	var addresses;
	
	 jQuery.getJSON("../demographicSupport.do",
             {
                     method: "getAddressAndPhoneHistoryAsJson",
                     demographicNo: demographicNo
             },
             function(response){
                 if (response instanceof Array) {
                     addresses = response;
           	  	} else {
                     var arr = new Array();
                     arr[0] = response;
                     addresses = arr;
            	}
                 
                addressHistory = generateMarkup(addresses,'address','Address');
                homePhoneHistory = generateMarkup(addresses,'phone','Phone #');
                workPhoneHistory = generateMarkup(addresses,'phone2','Phone #');
                cellPhoneHistory = generateMarkup(addresses,'cell','Phone #');
       });
       
       updateDOBFromCalField();
});


