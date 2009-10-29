<!-- 
/*
* 
* Copyright (c) 2001-2009. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.net.URLEncoder" %>

<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%	
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%
GregorianCalendar cal = new GregorianCalendar();
int curYear = cal.get(Calendar.YEAR);
int curMonth = (cal.get(Calendar.MONTH)+1);
int curDay = cal.get(Calendar.DAY_OF_MONTH);

int startHour=Integer.parseInt(((String) session.getAttribute("starthour")).trim());
int endHour=Integer.parseInt(((String) session.getAttribute("endhour")).trim());
int everyMin=Integer.parseInt(((String) session.getAttribute("everymin")).trim());

String curUser_no = (String) session.getAttribute("user");
String prov= ((String ) oscarVariables.getProperty("billregion","")).trim().toUpperCase();
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
String mygroupno = (String) session.getAttribute("groupno");

String newticklerwarningwindow=null;
String default_pmm=null;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	newticklerwarningwindow = (String) session.getAttribute("newticklerwarningwindow");
	default_pmm = (String)session.getAttribute("default_pmm");
}   
%>

<table  class="topnav" id="firstTable">
	<tr>
		<td>
			<ul id="navlist">
				<li>
    				<a href="<html:rewrite page="/provider/providercontrol.jsp"/>?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=0&displaymode=day&dboperation=searchappointmentday" TITLE="<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>"><u>T</u>oday</a>
				</li>
				<li>
					<a href="<html:rewrite page="/provider/providercontrol.jsp"/>?year=<%=curYear%>&month=<%=curMonth%>&day=1&view=0&displaymode=month&dboperation=searchappointmentmonth" TITLE="<bean:message key="provider.appointmentProviderAdminDay.viewMonthSched"/>">Mo<u>n</u>th</a>
				</li>
				<li>
					<a href="#" ONCLICK ="popupPage2('http://resource.oscarmcmaster.org/oscarResource/');return false;" title="<bean:message key="global.resources"/>">R<u>e</u>sources</a>
				</li>
				<li>
					<a href="<html:rewrite page="/PMmodule/ClientSearch2.do"/>" title="Search for patient records"><u>S</u>earch</a>
				</li>
				<li>
					<a HREF="#" ONCLICK ="popupPage2('<html:rewrite page="/report/reportindex.jsp"/>','reportPage');return false;" title="<bean:message key="global.genReport"/>"><u>R</u>eport</a>
				</li>
				<oscar:oscarPropertiesCheck property="NOT_FOR_CAISI" value="no" defaultVal="true">
				<li>					
					<a HREF="#" ONCLICK ="popupPage2('<html:rewrite page="/billing"/>/CA/<%=prov%>/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;" title="<bean:message key="global.genBillReport"/>"><u>B</u>illing</a>
				</li>
				</oscar:oscarPropertiesCheck>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment.doctorLink" rights="r">
				<oscar:oscarPropertiesCheck property="NOT_FOR_CAISI" value="no" defaultVal="true">
				<li>
					<a HREF="#" ONCLICK ="popupPage2('<html:rewrite page="/oscarMDS/Index.jsp"/>?providerNo=<%=curUser_no%>', 'Lab');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>">
						<span id="oscar_new_lab"><u>L</u>ab</span>
					</a>
       				<oscar:newUnclaimedLab>
       					<a class="tabalert" HREF="#" ONCLICK="popupPage2('<html:rewrite page="/oscarMDS/Index.jsp"/>?providerNo=0&searchProviderNo=0&status=N&lname=&fname=&hnum=&pageNum=1&startIndex=0', 'Lab');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'>*</a>
       				</oscar:newUnclaimedLab>					       
				</li>
				</oscar:oscarPropertiesCheck>
</security:oscarSec>
				<li>
					<a HREF="#" ONCLICK ="popupOscarRx(600,900,'<html:rewrite page="/oscarMessenger/DisplayMessages.do?"/>providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>');return false;" title="<bean:message key="global.messenger"/>">
						<span id="oscar_new_msg"><u>M</u>sg</span>
					</a>
				</li>
				<li>
					<a HREF="#" ONCLICK ="popupOscarRx(625,900,'<html:rewrite page="/oscarEncounter/IncomingConsultation.do"/>?providerNo=<%=curUser_no %>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.viewConReq"/>">
					<span id="oscar_aged_consults">C<u>o</u>n</span></a>
				</li>
				<li>    
					<a href="#" onClick ="popupPage(400,680,'<html:rewrite page="/provider/providerpreference.jsp"/>?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>"><u>P</u>ref</a>
				</li>				
				<li>
					<a HREF="#" onclick="popup('700', '1000', '<html:rewrite page="/dms/documentReport.jsp"/>?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>', 'edocView');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.viewEdoc"/>">e<u>D</u>oc</a>
				</li>
				<li>
					<a HREF="#" ONCLICK ="popupPage2('<html:rewrite page="/Tickler.do"/>?filter.demographic_no=','<bean:message key="global.tickler"/>');return false;" TITLE='<bean:message key="global.tickler"/>'+'+'>
					<span id="oscar_new_tickler">T<u>i</u>ckler</span></a>
				</li>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc" rights="r">
				<li>
					<a HREF="#" ONCLICK ="popupOscarRx(700,687,'<html:rewrite page="/admin/admin.jsp"/>');return false;"><u>A</u>dmin</a>
				</li>
</security:oscarSec>
				
				<li>
					<a href='<html:rewrite page="/PMmodule/ProviderInfo.do"/>'>Program</a>
				</li>
			</ul>  
			
		</td>
		<td align="right" valign="bottom">
 			<a href="javascript: function myFunction() {return false; }" onClick="popup(700,1000,'<html:rewrite page="/scratch/index.jsp"/>','scratch')"><span id="oscar_scratch"></span></a>
  			&nbsp;&nbsp;   
			<a href=# onClick ="popupPage(600,750,'http://resource.oscarmcmaster.org/oscarResource/');return false;"><u>H</u>elp</a>
			&nbsp;
			<a href='<html:rewrite page="/PMmodule/ProviderInfo.do"/>'>Home</a> &nbsp;
			<a href="<html:rewrite page="/logout.jsp"/>">Lo<u>g</u>out</a>
		</td>
	</tr>
</table>