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

<%--
/*
 * $RCSfile: reportindex.jsp,v $ *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
*/ 
--%>
<% 
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String mygroupno = (String) session.getAttribute("groupno");  
%>
<%@ page import="java.util.*, oscar.*, java.sql.*, java.text.*, java.lang.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%  if(!reportMainBean.getBDoConfigure()) { %>
<%@ include file="reportMainBeanConn.jsp" %>  
<% } %>
<%--@ include file="reportMainBeanConn.jsp" --%>  

<html>
<head>
<title>REPORT SETTING</title>
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
function go(r) {
//s.options[s.selectedIndex].value
  var s = document.report.provider_no.value ;
  var t = document.report.sdate.value ;
  var u = 'reportdaysheet.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ t;
  if(r=='new') {
    if(confirm("Are you sure you want to see only new appts? (The new appts status would be changed to 'old'.)") ) {
	  popupPage(600,750,u);
	}
  } else {
	popupPage(600,750,u);
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
<form name='report' >
<table border=0 cellspacing=0 cellpadding=0 width="100%" >
  <tr bgcolor="#486ebd"> 
      <th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">REPORT LIST</font></th>
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
        <!--li><a HREF="#" ONCLICK ="popupPage(600,750,'reportedblist.jsp')" >EDB List</a></li-->
        <li><a HREF="#" ONCLICK ="popupPage(600,750,'reportactivepatientlist.jsp')" >Active Patient List</a></li>
        <li>Day Sheet
<select name="provider_no" >
<%
   ResultSet rsgroup = reportMainBean.queryResults("mygroup_no", "search_group");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
<%
 	 } 
%>
<%
     rsgroup = reportMainBean.queryResults("last_name", "search_provider");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%> ><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
%>
  <option value="*"  >All Providers</option>
</select>
<select name="sdate" >
<%
  GregorianCalendar now = new GregorianCalendar();
  GregorianCalendar cal = (GregorianCalendar) now.clone();
  String today = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ;

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
<br>
*  <a HREF="#" ONCLICK ="go('all')" >All appointments</a><br>
*  <a HREF="#" ONCLICK ="go('new')" title="New appts will be old after this view, !">Print Day Sheet or only new appointments</a> 
        <li><!--a HREF="#" ONCLICK ="popupPage(600,750,'reportnoshowlist.jsp')" -->No Show List & Letters</a></li>
        <li><a HREF="#" ONCLICK ="ggo('all')" >Bad Appt Sheet</a>
<select name="pprovider_no" >
<%
   rsgroup = reportMainBean.queryResults("mygroup_no", "search_group");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
<%
 	 } 
%>
<%
     rsgroup = reportMainBean.queryResults("last_name", "search_provider");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%> ><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
%>
  <option value="*"  >All Providers</option>
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
        <li><a HREF="#"  ONCLICK ="pcgo()">Patient Chart List</a>
<select name="pcprovider_no" >
<%
   rsgroup = reportMainBean.queryResults("mygroup_no", "search_group");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
<%
 	 } 
%>
<%
     rsgroup = reportMainBean.queryResults("last_name", "search_provider");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%> ><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
%>
</select>
        </li>
        <li><a HREF="#"  ONCLICK ="nsgo()">No Show Appointment List</a>
<select name="nsprovider_no" >
<%
   rsgroup = reportMainBean.queryResults("mygroup_no", "search_group");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> ><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
<%
 	 } 
%>
<%
     rsgroup = reportMainBean.queryResults("last_name", "search_provider");
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
      </ol>
    </td>
            </tr>
            <tr>
              <td> 
                <div align="right">
                  <input type="button" name="Button" value="Cancel" onClick="window.close()">
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
</html>
