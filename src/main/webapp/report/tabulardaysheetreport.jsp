<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
  if (session.getAttribute("user") == null){
  	response.sendRedirect("../logout.jsp");
  }
  String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("a.start_time") ;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<% 
  String [][] dbQueries=new String[][] { 
	{"search_daysheetall", "select a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, p.last_name, p.first_name, d.last_name,d.first_name,d.chart_no, d.phone, d.date_of_birth, d.month_of_birth, d.year_of_birth, d.hin from appointment a,demographic d,provider p, mygroup m where a.appointment_date=? and  m.mygroup_no=? and a.status != 'C' and a.demographic_no=d.demographic_no and a.provider_no=p.provider_no AND p.provider_no=m.provider_no order by p.provider_no, a.appointment_date, "+orderby }, 
	{"search_daysheetsingleall", "select a.appointment_date, a.provider_no,a.start_time,a.end_time, a.reason,p.last_name,p.first_name,d.last_name,d.first_name,d.chart_no, d.phone, d.date_of_birth, d.month_of_birth, d.year_of_birth, d.hin from appointment a,demographic d,provider p where a.appointment_date=? and a.provider_no=? and a.status != 'C' and a.demographic_no=d.demographic_no and a.provider_no=p.provider_no order by a.appointment_date,"+orderby },
  };
 	String[][] responseTargets=new String[][] {  };
  	daySheetBean.doConfigure(dbQueries,responseTargets);
   	GregorianCalendar now = new GregorianCalendar();
  	String createtime = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) +" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) ;
	String date = request.getParameter("sdate");
  	String provider_no = request.getParameter("provider_no");
  	boolean bodd = false;
  	ResultSet rsdemo = null;
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR - <bean:message key="report.tabulardaysheetreport.title"/>=</title>
<link rel="stylesheet" href="../media/css/oscar.css">
<link rel="stylesheet" href="../media/css/reporting.css">
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<table width="100%" bgcolor="#D3D3D3" border="0">
	<tr>
		<td height="40" width="25"></td>
		<td width="90%" align="left">
		<p><font color="#4D4D4D"><b><font size="4">oscar<font
			size="3"><bean:message key="report.tabulardaysheetreport.msgTitle"/> (<%=createtime%>)</font></font></b></font></p>
		</td>
		<td><input type="button" name="Button" value="<bean:message key="report.tabulardaysheetreport.btnPrint"/>" onClick="window.print()">
                    <input type="button" name="Button" value=" <bean:message key="report.tabulardaysheetreport.btnExit"/> " onClick="window.close()">
                </td>
    
	</tr>
</table>
<%
	boolean bFistL = true;
	String strTemp = "";
	if ((5 >= provider_no.length()) || (5 < provider_no.length()) && !(provider_no.substring(0, 5).compareTo("_grp_") == 0))
	{
		rsdemo = daySheetBean.queryResults(new String[] {date, provider_no}, "search_daysheetsingleall");
	}
	else
	{
		rsdemo = daySheetBean.queryResults(new String[] {date, provider_no.substring(5, provider_no.length())}, "search_daysheetall");
    }
	boolean veryFirst = true;
	int hour = 8;
	int min = 0;
	String time = ((10 <= hour) ? Integer.toString(hour) : "0" + Integer.toString(hour)) + ":" + ((10 <= min) ?
		Integer.toString(min) : "0" + Integer.toString(min));
	String appTime;
	String lastwritten = "";
	while (rsdemo.next())
	{
		if(!strTemp.equals(rsdemo.getString("a.provider_no")) )
		{
			strTemp = rsdemo.getString("a.provider_no") ;
			bFistL = true;
			time = ((10 <= hour) ? Integer.toString(hour) : "0" + Integer.toString(hour)) + ":" + ((10 <= min) ?
				Integer.toString(min) : "0" + Integer.toString(min));
			if (!(veryFirst))
			{
				while (18 > hour && 60 > min)
				{
					time = ((10 <= hour) ? Integer.toString(hour) : "0" + Integer.toString(hour)) + ":" + ((10 <= min) ?
						Integer.toString(min) : "0" + Integer.toString(min));
%>
<tr bgcolor="<%=bodd?"#F6F6F6":"#FFFFFF"%>">
	<td class="items"><%=time%></td>
	<td class="items">&nbsp;</td>
	<td class="items">&nbsp;</td>
	<td class="items">&nbsp;</td>
	<td class="items">&nbsp;</td>
	<td class="items">&nbsp;</td>
	<td class="items">&nbsp;</td>
	<td class="items">&nbsp;</td>
	<td class="items">&nbsp;</td>
	<td class="items">&nbsp;</td>
</tr>
<%
					bodd = !(bodd);
					if (45 == min)
					{
						min = 0;
						hour += 1;
					}
					else
					{
						min += 15;
					}
				}
				hour = 8;
				min = 0;
				time = ((10 <= hour) ? Integer.toString(hour) : "0" + Integer.toString(hour)) + ":" + ((10 <= min) ? Integer.toString(min) : "0" + Integer.toString(min));
			}
			else
			{
				veryFirst = false;
			}
%>
</table>
<p>
<%
			hour = 8;
			min = 0;
		}
		if(bFistL)
		{
			bFistL = bodd = false;
%>

<table width="100%" border="0" cellspacing="0" cellpadding="1"
	class="smallerTable">
	<tr>
		<td><font size=4><b><%=providerBean.getProperty(rsdemo.getString("a.provider_no")) + "</b>  (" + date + ")"%><font></td>
		<td align="right"></td>
	</tr>
</table>
<table width="100%" border="0" bgcolor="#ffffff" cellspacing="0"
	cellpadding="0" class="smallerTable">
	<tr>
		<td class="items"><b><bean:message key="report.tabulardaysheetreport.msgTime"/></b></td>
		<td class="items"><b><bean:message key="report.tabulardaysheetreport.msgChart"/></b></td>
		<td class="items"><b><bean:message key="report.tabulardaysheetreport.msgName"/></b></td>
		<td class="items" align="center" width="79"><b><bean:message key="report.tabulardaysheetreport.msgDoB"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgPHN"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgFee1"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgDiag1"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgDiag2"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgDiag3"/></b></td>
		<td class="items"><b><bean:message key="report.tabulardaysheetreport.msgDescription"/></b></td>
	</tr>
	<%
    	}
		appTime = rsdemo.getString("a.start_time");
		String endTime = rsdemo.getString("a.end_time");
		bodd = !(bodd);
		if (!(appTime.equals(time)))
		{
			while ((18 != hour) && !(appTime.equals(time + ":00")))
			{
				if (!(time.equalsIgnoreCase(lastwritten)))
				{
%>
	<tr bgcolor="<%=bodd?"#F6F6F6":"#FFFFFF"%>">
		<td class="items"><%=time%></td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
	</tr>
	<%
				bodd = !(bodd);
				}
				if (45 == min)
				{
					min = 0;
					hour += 1;
				}
				else
				{
					min += 15;
				}
				time = ((10 <= hour) ? Integer.toString(hour) : "0" + Integer.toString(hour)) + ":" + ((10 <= min) ? Integer.toString(min) : "0" + Integer.toString(min));
			}
		}
		int t =	Integer.parseInt(time.split(":")[0]) * 60 + Integer.parseInt(time.split(":")[1]);
		int st =  Integer.parseInt(appTime.split(":")[0]) * 120 + Integer.parseInt(appTime.split(":")[1]) * 60 + Integer.parseInt(appTime.split(":")[2]);
		if (t <= st)
		{
%>
	<tr bgcolor="<%=((bodd)?"#F6F6F6":"#FFFFFF")%>">
		<td class="items"><%=time%></td>
		<td class="items"><%=rsdemo.getString("d.chart_no")%></td>
		<td class="items"><%=Misc.toUpperLowerCase(rsdemo.getString("d.last_name")) + ", " + Misc.toUpperLowerCase(rsdemo.getString("d.first_name")) + " Ph:" + rsdemo.getString("d.phone")%></td>
		<td class="items"><%=rsdemo.getString("d.date_of_birth") + "-" + rsdemo.getString("d.month_of_birth") + "-" + rsdemo.getString("d.year_of_birth")%></td>
		<td class="items"><%=rsdemo.getString("d.hin")%></td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items">&nbsp;</td>
		<td class="items"><%=rsdemo.getString("a.reason")%>&nbsp;</td>
		<%
			bodd = !(bodd);
			String[] ets = endTime.split(":");
			int m = Integer.parseInt(ets[1]) + 1;
			if (t <= st)
			{
				lastwritten = time;
			}
			else if (60 == m)
			{
				hour += 1;
				min = 0;
				time = ((10 <= hour) ? Integer.toString(hour) : "0" + Integer.toString(hour)) + ":" + ((10 <= min) ? Integer.toString(min) : "0" + Integer.toString(min));
			}
			else
			{
				hour = (m <= min) ? hour + 1 : hour;
				min = m;
				time = ets[0] + ":" + ((10 <= m) ? Integer.toString(m) : "0" + Integer.toString(m));
			}
		}
	}

	while (18 > hour)
	{
		if (veryFirst)
		{
			veryFirst = false;
%>
		<table width="100%" border="0" bgcolor="#ffffff" cellspacing="0"
			cellpadding="0">
			<tr>
				<td><b><bean:message key="report.tabulardaysheetreport.msgTime"/></b></td>
				<td><b><bean:message key="report.tabulardaysheetreport.msgChart"/></b></td>
				<td><b><bean:message key="report.tabulardaysheetreport.msgName"/></b></td>
				<td align="center" width="79"><b><bean:message key="report.tabulardaysheetreport.msgDoB"/></b></td>
				<td align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgPHN"/></b></td>
				<td align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgFee1"/></b></td>
				<td align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgDiag1"/></b></td>
				<td align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgDiag2"/></b></td>
				<td align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgDiag3"/></b></td>
				<td><b><bean:message key="report.tabulardaysheetreport.msgDescription"/></b></td>
			</tr>
			<%
		}
		time = ((10 <= hour) ? Integer.toString(hour) : "0" + Integer.toString(hour)) + ":" + ((10 <= min) ? Integer.toString(min) : "0" + Integer.toString(min));
		if (!(time.equalsIgnoreCase(lastwritten)))
		{
%>
			<tr bgcolor="<%=bodd?"#F6F6F6":"#FFFFFF"%>">
				<td class="items"><%=time%></td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
			</tr>
			<%
  		bodd = !(bodd);
		}
		if (45 == min)
		{
			min = 0;
			hour += 1;
		}
		else
		{
			min += 15;
		}
		time = ((10 <= hour) ? Integer.toString(hour) : "0" + Integer.toString(hour)) + ":" + ((10 <= min) ? Integer.toString(min) : "0" + Integer.toString(min));
	}
%>
		</table>
</body>
</html>
