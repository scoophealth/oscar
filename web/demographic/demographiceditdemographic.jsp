<%-- @ taglib uri="../WEB-INF/taglibs-log.tld" prefix="log" --%>
<%--
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
--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String demographic$ = request.getParameter("demographic_no") ;
    
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());  
    CountryCodeDAO ccDAO =  (CountryCodeDAO) ctx.getBean("countryCodeDAO");
    List<CountryCode> countryList = ccDAO.getAllCountryCodes();
    %>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic"
	rights="r" reverse="<%=true%>">
	<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<security:oscarSec roleName="<%=roleName$%>"
	objectName='<%="_demographic$"+demographic$%>' rights="o"
	reverse="<%=false%>">
You have no rights to access the data!
<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<%@ page
	import="java.util.*, java.sql.*, java.net.*,java.text.DecimalFormat, oscar.*, oscar.oscarDemographic.data.ProvinceNames, oscar.oscarWaitingList.WaitingList"
	errorPage="../appointment/errorpage.jsp"%>
<%@ page import="org.oscarehr.phr.PHRAuthentication"%>
<%@ page import="oscar.oscarDemographic.data.*"%>
<%@ page
	import="org.springframework.web.context.*,org.springframework.web.context.support.*,org.oscarehr.common.dao.*,org.oscarehr.common.model.*"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<jsp:useBean id="oscarVariables" class="java.util.Properties"
	scope="session" />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");

	String curProvider_no = (String) session.getAttribute("user");
	String demographic_no = request.getParameter("demographic_no") ;
	String userfirstname = (String) session.getAttribute("userfirstname");
	String userlastname = (String) session.getAttribute("userlastname");
	String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
	String str = null;
	int nStrShowLen = 20;
        String prov= ((String ) oscarVariables.getProperty("billregion","")).trim().toUpperCase();

        OscarProperties oscarProps = OscarProperties.getInstance();

        ProvinceNames pNames = ProvinceNames.getInstance();
   oscar.oscarDemographic.data.DemographicExt ext = new oscar.oscarDemographic.data.DemographicExt();
   ArrayList arr = ext.getListOfValuesForDemo(demographic_no);
   Hashtable demoExt = ext.getAllValuesForDemo(demographic_no);
   
    GregorianCalendar now=new GregorianCalendar();
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
%>



<html:html locale="true">
<head>
<title><bean:message
	key="demographic.demographiceditdemographic.title" /></title>
<html:base />
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
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
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<script language="javascript" type="text/javascript"
	src="../share/javascript/Oscar.js"></script>

<!--popup menu for encounter type -->
<script src="<c:out value="${ctx}"/>/share/javascript/popupmenu.js"
	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/menutility.js"
	type="text/javascript"></script>

<script language="JavaScript" type="text/javascript">

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
  var popup=window.open(page, "apptProvider", windowprops);
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

function checkName() {
	var typeInOK = false;
	if(document.updatedelete.last_name.value!="" && document.updatedelete.first_name.value!="" && document.updatedelete.last_name.value!=" " && document.updatedelete.first_name.value!=" ") {
	    typeInOK = true;
	} else {
		alert ("You must type in the following fields: Last Name, First Name.");
    }
	return typeInOK;
}
function checkDob() {
	var typeInOK = false;
	var yyyy = document.updatedelete.year_of_birth.value;
	var mm = document.updatedelete.month_of_birth.value;
	var dd = document.updatedelete.date_of_birth.value;

	if(checkTypeNum(yyyy) && checkTypeNum(mm) && checkTypeNum(dd) ){
        var check_date = new Date(yyyy,(mm-1),dd);
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
   //alert( isValidDate(dd,mm,yyyy) );

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
	var hin = document.updatedelete.hin.value;
	var province = document.updatedelete.hc_type.value;
	//alert(hin);
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

function checkTypeInEdit() {
  if ( !checkName() ) return false;
  if ( !checkDob() ) return false;
  if ( !checkHin() ) return false;
  //if ( !checkAllDate() ) return false;
  return(true);
}

function checkTypeNum(typeIn) {
	var typeInOK = true;
	var i = 0;
	var length = typeIn.length;
	var ch;
	// walk through a string and find a number
	if (length>=1) {
	  while (i <  length) {
		  ch = typeIn.substring(i, i+1);
		  if (ch == "-") { i++; continue; }
		  if ((ch < "0") || (ch > "9") ) {
			  typeInOK = false;
			  break;
		  }
	    i++;
    }
	} else typeInOK = false;
	return typeInOK;
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
function checkONReferralNo() {
  var referralNo = document.updatedelete.r_doctor_ohip.value ;
  if (document.updatedelete.hc_type.value == 'ON' && referralNo.length > 0 && referralNo.length != 6) {
    alert("The referral doctor's no. is wrong. Please correct it!") ;
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

function newStatus() {
    newOpt = prompt("Please enter the new status:", "");
    if (newOpt != "") {
        document.updatedelete.patient_status.options[document.updatedelete.patient_status.length] = new Option(newOpt, newOpt);
        document.updatedelete.patient_status.options[document.updatedelete.patient_status.length-1].selected = true;
    } else {
        alert("Invalid entry");
    }
}

function newStatus1() {
    newOpt = prompt("Please enter the new status:", "");
    if (newOpt != "") {
        document.updatedelete.roster_status.options[document.updatedelete.roster_status.length] = new Option(newOpt, newOpt);
        document.updatedelete.roster_status.options[document.updatedelete.roster_status.length-1].selected = true;
    } else {
        alert("Invalid entry");
    }
}

</script>
<script language="JavaScript">
function showHideDetail(){
    showHideItem('editDemographic');
    showHideItem('viewDemographics2');
    showHideItem('updateButton');
    showHideItem('swipeButton');
}

function showHideItem(id){
    if(document.getElementById(id).style.display == 'none')
        document.getElementById(id).style.display = 'inline';
    else
        document.getElementById(id).style.display = 'none';
}

function showItem(id){
        document.getElementById(id).style.display = 'inline';
}

function hideItem(id){
        document.getElementById(id).style.display = 'none';
}

<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=false%>" >
var numMenus = 1;
var encURL = "<c:out value="${ctx}"/>/oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo=<%=demographic_no%>&curProviderNo=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&encType=<%=URLEncoder.encode("telephone encounter with client")%>&userName=<%=URLEncoder.encode( userfirstname+" "+userlastname) %>&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=";
function showMenu(menuNumber, eventObj) {        
    var menuId = 'menu' + menuNumber;
    return showPopup(menuId, eventObj);
}

function add2url(txt) {
    var reasonLabel = "reason=";
    var encTypeLabel = "encType=";
    var beg = encURL.indexOf(reasonLabel);
    beg+= reasonLabel.length;
    var end = encURL.indexOf("&", beg);
    var part1 = encURL.substring(0,beg);
    var part2 = encURL.substr(end);
    encURL = part1 + encodeURI(txt) + part2;
    beg = encURL.indexOf(encTypeLabel);
    beg += encTypeLabel.length;
    end = encURL.indexOf("&", beg);
    part1 = encURL.substring(0,beg);
    part2 = encURL.substr(end);
    encURL = part1 + encodeURI(txt) + part2;    
    popupEChart(710, 1024,encURL);
    return false;
}

function customReason() {
    var txtInput;
    var list = document.getElementById("listCustom");
    if( list.style.display == "block" )        
        list.style.display = "none";
    else {
        list.style.display = "block";
        txtInput = document.getElementById("txtCustom");
        txtInput.focus();
    }
        
    return false;
}

function grabEnterCustomReason(event){

  var txtInput = document.getElementById("txtCustom");
  if(window.event && window.event.keyCode == 13){          
      add2url(txtInput.value);
  }else if (event && event.which == 13){     
      add2url(txtInput.value);
  }
  
  return true;
}
</security:oscarSec>
</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>
<body onLoad="setfocus(); checkONReferralNo(); formatPhoneNum();"
	topmargin="0" leftmargin="0" rightmargin="0">
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="demographic.demographiceditdemographic.msgPatientDetailRecord" />
		</td>
		<td class="MainTableTopRowRightColumn" width="400">
		<table class="TopStatusBar">
			<tr>
				<td>
				<%
                           java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
                                //----------------------------REFERRAL DOCTOR------------------------------
                                String rdohip="", rd="", fd="", family_doc = "";

                                String resident="", nurse="", alert="", notes="", midwife="";
                                ResultSet rs = null;
                                rs = apptMainBean.queryResults(demographic_no, "search_demographiccust");
                                while (rs.next()) { 
                                        resident = (apptMainBean.getString(rs,"cust1"));
                                        nurse = (apptMainBean.getString(rs,"cust2"));
                                        alert = (apptMainBean.getString(rs,"cust3"));
                                        midwife = (apptMainBean.getString(rs,"cust4"));
                                        notes = SxmlMisc.getXmlContent(apptMainBean.getString(rs,"content"),"unotes") ;
                                        notes = notes==null?"":notes;
                                }
                                rs.close();
                                
                                String dateString = curYear+"-"+curMonth+"-"+curDay;
                                int age=0, dob_year=0, dob_month=0, dob_date=0;

                                int param = Integer.parseInt(demographic_no);

                                rs = apptMainBean.queryResults(param, request.getParameter("dboperation"));
                                if(rs==null) {
                                        out.println("failed!!!");
                                } else {
                                        while (rs.next()) {
                                                //----------------------------REFERRAL DOCTOR------------------------------
                                                fd=apptMainBean.getString(rs,"family_doctor");
                                                if (fd==null) {
                                                        rd = "";
                                                        rdohip="";
                                                        family_doc = "";
                                                }else{
                                                        rd = SxmlMisc.getXmlContent(apptMainBean.getString(rs,"family_doctor"),"rd")    ;
                                                        rd = rd !=null ? rd : "" ;
                                                        rdohip = SxmlMisc.getXmlContent(apptMainBean.getString(rs,"family_doctor"),"rdohip");
                                                        rdohip = rdohip !=null ? rdohip : "" ;
                                                        family_doc = SxmlMisc.getXmlContent(apptMainBean.getString(rs,"family_doctor"),"family_doc");
                                                        family_doc = family_doc !=null ? family_doc : "" ;
                                                }
                                                //----------------------------REFERRAL DOCTOR --------------end-----------

                                                dob_year = Integer.parseInt(apptMainBean.getString(rs,"year_of_birth"));
                                                dob_month = Integer.parseInt(apptMainBean.getString(rs,"month_of_birth"));
                                                dob_date = Integer.parseInt(apptMainBean.getString(rs,"date_of_birth"));
                                                if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
                        %> <%=apptMainBean.getString(rs,"last_name")%>,
				<%=apptMainBean.getString(rs,"first_name")%> <%=apptMainBean.getString(rs,"sex")%>
				<%=age%> years <span style="margin-left: 20px;"><i>Next
				Appointment: <oscar:nextAppt
					demographicNo='<%=apptMainBean.getString(rs,"demographic_no")%>' /></i></span>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">
		<table border=0 cellspacing=0 width="100%">
			<tr class="Header">
				<td style="font-weight: bold">Appointment</td>
			</tr>
			<tr>
				<td><a
					href='demographiccontrol.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&last_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>&first_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>&orderby=appttime&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25'><bean:message
					key="demographic.demographiceditdemographic.btnApptHist" /></a></td>
			</tr>
			<%
                    String wLReadonly = ""; 
                    WaitingList wL = WaitingList.getInstance();
                   if(!wL.getFound()){ 
                        wLReadonly = "readonly"; 
                        }
                   if(wLReadonly.equals("")){ 
                %>
			<tr>
				<td><a
					href="../oscarWaitingList/SetupDisplayPatientWaitingList.do?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>">Waiting
				List</a></td>
			</tr>
			<%}%>
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="admin.admin.billing" /></td>
			</tr>
			<tr>
				<td>
				<% if (vLocale.getCountry().equals("BR")) { %> <!--a href="javascript: function myFunction() {return false; }" onClick="popupPage(500,600,'../billing/billinghistory.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&last_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>&first_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10')">Billing History</a-->
				<a
					href='../oscar/billing/consultaFaturamentoPaciente/init.do?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>'>Hist&oacute;rico
				do Faturamento</a>
				</th>
				<% } else if("ON".equals(prov)) {%>
				<a href="javascript: function myFunction() {return false; }"
					onClick="popupPage(500,600,'../billing/CA/ON/billinghistory.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&last_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>&first_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10')">Billing
				History</a>
				<%}else{%>
				<a href="#"
					onclick="popupPage(800,1000,'../billing/CA/BC/billStatus.jsp?lastName=<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>&firstName=<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>&filterPatient=true&demographicNo=<%=apptMainBean.getString(rs,"demographic_no")%>');return false;">Invoice
				List</a>
				<%}%>
				</td>
			</tr>
			<% if (!vLocale.getCountry().equals("BR")) { %>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700, 1000, '../billing.do?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=&appointment_no=0&demographic_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>%2C<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>&demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&providerview=1&user_no=<%=curProvider_no%>&apptProvider_no=none&appointment_date=<%=dateString%>&start_time=0:00&bNewForm=1&status=t');return false;"
					title="bill a patient">Create Invoice</a></td>
			</tr>
			<%    if("ON".equals(prov)) {
                	       String default_view = oscarVariables.getProperty("default_view", "");
                %>
			<%    if (!oscarProps.getProperty("clinic_no", "").startsWith("1022")) { // part 2 of quick hack to make Dr. Hunter happy %>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="window.open('../billing/CA/ON/specialtyBilling/fluBilling/addFluBilling.jsp?function=demographic&functionid=<%=apptMainBean.getString(rs,"demographic_no")%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>%2C<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>&hin=<%=URLEncoder.encode(apptMainBean.getString(rs,"hin")!=null?apptMainBean.getString(rs,"hin"):"")%><%=URLEncoder.encode(apptMainBean.getString(rs,"ver")!=null?apptMainBean.getString(rs,"ver"):"")%>&demo_sex=<%=URLEncoder.encode(apptMainBean.getString(rs,"sex"))%>&demo_hctype=<%=URLEncoder.encode(apptMainBean.getString(rs,"hc_type")==null?"null":apptMainBean.getString(rs,"hc_type"))%>&rd=<%=URLEncoder.encode(rd==null?"null":rd)%>&rdohip=<%=URLEncoder.encode(rdohip==null?"null":rdohip)%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(apptMainBean.getString(rs,"year_of_birth")),Integer.parseInt(apptMainBean.getString(rs,"month_of_birth")),Integer.parseInt(apptMainBean.getString(rs,"date_of_birth")))%>','', 'scrollbars=yes,resizable=yes,width=720,height=500');return false;"
					title='Add Flu Billing'>Flu Billing</a></td>
			</tr>
			<% } %>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupS('../billing/CA/ON/billingShortcutPg1.jsp?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("hospital_view", default_view))%>&hotclick=&appointment_no=0&demographic_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>%2C<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>&demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&providerview=1&user_no=<%=curProvider_no%>&apptProvider_no=none&appointment_date=<%=dateString%>&start_time=0:00&bNewForm=1&status=t');return false;"
					title="bill a patient">Hospital Billing</a></td>
			</tr>

			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="window.open('../billing/CA/ON/inr/addINRbilling.jsp?function=demographic&functionid=<%=apptMainBean.getString(rs,"demographic_no")%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(apptMainBean.getString(rs,"last_name"))%>%2C<%=URLEncoder.encode(apptMainBean.getString(rs,"first_name"))%>&hin=<%=URLEncoder.encode(apptMainBean.getString(rs,"hin")!=null?apptMainBean.getString(rs,"hin"):"")%><%=URLEncoder.encode(apptMainBean.getString(rs,"ver")!=null?apptMainBean.getString(rs,"ver"):"")%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(apptMainBean.getString(rs,"year_of_birth")),Integer.parseInt(apptMainBean.getString(rs,"month_of_birth")),Integer.parseInt(apptMainBean.getString(rs,"date_of_birth")))%>','', 'scrollbars=yes,resizable=yes,width=600,height=400');return false;"
					title='Add INR Billing'>Add INR</a>
				</th>
				</td>
			</tr>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="window.open('../billing/CA/ON/inr/reportINR.jsp?provider_no=<%=curProvider_no%>','', 'scrollbars=yes,resizable=yes,width=600,height=600');return false;"
					title='INR Billing'>Bill INR</a>
				</th>
				</td>
			</tr>
			<%  } %>
			<% } %>
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="oscarEncounter.Index.clinicalModules" /></td>
			</tr>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,960,'../oscarEncounter/oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp?de=<%=apptMainBean.getString(rs,"demographic_no")%>&proNo=<%=apptMainBean.getString(rs,"provider_no")%>')"><bean:message
					key="demographic.demographiceditdemographic.btnConsultation" /></a></td>
			</tr>
			<% if (!vLocale.getCountry().equals("BR")) { %>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupOscarRx(700,960,'../oscarRx/choosePatient.do?providerNo=<%=curProvider_no%>&demographicNo=<%=demographic_no%>')"><bean:message
					key="global.prescriptions" /></a>
				</th>
				</td>
			</tr>
			<% } %>
			<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart"
				rights="r" reverse="<%=false%>">
				<tr>
					<td><!--a href=# onclick="popupPage(600,800,'../provider/providercontrol.jsp?appointment_no=&demographic_no=<%=demographic_no%>&curProvider_no=&reason=<%=URLEncoder.encode("telephone encounter with client")%>&username=&appointment_date=&start_time=&status=&displaymode=encounter&dboperation=search_demograph&template=');return false;" title="Tel-Progress Notes">Add Encounter</a-->
					<a href="javascript: function myFunction() {return false; }"
						onClick="popupEChart(710, 1024,encURL);return false;"
						title="<bean:message key="demographic.demographiceditdemographic.btnEChart"/>">
					<bean:message
						key="demographic.demographiceditdemographic.btnEChart" /></a>&nbsp;<a
						style="text-decoration: none;" href="#"
						onmouseover="return !showMenu('1', event);">+</a>
					<div id='menu1' class='menu' onclick='event.cancelBubble = true;'>
					<h3 style='text-align: center'>Encounter Type</h3>
					<br>
					<ul>
						<li><a href="#" onmouseover='this.style.color="black"'
							onmouseout='this.style.color="white"'
							onclick="return add2url('face to face encounter with client');">face
						to face encounter with client</a><br>
						</li>
						<li><a href="#" onmouseover='this.style.color="black"'
							onmouseout='this.style.color="white"'
							onclick="return add2url('telephone encounter with client');">telephone
						encounter with client</a><br>
						</li>
						<li><a href="#" onmouseover='this.style.color="black"'
							onmouseout='this.style.color="white"'
							onclick="return add2url('encounter without client');">encounter
						without client</a><br>
						</li>
						<li><a href="#" onmouseover='this.style.color="black"'
							onmouseout='this.style.color="white"'
							onclick="return customReason();">Custom</a></li>
						<li id="listCustom" style="display: none;"><input
							id="txtCustom" type="text" size="16" maxlength="32"
							onkeypress="return grabEnterCustomReason(event);"></li>
					</ul>
					</div>
					</td>
				</tr>
				<tr>
					<td><a
						href="javascript: function myFunction() {return false; }"
						onClick="popupPage(700,960,'<c:out value="${ctx}"/>/oscarPrevention/index.jsp?demographic_no=<%=demographic_no%>');return false;">
					<bean:message key="oscarEncounter.LeftNavBar.Prevent" /></a></td>
				</tr>
			</security:oscarSec>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,1000,'../tickler/ticklerDemoMain.jsp?demoview=<%=demographic_no%>');return false;">
				<bean:message key="global.tickler" /></a></td>
			</tr>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popup(700,960,'../oscarMessenger/SendDemoMessage.do?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>','msg')">Send
				a Message</a></td>
			</tr>
			<oscar:oscarPropertiesCheck property="MY_OSCAR" value="yes">
				<phr:indivoRegistered provider="<%=curProvider_no%>"
					demographic="<%=demographic_no%>">
					<tr>
						<td><a
							href="javascript: function myFunction() {return false; }"
							ONCLICK="popupOscarRx(600,900,'../phr//PhrMessage.do?method=createMessage&providerNo=<%=curProvider_no%>&demographicNo=<%=demographic_no%>')"
							title="myOscar">Send Message to PHR</a></td>
					</tr>
				</phr:indivoRegistered>
			</oscar:oscarPropertiesCheck>
			<% if (oscarProps.getProperty("clinic_no", "").startsWith("1022")) { // quick hack to make Dr. Hunter happy %>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,1000,'../form/forwardshortcutname.jsp?formname=AR1&demographic_no=<%=request.getParameter("demographic_no")%>');">AR1</a>
				</td>
			</tr>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,1000,'../form/forwardshortcutname.jsp?formname=AR2&demographic_no=<%=request.getParameter("demographic_no")%>');">AR2</a>
				</td>
			</tr>
			<% } %>
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="oscarEncounter.Index.clinicalResources" /></td>
			</tr>
			<tr>
				<td><!--th><a href="javascript: function myFunction() {return false; }" onClick="popupPage(500,600,'demographicsummary.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>')">Patient Summary</a> </th-->
				<a href="javascript: function myFunction() {return false; }"
					onClick="popupPage(710,970,'../dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=apptMainBean.getString(rs,"demographic_no")%>&curUser=<%=curProvider_no%>')"><bean:message
					key="demographic.demographiceditdemographic.msgDocuments" /></a></td>
			</tr>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(710,970,'../dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=apptMainBean.getString(rs,"demographic_no")%>&curUser=<%=curProvider_no%>&mode=add')"><bean:message
					key="demographic.demographiceditdemographic.btnAddDocument" /></a></td>
			</tr>
			<tr>
				<td><a
					href="../eform/efmpatientformlist.jsp?demographic_no=<%=demographic_no%>"><bean:message
					key="demographic.demographiceditdemographic.btnEForm" /></a></td>
			</tr>
			<tr>
				<td><a
					href="../eform/efmformslistadd.jsp?demographic_no=<%=demographic_no%>">
				<bean:message
					key="demographic.demographiceditdemographic.btnAddEForm" /> </a></td>
			</tr>
		</table>
		</td>
		<td class="MainTableRightColumn" valign="top">
		<table border=0 cellspacing=4 width="100%">
			<tr>
				<td colspan="4"><%-- log:info category="Demographic">Demographic [<%=demographic_no%>] is viewed by User [<%=userfirstname%> <%=userlastname %>]  </log:info --%>
				<%@ include file="zdemographicfulltitlesearch.jsp"%>
				</td>
			</tr>
			<tr>
				<td>
				<form method="post" name="updatedelete" id="updatedelete"
					action="demographiccontrol.jsp"
					onSubmit="return checkTypeInEdit();"><input type="hidden"
					name="demographic_no"
					value="<%=apptMainBean.getString(rs,"demographic_no")%>">
			<tr>
				<td>
				<table width="100%" bgcolor="#CCCCFF" cellspacing="1"
					cellpadding="1">
					<tr>
						<td class="RowTop">
						<%
                            DemographicMerged dmDAO = new DemographicMerged();                            
                            String dboperation = "search_detail";
                            String head = dmDAO.getHead(demographic_no);
                            ArrayList records = dmDAO.getTail(head);
                            if (vLocale.getCountry().equals("BR"))
                                dboperation = "search_detail_ptbr";
                                
                            %><b>Record</b> ( <%if (head.equals(demographic_no)){
                                    %><%=demographic_no%>
						<%
                                }else{
                                    %><a
							href="demographiccontrol.jsp?demographic_no=<%= head %>&displaymode=edit&dboperation=<%= dboperation %>"><%=head%></a>
						<%}
                            
                                for (int i=0; i < records.size(); i++){
                                    if (((String) records.get(i)).equals(demographic_no)){
                                        %><%=", "+demographic_no %>
						<%
                                    }else{
                                        %>, <a
							href="demographiccontrol.jsp?demographic_no=<%= records.get(i) %>&displaymode=edit&dboperation=<%= dboperation %>"><%=records.get(i)%></a>
						<%
                                    }
                                }
                            %> ) <%= head.equals(demographic_no) ? "<a href=\"javascript: showHideDetail();\">Edit</a>" : ""%>
						</td>
					</tr>
					<tr>
						<td bgcolor="#eeeeff"><!---new-->
						<div style="background-color: #EEEEFF;" id="viewDemographics2">
						<div class="demographicWrapper" style="background-color: #EEEEFF;">
						<div style="width: 49%; float: left;">
						<div class="demographicSection" style="margin-top: 2px;">
						<h3>&nbsp;Demographics</h3>
						<div style="background-color: #EEEEFF;">
						<ul>
							<li><bean:message
								key="demographic.demographiceditdemographic.formLastName" />: <b><%=apptMainBean.getString(rs,"last_name")%></b>
							<bean:message
								key="demographic.demographiceditdemographic.formFirstName" />: <b>
							<%=apptMainBean.getString(rs,"first_name")%></b></li>
							<li>Age:<b><%=age%></b> &nbsp; <bean:message
								key="demographic.demographiceditdemographic.formDOB" />:<b>(<%=apptMainBean.getString(rs,"year_of_birth")%>-<%=apptMainBean.getString(rs,"month_of_birth")%>-<%=apptMainBean.getString(rs,"date_of_birth")%>)</b>
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formSex" />:<b><%=apptMainBean.getString(rs,"sex")%></b>
							</li>
							<li>Language: <b><%= apptMainBean.getString(demoExt.get("language"))%></b>
							<% if (apptMainBean.getString(rs,"country_of_origin") != null &&  !apptMainBean.getString(rs,"country_of_origin").equals("") && !apptMainBean.getString(rs,"country_of_origin").equals("-1")){ 
                                                        CountryCode countryCode = ccDAO.getCountryCode(apptMainBean.getString(rs,"country_of_origin"));
                                                        if  (countryCode != null){
                                                    %> Country Of
							Origin: <b><%=countryCode.getCountryName() %></b> <%      }
                                                    }
                                                %>
							</li>
							<%if (oscarProps.getProperty("EXTRA_DEMO_FIELDS") !=null){
                                              String fieldJSP = oscarProps.getProperty("EXTRA_DEMO_FIELDS"); 
                                              fieldJSP+= "View.jsp";  
                                            %>
							<jsp:include page="<%=fieldJSP%>">
								<jsp:param name="demo" value="<%=demographic_no%>" />
							</jsp:include>
							<%}%>

						</ul>
						</div>
						</div>

						<div class="demographicSection">
						<h3>&nbsp;Other Contacts: <b><a
							href="javascript: function myFunction() {return false; }"
							onClick="popup(700,960,'AddAlternateContact.jsp?demo=<%=apptMainBean.getString(rs,"demographic_no")%>','AddRelation')">Add
						Relation<!--i18n--></a></b></h3>
						<div style="background-color: #EEEEFF;">
						<ul>
							<%DemographicRelationship demoRelation = new DemographicRelationship();
                                          ArrayList relList = demoRelation.getDemographicRelationshipsWithNamePhone(apptMainBean.getString(rs,"demographic_no"));
                                          for (int reCounter = 0; reCounter < relList.size(); reCounter++){
                                             Hashtable relHash = (Hashtable) relList.get(reCounter);
                                             String sdb = relHash.get("subDecisionMaker") == null?"":((Boolean) relHash.get("subDecisionMaker")).booleanValue()?"<span title=\"SDM\" >/SDM</span>":"";
                                             String ec = relHash.get("emergencyContact") == null?"":((Boolean) relHash.get("emergencyContact")).booleanValue()?"<span title=\"Emergency Contact\">/EC</span>":"";

                                          %>
							<li><b><%=relHash.get("relation")%><%=sdb%><%=ec%>: </b><%=relHash.get("lastName")%>,
							<%=relHash.get("firstName")%> ,<%=relHash.get("phone")%></li>
							<%}%>

						</ul>
						</div>
						</div>

						<div class="demographicSection">
						<h3>&nbsp;Clinic Status</h3>
						<div style="background-color: #EEEEFF;">
						<ul>
							<li><bean:message
								key="demographic.demographiceditdemographic.formRosterStatus" />:
							<b><%=apptMainBean.getString(rs,"roster_status")%></b> <bean:message
								key="demographic.demographiceditdemographic.DateJoined" />: <b><%=MyDateFormat.getMyStandardDate(apptMainBean.getString(rs,"hc_renew_date"))%></b>
							</li>

							<li>
							<%
String PatStat = rs.getString("patient_status");
String Dead = "DE";
String Inactive = "IN";

if ( PatStat.equals(Dead) ) {%>
							<div style="color: #FF0000;"><bean:message
								key="demographic.demographiceditdemographic.formPatientStatus" />:<b><%=rs.getString("patient_status")%></b></div>
							<%} else if (PatStat.equals(Inactive) ){%>
							<div style="color: #0000FF;"><bean:message
								key="demographic.demographiceditdemographic.formPatientStatus" />:<b><%=rs.getString("patient_status")%></b>
							<%} else {%> <bean:message
								key="demographic.demographiceditdemographic.formPatientStatus" />:<b><%=rs.getString("patient_status")%></b>
							<%}%>
							
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formChartNo" />:<b><%=apptMainBean.getString(rs,"chart_no")%></b>
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.cytolNum" />: <b>
							<%=apptMainBean.getString(demoExt.get("cytolNum"))%></b></li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formDateJoined1" />:
							<b><%=MyDateFormat.getMyStandardDate(apptMainBean.getString(rs,"date_joined"))%></b>
							<bean:message
								key="demographic.demographiceditdemographic.formEndDate" />: <b><%=MyDateFormat.getMyStandardDate(apptMainBean.getString(rs,"end_date"))%></b>
							</li>
						</ul>
						</div>
						</div>

						<div class="demographicSection">
						<h3>&nbsp;<bean:message
							key="demographic.demographiceditdemographic.formAlert" /></h3>
						<div style="background-color: #EEEEFF; color: red;"><%=alert%>
						&nbsp;</div>
						</div>

						</div>
						<div style="width: 49%; float: left; margin-left: 5px;">
						<div class="demographicSection" style="margin-top: 2px;">
						<h3>&nbsp;Contact Information</h3>
						<div style="background-color: #EEEEFF;">
						<ul>
							<li><bean:message
								key="demographic.demographiceditdemographic.formPhoneH" />:<b><%=apptMainBean.getString(rs,"phone")%>
							<%=apptMainBean.getString(demoExt.get("hPhoneExt"))%></b> <bean:message
								key="demographic.demographiceditdemographic.formPhoneW" />:<b>
							<%=apptMainBean.getString(rs,"phone2")%> <%=apptMainBean.getString(demoExt.get("wPhoneExt"))%></b>


							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formPhoneC" />:<b>
							<%=apptMainBean.getString(demoExt.get("demo_cell"))%></b></li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formAddr" />: <b><%=apptMainBean.getString(rs,"address")%></b>
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formCity" />: <b><%=apptMainBean.getString(rs,"city")%></b>
							</li>
							<li>
							<% if(oscarProps.getProperty("demographicLabelProvince") == null) { %>
							<bean:message
								key="demographic.demographiceditdemographic.formProcvince" /> <% } else {
			                                  out.print(oscarProps.getProperty("demographicLabelProvince"));
										   } %> : <b> <%=apptMainBean.getString(rs,"province")%></b></li>
							<li>
							<% if(oscarProps.getProperty("demographicLabelPostal") == null) { %>
							<bean:message
								key="demographic.demographiceditdemographic.formPostal" /> <% } else {
			                                  out.print(oscarProps.getProperty("demographicLabelPostal"));
										   } %> : <b> <%=apptMainBean.getString(rs,"postal")%></b></li>

							<li><bean:message
								key="demographic.demographiceditdemographic.formEmail" />: <b>
							<%=apptMainBean.getString(rs,"email")!=null? apptMainBean.getString(rs,"email") : ""%></b>
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formNewsLetter" />:
							<b> <%=apptMainBean.getString(rs,"newsletter")!=null? apptMainBean.getString(rs,"newsletter") : "Unknown"%></b>
							</li>
						</ul>
						</div>
						</div>

						<div class="demographicSection">
						<h3>&nbsp;Health Insurance</h3>
						<div style="background-color: #EEEEFF;">
						<ul>
							<li><bean:message
								key="demographic.demographiceditdemographic.formHin" />: <b><%=apptMainBean.getString(rs,"hin")%>
							&nbsp; <%=apptMainBean.getString(rs,"ver")%></b> <bean:message
								key="demographic.demographiceditdemographic.formHCType" />:<b><%=apptMainBean.getString(rs,"hc_type")==null?"":apptMainBean.getString(rs,"hc_type") %></b>
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formEFFDate" />:<b><%=MyDateFormat.getMyStandardDate(apptMainBean.getString(rs,"eff_date"))%></b>
							</li>
						</ul>
						</div>
						</div>

						<div class="demographicSection">
						<h3>&nbsp;Patient Clinic Status</h3>
						<div style="background-color: #EEEEFF;">
						<ul>
							<li>
							<% if(oscarProps.getProperty("demographicLabelDoctor") != null) { out.print(oscarProps.getProperty("demographicLabelDoctor","")); } else { %>
							<bean:message
								key="demographic.demographiceditdemographic.formDoctor" />
							<% } %>: <b><%=providerBean.getProperty(apptMainBean.getString(rs,"provider_no"),"")%></b>
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formNurse" />: <b><%=providerBean.getProperty(resident,"")%></b>
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formMidwife" />: <b><%=providerBean.getProperty(midwife,"")%></b>
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formResident" />:<b>
							<%=providerBean.getProperty(nurse,"")%></b></li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formRefDoc" />: <b><%=rd%></b>
							</li>
							<li><bean:message
								key="demographic.demographiceditdemographic.formRefDocNo" />: <b><%=rdohip%></b>
							</li>
						</ul>
						</div>
						</div>


						<div class="demographicSection">
						<h3>&nbsp;<bean:message
							key="demographic.demographiceditdemographic.formNotes" /></h3>
						<div style="background-color: #EEEEFF;"><%=notes%> &nbsp;</div>
						</div>
						</div>
						</div>

						<% // customized key
if(oscarVariables.getProperty("demographicExt") != null) {
	String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
%>
						<div class="demographicSection">
						<h3>&nbsp;Special</h3>
						<div style="background-color: #EEEEFF;">
						<% 	for(int k=0; k<propDemoExt.length; k++) {%> <%=propDemoExt[k]+": <b>" + apptMainBean.getString(demoExt.get(propDemoExt[k].replace(' ', '_')))%>
						</b>&nbsp;<%=((k+1)%4==0&&(k+1)<propDemoExt.length)?"<br>":"" %> <% 	} %>
						</div>
						</div>
						<% } %>
						</div>




						<!--newEnd-->

						<table width="100%" bgcolor="#EEEEFF" border=0
							id="editDemographic" style="display: none;">
							<tr>
								<td align="right"
									title='<%=apptMainBean.getString(rs,"demographic_no")%>'>
								<b><bean:message
									key="demographic.demographiceditdemographic.formLastName" />: </b></td>
								<td align="left"><input type="text" name="last_name"
									size="30" value="<%=apptMainBean.getString(rs,"last_name")%>"
									onBlur="upCaseCtrl(this)"></td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formFirstName" />:
								</b></td>
								<td align="left"><input type="text" name="first_name"
									size="30" value="<%=apptMainBean.getString(rs,"first_name")%>"
									onBlur="upCaseCtrl(this)"></td>
							</tr>

							<%
                            if (vLocale.getCountry().equals("BR")) { %>
							<tr>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formRG" />:</b></td>
								<td align="left"><input type="text" name="rg"
									value="<%=apptMainBean.getString(rs,"rg")==null?"":apptMainBean.getString(rs,"rg")%>"
									onBlur="upCaseCtrl(this)"></td>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formCPF" />:</b></td>
								<td align="left"><input type="text" name="cpf"
									value="<%=apptMainBean.getString(rs,"cpf")==null?"":apptMainBean.getString(rs,"cpf")%>"
									onBlur="upCaseCtrl(this)"></td>
							</tr>
							<tr>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formMaritalState" />:</b></td>
								<td align="left"><select name="marital_state">
									<option value="-">-</option>
									<option value="S"
										<%if (apptMainBean.getString(rs,"marital_state").trim().equals("S")){%>
										selected <%}%>><bean:message
										key="demographic.demographicaddrecordhtm.formMaritalState.optSingle" /></option>
									<option value="M"
										<%if (apptMainBean.getString(rs,"marital_state").trim().equals("M")){%>
										selected <%}%>><bean:message
										key="demographic.demographicaddrecordhtm.formMaritalState.optMarried" /></option>
									<option value="R"
										<%if (apptMainBean.getString(rs,"marital_state").trim().equals("R")){%>
										selected <%}%>><bean:message
										key="demographic.demographicaddrecordhtm.formMaritalState.optSeparated" /></option>
									<option value="D"
										<%if (apptMainBean.getString(rs,"marital_state").trim().equals("D")){%>
										selected <%}%>><bean:message
										key="demographic.demographicaddrecordhtm.formMaritalState.optDivorced" /></option>
									<option value="W"
										<%if (apptMainBean.getString(rs,"marital_state").trim().equals("W")){%>
										selected <%}%>><bean:message
										key="demographic.demographicaddrecordhtm.formMaritalState.optWidower" /></option>
								</select></td>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formBirthCertificate" />:</b>
								</td>
								<td align="left"><input type="text"
									name="birth_certificate"
									value="<%=apptMainBean.getString(rs,"birth_certificate")==null?"":apptMainBean.getString(rs,"birth_certificate")%>"
									onBlur="upCaseCtrl(this)"></td>
							</tr>
							<tr>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formMarriageCertificate" />:</b></td>
								<td align="left"><input type="text"
									name="marriage_certificate"
									value="<%=apptMainBean.getString(rs,"marriage_certificate")==null?"":apptMainBean.getString(rs,"marriage_certificate")%>"
									onBlur="upCaseCtrl(this)"></td>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formPartnerName" />:</b></td>
								<td align="left"><input type="text" name="partner_name"
									value="<%=apptMainBean.getString(rs,"partner_name")==null?"":apptMainBean.getString(rs,"partner_name")%>"
									onBlur="upCaseCtrl(this)"></td>
							</tr>
							<tr>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formFatherName" />:</b></td>
								<td align="left"><input type="text" name="father_name"
									value="<%=apptMainBean.getString(rs,"father_name")==null?"":apptMainBean.getString(rs,"father_name")%>"
									onBlur="upCaseCtrl(this)"></td>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formMotherName" />:</b></td>
								<td align="left"><input type="text" name="mother_name"
									value="<%=apptMainBean.getString(rs,"mother_name")==null?"":apptMainBean.getString(rs,"mother_name")%>"
									onBlur="upCaseCtrl(this)"></td>
							</tr>
							<%}%>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formAddr" />: </b></td>
								<td align="left"><input type="text" name="address"
									size="30" value="<%=apptMainBean.getString(rs,"address")%>">
								<% if (vLocale.getCountry().equals("BR")) { %> <b><bean:message
									key="demographic.demographicaddrecordhtm.formAddressNo" />:</b> <input
									type="text" name="address_no" size="30"
									value="<%=apptMainBean.getString(rs,"address_no")==null?"":apptMainBean.getString(rs,"address_no")%>"
									size="6"> <%}%>
								</td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formCity" />: </b></td>
								<td align="left"><input type="text" name="city" size="30"
									value="<%=apptMainBean.getString(rs,"city")%>"></td>
							</tr>
							<% if (vLocale.getCountry().equals("BR")) { %>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formComplementaryAddress" />:
								</b></td>
								<td align="left"><input type="text"
									name="complementary_address"
									value="<%=apptMainBean.getString(rs,"complementary_address")==null?"":apptMainBean.getString(rs,"complementary_address")%>"
									onBlur="upCaseCtrl(this)"></td>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formDistrict" />: </b></td>
								<td align="left"><input type="text" name="district"
									value="<%=apptMainBean.getString(rs,"district")==null?"":apptMainBean.getString(rs,"district")%>"
									onBlur="upCaseCtrl(this)"></td>
							</tr>
							<%}%>
							<tr valign="top">
								<td align="right"><b> <% if(oscarProps.getProperty("demographicLabelProvince") == null) { %>
								<bean:message
									key="demographic.demographiceditdemographic.formProcvince" /> <% } else {
                                  out.print(oscarProps.getProperty("demographicLabelProvince"));
                              	 } %> : </b></td>
								<td align="left">
								<% if (vLocale.getCountry().equals("BR")) { %> <input type="text"
									name="province"
									value="<%=apptMainBean.getString(rs,"province")%>"> <% } else { %>
								<% String province = apptMainBean.getString(rs,"province"); %> <select
									name="province" style="width: 200px">
									<option value="OT"
										<%=(province==null || province.equals("OT") || province.equals("") || province.length() > 2)?" selected":""%>>Other</option>
									<% if (pNames.isDefined()) {
                                       for (ListIterator li = pNames.listIterator(); li.hasNext(); ) {
                                           String pr2 = (String) li.next(); %>
									<option value="<%=pr2%>"
										<%=pr2.equals(province)?" selected":""%>><%=li.next()%></option>
									<% } %>
									<% } else { %>
									<option value="AB" <%="AB".equals(province)?" selected":""%>>AB-Alberta</option>
									<option value="BC" <%="BC".equals(province)?" selected":""%>>BC-British
									Columbia</option>
									<option value="MB" <%="MB".equals(province)?" selected":""%>>MB-Manitoba</option>
									<option value="NB" <%="NB".equals(province)?" selected":""%>>NB-New
									Brunswick</option>
									<option value="NL" <%="NL".equals(province)?" selected":""%>>NL-Newfoundland
									& Labrador</option>
									<option value="NT" <%="NT".equals(province)?" selected":""%>>NT-Northwest
									Territory</option>
									<option value="NS" <%="NS".equals(province)?" selected":""%>>NS-Nova
									Scotia</option>
									<option value="NU" <%="NU".equals(province)?" selected":""%>>NU-Nunavut</option>
									<option value="ON" <%="ON".equals(province)?" selected":""%>>ON-Ontario</option>
									<option value="PE" <%="PE".equals(province)?" selected":""%>>PE-Prince
									Edward Island</option>
									<option value="QC" <%="QC".equals(province)?" selected":""%>>QC-Quebec</option>
									<option value="SK" <%="SK".equals(province)?" selected":""%>>SK-Saskatchewan</option>
									<option value="YT" <%="YT".equals(province)?" selected":""%>>YT-Yukon</option>
									<option value="US" <%="US".equals(province)?" selected":""%>>US
									resident</option>
									<% } %>
								</select> <% } %>
								</td>
								<td align="right"><b> <% if(oscarProps.getProperty("demographicLabelPostal") == null) { %>
								<bean:message
									key="demographic.demographiceditdemographic.formPostal" /> <% } else {
                                  out.print(oscarProps.getProperty("demographicLabelPostal"));
                              	 } %> : </b></td>
								<td align="left"><input type="text" name="postal" size="30"
									value="<%=apptMainBean.getString(rs,"postal")%>"
									onBlur="upCaseCtrl(this)"></td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPhoneH" />: </b></td>
								<td align="left"><%-- // <input type="text" name="phone" size="30" value="<%=apptMainBean.getString(rs,"phone")!=null && apptMainBean.getString(rs,"phone").length()==10?apptMainBean.getString(rs,"phone").substring(0,3) + "-" + apptMainBean.getString(rs,"phone").substring(3,6) +"-"+  apptMainBean.getString(rs,"phone").substring(6):apptMainBean.getString(rs,"phone")%>">--%>
								<input type="text" name="phone" onblur="formatPhoneNum();"
									style="display: inline; width: auto;"
									value="<%=apptMainBean.getString(rs,"phone")%>"> Ext:<input
									type="text" name="hPhoneExt"
									value="<%=apptMainBean.getString(demoExt.get("hPhoneExt"))%>"
									size="4" /> <input type="hidden" name="hPhoneExtOrig"
									value="<%=apptMainBean.getString(demoExt.get("hPhoneExt"))%>" />
								</td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPhoneW" />:</b></td>
								<td align="left"><input type="text" name="phone2"
									onblur="formatPhoneNum();"
									style="display: inline; width: auto;"
									value="<%=apptMainBean.getString(rs,"phone2")%>"> Ext:<input
									type="text" name="wPhoneExt"
									value="<%=apptMainBean.getString(demoExt.get("wPhoneExt"))%>"
									style="display: inline" size="4" /> <input type="hidden"
									name="wPhoneExtOrig"
									value="<%=apptMainBean.getString(demoExt.get("wPhoneExt"))%>" />
								</td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPhoneC" />: </b></td>
								<td align="left"><%-- // <input type="text" name="phone" size="30" value="<%=apptMainBean.getString(rs,"phone")!=null && apptMainBean.getString(rs,"phone").length()==10?apptMainBean.getString(rs,"phone").substring(0,3) + "-" + apptMainBean.getString(rs,"phone").substring(3,6) +"-"+  apptMainBean.getString(rs,"phone").substring(6):apptMainBean.getString(rs,"phone")%>">--%>
								<input type="text" name="demo_cell" onblur="formatPhoneNum();"
									style="display: inline; width: auto;"
									value="<%=apptMainBean.getString(demoExt.get("demo_cell"))%>">
								<input type="hidden" name="demo_cellOrig"
									value="<%=apptMainBean.getString(demoExt.get("demo_cell"))%>" />
								</td>
								<td align="right"><b>Country Of Origin: </b></td>
								<td align="left"><select name="countryOfOrigin">
									<option value="-1">Not Set</option>
									<%for(CountryCode cc : countryList){ %>
									<option value="<%=cc.getCountryId()%>"
										<% if ( apptMainBean.getString(rs,"country_of_origin").equals(cc.getCountryId())){out.print("SELECTED") ;}%>><%=cc.getCountryName() %></option>
									<%}%>
								</select></td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formEmail" />: </b></td>
								<td align="left"><input type="text" name="email" size="30"
									value="<%=apptMainBean.getString(rs,"email")!=null? apptMainBean.getString(rs,"email") : ""%>">
								</td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPIN" />: </b></td>
								<td align="left"><input type="text" name="pin" size="30"
									value="<%=apptMainBean.getString(rs,"pin")!=null? apptMainBean.getString(rs,"pin") : ""%>"><br />
								<%if (apptMainBean.getString(rs,"pin")==null || apptMainBean.getString(rs,"pin").equals("")) {%>
								<a href="javascript:"
									onclick="popup(600, 650, '../phr/indivo/RegisterIndivo.jsp?demographicNo=<%=demographic_no%>', 'indivoRegistration');"><sub
									style="white-space: nowrap;">Register for MyOSCAR</sub></a> <%}%>
								</td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formNewsLetter" />:
								</b></td>
								<td align="left">
								<% String newsletter = apptMainBean.getString(rs,"newsletter").trim();
                                     if( newsletter == null || newsletter.equals("")) {
                                        newsletter = "Unknown";
                                     }
                                  %> <select name="newsletter">
									<option value="Unknown" <%if(newsletter.equals("Unknown")){%>
										selected <%}%>><bean:message
										key="demographic.demographicaddrecordhtm.formNewsLetter.optUnknown" /></option>
									<option value="No" <%if(newsletter.equals("No")){%> selected
										<%}%>><bean:message
										key="demographic.demographicaddrecordhtm.formNewsLetter.optNo" /></option>
									<option value="Paper" <%if(newsletter.equals("Paper")){%>
										selected <%}%>><bean:message
										key="demographic.demographicaddrecordhtm.formNewsLetter.optPaper" /></option>
									<option value="Electronic"
										<%if(newsletter.equals("Electronic")){%> selected <%}%>><bean:message
										key="demographic.demographicaddrecordhtm.formNewsLetter.optElectronic" /></option>
								</select></td>
								<td align="right"><b>SIN:</b></td>
								<td align="left"><input type="text" name="sin" size="30"
									value="<%=apptMainBean.getString(rs,"sin")%>"></td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formDOB" /></b><bean:message
									key="demographic.demographiceditdemographic.formDOBDetais" /><b>:</b>
								</td>
								<td align="left" nowrap><input type="text"
									name="year_of_birth"
									value="<%=apptMainBean.getString(rs,"year_of_birth")%>"
									size="3" maxlength="4"> <input type="text"
									name="month_of_birth"
									value="<%=apptMainBean.getString(rs,"month_of_birth")%>"
									size="2" maxlength="2"> <input type="text"
									name="date_of_birth"
									value="<%=apptMainBean.getString(rs,"date_of_birth")%>"
									size="2" maxlength="2"> <b>Age: <input type="text"
									name="age" readonly value="<%=age%>" size="3"> </b></td>
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formSex" />:</b></td>
								<td align="left" valign="top"><input type="text" name="sex"
									style="width: 20px;"
									value="<%=apptMainBean.getString(rs,"sex")%>"
									onBlur="upCaseCtrl(this)" size="1" maxlength="1"> <b>Language:</b>
								<input type="text" name="language"
									value="<%=apptMainBean.getString(demoExt.get("language"))%>"
									onBlur="upCaseCtrl(this)" size="19" /> <input type="hidden"
									name="languageOrig"
									value="<%=apptMainBean.getString(demoExt.get("language"))%>" />
								</td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formHin" />: </b></td>
								<td align="left" nowrap><input type="text" name="hin"
									value="<%=apptMainBean.getString(rs,"hin")%>" size="17">
								<b><bean:message
									key="demographic.demographiceditdemographic.formVer" /></b> <input
									type="text" name="ver"
									value="<%=apptMainBean.getString(rs,"ver")%>" size="3"
									onBlur="upCaseCtrl(this)"></td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formEFFDate" />:</b></td>
								<td align="left">
								<%
                                 // Put 0 on the left on dates
                                 DecimalFormat decF = new DecimalFormat();
                                 // Year
                                 decF.applyPattern("0000");
                                 String effDateYear = decF.format(MyDateFormat.getYearFromStandardDate(apptMainBean.getString(rs,"eff_date")));
                                 // Month and Day
                                 decF.applyPattern("00");
                                 String effDateMonth = decF.format(MyDateFormat.getMonthFromStandardDate(apptMainBean.getString(rs,"eff_date")));
                                 String effDateDay = decF.format(MyDateFormat.getDayFromStandardDate(apptMainBean.getString(rs,"eff_date")));
                              %> <input type="text" name="eff_date_year"
									size="4" maxlength="4" value="<%= effDateYear%>"> <input
									type="text" name="eff_date_month" size="2" maxlength="2"
									value="<%= effDateMonth%>"> <input type="text"
									name="eff_date_date" size="2" maxlength="2"
									value="<%= effDateDay%>"></td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formHCType" />:</b></td>
								<td align="left">
								<% if(vLocale.getCountry().equals("BR")) { %> <% String hctype = apptMainBean.getString(rs,"hc_type")==null?"":apptMainBean.getString(rs,"hc_type"); %>
								<input type="text" name="hc_type" value="<%=hctype%>"> <% } else {%>
								<% String hctype = apptMainBean.getString(rs,"hc_type")==null?"":apptMainBean.getString(rs,"hc_type"); %>
								<select name="hc_type" style="width: 200px">
									<option value="OT"
										<%=(hctype.equals("OT") || hctype.equals("") || hctype.length() > 2)?" selected":""%>>Other</option>
									<% if (pNames.isDefined()) {
                                       for (ListIterator li = pNames.listIterator(); li.hasNext(); ) {
                                           String province = (String) li.next(); %>
									<option value="<%=province%>"
										<%=province.equals(hctype)?" selected":""%>><%=li.next()%></option>
									<% } %>
									<% } else { %>
									<option value="AB" <%=hctype.equals("AB")?" selected":""%>>AB-Alberta</option>
									<option value="BC" <%=hctype.equals("BC")?" selected":""%>>BC-British
									Columbia</option>
									<option value="MB" <%=hctype.equals("MB")?" selected":""%>>MB-Manitoba</option>
									<option value="NB" <%=hctype.equals("NB")?" selected":""%>>NB-New
									Brunswick</option>
									<option value="NL" <%=hctype.equals("NL")?" selected":""%>>NL-Newfoundland
									& Labrador</option>
									<option value="NT" <%=hctype.equals("NT")?" selected":""%>>NT-Northwest
									Territory</option>
									<option value="NS" <%=hctype.equals("NS")?" selected":""%>>NS-Nova
									Scotia</option>
									<option value="NU" <%=hctype.equals("NU")?" selected":""%>>NU-Nunavut</option>
									<option value="ON" <%=hctype.equals("ON")?" selected":""%>>ON-Ontario</option>
									<option value="PE" <%=hctype.equals("PE")?" selected":""%>>PE-Prince
									Edward Island</option>
									<option value="QC" <%=hctype.equals("QC")?" selected":""%>>QC-Quebec</option>
									<option value="SK" <%=hctype.equals("SK")?" selected":""%>>SK-Saskatchewan</option>
									<option value="YT" <%=hctype.equals("YT")?" selected":""%>>YT-Yukon</option>
									<option value="US" <%=hctype.equals("US")?" selected":""%>>US
									resident</option>
									<% } %>
								</select> <% }%>
								</td>
								<td align="right" nowrap><b> <bean:message
									key="demographic.demographiceditdemographic.cytolNum" />:</b></td>
								<td><input type="text" name="cytolNum"
									style="display: inline; width: auto;"
									value="<%=apptMainBean.getString(demoExt.get("cytolNum"))%>">
								<input type="hidden" name="cytolNumOrig"
									value="<%=apptMainBean.getString(demoExt.get("cytolNum"))%>" />
								</td>
							</tr>
							<tr valign="top">
								<td align="right" nowrap><b>
								<% if(oscarProps.getProperty("demographicLabelDoctor") != null) { out.print(oscarProps.getProperty("demographicLabelDoctor","")); } else { %>
								<bean:message
									key="demographic.demographiceditdemographic.formDoctor" />
								<% } %>: </b></td>
								<td align="left"><select name="provider_no"
									style="width: 200px">
									<option value=""></option>
									<%
                          ResultSet rsdemo = apptMainBean.queryResults("search_provider_doc");
                          while (rsdemo.next()) {
                        %>
									<option value="<%=rsdemo.getString("provider_no")%>"
										<%=rsdemo.getString("provider_no").equals(apptMainBean.getString(rs,"provider_no"))?"selected":""%>>
									<%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
									<% } %>
								</select></td>
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formNurse" />: </b></td>
								<td align="left"><select name="resident"
									style="width: 200px">
									<option value=""></option>
									<%
                          rsdemo.close();
                          rsdemo=apptMainBean.queryResults("search_provider_doc");
                          while (rsdemo.next()) {
                        %>
									<option value="<%=rsdemo.getString("provider_no")%>"
										<%=rsdemo.getString("provider_no").equals(resident)?"selected":""%>>
									<%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
									<% } %>
								</select></td>
							</tr>
							<tr valign="top">
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formMidwife" />: </b></td>
								<td align="left"><select name="midwife"
									style="width: 200px">
									<option value=""></option>
									<%
                          rsdemo.close();
                          rsdemo=apptMainBean.queryResults("search_provider_doc");
                          while (rsdemo.next()) {
                        %>
									<option value="<%=rsdemo.getString("provider_no")%>"
										<%=rsdemo.getString("provider_no").equals(midwife)?"selected":""%>>
									<%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
									<% } %>
								</select></td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formResident" />:</b></td>
								<td align="left"><select name="nurse" style="width: 200px">
									<option value=""></option>
									<%
                          rsdemo.close();
                          rsdemo = apptMainBean.queryResults("search_provider_doc");
                          while (rsdemo.next()) {
                        %>
									<option value="<%=rsdemo.getString("provider_no")%>"
										<%=rsdemo.getString("provider_no").equals(nurse)?"selected":""%>>
									<%=Misc.getShortStr( (apptMainBean.getString(rsdemo,"last_name")+","+apptMainBean.getString(rsdemo,"first_name")),"",nStrShowLen)%></option>
									<% } %>
								</select></td>
							</tr>

							<tr valign="top">
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formRefDoc" />: </b></td>
								<td align="left">
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
									  	prop.setProperty("referral_no",apptMainBean.getString(rs1,"referral_no"));
									  	prop.setProperty("last_name",apptMainBean.getString(rs1,"last_name"));
									  	prop.setProperty("first_name",apptMainBean.getString(rs1,"first_name"));
									  	//prop.setProperty("specialty",apptMainBean.getString(rs1,"specialty"));
									  	//prop.setProperty("phone",apptMainBean.getString(rs1,"phone"));
									  	vecRef.add(prop);
                                      }
                                      rs1.close();
                                  %> <select name="r_doctor"
									onChange="changeRefDoc()" style="width: 200px">
									<option value=""></option>
									<% for(int k=0; k<vecRef.size(); k++) {
                                  		prop= (Properties) vecRef.get(k);
                                  	%>
									<option
										value="<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>"
										<%=prop.getProperty("referral_no").equals(rdohip)?"selected":""%>>
									<%=Misc.getShortStr( (prop.getProperty("last_name")+","+prop.getProperty("first_name")),"",nStrShowLen)%></option>
									<% }
 	                      	rsdemo.close();
 	                       %>
								</select> <script language="Javascript">
<!--
function changeRefDoc() {
//alert(document.updatedelete.r_doctor.value);
var refName = document.updatedelete.r_doctor.options[document.updatedelete.r_doctor.selectedIndex].value;
var refNo = "";
  	<% for(int k=0; k<vecRef.size(); k++) {
  		prop= (Properties) vecRef.get(k);
  	%>
if(refName=="<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>") {
  refNo = '<%=prop.getProperty("referral_no", "")%>';
}
<% } %>
document.updatedelete.r_doctor_ohip.value = refNo;
}
//-->
</script> <% } else {%> <input type="text" name="r_doctor" size="30" maxlength="40"
									value="<%=rd%>"> <% } %>
								</td>
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formRefDocNo" />: </b></td>
								<td align="left"><input type="text" name="r_doctor_ohip"
									size="20" maxlength="6" value="<%=rdohip%>"> <% if("ON".equals(prov)) { %>
								<a
									href="javascript:referralScriptAttach2('r_doctor_ohip','r_doctor')">Search
								#</a> <% } %>
								</td>
							</tr>

							<tr valign="top">
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formRosterStatus" />:
								</b></td>
								<td align="left">
								<%String rosterStatus = apptMainBean.getString(rs,"roster_status");
                                  if (rosterStatus == null) {
                                     rosterStatus = "";
                                  }
                                  %> <!--  input type="text" name="roster_status" size="30" value="<%--=rosterStatus--%>" onBlur="upCaseCtrl(this)" -->
								<select name="roster_status" style="width: 120">
									<option value=""></option>
									<option value="RO"
										<%=rosterStatus.equals("RO")?" selected":""%>>RO -
									rostered</option>
									<option value="NR"
										<%=rosterStatus.equals("NR")?" selected":""%>>NR -
									not rostered</option>
									<option value="TE"
										<%=rosterStatus.equals("TE")?" selected":""%>>TE -
									terminated</option>
									<option value="FS"
										<%=rosterStatus.equals("FS")?" selected":""%>>FS -
									fee for service</option>
									<% ResultSet rsstatus1 = apptMainBean.queryResults("search_rsstatus");
                                     while (rsstatus1.next()) { %>
									<option
										<%=rosterStatus.equals(rsstatus1.getString("roster_status"))?" selected":""%>><%=rsstatus1.getString("roster_status")%></option>
									<% }
                                     rsstatus1.close();
                                   // end while %>
								</select> <input type="button" onClick="newStatus1();" value="Add New">
								</td>
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.DateJoined" />: </b></td>
								<td align="left">
								<%
                                 // Format year
                                 decF.applyPattern("0000");
                                 String hcRenewYear = decF.format(MyDateFormat.getYearFromStandardDate(apptMainBean.getString(rs,"hc_renew_date")));
                                 decF.applyPattern("00");
                                 String hcRenewMonth = decF.format(MyDateFormat.getMonthFromStandardDate(apptMainBean.getString(rs,"hc_renew_date")));
                                 String hcRenewDay = decF.format(MyDateFormat.getDayFromStandardDate(apptMainBean.getString(rs,"hc_renew_date")));
                              %> <input type="text"
									name="hc_renew_date_year" size="4" maxlength="4"
									value="<%= hcRenewYear %>"> <input type="text"
									name="hc_renew_date_month" size="2" maxlength="2"
									value="<%= hcRenewMonth %>"> <input type="text"
									name="hc_renew_date_date" size="2" maxlength="2"
									value="<%= hcRenewDay %>"></td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPatientStatus" />:</b>
								<b> </b></td>
								<td align="left">
								<% if (vLocale.getCountry().equals("BR")) { %> <%String pacStatus = apptMainBean.getString(rs,"patient_status");
                                  if (pacStatus == null) {
                                     pacStatus = "";
                                  }
                                  %> <input type="text"
									name="patient_status" value="<%=pacStatus%>"> <% } else {
                                String patientStatus = apptMainBean.getString(rs,"patient_status"); %>
								<select name="patient_status" style="width: 120">
									<option value="AC"
										<%=patientStatus.equals("AC")?" selected":""%>>AC -
									Active</option>
									<option value="IN"
										<%=patientStatus.equals("IN")?" selected":""%>>IN -
									Inactive</option>
									<option value="DE"
										<%=patientStatus.equals("DE")?" selected":""%>>DE -
									Deceased</option>
									<option value="MO"
										<%=patientStatus.equals("MO")?" selected":""%>>MO -
									Moved</option>
									<option value="FI"
										<%=patientStatus.equals("FI")?" selected":""%>>FI -
									Fired</option>
									<% ResultSet rsstatus = apptMainBean.queryResults("search_ptstatus");
                                     while (rsstatus.next()) { %>
									<option
										<%=patientStatus.equals(rsstatus.getString("patient_status"))?" selected":""%>><%=rsstatus.getString("patient_status")%></option>
									<% }
                                  rsstatus.close();
                                   // end while %>
								</select> <input type="button" onClick="newStatus();" value="Add New">
								<% } // end if...then...else %>
								</td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formChartNo" />:</b></td>
								<td align="left"><input type="text" name="chart_no"
									size="30" value="<%=apptMainBean.getString(rs,"chart_no")%>">
								</td>
							</tr>
							<!-- start -->
							<%if (oscarProps.getProperty("EXTRA_DEMO_FIELDS") !=null){
                                 String fieldJSP = oscarProps.getProperty("EXTRA_DEMO_FIELDS");
                                 fieldJSP+= ".jsp";                                    
                            %>
							<jsp:include page="<%=fieldJSP%>">
								<jsp:param name="demo" value="<%=demographic_no%>" />
							</jsp:include>
							<%}%>

							<!-- end -->
							<%
                            if (vLocale.getCountry().equals("BR")) { %>
							<tr valign="top">
								<td align="right"><b></b></td>
								<td align="left"></td>
								<td align="right"><b><bean:message
									key="demographic.demographicaddrecordhtm.formChartAddress" />:
								</b></td>
								<td align="left"><input type="text" name="chart_address"
									value="<%=apptMainBean.getString(rs,"chart_address")==null?"":apptMainBean.getString(rs,"chart_address")%>">
								</td>
							</tr>
							<%}%>

							<tr valign="top">
								<td colspan="4">
								<table border="1" width="100%">
									<tr>
										<td align="right" width="16%" nowrap><b>Waiting
										List:</b></td>
										<td align="left" width="31%">
										<%
                                ResultSet rsWLStatus = apptMainBean.queryResults(demographic_no,"search_wlstatus");
 	                        String wlId="", listID="", wlnote=""; 
 	                        String wlReferralDate=""; 
                                if (rsWLStatus.next()){
                                    wlId = rsWLStatus.getString("id"); 
                                    listID = rsWLStatus.getString("listID");
                                    wlnote = rsWLStatus.getString("note");
                                    wlReferralDate = rsWLStatus.getString("onListSince"); 
                                    if(wlReferralDate != null  &&  wlReferralDate.length()>10){ 
                                        wlReferralDate = wlReferralDate.substring(0, 11);
                                    }
                                }
                                rsWLStatus.close();
                               %> <input type="hidden" name="wlId"
											value="<%=wlId%>"> <select name="list_id">
											<%if(wLReadonly.equals("")){%>
											<option value="0">--Select Waiting List--</option>
											<%}else{%>
											<option value="0">--Please Create Waiting List Name
											first--</option>
											<%} %>
											<%
                                      ResultSet rsWL = apptMainBean.queryResults("search_waiting_list");
                                      while (rsWL.next()) {
                                    %>
											<option value="<%=rsWL.getString("ID")%>"
												<%=rsWL.getString("ID").equals(listID)?" selected":""%>>
											<%=rsWL.getString("name")%></option>
											<%
                                      }
                                      rsWL.close();
                                    %>
										</select></td>
										<td align="right" nowrap><b>Waiting List Note: </b></td>
										<td align="left"><input type="text"
											name="waiting_list_note" value="<%=wlnote%>" size="34"
											<%=wLReadonly%>></td>
									</tr>
									<tr>
										<td colspan="2">&nbsp;</td>
										<td align="right" nowrap><b>Date of request: </b></td>
										<td align="left"><input type="text"
											name="waiting_list_referral_date"
											id="waiting_list_referral_date" size="11"
											value="<%=wlReferralDate%>" <%=wLReadonly%>><img
											src="../images/cal.gif" id="referral_date_cal">(yyyy-mm-dd)
										</td>

									</tr>
								</table>
								</td>
							</tr>

							<tr valign="top">
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formDateJoined1" />:
								</b></td>
								<td align="left">
								<%
                                 String date_joined = apptMainBean.getString(rs,"date_joined");
                                 String dateJoinedYear = "";
                                 String dateJoinedMonth = "";
                                 String dateJoinedDay = "";
                                 if( date_joined != null && date_joined.length() == 10 ) {
                                    // Format year
                                    decF.applyPattern("0000");
                                    dateJoinedYear = decF.format(MyDateFormat.getYearFromStandardDate(date_joined));
                                    decF.applyPattern("00");
                                    dateJoinedMonth = decF.format(MyDateFormat.getMonthFromStandardDate(date_joined));
                                    dateJoinedDay = decF.format(MyDateFormat.getDayFromStandardDate(date_joined));
                                 }
                              %> <input type="text"
									name="date_joined_year" size="4" maxlength="4"
									value="<%= dateJoinedYear %>"> <input type="text"
									name="date_joined_month" size="2" maxlength="2"
									value="<%= dateJoinedMonth %>"> <input type="text"
									name="date_joined_date" size="2" maxlength="2"
									value="<%= dateJoinedDay %>"></td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formEndDate" />: </b></td>
								<td align="left">
								<%
                                 // Format year
                                 decF.applyPattern("0000");
                                 String endYear = decF.format(MyDateFormat.getYearFromStandardDate(apptMainBean.getString(rs,"end_date")));
                                 decF.applyPattern("00");
                                 String endMonth = decF.format(MyDateFormat.getMonthFromStandardDate(apptMainBean.getString(rs,"end_date")));
                                 String endDay = decF.format(MyDateFormat.getDayFromStandardDate(apptMainBean.getString(rs,"end_date")));
                              %> <input type="text" name="end_date_year"
									size="4" maxlength="4" value="<%= endYear %>"> <input
									type="text" name="end_date_month" size="2" maxlength="2"
									value="<%= endMonth %>"> <input type="text"
									name="end_date_date" size="2" maxlength="2"
									value="<%= endDay %>"></td>
							</tr>
							<% // customized key
if(oscarVariables.getProperty("demographicExt") != null) {
    boolean bExtForm = oscarVariables.getProperty("demographicExtForm") != null ? true : false;
    String [] propDemoExtForm = bExtForm ? (oscarVariables.getProperty("demographicExtForm","").split("\\|") ) : null;
	String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
	for(int k=0; k<propDemoExt.length; k=k+2) {
%>
							<tr valign="top" bgcolor="#CCCCFF">
								<td align="right" nowrap><b><%=propDemoExt[k]%>: </b></td>
								<td align="left">
								<% if(bExtForm) {
                                  	if(propDemoExtForm[k].indexOf("<select")>=0) {
                                		out.println(propDemoExtForm[k].replaceAll("value=\""+apptMainBean.getString(demoExt.get(propDemoExt[k].replace(' ', '_')))+"\"" , "value=\""+apptMainBean.getString(demoExt.get(propDemoExt[k].replace(' ', '_')))+"\"" + " selected") );
                                  	} else {
                              			out.println(propDemoExtForm[k].replaceAll("value=\"\"", "value=\""+apptMainBean.getString(demoExt.get(propDemoExt[k].replace(' ', '_')))+"\"" ) );
                                  	}
                              	 } else { %> <input type="text"
									name="<%=propDemoExt[k].replace(' ', '_')%>"
									value="<%=apptMainBean.getString(demoExt.get(propDemoExt[k].replace(' ', '_')))%>" />
								<% }  %> <input type="hidden"
									name="<%=propDemoExt[k].replace(' ', '_')%>Orig"
									value="<%=apptMainBean.getString(demoExt.get(propDemoExt[k].replace(' ', '_')))%>" />
								</td>
								<% if((k+1)<propDemoExt.length) { %>
								<td align="right" nowrap><b>
								<%out.println(propDemoExt[k+1]+":");%> </b></td>
								<td align="left">
								<% if(bExtForm) {
                                  	if(propDemoExtForm[k+1].indexOf("<select")>=0) {
                                		out.println(propDemoExtForm[k+1].replaceAll("value=\""+apptMainBean.getString(demoExt.get(propDemoExt[k+1].replace(' ', '_')))+"\"" , "value=\""+apptMainBean.getString(demoExt.get(propDemoExt[k+1].replace(' ', '_')))+"\"" + " selected") );
                                  	} else {
                              			out.println(propDemoExtForm[k+1].replaceAll("value=\"\"", "value=\""+apptMainBean.getString(demoExt.get(propDemoExt[k+1].replace(' ', '_')))+"\"" ) );
                                  	}
                              	 } else { %> <input type="text"
									name="<%=propDemoExt[k+1].replace(' ', '_')%>"
									value="<%=apptMainBean.getString(demoExt.get(propDemoExt[k+1].replace(' ', '_')))%>" />
								<% }  %> <input type="hidden"
									name="<%=propDemoExt[k+1].replace(' ', '_')%>Orig"
									value="<%=apptMainBean.getString(demoExt.get(propDemoExt[k+1].replace(' ', '_')))%>" />
								</td>
								<% } else {%>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<% }  %>
							</tr>
							<% 	}
}
if(oscarVariables.getProperty("demographicExtJScript") != null) { out.println(oscarVariables.getProperty("demographicExtJScript")); }
%>

							<tr valign="top">
								<td nowrap colspan="4">
								<table width="100%" bgcolor="#EEEEFF">
									<tr>
										<td width="7%" align="right"><font color="#FF0000"><b><bean:message
											key="demographic.demographiceditdemographic.formAlert" />: </b></font></td>
										<td><textarea name="alert" style="width: 100%" cols="80"
											rows="2"><%=alert%></textarea></td>
									</tr>
									<tr>
										<td align="right"><b><bean:message
											key="demographic.demographiceditdemographic.formNotes" />: </b></td>
										<td><textarea name="notes" style="width: 100%" cols="60"><%=notes%></textarea>
										</td>
									</tr>
								</table>
								</td>
							</tr>

						</table>
						</td>
					</tr>
					<tr bgcolor="#CCCCFF">
						<td colspan="4">
						<table border=0 width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td width="30%" valign="top"><input type="hidden"
									name="dboperation" value="update_record"> <%
                                  if (vLocale.getCountry().equals("BR")) { %>
								<input type="hidden" name="dboperation2"
									value="update_record_ptbr"> <%}%> <input type="button"
									name="Button" value="<bean:message key="global.btnBack" />"
									onclick="history.go(-1);return false;"> <input
									type="button" name="Button"
									value="<bean:message key="global.btnCancel" />"
									onclick=self.close();> <input type="button" size="110"
									name="Button" value="Export This Demographic"
									onclick="window.open('demographicExport.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>');">
								</td>
								<td width="30%" align='center' valign="top"><input
									type="hidden" name="displaymode" value="Update Record">
								<!-- security code block --> <span id="updateButton"
									style="display: none;"> <security:oscarSec
									roleName="<%=roleName$%>" objectName="_demographic" rights="w">
									<input type="submit"
										value="<bean:message key="demographic.demographiceditdemographic.btnUpdate"/>">
								</security:oscarSec> <span> <!-- security code block --></td>
								<td width="40%" align='right' valign="top"><span
									id="swipeButton" style="display: none;"> <input
									type="button" name="Button"
									value="<bean:message key="demographic.demographiceditdemographic.btnSwipeCard"/>"
									onclick="window.open('zdemographicswipe.jsp','', 'scrollbars=yes,resizable=yes,width=600,height=300, top=360, left=0')">
								</span> <!--input type="button" name="Button" value="<bean:message key="demographic.demographiceditdemographic.btnSwipeCard"/>" onclick="javascript:window.alert('Health Card Number Already Inuse');"-->
								<input type="button" size="110" name="Button"
									value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFEnvelope"/>"
									onclick="window.location='../report/GenerateEnvelopes.do?demos=<%=apptMainBean.getString(rs,"demographic_no")%>'">
								<input type="button" size="110" name="Button"
									value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFLabel"/>"
									onclick="window.location='printDemoLabelAction.do?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>'">
								<input type="button" size="110" name="Button"
									value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFAddressLabel"/>"
									onclick="window.location='printDemoAddressLabelAction.do?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>'">
								<input type="button" size="110" name="Button"
									value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFChartLabel"/>"
									onclick="window.location='printDemoChartLabelAction.do?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>'">
								<input type="button" name="Button" size="110"
									value="<bean:message key="demographic.demographiceditdemographic.btnPrintLabel"/>"
									onclick="window.location='demographiclabelprintsetting.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>'">
								</td>
						</table>
						</form>
						</td>
					</tr>
				</table>
				<%
                    }
                  }
                  apptMainBean.closePstmtConn();
                %>
				
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>


<script type="text/javascript"> 
Calendar.setup({ inputField : "waiting_list_referral_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "referral_date_cal", singleClick : true, step : 1 }); 
</script>
</body>
</html:html>


