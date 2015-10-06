<%--

    Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
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
    Andromedia, to be provided as
    part of the OSCAR McMaster
    EMR System

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  	if (session.getAttribute("user") == null){
		response.sendRedirect("../logout.jsp");
	}
%>
<%@ page
	import="oscar.form.*, java.util.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.oscarDB.*,oscar.oscarBilling.ca.bc.MSP.*, oscar.oscarBilling.ca.bc.Teleplan.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>

<%
   boolean readonly = false;
   String readOnlyParam = request.getAttribute("readonly")!=null?(String)request.getAttribute("readonly"):"";
   if(readOnlyParam != null && readOnlyParam.equals("true")){
      readonly = true;
   }

   boolean hideToBill = false;
   if (request.getParameter("hideToBill") != null){
      hideToBill = true;
   }

   oscar.oscarBilling.ca.bc.pageUtil.WCBForm form = (oscar.oscarBilling.ca.bc.pageUtil.WCBForm) request.getAttribute("WCBForm");
   boolean haveClaims = false;
   boolean haveEmps   = false;
    ArrayList claims = new ArrayList();
   ArrayList emps = new ArrayList();
   if(form != null){
     WcbHelper wcbHelper = new WcbHelper(form.getDemographic());
      claims = (ArrayList) wcbHelper.getClaimInfo(form.getDemographic());
   emps = (ArrayList) wcbHelper.getEmployers(form.getDemographic());

   haveClaims = isEmpty(claims);
   haveEmps   = isEmpty(emps);
   }

   oscar.oscarBilling.ca.bc.data.BillingFormData data = new oscar.oscarBilling.ca.bc.data.BillingFormData ();
   request.setAttribute("injuryLocations",data.getInjuryLocationList());
   String fromBilling = request.getParameter("fromBilling");
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>OSCAR BC Billing - WCB</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<link rel="stylesheet" href="../../../share/css/reporting.css">
<link rel="stylesheet" href="../../../share/calendar/calendar.css">
<script src="../../../share/javascript/Oscar.js"></script>
<script src="../../../share/calendar/calendar.js"></script>
<script
	src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"
	type="text/javascript"></script>
<script src="../../../share/calendar/calendar-setup.js"
	type="text/javascript"></script>

<script language="JavaScript">


function checkAskiiData(textEle){
    var textstr = textEle.value;
    for (i=0; i<textstr.length; i++) {
        var charCode = textstr.charCodeAt(i);
        if(!validChar(charCode)){
            //textEle.focus();
            alert("Text Field Contains an extend Askii Character that is not permitted.  Please remove "+textstr.charAt(i)+" character");
            var caretPos = i;
            if(textEle.createTextRange) {
                var range = textEle.createTextRange();
                range.move('character', caretPos);
                range.select();
            }
            else {
                if(textEle.selectionStart) {
                    textEle.focus();
                    textEle.setSelectionRange(caretPos, caretPos+1);
                }
                else
                    textEle.focus();
            }
            return false;
        }

    }
    return true;

}

function validChar(ch){
    if(ch == 13 || ch == 10){  //Let new lines pass.  They will be removed anyway
        return true;
    }

    if(ch < 32 || ch > 126){
        return false;
    }
    return true;
}


function setStateofConditionalElements(){

  if(document.WCBForm.w_rphysician[0].checked == true){
		ShowElementById('secondSection2')
 }
else{
		HideElementById('secondSection2')
	}
  if(document.WCBForm.w_work[0].checked == true){
		ShowElementById('thirdSection9')
  }
  else{
		HideElementById('thirdSection9')
  }
  if(document.WCBForm.w_capability[1].checked == true){
		ShowElementById('forthSection2')
	}
	else{
		HideElementById('forthSection2')
	}
  if(document.WCBForm.w_rehab[0].checked == true){
		ShowElementById('forthSection5')
  }else{
		HideElementById('forthSection5')
  }
}
function popup( height, width, url, windowName){
  var page = url;
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(url, windowName, windowprops);
  if (popup != null){
    if (popup.opener == null){
      popup.opener = self;
    }
  }
  popup.focus();
  return false;
}

function isformNeeded(){
  if (document.WCBForm.formNeeded.checked){
     //alert("show elements");
     ShowElementById('secondSection1');
     ShowElementById('secondSection2');
     ShowElementById('secondSection3');
     ShowElementById('secondSection4');
     ShowElementById('thirdSection1');
     ShowElementById('thirdSection8');
     ShowElementById('thirdSection9');
     ShowElementById('thirdSection10');
     ShowElementById('forthSectionTitle');
     ShowElementById('forthSection1');
     ShowElementById('forthSection2');
     ShowElementById('forthSection3');
     ShowElementById('forthSection4');
     ShowElementById('forthSection5');
     ShowElementById('forthSection6');
     ShowElementById('forthSection7');
     ShowElementById('forthSection8');
     ShowElementById('secondSectionHeader');
     ShowElementById('thirdSectionHeader');
     ShowElementById('employerSectionTitle');
     ShowElementById('employerSection');
     ShowElementById('reportTypeSection');
     ShowElementById('workersAddressSection');
     setStateofConditionalElements();

  }else{
     //alert("hide elements");
     HideElementById('secondSection1');
     HideElementById('secondSection2');
     HideElementById('secondSection3');
     HideElementById('secondSection4');
     HideElementById('thirdSection1');
     HideElementById('thirdSection8');
     HideElementById('thirdSection9');
     HideElementById('thirdSection10');
     HideElementById('forthSectionTitle');
     HideElementById('forthSection1');
     HideElementById('forthSection2');
     HideElementById('forthSection3');
     HideElementById('forthSection4');
     HideElementById('forthSection5');
     HideElementById('forthSection6');
     HideElementById('forthSection7');
     HideElementById('forthSection8');
     HideElementById('secondSectionHeader');
     HideElementById('thirdSectionHeader');
     HideElementById('employerSection');
     HideElementById('employerSectionTitle');
     HideElementById('reportTypeSection');
     HideElementById('workersAddressSection');
     if (document.WCBForm.w_feeitem.value == "" && document.WCBForm.w_extrafeeitem.value != ""){
        document.WCBForm.w_feeitem.value = document.WCBForm.w_extrafeeitem.value ;
        document.WCBForm.w_extrafeeitem.value = "";
     }
  }
  }


  function HideElementById(ele){
     document.getElementById(ele).style.display='none';
  }

  function ShowElementById(ele){
     document.getElementById(ele).style.display='';
  }

  function showpic(picture,id){
     if (document.getElementById){ // Netscape 6 and IE 5+
        var targetElement = document.getElementById(picture);
        var bal = document.getElementById(id);

        var offsetTrail = document.getElementById(id);
        var offsetLeft = 0;
        var offsetTop = 0;
        while (offsetTrail) {
           offsetLeft += offsetTrail.offsetLeft;
           offsetTop += offsetTrail.offsetTop;
           offsetTrail = offsetTrail.offsetParent;
        }
        if (navigator.userAgent.indexOf("Mac") != -1 &&
           typeof document.body.leftMargin != "undefined") {
           offsetLeft += document.body.leftMargin;
           offsetTop += document.body.topMargin;
        }

        targetElement.style.left = offsetLeft +bal.offsetWidth;
        targetElement.style.top = offsetTop;
        targetElement.style.visibility = 'visible';
     }
  }

  function hidepic(picture){
     if (document.getElementById){ // Netscape 6 and IE 5+
        var targetElement = document.getElementById(picture);
        targetElement.style.visibility = 'hidden';
     }
  }

  function quickPickWCBcode(code){
  document.WCBForm.w_feeitem.value = code;
  }

  function popFeeItemList(form,field){
     var width = 575;
     var height = 400;
     var str = document.forms[form].elements[field].value;
     var url = '<rewrite:reWrite jspPage="support/billingfeeitem.jsp"/>'+'?form=' +form+ '&field='+field+'&searchStr=' +str;
     var windowName = field;
     popup(height,width,url,windowName);
  }

  function popICD9List(form,field){
     var width = 575;
     var height = 400;
     var str = document.forms[form].elements[field].value;
     var url = '<rewrite:reWrite jspPage="support/icd9.jsp"/>'+'?form=' +form+ '&field='+field+'&searchStr=' +str;
     var windowName = field;
     popup(height,width,url,windowName);
  }

  function popBodyPartList(form,field){
     var width = 650;
     var height = 400;
     var str = document.forms[form].elements[field].value;
     var url = '<rewrite:reWrite jspPage="support/bodypart.jsp"/>'+'?form=' +form+ '&field='+field+'&searchStr=' +str;
     var windowName = field;
     popup(height,width,url,windowName);
  }

  function popNOIList(form,field){
     var width = 800;
     var height = 400;
     var str = document.forms[form].elements[field].value;
     var url = '<rewrite:reWrite jspPage="support/natureinjury.jsp"/>'+'?form=' +form+ '&field='+field+'&searchStr=' +str;
     var windowName = field;
     popup(height,width,url,windowName);
  }

  function checkTextLimit(textField, maximumlength) {
     if (textField.value.length > maximumlength + 1){
        alert("Maximun "+maximumlength+" characters");
     }
     if (textField.value.length > maximumlength){
        textField.value = textField.value.substring(0, maximumlength);
     }
  }

  function setClaim(w_wcbno,w_empname,w_opaddress,w_opcity,w_emparea,w_empphone,w_icd9,w_bp,w_side,w_noi,w_doi){
     document.WCBForm.w_wcbno.value = w_wcbno;
     document.WCBForm.w_icd9.value = w_icd9;
     document.WCBForm.w_bp.value = w_bp;
     document.WCBForm.w_noi.value = w_noi;
     document.WCBForm.w_doi.value = w_doi;
     document.WCBForm.w_side.value = w_side;

     //still need side

     setEmployer(w_empname,w_opaddress,w_opcity,w_emparea,w_empphone);
  }

  function setEmployer(w_empname,w_opaddress,w_opcity,w_emparea,w_empphone){
    document.WCBForm.w_empname.value = w_empname;
    document.WCBForm.w_opaddress.value = w_opaddress;
    document.WCBForm.w_opcity.value = w_opcity;
    document.WCBForm.w_emparea.value = w_emparea;
    document.WCBForm.w_empphone.value = w_empphone;
  }
  
  function billingFormActive(){
     oscarLog("billingFormActive")
      if(window.opener && window.opener.replaceWCB){
          oscarLog("Calling on replaceWCB");
        window.opener.replaceWCB('1');  
      }
      oscarLog("billingFormActiveEnd");
  }

  function validateForm(){

    //if Check to make sure WCB ID is only numeric
    var claimNum = document.WCBForm.w_wcbno.value; 
    if (claimNum != ""){
         if (!isNumericInt(claimNum)){
            alert("Claim # has to be numeric");
            document.WCBForm.w_wcbno.focus();
            return false;
         }
     }
    
     if(!checkAskiiData(document.getElementById('w_problem'))){
         return false;
     }
     if(!checkAskiiData(document.getElementById('w_diagnosis'))){
         return false;
     }
     if(!checkAskiiData(document.getElementById('w_clinicinfo'))){
         return false;
     }
     if(!checkAskiiData(document.getElementById('w_capreason'))){
         return false;
     }
      return true;
  }

  function grabEnter(event,callb){
     if( (window.event && window.event.keyCode == 13) || (event && event.which == 13) )  {
        eval(callb);
        return false;
     }
  }
  
var WCBFeeItemCall = "popFeeItemList('WCBForm','w_feeitem')";
var extraFeeItemCall = "popFeeItemList('WCBForm','w_extrafeeitem')";
var icd9Call   = "popICD9List('WCBForm','w_icd9')";
var bodyPartCall   = "popBodyPartList('WCBForm','w_bp')";
var natureOfInjuryCall = "popNOIList('WCBForm','w_noi')";


</script>
</head>
<body onLoad="isformNeeded()" bgproperties="fixed" topmargin="0"
	leftmargin="0" rightmargin="0">
<html:errors />
<html:form action="/billing/CA/BC/formwcb" onsubmit="return validateForm()">
	<html:hidden property="w_servicelocation" />

	<!-- Params for billingBC.jsp `-->
        <input type="hidden" name="method" value="save" />
	<input type="hidden" name="billRegion" value="BC" />
	<input type="hidden" name="billForm" value="GP" />
	<input type="hidden" name="appointment_no" value="0" />
	<input type="hidden" name="demographic_name"
		value="<%=form.getW_lname() + " " + form.getW_fname()%>" />
	<input type="hidden" name="demographic_no"
		value="<%=form.getDemographic()%>" />
	<input type="hidden" name="appointment_no" value="0" />
	<input type="hidden" name="providerview"
		value="<%=form.getProviderNo()%>" />
	<input type="hidden" name="user_no"
		value="<%=(String) session.getAttribute("user")%>" />
	<input type="hidden" name="status" value="t" />
	<input type="hidden" name="apptProvider_no"
		value="<%=form.getProviderNo()%>" />
	<input type="hidden" name="bNewForm" value="1" />
	<input type="hidden" name="fromBilling" value="<%=fromBilling%>" />
	<html:hidden property="wcbFormId" />

	<%
java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd");
String fmtApptDate = fmt.format(new Date());
%>
	<input type="hidden" name="appointment_date" value="<%=fmtApptDate%>" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr bgcolor="#000000">
			<td height="40" width="10%"></td>
			<td width="90%" align="left">
			<p><font face="Verdana, Arial, Helvetica, sans-serif"
				color="#FFFFFF"> <b> <font
				face="Arial, Helvetica, sans-serif" size="4"> oscar Billing -
			Workers Compensation Board - Physician Report<font size="3"></font> </font>
			</b> </font></p>
			</td>
		</tr>
	</table>

	<br>
	<table border="0" cellspacing="0" cellpadding="0" width="100%"
		class="SmallerText">
		<tr>
                <%
                if(request.getParameter("billingcode") != null){
                    String billingcode = request.getParameter("billingcode");
                    if (WCBCodes.getInstance().isFormNeeded(billingcode)){
                        form.setFormNeeded("1");
                    }
                }              
                %>
                    
			<td>Form Needed <html:checkbox value="1" property="formNeeded"
				onclick="isformNeeded();" /></td>
			<td colspan="1" valign="top" height="25" class="SmallerText"
				id="reportTypeSection">Physician's First Report <html:radio
				value="F" property="w_reporttype" /> or The worker's condition or
			treatment has changed: <html:radio value="C" property="w_reporttype" />
			</span></td>
		</tr>

		<tr class="SectionHead">
			<td colspan="2">
			<%if(!haveClaims){%> <a
				href="javascript: function myFunction() {return false; }"
				onClick="showpic('claimLayer','claimId');" id="claimId"> <%}%> <b>WCB
			Claim Number:</B> <html:text maxlength="25" property="w_wcbno" /> *Can be left blank <%if(!haveClaims){%>
			</a> <%}%>
			</td>
		</tr>

		<tr id="employerSectionTitle">
			<td colspan="2">Employer's Info</td>
		</tr>
		<tr id="employerSection">
			<td width="100%" align="left" valign="top" colspan=2>

			<table border="0" cellspacing="0" cellpadding="0" width="100%">
				<tr id="firstSection1">
					<td class="SmallerText">
					<%if(!haveClaims){%> <a
						href="javascript: function myFunction() {return false; }"
						onClick="showpic('employerLayer','employerId');" id="employerId">
					<%}%> Employer's Name: <%if(!haveClaims){%> </a> <%}%>
					</td>
					<td><html:text maxlength="25" property="w_empname" /></td>
					<!--</tr>
				<tr id="firstSection2">-->
					<td class="SmallerText">Operating Address:</td>
					<td><html:text maxlength="25" property="w_opaddress" size="25" /></td>
				</tr>
				<tr id="firstSection3">
					<td class="SmallerText">Operating City:</td>
					<td><html:text maxlength="25" property="w_opcity" /></td>
					<!--</tr>
				<tr id="firstSection4">-->
					<td class="SmallerText">Employers Telephone No:</td>
					<td><html:text maxlength="3" property="w_emparea" size="3" />-<html:text
						maxlength="7" property="w_empphone" size="10" /></td>
					</td>
				</tr>

			</table>
			</td>
		</tr>

		<tr>
			<td width="100%" align="left" valign="top" colspan=2>


			<table cellspacing="0" cellpadding="0" border="0" width="100%">
				<tr class="SectionHead">
					<td colspan="4" class="SmallerText">Worker's Info</td>
				</tr>
				<tr>
					<td colspan="1" class="SmallerText"><b>Last Name:</b> <html:text
						maxlength="18" property="w_lname" size="20" /></td>
					<td class="SmallerText"><b>First Name:</b> <html:text
						maxlength="12" property="w_fname" size="20" /></td>
					<td class="SmallerText" colspan="2">Initial: <html:text
						maxlength="1" property="w_mname" size="1" /></td>

				</tr>
				<tr>

					<td class="SmallerText"><b>Date of Birth:</b> <html:text
						readonly="readonly" maxlength="10" property="w_dob" size="10" />
					</td>
					<td class="SmallerText"><b>Gender:</b> <html:text
						readonly="readonly" maxlength="1" property="w_gender" size="1" />
					</td>
					<td class="SmallerText"><b>PHN:</b> <html:text maxlength="12"
						property="w_phn" size="12" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr id="workersAddressSection">
					<td class="SmallerText">Worker's Telephone No: <html:text
						maxlength="3" property="w_area" size="3" /> <html:text
						maxlength="7" property="w_phone" size="7" /></td>
					<td class="SmallerText">Address: <html:text maxlength="25"
						property="w_address" size="20" /></td>
					<td class="SmallerText">City: <html:text maxlength="20"
						property="w_city" size="20" /></td>
					<td class="SmallerText">Postal Code: <html:text maxlength="6"
						property="w_postal" size="6" /></td>


					</td>
				</tr>
				<tr>
					<td colspan="4" class="SmallerText">&nbsp;</td>
				</tr>

			</table>
			</span></td>

		</tr>
		<tr class="SectionHead" id="secondSectionHeader">
			<td colspan="2">&nbsp;</td>
		</tr>

		<tr id="secondSection1">
			<td>Are you the worker's regular physician?</td>
			<td><html:radio value="Y" property="w_rphysician"
				onclick="ShowElementById('secondSection2')" /> Yes <html:radio
				value="N" property="w_rphysician"
				onclick="HideElementById('secondSection2')" /> No</td>
		</tr>
		<tr id="secondSection2">
			<td>If yes, how long has the worker been your patient?</td>
			<td><html:radio value="1" property="w_duration" /> 0-6 months <html:radio
				value="2" property="w_duration" /> 7-12 months <html:radio value="9"
				property="w_duration" />&gt; 12 months</td>
		</tr>
		<tr id="secondSection3">
			<td>Who rendered the first treatment?</td>
			<td><html:text maxlength="25" property="w_ftreatment" size="25" />
			</td>
		</tr>
		<tr id="secondSection4">
			<td>Prior/Other Problems Affecting Injury, Recovery and
			Disability</td>
			<td><html:textarea cols="50" onkeyup="checkTextLimit(this.form.w_problem,160);" styleId="w_problem"
				property="w_problem" style="height:50px;width:100%;"></html:textarea>
			</td>
		</tr>

		<tr class="SectionHead" id="thirdSectionHeader">
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td class="SectionHead">Injury Codes and Descriptions</td>
			<td><b>Date of Service</b> <html:text property="w_servicedate"
				styleId="w_servicedate" size="10" readonly="true" /> <a id="hlSDate"><img
				title="Calendar" src="../../../images/cal.gif" alt="Calendar"
				border="0" /></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>Date of Injury:</b>
			<html:text styleId="w_doi" property="w_doi" readonly="true" size="12" />
			<a id="hlDDate"><img title="Calendar"
				src="../../../images/cal.gif" alt="Calendar" border="0" /></a></td>
		</tr>
		<tr id="thirdSection1">
			<td>Diagnosis:</td>
			<td><html:text maxlength="120" property="w_diagnosis" styleId="w_diagnosis" size="120" />
			</td>
		</tr>
		<tr>
			<td><a href="javascript: function myFunction() {return false; }"
				onClick="showpic('Layer1','Calcs');" id="Calcs">WCB code: </a>

			</td>


			<td><html:text maxlength="5" property="w_feeitem" size="5" onkeypress="return grabEnter(event,WCBFeeItemCall)" />
                            <a onClick="popFeeItemList('WCBForm','w_feeitem');">Search</a></td>
		</tr>
		<tr>
			<td>Service code:</td>
			<td><html:text maxlength="5" property="w_extrafeeitem" size="5" onkeypress="return grabEnter(event,extraFeeItemCall)" />
			<a onClick="popFeeItemList('WCBForm','w_extrafeeitem');">Search</a></td>
		</tr>
		<tr>
			<td><b>ICD9:</b></td>
			<td><html:text maxlength="5" property="w_icd9" size="5" onkeypress="return grabEnter(event,icd9Call)" /> <a
				onClick="popICD9List('WCBForm','w_icd9');">Search</a></td>
		</tr>
		<tr>
			<td><b>Body Part:</b></td>
			<td><html:text maxlength="5" property="w_bp" size="5" onkeypress="return grabEnter(event,bodyPartCall)" /> <a
				onClick="popBodyPartList('WCBForm','w_bp');">Search</a></td>
		</tr>
		<tr>
			<td><b>Side:</b></td>
			<td><html:select property="w_side">
				<html:options collection="injuryLocations" property="sidetype"
					labelProperty="sidedesc" />
			</html:select></td>
		</tr>
		<tr>
			<td><b>Nature of Injury</b></td>
			<td><html:text maxlength="5" property="w_noi" size="5" onkeypress="return grabEnter(event,natureOfInjuryCall)" /> <a
				onClick="popNOIList('WCBForm','w_noi');">Search</a></td>
		</tr>
		<tr id="thirdSection8">
			<td>From injury or since last report, has the worker been
			disabled from work?</td>
			<td><html:radio value="Y" property="w_work"
				onclick="ShowElementById('thirdSection9')" />Yes <html:radio
				value="N" property="w_work"
				onclick="HideElementById('thirdSection9')" />No
			</p>
			</td>
		</tr>
		<tr id="thirdSection9">
			<td>If Yes, as of what date? (if known)</td>
			<td align="left" valign="top"><html:text readonly="true"
				styleId="w_workdate" property="w_workdate"
				onclick="window.WCBForm.w_workdate.value = '';" /> <a id="hlWDate"><img
				title="Calendar" src="../../../images/cal.gif" alt="Calendar"
				border="0" /></a></td>
		</tr>
		<tr id="thirdSection10">
			<td align="left" valign="top">Clinical Information:<br>
			<small>What happened?<br>
			Subjective Symptoms<br>
			Examination<br>
			Investigations<br>
			Treatment, Meds</small></td>
			<td align="left" valign="top"><html:textarea
				styleClass="mhAssTextarea"
				onkeyup="checkTextLimit(this.form.w_clinicinfo,800);" styleId="w_clinicinfo"
				property="w_clinicinfo" style="height:80px;width:100%;"></html:textarea>
			</td>
		</tr>
		<tr id="forthSectionTitle">
			<td colspan="2" class="SectionHead">Return to Work Planning</td>
		</tr>
		<tr id="forthSection1">
			<td>Is the worker now medically capable of working full duties,
			full time?</td>
			<td><html:radio value="Y" property="w_capability"
				onclick="HideElementById('forthSection2')" /> Yes <html:radio
				value="N" property="w_capability"
				onclick="ShowElementById('forthSection2')" /> No</td>
		</tr>
		<tr id="forthSection2">
			<td valign="top">If No: What are the current physical and/or
			psychological restrictions?</td>
			<td><html:textarea styleClass="mhAssTextarea"
				onkeyup="checkTextLimit(this.form.w_capreason,240);" styleId="w_capreason"
				property="w_capreason" style="height:80px;width:100%;"></html:textarea>
			</td>
		</tr>
		<tr id="forthSection3">
			<td valign="top">Estimated time before the worker will be able
			to return to the workplace.</td>
			<td><html:radio value="0" property="w_estimate" />At Work<br>
			<html:radio value="1" property="w_estimate" />1-6 days <html:radio
				value="2" property="w_estimate" />7-13 days<br>
			<html:radio value="3" property="w_estimate" />14-20 days <html:radio
				value="9" property="w_estimate" />20 days</td>
		</tr>
		<tr id="forthSection4">
			<td>If appropriate, is the worker now ready for a rehabilitation
			program?</td>
			<td><html:radio value="Y" property="w_rehab"
				onclick="ShowElementById('forthSection5')" /> Yes <html:radio
				value="N" property="w_rehab"
				onclick="HideElementById('forthSection5')" /> No</td>
		</tr>
		<tr id="forthSection5">
			<td>If Yes, Select &quot;WCP&quot; or Other</td>
			<td><html:radio value="C" property="w_rehabtype" />WCP <html:radio
				value="O" property="w_rehabtype" />Other</td>
		</tr>
		<tr id="forthSection6">
			<td>Do you wish to consult with WCB physician or nurse advisor?</td>
			<td><html:radio value="Y" property="w_wcbadvisor" /> Yes <html:radio
				value="N" property="w_wcbadvisor" /> No</td>
		</tr>
		<tr id="forthSection7">
			<td>If possible, please estimate date of Maximal Medical
			Recovery</td>
			<td><html:text readonly="readonly" styleId="w_estimatedate"
				property="w_estimatedate"
				onclick="window.WCBForm.w_estimatedate.value = '';" /><a
				id="hlEDate"> <img title="Calendar"
				src="../../../images/cal.gif" alt="Calendar" border="0" /></a></td>
		</tr>
		<tr id="forthSection8">
			<td>Further Correspondence to Follow: (2nd electronic form or
			paper)</td>
			<td><html:radio value="Y" property="w_tofollow" /> Yes <html:radio
				value="N" property="w_tofollow" /> No</td>
		</tr>
		<tr>
			<td>Payee Number: <html:text readonly="readonly"
				property="w_payeeno" size="5" /></td>
			<td>Provider No: <html:text readonly="readonly"
				property="w_pracno" size="5" />(<%=form.getW_pracname()%>)</td>
		</tr>
		<tr>
			<td colspan="2" align="center" valign="top" class="SectionHead">
                            <input type="button" onclick="checkAskiiData(document.getElementById('w_diagnosis'))"/>

			    <html:hidden property="doValidate" />
                            <%if(hideToBill){ %>
                            <hidden name="hideToBill" value="true"/>
                            <%}%>
                            <input type="submit" name="save" value="Save" />| 
                            <input type="submit" name="saveAndClose" value="Save and Close" />|  
                            <%if( !hideToBill){%>
                            <input type="submit" name="saveandbill" value="Save and Bill" />|
                            <%}%> 
                            <input type="button" value="Cancel" onClick="window.close();" /> | 
                            <input type="button" value="Print" onClick="window.print()" />
                        </td>
		</tr>
	</table>
	<script language='javascript'>
   Calendar.setup({inputField:"w_servicedate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});
	//Calendar.setup({inputField:"w_servicedate",ifFormat:"y-m-d",button:"hlSDate",align:"Bl",singleClick:true});

	Calendar.setup({inputField:"w_estimatedate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlEDate",singleClick:true,step:1});
	//Calendar.setup({inputField:"w_estimatedate",ifFormat:"y-m-d",button:"hlEDate",align:"Tl",singleClick:true});

   Calendar.setup({inputField:"w_workdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlWDate",singleClick:true,step:1});
   //Calendar.setup({inputField:"w_workdate",ifFormat:"y-m-d",button:"hlWDate",align:"Bl",singleClick:true});

	Calendar.setup({inputField:"w_doi",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlDDate",singleClick:true,step:1});
   //Calendar.setup({inputField:"w_doi",ifFormat:"y-m-d",button:"hlDDate",align:"Bl",singleClick:true});
</script>
</html:form>



<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<div id="Layer1"
	style="position: absolute; left: 1px; top: 1px; width: 400px; height: 311px; visibility: hidden; z-index: 1">
<!--  This should be changed to automagically fill if this changes often -->
<table width="100%" border="1" cellspacing="0" cellpadding="1" style="font-size: 10px;"
	align=center>
	<tr class="SectionHead">
		<td class="wcblayerTitle">Code</td>
		<td class="wcblayerTitle">&nbsp;</td>
		<td class="wcblayerTitle">Description</td>
		<td class="wcblayerTitle" align="right"><a
			href="javascript: function myFunction() {return false; }"
			onClick="hidepic('Layer1');" style="text-decoration: none;">X</a></td>
	</tr>

	<tr class="SectionHead">
		<td class="wcblayerTitle"><a href="#"
			onClick="quickPickWCBcode('19937');hidepic('Layer1');return false;">
		19937 </a></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td colspan="2" class="wcblayerItem">WCB FIRST TELEPLAN E-FORM
		REPORT OF INJURY</td>
	</tr>

	<tr class="SectionHead">
		<td class="wcblayerTitle"><a href="#"
			onClick="quickPickWCBcode('19938');hidepic('Layer1');return false;">
		19938 </a></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td colspan="2" class="wcblayerItem">WCB TELEPLAN E-FORM REC'D
		WITHIN 6-7 DAYS</td>
	</tr>
	<tr class="SectionHead">
		<td class="wcblayerTitle"><a href="#"
			onClick="quickPickWCBcode('19939');hidepic('Layer1');return false;">
		19939 </a></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td colspan="2" class="wcblayerItem">WCB TELEPLAN E-FORM 8 REC'D WITHIN 10 DAYS OF FAX</td>
	</tr>

	<tr class="SectionHead">
		<td class="wcblayerTitle"><a href="#"
			onClick="quickPickWCBcode('19940');hidepic('Layer1');return false;">
		19940 </a></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td colspan="2" class="wcblayerItem">WCB TELEPLAN E-FORM 11
		REC'D WITHIN 5 DAYS OF SERV</td>
	</tr>
	<tr class="SectionHead">
		<td class="wcblayerTitle"><a href="#"
			onClick="quickPickWCBcode('19941');hidepic('Layer1');return false;">
		19941 </a></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td colspan="2" class="wcblayerItem">WCB TELEPLAN E-FORM 11
		REC'D WITHIN 6-7 DAYS</td>
	</tr>
	<tr class="SectionHead">
		<td class="wcblayerTitle"><a href="#"
			onClick="quickPickWCBcode('19943');hidepic('Layer1');return false;">
		19943 </a></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td colspan="2" class="wcblayerItem">WCB E-FORM 8 RESUBMISSION,
		NO CHARGE</td>
	</tr>
	<tr class="SectionHead">
		<td class="wcblayerTitle"><a href="#"
			onClick="quickPickWCBcode('19944');hidepic('Layer1');return false;">
		19944 </a></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td colspan="2" class="wcblayerItem">WCB E-FORM 11 SUBMISSION</td>
	</tr>
</table>

</div>



<div id="claimLayer"
	style="position: absolute; left: 1px; top: 1px; width: 350px; height: 311px; visibility: hidden; z-index: 1">
<!--  This should be changed to automagically fill if this changes often -->
<table width="98%" border="0" cellspacing="1" cellpadding="1"
	align=center>
	<tr class="SectionHead">
		<td class="wcblayerTitle">Claim #</td>
		<td class="wcblayerTitle" align="right"><a
			href="javascript: function myFunction() {return false; }"
			onClick="hidepic('claimLayer');" style="text-decoration: none;">X</a>
		</td>
	</tr>

	<% for (int i = 0 ; i < claims.size(); i++){
      WcbHelper.WCBClaim claim = (WcbHelper.WCBClaim) claims.get(i);
      WcbHelper.WCBEmployer emp = claim.wcbEmp;
      if (claim.getClaimNumber() != null && claim.getClaimNumber().trim().equals("")){
        continue;
      }
    %>
	<tr class="SectionHead">
		<td class="wcblayerTitle">
                    <a href="#"
			onClick="setClaim('<%=claim.getClaimNumber()%>','<%=emp.w_empname%>','<%=emp.w_opaddress%>','<%=emp.w_opcity%>','<%=emp.w_emparea%>','<%=emp.w_empphone%>','<%=claim.w_icd9%>','<%=claim.w_bp%>','<%=claim.w_side%>','<%=claim.w_noi%>','<%=claim.w_doi%>');hidepic('claimLayer');return false;">
		<%=claim.getClaimNumber()%> </a></td>
		<td class="wcblayerItem">&nbsp;</td>

	</tr>
	<%} %>
</table>

</div>


<div id="employerLayer"
	style="position: absolute; left: 1px; top: 1px; width: 350px; height: 311px; visibility: hidden; z-index: 1">
<!--  This should be changed to automagically fill if this changes often -->
<table width="98%" border="0" cellspacing="1" cellpadding="1"
	align=center>
	<tr class="SectionHead">



		<td class="wcblayerTitle">Employer's Name</td>
		<td class="wcblayerTitle">Operating Address</td>
		<td class="wcblayerTitle">Operating City</td>
		<td class="wcblayerTitle">Employers Telephone No</td>
		<td class="wcblayerTitle" align="right"><a
			href="javascript: function myFunction() {return false; }"
			onClick="hidepic('employerLayer');" style="text-decoration: none;">X</a>
		</td>
	</tr>

	<% for (int i = 0 ; i < emps.size(); i++){
      WcbHelper.WCBEmployer emp = (WcbHelper.WCBEmployer) emps.get(i);
    %>
	<tr class="SectionHead">
		<td class="wcblayerTitle"><a href="#"
			onClick="setEmployer('<%=emp.w_empname%>','<%=emp.w_opaddress%>','<%=emp.w_opcity%>','<%=emp.w_emparea%>','<%=emp.w_empphone%>');hidepic('employerLayer');return false;">
		<%=emp.w_empname%> </a></td>
		<td class="wcblayerTitle"><%=emp.w_opaddress%></td>
		<td class="wcblayerTitle"><%=emp.w_opcity%></td>
		<td class="wcblayerTitle"><%=emp.w_emparea%>-<%=emp.w_empphone%></td>
		<td colspan="2" class="wcblayerItem">&nbsp;</td>
	</tr>
	<%}%>
</table>

</div>



</body>
</html:html>


<%!

String checked(String val,String str,boolean dfault){
    String retval = "";
    if(str == null || str.equals("null")){
        str = "";
    }
    if (str.equals("")  && dfault){
        retval = "CHECKED";
    }else if (str != null && str.equalsIgnoreCase(val) ){
        retval = "CHECKED";
    }
    return retval;
}

boolean isEmpty(ArrayList a){
   boolean isEmpty = false;
   if ( a.size() == 0 ) isEmpty = true;
   return isEmpty;
}
%>
