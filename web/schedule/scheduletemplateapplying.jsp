<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String weekdaytag[] = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
  boolean bAlternate =(request.getParameter("alternate")!=null&&request.getParameter("alternate").equals("checked") )?true:false;
  boolean bOrigAlt = false;

%>
<%@ page import="java.util.*, java.net.*, java.sql.*, oscar.*, oscar.util.*, java.text.*, java.lang.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="scheduleMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="scheduleRscheduleBean" class="oscar.RscheduleBean" scope="session" />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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
<%  if(!scheduleMainBean.getBDoConfigure()) { %>
<%@ include file="scheduleMainBeanConn.jsp" %>  
<% } %>
<%
  String today = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd" );
  String lastYear = (Integer.parseInt(today.substring(0,today.indexOf('-'))) - 1) + today.substring(today.indexOf('-'));
  //System.out.println("last year is: " + lastYear);

  if(request.getParameter("delete")!= null && request.getParameter("delete").equals("1") ) { //delete rschedule
    String[] param =new String[2];
	String edate = null;
    param[0]=request.getParameter("provider_no");
    param[1]=request.getParameter("sdate")!=null?request.getParameter("sdate"):today;
    ResultSet rsgroup = scheduleMainBean.queryResults(param,"search_rschedule_current1");
    while (rsgroup.next()) { 
      param[1]= rsgroup.getString("sdate");
      edate= rsgroup.getString("edate");
    }
    String[] param1 =new String[3];
    param1[0]=param[0];
    param1[1]="1";
    param1[2]=param[1];
    int rowsAffected = scheduleMainBean.queryExecuteUpdate(param1,"delete_rschedule");

	if(request.getParameter("deldate")!= null && (request.getParameter("deldate").equals("b") || request.getParameter("deldate").equals("all")) ) { //delete scheduledate
	  String dbOpt = "delete_scheduledate_all";
	  if(request.getParameter("deldate").equals("b")) dbOpt = "delete_scheduledate_b"; 
      String[] param0 =new String[3];
      param0[0]=param[0];
      param0[1]=param[1] ;
      param0[2]=edate ;
      rowsAffected = scheduleMainBean.queryExecuteUpdate(param0,dbOpt);
	}
    response.sendRedirect("scheduletemplateapplying.jsp?provider_no="+param[0]+"&provider_name="+URLEncoder.encode(request.getParameter("provider_name")) );
  } else {
%>

<% scheduleRscheduleBean.clear(); %>

<head>
<title><bean:message key="schedule.scheduletemplateapplying.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
}
function selectrschedule(s) {
  if(self.location.href.lastIndexOf("&sdate=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&sdate="));
  else a = self.location.href;
	self.location.href = a + "&sdate=" +s.options[s.selectedIndex].value ;
}
function onBtnDelete() {
  if( confirm("<bean:message key="schedule.scheduletemplateapplying.msgDeleteConfirmation"/>") ) {
    self.location.href += "&delete=1&deldate=all" ;
  } else {;}
}

function checkDate(year,month,day) {
	// target must in the format 'Month Day, Year' e.g. 'July 4, 1998'
  var theMonth = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
  var target = theMonth[month-1] +" " +day+", " +year;
  var original = theMonth[month-1] +" 1, " +year;
	var today = new Date(original);
	var tomorrow = new Date(target);
	if(today.getMonth() != tomorrow.getMonth()) { return false; }
	else { return true; }
}
function onChangeDates() {
	if(!checkDate(document.schedule.syear.value,document.schedule.smonth.value,document.schedule.sday.value) ) { alert("<bean:message key="schedule.scheduletemplateapplying.msgIncorrectOutput"/>"); }
}
function onChangeDatee() {
	if(!checkDate(document.schedule.eyear.value,document.schedule.emonth.value,document.schedule.eday.value) ) { alert("<bean:message key="schedule.scheduletemplateapplying.msgIncorrectOutput"/>"); }
}
function onAlternate() {
  if(document.schedule.alternate.checked) {
    a = self.location.href.lastIndexOf("&bFirstDisp=") > 0 ? "":"&bFirstDisp=0"; 
    if(self.location.href.lastIndexOf("&alternate=") > 0 ) c = self.location.href; 
    else c = self.location.href; 
	  self.location.href = c + a + "&alternate=checked" ;
  } else {
    a = self.location.href.lastIndexOf("&bFirstDisp=") > 0 ? "":"&bFirstDisp=0"; 
    if(self.location.href.lastIndexOf("&alternate=") > 0 ) c = self.location.href.substring(0,self.location.href.lastIndexOf("&alternate="));
    else c = self.location.href;
	  self.location.href = c + a ;
  }
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function addDataString() {
  var str="";
  var str1="";
  if(document.schedule.checksun.checked) {
    str += "1 ";
    str1 += "<SUN>"+document.schedule.sunfrom1.value+"</SUN>"; 
  }
  if(document.schedule.checksun.unchecked) {
    str = str.replace("1 ","");
//	str1 = str1.replace();
  }
  if(document.schedule.checkmon.checked) {
    str += "2 ";
	str1 += "<MON>"+document.schedule.monfrom1.value+"</MON>";
  }
  if(document.schedule.checkmon.unchecked) {    str = str.replace("2 ","");  }
  if(document.schedule.checktue.checked) {
    str += "3 ";  
	str1 += "<TUE>"+document.schedule.tuefrom1.value+"</TUE>";
  }
  if(document.schedule.checktue.unchecked) {
    str = str.replace("3 ","");
  }
  if(document.schedule.checkwed.checked) {
    str += "4 ";
	str1 += "<WED>"+document.schedule.wedfrom1.value+"</WED>";
  }
  if(document.schedule.checkwed.unchecked) {    str = str.replace("4 ","");  }
  if(document.schedule.checkthu.checked) {
    str += "5 ";
	str1 += "<THU>"+document.schedule.thufrom1.value+"</THU>";
  }
  if(document.schedule.checkthu.unchecked) {    str = str.replace("5 ","");  }
  if(document.schedule.checkfri.checked) {
    str += "6 ";
	str1 += "<FRI>"+document.schedule.frifrom1.value+"</FRI>";
  }
  if(document.schedule.checkfri.unchecked) {    str = str.replace("6 ","");  }
  if(document.schedule.checksat.checked) {
    str += "7 ";
	str1 += "<SAT>"+document.schedule.satfrom1.value+"</SAT>";
  }
  if(document.schedule.checksat.unchecked) {    str = str.replace("7 ","");  }

	document.schedule.day_of_week.value = str; 
	document.schedule.avail_hour.value = str1; 
	
	if(document.schedule.syear.value=="" || document.schedule.smonth.value=="" || document.schedule.sday.value=="") {
//	  alert("<bean:message key="schedule.scheduletemplateapplying.msgInputDate"/>"); return false;
	} else {
	  return true;
	}
}
function addDataStringB() {
  var strB="";
  var str1B="";
  if(document.schedule.checksun2.checked) {
    strB += "1 ";
    str1B += "<SUN>"+document.schedule.sunfrom2.value+"</SUN>"; 
  }
  if(document.schedule.checksun2.unchecked) {
    strB = strB.replace("1 ","");
//	str1B = str1B.replace();
  }
  if(document.schedule.checkmon2.checked) {
    strB += "2 ";
	str1B += "<MON>"+document.schedule.monfrom2.value+"</MON>";
  }
  if(document.schedule.checkmon2.unchecked) {    strB = strB.replace("2 ","");  }
  if(document.schedule.checktue2.checked) {
    strB += "3 ";  
	str1B += "<TUE>"+document.schedule.tuefrom2.value+"</TUE>";
  }
  if(document.schedule.checktue2.unchecked) {
    strB = strB.replace("3 ","");
  }
  if(document.schedule.checkwed2.checked) {
    strB += "4 ";
	str1B += "<WED>"+document.schedule.wedfrom2.value+"</WED>";
  }
  if(document.schedule.checkwed2.unchecked) {    strB = strB.replace("4 ","");  }
  if(document.schedule.checkthu2.checked) {
    strB += "5 ";
	str1B += "<THU>"+document.schedule.thufrom2.value+"</THU>";
  }
  if(document.schedule.checkthu2.unchecked) {    strB = strB.replace("5 ","");  }
  if(document.schedule.checkfri2.checked) {
    strB += "6 ";
	str1B += "<FRI>"+document.schedule.frifrom2.value+"</FRI>";
  }
  if(document.schedule.checkfri2.unchecked) {    strB = strB.replace("6 ","");  }
  if(document.schedule.checksat2.checked) {
    strB += "7 ";
	str1B += "<SAT>"+document.schedule.satfrom2.value+"</SAT>";
  }
  if(document.schedule.checksat2.unchecked) {    strB = strB.replace("7 ","");  }

	document.schedule.day_of_weekB.value = strB; 
	document.schedule.avail_hourB.value = str1B; 
	if(document.schedule.syear.value=="" || document.schedule.smonth.value=="" || document.schedule.sday.value=="") {
//	  alert("<bean:message key="schedule.scheduletemplateapplying.msgInputDate"/>"); return false;
	} else {
	  return true;
	}
}
function addDataString1() {
  var str="";
	if(document.schedule.syear.value=="" || document.schedule.smonth.value=="" || document.schedule.sday.value=="" || document.schedule.eyear.value=="" || document.schedule.emonth.value=="" || document.schedule.eday.value=="") {
	  alert("<bean:message key="schedule.scheduletemplateapplying.msgInputDate"/>");
	  return false;
	} else if( !checkDate(document.schedule.syear.value,document.schedule.smonth.value,document.schedule.sday.value) || !checkDate(document.schedule.eyear.value,document.schedule.emonth.value,document.schedule.eday.value) || (document.schedule.eyear.value-1)<(document.schedule.syear.value-1) ) {
	  alert("<bean:message key="schedule.scheduletemplateapplying.msgInputCorrectDate"/>");
	  return false;
	} else {
	  return true;
	}
}
//-->
</script>
</head>
<%
  int rowsAffected = 0;
  String[] param1 =new String[2];
  param1[0]=request.getParameter("provider_no");
  //param1[1]="1";
  param1[1]=request.getParameter("sdate")!=null?request.getParameter("sdate"):today;
  ResultSet rsgroup = scheduleMainBean.queryResults(param1,"search_rschedule_current1");
  
  if (rsgroup.next()) { 
    scheduleRscheduleBean.setRscheduleBean(rsgroup.getString("provider_no"),rsgroup.getString("sdate"),rsgroup.getString("edate"), rsgroup.getString("available"),rsgroup.getString("day_of_week"), rsgroup.getString("avail_hourB"), rsgroup.getString("avail_hour"), rsgroup.getString("creator"));
    if(rsgroup.getString("available").equals("A")&&request.getParameter("bFirstDisp")==null) bOrigAlt = true;
    //break;
  } else {
      rsgroup = null;
      rsgroup = scheduleMainBean.queryResults(param1,"search_rschedule_current2");
      if (rsgroup.next()) { 
        scheduleRscheduleBean.setRscheduleBean(rsgroup.getString("provider_no"),rsgroup.getString("sdate"),rsgroup.getString("edate"), rsgroup.getString("available"),rsgroup.getString("day_of_week"), rsgroup.getString("avail_hourB"), rsgroup.getString("avail_hour"), rsgroup.getString("creator"));
        if(rsgroup.getString("available").equals("A")&&request.getParameter("bFirstDisp")==null) bOrigAlt = true;
        //break;
	  }
  }
%>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<form method="post" name="schedule" action="schedulecreatedate.jsp" onSubmit="<%=bAlternate||bOrigAlt?"addDataStringB();":""%>addDataString();return(addDataString1())">

<table border="0" width="100%">
<tr>
      <td width="150" bgcolor="#009966"> 
        <!--left column-->
        <table border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr bgcolor="#486ebd"> 
            <th align="CENTER" bgcolor="#009966">
              <br>
              <p><font face="Helvetica" color="#FFFFFF"><bean:message key="schedule.scheduletemplateapplying.msgMainLabel"/></font></p>
            </th>
    </tr>
  </table>
        <table width="98%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <p><font size="-1"><bean:message key="schedule.scheduletemplateapplying.msgStepOne"/></font></p>
              <p><font size="-1"><bean:message key="schedule.scheduletemplateapplying.msgStepTwo"/></font></p>
              <p><font size="-1"><bean:message key="schedule.scheduletemplateapplying.msgStepThree"/></font></p>
              <p><font size="-1"><bean:message key="schedule.scheduletemplateapplying.msgStepFour"/></font></p>
              <p><font size="-1"><bean:message key="schedule.scheduletemplateapplying.msgObs"/></font></p>
              <p>&nbsp;</p>
            </td>
          </tr>
        </table>

      </td><td>

<center>
<%

//System.out.println(":" + scheduleRscheduleBean.day_of_week + scheduleRscheduleBean.avail_hourB + scheduleRscheduleBean.avail_hour); 
//System.out.println(scheduleRscheduleBean.sdate); 
  String syear = "",smonth="",sday="",eyear="",emonth="",eday="";
  String[] param2 =new String[7];
  for(int i=0; i<7; i++) {param2[i]="";}
  String[][] param3 =new String[7][2];
  for(int i=0; i<7; i++) {
    for(int j=0; j<2; j++) {
	    param3[i][j]="";
	  }
  }
  if(scheduleRscheduleBean.provider_no!="") {
    syear = ""+MyDateFormat.getYearFromStandardDate(scheduleRscheduleBean.sdate);
    smonth = ""+MyDateFormat.getMonthFromStandardDate(scheduleRscheduleBean.sdate);
    sday = ""+MyDateFormat.getDayFromStandardDate(scheduleRscheduleBean.sdate);
    eyear = ""+MyDateFormat.getYearFromStandardDate(scheduleRscheduleBean.edate);
    emonth = ""+MyDateFormat.getMonthFromStandardDate(scheduleRscheduleBean.edate);
    eday = ""+MyDateFormat.getDayFromStandardDate(scheduleRscheduleBean.edate);

    String availhour = scheduleRscheduleBean.avail_hour;
    //String availhourB = scheduleRscheduleBean.avail_hourB;
    
    StringTokenizer st = new StringTokenizer(scheduleRscheduleBean.day_of_week.substring(0,scheduleRscheduleBean.day_of_week.indexOf("|")==-1?scheduleRscheduleBean.day_of_week.length():scheduleRscheduleBean.day_of_week.indexOf("|")) );
    while (st.hasMoreTokens() ) {
      int j = Integer.parseInt(st.nextToken())-1;
	    int i = j==7?0:j;
      param2[i]="checked";
      if(SxmlMisc.getXmlContent(availhour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">") != null) {
	      StringTokenizer sthour = new StringTokenizer(SxmlMisc.getXmlContent(availhour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">"), "^"); //not "-"
        j = 0;
		    while (sthour.hasMoreTokens() ) {
          param3[i][j]=sthour.nextToken(); j++;
        }
	    }
    }
  }

%>
          <table width="99%" border="0" cellspacing="0" cellpadding="0">
            <tr> 
              <td bgcolor="#CCFFCC"><b><%=request.getParameter("provider_name")%></b> 
                <input type="hidden" name="provider_name" value="<%=request.getParameter("provider_name")%>">
              </td>
              <td bgcolor="#CCFFCC" nowrap align="right"> 
                  <select name="select"  onChange="selectrschedule(this)">
<%
  param1[1]=lastYear; //today - query for the future date
  rsgroup = scheduleMainBean.queryResults(param1,"search_rschedule_future1");
 	while (rsgroup.next()) { 
%>
                  <option value="<%=rsgroup.getString("sdate")%>" <%=request.getParameter("sdate")!=null?(rsgroup.getString("sdate").equals(request.getParameter("sdate"))?"selected":""):(rsgroup.getString("sdate").equals(scheduleRscheduleBean.sdate)?"selected":"")%> >
                  <%=rsgroup.getString("sdate")+" ~ "+rsgroup.getString("edate")%></option>
<%
 	}
%>
                  </select>
                <input type="button" name="command" value="<bean:message key="schedule.scheduletemplateapplying.btnDelete"/>" onClick="onBtnDelete()">
              </td>
            </tr>
            <tr> 
              <td colspan="2">&nbsp;</td>
            </tr>
            <tr> 
              <td bgcolor="#CCFFCC" colspan="2"><bean:message key="schedule.scheduletemplateapplying.msgDate"/>: &nbsp; <bean:message key="schedule.scheduletemplateapplying.msgFrom"/><font size="-2"><bean:message key="schedule.scheduletemplateapplying.msgDateFormat"/></font>: 
                <input type="text" name="syear" size="4" maxlength="4" value="<%=syear%>">
                - 
                <input type="text" name="smonth" size="2" maxlength="2" value="<%=smonth%>">
                - 
                <input type="text" name="sday" size="2" maxlength="2" value="<%=sday%>" onChange="onChangeDates()">
                &nbsp; &nbsp; <bean:message key="schedule.scheduletemplateapplying.msgTo"/><font size="-2"><bean:message key="schedule.scheduletemplateapplying.msgDateFormat"/></font>: 
                <input type="text" name="eyear" size="4" maxlength="4" value="<%=eyear%>">
                - 
                <input type="text" name="emonth" size="2" maxlength="2" value="<%=emonth%>">
                - 
                <input type="text" name="eday" size="2" maxlength="2" value="<%=eday%>" onChange="onChangeDatee()">
              </td>
            </tr>
            <tr> 
              <td colspan="2">&nbsp;</td>
            </tr>
            <tr> 
              <td colspan="2"><bean:message key="schedule.scheduletemplateapplying.msgAvaiableEvery"/><font size="-2"> (<bean:message key="schedule.scheduletemplateapplying.msgDayOfWeek"/>): </font>
              <input type="checkbox" name="alternate" value="checked" onClick = "onAlternate()" <%=bOrigAlt||bAlternate?"checked":""%> ><bean:message key="schedule.scheduletemplateapplying.msgAlternateWeekSetting"/></td>
            </tr>
            <tr> 
              <td nowrap align="center" colspan="2"> 
                <table border=2 width=100% cellspacing="0" cellpadding="0">
                <tr><td width=70%>
                
<script language=javascript>
<!--
function tranbutton_click(myfield) {
  var dow = document.schedule;
  if(dow.mytemplate.selectedIndex >-1)  {
    myfield.value = dow.mytemplate.value ;
  }
}
function tranbutton1_click() {
  tranbutton_click(document.schedule.sunfrom1);
}
function tranbutton2_click() {
  tranbutton_click(document.schedule.monfrom1);
}
function tranbutton3_click() {
  tranbutton_click(document.schedule.tuefrom1);
}
function tranbutton4_click() {
  tranbutton_click(document.schedule.wedfrom1);
}
function tranbutton5_click() {
  tranbutton_click(document.schedule.thufrom1);
}
function tranbutton6_click() {
  tranbutton_click(document.schedule.frifrom1);
}
function tranbutton7_click() {
  tranbutton_click(document.schedule.satfrom1);
}
//-->
</script>
                <table border=1 width=100% cellspacing="0" cellpadding="0">
                  <tr bgcolor="#CCFFCC"> 
                    <td> 
                      <p><font size="-1"> 
                        <input type="checkbox" name="checksun" value="1" onClick = "addDataString()" <%=param2[0]%>>
                        <bean:message key="schedule.scheduletemplateapplying.msgSunday"/></font> 
                    </td>
                    <td><font size="-1">
                      <input type="text" name="sunfrom1" size="20" value="<%=param3[0][0]%>" readonly >
                      <input type="button" name="sunto1" value="<<" onclick="javascript:tranbutton1_click();" >
                      </font> </td>
                  </tr>
                  <tr> 
                    <td> <font size="-1"> 
                      <input type="checkbox" name="checkmon" value="2" onClick = "addDataString()" <%=param2[1]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgMonday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="monfrom1" size="20" value="<%=param3[1][0]%>" readonly>
                      <input type="button" name="monto1" value="<<" onclick="javascript:tranbutton2_click();" >
                      </font></td>
                  </tr>
                  <tr bgcolor="#CCFFCC"> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checktue" value="3" onClick = "addDataString()" <%=param2[2]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgTuesday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="tuefrom1" size="20" value="<%=param3[2][0]%>" readonly>
                      <input type="button" name="tueto1" value="<<" onclick="javascript:tranbutton3_click();"  >
                      </font></td>
                  </tr>
                  <tr> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checkwed" value="4" onClick = "addDataString()" <%=param2[3]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgWednesday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="wedfrom1" size="20" value="<%=param3[3][0]%>" readonly>
                      <input type="button" name="wedto1" value="<<" onclick="javascript:tranbutton4_click();" >
                      </font></td>
                  </tr>
                  <tr bgcolor="#CCFFCC"> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checkthu" value="5" onClick = "addDataString()" <%=param2[4]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgThursday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="thufrom1" size="20" value="<%=param3[4][0]%>" readonly>
                      <input type="button" name="thuto1" value="<<" onclick="javascript:tranbutton5_click();" >
                      </font></td>
                  </tr>
                  <tr> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checkfri" value="6" onClick = "addDataString()" <%=param2[5]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgFriday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="frifrom1" size="20" value="<%=param3[5][0]%>" readonly>
                      <input type="button" name="frito1" value="<<" onclick="javascript:tranbutton6_click();" >
                      </font></td>
                  </tr>
                  <tr bgcolor="#CCFFCC"> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checksat" value="7" onClick = "addDataString()" <%=param2[6]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgSaturday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="satfrom1" size="20" value="<%=param3[6][0]%>" readonly>
                      <input type="button" name="satto1" value="<<" onclick="javascript:tranbutton7_click();" >
                      </font></td>
                  </tr>
<%
  if(bOrigAlt && request.getParameter("bFirstDisp")==null || bAlternate && request.getParameter("bFirstDisp")!=null) {
    String availhour = scheduleRscheduleBean.avail_hourB;
    //String availhourB = scheduleRscheduleBean.avail_hourB;
    
    String stToken = "";
    if(scheduleRscheduleBean.day_of_week.indexOf("|")!=-1) stToken = scheduleRscheduleBean.day_of_week.substring(scheduleRscheduleBean.day_of_week.indexOf("|")+1);
//scheduleRscheduleBean.day_of_week.indexOf("|")==-1?scheduleRscheduleBean.day_of_week.length():()    
  for(int i=0; i<7; i++) {param2[i]="";}
  for(int i=0; i<7; i++) {
    for(int j=0; j<2; j++) {
	    param3[i][j]="";
	  }
  }

    StringTokenizer st = new StringTokenizer(stToken );
    while (st.hasMoreTokens() ) {
      int j = Integer.parseInt(st.nextToken())-1;
	    int i = j==7?0:j;
      param2[i]="checked";
      if(SxmlMisc.getXmlContent(availhour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">") != null) {
	      StringTokenizer sthour = new StringTokenizer(SxmlMisc.getXmlContent(availhour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">"), "^"); //not '-'
        j = 0;
		    while (sthour.hasMoreTokens() ) {
          param3[i][j]=sthour.nextToken(); j++;
        }
	    }
    }
  //}
%>
<script language=javascript>
<!--
function tranbuttonb1_click() {
  tranbutton_click(document.schedule.sunfrom2);
}
function tranbuttonb2_click() {
  tranbutton_click(document.schedule.monfrom2);
}
function tranbuttonb3_click() {
  tranbutton_click(document.schedule.tuefrom2);
}
function tranbuttonb4_click() {
  tranbutton_click(document.schedule.wedfrom2);
}
function tranbuttonb5_click() {
  tranbutton_click(document.schedule.thufrom2);
}
function tranbuttonb6_click() {
  tranbutton_click(document.schedule.frifrom2);
}
function tranbuttonb7_click() {
  tranbutton_click(document.schedule.satfrom2);
}
//-->
</script>
                  <tr bgcolor="#00C5CD"> 
                    <td> 
                      <p><font size="-1"> 
                        <input type="checkbox" name="checksun2" value="1" onClick = "addDataString()" <%=param2[0]%>>
                        <bean:message key="schedule.scheduletemplateapplying.msgSunday"/></font> 
                    </td>
                    <td><font size="-1">
                      <input type="text" name="sunfrom2" size="20" value="<%=param3[0][0]%>">
                      <input type="button" name="sunto2" value="<<" onclick="javascript:tranbuttonb1_click();" >
                      </font> </td>
                  </tr>
                  <tr bgcolor="#E0FFFF"> 
                    <td> <font size="-1"> 
                      <input type="checkbox" name="checkmon2" value="2" onClick = "addDataString()" <%=param2[1]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgMonday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="monfrom2" size="20" value="<%=param3[1][0]%>">
                      <input type="button" name="monto2" value="<<" onclick="javascript:tranbuttonb2_click();" >
                      </font></td>
                  </tr>
                  <tr bgcolor="#00C5CD"> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checktue2" value="3" onClick = "addDataString()" <%=param2[2]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgTuesday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="tuefrom2" size="20" value="<%=param3[2][0]%>">
                      <input type="button" name="tueto2" value="<<" onclick="javascript:tranbuttonb3_click();"  >
                      </font></td>
                  </tr>
                  <tr bgcolor="#E0FFFF"> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checkwed2" value="4" onClick = "addDataString()" <%=param2[3]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgWednesday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="wedfrom2" size="20" value="<%=param3[3][0]%>">
                      <input type="button" name="wedto2" value="<<" onclick="javascript:tranbuttonb4_click();" >
                      </font></td>
                  </tr>
                  <tr bgcolor="#00C5CD"> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checkthu2" value="5" onClick = "addDataString()" <%=param2[4]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgThursday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="thufrom2" size="20" value="<%=param3[4][0]%>">
                      <input type="button" name="thuto2" value="<<" onclick="javascript:tranbuttonb5_click();" >
                      </font></td>
                  </tr>
                  <tr bgcolor="#E0FFFF"> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checkfri2" value="6" onClick = "addDataString()" <%=param2[5]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgFriday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="frifrom2" size="20" value="<%=param3[5][0]%>">
                      <input type="button" name="frito2" value="<<" onclick="javascript:tranbuttonb6_click();" >
                      </font></td>
                  </tr>
                  <tr bgcolor="#00C5CD"> 
                    <td><font size="-1"> 
                      <input type="checkbox" name="checksat2" value="7" onClick = "addDataString()" <%=param2[6]%>>
                      <bean:message key="schedule.scheduletemplateapplying.msgSaturday"/></font></td>
                    <td><font size="-1">
                      <input type="text" name="satfrom2" size="20" value="<%=param3[6][0]%>">
                      <input type="button" name="satto2" value="<<" onclick="javascript:tranbuttonb7_click();" >
                      </font></td>
                  </tr>
<% }
%>

                </table>
                
                </td><td>
   <select size=<%=bOrigAlt||bAlternate?22:11%> name="mytemplate" >
	<%
   ResultSet rsdemo = null;
   String param = "Public";
   rsdemo = scheduleMainBean.queryResults(param, "search_scheduletemplate");
   while (rsdemo.next()) { 
	%>
        <option value="<%=rsdemo.getString("name")%>"><%=rsdemo.getString("name")+" |"+rsdemo.getString("summary")%></option>
  <%
   }
   param =request.getParameter("provider_no");
   rsdemo = scheduleMainBean.queryResults(param, "search_scheduletemplate");
   while (rsdemo.next()) { 
	%>
        <option value="<%=rsdemo.getString("name")%>"><%=rsdemo.getString("name")+" |"+rsdemo.getString("summary")%></option>
  <% }	%>
   </select>             
                
                </td></tr>
                </table>

              </td>
              <input type="hidden" name="day_of_week" value="">
              <input type="hidden" name="avail_hour" value="">
              <input type="hidden" name="day_of_weekB" value="">
              <input type="hidden" name="avail_hourB" value="">
            </tr>
            <tr> 
              <td colspan="2">&nbsp;</td>
            </tr>
            <tr> 
              <td colspan="2">&nbsp;</td>
            </tr>
            <tr> 
              <td bgcolor="#CCFFCC" colspan="2"> 
                <div align="right"> 
                  <input type="hidden" name="provider_no" value="<%=request.getParameter("provider_no")%>">
                  <input type="hidden" name="available" value="<%=bAlternate||bOrigAlt?"A":"1"%>">
                  <input type="hidden" name="Submit" value=" Next ">
                  <input type="submit" value='<bean:message key="schedule.scheduletemplateapplying.btnNext"/>'>
                  <input type="button" value='<bean:message key="schedule.scheduletemplateapplying.btnCancel"/>' onclick="window.close()">
                </div>
              </td>
            </tr>
          </table>
<p> 

          <p>&nbsp;</p>
        </center>
  </td></tr>
</table>


</form>
<%
} //end if
   //scheduleMainBean.closePstmtConn();
%>
</body>
</html:html>
