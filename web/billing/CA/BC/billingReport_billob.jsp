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


<table width="100%" border="2" cellpadding="0" cellspacing="0">

	<% 
 String dateBegin = request.getParameter("xml_vdate");
   String dateEnd = request.getParameter("xml_appointment_date");
   if (dateEnd.compareTo("") == 0) dateEnd = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
   if (dateBegin.compareTo("") == 0) dateBegin="1950-01-01"; // set to any really early time, start searching from beginning
  BigDecimal bdOBFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal BigOBTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
      
 double dOBFee = 0.00;

 ResultSet rs=null ;
  ResultSet rs2=null ; 
  String[] param =new String[3];
  String[] param2 =new String[10];
  param[0] = request.getParameter("providerview");
 param[1] = dateBegin;  
  param[2] = dateEnd;  
  rs = apptMainBean.queryResults(param, "search_billob");
int rCount = 0;
  boolean bodd=false;
   nItems=0;
  String apptDoctorNo="", apptNo="", demoNo = "", demoName="", userno="", apptDate="", apptTime="", reason="",  note="", total="";
  if(rs==null) {
    out.println("failed!!!"); 
  } else {
  %>
	<tr bgcolor="#CCCCFF">
		<TH align="center" width="5%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">INVOICE</font></b></TH>
		<TH align="center" width="20%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">PATIENT</font></b></TH>
		<TH align="center" width="10%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">DATE</font></b></TH>
		<TH align="center" width="20%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">BILLING CODE</font></b></TH>
		<TH align="center" width="10%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">BILLED</font></b></TH>
		<TH align="center" width="5%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">STATUS</font></b></TH>

	</tr>
	<%
    while (rs.next()) {
     
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
     demoName = apptMainBean.getString(rs,"demographic_name");
       apptDate = apptMainBean.getString(rs,"billing_date");
      reason = apptMainBean.getString(rs,"status");
      total = apptMainBean.getString(rs,"total").indexOf(".") > 0?apptMainBean.getString(rs,"total"):apptMainBean.getString(rs,"total").substring(0,apptMainBean.getString(rs,"total").length()-2)+"."+apptMainBean.getString(rs,"total").substring(apptMainBean.getString(rs,"total").length()-2) ;
      
      if (reason.equals("S")){
        dOBFee = Double.parseDouble(total);
                       bdOBFee = new BigDecimal(dOBFee).setScale(2, BigDecimal.ROUND_HALF_UP);
      		        	BigOBTotal = BigOBTotal.add(bdOBFee);
      }	    
      
      for(int i=0; i<10; i++){
      param2[i] = "";
      }
      rCount = 0;
   rs2 = apptMainBean.queryResults(apptMainBean.getString(rs,"billing_no"), "search_bill_record");
   while (rs2.next()){
   param2[rCount] = rs2.getString("service_code");
   rCount = rCount +1;
   }
   
   

%>
	<tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
		<TD align="left" width="5%"><b><font size="2"
			face="Arial, Helvetica, sans-serif"><a href=#
			onClick='popupPage(700,720, "../../../billing/CA/BC/billingView.do?billing_no=<%=apptMainBean.getString(rs,"billing_no")%>&dboperation=search_bill&hotclick=0")'
			title="<%=reason%>"> <%=apptMainBean.getString(rs,"billing_no")%></a></font></b></TD>
		<TD align="left" width="20%"><b><font size="2"
			face="Arial, Helvetica, sans-serif"><%=demoName%></font></b></TD>
		<TD align="left" width="12%"><b><font size="2"
			face="Arial, Helvetica, sans-serif"><%=apptDate%></font></b></TD>

		<TD align="left" width="20%"><b><font size="2"
			face="Arial, Helvetica, sans-serif"> <% for(int x=0; x<10; x++){ %>
		<%=param2[x]%> &nbsp; <%}%> </font></b></TD>
		<TD align="right" width="10%"><b> <font size="2"
			face="Arial, Helvetica, sans-serif"><%=total%></font></b></TD>
		<TD align="center" width="5%"><b><font size="2"
			face="Arial, Helvetica, sans-serif"><%=reason%></font></b></TD>

	</tr>
	<%  rowCount = rowCount + 1;
    }
    if (rowCount == 0) {
    %>
	<tr bgcolor="<%=bodd?"ivory":"white"%>">
		<TD colspan="6" align="center"><b><font size="2"
			face="Arial, Helvetica, sans-serif">No unbill items</font></b></TD>
	</tr>
	<% }else{
%>
	<tr bgcolor="<%=bodd?"ivory":"white"%>">
		<TD colspan="4" align="right"><b><font size="2"
			face="Arial, Helvetica, sans-serif">Total</font></b></TD>
		<TD align="right"><b><font size="2"
			face="Arial, Helvetica, sans-serif"><%=BigOBTotal%></font></b></TD>
		<TD align="center"><b><font size="2"
			face="Arial, Helvetica, sans-serif">Paid </font></b></TD>
	</tr>
	<%
}}%>

</table>
