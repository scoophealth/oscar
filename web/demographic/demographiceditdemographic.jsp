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
  String curProvider_no = (String) session.getAttribute("user");
  String demographic_no = request.getParameter("demographic_no") ;
  String userfirstname = (String) session.getAttribute("userfirstname");
  String userlastname = (String) session.getAttribute("userlastname");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
  String str = null;
  int nStrShowLen = 14;
%>

<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<html>
<head>
<title>PATIENT DETAIL INFO</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">
<!--
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
//<!--oscarMessenger code block-->
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
//<!--/oscarMessenger code block -->
function checkTypeIn() {
  var dob = document.titlesearch.keyword ;
  if(document.titlesearch.search_mode[2].checked) {
    if(dob.value.length==8) {
      dob.value = dob.value.substring(0, 4)+"-"+dob.value.substring(4, 6)+"-"+dob.value.substring(6, 8);
      //alert(dob.value.length);
    }
    if(dob.value.length != 10) {
      alert("You have a wrong DOB input!!!");
    }
  }
}
function checkPhoneNum() {
  var typeIn = document.updatedelete.phone.value ;
  while(typeIn.indexOf('-') > 0 ) {
    typeIn = typeIn.replace("-", "") ;
  }
  document.updatedelete.phone.value = typeIn ;
}

//-->
</script>



</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="<%=deepcolor%>"><th><font face="Helvetica">PATIENT'S DETAIL RECORD</font></th></tr>
</table>

<%@ include file="zdemographicfulltitlesearch.htm" %>

<%

 //----------------------------REFERRAL DOCTOR------------------------------
 
 String rdohip="", rd="", fd="";
 //----------------------------REFERRAL DOCTOR --------------end-----------
 
  String resident="", nurse="", alert="", notes="";
  ResultSet rs = null;
  rs = apptMainBean.queryResults(demographic_no, "search_demographiccust");
  while (rs.next()) {
    resident= rs.getString("cust1");
    nurse= rs.getString("cust2");
    alert= rs.getString("cust3");
    notes= SxmlMisc.getXmlContent(rs.getString("content"),"unotes") ;
	notes=notes==null?"":notes; 
  }

  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0, dob_year=0, dob_month=0, dob_date=0;
  
  int param = Integer.parseInt(demographic_no);
  // System.out.println("from editcpp : "+ param);
 
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
          }else{
              rd = SxmlMisc.getXmlContent(rs.getString("family_doctor"),"rd")    ;
                
          rdohip = SxmlMisc.getXmlContent(rs.getString("family_doctor"),"rdohip");
          }
          //----------------------------REFERRAL DOCTOR --------------end-----------
    

    
    
    dob_year = Integer.parseInt(rs.getString("year_of_birth"));
      dob_month = Integer.parseInt(rs.getString("month_of_birth"));
      dob_date = Integer.parseInt(rs.getString("date_of_birth"));
      if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
/*  if(!(rs.getString("month_of_birth").equals(""))) {//   ||rs.getString("year_of_birth")||rs.getString("date_of_birth")) {    	if(curMonth>Integer.parseInt(rs.getString("month_of_birth"))) {    		age=curYear-Integer.parseInt(rs.getString("year_of_birth"));    	} else {    		if(curMonth==Integer.parseInt(rs.getString("month_of_birth")) &&    			curDay>Integer.parseInt(rs.getString("date_of_birth"))) {    			age=curYear-Integer.parseInt(rs.getString("year_of_birth"));    		} else {    			age=curYear-Integer.parseInt(rs.getString("year_of_birth"))-1;     		}    	}	     }
*/   
%> 
<table width="100%" border="1" cellspacing="0" cellpadding="1" bgcolor="#CCCCFF" bordercolor="#669966">
  <tr>
    <!--th><a href="#" onClick="popupPage(500,600,'demographicsummary.jsp?demographic_no=<%=rs.getString("demographic_no")%>')">Patient Summary</a> </th-->
    <th><a href="#" onClick="popupPage(500,600,'../dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=rs.getString("demographic_no")%>&curUser=<%=curProvider_no%>')">Documents</a></th>
    <th><a href="../e_form/ShowMyForm.jsp?demographic_no=<%=demographic_no%>">E-Forms</a></th>
    <th><a href='demographiccontrol.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10'>Appt.History</a></th>
    <th>
      <!--a href="#" onClick="popupPage(500,600,'../billing/billinghistory.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10')">Billing History</a-->
      <a href='../billing/billinghistory.jsp?demographic_no=<%=rs.getString("demographic_no")%>&last_name=<%=URLEncoder.encode(rs.getString("last_name"))%>&first_name=<%=URLEncoder.encode(rs.getString("first_name"))%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10'>Billing History</a></th>
  </tr>
</table> 

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <form method="post" name="updatedelete" id="updatedelete" action="demographiccontrol.jsp">
    <input type="hidden" name="demographic_no" value="<%=rs.getString("demographic_no")%>">
    <tr> 
      <td align="right" title='<%=rs.getString("demographic_no")%>'> <b>Last Name: </b></td>
      <td align="left"> 
        <input type="text" name="last_name" value="<%=rs.getString("last_name")%>" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right"><b>First Name: </b> </td>
      <td align="left"> 
        <input type="text" name="first_name" value="<%=rs.getString("first_name")%>" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <tr valign="top"> 
      <td  align="right"> <b>Address: </b></td>
      <td align="left" > 
        <input type="text" name="address" value="<%=rs.getString("address")%>">
      </td>
      <td align="right"><b>City: </b></td>
      <td align="left"> 
        <input type="text" name="city" value="<%=rs.getString("city")%>">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b>Province: </b> </td>
      <td  align="left"> 
        <input type="text" name="province" value="<%=rs.getString("province")%>">
      </td>
      <td  align="right"><b>Postal: </b> </td>
      <td  align="left"> 
        <input type="text" name="postal" value="<%=rs.getString("postal")%>" onBlur="upCaseCtrl(this)">
      </td>
    </tr>
    <tr valign="top"> 
      <td  align="right"><b>Phone(H): </b> </td>
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
      <td  align="right"><b>Phone(W):</b> </td>
      <td  align="left"> 
        <input type="text" name="phone2" value="<%=rs.getString("phone2")%>">
      </td>
    </tr>
    <tr valign="top"> 
      <td  align="right"><b>DOB</b><font size="-2">(yyyymmdd)</font><b>:</b> </td>
      <td  align="left" nowrap> 
        <input type="text" name="year_of_birth" value="<%=rs.getString("year_of_birth")%>" size="4" maxlength="4">
        <input type="text" name="month_of_birth" value="<%=rs.getString("month_of_birth")%>" size="2" maxlength="2">
        <input type="text" name="date_of_birth" value="<%=rs.getString("date_of_birth")%>" size="2" maxlength="2">
        <b>Age: 
        <input type="text" name="age" readonly value="<%=age%>" size="3">
        </b> </td>
      <td  align="right" nowrap><b>Sex:</b> </td>
      <td align="left">
        <input type="text" name="sex" value="<%=rs.getString("sex")%>" onBlur="upCaseCtrl(this)" size="1" maxlength="1">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b>HIN: </b></td>
      <td align="left" nowrap> 
        <input type="text" name="hin" value="<%=rs.getString("hin")%>" size="15">
        <b>Ver.</b> 
        <input type="text" name="ver" value="<%=rs.getString("ver")%>" size="3">
      </td>
      <td align="right"><b>EFF Date:</b></td>
      <td align="left">
        <input type="text" name="eff_date_year" size="4" maxlength="4" value="<%=MyDateFormat.getYearFromStandardDate(rs.getString("eff_date"))%>">
        <input type="text" name="eff_date_month" size="2" maxlength="2" value="<%=MyDateFormat.getMonthFromStandardDate(rs.getString("eff_date"))%>">
        <input type="text" name="eff_date_date" size="2" maxlength="2" value="<%=MyDateFormat.getDayFromStandardDate(rs.getString("eff_date"))%>">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b>HC Type:</b> </td>
      <td align="left" > 
        <!--input type="text" name="hc_type" value=""-->
		<% String hctype = rs.getString("hc_type")==null?"":rs.getString("hc_type"); %>
        <select name="hc_type">
          <option value="<%=hctype%>" selected><%=hctype%></option>
          <option value="ON" <%=hctype.equals("ON")?"selected":"" %> >ON-Ontario</option>
          <option value="AB" <%=hctype.equals("AB")?"selected":"" %> >AB-Alberta</option>
          <option value="BC" <%=hctype.equals("BC")?"selected":"" %> >BC-British Columbia</option>
          <option value="MB" <%=hctype.equals("MB")?"selected":"" %> >MB-Manitoba</option>
          <option value="NF" <%=hctype.equals("NF")?"selected":"" %> >NF-Newfoundland</option>
          <option value="NB" <%=hctype.equals("NB")?"selected":"" %> >NB-New Brunswick</option>
          <option value="YT" <%=hctype.equals("YT")?"selected":"" %> >YT-Yukon</option>
          <option value="NS" <%=hctype.equals("NS")?"selected":"" %> >NS-Nova Scotia</option>
          <option value="PE" <%=hctype.equals("PE")?"selected":"" %> >PE-Prince Edward Island</option>
          <option value="SK" <%=hctype.equals("SK")?"selected":"" %> >SK-Saskatchewan</option>
        </select>
		      </td>
      <td align="right" nowrap  bgcolor='ivory'><b>Nurse: </b> </td>
      <td align="left" bgcolor='ivory'>
        <select name="resident">
          <option value="" ></option>
<%
  ResultSet rsdemo = apptMainBean.queryResults("last_name", "search_provider");
  while (rsdemo.next()) { 
%>
  <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(resident)?"selected":""%> >
  <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
<% } %>
        </select>
      </td>
    </tr>
    <tr valign="top" bgcolor='ivory'> 
      <td align="right" nowrap><b>Doctor: </b></td>
      <td align="left" > 
        <select name="provider_no">
          <option value="" ></option>
<%
  rsdemo = apptMainBean.queryResults("last_name", "search_provider");
  while (rsdemo.next()) { 
%>
  <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(rs.getString("provider_no"))?"selected":""%> >
  <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
<% } %>
        </select>
      </td>
      <td align="right"><b>Resident:</b> </td>
      <td align="left">
        <select name="nurse">
          <option value="" ></option>
<%
  rsdemo = apptMainBean.queryResults("last_name", "search_provider");
  while (rsdemo.next()) { 
%>
  <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(nurse)?"selected":""%> >
  <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%></option>
<% } %>
        </select>
      </td>
    </tr>
    
    <tr valign="top">
          <td align="right" nowrap><b>Referral Doctor: </b></td>
          <td align="left" >
            <input type="text" name="r_doctor" maxlength="40" value="<%=rd%>">
          </td>
          <td align="right" nowrap><b>Referral Doctor #: </b></td>
          <td align="left">
            <input type="text" name="r_doctor_ohip" maxlength="6" value="<%=rdohip%>">
          </td>
    </tr>
    
    <tr valign="top"> 
      <td align="right" nowrap><b><font size="-2">PCN </font><font size="-1">Roster 
        Status</font>:</b></td>
      <td align="left" > 
        <input type="text" name="roster_status" value="<%=rs.getString("roster_status")%>" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right" nowrap><b><font size="-2">PCN </font><font size="-1">Date 
        Joined</font>: </b></td>
      <td align="left">
        <input type="text" name="hc_renew_date_year" size="4" maxlength="4" value="<%=MyDateFormat.getYearFromStandardDate(rs.getString("hc_renew_date"))%>">
        <input type="text" name="hc_renew_date_month" size="2" maxlength="2" value="<%=MyDateFormat.getMonthFromStandardDate(rs.getString("hc_renew_date"))%>">
        <input type="text" name="hc_renew_date_date" size="2" maxlength="2" value="<%=MyDateFormat.getDayFromStandardDate(rs.getString("hc_renew_date"))%>">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right"><b>Patient Status:</b> <b> </b></td>
      <td align="left" > 
        <input type="text" name="patient_status" value="<%=rs.getString("patient_status")%>">
      </td>
      <td align="right" bgcolor='<%=deepcolor%>'><b>Chart No.:</b> </td>
      <td align="left" bgcolor='<%=deepcolor%>'> 
        <input type="text" name="chart_no" value="<%=rs.getString("chart_no")%>">
      </td>
    </tr>
    <tr valign="top"> 
      <td align="right" nowrap><b>Date Joined: </b></td>
      <td align="left" > 
        <input type="text" name="date_joined_year" size="4" maxlength="4" value="<%=MyDateFormat.getYearFromStandardDate(rs.getString("date_joined"))%>">
        <input type="text" name="date_joined_month" size="2" maxlength="2" value="<%=MyDateFormat.getMonthFromStandardDate(rs.getString("date_joined"))%>">
        <input type="text" name="date_joined_date" size="2" maxlength="2" value="<%=MyDateFormat.getDayFromStandardDate(rs.getString("date_joined"))%>">
      </td>
      <td align="right"><b>End Date: </b></td>
      <td align="left"> 
        <input type="text" name="end_date_year" size="4" maxlength="4" value="<%=MyDateFormat.getYearFromStandardDate(rs.getString("end_date"))%>">
        <input type="text" name="end_date_month" size="2" maxlength="2" value="<%=MyDateFormat.getMonthFromStandardDate(rs.getString("end_date"))%>">
        <input type="text" name="end_date_date" size="2" maxlength="2" value="<%=MyDateFormat.getDayFromStandardDate(rs.getString("end_date"))%>">
      </td>
    <tr valign="top"> 
      <td nowrap colspan="4">
	    <table width="100%" bgcolor="#EEEEFF">
          <tr>
	        <td width="10%" align="right"><font color="#FF0000"><b>Alert: </b></font> 
            </td>
        <td>
              <textarea name="alert" style="width:100%" cols="80" rows="2"><%=alert%></textarea>
            </td>
	  </tr><tr>
            <td align="right"><b>Notes : </b></td>
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
              <input type="button" name="Button" value=" Back " onclick=history.go(-1);><input type="button" name="Button" value="Cancel" onclick=self.close();>
            </td>
            <td  width="30%" align='center'> 
              <input type="submit" name="displaymode" value="Update Record" onClick="checkPhoneNum()">
            </td>
            <td width="40%" align='right'> 
               <input type="button" name="Button" value="Swipe Card" onclick="window.open('zdemographicswipe.htm','', 'scrollbars=yes,resizable=yes,width=600,height=300, top=360, left=0')";>
              <input type="button" name="Button" value="Print Label" onclick="window.location='demographiclabelprintsetting.jsp?demographic_no=<%=rs.getString("demographic_no")%>'";>
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
    <th><a href="../e_form/MyForm.jsp?demographic_no=<%=demographic_no%>" > Add E-Form </a></th>
    <th><a href=# onclick="window.open('../dms/adddocument.jsp?function=demographic&functionid=<%=rs.getString("demographic_no")%>&creator=<%=curProvider_no%>','', 'scrollbars=yes,resizable=yes,width=600,height=300');return false;">Add Document</a></th>
    <th><!--a href=# onclick="popupPage(600,800,'../provider/providercontrol.jsp?appointment_no=&demographic_no=<%=demographic_no%>&curProvider_no=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&username=&appointment_date=&start_time=&status=&displaymode=encounter&dboperation=search_demograph&template=');return false;" title="Tel-Progress Notes">Add Encounter</a-->
    <a href=# onClick="popupPage(700,980,'../oscarEncounter/IncomingEncounter.do?providerNo=<%=curProvider_no%>&appointmentNo=&demographicNo=<%=demographic_no%>&curProviderNo=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&userName=<%=URLEncoder.encode( userfirstname+" "+userlastname) %>&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&appointmentDate=&startTime=&status=');return false;" title="E-Chart">
            E-Chart</a></th>
    <th><a href=# onclick="popupPage(700, 1000, '../billing/billingOB.jsp?billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=&appointment_no=0&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&demographic_no=<%=rs.getString("demographic_no")%>&providerview=1&user_no=<%=curProvider_no%>&apptProvider_no=none&appointment_date=0000-00-00&start_time=0:00&bNewForm=1');return false;" title="bill a patient">Add Billing</a></th>
    <th><a href=# onclick="window.open('../billing/specialtyBilling/fluBilling/addFluBilling.jsp?function=demographic&functionid=<%=rs.getString("demographic_no")%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&hin=<%=URLEncoder.encode(rs.getString("hin"))%><%=URLEncoder.encode(rs.getString("ver"))%>&demo_sex=<%=URLEncoder.encode(rs.getString("sex"))%>&demo_hctype=<%=URLEncoder.encode(rs.getString("hc_type")==null?"null":rs.getString("hc_type"))%>&rd=<%=URLEncoder.encode(rd==null?"null":rd)%>&rdohip=<%=URLEncoder.encode(rdohip==null?"null":rdohip)%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(rs.getString("year_of_birth")),Integer.parseInt(rs.getString("month_of_birth")),Integer.parseInt(rs.getString("date_of_birth")))%>','', 'scrollbars=yes,resizable=yes,width=640,height=400');return false;" title='Add Flu Billing'>Flu Billing</a></th>
   
    <th><a href=# onclick="window.open('../billing/inr/addINRbilling.jsp?function=demographic&functionid=<%=rs.getString("demographic_no")%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(rs.getString("last_name"))%>%2C<%=URLEncoder.encode(rs.getString("first_name"))%>&hin=<%=URLEncoder.encode(rs.getString("hin"))%><%=URLEncoder.encode(rs.getString("ver"))%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(rs.getString("year_of_birth")),Integer.parseInt(rs.getString("month_of_birth")),Integer.parseInt(rs.getString("date_of_birth")))%>','', 'scrollbars=yes,resizable=yes,width=600,height=400');return false;" title='Add INR Billing'>Add INR</a></th>
        <th><a href=# onclick="window.open('../billing/inr/reportINR.jsp?provider_no=<%=curProvider_no%>','', 'scrollbars=yes,resizable=yes,width=600,height=600');return false;" title='INR Billing'>Bill INR</a></th>
     <th><!--<a href=# onClick="popupOscarRx(700,960,'../../oscarRx/choosePatient.do?providerNo=<%=curProvider_no%>&demographicNo=<%=demographic_no%>')">Prescription</a>--><a href=# onClick="popupOscarRx(700,960,'../packageNA.jsp?pkg=oscarRx')">Prescription</a></th>
<th>
<a href=# onclick="popupPage(100,355,'../oscarEncounter/oscarConsultationRequest/ConsultChoice.jsp?de=<%=rs.getString("demographic_no")%>&proNo=<%=rs.getString("provider_no")%>')">Consultations</a></th>
</th>  
</tr> 
</table>            <!--a href=# onClick="popupPage(600,750,'../provider/providercontrol.jsp?appointment_no=&demographic_no=<%--=demographic_no--%>&curProvider_no=&reason=<%=URLEncoder.encode("Tel-Progress Notes")%>&username=&appointment_date=&start_time=&status=&displaymode=encounter&dboperation=search_demograph&template=');return false;" title="Tel-Progress Notes">
            <img src="../images/encounter.gif" border="0" height="15"></a> |            
            <a href=# onClick="popupPage(700, 720, '../billing/billingOB.jsp?hotclick=&appointment_no=0&demographic_name=<%--=rs.getString("last_name")--%>%2C<%--=rs.getString("first_name")--%>&demographic_no=<%--=rs.getString("demographic_no")--%>&providerview=1&user_no=<%--=curProvider_no--%>&apptProvider_no=none&appointment_date=0000-00-00&start_time=0:00&bNewForm=1');return false;" title="bill a patient"> $ </a> |       -->   

<%
    }
  }
  apptMainBean.closePstmtConn();
%>
</body>
</html>
