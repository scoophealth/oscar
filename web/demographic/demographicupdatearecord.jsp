<%@ page import="java.sql.*, java.util.*, oscar.oscarWaitingList.WaitingList" errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
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
<html> 
<head><title>demographic: the following records</title>

</head>
<link rel="stylesheet" href="../web.css" />
<body onLoad="this.focus()" >
  <center>
    <table border="0" cellspacing="0" cellpadding="0" width="100%" >
      <tr bgcolor="#486ebd"> 
            <th align="CENTER"><font face="Helvetica" color="#FFFFFF">
            UPDATE demographic RECORD</font></th>
      </tr>
    </table>
<%
  ResultSet rs = null;
  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.action.Action.LOCALE_KEY);

  //if action is good, then give me the result
    String[] param =new String[27];
	  param[0]=request.getParameter("last_name");
	  param[1]=request.getParameter("first_name");
	  param[2]=request.getParameter("address");
	  param[3]=request.getParameter("city");
	  param[4]=request.getParameter("province");
	  param[5]=request.getParameter("postal");
	  param[6]=request.getParameter("phone");
	  param[7]=request.getParameter("phone2");
	  param[8]=request.getParameter("email");
	  param[9]=request.getParameter("pin");
	  param[10]=request.getParameter("year_of_birth");

	  param[11]=request.getParameter("month_of_birth")!=null && request.getParameter("month_of_birth").length()==1 ? "0"+request.getParameter("month_of_birth") : request.getParameter("month_of_birth");
	  param[12]=request.getParameter("date_of_birth")!=null && request.getParameter("date_of_birth").length()==1 ? "0"+request.getParameter("date_of_birth") : request.getParameter("date_of_birth");
	  param[13]=request.getParameter("hin");
	  param[14]=request.getParameter("ver");
	  param[15]=request.getParameter("roster_status");
	  param[16]=request.getParameter("patient_status");
	  param[17]=request.getParameter("date_joined_year")+"-"+request.getParameter("date_joined_month")+"-"+request.getParameter("date_joined_date");
	  param[18]=request.getParameter("chart_no");
	  param[19]=request.getParameter("provider_no");
	  param[20]=request.getParameter("sex");
	  param[21]=request.getParameter("end_date_year")+"-"+request.getParameter("end_date_month")+"-"+request.getParameter("end_date_date");
	  param[22]=request.getParameter("eff_date_year")+"-"+request.getParameter("eff_date_month")+"-"+request.getParameter("eff_date_date");
	  param[23]=request.getParameter("pcn_indicator");
	  param[24]=request.getParameter("hc_type");
	  param[25]=request.getParameter("hc_renew_date_year")+"-"+request.getParameter("hc_renew_date_month")+"-"+request.getParameter("hc_renew_date_date");
	  param[26]="<rdohip>" + request.getParameter("r_doctor_ohip") + "</rdohip><rd>" + request.getParameter("r_doctor") + "</rd>" + (request.getParameter("family_doc")!=null? ("<family_doc>" + request.getParameter("family_doc") + "</family_doc>") : "") ;  
	  int []intparam=new int[] {Integer.parseInt(request.getParameter("demographic_no"))};

  int rowsAffected = apptMainBean.queryExecuteUpdate(param, intparam,  request.getParameter("dboperation"));
  if (rowsAffected ==1) {
    //find the democust record for update
    rs = apptMainBean.queryResults(request.getParameter("demographic_no"), "search_custrecordno");
    if(rs.next() ) { //update
      String[] param1 =new String[6];
	    param1[0]=request.getParameter("resident");
	    param1[1]=request.getParameter("nurse");
	    param1[2]=request.getParameter("alert");
            param1[3]=request.getParameter("midwife");
	    param1[4]="<unotes>"+ request.getParameter("notes")+"</unotes>";
	    param1[5]=request.getParameter("demographic_no");
        rowsAffected = apptMainBean.queryExecuteUpdate(param1, "update_custrecord");
        
    } else { //add
	    String[] param2 =new String[6];
	    param2[0]=request.getParameter("demographic_no");
	    param2[1]=request.getParameter("resident");
	    param2[2]=request.getParameter("nurse");
	    param2[3]=request.getParameter("alert");
	    param2[4]=request.getParameter("midwife");
	    param2[5]="<unotes>"+ request.getParameter("notes")+"</unotes>";
        rowsAffected = apptMainBean.queryExecuteUpdate(param2, "add_custrecord");
    }
    //add to waiting list if the waiting_list parameter in the property file is set to true
   
    WaitingList wL = WaitingList.getInstance();
    if(wL.getFound()){
    
        String[] paramWLChk = new String[2];
        paramWLChk[0] = request.getParameter("demographic_no");
        paramWLChk[1] = request.getParameter("list_id");    
        //check if patient has already added to the waiting list and check if the patient already has an appointment in the future
        rs = apptMainBean.queryResults(paramWLChk, "search_demo_waiting_list");

        if(!rs.next()){
            String[] paramWLPosition = new String[1];
            paramWLPosition[0] = request.getParameter("list_id");
            if(paramWLPosition[0].compareTo("")!=0){
                ResultSet rsWL = apptMainBean.queryResults(paramWLPosition, "search_waitingListPosition");
                if(rsWL.next()){
                    String[] paramWL = new String[4];
                    paramWL[0] = request.getParameter("list_id");
                    paramWL[1] = request.getParameter("demographic_no");
                    paramWL[2] = request.getParameter("waiting_list_note");
                    //System.out.println("max position: " + Integer.toString(rsWL.getInt("position")));
                    paramWL[3] = Integer.toString(rsWL.getInt("position") + 1);
                    apptMainBean.queryExecuteUpdate(paramWL, "add2waitinglist");
                }
            }
        }
    }
    
    if (vLocale.getCountry().equals("BR")) {
	    //find the demographic_ptbr record for update
	    rs = apptMainBean.queryResults(request.getParameter("demographic_no"),"search_demographic_ptbr");
	    if(rs.next() ) { //update
	  	 	String[] parametros = new String[13];
  	  	
	  	  	parametros[0]=request.getParameter("cpf");
	  	  	parametros[1]=request.getParameter("rg");
	  	  	parametros[2]=request.getParameter("chart_address");
	  	  	parametros[3]=request.getParameter("marriage_certificate");
	  	  	parametros[4]=request.getParameter("birth_certificate");
	  	  	parametros[5]=request.getParameter("marital_state");
	  	  	parametros[6]=request.getParameter("partner_name");
	  	  	parametros[7]=request.getParameter("father_name");
	  	  	parametros[8]=request.getParameter("mother_name");
	  	  	parametros[9]=request.getParameter("district");
	  	  	parametros[10]=request.getParameter("address_no")==null || request.getParameter("address_no").trim().equals("")?"0":request.getParameter("address_no");
	  	  	parametros[11]=request.getParameter("complementary_address");
	  	  	parametros[12]=request.getParameter("demographic_no");
  	
	  		rowsAffected = apptMainBean.queryExecuteUpdate(parametros,"update_record_ptbr");
    
	    }else{//add
	 	 	String[] parametros = new String[13];
	  	  	
	  	  	parametros[0]=request.getParameter("demographic_no");
	  	  	parametros[1]=request.getParameter("cpf");
	  	  	parametros[2]=request.getParameter("rg");
	  	  	parametros[3]=request.getParameter("chart_address");
	  	  	parametros[4]=request.getParameter("marriage_certificate");
	  	  	parametros[5]=request.getParameter("birth_certificate");
	  	  	parametros[6]=request.getParameter("marital_state");
	  	  	parametros[7]=request.getParameter("partner_name");
	  	  	parametros[8]=request.getParameter("father_name");
	  	  	parametros[9]=request.getParameter("mother_name");
	  	  	parametros[10]=request.getParameter("district");
	  	  	parametros[11]=request.getParameter("address_no")==null || request.getParameter("address_no").trim().equals("")?"0":request.getParameter("address_no");
	  	  	parametros[12]=request.getParameter("complementary_address");
  	
  	
	  		rowsAffected = apptMainBean.queryExecuteUpdate(parametros,"add_record_ptbr");
	    }
	}
%>
  <h2> Update a Provider Record Successfully ! 
  <p><a href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&displaymode=edit&dboperation=search_detail"><%= request.getParameter("demographic_no") %></a>
  </h2>
<%--
<script LANGUAGE="JavaScript">
     	self.opener.refresh();
      //self.close();
</script>
--%>
<%  
    response.sendRedirect("demographiccontrol.jsp?demographic_no=" + request.getParameter("demographic_no") + "&displaymode=edit&dboperation=search_detail");
    //response.sendRedirect("search.jsp");
  } else {
%>
  <h1>Sorry, fail to update !!! <%= request.getParameter("demographic_no") %>.
<%  
  }
  apptMainBean.closePstmtConn(); 
%>
  <p></p>
<%@ include file="footer.jsp" %>
<script language="JavaScript">
this.focus();
</SCRIPT>  
  </center>
</body>
</html>
