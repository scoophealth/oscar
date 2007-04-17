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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String demographic$ = request.getParameter("demographic_no") ;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>" >
<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<security:oscarSec roleName="<%=roleName$%>" objectName="<%="_demographic$"+demographic$%>" rights="o" reverse="<%=false%>" >
You have no rights to access the data!
<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<%@ page import="java.util.*, java.sql.*, java.net.*,java.text.DecimalFormat, oscar.*, oscar.oscarDemographic.data.ProvinceNames, oscar.oscarWaitingList.WaitingList" errorPage="../appointment/errorpage.jsp" %>
<%@page  import="oscar.oscarDemographic.data.*"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

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
   DemographicExt ext = new DemographicExt();
   ArrayList arr = ext.getListOfValuesForDemo(demographic_no);
   Hashtable demoExt = ext.getAllValuesForDemo(demographic_no);
%>



<html:html locale="true">
<head>
<title><bean:message key="demographic.demographiceditdemographic.title"/></title>
<html:base/>
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

<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<script language="javascript" type="text/javascript" src="../share/javascript/Oscar.js" ></script>

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

function refresh() {
  //history.go(0);
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



</script>

<style type="text/css">
div.demographicSection{
   //width:49%;
   width:100%;
   margin-top: 2px;
   margin-left:3px;
   border-top: 1px solid #ccccff;
   border-bottom: 1px solid #ccccff;
   border-left: 1px solid #ccccff;
   border-right: 1px solid #ccccff;
   float: left;
}

div.demographicSection h3 {
   background-color: #ccccff;
   font-size: 8pt;
   font-variant:small-caps;
   font:bold;
   margin-top:0px;
   padding-top:0px;
   margin-bottom:0px;
   padding-bottom:0px;
}

div.demographicSection ul{

       list-style:none;
       list-style-type:none;
       list-style-position:outside;
       padding-left:1px;
       margin-left:1px;
       margin-top:0px;
       padding-top:1px;
       margin-bottom:0px;
       padding-bottom:0px;
}


div.demographicSection li {
padding-right: 15px;
white-space: nowrap;
}


div.demographicWrapper {
  background-color: #eeeeff;
  margin-top: 5px;
  margin-left:1px;
  margin-right:1px;
}
</style>

</head>
<body onLoad="setfocus(); checkONReferralNo(); formatPhoneNum();" topmargin="0" leftmargin="0" rightmargin="0">
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="demographic.demographiceditdemographic.msgPatientDetailRecord"/>
            </td>
            <td class="MainTableTopRowRightColumn" width="400">
                <table class="TopStatusBar" >
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
                                        resident = s(rs.getString("cust1"));
                                        nurse = s(rs.getString("cust2"));
                                        alert = s(rs.getString("cust3"));
                                        midwife = s(rs.getString("cust4"));
                                        notes = SxmlMisc.getXmlContent(rs.getString("content"),"unotes") ;
                                        notes = notes==null?"":notes;
                                }

                                GregorianCalendar now=new GregorianCalendar();
                                int curYear = now.get(Calendar.YEAR);
                                int curMonth = (now.get(Calendar.MONTH)+1);
                                int curDay = now.get(Calendar.DAY_OF_MONTH);
                                String dateString = curYear+"-"+curMonth+"-"+curDay;
                                int age=0, dob_year=0, dob_month=0, dob_date=0;

                                int param = Integer.parseInt(demographic_no);

                                rs = apptMainBean.queryResults(param, request.getParameter("dboperation"));
                                if(rs==null) {
                                        out.println("failed!!!");
                                } else {
                                        while (rs.next()) {
                                                //----------------------------REFERRAL DOCTOR------------------------------
                                                fd=rs.getString("family_doctor");
                                                if (fd==null) {
                                                        rd = "";
                                                        rdohip="";
                                                        family_doc = "";
                                                }else{
                                                        rd = SxmlMisc.getXmlContent(rs.getString("family_doctor"),"rd")    ;
                                                        rd = rd !=null ? rd : "" ;
                                                        rdohip = SxmlMisc.getXmlContent(rs.getString("family_doctor"),"rdohip");
                                                        rdohip = rdohip !=null ? rdohip : "" ;
                                                        family_doc = SxmlMisc.getXmlContent(rs.getString("family_doctor"),"family_doc");
                                                        family_doc = family_doc !=null ? family_doc : "" ;
                                                }
                                                //----------------------------REFERRAL DOCTOR --------------end-----------

                                                dob_year = Integer.parseInt(rs.getString("year_of_birth"));
                                                dob_month = Integer.parseInt(rs.getString("month_of_birth"));
                                                dob_date = Integer.parseInt(rs.getString("date_of_birth"));
                                                if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
                        %>
                        <%=rs.getString("last_name")%>, <%=rs.getString("first_name")%> <%=rs.getString("sex")%> <%=age%> years
                        <span style="margin-left:20px;"><i>Next Appointment: <oscar:nextAppt demographicNo="<%=rs.getString("demographic_no")%>"/></i></span>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
            <table border=0 cellspacing=0 width="100%">
                <tr class="Header">
                    <td style="font-weight:bold">
                        Appointment
                    </td>
                </tr>
                <tr><td>
                    <a href='demographiccontrol.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25'><bean:message key="demographic.demographiceditdemographic.btnApptHist"/></a>
                </td></tr>
                <%
                    WaitingList wL = WaitingList.getInstance();
                    if(wL.getFound()){
                %>
                <tr><td>
                    <a href="../oscarWaitingList/SetupDisplayPatientWaitingList.do?demographic_no=<%=rs.getString("demographic_no")%>">Waiting List</a>
                </td></tr>
                <%}%>
                <tr class="Header">
                    <td style="font-weight:bold">
                        <bean:message key="admin.admin.billing"/>
                    </td>
                </tr>
                <tr><td>
                    <% if (vLocale.getCountry().equals("BR")) { %>
                    <!--a href="javascript: function myFunction() {return false; }" onClick="popupPage(500,600,'../billing/billinghistory.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10')">Billing History</a-->
                    <a href='../oscar/billing/consultaFaturamentoPaciente/init.do?demographic_no=<%=rs.getString("demographic_no")%>'>Hist&oacute;rico do Faturamento</a></th>
                    <% } else if("ON".equals(prov)) {%>
                    <a href="javascript: function myFunction() {return false; }" onClick="popupPage(500,600,'../billing/CA/ON/billinghistory.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10')">Billing History</a>
                    <%}else{%>  
                    <a href="#" onclick ="popupPage(800,1000,'../billing/CA/BC/billStatus.jsp?lastName=<%=URLEncoder.encode(rs.getString("last_name"))%>&firstName=<%=URLEncoder.encode(rs.getString("first_name"))%>&filterPatient=true&demographicNo=<%=rs.getString("demographic_no")%>');return false;">Invoice List</a>
                    <%}%>
                </td></tr>
                <% if (!vLocale.getCountry().equals("BR")) { %>
                <tr><td>
                    <a href="javascript: function myFunction() {return false; }" onClick="popupPage(700, 1000, '../billing.do?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=&appointment_no=0&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&demographic_no=<%=rs.getString("demographic_no")%>&providerview=1&user_no=<%=curProvider_no%>&apptProvider_no=none&appointment_date=<%=dateString%>&start_time=0:00&bNewForm=1&status=t');return false;" title="bill a patient">Create Invoice</a>
                </td></tr>
                <%    if("ON".equals(prov)) {
                	       String default_view = oscarVariables.getProperty("default_view", "");
                %>
                   <%    if (!oscarProps.getProperty("clinic_no", "").startsWith("1022")) { // part 2 of quick hack to make Dr. Hunter happy %>
                         <tr><td>
                         <a href="javascript: function myFunction() {return false; }" onClick="window.open('../billing/CA/ON/specialtyBilling/fluBilling/addFluBilling.jsp?function=demographic&functionid=<%=rs.getString("demographic_no")%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&hin=<%=URLEncoder.encode(rs.getString("hin"))%><%=URLEncoder.encode(rs.getString("ver"))%>&demo_sex=<%=URLEncoder.encode(rs.getString("sex"))%>&demo_hctype=<%=URLEncoder.encode(rs.getString("hc_type")==null?"null":rs.getString("hc_type"))%>&rd=<%=URLEncoder.encode(rd==null?"null":rd)%>&rdohip=<%=URLEncoder.encode(rdohip==null?"null":rdohip)%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(rs.getString("year_of_birth")),Integer.parseInt(rs.getString("month_of_birth")),Integer.parseInt(rs.getString("date_of_birth")))%>','', 'scrollbars=yes,resizable=yes,width=720,height=500');return false;" title='Add Flu Billing'>Flu Billing</a>
                         </td></tr>
                      <% } %>
                      <tr><td>
                      <a href="javascript: function myFunction() {return false; }" onClick="popupS('../billing/CA/ON/billingShortcutPg1.jsp?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("hospital_view", default_view))%>&hotclick=&appointment_no=0&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&demographic_no=<%=rs.getString("demographic_no")%>&providerview=1&user_no=<%=curProvider_no%>&apptProvider_no=none&appointment_date=<%=dateString%>&start_time=0:00&bNewForm=1&status=t');return false;" title="bill a patient">Hospital Billing</a>
                      </td></tr>

                      <tr><td>
                      <a href="javascript: function myFunction() {return false; }" onClick="window.open('../billing/CA/ON/inr/addINRbilling.jsp?function=demographic&functionid=<%=rs.getString("demographic_no")%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&hin=<%=URLEncoder.encode(rs.getString("hin"))%><%=URLEncoder.encode(rs.getString("ver"))%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(rs.getString("year_of_birth")),Integer.parseInt(rs.getString("month_of_birth")),Integer.parseInt(rs.getString("date_of_birth")))%>','', 'scrollbars=yes,resizable=yes,width=600,height=400');return false;" title='Add INR Billing'>Add INR</a></th>
                      </td></tr>
                      <tr><td>
                      <a href="javascript: function myFunction() {return false; }" onClick="window.open('../billing/CA/ON/inr/reportINR.jsp?provider_no=<%=curProvider_no%>','', 'scrollbars=yes,resizable=yes,width=600,height=600');return false;" title='INR Billing'>Bill INR</a></th>
                      </td></tr>
                  <%  } %>
                <% } %>
                <tr class="Header">
                    <td style="font-weight:bold">
                        <bean:message key="oscarEncounter.Index.clinicalModules"/>
                    </td>
                </tr>
                <tr><td>
                    <a href="javascript: function myFunction() {return false; }" onClick="popupPage(700,960,'../oscarEncounter/oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp?de=<%=rs.getString("demographic_no")%>&proNo=<%=rs.getString("provider_no")%>')"><bean:message key="demographic.demographiceditdemographic.btnConsultation"/></a>
                </td></tr>
                <% if (!vLocale.getCountry().equals("BR")) { %>
                <tr><td>
                    <a href="javascript: function myFunction() {return false; }" onClick="popupOscarRx(700,960,'../oscarRx/choosePatient.do?providerNo=<%=curProvider_no%>&demographicNo=<%=demographic_no%>')"><bean:message key="global.prescriptions"/></a></th>
                </td></tr>
                <% } %>
                <tr><td>
                    <!--a href=# onclick="popupPage(600,800,'../provider/providercontrol.jsp?appointment_no=&demographic_no=<%=demographic_no%>&curProvider_no=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&username=&appointment_date=&start_time=&status=&displaymode=encounter&dboperation=search_demograph&template=');return false;" title="Tel-Progress Notes">Add Encounter</a-->
                    <a href="javascript: function myFunction() {return false; }" onClick="popupEChart(700, 1048,'../oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo=<%=demographic_no%>&curProviderNo=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&userName=<%=URLEncoder.encode( userfirstname+" "+userlastname) %>&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=');return false;" title="<bean:message key="demographic.demographiceditdemographic.btnEChart"/>">
                    <bean:message key="demographic.demographiceditdemographic.btnEChart"/></a>
                </td></tr>
                <tr><td>
                    <a href="javascript: function myFunction() {return false; }" onClick="popupPage(700,1000,'../tickler/ticklerDemoMain.jsp?demoview=<%=demographic_no%>');return false;" >
                    <bean:message key="global.tickler"/></a>
                </td></tr>
                <tr><td>
                    <a href="javascript: function myFunction() {return false; }" onClick="popup(700,960,'../oscarMessenger/SendDemoMessage.do?demographic_no=<%=rs.getString("demographic_no")%>','msg')">Send a Message</a>
                </td></tr>
                <% if (oscarProps.getProperty("clinic_no", "").startsWith("1022")) { // quick hack to make Dr. Hunter happy %>
                <tr><td>
                    <a href="javascript: function myFunction() {return false; }" onClick="popupPage(700,1000,'../form/forwardshortcutname.jsp?formname=AR1&demographic_no=<%=request.getParameter("demographic_no")%>');">AR1</a>
                </td></tr>
                <tr><td>
                    <a href="javascript: function myFunction() {return false; }" onClick="popupPage(700,1000,'../form/forwardshortcutname.jsp?formname=AR2&demographic_no=<%=request.getParameter("demographic_no")%>');">AR2</a>
                </td></tr>
                <% } %>
                <tr class="Header">
                    <td style="font-weight:bold">
                        <bean:message key="oscarEncounter.Index.clinicalResources"/>
                    </td>
                </tr>
                <tr><td>
                    <!--th><a href="javascript: function myFunction() {return false; }" onClick="popupPage(500,600,'demographicsummary.jsp?demographic_no=<%=rs.getString("demographic_no")%>')">Patient Summary</a> </th-->
                    <a href="javascript: function myFunction() {return false; }" onClick="popupPage(710,970,'../dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=rs.getString("demographic_no")%>&curUser=<%=curProvider_no%>')"><bean:message key="demographic.demographiceditdemographic.msgDocuments"/></a>
                </td></tr>
                <tr><td>
                    <a href="javascript: function myFunction() {return false; }" onClick="popupPage(710,970,'../dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=rs.getString("demographic_no")%>&curUser=<%=curProvider_no%>&mode=add')"><bean:message key="demographic.demographiceditdemographic.btnAddDocument"/></a>
                </td></tr>
                <tr><td>
                    <a href="../eform/efmpatientformlist.jsp?demographic_no=<%=demographic_no%>"><bean:message key="demographic.demographiceditdemographic.btnEForm"/></a>
                </td></tr>
                <tr><td>
                    <a href="../eform/efmformslistadd.jsp?demographic_no=<%=demographic_no%>" > <bean:message key="demographic.demographiceditdemographic.btnAddEForm"/> </a>
                </td></tr>
            </table>
            </td>
            <td class="MainTableRightColumn" valign="top">
                <table border=0 cellspacing=4 width="100%">
                    <tr>
                        <td colspan="4">
                            <%-- log:info category="Demographic">Demographic [<%=demographic_no%>] is viewed by User [<%=userfirstname%> <%=userlastname %>]  </log:info --%>
                            <%@ include file="zdemographicfulltitlesearch.jsp" %>
                        </td>
                    </tr>
                    <tr>
                    <td>
                    <form method="post" name="updatedelete" id="updatedelete" action="demographiccontrol.jsp" onSubmit="return checkTypeInEdit();">
                    <input type="hidden" name="demographic_no" value="<%=rs.getString("demographic_no")%>">
                    <tr><td>
                    <table width="100%" bgcolor="#CCCCFF" cellspacing="1" cellpadding="1">
                        <tr><td class="RowTop">
                            <b>Record</b> (<%=rs.getString("demographic_no")%>) <a href="javascript: showHideDetail();">Edit</a>
                        </td></tr>
                        <tr><td bgcolor="#eeeeff">


                       <!---new-->
                       <div style="background-color: #EEEEFF;" id="viewDemographics2">
                          <div class="demographicWrapper" style="background-color: #EEEEFF;" >
                             <div style="width:49%;float:left;">
                                    <div class="demographicSection" style="margin-top: 2px;">
                                        <h3>&nbsp;Demographics</h3>
                                        <div style="background-color: #EEEEFF;" >
                                        <ul>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formLastName"/>: <b><%=rs.getString("last_name")%></b>
                                                <bean:message key="demographic.demographiceditdemographic.formFirstName"/>: <b> <%=rs.getString("first_name")%></b>
                                            </li>
                                            <li>
                                                Age:<b><%=age%></b> &nbsp;
                                                <bean:message key="demographic.demographiceditdemographic.formDOB"/>:<b>(<%=rs.getString("year_of_birth")%>-<%=rs.getString("month_of_birth")%>-<%=rs.getString("date_of_birth")%>)</b>
                                            </li>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formSex"/>:<b><%=rs.getString("sex")%></b>
                                            </li>
                                            <li>
                                                Language: <b><%=s(demoExt.get("language"))%></b>
                                            </li>
                                        </ul>
                                        </div>
                                    </div>

                                    <div class="demographicSection">
                                        <h3>&nbsp;Other Contacts: <b><a href="javascript: function myFunction() {return false; }" onClick="popup(700,960,'AddAlternateContact.jsp?demo=<%=rs.getString("demographic_no")%>','AddRelation')">Add Relation<!--i18n--></a></b></h3>
                                        <div style="background-color: #EEEEFF;" >
                                        <ul>
                                        <%DemographicRelationship demoRelation = new DemographicRelationship();
                                          ArrayList relList = demoRelation.getDemographicRelationshipsWithNamePhone(rs.getString("demographic_no"));
                                          for (int reCounter = 0; reCounter < relList.size(); reCounter++){
                                             Hashtable relHash = (Hashtable) relList.get(reCounter);
                                             String sdb = relHash.get("subDecisionMaker") == null?"":((Boolean) relHash.get("subDecisionMaker")).booleanValue()?"<span title=\"SDM\" >/SDM</span>":"";
                                             String ec = relHash.get("emergencyContact") == null?"":((Boolean) relHash.get("emergencyContact")).booleanValue()?"<span title=\"Emergency Contact\">/EC</span>":"";

                                          %>
                                            <li>
                                            <b><%=relHash.get("relation")%><%=sdb%><%=ec%>: </b><%=relHash.get("lastName")%>, <%=relHash.get("firstName")%> ,<%=relHash.get("phone")%>
                                            </li>
                                        <%}%>

                                        </ul>
                                        </div>
                                    </div>

                                    <div class="demographicSection">
                                        <h3>&nbsp;Clinic Status</h3>
                                        <div style="background-color: #EEEEFF;" >
                                        <ul>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formRosterStatus"/>: <b><%=rs.getString("roster_status")%></b>
                                                <bean:message key="demographic.demographiceditdemographic.DateJoined"/>: <b><%=rs.getString("hc_renew_date")%></b>
                                            </li>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formPatientStatus"/>:<b><%=rs.getString("patient_status")%></b>
                                            </li>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formChartNo"/>:<b><%=rs.getString("chart_no")%></b>
                                            </li>
                                             <li>
                                                <bean:message key="demographic.demographiceditdemographic.cytolNum"/>: <b> <%=s(demoExt.get("cytolNum"))%></b>
                                            </li>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formDateJoined1"/>: <b><%=rs.getString("date_joined")%></b>
                                                <bean:message key="demographic.demographiceditdemographic.formEndDate"/>: <b><%=rs.getString("end_date")%></b>
                                            </li>
                                        </ul>
                                        </div>
                                    </div>

                                    <div class="demographicSection">
                                        <h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.formAlert"/></h3>
                                        <div style="background-color: #EEEEFF;" >
                                        <%=alert%> &nbsp;
                                    </div>
                                </div>

                             </div>
                             <div style="width:49%;float:left;margin-left:5px;">
                                <div class="demographicSection" style="margin-top: 2px;">
                                    <h3>&nbsp;Contact Information</h3>
                                    <div style="background-color: #EEEEFF;" >
                                    <ul>
                                        <li>
                                            <bean:message key="demographic.demographiceditdemographic.formPhoneH"/>:<b><%=rs.getString("phone")%> <%=s(demoExt.get("hPhoneExt"))%></b>
                                            <bean:message key="demographic.demographiceditdemographic.formPhoneW"/>:<b> <%=rs.getString("phone2")%> <%=s(demoExt.get("wPhoneExt"))%></b>


                                        </li>
                                        <li>
                                        <bean:message key="demographic.demographiceditdemographic.formPhoneC"/>:<b> <%=s(demoExt.get("demo_cell"))%></b>
                                        </li>
                                        <li>
                                            <bean:message key="demographic.demographiceditdemographic.formAddr"/>: <b><%=rs.getString("address")%></b>
                                        </li>
                                        <li>
                                            <bean:message key="demographic.demographiceditdemographic.formCity"/>: <b><%=rs.getString("city")%></b>
                                        </li>
                                        <li>
										<% if(oscarProps.getProperty("demographicLabelProvince") == null) { %>
                                            <bean:message key="demographic.demographiceditdemographic.formProcvince"/>
										<% } else {
			                                  out.print(oscarProps.getProperty("demographicLabelProvince"));
										   } %>
										   : <b> <%=rs.getString("province")%></b>
                                        </li>
                                        <li>
										<% if(oscarProps.getProperty("demographicLabelPostal") == null) { %>
                                            <bean:message key="demographic.demographiceditdemographic.formPostal"/>
										<% } else {
			                                  out.print(oscarProps.getProperty("demographicLabelPostal"));
										   } %>
										   : <b> <%=rs.getString("postal")%></b>
                                        </li>

                                        <li>
                                            <bean:message key="demographic.demographiceditdemographic.formEmail"/>: <b> <%=rs.getString("email")!=null? rs.getString("email") : ""%></b>
                                        </li>
                                    </ul>
                                    </div>
                                </div>

                                <div class="demographicSection">
                                    <h3>&nbsp;Health Insurance</h3>
                                    <div style="background-color: #EEEEFF;" >
                                    <ul>
                                        <li>
                                          <bean:message key="demographic.demographiceditdemographic.formHin"/>: <b><%=rs.getString("hin")%> &nbsp; <%=rs.getString("ver")%></b>
                                          <bean:message key="demographic.demographiceditdemographic.formHCType"/>:<b><%=rs.getString("hc_type")==null?"":rs.getString("hc_type") %></b>
                                        </li>
                                        <li>
                                          <bean:message key="demographic.demographiceditdemographic.formEFFDate"/>:<b><%=rs.getString("eff_date")%></b>
                                        </li>
                                    </ul>
                                    </div>
                                </div>

                                <div class="demographicSection">
                                        <h3>&nbsp;Patient Clinic Status</h3>
                                        <div style="background-color: #EEEEFF;" >
                                        <ul>
                                            <li>
                                                <% if(oscarProps.getProperty("demographicLabelDoctor") != null) { out.print(oscarProps.getProperty("demographicLabelDoctor","")); } else { %>
                                                <bean:message key="demographic.demographiceditdemographic.formDoctor"/><% } %>: <b><%=providerBean.getProperty(rs.getString("provider_no"),"")%></b>
                                            </li>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formNurse"/>: <b><%=providerBean.getProperty(resident,"")%></b>
                                            </li>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formMidwife"/>: <b><%=providerBean.getProperty(midwife,"")%></b>
                                            </li>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formResident"/>:<b> <%=providerBean.getProperty(nurse,"")%></b>
                                            </li>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formRefDoc"/>: <b><%=rd%></b>
                                            </li>
                                            <li>
                                                <bean:message key="demographic.demographiceditdemographic.formRefDocNo"/>: <b><%=rdohip%></b>
                                            </li>
                                        </ul>
                                        </div>
                                </div>


                                <div class="demographicSection">
                                    <h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.formNotes"/></h3>
                                    <div style="background-color: #EEEEFF;" >
                                    <%=notes%> &nbsp;
                                    </div>
                                </div>
                             </div>
                            </div>

<% // customized key
if(oscarVariables.getProperty("demographicExt") != null) {
	String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
%>
                                <div class="demographicSection">
                                    <h3>&nbsp;Special</h3>
                                    <div style="background-color: #EEEEFF;" >
<% 	for(int k=0; k<propDemoExt.length; k++) {%>
                                    <%=propDemoExt[k]+": <b>" + s(demoExt.get(propDemoExt[k].replace(' ', '_')))%> </b>&nbsp;<%=((k+1)%4==0&&(k+1)<propDemoExt.length)?"<br>":"" %>
<% 	} %>
                                    </div>
                                </div>
<% } %>

                        </div>




                       <!--newEnd-->

                        <table width="100%" bgcolor="#EEEEFF" border=0 id="editDemographic" style="display:none;">
                            <tr>
                              <td align="right" title='<%=rs.getString("demographic_no")%>'> <b><bean:message key="demographic.demographiceditdemographic.formLastName"/>: </b></td>
                              <td align="left">
                                <input type="text" name="last_name" size="30" value="<%=rs.getString("last_name")%>" onBlur="upCaseCtrl(this)">
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formFirstName"/>: </b> </td>
                              <td align="left">
                                <input type="text" name="first_name" size="30" value="<%=rs.getString("first_name")%>" onBlur="upCaseCtrl(this)">
                              </td>
                            </tr>

                            <%
                            if (vLocale.getCountry().equals("BR")) { %>
                            <tr>
                              <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formRG"/>:</b></td>
                              <td align="left">
                                <input type="text" name="rg" value="<%=rs.getString("rg")==null?"":rs.getString("rg")%>" onBlur="upCaseCtrl(this)">
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formCPF"/>:</b> </td>
                              <td align="left">
                                <input type="text" name="cpf" value="<%=rs.getString("cpf")==null?"":rs.getString("cpf")%>" onBlur="upCaseCtrl(this)">
                              </td>
                            </tr>
                            <tr>
                              <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formMaritalState"/>:</b></td>
                              <td align="left">
                                <select name="marital_state">
                                    <option value="-">-</option>
                                        <option value="S" <%if (rs.getString("marital_state").trim().equals("S")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optSingle"/></option>
                                        <option value="M" <%if (rs.getString("marital_state").trim().equals("M")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optMarried"/></option>
                                        <option value="R" <%if (rs.getString("marital_state").trim().equals("R")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optSeparated"/></option>
                                        <option value="D" <%if (rs.getString("marital_state").trim().equals("D")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optDivorced"/></option>
                                        <option value="W" <%if (rs.getString("marital_state").trim().equals("W")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optWidower"/></option>
                                </select>
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formBirthCertificate"/>:</b> </td>
                              <td align="left">
                                <input type="text" name="birth_certificate" value="<%=rs.getString("birth_certificate")==null?"":rs.getString("birth_certificate")%>" onBlur="upCaseCtrl(this)">
                              </td>
                            </tr>
                            <tr>
                              <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formMarriageCertificate"/>:</b></td>
                              <td align="left">
                                <input type="text" name="marriage_certificate" value="<%=rs.getString("marriage_certificate")==null?"":rs.getString("marriage_certificate")%>" onBlur="upCaseCtrl(this)">
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPartnerName"/>:</b> </td>
                              <td align="left">
                                <input type="text" name="partner_name" value="<%=rs.getString("partner_name")==null?"":rs.getString("partner_name")%>" onBlur="upCaseCtrl(this)">
                              </td>
                            </tr>
                            <tr>
                              <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formFatherName"/>:</b></td>
                              <td align="left">
                                <input type="text" name="father_name" value="<%=rs.getString("father_name")==null?"":rs.getString("father_name")%>" onBlur="upCaseCtrl(this)">
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formMotherName"/>:</b> </td>
                              <td align="left">
                                <input type="text" name="mother_name" value="<%=rs.getString("mother_name")==null?"":rs.getString("mother_name")%>" onBlur="upCaseCtrl(this)">
                              </td>
                            </tr>
                            <%}%>
                            <tr valign="top">
                              <td  align="right"> <b><bean:message key="demographic.demographiceditdemographic.formAddr"/>: </b></td>
                              <td align="left" >
                                <input type="text" name="address" size="30" value="<%=rs.getString("address")%>"><% if (vLocale.getCountry().equals("BR")) { %>
                                <b><bean:message key="demographic.demographicaddrecordhtm.formAddressNo"/>:</b>
                                <input type="text" name="address_no" size="30" value="<%=rs.getString("address_no")==null?"":rs.getString("address_no")%>" size="6">
                                <%}%>
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formCity"/>: </b></td>
                              <td align="left">
                                <input type="text" name="city" size="30" value="<%=rs.getString("city")%>">
                              </td>
                            </tr>
                            <% if (vLocale.getCountry().equals("BR")) { %>
                            <tr valign="top">
                              <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formComplementaryAddress"/>: </b> </td>
                              <td  align="left">
                                <input type="text" name="complementary_address" value="<%=rs.getString("complementary_address")==null?"":rs.getString("complementary_address")%>" onBlur="upCaseCtrl(this)">
                              </td>
                              <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formDistrict"/>: </b> </td>
                              <td  align="left">
                                <input type="text" name="district" value="<%=rs.getString("district")==null?"":rs.getString("district")%>" onBlur="upCaseCtrl(this)">
                              </td>
                            </tr>
                            <%}%>
                            <tr valign="top">
                              <td align="right"><b>
							  <% if(oscarProps.getProperty("demographicLabelProvince") == null) { %>
                              <bean:message key="demographic.demographiceditdemographic.formProcvince"/>
                              <% } else {
                                  out.print(oscarProps.getProperty("demographicLabelProvince"));
                              	 } %>
                              :
                              </b></td>
                              <td  align="left">
                                <% if (vLocale.getCountry().equals("BR")) { %>
                                <input type="text" name="province" value="<%=rs.getString("province")%>">
                                <% } else { %>
                                <% String province = rs.getString("province"); %>
                                <select name="province" style="width:200px">
                                  <option value="OT"<%=(province.equals("OT") || province.equals("") || province.length() > 2)?" selected":""%>>Other</option>
                                <% if (pNames.isDefined()) {
                                       for (ListIterator li = pNames.listIterator(); li.hasNext(); ) {
                                           String pr2 = (String) li.next(); %>
                                           <option value="<%=pr2%>"<%=pr2.equals(province)?" selected":""%>><%=li.next()%></option>
										<% } %>
                                    <% } else { %>
                                      <option value="AB"<%=province.equals("AB")?" selected":""%>>AB-Alberta</option>
                                      <option value="BC"<%=province.equals("BC")?" selected":""%>>BC-British Columbia</option>
                                      <option value="MB"<%=province.equals("MB")?" selected":""%>>MB-Manitoba</option>
                                      <option value="NB"<%=province.equals("NB")?" selected":""%>>NB-New Brunswick</option>
                                      <option value="NL"<%=province.equals("NL")?" selected":""%>>NL-Newfoundland & Labrador</option>
                                      <option value="NT"<%=province.equals("NT")?" selected":""%>>NT-Northwest Territory</option>
                                      <option value="NS"<%=province.equals("NS")?" selected":""%>>NS-Nova Scotia</option>
                                      <option value="NU"<%=province.equals("NU")?" selected":""%>>NU-Nunavut</option>
                                      <option value="ON"<%=province.equals("ON")?" selected":""%>>ON-Ontario</option>
                                      <option value="PE"<%=province.equals("PE")?" selected":""%>>PE-Prince Edward Island</option>
                                      <option value="QC"<%=province.equals("QC")?" selected":""%>>QC-Quebec</option>
                                      <option value="SK"<%=province.equals("SK")?" selected":""%>>SK-Saskatchewan</option>
                                      <option value="YT"<%=province.equals("YT")?" selected":""%>>YT-Yukon</option>
                                      <option value="US"<%=province.equals("US")?" selected":""%>>US resident</option>
                                    <% } %>
                                </select>
                                <% } %>
                              </td>
                              <td  align="right"><b>
							  <% if(oscarProps.getProperty("demographicLabelPostal") == null) { %>
                              <bean:message key="demographic.demographiceditdemographic.formPostal"/>
                              <% } else {
                                  out.print(oscarProps.getProperty("demographicLabelPostal"));
                              	 } %>
                              :
                              </b> </td>
                              <td  align="left">
                                <input type="text" name="postal" size="30" value="<%=rs.getString("postal")%>" onBlur="upCaseCtrl(this)">
                              </td>
                            </tr>
                            <tr valign="top">
                              <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formPhoneH"/>: </b> </td>
                              <td align="left" >
                                <%-- // <input type="text" name="phone" size="30" value="<%=rs.getString("phone")!=null && rs.getString("phone").length()==10?rs.getString("phone").substring(0,3) + "-" + rs.getString("phone").substring(3,6) +"-"+  rs.getString("phone").substring(6):rs.getString("phone")%>">--%>
                                <input type="text" name="phone"  onblur="formatPhoneNum();" style="display:inline;width:auto;" value="<%=rs.getString("phone")%>">
                                Ext:<input type="text" name="hPhoneExt" value="<%=s(demoExt.get("hPhoneExt"))%>"  size="4" />
                                <input type="hidden" name="hPhoneExtOrig" value="<%=s(demoExt.get("hPhoneExt"))%>" />
                              </td>
                              <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formPhoneW"/>:</b> </td>
                              <td  align="left">
                                <input type="text" name="phone2"  onblur="formatPhoneNum();" style="display:inline;width:auto;" value="<%=rs.getString("phone2")%>">
                                Ext:<input type="text" name="wPhoneExt" value="<%=s(demoExt.get("wPhoneExt"))%>"  style="display:inline" size="4" />
                                <input type="hidden" name="wPhoneExtOrig" value="<%=s(demoExt.get("wPhoneExt"))%>" />
                              </td>
                            </tr>
                             <tr valign="top">
                              <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formPhoneC"/>: </b> </td>
                              <td align="left" >
                                <%-- // <input type="text" name="phone" size="30" value="<%=rs.getString("phone")!=null && rs.getString("phone").length()==10?rs.getString("phone").substring(0,3) + "-" + rs.getString("phone").substring(3,6) +"-"+  rs.getString("phone").substring(6):rs.getString("phone")%>">--%>
                                <input type="text" name="demo_cell"  onblur="formatPhoneNum();" style="display:inline;width:auto;" value="<%=s(demoExt.get("demo_cell"))%>">
                                <input type="hidden" name="demo_cellOrig" value="<%=s(demoExt.get("demo_cell"))%>" />
                              </td>
                            </tr>
                            <tr valign="top">
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formEmail"/>: </b> </td>
                              <td  align="left">
                                <input type="text" name="email" size="30" value="<%=rs.getString("email")!=null? rs.getString("email") : ""%>">
                              </td>
                              <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formPIN"/>: </b> </td>
                              <td  align="left">
                                <input type="text" name="pin" size="30" value="<%=rs.getString("pin")!=null? rs.getString("pin") : ""%>" >
                              </td>
                            </tr>
                            <tr valign="top">
                              <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formDOB"/></b><bean:message key="demographic.demographiceditdemographic.formDOBDetais"/><b>:</b> </td>
                              <td  align="left" nowrap>
                                <input type="text" name="year_of_birth" value="<%=rs.getString("year_of_birth")%>" size="3" maxlength="4">
                                <input type="text" name="month_of_birth" value="<%=rs.getString("month_of_birth")%>" size="2" maxlength="2">
                                <input type="text" name="date_of_birth" value="<%=rs.getString("date_of_birth")%>" size="2" maxlength="2">
                                <b>Age:
                                <input type="text" name="age" readonly value="<%=age%>" size="3">
                                </b> </td>
                              <td  align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formSex"/>:</b> </td>
                              <td align="left" valign="top">
                                <input type="text" name="sex" style="width:20px;" value="<%=rs.getString("sex")%>" onBlur="upCaseCtrl(this)" size="1" maxlength="1">
                                <b>Language:</b>
                                    <input type="text" name="language" value="<%=s(demoExt.get("language"))%>" onBlur="upCaseCtrl(this)" size="19" />
                                    <input type="hidden" name="languageOrig" value="<%=s(demoExt.get("language"))%>" />
                              </td>
                            </tr>
                            <tr valign="top">
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formHin"/>: </b></td>
                              <td align="left" nowrap>
                                <input type="text" name="hin" value="<%=rs.getString("hin")%>" size="17">
                                <b><bean:message key="demographic.demographiceditdemographic.formVer"/></b>
                                <input type="text" name="ver" value="<%=rs.getString("ver")%>" size="3"  onBlur="upCaseCtrl(this)">
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formEFFDate"/>:</b></td>
                              <td align="left">
                              <%
                                 // Put 0 on the left on dates
                                 DecimalFormat decF = new DecimalFormat();
                                 // Year
                                 decF.applyPattern("0000");
                                 String effDateYear = decF.format(MyDateFormat.getYearFromStandardDate(rs.getString("eff_date")));
                                 // Month and Day
                                 decF.applyPattern("00");
                                 String effDateMonth = decF.format(MyDateFormat.getMonthFromStandardDate(rs.getString("eff_date")));
                                 String effDateDay = decF.format(MyDateFormat.getDayFromStandardDate(rs.getString("eff_date")));
                              %>
                                <input type="text" name="eff_date_year" size="4" maxlength="4" value="<%= effDateYear%>">
                                <input type="text" name="eff_date_month" size="2" maxlength="2" value="<%= effDateMonth%>">
                                <input type="text" name="eff_date_date" size="2" maxlength="2" value="<%= effDateDay%>">
                              </td>
                            </tr>
                            <tr valign="top">
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formHCType"/>:</b> </td>
                              <td align="left" >
                                <% if(vLocale.getCountry().equals("BR")) { %>
                                   <% String hctype = rs.getString("hc_type")==null?"":rs.getString("hc_type"); %>
                                   <input type="text" name="hc_type" value="<%=hctype%>">
                                <% } else {%>
                                <% String hctype = rs.getString("hc_type")==null?"":rs.getString("hc_type"); %>
                                <select name="hc_type" style="width:200px">
                                <option value="OT"<%=(hctype.equals("OT") || hctype.equals("") || hctype.length() > 2)?" selected":""%>>Other</option>
                                <% if (pNames.isDefined()) {
                                       for (ListIterator li = pNames.listIterator(); li.hasNext(); ) {
                                           String province = (String) li.next(); %>
                                           <option value="<%=province%>"<%=province.equals(hctype)?" selected":""%>><%=li.next()%></option>
                                       <% } %>
                                <% } else { %>
                                  <option value="AB"<%=hctype.equals("AB")?" selected":""%>>AB-Alberta</option>
                                  <option value="BC"<%=hctype.equals("BC")?" selected":""%>>BC-British Columbia</option>
                                  <option value="MB"<%=hctype.equals("MB")?" selected":""%>>MB-Manitoba</option>
                                  <option value="NB"<%=hctype.equals("NB")?" selected":""%>>NB-New Brunswick</option>
                                  <option value="NL"<%=hctype.equals("NL")?" selected":""%>>NL-Newfoundland & Labrador</option>
                                  <option value="NT"<%=hctype.equals("NT")?" selected":""%>>NT-Northwest Territory</option>
                                  <option value="NS"<%=hctype.equals("NS")?" selected":""%>>NS-Nova Scotia</option>
                                  <option value="NU"<%=hctype.equals("NU")?" selected":""%>>NU-Nunavut</option>
                                  <option value="ON"<%=hctype.equals("ON")?" selected":""%>>ON-Ontario</option>
                                  <option value="PE"<%=hctype.equals("PE")?" selected":""%>>PE-Prince Edward Island</option>
                                  <option value="QC"<%=hctype.equals("QC")?" selected":""%>>QC-Quebec</option>
                                  <option value="SK"<%=hctype.equals("SK")?" selected":""%>>SK-Saskatchewan</option>
                                  <option value="YT"<%=hctype.equals("YT")?" selected":""%>>YT-Yukon</option>
                                  <option value="US"<%=hctype.equals("US")?" selected":""%>>US resident</option>
                                <% } %>
                                </select>
                                <% }%>
                              </td>
                              <td align="right" nowrap><b>
                               <bean:message key="demographic.demographiceditdemographic.cytolNum"/>:</b>
                              </td>
                              <td>
                                <input type="text" name="cytolNum" style="display:inline;width:auto;" value="<%=s(demoExt.get("cytolNum"))%>">
                                <input type="hidden" name="cytolNumOrig" value="<%=s(demoExt.get("cytolNum"))%>" />
                              </td>
                            </tr>
                            <tr valign="top">
                              <td align="right" nowrap><b><% if(oscarProps.getProperty("demographicLabelDoctor") != null) { out.print(oscarProps.getProperty("demographicLabelDoctor","")); } else { %>
                                                <bean:message key="demographic.demographiceditdemographic.formDoctor"/><% } %>: </b></td>
                              <td align="left" >
                                <select name="provider_no" style="width:200px">
                                  <option value="" ></option>
                        <%
                          ResultSet rsdemo = apptMainBean.queryResults("search_provider_doc");
                          while (rsdemo.next()) {
                        %>
                          <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(rs.getString("provider_no"))?"selected":""%> >
                          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
                        <% } %>
                                </select>
                              </td>
                              <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formNurse"/>: </b> </td>
                              <td align="left">
                                <select name="resident" style="width:200px">
                                  <option value="" ></option>
                        <%
                          rsdemo.beforeFirst();
                          while (rsdemo.next()) {
                        %>
                          <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(resident)?"selected":""%> >
                          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
                        <% } %>
                                </select>
                              </td>
                            </tr>
                            <tr valign="top">
                              <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formMidwife"/>: </b></td>
                              <td align="left" >
                                <select name="midwife" style="width:200px">
                                  <option value="" ></option>
                        <%
                          rsdemo.beforeFirst();
                          while (rsdemo.next()) {
                        %>
                          <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(midwife)?"selected":""%> >
                          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
                        <% } %>
                                </select>
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formResident"/>:</b> </td>
                              <td align="left">
                                <select name="nurse" style="width:200px">
                                  <option value="" ></option>
                        <%
                          rsdemo.beforeFirst();
                          while (rsdemo.next()) {
                        %>
                          <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(nurse)?"selected":""%> >
                          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
                        <% } %>
                                </select>
                              </td>
                            </tr>

                            <tr valign="top">
                                  <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formRefDoc"/>: </b></td>
                                  <td align="left" >
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
									  	//prop.setProperty("specialty",rs1.getString("specialty"));
									  	//prop.setProperty("phone",rs1.getString("phone"));
									  	vecRef.add(prop);
                                      }
                                  %>
                                	<select name="r_doctor" onChange="changeRefDoc()" style="width:200px">
                                  	<option value="" ></option>
                                  	<% for(int k=0; k<vecRef.size(); k++) {
                                  		prop= (Properties) vecRef.get(k);
                                  	%>
                          <option value="<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>" <%=prop.getProperty("referral_no").equals(rdohip)?"selected":""%> >
                          <%=Misc.getShortStr( (prop.getProperty("last_name")+","+prop.getProperty("first_name")),"",nStrShowLen)%></option>
 	                      <% } %>         	</select>
<script language="Javascript">
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
</script>
                                  <% } else {%>
                                    <input type="text" name="r_doctor" size="30" maxlength="40" value="<%=rd%>">
                                  <% } %>
                                  </td>
                                  <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formRefDocNo"/>: </b></td>
                                  <td align="left">
                                    <input type="text" name="r_doctor_ohip" size="20" maxlength="6" value="<%=rdohip%>">
                					<% if("ON".equals(prov)) { %>
									<a href="javascript:referralScriptAttach2('r_doctor_ohip','r_doctor')">Search #</a>
                					<% } %>
                                  </td>
                            </tr>

                            <tr valign="top">
                              <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formRosterStatus"/>: </b></td>
                              <td align="left" >
                                <%String rosterStatus = rs.getString("roster_status");
                                  if (rosterStatus == null) {
                                     rosterStatus = "";
                                  }
                                  %>
                                <!--  input type="text" name="roster_status" size="30" value="<%--=rosterStatus--%>" onBlur="upCaseCtrl(this)" -->
                                <select name="roster_status" style="width:120">
                                  <option value="" > </option>
                                  <option value="RO"<%=rosterStatus.equals("RO")?" selected":""%>>RO - rostered</option>
                                  <option value="NR"<%=rosterStatus.equals("NR")?" selected":""%>>NR - not rostered</option>
                                  <option value="TE"<%=rosterStatus.equals("TE")?" selected":""%>>TE - terminated</option>
                                  <option value="FS"<%=rosterStatus.equals("FS")?" selected":""%>>FS - fee for service</option>
                                  <% ResultSet rsstatus1 = apptMainBean.queryResults("search_rsstatus");
                                     while (rsstatus1.next()) { %>
                                       <option<%=rosterStatus.equals(rsstatus1.getString("roster_status"))?" selected":""%>><%=rsstatus1.getString("roster_status")%></option>
                                  <% } // end while %>
                                </select>
								<input type="button" onClick="newStatus1();" value="Add New">
                              </td>
                              <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.DateJoined"/>: </b></td>
                              <td align="left">
                              <%
                                 // Format year
                                 decF.applyPattern("0000");
                                 String hcRenewYear = decF.format(MyDateFormat.getYearFromStandardDate(rs.getString("hc_renew_date")));
                                 decF.applyPattern("00");
                                 String hcRenewMonth = decF.format(MyDateFormat.getMonthFromStandardDate(rs.getString("hc_renew_date")));
                                 String hcRenewDay = decF.format(MyDateFormat.getDayFromStandardDate(rs.getString("hc_renew_date")));
                              %>
                                <input type="text" name="hc_renew_date_year" size="4" maxlength="4" value="<%= hcRenewYear %>">
                                <input type="text" name="hc_renew_date_month" size="2" maxlength="2" value="<%= hcRenewMonth %>">
                                <input type="text" name="hc_renew_date_date" size="2" maxlength="2" value="<%= hcRenewDay %>">
                              </td>
                            </tr>
                            <tr valign="top">
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formPatientStatus"/>:</b> <b> </b></td>
                              <td align="left" >
                                <% if (vLocale.getCountry().equals("BR")) { %>
                                 <%String pacStatus = rs.getString("patient_status");
                                  if (pacStatus == null) {
                                     pacStatus = "";
                                  }
                                  %>
                                  <input type="text" name="patient_status" value="<%=pacStatus%>">
                                <% } else {
                                String patientStatus = rs.getString("patient_status"); %>
                                <select name="patient_status" style="width:120">
                                  <option value="AC"<%=patientStatus.equals("AC")?" selected":""%>>AC - Active</option>
                                  <option value="IN"<%=patientStatus.equals("IN")?" selected":""%>>IN - Inactive</option>
                                  <option value="DE"<%=patientStatus.equals("DE")?" selected":""%>>DE - Deceased</option>
                                  <option value="MO"<%=patientStatus.equals("MO")?" selected":""%>>MO - Moved</option>
                                  <option value="FI"<%=patientStatus.equals("FI")?" selected":""%>>FI - Fired</option>
                                  <% ResultSet rsstatus = apptMainBean.queryResults("search_ptstatus");
                                     while (rsstatus.next()) { %>
                                       <option<%=patientStatus.equals(rsstatus.getString("patient_status"))?" selected":""%>><%=rsstatus.getString("patient_status")%></option>
                                  <% } // end while %>
                                </select>
                                <input type="button" onClick="newStatus();" value="Add New">
                                <% } // end if...then...else %>
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formChartNo"/>:</b> </td>
                              <td align="left">
                                <input type="text" name="chart_no" size="30" value="<%=rs.getString("chart_no")%>">
                              </td>
                            </tr>
                            <%
                            if (vLocale.getCountry().equals("BR")) { %>
                            <tr valign="top">
                              <td align="right"><b></b></td>
                              <td align="left" >
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formChartAddress"/>: </b></td>
                              <td align="left">
                                <input type="text" name="chart_address" value="<%=rs.getString("chart_address")==null?"":rs.getString("chart_address")%>">
                              </td>
                            </tr>
                            <%}%>
                            <%if(wL.getFound()){%>
                            <tr valign="top">
                              <td align="right" nowrap><b>Waiting List: </b></td>
                              <td align="left" >
                              <%
                                ResultSet rsWLStatus = apptMainBean.queryResults(demographic_no,"search_wlstatus");
                                String listID = "", wlnote="";
                                if (rsWLStatus.next()){
                                    listID = rsWLStatus.getString("listID");
                                    wlnote = rsWLStatus.getString("note");
                                }
                               %>
                                <select name="list_id">
                                  <option value="0" >--Select Waiting List--</option>
                                  <%
                                      ResultSet rsWL = apptMainBean.queryResults("search_waiting_list");
                                      while (rsWL.next()) {
                                    %>
                                              <option value="<%=rsWL.getString("ID")%>" <%=rsWL.getString("ID").equals(listID)?" selected":""%>>
                                              <%=rsWL.getString("name")%></option>
                                              <%
                                      }
                                    %>
                                </select>
                              </td>
                              <td align="right" nowrap><b>Waiting List Note: </b></td>
                              <td align="left">
                                <input type="text" name="waiting_list_note" value="<%=wlnote%>" >
                              </td>
                            </tr>
                            <%}%>
                            <tr valign="top">
                              <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formDateJoined1"/>: </b></td>
                              <td align="left" >
                              <%
                                 // Format year
                                 decF.applyPattern("0000");
                                 String dateJoinedYear = decF.format(MyDateFormat.getYearFromStandardDate(rs.getString("date_joined")));
                                 decF.applyPattern("00");
                                 String dateJoinedMonth = decF.format(MyDateFormat.getMonthFromStandardDate(rs.getString("date_joined")));
                                 String dateJoinedDay = decF.format(MyDateFormat.getDayFromStandardDate(rs.getString("date_joined")));
                              %>
                                <input type="text" name="date_joined_year" size="4" maxlength="4" value="<%= dateJoinedYear %>">
                                <input type="text" name="date_joined_month" size="2" maxlength="2" value="<%= dateJoinedMonth %>">
                                <input type="text" name="date_joined_date" size="2" maxlength="2" value="<%= dateJoinedDay %>">
                              </td>
                              <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formEndDate"/>: </b></td>
                              <td align="left">
                              <%
                                 // Format year
                                 decF.applyPattern("0000");
                                 String endYear = decF.format(MyDateFormat.getYearFromStandardDate(rs.getString("end_date")));
                                 decF.applyPattern("00");
                                 String endMonth = decF.format(MyDateFormat.getMonthFromStandardDate(rs.getString("end_date")));
                                 String endDay = decF.format(MyDateFormat.getDayFromStandardDate(rs.getString("end_date")));
                              %>
                                <input type="text" name="end_date_year" size="4" maxlength="4" value="<%= endYear %>">
                                <input type="text" name="end_date_month" size="2" maxlength="2" value="<%= endMonth %>">
                                <input type="text" name="end_date_date" size="2" maxlength="2" value="<%= endDay %>">
                              </td>
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
                              <td align="left" >
                              <% if(bExtForm) {
                                  	if(propDemoExtForm[k].indexOf("<select")>=0) {
                                		out.println(propDemoExtForm[k].replaceAll("value=\""+s(demoExt.get(propDemoExt[k].replace(' ', '_')))+"\"" , "value=\""+s(demoExt.get(propDemoExt[k].replace(' ', '_')))+"\"" + " selected") );
                                  	} else {
                              			out.println(propDemoExtForm[k].replaceAll("value=\"\"", "value=\""+s(demoExt.get(propDemoExt[k].replace(' ', '_')))+"\"" ) );
                                  	}
                              	 } else { %>
                                <input type="text" name="<%=propDemoExt[k].replace(' ', '_')%>" value="<%=s(demoExt.get(propDemoExt[k].replace(' ', '_')))%>" />
                              <% }  %>
								<input type="hidden" name="<%=propDemoExt[k].replace(' ', '_')%>Orig" value="<%=s(demoExt.get(propDemoExt[k].replace(' ', '_')))%>" />
                              </td>
                              <% if((k+1)<propDemoExt.length) { %>
                              <td align="right" nowrap><b><%= propDemoExt[k+1]+":"%> </b></td>
                              <td align="left" >
                              <% if(bExtForm) {
                                  	if(propDemoExtForm[k+1].indexOf("<select")>=0) {
                                		out.println(propDemoExtForm[k+1].replaceAll("value=\""+s(demoExt.get(propDemoExt[k+1].replace(' ', '_')))+"\"" , "value=\""+s(demoExt.get(propDemoExt[k+1].replace(' ', '_')))+"\"" + " selected") );
                                  	} else {
                              			out.println(propDemoExtForm[k+1].replaceAll("value=\"\"", "value=\""+s(demoExt.get(propDemoExt[k+1].replace(' ', '_')))+"\"" ) );
                                  	}
                              	 } else { %>
                                <input type="text" name="<%=propDemoExt[k+1].replace(' ', '_')%>" value="<%=s(demoExt.get(propDemoExt[k+1].replace(' ', '_')))%>" />
                              <% }  %>
								<input type="hidden" name="<%=propDemoExt[k+1].replace(' ', '_')%>Orig" value="<%=s(demoExt.get(propDemoExt[k+1].replace(' ', '_')))%>" />
                              </td>
                              <% } else {%>
                              <td>&nbsp;</td><td>&nbsp;</td>
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
                                       <td width="7%" align="right"><font color="#FF0000"><b><bean:message key="demographic.demographiceditdemographic.formAlert"/>: </b></font>
                                       </td>
                                       <td>
                                          <textarea name="alert" style="width:100%" cols="80" rows="2"><%=alert%></textarea>
                                       </td>
                                     </tr>
                                     <tr>
                                        <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formNotes"/>: </b></td>
                                        <td>
                                           <textarea name="notes" style="width:100%" cols="60"><%=notes%></textarea>
                                        </td>
                                     </tr>
                                  </table>
                               </td>
                            </tr>

                        </table>
                        </td></tr>
                    <tr bgcolor="#CCCCFF">
                      <td colspan="4">
                        <table border=0  width="100%" cellpadding="0" cellspacing="0">
                          <tr>
                            <td width="30%" valign="top">
                              <input type="hidden" name="dboperation" value="update_record">
                                  <%
                                  if (vLocale.getCountry().equals("BR")) { %>
                                  <input type="hidden" name="dboperation2" value="update_record_ptbr">
                                  <%}%>
                              <input type="button" name="Button" value="<bean:message key="global.btnBack" />" onclick="history.go(-1);return false;">
                              <input type="button" name="Button" value="<bean:message key="global.btnCancel" />" onclick=self.close();>
                            </td>
                            <td  width="30%" align='center' valign="top">
                              <input type="hidden" name="displaymode" value="Update Record" >
<!-- security code block -->
   <span id="updateButton" style="display:none;">
	<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w">
                              <input type="submit" value="<bean:message key="demographic.demographiceditdemographic.btnUpdate"/>">
	</security:oscarSec>
   <span>
<!-- security code block -->
                            </td>
                            <td width="40%" align='right' valign="top">
                              <span id="swipeButton" style="display:none;">
                               <input type="button" name="Button" value="<bean:message key="demographic.demographiceditdemographic.btnSwipeCard"/>" onclick="window.open('zdemographicswipe.jsp','', 'scrollbars=yes,resizable=yes,width=600,height=300, top=360, left=0')">
                              </span>
                               <!--input type="button" name="Button" value="<bean:message key="demographic.demographiceditdemographic.btnSwipeCard"/>" onclick="javascript:window.alert('Health Card Number Already Inuse');"-->
                               <input type="button" size="110" name="Button" value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFEnvelope"/>" onclick="window.location='../report/GenerateEnvelopes.do?demos=<%=rs.getString("demographic_no")%>'">
                               <input type="button" size="110" name="Button" value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFLabel"/>" onclick="window.location='printDemoLabelAction.do?demographic_no=<%=rs.getString("demographic_no")%>'">
                               <input type="button" name="Button" size="110" value="<bean:message key="demographic.demographiceditdemographic.btnPrintLabel"/>" onclick="window.location='demographiclabelprintsetting.jsp?demographic_no=<%=rs.getString("demographic_no")%>'">
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
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
</body>
</html:html>

<%!
 String s(Object o){
     String ret = "";
     if (null != o){
         ret = (String) o;
     }
     return ret;
 }
%>

