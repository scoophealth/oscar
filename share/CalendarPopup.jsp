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
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<%
  String urlfrom = request.getParameter("urlfrom")==null?"":request.getParameter("urlfrom") ;
  //to prepare calendar display  
  int year = Integer.parseInt(request.getParameter("year"));
  int month = Integer.parseInt(request.getParameter("month"));
  //int day = now.get(Calendar.DATE);
  int delta = request.getParameter("delta")==null?0:Integer.parseInt(request.getParameter("delta")); //add or minus month
  GregorianCalendar now = new GregorianCalendar(year,month-1,1);

 	now.add(now.MONTH, delta);
  year = now.get(Calendar.YEAR);
  month = now.get(Calendar.MONTH)+1;
  
  //the date of today
  GregorianCalendar cal = new GregorianCalendar();
  int todayDate = cal.get(Calendar.DATE);
  boolean bTodayDate = false;
%>

<html>
<head>
<title>CALENDAR</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
}
function typeInDate(year1,month1,day1) {
  self.close();
  opener.location.href="<%=urlfrom%>"+"?year=" + year1 + "&month=" + month1 + "&day=" + day1 +"&view=0&displaymode=day&dboperation=searchappointmentday" ;
}
//-->
</script>
</head>
<body bgcolor="ivory" onLoad="setfocus()"  leftmargin="0" rightmargin="0">
<%
  String[] arrayMonth = new String[] { "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec" };
  now.add(now.DATE, -1); 
  DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
  int [][] dateGrid = aDate.getMonthDateGrid();
%>
      <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
  			<tr>
        	  <td BGCOLOR="#CCCCFF" width="50%" align="center" >
			  <a href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&delta=-1"> &nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="View Last Month" vspace="2"> last month&nbsp;&nbsp; 
              </a>  <b><span CLASS=title><%=year%>-<%=month%></span></b>
        <a href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&delta=1"> &nbsp;&nbsp;next month <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="View Next Month" vspace="2">&nbsp;&nbsp;</a></td>
  			</TR>
		</table>
    <table width="100%" border="0" cellspacing="0" cellpadding="2" >
       <tr align="center" bgcolor="#FFFFFF"> 
       <th>
<%
  for(int i=0; i<12; i++) {
%>
<a href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=i+1%>"><font SIZE="2" <%=(i+1)==month?"color='blue'":""%>><%=arrayMonth[i]%></a>
<% } %>
       </th>
    </tr></table>
          <table width="100%" border="1" cellspacing="0" cellpadding="2"  bgcolor="silver" >
            <tr bgcolor="#CCCCFF" align="center"> 
              <th width="14%"><font color="red">Sun</font></td>
              <th width="14%">Mon</font></td>
              <th width="14%">Tue</font></td>
              <th width="14%">Wed</font></td>
              <th width="14%">Thu</font></td>
              <th width="14%">Fri</td>
              <th width="14%"><font color="green">Sat</font></td>
            </tr>
            
            <%
              for (int i=0; i<dateGrid.length; i++) {
                out.println("<tr>");
                for (int j=0; j<7; j++) {
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    now.add(now.DATE, 1);
                    if(todayDate == now.get(Calendar.DATE)) bTodayDate = true;
                    else bTodayDate = false;
            %>
                      <td align="center" bgcolor='<%=bTodayDate?"gold":"#EEEEFF"%>'>
                      <a href="#" onClick="typeInDate(<%=year%>,<%=month%>,<%= dateGrid[i][j] %>)">&nbsp;&nbsp; <%= dateGrid[i][j] %> &nbsp;&nbsp; </a>
                      </td>
            <%    }  
                }
                out.println("</tr>");
              }
            %>
            
          </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr> 
              <td>&nbsp;</td>
            </tr>
            <tr> 
              <td bgcolor="#CCCCFF"> 
                <div align="center"> 
                  <input type="button" name="Cancel" value=" Exit " onClick="window.close()">
                </div>
              </td>
            </tr>
          </table>

</body>
</html>