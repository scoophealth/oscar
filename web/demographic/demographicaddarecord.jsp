<%@ page  import="java.sql.*, java.util.*, oscar.MyDateFormat" errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />

<html>
<head>
<link rel="stylesheet" href="../web.css" />
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
      //parent.refresh();
      close();
    }   
    //-->
</script>
</head>
<body  onload="start()"  background="../images/gray_bg.jpg" bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<center>
    <table border="0" cellspacing="0" cellpadding="0" width="100%" >
      <tr bgcolor="#486ebd"> 
            <th align="CENTER"><font face="Helvetica" color="#FFFFFF">
            ADD A DEMOGRAPHIC RECORD</font></th>
      </tr>
    </table>
 
<%
  //if action is good, then give me the result
	  //param[0]=Integer.parseInt((new GregorianCalendar()).get(Calendar.MILLISECOND) ); //int
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
	  param[19]=request.getParameter("staff");
	  param[20]=request.getParameter("sex");
	  param[21]=request.getParameter("end_date_year")+"-"+request.getParameter("end_date_month")+"-"+request.getParameter("end_date_date");
	  param[22]=request.getParameter("eff_date_year")+"-"+request.getParameter("eff_date_month")+"-"+request.getParameter("eff_date_date");
	  param[23]=request.getParameter("pcn_indicator");
	  param[24]=request.getParameter("hc_type");
	  param[25]=request.getParameter("hc_renew_date_year")+"-"+request.getParameter("hc_renew_date_month")+"-"+request.getParameter("hc_renew_date_date");
	  param[26]="<rdohip>" + request.getParameter("r_doctor_ohip") + "</rdohip>" + "<rd>" + request.getParameter("r_doctor") + "</rd>"+ (request.getParameter("family_doc")!=null? ("<family_doc>" + request.getParameter("family_doc") + "</family_doc>") : "");    

	String[] paramName =new String[5];
	  paramName[0]=param[0].trim();
	  paramName[1]=param[1].trim();
	  paramName[2]=param[8].trim();
	  paramName[3]=param[9].trim();
	  paramName[4]=param[10].trim();
	  //System.out.println("from -------- :"+ param[0]+ ": next :"+param[1]);
    ResultSet rs = apptMainBean.queryResults(paramName, "search_lastfirstnamedob");
    
    if(rs.next()) { //!rs.getString("cpp_id").equals("")) 
      out.println("***<font color='red'>You may have Demographic the  record already!!! Please search it first, then delete the duplicated one.</font>***<br>");
      return;
    }

    // int rowsAffected = apptMainBean.queryExecuteUpdate(intparam, param, request.getParameter("dboperation"));
    
  int rowsAffected = apptMainBean.queryExecuteUpdate(param, request.getParameter("dboperation")); //add_record
  if (rowsAffected ==1) {
    //find the demo_no and add democust record for alert
    String[] param1 =new String[7];
	  param1[0]=request.getParameter("last_name");
	  param1[1]=request.getParameter("first_name");
	  param1[2]=request.getParameter("year_of_birth");
	  param1[3]=request.getParameter("month_of_birth");
	  param1[4]=request.getParameter("date_of_birth");
	  param1[5]=request.getParameter("hin");
	  param1[6]=request.getParameter("ver");
    
    rs = apptMainBean.queryResults(param1, "search_demoaddno");
    if(rs.next()) { //
      //add democust record for alert
      String[] param2 =new String[6];
	    param2[0]=rs.getString("demographic_no");
	    param2[1]=request.getParameter("cust1");
	    param2[2]=request.getParameter("cust2");
	    param2[3]=request.getParameter("cust3");
	    param2[4]=request.getParameter("cust4");
	    param2[5]=request.getParameter("content");
	    System.out.println("demographic_no" + param2[0] +param2[1]+param2[2]+param2[3]+param2[4]+param2[5] );
      rowsAffected = apptMainBean.queryExecuteUpdate(param2, "add_custrecord" ); //add_record
    }
    
%>
  <p><h2>Successful Addition of a Demographic Record.
  </h2></p>
<%  
  } else {
%>
  <p><h1>Sorry, addition has failed.</h1></p>
<%  
  }
  apptMainBean.closePstmtConn();
%>
  <p> </p>
<%@ include file="footer.htm" %>
</center>
</body>
</html>