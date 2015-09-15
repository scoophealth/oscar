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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.form.*, oscar.OscarProperties"%>
<%@page import="org.oscarehr.common.dao.FrmLabReqPreSetDao, org.oscarehr.util.SpringUtils"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Laboratory Requisition</title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="labReqStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<script src="../share/javascript/prototype.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<%
	String formClass = "LabReq";
	String formLink = "formlabreq.jsp";

   boolean readOnly = false;
   int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
   int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
   java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
        
	props = ((FrmLabReqRecord) rec).getFormCustRecord(props, provNo);
   OscarProperties oscarProps = OscarProperties.getInstance();

   if (request.getParameter("labType") != null){
      if (formId == 0 ){
         FrmLabReqPreSetDao preSetDao = (FrmLabReqPreSetDao) SpringUtils.getBean("frmLabReqPreSetDao"); 
         String labPreSet = request.getParameter("labType");
         props = preSetDao.fillPropertiesByLabType(labPreSet,props);
      }
   }
   
   if (request.getParameter("readOnly") != null){
      readOnly = true;    
   }

   String patientName = props.getProperty("patientName"," , ");
   String[] patientNames = patientName.split(",");
   
   String[] patientDOB = props.getProperty("birthDate", " / / ").split("/");
   request.removeAttribute("submit");
%>

<script type="text/javascript" language="Javascript">

var temp;
temp = "";


    function onPrint(pdf) {
         
        var ret = checkAllDates();
        if(ret==true)
        {            
            
            //ret = confirm("Do you wish to save this form and view the print preview?");
            //popupFixedPage(650,850,'../provider/notice.htm');
            temp=document.forms[0].action;
            
            if( pdf ) {                
                document.forms[0].action = "<rewrite:reWrite jspPage="formname.do?__title=Lab+Request&__cfgfile=labReqPrint&__template=newReqLab"/>";
                document.forms[0].submit.value="printall"; 
                document.forms[0].target="_self";
            }
            else {                
                document.forms[0].action = "<rewrite:reWrite jspPage="formname.do"/>";
                document.forms[0].submit.value="printLabReq"; 
                document.forms[0].target="labReqPrint";          
            }
                
              
        }
        return ret;
    }
    function onSave() {
        if (temp != "") { document.forms[0].action = temp; }
        document.forms[0].target="_self";        
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true)
        {
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        if (temp != "") { document.forms[0].action = temp; }
        document.forms[0].target="_self";
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
function popupFixedPage(vheight,vwidth,varpage) { 
  var page = "" + varpage;
  windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
  var popup=window.open(page, "planner", windowprop);
}

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=3100;

    function isInteger(s){
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        // All characters are numbers.
        return true;
    }

    function stripCharsInBag(s, bag){
        var i;
        var returnString = "";
        // Search through string's characters one by one.
        // If character is not in bag, append to returnString.
        for (i = 0; i < s.length; i++){
            var c = s.charAt(i);
            if (bag.indexOf(c) == -1) returnString += c;
        }
        return returnString;
    }

    function daysInFebruary (year){
        // February has 29 days in any year evenly divisible by four,
        // EXCEPT for centurial years which are not also divisible by 400.
        return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
    }
    function DaysArray(n) {
        for (var i = 1; i <= n; i++) {
            this[i] = 31
            if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
            if (i==2) {this[i] = 29}
       }
       return this
    }

    function isDate(dtStr){
        var daysInMonth = DaysArray(12)
        var pos1=dtStr.indexOf(dtCh)
        var pos2=dtStr.indexOf(dtCh,pos1+1)
        var strMonth=dtStr.substring(0,pos1)
        var strDay=dtStr.substring(pos1+1,pos2)
        var strYear=dtStr.substring(pos2+1)
        strYr=strYear
        if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
        if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
        for (var i = 1; i <= 3; i++) {
            if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
        }
        month=parseInt(strMonth)
        day=parseInt(strDay)
        year=parseInt(strYr)
        if (pos1==-1 || pos2==-1){
            return "format"
        }
        if (month<1 || month>12){
            return "month"
        }
        if (day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
            return "day"
        }
        if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
            return "year"
        }
        if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
            return "date"
        }
    return true
    }


    function checkTypeIn(obj) {
      if(!checkTypeNum(obj.value) ) {
          alert ("You must type in a number in the field.");
        }
    }

    function valDate(dateBox)
    {
        try
        {
            var dateString = dateBox.value;
            if(dateString == "")
            {
    //            alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            var y = dt[0];
            var m = dt[1];
            var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true)
            {
                var s = dateBox.name;
                alert('Invalid '+pass+' in field ' + s.substring(3));
                dateBox.focus();
                return false;
            }
        }
        catch (ex)
        {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].formDate)==false){
            b = false;
        }
        return b;

    }

    function popup(link) {
    windowprops = "height=700, width=960,location=no,"
    + "scrollbars=yes, menubars=no, toolbars=no, resizable=no, top=0, left=0 titlebar=yes";
    window.open(link, "_blank", windowprops);
}
</script>

<body style="page: doublepage; page-break-after: right">
<html:form action="/form/formname">

	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="patientLastName"
		value="<%=patientNames[0].trim()%>" />
	<input type="hidden" name="patientFirstName"
		value="<%=patientNames[1].trim()%>" />
	<input type="hidden" name="patientBirthYear"
		value="<%=patientDOB[0].trim()%>" />
	<input type="hidden" name="patientBirthMth"
		value="<%=patientDOB[1].trim()%>" />
	<input type="hidden" name="patientBirthDay"
		value="<%=patientDOB[2].trim()%>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />
	<input type="hidden" name="formId" value="<%=formId%>" />

	<table class="Head" class="hidePrint">
		<tr>
			<td nowrap="true">
			<% if(!readOnly){ %> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <% } %>
			<input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print Pdf" onclick="javascript:return onPrint(true);" /></td>
		</tr>
	</table>

	<!-- class="TableWithBorder" -->
	<table class="outerTable" width="100%">
		<tr>
			<td>
			<table width="100%">
				<tr>
					<td class="title" colspan="3" nowrap="true">LABORATORY
					REQUISITION</td>
				</tr>
				<tr>
					<td colspan="3" nowrap="true">Requisitioning
					Physician/Practitioner:<br>
					<input type="hidden" style="width: 100%" name="provName"
						value="<%=props.getProperty("provName", "")%>" /> <input
						type="hidden" style="width: 100%" name="reqProvName"
						value="<%=props.getProperty("reqProvName", "")%>" /> <%=props.getProperty("reqProvName", "")%>&nbsp;<br>
					<%-- Dr. Hunter wants the form to say "Physician" instead of "Family Physician".  This is a quick and dirty hack to make it work.  This
     should really be rewritten more elegantly at some later point in time. --%>
					<br><%=oscarProps.getProperty("clinic_no", "").startsWith("1022")?"Physician:":"Family Physician:"%><br>
					<%=props.getProperty("provName", "")==null?"":props.getProperty("provName", "")%>&nbsp;<br>
					<input type="hidden" style="width: 100%" name="clinicName"
						value="<%=props.getProperty("clinicName","")%>" /> <%=props.getProperty("clinicName","")%>&nbsp;<br>
					<input type="hidden" style="width: 100%" name="clinicAddress"
						value="<%=props.getProperty("clinicAddress", "")%>" /> <%=props.getProperty("clinicAddress", "")%>&nbsp;<br>
					<input type="hidden" style="width: 100%" name="clinicCity"
						value="<%=props.getProperty("clinicCity", "")%>" /> <%=props.getProperty("clinicCity", "")%>,<%=props.getProperty("clinicProvince","") %><br>
					<input type="hidden" style="width: 100%" name="clinicPC"
						value="<%=props.getProperty("clinicPC", "")%>" /> <%=props.getProperty("clinicPC", "")%>&nbsp;<br>
					</td>
				</tr>
				<tr>
					<td>
					<table width="100%" border="1"
						style="border-right: 0; border-bottom: 0;" cellspacing="0">
						<tr>
							<td colspan="3">Physician/Practitioner Number<br>
							<input type="hidden" name="practitionerNo"
								value="<%=props.getProperty("practitionerNo", "")%>" /> <%=props.getProperty("practitionerNo", "")%>&nbsp;
							</td>
						</tr>
						<tr>
							<td>
							<table>
								<tr>
									<td nowrap="true" valign="top">Check one:</td>
									<td><input type="checkbox" name="ohip"
										<%=props.getProperty("ohip", "")%> /><br>
									<input type="checkbox" name="thirdParty"
										<%=props.getProperty("thirdParty", "")%> /><br>
									<input type="checkbox" name="wcb"
										<%=props.getProperty("wcb", "")%> /><br>
									</td>
									<td nowrap="true">OHIP/Insured<br>
									Third Party/Uninsured<br>
									WCB</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="3">Additional Clinical Information<br>
							<textarea name="aci" style="width: 100%; height: 59px;"><%=props.getProperty("aci", "")%></textarea>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
			<td width="100%">
			<table width="100%" cellspacing="0" border="1"
				style="border-bottom: 0;">
				<tr>
					<td class="lab" valign="top">Laboratory Number <br>
					<br>
					<br>
					<br>
					<br>
					</td>
					<td class="lab" colspan="2" rowspan="2" valign="top" width="65%">
					Laboratory Name and Address</td>
				</tr>
				<tr>
					<td class="lab" valign="top">Total Fee <br>
					<br>
					<br>
					<br>
					</td>
				</tr>
				<tr>
					<td class="lab" valign="top">Laboratory Accounting Number<br>
					<br>
					</td>
					<td class="lab" valign="top">Service Date (yyyy/mm/dd)</td>
					<td class="lab" valign="top">Ref. Lab.</td>
				</tr>
				<tr>
					<td colspan="3">
					<table width="100%">
						<tr>
							<td width="33%"><input type="hidden" style="width: 90%"
								name="patientName"
								value="<%=props.getProperty("patientName", "")%>" /> <%=props.getProperty("patientName", "")%>&nbsp;
							</td>
							<td>Health Number:</td>
							<td><input type="hidden" name="healthNumber" size="10"
								value="<%=props.getProperty("healthNumber", "")%>" /> <%=props.getProperty("healthNumber", "")%>&nbsp;
							</td>
							<td>Province:</td>
							<td><input type="hidden" name="province" size="12"
								value="<%=props.getProperty("province", "")%>" /> <%=props.getProperty("province", "")%>&nbsp;
							</td>
						</tr>
						<tr>
							<td><input type="hidden" style="width: 90%"
								name="patientAddress"
								value="<%=props.getProperty("patientAddress", "")%>" /> <%=props.getProperty("patientAddress", "")%>
							</td>
							<td>Version:</td>
							<td><input type="hidden" name="version" size="10"
								value="<%=props.getProperty("version", "")%>" /> <%=props.getProperty("version", "")%>
							</td>
							<td>Other Registration Number:</td>
							<td><input type="text" name="orn" size="12"
								value="<%=props.getProperty("orn", "")%>" /></td>
						</tr>
						<td><input type="hidden" style="width: 90%"
							name="patientCity"
							value="<%=props.getProperty("patientCity", "")%>" /> <%=props.getProperty("patientCity", "")%>
						</td>
						<td>Date of Birth:</td>
						<td><input type="hidden" name="birthDate" size="10"
							value="<%=props.getProperty("birthDate", "")%>" /> <%=props.getProperty("birthDate", "")%>
						</td>
						<td>Phone Number:</td>
						<td><input type="hidden" name="phoneNumber" size="12"
							value="<%=props.getProperty("phoneNumber", "")%>" /> <%=props.getProperty("phoneNumber", "")%>
						</td>
						</tr>
						<td><input type="hidden" style="width: 90%" name="patientPC"
							value="<%=props.getProperty("patientPC", "")%>" /> <%=props.getProperty("patientPC", "")%>
						</td>
						<td>Payment Program:</td>
						<td><input type="text" name="paymentProgram" size="10"
							value="<%=props.getProperty("paymentProgram", "")%>" /></td>
						<td>Sex:</td>
						<td><input type="hidden" name="sex" size="12"
							value="<%=props.getProperty("sex", "")%>" /> <%=props.getProperty("sex", "")%>
						</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
			<table class="test" width="100%" border="1" cellspacing="0">
				<tr>
					<td width="33%">
					<table border="1"
						style="border-top: 0; border-left: 0; border-right: 0; border-bottom: 0;"
						cellspacing="0">
						<tr class="testType">
							<td style="border-right: 0;">&nbsp;</td>
							<td style="border-left: 0; width: 100%;"><a>Biochemistry</a>
							</td>
							<td class="code" nowrap="true">LAB CODE</td>
							<td class="code" nowrap="true">FEE CODE</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_glucose"
								<%=props.getProperty("b_glucose", "")%> /></td>
							<td>Glucose</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_creatine"
								<%=props.getProperty("b_creatine", "")%> /></td>
							<td>Creatinine</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_uricAcid"
								<%=props.getProperty("b_uricAcid", "")%> /></td>
							<td>Uric Acid</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_sodium"
								<%=props.getProperty("b_sodium", "")%> /></td>
							<td>Sodium</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_potassium"
								<%=props.getProperty("b_potassium", "")%> /></td>
							<td>Potassium</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_chloride"
								<%=props.getProperty("b_chloride", "")%> /></td>
							<td>Chloride</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_ast"
								<%=props.getProperty("b_ast", "")%> /></td>
							<td>AST (SGOT)</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_alkPhosphate"
								<%=props.getProperty("b_alkPhosphate", "")%> /></td>
							<td>Alk. Phosphate</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_bilirubin"
								<%=props.getProperty("b_bilirubin", "")%> /></td>
							<td>Bilirubin</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_cholesterol"
								<%=props.getProperty("b_cholesterol", "")%> /></td>
							<td>Cholesterol</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_triglyceride"
								<%=props.getProperty("b_triglyceride", "")%> /></td>
							<td>Triglyceride</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="b_urinalysis"
								<%=props.getProperty("b_urinalysis", "")%> /></td>
							<td nowrap="true">Urinalysis (chemical)</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4">
							<table width="100%">
								<tr>
									<td colspan="4">Viral Hepatitis (check <u>one</u> only)</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="v_acuteHepatitis"
										<%=props.getProperty("v_acuteHepatitis", "")%> /></td>
									<td colspan="3">Acute hepatitis</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="v_chronicHepatitis"
										<%=props.getProperty("v_chronicHepatitis", "")%> /></td>
									<td colspan="3">Chronic hepatitis</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="v_immune"
										<%=props.getProperty("v_immune", "")%> /></td>
									<td colspan="3">Immune status / prev. exposure</td>
								<tr>
									<td colspan="2">Specify:</td>
									<td>Hepatitis A</td>
									<td><input type="text" name="v_hepA"
										value="<%=props.getProperty("v_hepA", "")%>"
										style="width: 100%;" /></td>
								</tr>
								<tr>
									<td colspan="2">&nbsp;</td>
									<td>Hepatitis B</td>
									<td><input type="text" name="v_hepB"
										value="<%=props.getProperty("v_hepB", "")%>"
										style="width: 100%;" /></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="4">
							<table>
								<tr>
									<td colspan="2">"I certify the tests ordered are not for
									registered in or out patients of a hospital".<br>
									<br>
									</td>
								</tr>
								<tr>
									<td>Signature</td>
									<td>Date</td>
								</tr>
								<tr>
									<td>_________________</td>
									<td><input type="text" name="formDate" size="10"
										value="<%=props.getProperty("formDate", "")%>" /></td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
					<td width="33%">
					<table border="1"
						style="border-top: 0; border-left: 0; border-right: 0; border-bottom: 0;"
						cellspacing="0">
						<tr class="testType">
							<td style="border-right: 0;">&nbsp;</td>
							<td style="border-left: 0; width: 100%;"><a>Hematology</a></td>
							<td class="code" nowrap="true">LAB CODE</td>
							<td class="code" nowrap="true">FEE CODE</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="h_bloodFilmExam"
								<%=props.getProperty("h_bloodFilmExam", "")%> /></td>
							<td nowrap="true">Blood Film Exam</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="h_hemoglobin"
								<%=props.getProperty("h_hemoglobin", "")%> /></td>
							<td nowrap="true">Hemoglobin</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="h_wcbCount"
								<%=props.getProperty("h_wcbCount", "")%> /></td>
							<td nowrap="true">W.C.B. count</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="h_hematocrit"
								<%=props.getProperty("h_hematocrit", "")%> /></td>
							<td nowrap="true">Hematocrit</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="h_prothrombTime"
								<%=props.getProperty("h_prothrombTime", "")%> /></td>
							<td nowrap="true">Prothromb. time</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="h_otherC"
								<%=props.getProperty("h_otherC", "")%> /></td>
							<td style="padding-bottom: 1px"><input type="text"
								name="h_other" value="<%=props.getProperty("h_other", "")%>" /></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr class="testType">
							<td style="border-right: 0;">&nbsp;</td>
							<td colspan="3" style="border-left: 0; width: 100%;"><a>Immunology</a>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="i_pregnancyTest"
								<%=props.getProperty("i_pregnancyTest", "")%> /></td>
							<td nowrap="true">Pregnancy Test</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="i_heterophile"
								<%=props.getProperty("i_heterophile", "")%> /></td>
							<td>Heterophile antibodies screen</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="i_rubella"
								<%=props.getProperty("i_rubella", "")%> /></td>
							<td nowrap="true">Rubella</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="i_prenatal"
								<%=props.getProperty("i_prenatal", "")%> /></td>
							<td>Prenatal: <small>ABO, RhD, anitbody screen
							(titre and ident. if positive</small></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="i_repeatPrenatal"
								<%=props.getProperty("i_repeatPrenatal", "")%> /></td>
							<td>Repeat Prenatal antibodies</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="i_prenatalHepatitisB"
								<%=props.getProperty("i_prenatalHepatitisB", "")%> /></td>
							<td nowrap="true">Prenatal Hepatitis B</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="i_vdrl"
								<%=props.getProperty("i_vdrl", "")%> /></td>
							<td nowrap="true">VDRL</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="i_otherC"
								<%=props.getProperty("i_otherC", "")%> /></td>
							<td style="padding-bottom: 1px"><input type="text"
								name="i_other" value="<%=props.getProperty("i_other", "")%>" /></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr class="testType">
							<td style="border-right: 0;">&nbsp;</td>
							<td colspan="3" style="border-left: 0; width: 100%;"><a>Microbiology</a>
							Sensitivities if warranted</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="m_cervicalVaginal"
								<%=props.getProperty("m_cervicalVaginal", "")%> /></td>
							<td nowrap="true">Cervical, vaginal</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="m_sputum"
								<%=props.getProperty("m_sputum", "")%> /></td>
							<td nowrap="true">Sputum</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="m_throat"
								<%=props.getProperty("m_throat", "")%> /></td>
							<td nowrap="true">Throat</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="m_urine"
								<%=props.getProperty("m_urine", "")%> /></td>
							<td nowrap="true">Urine</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="m_stoolCulture"
								<%=props.getProperty("m_stoolCulture", "")%> /></td>
							<td nowrap="true">Stool culture</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td valign="top"><input type="checkbox" name="m_otherSwabs"
								<%=props.getProperty("m_otherSwabs", "")%> /></td>
							<td nowrap="true" style="padding-bottom: 2px"><input
								type="text" style="width: 100%;" name="m_other"
								value="<%=props.getProperty("m_other", "")%>" /></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					</td>
					<td width="33%">
					<table border="1"
						style="border-top: 0; border-left: 0; border-right: 0; border-bottom: 0;"
						cellspacing="0">
						<tr class="testType">
							<td><a>Other test, one per line</a> (please use terminology
							of the Schedule of Benefits)</td>
							<td class="code">LAB CODE</td>
							<td class="code">FEE CODE</td>
							<td class="code">NO OF SERV</td>
						</tr>
						<tr>
							<td rowspan="9"><textarea name="otherTest"
								style="width: 100%; height: 159px; overflow: auto;"><%=props.getProperty("otherTest", "")%></textarea>
							</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr class="testType">
							<td colspan="4"><a>Laboratory use only</a></td>
						</tr>
						<tr>
							<td>Documentation Fee</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>Gyn. Specimen (Pap Smear)</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td style="padding-bottom: 1px;">&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

	<table class="Head" class="hidePrint">
		<tr>
			<td nowrap="true">
			<% if(!readOnly){ %> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <% } %>
			<input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print Pdf" onclick="javascript:return onPrint(true);" /></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
