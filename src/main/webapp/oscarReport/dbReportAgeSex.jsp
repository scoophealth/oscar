<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ include file="dbReport.jspf"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ReportAgeSex" %>
<%@ page import="org.oscarehr.common.dao.ReportAgeSexDao" %>
<%
	ReportAgeSexDao reportAgeSexDao = SpringUtils.getBean(ReportAgeSexDao.class);

GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

  String nowDate = String.valueOf(curYear)+"-"+String.valueOf(curMonth) + "-" + String.valueOf(curDay);
  int dob_yy=0, dob_dd=0, dob_mm=0, age=0;
  reportAgeSexDao.deleteAllByDate(now.getTime());

  String demo_no="", demo_sex="", provider_no="", roster="", patient_status="", date_joined="";
  String demographic_dob="1800";

   ResultSet rsdemo2 = null;
	int count1 = 0;
	int param4 = Integer.parseInt(demographic_dob);
 	String[] param =new String[8];
	rsdemo2 = apptMainBean.queryResults(param4, "search_demographic_all");
	while (rsdemo2.next()) {
      demo_no = rsdemo2.getString("demographic_no");
      demo_sex = rsdemo2.getString("sex");
      roster = rsdemo2.getString("roster_status");
      patient_status = rsdemo2.getString("patient_status");
      provider_no = rsdemo2.getString("provider_no");
      date_joined = rsdemo2.getString("date_joined");
      dob_yy = Integer.parseInt(rsdemo2.getString("year_of_birth"));
      dob_mm = Integer.parseInt(rsdemo2.getString("month_of_birth"));
      dob_dd = Integer.parseInt(rsdemo2.getString("date_of_birth"));
      if(dob_yy!=0) age=MyDateFormat.getAge(dob_yy,dob_mm,dob_dd);


	   ReportAgeSex reportAgeSex = new ReportAgeSex();
	   reportAgeSex.setDemographicNo(Integer.parseInt(demo_no));
	   reportAgeSex.setAge(age);
	   reportAgeSex.setRoster(roster);
	   reportAgeSex.setSex(demo_sex);
	   reportAgeSex.setProviderNo(provider_no);
	   reportAgeSex.setDateJoined(MyDateFormat.getSysDate(date_joined));
	   reportAgeSex.setStatus(patient_status);
	   reportAgeSex.setReportDate(MyDateFormat.getSysDate(nowDate));
	   reportAgeSexDao.persist(reportAgeSex);
   }





%>
<jsp:forward page='oscarReportAgeSex.jsp' />
