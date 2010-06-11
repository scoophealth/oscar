<%--
  This file is deprecated - functionality is relocated to oscarReportAgeSex.jsp
--%>

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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
String Total="0", mNum="", fNum="";
 String dateBegin = request.getParameter("xml_vdate");
   String dateEnd = request.getParameter("xml_appointment_date");
   if (dateEnd.compareTo("") == 0) dateEnd = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
   if (dateBegin.compareTo("") == 0) dateBegin="1950-01-01"; // set to any early date to start search from beginning
ResultSet rs;
ResultSet rs2;
String[] param = new String[7];
param[0] = "RO";
param[1] = "%"; 
param[2] = providerview; 
param[3] = "0";
param[4] = "200";
param[5] = dateBegin;
param[6] = dateEnd ;
rs = null;
rs = apptMainBean.queryResults(param, "count_reportagesex_roster");
while(rs.next()){
Total = apptMainBean.getString(rs,"n");
}


  BigDecimal percent = new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP);
       BigDecimal mdNum = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal fdNum = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal mPercId = new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);
    BigDecimal fPercId = new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);
    BigDecimal mPerc = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal fPerc = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal mPercTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal fPercTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

    BigDecimal mTotal = new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);
    BigDecimal fTotal= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);
    BigDecimal BigTotal= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
     BigDecimal BigTotalPerc= new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);    
   BigDecimal LineTotal= new BigDecimal(0).setScale(0, BigDecimal.ROUND_HALF_UP);    
   BigDecimal LinePerc= new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);    
   %>
<pre><font face="Arial, Helvetica, sans-serif" size="2"> <bean:message
	key="oscarReport.oscarReportAgeSex.msgDate" />: <%=curYear%>-<%=curMonth%>-<%=curDay%>                          <bean:message
	key="oscarReport.oscarReportAgeSex.msgUnit" />: <%=clinic%>                                              <bean:message
	key="oscarReport.oscarReportAgeSex.msgPhysician" />: <%=providerview%></font></pre>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#CCCCFF">
		<td>
		<div align="center"><bean:message
			key="oscarReport.oscarReportAgeSex_noroster.msgAge" /></div>
		</td>
		<td colspan='12'>
		<div align="center">---------------------------<bean:message
			key="oscarReport.oscarReportAgeSex_noroster.msgFemale" />
		---------------------------------</div>
		</td>
		<td>
		<div align="center"></div>
		</td>
		<td colspan='12'>
		<div align="center">----------------------------<bean:message
			key="oscarReport.oscarReportAgeSex_noroster.msgMale" />
		----------------------------------</div>
		</td>
		<td colspan='2'>
		<div align="center">---<bean:message
			key="oscarReport.oscarReportAgeSex_noroster.msgTotal" /> ---</div>

		</td>
	</tr>
	<tr bgcolor="#CCCCFF">
		<td width="10%">
		<div align="center"><bean:message
			key="oscarReport.oscarReportAgeSex_noroster.msgGroup" /></div>
		</td>
		<td width="8%">
		<div align="right">####</div>
		</td>
		<td width="8%">
		<div align="right">%%%%</div>
		</td>
		<td width="2%">
		<div align="center">+</div>
		</td>
		<td width="2%">
		<div align="center">9</div>
		</td>
		<td width="2%">
		<div align="center">8</div>
		</td>
		<td width="2%">
		<div align="center">7</div>
		</td>
		<td width="2%">
		<div align="center">6</div>
		</td>
		<td width="2%">
		<div align="center">5</div>
		</td>
		<td width="2%">
		<div align="center">4</div>
		</td>
		<td width="2%">
		<div align="center">3</div>
		</td>
		<td width="2%">
		<div align="center">2</div>
		</td>
		<td width="2%">
		<div align="center">1</div>
		</td>
		<td width="2%">
		<div align="center">0</div>
		</td>
		<td width="2%">
		<div align="center">1</div>
		</td>
		<td width="2%">
		<div align="center">2</div>
		</td>
		<td width="2%">
		<div align="center">3</div>
		</td>
		<td width="2%">
		<div align="center">4</div>
		</td>
		<td width="2%">
		<div align="center">5</div>
		</td>
		<td width="2%">
		<div align="center">6</div>
		</td>
		<td width="2%">
		<div align="center">7</div>
		</td>
		<td width="2%">
		<div align="center">8</div>
		</td>
		<td width="2%">
		<div align="center">9</div>
		</td>
		<td width="2%">
		<div align="center">+</div>
		</td>
		<td width="8%">
		<div align="right">%%%%</div>
		</td>
		<td width="8%">
		<div align="right">####</div>
		</td>
		<td width="8%">
		<div align="right">####</div>
		</td>
		<td width="8%">
		<div align="right">%%%%</div>
		</td>
	</tr>

	<% String[][] AgeMatrix = new String[20][2];
     AgeMatrix[0][0] = "0";
     AgeMatrix[0][1] = "4";
     AgeMatrix[1][0] = "5";
     AgeMatrix[1][1] = "9";
     AgeMatrix[2][0] = "10";
     AgeMatrix[2][1] = "14";
     AgeMatrix[3][0] = "15";
     AgeMatrix[3][1] = "19";
     AgeMatrix[4][0] = "20";
     AgeMatrix[4][1] = "24";
     AgeMatrix[5][0] = "25";
     AgeMatrix[5][1] = "29";
     AgeMatrix[6][0] = "30";
     AgeMatrix[6][1] = "34";
     AgeMatrix[7][0] = "35";
     AgeMatrix[7][1] = "39";
     AgeMatrix[8][0] = "40";
     AgeMatrix[8][1] = "44";
     AgeMatrix[9][0] = "45";
     AgeMatrix[9][1] = "49";
     AgeMatrix[10][0] = "50";
     AgeMatrix[10][1] = "54";
     AgeMatrix[11][0] = "55";
     AgeMatrix[11][1] = "59";
     AgeMatrix[12][0] = "60";
     AgeMatrix[12][1] = "64";
     AgeMatrix[13][0] = "65";
     AgeMatrix[13][1] = "69";
     AgeMatrix[14][0] = "70";
     AgeMatrix[14][1] = "74";
     AgeMatrix[15][0] = "75";
     AgeMatrix[15][1] = "79";
     AgeMatrix[16][0] = "80";
     AgeMatrix[16][1] = "84";
     AgeMatrix[17][0] = "85";
     AgeMatrix[17][1] = "89";
     AgeMatrix[18][0] = "90";
     AgeMatrix[18][1] = "94";
     AgeMatrix[19][0] = "95";
     AgeMatrix[19][1] = "200";
     
     
     
     for (int i=0;i<20; i++){
     
  String[] param1 = new String[7];
  param1[0] = "RO";
  param1[1] = "M%";
  param1[2] = providerview;
  param1[3] = AgeMatrix[i][0];
  param1[4] = AgeMatrix[i][1];
  param1[5] = dateBegin;
param1[6] = dateEnd ;
  rs = null;
  rs = apptMainBean.queryResults(param1, "count_reportagesex_roster");
  while(rs.next()){
  mNum = apptMainBean.getString(rs,"n");
  }
  String[] param2 = new String[7];
  param2[0] = "RO";
  param2[1] = "F%";
  param2[2] = providerview;
  param2[3] = AgeMatrix[i][0];
  param2[4] = AgeMatrix[i][1];
  param2[5] = dateBegin;
param2[6] = dateEnd ;
  rs2 = null;
  rs2 = apptMainBean.queryResults(param2, "count_reportagesex_roster");
  while(rs2.next()){
  fNum = rs2.getString("n");
}
  if (Total ==null || Total.compareTo("") == 0 || Total.compareTo("0") ==0){Total = "9999";}
  if (mNum ==null || mNum.compareTo("") == 0 || mNum.compareTo("0") ==0){mNum="0";}
  mdNum = new BigDecimal(Double.parseDouble(mNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
  BigTotal = new BigDecimal(Double.parseDouble(Total)).setScale(2, BigDecimal.ROUND_HALF_UP);
  mPerc = mdNum.divide(BigTotal,4);
  mPerc = mPerc.multiply(percent).setScale(1, BigDecimal.ROUND_HALF_UP);;
  mPercId = mPerc.setScale(0, BigDecimal.ROUND_HALF_UP);
    fdNum = new BigDecimal(Double.parseDouble(fNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
   fPerc = fdNum.divide(BigTotal,4);
  fPerc = fPerc.multiply(percent).setScale(1, BigDecimal.ROUND_HALF_UP);
  fPercId = fPerc.setScale(0, BigDecimal.ROUND_HALF_UP);
   
   LineTotal = fdNum.add(mdNum).setScale(0, BigDecimal.ROUND_HALF_UP);
   LinePerc = fPerc.add(mPerc);
  %>

	<tr>
		<td>
		<div align="center"><%=AgeMatrix[i][0]%>-<%=AgeMatrix[i][1]%></div>
		</td>
		<td>
		<div align="right"><%=mNum%></div>
		</td>
		<td>
		<div align="right"><%=mPerc%></div>
		</td>
		<%=WriteMaleBar(Integer.parseInt(mPercId.toString()))%>
		<td bgcolor="#000000">
		<div align="center"><font color="#CCCCCC">|</font></div>
		</td>
		<%=WriteFemaleBar(Integer.parseInt(fPercId.toString()))%>

		<td>
		<div align="right"><%=fPerc%></div>
		</td>
		<td>
		<div align="right"><%=fNum%></div>
		</td>
		<td>
		<div align="right"><%=LineTotal%></div>
		</td>
		<td>
		<div align="right"><%=LinePerc%></div>
		</td>
	</tr>
	<% 
  
  mPercTotal = mPercTotal.add(mPerc);
  fPercTotal = fPercTotal.add(fPerc);
  mTotal = mTotal.add(mdNum);
  fTotal = fTotal.add(fdNum);
  BigTotalPerc = BigTotalPerc.add(LinePerc);
  } %>
	<tr bgcolor="#CCCCFF">
		<td width="10%">
		<div align="center"></div>
		</td>
		<td width="8%">
		<div align="right"><%=fTotal.toString().substring(0, fTotal.toString().indexOf("."))%></div>
		</td>
		<td width="8%">
		<div align="right"><%=fPercTotal%></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="2%">
		<div align="center"></div>
		</td>
		<td width="8%">
		<div align="right"><%=mPercTotal%></div>
		</td>
		<td width="8%">
		<div align="right"><%=mTotal.toString().substring(0, mTotal.toString().indexOf("."))%></div>
		</td>
		<td width="8%">
		<div align="right"><%=BigTotal.toString().substring(0, BigTotal.toString().indexOf("."))%></div>
		</td>
		<td width="8%">
		<div align="right"><%=BigTotalPerc%></div>
		</td>
	</tr>
</table>
