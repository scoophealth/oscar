<%--
  /*
    input: urlfrom and param
	output: urlfrom + "?year-day" + param
	or
	output: opener.param.substring("&formdatebox=".length()) = year1 + "-" + month1 + "-" + day1
  */
--%>
<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<%
String urlfrom = request.getParameter("urlfrom")==null?"":request.getParameter("urlfrom") ;
String param = request.getParameter("param")==null?"":request.getParameter("param") ;
//to prepare calendar display  
int year = Integer.parseInt(request.getParameter("year"));
int month = Integer.parseInt(request.getParameter("month"));
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
<%
    if (param.startsWith("&formdatebox=")) {
%>
  opener.<%=param.substring("&formdatebox=".length())%> = year1 + "-" + month1 + "-" + day1; 
<%
    } else {
%>  
  opener.location.href="<%=urlfrom%>"+"?year=" + year1 + "&month=" + month1 + "&day=" + day1 +"<%=param%>"; 
<%  }  %>  
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
<tr BGCOLOR="#CCCCFF" >
	<td width="5%" align="center" nowrap>
	<a href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&param=<%=URLEncoder.encode(param)%>&delta=-12">
	<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="Last Year" vspace="2">
	<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="Last Year" vspace="2">
	</a></td>
	<td align="center" nowrap>
	<a href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&param=<%=URLEncoder.encode(param)%>&delta=-1"> 
	<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="View Last Month" vspace="2"> last month
	</a>
	<b><span CLASS=title><%=year%>-<%=month%></span></b>
	<a href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&param=<%=URLEncoder.encode(param)%>&delta=1"> 
	next month <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="View Next Month" vspace="2"></a></td>
	<td align='right'>
	<a href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=month%>&param=<%=URLEncoder.encode(param)%>&delta=12">
	<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="Next Year" vspace="2">
	<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="Next Year" vspace="2"></a></td>
</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="2" >
<tr align="center" bgcolor="#FFFFFF"> 
	<th>
<%
  for(int i=0; i<12; i++) {
%>
<a href="CalendarPopup.jsp?urlfrom=<%=urlfrom%>&year=<%=year%>&month=<%=i+1%>&param=<%=URLEncoder.encode(param)%>"><font SIZE="2" <%=(i+1)==month?"color='red'":"color='blue'"%>><%=arrayMonth[i]%></a>
<% } %>
	</th>
</tr>
</table>

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
<%
		}  
	}
	out.println("</tr>");
}
%>

</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr> 
	<td bgcolor="#CCCCFF" align="center"> 
	<input type="button" name="Cancel" value=" Exit " onClick="window.close()">
	</td>
</tr>
</table>

</body>
</html>