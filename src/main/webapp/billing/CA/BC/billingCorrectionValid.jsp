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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title></title>
</head>
<body>
<%@ page
	import="java.io.*, java.sql.*, java.util.*,java.net.*, oscar.*, java.math.*"
	errorPage="errorpage.jsp"%>


<jsp:useBean id="dbBillingDataBean" scope="page"
	class="oscar.dbBillingData">
	<jsp:setProperty name="dbBillingDataBean" property="*" />
</jsp:useBean>
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>
<center>
<h2>Billing Correction Review (session)</h2>
</center>
<hr>
<%
 
 try {
 BigDecimal billingunit = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal otherunit = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal percent = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
 BigDecimal percentPremium = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal BigTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal BigSubTotal = new BigDecimal(0).setScale(2);
 BigDecimal xpercent = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
 BigDecimal xpercentPremium = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 
String numCode="";
String diagcode="";
String diagnostic_code = request.getParameter("xml_diagnostic_detail");
String billunit = "", content="", billingamount="";
String r_doctor =request.getParameter("rd");
String roster_status = request.getParameter("roster")==null?"": request.getParameter("roster");
String m_review = request.getParameter("m_review")==null?"":"checked";
String r_doctor_ohip=request.getParameter("rdohip");
String r_status = request.getParameter("referral")==null?"":"checked";
String HCTYPE = request.getParameter("hc_type");
String HCSex = request.getParameter("hc_sex");
String specialty = request.getParameter("specialty");
       content = content + "<rdohip>" + r_doctor_ohip+"</rdohip>" + "<rd>" + r_doctor + "</rd>";
   content = content + "<xml_referral>" + r_status +"</xml_referral>" + "<mreview>" + m_review + "</mreview>";
    content = content + "<hctype>" + HCTYPE+"</hctype>" + "<demosex>" + HCSex + "</demosex>";
content = content + "<specialty>" + specialty + "</specialty>";
content = content + "<xml_roster>" + roster_status + "</xml_roster>";


if (diagnostic_code == null || diagnostic_code.compareTo("") == 0){
 diagnostic_code = "000|Other code";
 diagcode="000";
 } else{
 
 
diagcode = diagnostic_code.substring(0,3);
numCode = "";
for(int i=0;i<diagcode.length();i++)
 {
 String c = diagcode.substring(i,i+1);
 if(c.hashCode()>=48 && c.hashCode()<=58)
 numCode += c;
 }
 if (numCode.length() < 3) {
 diagnostic_code = "000|Other code";
 diagcode="000";
 }
}
       String pValue="", pCode="", pDesc="", pPerc="", pUnit = "";
       String eValue="", eCode="", eDesc="", ePerc="", eUnit = "";
         String xValue="", xCode="", xDesc="", xPerc="", xUnit = "";
       String eFlag = "", xFlag="";
  	String otherstr2="", otherstr="", scode = "", desc="", value="", percentage="";
 	String temp=null;//default is not null  
 	String tempBill=null;
 	String[] strAuth=null;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if( temp.indexOf("xml_")==-1 ) continue;
  	content+="<" +temp+ ">" +SxmlMisc.replaceHTMLContent(request.getParameter(temp))+ "</" +temp+ ">";
         }
       
         

		BigDecimal pValue1 = new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_UP);
	 BillingBean billing = new BillingBean();

	 for (Enumeration f = request.getParameterNames() ; f.hasMoreElements() ;) {
		tempBill=f.nextElement().toString();
		if( tempBill.indexOf("servicecode")==-1 ) continue;
		billunit = request.getParameter("billingunit" + tempBill.substring(11));
		billingamount = request.getParameter("billingamount" + tempBill.substring(11));
  	    dbBillingDataBean.setService_code(request.getParameter(tempBill));      
  	    
  	    strAuth = dbBillingDataBean.ejbLoad();
  	     if(strAuth!=null) { //login successfully
 		 scode = strAuth[0];
 		 desc = strAuth[1];
  		 value = strAuth[2];
  		 percentage = strAuth[3];
  	if (billingamount==null || billingamount.compareTo("")==0){
	value = value;
	}else{
	value = billingamount;
	
	
}
  	
  	BigDecimal otherunit2 = new BigDecimal(Double.parseDouble(value)).setScale(2, BigDecimal.ROUND_HALF_UP);
  billingunit = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  		billingunit = new BigDecimal(Double.parseDouble(billunit)).setScale(2, BigDecimal.ROUND_HALF_UP);
  	 otherunit2 = billingunit.multiply(otherunit2).setScale(2, BigDecimal.ROUND_HALF_UP);
  	 
  		 if (scode.compareTo("P009A")==0 || scode.compareTo("P006A") == 0 || scode.compareTo("P011A") == 0 || scode.compareTo("P018A") == 0 || scode.compareTo("P038A") == 0 || scode.compareTo("P020A") == 0 || scode.compareTo("P041A") == 0 ){
  		
  		pValue1 = pValue1.add(otherunit2);
  		pCode = scode;
  		pValue = pValue1.toString();
  		pPerc = percentage;
  		pDesc = desc;
  		pUnit = billunit;
  			 if (eCode.compareTo("") == 0){
					  		}else {
					  		eFlag = "1";
			  		}

  		}
  		
  		 
  		 if (value.compareTo(".00") ==0 ){
  		
  		
  		if (scode.compareTo("E411A") == 0 ){
  	
  		eCode = scode;
  		eDesc = desc;
  		eValue = value;
  		ePerc = percentage;
  		eUnit = billunit; 	
  	
  		eFlag = "1";
		}
  		 else{
  		 xCode = scode;
		xDesc = desc;
		xValue = value;
		xPerc = percentage;
  		xUnit = billunit; 
		
					xFlag = "1";
			  		}
  		 }
  		 
  		 else{
  	//	billingunit = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  	//	billingunit = new BigDecimal(Double.parseDouble(billunit)).setScale(2, BigDecimal.ROUND_HALF_UP);
	//        otherunit2 = billingunit.multiply(otherunit2).setScale(2, BigDecimal.ROUND_HALF_UP);
BigTotal = BigTotal.add(otherunit2);
otherstr2 = otherunit2.toString();


StringBuffer sotherBuffer = new StringBuffer(otherstr2);
int f0 = otherstr2.indexOf('.');
sotherBuffer.deleteCharAt(f0);
sotherBuffer.insert(f0,"");
otherstr2 = sotherBuffer.toString();




  	   BillingItemBean billingItem = new BillingItemBean();
  	   billingItem.setService_code(scode);
    	   billingItem.setDesc(desc);
           billingItem.setService_value(otherstr2);
           billingItem.setPercentage(percentage);
           billingItem.setDiag_code(diagcode);
           billingItem.setQuantity(billunit);
   	   billing.addBillingItem(billingItem);
	 }
	 
  	}
  	  
	
  	%>

<%	
         } if (eFlag.compareTo("1") == 0){
			  		
			  	//	billingunit = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
			  	//	billingunit = new BigDecimal(Double.parseDouble(billunit)).setScale(2, BigDecimal.ROUND_HALF_UP);
			  		BigDecimal ecodeunit = new BigDecimal(Double.parseDouble(pValue)).setScale(2, BigDecimal.ROUND_HALF_UP);
					  		
					  		
					  		percent = new BigDecimal(ePerc).setScale(4, BigDecimal.ROUND_HALF_UP);
							        	
							        	
					        	percentPremium = ecodeunit.multiply(percent).setScale(2, BigDecimal.ROUND_HALF_UP);
					        //	percentPremium = billingunit.multiply(percentPremium).setScale(2, BigDecimal.ROUND_HALF_UP);
					        	// otherunit2 = percentPremium;
					        	
					        	BigTotal = BigTotal.add(percentPremium);
							otherstr2 = percentPremium.toString();
							StringBuffer sotherBuffer = new StringBuffer(otherstr2);
							int f0 = otherstr2.indexOf('.');
							sotherBuffer.deleteCharAt(f0);
							sotherBuffer.insert(f0,"");
							otherstr2 = sotherBuffer.toString();
							
							
							
							
							  	   BillingItemBean billingItem = new BillingItemBean();
							  	   billingItem.setService_code(eCode);
							    	   billingItem.setDesc(eDesc);
							           billingItem.setService_value(otherstr2);
							           billingItem.setPercentage(ePerc);
							           billingItem.setDiag_code(diagcode);
							    billingItem.setQuantity(eUnit);
					   	   billing.addBillingItem(billingItem);
			  		
  		}	
        if (xFlag.compareTo("1") == 0){
				  		
				  	//	billingunit = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
				  	//	billingunit = new BigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP);
				  	BigDecimal xcodeunit = new BigDecimal(Double.parseDouble(pValue)).setScale(4, BigDecimal.ROUND_HALF_UP);
						  		
						  		BigTotal = BigTotal.subtract(percentPremium);      		        	
						  		xpercent = new BigDecimal(xPerc).setScale(4, BigDecimal.ROUND_HALF_UP);
								     

						  	xpercentPremium = xpercent.multiply(xcodeunit).setScale(2, BigDecimal.ROUND_HALF_UP);
						        	xpercentPremium = billingunit.multiply(xpercentPremium).setScale(2, BigDecimal.ROUND_HALF_UP);
						        	// otherunit2 = percentPremium;
						        	
						        	BigTotal = BigTotal.add(percentPremium);
						        	BigTotal = BigTotal.add(xpercentPremium);
								otherstr2 = xpercentPremium.toString();
								StringBuffer sotherBuffer = new StringBuffer(otherstr2);
								int f0 = otherstr2.indexOf('.');
								sotherBuffer.deleteCharAt(f0);
								sotherBuffer.insert(f0,"");
								otherstr2 = sotherBuffer.toString();
								
								
								
								
								  	   BillingItemBean billingItem = new BillingItemBean();
								  	   billingItem.setService_code(xCode);
								    	   billingItem.setDesc(xDesc);
								           billingItem.setService_value(otherstr2);
								           billingItem.setPercentage(xPerc);
								           billingItem.setDiag_code(diagcode);
								    billingItem.setQuantity(xUnit);
						   	   billing.addBillingItem(billingItem);
				  		
  		}	
	
        session.setAttribute("billing", billing);
  	
 
       otherstr = BigTotal.toString();
       StringBuffer otherBuffer = new StringBuffer(otherstr);
       int f1 = otherstr.indexOf('.');
       otherBuffer.deleteCharAt(f1);
       otherBuffer.insert(f1,"");
       otherstr = otherBuffer.toString();
        

    
	BillingDataBean billingDataBean = new BillingDataBean();
  	session.setAttribute("billingDataBean", billingDataBean);
  	billingDataBean.setContent(content);
        billingDataBean.setBilling_no(request.getParameter("xml_billing_no"));
        billingDataBean.setHin(request.getParameter("hin"));
        billingDataBean.setDob(request.getParameter("dob"));
        billingDataBean.setVisittype(request.getParameter("visittype"));
        billingDataBean.setVisitdate(request.getParameter("xml_vdate"));
       	billingDataBean.setStatus(request.getParameter("status"));
     	billingDataBean.setClinic_ref_code(request.getParameter("clinic_ref_code"));
     	billingDataBean.setProviderNo(request.getParameter("provider_no"));
     	billingDataBean.setBilling_date(request.getParameter("xml_appointment_date"));
     	billingDataBean.setUpdate_date(request.getParameter("update_date"));
     	billingDataBean.setTotal(otherstr);
     	
     	BillingPatientDataBean billingPatientDataBean = new BillingPatientDataBean();
     	session.setAttribute("billingPatientDataBean",billingPatientDataBean);
     	billingPatientDataBean.setDemoname(request.getParameter("demo_name"));
        billingPatientDataBean.setAddress(request.getParameter("demo_address"));
        billingPatientDataBean.setProvince(request.getParameter("demo_province"));
        billingPatientDataBean.setCity(request.getParameter("demo_city"));
        billingPatientDataBean.setPostal(request.getParameter("demo_postal"));
        billingPatientDataBean.setSex(request.getParameter("demo_sex"));
 %>

<%
  response.sendRedirect("billingCorrectionReview.jsp");
  }
   catch (java.lang.ArrayIndexOutOfBoundsException _e0) {
 }%>


</body>
</html>
