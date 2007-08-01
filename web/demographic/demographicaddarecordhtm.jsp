<%
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String str = null;
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="java.util.*, java.sql.*, oscar.*, oscar.oscarDemographic.data.ProvinceNames, oscar.oscarWaitingList.WaitingList" errorPage="errorpage.jsp" %>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="addDemoBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<%
  String [][] dbQueries=new String[][] {
    {"search_provider", "select * from provider where provider_type='doctor' and status='1' order by last_name"},
    {"search_rsstatus", "select distinct roster_status from demographic where roster_status != '' and roster_status != 'RO' and roster_status != 'NR' and roster_status != 'TE' and roster_status != 'FS' "},
    {"search_ptstatus", "select distinct patient_status from demographic where patient_status != '' and patient_status != 'AC' and patient_status != 'IN' and patient_status != 'DE' and patient_status != 'MO' and patient_status != 'FI'"},
    {"search_waiting_list", "select * from waitingListName where group_no='" + session.getAttribute("groupno") +"' and is_history='N'  order by name"}
  };
  String[][] responseTargets=new String[][] {  };
  addDemoBean.doConfigure(dbParams,dbQueries,responseTargets);

  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);

  OscarProperties props = OscarProperties.getInstance();
  props.loader("oscar_mcmaster.properties");

  GregorianCalendar now=new GregorianCalendar();
  String curYear = Integer.toString(now.get(Calendar.YEAR));
  String curMonth = Integer.toString(now.get(Calendar.MONTH)+1);
  if (curMonth.length() < 2) curMonth = "0"+curMonth;
  String curDay = Integer.toString(now.get(Calendar.DAY_OF_MONTH));
  if (curDay.length() < 2) curDay = "0"+curDay;

  int nStrShowLen = 20;
  OscarProperties oscarProps = OscarProperties.getInstance();

  ProvinceNames pNames = ProvinceNames.getInstance();
  String prov= ((String ) props.getProperty("billregion","")).trim().toUpperCase();

  String billingCentre = ((String ) props.getProperty("billcenter","")).trim().toUpperCase();
  String defaultCity = prov.equals("ON")&&billingCentre.equals("N") ? "Toronto":"";
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
<head>
<title> <bean:message key="demographic.demographicaddrecordhtm.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
  <!-- calendar stylesheet -->
  <link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

  <!-- main calendar program -->
  <script type="text/javascript" src="../share/calendar/calendar.js"></script>

  <!-- language for the calendar -->
  <script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

  <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
  <script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<link rel="stylesheet" href="../web.css" />
<style type="text/css">
    BODY{
        FONT-SIZE: Normal;
        FONT-FAMILY: Verdana, Tahoma, Arial, sans-serif;
    }
    TABLE{
        font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
    }
    TD{
        font-size:11pt;
    }

    TH{
        font-size:11pt;
    }
    .subject {
        background-color: #003399;
        color: #FFFFFF;
        font-size: 14pt;
        font-weight: bold;
        text-align: centre;
    }
    .title {
        background-color: #6699CC;
        color: #000000;
        font-weight: bold;
        text-align: center;
        height="20px"
    }

</style>
<script language="JavaScript">

function setfocus() {
  this.focus();
  document.adddemographic.last_name.focus();
  document.adddemographic.last_name.select();
  window.resizeTo(1000,700);
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
//function showDate(){
//  var now=new Date();
//  var year=now.getYear();
//  var month=now.getMonth()+1;
//  var date=now.getDate();
//  //var DateVal=""+year+"-"+month+"-"+date;
//  document.adddemographic.date_joined_year.value=year;
//  document.adddemographic.date_joined_month.value=month;
//  document.adddemographic.date_joined_date.value=date;
//}
function checkTypeNum(typeIn) {
	var typeInOK = true;
	var i = 0;
	var length = typeIn.length;
	var ch;
	// walk through a string and find a number
	if (length>=1) {
	  while (i <  length) {
		ch = typeIn.substring(i, i+1);
		if ((ch < "0") || (ch > "9")) {
			typeInOK = false;
			break;
		}
	    i++;
      }
	} else typeInOK = false;
	return typeInOK;
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

function checkTypeInAdd() {
	var typeInOK = false;
	if(document.adddemographic.last_name.value!="" && document.adddemographic.first_name.value!="" && document.adddemographic.sex.value!="") {
      if(checkTypeNum(document.adddemographic.year_of_birth.value) && checkTypeNum(document.adddemographic.month_of_birth.value) && checkTypeNum(document.adddemographic.date_of_birth.value) ){
	    typeInOK = true;
	  }
	}
	if(!typeInOK) alert ("<bean:message key="demographic.demographicaddrecordhtm.msgMissingFields"/>");
	return typeInOK;
}

function newStatus() {
    newOpt = prompt("Please enter the new status:", "");
    if (newOpt != "") {
        document.adddemographic.patient_status.options[document.adddemographic.patient_status.length] = new Option(newOpt, newOpt);
        document.adddemographic.patient_status.options[document.adddemographic.patient_status.length-1].selected = true;
    } else {
        alert("Invalid entry");
    }
}
function newStatus1() {
    newOpt = prompt("Please enter the new status:", "");
    if (newOpt != "") {
        document.adddemographic.roster_status.options[document.adddemographic.roster_status.length] = new Option(newOpt, newOpt);
        document.adddemographic.roster_status.options[document.adddemographic.roster_status.length-1].selected = true;
    } else {
        alert("Invalid entry");
    }
}

function formatPhoneNum() {
    if (document.adddemographic.phone.value.length == 10) {
        document.adddemographic.phone.value = document.adddemographic.phone.value.substring(0,3) + "-" + document.adddemographic.phone.value.substring(3,6) + "-" + document.adddemographic.phone.value.substring(6);
        }
    if (document.adddemographic.phone.value.length == 11 && document.adddemographic.phone.value.charAt(3) == '-') {
        document.adddemographic.phone.value = document.adddemographic.phone.value.substring(0,3) + "-" + document.adddemographic.phone.value.substring(4,7) + "-" + document.adddemographic.phone.value.substring(7);
    }

    if (document.adddemographic.phone2.value.length == 10) {
        document.adddemographic.phone2.value = document.adddemographic.phone2.value.substring(0,3) + "-" + document.adddemographic.phone2.value.substring(3,6) + "-" + document.adddemographic.phone2.value.substring(6);
        }
    if (document.adddemographic.phone2.value.length == 11 && document.adddemographic.phone2.value.charAt(3) == '-') {
        document.adddemographic.phone2.value = document.adddemographic.phone2.value.substring(0,3) + "-" + document.adddemographic.phone2.value.substring(4,7) + "-" + document.adddemographic.phone2.value.substring(7);
    }
}
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

function checkName() {
	var typeInOK = false;
	if(document.adddemographic.last_name.value!="" && document.adddemographic.first_name.value!="" && document.adddemographic.last_name.value!=" " && document.adddemographic.first_name.value!=" ") {
	    typeInOK = true;
	} else {
		alert ("You must type in the following fields: Last Name, First Name.");
    }
	return typeInOK;
}

function checkDob() {
	var typeInOK = false;
	var yyyy = document.adddemographic.year_of_birth.value;
	var selectBox = document.adddemographic.month_of_birth;
	var mm = selectBox.options[selectBox.selectedIndex].value
	selectBox = document.adddemographic.date_of_birth;
	var dd = selectBox.options[selectBox.selectedIndex].value

	if(checkTypeNum(yyyy) && checkTypeNum(mm) && checkTypeNum(dd) ){
        //alert(yyyy); alert(mm); alert(dd);
        var check_date = new Date(yyyy,(mm-1),dd);
        //alert(check_date);
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
		    //alert("failed in here 1");
		}
		if ( yyyy == "0000"){ 
        typeInOK = false;
      }
	}

	if (!typeInOK){
      alert ("You must type in the right DOB.");
   }
   
   if (!isValidDate(dd,mm,yyyy)){
      alert ("DOB Date is an incorrect date");
      typeInOK = false;
   }
   
	return typeInOK;
}


function isValidDate(day,month,year){
   month = ( month - 1 );
   dteDate=new Date(year,month,day);
//alert(dteDate);
   return ((day==dteDate.getDate()) && (month==dteDate.getMonth()) && (year==dteDate.getFullYear()));
}

function checkHin() {
	var typeInOK = false;
	var hin = document.adddemographic.hin.value;
	var province = document.adddemographic.hc_type.value;
	//alert(province);
	//check OHIP, no others
	if(province=="ON") {
		//alert(hin.length + " | " + hin.charAt(1));
		if(checkTypeNum(hin) && hin.length==10) {
		    typeInOK = mod10Check(hin);
		}
	}

	//don't check
	if(province!="ON" || hin.length==0) typeInOK = true;

	if (!typeInOK) alert ("You must type in the right HIN.");
	return typeInOK;
}
	function mod10Check(hinNum) {
		var typeInOK = false;
		var hChar = new Array();
		var sum = 0;
		for (i=0; i<hinNum.length; i++) {
			hChar[i] = hinNum.charAt(i);
		}

		for (i=0; i<hinNum.length; i=i+2) {
			hChar[i] = mod10CheckCalDig(hChar[i]);
		}

		for (i=0; i<hinNum.length-1; i++) {
			sum = eval(sum*1 + hChar[i]*1);
		}

		var calDigit = 10-(""+sum).charAt((""+sum).length-1) ;
		if (hChar[hinNum.length-1] == ( (""+calDigit).charAt((""+calDigit).length-1) )) typeInOK = true;

		return typeInOK;
	}
	function mod10CheckCalDig(dig) {
		var ret = dig*2 + "";
		if (ret.length==2) ret = eval(ret.charAt(0)*1+ret.charAt(1)*1);

		return ret;
	}

function checkAllDate() {
	var typeInOK = false;
	typeInOK = checkDateYMD( document.adddemographic.date_joined_year.value , document.adddemographic.date_joined_month.value , document.adddemographic.date_joined_date.value , "Date Joined" );
	if (!typeInOK) { return false; }

	typeInOK = checkDateYMD( document.adddemographic.end_date_year.value , document.adddemographic.end_date_month.value , document.adddemographic.end_date_date.value , "End Date" );
	if (!typeInOK) { return false; }

	typeInOK = checkDateYMD( document.adddemographic.hc_renew_date_year.value , document.adddemographic.hc_renew_date_month.value , document.adddemographic.hc_renew_date_date.value , "PCN Date" );
	if (!typeInOK) { return false; }

	typeInOK = checkDateYMD( document.adddemographic.eff_date_year.value , document.adddemographic.eff_date_month.value , document.adddemographic.eff_date_date.value , "EFF Date" );
	if (!typeInOK) { return false; }

	return typeInOK;
}
	function checkDateYMD(yy, mm, dd, fieldName) {
		var typeInOK = false;
		if((yy.length==0) && (mm.length==0) && (dd.length==0) ){
			typeInOK = true;
		} else if(checkTypeNum(yy) && checkTypeNum(mm) && checkTypeNum(dd) ){
			if (checkDateYear(yy) && checkDateMonth(mm) && checkDateDate(dd)) {
				typeInOK = true;
			}
		}
		if (!typeInOK) { alert ("You must type in the right '" + fieldName + "'."); return false; }
		return typeInOK;
	}

	function checkDateYear(y) {
		if (y>1900 && y<2045) return true;
		return false;
	}
	function checkDateMonth(y) {
		if (y>=1 && y<=12) return true;
		return false;
	}
	function checkDateDate(y) {
		if (y>=1 && y<=31) return true;
		return false;
	}

function checkFormTypeIn() {
	if ( !checkName() ) return false;
	if ( !checkDob() ) return false;
	if ( !checkHin() ) return false;
	if ( !checkAllDate() ) return false;
	return true;
}

</script>
</head>
<!-- Databases have alias for today. It is not necessary give the current date -->
<!--<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus();showDate();"  topmargin="0" leftmargin="0" rightmargin="0">-->
<body bgproperties="fixed" onLoad="setfocus();"  topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="#CCCCFF">
    <th class="subject"><bean:message key="demographic.demographicaddrecordhtm.msgMainLabel"/></th>
  </tr>
</table>

<%@ include file="zdemographicfulltitlesearch.jsp" %>
<table width="100%" bgcolor="#CCCCFF">
<tr><td class="RowTop">
    <b><bean:message key="demographic.record"/></b>
</td></tr>
<tr><td>
<table border="0" cellpadding="1" cellspacing="0" width="100%" bgcolor="#EEEEFF">
  <form method="post" name="adddemographic" action="demographiccontrol.jsp"  onsubmit="return checkFormTypeIn()">
    <tr>
      <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formLastName"/><font color="red">:</font> </b></td>
      <td align="left">
        <input type="text" name="last_name" onBlur="upCaseCtrl(this)" size=30 />
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formFirstName"/><font color="red">:</font> </b> </td>
      <td align="left">
        <input type="text" name="first_name" onBlur="upCaseCtrl(this)"  size=30>
      </td>
    </tr>
    <%
    if (vLocale.getCountry().equals("BR")) { %>
    <tr>
      <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formRG"/>:</b></td>
      <td align="left">
        <input type="text" name="rg" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formCPF"/>:</b> </td>
      <td align="left">
        <input type="text" name="cpf" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <tr>
      <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formMaritalState"/>:</b></td>
      <td align="left">
        <select name="marital_state">
            <option value="-">-</option>
        	<option value="S"><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optSingle"/></option>
        	<option value="M"><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optMarried"/></option>
        	<option value="R"><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optSeparated"/></option>
        	<option value="D"><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optDivorced"/></option>
        	<option value="W"><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optWidower"/></option>
        </select>
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formBirthCertificate"/>:</b> </td>
      <td align="left">
        <input type="text" name="birth_certificate" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <tr>
      <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formMarriageCertificate"/>:</b></td>
      <td align="left">
        <input type="text" name="marriage_certificate" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPartnerName"/>:</b> </td>
      <td align="left">
        <input type="text" name="partner_name" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <tr>
      <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formFatherName"/>:</b></td>
      <td align="left">
        <input type="text" name="father_name" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formMotherName"/>:</b> </td>
      <td align="left">
        <input type="text" name="mother_name" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <%}%>
    <tr valign="top">
      <td  align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formAddress"/>: </b></td>
      <td align="left" >
        <input type="text" name="address" size=40 /><% if (vLocale.getCountry().equals("BR")) { %>
        <b><bean:message key="demographic.demographicaddrecordhtm.formAddressNo"/>:</b>
        <input type="text" name="address_no" size="6">
        <%}%>
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formCity"/>: </b></td>
      <td align="left">
        <input type="text" name="city" value="<%=defaultCity %>" />
      </td>
    </tr>
    <% if (vLocale.getCountry().equals("BR")) { %>
    <tr valign="top">
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formComplementaryAddress"/>: </b> </td>
      <td  align="left">
        <input type="text" name="complementary_address" onBlur="upCaseCtrl(this)">
      </td>
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formDistrict"/>: </b> </td>
      <td  align="left">
        <input type="text" name="district" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <%}%>
    <tr valign="top">
      <td align="right"><b>
	  <% if(oscarProps.getProperty("demographicLabelProvince") == null) { %>                             
      <bean:message key="demographic.demographicaddrecordhtm.formprovince"/>
      <% } else { 
          out.print(oscarProps.getProperty("demographicLabelProvince"));
      	 } %>
      : </b> 
      </td>
      <td  align="left">
        <% if (vLocale.getCountry().equals("BR")) { %>
        <input type="text" name="province" value="<%=props.getProperty("billregion", "ON")%>">
        <% } else { %>
            <select name="province">
            <% String billregion = props.getProperty("billregion", ""); %>
              <option value="OT"<%=billregion.equals("")||billregion.equals("OT")?" selected":""%>>Other</option>
              <%-- <option value="">None Selected</option> --%>
            <% if (pNames.isDefined()) {
                   for (ListIterator li = pNames.listIterator(); li.hasNext(); ) {
                       String province = (String) li.next(); %>
                       <option value="<%=province%>"<%=province.equals(billregion)?" selected":""%>><%=li.next()%></option>
                   <% } %>
            <% } else { %>
              <option value="AB"<%=billregion.equals("AB")?" selected":""%>>AB-Alberta</option>
              <option value="BC"<%=billregion.equals("BC")?" selected":""%>>BC-British Columbia</option>
              <option value="MB"<%=billregion.equals("MB")?" selected":""%>>MB-Manitoba</option>
              <option value="NB"<%=billregion.equals("NB")?" selected":""%>>NB-New Brunswick</option>
              <option value="NL"<%=billregion.equals("NL")?" selected":""%>>NL-Newfoundland & Labrador</option>
              <option value="NT"<%=billregion.equals("NT")?" selected":""%>>NT-Northwest Territory</option>
              <option value="NS"<%=billregion.equals("NS")?" selected":""%>>NS-Nova Scotia</option>
              <option value="NU"<%=billregion.equals("NU")?" selected":""%>>NU-Nunavut</option>
              <option value="ON"<%=billregion.equals("ON")?" selected":""%>>ON-Ontario</option>
              <option value="PE"<%=billregion.equals("PE")?" selected":""%>>PE-Prince Edward Island</option>
              <option value="QC"<%=billregion.equals("QC")?" selected":""%>>QC-Quebec</option>
              <option value="SK"<%=billregion.equals("SK")?" selected":""%>>SK-Saskatchewan</option>
              <option value="YT"<%=billregion.equals("YT")?" selected":""%>>YT-Yukon</option>
              <option value="US"<%=billregion.equals("US")?" selected":""%>>US resident</option>

            <% } %>
            </select>
        <% } %>
      </td>
      <td  align="right"><b>
	  <% if(oscarProps.getProperty("demographicLabelPostal") == null) { %>                             
      <bean:message key="demographic.demographicaddrecordhtm.formPostal"/>
      <% } else { 
          out.print(oscarProps.getProperty("demographicLabelPostal"));
      	 } %>
      : </b> </td>
      <td  align="left">
        <input type="text" name="postal" onBlur="upCaseCtrl(this)">
      </td>
    </tr>

    <tr valign="top">
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPhoneHome"/>: </b> </td>
      <td align="left" >
        <input type="text" name="phone" onBlur="formatPhoneNum()" value="<%=props.getProperty("phoneprefix", "905-")%>">
        Ext:<input type="text" name="hPhoneExt" value=""  size="4" />
      </td>
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPhoneWork"/>:</b> </td>      
      <td  align="left">
        <input type="text" name="phone2" onBlur="formatPhoneNum()" value="">
        Ext:<input type="text" name="wPhoneExt" value=""  style="display:inline" size="4" />
      </td>
    </tr>
     <tr valign="top">
      <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formPhoneC"/>: </b> </td>
      <td align="left" colspan="3" >
        <input type="text" name="cellphone" onBlur="formatPhoneNum()" >

      </td>
    </tr>
    <tr valign="top">
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formEMail"/>: </b> </td>
      <td align="left" >
        <input type="text" name="email" value="">
      </td>
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPIN"/>:</b> </td>
      <td  align="left">
        <input type="text" name="pin" value="">
      </td>
    </tr>
    <tr valign="top">
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formDOB"/></b><font size="-2">(yyyymmdd)</font><b><font color="red">:</font></b></td>
      <td  align="left" nowrap>
        <table border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td>
              <input type="text" name="year_of_birth" size="4" maxlength="4" value="yyyy"
              onFocus="if(this.value=='yyyy')this.value='';" onBlur="if(this.value=='')this.value='yyyy';">
            </td>
            <td>-</td>
            <td>
              <!--input type="text" name="month_of_birth" size="2" maxlength="2"-->
              <select name="month_of_birth">
                <option value="01">01
                <option value="02">02
                <option value="03">03
                <option value="04">04
                <option value="05">05
                <option selected value="06">06
                <option value="07">07
                <option value="08">08
                <option value="09">09
                <option value="10">10
                <option value="11">11
                <option value="12">12
              </select>
            </td>
            <td>-</td>
            <td>
              <!--input type="text" name="date_of_birth" size="2" maxlength="2"-->
              <select name="date_of_birth">
                <option value="01">01
                <option value="02">02
                <option value="03">03
                <option value="04">04
                <option value="05">05
                <option value="06">06
                <option value="07">07
                <option value="08">08
                <option value="09">09
                <option value="10">10
                <option value="11">11
                <option value="12">12
                <option value="13">13
                <option value="14">14
                <option selected value="15">15
                <option value="16">16
                <option value="17">17
                <option value="18">18
                <option value="19">19
                <option value="20">20
                <option value="21">21
                <option value="22">22
                <option value="23">23
                <option value="24">24
                <option value="25">25
                <option value="26">26
                <option value="27">27
                <option value="28">28
                <option value="29">29
                <option value="30">30
                <option value="31">31
              </select>
            </td>
            <td><b></b></td>
            <td>&nbsp; </td>
          </tr>
        </table>
      </td>
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formSex"/><font color="red">:</font></b></td>
      <td align="left">
        <select name="sex">
          <option value="F" selected><bean:message key="demographic.demographicaddrecordhtm.formF"/></option>
          <option value="M"><bean:message key="demographic.demographicaddrecordhtm.formM"/></option>
        </select>
      </td>
    </tr>
    <tr valign="top">
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formHIN"/>: </b></td>
      <td align="left" nowrap >
        <input type="text" name="hin" size="15">
        <b><bean:message key="demographic.demographicaddrecordhtm.formVer"/>:
        <input type="text" name="ver" value="" size="3"  onBlur="upCaseCtrl(this)">
        </b></td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formEFFDate"/></b><b>: </b></td>
      <td align="left"><b>
        <input type="text" name="eff_date_year" size="4" maxlength="4">
        <input type="text" name="eff_date_month" size="2" maxlength="2">
        <input type="text" name="eff_date_date" size="2" maxlength="2">
        </b></td>
    </tr>
    <tr>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formHCType"/>: </b></td>
      <td>
        <% if(vLocale.getCountry().equals("BR")) { %>
           <input type="text" name="hc_type" value="">
        <% } else {%>
        <% String billregion = props.getProperty("billregion", ""); %>
        <select name="hc_type">
        <option value="OT"<%=billregion.equals("")||billregion.equals("OT")?" selected":""%>>Other</option>
        <% if (pNames.isDefined()) {
                   for (ListIterator li = pNames.listIterator(); li.hasNext(); ) {
                       String province = (String) li.next(); %>
                       <option value="<%=province%>"<%=province.equals(billregion)?" selected":""%>><%=li.next()%></option>
                   <% } %>
            <% } else { %>
                  <option value="AB"<%=billregion.equals("AB")?" selected":""%>>AB-Alberta</option>
                  <option value="BC"<%=billregion.equals("BC")?" selected":""%>>BC-British Columbia</option>
                  <option value="MB"<%=billregion.equals("MB")?" selected":""%>>MB-Manitoba</option>
                  <option value="NB"<%=billregion.equals("NB")?" selected":""%>>NB-New Brunswick</option>
                  <option value="NL"<%=billregion.equals("NL")?" selected":""%>>NL-Newfoundland & Labrador</option>
                  <option value="NT"<%=billregion.equals("NT")?" selected":""%>>NT-Northwest Territory</option>
                  <option value="NS"<%=billregion.equals("NS")?" selected":""%>>NS-Nova Scotia</option>
                  <option value="NU"<%=billregion.equals("NU")?" selected":""%>>NU-Nunavut</option>
                  <option value="ON"<%=billregion.equals("ON")?" selected":""%>>ON-Ontario</option>
                  <option value="PE"<%=billregion.equals("PE")?" selected":""%>>PE-Prince Edward Island</option>
                  <option value="QC"<%=billregion.equals("QC")?" selected":""%>>QC-Quebec</option>
                  <option value="SK"<%=billregion.equals("SK")?" selected":""%>>SK-Saskatchewan</option>
                  <option value="YT"<%=billregion.equals("YT")?" selected":""%>>YT-Yukon</option>
                  <option value="US"<%=billregion.equals("US")?" selected":""%>>US resident</option>
          <% } %>
          </select>
        <% }%>
      </td>
      <td align="right">    
         <b>Language:</b>          
      </td>
      <td>
         <input type="text" name="language" value="" onBlur="upCaseCtrl(this)" size="19" />
      </td>
    </tr>
     </tr>
     <tr valign="top">
      <td  align="right"><b>  <bean:message key="demographic.demographiceditdemographic.cytolNum"/>:</b> </td>
      <td align="left" colspan="3" >
        <input type="text" name="cytolNum">

      </td>
    </tr>
    <tr valign="top">
      <td align="right"><b><% if(oscarProps.getProperty("demographicLabelDoctor") != null) { out.print(oscarProps.getProperty("demographicLabelDoctor","")); } else { %>
                                                <bean:message key="demographic.demographiceditdemographic.formDoctor"/><% } %>
                                                : </b></td>
      <td align="left" >
        <select name="staff">
          <%
  ResultSet rsdemo = addDemoBean.queryResults("search_provider");
  while (rsdemo.next()) {
%>
          <option value="<%=rsdemo.getString("provider_no")%>">
          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",12)%></option>
          <%
  }
%>
          <option value="" ></option>
        </select>
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formNurse"/>: </b></td>
      <td>
        <select name="cust1">
          <option value="" ></option>
          <%
  rsdemo.beforeFirst();
  while (rsdemo.next()) {
%>
          <option value="<%=rsdemo.getString("provider_no")%>">
          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",12)%></option>
          <%
  }
%>
        </select>
      </td>
    </tr>
    <tr valign="top">
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formMidwife"/>: </b></td>
      <td>
        <select name="cust4">
          <option value="" ></option>
          <%
  rsdemo.beforeFirst();
  while (rsdemo.next()) {
%>
          <option value="<%=rsdemo.getString("provider_no")%>">
          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",12)%></option>
          <%
  }
%>
        </select>
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formResident"/>: </b> </td>
      <td align="left">
        <select name="cust2">
          <option value="" ></option>
          <%
  rsdemo.beforeFirst();
  while (rsdemo.next()) {
%>
          <option value="<%=rsdemo.getString("provider_no")%>">
          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",12)%></option>
          <%
  }
%>
        </select>
      </td>
    </tr>
    <tr valign="top">
      <td align="right" height="10"><b><bean:message key="demographic.demographicaddrecordhtm.formReferalDoctor"/>:</b></td>
      <td align="left" height="10" >
                                  <% if(oscarProps.getProperty("isMRefDocSelectList", "").equals("true") ) {
                                  		// drop down list
									  String	sql   = "select * from billingreferral order by last_name, first_name" ;
									  oscar.oscarBilling.ca.on.data.BillingONDataHelp dbObj = new oscar.oscarBilling.ca.on.data.BillingONDataHelp();
									  ResultSet rs1 = dbObj.searchDBRecord(sql);
										// System.out.println(sql);
									  Properties prop = null;
									  Vector vecRef = new Vector();
									  while (rs1.next()) {
									  	prop = new Properties();
									  	prop.setProperty("referral_no",rs1.getString("referral_no"));
									  	prop.setProperty("last_name",rs1.getString("last_name"));
									  	prop.setProperty("first_name",rs1.getString("first_name"));
									  	vecRef.add(prop);
                                      }
                                  %>
                                	<select name="r_doctor" onChange="changeRefDoc()" style="width:200px">
                                  	<option value="" ></option>
                                  	<% for(int k=0; k<vecRef.size(); k++) {
                                  		prop= (Properties) vecRef.get(k);
                                  	%>
                          <option value="<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>"  >
                          <%=Misc.getShortStr( (prop.getProperty("last_name")+","+prop.getProperty("first_name")),"",nStrShowLen)%></option>
 	                      <% } %>         	</select>
<script language="Javascript">
<!--
function changeRefDoc() {
//alert(document.forms[1].r_doctor.value);
var refName = document.forms[1].r_doctor.options[document.forms[1].r_doctor.selectedIndex].value;
var refNo = "";
  	<% for(int k=0; k<vecRef.size(); k++) {
  		prop= (Properties) vecRef.get(k);
  	%>
if(refName.indexOf("<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>")>=0) {
  refNo = <%=prop.getProperty("referral_no", "")%>;
}
<% } %>
document.forms[1].r_doctor_ohip.value = refNo;
}
//-->
</script>
                                  <% } else {%>
                                    <input type="text" name="r_doctor" size="30" maxlength="40" value="">
                                  <% } %>
      </td>
      <td align="right" nowrap height="10"><b><bean:message key="demographic.demographicaddrecordhtm.formReferalDoctorN"/>:</b></td>
      <td align="left" height="10">
        <input type="text" name="r_doctor_ohip" maxlength="6">
                					<% if("ON".equals(prov)) { %>
									<a href="javascript:referralScriptAttach2('r_doctor_ohip','r_doctor')">Search #</a>
                					<% } %>
      </td>
    </tr>
    <tr valign="top">
      <td align="right" nowrap><b><bean:message key="demographic.demographicaddrecordhtm.formPCNRosterStatus"/>: </b></td>
      <td align="left" >
        <!--input type="text" name="roster_status" onBlur="upCaseCtrl(this)"-->
        <select name="roster_status" style="width:160">
          <option value="" > </option>
          <option value="RO">RO - rostered</option>
          <option value="NR">NR - not rostered</option>
          <option value="TE">TE - terminated</option>
          <option value="FS">FS - fee for service</option>
          <% ResultSet rsstatus1 = addDemoBean.queryResults("search_rsstatus");
             while (rsstatus1.next()) { %>
               <option value="<%=rsstatus1.getString("roster_status")%>"><%=rsstatus1.getString("roster_status")%></option>
          <% } // end while %>
		</select>
		<input type="button" onClick="newStatus1();" value="Add New" />
      </td>
      <td align="right" nowrap><b><bean:message key="demographic.demographicaddrecordhtm.formPCNDateJoined"/>: </b></td>
      <td align="left">
        <input type="text" name="hc_renew_date_year" size="4" maxlength="4">
        <input type="text" name="hc_renew_date_month" size="2" maxlength="2">
        <input type="text" name="hc_renew_date_date" size="2" maxlength="2">
      </td>
    </tr>
    <tr valign="top">
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPatientStatus"/>:</b></td>
      <td align="left" >
        <% if (vLocale.getCountry().equals("BR")) { %>
          <input type="text" name="patient_status" value="AC" onBlur="upCaseCtrl(this)">
        <% } else { %>
        <select name="patient_status" style="width:160">
          <option value="AC">AC - Active</option>
          <option value="IN">IN - Inactive</option>
          <option value="DE">DE - Deceased</option>
          <option value="MO">MO - Moved</option>
          <option value="FI">FI - Fired</option>
          <% ResultSet rsstatus = addDemoBean.queryResults("search_ptstatus");
             while (rsstatus.next()) { %>
               <option value="<%=rsstatus.getString("patient_status")%>" ><%=rsstatus.getString("patient_status")%></option>
          <% } // end while %>
        </select>
        <input type="button" onClick="newStatus();" value="Add New">
        <% } // end if...then...else %>
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formChartNo"/>:</b></td>
      <td align="left">
        <input type="text" name="chart_no" value="">
      </td>
    </tr>
    <%
    if (vLocale.getCountry().equals("BR")) { %>
    <tr valign="top">
      <td align="right"><b></b></td>
      <td align="left" >
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formChartAddress"/>:</b></td>
      <td align="left">
        <input type="text" name="chart_address" value="">
      </td>
    </tr>
    <%}%>
    <%
        String wLReadonly = ""; 
        WaitingList wL = WaitingList.getInstance();
        if(!wL.getFound()){ 
            wLReadonly = "readonly";
            }
    %>
     <tr> 
         <td colspan="4"> 
                 <table border="1" width="100%"> 
                             <tr valign="top"> 
                               <td align="right"  width="15%" nowrap><b>Add patient to<br/> waiting list: </b></td> 
                               <td align="left"  width="38%"> 
                                 <select name="list_id"> 
                                <% if(wLReadonly.equals("")){ %> 
                                        <option value="0" >--Select Waiting List--</option> 
                                <%}else{ %>   
                                        <option value="0" >--Please Create Waiting List Name first--</option> 
                                <%} %> 
                                   <% 
                                       ResultSet rsWL = addDemoBean.queryResults("search_waiting_list"); 
                                       while (rsWL.next()) { 
                                    %> 
                                               <option value="<%=rsWL.getString("ID")%>"> 
                                               <%=rsWL.getString("name")%></option> 
                                               <% 
                                       } 
                                     %> 
                                 </select> 
                               </td> 
                               <td align="right" nowrap><b>Waiting List Note: </b></td> 
                               <td align="left"> 
                                 <input type="text" name="waiting_list_note" size="36" <%=wLReadonly%> > 
                               </td> 
                             </tr> 
                              
                             <tr> 
                                  <td colspan="2" align="right">&nbsp;</td> 
                                      <td align="right" nowrap><b>*Date of request: </b></td> 
                                      <td align="left"> 
                                        <input type="text" name="waiting_list_referral_date"  id="waiting_list_referral_date"  value="" size="12" <%=wLReadonly%> > 
                                        <img src="../images/cal.gif" id="referral_date_cal">(yyyy-mm-dd) 
                                      </td> 
                             </tr> 
                         </table>                             
         </td> 
    </tr>

    <tr valign="top">
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formDateJoined"/></b><b>: </b></td>
      <td align="left" >
        <input type="text" name="date_joined_year" size="4" maxlength="4" value="<%=curYear%>">
        <input type="text" name="date_joined_month" size="2" maxlength="2" value="<%=curMonth%>">
        <input type="text" name="date_joined_date" size="2" maxlength="2" value="<%=curDay%>">
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formEndDate"/></b><b>: </b> </td>
      <td align="left">
        <input type="text" name="end_date_year" size="4" maxlength="4">
        <input type="text" name="end_date_month" size="2" maxlength="2">
        <input type="text" name="end_date_date" size="2" maxlength="2">
      </td>
    </tr>
<% if(oscarVariables.getProperty("demographicExt") != null) {    
    boolean bExtForm = oscarVariables.getProperty("demographicExtForm") != null ? true : false;
    String [] propDemoExtForm = bExtForm ? (oscarVariables.getProperty("demographicExtForm","").split("\\|") ) : null;
	String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
	for(int k=0; k<propDemoExt.length; k=k+2) {
%>	
    <tr valign="top" bgcolor="#CCCCFF">
      <td align="right"><b><%=propDemoExt[k] %></b><b>: </b></td>
      <td align="left" >
		<% if(bExtForm) { 
			out.println(propDemoExtForm[k] );
		 } else { %>
        <input type="text" name="<%=propDemoExt[k].replace(' ', '_') %>" value="">
        <% }  %>    
      </td>
      <td align="right"><%=(k+1)<propDemoExt.length?("<b>"+propDemoExt[k+1]+": </b>") : "&nbsp;" %> </td>
      <td align="left">
		<% if(bExtForm && (k+1)<propDemoExt.length) { 
			out.println(propDemoExtForm[k+1] );
		 } else { %>
        <%=(k+1)<propDemoExt.length?"<input type=\"text\" name=\""+propDemoExt[k+1].replace(' ', '_')+"\"  value=''>" : "&nbsp;" %>
        <% }  %>    
      </td>
    </tr>
<% 	} 
}
if(oscarVariables.getProperty("demographicExtJScript") != null) { out.println(oscarVariables.getProperty("demographicExtJScript")); }
%>    
    <tr >
      <td colspan="4">
        <table width="100%" bgcolor="#EEEEFF">
          <tr>
            <td width="10%" align="right"><font color="#FF0000"><b><bean:message key="demographic.demographicaddrecordhtm.formAlert"/>: </b></font>
            </td>
            <td>
              <textarea name="cust3" style="width:100%" rows="2" ></textarea>
            </td>
          </tr>
          <tr>
            <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formNotes"/> : </b></td>
            <td>
              <textarea name="content" style="width:100%" rows="2"></textarea>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr bgcolor="#CCCCFF">
      <td colspan="4">
        <div align="center">
          <input type="hidden" name="dboperation" value="add_record">
          <%
          if (vLocale.getCountry().equals("BR")) { %>
          <input type="hidden" name="dboperation2" value="add_record_ptbr">
          <%}%>
<%--
          <input type="submit" name="displaymode" value="<bean:message key="demographic.demographicaddrecordhtm.btnAddRecord"/>" onclick="document.forms['adddemographic'].displaymode.value='Add Record'; document.forms['adddemographic'].submit();">
--%>
          <input type="hidden" name="displaymode" value="Add Record">
          <input type="submit" name="submit" value="<bean:message key="demographic.demographicaddrecordhtm.btnAddRecord"/>">
          <input type="button" name="Button" value="<bean:message key="demographic.demographicaddrecordhtm.btnSwipeCard"/>" onclick="window.open('zadddemographicswipe.htm','', 'scrollbars=yes,resizable=yes,width=600,height=300')";>
          &nbsp;
          <input type="button" name="Button" value="<bean:message key="demographic.demographicaddrecordhtm.btnCancel"/>" onclick=self.close();>
        </div>
      </td>
    </tr>
  </form>
</table>
</td></tr></table>
 * <font face="Courier New, Courier, mono" size="-1"><bean:message key="demographic.demographicaddrecordhtm.formDateFormat"/> </font>
<% addDemoBean.closePstmtConn(); %>
 
<script type="text/javascript"> 
Calendar.setup({ inputField : "waiting_list_referral_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "referral_date_cal", singleClick : true, step : 1 }); 
</script> 
</body>
</html:html>
