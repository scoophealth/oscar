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


<table width="75%" border="0" cellpadding="2">
  <tr bgcolor="#CCCCFF">
    <td>Provider</td>
    <td>&nbsp;</td>
    <td>Clinic</td>
    <td>Outpatient</td>
    <td>Hospital</td>
    <td>ER</td>
    <td>Nursing Home</td>
  </tr>

<%
String cTotal="0",hTotal="0",oTotal="0", mNum="", fNum="";
String p_last="",p_no="",p_first="", team="", oldteam="";
 String dateBegin = request.getParameter("xml_vdate");
   String dateEnd = request.getParameter("xml_appointment_date");
   if (dateEnd.compareTo("") == 0) dateEnd = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
   if (dateBegin.compareTo("") == 0) dateBegin="0000-00-00";
ResultSet rs;
ResultSet rs2;
ResultSet rs3;
ResultSet rs4;
ResultSet rs5;
ResultSet rs6;



String param = "visitreport";


String[] param2 = new String[5];

param2[0] = "00";
param2[1] = "01";
param2[2] = "02";
param2[3] = "03";
param2[4] = "04";


String[] param3 = new String[4];

String[] visitcount = new String[5];
visitcount[0] = "";
visitcount[1] = "";
visitcount[2] = "";
visitcount[3] = "";
visitcount[4] = "";
 
    BigDecimal ccTotal = new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);
    BigDecimal hhTotal= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);
    BigDecimal BigTotal= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   
   BigDecimal ooTotal= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   BigDecimal BigTotal0= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   BigDecimal BigTotal1= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   BigDecimal BigTotal2= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   BigDecimal BigTotal3= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   BigDecimal BigTotal4= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   
      BigDecimal Total0= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
      BigDecimal Total1= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
      BigDecimal Total2= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
      BigDecimal Total3= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   BigDecimal Total4= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);   
 boolean bodd=true;	     

rs = null;
rs = apptMainBean.queryResults(param, "search_reportprovider");
while(rs.next()){
p_last = rs.getString("last_name");
p_first = rs.getString("first_name");
p_no = rs.getString("provider_no");
team = rs.getString("team");

param3[0] = p_no;
param3[1] = param2[0];
param3[2] = dateBegin;
param3[3] = dateEnd ;

rs2 = apptMainBean.queryResults(param3, "count_visit");
while(rs2.next()){
visitcount[0] = rs2.getString("n");
}
param3[0] = p_no;
param3[1] = param2[1];
param3[2] = dateBegin;
param3[3] = dateEnd ;

rs3 = apptMainBean.queryResults(param3, "count_visit");
while(rs3.next()){
visitcount[1] = rs3.getString("n");
}
param3[0] = p_no;
param3[1] = param2[2];
param3[2] = dateBegin;
param3[3] = dateEnd ;

rs4 = apptMainBean.queryResults(param3, "count_visit");
while(rs4.next()){
visitcount[2] = rs4.getString("n");
}
param3[0] = p_no;
param3[1] = param2[3];
param3[2] = dateBegin;
param3[3] = dateEnd ;

rs5 = apptMainBean.queryResults(param3, "count_visit");
while(rs5.next()){
visitcount[3] = rs5.getString("n");
}
param3[0] = p_no;
param3[1] = param2[4];
param3[2] = dateBegin;
param3[3] = dateEnd ;

rs6 = apptMainBean.queryResults(param3, "count_visit");
while(rs6.next()){
visitcount[4] = rs6.getString("n");
}

if (oldteam.compareTo(team) != 0)  bodd=bodd?false:true; //for the color of rows

%>
<tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
    <td><%=oldteam.equals(team)?"":team%></td>
    <td><%=p_no%> <%=p_last%>,<%=p_first%></td>
    <td><%=visitcount[0]%></td>
    <td><%=visitcount[1]%></td>
    <td><%=visitcount[2]%></td>
    <td><%=visitcount[3]%></td>
    <td><%=visitcount[4]%></td>
  </tr>

<%

    Total0= new BigDecimal(Integer.parseInt(visitcount[0].toString())).setScale(0, BigDecimal.ROUND_HALF_UP);    
      Total1= new BigDecimal(Integer.parseInt(visitcount[1].toString())).setScale(0, BigDecimal.ROUND_HALF_UP);    
       Total2= new BigDecimal(Integer.parseInt(visitcount[2].toString())).setScale(0, BigDecimal.ROUND_HALF_UP);    
       Total3= new BigDecimal(Integer.parseInt(visitcount[3].toString())).setScale(0, BigDecimal.ROUND_HALF_UP);    
   Total4= new BigDecimal(Integer.parseInt(visitcount[4].toString())).setScale(0, BigDecimal.ROUND_HALF_UP);  
BigTotal0 = BigTotal0.add(Total0);
BigTotal1 = BigTotal1.add(Total1);
BigTotal2 = BigTotal2.add(Total2);
BigTotal3 = BigTotal3.add(Total3);
BigTotal4 = BigTotal4.add(Total4);
oldteam = team;
   
}   %>
  <tr bgcolor="#CCCCFF">
    <td>&nbsp;</td>
    <td>SUBTOTAL</td>
    <td><%=BigTotal0%></td>
    <td><%=BigTotal1%></td>
    <td><%=BigTotal2%></td>
    <td><%=BigTotal3%></td>
    <td><%=BigTotal4%></td>
  </tr>
</table>
