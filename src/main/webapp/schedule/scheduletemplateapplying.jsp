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
<%@ page
	import="java.util.*, java.net.*, java.sql.*, oscar.*, oscar.util.*, java.text.*, java.lang.*, org.apache.struts.util.*"
	errorPage="../appointment/errorpage.jsp"%>

<jsp:useBean id="scheduleRscheduleBean" class="oscar.RscheduleBean"	scope="session" />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ScheduleDate" %>
<%@ page import="org.oscarehr.common.dao.ScheduleDateDao" %>
<%@ page import="org.oscarehr.common.model.RSchedule" %>
<%@ page import="org.oscarehr.common.dao.RScheduleDao" %>
<%@ page import="org.oscarehr.common.model.ScheduleTemplate" %>
<%@ page import="org.oscarehr.common.dao.ScheduleTemplateDao" %>
<%
	ScheduleDateDao scheduleDateDao = SpringUtils.getBean(ScheduleDateDao.class);
	RScheduleDao rScheduleDao = (RScheduleDao)SpringUtils.getBean("rScheduleDao");
	ScheduleTemplateDao scheduleTemplateDao = SpringUtils.getBean(ScheduleTemplateDao.class);
%>
<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%><html:html locale="true">

<%
    if(session.getAttribute("user") == null ) response.sendRedirect("../logout.jsp");
    String CurProviderNo = (String) session.getAttribute("user");

    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String CurRoleName = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

    boolean isSiteAccessPrivacy=false;
%>


<security:oscarSec objectName="_site_access_privacy" roleName="<%=CurRoleName%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>


<%!  boolean bMultisites=org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>
<%!  String [] bgColors; %>
<%!  List<String> excludedSites = new ArrayList<String>(); %>
<%

  String weekdaytag[] = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
  boolean bAlternate =(request.getParameter("alternate")!=null&&request.getParameter("alternate").equals("checked") )?true:false;
  boolean bOrigAlt = false;

  OscarProperties props = OscarProperties.getInstance();

  boolean bMoreAddr = bMultisites
  						? true
  						: (props.getProperty("scheduleSiteID", "").equals("") ? false : true);
  String [] addr;

  if (bMultisites) {
	//multisite starts =====================
	  SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
      List<Site> sites = siteDao.getActiveSitesByProviderNo(request.getParameter("provider_no"));
      List<Site> managerSites;

	  if (isSiteAccessPrivacy) {
		  // login user have site manager role
		  managerSites = siteDao.getActiveSitesByProviderNo(CurProviderNo);
		  //build excluded sites list for sites that not in current site manager
		  for (Site site : sites) {
			  if (!managerSites.contains(site)) {
				  excludedSites.add(site.getName());
			  }
		  }

	  }

	  //login user have admin role
	  addr = new String[sites.size()+1];
	  bgColors = new String[sites.size()+1];

      for (int i=0; i<sites.size(); i++) {
		  addr[i]=sites.get(i).getName();
		  bgColors[i]=sites.get(i).getBgColor();
      }
	  addr[sites.size()]="NONE";
	  bgColors[sites.size()]="white";


	//multisite ends =====================
  } else {
  	  addr = props.getProperty("scheduleSiteID", "").split("\\|");
  }

%>
<%
  String today = UtilDateUtilities.DateToString(new java.util.Date(), "yyyy-MM-dd" );
  String lastYear = (Integer.parseInt(today.substring(0,today.indexOf('-'))) - 2) + today.substring(today.indexOf('-'));

  if(request.getParameter("delete")!= null && request.getParameter("delete").equals("1") ) { //delete rschedule
	
	String[] param =new String[2];
	String edate = null;
    param[0]=request.getParameter("provider_no");
    param[1]=request.getParameter("sdate")!=null?request.getParameter("sdate"):today;
    RSchedule rs1 = rScheduleDao.search_rschedule_current1(request.getParameter("provider_no"), ConversionUtils.fromDateString(request.getParameter("sdate")!=null?request.getParameter("sdate"):today));
    
    
    if (rs1 != null) {
      param[1]= ConversionUtils.toDateString(rs1.getsDate());
      edate= ConversionUtils.toDateString(rs1.geteDate());
    }
    
     List<RSchedule> rsl = rScheduleDao.findByProviderNoAndDates(request.getParameter("provider_no"),MyDateFormat.getSysDate(request.getParameter("sdate")!=null?request.getParameter("sdate"):today));
 
   	for(RSchedule rs:rsl) {
   		rs.setStatus("D");
   		rScheduleDao.merge(rs);
   	}
   	
    rsl = rScheduleDao.findByProviderAvailableAndDate(request.getParameter("provider_no"),"A",MyDateFormat.getSysDate(request.getParameter("sdate")!=null?request.getParameter("sdate"):today));
    for(RSchedule rs:rsl) {
        rs.setStatus("D");
        rScheduleDao.merge(rs);
    }

	if(request.getParameter("deldate")!= null && (request.getParameter("deldate").equals("b") || request.getParameter("deldate").equals("all")) ) { //delete scheduledate
	  if(request.getParameter("deldate").equals("b")) {
		  List<ScheduleDate> sds = scheduleDateDao.findByProviderPriorityAndDateRange(request.getParameter("provider_no"),'b',MyDateFormat.getSysDate(request.getParameter("sdate")!=null?request.getParameter("sdate"):today), MyDateFormat.getSysDate(edate));
		  for(ScheduleDate sd:sds) {
			  sd.setStatus('D');
			  scheduleDateDao.merge(sd);
		  }
	  } else {
		  List<ScheduleDate> sds = scheduleDateDao.findByProviderAndDateRange(request.getParameter("provider_no"),MyDateFormat.getSysDate(request.getParameter("sdate")!=null?request.getParameter("sdate"):today), MyDateFormat.getSysDate(edate));
		  for(ScheduleDate sd:sds) {
			  sd.setStatus('D');
			  scheduleDateDao.merge(sd);
		  }
	  }
	}
    response.sendRedirect("scheduletemplateapplying.jsp?provider_no="+param[0]+"&provider_name="+URLEncoder.encode(request.getParameter("provider_name")) );
  } else {
%>

<% scheduleRscheduleBean.clear(); %>

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="schedule.scheduletemplateapplying.title" /></title>
<link rel="stylesheet" href="../web.css" />
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script language="JavaScript">
<!--


function displayTemplate(s) {
    var url = "scheduleDisplayTemplate.jsp?name=" + s[s.selectedIndex].value + "&providerid=<%=request.getParameter("provider_no")%>";
    var div = "template";

    var objAjax = new Ajax.Request (
                        url,
                        {
                            method: 'get',
                            onSuccess: function(request) {
                                            while( $(div).firstChild )
                                                $(div).removeChild($(div).firstChild);

                                            if( navigator.userAgent.indexOf("AppleWebKit") > -1 )
                                                $(div).updateSafari(request.responseText);
                                            else
                                                $(div).update(request.responseText);
                                       },
                            onFailure: function(request) {
                                            $(div).innerHTML = "<h3>Error:</h3>" + request.status;
                                        }
                        }

                  );
}

function selectrschedule(s) {
    var ref = "<rewrite:reWrite jspPage="scheduletemplateapplying.jsp"/>";
    ref += "?provider_no=<%=request.getParameter("provider_no")%>&provider_name=<%=request.getParameter("provider_name")%>";
    ref += "&sdate=" +s.options[s.selectedIndex].value;
    self.location.href = ref;
}
function onBtnDelete(s) {
  if( confirm("<bean:message key="schedule.scheduletemplateapplying.msgDeleteConfirmation"/>") ) {
    var ref = "<rewrite:reWrite jspPage="scheduletemplateapplying.jsp"/>";
    ref += "?provider_no=<%=request.getParameter("provider_no")%>&provider_name=<%=request.getParameter("provider_name")%>";
    ref += "&sdate=" +s.options[s.selectedIndex].value;
    ref += "&delete=1&deldate=all";
    self.location.href = ref;
  }
}

function checkDate(y,m,d) {
    var days = new Array(31,28,31,30,31,30,31,31,30,31,30,31);
    var year, month, day;

    //do we have sane values for date?
    if( isNaN(year = parseInt(y)) || isNaN(month = parseInt(m)) || isNaN(day = parseInt(d)) )
        return false;

    //are we dealing with a leap year?
    if( (year % 4 == 0) && (year % 100 != 0) )
        days[1] = 29;
    else if( (year % 4 == 0) && (year % 100 == 0) && (year % 400 == 0) )
        days[1] = 29;

    if( (year < 1970) || (month < 1) || (month > 12) || (day < 1) || (day >days[month-1]) )
        return false;

    return true;
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
    <%=bMoreAddr? getJSstr("A7", "sunaddr1") : "" %>
    //alert("<A7>"+document.schedule.sunaddr1[document.schedule.sunaddr1.selectedIndex].text+"</A7>");
  }
  if(document.schedule.checksun.unchecked) {
    str = str.replace("1 ","");
//	str1 = str1.replace();
  }
  if(document.schedule.checkmon.checked) {
    str += "2 ";
	str1 += "<MON>"+document.schedule.monfrom1.value+"</MON>";
    <%=bMoreAddr? getJSstr("A1", "monaddr1") : "" %>
  }
  if(document.schedule.checkmon.unchecked) {    str = str.replace("2 ","");  }
  if(document.schedule.checktue.checked) {
    str += "3 ";
	str1 += "<TUE>"+document.schedule.tuefrom1.value+"</TUE>";
    <%=bMoreAddr? getJSstr("A2", "tueaddr1") : "" %>
  }
  if(document.schedule.checktue.unchecked) {
    str = str.replace("3 ","");
  }
  if(document.schedule.checkwed.checked) {
    str += "4 ";
	str1 += "<WED>"+document.schedule.wedfrom1.value+"</WED>";
    <%=bMoreAddr? getJSstr("A3", "wedaddr1") : "" %>
  }
  if(document.schedule.checkwed.unchecked) {    str = str.replace("4 ","");  }
  if(document.schedule.checkthu.checked) {
    str += "5 ";
	str1 += "<THU>"+document.schedule.thufrom1.value+"</THU>";
    <%=bMoreAddr? getJSstr("A4", "thuaddr1") : "" %>
  }
  if(document.schedule.checkthu.unchecked) {    str = str.replace("5 ","");  }
  if(document.schedule.checkfri.checked) {
    str += "6 ";
	str1 += "<FRI>"+document.schedule.frifrom1.value+"</FRI>";
    <%=bMoreAddr? getJSstr("A5", "friaddr1") : "" %>
  }
  if(document.schedule.checkfri.unchecked) {    str = str.replace("6 ","");  }
  if(document.schedule.checksat.checked) {
    str += "7 ";
	str1 += "<SAT>"+document.schedule.satfrom1.value+"</SAT>";
    <%=bMoreAddr? getJSstr("A6", "sataddr1") : "" %>
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
  var str1="";
  if(document.schedule.checksun2.checked) {
    strB += "1 ";
    str1 += "<SUN>"+document.schedule.sunfrom2.value+"</SUN>";
    <%=bMoreAddr? getJSstr("A7", "sunaddr2") : "" %>
  }
  if(document.schedule.checksun2.unchecked) {
    strB = strB.replace("1 ","");
//	str1 = str1.replace();
  }
  if(document.schedule.checkmon2.checked) {
    strB += "2 ";
	str1 += "<MON>"+document.schedule.monfrom2.value+"</MON>";
    <%=bMoreAddr? getJSstr("A1", "monaddr2") : "" %>
  }
  if(document.schedule.checkmon2.unchecked) {    strB = strB.replace("2 ","");  }
  if(document.schedule.checktue2.checked) {
    strB += "3 ";
	str1 += "<TUE>"+document.schedule.tuefrom2.value+"</TUE>";
    <%=bMoreAddr? getJSstr("A2", "tueaddr2") : "" %>
  }
  if(document.schedule.checktue2.unchecked) {
    strB = strB.replace("3 ","");
  }
  if(document.schedule.checkwed2.checked) {
    strB += "4 ";
	str1 += "<WED>"+document.schedule.wedfrom2.value+"</WED>";
    <%=bMoreAddr? getJSstr("A3", "wedaddr2") : "" %>
  }
  if(document.schedule.checkwed2.unchecked) {    strB = strB.replace("4 ","");  }
  if(document.schedule.checkthu2.checked) {
    strB += "5 ";
	str1 += "<THU>"+document.schedule.thufrom2.value+"</THU>";
    <%=bMoreAddr? getJSstr("A4", "thuaddr2") : "" %>
  }
  if(document.schedule.checkthu2.unchecked) {    strB = strB.replace("5 ","");  }
  if(document.schedule.checkfri2.checked) {
    strB += "6 ";
	str1 += "<FRI>"+document.schedule.frifrom2.value+"</FRI>";
    <%=bMoreAddr? getJSstr("A5", "friaddr2") : "" %>
  }
  if(document.schedule.checkfri2.unchecked) {    strB = strB.replace("6 ","");  }
  if(document.schedule.checksat2.checked) {
    strB += "7 ";
	str1 += "<SAT>"+document.schedule.satfrom2.value+"</SAT>";
    <%=bMoreAddr? getJSstr("A6", "sataddr2") : "" %>
  }
  if(document.schedule.checksat2.unchecked) {    strB = strB.replace("7 ","");  }

	document.schedule.day_of_weekB.value = strB;
	document.schedule.avail_hourB.value = str1;
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
	} else if( !checkDate(document.schedule.syear.value,document.schedule.smonth.value,document.schedule.sday.value) || !checkDate(document.schedule.eyear.value,document.schedule.emonth.value,document.schedule.eday.value) ) {
	  alert("<bean:message key="schedule.scheduletemplateapplying.msgInputCorrectDate"/>");
	  return false;
	}

        var sDate = new Date(document.schedule.syear.value,document.schedule.smonth.value,document.schedule.sday.value);
        var eDate = new Date(document.schedule.eyear.value,document.schedule.emonth.value,document.schedule.eday.value);

        if( sDate > eDate ) {
            alert("<bean:message key="schedule.scheduletemplateapplying.msgDateOrder"/>");
            return false;
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
  
  RSchedule rs1 = rScheduleDao.search_rschedule_current1(request.getParameter("provider_no"), ConversionUtils.fromDateString(request.getParameter("sdate")!=null?request.getParameter("sdate"):today));
  
  if (rs1 != null) {
    scheduleRscheduleBean.setRscheduleBean(rs1.getProviderNo(),ConversionUtils.toDateString(rs1.getsDate()),ConversionUtils.toDateString(rs1.geteDate()), rs1.getAvailable(),rs1.getDayOfWeek(),rs1.getAvailHourB(), rs1.getAvailHour(), rs1.getCreator());
    if(rs1.getAvailable().equals("A")&&request.getParameter("bFirstDisp")==null) bOrigAlt = true;
    //break;
  } else {
      rs1 = rScheduleDao.search_rschedule_current2(request.getParameter("provider_no"), ConversionUtils.fromDateString(request.getParameter("sdate")!=null?request.getParameter("sdate"):today));
      
      if (rs1 != null) {
        scheduleRscheduleBean.setRscheduleBean(rs1.getProviderNo(),ConversionUtils.toDateString(rs1.getsDate()),ConversionUtils.toDateString(rs1.geteDate()), rs1.getAvailable(),rs1.getDayOfWeek(),rs1.getAvailHourB(), rs1.getAvailHour(), rs1.getCreator());
        if(rs1.getAvailable().equals("A")&&request.getParameter("bFirstDisp")==null) bOrigAlt = true;
        //break;
	  }
  }
%>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<form method="post" name="schedule" action="schedulecreatedate.jsp"
	onSubmit="<%=bAlternate||bOrigAlt?"addDataStringB();":""%>addDataString();return(addDataString1())">

<table border="0" width="100%">
	<!-- <tr>
        <td>&nbsp;</td><td>&nbsp;</td>
        <td style="height: 100%; vertical-align: middle;" rowspan="2"><div style="background-color: #486ebd" id="template"></div></td>
    </tr> -->
	<tr>
		<td style="vertical-align: top;" width="150" bgcolor="#009966"><!--left column-->
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr bgcolor="#486ebd">
				<th align="CENTER" bgcolor="#009966"><br>
				<p><font face="Helvetica" color="#FFFFFF"><bean:message
					key="schedule.scheduletemplateapplying.msgMainLabel" /></font></p>
				</th>
			</tr>
		</table>
		<table width="98%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<p><font size="-1"><bean:message
					key="schedule.scheduletemplateapplying.msgStepOne" /></font></p>
				<p><font size="-1"><bean:message
					key="schedule.scheduletemplateapplying.msgStepTwo" /></font></p>
				<p><font size="-1"><bean:message
					key="schedule.scheduletemplateapplying.msgStepThree" /></font></p>
				<p><font size="-1"><bean:message
					key="schedule.scheduletemplateapplying.msgStepFour" /></font></p>
				<p><font size="-1"><bean:message
					key="schedule.scheduletemplateapplying.msgObs" /></font></p>
				<p>&nbsp;</p>
				</td>
			</tr>
		</table>

		</td>
		<td style="padding-top: 5px; vertical-align: top">

		<center>
		<%

  String syear = "",smonth="",sday="",eyear="",emonth="",eday="";
  String[] param2 =new String[7];
  for(int i=0; i<7; i++) {param2[i]="";}
  String[][] param3 =new String[7][2];
  String[][] param4 =new String[7][2];
  for(int i=0; i<7; i++) {
    for(int j=0; j<2; j++) {
	    param3[i][j]="";
	    param4[i][j]="";
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

		  if(bMoreAddr) {
			if(SxmlMisc.getXmlContent(availhour, ("<A"+(i==0?7:i)+">"),"</A"+(i==0?7:i)+">") != null) {
		    	  sthour = new StringTokenizer(SxmlMisc.getXmlContent(availhour, ("<A"+(i==0?7:i)+">"),"</A"+(i==0?7:i)+">"), "^");
		          j = 0;
				  while (sthour.hasMoreTokens() ) {
	    	        param4[i][j]=sthour.nextToken(); j++;
	        	  }
			}
		  }
	  }
    }
  }

%>
		<table width="99%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td bgcolor="#CCFFCC"><b><%=request.getParameter("provider_name")%></b>
				<input type="hidden" name="provider_name"
					value="<%=request.getParameter("provider_name")%>"></td>
				<td bgcolor="#CCFFCC" nowrap align="right"><select
					name="select" onChange="selectrschedule(this)">
					<%
 
  List<RSchedule> rss = rScheduleDao.search_rschedule_future1(request.getParameter("provider_no"),ConversionUtils.fromDateString(lastYear));
 
 	for (RSchedule rs:rss) {
%>
					<option value="<%=ConversionUtils.toDateString(rs.getsDate())%>"
						<%=request.getParameter("sdate")!=null?(ConversionUtils.toDateString(rs.getsDate()).equals(request.getParameter("sdate"))?"selected":""):(ConversionUtils.toDateString(rs.getsDate()).equals(scheduleRscheduleBean.sdate)?"selected":"")%>>
					<%=ConversionUtils.toDateString(rs.getsDate())+" ~ "+ConversionUtils.toDateString(rs.geteDate())%></option>
					<%
 	}
        MessageResources msg = MessageResourcesFactory.createFactory().createResources("oscarResources");
%>
				</select> <input type="button" name="command"
					value="<bean:message key="schedule.scheduletemplateapplying.btnDelete"/>"
					onClick="onBtnDelete(document.forms['schedule'].elements['select'])">
				</td>
			</tr>
			<tr>
				<td style="color: red" colspan="2"><%=request.getParameter("overlap") != null?msg.getMessage("schedule.scheduletemplateapplying.msgScheduleConflict"):"&nbsp;"%></td>
			</tr>
			<tr>
				<td bgcolor="#CCFFCC" colspan="2"><bean:message
					key="schedule.scheduletemplateapplying.msgDate" /> <bean:message
					key="schedule.scheduletemplateapplying.msgFrom" />: <input
					type="text" name="syear" size="4" maxlength="4" value="<%=syear%>"
					style="width: 40px;" /> - <input type="text" name="smonth" size="2"
					maxlength="2" value="<%=smonth%>" style="width: 30px;" /> - <input
					type="text" name="sday" size="2" maxlength="2" value="<%=sday%>"
					onChange="onChangeDates()" style="width: 30px;" /> <font size="-2"><bean:message
					key="schedule.scheduletemplateapplying.msgDateFormat" /></font> &nbsp;
				&nbsp; <bean:message key="schedule.scheduletemplateapplying.msgTo" />:
				<input type="text" name="eyear" size="4" maxlength="4"
					value="<%=eyear%>" style="width: 40px;" /> <input type="hidden"
					name="origeyear" value="<%=eyear%>"> - <input type="text"
					name="emonth" size="2" maxlength="2" value="<%=emonth%>"
					style="width: 30px;" /> <input type="hidden" name="origemonth"
					value="<%=emonth%>"> - <input type="text" name="eday"
					size="2" maxlength="2" value="<%=eday%>" onChange="onChangeDatee()"
					style="width: 30px;" /> <input type="hidden" name="origeday"
					value="<%=eday%>"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2"><bean:message
					key="schedule.scheduletemplateapplying.msgAvaiableEvery" /><font
					size="-2"> (<bean:message
					key="schedule.scheduletemplateapplying.msgDayOfWeek" />): </font> <input
					type="checkbox" name="alternate" value="checked"
					onClick="onAlternate()" <%=bOrigAlt||bAlternate?"checked":""%>><bean:message
					key="schedule.scheduletemplateapplying.msgAlternateWeekSetting" /></td>
			</tr>
			<tr>
				<td nowrap align="center" colspan="2">
				<table border=2 width=100% cellspacing="0" cellpadding="0">
					<tr>
						<td width=70%><script language=javascript>
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
								<p><font size="-1"> <input type="checkbox"
									name="checksun" value="1" onClick="addDataString()"
									<%=param2[0]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgSunday" /></font>
								</td>
								<td><font size="-1"> <input type="text"
									name="sunfrom1" size="20" value="<%=param3[0][0]%>" readonly>
								<input type="button" name="sunto1" value="<<" onclick="javascript:tranbutton1_click();" >
								</font> <%=bMoreAddr? getSelectAddr("sunaddr1", addr, param4[0][0]) : ""  %>
								</td>
							</tr>
							<tr>
								<td><font size="-1"> <input type="checkbox"
									name="checkmon" value="2" onClick="addDataString()"
									<%=param2[1]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgMonday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="monfrom1" size="20" value="<%=param3[1][0]%>" readonly>
								<input type="button" name="monto1" value="<<" onclick="javascript:tranbutton2_click();" >
								</font> <%=bMoreAddr? getSelectAddr("monaddr1", addr, param4[1][0]) : ""  %>
								</td>
							</tr>
							<tr bgcolor="#CCFFCC">
								<td><font size="-1"> <input type="checkbox"
									name="checktue" value="3" onClick="addDataString()"
									<%=param2[2]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgTuesday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="tuefrom1" size="20" value="<%=param3[2][0]%>" readonly>
								<input type="button" name="tueto1" value="<<" onclick="javascript:tranbutton3_click();"  >
								</font> <%=bMoreAddr? getSelectAddr("tueaddr1", addr, param4[2][0]) : ""  %>
								</td>
							</tr>
							<tr>
								<td><font size="-1"> <input type="checkbox"
									name="checkwed" value="4" onClick="addDataString()"
									<%=param2[3]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgWednesday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="wedfrom1" size="20" value="<%=param3[3][0]%>" readonly>
								<input type="button" name="wedto1" value="<<" onclick="javascript:tranbutton4_click();" >
								</font> <%=bMoreAddr? getSelectAddr("wedaddr1", addr, param4[3][0]) : ""  %>
								</td>
							</tr>
							<tr bgcolor="#CCFFCC">
								<td><font size="-1"> <input type="checkbox"
									name="checkthu" value="5" onClick="addDataString()"
									<%=param2[4]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgThursday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="thufrom1" size="20" value="<%=param3[4][0]%>" readonly>
								<input type="button" name="thuto1" value="<<" onclick="javascript:tranbutton5_click();" >
								</font> <%=bMoreAddr? getSelectAddr("thuaddr1", addr, param4[4][0]) : ""  %>
								</td>
							</tr>
							<tr>
								<td><font size="-1"> <input type="checkbox"
									name="checkfri" value="6" onClick="addDataString()"
									<%=param2[5]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgFriday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="frifrom1" size="20" value="<%=param3[5][0]%>" readonly>
								<input type="button" name="frito1" value="<<" onclick="javascript:tranbutton6_click();" >
								</font> <%=bMoreAddr? getSelectAddr("friaddr1", addr, param4[5][0]) : ""  %>
								</td>
							</tr>
							<tr bgcolor="#CCFFCC">
								<td><font size="-1"> <input type="checkbox"
									name="checksat" value="7" onClick="addDataString()"
									<%=param2[6]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgSaturday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="satfrom1" size="20" value="<%=param3[6][0]%>" readonly>
								<input type="button" name="satto1" value="<<" onclick="javascript:tranbutton7_click();" >
								</font> <%=bMoreAddr? getSelectAddr("sataddr1", addr, param4[6][0]) : ""  %>
								</td>
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
	    param4[i][j]="";
	  }
  }

    StringTokenizer st = new StringTokenizer(stToken );
    while (st.hasMoreTokens() ) {
      int j = Integer.parseInt(st.nextToken())-1;
	  int i = j==7?0:j;
      param2[i]="checked";
      if(SxmlMisc.getXmlContent(availhour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">") != null) {
	      StringTokenizer sthour = new StringTokenizer(SxmlMisc.getXmlContent(availhour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">"), "^");
          j = 0;
		  while (sthour.hasMoreTokens() ) {
          	param3[i][j]=sthour.nextToken();
          	j++;
          }

		  if(bMoreAddr) {
		      sthour = new StringTokenizer(SxmlMisc.getXmlContent(availhour, ("<A"+(i==0?7:i)+">"),"</A"+(i==0?7:i)+">"), "^");
	          j = 0;
			  while (sthour.hasMoreTokens() ) {
	            param4[i][j]=sthour.nextToken();
	            j++;
	          }
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
								<p><font size="-1"> <input type="checkbox"
									name="checksun2" value="1" onClick="addDataString()"
									<%=param2[0]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgSunday" /></font>
								</td>
								<td><font size="-1"> <input type="text"
									name="sunfrom2" size="20" value="<%=param3[0][0]%>"> <input
									type="button" name="sunto2" value="<<" onclick="javascript:tranbuttonb1_click();" >
								</font> <%=bMoreAddr? getSelectAddr("sunaddr2", addr, param4[0][0]) : ""  %>
								</td>
							</tr>
							<tr bgcolor="#E0FFFF">
								<td><font size="-1"> <input type="checkbox"
									name="checkmon2" value="2" onClick="addDataString()"
									<%=param2[1]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgMonday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="monfrom2" size="20" value="<%=param3[1][0]%>"> <input
									type="button" name="monto2" value="<<" onclick="javascript:tranbuttonb2_click();" >
								</font> <%=bMoreAddr? getSelectAddr("monaddr2", addr, param4[1][0]) : ""  %>
								</td>
							</tr>
							<tr bgcolor="#00C5CD">
								<td><font size="-1"> <input type="checkbox"
									name="checktue2" value="3" onClick="addDataString()"
									<%=param2[2]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgTuesday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="tuefrom2" size="20" value="<%=param3[2][0]%>"> <input
									type="button" name="tueto2" value="<<" onclick="javascript:tranbuttonb3_click();"  >
								</font> <%=bMoreAddr? getSelectAddr("tueaddr2", addr, param4[2][0]) : ""  %>
								</td>
							</tr>
							<tr bgcolor="#E0FFFF">
								<td><font size="-1"> <input type="checkbox"
									name="checkwed2" value="4" onClick="addDataString()"
									<%=param2[3]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgWednesday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="wedfrom2" size="20" value="<%=param3[3][0]%>"> <input
									type="button" name="wedto2" value="<<" onclick="javascript:tranbuttonb4_click();" >
								</font> <%=bMoreAddr? getSelectAddr("wedaddr2", addr, param4[3][0]) : ""  %>
								</td>
							</tr>
							<tr bgcolor="#00C5CD">
								<td><font size="-1"> <input type="checkbox"
									name="checkthu2" value="5" onClick="addDataString()"
									<%=param2[4]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgThursday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="thufrom2" size="20" value="<%=param3[4][0]%>"> <input
									type="button" name="thuto2" value="<<" onclick="javascript:tranbuttonb5_click();" >
								</font> <%=bMoreAddr? getSelectAddr("thuaddr2", addr, param4[4][0]) : ""  %>
								</td>
							</tr>
							<tr bgcolor="#E0FFFF">
								<td><font size="-1"> <input type="checkbox"
									name="checkfri2" value="6" onClick="addDataString()"
									<%=param2[5]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgFriday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="frifrom2" size="20" value="<%=param3[5][0]%>"> <input
									type="button" name="frito2" value="<<" onclick="javascript:tranbuttonb6_click();" >
								</font> <%=bMoreAddr? getSelectAddr("friaddr2", addr, param4[5][0]) : ""  %>
								</td>
							</tr>
							<tr bgcolor="#00C5CD">
								<td><font size="-1"> <input type="checkbox"
									name="checksat2" value="7" onClick="addDataString()"
									<%=param2[6]%>> <bean:message
									key="schedule.scheduletemplateapplying.msgSaturday" /></font></td>
								<td><font size="-1"> <input type="text"
									name="satfrom2" size="20" value="<%=param3[6][0]%>"> <input
									type="button" name="satto2" value="<<" onclick="javascript:tranbuttonb7_click();" >
								</font> <%=bMoreAddr? getSelectAddr("sataddr2", addr, param4[6][0]) : ""  %>
								</td>
							</tr>
							<% }
%>

						</table>

						</td>
						<td><select size=<%=bOrigAlt||bAlternate?22:11%>
							onclick="displayTemplate(this)" name="mytemplate">
							<%
							
							for(ScheduleTemplate st: scheduleTemplateDao.findByProviderNo("Public")) {
   
	%>
							<option value="<%=st.getId().getName()%>"><%=st.getId().getName()+" |"+st.getSummary()%></option>
							<%
   }
							
							for(ScheduleTemplate st: scheduleTemplateDao.findByProviderNo(request.getParameter("provider_no"))) {
							
	%>
							<option value="<%=st.getId().getName()%>"><%=st.getId().getName()+" |"+st.getSummary()%></option>
							<% }	%>
						</select></td>
					</tr>
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
				<div align="right"><input type="hidden" name="provider_no"
					value="<%=request.getParameter("provider_no")%>"> <input
					type="hidden" name="available"
					value="<%=bAlternate||bOrigAlt?"A":"1"%>"> <input
					type="hidden" name="Submit" value=" Next "> <input
					type="submit"
					value='<bean:message key="schedule.scheduletemplateapplying.btnNext"/>'>
				</div>
				</td>
			</tr>
		</table>
		<p>
		<p>&nbsp;</p>
		</center>
		</td>
		<td style="height: 100%; vertical-align: middle;" rowspan="2">
		<div style="background-color: #486ebd" id="template"></div>
		</td>
	</tr>
</table>


</form>
<%
} //end if
%>
</body>
<%! String getSelectAddr(String s, String [] site, String sel) {

		boolean isExcludedSiteSelected = false;
		if (bMultisites && excludedSites.contains(sel))
			isExcludedSiteSelected = true; //"; text-decoration:line-through;";

		String ret = "<select name='" + s + "' "  + (isExcludedSiteSelected ? " disabled style='text-decoration:line-through;'  " : "")
			+ " onchange='this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor'>";
		int ind=0;
		boolean isSiteSel=false;

		for(int i=0; i<site.length; i++) {
			String t = site[i].equals(sel) ? " selected" : "";
			if (site[i].equals(sel)) {
				ind=i;
				isSiteSel = true;
			}
			if (i==site.length - 1 && isSiteSel ==false) {
				//if None of the site has been selected, default select to the last one "None"
				ind=i;
				t = " selected";
			}

			if ((isExcludedSiteSelected) || (!excludedSites.contains(site[i]))) {
				ret += "<option value='" + site[i] + "'" + t + (bMultisites? " style='background-color:"+bgColors[i] + "'": "") +">" + site[i] + "</option>";
			}
		}
		ret += "</select>";
		if (bMultisites)
			ret += "<script>document.schedule."+s+".style.backgroundColor='"+bgColors[ind]+"';</script>";
		if (isExcludedSiteSelected)
			ret += "<script>document.schedule.check"+s.substring(0,3)+".disabled='true';</script>";
		return ret;
}
%>
<%! String getJSstr(String s, String obj) {
		String ret = "";
		ret +="str1 +=" + "\"<"+s+">\""+ "+" + "document.schedule." + obj
		+ "[" + "document.schedule." + obj + ".selectedIndex" + "].text"+ "+" +"\"</"+s+">\";";
		return ret;
}
%>
</html:html>
