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
	String formClass = "MentalHealthForm42";
	String formLink = "formMentalHealthForm42.jsp";
	
   boolean readOnly = false;
   int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
   int formId = Integer.parseInt(request.getParameter("formId"));
	String provNo = (String) session.getAttribute("user");
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
   java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
        
	props = ((FrmMentalHealthForm42Record) rec).getFormCustRecord(props, provNo);
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
            document.forms[0].action = "<rewrite:reWrite jspPage="formname.do?__title=MentalHealthForm42&__cfgfile=mentalHealthForm42Print&__cfgfile=mentalHealthForm42Print_2&__template=mentalHealthForm42"/>";
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
  <tr align="left">
    <th width="131" height="59" scope="col"><p>&nbsp;Ministry<br />
      &nbsp;Of<br />
      &nbsp;Health
    </p></th>
    <th width="243" scope="col">Form42<br />
      Mental Health Act</th>
    <th width="322" scope="col">Notice to Person under Subsection 38.1 of<br/>
the Act of Application for Psychiatric<br />
Assessment under Section 15 or an Order<br />
under Section 32 of the Act</th>
  </tr>
</table>
<hr />
<p>&nbsp;</p>
<table width="100%" border="0">
  <tr>
    <td height="40"><b>Part I</b>&nbsp;<I>(complete only if appropriate)</I></td>
  </tr>
  <tr>
    <td height="40">To:
      <label>
        <input name="name" type="text" id="name" size="60" maxlength="60" value="<%= props.getProperty("name", "") %>"/>
      </label>(name of person)
    </td>
  </tr>
  <tr>
    <td height="40">Of&nbsp;
    	<label>
          <input name="homeAddress" type="text" id="homeAddress" size="150" maxlength="250" value="<%= props.getProperty("homeAddress", "") %>"/>
        </label>(home address)
    </td>
  </tr>
  <tr>
    <td height="40">This is to inform you that
    	<label>
          <input name="physician" type="text" id="physician" size="60" maxlength="60" value="<%= props.getProperty("physician", "") %>"/>
        </label>(name of physician)
    </td>
  </tr>
  <tr>
    <td height="40">examined you on
    	<label>
          <input name="dateOfExamination" type="text" id="dateOfExamination" size="10" maxlength="10" value="<%= props.getProperty("dateOfExamination", "") %>"/>
        </label>(date of examination)(day/month/year) and has made an application for you to have a psychiatric assessment.
    </td>
  </tr>
  <tr>
    <td height="40"><strong>Part A and/or Part B must be completed</strong>
     </td>
  </tr>
  <tr>
    <td height="40"><strong>Part A </strong>
     </td>
  </tr>
  <tr>
    <td height="40">That physician has certified that he/she has reasonable cause to believe that you have:
     </td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkThreatenedA" id="chkThreatenedA" <%=props.getProperty("chkThreatenedA","")%> />
	   </label>
       threatened or attempted or are threatening or attempting to cause bodily harm to yourself;
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkBehavedA" id="chkBehavedA" <%=props.getProperty("chkBehavedA","")%> />
	   </label>
       behaved or are behaving violently towards another person or have caused or are causing another person to fear bodily harm from you; or
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkCompetenceA" id="chkCompetenceA" <%=props.getProperty("chkCompetenceA","")%> />
	   </label>
       shown or are showing a lack of competence to care for yourself.
	</td>
  </tr>
  <tr>
    <td height="40">
       and that you are suffering from a mental disorder of a nature or quality that likely will result in:
    </td>
  </tr>  
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkHarmYourselfA" id="chkHarmYourselfA" <%=props.getProperty("chkHarmYourselfA","")%> />
	   </label>
       serious bodily harm to yourself;
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkHarmAnotherA" id="chkHarmAnotherA" <%=props.getProperty("chkHarmAnotherA","")%> />
	   </label>
       serious bodily harm to another person; or
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkImpairmentA" id="chkImpairmentA" <%=props.getProperty("chkImpairmentA","")%> />
	   </label>
       serious physical impairment of you.
	</td>
  </tr>
  <tr>
    <td height="40">
       <strong>Part B </strong>
	</td>
  </tr>
  <tr>
    <td height="40">
       That physician has certified that he/she has reasonable cause to believe that you: 
	</td>
  </tr>
  <tr>
    <td height="40">
       a) have perviously received treatment for mental disorder of an ongoing or recurring nature that, when not treated, is of a nature or quality that likely will result in
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkHarmYourselfB" id="chkHarmYourselfB" <%=props.getProperty("chkHarmYourselfB","")%> />
	   </label>
       serious bodily harm to yourself,
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkHarmAnotherB" id="chkHarmAnotherB" <%=props.getProperty("chkHarmAnotherB","")%> />
	   </label>
       serious bodily harm to another person,
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkDeteriorationB" id="chkDeteriorationB" <%=props.getProperty("chkDeteriorationB","")%> />
	   </label>
       substantial mental or physical deterioration of you, or
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkImpairmentB" id="chkImpairmentB" <%=props.getProperty("chkImpairmentB","")%> />
	   </label>
       serious physical impairment of you;
	</td>
  </tr>
  <tr>
    <td height="40">
       b) have shown clinical improvement as a result of the treatment;
	</td>
  </tr>
  <tr>
    <td height="40">
       c) are suffering from the same mental disorder as the one for which you previously received treatment or from a mental disorder that is similar to the previous one;
	</td>
  </tr>
  <tr>
    <td height="40">
       d) given your history of mental disorder and current mental or physical condition, your are likely to
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkHarmYourselfB2" id="chkHarmYourselfB2" <%=props.getProperty("chkHarmYourselfB2","")%> />
	   </label>
       cause serious bodily harm to yourself,
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkHarmAnotherB2" id="chkHarmAnotherB2" <%=props.getProperty("chkHarmAnotherB2","")%> />
	   </label>
       cause serious bodily harm to another person,
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkDeteriorationB2" id="chkDeteriorationB2" <%=props.getProperty("chkDeteriorationB2","")%> />
	   </label>
       suffer substantial mental or physical deterioration, or
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkImpairmentB2" id="chkImpairmentB2" <%=props.getProperty("chkImpairmentB2","")%> />
	   </label>
       suffer serious physical impairment;
	</td>
  </tr>
  <tr>
    <td height="40">
       e) have been found incapable, within  the meaning of the Health Care Consent Act, 1996 of consenting to your treatment in a psychiatric facility and the consent of your substitute decision-maker has been obtained; and
	</td>
  </tr>
  <tr>
    <td height="40">
       f) you are not suitable for admission or continuation as an informal or voluntary patient.
	</td>
  </tr>
  <tr>
    <td height="40">
       The application is sufficient authority to hold you in custody in this hospital for up to 72 hours.
	</td>
  </tr>
  <tr>
    <td height="40">
       You have the right to retain and instruct a lawyer without dely.
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
        <input name="dateOfSign" type="text" id="dateOfSign" size="10" maxlength="10" value="<%= props.getProperty("dateOfSign", "") %>"/>
      </label>(date)&nbsp;&nbsp;&nbsp;&nbsp;
      <label>
        <input name="signPhysician" type="text" id="signPhysician" size="60" maxlength="60" value="<%= props.getProperty("signPhysician", "") %>"/>
      </label>(signature of attending physician)
	</td>
  </tr>
  <tr>
    <td height="40">
       <strong>Part II <I>(complete only if appropriate)</I></strong>
	</td>
  </tr>
  <tr>
    <td height="40">To:
       <label>
        <input name="name2" type="text" id="name2" size="60" maxlength="60" value="<%= props.getProperty("name2", "") %>"/>
      </label>(name of person)
    </td>
  </tr>
  <tr>
    <td height="40">Of&nbsp;&nbsp;
       <label>
        <input name="homeAddress2" type="text" id="homeAddress2" size="150" maxlength="250" value="<%= props.getProperty("homeAddress2", "") %>"/>
      </label>(home address)
    </td>
  </tr>
  <tr>
    <td height="40">This is to inform you that
       <label>
        <input name="nameOfMinisterHealth" type="text" id="nameOfMinisterHealth" size="150" maxlength="250" value="<%= props.getProperty("nameOfMinisterHealth", "") %>"/>
      </label>(name of Minister of Health and Long-Term Care)
    </td>
  </tr>
  <tr>
    <td height="40">
    	Minister of Health and Long-Term Care for the Province of Ontario, has reasonable cause to believe that you are suffering from mental disorder of a nature or quality that likely will result in:
    </td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkHarmYourself2" id="chkHarmYourself2" <%=props.getProperty("chkHarmYourself2","")%> />
	   </label>
       serious bodily harm to yourself; or
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
       		<input type="checkbox" name="chkHarmAnother2" id="chkHarmAnother2" <%=props.getProperty("chkHarmAnother2","")%> />
	   </label>
       serious bodily harm to another person.
	</td>
  </tr>
  <tr>
    <td height="40">
       unless you are placed in the custody of a psychiatric facility and has by Order dated
	</td>
  </tr>
  <tr>
    <td height="40">
       <label>
        <input name="dateOfOrder" type="text" id="dateOfOrder" size="10" maxlength="10" value="<%= props.getProperty("dateOfOrder", "") %>"/>
      </label>(date of order)(day/month/year), authorized your custody in a psychiatric facility for up to 72 hours.
    </td>
  </tr>
  <tr>
    <td height="40">
    	You have the right to retain and instruct a lawyer without delay.
    </td>
  </tr>
  <tr>
    <td height="40">
       <label>
        <input name="dateOfSign2" type="text" id="dateOfSign2" size="10" maxlength="10" value="<%= props.getProperty("dateOfSign2", "") %>"/>
      </label>(date)&nbsp;&nbsp;&nbsp;&nbsp;
      <label>
        <input name="signPhysician2" type="text" id="signPhysician2" size="60" maxlength="60" value="<%= props.getProperty("signPhysician2", "") %>"/>
      </label>(signature of attending physician)
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
