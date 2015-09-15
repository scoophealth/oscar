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

<%@ page
	import="oscar.form.*, oscar.OscarProperties, java.util.Date, oscar.util.UtilDateUtilities"%>
<%@page import="org.oscarehr.common.dao.FrmLabReqPreSetDao, org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Laboratory Requisition</title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="labReq07Style.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<script src="../share/javascript/prototype.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<%
	//String formClass = "LabReq07";
	//String formLink = "formlabreq07.jsp";
	String formClass = "MentalHealthForm1";
	String formLink = "formMentalHealthForm1.jsp";
	
   boolean readOnly = false;
   int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
   int formId = Integer.parseInt(request.getParameter("formId"));
	String provNo = (String) session.getAttribute("user");
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
   java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
        
	props = ((FrmMentalHealthForm1Record) rec).getFormCustRecord(props, provNo);
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

   String patientName = props.getProperty("clientName"," , ");
   String[] patientNames = patientName.split(",");
   
   String[] patientDOB = props.getProperty("clientDOB", " / / ").split("/");
   request.removeAttribute("submit");
%>

<script type="text/javascript" language="Javascript">

var temp;
temp = "";


    function onPrintPDF() {
         
        //var ret = checkAllDates();
        //if(ret==true)
        //{            
            
            //ret = confirm("Do you wish to save this form and view the print preview?");
            //popupFixedPage(650,850,'../provider/notice.htm');
            temp=document.forms[0].action;         
            document.forms[0].action = "<rewrite:reWrite jspPage="formname.do?__title=MentalHealthForm1&__cfgfile=mentalHealthForm1Print&__cfgfile=mentalHealthForm1Print_2&__cfgfile=mentalHealthForm1Print_3&__template=mentalHealthForm1"/>";
            document.forms[0].submit.value="printall"; 
            document.forms[0].target="_self";
        //}
        //return ret;
        return true;
    }
    function onSave() {
        if (temp != "") { document.forms[0].action = temp; }
        document.forms[0].target="_self";        
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
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
            if (dt[1] == null) 
                dt = dateString.split('-');
            var y = dt[0];
            var m = dt[1];
            var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true)
            {
                var s = dateBox.name;
                //alert('Invalid '+pass+' in field ' + s.substring(3));
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
        if (valDate(document.forms[0].b_dateSigned) == false) {
            alert("The 'Signature Date' field is not valid");
            b = false;
        }
        if (valDate(document.forms[0].o_specimenCollectionDate) == false) {
            alert("The 'Specimen Collection Date' field is not valid");
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

	<table class="Head" class="hidePrint">
		<tr>
			<td nowrap="true">
			<% if(!readOnly){ %> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <% } %>
			<input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print Pdf" onclick="javascript:return onPrintPDF();" /></td>
		</tr>
	</table>

	<table width="100%" height="63" border="0">
  <tr>
    <th width="131" height="59" scope="col"><p>Ministry<br />
      Of<br />
      Health
    </p></th>
    <th width="243" scope="col">Form1<br />
      Mental Health Act</th>
    <th width="322" scope="col">Application by Physician for<br />
	Psychiatric Assessment</th>
  </tr>
</table>
<hr />
<p>&nbsp;</p>
<table width="100%" border="0">
  <tr>
    <td width="315" scope="col">Name of physician</td>
    <td width="403" scope="col">
      <label>
        <input name="physicianName" type="text" id="physicianName" size="60" maxlength="60" value="<%= props.getProperty("physicianName", "") %>"/>
      </label>
    </td>
  </tr>
  <tr>
    <td height="26"><div align="left">Physician address</div></td>
    <td>
        <label>
          <input name="physicianAddress" type="text" id="physicianAddress" size="60" maxlength="260" value="<%= props.getProperty("physicianAddress", "") %>"/>
        </label>
    </td>
  </tr>
  <tr>
    <td><div align="left">Telephone number </div></td>
    <td>
      <label>
        <input name="telephoneNumber" type="text" id="telephoneNumber" size="60" maxlength="20" value="<%= props.getProperty("telephoneNumber", "") %>"/>
      </label>
    </td>
  </tr>
  <tr>
    <td><div align="left">Fax number </div></td>
    <td>
      <label>
        <input name="faxNumber" type="text" id="faxNumber" size="60" maxlength="20" value="<%= props.getProperty("faxNumber", "") %>"/>
      </label>
    </td>
  </tr>
  <tr>
    <td>On (date)</td>
    <td>
      <label>
        <input name="onDate" type="text" id="onDate" size="60" maxlength="10" value="<%= props.getProperty("onDate", "") %>"/>
      </label>
    </td>
  </tr>
  <tr>
    <td>I personally examined (print full name of person)</td>
    <td>
      <label>
        <input name="clientName" type="hidden" id="clientName" size="60" maxlength="60" readonly value="<%= props.getProperty("clientName", "") %>"/><%= props.getProperty("clientName", "") %>
      </label>
    </td>
  </tr>
  <tr>
    <td>whose address is (home address)</td>
    <td>
      <label>
        <input name="clientAddress" type="hidden" id="clientAddress" size="60" maxlength="250" readonly value="<%= props.getProperty("clientAddress", "") %>"/><%= props.getProperty("clientAddress", "") %>
      </label>
    </td>
  </tr>
</table>
<p>
  <!--ddd -->
</p>
<div>
  <p>You may only sign this <strong>Form 1</strong> if you have personally examined the person within the past seven days. In deciding if a Form 1 is appropriate, you must complete <strong>either </strong>Box A (serious harm test) <strong>or </strong>Box B (persons who are incapable of consenting to treatment and meet the specified criteria test) below.</p>
  <p>&nbsp;</p>
</div>
<table width="100%" border="1">
  <tr>
    <th scope="col"><p>Box A - Section 15(1) of the Mental Health Act <br />
    Serious Harm Test</p></th>
  </tr>
  <tr>
    <td><strong>The Past / Present Test</strong> (check one or more)</td>
  </tr>
  <tr>
    <td>I have reasonalbe cause to believe that the person:</td>
  </tr>
  <tr>
    <td>
       <label>
       		<input type="checkbox" name="threatened" id="threatened" <%=props.getProperty("threatened","")%> />
	   </label>
       has threatened or is threatening to cause bodily harm to himself or herself
	</td>
  </tr>
  
  
  
  <tr>
    <td>
      <label>
      <input type="checkbox" name="attempted" id="attempted" <%=props.getProperty("attempted","")%> />
			
      </label>
    has attempted or is attempting to cause bodily harm to himself or herself
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="behaved" id="behaved" <%=props.getProperty("behaved","")%>/>
      </label>
    has behaved or is behaving violently towards another person
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="caused" id="caused" <%=props.getProperty("caused","")%>/>
      </label>
    has caused or is causing another person to fear bodily harm from him or her; or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="shown" id="shown" <%=props.getProperty("shown","")%>/>
      </label>
    has shown or is showing a lack of competence to care for himself or herself
    </td>
  </tr>
  <tr>
    <td>I base this belief on the following information (you may, as appropriate in the circumstances, rely on any combination of your own observations and information communicated to you by others.)</td>
  </tr>
  <tr>
    <td><p>My own observations:</p>
        <label>
          <textarea name="observation" id="observation" cols="118" rows="5"><%=props.getProperty("observation","")%></textarea>
        </label>
    <p>&nbsp;</p></td>
  </tr>
  <tr>
    <td><p>Facts communicated to me by others:</p>      
        <textarea name="facts" id="facts" cols="118" rows="5"><%=props.getProperty("facts","")%></textarea>
    <p>&nbsp;</p></td>
  </tr>
  <tr>
    <td><strong>The future Test</strong> (check one or more)</td>
  </tr>
  <tr>
    <td>I am of the opinion that the person is apparently suffering from mental disorder of a nature or quality that likely will result in:</td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmHimself" id="harmHimself" <%=props.getProperty("harmHimself","")%>/>
      </label>
    serious bodily harm to himself or herself,
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmOthers" id="harmOthers" <%=props.getProperty("harmOthers","")%>/>
      </label>
    serious bodily harm to another person,
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="impairment" id="impairment" <%=props.getProperty("impairment","")%> />
      </label>
    serious physical impairment of himself or herself
    </td>
  </tr>
  <tr>
    <td><p>I base this opnion on the following information (you may, as appropriate in the circumstances, rely on any combination of your own observations and information communicated to you by others.)</p></td>
  </tr>
  <tr>
    <td><p>My own observastions:</p>      
        <label>
          <textarea name="observation2" id="observation2" cols="118" rows="5"> <%=props.getProperty("observation2","")%></textarea>
        </label>
    <p>&nbsp;</p></td>
  </tr>
  <tr>
    <td><p>Facts communicated by others:</p>
        <label>
          <textarea name="facts2" id="facts2" cols="118" rows="5"><%=props.getProperty("facts2","")%></textarea>
        </label>
    <p>&nbsp;</p></td>
  </tr>
</table>
<p>&nbsp;</p>
<table width="100%" border="1">
  <tr>
    <th scope="col"><p>Box B - Section 15(1.1) of the Mental Health Act<br />
      Patients who are Incapable of Consenting to Treatment and Meet the Specified Criteria</p>
      <p>Note: The patient must meet the criteria set out in each of the following conditions.</p></th>
  </tr>
  <tr>
    <td>I have reasonable cause to believe that the person:</td>
  </tr>
  <tr>
    <td>1. Has previously received treatment for mental disorder of an ongoing or recurring nature that, when not treated, is of a nature or quality that likely will result in one or more of the following: (please indicate one or more)</td>
  </tr>
  <tr>
    <td>
      <label>
        <input name="harmHimselfB" type="checkbox" id="harmHimselfB" <%=props.getProperty("harmHimselfB","")%> />
      </label>
    serious bodily harm to himself or herself,
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmOthersB" id="harmOthersB" <%=props.getProperty("harmOthersB","")%>/>
      </label>
    serious bodily harm to another person,
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="deteriorationB" id="deteriorationB" <%=props.getProperty("deteriorationB","")%>/>
      </label>
    substantial mental or physical deterioration of himself or herself, or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="impairmentB" id="impairmentB" <%=props.getProperty("impairmentB","")%>/>
      </label>
    serious physical impairment of himself or herself;
    </td>
  </tr>
  <tr>
    <td>AND</td>
  </tr>
  <tr>
    <td>2. Has shown clinical improvement as a result of the treatment.</td>
  </tr>
  <tr>
    <td>AND</td>
  </tr>
  <tr>
    <td>I am of the opinion that the person,</td>
  </tr>
  <tr>
    <td>3. Is incapable, within the meaning of the <em>Health Care Consent Act, 1996</em>, of consenting to his or her treatment in a psychiatric facility and the consent of his or her substitute decision-maker has been obtained;</td>
  </tr>
  <tr>
    <td>AND</td>
  </tr>
  <tr>
    <td>4. Is apparently suffering from the same mental disorder as the one for which he or she previously received treatment or from a mental disorder that is similar to the previous one;</td>
  </tr>
  <tr>
    <td>AND</td>
  </tr>
  <tr>
    <td>5. Given the person's history of mental disorder and current mental or physical condition, is likely to: (choose one or more of the following)</td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmHimselfB2" id="harmHimselfB2" <%=props.getProperty("harmHimselfB2","")%>/>
      </label>
    cause serious bodily harm to himself or herself, or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmOthersB2" id="harmOthersB2" <%=props.getProperty("harmOthersB2","")%>/>
      </label>
    cause serious bodily harm to another person, or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="deteriorationB2" id="deteriorationB2" <%=props.getProperty("deteriorationB2","")%>/>
      </label>
    suffer substantial mental or physical deterioration, or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="impairmentB2" id="impairmentB2" <%=props.getProperty("impairmentB2","")%>/>
      </label>
    suffer serious physical impairment
    </td>
  </tr>
  <tr>
    <td height="44">I base this opinion on the following information (you may, as appropriate in the circumstances, rely on any combination of your own observations and information communicated to you by others.)</td>
  </tr>
  <tr>
    <td><p>My own observations:</p>      
        <label>
          <textarea name="observationB" id="observationB" cols="118" rows="5"><%=props.getProperty("observationB","")%></textarea>
        </label>
    <p>&nbsp;</p></td>
  </tr>
  <tr>
    <td><p>Facts communicated by others:</p>
        <label>
          <textarea name="factsB" id="factsB" cols="118" rows="5"><%=props.getProperty("factsB","")%></textarea>
        </label>
    <p>&nbsp;</p></td>
  </tr>
</table>
<p>&nbsp;</p>
<table width="100%" height="490" border="0">
  <tr>
    <td colspan="2" scope="col">I have made careful inquiry into all the facts necessary for me to form my opinion as to the nature and quality of the person's mental disorder. I hereby make application for a psychiatric assessment of the person named.</td>
  </tr>
  <tr>
    <td width="40%">Today's date<label>
          <input name="todayDate" type="text" id="todayDate" size="20" maxlength="10" value="<%=props.getProperty("todayDate","")%>"/>
        </label>
    </td>
    <td width="60%"><p>Today's time
        <input name="todayTime" type="text" id="todayTime" size="20" maxlength="8" value="<%=props.getProperty("todayTime","")%>"/>
      </p></td>
  </tr>
  <tr>
    <td colspan="2">Examining physician's signature (signature of physician)<label>
          <input name="signature" type="text" id="signature" size="60" maxlength="60" value="<%=props.getProperty("signature","")%>"/>
        </label>
    </td>
  </tr>
  <tr>
    <td colspan="2">This form authorizes, for a period of 7 days including the date of signature, the apprehension of the person named and his or her detention in a psychiatirc facility for a maximum of 72 hours.</td>
  </tr>
  <tr>
    <td colspan="2"><strong>For Use at the Psychiatric Facility</strong></td>
  </tr>
  <tr>
    <td height="53" colspan="2">Once the period of detention at the psychiatric facility begins, the attending physician should note the date and time this occurs and must promptly give the person a Form 42.</td>
  </tr>
  <tr>
    <td height="42">Date and time detention commences
          <input name="datetimeOfDetention" type="text" id="datetimeOfDetention" size="20" maxlength="20" value="<%=props.getProperty("datetimeOfDetention","")%>"/>
        
    </td>
    <td height="42">Signature of physician
          <input name="signature1" type="text" id="signature1" size="60" maxlength="60" value="<%=props.getProperty("signature1","")%>"/>
        
    </td>
  </tr>
  
  <tr>
    <td height="42">Date and time Form42 Delivered &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>
          <input name="datetimeOfDelivered" type="text" id="datetimeOfDelivered" size="20" maxlength="20" value="<%=props.getProperty("datetimeOfDelivered","")%>"/>
        </label>
    </td>
<td>Signature of physician<label>
          <input name="signature2" type="text" id="signature2" size="60" maxlength="60" value="<%=props.getProperty("signature2","")%>"/>
        </label>
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
				value="Print Pdf" onclick="javascript:return onPrintPDF();" /></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
