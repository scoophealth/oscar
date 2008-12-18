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
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
  String user_no="";
user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="errorpage.jsp"%>
<%@ include file="../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbBilling.jsp"%>
<% 	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
  String[] yearArray = new String[5];
  String thisyear = request.getParameter("year")==null?String.valueOf(curYear):request.getParameter("year").compareTo("")==0?String.valueOf(curYear):request.getParameter("year");
if (thisyear.compareTo("")==0) thisyear = String.valueOf(curYear);
String yearColor = "";  
yearArray[0] = String.valueOf(curYear);
    yearArray[1] = String.valueOf(curYear-1);
      yearArray[2] = String.valueOf(curYear-2);
        yearArray[3] = String.valueOf(curYear-3);
          yearArray[4] = String.valueOf(curYear-4);
          
if (thisyear.compareTo(yearArray[0])==0) yearColor="#B1D3EF";

if (thisyear.compareTo(yearArray[1])==0) yearColor="#BBBBBB";
if (thisyear.compareTo(yearArray[2])==0) yearColor="#CCCCCC";
if (thisyear.compareTo(yearArray[3])==0) yearColor="#DDDDDD";
if (thisyear.compareTo(yearArray[4])==0) yearColor="#EEEEEE";
          
          
  String monthCode = "";
  if (curMonth == 1) monthCode = "A";
    if (curMonth == 2) monthCode = "B";
      if (curMonth == 3) monthCode = "C";
        if (curMonth == 4) monthCode = "D";
          if (curMonth == 5) monthCode = "E";
            if (curMonth == 6) monthCode = "F";
              if (curMonth == 7) monthCode = "G";
                if (curMonth == 8) monthCode = "H";
                  if (curMonth == 9) monthCode = "I";
                    if (curMonth == 10) monthCode = "J";
                      if (curMonth == 11) monthCode = "K";
                        if (curMonth == 12) monthCode = "L";
                      String ohipdownload = oscarVariables.getProperty("HOME_DIR") ;;
    session.setAttribute("ohipdownload", ohipdownload);      
  %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
<meta http-equiv="Content-Type"
	content="text/html; charset=charset=iso-8859-1">
<script language="JavaScript" type="text/JavaScript">
<!--
function reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
  else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
}
reloadPage(true);

function findObj(n, d) { 
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function showHideLayers() {
  var i,p,v,obj,args=showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
//-->
</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0">
<div id="Layer1"
	style="position: absolute; left: 90px; top: 35px; width: 0px; height: 12px; z-index: 1"></div>
<div id="Layer2"
	style="position: absolute; left: 45px; top: 61px; width: 129px; height: 123px; z-index: 2; background-color: #EEEEFF; layer-background-color: #6666FF; border: 1px none #000000; visibility: hidden;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#DDDDEE">
		<td align='CENTER'><font size="2"
			face="Tahoma, Geneva, Arial, Helvetica, san-serif"><strong>Last
		5 Years</strong></font></td>
	</tr>
	<% for (int i=0; i<5;i++) { %>
	<tr>
		<td align='CENTER'><font size="2"
			face="Tahoma, Geneva, Arial, Helvetica, san-serif"><a
			href="billingOHIPreport.jsp?year=<%=yearArray[i]%>">YEAR <%=yearArray[i]%></a></font></td>
	</tr>
	<% } %>
</table>
</div>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF"> OHIP Report - <%=thisyear%></font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>


<table width="100%" border="0" bgcolor="#E6F0F7">
	<form name="form1" method="post" action="genreport.jsp">
	<tr>

		<td width="220"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><a href="#"
			onClick="showHideLayers('Layer2','','show')">Show Archive</a> </font></b></td>

		<td width="220"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Select provider </font></b></td>
		<td width="254"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"> <select name="provider">
			<option value="all">All Providers</option>
			<% String proFirst="";
           String proLast="";
           String proOHIP="";
           String specialty_code; 
String billinggroup_no;
           int Count = 0;
        ResultSet rslocal;
        rslocal = null;
 rslocal = apptMainBean.queryResults("%", "search_provider_dt");
 while(rslocal.next()){
 proFirst = rslocal.getString("first_name");
 proLast = rslocal.getString("last_name");
 proOHIP = rslocal.getString("ohip_no"); 
 billinggroup_no= SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
 specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");


 %>
			<option value="<%=proOHIP%>,<%=specialty_code%>|<%=billinggroup_no%>"><%=proLast%>,
			<%=proFirst%></option>
			<% 

 }
// apptMainBean.closePstmtConn();
  %>
		</select> </font></b></td>
		<td width="181"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Select billing center</font></b></td>
		<td width="254"><font face="Arial, Helvetica, sans-serif"
			size="2"> <select name="billcenter">

			<% String centerCode="";
           String centerDesc="";
           
           int Count1 = 0;
        ResultSet rsCenter;
        rsCenter = null;
 rsCenter = apptMainBean.queryResults("%", "search_bill_center");
 while(rsCenter.next()){
 centerCode = rsCenter.getString("billcenter_code");
 centerDesc = rsCenter.getString("billcenter_desc");
  
 %>
			<option value="<%=centerCode%>"
				<%=oscarVariables.getProperty("billcenter").compareTo(centerCode)==0?"selected":""%>><%=centerDesc%></option>
			<% } %>
		</select></td>
		<td width="277"><font color="#003366"> <input
			type="submit" name="Submit" value="Create Report"> <input
			type="hidden" name="monthCode" value="<%=monthCode%>"> <input
			type="hidden" name="verCode" value="V03"> <input
			type="hidden" name="curUser" value="<%=user_no%>"> <input
			type="hidden" name="curDate" value="<%=nowDate%>"> </font></td>
	</tr>
	<tr>
		<td colspan="4"><font color="#003366"><b><font
			face="Arial, Helvetica, sans-serif" size="2"> </font></b><font
			face="Arial, Helvetica, sans-serif" size="2"> </font></font><font
			color="#003366"><b><font
			face="Arial, Helvetica, sans-serif" size="2"> </font></b><font
			face="Arial, Helvetica, sans-serif" size="2"> </font></font></td>
	</tr>
	</form>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	bgcolor="<%=yearColor%>">
	<tr>
		<td colspan="6"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"> <b>Activity List </b></font></td>
	</tr>
	<tr>
		<td width="24%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Provider</font></td>
		<!-- <td width="10%"><font face="Arial, Helvetica, sans-serif" size="2" color="#003366">Group 
      No</font></td> -->
		<td width="16%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Creation Date </font></td>
		<td width="10%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Clm/Rec</font></td>
		<td width="15%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003355">Total</font></td>
		<td width="15%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Ohip Filename</font></td>
		<td width="20%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">HTML Filename</font></td>
	</tr>

	<%
ResultSet rspro = null;
String[] paramYear = new String[2];
paramYear[0] = thisyear+"/01/01";
paramYear[1] = thisyear+"/12/31";
   String pro_ohip="", pro_group="", pro_name="", updatedate="", cr="", oFile="", hFile="", total="";
   rslocal = null;
   rslocal = apptMainBean.queryResults(paramYear, "search_billactivity");
   while(rslocal.next()){
   pro_ohip = rslocal.getString("providerohipno"); 
   pro_group = rslocal.getString("groupno");
   updatedate = rslocal.getString("updatedatetime");
   cr = rslocal.getString("claimrecord");
   oFile = rslocal.getString("ohipfilename");
   hFile = rslocal.getString("htmlfilename");
   total = rslocal.getString("total")==null?"0.00":rslocal.getString("total");
    rspro = apptMainBean.queryResults(pro_ohip, "search_provider_ohip_dt");
    while(rspro.next()){
    pro_name = rspro.getString("last_name") + ", " + rspro.getString("first_name");
    }
   %>

	<tr>
		<td width="24%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><%=pro_name%></font></td>
		<!--<td width="10%"><font face="Arial, Helvetica, sans-serif" size="2" color="#003366"><%=pro_group%></font></td>-->
		<td width="16%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><%=updatedate%></font></td>
		<td width="15%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><%=cr%></font><font
			face="Arial, Helvetica, sans-serif" size="2" color="#003366">&nbsp;</font></td>
		<td width="15%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><%=total.substring(0,total.indexOf(".")) + total.substring(total.indexOf("."), total.indexOf(".") + 3)%></font><font
			face="Arial, Helvetica, sans-serif" size="2" color="#003366">&nbsp;</font></td>

		<td width="15%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><a
			href="../servlet/OscarDownload?homepath=ohipdownload&filename=<%=oFile%>"
			target="_blank"><%=oFile%></a></font></td>
		<td width="20%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><a
			href="../servlet/OscarDownload?homepath=ohipdownload&filename=<%=hFile%>"
			target="_blank"><%=hFile%></a></font></td>
	</tr>
	<%  }
  
   
 apptMainBean.closePstmtConn();
  %>
</table>
</body>
</html>
