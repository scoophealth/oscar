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
<meta http-equiv="refresh" content="180;">

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





</script>
<!-- end of keycode block -->
</html:html>
<%
long finLoad = System.currentTimeMillis();
System.out.println("TOTAL LOAD TIME FOR DAY SHEET:" + (finLoad-loadPage)*.001);
%>
