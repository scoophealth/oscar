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

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String str = null;
%> 
<%@ page import="java.lang.*, java.util.*, java.text.*,java.sql.*, oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="addDemoBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
    {"search_provider", "select * from provider order by ?"},
  };
  String[][] responseTargets=new String[][] {  };
  addDemoBean.doConfigure(dbParams,dbQueries,responseTargets);
%>

<html>
<head>
<title> ADD A DEMOGRAPHIC RECORD</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.adddemographic.last_name.focus();
  document.adddemographic.last_name.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function showDate(){
  var now=new Date();
  var year=now.getYear();
  var month=now.getMonth()+1;
  var date=now.getDate();
  //var DateVal=""+year+"-"+month+"-"+date;
  document.adddemographic.date_joined_year.value=year;
  document.adddemographic.date_joined_month.value=month;
  document.adddemographic.date_joined_date.value=date;
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
	if(!typeInOK) alert ("You must type in the following fields: Last Name, First Name, DOB, and Sex.");
	return typeInOK;
}
//-->
</script>
</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus();showDate();"  topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
	  <tr bgcolor="#CCCCFF"> 
      <th><font face="Helvetica">ADD A DEMOGRAPHIC RECORD</font></th>
	  </tr>
	</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#EEEEFF">
	<form method="post" name="titlesearch" action="demographiccontrol.jsp" onSubmit="checkTypeIn()">
		<tr valign="top"><td rowspan="2" ALIGN="right" valign="middle"> <font face="Verdana" color="#0000FF"><b><i>Search 
        </i></b></font></td>
			
      <td width="10%" nowrap><font size="1" face="Verdana" color="#0000FF"> 
        <input type="radio"  checked name="search_mode" value="search_name">
        Name </font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF"> 
          <input type="radio"  name="search_mode" value="search_phone">
          Phone</font></td> 
        <td nowrap><font size="1" face="Verdana" color="#0000FF">
          <input type="radio"  name="search_mode" value="search_dob">
          DOB(yyyymmdd)</font></td> 
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
        Address </font></td>
        <td nowrap><font size="1" face="Verdana" color="#0000FF"> 
          <input type="radio" name="search_mode" value="search_hin">
          HIN</font></td>
        
      <td><font size="1" face="Verdana" color="#0000FF">
        <input type="radio"  name="search_mode" value="search_chart_no">
        Chart No</font></td>
		</tr>
	</form>
</table>

<table border="0" cellpadding="1" cellspacing="0" width="100%">
  <form method="post" name="adddemographic" action="demographiccontrol.jsp"  onSubmit="return(checkTypeIn())">
    <tr> 
      <td align="right"> <b>Last Name<font color="red">:</font> </b></td>
      <td align="left"> 
        <input type="text" name="last_name" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right"><b>First Name<font color="red">:</font> </b> </td>
      <td align="left"> 
        <input type="text" name="first_name" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <tr valign="top"> 
      <td  align="right"> <b>Address: </b></td>
      <td align="left" > 
        <input type="text" name="address">
      </td>
      <td align="right"><b>City: </b></td>
      <td align="left"> 
        <input type="text" name="city">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b>Province: </b> </td>
      <td  align="left"> 
        <input type="text" name="province" value="ON">
      </td>
      <td  align="right"><b>Postal: </b> </td>
      <td  align="left"> 
        <input type="text" name="postal" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <tr valign="top"> 
      <td  align="right"><b>Phone (H): </b> </td>
      <td align="left" > 
        <input type="text" name="phone" value="905-">
      </td>
      <td  align="right"><b>Phone (W):</b> </td>
      <td  align="left"> 
        <input type="text" name="phone2" value="">
      </td>
    </tr>
    <tr valign="top"> 
      <td  align="right"><b>DOB</b><font size="-2">(yyyymmdd)</font><b><font color="red">:</font></b></td>
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
                <option value="01">1 
                <option value="02">2 
                <option value="03">3 
                <option value="04">4 
                <option value="05">5 
                <option selected value="06">6 
                <option value="07">7 
                <option value="08">8 
                <option value="09">9 
                <option value="10">10 
                <option value="11">11 
                <option value="12">12 
              </select>
            </td>
            <td>-</td>
            <td> 
              <!--input type="text" name="date_of_birth" size="2" maxlength="2"-->
              <select name="date_of_birth">
                <option value="1">1 
                <option value="2">2 
                <option value="3">3 
                <option value="4">4 
                <option value="5">5 
                <option value="6">6 
                <option value="7">7 
                <option value="8">8 
                <option value="9">9 
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
      <td  align="right"><b>Sex<font color="red">:</font></b></td>
      <td align="left"> 
        <select name="sex">
          <option value="F" selected>F</option>
          <option value="M">M</option>
        </select>
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b>HIN: </b></td>
      <td align="left" nowrap > 
        <input type="text" name="hin" size="15">
        <b>Ver: 
        <input type="text" name="ver" value="" size="3">
        </b></td>
      <td align="right"><b>*EFF Date</b><b>: </b></td>
      <td align="left"><b> 
        <input type="text" name="eff_date_year" size="4" maxlength="4">
        <input type="text" name="eff_date_month" size="2" maxlength="2">
        <input type="text" name="eff_date_date" size="2" maxlength="2">
        </b></td>
    </tr>
    <tr> 
      <td align="right"><b>HC Type: </b></td>
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
      <td align="right"><b>Nurse: </b></td>
      <td> 
        <select name="cust1">
          <option value="" ></option>
          <%
  ResultSet rsdemo = addDemoBean.queryResults("last_name", "search_provider");
  while (rsdemo.next()) { 
%>
          <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(curUser_no)?"selected":""%> > 
          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",12)%></option>
          <%
  }
%>
        </select>
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b>Doctor: </b></td>
      <td align="left" > 
        <select name="staff">
          <option value="" ></option>
          <%
  rsdemo = addDemoBean.queryResults("last_name", "search_provider");
  while (rsdemo.next()) { 
%>
          <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(curUser_no)?"selected":""%> > 
          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",12)%></option>
          <%
  }
%>
        </select>
      </td>
      <td align="right"><b>Resident: </b> </td>
      <td align="left"> 
        <select name="cust2">
          <option value="" ></option>
          <%
  rsdemo = addDemoBean.queryResults("last_name", "search_provider");
  while (rsdemo.next()) { 
%>
          <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(curUser_no)?"selected":""%> > 
          <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",12)%></option>
          <%
  }
  addDemoBean.closePstmtConn();
  
%>
        </select>
      </td>
    </tr>
    <tr valign="top">
      <td align="right" height="10"><b>Referral Doctor:</b></td>
      <td align="left" height="10" > 
        <input type="text" name="r_doctor" maxlength="40">
      </td>
      <td align="right" nowrap height="10"><b>Referral Doctor #:</b></td>
      <td align="left" height="10"> 
        <input type="text" name="r_doctor_ohip" maxlength="6">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right" nowrap><b><font size="-2">PCN</font><font size="-1">Roster 
        Status</font>: </b> </td>
      <td align="left" > 
        <input type="text" name="roster_status" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right" nowrap><b><font size="-2">PCN </font><font size="-1">Date 
        Joined</font>:</b></td>
      <td align="left"> 
        <input type="text" name="hc_renew_date_year" size="4" maxlength="4">
        <input type="text" name="hc_renew_date_month" size="2" maxlength="2">
        <input type="text" name="hc_renew_date_date" size="2" maxlength="2">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b>Patient Status:</b></td>
      <td align="left" > 
        <input type="text" name="patient_status">
      </td>
      <td align="right"><b>Chart No.:</b></td>
      <td align="left"> 
        <input type="text" name="chart_no" value="">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b>*Date Joined</b><b>: </b></td>
      <td align="left" > 
        <input type="text" name="date_joined_year" size="4" maxlength="4">
        <input type="text" name="date_joined_month" size="2" maxlength="2">
        <input type="text" name="date_joined_date" size="2" maxlength="2">
      </td>
      <td align="right"><b>*End Date</b><b>: </b> </td>
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
            <td width="10%" align="right"><font color="#FF0000"><b>Alert: </b></font> 
            </td>
            <td> 
              <textarea name="cust3" style="width:100%" rows="2" ></textarea>
            </td>
          </tr>
          <tr> 
            <td align="right"><b>Notes : </b></td>
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
          <input type="submit" name="displaymode" value="Add Record"">
          <input type="button" name="Button" value="Swipe Card" onclick="window.open('zadddemographicswipe.htm','', 'scrollbars=yes,resizable=yes,width=600,height=300')";>
          &nbsp; 
          <input type="button" name="Button" value="Cancel" onclick=self.close();>
        </div>
      </td>
    </tr>
  </form>
</table>
 * <font face="Courier New, Courier, mono" size="-1">Date format: yyyy-mm-dd </font> 
</body>
</html>