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
	String formClass = "BCBirthSumMo2008";
	String formLink = "formbcbirthsummo2008.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
    //props.setProperty("c_lastVisited", "pg1");

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
<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Birth Summary (Mother:)</title>
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
        document.forms[0].submit.value="print"; 
        var ret = checkAllDates();
        if(ret==true)
        {            
            document.forms[0].action = "../form/createpdf?__title=British+Columbia+Labour+and+Birth+Summary+Record+2008&__cfgfile=bclb2008PrintCfgPg1&__template=bcbirthsummary2008";
            document.forms[0].target="_blank";            
        }
        return ret;
    }


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


    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true) {
			ret = checkAllTimes();
		}
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
    
function syncDemo() { 
    document.forms[0].c_surname.value = "<%=props.getProperty("c_surname_cur", "")%>";
    document.forms[0].c_givenName.value = "<%=props.getProperty("c_givenName_cur", "")%>";
    document.forms[0].c_address.value = "<%=props.getProperty("c_address_cur", "")%>";
    document.forms[0].c_city.value = "<%=props.getProperty("c_city_cur", "")%>";
    document.forms[0].c_province.value = "<%=props.getProperty("c_province_cur", "")%>";
    document.forms[0].c_postal.value = "<%=props.getProperty("c_postal_cur", "")%>";
    document.forms[0].c_phn.value = "<%=props.getProperty("c_phn_cur", "")%>";
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

    function valDate(dateBox)   {
        try   {
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
        }  catch (ex) {
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
        if(valDate(document.forms[0].birTimeDate1)==false){
            b = false;
        } else if(valDate(document.forms[0].birTimeDate2)==false){
            b = false;
        } else if(valDate(document.forms[0].birTimeDate3)==false){
            b = false;
        } else if(valDate(document.forms[0].birTimeDate4)==false){
            b = false;
        } else if(valDate(document.forms[0].birTimeDate5)==false){
            b = false;
        } 

        return b;
    }

	function checkAllTimes() {
        var b = true;
        if(valTime(document.forms[0].birTimeHour1)==false){
            b = false;
        } else if(valTime(document.forms[0].birTimeHour2)==false){
            b = false;
        } else if(valTime(document.forms[0].birTimeHour3)==false){
            b = false;
        } else if(valTime(document.forms[0].birTimeHour4)==false){
            b = false;
        } else if(valTime(document.forms[0].birTimeHour5)==false){
            b = false;
        } 

        return b;
    }
    
function calc1() {
	sDate = document.forms[0].birTimeDate2.value;
	eDate = document.forms[0].birTimeDate3.value;
	
	sTime = document.forms[0].birTimeHour2.value;
	eTime = document.forms[0].birTimeHour3.value;
	objHour = document.forms[0].birDurHour1;
	objMin = document.forms[0].birDurMin1;
	//alert(sHour + sMin + " " + eHour + eMin);
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
}
function calc2() {
	sDate = document.forms[0].birTimeDate3.value;
	eDate = document.forms[0].birTimeDate4.value;
	sTime = document.forms[0].birTimeHour3.value;
	eTime = document.forms[0].birTimeHour4.value;
	objHour = document.forms[0].birDurHour2;
	objMin = document.forms[0].birDurMin2;
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
}
function calc3() {
	sDate = document.forms[0].birTimeDate4.value;
	eDate = document.forms[0].birTimeDate5.value;
	sTime = document.forms[0].birTimeHour4.value;
	eTime = document.forms[0].birTimeHour5.value;
	objHour = document.forms[0].birDurHour3;
	objMin = document.forms[0].birDurMin3;
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
}
function calcRup() {
	sDate = document.forms[0].birTimeDate1.value;
	eDate = document.forms[0].birTimeDate4.value;
	sTime = document.forms[0].birTimeHour1.value;
	eTime = document.forms[0].birTimeHour4.value;
	objHour = document.forms[0].birDurRupHour;
	objMin = document.forms[0].birDurRupMin;
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
	document.forms[0].birDurRupHour.value = document.forms[0].birDurRupHour.value + ":" + objMin.value;
}
function calcLOS() {
	tempV = document.forms[0].admisDateTime.value;
	sDate = tempV.length>10? tempV.substring(0,10) : "";
	sTime = tempV.length>10? tempV.substring(11) : "";
	tempV = document.forms[0].dischargeDateTime.value;
	eDate = tempV.length>10? tempV.substring(0,10) : "";
	eTime = tempV.length>10? tempV.substring(11) : "";
	objHour = document.forms[0].conLos;
	objMin = "";
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
}
function calcPPLOS() {
	sDate = document.forms[0].birTimeDate5.value;
	sTime = document.forms[0].birTimeHour5.value;
	tempV = document.forms[0].dischargeDateTime.value;
	eDate = tempV.length>10? tempV.substring(0,10) : "";
	eTime = tempV.length>10? tempV.substring(11) : "";
	objHour = document.forms[0].ppLos;
	objMin = "";
	calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin);
}
function calculateDiffDateTime(sDate, sTime, eDate, eTime, objHour, objMin) {
    var sdd = sDate.substring(0, sDate.indexOf("/"));
    var smm = eval(sDate.substring(eval(sDate.indexOf("/")+1), sDate.lastIndexOf("/")) - 1);
    var syyyy  = sDate.substring(eval(sDate.lastIndexOf("/")+1));
    var edd = eDate.substring(0, eDate.indexOf("/"));
    var emm = eval(eDate.substring(eval(eDate.indexOf("/")+1), eDate.lastIndexOf("/")) - 1);
    var eyyyy  = eDate.substring(eval(eDate.lastIndexOf("/")+1));
	tempV = sTime;
	sHour = tempV.substring(0, tempV.indexOf(":"));
	sMin = tempV.substring(eval(tempV.indexOf(":")+1) );
	tempV = eTime;
	eHour = tempV.substring(0, tempV.indexOf(":"));
	eMin = tempV.substring(eval(tempV.indexOf(":")+1) );
    var s_date=new Date(syyyy,smm,sdd,sHour,sMin,0);
    var e_date=new Date(eyyyy,emm,edd,eHour,eMin,0);

	var one_hour=1000*60*60;
	//alert(s_date.getTime());	//alert(e_date.getTime());
	b = Math.floor((e_date.getTime()-s_date.getTime())/one_hour);
	objHour.value = b;
	b = Math.ceil((e_date.getTime()-s_date.getTime())%one_hour);
	b = Math.ceil(b/(1000*60));
	objMin.value = b;
    return b;
}
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1"
	onLoad="setfocus()">
<!--
@oscar.formDB Table="formBCBirthSumMo2008" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
-->
<html:form action="/form/formname">
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> /-->
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
				value="Print" onclick="javascript:return onPrint();return false;" />
			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="50%" valign="top">

			<table class="headline" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
					British Columbia Labour and Birth Summary Record</th>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th align="left">1. IDENTIFICATION</th>
				</tr>
				<tr>
					<td>

					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<tr>
							<td width="40%" rowspan="2" valign="top">NEWBORN ID NUMBER<br>
							<input type="text" name="c_newBornID" style="width: 100%"
								size="30" maxlength="30"
								value="<%= props.getProperty("c_newBornID", "") %>"
								@oscar.formDB /></td>
							<td width="20%" valign="top"><input type="checkbox"
								name="singleton"
								<%= props.getProperty("singleton", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Singleton</td>
							<td valign="top"><input type="checkbox" name="twinA"
								<%= props.getProperty("twinA", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Twin A <br>
							<input type="checkbox" name="twinB"
								<%= props.getProperty("twinB", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Twin B</td>
							<td><input type="checkbox" name="tripletA"
								<%= props.getProperty("tripletA", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Triplet A <br>
							<input type="checkbox" name="tripletB"
								<%= props.getProperty("tripletB", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Triplet B <br>
							<input type="checkbox" name="tripletC"
								<%= props.getProperty("tripletC", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Triplet C</td>
						</tr>
						<tr>
							<td colspan="3">Gest. Age: <input type="text" name="gestAge"
								size="5" maxlength="5"
								value="<%= props.getProperty("gestAge", "") %>" @oscar.formDB />
							wks. <input type="text" name="gestWks" size="5" maxlength="5"
								value="<%= props.getProperty("gestWks", "") %>" @oscar.formDB />
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td><I>(status prior to this delivery as on Antenatal
					Record, Part 1)</I><br>
					<br>
					<b>G&nbsp;</b><input type="text"
						name="pg1_gravida" size="4" maxlength="4"
						value="<%= props.getProperty("pg1_gravida", "") %>" @oscar.formDB />
					<b>T&nbsp;</b><input type="text"
						name="pg1_term" size="3" maxlength="4"
						value="<%= props.getProperty("pg1_term", "") %>" @oscar.formDB />
					<b>P&nbsp;</b><input type="text"
						name="pg1_preterm" size="3" maxlength="4"
						value="<%= props.getProperty("pg1_preterm", "") %>" @oscar.formDB />
					<b>A&nbsp;</b><input type="text"
						name="pg1_abortion" size="3" maxlength="3"
						value="<%= props.getProperty("pg1_abortion", "") %>" @oscar.formDB />
					<b>L&nbsp;</b><input type="text"
						name="pg1_living" size="3" maxlength="3"
						value="<%= props.getProperty("pg1_living", "") %>" @oscar.formDB /><br>
					<b>EDD&nbsp;</b><input type="text"	name="pg1_EDD" id="pg1_EDD" size="11" maxlength="11"
						value="<%= props.getProperty("pg1_EDD", "") %>" @oscar.formDB />	<img src="../images/cal.gif" id="pg1_EDD_cal">
					</td>
				</tr>
			</table>

			</td>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="55%">DATE <img src="../images/cal.gif"
						id="pg1_formDate_cal"> <%=bSync? ("<b><a href=# onClick='syncDemo(); return false;'><font color='red'>Synchronize</font></a></b>") :"" %>
					<br>
					<input type="text" name="pg1_formDate" id="pg1_formDate"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_formDate", "") %>" @oscar.formDB
						dbType="date" /></td>
					<td>MOTHER'S I.D. NUMBER<br>
					<input type="text" name="pg1_motherID" style="width: 100%"
						size="15" maxlength="20"
						value="<%= props.getProperty("pg1_motherID", "") %>" @oscar.formDB />
					</td>
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
					<td>PERSONAL HEALTH NUMBER<br>
					<input type="text" name="c_phn" style="width: 100%" size="20"
						maxlength="20" value="<%= props.getProperty("c_phn", "") %>"
						@oscar.formDB /></td>
					<td><span class="small9"> <a href=#
						onClick="popupFixedPage(600, 300, 'formbcarpg1namepopup.jsp?fieldname=c_phyMid'); return false;">PHYSICIAN
					/ MIDWIFE NAME</a></span><br>
					<input type="text" name="c_phyMid" style="width: 100%" size="30"
						maxlength="60" value="<%= props.getProperty("c_phyMid", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<th align="left">2. Labour</th>
		</tr>
		<tr>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="45%" nowrap><span class="small9"> 
					   
					   <input type="checkbox" name="labNoLabour1" <%= props.getProperty("labNoLabour1", "") %>  @oscar.formDB dbType="tinyint(1)" /> No Labour &nbsp;&nbsp; <br>
					   
					   <input type="checkbox" name="labSpontaneous1" <%= props.getProperty("labSpontaneous1", "") %>  @oscar.formDB dbType="tinyint(1)" /> SPONTANEOUS &nbsp;&nbsp; <br> 
						
						<input type="checkbox" name="labAugmented1" <%= props.getProperty("labAugmented1", "") %>  @oscar.formDB dbType="tinyint(1)" /> AUGMENTED: &nbsp;&nbsp; 
						
						<input type="checkbox" name="labArm1" <%= props.getProperty("labArm1", "") %>  @oscar.formDB dbType="tinyint(1)" /> ARM &nbsp;&nbsp; 
						
						<input type="checkbox" name="labOxytocin1" <%= props.getProperty("labOxytocin1", "") %>  @oscar.formDB dbType="tinyint(1)" /> OXYTOCIN &nbsp;&nbsp; </span>
					
						<input type="checkbox"	name="labOtherChk1" <%= props.getProperty("labOtherChk1", "") %>  @oscar.formDB dbType="tinyint(1)" /> OTHER: 
					
						<input type="text" name="labOther1" style="size="15" maxlength="40" value="<%= props.getProperty("labOther1", "") %>" @oscar.formDB /> &nbsp;Indication
					
						<input type="text" name="labIndication1" style="size="20" maxlength="40" value="<%= props.getProperty("labIndication1", "") %>"	@oscar.formDB /> 
					
					</td>
				</tr>
				<tr>
					<td nowrap><span class="small8"> <input type="checkbox"
						name="labInduced2"
						<%= props.getProperty("labInduced2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> INDUCED &nbsp;&nbsp; 
						
						<input type="checkbox"
						name="labFoley2"
						<%= props.getProperty("labFoley2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> FOLEY &nbsp;&nbsp; 


						<input type="checkbox"
						name="labArm2"
						<%= props.getProperty("labArm2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> ARM: &nbsp;&nbsp; 
						
												
						<input
						type="checkbox" name="labOxytocin2"
						<%= props.getProperty("labOxytocin2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OXYTOCIN &nbsp;&nbsp; </span>
						
						<input type="checkbox"
						name="labProstaglandin2"
						<%= props.getProperty("labProstaglandin2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> PROSTAGLANDIN, # Inserted&nbsp; 
				<input type="text" name="labNumInserted" size="2" maxlength="2" value="<%= props.getProperty("labNumInserted", "") %>" @oscar.formDB />
					
					<input type="checkbox" name="labOtherChk2"
						<%= props.getProperty("labOtherChk2", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> <span class="small9"> OTHER: </span>
					<input type="text" name="labOther2" style="size="15" maxlength="60"
						value="<%= props.getProperty("labOther2", "") %>" @oscar.formDB />

					<span class="small9"> &nbsp;Primary Indication </span>
					
					
					<input type="text" name="labIndication2" style="size="50" maxlength="80" value="<%= props.getProperty("labIndication2", "") %>" @oscar.formDB />

						</td>
				</tr>
			</table>

			</td>
		</tr>
		<tr>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th align="left">3. Intrapartum</th>
				</tr>					
			
			
				
				<tr>
					<td><span class="small9"> <B>Liquor:</B> <input
						type="checkbox" name="labAmnFluClear"
						<%= props.getProperty("labAmnFluClear", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> CLEAR &nbsp;&nbsp; 

						<input type="checkbox"
						name="labAmnFluMeco"
						<%= props.getProperty("labAmnFluMeco", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> MECONIUM &nbsp;&nbsp;
						
						<input type="checkbox"
						name="labAmnFluBloody"
						<%= props.getProperty("labAmnFluBloody", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> BLOODY &nbsp;&nbsp; 
						
						</td>
					</tr><tr>	
						<td><span class="small9"> Fetal Surveillance&nbsp;&nbsp;<input
						type="checkbox" name="labInterAusc"
						<%= props.getProperty("labInterAusc", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> INTERMITTENT AUSCULTATION &nbsp;&nbsp; 
						
						<input
						type="checkbox" name="labExtEfm"
						<%= props.getProperty("labExtEfm", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> External EFM &nbsp;&nbsp; 
						
						<input
						type="checkbox" name="labIntEfm"
						<%= props.getProperty("labIntEfm", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> Internal EFM &nbsp;&nbsp; 

						
											
						<input
						type="checkbox" name="labIupc"
						<%= props.getProperty("labIupc", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> IUPC &nbsp;&nbsp;
						
						
						Indication for EFM						
						<input type="text"
						name="labIndicationEFM" size="20" maxlength="20"
						value="<%= props.getProperty("labIndicationEFM", "") %>"
						@oscar.formDB /> 
								
						
						<br>
					
					
						<input
						type="checkbox" name="labFetalBld"
						<%= props.getProperty("labFetalBld", "") %>  @oscar.formDB
						dbType="tinyint(1)" />FETAL BLOOD SAMPLING &nbsp;
											
					
						LOWEST: pH <input type="text"
						name="labFetalBldPh" size="10" maxlength="10"
						value="<%= props.getProperty("labFetalBldPh", "") %>"
						@oscar.formDB /> Base Excess <input type="text" name="labFetalBldBe"
						size="10" maxlength="10"
						value="<%= props.getProperty("labFetalBldBe", "") %>"
						@oscar.formDB /> </span></td>
				</tr>
				<tr>
							<table width="100%" border="1" cellspacing="0" cellpadding="0">
<td width="33%">

<b>Fetal Presentation</b>

<input type="checkbox" name="labCephalic" <%= props.getProperty("labCephalic", "") %>  @oscar.formDB dbType="tinyint(1)" />Cephalic &nbsp;
<input type="checkbox" name="labBreech" <%= props.getProperty("labBreech", "") %>  @oscar.formDB dbType="tinyint(1)" />Breech &nbsp;
<input type="checkbox" name="labFrank" <%= props.getProperty("labFrank", "") %>  @oscar.formDB dbType="tinyint(1)" />Frank &nbsp;
<br>
<input type="checkbox" name="labComplete" <%= props.getProperty("labComplete", "") %>  @oscar.formDB dbType="tinyint(1)" />Complete &nbsp;
<input type="checkbox" name="labIncomplete" <%= props.getProperty("labIncomplete", "") %>  @oscar.formDB dbType="tinyint(1)" />Incomplete &nbsp;
<input type="checkbox" name="labFootling" <%= props.getProperty("labFootling", "") %>  @oscar.formDB dbType="tinyint(1)" />Footling &nbsp;
<br>
Other Presentation (specifiy) <input type="text" name="labOtherPresentation" style="size="50" maxlength="80" value="<%= props.getProperty("labOtherPresentation", "") %>" @oscar.formDB />
				</td>
				
				<td width="33%">
				<b>Analgesia/Anaesthesia</b><br>
				<input type="checkbox" name="AnalgesiaNone" <%= props.getProperty("AnalgesiaNone", "") %>  @oscar.formDB dbType="tinyint(1)" />None &nbsp;
				<input type="checkbox" name="AnalgesiaOpioids" <%= props.getProperty("AnalgesiaOpioids", "") %>  @oscar.formDB dbType="tinyint(1)" />Opioids &nbsp;
				<input type="checkbox" name="AnalgesiaEntonox" <%= props.getProperty("AnalgesiaEntonox", "") %>  @oscar.formDB dbType="tinyint(1)" />Entonox &nbsp;
				<br>
				<input type="checkbox" name="AnalgesiaLocal" <%= props.getProperty("AnalgesiaLocal", "") %>  @oscar.formDB dbType="tinyint(1)" />Local &nbsp;
				<input type="checkbox" name="AnalgesiaPudendal" <%= props.getProperty("AnalgesiaPudendal", "") %>  @oscar.formDB dbType="tinyint(1)" />Pudendal &nbsp;
				<input type="checkbox" name="AnalgesiaOther" <%= props.getProperty("AnalgesiaOther", "") %>  @oscar.formDB dbType="tinyint(1)" />Other: &nbsp;
				<input type="text" name="AnalgesiaOthertext" size="10" maxlength="10" value="<%= props.getProperty("AnalgesiaOthertext", "") %>" @oscar.formDB />
				
				<br>
				Labour <input type="checkbox" name="LabourEpidural" <%= props.getProperty("LabourEpidural", "") %>  @oscar.formDB dbType="tinyint(1)" />Epidural &nbsp;
				<input type="checkbox" name="LabourSpinal" <%= props.getProperty("LabourSpinal", "") %>  @oscar.formDB dbType="tinyint(1)" />Spinal &nbsp;
				<input type="checkbox" name="LabourCombined" <%= props.getProperty("LabourCombined", "") %>  @oscar.formDB dbType="tinyint(1)" />Combined &nbsp;
				<br>
				CS <input type="checkbox" name="CSEpidural" <%= props.getProperty("CSEpidural", "") %>  @oscar.formDB dbType="tinyint(1)" />Epidural &nbsp;
				<input type="checkbox" name="CSSpinal" <%= props.getProperty("CSSpinal", "") %>  @oscar.formDB dbType="tinyint(1)" />Spinal &nbsp;
				<input type="checkbox" name="CSCombined" <%= props.getProperty("CSCombined", "") %>  @oscar.formDB dbType="tinyint(1)" />Combined &nbsp;
				<input type="checkbox" name="CSGeneral" <%= props.getProperty("CSGeneral", "") %>  @oscar.formDB dbType="tinyint(1)" />General &nbsp;
				</td>
				
				<td width="33%">
				
				<b>Prophylactic Antibiotics</b>
				<br>
				<input type="checkbox" name="ProphylacticNone" <%= props.getProperty("ProphylacticNone", "") %>  @oscar.formDB dbType="tinyint(1)" />None &nbsp;<br>
				<input type="checkbox" name="ProphylacticIntrapartum" <%= props.getProperty("ProphylacticIntrapartum", "") %>  @oscar.formDB dbType="tinyint(1)" />Intrapartum # doses &nbsp;
				<input type="text" name="ProphylacticIntrapartumDoses" size="10" maxlength="10" value="<%= props.getProperty("ProphylacticIntrapartumDoses", "") %>" @oscar.formDB /><br>
				<input type="checkbox" name="ProphylacticIntraoperative" <%= props.getProperty("ProphylacticIntraoperative", "") %>  @oscar.formDB dbType="tinyint(1)" />Intraoperative &nbsp;<br>
				<input type="checkbox" name="ProphylacticOther" <%= props.getProperty("ProphylacticOther", "") %>  @oscar.formDB dbType="tinyint(1)" />Other &nbsp;				
				<input type="text" name="ProphylacticOtherText" size="10" maxlength="10" value="<%= props.getProperty("ProphylacticOtherText", "") %>" @oscar.formDB />
				</td>
				
				</tr>
							</table>
			</table>

			</td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="45%" valign="top">

			<table class="small8" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="4"><B>
					<div class="small10">4. DELIVERY</div>
					</B></td>
				</tr>
				<tr>
						<td colspan="4" nowrap>
					Fetal Position at Onset of Labour (specify)
					<input type="text" name="delFetalPosLab" size="30" maxlength="40" value="<%= props.getProperty("delFetalPosLab", "") %>" @oscar.formDB /> <br>
					Fetal Position at Delivery &nbsp;&nbsp; 
					<input type="checkbox" name="delOA" <%= props.getProperty("delOA", "") %>  @oscar.formDB dbType="tinyint(1)" />OA  &nbsp;
					<input type="checkbox" name="delOP" <%= props.getProperty("delOP", "") %>  @oscar.formDB dbType="tinyint(1)" />OP  &nbsp;
					<input type="checkbox" name="delOther" <%= props.getProperty("delOther", "") %>  @oscar.formDB dbType="tinyint(1)" />Other  &nbsp;					
					<input type="text" name="delOtherText" size="20" maxlength="20" value="<%= props.getProperty("delOtherText", "") %>" @oscar.formDB />
					<br>
					<input type="checkbox" name="delSvd" <%= props.getProperty("delSvd", "") %>  @oscar.formDB dbType="tinyint(1)" /> SVD &nbsp;</td>
				</tr>
				<tr>
						<td colspan="4" nowrap>
					VBAC Candidate &nbsp;&nbsp;
					<input type="checkbox" name="delVBACCandidateNo" <%= props.getProperty("delVBACCandidateNo", "") %>  @oscar.formDB dbType="tinyint(1)" />No&nbsp;
					<input type="checkbox" name="delVBACCandidateYes" <%= props.getProperty("delVBACCandidateYes", "") %>  @oscar.formDB dbType="tinyint(1)" />Yes&nbsp;&nbsp;
					Trail of Labour&nbsp;<input type="checkbox" name="delVBACCandidateElective" <%= props.getProperty("delVBACCandidateElective", "") %>  @oscar.formDB dbType="tinyint(1)" />&nbsp;Elective&nbsp;&nbsp;&nbsp;
					CS<input type="checkbox" name="delVBACCandidateCS" <%= props.getProperty("delVBACCandidateCS", "") %>  @oscar.formDB dbType="tinyint(1)" /><br> 
					</td>
				</tr>								
				
				<tr>
					<td width="25%" valign="top">
					<br>
					<input type="checkbox" name="delAssiDel" <%= props.getProperty("delAssiDel", "") %>  @oscar.formDB dbType="tinyint(1)" /> Assisted
					</td>
					<td width="25%" valign="top">
					<br>
					<input type="checkbox" name="delVacuum" <%= props.getProperty("delVacuum", "") %>  @oscar.formDB dbType="tinyint(1)" /> Vacuum
					</td>
					<td width="25%" valign="top">
					<br>
					&nbsp;&nbsp;Forceps<br> 
					<input type="checkbox" name="delOutlet" <%= props.getProperty("delOutlet", "") %>  @oscar.formDB dbType="tinyint(1)" /> Outlet <br>
					<input type="checkbox" name="delLow" <%= props.getProperty("delLow", "") %>  @oscar.formDB dbType="tinyint(1)" /> Low<br>
					<input type="checkbox" name="delMid" <%= props.getProperty("delMid", "") %>  @oscar.formDB dbType="tinyint(1)" /> Mid<br>
					<input type="checkbox" name="delRotation" <%= props.getProperty("delRotation", "") %>  @oscar.formDB dbType="tinyint(1)" /> Rotation<br>
					</td>
					<td width="25%" valign="top">
					<br>
					&nbsp;&nbsp;Application<br>
					<input type="checkbox" name="delEasy" <%= props.getProperty("delEasy", "") %>  @oscar.formDB dbType="tinyint(1)" /> Easy <br> 
					<input type="checkbox" name="delModDiff" <%= props.getProperty("delModDiff", "") %>  @oscar.formDB dbType="tinyint(1)" /> Mod. Difficult <br> 
					<input type="checkbox" name="delDiff" <%= props.getProperty("delDiff", "") %>  @oscar.formDB dbType="tinyint(1)" /> Difficult <br>
						
						</td>
				</tr>
				<tr>
					<td colspan="4">
						<input type="checkbox" name="delCesSect" <%= props.getProperty("delCesSect", "") %>  @oscar.formDB	dbType="tinyint(1)" /> Cesarean
						<input type="checkbox" name="delPrim" <%= props.getProperty("delPrim", "") %>  @oscar.formDB dbType="tinyint(1)" /> Primary
						<input type="checkbox" name="delRepeat" <%= props.getProperty("delRepeat", "") %>  @oscar.formDB dbType="tinyint(1)" /> Repeat:
						CS #<input type="text" name="delCesSectNo" size="6" maxlength="20" value="<%= props.getProperty("delCesSectNo", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<br>
						&nbsp;&nbsp;Primary Indication: <input type="text" name="delPrimaryIndication" size="30" maxlength="40" value="<%= props.getProperty("delPrimaryIndication", "") %>" @oscar.formDB /><br>
						&nbsp;&nbsp;<input type="checkbox" name="delElective" <%= props.getProperty("delElective", "") %>  @oscar.formDB dbType="tinyint(1)" /> Elective
						<input type="checkbox" name="delUrgent" <%= props.getProperty("delUrgent", "") %>  @oscar.formDB dbType="tinyint(1)" /> Urgent
						<input type="checkbox" name="delEmergent" <%= props.getProperty("delEmergent", "") %>  @oscar.formDB dbType="tinyint(1)" /> Emergent
					</td>
				</tr>
				
			<tr>
				<td nowrap>
					Decision at 
					<input type="text" name="delDecisionDate" id="delDecisionDate" size="10" maxlength="10" value="<%= props.getProperty("delDecisionDate", "") %>" @oscar.formDB /><img src="../images/cal.gif"	id="delDecisionDate_cal">
				</td>
				<td>
					<input type="text" name="delDecisionHrs" size="5" maxlength="5" value="<%= props.getProperty("delDecisionHrs", "") %>" @oscar.formDB />hrs				
				</td>						
					
				<td>
					<input type="text" name="delDecisionCM" size="5" maxlength="5" value="<%= props.getProperty("delDecisionCM", "") %>" @oscar.formDB />cm
				</td>
			</tr>			
			<tr>
				<td align="right" nowrap>
					dd/mm/yyy
				</td>
				<td align="center">
					time				
				</td>						
						
				<td>
					Cervix Dialated
				</td>
 			
			</tr>			
			
				<tr>
					<td nowrap>
 						Maternal Position at Delivery (specify):<br>
						<input type="text" name="delDecisionMaternalPosition" size="30" maxlength="30" value="<%= props.getProperty("delDecisionMaternalPosition", "") %>" @oscar.formDB />

					</td>
					
				</tr>			
			
			
			</table>

			</td>
			
			
			
			<td colspan="2" valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="3" valign="top">
							Oxytocin
							<input type="checkbox" name="delOxyNone" <%= props.getProperty("delOxyNone", "") %>  @oscar.formDB dbType="tinyint(1)" /> None
							<input type="checkbox" name="delOxyIM" <%= props.getProperty("delOxyIM", "") %>  @oscar.formDB dbType="tinyint(1)" /> IM							
							<input type="checkbox" name="delOxyIV" <%= props.getProperty("delOxyIV", "") %>  @oscar.formDB dbType="tinyint(1)" /> IV
							<input type="checkbox" name="delOxyInfusion" <%= props.getProperty("delOxyInfusion", "") %>  @oscar.formDB dbType="tinyint(1)" /> Infusion 
					</td>
				</tr>
				<tr>
					<td width="50%" colspan="2">
					<table class="small8" width="100%" border="0" cellspacing="0" cellpadding="0">
						
							
						<tr>
							<td><B>Placenta</B></td>						
						</tr>	
						<tr>

							<td align="left" nowrap>
								COMPLETE
								<input type="checkbox" name="delPlaCompY" <%= props.getProperty("delPlaCompY", "") %>  @oscar.formDB dbType="tinyint(1)" /> Yes
								<input type="checkbox" name="delPlaCompN" <%= props.getProperty("delPlaCompN", "") %>  @oscar.formDB dbType="tinyint(1)" /> No <br>
								<input type="checkbox" name="delPlaMaternalEffort" <%= props.getProperty("delPlaMaternalEffort", "") %>  @oscar.formDB dbType="tinyint(1)" /> Maternal Effort <br>
								<input type="checkbox" name="delPlaControlledTraction" <%= props.getProperty("delPlaControlledTraction", "") %>  @oscar.formDB dbType="tinyint(1)" /> Controlled Traction <br>
								<input type="checkbox" name="delPlaManual" <%= props.getProperty("delPlaManual", "") %>  @oscar.formDB dbType="tinyint(1)" /> Manual <br>
								<input type="checkbox" name="delPlaOperative" <%= props.getProperty("delPlaOperative", "") %>  @oscar.formDB dbType="tinyint(1)" /> Operative <br> 
							
							Sent to Pathology
								<input type="checkbox" name="delPlaPSPY" <%= props.getProperty("delPlaPSPY", "") %>  @oscar.formDB	dbType="tinyint(1)" /> YES
								<input type="checkbox" name="delPlaPSPN" <%= props.getProperty("delPlaPSPN", "") %>  @oscar.formDB dbType="tinyint(1)" /> NO
							</td>
						</tr>
					</table>

					</td>
					<td width="50%">
					<table class="small8" width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
							<B>Cord</B>
							</td>						
						</tr>
						<tr>
							
							<td width="50%" align="left" nowrap>
								Vessels
								<input type="checkbox" name="delCord2" <%= props.getProperty("delCord2", "") %>  @oscar.formDB dbType="tinyint(1)" /> 2
								<input type="checkbox" name="delCord3" <%= props.getProperty("delCord3", "") %>  @oscar.formDB dbType="tinyint(1)" /> 3 <br>
								Cord Gasses
								<input type="checkbox" name="delCordGassesYes" <%= props.getProperty("delCordGassesYes", "") %>  @oscar.formDB dbType="tinyint(1)" /> Yes <br>
								<input type="checkbox" name="delCordGassesNo" <%= props.getProperty("delCordGassesNo", "") %>  @oscar.formDB dbType="tinyint(1)" /> No <br>
								Cord Clamped								
								<input type="checkbox" name="delCordClampedEarly" <%= props.getProperty("delCordClampedEarly", "") %>  @oscar.formDB dbType="tinyint(1)" /> Early (<2min)<br>
								<input type="checkbox" name="delCordClampedLate" <%= props.getProperty("delCordClampedLate", "") %>  @oscar.formDB dbType="tinyint(1)" /> Late (>2min <br> 
								Abormalities/Complications:<br>
								<input type="text" name="delCordAbormalitiesComplications" size="20" maxlength="20" value="<%= props.getProperty("delCordAbormalitiesComplications", "") %>" @oscar.formDB />
						
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td width="40%" rowspan="2" valign="top">

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td colspan="2"><B>Perineum/Vagina/Cervix</B></td>
						</tr>
						<tr>
							<td colspan="2">
								<input type="checkbox" name="delPerIntact" <%= props.getProperty("delPerIntact", "") %>  @oscar.formDB dbType="tinyint(1)" /> Intact <br>
								<input type="checkbox" name="delPerLaceration" <%= props.getProperty("delPerLaceration", "") %>  @oscar.formDB dbType="tinyint(1)" /> Lacertation <br>
								&nbsp;&nbsp;&nbsp; 
								<input type="checkbox" name="delPer1" <%= props.getProperty("delPer1", "") %>  @oscar.formDB dbType="tinyint(1)" /> 1st 
								<input type="checkbox" name="delPer2" <%= props.getProperty("delPer2", "") %>  @oscar.formDB dbType="tinyint(1)" /> 2nd 
								<input type="checkbox" name="delPer3" <%= props.getProperty("delPer3", "") %>  @oscar.formDB dbType="tinyint(1)" /> 3rd 
								<input type="checkbox" name="delPer4" <%= props.getProperty("delPer4", "") %>  @oscar.formDB dbType="tinyint(1)" /> 4th 
								<br>
								<input type="checkbox" name="delPerEpisio" <%= props.getProperty("delPerEpisio", "") %>  @oscar.formDB dbType="tinyint(1)" /> Episiotomy
								<input type="checkbox" name="delPerMidline" <%= props.getProperty("delPerMidline", "") %>  @oscar.formDB dbType="tinyint(1)" /> Midline
								<input type="checkbox" name="delPerMedio" <%= props.getProperty("delPerMedio", "") %>  @oscar.formDB dbType="tinyint(1)" /> Mediolateral
<br>
								<input type="checkbox" name="delPerCerTear" <%= props.getProperty("delPerCerTear", "") %>  @oscar.formDB dbType="tinyint(1)" /> Cervical Tear <br>
								<input type="checkbox" name="delPerOthTrau" <%= props.getProperty("delPerOthTrau", "") %>  @oscar.formDB dbType="tinyint(1)" /> Other Trauma: 
								<input type="text" name="delPerOthTrauSpec" size="12" maxlength="60" value="<%= props.getProperty("delPerOthTrauSpec", "") %>" @oscar.formDB />								
								</td>
						</tr>
						<tr>
							<td colspan="3" align="right" nowrap>
								Initials
							</td>
						</tr>
						<tr>
							<td nowrap>		
							Sponge Count Correct
							<input type="checkbox" name="delEstSpoY1"	<%= props.getProperty("delEstSpoY1", "") %>  @oscar.formDB dbType="tinyint(1)" /> Yes							
							<input type="checkbox" name="delEstSpoN1" <%= props.getProperty("delEstSpoN1", "") %>  @oscar.formDB dbType="tinyint(1)" /> No
							<input type="text" name="delEstSpoInit1" size="10" maxlength="50" value="<%= props.getProperty("delEstSpoInit1", "") %>" @oscar.formDB />
							</td>
						</tr>
						
						
						<tr>
							<td nowrap>
								Needle Count Correct
								<input type="checkbox" name="delEstSpoY2" <%= props.getProperty("delEstSpoY2", "") %>  @oscar.formDB dbType="tinyint(1)" /> Yes
								<input type="checkbox" name="delEstSpoN2" <%= props.getProperty("delEstSpoN2", "") %>  @oscar.formDB dbType="tinyint(1)" /> No
								<input type="text" name="delEstSpoInit2" size="10" maxlength="50" value="<%= props.getProperty("delEstSpoInit2", "") %>" @oscar.formDB />
							</td>
						</tr>
					
						<tr>
							<td colspan="2">
							Repaired by: 
							<input type="text" name="delPerSutu" size="12" maxlength="60" value="<%= props.getProperty("delPerSutu", "") %>" @oscar.formDB />MD/RM
							</td>
						</tr>
					</table>

					</td>
					<td colspan="2" valign="top">

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td><B>Estimated Blood Loss</B></td>
						</tr>
						<tr>
							<td nowrap>
							<input type="checkbox" name="delEstBld1" <%= props.getProperty("delEstBld1", "") %>  @oscar.formDB dbType="tinyint(1)" /> &lt;500 cc &nbsp;&nbsp; 
							<input type="checkbox" name="delEstBld2" <%= props.getProperty("delEstBld2", "") %>  @oscar.formDB dbType="tinyint(1)" /> &lt;500-1000 cc &nbsp;&nbsp; 
							<input type="checkbox" name="delEstBld3" <%= props.getProperty("delEstBld3", "") %>  @oscar.formDB dbType="tinyint(1)" /> &gt;1000 cc <BR>
							Intervention Required
							<input type="checkbox" name="delEstBldY" <%= props.getProperty("delEstBldY", "") %>  @oscar.formDB dbType="tinyint(1)" /> Yes
							<input type="checkbox" name="delEstBldN" <%= props.getProperty("delEstBldN", "") %>  @oscar.formDB	dbType="tinyint(1)" /> No 
							<br>
							If yes:<br>
							<input type="checkbox" name="delEstBldMedication" <%= props.getProperty("delEstBldMedication", "") %>  @oscar.formDB dbType="tinyint(1)" /> Medication <br>
							<input type="checkbox" name="delEstBldBloodProducts" <%= props.getProperty("delEstBldBloodProducts", "") %>  @oscar.formDB dbType="tinyint(1)" /> Blood Products <br>
							<input type="checkbox" name="delEstBldOther" <%= props.getProperty("delEstBldOther", "") %>  @oscar.formDB dbType="tinyint(1)" /> Other (specify): 
							<input type="text" name="delEstBldOtherSpec" size="15" maxlength="30" value="<%= props.getProperty("delEstBldOtherSpec", "") %>" @oscar.formDB />
							</td>
						</tr>
					</table>


					</td>
				</tr>
			
					

					

			
			</table>

			</td>
		</tr>
	</table>



	<table width="100%" border="1" cellspacing="0" cellpadding="2">
		<tr>
			<td width="50%" valign="top">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2"><B>
					<div class="small10">5. Time Summary</div>
					</B></td>
				</tr>
				<tr>
					<td width="60%" valign="top">

					<table class="small8" width="100%" border="0" cellspacing="0" cellpadding="2">
						
						<tr>
							<td width="20%" valign="top"></td>
							<th width="30%" valign="top">HOURS:MINS</th>
							<th>DAY/MONTH/YEAR</th>
						</tr>
						<tr>
							<td align="right">MEMBRANES RUPTURED</td>
							<td><input type="text" name="birTimeHour1"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour1", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate1" id="birTimeDate1"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate1", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate1_cal"></td>
						</tr>
						<tr>
							<td align="right">1st STAGE</td>
							<td><input type="text" name="birTimeHour2"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour2", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate2" id="birTimeDate2"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate2", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate2_cal"></td>
						</tr>
						<tr>
							<td align="right">2nd STAGE</td>
							<td><input type="text" name="birTimeHour3"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour3", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate3" id="birTimeDate3"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate3", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate3_cal"></td>
						</tr>
						<tr>
							<td align="right">Time of Birth</td>
							<td><input type="text" name="birTimeHour4"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour4", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate4" id="birTimeDate4"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate4", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate4_cal"></td>
						</tr>
						<tr>
							<td align="right">Placenta Delivered</td>
							<td><input type="text" name="birTimeHour5"
								style="width: 100%" size="5" maxlength="5"
								value="<%= props.getProperty("birTimeHour5", "") %>"
								@oscar.formDB dbType="time" /></td>
							<td><input type="text" name="birTimeDate5" id="birTimeDate5"
								size="10" maxlength="10"
								value="<%= props.getProperty("birTimeDate5", "") %>"
								@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
								id="birTimeDate5_cal"></td>
						</tr>
					</table>

					</td>
					<td align="center">

					<table class="small8" width="70%" border="0" cellspacing="0"
						cellpadding="2">
						<tr>
							<th colspan="3">DURATION</th>
						</tr>
						<tr>
							<td width="33%"></td>
							<th>HOURS</th>
							<th>MINS</th>
						</tr>
						<tr>
							<td align="right">1st STAGE</td>
							<td><input type="text" name="birDurHour1"
								onDblClick="calc1();" class="spe" size="2" maxlength="2"
								value="<%= props.getProperty("birDurHour1", "") %>"
								@oscar.formDB /></td>
							<td><input type="text" name="birDurMin1" size="2"
								maxlength="2" value="<%= props.getProperty("birDurMin1", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td align="right">2nd STAGE</td>
							<td><input type="text" name="birDurHour2"
								onDblClick="calc2();" class="spe" size="2" maxlength="2"
								value="<%= props.getProperty("birDurHour2", "") %>"
								@oscar.formDB /></td>
							<td><input type="text" name="birDurMin2" size="2"
								maxlength="2" value="<%= props.getProperty("birDurMin2", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td align="right">3rd STAGE</td>
							<td><input type="text" name="birDurHour3"
								onDblClick="calc3();" class="spe" size="2" maxlength="2"
								value="<%= props.getProperty("birDurHour3", "") %>"
								@oscar.formDB /></td>
							<td><input type="text" name="birDurMin3" size="2"
								maxlength="2" value="<%= props.getProperty("birDurMin3", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td align="right" colspan="2" rowspan="2">DURATION OF
							RUPTURED MEMBRANES</td>
							<td>HOURS</td>
						</tr>
						<tr>
							<td><input type="text" name="birDurRupHour"
								onDblClick="calcRup();" class="spe" style="width: 100%" size="5"
								maxlength="5"
								value="<%= props.getProperty("birDurRupHour", "") %>"
								@oscar.formDB /> <input type="hidden" name="birDurRupMin" /></td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td colspan="2">

					<table class="small8" width="100%" border="1" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="20%" rowspan="2" nowrap><input type="checkbox"
								name="birMale"
								<%= props.getProperty("birMale", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Male<BR>
							<input type="checkbox" name="birFemale"
								<%= props.getProperty("birFemale", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Female<BR>
							<input type="checkbox" name="birAmbiguous"
								<%= props.getProperty("birAmbiguous", "") %>  @oscar.formDB
								dbType="tinyint(1)" /> Undifferentiated</td>
							<td width="33%" colspan="3">APGAR</td>
							<td width="10%" rowspan="2" nowrap>WEIGHT (g)<BR>
							<input type="text" name="birWeight" style="width: 100%" size="5"
								maxlength="6" value="<%= props.getProperty("birWeight", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td width="10%">at 1 min.<br>
							<input type="text" name="birApgar1" size="6" maxlength="10"
								value="<%= props.getProperty("birApgar1", "") %>" @oscar.formDB />
							</td>
							<td width="10%">at 5 min.<br>
							<input type="text" name="birApgar5" size="6" maxlength="10"
								value="<%= props.getProperty("birApgar5", "") %>" @oscar.formDB />
							</td>
							<td width="20%">at 10 min.<br>
							<input type="text" name="birApgar10" size="10" maxlength="20"
								value="<%= props.getProperty("birApgar10", "") %>" @oscar.formDB />
							</td>
						</tr>
					</table>


					</td>
				</tr>
			</table>



			</td>
			<td valign="top">

			<table class="small8" width="100%" border="0" cellspacing="0"
				cellpadding="2">
				<tr>
					<td width="10%">DELIVERED<br>
					BY:</td>
					<td><input type="text" name="birDelBy" style="width: 100%"
						size="60" maxlength="80"
						value="<%= props.getProperty("birDelBy", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="checkbox" name="birDelMD"
						<%= props.getProperty("birDelMD", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> MD &nbsp;&nbsp; <input type="checkbox"
						name="birDelRM"
						<%= props.getProperty("birDelRM", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> RM &nbsp;&nbsp; <input type="checkbox"
						name="birDelRN"
						<%= props.getProperty("birDelRN", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> RN &nbsp;&nbsp; <input type="checkbox"
						name="birDelOther"
						<%= props.getProperty("birDelOther", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OTHER <I>(specify):</I> <input type="text"
						name="birDelOtherSpec" size="6" maxlength="15"
						value="<%= props.getProperty("birDelOtherSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td>MD/RMS<br>
					PRESENT:</td>
					<td><textarea name="birMDPres" style="width: 100%" cols="30"
						rows="3" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("birMDPres", "") %></textarea>
					</td>
				</tr>
				<tr>
					<td>NURSES<br>
					PRESENT:</td>
					<td><textarea name="birNurPres" style="width: 100%" cols="30"
						rows="3" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("birNurPres", "") %></textarea>
					</td>
				</tr>
				<tr>
					<td>OTHERS<br>
					PRESENT:</td>
					<td><textarea name="birOtherPres" style="width: 100%"
						cols="30" rows="3" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("birOtherPres", "") %></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2" nowrap>
							Stillbirth:
							<input type="checkbox" name="delStillbirthAntepartum" <%= props.getProperty("delStillbirthAntepartum", "") %>  @oscar.formDB dbType="tinyint(1)" /> Antepartum 
							<input type="checkbox" name="delStillbirthIntrapartum" <%= props.getProperty("delStillbirthIntrapartum", "") %>  @oscar.formDB dbType="tinyint(1)" /> Intrapartum
							 
					</td>
				</tr>
			</table>

			</td>
		</tr>
	</table>


	<table class="small8" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td width="60%"><B>Comments on Labour Birth:</B> <input
				type="checkbox" name="comLabNormal"
				<%= props.getProperty("comLabNormal", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> Normal &nbsp;&nbsp; If not, specify: <input
				type="text" name="comLabNormalSpec" size="10" maxlength="20"
				value="<%= props.getProperty("comLabNormalSpec", "") %>"
				@oscar.formDB /></td>
			<td>Place of Birth: <input type="checkbox" name="comLabPlaHosp"
				<%= props.getProperty("comLabPlaHosp", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> Hospital &nbsp;&nbsp; <input type="checkbox"
				name="comLabPlaHome"
				<%= props.getProperty("comLabPlaHome", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> Home &nbsp;&nbsp; <input type="checkbox"
				name="comLabPlaTran"
				<%= props.getProperty("comLabPlaTran", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> Other:
				<input type="text" name="comLabPlaTranText" size="10" maxlength="20" value="<%= props.getProperty("comLabPlaTranText", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td colspan="2"><textarea name="comLabBirth" style="width: 100%" cols="100" rows="2" @oscar.formDB dbType="text"> <%= props.getProperty("comLabBirth", "") %> </textarea>
			</td>
		</tr>
	</table>

	<hr>
	<table class="small8" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
		
		
		
			<td colspan="2" nowrap><B>CONSULT TO:</B> <input type="text"
				name="consultTo" size="30" maxlength="120"
				value="<%= props.getProperty("consultTo", "") %>" @oscar.formDB />
			&nbsp; | ADMISSION <input type="text" name="admisDateTime"
				id="admisDateTime" size="12" maxlength="16"
				value="<%= props.getProperty("admisDateTime", "") %>" @oscar.formDB />
			<img src="../images/cal.gif" id="admisDateTime_cal">
			<td width="20%">SIGNATURE</td>
			<td width="20%">SIGNATURE</td>
		</tr>
		<tr>
			<td width="15%"><input type="checkbox" name="conObste"
				<%= props.getProperty("conObste", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> OBSTETRICIAN</td>
			<td><input type="checkbox" name="conPedia"
				<%= props.getProperty("conPedia", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> PEDIATRICIAN | DISCHARGE <input
				type="text" name="dischargeDateTime" id="dischargeDateTime"
				size="12" maxlength="16"
				value="<%= props.getProperty("dischargeDateTime", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="dischargeDateTime_cal"> 
				| 
				PP LOS <input type="text" name="ppLos" onDblClick="calcPPLOS();" class="spe" size="3" maxlength="3" value="<%= props.getProperty("ppLos", "") %>" @oscar.formDB /> hr
				 </td>
			<td><input type="text" name="conRmSignat" style="width: 100%"
				size="30" maxlength="60"
				value="<%= props.getProperty("conRmSignat", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="conMdSignat" style="width: 100%"
				size="30" maxlength="60"
				value="<%= props.getProperty("conMdSignat", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="conFamPhy"
				<%= props.getProperty("conFamPhy", "") %>  @oscar.formDB
				dbType="tinyint(1)" /> FAMILY PHYSICIAN</td>
			<td nowrap>
			<table class="small8" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td nowrap><input type="checkbox" name="conOther"
						<%= props.getProperty("conOther", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> OTHER: <input type="text"
						name="conOtherTxt" size="20" maxlength="30"
						value="<%= props.getProperty("conOtherTxt", "") %>" @oscar.formDB />
					</td>
					<td align="right" nowrap>L.O.S. <input type="text"
						name="conLos" onDblClick="calcLOS();" class="spe" size="3"
						maxlength="3" value="<%= props.getProperty("conLos", "") %>"
						@oscar.formDB /> hr</td>
				</tr>
			</table>

			</td>
			<td align="right">RM/RN</td>
			<td align="right">MD/RM</td>
		</tr>
	</table>

	<table class="small8" width="100%" border="1" cellspacing="0"
		cellpadding="0">
		<tr>
			<td>HLTH 1588 Rev. 2008/03 Prepared by the B.C Reproductive Care
			Program <br>
			* Date format: <span class="small8">(dd/mm/yyyy)</span></td>
		</tr>
	</table>

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
				value="Print" onclick="javascript:return onPrint();return false()" />
			</td>
		</tr>
	</table>

</html:form>
<br>
<br>
<br>
<br>
<br>
<br>
<script type="text/javascript">
Calendar.setup({ inputField : "birTimeDate1", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate1_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "birTimeDate2", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate2_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "birTimeDate3", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate3_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "birTimeDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate4_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "birTimeDate5", ifFormat : "%d/%m/%Y", showsTime :false, button : "birTimeDate5_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_formDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_formDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_EDD", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_EDD_cal", singleClick : true, step : 1 });


Calendar.setup({ inputField : "delDecisionDate", ifFormat : "%d/%m/%Y", showsTime :true, button : "delDecisionDate_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "admisDateTime", ifFormat : "%d/%m/%Y %H:%M", showsTime :true, button : "admisDateTime_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "dischargeDateTime", ifFormat : "%d/%m/%Y %H:%M", showsTime :true, button : "dischargeDateTime_cal", singleClick : true, step : 1 });
</script>
</body>
</html:html>
