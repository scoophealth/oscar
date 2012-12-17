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
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ReportAgeSex" %>
<%@ page import="org.oscarehr.common.dao.ReportAgeSexDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="oscar.util.ConversionUtils" %>
<%
	ReportAgeSexDao reportAgeSexDao = SpringUtils.getBean(ReportAgeSexDao.class);
    DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);

GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

  String nowDate = String.valueOf(curYear)+"-"+String.valueOf(curMonth) + "-" + String.valueOf(curDay);
  int dob_yy=0, dob_dd=0, dob_mm=0, age=0;
  reportAgeSexDao.deleteAllByDate(now.getTime());

  String demo_no="", demo_sex="", provider_no="", roster="", patient_status="", date_joined="";
  String demographic_dob="1800";

	int count1 = 0;
	
 	String[] param =new String[8];
 	for(Demographic d: demographicDao.getDemographicWithGreaterThanYearOfBirth(Integer.parseInt(demographic_dob))) {
 	
      demo_no = d.getDemographicNo().toString();
      demo_sex = d.getSex();
      roster = d.getRosterStatus();
      patient_status = d.getPatientStatus();
      provider_no = d.getProviderNo();
      date_joined =ConversionUtils.toDateString(d.getDateJoined());
      dob_yy = Integer.parseInt(d.getYearOfBirth());
      dob_mm = Integer.parseInt(d.getMonthOfBirth());
      dob_dd = Integer.parseInt(d.getDateOfBirth());
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
