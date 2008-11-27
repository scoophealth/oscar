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
  <%@ page errorPage="../errorpage.jsp" %>
  <%@ page import="java.util.*" %>
  <%@ page import="java.sql.*" %>
  <%@ page import="oscar.login.*,oscar.util.SqlUtils, oscar.oscarDB.*, oscar.MyDateFormat" %>
  <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
  <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
if(session.getAttribute("user") == null )
	response.sendRedirect("../logout.jsp");
String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
String curUser_no = (String)session.getAttribute("user");
%>

<%
  String   tdTitleColor = "#CCCC99";
  String   tdSubtitleColor = "#CCFF99";
  String   tdInterlColor = "white";
  String   startDate    = request.getParameter("startDate");
  String   endDate      = request.getParameter("endDate");
  Vector   vecProvider  = new Vector();
  Properties prop = null;
  String   providerName   = "";
  String sql = "";
//provider name list, date, action, create button
%>

<%
  DBPreparedHandler dbObj = new DBPreparedHandler();
  Properties propName = new   Properties();
  // select provider list
  sql   = "select * from provider p order by p.first_name, p.last_name ";
  ResultSet rs = dbObj.queryResults(sql);

  while (rs.next()) {
    propName.setProperty(dbObj.getString(rs,"provider_no"), dbObj.getString(rs,"first_name") + " " + dbObj.getString(rs,"last_name"));
    prop = new Properties();

    prop.setProperty("providerNo", dbObj.getString(rs,"provider_no"));
    prop.setProperty("name", dbObj.getString(rs,"first_name") + " " + dbObj.getString(rs,"last_name"));
    vecProvider.add(prop);
  }
%>
  <html:html locale="true">
    <head>
      <title>
        Log Report
      </title>
      <link rel="stylesheet" href="../receptionist/receptionistapptstyle.css">
      <!-- calendar stylesheet -->
      <link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />
      <!-- main calendar program -->
      <script type="text/javascript" src="../share/calendar/calendar.js"></script>
      <!-- language for the calendar -->
      <script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
      <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
      <script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
      <script language="JavaScript">

                <!--
function setfocus() {
	this.focus();
	//  document.titlesearch.keyword.select();
}
function onSub() {
//  if( document.myform.codeType.value=="" || document.myform.startDate.value=="" || document.myform.endDate.value==""
//    || (document.myform.providerNoDoctor.value=="" && document.myform.providerNoResident.value==""
//     && document.myform.providerNoNP.value=="" && document.myform.providerNoSW.value=="") ) {
//    alert("Please select the codeType/period/provider item(s) from the drop down list before query.");
//    return false ;
//  } else {
    return true;
//  }
}

//-->

      </script>
    </head>
    <body bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr bgcolor="#486ebd">
          <th align="CENTER" width="90%">
            <font face="Helvetica" color="#FFFFFF">
              Log Admin Report
            </font>
          </th>
        </tr>
      </table>
        <form name="myform" action="logReport.jsp" method="POST" onSubmit="return ( onSub());">
      <table width="100%" border="0" bgcolor="ivory" cellspacing="1" cellpadding="1">
          <tr bgcolor="lightsteelblue">
            <td>
              Provider:
              <select name="providerNo">
                  <option value="*">All</option>
<%
                for (int i = 0; i < vecProvider.size(); i++) {
                    String prov = ((Properties)vecProvider.get(i)).getProperty("providerNo", "");
                    String selected = request.getParameter("providerNo");
%>
                  <option value="<%=prov %>"
                    <% if ((selected != null) && (selected.equals(prov))) { %> selected<% } %>>
                  <%= ((Properties)vecProvider.get(i)).getProperty("name", "") %>
                  </option>
<%
                }
%>
              </select>
            </td>
            <td nowrap>
              start
              <input type="text" name="startDate" id="startDate" value="<%=startDate!=null?startDate:""%>" size="10" readonly>
              <img src="../images/cal.gif" id="startDate_cal">
              end
              <input type="text" name="endDate" id="endDate" value="<%=endDate!=null?endDate:""%>" size="10" readonly>
              <img src="../images/cal.gif" id="endDate_cal">
            </td>
            <td nowrap>

              <select name="content">
                <option value="admin">Admin</option>
                <option value="login">Log in</option>
              </select>
            </td>
            <td>
              <input type="submit" name="submit" value="Go">
            </td>
          </tr>
      </table>
        </form>
<%
	out.flush();
	//String startDate = "";
	//String endDate = "";
	boolean bAll = false;
	Vector<Properties> vec = new Vector<Properties>();
	String providerNo = "";
	if (request.getParameter("submit") != null) {
	  providerNo = request.getParameter("providerNo");
	  String action = request.getParameter("submit");
	  String content = request.getParameter("content");
	  if(content.equals("login")) content = "login";
	  if(content.equals("admin")) content = "%";
	  
	  String sDate = request.getParameter("startDate");
	  String eDate = request.getParameter("endDate");
	  String strDbType = oscar.OscarProperties.getInstance().getProperty("db_type").trim();
	  if("oracle".equalsIgnoreCase(strDbType)){
		if("".equals(sDate) || sDate==null)  sDate = "1900-01-01";
		if("".equals(eDate) || eDate==null)  eDate = "2999-01-01";
	  }

	  DBPreparedHandlerParam[] params = new DBPreparedHandlerParam[2];
	  params[0]= new DBPreparedHandlerParam(MyDateFormat.getSysDateEX(eDate, 1));
	  params[1]= new DBPreparedHandlerParam(MyDateFormat.getSysDate(sDate));

      sql = "select * from log force index (datetime) where provider_no='" + providerNo + "' and dateTime <= ?";
      sql += " and dateTime >= ? and content like '" + content + "' order by dateTime desc ";

      if("*".equals(providerNo)) {
		  bAll = true;
	      sql = "select * from log force index (datetime) where dateTime <= ?";
	      sql += " and dateTime >= ? and content like '" + content + "' order by dateTime desc ";
      }
//      System.out.println("sql:" + sql);
      rs = dbObj.queryResults(sql, params);
      while (rs.next()) {
        prop = new Properties();
        prop.setProperty("dateTime", "" + rs.getTimestamp("dateTime"));
        prop.setProperty("action", dbObj.getString(rs,"action"));
        prop.setProperty("content", dbObj.getString(rs,"content"));
        prop.setProperty("contentId", dbObj.getString(rs,"contentId"));
        prop.setProperty("ip", dbObj.getString(rs,"ip"));
        prop.setProperty("provider_no", dbObj.getString(rs,"provider_no"));
        vec.add(prop);
      }

	  //startDate = sDate;
	  //endDate = eDate;
	}
%>
        <table border="0" cellspacing="0" cellpadding="0" width="100%">
          <tr bgcolor="<%="#669999"%>">
            <th align="left">
              <font face="Helvetica" color="white">
                <%=propName.getProperty(providerNo,"")%> - Log Report
              </font>
            </th>
            <th width="10%" nowrap>
              <input type="button" name="Button" value="Print" onClick="window.print()">
              <input type="button" name="Button" value=" Exit " onClick="window.close()">
            </th>
          </tr>
        </table>
        <table border="0" cellspacing="0" cellpadding="0" width="100%">
          <tr>
            <td>
              Period: (
              <%= startDate==null?"":startDate %>
              ~
              <%= endDate==null?"":endDate %>
              )
            </td>
          </tr>
        </table>
        <table width="100%" border="1" bgcolor="#ffffff" cellspacing="0" cellpadding="0">
          <tr bgcolor="<%=tdTitleColor%>">
            <TH width="30%">
              Time
            </TH>
            <TH width="10%">
              Action
            </TH>
            <TH width="10%">
              Content
            </TH>
            <TH width="20%">
              Keyword
            </TH>
            <TH >
              IP
            </TH>
<% if(bAll) { %>
            <TH>Provider</TH>
<% } %>
          </tr>
<%
String catName = "";
String color = "";
int codeNum = 0;
int vecNum = 0;
for (int i = 0; i < vec.size(); i++) {
	prop = (Properties) vec.get(i);
    color = i%2==0?tdInterlColor:"white";
%>
		<tr bgcolor="<%=color %>" align="center">
          <td><%=prop.getProperty("dateTime")%>&nbsp;</td>
          <td><%=prop.getProperty("action")%>&nbsp;</td>
          <td><%=prop.getProperty("content")%>&nbsp;</td>
          <td><%=prop.getProperty("contentId")%>&nbsp;</td>
          <td><%=prop.getProperty("ip")%>&nbsp;</td>
<% if(bAll) { %>
          <td><%=propName.getProperty(prop.getProperty("provider_no"), "")%></td>
<% } %>
        </tr>
<% } %>
   <script type="text/javascript">
Calendar.setup({ inputField : "startDate", ifFormat : "%Y/%m/%d", showsTime :false, button : "startDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "endDate", ifFormat : "%Y/%m/%d", showsTime :false, button : "endDate_cal", singleClick : true, step : 1 });
      </script>
    </body>
  </html:html>
