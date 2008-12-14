<% response.setHeader("Cache-Control","no-cache");%>

<!-- add by caisi -->
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="myoscar" %>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr" %>

<!-- add by caisi end<style>* {border:1px solid black;}</style> -->

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
	long loadPage = System.currentTimeMillis();
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment" rights="r" reverse="<%=true%>" >
<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<!-- caisi infirmary view extension add ffffffffffffff-->
<caisi:isModuleLoad moduleName="caisi">
<%
if (request.getParameter("year")!=null && request.getParameter("month")!=null && request.getParameter("day")!=null)
	{
		java.util.Date infirm_date=new java.util.GregorianCalendar(Integer.valueOf(request.getParameter("year")).intValue(), Integer.valueOf(request.getParameter("month")).intValue()-1, Integer.valueOf(request.getParameter("day")).intValue()).getTime();
		session.setAttribute("infirmaryView_date",infirm_date);

	}else
	{
		session.setAttribute("infirmaryView_date",null);
	}
	String reqstr =request.getQueryString(); 
	if (reqstr == null) 
	{
		//Hack:: an unknown bug of struts or JSP causing the queryString to be null
		String year_q = request.getParameter("year");
	    String month_q =request.getParameter("month");
	    String day_q = request.getParameter("day");
	    String view_q = request.getParameter("view");
	    String displayMode_q = request.getParameter("displaymode");
	    reqstr = "year=" + year_q + "&month=" + month_q 
           + "&day="+ day_q + "&view=" + view_q + "&displaymode=" + displayMode_q;
	}
	session.setAttribute("infirmaryView_OscarQue",reqstr); 
	String absurl="/infirm.do?action=showProgram";
%>
<c:import url="/infirm.do?action=showProgram" />
</caisi:isModuleLoad>
<!-- caisi infirmary view extension add end ffffffffffffff-->

<%@ page import="java.util.*, java.text.*,java.sql.*, java.net.*, oscar.*, oscar.util.*" %>
<%@ page import="org.apache.commons.lang.*" %>

<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<jsp:useBean id="as" class="oscar.appt.ApptStatusData" scope="page" />
<jsp:useBean id="dateTimeCodeBean" class="java.util.Hashtable" scope="page" />

<!-- Struts for i18n -->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%!
/**
Checks if the schedule day is patients birthday
**/
public boolean isBirthday(String schedDate,String demBday){
	return schedDate.equals(demBday);
}
public boolean patientHasOutstandingPrivateBills(String demographicNo){
	oscar.oscarBilling.ca.bc.MSP.MSPReconcile msp = new oscar.oscarBilling.ca.bc.MSP.MSPReconcile();
	return msp.patientHasOutstandingPrivateBill(demographicNo);
}
%>
<%
    if(session.getAttribute("user") == null )
        response.sendRedirect("../logout.jsp");

	String curUser_no = (String) session.getAttribute("user");
	oscar.oscarSecurity.CookieSecurity cs = new oscar.oscarSecurity.CookieSecurity();
    response.addCookie(cs.GiveMeACookie(oscar.oscarSecurity.CookieSecurity.providerCookie));

    String mygroupno = (String) session.getAttribute("groupno");
    String userfirstname = (String) session.getAttribute("userfirstname");
    String userlastname = (String) session.getAttribute("userlastname");
    String prov= ((String ) oscarVariables.getProperty("billregion","")).trim().toUpperCase();

	boolean bShortcutForm = oscarVariables.getProperty("appt_formview", "").equalsIgnoreCase("on") ? true : false;
	String formName = bShortcutForm ? oscarVariables.getProperty("appt_formview_name") : "";
	String formNameShort = formName.length() > 3 ? (formName.substring(0,2)+".") : formName;
        String formName2 = bShortcutForm ? oscarVariables.getProperty("appt_formview_name2", "") : "";
	String formName2Short = formName2.length() > 3 ? (formName2.substring(0,2)+".") : formName2;
        boolean bShortcutForm2 = bShortcutForm && !formName2.equals("");

    //String userprofession = (String) session.getAttribute("userprofession");
    int startHour=Integer.parseInt(((String) session.getAttribute("starthour")).trim());
    int endHour=Integer.parseInt(((String) session.getAttribute("endhour")).trim());
    int everyMin=Integer.parseInt(((String) session.getAttribute("everymin")).trim());
    String newticklerwarningwindow=null;
    String default_pmm=null;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	newticklerwarningwindow = (String) session.getAttribute("newticklerwarningwindow");
	default_pmm = (String)session.getAttribute("default_pmm");
}   
    int lenLimitedL=11, lenLimitedS=3; //L - long, S - short
    int len = lenLimitedL;
    int view = request.getParameter("view")!=null ? Integer.parseInt(request.getParameter("view")) : 0; //0-multiple views, 1-single view
    //// THIS IS THE VALUE I HAVE BEEN LOOKING FOR!!!!!
	boolean bDispTemplatePeriod = ( oscarVariables.getProperty("receptionist_alt_view") != null && oscarVariables.getProperty("receptionist_alt_view").equals("yes") ); // true - display as schedule template period, false - display as preference

%>
<%
   
    ResultSet rsTickler = null;
    ResultSet rsStudy = null;
    ResultSet rsDemo = null;
    String tickler_no="", textColor="", tickler_note="";
    String ver = "", roster="";
    String yob = "";
    String mob = "";
    String dob = "";
    String demBday = "";
    StringBuffer study_no=null, study_link=null,studyDescription=null;
	String studySymbol = "#", studyColor = "red";

    String resourcebaseurl = "http://resource.oscarmcmaster.org/oscarResource/";
    Object[] rst=apptMainBean.queryResultsCaisi("resource_baseurl", "search_resource_baseurl");
    ResultSet rsgroup1 =(ResultSet) rst[0];
    while (rsgroup1.next()) {
 	    resourcebaseurl = rsgroup1.getString("value");
    }
    rsgroup1 = null;
    ((Statement) rst[1]).close();

    GregorianCalendar cal = new GregorianCalendar();
    int curYear = cal.get(Calendar.YEAR);
    int curMonth = (cal.get(Calendar.MONTH)+1);
    int curDay = cal.get(Calendar.DAY_OF_MONTH);

    int year = Integer.parseInt(request.getParameter("year"));
    int month = Integer.parseInt(request.getParameter("month"));
    int day = Integer.parseInt(request.getParameter("day"));

    //verify the input date is really existed
    cal = new GregorianCalendar(year,(month-1),day);
    year = cal.get(Calendar.YEAR);
    month = (cal.get(Calendar.MONTH)+1);
    day = cal.get(Calendar.DAY_OF_MONTH);

    String strDate = year + "-" + month + "-" + day;
    String monthDay = month + "-" + day;
    SimpleDateFormat inform = new SimpleDateFormat ("yyyy-MM-dd", request.getLocale());
    String formatDate;
    try {
     java.util.ResourceBundle prop = ResourceBundle.getBundle("oscarResources", request.getLocale());
     formatDate = UtilDateUtilities.DateToString(inform.parse(strDate), prop.getString("date.EEEyyyyMMdd"),request.getLocale());
    } catch (Exception e) {
     e.printStackTrace();
     formatDate = UtilDateUtilities.DateToString(inform.parse(strDate), "EEE, yyyy-MM-dd");
    }
    String strYear=""+year;
    String strMonth=month>9?(""+month):("0"+month);
    String strDay=day>9?(""+day):("0"+day);    
%>
<!--
/*
 *
 * Copyright (c) 2005-2008. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
-->
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="oscar.util.*"%>
<%@page import="oscar.oscarDB.*"%>
<html:html locale="true">
<head>
<title><%=WordUtils.capitalize(userlastname + ", " +  org.apache.commons.lang.StringUtils.substring(userfirstname, 0, 1)) + "-"%><bean:message key="provider.appointmentProviderAdminDay.title"/></title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css" type="text/css">
<c:if test="${empty sessionScope.archiveView or sessionScope.archiveView != true}">
	<meta http-equiv="refresh" content="180;">
</c:if>

<script language="javascript" type="text/javascript" src="../share/javascript/Oscar.js" ></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../phr/phr.js"></script>
<script language="JavaScript">




function confirmPopupPage(height, width, queryString, doConfirm){
   if (doConfirm == "Yes"){
      if (confirm("Are you sure you would like to book an apointment here.")){
         popupPage(height, width, queryString);
      }
   }else{
      popupPage(height, width, queryString);
   }
}

function setfocus() {
    this.focus();
}

function popupPage(vheight,vwidth,varpage) {
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
  var popup=window.open(page, "<bean:message key="provider.appointmentProviderAdminDay.apptProvider"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}

function popupPageOfChangePassword(){
	<% 	
		Integer ed;
		String expired_days="";
		if(session.getAttribute("expired_days")!=null){
			expired_days = (String)session.getAttribute("expired_days");
		}	
		//System.out.println("***** expired days???"+expired_days);
		//ed = Integer.valueOf(expired_days);
		if(!(expired_days.equals(" ")||expired_days.equals("")||expired_days==null)) {
		//if(ed >= 0) {
			//System.out.println("***** inside ***** ");
	%>
		
	window.open("changePassword.jsp","changePassword","resizable=yes,scrollbars=yes,width=400,height=300");
	changePassword.moveTo(0,0);
	<%
		}		
	%>
}

function popupPage2(varpage) {
    popupPage2(varpage, "apptProviderSearch");
}

function popupPage2(varpage, windowname) {
    var page = "" + varpage;
    windowprops = "height=700,width=1000,location=no,"
    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=10,left=0";
    var popup = window.open(page, windowname, windowprops);
    if (popup != null) {
       if (popup.opener == null) {
          popup.opener = self;
       }
       popup.focus();
    }
}

<!--oscarMessenger code block-->
function popupOscarRx(vheight,vwidth,varpage) {
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="global.oscarRx"/>_appt", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}

function review(key) {
  if(self.location.href.lastIndexOf("?") > 0) {
    if(self.location.href.lastIndexOf("&viewall=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&viewall="));
    else a = self.location.href;
  } else {
    a="providercontrol.jsp?year="+document.jumptodate.year.value+"&month="+document.jumptodate.month.value+"&day="+document.jumptodate.day.value+"&view=0&displaymode=day&dboperation=searchappointmentday";
  }
  self.location.href = a + "&viewall="+key ;
}

function refresh() {
  document.location.reload();
}

function refresh1() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    document.location.reload();
  }
}

function onUnbilled(url) {
  if(confirm("<bean:message key="provider.appointmentProviderAdminDay.onUnbilled"/>")) {
    popupPage(700,720, url);
  }
}

function changeGroup(s) {
	var newGroupNo = s.options[s.selectedIndex].value;
	if(newGroupNo.indexOf("_grp_") != -1) {
	  newGroupNo = s.options[s.selectedIndex].value.substring(5);
        }else{
          newGroupNo = s.options[s.selectedIndex].value;
        }
        <%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){%>
	  popupPage(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
        <%}else {%>
          popupPage(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
        <%}%>
}

function ts1(s) {
  popupPage(360,780,('../appointment/addappointment.jsp?'+s));
}
function tsr(s) {
  popupPage(360,780,('../appointment/appointmentcontrol.jsp?displaymode=edit&dboperation=search&'+s));
}
function goFilpView(s) {
	self.location.href = "../schedule/scheduleflipview.jsp?originalpage=../provider/providercontrol.jsp&startDate=<%=year+"-"+month+"-"+day%>" + "&provider_no="+s ;
}
function goZoomView(s, n) {
	self.location.href = "providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider="+s+"&curProviderName="+n+"&displaymode=day&dboperation=searchappointmentday" ;
}
function findProvider(p,m,d) {
  popupPage(300,400, "receptionistfindprovider.jsp?pyear=" +p+ "&pmonth=" +m+ "&pday=" +d+ "&providername="+ document.findprovider.providername.value );
}

//popup a new tickler warning window
function load() {
    if ("<%=newticklerwarningwindow%>"=="enabled") {
        if (IsPopupBlocker()) {
            alert("You have a popup blocker, so you can not see the new tickler warning window. Please disable the pop blocker in your google bar, yahoo bar or IE ...");
	} 
        var pu=window.open("../UnreadTickler.do",null,"height=120,width=250,location=no,scrollbars=no,menubars=no,toolbars=no,resizable=yes,top=500,left=700");
        pu.focus();
    }
    popupPageOfChangePassword();
    refreshAllTabAlerts();
}

function IsPopupBlocker() {
	var oWin = window.open("","testpopupblocker","width=100,height=50,top=5000,left=5000");
	if (oWin==null || typeof(oWin)=="undefined") {
		return true;
	} else {
		oWin.close();
		return false;
	}
}

<%-- Refresh tab alerts --%>
function refreshAllTabAlerts() {
    refreshTabAlerts("oscar_new_lab");
    refreshTabAlerts("oscar_new_msg");
    refreshTabAlerts("oscar_new_tickler");
    refreshTabAlerts("oscar_aged_consults");
    refreshTabAlerts("oscar_scratch");
}

function callRefreshTabAlerts(id) {
    setTimeout("refreshTabAlerts('"+id+"')", 10);
}

function refreshTabAlerts(id) {
    var url = "../provider/tabAlertsRefresh.jsp";
    var pars = "id=" + id;
    
    var myAjax = new Ajax.Updater(id, url, {method: 'get', parameters: pars});
}

<oscar:oscarPropertiesCheck property="MY_OSCAR" value="yes">
    <phr:IfTimeToExchange>
        phrExchangeGo('../phrExchange.do');
    </phr:IfTimeToExchange>
</oscar:oscarPropertiesCheck>
/*
// myoscar JavaScript server call --we are using the JSP check instead (see above)
function phrAsyncActivate() {
    var interval = 10;
    var date = new Date();
    var curr_min = d.getMinutes();
    var timeleft = curr_min%interval + 4;
    setTimeout(phrAsyncGo2, timeleft);
}
function phrAsyncGo2() {
    alert("executing");
    new Ajax.Request('../phrAsync.do', { method:'get' });
    phrAsyncActivate();
}




*/

</SCRIPT>

   <style type="text/css">    
        #navlist{
            margin: 0;
            padding: 0;
            white-space: nowrap;
        }    

        #navlist li {
            padding-top: 0.5px;
            padding-bottom: 0.5px;
            padding-left: 2.5px;
            padding-right: 2.5px;
            display: inline; 
        }

        #navlist li:hover { color: #fff; background-color: #486ebd; }
        #navlist li a:hover { color: #fff; background-color: #486ebd; }
        
        
    </style>
</head>
<%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable()){%>
<body bgcolor="#EEEEFF" onload="load();" topmargin="0" leftmargin="0" rightmargin="0">
<c:import url="/SystemMessage.do?method=view" />
<c:import url="/FacilityMessage.do?method=view" />
<%}else{%>
<body bgcolor="#EEEEFF" onLoad="refreshAllTabAlerts();" topmargin="0" leftmargin="0" rightmargin="0">
<%}%>
<%
   int numProvider=0, numAvailProvider=0;
   String [] curProvider_no;
   String [] curProviderName;
   ResultSet rsgroup = null;
   //initial provider bean for all the application
   if(providerBean.isEmpty()) {
	   rst= apptMainBean.queryResultsCaisi("searchallprovider");
     rsgroup = (ResultSet) rst[0];
 	   while (rsgroup.next()) {
 	    providerBean.setProperty(rsgroup.getString("provider_no"), new String( rsgroup.getString("last_name")+","+rsgroup.getString("first_name") ));
 	   }
 	  ((Statement) rst[1]).close();
 	 }

if(providerBean.get(mygroupno) != null) { //single appointed provider view
     numProvider=1;
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];
     curProvider_no[0]=mygroupno;
     curProviderName[0]=providerBean.getProperty(mygroupno);
} else {
	if(view==0) { //multiple views
		rst=apptMainBean.queryResultsCaisi(mygroupno, "searchmygroupcount");
		rsgroup = (ResultSet) rst[0];
		while (rsgroup.next()) {
			numProvider=rsgroup.getInt(1);
		}
		((Statement) rst[1]).close();
       String [] param3 = new String [2];
       param3[0] = mygroupno;
       param3[1] = strDate; //strYear +"-"+ strMonth +"-"+ strDay ;
  	   rst= apptMainBean.queryResultsCaisi(param3, "search_numgrpscheduledate");
  	 rsgroup=(ResultSet) rst[0];
  	   while (rsgroup.next()) {
			numAvailProvider = rsgroup.getInt(1);
		}
  	 ((Statement) rst[1]).close();
  	 
     if(numProvider==0) {
       numProvider=1; //the login user
       curProvider_no = new String []{curUser_no};  //[numProvider];
       curProviderName = new String []{(userlastname+", "+userfirstname)}; //[numProvider];
     } else {
       if(request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1") ) {
         if(numProvider >= 5) {lenLimitedL = 2; lenLimitedS = 3; }
       } else {
         if(numAvailProvider >= 5) {lenLimitedL = 2; lenLimitedS = 3; }
         if(numAvailProvider == 2) {lenLimitedL = 20; lenLimitedS = 10; len = 20;}
         if(numAvailProvider == 1) {lenLimitedL = 30; lenLimitedS = 30; len = 30; }
       }
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];

		rst=apptMainBean.queryResultsCaisi(mygroupno, "searchmygroupprovider");
	   rsgroup = (ResultSet) rst[0];
	   int iTemp=0;
     while (rsgroup.next()) {
       curProvider_no[iTemp]=rsgroup.getString("provider_no");
       curProviderName[iTemp]=rsgroup.getString("first_name")+" "+rsgroup.getString("last_name");
       iTemp++;
     }
     ((Statement)rst[1]).close();
     }
   } else { //single view
     numProvider=1;
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];
     curProvider_no[0]=request.getParameter("curProvider");
     curProviderName[0]=request.getParameter("curProviderName");
   }
}
   //set timecode bean
   String bgcolordef = "#486ebd" ;
   String [] param3 = new String[2];
   param3[0] = strDate; //strYear+"-"+strMonth+"-"+strDay;
   for(int nProvider=0;nProvider<numProvider;nProvider++) {
     param3[1] = curProvider_no[nProvider];
     	rst=apptMainBean.queryResultsCaisi(param3, "search_appttimecode");
	   rsgroup = (ResultSet)rst[0];
     while (rsgroup.next()) {
       dateTimeCodeBean.put(rsgroup.getString("provider_no"), rsgroup.getString("timecode"));
     }
   }
   ((Statement)rst[1]).close();
   	rst=apptMainBean.queryResultsCaisi("search_timecode");
	 rsgroup = (ResultSet)rst[0];
   while (rsgroup.next()) {
     dateTimeCodeBean.put("description"+rsgroup.getString("code"), rsgroup.getString("description"));
     dateTimeCodeBean.put("duration"+rsgroup.getString("code"), rsgroup.getString("duration"));
     dateTimeCodeBean.put("color"+rsgroup.getString("code"), (rsgroup.getString("color")==null || rsgroup.getString("color").equals(""))?bgcolordef:rsgroup.getString("color") );
     dateTimeCodeBean.put("confirm" + rsgroup.getString("code"), rsgroup.getString("confirm"));
   }
   ((Statement)rst[1]).close();

   java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
%>


<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%" id="firstTable">
<tr>
  <td >
    <ul id="navlist">
    
      <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">

        <li>
            <a href="providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>' ; return true"><bean:message key="global.day"/></a>
        </li>
         <li>
            <a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewMonthSched"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewMonthSched"/>' ; return true"><bean:message key="global.month"/></a>
         </li>
         <li>
            <a href="#" ONCLICK ="popupPage2('<%=resourcebaseurl%>');return false;" title="<bean:message key="global.resources"/>" onmouseover="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewResources"/>';return true"><bean:message key="global.resources"/></a>
         </li>
      </caisi:isModuleLoad>

         <li>
            <caisi:isModuleLoad moduleName="caisi">
               <a HREF="../PMmodule/ClientSearch2.do" TITLE='<bean:message key="global.searchPatientRecords"/>' OnMouseOver="window.status='<bean:message key="global.searchPatientRecords"/>' ; return true"><bean:message key="provider.appointmentProviderAdminDay.search"/></a>
            </caisi:isModuleLoad>
            <caisi:isModuleLoad moduleName="caisi" reverse="true">
               <a HREF="#" ONCLICK ="popupPage2('../demographic/search.jsp');return false;"  TITLE='<bean:message key="global.searchPatientRecords"/>' OnMouseOver="window.status='<bean:message key="global.searchPatientRecords"/>' ; return true"><bean:message key="provider.appointmentProviderAdminDay.search"/></a>
            </caisi:isModuleLoad>
        </li>

      <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
        <li>
            <a HREF="#" ONCLICK ="popupPage2('../report/reportindex.jsp','reportPage');return false;"   TITLE='<bean:message key="global.genReport"/>' OnMouseOver="window.status='<bean:message key="global.genReport"/>' ; return true"><bean:message key="global.report"/></a>
        </li>
        <li>
             <%if (vLocale.getCountry().equals("BR")) { %>
               <a HREF="#" ONCLICK ="popupPage2('../oscar/billing/consultaFaturamentoMedico/init.do');return false;" TITLE='<bean:message key="global.genBillReport"/>' onMouseOver="window.status='<bean:message key="global.genBillReport"/>';return true"><bean:message key="global.billing"/></a>
             <% } else {%>
                <a HREF="#" ONCLICK ="popupPage2('../billing/CA/<%=prov%>/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;" TITLE='<bean:message key="global.genBillReport"/>' onMouseOver="window.status='<bean:message key="global.genBillReport"/>';return true"><bean:message key="global.billing"/></a>
             <% } %>
        </li>
	<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment.doctorLink" rights="r">
           <li>
               <a HREF="#" ONCLICK ="popupPage2('../oscarMDS/Index.jsp?providerNo=<%=curUser_no%>', 'Lab');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'>
		   <span id="oscar_new_lab"><bean:message key="global.lab"/></span>
               </a>
               <oscar:newUnclaimedLab>
               <a class="tabalert" HREF="#" ONCLICK ="popupPage2('../oscarMDS/Index.jsp?providerNo=0&searchProviderNo=0&status=N&lname=&fname=&hnum=&pageNum=1&startIndex=0', 'Lab');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'>*</a>
               </oscar:newUnclaimedLab>
           </li>
	     </security:oscarSec>
	     
	 </caisi:isModuleLoad>
         <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true"> 
             <li>
                 <a HREF="#" ONCLICK ="popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')" title="<bean:message key="global.messenger"/>">
                 <span id="oscar_new_msg"><bean:message key="global.msg"/></span></a>
             </li>
         </caisi:isModuleLoad>
     <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">     
        <li>
         <a HREF="#" ONCLICK ="popupOscarRx(625,900,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')" title="<bean:message key="provider.appointmentProviderAdminDay.viewConReq"/>">
         <span id="oscar_aged_consults"><bean:message key="global.con"/></span></a>
        </li>
     </caisi:isModuleLoad>
     
        <li>    <!-- remove this and let providerpreference check -->
	    <caisi:isModuleLoad moduleName="ticklerplus">
		<a href=# onClick ="popupPage(500,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' ; return true"><bean:message key="global.pref"/></a>
	    </caisi:isModuleLoad>	
            <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
		<a href=# onClick ="popupPage(400,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' ; return true"><bean:message key="global.pref"/></a>
            </caisi:isModuleLoad>
        </li>
         <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
        <li>
           <a HREF="#" onclick="popup('700', '1000', '../dms/documentReport.jsp?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>', 'edocView');" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewEdoc"/>'><bean:message key="global.edoc"/></a>
        </li>
         </caisi:isModuleLoad>
        <li>
           <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
	    <a HREF="#" ONCLICK ="popupPage2('../tickler/ticklerMain.jsp','<bean:message key="global.tickler"/>');return false;" TITLE='<bean:message key="global.tickler"/>'>
		<span id="oscar_new_tickler"><bean:message key="global.btntickler"/></span></a>
           </caisi:isModuleLoad>
           <caisi:isModuleLoad moduleName="ticklerplus">
	    <a HREF="#" ONCLICK ="popupPage2('../Tickler.do?filter.demographic_no=','<bean:message key="global.tickler"/>');return false;" TITLE='<bean:message key="global.tickler"/>'+'+'>
		<span id="oscar_new_tickler"><bean:message key="global.btntickler"/></span></a>
           </caisi:isModuleLoad>
        </li>
        <oscar:oscarPropertiesCheck property="WORKFLOW" value="yes">
           <li><a href="javascript: function myFunction() {return false; }" onClick="popup(700,1000,'../oscarWorkflow/WorkFlowList.jsp','<bean:message key="global.workflow"/>')"><bean:message key="global.btnworkflow"/></a></li> 
        </oscar:oscarPropertiesCheck>
         <oscar:oscarPropertiesCheck property="MY_OSCAR" value="yes">
            <myoscar:indivoRegistered provider="<%=curUser_no%>">                               
           
            <li>
                <a HREF="#" ONCLICK ="popup('600', '900','../phr/PhrMessage.do?method=viewMessages','INDIVOMESSENGER2<%=curUser_no%>')" title='<bean:message key="global.myoscar"/>'><phr:setColor><bean:message key="global.btnmyoscar"/></phr:setColor></a>
            </li>
            </myoscar:indivoRegistered>
        </oscar:oscarPropertiesCheck>
        <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
		<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc" rights="r">
          <li>
	     <a HREF="#" ONCLICK ="popupOscarRx(700,687,'../admin/admin.jsp');return false;"><bean:message key="global.admin"/></a>
          </li>
        </security:oscarSec>
		</caisi:isModuleLoad>
		
        <%int menuTagNumber=0; %> 
        <caisi:isModuleLoad moduleName="caisi">
           <li>
             <a href='<html:rewrite page="/PMmodule/ProviderInfo.do"/>'>Program</a>
	     <% menuTagNumber++ ; %>
           </li>
        </caisi:isModuleLoad>
	
    </ul>  <!--- old TABLE -->
    
  </td>
  
  <form method="post" name="findprovider" onSubmit="findProvider(<%=year%>,<%=month%>,<%=day%>);return false;" target="apptReception" action="receptionistfindprovider.jsp">
  <td align="right" valign="bottom">   
  <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
   <INPUT TYPE="text" NAME="providername" VALUE="" WIDTH="2" HEIGHT="10" border="0" size="10" maxlength="10">-
   <INPUT TYPE="SUBMIT" NAME="Go" VALUE='<bean:message key="receptionist.appointmentreceptionistadminday.btnGo"/>' onClick="findProvider(<%=year%>,<%=month%>,<%=day%>);return false;">

   <a href=# onClick ="popupPage(600,750,'<%=resourcebaseurl+"Support"%>')"><bean:message key="global.help"/></a>
   &nbsp;&nbsp;
   </caisi:isModuleLoad>
	  <a href="javascript: function myFunction() {return false; }" onClick="popup(700,1000,'../scratch/index.jsp','scratch')"><span id="oscar_scratch"></span></a>&nbsp;
  </td>
  </form>

</tr>
</table>





<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C0C0C0">
  <tr><td>
    <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
      <tr>
        <td BGCOLOR="ivory" width="33%">
         <a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day-1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
         &nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="<bean:message key="provider.appointmentProviderAdminDay.viewPrevDay"/>" vspace="2"></a>
         <b><span CLASS=title><%=formatDate%></span></b>
         <a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day+1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
         <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="<bean:message key="provider.appointmentProviderAdminDay.viewNextDay"/>" vspace="2">&nbsp;&nbsp;</a>
        <a href=# onClick ="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../provider/providercontrol.jsp&year=<%=strYear%>&month=<%=strMonth%>&param=<%=URLEncoder.encode("&view=0&displaymode=day&dboperation=searchappointmentday","UTF-8")%>')"><bean:message key="global.calendar"/></a></td>
        <td ALIGN="center"  BGCOLOR="ivory" width="33%">
	<% if(view==1) {%>
	<a href='providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=0&displaymode=day&dboperation=searchappointmentday'><bean:message key="provider.appointmentProviderAdminDay.grpView"/></a>
	<% } else { %>
	<B><bean:message key="global.hello"/>
	<% out.println( userfirstname+" "+userlastname); %>
	</b></TD>
	<% } %>
        <td ALIGN="RIGHT" BGCOLOR="Ivory">
<!-- caisi infirmary view extension add ffffffffffff-->
<caisi:isModuleLoad moduleName="caisi">
<table><tr><td>
    <caisi:ProgramExclusiveView providerNo="<%=curUser_no%>" value="appointment">
	<% session.setAttribute("infirmaryView_isOscar", "true"); %>
    </caisi:ProgramExclusiveView>
    <caisi:ProgramExclusiveView providerNo="<%=curUser_no%>" value="case-management">
	<% session.setAttribute("infirmaryView_isOscar", "false"); %>
    </caisi:ProgramExclusiveView>
</caisi:isModuleLoad>

<caisi:isModuleLoad moduleName="TORONTO_RFQ">
	<% session.setAttribute("infirmaryView_isOscar", "false"); %>
</caisi:isModuleLoad>
<!-- caisi infirmary view extension add end ffffffffffffff-->


<logic:notEqual name="infirmaryView_isOscar" value="false">

<%
//session.setAttribute("case_program_id", null);
%>
  <a href=# onClick = "popupPage(300,450,'providerchangemygroup.jsp?mygroup_no=<%=mygroupno%>' );return false;" title="<bean:message key="provider.appointmentProviderAdminDay.chGrpNo"/>"><bean:message key="global.group"/>:</a>
  <select name="mygroup_no" onChange="changeGroup(this)">
  <option value=".<bean:message key="global.default"/>">.<bean:message key="global.default"/></option>
<%
	request.getSession().setAttribute("archiveView","false");
	rst=apptMainBean.queryResultsCaisi( "searchmygroupno");
   rsgroup = (ResultSet) rst[0];
 	 while (rsgroup.next()) {
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%=rsgroup.getString("mygroup_no")%></option>
<%
 	 }
 	((Statement)rst[1]).close();
%>

<% rst = apptMainBean.queryResultsCaisi("searchprovider");
	rsgroup = (ResultSet) rst[0];
 	 while (rsgroup.next()) {
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=mygroupno.equals(rsgroup.getString("provider_no"))?"selected":""%> %> <%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
 	((Statement)rst[1]).close();
%>
</select>
<% if(request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1") ) { %>
         <a href=# onClick = "review('1')" title="<bean:message key="provider.appointmentProviderAdminDay.viewProvAval"/>"><bean:message key="provider.appointmentProviderAdminDay.schedView"/></a> &nbsp;|&nbsp;
<% } else {  %>
         <a href=# onClick = "review('1')" title="<bean:message key="provider.appointmentProviderAdminDay.viewAllProv"/>"><bean:message key="provider.appointmentProviderAdminDay.viewAll"/></a> &nbsp;|&nbsp;
<% } %>
</logic:notEqual>

<logic:equal name="infirmaryView_isOscar" value="false">
<%
	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
%>
&nbsp&nbsp&nbsp&nbsp
</logic:equal>

         <a href="../logout.jsp"><bean:message key="global.btnLogout"/> <img src="../images/next.gif"  border="0" width="10" height="9" align="absmiddle"> &nbsp;</a>
         
<!-- caisi infirmary view extension add fffffffffffff-->
<caisi:isModuleLoad moduleName="caisi">
	<%@ include file="infirmaryviewprogramlist.jsp" %>	
</caisi:isModuleLoad>
<!-- caisi infirmary view extension add end fffffffffffff-->

      </td>
      </tr>

      <tr><td colspan="3">
        <table border="0" cellpadding="0" bgcolor="#486ebd" cellspacing="0" width="100%">
        <tr>
<%
boolean bShowDocLink = false;
boolean bShowEncounterLink = false;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment.doctorLink" rights="r">
<% bShowDocLink = true; %>
</security:oscarSec>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r">
<% bShowEncounterLink = true; %>
</security:oscarSec>


<%
int hourCursor=0, minuteCursor=0, depth=everyMin; //depth is the period, e.g. 10,15,30,60min.
String am_pm=null;
boolean bColor=true, bColorHour=true; //to change color

int iCols=0, iRows=0, iS=0,iE=0,iSm=0,iEm=0; //for each S/E starting/Ending hour, how many events
int ih=0, im=0, iSn=0, iEn=0 ; //hour, minute, nthStartTime, nthEndTime, rowspan
boolean bFirstTimeRs=true;
boolean bFirstFirstR=true;
DBPreparedHandlerParam[] paramTickler =new DBPreparedHandlerParam[2];
String[] param =new String[2];
String strsearchappointmentday=request.getParameter("dboperation");
ResultSet rs = null;

boolean userAvail = true;
int me = -1;
for(int nProvider=0;nProvider<numProvider;nProvider++) {
	if(curUser_no.equals(curProvider_no[nProvider]) ) {
       //userInGroup = true;
		me = nProvider; break;
	}
}
   StringBuffer hourmin = null;
   String [] param1 = new String[2];
   for(int nProvider=0;nProvider<numProvider;nProvider++) {
     userAvail = true;
     int timecodeLength = dateTimeCodeBean.get(curProvider_no[nProvider])!=null?((String) dateTimeCodeBean.get(curProvider_no[nProvider]) ).length() : 4*24;
     depth = bDispTemplatePeriod ? (24*60 / timecodeLength) : everyMin; // add function to display different time slot
     param1[0] = strDate; //strYear+"-"+strMonth+"-"+strDay;
     param1[1] = curProvider_no[nProvider];
     rst = apptMainBean.queryResultsCaisi(param1, "search_scheduledate_single");
		
     rsgroup=(ResultSet) rst[0];
     //viewall function
     if(request.getParameter("viewall")==null || request.getParameter("viewall").equals("0") ) {
       if(!rsgroup.next() || rsgroup.getString("available").equals("0")) {
         if(nProvider!=me ) continue;
         else userAvail = false;
       }
     }
     ((Statement)rst[1]).close();
     bColor=bColor?false:true;
 %>
            <td valign="top" width="<%=1*100/numProvider%>%"> <!-- for the first provider's schedule -->

        <table border="0" cellpadding="0" bgcolor="#486ebd" cellspacing="0" width="100%"><!-- for the first provider's name -->
          <tr><td NOWRAP ALIGN="center" BGCOLOR="<%=bColor?"#bfefff":"silver"%>">
 <!-- caisi infirmary view extension modify ffffffffffff-->
  <logic:notEqual name="infirmaryView_isOscar" value="false">
          <b><input type='radio' name='flipview' onClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="Flip view"  >
          <a href=# onClick="goZoomView('<%=curProvider_no[nProvider]%>','<%=curProviderName[nProvider]%>')" onDblClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>" >
          <!--a href="providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider=<%=curProvider_no[nProvider]%>&curProviderName=<%=curProviderName[nProvider]%>&displaymode=day&dboperation=searchappointmentday" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>"-->
          <%=curProviderName[nProvider]%></a></b>
          <% if (!userAvail) {%>
          [<bean:message key="provider.appointmentProviderAdminDay.msgNotOnSched"/>]
          <% } %>
</logic:notEqual>
<logic:equal name="infirmaryView_isOscar" value="false">
	<%String prID="1"; %> 	
	<logic:present name="infirmaryView_programId">
	<%prID=(String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID); %>
	</logic:present>
	<logic:iterate id="pb" name="infirmaryView_programBeans" type="org.apache.struts.util.LabelValueBean">
	  	<%if (pb.getValue().equals(prID)) {%>
  		<b><%=pb.getLabel()%></label></b>
		<%} %>
  	</logic:iterate>
</logic:equal>
<!-- caisi infirmary view extension modify end ffffffffffffffff-->
</td></tr>
          <tr><td valign="top">

<!-- caisi infirmary view exteion add -->
<!--  fffffffffffffffffffffffffffffffffffffffffff-->
<caisi:isModuleLoad moduleName="caisi">
<%@ include file="infirmarydemographiclist.jsp" %>
</caisi:isModuleLoad>
<logic:notEqual name="infirmaryView_isOscar" value="false">
<!-- caisi infirmary view exteion add end ffffffffffffffffff-->
<!-- =============== following block is the original oscar code. -->
        <!-- table for hours of day start -->
        <table border="1" cellpadding="0" bgcolor="<%=userAvail?"#486ebd":"silver"%>" cellspacing="0" width="100%">
				<%
          bFirstTimeRs=true;
          bFirstFirstR=true;
  				param[0]=curProvider_no[nProvider];
	 				param[1]=year+"-"+month+"-"+day;//e.g."2001-02-02";
	 			
	 			rst=apptMainBean.queryResultsCaisi(param, strsearchappointmentday);
   				rs = (ResultSet)rst[0];

			    for(ih=startHour*60; ih<=(endHour*60+(60/depth-1)*depth); ih+=depth) { // use minutes as base
            hourCursor = ih/60;
            minuteCursor = ih%60;
            bColorHour=minuteCursor==0?true:false; //every 00 minute, change color

            //templatecode
            if(dateTimeCodeBean.get(curProvider_no[nProvider]) != null) {
	            int nLen = 24*60 / ((String) dateTimeCodeBean.get(curProvider_no[nProvider]) ).length();
	            int ratio = (hourCursor*60+minuteCursor)/nLen;
              hourmin = new StringBuffer(dateTimeCodeBean.get(curProvider_no[nProvider])!=null?((String) dateTimeCodeBean.get(curProvider_no[nProvider])).substring(ratio,ratio+1):" " );
            } else { hourmin = new StringBuffer(); }
        %>
          <tr>
            <td align="RIGHT" bgcolor="<%=bColorHour?"#3EA4E1":"#00A488"%>" width="5%" NOWRAP><b><font face="verdana,arial,helvetica" size="2">
             <a href=# onClick="confirmPopupPage(400,780,'../appointment/addappointment.jsp?provider_no=<%=curProvider_no[nProvider]%>&bFirstDisp=<%=true%>&year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&start_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+ (minuteCursor<10?"0":"") +minuteCursor %>&end_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+(minuteCursor+depth-1)%>&duration=<%=dateTimeCodeBean.get("duration"+hourmin.toString())%>','<%= dateTimeCodeBean.get("confirm"+hourmin.toString()) %>');return false;"
  title='<%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+ (minuteCursor<10?"0":"")+minuteCursor)%> - <%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+((minuteCursor+depth-1)<10?"0":"")+(minuteCursor+depth-1))%>' class="adhour">
             <%=(hourCursor<10?"0":"") +hourCursor+ ":"%><%=(minuteCursor<10?"0":"")+minuteCursor%>&nbsp;</a></font></b></td>
            <td width='1%' <%=dateTimeCodeBean.get("color"+hourmin.toString())!=null?("bgcolor="+dateTimeCodeBean.get("color"+hourmin.toString()) ):""%> title='<%=dateTimeCodeBean.get("description"+hourmin.toString())%>'><font color='<%=(dateTimeCodeBean.get("color"+hourmin.toString())!=null && !dateTimeCodeBean.get("color"+hourmin.toString()).equals(bgcolordef) )?"black":"white" %>'><%=hourmin.toString() %></font>
            </td>
				<%
          	while (bFirstTimeRs?rs.next():true) { //if it's not the first time to parse the standard time, should pass it by
          	  len = bFirstTimeRs&&!bFirstFirstR?lenLimitedS:lenLimitedL;

          	  iS=Integer.parseInt(apptMainBean.getString(rs,"start_time").substring(0,2));
        	    iSm=Integer.parseInt(apptMainBean.getString(rs,"start_time").substring(3,5));
         	    iE=Integer.parseInt(apptMainBean.getString(rs,"end_time").substring(0,2));
     	        iEm=Integer.parseInt(apptMainBean.getString(rs,"end_time").substring(3,5));
          	  if( (ih < iS*60+iSm) && (ih+depth-1)<iS*60+iSm ) { //iS not in this period (both start&end), get to the next period
          	  	//out.println("<td width='10'>&nbsp;</td>"); //should be comment
          	  	bFirstTimeRs=false;
          	  	break;
          	  }
          	  if( (ih > iE*60+iEm) ) { //appt before this time slot (both start&end), get to the next period
          	  	//out.println("<td width='10'>&nbsp;</td>"); //should be comment
          	  	bFirstTimeRs=true;
          	  	continue;
          	  }
         	    iRows=((iE*60+iEm)-ih)/depth+1; //to see if the period across an hour period
         	    //iRows=(iE-iS)*60/depth+iEm/depth-iSm/depth+1; //to see if the period across an hour period

         	    //get time format: 00:00am/pm
         	    //String startTime = (iS>12?("0"+(iS-12)):apptMainBean.getString(rs,"start_time").substring(0,2))+":"+apptMainBean.getString(rs,"start_time").substring(3,5)+am_pm ;
         	    //String endTime   = (iE>12?("0"+(iE-12)):apptMainBean.getString(rs,"end_time").substring(0,2))  +":"+apptMainBean.getString(rs,"end_time").substring(3,5)+(iE<12?"am":"pm");
                    String name = UtilMisc.toUpperLowerCase(apptMainBean.getString(rs,"name"));
          	  int demographic_no = rs.getInt("demographic_no");
                  paramTickler[0]=new DBPreparedHandlerParam(String.valueOf(demographic_no));
                  paramTickler[1]=new DBPreparedHandlerParam(MyDateFormat.getSysDate(strDate)); //year+"-"+month+"-"+day;//e.g."2001-02-02";
                  rsTickler = null;
                  rsTickler = apptMainBean.queryResults_paged(paramTickler, "search_tickler", 0);
                  tickler_no = "";
                  tickler_note="";
                  while (rsTickler.next()){
                      tickler_no = rsTickler.getString("tickler_no");
                      tickler_note = rsTickler.getString("message")==null?tickler_note:tickler_note + "\n" + rsTickler.getString("message");
                  }
                  rsDemo = null;
                  ver = "";
                  roster = "";
                  rsDemo = apptMainBean.queryResults(demographic_no, "search_demograph");
                  if(rsDemo.next()){
                    ver = rsDemo.getString("ver");
                    roster = rsDemo.getString("roster_status");
                    int intMob = rsDemo.getInt("month_of_birth");
                    int intDob = rsDemo.getInt("date_of_birth");
		    mob = String.valueOf(intMob);
                    dob = String.valueOf(intDob);
                    demBday = mob + "-" + dob;

                    if (roster == null || !roster.equalsIgnoreCase("FS")) {
                        roster = "";
                    }
                  }
                  study_no = new StringBuffer("");
                  study_link = new StringBuffer("");
				  studyDescription = new StringBuffer("");
                  rsStudy = null;
                  rsStudy = apptMainBean.queryResults(demographic_no, "search_studycount");
				  int numStudy = 0;
				  if (rsStudy.next()) numStudy =  rsStudy.getInt(1);
				  if (numStudy == 1) {
                      rsStudy = null;
                      rsStudy = apptMainBean.queryResults(demographic_no, "search_study");
                      while (rsStudy.next()){
                          study_no = new StringBuffer(rsStudy.getString("s.study_no"));
                          study_link = new StringBuffer(rsStudy.getString("s.study_link"));
                          studyDescription = new StringBuffer(rsStudy.getString("s.description"));
                      }
				  } else if (numStudy > 1) {
                      study_no = new StringBuffer("0");
                      study_link = new StringBuffer("formstudy.jsp");
				      studyDescription = new StringBuffer("Form Studies");
				  }

          	  String reason = apptMainBean.getString(rs,"reason");
          	  String notes = apptMainBean.getString(rs,"notes");
          	  String status = (apptMainBean.getString(rs,"status")).trim();
          	  bFirstTimeRs=true;
			    as.setApptStatus(status);
        %>
            <td bgcolor='<%=as.getBgColor()%>' rowspan="<%=iRows%>" <%-- =view==0?(len==lenLimitedL?"nowrap":""):"nowrap"--%> nowrap>

            <%
			    if (as.getNextStatus() != null && !as.getNextStatus().equals("")) {
            %>
            <a href="providercontrol.jsp?appointment_no=<%=apptMainBean.getString(rs,"appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&status=&statusch=<%=as.getNextStatus()%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=addstatus&dboperation=updateapptstatus&viewall=<%=request.getParameter("viewall")==null?"0":(request.getParameter("viewall"))%>"; title="<%=as.getTitle()%> " > 
            <%
				}
			    if (as.getNextStatus() != null) {
            %>
            <img src="../images/<%=as.getImageName()%>" border="0" height="10" title="<%=as.getTitle()%>"></a> 
            <%
                } else {
	                out.print("&nbsp;");
                }
            %>
<%--|--%>
        <%
        			if(demographic_no==0) {
        %>
        	<!--  caisi  -->
        	<% if (tickler_no.compareTo("") != 0) {%>	
	        	<caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
        			<a href="#" onClick="popupPage(700,1000, '../tickler/ticklerDemoMain.jsp?demoview=0');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a>
    			</caisi:isModuleLoad>
    			<caisi:isModuleLoad moduleName="ticklerplus">
    				<a href="../ticklerPlus/index.jsp" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a>
    			</caisi:isModuleLoad>
    		<%} %>	    		
<a href=# onClick ="popupPage(400,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=apptMainBean.getString(rs,"appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=0&displaymode=edit&dboperation=search');return false;" title="<%=iS+":"+(iSm>10?"":"0")+iSm%>-<%=iE+":"+iEm%>
<%=name%>
<bean:message key="provider.appointmentProviderAdminDay.reason"/>: <%=UtilMisc.htmlEscape(reason)%>
<bean:message key="provider.appointmentProviderAdminDay.notes"/>: <%=UtilMisc.htmlEscape(notes)%>" >
            .<%=(view==0&&numAvailProvider!=1)?(name.length()>len?name.substring(0,len).toUpperCase():name.toUpperCase()):name.toUpperCase()%></font></a></td>
        <%
        			} else {
        			  //System.out.println(name+" / " +demographic_no);
				%>	<% if (tickler_no.compareTo("") != 0) {%>
			        	<caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
                                        <a href="#" onClick="popupPage(700,1000, '../tickler/ticklerDemoMain.jsp?demoview=<%=demographic_no%>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a>
    					</caisi:isModuleLoad>
    					<caisi:isModuleLoad moduleName="ticklerplus">
		    				<!--  <a href="../Tickler.do?method=filter&filter.client=<%=demographic_no %>" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a> -->
    						<a href="#" onClick="popupPage(700,1000, '../Tickler.do?method=filter&filter.client=<%=demographic_no %>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a> 	
    					</caisi:isModuleLoad>
					<%} %>

<!-- doctor code block -->
<% if(bShowDocLink) { %>
<!-- security:oscarSec roleName="<%--=roleName$--%>" objectName="_appointment.doctorLink" rights="r" -->
<% if ("".compareTo(study_no.toString()) != 0) {%>	<a href="#" onClick="popupPage(700,1000, '../form/study/forwardstudyname.jsp?study_link=<%=study_link.toString()%>&demographic_no=<%=demographic_no%>&study_no=<%=study_no%>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.study"/>: <%=UtilMisc.htmlEscape(studyDescription.toString())%>"><%="<font color='"+studyColor+"'>"+studySymbol+"</font>"%></a><%} %>

<% if (ver!=null && ver!="" && "##".compareTo(ver.toString()) == 0){%><a href="#" title="<bean:message key="provider.appointmentProviderAdminDay.versionMsg"/> <%=UtilMisc.htmlEscape(ver)%>"> <font color="red">*</font><%}%>

<% if (roster!="" && "FS".equalsIgnoreCase(roster)){%> <a href="#" title="<bean:message key="provider.appointmentProviderAdminDay.rosterMsg"/> <%=UtilMisc.htmlEscape(roster)%>"><font color="red">$</font><%}%>
<!-- /security:oscarSec -->
<% } %>
<!-- doctor code block -->

<a href=# onClick ="popupPage(400,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=apptMainBean.getString(rs,"appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search');return false;"  <oscar:oscarPropertiesCheck property="SHOW_APPT_REASON_TOOLTIP" value="yes" defaultVal="true"> title="<%=name%>
&nbsp; reason: <%=UtilMisc.htmlEscape(reason)%>
&nbsp; notes: <%=UtilMisc.htmlEscape(notes)%>"</oscar:oscarPropertiesCheck>   ><%=(view==0)?(name.length()>len?name.substring(0,len):name):name%></a>
<% if(len==lenLimitedL || view!=0 || numAvailProvider==1 ) {%>
<!-- doctor code block -->
<% if(bShowEncounterLink) { %>
<% String  eURL = "../oscarEncounter/IncomingEncounter.do?providerNo="+curUser_no+"&appointmentNo="+apptMainBean.getString(rs,"appointment_no")+"&demographicNo="+demographic_no+"&curProviderNo="+curProvider_no[nProvider]+"&reason="+URLEncoder.encode(reason)+"&encType="+URLEncoder.encode("face to face encounter with client","UTF-8")+"&userName="+URLEncoder.encode( userfirstname+" "+userlastname)+"&curDate="+curYear+"-"+curMonth+"-"+curDay+"&appointmentDate="+year+"-"+month+"-"+day+"&startTime="+iS+":"+iSm+"&status="+status;%>
<a href=# onClick="popupPage(710, 1024,'<%=eURL%>');return false;" title="<bean:message key="global.encounter"/>">
            |<bean:message key="provider.appointmentProviderAdminDay.btnE"/></a>
<% } %>
<!-- billing code block -->
	  <security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r">

            <%= bShortcutForm?"<a href=# onClick='popupPage2( \"../form/forwardshortcutname.jsp?formname="+formName+"&demographic_no="+demographic_no+"\")' title='form'>|"+formNameShort+"</a>" : ""%>
            <%= bShortcutForm2?"<a href=# onClick='popupPage2( \"../form/forwardshortcutname.jsp?formname="+formName2+"&demographic_no="+demographic_no+"\")' title='form'>|"+formName2Short+"</a>" : ""%>
             <% if(status.indexOf('B')==-1) {
                //java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.action.Action.LOCALE_KEY);
                if (vLocale.getCountry().equals("BR")) { %>
               <a href=# onClick='popupPage(700,1000, "../oscar/billing/procedimentoRealizado/init.do?appId=<%=apptMainBean.getString(rs,"appointment_no")%>");return false;' title="Faturamento">|FAT|</a>
             <% } else {%>
	       <!--<a href=# onClick='popupPage(700,1000, "../billing/billingOB.jsp?billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=apptMainBean.getString(rs,"appointment_no")%>&demographic_name=<%=URLEncoder.encode(name)%>&status=<%=status%>&demographic_no=<%=demographic_no%>&providerview=<%=curProvider_no[nProvider]%>&user_no=<%=curUser_no%>&apptProvider_no=<%=curProvider_no[nProvider]%>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=iS+":"+iSm%>&bNewForm=1");return false;' title="<bean:message key="global.billing"/>">|<bean:message key="provider.appointmentProviderAdminDay.btnB"/>|</a>-->
           <!--    <a href=# onClick='popupPage(700,1000, "../billing.do?billRegion=<%=URLEncoder.encode(oscarVariables.getProperty("billregion"))%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=apptMainBean.getString(rs,"appointment_no")%>&demographic_name=<%=URLEncoder.encode(name)%>&status=<%=status%>&demographic_no=<%=demographic_no%>&providerview=<%=curProvider_no[nProvider]%>&user_no=<%=curUser_no%>&apptProvider_no=<%=curProvider_no[nProvider]%>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=iS+":"+iSm%>&bNewForm=1");return false;' title="<bean:message key="global.billing"/>">|<bean:message key="provider.appointmentProviderAdminDay.btnB"/>|</a>-->
               <a href=# onClick='popupPage(755,1200, "../billing.do?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=apptMainBean.getString(rs,"appointment_no")%>&demographic_name=<%=URLEncoder.encode(name)%>&status=<%=status%>&demographic_no=<%=demographic_no%>&providerview=<%=curProvider_no[nProvider]%>&user_no=<%=curUser_no%>&apptProvider_no=<%=curProvider_no[nProvider]%>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=iS+":"+iSm%>&bNewForm=1");return false;' title="<bean:message key="global.billing"/>">|<bean:message key="provider.appointmentProviderAdminDay.btnB"/></a>

             <% } %>
<% } else {%>
    <a href=# onClick='onUnbilled("../billing/CA/<%=prov%>/billingDeleteWithoutNo.jsp?status=<%=status%>&appointment_no=<%=apptMainBean.getString(rs,"appointment_no")%>");return false;' title="<bean:message key="global.billingtag"/>">|-<bean:message key="provider.appointmentProviderAdminDay.btnB"/></a>
<% } %>
<!--/security:oscarSec-->
	  </security:oscarSec>
<!-- billing code block -->
<security:oscarSec roleName="<%=roleName$%>" objectName="_masterLink" rights="r">
    <% if (vLocale.getCountry().equals("BR")) {%>
    <a href="javascript: function myFunction() {return false; }" onClick="popup(700,1000,'../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail_ptbr','master')"
    title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>">|<bean:message key="provider.appointmentProviderAdminDay.btnM"/></a>
    <%}else{%>
    <a href="javascript: function myFunction() {return false; }" onClick="popup(700,1000,'../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail','master')"
    title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>">|<bean:message key="provider.appointmentProviderAdminDay.btnM"/></a>
    <%}%>
</security:oscarSec>
      <% if (!vLocale.getCountry().equals("BR")) { %>

<!-- doctor code block -->

          <!-- doctor code block -->
	  <security:oscarSec roleName="<%=roleName$%>" objectName="_appointment.doctorLink" rights="r">
      <a href=# onClick="popupOscarRx(700,960,'../oscarRx/choosePatient.do?providerNo=<%=curUser_no%>&demographicNo=<%=demographic_no%>')" title="<bean:message key="global.prescriptions"/>">|<bean:message key="global.rx"/>
      </a>
       <%
	  if("bc".equalsIgnoreCase(prov)){
	  if(patientHasOutstandingPrivateBills(String.valueOf(demographic_no))){
	  %>
	  |<b style="color:#FF0000">$</b>
	  <%}}%>
      <oscar:oscarPropertiesCheck property="SHOW_APPT_REASON" value="yes">| <b><%=reason%></b></oscar:oscarPropertiesCheck>
	  </security:oscarSec>
	 
	  <!-- add one link to caisi Program Management Module -->
	  <caisi:isModuleLoad moduleName="caisi">
	  	<!--<a href=# onClick="popupPage(700, 1048,'../PMmodule/ClientManager.do?id=<%=demographic_no%>')" title="Program Management">|P</a>-->
	  	<a href='../PMmodule/ClientManager.do?id=<%=demographic_no%>' title="Program Management">|P</a>
    </caisi:isModuleLoad>   
          <%
     //out.print(monthDay + " " + demBday);
      if(isBirthday(monthDay,demBday)){%>
      	| <img src="../images/cake.gif" height="20"/>

      <%}%>
      <% } %>
<% } %>
        		</font></td>
        <%
        			}
        			bFirstFirstR = false;
          	}
            //out.println("<td width='1'>&nbsp;</td></tr>"); give a grid display
            out.println("<td width='1'></td></tr>"); //no grid display
          }
		  ((Statement)rst[1]).close();
    			//apptMainBean.closePstmtConn();
				%>

          </table> <!-- end table for each provider schedule display -->
<!-- caisi infirmary view extension add fffffffffff-->
</logic:notEqual>
<!-- caisi infirmary view extension add end fffffffffffffff-->

         </td></tr>
          <tr><td ALIGN="center" BGCOLOR="<%=bColor?"#bfefff":"silver"%>">
<!-- caisi infirmary view extension modify fffffffffffffffffff-->
<logic:notEqual name="infirmaryView_isOscar" value="false">
          <b><input type='radio' name='flipview' onClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="<bean:message key="provider.appointmentProviderAdminDay.flipView"/>"  >
          <a href=# onClick="goZoomView('<%=curProvider_no[nProvider]%>','<%=curProviderName[nProvider]%>')" onDblClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>" >
          <!--a href="providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider=<%=curProvider_no[nProvider]%>&curProviderName=<%=curProviderName[nProvider]%>&displaymode=day&dboperationhappo=searcintmentday" title="zoom view"-->
          <%=curProviderName[nProvider]%></a></b>
          <% if(!userAvail) { %>
          [<bean:message key="provider.appointmentProviderAdminDay.msgNotOnSched"/>]
          <% } %>
</logic:notEqual>
<logic:equal name="infirmaryView_isOscar" value="false">
	<%String prID="1"; %> 
	<logic:present name="infirmaryView_programId">
        <%prID=(String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID); %>
	</logic:present>
	<logic:iterate id="pb" name="infirmaryView_programBeans" type="org.apache.struts.util.LabelValueBean">
	  	<%if (pb.getValue().equals(prID)) {%>
  		<b><%=pb.getLabel()%></label></b>
		<%} %>
  	</logic:iterate>
</logic:equal>
<!-- caisi infirmary view extension modify end ffffffffffffffffff-->
          </td></tr>

       </table><!-- end table for each provider name -->

            </td>
 <%
   } //end of display team a, etc.   
   apptMainBean.closePstmtConn();
   
 %>


          </tr>
        </table>        <!-- end table for the whole schedule row display -->




        </td>
      </tr>

      <tr><td colspan="3">
      <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
  			<tr>
        	<td BGCOLOR="ivory" width="50%">
         	 <a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day-1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
         	 &nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="<bean:message key="provider.appointmentProviderAdminDay.viewPrevDay"/>" vspace="2"></a>
           <b><span CLASS=title><%=formatDate%></span></b>
           <a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day+1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
           <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="<bean:message key="provider.appointmentProviderAdminDay.viewNextDay"/>" vspace="2">&nbsp;&nbsp;</a></td>
        	<td ALIGN="RIGHT" BGCOLOR="Ivory">
           <a href="../logout.jsp"><bean:message key="global.btnLogout"/> <img src="../images/next.gif"  border="0" width="10" height="9" align="absmiddle"> &nbsp;</a> </td>
  			</TR>
			</table>
		</td></tr>

	</table>
	</td></tr>
</table>
</body>
<!-- key shortcut hotkey block added by phc -->
<script language="JavaScript">

// popup blocking for the site must be off!
// developed on Windows FF 2, 3 IE 6 Linux FF 1.5 
// FF on Mac and Opera on Windows work but will require shift or control with alt and Alpha 
// to fire the altKey + Alpha combination - strange

// Modification Notes:
//     event propagation has not been blocked beyond returning false for onkeydown (onkeypress may or may not fire depending)
//     keyevents have not been even remotely standardized so test mods across agents/systems or something will break!
//     use popupOscarRx so that this codeblock can be cut and pasted to appointmentprovideradminmonth.jsp

// Internationalization Notes:
//     underlines should be added to the labels to prompt/remind the user and should correspond to
//     the actual key whose keydown fires, which is also stored in the oscarResources.properties files
//     if you are using the keydown/up event the value stored is the actual key code
//     which, at least with a US keyboard, also is the uppercase ASCII code, ie A keyCode=65

document.onkeydown=function(e){
	evt = e || window.event;  // window.event is the IE equivalent
	if (evt.altKey) {
		//use (evt.altKey || evt.metaKey) for Mac if you want Apple+A, you will probably want a seperate onkeypress handler in that case to return false to prevent propagation
		switch(evt.keyCode) {
			case <bean:message key="global.adminShortcut"/> : popupOscarRx(700,687,'../admin/admin.jsp');  return false;  //run code for 'A'dmin
			case <bean:message key="global.billingShortcut"/> : popupOscarRx(600,1000,'../billing/CA/<%=prov%>/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;  //code for 'B'illing
			case <bean:message key="global.calendarShortcut"/> : popupOscarRx(310,430,'../share/CalendarPopup.jsp?urlfrom=../provider/providercontrol.jsp&year=<%=strYear%>&month=<%=strMonth%>&param=<%=URLEncoder.encode("&view=0&displaymode=day&dboperation=searchappointmentday","UTF-8")%>');  return false;  //run code for 'C'alendar
			case <bean:message key="global.edocShortcut"/> : popupOscarRx('700', '1000', '../dms/documentReport.jsp?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>', 'edocView');  return false;  //run code for e'D'oc
			case <bean:message key="global.resourcesShortcut"/> : popupOscarRx(550,687,'<%=resourcebaseurl%>'); return false; // code for R'e'sources
			case <bean:message key="global.helpShortcut"/> : popupOscarRx(600,750,'<%=resourcebaseurl+"Support"%>');  return false;  //run code for 'H'elp
			case <bean:message key="global.ticklerShortcut"/> : {  
				<caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
					popupOscarRx(700,1000,'../tickler/ticklerMain.jsp','<bean:message key="global.tickler"/>') //run code for t'I'ckler  
				</caisi:isModuleLoad>
				<caisi:isModuleLoad moduleName="ticklerplus">
					popupOscarRx(700,1000,'../Tickler.do','<bean:message key="global.tickler"/>'); //run code for t'I'ckler+  
				</caisi:isModuleLoad>
				return false;
			}
			case <bean:message key="global.labShortcut"/> : popupOscarRx(600,900,'../oscarMDS/Index.jsp?providerNo=<%=curUser_no%>', '<bean:message key="global.lab"/>');  return false;  //run code for 'L'ab
			case <bean:message key="global.msgShortcut"/> : popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>'); return false;  //run code for 'M'essage
			case <bean:message key="global.monthShortcut"/> : window.open("providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth","_self"); return false ;  //run code for Mo'n'th
			case <bean:message key="global.conShortcut"/> : popupOscarRx(625,900,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>');  return false;  //run code for c'O'nsultation
			case <bean:message key="global.reportShortcut"/> : popupOscarRx(650,900,'../report/reportindex.jsp','reportPage');  return false;  //run code for 'R'eports
			case <bean:message key="global.prefShortcut"/> : {
				    <caisi:isModuleLoad moduleName="ticklerplus">
					popupOscarRx(500,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>'); //run code for tickler+ 'P'references 
					return false;   
				    </caisi:isModuleLoad>	
			            <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
					popupOscarRx(400,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>'); //run code for 'P'references 
					return false; 
			            </caisi:isModuleLoad>
			}
			case <bean:message key="global.searchShortcut"/> : popupOscarRx(550,687,'../demographic/search.jsp');  return false;  //run code for 'S'earch
			case <bean:message key="global.dayShortcut"/> : window.open("providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday","_self") ;  return false;  //run code for 'T'oday                       
			case <bean:message key="global.viewShortcut"/> : {
				<% if(request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1") ) { %>
				         review('0');  return false; //scheduled providers 'V'iew 
				<% } else {  %>
				         review('1');  return false; //all providers 'V'iew 
				<% } %>
			}
			case <bean:message key="global.workflowShortcut"/> : popupOscarRx(700,1000,'../oscarWorkflow/WorkFlowList.jsp','<bean:message key="global.workflow"/>'); return false ; //code for 'W'orkflow
			case <bean:message key="global.myoscarShortcut"/> : popupOscarRx('600', '900','../phr/PhrMessage.do?method=viewMessages','INDIVOMESSENGER2<%=curUser_no%>')
			default : return;
               }
	}
	if (evt.ctrlKey) {
               switch(evt.keyCode || evt.charCode) {
			case <bean:message key="global.btnLogoutShortcut"/> : window.open('../logout.jsp','_self');  return false;  // 'Q'uit/log out
			default : return;
               }        

        }
}

</script>
<!-- end of keycode block -->
</html:html>
<%
long finLoad = System.currentTimeMillis();
System.out.println("TOTAL LOAD TIME FOR DAY SHEET:" + (finLoad-loadPage)*.001);
%>
