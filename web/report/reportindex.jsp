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
/*
 * $RCSfile: AbstractApplication.java,v $ *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
*/ 
--%>
<% 
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String mygroupno = (String) session.getAttribute("groupno");  
%>
<%@ page import="java.util.*, oscar.*, java.sql.*, java.text.*, java.net.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%  if(!reportMainBean.getBDoConfigure()) { %>
<%@ include file="reportMainBeanConn.jsp" %>  
<% } %>
<%--@ include file="reportMainBeanConn.jsp" --%>  

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html locale="true">
<head>
<title><bean:message key="report.reportindex.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.schedule.keyword.focus();
  //document.schedule.keyword.select();
}
function selectprovider(s) {
	self.location.href = "scheduletemplatesetting1.jsp?provider_no="+s.options[s.selectedIndex].value+"&provider_name="+urlencode(s.options[s.selectedIndex].text);
}
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=10,left=15";//360,680
  var popup=window.open(page, "scheduleholiday", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function ogo() {
  var s = document.report.startDate.value.replace('/', '-');
  s = s.replace('/', '-');
  var e = document.report.endDate.value.replace('/', '-');
  e = e.replace('/', '-');
  var u = 'reportnewedblist.jsp?startDate=' + s + '&endDate=' + e;
	popupPage(600,750,u);
}
function go(r) {
//s.options[s.selectedIndex].value
  var s = document.report.provider_no.value ;
  var t = document.report.sdate.value ;
  var u = document.report.asdate.value ;
  var v = document.report.aedate.value ;
  var w = 'reportdaysheet.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ t;
  var x = 'reportdaysheet.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ u + '&edate=' + v;
  if(r=='new') {
    if(confirm("<bean:message key="report.reportindex.msgGoConfirm"/>") ) {
	  popupPage(600,750,w);
	}
  } else {
	popupPage(600,750,x);
  }
}
function ggo(r) {
//s.options[s.selectedIndex].value
  var s = document.report.pprovider_no.value ;
  var t = document.report.ssdate.value ;
  var u = 'reportapptsheet.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ t;
	popupPage(600,750,u);
}
ONCLICK ="popupPage(600,750,'reportpatientchartlist.jsp')" 
function pcgo() {
  var s = document.report.pcprovider_no.value ;
  var u = 'reportpatientchartlist.jsp?provider_no=' + s;
	popupPage(600,750,u);
}
function opcgo() {
  var s = document.report.opcprovider_no.value ;
  var a = document.report.age.value ;
  var u = 'reportpatientchartlistspecial.jsp?provider_no=' + s + '&age=' + a;
	popupPage(600,1010,u);
}
function nsgo() {
  var s = document.report.nsprovider_no.value ;
  var t = document.report.nsdate.value ;
  var u = 'reportnoshowapptlist.jsp?provider_no=' + s +'&sdate='+ t;
	popupPage(600,750,u);
}
//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<%
  GregorianCalendar now = new GregorianCalendar();
  GregorianCalendar cal = (GregorianCalendar) now.clone();
  String today = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ;
%>
<form name='report' >
<table border=0 cellspacing=0 cellpadding=0 width="100%" >
  <tr bgcolor="#486ebd"> 
      <th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message key="report.reportindex.msgTitle"/></font></th>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="95%">
            <tr> 
              <td>&nbsp;</td>
            </tr>
            <tr> 
  <tr>
    <td> 
      <ol>
        <li><a HREF="#" ONCLICK ="ogo()" ><bean:message key="report.reportindex.btnEDBList"/></a> &nbsp;
           <bean:message key="report.reportindex.formFrom"/> <INPUT TYPE="text" NAME="startDate" VALUE="0001/01/01" size='10'> &nbsp; <bean:message key="report.reportindex.formTo"/> 
           <INPUT TYPE="text" NAME="endDate" VALUE="<%=today%>" size='10'>
           <INPUT TYPE="button" NAME="button" VALUE="<bean:message key="report.reportindex.btnCreateReport"/>" onClick="ogo()"></li>
        <li><a HREF="#" ONCLICK ="popupPage(600,750,'reportactivepatientlist.jsp')" ><bean:message key="report.reportindex.btnActivePList"/></a></li>
        <li><bean:message key="report.reportindex.formDaySheet"/>
<select name="provider_no" >
<%
   ResultSet rsgroup = reportMainBean.queryResults("search_group");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
<%
 	 } 
%>
<%
     rsgroup = reportMainBean.queryResults("search_provider");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%> ><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
%>
  <option value="*"  ><bean:message key="report.reportindex.formAllProviders"/></option>
</select>

<br>
*  <a HREF="#" ONCLICK ="go('all')" ><bean:message key="report.reportindex.btnAllAppt"/></a> 
<a HREF="#" onClick ="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../report/reportindex.jsp&year=<%=now.get(Calendar.YEAR)%>&month=<%=now.get(Calendar.MONTH)+1%>&param=<%=URLEncoder.encode("&formdatebox=document.report.asdate.value")%>')"><bean:message key="report.reportindex.formFrom"/></a>
<input type='text' name="asdate" VALUE="<%=today%>"  size=10>
<a HREF="#" onClick ="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../report/reportindex.jsp&year=<%=now.get(Calendar.YEAR)%>&month=<%=now.get(Calendar.MONTH)+1%>&param=<%=URLEncoder.encode("&formdatebox=document.report.aedate.value")%>')"><bean:message key="report.reportindex.formTo"/> </a>
<input type='text' name="aedate" VALUE="<%=today%>" size=10>
<br>
*  <a HREF="#" ONCLICK ="go('new')" title="<bean:message key="report.reportindex.msgNewApptsOld"/>"><bean:message key="report.reportindex.btnPrintDaySheet"/></a>
<select name="sdate" >
<%
  cal.add(cal.DATE, -1) ;
  for(int i=0; i<31; i++) {
    String dateString = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE) ;
%>
  <option value="<%=dateString%>" <%=dateString.equals(today)?"selected":""%> ><%=dateString%></option>
<%
    cal.add(cal.DATE, 1) ;
 	}
%>
</select>
        <%
           // if browser set to pt_BR, do not show following message
           String country = request.getLocale() .getCountry();
           if (!country.equals("BR")) {
        %>
        <li><!--a HREF="#" ONCLICK ="popupPage(600,750,'reportnoshowlist.jsp')" --><bean:message key="report.reportindex.formNoShow"/></a></li>
        <% } %>
        <li><a HREF="#" ONCLICK ="ggo('all')" ><bean:message key="report.reportindex.formBadAppt"/></a>
<select name="pprovider_no" >
<%
   rsgroup = reportMainBean.queryResults("search_group");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
<%
 	 } 
%>
<%
     rsgroup = reportMainBean.queryResults("search_provider");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%> ><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
%>
  <option value="*"  ><bean:message key="report.reportindex.formAllProviders"/></option>
</select>
<select name="ssdate" >
<%
  cal.add(cal.DATE, -31) ;
  for(int i=0; i<31; i++) {
    String dateString = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE) ;
%>
  <option value="<%=dateString%>" <%=dateString.equals(today)?"selected":""%> ><%=dateString%></option>
<%
    cal.add(cal.DATE, 1) ;
 	}
%>
</select>
        <li><a HREF="#"  ONCLICK ="pcgo()"><bean:message key="report.reportindex.btnPatientChartList"/></a>
<select name="pcprovider_no" >
<%
   rsgroup = reportMainBean.queryResults("search_group");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
<%
 	 } 
%>
<%
     rsgroup = reportMainBean.queryResults("search_provider");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%> ><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
%>
</select>
        </li>
        <li><a HREF="#"  ONCLICK ="opcgo()"><bean:message key="report.reportindex.btnOldPatient"/></a>
<select name="opcprovider_no" >
<%
   rsgroup = reportMainBean.queryResults("search_group");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
<%
 	 } 
%>
<%
     rsgroup = reportMainBean.queryResults("search_provider");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%> ><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
%>
</select> <bean:message key="report.reportindex.btnOldPatientAge" /> <input type=text name=age value='65'>
        </li>
        <li><a HREF="#"  ONCLICK ="nsgo()"><bean:message key="report.reportindex.btnNoShowAppointmentList"/></a>
<select name="nsprovider_no" >
<%
   rsgroup = reportMainBean.queryResults("search_group");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
<%
 	 } 
%>
<%
     rsgroup = reportMainBean.queryResults("search_provider");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%> ><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
%>
</select>
<select name="nsdate" >
<%
  cal.add(cal.DATE, -61) ;
  for(int i=0; i<31; i++) {
    String dateString = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE) ;
%>
  <option value="<%=dateString%>" <%=dateString.equals(today)?"selected":""%> ><%=dateString%></option>
<%
    cal.add(cal.DATE, 1) ;
 	}
%>
</select>

        </li>
        <li>
         <a href="../oscarReport/ConsultationReport.jsp" target="_blank"><bean:message key="report.reportindex.btnConsultationReport"/></a>
        </li>
	<%
           // If country = Brazil, do not show the following links:
           if (!country.equals("BR")) {
        %>
        <li>
         <a href="../oscarReport/LabReqReport.jsp" target="_blank"><bean:message key="report.reportindex.btnLaboratoryRequisition"/></a>
        </li>
        <li>
          <a href="../oscarReport/ReportDemographicReport.jsp" target="_blank"><bean:message key="report.reportindex.btnDemographicReportTool"/></a>
        </li>        
        <li>
          <a href=# onClick="popupPage(600,750,'demographicstudyreport.jsp')" ><bean:message key="report.reportindex.btnDemographicStudyList"/></a>
        </li>        
        <% } // end - if country %>
		</ol>
    </td>
            </tr>
            <tr>
              <td> 
                <div align="right">
                  <input type="button" name="Button" value="<bean:message key="report.reportindex.btnCancel"/>" onClick="window.close()">
                </div>
              </td>
            </tr>
          </table>
<p> 

          <p>&nbsp;</p>
          <p>&nbsp;</p>
          <p>&nbsp;</p>
        </body>
</form>        
</html:html>
