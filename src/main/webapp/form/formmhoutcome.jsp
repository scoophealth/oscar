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

<%@ page import="oscar.form.*, java.util.*"%>
<%@ page import="java.io.FileInputStream"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Mental Health Outcome</title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="mhStyles.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">

<%
	String formClass = "MentalHealth";
	String formLink = "formmhoutcome.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
    if ( formId ==0 ){
	props = ((FrmMentalHealthRecord) rec).getFormCustRecord(props, provNo);
    }
    oscar.oscarEncounter.util.EctFileUtil list = new oscar.oscarEncounter.util.EctFileUtil();
    props.setProperty("c_lastVisited", "outcome");

    String projecthome = oscarVariables.getProperty("project_home");
    String path = "form/dataFiles";
%>



<script type="text/javascript" language="Javascript">
	function popupFixedPage(vheight,vwidth,varpage) { 
		var page = "" + varpage;
		windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
		var popup=window.open(page, "planner", windowprop);
	}
	
	var mainAction = "";
	var mainTarget = "";
	
	 function getMainAction(){
	    mainAction = document.forms[0].action;	  
	    mainTarget = docuemtn.forms[0].target;	  
	 }

    function onPrint() {
        document.forms[0].submit.value="print"; //printReferral
        var ret = numvalidate();
        //if(ret==true) {
            //ret = confirm("Do you wish to save this form and view the print preview?");
            popupFixedPage(650,850,'../provider/notice.htm');
            document.forms[0].action = "formmhoutcomeprint.jsp";
            document.forms[0].target="planner";
            document.forms[0].submit();
            document.forms[0].target="apptProviderSearch";
        //}
        return false;
    }
    function onSave() {
        document.forms[0].submit.value="save";
        document.forms[0].action = mainAction;
        document.forms[0].target = mainTarget;
        var ret = checkAllDates();
        if(ret==true)
        {
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    function onSaveExit() {
        var ret = checkAllDates();
        document.forms[0].action = mainAction;
        document.forms[0].target = mainTarget;
        if(ret==true)
        {
            ret = confirm("Are you sure you wish to save this form and return to the encounter page?");
        }
        return ret;
    }
    
    function insert(fromName, num) {
        switch(fromName) {
        case "o_sp":
            if(document.forms[0].o_sp1.value=="") {
                document.forms[0].o_sp1.value = num;
            }else if(document.forms[0].o_sp2.value=="") {
                document.forms[0].o_sp2.value = num;
            }else if(document.forms[0].o_sp3.value=="") {
                document.forms[0].o_sp3.value = num;
            }
            break;
        case "o_pe":
            if(document.forms[0].o_pe1.value=="") {
                document.forms[0].o_pe1.value = num;
            }else if(document.forms[0].o_pe2.value=="") {
                document.forms[0].o_pe2.value = num;
            }else if(document.forms[0].o_pe3.value=="") {
                document.forms[0].o_pe3.value = num;
            }
            break;
        case "o_d":
            if(document.forms[0].o_d1.value=="") {
                document.forms[0].o_d1.value = num;
            }else if(document.forms[0].o_d2.value=="") {
                document.forms[0].o_d2.value = num;
            }else if(document.forms[0].o_d3.value=="") {
                document.forms[0].o_d3.value = num;
            }
            break;
        case "o_pns":
            if(document.forms[0].o_pns1.value=="") {
                document.forms[0].o_pns1.value = num;
            }else if(document.forms[0].o_pns2.value=="") {
                document.forms[0].o_pns2.value = num;
            }else if(document.forms[0].o_pns3.value=="") {
                document.forms[0].o_pns3.value = num;
            }
            break;
        default:
            alert("case not covered");
            break;
        }
    }
    function numvalidate() {
            if( isNaN(document.forms[0].o_sp1.value) ) {
                bringthToAttentionth(document.forms[0].o_sp1);
                return false;
            }
            if( isNaN(document.forms[0].o_sp2.value)) {
                bringthToAttentionth(document.forms[0].o_sp2);
                return false;
            }
            if(isNaN(document.forms[0].o_sp3.value)) {
                bringthToAttentionth(document.forms[0].o_sp3);
                return false;
            }
            if(isNaN(document.forms[0].o_pe1.value)) {
                bringthToAttentionth(document.forms[0].o_pe1);
                return false;
            }
            if(isNaN(document.forms[0].o_pe2.value)) {
                 bringthToAttentionth(document.forms[0].o_pe2);
                 return false;
            }
            if(isNaN(document.forms[0].o_pe3.value)) {
                 bringthToAttentionth(document.forms[0].o_pe3);
                 return false;
            }
            if(isNaN(document.forms[0].o_d1.value)) {
                 bringthToAttentionth(document.forms[0].o_d1);
                 return false;
            }
            if(isNaN(document.forms[0].o_d2.value)) {
                 bringthToAttentionth(document.forms[0].o_d2);
                 return false;
            }
            if(isNaN(document.forms[0].o_d3.value)) {
                 bringthToAttentionth(document.forms[0].o_d3);
                 return false;
            }
            if(isNaN(document.forms[0].o_pns1.value)) {
                 bringthToAttentionth(document.forms[0].o_pns1);
                 return false;
            }
            if(isNaN(document.forms[0].o_pns2.value)) {
                 bringthToAttentionth(document.forms[0].o_pns2);
                 return false;
            }
            if(isNaN(document.forms[0].o_pns3.value)) {
                 bringthToAttentionth(document.forms[0].o_pns3);
                 return false;
            }


    }
    function bringthToAttentionth(docItem){
       docItem.focus();
       alert("This value must be numeric.");
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
                alert('Invalid '+pass+' in field Termination Date');
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
        if(valDate(document.forms[0].o_formDate)==false){
            b = false;
        }
        return b;
    }
</script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>
<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="getMainAction()">
<html:form action="/form/formname" onsubmit="return numvalidate()">

	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="c_lastVisited"
		value=<%=props.getProperty("c_lastVisited", "Referral")%> />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="submit" value="Print"
				onclick="javascript:return onPrint();" /></td>
			<td align="right"><a
				href="formmhreferral.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Referral</a>
			&nbsp;|&nbsp; <a
				href="formmhassessment.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Assessment</a>
			&nbsp;|&nbsp; Outcome</td>
		</tr>
	</table>

	<table cellpadding="1" cellspacing="0" class="mainTable"
		bgcolor="#FFF8DC">
		<tr>
			<th align="center"><big>MENTAL HEALTH OUTCOME</big><br>
			<br>
			</th>
		</tr>
		<tr>
			<td colspan="4">
			<table border="0" cellpadding="2" cellspacing="0" width="100%">
				<tr>
					<td width="50%" rowspan="5">&nbsp;</td>
					<td>Name:</td>
					<td align="right"><input type="text" name="c_pName" size="40"
						value="<%= props.getProperty("c_pName", "") %>" readonly="true" /></td>
				</tr>
				<tr>
					<td>Sex:</td>
					<td align="right"><input type="text" name="c_sex" size="40"
						value="<%= props.getProperty("c_sex", "") %>" readonly="true" /></td>
				</tr>
				</tr>
				<td>Address:</td>
				<td align="right"><input type="text" name="c_address" size="40"
					value="<%= props.getProperty("c_address", "") %>" readonly="true" /></td>
				</tr>
				<tr>
					<td>Home Phone:</td>
					<td align="right"><input type="text" name="c_homePhone"
						size="40" value="<%= props.getProperty("c_homePhone", "") %>"
						readonly="true" /></td>
				</tr>
				<tr>
					<td>Birth Date <small>(yyyy/mm/dd)</small>:</td>
					<td align="right"><input type="text" name="c_birthDate"
						size="40" value="<%= props.getProperty("c_birthDate", "") %>"
						readonly="true" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td>
			<table class="TableWithBorder" cellpadding="2" cellspacing="0"
				width="100%">
				<tr>
					<td>Referral Date<small>(yyyy/mm/dd)</small>:</td>
					<td align="left"><input type="text" name="c_referralDate"
						value="<%= props.getProperty("c_referralDate", "") %>"
						readonly="true" /></td>
					<td>Referred By:</td>
					<td align="left"><input type="text" name="c_referredBy"
						value="<%= props.getProperty("c_referredBy", "") %>"
						readonly="true" /></td>
					<td>Number of Visits:</td>
					<td align="left"><input type="text" name="o_numVisits"
						value="<%= props.getProperty("o_numVisits", "") %>" /></td>
				</tr>
				<tr>
					<td>Termination Date<small>(yyyy/mm/dd)</small>:</td>
					<td align="left"><input type="text" name="o_formDate"
						value="<%= props.getProperty("o_formDate", "") %>" /></td>
					<td>HSO Specialist:</td>
					<td align="left"><input type="text" name="o_specialist"
						value="<%= props.getProperty("o_specialist", "") %>" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td colspan="4">
			<table border="1" cellpadding="2" cellspacing="0" width="100%">
				<tr>
					<td class="mhList" valign="top">Services Provided:<br>
					<br>
					&nbsp; 1. <input type="text" name="o_sp1"
						value="<%= props.getProperty("o_sp1", "") %>" size="2" /> &nbsp;
					2. <input type="text" name="o_sp2"
						value="<%= props.getProperty("o_sp2", "") %>" size="2" /> &nbsp;
					3. <input type="text" name="o_sp3"
						value="<%= props.getProperty("o_sp3", "") %>" size="2" /> <br>
					<br>
					<% String[] sp = list.loadData("mhOutcome/ServicesProvided.txt", projecthome, path );
                            for (int i=0; i<sp.length; i++)
                            {
                                if(sp[i]!=null)
                                {
                        %> <a
						href="javascript: function myFunction() {return false; }"
						class="mhlink" onclick="javascript:insert('o_sp', <%=i+1%>);"><%=i+1%>.
					<%= sp[i] %></a><br>
					<%
                                }
                            }
                        %> &nbsp;<input type="text" name="o_spOther"
						value="<%= props.getProperty("o_spOther", "") %>" /></td>
					<td class="mhList" valign="top">Problems Encountered:<br>
					<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. <input type="text" name="o_pe1"
						value="<%= props.getProperty("o_pe1", "") %>" size="2" /> &nbsp;
					2. <input type="text" name="o_pe2"
						value="<%= props.getProperty("o_pe2", "") %>" size="2" /> &nbsp;
					3. <input type="text" name="o_pe3"
						value="<%= props.getProperty("o_pe3", "") %>" size="2" /> <br>
					<br>
					<% String[] pe = list.loadData("mhOutcome/ProblemsEncountered.txt", projecthome, path );
                            for (int i=0; i<pe.length; i++)
                            {
                                if(pe[i]!=null)
                                {
                        %> <a
						href="javascript: function myFunction() {return false; }"
						class="mhlink" onclick="javascript:insert('o_pe', <%=i+1%>);"><%=i+1%>.
					<%= pe[i] %></a><br>
					<%
                                }
                            }
                        %> &nbsp;<input type="text" name="o_peOther"
						value="<%= props.getProperty("o_peOther", "") %>" /></td>
					<td class="mhList" valign="top">Disposition:<br>
					<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. <input type="text" name="o_d1"
						value="<%= props.getProperty("o_d1", "") %>" size="2" /> &nbsp; 2.
					<input type="text" name="o_d2"
						value="<%= props.getProperty("o_d2", "") %>" size="2" /> &nbsp; 3.
					<input type="text" name="o_d3"
						value="<%= props.getProperty("o_d3", "") %>" size="2" /> <br>
					<br>
					<% String[] d = list.loadData("mhOutcome/Disposition.txt", projecthome, path );
                            for (int i=0; i<d.length; i++)
                            {
                                if(d[i]!=null)
                                {
                        %> <a
						href="javascript: function myFunction() {return false; }"
						class="mhlink" onclick="javascript:insert('o_d', <%=i+1%>);"><%=i+1%>.
					<%= d[i] %></a><br>
					<%
                                }
                            }
                        %> &nbsp;<input type="text" name="o_dOther"
						value="<%= props.getProperty("o_dOther", "") %>" /></td>
					<td class="mhList" valign="top">Patient Not Seen:<br>
					<br>
					&nbsp; 1. <input typns="text" name="o_pns1"
						value="<%= props.getProperty("o_pns1", "") %>" size="2" /> &nbsp;
					2. <input typns="text" name="o_pns2"
						value="<%= props.getProperty("o_pns2", "") %>" size="2" /> &nbsp;
					3. <input typns="text" name="o_pns3"
						value="<%= props.getProperty("o_pns3", "") %>" size="2" /> <br>
					<br>
					<% String[] pns = list.loadData("mhOutcome/PatientNotSeen.txt", projecthome, path );
                            for (int i=0; i<pns.length; i++)
                            {
                                if(pns[i]!=null)
                                {
                        %> <a
						href="javascript: function myFunction() {return false; }"
						class="mhlink" onclick="javascript:insert('o_pns', <%=i+1%>);"><%=i+1%>.
					<%= pns[i] %></a><br>
					<%
                                }
                            }
                        %> &nbsp;<input type="text" name="o_pnsOther"
						value="<%= props.getProperty("o_pnsOther", "") %>" /></td>
				</tr>
				<tr>
					<td class="mhSelect"><br>
					</td>
					<td class="mhSelect"><br>
					</td>
					<td class="mhSelect"><br>
					</td>
					<td class="mhSelect"><br>
					</td>
				</tr>
				<tr>

					<td colspan="4" class="mhList" valign="top">Assessment
					Comments:<br>
					<textarea class="mhOutTextarea" name="o_outComments"><%= props.getProperty("o_outComments", "") %></textarea>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="submit" value="Print"
				onclick="javascript:return onPrint();" /></td>
			<td align="right"><a
				href="formmhreferral.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Referral</a>
			&nbsp;|&nbsp; <a
				href="formmhassessment.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Assessment</a>
			&nbsp;|&nbsp; Outcome</td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
