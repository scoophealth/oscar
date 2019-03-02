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
<%@page import="oscar.OscarProperties" %>
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
			if (!rosterEnrolledToValid(false)) return false;
			if(rosterStatusTerminationDateFilled() || rosterStatusTerminationReasonFilled() ) {
				alert('Please clear the roster termination date and roster termination reason fields');
				return false;
			}
			return true;
		} else if(document.updatedelete.roster_status.value=="TE") {
			if (!rosterStatusDateValid(false)) return false;
			if (!rosterStatusTerminationDateValid(false)) return false;
			if (!rosterStatusTerminationReasonNotBlank()) return false;
			return true;
		} else {
			return true;
		}
	}

	if (rosterStatusDateAllowed()) {
		if (document.updatedelete.roster_status.value=="RO") { //Patient rostered
			if (!rosterStatusDateValid(false)) return false;
			if (!rosterEnrolledToValid(false)) return false;
		}
		else {
			if (!rosterStatusTerminationDateValid(true)) return false;
		}
	} else {
		return false;
	}
	
	if (!rosterStatusDateValid(true)) return false;
	if (!rosterStatusTerminationDateValid(true)) return false;
	if (!rosterEnrolledToValid(true)) return false;
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


function checkTypeInEdit() {
  if ( !checkName() ) return false;
  if ( !checkDob() ) return false;
  if ( !checkHin() ) return false;
  if ( !checkSex() ) return false;
  <% if("false".equals(OscarProperties.getInstance().getProperty("skip_postal_code_validation","false"))) { %>
  if ( !isPostalCode() ) return false;
  <% } %>
  if ( !checkRosterStatus() ) return false;
  if ( !checkPatientStatus() ) return false;
  return(true);
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

function isPostalCode2()
{
    if(isCanadian()){
         e = document.updatedelete.mailingPostal;
         postalcode = e.value;
        	
         rePC = new RegExp(/(^s*([a-z](\s)?\d(\s)?){3}$)s*/i);
    
         if (!rePC.test(postalcode)) {
              e.focus();
              alert("The entered mailing Postal Code is not valid");
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


function setProvince(sdCode) {
	jQuery("#country").bind('change',function(){
		updateProvinces('');
	});
	
    jQuery.ajax({
        type: "POST",
        url:  '../demographicSupport.do',
        data: 'method=getCountryAndProvinceCodes',
        dataType: 'json',
        success: function (data) {
        	jQuery('#country').append(jQuery('<option>').text('').attr('value', ''));
        	jQuery.each(data, function(i, value) {
                 jQuery('#country').append(jQuery('<option>').text(value.label).attr('value', value.value));
             });
        	
        	 if(sdCode.indexOf('-') != -1) {
            	 jQuery("#country").val(sdCode.split("-")[0]);
             } else {
           	//  jQuery("#country").val('CA');
             }
        	
        	updateProvinces(sdCode);
        }
	});
  }


function updateProvinces(province) {
	var country = jQuery("#country").val();
	console.log('country is ' + country );
	if(country == '') {
		console.log('t1');
		return;
	}
	
	jQuery.ajax({
        type: "POST",
        url:  '../demographicSupport.do',
        data: 'method=getCountryAndProvinceCodes&country=' + country,
        dataType: 'json',
        success: function (data) {
        	jQuery('#province').empty();
        	jQuery.each(data, function(i, value) {
                 jQuery('#province').append(jQuery('<option>').text(value.label).attr('value', value.value));
             });
        	
        	
        	if(province != null) {
        		jQuery("#province").val(province);
        	}
        	
        	
        }
	});
}


function setMailingProvince(sdCode) {
	jQuery("#mailingCountry").bind('change',function(){
		updateMailingProvinces('');
	});
	
    jQuery.ajax({
        type: "POST",
        url:  '../demographicSupport.do',
        data: 'method=getCountryAndProvinceCodes',
        dataType: 'json',
        success: function (data) {
        	jQuery('#mailingCountry').append(jQuery('<option>').text('').attr('value', ''));
        	jQuery.each(data, function(i, value) {
                 jQuery('#mailingCountry').append(jQuery('<option>').text(value.label).attr('value', value.value));
             });
             
             if(sdCode.indexOf('-') != -1) {
            	 jQuery("#mailingCountry").val(sdCode.split("-")[0]);
             } else {
           	//  jQuery("#mailingCountry").val('CA');
             }
        	
        	updateMailingProvinces(sdCode);
        }
	});
  }


function updateMailingProvinces(province) {
	var country = jQuery("#mailingCountry").val();
	if(country == '') {
		return;
	}
	jQuery.ajax({
        type: "POST",
        url:  '../demographicSupport.do',
        data: 'method=getCountryAndProvinceCodes&country=' + country,
        dataType: 'json',
        success: function (data) {
        	jQuery('#mailingProvince').empty();
        	 
        	jQuery.each(data, function(i, value) {
                 jQuery('#mailingProvince').append(jQuery('<option>').text(value.label).attr('value', value.value));
             });
        	
        	
        	if(province != null) {
        		jQuery("#mailingProvince").val(province);
        	}
        	
        	
        }
	});
}
