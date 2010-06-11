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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
String cTotal="0",hTotal="0",oTotal="0", mNum="", fNum="";
 String dateBegin = request.getParameter("xml_vdate");
   String dateEnd = request.getParameter("xml_appointment_date");
   if (dateEnd.compareTo("") == 0) dateEnd = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
   if (dateBegin.compareTo("") == 0) dateBegin="1950-01-01"; // set to any early date to start search from beginning
ResultSet rs;
ResultSet rs2;
String[] param = new String[3];

param[0] = clinicview; 
param[1] = dateBegin;
param[2] = dateEnd ;

String[] param2 = new String[6];

param2[0] = "1972";
param2[1] = "1982";
param2[2] = "1983";
param2[3] = "1994";
param2[4] = dateBegin;
param2[5] = dateEnd ;

String[] param3 = new String[7];

param3[0] = clinicview;
param3[1] = "1972";
param3[2] = "1982";
param3[3] = "1983";
param3[4] = "1994";
param3[5] = dateBegin;
param3[6] = dateEnd ;
rs = null;
String newBilling = "";
if (OscarProperties.getInstance().getBooleanProperty("isNewONbilling","true")){
    newBilling = "_new";  //I've added the coresponding queries to dbReport.jsp with the suffix _new /count
}
rs = apptMainBean.queryResults(param, "count_larrykain_clinic"+newBilling);
while(rs.next()){
cTotal = apptMainBean.getString(rs,"n");
}
rs = null;
rs = apptMainBean.queryResults(param2, "count_larrykain_hospital"+newBilling);
while(rs.next()){
hTotal = apptMainBean.getString(rs,"n");
}
rs = null;
rs = apptMainBean.queryResults(param3, "count_larrykain_other"+newBilling);
while(rs.next()){
oTotal = apptMainBean.getString(rs,"n");
}

 
    BigDecimal ccTotal = new BigDecimal(Integer.parseInt(cTotal)).setScale(0, BigDecimal.ROUND_HALF_UP);
    BigDecimal hhTotal= new BigDecimal(Integer.parseInt(hTotal)).setScale(0, BigDecimal.ROUND_HALF_UP);
    BigDecimal BigTotal= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   
   BigDecimal ooTotal= new BigDecimal(Integer.parseInt(oTotal)).setScale(0, BigDecimal.ROUND_HALF_UP);    
   
   
   BigTotal = BigTotal.add(ccTotal);
   BigTotal = BigTotal.add(hhTotal);
   BigTotal = BigTotal.add(ooTotal);
   %>
<p></p>
<table width="60%" border="0" cellspacing="0" cellpadding="2"
	align="center">
	<tr bgcolor="#CCCCFF">
		<td colspan="2"><font size="2"
			face="Arial, Helvetica, sans-serif"><b>Encounter
		Statistics - Larry Kain Report</b></font></td>
	</tr>
	<tr bgcolor="#F1E9FE">
		<td><font size="2" face="Arial, Helvetica, sans-serif"><%=clinic%></font></td>
		<td><font size="2" face="Arial, Helvetica, sans-serif">Run
		on <%=MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay)%></font></td>
	</tr>
	<tr bgcolor="#F1E9FE">
		<td colspan="2"><font size="2"
			face="Arial, Helvetica, sans-serif">Visits between <%=dateBegin%>
		and <%=dateEnd%></font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><font size="2" face="Arial, Helvetica, sans-serif">Clinic
		visits</font></td>
		<td align="right"><font size="2"
			face="Arial, Helvetica, sans-serif"><%=cTotal%></font></td>
	</tr>
	<tr>
		<td><font size="2" face="Arial, Helvetica, sans-serif">HHS
		visits</font></td>
		<td align="right"><font size="2"
			face="Arial, Helvetica, sans-serif"><%=hTotal%></font></td>
	</tr>
	<tr>
		<td><font size="2" face="Arial, Helvetica, sans-serif">All
		other visits</font></td>
		<td align="right"><font size="2"
			face="Arial, Helvetica, sans-serif"><%=oTotal%></font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr bgcolor="#CCCCFF">
		<td><font size="2" face="Arial, Helvetica, sans-serif"><b>Total</b></font></td>
		<td align="right"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><%=BigTotal%><b></font></td>
	</tr>
</table>
