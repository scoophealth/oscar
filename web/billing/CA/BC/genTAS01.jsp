 <%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat" errorPage="errorpage.jsp" %>
<%@ include file="../../../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbBilling.jsp" %>
<html>
<head>
<link rel="stylesheet" href="billing.css" >
<title>Teleplan
      Reconcilliation</title>
</head>

<body bgcolor="#EBF4F5" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr bgcolor="#486ebd">
     <th align='LEFT'>
		<input type='button' name='print' value='Print' onClick='window.print()'> </th> 
    <th align='CENTER'  ><font face="Arial, Helvetica, sans-serif" color="#FFFFFF">Teleplan 
      Reconcilliation - Billed Report</font></th>
      <th align='RIGHT'><input type='button' name='close' value='Close' onClick='window.close()'></th>
  </tr>
</table>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);
%>

<% String raNo = "", flag="", plast="", pfirst="", pohipno="", proNo="";
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="";



      proNo = request.getParameter("proNo");
      raNo = request.getParameter("rano");
      if (raNo.compareTo("") == 0 || raNo == null){
      flag = "0";
      }
      else{
      
      %>
        <table border="0" cellspacing="0" cellpadding="0" width="100%" >
      	      <tr bgcolor="#333333">
      	        <th align='CENTRE'><form action="genRAError.jsp"><input type="hidden" name="rano" value="<%=raNo%>"><select name="proNo"><option value="all"  <%=proNo.equals("all")?"selected":""%>>All Providers</option>
	
	<%   
	    ResultSet rsdemo3 = null;
	    ResultSet rsdemo2 = null;
      	    ResultSet rsdemo = null;
      	  	    rsdemo = apptMainBean.queryResults(raNo, "search_taprovider");
      	     while (rsdemo.next()) {   
      	     pohipno = rsdemo.getString("t_practitionerno");
      	     plast = rsdemo.getString("last_name");
      	     pfirst = rsdemo.getString("first_name");
	  
	  %>
	 <option value="<%=pohipno%>" <%=proNo.equals(pohipno)?"selected":""%>><%=plast%>,<%=pfirst%></option>
	   <%
	  
	  }
      %>
      </select><input type=submit name=submit value=Generate></form></th></tr>
      </table>
      
      
      <% if (proNo.compareTo("") == 0 || proNo.compareTo("all") == 0 || proNo == null){ 
      %>
      <table width="100%" border="1" cellspacing="0" cellpadding="0" bgcolor="#EFEFEF"><form>
        <tr> 
         <td width="10%" height="16">Office No</td>
         <td width="10%" height="16">Practitioner</td>
         <td width="5%" height="16">AJC1</td>
         <td width="5%" height="16">AJA1</td>
         <td width="5%" height="16">AJC2</td>
	          <td width="5%" height="16">AJA2</td>
         <td width="5%" height="16">AJC3</td>
	          <td width="5%" height="16">AJA3</td>
         <td width="5%" height="16">AJC4</td>
	          <td width="5%" height="16">AJA4</td>
         <td width="5%" height="16">AJC5</td>
	          <td width="5%" height="16">AJA5</td>
         <td width="5%" height="16">AJC6</td>
	          <td width="5%" height="16">AJA6</td>
         <td width="5%" height="16">AJC7</td>
	          <td width="5%" height="16">AJA7</td>
         
         <td width="10%" height="16" align="right">Paid Amount</td>
          </tr>
      <%
      
      
         String[] param = new String[3];
                param[0] = raNo;
          param[1] = "S01";
          param[2] = "%";
          
                 String[] param0 = new String[2];
                rsdemo2 = null;
      rsdemo = null;
            	    rsdemo = apptMainBean.queryResults(param, "search_taS01");
            	     while (rsdemo.next()) {   
            	    account = rsdemo.getString("t_officeno");
            	    
            	   // param0[0]=raNo;
            	   // param0[1]=account;
            	   //   demoLast = "";
            	   // rsdemo3 =apptMainBean.queryResults(param0[1],"search_bill");
            	   // while (rsdemo3.next()) {
            	   // demoLast = rsdemo3.getString("demographic_name");
            	   // }
            	   // rsdemo2 = apptMainBean.queryResults(param0,"search_rabillno");
            	    
            	   // while (rsdemo2.next()) {   
             	   //servicecode = rsdemo2.getString("service_code");
             	   //servicedate = rsdemo2.getString("service_date");
             	   //serviceno = rsdemo2.getString("service_count");
      	           //explain = rsdemo2.getString("error_code");
      	           //amountsubmit = rsdemo2.getString("amountclaim");
      	           //amountpay = rsdemo2.getString("amountpay");
                   //                        if (explain.compareTo("") == 0 || explain == null){
		//		            	           explain = "**";
      	         //  }      
      %>
        <tr> 
                <td width="10%" height="16"><%=rsdemo.getString("t_officeno")%>&nbsp; </td>
               <td width="10%" height="16"><%=rsdemo.getString("t_practitionerno")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_ajc1")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_aja1")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_ajc2")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_aja2")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_ajc3")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_aja3")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_ajc4")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_aja4")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_ajc5")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_aja5")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_ajc6")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_aja6")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_ajc7")%>&nbsp; </td>
               <td width="5%" height="16"><%=rsdemo.getString("t_aja7")%>&nbsp; </td>
               <td width="10%" height="16" align=right><%=rsdemo.getString("t_paidamt")%></td>
  </tr>
      
      
      <%
      }
       
      }else {
      
      }
      }%>
     
             
</table>
     
 
 </body>
 </html>