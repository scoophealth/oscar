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

<%@ page import="java.util.*, java.sql.*, java.net.*,java.text.DecimalFormat, oscar.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
	if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
	
	String curProvider_no = (String) session.getAttribute("user");
	String demographic_no = request.getParameter("demographic_no") ;
	String userfirstname = (String) session.getAttribute("userfirstname");
	String userlastname = (String) session.getAttribute("userlastname");
	String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
	String str = null;
	int nStrShowLen = 14;
        String prov= ((String ) oscarVariables.getProperty("billregion","")).trim().toUpperCase();
%>



<html:html locale="true">
<head>
<title><bean:message key="demographic.demographiceditdemographic.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">

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

function checkTypeIn() {
  var dob = document.titlesearch.keyword; typeInOK = false;
  
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
  }
}

function checkTypeInEdit() {
  var typeInOK = false;
    if(document.updatedelete.last_name.value!="" && document.updatedelete.first_name.value!="" && document.updatedelete.sex.value!="") {
      if(checkTypeNum(document.updatedelete.year_of_birth.value) && checkTypeNum(document.updatedelete.month_of_birth.value) && checkTypeNum(document.updatedelete.date_of_birth.value) ){
	    typeInOK = true;
      }
    }
  if(!typeInOK) alert ("<bean:message key="demographic.demographicaddrecordhtm.msgMissingFields"/>");
  return typeInOK;
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
function checkPhoneNum() {
  var typeIn = document.updatedelete.phone.value ;
  while(typeIn.indexOf('-') > 0 ) {
    typeIn = typeIn.replace("-", "") ;
  }
  document.updatedelete.phone.value = typeIn ;
}
function checkONReferralNo() {
  var referralNo = document.updatedelete.r_doctor_ohip.value ;
  if (document.updatedelete.hc_type.options[8].selected && referralNo.length > 0 && referralNo.length != 6) {
    alert("The referral doctor's no. is wrong. Please correct it!") ;
  }
}

function refresh() {
  //history.go(0);
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

</script>

</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus(); checkONReferralNo();" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="<%=deepcolor%>"><th><font face="Helvetica"><bean:message key="demographic.demographiceditdemographic.msgPatientDetailRecord"/></font></th></tr>
</table>
<%-- log:info category="Demographic">Demographic [<%=demographic_no%>] is viewed by User [<%=userfirstname%> <%=userlastname %>]  </log:info --%>

<%@ include file="zdemographicfulltitlesearch.jsp" %>

<%
   java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.action.Action.LOCALE_KEY);   
	//----------------------------REFERRAL DOCTOR------------------------------
	String rdohip="", rd="", fd="", family_doc = "";
 
	String resident="", nurse="", alert="", notes="", midwife="";
	ResultSet rs = null;
	rs = apptMainBean.queryResults(demographic_no, "search_demographiccust");
	while (rs.next()) {
		resident = rs.getString("cust1");
		nurse = rs.getString("cust2");
		alert = rs.getString("cust3");
                midwife = rs.getString("cust4");
		notes = SxmlMisc.getXmlContent(rs.getString("content"),"unotes") ;
		notes = notes==null?"":notes; 
	}

	GregorianCalendar now=new GregorianCalendar();
	int curYear = now.get(Calendar.YEAR);
	int curMonth = (now.get(Calendar.MONTH)+1);
	int curDay = now.get(Calendar.DAY_OF_MONTH);
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
<table width="100%" border="1" cellspacing="0" cellpadding="1" bgcolor="#CCCCFF" bordercolor="#669966">
  <tr>
    <!--th><a href="#" onClick="popupPage(500,600,'demographicsummary.jsp?demographic_no=<%=rs.getString("demographic_no")%>')">Patient Summary</a> </th-->
    <th><a href="#" onClick="popupPage(500,600,'../dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=rs.getString("demographic_no")%>&curUser=<%=curProvider_no%>')"><bean:message key="demographic.demographiceditdemographic.msgDocuments"/></a></th>
    <th><a href="../eform/showmyform.jsp?demographic_no=<%=demographic_no%>"><bean:message key="demographic.demographiceditdemographic.btnEForm"/></a></th>
    <th><a href='demographiccontrol.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10'><bean:message key="demographic.demographiceditdemographic.btnApptHist"/></a></th>
    <th>
     <% if (vLocale.getCountry().equals("BR")) { %>  
      <!--a href="#" onClick="popupPage(500,600,'../billing/billinghistory.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10')">Billing History</a-->
      <a href='../oscar/billing/consultaFaturamentoPaciente/init.do?demographic_no=<%=rs.getString("demographic_no")%>'>Hist&oacute;rico do Faturamento</a></th>
    <% } else  { %>  
      <!--a href="#" onClick="popupPage(500,600,'../billing/billinghistory.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10')">Billing History</a-->
      <a href='../billing/CA/<%=prov%>/billinghistory.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10'><bean:message key="demographic.demographiceditdemographic.btnBillingHist"/></a></th>
    <% } %>  
  </tr>
</table> 

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <form method="post" name="updatedelete" id="updatedelete" action="demographiccontrol.jsp" onsubmit="return checkTypeInEdit();">
    <input type="hidden" name="demographic_no" value="<%=rs.getString("demographic_no")%>">
    <tr> 
      <td align="right" title='<%=rs.getString("demographic_no")%>'> <b><bean:message key="demographic.demographiceditdemographic.formLastName"/>: </b></td>
      <td align="left"> 
        <input type="text" name="last_name" value="<%=rs.getString("last_name")%>" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formFirstName"/>: </b> </td>
      <td align="left"> 
        <input type="text" name="first_name" value="<%=rs.getString("first_name")%>" onBlur="upCaseCtrl(this)">
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
        <input type="text" name="address" value="<%=rs.getString("address")%>"><% if (vLocale.getCountry().equals("BR")) { %>
        <b><bean:message key="demographic.demographicaddrecordhtm.formAddressNo"/>:</b>
        <input type="text" name="address_no" value="<%=rs.getString("address_no")==null?"":rs.getString("address_no")%>" size="6">        
        <%}%>
      </td>
      <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formCity"/>: </b></td>
      <td align="left"> 
        <input type="text" name="city" value="<%=rs.getString("city")%>">
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
      <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formProcvince"/>: </b> </td>
      <td  align="left">
        <% if (vLocale.getCountry().equals("BR")) { %>
        <input type="text" name="province" value="<%=rs.getString("province")%>">
        <% } else { %>
        <select name="province">
        <% String province = rs.getString("province"); %>          
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
          <option value=""<%=(province.equals("") || province.length() > 2)?" selected":""%>>Other</option>
        </select>
        <% } %>
      </td>
      <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formPostal"/>: </b> </td>
      <td  align="left"> 
        <input type="text" name="postal" value="<%=rs.getString("postal")%>" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <tr valign="top"> 
      <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formPhoneH"/>: </b> </td>
      <td align="left" > 
        <input type="text" name="phone" value="
<%
if(rs.getString("phone")!=null && rs.getString("phone").length()==10){
  out.print(rs.getString("phone").substring(0,3) + "-" + rs.getString("phone").substring(3,6) +"-"+  rs.getString("phone").substring(6) );
} else {
  out.print(rs.getString("phone") );
}
%>
">
      </td>
      <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formPhoneW"/>:</b> </td>
      <td  align="left"> 
        <input type="text" name="phone2" value="<%=rs.getString("phone2")%>">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formEmail"/>: </b> </td>
      <td  align="left"> 
        <input type="text" name="email" value="<%=rs.getString("email")!=null? rs.getString("email") : ""%>">
      </td>
      <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formPIN"/>: </b> </td>
      <td  align="left"> 
        <input type="text" name="pin" value="<%=rs.getString("pin")!=null? rs.getString("pin") : ""%>" >
      </td>
    </tr>
    <tr valign="top"> 
      <td  align="right"><b><bean:message key="demographic.demographiceditdemographic.formDOB"/></b><bean:message key="demographic.demographiceditdemographic.formDOBDetais"/><b>:</b> </td>
      <td  align="left" nowrap> 
        <input type="text" name="year_of_birth" value="<%=rs.getString("year_of_birth")%>" size="4" maxlength="4">
        <input type="text" name="month_of_birth" value="<%=rs.getString("month_of_birth")%>" size="2" maxlength="2">
        <input type="text" name="date_of_birth" value="<%=rs.getString("date_of_birth")%>" size="2" maxlength="2">
        <b>Age: 
        <input type="text" name="age" readonly value="<%=age%>" size="3">
        </b> </td>
      <td  align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formSex"/>:</b> </td>
      <td align="left">
        <input type="text" name="sex" value="<%=rs.getString("sex")%>" onBlur="upCaseCtrl(this)" size="1" maxlength="1">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formHin"/>: </b></td>
      <td align="left" nowrap> 
        <input type="text" name="hin" value="<%=rs.getString("hin")%>" size="15">
        <b><bean:message key="demographic.demographiceditdemographic.formVer"/></b> 
        <input type="text" name="ver" value="<%=rs.getString("ver")%>" size="3">
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
		<% String hctype = rs.getString("hc_type")==null?"":rs.getString("hc_type"); %>
        <select name="hc_type">
          <option value="" >None Selected</option>
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
        </select>
      </td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr valign="top"> 
      <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formDoctor"/>: </b></td>
      <td align="left" > 
        <select name="provider_no">
          <option value="" ></option>
<%
  ResultSet rsdemo = apptMainBean.queryResults("search_provider");
  while (rsdemo.next()) { 
%>
  <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(rs.getString("provider_no"))?"selected":""%> >
  <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
<% } %>
        </select>
      </td>
      <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formNurse"/>: </b> </td>
      <td align="left">
        <select name="resident">
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
        <select name="midwife">
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
        <select name="nurse">
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
            <input type="text" name="r_doctor" maxlength="40" value="<%=rd%>">
          </td>
          <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formRefDocNo"/>: </b></td>
          <td align="left">
            <input type="text" name="r_doctor_ohip" maxlength="6" value="<%=rdohip%>">
          </td>
    </tr>
    
    <tr valign="top"> 
      <td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.formRosterStatus"/>: </b></td>
      <td align="left" > 
        <input type="text" name="roster_status" value="<%=rs.getString("roster_status")%>" onBlur="upCaseCtrl(this)">
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
      <td align="right"><b>Patient Status:</b> <b> </b></td>
      <td align="left" >
        <% if (vLocale.getCountry().equals("BR")) { %>
          <input type="text" name="patient_status" value="<%=rs.getString("patient_status")%>">
        <% } else { 
        String patientStatus = rs.getString("patient_status"); %>
        <select name="patient_status">
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
        <input type="text" name="chart_no" value="<%=rs.getString("chart_no")%>">
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
    <tr valign="top"> 
      <td nowrap colspan="4">
	    <table width="100%" bgcolor="#EEEEFF">
          <tr>
	        <td width="10%" align="right"><font color="#FF0000"><b><bean:message key="demographic.demographiceditdemographic.formAlert"/>: </b></font> 
            </td>
        <td>
              <textarea name="alert" style="width:100%" cols="80" rows="2"><%=alert%></textarea>
            </td>
	  </tr><tr>
            <td align="right"><b><bean:message key="demographic.demographiceditdemographic.formNotes"/>: </b></td>
        <td>
              <textarea name="notes" style="width:100%" cols="60"><%=notes%></textarea>
            </td>
	  </tr></table>
    <tr bgcolor="#CCCCFF"> 
      <td colspan="4"> 
        <table border=0  width="100%" cellpadding="0" cellspacing="0">
          <tr> 
            <td width="30%"> 
              <input type="hidden" name="dboperation" value="update_record">
	          <% 
	          if (vLocale.getCountry().equals("BR")) { %>  
	          <input type="hidden" name="dboperation2" value="update_record_ptbr">          
	          <%}%> 
              <input type="button" name="Button" value="<bean:message key="global.btnBack" />" onclick=history.go(-1);>
              <input type="button" name="Button" value="<bean:message key="global.btnCancel" />" onclick=self.close();>
            </td>
            <td  width="30%" align='center'> 
              <input type="hidden" name="displaymode" value="Update Record" >
              <input type="submit" value="<bean:message key="demographic.demographiceditdemographic.btnUpdate"/>">
            </td>
            <td width="40%" align='right'> 
               <input type="button" name="Button" value="<bean:message key="demographic.demographiceditdemographic.btnSwipeCard"/>" onclick="window.open('zdemographicswipe.jsp','', 'scrollbars=yes,resizable=yes,width=600,height=300, top=360, left=0')";>
              <input type="button" name="Button" value="<bean:message key="demographic.demographiceditdemographic.btnPrintLabel"/>" onclick="window.location='demographiclabelprintsetting.jsp?demographic_no=<%=rs.getString("demographic_no")%>'";>
            </td>
        </table>
    </tr>
  </form>
</table>

<p>
<table border=0  width="100%" cellpadding="0" cellspacing="0" bgcolor='#CCCCFF'>
  <tr><th>
  </th></tr>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="1" bgcolor="#CCCCFF" bordercolor="#669966">
  <tr>
    <th><a href="../eform/myform.jsp?demographic_no=<%=demographic_no%>" > <bean:message key="demographic.demographiceditdemographic.btnAddEForm"/> </a></th>
    <th><a href=# onclick="window.open('../dms/adddocument.jsp?function=demographic&functionid=<%=rs.getString("demographic_no")%>&creator=<%=curProvider_no%>','', 'scrollbars=yes,resizable=yes,width=600,height=300');return false;"><bean:message key="demographic.demographiceditdemographic.btnAddDocument"/></a></th>
    <th><!--a href=# onclick="popupPage(600,800,'../provider/providercontrol.jsp?appointment_no=&demographic_no=<%=demographic_no%>&curProvider_no=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&username=&appointment_date=&start_time=&status=&displaymode=encounter&dboperation=search_demograph&template=');return false;" title="Tel-Progress Notes">Add Encounter</a-->
    <a href=# onClick="popupEChart(700,980,'../oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo=<%=demographic_no%>&curProviderNo=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&userName=<%=URLEncoder.encode( userfirstname+" "+userlastname) %>&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=');return false;" title="<bean:message key="demographic.demographiceditdemographic.btnEChart"/>">
            <bean:message key="demographic.demographiceditdemographic.btnEChart"/></a></th>
    <% if (!vLocale.getCountry().equals("BR")) { %>
    <th>
       <a href=# onclick="popupPage(700, 1000, '../billing.do?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=&appointment_no=0&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&demographic_no=<%=rs.getString("demographic_no")%>&providerview=1&user_no=<%=curProvider_no%>&apptProvider_no=none&appointment_date=0001-01-01&start_time=0:00&bNewForm=1&status=t');return false;" title="bill a patient">Add Billing</a>
    </th>
    <th>
       <a href=# onclick="window.open('../billing/specialtyBilling/fluBilling/addFluBilling.jsp?function=demographic&functionid=<%=rs.getString("demographic_no")%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&hin=<%=URLEncoder.encode(rs.getString("hin"))%><%=URLEncoder.encode(rs.getString("ver"))%>&demo_sex=<%=URLEncoder.encode(rs.getString("sex"))%>&demo_hctype=<%=URLEncoder.encode(rs.getString("hc_type")==null?"null":rs.getString("hc_type"))%>&rd=<%=URLEncoder.encode(rd==null?"null":rd)%>&rdohip=<%=URLEncoder.encode(rdohip==null?"null":rdohip)%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(rs.getString("year_of_birth")),Integer.parseInt(rs.getString("month_of_birth")),Integer.parseInt(rs.getString("date_of_birth")))%>','', 'scrollbars=yes,resizable=yes,width=640,height=400');return false;" title='Add Flu Billing'>Flu Billing</a></th>
    <th><a href=# onclick="window.open('../billing/inr/addINRbilling.jsp?function=demographic&functionid=<%=rs.getString("demographic_no")%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&hin=<%=URLEncoder.encode(rs.getString("hin"))%><%=URLEncoder.encode(rs.getString("ver"))%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(rs.getString("year_of_birth")),Integer.parseInt(rs.getString("month_of_birth")),Integer.parseInt(rs.getString("date_of_birth")))%>','', 'scrollbars=yes,resizable=yes,width=600,height=400');return false;" title='Add INR Billing'>Add INR</a></th>
    <th><a href=# onclick="window.open('../billing/inr/reportINR.jsp?provider_no=<%=curProvider_no%>','', 'scrollbars=yes,resizable=yes,width=600,height=600');return false;" title='INR Billing'>Bill INR</a></th>
    <th><a href=# onClick="popupOscarRx(700,960,'../oscarRx/choosePatient.do?providerNo=<%=curProvider_no%>&demographicNo=<%=demographic_no%>')"><bean:message key="global.prescriptions"/></a></th>
    <% } %>  
<th>
<a href="javascript: function myFunction() {return false; }" onclick="popupPage(100,355,'../oscarEncounter/oscarConsultationRequest/ConsultChoice.jsp?de=<%=rs.getString("demographic_no")%>&proNo=<%=rs.getString("provider_no")%>')"><bean:message key="demographic.demographiceditdemographic.btnConsultation"/></a></th>
</th>  
</tr> 
</table>            <!--a href=# onClick="popupPage(600,750,'../provider/providercontrol.jsp?appointment_no=&demographic_no=<%--=demographic_no--%>&curProvider_no=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&username=&appointment_date=&start_time=&status=&displaymode=encounter&dboperation=search_demograph&template=');return false;" title="Tel-Progress Notes">
            <img src="../images/encounter.gif" border="0" height="15"></a> |            
            <a href=# onClick="popupPage(700, 720, '../billing/billingOB.jsp?hotclick=&appointment_no=0&demographic_name=<%--=rs.getString("last_name")--%>%2C<%--=rs.getString("first_name")--%>&demographic_no=<%--=rs.getString("demographic_no")--%>&providerview=1&user_no=<%--=curProvider_no--%>&apptProvider_no=none&appointment_date=0001-01-01&start_time=0:00&bNewForm=1');return false;" title="bill a patient"> $ </a> |       -->   

<%
    }
  }
  apptMainBean.closePstmtConn();
%>
</body>
</html:html>
