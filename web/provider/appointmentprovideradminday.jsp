<%@ page import="java.lang.*, java.util.*, java.text.*,java.sql.*, java.net.*, oscar.*, oscar.util.*" errorPage="errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/msg-tag.tld" prefix="oscarmessage" %>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<jsp:useBean id="as" class="oscar.appt.ApptStatusData" scope="page" />
<jsp:useBean id="dateTimeCodeBean" class="java.util.Hashtable" scope="page" />

<!-- Struts for i18n -->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
    if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("doctor"))
        response.sendRedirect("../logout.jsp");

	oscar.oscarSecurity.CookieSecurity cs = new oscar.oscarSecurity.CookieSecurity();
    response.addCookie(cs.GiveMeACookie());

	String curUser_no = (String) session.getAttribute("user");
    String mygroupno = (String) session.getAttribute("groupno");  
    String userfirstname = (String) session.getAttribute("userfirstname");
    String userlastname = (String) session.getAttribute("userlastname");

	boolean bShortcutForm = oscarVariables.getProperty("appt_formview", "").equalsIgnoreCase("on") ? true : false;
	String formName = bShortcutForm ? oscarVariables.getProperty("appt_formview_name") : ""; 
	String formNameShort = formName.length() > 3 ? (formName.substring(0,2)+".") : formName; 

    //String userprofession = (String) session.getAttribute("userprofession");
    int startHour=Integer.parseInt(((String) session.getAttribute("starthour")).trim());
    int endHour=Integer.parseInt(((String) session.getAttribute("endhour")).trim());
    int everyMin=Integer.parseInt(((String) session.getAttribute("everymin")).trim());

    int lenLimitedL=11, lenLimitedS=3; //L - long, S - short
    int len = lenLimitedL;
    int view = request.getParameter("view")!=null ? Integer.parseInt(request.getParameter("view")) : 0; //0-multiple views, 1-single view
%>
<%
    ResultSet rsTickler = null;
    ResultSet rsStudy = null;
    String tickler_no="", textColor="", tickler_note="";
    StringBuffer study_no=null, study_link=null,studyDescription=null;
	String studySymbol = "#", studyColor = "red";

    String resourcebaseurl = "http://67.69.12.117:8080/oscarResource/";
    ResultSet rsgroup1 = apptMainBean.queryResults("resource_baseurl", "search_resource_baseurl");
    while (rsgroup1.next()) { 
 	    resourcebaseurl = rsgroup1.getString("value");
    }
    rsgroup1 = null;

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

<html:html locale="true">
<head>
<title><bean:message key="provider.appointmentProviderAdminDay.title"/></title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css" type="text/css">
<% response.setHeader("Cache-Control","no-cache");%>
<meta http-equiv="refresh" content="180;">
</head>
<script language="JavaScript">
<!--
function setfocus() {
  //this.focus();
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

function popupPage2(varpage) {
    popupPage2(varpage, "apptProviderSearch");
}

function popupPage2(varpage, windowname) {
    var page = "" + varpage;
    windowprops = "height=700,width=1000,location=no,"
    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=10,left=0";
    window.open(page, windowname, windowprops);
}

//<!--oscarMessenger code block-->
function popupOscarRx(vheight,vwidth,varpage) { 
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="global.oscarRx"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
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
  history.go(0);
}
function refresh1() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}
function onUnbilled(url) {
  if(confirm("<bean:message key="provider.appointmentProviderAdminDay.onUnbilled"/>")) {
    popupPage(700,720, url);
  }
}
function changeGroup(s) {
	var newGroupNo = s.options[s.selectedIndex].value;
	popupPage(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
}
function ts1(s) {
  popupPage(360,680,('../appointment/addappointment.jsp?'+s));
}
function tsr(s) {
  popupPage(360,680,('../appointment/appointmentcontrol.jsp?displaymode=edit&dboperation=search&'+s)); 
}
function goFilpView(s) {
	self.location.href = "../schedule/scheduleflipview.jsp?originalpage=../provider/providercontrol.jsp&startDate=<%=year+"-"+month+"-"+day%>" + "&provider_no="+s ;
}
function goZoomView(s, n) {
	self.location.href = "providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider="+s+"&curProviderName="+n+"&displaymode=day&dboperation=searchappointmentday" ;
}
//-->
</SCRIPT>
<body bgcolor="#EEEEFF" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<%
   int numProvider=0, numAvailProvider=0;
   String [] curProvider_no;
   String [] curProviderName;
   ResultSet rsgroup = null;
   //initial provider bean for all the application
   if(providerBean.isEmpty()) {
     rsgroup = apptMainBean.queryResults("searchallprovider");
 	   while (rsgroup.next()) { 
 	    providerBean.setProperty(rsgroup.getString("provider_no"), new String( rsgroup.getString("last_name")+","+rsgroup.getString("first_name") ));
 	   }
 	 }
   
   if(view==0) { //multiple views
	   rsgroup = apptMainBean.queryResults(mygroupno, "searchmygroupcount");
 	   while (rsgroup.next()) { 
       numProvider=rsgroup.getInt(1);
     }

       String [] param3 = new String [2];
       param3[0] = mygroupno;
       param3[1] = strDate; //strYear +"-"+ strMonth +"-"+ strDay ;
  	   rsgroup = apptMainBean.queryResults(param3, "search_numgrpscheduledate");
 	     while (rsgroup.next()) { 
         numAvailProvider = rsgroup.getInt(1);
       }

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

	   rsgroup = apptMainBean.queryResults(mygroupno, "searchmygroupprovider");
	   int iTemp=0;
     while (rsgroup.next()) { 
       curProvider_no[iTemp]=rsgroup.getString("provider_no");
       curProviderName[iTemp]=rsgroup.getString("first_name")+" "+rsgroup.getString("last_name");
       iTemp++;
     }
     }
   } else { //single view
     numProvider=1;
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];
     curProvider_no[0]=request.getParameter("curProvider");
     curProviderName[0]=request.getParameter("curProviderName");
   }
   
   //set timecode bean
   String bgcolordef = "#486ebd" ;
   String [] param3 = new String[2];
   param3[0] = strDate; //strYear+"-"+strMonth+"-"+strDay;
   for(int nProvider=0;nProvider<numProvider;nProvider++) {
     param3[1] = curProvider_no[nProvider];
	   rsgroup = apptMainBean.queryResults(param3, "search_appttimecode");
     while (rsgroup.next()) { 
       dateTimeCodeBean.put(rsgroup.getString("provider_no"), rsgroup.getString("timecode"));
     } 
   }
	 rsgroup = apptMainBean.queryResults("search_timecode");
   while (rsgroup.next()) { 
     dateTimeCodeBean.put("description"+rsgroup.getString("code"), rsgroup.getString("description"));
     dateTimeCodeBean.put("duration"+rsgroup.getString("code"), rsgroup.getString("duration"));
     dateTimeCodeBean.put("color"+rsgroup.getString("code"), (rsgroup.getString("color")==null || rsgroup.getString("color").equals(""))?bgcolordef:rsgroup.getString("color") );
   } 
   
   java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.action.Action.LOCALE_KEY);   
%>
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
<tr>
  <td VALIGN="BOTTOM" HEIGHT="20"> 

    <table BORDER="0" CELLPADDING="0" CELLSPACING="0" height="20">
      <tr>
        <td></td><td rowspan="2" BGCOLOR="ivory" ALIGN="MIDDLE" nowrap height="20"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a href="providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>' ; return true"> 
         &nbsp;&nbsp;<bean:message key="global.day"/>&nbsp;&nbsp; </a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewMonthSched"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewMonthSched"/>' ; return true"><bean:message key="global.month"/></a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a href="#" ONCLICK ="popupPage2('<%=resourcebaseurl%>');return false;" title="<bean:message key="global.resources"/>" onmouseover="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewResources"/>';return true"><bean:message key="global.resources"/></a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupPage2('../demographic/search.jsp');return false;"  TITLE='<bean:message key="global.searchPatientRecords"/>' OnMouseOver="window.status='<bean:message key="global.searchPatientRecords"/>' ; return true"><bean:message key="provider.appointmentProviderAdminDay.search"/></a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupPage2('../report/reportindex.jsp');return false;"   TITLE='<bean:message key="global.genReport"/>' OnMouseOver="window.status='<bean:message key="global.genReport"/>' ; return true"><bean:message key="global.report"/></a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">

             <% 
                //java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.action.Action.LOCALE_KEY);
                if (vLocale.getCountry().equals("BR")) { %>  
               <a HREF="#" ONCLICK ="popupPage2('../oscar/billing/consultaFaturamentoMedico/init.do');return false;" TITLE='<bean:message key="global.genBillReport"/>' onmouseover="window.status='<bean:message key="global.genBillReport"/>';return true"><bean:message key="global.billing"/></a></font></td>
             <% } else {%>  
				<a HREF="#" ONCLICK ="popupPage2('../billing/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;" TITLE='<bean:message key="global.genBillReport"/>' onmouseover="window.status='<bean:message key="global.genBillReport"/>';return true"><bean:message key="global.billing"/></a></font></td>
             <% } %>

        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupPage2('../oscarMDS/Index.jsp?providerNo=<%=curUser_no%>', '<bean:message key="global.lab"/>');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'><bean:message key="global.lab"/></a></font></td>

<!-- oscarMessenger code block -->
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')" title="<bean:message key="global.messenger"/>">
         <oscarmessage:newMessage providerNo="<%=curUser_no%>"/></a></font></td>
<!--/oscarMessenger code block -->
<!-- oscarEcounter/consultationRequest.jsp code block -->
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupOscarRx(600,900,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')" title="<bean:message key="provider.appointmentProviderAdminDay.viewConReq"/>">
         <bean:message key="global.con"/></a></font>
         </td>
<!--/oscarEcounter code block -->

        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a href=# onClick ="popupPage(200,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' ; return true"><bean:message key="global.pref"/></a></font></td>
       
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupPage2('../dms/documentReport.jsp?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewEdoc"/>'><bean:message key="global.edoc"/></a></font></td>
   <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupPage2('../tickler/ticklerMain.jsp');return false;" TITLE='<bean:message key="global.tickler"/>'><bean:message key="global.tickler"/></a></font></td>

<td></td>
      </tr><tr> 
        <td valign="bottom"><img src="../images/tabs_l_active_end_alone.gif" width="14" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_r_active_end.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
<!-- oscarMessenger code block -->
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
<!--/oscarMessenger code block -->
<!-- oscarMessenger code block -->
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
<!--/oscarMessenger code block -->
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        
        <td valign="bottom"><img src="../images/tabs_r_inactive_end.gif" width="17" height="20" border="0"></td>
      </tr>
    </table>

  </td>
  <td align="right" valign="bottom">
  <a href=# onClick ="popupPage(600,750,'<%=resourcebaseurl+"Support"%>')"><bean:message key="global.help"/></a>  
  &nbsp;&nbsp;
  </td>
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
        <a href=# onClick ="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../provider/providercontrol.jsp&year=<%=strYear%>&month=<%=strMonth%>&param=<%=URLEncoder.encode("&view=0&displaymode=day&dboperation=searchappointmentday")%>')"><bean:message key="global.calendar"/></a></td>
        <td ALIGN="center"  BGCOLOR="ivory" width="33%">
	<% if(view==1) {%>
	<a href='providercontrol.jsp?year="+strYear+"&month="+strMonth+"&day="+strDay+"&view=0&displaymode=day&dboperation=searchappointmentday'><bean:message key="provider.appointmentProviderAdminDay.grpView"/></a>
	<% } else { %>
	<B><bean:message key="global.hello"/>
	<% out.println( userfirstname+" "+userlastname); %>
	</b></TD> 
	<% } %>
        <td ALIGN="RIGHT" BGCOLOR="Ivory">
  <a href=# onClick = "popupPage(300,450,'providerchangemygroup.jsp?mygroup_no=<%=mygroupno%>' );return false;" title="<bean:message key="provider.appointmentProviderAdminDay.chGrpNo"/>"><bean:message key="global.group"/>:</a>
  <select name="mygroup_no" onChange="changeGroup(this)">
  <option value=".<bean:message key="global.default"/>">.<bean:message key="global.default"/></option>
<%
   rsgroup = apptMainBean.queryResults( "searchmygroupno");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%=rsgroup.getString("mygroup_no")%></option>
<%
 	 }
%>
</select>
<% if(request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1") ) { %>        
         <a href=# onClick = "review('0')" title="<bean:message key="provider.appointmentProviderAdminDay.viewProvAval"/>"><bean:message key="provider.appointmentProviderAdminDay.schedView"/></a> &nbsp;|&nbsp; 
<% } else {  %>         
         <a href=# onClick = "review('1')" title="<bean:message key="provider.appointmentProviderAdminDay.viewAllProv"/>"><bean:message key="provider.appointmentProviderAdminDay.viewAll"/></a> &nbsp;|&nbsp; 
<% } %>
         <a href="../logout.jsp"><bean:message key="global.btnLogout"/> <img src="../images/next.gif"  border="0" width="10" height="9" align="absmiddle"> &nbsp;</a> </td>
      </tr>

      <tr><td colspan="3">
        <table border="0" cellpadding="0" bgcolor="#486ebd" cellspacing="0" width="100%">
        <tr>
				<%
          int hourCursor=0, minuteCursor=0, depth=everyMin; //depth is the period, e.g. 10,15,30,60min.
          String am_pm=null;
          boolean bColor=true, bColorHour=true; //to change color 

   int iCols=0, iRows=0, iS=0,iE=0,iSm=0,iEm=0; //for each S/E starting/Ending hour, how many events
   int ih=0, im=0, iSn=0, iEn=0 ; //hour, minute, nthStartTime, nthEndTime, rowspan
   boolean bFirstTimeRs=true;
   boolean bFirstFirstR=true;
 	 String[] paramTickler =new String[2];
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
//     bColor=bColor?false:true;
     userAvail = true; 
     param1[0] = strDate; //strYear+"-"+strMonth+"-"+strDay;
     param1[1] = curProvider_no[nProvider];
     rsgroup = apptMainBean.queryResults(param1,"search_scheduledate_single");
     
     //viewall function 
     if(request.getParameter("viewall")==null || request.getParameter("viewall").equals("0") ) {
       if(!rsgroup.next() || rsgroup.getString("available").equals("0")) {
         if(nProvider!=me ) continue;
         else userAvail = false;
       }
     }
     bColor=bColor?false:true;
 %>
            <td valign="top" width="<%=1*100/numProvider%>%"> <!-- for the first provider's schedule -->
         
        <table border="0" cellpadding="0" bgcolor="#486ebd" cellspacing="0" width="100%"><!-- for the first provider's name -->
          <tr><td ALIGN="center" BGCOLOR="<%=bColor?"#bfefff":"silver"%>">
          <b><input type='radio' name='flipview' onClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="Flip view"  >
          <a href=# onClick="goZoomView('<%=curProvider_no[nProvider]%>','<%=curProviderName[nProvider]%>')" onDblClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>" >
          <!--a href="providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider=<%=curProvider_no[nProvider]%>&curProviderName=<%=curProviderName[nProvider]%>&displaymode=day&dboperation=searchappointmentday" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>"-->
          <%=curProviderName[nProvider]%></a></b>
          <% if (!userAvail) {%>
          [<bean:message key="provider.appointmentProviderAdminDay.msgNotOnSched"/>]
          <% } %>
</td></tr>
          <tr><td valign="top">
        
        <!-- table for hours of day start --> 
        <table border="1" cellpadding="0" bgcolor="<%=userAvail?"#486ebd":"silver"%>" cellspacing="0" width="100%">
				<%
          bFirstTimeRs=true;
          bFirstFirstR=true;
  				param[0]=curProvider_no[nProvider];
	 				param[1]=year+"-"+month+"-"+day;//e.g."2001-02-02";
   				rs = apptMainBean.queryResults(param, strsearchappointmentday);

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
             <a href=# onClick ="popupPage(360,680,'../appointment/addappointment.jsp?provider_no=<%=curProvider_no[nProvider]%>&bFirstDisp=<%=true%>&year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&start_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+ (minuteCursor<10?"0":"") +minuteCursor %>&end_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+(minuteCursor+depth-1)%>&duration=<%=dateTimeCodeBean.get("duration"+hourmin.toString())%>');return false;" 
             title='<%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+ (minuteCursor<10?"0":"")+minuteCursor)%> - <%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+((minuteCursor+depth-1)<10?"0":"")+(minuteCursor+depth-1))%>' class="adhour">
             <%=(hourCursor<10?"0":"") +hourCursor+ ":"%><%=(minuteCursor<10?"0":"")+minuteCursor%>&nbsp;</a></font></b></td>
            <td width='1%' <%=dateTimeCodeBean.get("color"+hourmin.toString())!=null?("bgcolor="+dateTimeCodeBean.get("color"+hourmin.toString()) ):""%> title='<%=dateTimeCodeBean.get("description"+hourmin.toString())%>'><font color='<%=(dateTimeCodeBean.get("color"+hourmin.toString())!=null && !dateTimeCodeBean.get("color"+hourmin.toString()).equals(bgcolordef) )?"black":"white" %>'><%=hourmin.toString() %></font>
            </td>
				<%
          	while (bFirstTimeRs?rs.next():true) { //if it's not the first time to parse the standard time, should pass it by
          	  len = bFirstTimeRs&&!bFirstFirstR?lenLimitedS:lenLimitedL;
          	  
          	  iS=Integer.parseInt(rs.getString("start_time").substring(0,2));
        	    iSm=Integer.parseInt(rs.getString("start_time").substring(3,5));
         	    iE=Integer.parseInt(rs.getString("end_time").substring(0,2));
     	        iEm=Integer.parseInt(rs.getString("end_time").substring(3,5));
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
         	    //String startTime = (iS>12?("0"+(iS-12)):rs.getString("start_time").substring(0,2))+":"+rs.getString("start_time").substring(3,5)+am_pm ; 
         	    //String endTime   = (iE>12?("0"+(iE-12)):rs.getString("end_time").substring(0,2))  +":"+rs.getString("end_time").substring(3,5)+(iE<12?"am":"pm");
          	  String name = UtilMisc.toUpperLowerCase(rs.getString("name"));
          	  int demographic_no = rs.getInt("demographic_no");
                  paramTickler[0]=String.valueOf(demographic_no);
                  paramTickler[1]=strDate; //year+"-"+month+"-"+day;//e.g."2001-02-02";
                  rsTickler = null;
                  rsTickler = apptMainBean.queryResults(paramTickler, "search_tickler");
                  tickler_no = "";
                  tickler_note="";
                  while (rsTickler.next()){
                      tickler_no = rsTickler.getString("tickler_no");
                      tickler_note = rsTickler.getString("message")==null?tickler_note:tickler_note + "\n" + rsTickler.getString("message");
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
            
          	  String reason = rs.getString("reason");
          	  String notes = rs.getString("notes");
          	  String status = (rs.getString("status")).trim();
          	  bFirstTimeRs=true;
			    as.setApptStatus(status);
        %>	    
            <td bgcolor='<%=as.getBgColor()%>' rowspan="<%=iRows%>" <%=view==0?(len==lenLimitedL?"nowrap":""):"nowrap"%> >
            
            <%
			    if (as.getNextStatus() != null && !as.getNextStatus().equals("")) {
            %>
            <a href="providercontrol.jsp?appointment_no=<%=rs.getString("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&status=&statusch=<%=as.getNextStatus()%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=addstatus&dboperation=updateapptstatus&viewall=<%=request.getParameter("viewall")==null?"0":(request.getParameter("viewall"))%>"; title="<bean:message key='<%=as.getTitle()%>' />" >
            <%
				} 
			    if (as.getNextStatus() != null) {
            %>
            <img src="../images/<%=as.getImageName()%>" border="0" height="10" title="<bean:message key='<%=as.getTitle()%>' />"></a>
            <%
                } else {
	                out.print("&nbsp;");
                }
            %>
<%--|--%>
        <%
        			if(demographic_no==0) {
        %>
        		<% if (tickler_no.compareTo("") != 0) {%>	<a href="#" onClick="popupPage(700,1000, '../tickler/ticklerDemoMain.jsp?demoview=0');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a><%} %>
<a href=# onClick ="popupPage(360,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=rs.getString("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=0&displaymode=edit&dboperation=search');return false;" title="<%=iS+":"+(iSm>10?"":"0")+iSm%>-<%=iE+":"+iEm%>
<%=name%>
<bean:message key="provider.appointmentProviderAdminDay.reason"/>: <%=UtilMisc.htmlEscape(reason)%>
<bean:message key="provider.appointmentProviderAdminDay.notes"/>: <%=UtilMisc.htmlEscape(notes)%>" >
            .<%=(view==0&&numAvailProvider!=1)?(name.length()>len?name.substring(0,len).toUpperCase():name.toUpperCase()):name.toUpperCase()%></font></a></td>
        <%
        			} else {
        			  //System.out.println(name+" / " +demographic_no);
				%>	<% if (tickler_no.compareTo("") != 0) {%>	<a href="#" onClick="popupPage(700,1000, '../tickler/ticklerDemoMain.jsp?demoview=<%=demographic_no%>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a><%} %>
<% if (study_no.toString().compareTo("") != 0) {%>	<a href="#" onClick="popupPage(700,1000, '../form/study/forwardstudyname.jsp?study_link=<%=study_link.toString()%>&demographic_no=<%=demographic_no%>&study_no=<%=study_no%>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.study"/>: <%=UtilMisc.htmlEscape(studyDescription.toString())%>"><%="<font color='"+studyColor+"'>"+studySymbol+"</font>"%></a><%} %>

<a href=# onClick ="popupPage(360,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=rs.getString("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search');return false;" title="<%=name%>
reason: <%=UtilMisc.htmlEscape(reason)%>
notes: <%=UtilMisc.htmlEscape(notes)%>" ><%=(view==0)?(name.length()>len?name.substring(0,len):name):name%></a>
<% if(len==lenLimitedL || view!=0 || numAvailProvider==1 ) {%>
<a href=# onClick="popupPage(700,980,'../oscarEncounter/IncomingEncounter.do?providerNo=<%=curUser_no%>&appointmentNo=<%=rs.getString("appointment_no")%>&demographicNo=<%=demographic_no%>&curProviderNo=<%=curProvider_no[nProvider]%>&reason=<%=URLEncoder.encode(reason)%>&userName=<%=URLEncoder.encode( userfirstname+" "+userlastname) %>&curDate=<%=curYear%>-<%=curMonth%>-<%=curDay%>&appointmentDate=<%=year+"-"+month+"-"+day%>&startTime=<%=iS+":"+iSm%>&status=<%=status%>');return false;" title="<bean:message key="global.encounter"/>">
            |<bean:message key="provider.appointmentProviderAdminDay.btnE"/></a><%= bShortcutForm?"<a href=# onClick='popupPage2( \"../form/forwardshortcutname.jsp?formname="+formName+"&demographic_no="+demographic_no+"\")' title='form'>|"+formNameShort+"</a>" : ""%><% if(status.indexOf('B')==-1) { %>
             <% 
                //java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.action.Action.LOCALE_KEY);
                if (vLocale.getCountry().equals("BR")) { %>  
               <a href=# onClick='popupPage(700,1000, "../oscar/billing/procedimentoRealizado/init.do?appId=<%=rs.getString("appointment_no")%>");return false;' title="Faturamento">|FAT|</a>
             <% } else {%>  
	       <!--<a href=# onClick='popupPage(700,1000, "../billing/billingOB.jsp?billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=rs.getString("appointment_no")%>&demographic_name=<%=URLEncoder.encode(name)%>&status=<%=status%>&demographic_no=<%=demographic_no%>&providerview=<%=curProvider_no[nProvider]%>&user_no=<%=curUser_no%>&apptProvider_no=<%=curProvider_no[nProvider]%>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=iS+":"+iSm%>&bNewForm=1");return false;' title="<bean:message key="global.billing"/>">|<bean:message key="provider.appointmentProviderAdminDay.btnB"/>|</a>-->
               <a href=# onClick='popupPage(700,1000, "../billing/CA/BC/billing.do?billRegion=<%=URLEncoder.encode(oscarVariables.getProperty("billregion"))%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=rs.getString("appointment_no")%>&demographic_name=<%=URLEncoder.encode(name)%>&status=<%=status%>&demographic_no=<%=demographic_no%>&providerview=<%=curProvider_no[nProvider]%>&user_no=<%=curUser_no%>&apptProvider_no=<%=curProvider_no[nProvider]%>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=iS+":"+iSm%>&bNewForm=1");return false;' title="<bean:message key="global.billing"/>">|<bean:message key="provider.appointmentProviderAdminDay.btnB"/>|</a>
                 
             <% } %>  
<% } else {%>  
    <a href=# onClick='onUnbilled("../billing/billingDeleteWithoutNo.jsp?status=<%=status%>&appointment_no=<%=rs.getString("appointment_no")%>");return false;' title="<bean:message key="global.billing"/>">|-<bean:message key="provider.appointmentProviderAdminDay.btnB"/>|</a>
<% } %>          
    <% if (vLocale.getCountry().equals("BR")) {%>
    <a href=# onClick="popupPage2('../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail_ptbr');return false;" 
    title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><bean:message key="provider.appointmentProviderAdminDay.btnM"/></a>
    <%}else{%>
    <a href=# onClick="popupPage2('../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail');return false;" 
    title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><bean:message key="provider.appointmentProviderAdminDay.btnM"/></a>
    <%}%>

      <% if (!vLocale.getCountry().equals("BR")) { %>
      <a href=# onClick="popupOscarRx(700,960,'../oscarRx/choosePatient.do?providerNo=<%=curUser_no%>&demographicNo=<%=demographic_no%>')">|<bean:message key="global.rx"/></a>      
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
    			//apptMainBean.closePstmtConn();
				%>        

          </table> <!-- end table for each provider schedule display --> 

         </td></tr>
          <tr><td ALIGN="center" BGCOLOR="<%=bColor?"#bfefff":"silver"%>">
          <b><input type='radio' name='flipview' onClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="<bean:message key="provider.appointmentProviderAdminDay.flipView"/>"  >
          <a href=# onClick="goZoomView('<%=curProvider_no[nProvider]%>','<%=curProviderName[nProvider]%>')" onDblClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>" >
          <!--a href="providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider=<%=curProvider_no[nProvider]%>&curProviderName=<%=curProviderName[nProvider]%>&displaymode=day&dboperation=searchappointmentday" title="zoom view"-->
          <%=curProviderName[nProvider]%></a></b>
          <% if(!userAvail) { %>
          [<bean:message key="provider.appointmentProviderAdminDay.msgNotOnSched"/>]
          <% } %>
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
</html:html>
