<!-- 
/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
 -->
  
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*, org.oscarehr.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.springframework.context.*,org.springframework.web.context.support.*" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
	String formClass = "MentalHealthForm14";
	String formLink = "formMentalHealthForm14.jsp";
	String formLink_printPreview = "formMentalHealthForm14Print.jsp";
	int programNo = Integer.parseInt((String)request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID));
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);
	String project_home = request.getContextPath().substring(1);	

	boolean bView = false;
	if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
    <title>Mental Health Form1</title>
    <link rel="stylesheet" type="text/css" href="arStyle.css">
    <html:base/>
</head>


<script type="text/javascript" language="Javascript">

    function reset() {
        document.forms[0].target = "apptProviderSearch";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
	
    function onPrint() {
        document.forms[0].submit.value="print"; //printAR1
        var ret = checkAllDates();
        if(ret==true)
        {
            //ret = confirm("Do you wish to save this form and view the print preview?");
            popupFixedPage(660,860,'../provider/notice.htm');
            document.forms[0].action = "../form/createpdf?__title=&__cfgfile=&__cfgfile=&__template=";
            document.forms[0].target="planner";
            document.forms[0].submit();
            document.forms[0].target="apptProviderSearch";
        }
        return ret;
    }
    function onPrintPreview() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();   
        if(ret) {         	
        	document.forms[0].action = "/<%=project_home%>/form/formname.do" ; 
        	ret = confirm("Are you sure you want to save this form and see the print preview?");         		
        }    
        return ret; 
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret) {
            //reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true) {
            window.close();
        }
        return(false);
    }
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true) {
            //reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=60,screenY=60,top=20,left=20";
        var popup = window.open(varpage, "ar2", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
    function popPage(varpage,pageName) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=60,screenY=60,top=20,left=20";
        var popup = window.open(varpage,pageName, windowprops);
        //if (popup.opener == null) {
        //    popup.opener = self;
        //}
        popup.focus();
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
var maxYear=9900;

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
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            //var y = dt[2];  var m = dt[1];  var d = dt[0];
            var y = dt[0];  var m = dt[1];  var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true)
            {
                alert('Invalid '+pass+' in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }  catch (ex)  {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates() {
        var b = true;
        if(valDate(document.forms[0].dischargeDate)==false){
            b = false;
        } 

		if(valDate(document.forms[0].pg1_eddByDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_eddByUs)==false){
            b = false;
        } 

        return b;
    }
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<html:form action="/form/formname">
<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="form_class" value="<%=formClass%>" />

<% if (formId > 0) { %>
<input type="hidden" name="form_link" value="<%=formLink_printPreview%>" />
<%}else{ %>
<input type="hidden" name="form_link" value="<%=formLink%>" />
<%} %>

<input type="hidden" name="formId" value="<%=formId%>" />
<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
<input type="hidden" name="submit" value="exit"/>

<!-- 
<table width="100%" class="Head" class="hidePrint">
 -->
 <table width="100%">
    <tr width="100%">
        <td align="left">
<%
  if (!bView) {
%>
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
<%
  }
%>
            <input type="button" value="Exit" onclick="javascript:return onExit();"/>            
            <%
            String appPath = request.getContextPath();            
            %> 
            <% if (formId > 0) { %>
            <input type="submit" value="Print Preview" onclick="javascript:return onPrintPreview();"/>
            
            <% } %>
        </td>
    </tr>
</table>

<table width="100%" height="63" border="0">
  <tr>
    <th width="131" height="59" scope="col"><p>Ministry<br />
      Of<br />
      Health </p></th>
    <th width="243" scope="col">Form14<br />
      Mental Health Act</th>
    <th width="322" scope="col">Application by Physician
      for<br />
      Psychiatric Assessment</th>
  </tr>
</table>
<hr />
<p>&nbsp;</p>
<table width="100%" border="0">
  <tr>
    <td height="47"><p>I, (print full name of person) 
        <label>
          <input type="text" name="name" id="name" value="<%=props.getProperty("name","")%>"/>
        </label>
    </p></td>
  </tr>
  <tr>
    <td height="69">of (address) <label>
          <input name="address" type="text" id="address" size="100" maxlength="100" value="<%=props.getProperty("address","")%>"/>
        </label>
    </td>
  </tr>
  <tr>
    <td>hereby consent to the disclosure or transmittal to or the examination by (print name) <label>
          <input name="physicianName" type="text" id="physicianName" size="60" maxlength="60" value="<%=props.getProperty("physicianName","")%>"/>
        </label>
    </td>
  </tr>
  <tr>
    <td>of the clinical record compiled in (name of psychiatric facility) <label>
          <input name="nameOfFacility" type="text" id="nameOfFacility" size="60" maxlength="100" value="<%=props.getProperty("nameOfFacility","")%>"/>
        </label>
    </td>
  </tr>
  <tr>
    <td>in respect of (name of patient) <label>
          <input name="clientName" type="text" id="clientName" size="60" maxlength="60" readonly="readonly" value="<%=props.getProperty("clientName","")%>"/>
        </label>
      
    (date of birth, where available) <label>
        <input name="clientDOB" type="text" id="clientDOB" size="20" maxlength="10" readonly="readonly" value="<%=props.getProperty("clientDOB","")%>"/>
      </label>
    </td>
  </tr>
  <tr>
    <td>(witness) <label>
          <input name="witness" type="text" id="witness" size="60" maxlength="60" value="<%=props.getProperty("witness","")%>"/>
        </label>
    </td>
  </tr>
  <tr>
    <td>(signature) <label>
          <input name="signature" type="text" id="signature" size="60" maxlength="60" value="<%=props.getProperty("signature","")%>"/>
        </label>
    </td>
  </tr>
  <tr>
    <td>(if other than the patient, state relationship to the patient) <label>
          <input name="relationship" type="text" id="relationship" size="60" maxlength="20" value="<%=props.getProperty("relationship","")%>"/>
        </label>
    </td>
  </tr>
  <tr>
    <td>Date (day / month / year) <label>
          <input name="signatureDate" type="text" id="signatureDate" size="60" maxlength="20" value="<%=props.getProperty("signatureDate","")%>"/>
        </label>
    </td>
  </tr>
</table>
<p>&nbsp;</p>

<br>
<table>
    <tr>
        <td align="left">
<%
  if (!bView) {
%>
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
<%
  }
%>
            <input type="button" value="Exit" onclick="javascript:return onExit();"/>
            <% if (formId > 0) { %>
            <input type="submit" value="Print Preview" onclick="javascript:return onPrintPreview();"/>
            <%} %>
        </td>
    </tr>
</table>


</html:form>
</body>
</html:html>

