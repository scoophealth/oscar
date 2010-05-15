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

<%@ page errorPage="ErrorPage.jsp"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>

<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanFunctionGenerator" scope="session"
	class="bean.FunctionGenerator" />
<html>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>EditAppointment</title>
</head>
<body>


<%   
        int demographic_no = 0;
 
        if (!request.getParameter("demographic_no").equals (new String("0"))){
          demographic_no = new Integer(request.getParameter("demographic_no")).intValue() ;
        }       
   
        int creator = new Integer((String) session.getAttribute("user")).intValue();
        int appointment_no = new Integer( request.getParameter("appointment_no").trim() ).intValue();
        String appointment_date = request.getParameter("appointment_date").trim();
        String start_time = request.getParameter("start_time").trim();
        String end_time = request.getParameter("end_time").trim();
        String name = request.getParameter("name").trim();
        String reason = request.getParameter("reason").trim();
        String location = request.getParameter("location").trim();
//        String creator = request.getParameter("creator").trim();
        String status = request.getParameter("status").trim();
        String type = request.getParameter("type").trim();
        String notes = request.getParameter("notes").trim();
        String resources = request.getParameter("resources").trim();
        String createdatetime = request.getParameter("createdatetime").trim();

//************************************    
// if user click "Update" button
if (request.getParameter("submitButton")!=null &&
               request.getParameter("submitButton").equals("Update")){

/*
// if it it a NEW patient, insert into Demographic
      if(beanDBQuery.isNewPatientOrNot(name)){

         //  beanDBQuery.insertDemographic(name);
      }else{
// if it is an old patitnt, get demographic_no
           //demographic_no = beanDBQuery.getDemographic_no(name);

      }
*/
 
// where to get demography_no ???????? ,billing =?remarks=?style=?'
//      String queryString = update appointment set demographic_no=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,createdatetime=?,creator=?,remarks=? where appointment_no=? "}, //provider_no=? and appointment_date=? and start_time=?";     
/*        String queryString = "update appointment set appointment_date='"+appointment_date
                             +"',start_time='"+start_time+"',end_time='"+end_time
                             +"',name='"+name+"', notes='"+notes+"',reason ='"+reason
                             +"',location='"+location+"', resources='"+resources
                             +"', type='"+type+"',status='"+status
                             +"',createdatetime='"+createdatetime 
                             +"',creator="+creator
                             +",demographic_no="+demographic_no
                             +"  where appointment_no= "+appointment_no+"";*/
        String queryString = "update appointment set appointment_date='"+appointment_date
                             +"',start_time='"+start_time+"',end_time='"+end_time
                             +"',name='"+name+"', notes='"+notes+"',reason ='"+reason
                             +"',location='"+location+"', resources='"+resources
                             +"', type='"+type+"',status='"+status
                             +"',updatedatetime='"+createdatetime 
                             +"',creator="+creator
                             +",demographic_no="+demographic_no
                             +"  where appointment_no= "+appointment_no+"";

//if action is good, then give me the result
 
    if (beanDBConnect.executeUpdate(queryString)) {
    response.sendRedirect("AppointmentForm.jsp?appointment_no="+appointment_no+"&demographic_no="+demographic_no);


//if action is failed,
  } else {
%>
<br>
<br>
Sorry, update
<br>
has failed.
<%  
  }

  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
 


%>
<br>
<br>
<a href="AppointmentForm.jsp?appointment_no=<%=appointment_no%>">Return</a>

<%
//******************************************
// if user click "Search" button
}else{

//      response.sendRedirect("AppointmentSearch.jsp?name="+name+"&appointment_no="+appointment_no+"&appointment_date="+appointment_date+"&start_time="+start_time+"&end_time="+end_time+"&back_to=AppointmentForm2");
 
    String gotoJsp = "AppointmentSearch.jsp?name="+name+"&appointment_no="+appointment_no+"&appointment_date="+appointment_date+"&start_time="+start_time+"&end_time="+end_time+"&demographic_no="+demographic_no;
           gotoJsp = beanFunctionGenerator.getHttpHeaderString(gotoJsp);    

 
    response.sendRedirect(gotoJsp);

 
}


%>
</body>
</html>