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

<%@ page language="java"%>
<%@ page import="oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties"
	scope="session" />

<%
	String formClass = "BCNewBorn2008";
	String formLink = "formBCNewBorn2008pg2.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    //resource = resource + "ob/riskinfo/";    props.setProperty("c_lastVisited", "pg1");

	//get project_home
	String project_home = request.getContextPath().substring(1);
	boolean bSync = false;
	if(!props.getProperty("c_surname_cur", "").equals("") && !(props.getProperty("c_surname_cur", "").equals(props.getProperty("c_surname", ""))
	        && props.getProperty("c_givenName_cur", "").equals(props.getProperty("c_givenName", ""))
	        && props.getProperty("c_address_cur", "").equals(props.getProperty("c_address", ""))
	        && props.getProperty("c_city_cur", "").equals(props.getProperty("c_city", ""))
	        && props.getProperty("c_province_cur", "").equals(props.getProperty("c_province", ""))
	        && props.getProperty("c_postal_cur", "").equals(props.getProperty("c_postal", ""))
	        //&& props.getProperty("c_phn_cur", "").equals(props.getProperty("c_phn", ""))
	        && props.getProperty("c_phone_cur", "").trim().equals(props.getProperty("c_phone", "").trim())
	        )) {
	    bSync = true;
	}
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true;
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>

<title>British Columbia New Born Record 2008</title>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<link rel="stylesheet" type="text/css" href="bcArStyle.css">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />       
<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>


<html:base />
</head>

<script type="text/javascript" language="Javascript">

    function reset() {
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
	
        function onPrint() {
            document.forms[0].submit.value="print"; //printAR1
           // var ret = checkAllDates();
            var ret = true;
            if(ret==true)
            {
                document.forms[0].action = "../form/createpdf?__title=British+Columbia+Newborn+Record+2008+Part+2&__cfgfile=bcNB2008PrintCfgPg2&__template=bcNewBorn2008pg2";

                document.forms[0].target="_blank";
            }
            return ret;
        }	
	
    function onSave() {
        	document.forms[0].submit.value="save";
        	var ret = true;
        	//var ret = checkAllDates();
        	//if(ret==true) {
		   //	ret = checkAllTimes();
    		//}
         if(ret==true) {
            reset();
            ret = confirm("Are you sure you want to save this form?");
         }
        	 return ret;
         }
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret==true) {
			ret = checkAllTimes();
		}
        if(ret == true) {
            reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar2", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
    function popPage(varpage,pageName) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
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


function showDef(str, field) {
    if(document.getElementById)	{
        field.value = str;
    }
}
function syncDemo() {
    document.forms[0].c_surname.value = "<%=props.getProperty("c_surname_cur", "")%>";
    document.forms[0].c_givenName.value = "<%=props.getProperty("c_givenName_cur", "")%>";
    document.forms[0].c_address.value = "<%=props.getProperty("c_address_cur", "")%>";
    document.forms[0].c_city.value = "<%=props.getProperty("c_city_cur", "")%>";
    document.forms[0].c_province.value = "<%=props.getProperty("c_province_cur", "")%>";
    document.forms[0].c_postal.value = "<%=props.getProperty("c_postal_cur", "")%>";
    //document.forms[0].c_phn.value = "<%=props.getProperty("c_phn_cur", "")%>";
    document.forms[0].c_phone.value = "<%=props.getProperty("c_phone_cur", "")%>";
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

    function valDate(dateBox)  {
        try    {
            var dateString = dateBox.value;
            if(dateString == "") {
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            var y = dt[2];  var m = dt[1];  var d = dt[0];
            //var y = dt[0];  var m = dt[1];  var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true) {
                alert('Invalid '+pass+' in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        } catch (ex)  {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function valTime(dateBox)   {
        try   {
            var dateString = dateBox.value;
            if(dateString == "") {
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split(':');
            var m = dt[1];  var h = dt[0];
            var pass = false;
			if(h >= 0 && h <=23 && m >=0 && m <=59) { pass = true; }

            if(pass!=true) {
                alert('Invalid data in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }  catch (ex) {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates() {
        var b = true;
        if(valDate(document.forms[0].Section13Note1Date)==false){
            b = false;
        } else if(valDate(document.forms[0].Section13Note1Date)==false){
            b = false;
		  } else if(valDate(document.forms[0].Section13Note2Date)==false){
            b = false;
        } else if(valDate(document.forms[0].Section13Note3Date)==false){
            b = false;
        }

        return b;
    }

	function checkAllTimes() {
        var b = true;
        //if(valTime(document.forms[0].ID GOES HERE)==false){
        //    b = false;
        //}

        return b;
    }
</script>



<script type="text/javascript" language="Javascript">

function onCheck(a, groupName) {
    if (a.checked) {
		var s = groupName;
		unCheck(s);
		a.checked = true;
    }
}
function unCheck(s) {
    for (var i =0; i <document.forms[0].elements.length; i++) {
        if (document.forms[0].elements[i].name.indexOf(s) != -1 && document.forms[0].elements[i].name.indexOf(s) < 1) {
            document.forms[0].elements[i].checked = false;
    	}
	}
}
function isChecked(s) {
    for (var i =0; i <document.forms[0].elements.length; i++) {
        if (document.forms[0].elements[i].name == s) {
            if (document.forms[0].elements[i].checked) {
				return true;
			} else {
				return false;
			}
    	}
	}
}

function onCheckMaster(a, groupName) {
    if (!a.checked) {
		var s = groupName;
		unCheck(s);
		//a.checked = false;
    }
}
function onCheckSlave(a, masterName) {
    if (a.checked) {
		if (!isChecked(masterName) ) {
		  a.checked = false;
		} else {
		  a.checked = true;
		}
    }
}
</script>

<body onLoad="setfocus()">
<%--
@oscar.formDB Table="formBCNewBorn2008"
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'"
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL"
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL"
@oscar.formDB Field="formEdited" Type="timestamp"
@oscar.formDB Field="c_lastVisited" Type="char(3)"
--%>

<html:form action="/form/formname">
	<input type="hidden" name="c_lastVisited" value="pg1" />
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="provider_no" value=<%=""+provNo%> />
	<!--input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();" /></td>
			<%
  if (!bView) {
%>

			<td align="right"></td>
			<td align="right"><b>Edit:</b>
                            <a href="formBCNewBorn2008pg1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part 1<font size=-2></font></a> |
                Part 2 | <a href="formBCNewBorn2008pg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part2
			<font size=-2>(pg.2)</font></a></td>
			<%
  }
%>
		</tr>
	</table>



<table width="800"  border="1" cellspacing="1" height="500" >
    <tr>
        <td align="top" width="60%">
            <table cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="9hearingScreening">
                <tr>
                    <td rowspan="3" width="15%"><b>9. Date</b><br>
                                            <input type="text" name="Section9Date" id="Section9Date" size="8" maxlength="10" value="<%= props.getProperty("Section9Date", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section9Date_cal"> <br>

                        </td>
                    <td><b>Hearing Screening (completed by BC Early Hearing Program)</b><br>
                        <input type="checkbox" name="Section9Yes" <%= props.getProperty("Section9Yes", "") %> @oscar.formDB dbType="tinyint(1)" /> Yes
                               <input type="checkbox" name="Section9Passed" <%= props.getProperty("Section9Passed", "") %> @oscar.formDB dbType="tinyint(1)" /> Passed<br>
                               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                               <input type="checkbox" name="Section9PassedWithRiskFactor" <%= props.getProperty("Section9PassedWithRiskFactor", "") %> @oscar.formDB dbType="tinyint(1)" /> Passed with Risk Factors for Delayed Onset <br>
                               <input type="checkbox" name="Section9No" <%= props.getProperty("Section9No", "") %> @oscar.formDB dbType="tinyint(1)" /> No
                               &nbsp;&nbsp;<input type="checkbox" name="Section9Declined" <%= props.getProperty("Section9Declined", "") %> @oscar.formDB dbType="tinyint(1)" /> Declined
                               <input type="checkbox" name="Section9NA" <%= props.getProperty("Section9NA", "") %> @oscar.formDB dbType="tinyint(1)" />N/A<br>
                        Comment: <input type="text" name="Section9Comment" value="<%= props.getProperty("Section9Comment", "") %>" @oscar.formDB />
                                     </td>
                </tr>
                <tr>
                    <td>
                        Needs Follow-up: (by BC Early Hearing Program)<br>
                        <input type="checkbox" name="Section9AdditionalScreening" <%= props.getProperty("Section9AdditionalScreening", "") %> @oscar.formDB dbType="tinyint(1)" />Additional Screening
                               <input type="checkbox" name="Section9DiagnosticAssessment" <%= props.getProperty("Section9DiagnosticAssessment", "") %> @oscar.formDB dbType="tinyint(1)" />Diagnostic Assessment
                               <input type="checkbox" name="Section9Other" <%= props.getProperty("Section9Other", "") %> @oscar.formDB dbType="tinyint(1)" />Other
                               <input type="text" name="Section9OtherComment" value="<%= props.getProperty("Section9OtherComment", "") %>" @oscar.formDB />
                           </td>
                </tr>
                <tr>
                    <td>
                        Print Name: <input type="text" name="Section9PrintName" value="<%= props.getProperty("Section9PrintName", "") %>" @oscar.formDB />
                                           SIGNATURE: <input type="text" name="Section9Signature" value="<%= props.getProperty("Section9Signature", "") %>" @oscar.formDB />
                                      </td>
                </tr>
            </table>


        </td>

        <td rowspan="2">
        
<table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tr>
                <td >HOSPITAL NAME<br>
                    <input type="text" name="c_hospitalName"
                           <%=oscarVariables.getProperty("BCAR_hospital")==null? " ": ("class=\"spe\" onDblClick='showDef(\""+oscarVariables.getProperty("BCAR_hospital")+"\", this);'") %>
                           style="width: 100%" size="30" maxlength="80"
                           value="<%= props.getProperty("c_hospitalName", "") %>"
                       @oscar.formDB /></td>
                <td>DATE <img src="../images/cal.gif" id="pg1_formDate_cal">
                    <%=bSync? ("<b><a href=# onClick='syncDemo(); return false;'><font color='red'>Synchronize</font></a></b>") :"" %>
                    <br>
                    <input type="text" name="pg1_formDate" id="pg1_formDate" size="10"
                           maxlength="10"
                           value="<%= props.getProperty("pg1_formDate", "") %>" @oscar.formDB
                       /></td>
            </tr>
            <tr>
                <td>SURNAME<br>
                    <input type="text" name="c_surname" style="width: 100%" size="30"
                           maxlength="30" value="<%= props.getProperty("c_surname", "") %>"
                       @oscar.formDB /></td>
                <td>GIVEN NAME<br>
                    <input type="text" name="c_givenName" style="width: 100%" size="30"
                           maxlength="30" value="<%= props.getProperty("c_givenName", "") %>"
                       @oscar.formDB /></td>
            </tr>
            <tr>
                <td>ADDRESS<br>
                    <input type="text" name="c_address" style="width: 100%" size="50"
                           maxlength="60" value="<%= props.getProperty("c_address", "") %>"
                           @oscar.formDB /> <input type="text" name="c_city"
                                            style="width: 100%" size="50" maxlength="60"
                                            value="<%= props.getProperty("c_city", "") %>" @oscar.formDB /> <input
                        type="text" name="c_province" size="18" maxlength="50"
                        value="<%= props.getProperty("c_province", "") %>" @oscar.formDB />
                        <input type="text" name="c_postal" size="7" maxlength="8"
                           value="<%= props.getProperty("c_postal", "") %>" @oscar.formDB />
                       </td>
                <td valign="top">PHONE NUMBER<br>
                    <input type="text" name="c_phone" style="width: 100%" size="60"
                           maxlength="60" value="<%= props.getProperty("c_phone", "") %>"
                       @oscar.formDB /></td>
            </tr>
            <tr>
                <td colspan="2"><span class="small9"> 
                
               
                PHYSICIAN
                    / MIDWIFE NAME</span><br>
                    <input type="text" name="c_phyMid" style="width: 100%" size="30"
                           maxlength="60" value="<%= props.getProperty("c_phyMid", "") %>" @oscar.formDB /></td>
            </tr>
        </table>        
        
        </td>
    </tr>



    <tr><td>

            <table cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="Metaboilic Screening">
                <tr><td colspan="2" rowspan="2" width="15%"><b>10. Date</b><br>

                    <input type="text" name="Section10Date" id="Section10Date" size="8" maxlength="10" value="<%= props.getProperty("Section10Date", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section10Date_cal"> <br>
                    </td></tr>
                <tr>
                    <td>
                    <b>Metabolic Screening</b><br>
                    Blood Dot Card Collected: Age (h)
                    <input type="text" size="4" name="Section10MetabolicScreeningAge" value="<%= props.getProperty("Section10MetabolicScreeningAge", "") %>" @oscar.formDB />
                           <input type="checkbox" name="Section10MetabolicScreeningYes" <%= props.getProperty("Section10MetabolicScreeningYes", "") %> @oscar.formDB dbType="tinyint(1)" /> Yes
                           <input type="checkbox" name="Section10MetabolicScreeningNo" <%= props.getProperty("Section10MetabolicScreeningNo", "") %> @oscar.formDB dbType="tinyint(1)" /> No
                           Comment: <input type="text" size="4" name="Section10MetabolicScreeningComment" value="<%= props.getProperty("Section10MetabolicScreeningComment", "") %>" @oscar.formDB /><br>
                    Bilirubin Screening: Age (h)
                    <input type="text" size="4" name="Section10MetabolicScreeningBilirubinScreeningAge" value="<%= props.getProperty("Section10MetabolicScreeningBilirubinScreeningAge", "") %>" @oscar.formDB />
                           <input type="checkbox" name="Section10MetabolicScreeningBilirubinScreeningYes" <%= props.getProperty("Section10MetabolicScreeningBilirubinScreeningYes", "") %> @oscar.formDB dbType="tinyint(1)" /> Yes
                           <input type="checkbox" name="Section10MetabolicScreeningBilirubinScreeningNo" <%= props.getProperty("Section10MetabolicScreeningBilirubinScreeningNo", "") %> @oscar.formDB dbType="tinyint(1)" /> No
                           Comment: <input type="text" size="4" name="Section10MetabolicScreeningBilirubinScreeningComment" value="<%= props.getProperty("Section10MetabolicScreeningBilirubinScreeningComment", "") %>" @oscar.formDB /><br>
                    (nomogram on reverse)
                </tr>
            </table>



    </td><td></td></tr>


    <tr><td colspan="2">

            <table width="100% cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="Prophylaxis">
                <tr>
                    <td valign="top" width="15%">
                        <b>11. Date</b><br>
                            <input type="text" name="Section11Date" id="Section11Date" size="8" maxlength="10" value="<%= props.getProperty("Section11Date", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section11Date_cal"> <br>
                    </td>
                    <td>
                        <b>Prophylaxis</b><br>
                        HBsAg Prophylaxis Indicated:
                        <input type="checkbox" name="Section11HBsAgYes" <%= props.getProperty("Section11HBsAgYes", "") %> @oscar.formDB dbType="tinyint(1)" /> Yes
                               <input type="checkbox" name="Section11HBsAgNo" <%= props.getProperty("Section11HBsAgNo", "") %> @oscar.formDB dbType="tinyint(1)" /> No
                               <input type="checkbox" name="Section11HBsAgVaccine" <%= props.getProperty("Section11HBsAgVaccine", "") %> @oscar.formDB dbType="tinyint(1)" /> HBIG Given
                               <input type="checkbox" name="Section11HepatitisB" <%= props.getProperty("Section11HepatitisB", "") %> @oscar.formDB dbType="tinyint(1)" />Hepatitis B Vaccine Given <br>
                        HIV Prophylaxis Indicated:
                        <input type="checkbox" name="Section11HIVYes" <%= props.getProperty("Section11HIVYes", "") %> @oscar.formDB dbType="tinyint(1)" /> Yes
                               <input type="checkbox" name="Section11HIVNo" <%= props.getProperty("Section11HIVNo", "") %> @oscar.formDB dbType="tinyint(1)" /> No
                               <input type="checkbox" name="Section11HIVInitiated" <%= props.getProperty("Section11HIVInitiated", "") %> @oscar.formDB dbType="tinyint(1)" /> HIV Prophylaxis Initiated<br>
                        Group B Strep Intrapartum Prophylaxis:
                        <input type="checkbox" name="Section11StrepYes" <%= props.getProperty("Section11StrepYes", "") %> @oscar.formDB dbType="tinyint(1)" /> Yes
                               <input type="checkbox" name="Section11StrepNo" <%= props.getProperty("Section11StrepNo", "") %> @oscar.formDB dbType="tinyint(1)" /> No
                               Comment: <input type="text" name="Section11Comment" value="<%= props.getProperty("Section11Comment", "") %>" @oscar.formDB /><br>
                    </td>
                </tr>
            </table>


    </td></tr>



    <tr><td colspan="2">
            <table width="100% cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="Newborn Nutrition">
                <tr>
                    <td rowspan="2" valign="top" width="15%"><b>12. Date</b><br>
                        <input type="text" name="Section12Date" id="Section12Date" size="8" maxlength="10" value="<%= props.getProperty("Section12Date", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section12Date_cal"> <br>
                        </td>
                    <td>
                        <b>Newborn Nutrition</b><br>
                        Breastfeeding Initiated:
                        <input type="checkbox" name="Section121h" <%= props.getProperty("Section121h", "") %> @oscar.formDB dbType="tinyint(1)" /> &#8804;1 h
                               <input type="checkbox" name="Section12_1_2h" <%= props.getProperty("Section12_1_2h", "") %> @oscar.formDB dbType="tinyint(1)" /> >1-2 h
                               <input type="checkbox" name="Section12_2_24h" <%= props.getProperty("Section12_2_24h", "") %> @oscar.formDB dbType="tinyint(1)" /> >2-24 h
                               <input type="checkbox" name="Section12_g24h" <%= props.getProperty("Section12_g24h", "") %> @oscar.formDB dbType="tinyint(1)" /> > 24 h
                               <input type="checkbox" name="Section12NA" <%= props.getProperty("Section12NA", "") %> @oscar.formDB dbType="tinyint(1)" /> N/A
                               Comment: <input type="text" name="Section12Comment" value="<%= props.getProperty("Section12Comment", "") %>" @oscar.formDB /><br>
                        <input type="checkbox" name="Section12ExclusiveBreastmilk" <%= props.getProperty("Section12ExclusiveBreastmilk", "") %> @oscar.formDB dbType="tinyint(1)" /> Exclusive Breastmilk
                               <input type="checkbox" name="Section12PartialBreastmilk" <%= props.getProperty("Section12PartialBreastmilk", "") %> @oscar.formDB dbType="tinyint(1)" /> Partial Breastmilk
                               <input type="checkbox" name="Section12BreasmilkSubstitute" <%= props.getProperty("Section12BreasmilkSubstitute", "") %> @oscar.formDB dbType="tinyint(1)" /> Breastmilk Substitute
                               Supplementation Indication: <input type="text" name="Section12Supplementation" value="<%= props.getProperty("Section12Supplementation", "") %>" @oscar.formDB />
                                                       </td>
                </tr>
            </table>


    </td><td></td></tr>


    <tr><td colspan="2">

            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="Problem List">
                <tr>
                <td rowspan="2" width="15%"><b>13. Date</b><br><br>
                <input type="text" name="Section13Note1Date" id="Section13Note1Date" size="8" maxlength="10" value="<%= props.getProperty("Section13Note1Date", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section13Note1Date_cal"> <br>
                <input type="text" name="Section13Note2Date" id="Section13Note2Date" size="8" maxlength="10" value="<%= props.getProperty("Section13Note2Date", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section13Note2Date_cal"><br>
                <input type="text" name="Section13Note3Date" id="Section13Note3Date" size="8" maxlength="10" value="<%= props.getProperty("Section13Note3Date", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section13Note3Date_cal"><br>
                </td>

                <td><b>Problem List</b><br>
                    ACoRN Sequences Initiated:
                    <input type="checkbox" name="ProblemListYes" <%= props.getProperty("ProblemListYes", "") %> @oscar.formDB dbType="tinyint(1)" /> Yes
                    <input type="checkbox" name="ProblemListNo" <%= props.getProperty("ProblemListNo", "") %> @oscar.formDB dbType="tinyint(1)" /> No
                    <input type="checkbox" name="ProblemListSeeNotes" <%= props.getProperty("ProblemListSeeNotes", "") %> @oscar.formDB dbType="tinyint(1)" />
                    See Narrative Notes<br>
                <input type="text" name="Section13Note" size="100" value="<%= props.getProperty("Section13Note", "") %>" @oscar.formDB /><br>
                <input type="text" name="Section13Note2" size="100" value="<%= props.getProperty("Section13Note2", "") %>" @oscar.formDB /><br>
                <input type="text" name="Section13Note3" size="100" value="<%= props.getProperty("Section13Note3", "") %>" @oscar.formDB /><br>

                </td>
                <td rowspan="2"><b>Date Resolved <br>dd/mm/yyyy</b><br>
                    <input type="text" name="Section13Note1DateResolved" id="Section13Note1DateResolved" size="8" value="<%= props.getProperty("Section13Note1DateResolved", "") %>" @oscar.formDB dbType="date" /><img src="../images/cal.gif" id="Section13Note1DateResolved_cal"><br>
                <input type="text" name="Section13Note2DateResolved" id="Section13Note2DateResolved" size="8" value="<%= props.getProperty("Section13Note2DateResolved", "") %>" @oscar.formDB dbType="date" /><img src="../images/cal.gif" id="Section13Note2DateResolved_cal"><br>
                <input type="text" name="Section13Note3DateResolved" id="Section13Note3DateResolved" size="8" value="<%= props.getProperty("Section13Note3DateResolved", "") %>" @oscar.formDB dbType="date" /><img src="../images/cal.gif" id="Section13Note3DateResolved_cal">
                </td>
            </table>


    </td><td></td></tr>



    <tr><td colspan="2">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="Progress Notes">
                <tr>
                    <td rowspan="2" width="15%"><b>14. Date<br></b>
                    <input type="text" name="Section14Date1" id="Section14Date1" size="8" maxlength="10" value="<%= props.getProperty("Section14Date1", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section14Date1_cal"> <br>
                    <input type="text" name="Section14Date2" id="Section14Date2" size="8" maxlength="10" value="<%= props.getProperty("Section14Date2", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section14Date2_cal"> <br>
                    <input type="text" name="Section14Date3" id="Section14Date3" size="8" maxlength="10" value="<%= props.getProperty("Section14Date3", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section14Date3_cal"> <br>
                    <input type="text" name="Section14Date4" id="Section14Date4" size="8" maxlength="10" value="<%= props.getProperty("Section14Date4", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section14Date4_cal"> <br>
                    <input type="text" name="Section14Date5" id="Section14Date5" size="8" maxlength="10" value="<%= props.getProperty("Section14Date5", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section14Date5_cal"> <br>
                    <input type="text" name="Section14Date6" id="Section14Date6" size="8" maxlength="10" value="<%= props.getProperty("Section14Date6", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section14Date6_cal"> <br>
                    <input type="text" name="Section14Date7" id="Section14Date7" size="8" maxlength="10" value="<%= props.getProperty("Section14Date7", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section14Date7_cal"> <br>
                    <input type="text" name="Section14Date8" id="Section14Date8" size="8" maxlength="10" value="<%= props.getProperty("Section14Date8", "") %>" @oscar.formDB dbType="date" /> <img src="../images/cal.gif" id="Section14Date8_cal"> <br>

                    </td>
                    <td><b>Progress Notes</b><br>
                        <input type="text" name="Section14ProgressNotes1" size="100" value="<%= props.getProperty("Section14ProgressNotes1", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section14ProgressNotes2" size="100" value="<%= props.getProperty("Section14ProgressNotes2", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section14ProgressNotes3" size="100" value="<%= props.getProperty("Section14ProgressNotes3", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section14ProgressNotes4" size="100" value="<%= props.getProperty("Section14ProgressNotes4", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section14ProgressNotes5" size="100" value="<%= props.getProperty("Section14ProgressNotes5", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section14ProgressNotes6" size="100" value="<%= props.getProperty("Section14ProgressNotes6", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section14ProgressNotes7" size="100" value="<%= props.getProperty("Section14ProgressNotes7", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section14ProgressNotes8" size="100" value="<%= props.getProperty("Section14ProgressNotes8", "") %>" @oscar.formDB /><br>
                    </td>
                </tr>
            </table>



    </td><td></td></tr>




    <tr >
        <td valign="top">

            <table  width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12; "  summary="Discharge Examination">
                <tr>
                    <td>
                        <b>15. Discharge Examination</b><br>
                        Newborn Age:
                        <input type="checkbox" name="Section1512h" <%= props.getProperty("Section1512h", "") %> @oscar.formDB dbType="tinyint(1)" /> &#8804;12h
                               <input type="checkbox" name="Section151314h" <%= props.getProperty("Section151314h", "") %> @oscar.formDB dbType="tinyint(1)" /> 13-24 h
                               <input type="checkbox" name="Section152548h" <%= props.getProperty("Section152548h", "") %> @oscar.formDB dbType="tinyint(1)" /> 25-48 h
                               <input type="checkbox" name="Section154972h" <%= props.getProperty("Section154972h", "") %> @oscar.formDB dbType="tinyint(1)" /> 49-72 h
                               <input type="checkbox" name="Section15GT72" <%= props.getProperty("Section15GT72", "") %> @oscar.formDB dbType="tinyint(1)" /> >72 h <br>
                        Head Circumference
                        <input type="text" size="4" name="Section15HC" value="<%= props.getProperty("Section15HC", "") %>" @oscar.formDB /> cm
                               Weight:
                               <input type="text" size="4" name="Section15Weight" value="<%= props.getProperty("Section15Weight", "") %>" @oscar.formDB /> g
                               Weight loss:
                               <input type="text" size="4" name="Section15WeightLoss" value="<%= props.getProperty("Section15WeightLoss", "") %>" @oscar.formDB />% <br>

                        Normal Abnormal Comment: <br>
                        1. General
                        <input type="checkbox" name="Section15GeneralNormal" <%= props.getProperty("Section15GeneralNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15GeneralAbnormal" <%= props.getProperty("Section15GeneralAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15GeneralComment" value="<%= props.getProperty("Section15GeneralComment", "") %>" @oscar.formDB /><br>
                        2. Skin
                        <input type="checkbox" name="Section15SkinNormal" <%= props.getProperty("Section15SkinNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15SkinAbnormal" <%= props.getProperty("Section15SkinAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15SkinComment" value="<%= props.getProperty("Section15SkinComment", "") %>" @oscar.formDB /><br>
                        3. Head
                        <input type="checkbox" name="Section15HeadNormal" <%= props.getProperty("Section15HeadNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15HeadAbnormal" <%= props.getProperty("Section15HeadAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15HeadComment" value="<%= props.getProperty("Section15HeadComment", "") %>" @oscar.formDB /><br>
                        4. EENT
                        <input type="checkbox" name="Section15EENTNormal" <%= props.getProperty("Section15EENTNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15EENTAbnormal" <%= props.getProperty("Section15EENTAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15EENTComment" value="<%= props.getProperty("Section15EENTComment", "") %>" @oscar.formDB /><br>
                        5. Respiratory
                        <input type="checkbox" name="Section15RespiratoryNormal" <%= props.getProperty("Section15RespiratoryNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15RespiratoryAbnormal" <%= props.getProperty("Section15RespiratoryAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15RespiratoryComment" value="<%= props.getProperty("Section15RespiratoryComment", "") %>" @oscar.formDB /><br>
                        6. CVS
                        <input type="checkbox" name="Section15CVSNormal" <%= props.getProperty("Section15CVSNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15CVSAbnormal" <%= props.getProperty("Section15CVSAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15CVSComment" value="<%= props.getProperty("Section15CVSComment", "") %>" @oscar.formDB /><br>
                        7. Abdomen
                        <input type="checkbox" name="Section15AbdomenNormal" <%= props.getProperty("Section15AbdomenNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15AbdomenAbnormal" <%= props.getProperty("Section15AbdomenAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15AbdomenComment" value="<%= props.getProperty("Section15AbdomenComment", "") %>" @oscar.formDB /><br>
                        8. Unbilical Cord
                        <input type="checkbox" name="Section15UnbilicalCordNormal" <%= props.getProperty("Section15UnbilicalCordNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15UnbilicalCordAbnormal" <%= props.getProperty("Section15UnbilicalCordAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15UnbilicalCordComment" value="<%= props.getProperty("Section15UnbilicalCordComment", "") %>" @oscar.formDB /><br>
                        9. Genitorectal
                        <input type="checkbox" name="Section15GenitorectalNormal" <%= props.getProperty("Section15GenitorectalNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15GenitorectalAbnormal" <%= props.getProperty("Section15GenitorectalAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15GenitorectalComment" value="<%= props.getProperty("Section15GenitorectalComment", "") %>" @oscar.formDB /><br>
                        10. Musculoskeletal
                        <input type="checkbox" name="Section15MusculoskeletalNormal" <%= props.getProperty("Section15MusculoskeletalNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15MusculoskeletalAbnormal" <%= props.getProperty("Section15MusculoskeletalAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15MusculoskeletalComment" value="<%= props.getProperty("Section15MusculoskeletalComment", "") %>" @oscar.formDB /><br>
                        11. Neurological
                        <input type="checkbox" name="Section15NeurologicalNormal" <%= props.getProperty("Section15NeurologicalNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15NeurologicalAbnormal" <%= props.getProperty("Section15NeurologicalAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15NeurologicalComment" value="<%= props.getProperty("Section15NeurologicalComment", "") %>" @oscar.formDB /><br>
                        12. Other
                        <input type="checkbox" name="Section15OtherNormal" <%= props.getProperty("Section15OtherNormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="checkbox" name="Section15OtherAbnormal" <%= props.getProperty("Section15OtherAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" />
                               <input type="text" name="Section15OtherComment" value="<%= props.getProperty("Section15OtherComment", "") %>" @oscar.formDB /><br>
                    </td>
                </tr>
            </table>

            <table  width="100%" cellpadding="0" cellspacing="0" border="0" summary="Date Sig">
                <tr>
                    <td>Date<br>
                    <input type="text" name="Date" id="Date" value="<%= props.getProperty("Date", "") %>" @oscar.formDB dbType="date" /><img src="../images/cal.gif" id="Date_cal"><br>dd/mm/yyyy</td>
                    <td>SIGNATURE<br><input type="text" name="SIGNATURE" value="<%= props.getProperty("SIGNATURE", "") %>" @oscar.formDB /><br>
                        MD/RM
                    </td>
                </tr>
            </table>

        </td>


        <td valign="top">

            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="Status at Discharge">
                <tr>
                    <td>
                        <b>16. Status at Discharge</b><br>
                        <textarea name="Section16Text"  @oscar.formDB dbType="varchar(255)" /> <%= props.getProperty("Section16Text", "") %></textarea><br>
                        <input type="checkbox" name="Section16Exclusive" <%= props.getProperty("Section16Exclusive", "") %> @oscar.formDB dbType="tinyint(1)" /> Exclusive Breastmilk
                               <input type="checkbox" name="Section16Partial" <%= props.getProperty("Section16Partial", "") %> @oscar.formDB dbType="tinyint(1)" /> Partial Breastmilk
                               <input type="checkbox" name="Section16Substitute" <%= props.getProperty("Section16Substitute", "") %> @oscar.formDB dbType="tinyint(1)" /> Breastmilk Substitute <br>
                        Comment: <input type="text" name="Section16Comment" value="<%= props.getProperty("Section16Comment", "") %>" @oscar.formDB />
                                    </td>
                </tr>
            </table>


            <table width="100%" cellpadding="0" cellspacing="0" border="0" summary="Problems Requiring Follow-up">
                <tr>
                    <td>
                        Problems requiring Follow-up:<br>
                        <textarea name="ProblemsrequiringFollowup" @oscar.formDB /> <%= props.getProperty("ProblemsrequiringFollowup", "") %> </textarea>
                    </td>
                </tr>
            </table>

            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="Discharged">
                <tr>
                    <td><b>17. Discharged</b><br>
                </td></tr>
                <tr><td>
                        <input type="checkbox" name="Section17Home" <%= props.getProperty("Section17Home", "") %> @oscar.formDB dbType="tinyint(1)" /> Home
                               <input type="checkbox" name="Section17Adoption" <%= props.getProperty("Section17Adoption", "") %> @oscar.formDB dbType="tinyint(1)" /> Adoption
                               <input type="checkbox" name="Section17Foster" <%= props.getProperty("Section17Foster", "") %> @oscar.formDB dbType="tinyint(1)" /> Foster Home<br>
                               <input type="checkbox" name="Section17OtherHospital" <%= props.getProperty("Section17OtherHospital", "") %> @oscar.formDB dbType="tinyint(1)" /> Other Hospital
	                            <input type="text" name="Section17OtherHospitalSpecify" value="<%= props.getProperty("Section17OtherHospitalSpecify", "") %>" @oscar.formDB /> &nbsp;specify
                           </td>
                </tr>
                
                <tr>
                <td>
                <input type="checkbox" name="Section17Other" <%= props.getProperty("Section17Other", "") %> @oscar.formDB dbType="tinyint(1)" />Other 
                </td>
                </tr>
            </table>




            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="Follow-up">
                <tr>
                    <td>
                        <b>18. Follow-up by</b><br>
                        <input type="checkbox" name="Section18FamilyPhsician" <%= props.getProperty("Section18FamilyPhsician", "") %> @oscar.formDB dbType="tinyint(1)" />Family Physician<br>
                        <input type="checkbox" name="Section18Midwife" <%= props.getProperty("Section18Midwife", "") %> @oscar.formDB dbType="tinyint(1)" />Midwife<br>
                        <input type="checkbox" name="Section18Pediatrician" <%= props.getProperty("Section18Pediatrician", "") %> @oscar.formDB dbType="tinyint(1)" />Pediatrician<br>
                        <input type="checkbox" name="Section18OtherConsultant" <%= props.getProperty("Section18OtherConsultant", "") %> @oscar.formDB dbType="tinyint(1)" />Other Consultant<br>
                        <input type="checkbox" name="Section18PublicHealthNurse" <%= props.getProperty("Section18PublicHealthNurse", "") %> @oscar.formDB dbType="tinyint(1)" />Public Health Nurse<br>
                        <input type="checkbox" name="Section18MinistryforChildern" <%= props.getProperty("Section18MinistryforChildern", "") %> @oscar.formDB dbType="tinyint(1)" />Ministry for <br>
                        Childern &amp; Family Developemnt
                    </td>
                    <td>
                    		dd/mm/yyyy<br>
                        <input type="text" name="Section18FamilyPhsicianDate" size="8" value="<%= props.getProperty("Section18FamilyPhsicianDate", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section18MidwifeDate" size="8" value="<%= props.getProperty("Section18MidwifeDate", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section18PediatricianDate" size="8" value="<%= props.getProperty("Section18PediatricianDate", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section18OtherConsultantDate" size="8" value="<%= props.getProperty("Section18OtherConsultantDate", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section18PublicHealthNurseDate" size="8" value="<%= props.getProperty("Section18PublicHealthNurseDate", "") %>" @oscar.formDB /><br>
                        <input type="text" name="Section18MinistryforChildernDate" size="8" value="<%= props.getProperty("Section18MinistryforChildernDate", "") %>" @oscar.formDB /><br>
                    </td>
                </tr>
            </table>

            <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12; " summary="other">
                <tr>
                    <td>
                        <input type="checkbox" name="NeonatalDeath" <%= props.getProperty("NeonatalDeath", "") %> @oscar.formDB dbType="tinyint(1)" /> Neonatal Death<br>
                        <input type="checkbox" name="AutopsyConsented" <%= props.getProperty("AutopsyConsented", "") %> @oscar.formDB dbType="tinyint(1)" /> Autopsy Consented
                    </td>
                </tr>
            </table>

        </td>
    </tr>

</table>









































</html:form>
<script type="text/javascript">
Calendar.setup({ inputField : "Section9Date", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section9Date_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section10Date", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section10Date_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section11Date", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section11Date_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section12Date", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section12Date_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section13Note1Date", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section13Note1Date_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section13Note2Date", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section13Note2Date_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section13Note3Date", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section13Note3Date_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section13Note1DateResolved", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section13Note1DateResolved_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section13Note2DateResolved", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section13Note2DateResolved_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section13Note3DateResolved", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section13Note3DateResolved_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "Section14Date1", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section14Date1_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section14Date2", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section14Date2_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section14Date3", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section14Date3_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section14Date4", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section14Date4_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section14Date5", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section14Date5_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section14Date6", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section14Date6_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section14Date7", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section14Date7_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "Section14Date8", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section14Date8_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "pg1_formDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_formDate_cal", singleClick : true, step : 1 });





Calendar.setup({ inputField : "Date", ifFormat : "%d/%m/%Y", showsTime :false, button : "Date_cal", singleClick : true, step : 1 });




</script>
</body>
</html:html>
