<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String str = null;
%> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="java.lang.*, java.util.*, java.text.*,java.sql.*, oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="addDemoBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
    {"search_provider", "select * from provider order by last_name"},
  };
  String[][] responseTargets=new String[][] {  };
  addDemoBean.doConfigure(dbParams,dbQueries,responseTargets);
  
  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.action.Action.LOCALE_KEY);

  OscarProperties props = OscarProperties.getInstance();

  GregorianCalendar now=new GregorianCalendar();
  String curYear = Integer.toString(now.get(Calendar.YEAR));
  String curMonth = Integer.toString(now.get(Calendar.MONTH)+1);
  if (curMonth.length() < 2) curMonth = "0"+curMonth;
  String curDay = Integer.toString(now.get(Calendar.DAY_OF_MONTH));
  if (curDay.length() < 2) curDay = "0"+curDay;
%>

<html:html locale="true">
<head>
<title> <bean:message key="demographic.demographicaddrecordhtm.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />
<script language="JavaScript">

function setfocus() {
  this.focus();
  document.adddemographic.last_name.focus();
  document.adddemographic.last_name.select();
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
	var typeInOK = false;
	if(document.adddemographic.last_name.value!="" && document.adddemographic.first_name.value!="" && document.adddemographic.sex.value!="") {
      if(checkTypeNum(document.adddemographic.year_of_birth.value) && checkTypeNum(document.adddemographic.month_of_birth.value) && checkTypeNum(document.adddemographic.date_of_birth.value) ){
	    typeInOK = true;
	  }
	}
	if(!typeInOK) alert ("<bean:message key="demographic.demographicaddrecordhtm.msgMissingFields"/>");
	return typeInOK;
}

</script>
</head>
<!-- Databases have alias for today. It is not necessary give the current date -->
<!--<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus();showDate();"  topmargin="0" leftmargin="0" rightmargin="0">-->
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus();"  topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
	  <tr bgcolor="#CCCCFF"> 
      <th><font face="Helvetica"><bean:message key="demographic.demographicaddrecordhtm.msgMainLabel"/></font></th>
	  </tr>
	</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#EEEEFF">
	<form method="post" name="titlesearch" action="demographiccontrol.jsp" onsubmit="checkTypeIn()">
		<tr valign="top"><td rowspan="2" ALIGN="right" valign="middle"> <font face="Verdana" color="#0000FF"><b><i><bean:message key="demographic.demographicaddrecordhtm.msgSearch"/>
        </i></b></font></td>
			
      <td width="10%" nowrap><font size="1" face="Verdana" color="#0000FF"> 
        <input type="radio"  checked name="search_mode" value="search_name">
        <bean:message key="demographic.demographicaddrecordhtm.formName"/> </font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF"> 
          <input type="radio"  name="search_mode" value="search_phone">
          <bean:message key="demographic.demographicaddrecordhtm.formPhone"/></font></td> 
        <td nowrap><font size="1" face="Verdana" color="#0000FF">
          <input type="radio"  name="search_mode" value="search_dob">
          <bean:message key="demographic.demographicaddrecordhtm.formDOB"/>(yyyymmdd)</font></td> 
      <td valign="middle" rowspan="2" ALIGN="left"><input type="text" NAME="keyword" SIZE="17"  MAXLENGTH="100">
				<INPUT TYPE="hidden" NAME="orderby" VALUE="last_name" >
				<INPUT TYPE="hidden" NAME="dboperation" VALUE="search_titlename" >
				<INPUT TYPE="hidden" NAME="limit1" VALUE="0" >
				<INPUT TYPE="hidden" NAME="limit2" VALUE="10" >
				<INPUT TYPE="hidden" NAME="displaymode" VALUE="Search" >
				<INPUT TYPE="SUBMIT" NAME="displaymode" VALUE="Search" SIZE="17"></td>
		</tr><tr>
			
      <td nowrap><font size="1" face="Verdana" color="#0000FF"> 
        <input type="radio" name="search_mode" value="search_address">
        <bean:message key="demographic.demographicaddrecordhtm.formAddress"/> </font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF"> 
          <input type="radio" name="search_mode" value="search_hin">
          <bean:message key="demographic.demographicaddrecordhtm.formHIN"/></font></td>
        
      <td><font size="1" face="Verdana" color="#0000FF">
        <input type="radio"  name="search_mode" value="search_chart_no">
        <bean:message key="demographic.demographicaddrecordhtm.formChartNo"/></font></td>
		</tr>
	</form>
</table>

<table border="0" cellpadding="1" cellspacing="0" width="100%">
  <form method="post" name="adddemographic" action="demographiccontrol.jsp"  onsubmit="return checkTypeIn()">
    <tr> 
      <td align="right"> <b><bean:message key="demographic.demographicaddrecordhtm.formLastName"/><font color="red">:</font> </b></td>
      <td align="left"> 
        <input type="text" name="last_name" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formFirstName"/><font color="red">:</font> </b> </td>
      <td align="left"> 
        <input type="text" name="first_name" onBlur="upCaseCtrl(this)">
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
        <input type="text" name="address"><% if (vLocale.getCountry().equals("BR")) { %>
        <b><bean:message key="demographic.demographicaddrecordhtm.formAddressNo"/>:</b>
        <input type="text" name="address_no" size="6">        
        <%}%>
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formCity"/>: </b></td>
      <td align="left"> 
        <input type="text" name="city">
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
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formprovince"/>: </b> </td>
      <td  align="left"> 
        <input type="text" name="province" value="<%=props.getProperty("billregion", "ON")%>">
      </td>
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPostal"/>: </b> </td>
      <td  align="left"> 
        <input type="text" name="postal" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    
    <tr valign="top"> 
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPhoneHome"/>: </b> </td>
      <td align="left" > 
        <input type="text" name="phone" value="<%=props.getProperty("phoneprefix", "905-")%>">
      </td>
      <td  align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPhoneWork"/>:</b> </td>
      <td  align="left"> 
        <input type="text" name="phone2" value="">
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
        <input type="text" name="ver" value="" size="3">
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
        <select name="hc_type">
          <option value="ON" selected>ON-Ontario</option>
          <option value="AB">AB-Alberta</option>
          <option value="BC">BC-British Columbia</option>
          <option value="MB">MB-Manitoba</option>
          <option value="NF">NF-Newfoundland</option>
          <option value="NB">NB-New Brunswick</option>
          <option value="YT">YT-Yukon</option>
          <option value="NS">NS-Nova Scotia</option>
          <option value="PE">PE-Prince Edward Island</option>
          <option value="SK">SK-Saskatchewan</option>
        </select>
      </td>
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formNurse"/>: </b></td>
      <td> 
        <select name="cust1">
          <option value="" ></option>
          <%
  ResultSet rsdemo = addDemoBean.queryResults("search_provider");
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
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formDoctor"/>: </b></td>
      <td align="left" > 
        <select name="staff">
          <option value="" ></option>
          <%
  rsdemo = addDemoBean.queryResults("search_provider");
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
  rsdemo = addDemoBean.queryResults("search_provider");
  while (rsdemo.next()) { 
%>
          <option value="<%=rsdemo.getString("provider_no")%>"> 
          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",12)%></option>
          <%
  }
  addDemoBean.closePstmtConn();
  
%>
        </select>
      </td>
    </tr>
    <tr valign="top">
      <td align="right" height="10"><b><bean:message key="demographic.demographicaddrecordhtm.formReferalDoctor"/>:</b></td>
      <td align="left" height="10" > 
        <input type="text" name="r_doctor" maxlength="40">
      </td>
      <td align="right" nowrap height="10"><b><bean:message key="demographic.demographicaddrecordhtm.formReferalDoctorN"/>:</b></td>
      <td align="left" height="10"> 
        <input type="text" name="r_doctor_ohip" maxlength="6">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right" nowrap><bean:message key="demographic.demographicaddrecordhtm.formPCNRosterStatus"/><b>:</b> </td>
      <td align="left" > 
        <input type="text" name="roster_status" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right" nowrap><bean:message key="demographic.demographicaddrecordhtm.formPCNDateJoined"/><b>:</b></td>
      <td align="left"> 
        <input type="text" name="hc_renew_date_year" size="4" maxlength="4">
        <input type="text" name="hc_renew_date_month" size="2" maxlength="2">
        <input type="text" name="hc_renew_date_date" size="2" maxlength="2">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b><bean:message key="demographic.demographicaddrecordhtm.formPatientStatus"/>:</b></td>
      <td align="left" > 
        <input type="text" name="patient_status" value="AC" onBlur="upCaseCtrl(this)">
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
 * <font face="Courier New, Courier, mono" size="-1"><bean:message key="demographic.demographicaddrecordhtm.formDateFormat"/> </font> 
</body>
</html:html>
