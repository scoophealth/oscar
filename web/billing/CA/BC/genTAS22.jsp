 <%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
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
      <table width="100%" border="1" cellspacing="0" cellpadding="0" bgcolor="#EFEFEF">
        <tr> 
         <td width="10%" height="16">Payment Date</td>
         <td width="10%" height="16">Practitioner</td>
         <td width="10%" height="16">Practitioner Name</td>
         <td width="10%" height="16" align="right">Billed Amount</td>
         <td width="10%" height="16" align="right">Paid Amount</td>
         <td width="50%" height="16">Status &nbsp;</td>
         
          
          </tr>
      <%
      
      
         String[] param = new String[3];
                param[0] = raNo;
          param[1] = "S01";
          param[2] = "%";
          
                 String[] param0 = new String[2];
                rsdemo2 = null;
      rsdemo = null;
            	    rsdemo = apptMainBean.queryResults(param, "search_taS22");
            	     while (rsdemo.next()) {   
            	 //   account = rsdemo.getString("t_officeno");
            	    
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
         
  
         <td width="10%" height="16"><%=rsdemo.getString("t_payment")%></td>
               <td width="10%" height="16"><%=rsdemo.getString("t_practitionerno")%></td>
               <td width="10%" height="16"><%=rsdemo.getString("t_practitionername")%></td>
               <td width="10%" height="16" align="right"><%=moneyFormat(rsdemo.getString("t_amtbilled"))%></td>
               <td width="10%" height="16" align="right"><%=moneyFormat(rsdemo.getString("t_amtpaid"))%></td>
               <td width="50%" height="16"><%=rsdemo.getString("t_linecode").compareTo("Y")==0?"Practitioner Totals within Payee":""%></td>
      
  </tr>
      
      
      <%
      }
      %>

       
           </table>
 <table width="100%" border="1" cellspacing="0" cellpadding="0" bgcolor="#EFEFEF">
              <tr> 
               <td width="10%" height="16">Payment Date</td>
               <td width="5%" height="16">Payee</td>
               <td width="5%" height="16">AJ. Code</td>
               <td width="10%" height="16">AJ. Identifiction</td>
               <td width="10%" height="16">AJ. Message</td>
               <td width="10%" height="16">Calc Method</td>
               <td width="5%" height="16">Regular %</td>
               <td width="5%" height="16">One-Time %</td>
               <td width="5%" height="16">Net AMT</td>
               <td width="5%" height="16">Regular AMT</td>
               <td width="5%" height="16">Onetime AMT</td>
               <td width="5%" height="16">Balance FWD.</td>
               <td width="10%" height="16">AJ. Made</td>
               <td width="10%" height="16">AJ. Outstanding</td>
               
                   
                </tr>
            <%
            
            
               String[] param1 = new String[3];
                      param1[0] = raNo;
                param1[1] = "S01";
                param1[2] = "%";
                
                     
                      rsdemo = null;
            rsdemo = null;
                   	    rsdemo = apptMainBean.queryResults(param1, "search_taS23");
                  	     while (rsdemo.next()) {   
             %>
              <tr> 
        
                     <td width="10%" height="16"><%=rsdemo.getString("t_payment")%>&nbsp;</td>
      	       <td width="5%" height="16"><%=rsdemo.getString("t_payeeno")%>&nbsp;</td>
      	       <td width="5%" height="16"><%=rsdemo.getString("t_ajc")%>&nbsp;</td>
      	       <td width="10%" height="16"><%=rsdemo.getString("t_aji")%>&nbsp;</td>
      	       <td width="10%" height="16"><%=rsdemo.getString("t_ajm")%>&nbsp;</td>
      	       <td width="10%" height="16"><%=rsdemo.getString("t_calcmethod")%>&nbsp;</td>
      	       <td width="5%"  height="16" align="right"><%=moneyFormat(rsdemo.getString("t_rpercent"))%>&nbsp;</td>
      	       <td width="5%"  height="16" align="right"><%=moneyFormat(rsdemo.getString("t_opercent"))%>&nbsp;</td>
      	       <td width="5%"  height="16" align="right"><%=moneyFormat(rsdemo.getString("t_gamount"))%>&nbsp;</td>
      	       <td width="5%"  height="16" align="right"><%=moneyFormat(rsdemo.getString("t_ramount"))%>&nbsp;</td>
      	       <td width="5%"  height="16" align="right"><%=moneyFormat(rsdemo.getString("t_oamount"))%>&nbsp;</td>
      	       <td width="5%"  height="16" align="right"><%=moneyFormat(rsdemo.getString("t_balancefwd"))%>&nbsp;</td>
      	       <td width="10%" height="16" align="right"><%=moneyFormat(rsdemo.getString("t_adjmade"))%>&nbsp;</td>
               <td width="10%" height="16" align="right"><%=moneyFormat(rsdemo.getString("t_adjoutstanding"))%>&nbsp;</td>
        </tr>
            
            
            <%
            }
            
            %>
            </table>
            
             <table width="100%" border="1" cellspacing="0" cellpadding="0" bgcolor="#EFEFEF">
	                  <tr> 
	                   <td width="10%" height="16">Payment Date</td>
	                   <td width="10%" height="16">Payee</td>
	                   <td width="10%" height="16">Practitioner</td>
	                   <td width="70%" height="16">Message</td>
	                   
	                       
	                    </tr>
	                <%
	                
	                
	                   String[] param2 = new String[3];
	                          param2[0] = raNo;
	                    param2[1] = "S01";
	                    param2[2] = "%";
	                    
	                         
	                          rsdemo = null;
	                rsdemo = null;
	                      	    rsdemo = apptMainBean.queryResults(param2, "search_taS25");
	                      	     while (rsdemo.next()) {   
	                 %>
	                  <tr> 
	            
	                         <td width="10%" height="16"><%=rsdemo.getString("t_payment")%>&nbsp;</td>
	          	       <td width="10%" height="16"><%=rsdemo.getString("t_payeeno")%>&nbsp;</td>
	          	       <td width="10%" height="16"><%=rsdemo.getString("t_practitionerno")%>&nbsp;</td>
	          	         <td width="70%" height="16"><%=rsdemo.getString("t_message")%>&nbsp;</td>
	            </tr>
	                
	                
	                <%
	                }
	                
	                %>
            </table>
     <%
      
      }
      
      else {
      
      }
      
      }
     %>
     
             

     
     
     
             

  
 </body>
 </html>
<%!
    String moneyFormat(String str){       
        String moneyStr = "0.00";
        try{             
            moneyStr = new java.math.BigDecimal(str).movePointLeft(2).toString();
        }catch (Exception moneyException) { moneyException.printStackTrace(); }
    return moneyStr;
    }
%>