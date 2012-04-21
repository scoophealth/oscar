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
<%
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
%>
<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="../errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Summary</title>
<script language="JavaScript">
<!--
function validate_form(){
  // do some validation
  document.body.style.cursor = "wait";
  document.forms.form1.Submit.disabled = true;
  return true;
}
//-->
</SCRIPT>

</head>

<%
String errorMsg="", errorFlag = "";
String proNO = request.getParameter("xml_provider");
String temp2=null, content="", location2="", desc3="",desc2="", scode2=null, scode3=null, dsfee="", fee2="",fee3="", flag="", flag1="", flag2="";
     String pValue="", pCode="", pDesc="", pPerc="", pUnit = "";
        String eValue="", eCode="", eDesc="", ePerc="", eUnit = "";
         String xValue="", xCode="", xDesc="", xPerc="", xUnit = "";
            String eFlag = "", xFlag="";

if (proNO.compareTo("000000") == 0) {
errorFlag = "1";
errorMsg = errorMsg + "Error: Please select a valid Provider. <br>";
}
%>

<%
int counter = 0;
 String numCode="";
String diagcode="";
String diagnostic_code = request.getParameter("xml_diagnostic_code");
String diagnostic_detail = request.getParameter("xml_diagnostic_detail");
if (diagnostic_detail == null || diagnostic_detail.compareTo("") == 0) {
if (diagnostic_code.substring(0,3).compareTo("000") == 0){
// diagnostic_code = request.getParameter("xml_diagnostic_detail");
 diagnostic_code = "000|Other code";
diagcode = diagnostic_code.substring(0,3);

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
else {
diagcode = diagnostic_code.substring(0,3);
}
} else {
diagnostic_code = request.getParameter("xml_diagnostic_detail");
diagcode = diagnostic_detail.substring(0,3);
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
if (diagcode.compareTo("000") == 0) {
errorFlag = "1";
errorMsg = errorMsg + "Error: Please enter a valid Diagnostic Code. <br>";
}
String demoname=request.getParameter("demographic_name");
GregorianCalendar now=new GregorianCalendar();
ResultSet rslocal = null;
String demoNO = request.getParameter("demographic_no");
String r_doctor="", r_doctor_ohip="", r_status="";
  r_status=request.getParameter("xml_referral");
  String demoFirst="", demoLast="", demoHIN="", demoDOB="", demoDOBYY="", demoDOBMM="", demoDOBDD="", demoSex="", demoHCTYPE="";

 // String demoFirst="", demoLast="", demoHIN="", demoDOB="", demoDOBYY="", demoDOBMM="", demoDOBDD="";;// int dobCount1=0, dobCount2=0;
  rslocal = null;
 rslocal = apptMainBean.queryResults(demoNO, "search_demographic_details");
 while(rslocal.next()){
 demoFirst = rslocal.getString("first_name");
 demoLast = rslocal.getString("last_name");
 demoHIN = rslocal.getString("hin") + rslocal.getString("ver");
demoSex = rslocal.getString("sex");
    if (demoSex.compareTo("M")==0) demoSex ="1";
    if (demoSex.compareTo("F")==0) demoSex ="2";

    demoHCTYPE = rslocal.getString("hc_type")==null?"":rslocal.getString("hc_type");
    if(demoHCTYPE == null) demoHCTYPE = "ON";
    if (demoHCTYPE.compareTo("") == 0 || demoHCTYPE == null || demoHCTYPE.length() <2) {demoHCTYPE="ON"; }else{ demoHCTYPE=demoHCTYPE.substring(0,2).toUpperCase();}
   demoDOBYY = rslocal.getString("year_of_birth");
   demoDOBMM = rslocal.getString("month_of_birth");
  demoDOBDD = rslocal.getString("date_of_birth");
  if (rslocal.getString("family_doctor") == null){ r_doctor = "N/A"; r_doctor_ohip="000000";}else{
   r_doctor=SxmlMisc.getXmlContent(rslocal.getString("family_doctor"),"rd")==null?"":SxmlMisc.getXmlContent(rslocal.getString("family_doctor"),"rd");
   r_doctor_ohip=SxmlMisc.getXmlContent(rslocal.getString("family_doctor"),"rdohip")==null?"":SxmlMisc.getXmlContent(rslocal.getString("family_doctor"),"rdohip");
  }
   if ( demoDOBMM.length() ==1){
   demoDOBMM = "0" + demoDOBMM;
   }
   if ( demoDOBDD.length() ==1){
   demoDOBDD = "0" + demoDOBDD;
   }
   demoDOB = demoDOBYY + demoDOBMM + demoDOBDD;
  }

   if (r_doctor==null || r_status == null || r_doctor.equals("")) r_doctor="N/A";
   if (r_doctor_ohip==null || r_status == null || r_doctor_ohip.equals("")) r_doctor_ohip="000000";
ResultSet rsprovider = null;

String proFirst="", proLast="", proRMA="", proOHIPNO="", crFirst="Not", crLast="Available", apptFirst="Not", apptLast="Available", asstFirst="Not", asstLast="Available";
  rsprovider = null;
 rsprovider = apptMainBean.queryResults(proNO, "search_provider_name");
 while(rsprovider.next()){
 proFirst = rsprovider.getString("first_name");
 proLast = rsprovider.getString("last_name");
  proOHIPNO = rsprovider.getString("ohip_no");
 proRMA = rsprovider.getString("rma_no");
  }
    rsprovider = null;
   rsprovider = apptMainBean.queryResults(request.getParameter("apptProvider_no"), "search_provider_name");
   while(rsprovider.next()){
  apptFirst = rsprovider.getString("first_name");
  apptLast = rsprovider.getString("last_name");

  }
    rsprovider = null;
   rsprovider = apptMainBean.queryResults(request.getParameter("asstProvider_no"), "search_provider_name");
   while(rsprovider.next()){
 asstFirst = rsprovider.getString("first_name");
  asstLast = rsprovider.getString("last_name");
  }
      rsprovider = null;
     rsprovider = apptMainBean.queryResults(request.getParameter("user_no"), "search_provider_name");
     while(rsprovider.next()){
crFirst = rsprovider.getString("first_name");
   crLast = rsprovider.getString("last_name");
  }
  String visitdate = ""; visitdate = request.getParameter("xml_vdate");
  String billtype = request.getParameter("xml_billtype");
  String visittype = request.getParameter("xml_visittype");
  if (visittype.substring(0,2).compareTo("00") == 0) {
  visitdate = request.getParameter("xml_appointment_date");
} else {
   visitdate = request.getParameter("xml_vdate");
    if (visitdate.compareTo("") == 0 || visitdate == null){
     visitdate=request.getParameter("xml_appointment_date");
    }else{
     visitdate = visitdate;
    }
    }
  %>
<body bgcolor="#FFFFFF" text="#000000">
<form name="form1" method="post" action="billingSave.jsp"
	onsubmit="return validate_form();">
<p><b><font face="Arial, Helvetica, sans-serif">Generic
Billing Form for General Practice</font></b></p>
<p><b><font face="Arial, Helvetica, sans-serif"><u>Billing
Summary</u></font></b><br>
</p>

<table width="600" border="1">
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif">Patient
		: <%=request.getParameter("demographic_name")%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif">Health#
		:<%=demoHIN%></font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif">Billing
		Type: <%=billtype%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif">Service</font><font
			face="Arial, Helvetica, sans-serif"> Date: <%=request.getParameter("xml_appointment_date")%></font></b></td>
	</tr>
	<%
    	String local_desc="";
        String location1 = request.getParameter("xml_location");
        ResultSet rslocation = null;

 String visitLocation = clinicLocationDao.searchVisitLocation(location1);
 if(visitLocation!=null) {
	 local_desc = visitLocation;
 }

     //
       %>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif">Visit
		Type Code: <%=request.getParameter("xml_visittype")%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif">Billing
		Physician: <%=proFirst%> <%=proLast%></font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif">Visit
		Location: <%=local_desc%></font></b></td>
		<td width="46%"><font face="Arial, Helvetica, sans-serif"><b>Admission
		Date: <%=visitdate%></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif">Appointment
		Physician: <%=apptFirst%> <%=apptLast%> </font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif">Secordary
		Physician: <%=asstFirst%> <%=asstLast%> </font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif">Referral
		Physician: <%=r_doctor%> </font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif">Referral
		Physician Number: <%=r_doctor_ohip%> </font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif">Creator:
		<%=crFirst%> <%=crLast%> </font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif">Creation
		Date: <%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%></font></b></td>
	</tr>
</table>
<%


 BigDecimal otherfee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal otherfee02 = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal otherfee03 = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal pValue1= new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
  BigDecimal otherunit = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
     BigDecimal xPercent = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
     BigDecimal xPercentPremium = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
     BigDecimal percent = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
   BigDecimal percentPremium = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal BigTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
 BigDecimal BigSubTotal = new BigDecimal(0).setScale(2);
 String temp=null, location="", desc="", scode="", fee="";//default is not null
 String otherdesc1="", otherdbcode1="", otherfee1="", otherperc1="";
 String otherdesc2="", otherdbcode2="", otherfee2="", otherperc2="";
 String otherdesc3="", otherdbcode3="", otherfee3="", otherperc3="";
 int otherflag1 = 0, otherflag2=0, otherflag3=0;
 String recunit="";
 String otherstr="", otherstr2="", otherstr3="", othercode1="", othercode2="", othercode3="", othercode1unit="", othercode2unit="", othercode3unit="";
 othercode1 = request.getParameter("xml_other1");
 othercode1unit = request.getParameter("xml_other1_unit");
 othercode2 = request.getParameter("xml_other2");
 othercode2unit = request.getParameter("xml_other2_unit");
 othercode3 = request.getParameter("xml_other3");
 othercode3unit = request.getParameter("xml_other3_unit");

 if (othercode1.compareTo("") == 0 || othercode1 == null || othercode1.length() < 5) {
  otherflag1 = 0;
  othercode1 = "00000";
  }
  else {
  if (othercode1unit.compareTo("") == 0 || othercode1unit == null) {
  othercode1unit = "1";
  }

   ResultSet rsother = null;

 rsother = null;
 rsother = apptMainBean.queryResults(othercode1.substring(0,5), "search_servicecode_detail");
 while(rsother.next()){
 otherdesc1 = rsother.getString("description");
  otherdbcode1 = rsother.getString("service_code");
   otherfee1 = rsother.getString("value");
   otherperc1 = rsother.getString("percentage");

   }
    //

  if (otherdesc1.compareTo("") == 0 || otherdesc1 == null ) {
  otherflag1 = 0;
  }
  else
  {
//  otherflag1 = 1;



  if (otherfee1.compareTo(".00") == 0) {
    		 otherunit = new BigDecimal(Double.parseDouble(othercode1unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherperc1)).setScale(4, BigDecimal.ROUND_HALF_UP);
     BigDecimal otherperc01 = bdotherFee.multiply(otherunit).setScale(4, BigDecimal.ROUND_HALF_UP);

    if (otherdbcode1.compareTo("E411A") == 0) {



			 eCode = otherdbcode1;
         		eDesc = otherdesc1;
         		ePerc = otherperc01.toString();
         		eValue =otherfee1;
         		eUnit = othercode1unit;
         		eFlag = "1";
         		}
         		else{

         		xCode = otherdbcode1;
 			xDesc = otherdesc1;
 			xPerc = otherperc01.toString();
 			xValue = otherfee1;
 			xUnit = othercode1unit;
        		xFlag = "1";
          }
     }else{

    otherflag1 = 1;
              otherunit = new BigDecimal(Double.parseDouble(othercode1unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
       BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherfee1)).setScale(2, BigDecimal.ROUND_HALF_UP);
        otherfee = bdotherFee.multiply(otherunit).setScale(2, BigDecimal.ROUND_HALF_UP);
         BigTotal = BigTotal.add(otherfee);
       otherstr = otherfee.toString();
       StringBuffer sotherBuffer = new StringBuffer(otherstr);
       int f = otherstr.indexOf('.');
       sotherBuffer.deleteCharAt(f);
       sotherBuffer.insert(f,"");
otherstr = sotherBuffer.toString();

     	if (otherdbcode1.compareTo("P009A")==0 || otherdbcode1.compareTo("P006A") == 0 || otherdbcode1.compareTo("P011A")==0 || otherdbcode1.compareTo("P041A") == 0 || otherdbcode1.compareTo("P018A") == 0 || otherdbcode1.compareTo("P038A") == 0 || otherdbcode1.compareTo("P020A") == 0){
   	    pValue1 = pValue1.add(otherfee);
     pCode =otherdbcode1;
       	 pDesc = otherdesc1;
       	 pValue = pValue1.toString();

       if (eCode.compareTo("") == 0){
       }else{
       eFlag = "1";
       }


}



  }
  }
  }

  if (othercode2.compareTo("") == 0 || othercode2 == null || othercode2.length() < 5) {
  otherflag2 = 0;
   othercode2 = "00000";
  }
  else {
  if (othercode2unit.compareTo("") == 0 || othercode2unit == null) {
  othercode2unit = "1";
  }

 ResultSet  rsother = null;

 rsother = null;
 rsother = apptMainBean.queryResults(othercode2.substring(0,5), "search_servicecode_detail");
 while(rsother.next()){
 otherdesc2 = rsother.getString("description");
  otherdbcode2 = rsother.getString("service_code");
   otherfee2 = rsother.getString("value");
    otherperc2 = rsother.getString("percentage");
   }
   //

  if (otherdesc2.compareTo("") == 0 || otherdesc2 == null) {
  otherflag2 = 0;
  }
  else
  {



if (otherfee2.compareTo(".00") == 0) {
    		 otherunit = new BigDecimal(Double.parseDouble(othercode2unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherperc2)).setScale(4, BigDecimal.ROUND_HALF_UP);
     BigDecimal otherperc02 = bdotherFee.multiply(otherunit).setScale(4, BigDecimal.ROUND_HALF_UP);


    if (otherdbcode2.compareTo("E411A") == 0) {



			 eCode = otherdbcode2;
         		eDesc = otherdesc2;
         		ePerc = otherperc02.toString();
         		eValue =otherfee2;
         		eUnit = othercode2unit;
         		eFlag = "1";
         		}
         		else{

         		xCode = otherdbcode2;
 			xDesc = otherdesc2;
 			xPerc = otherperc02.toString();
 			xValue = otherfee2;
 			xUnit = othercode2unit;
        		xFlag = "1";
          }
     }else{
  otherflag2 = 1;
        BigDecimal otherunit2 = new BigDecimal(Double.parseDouble(othercode2unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
	BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherfee2)).setScale(2, BigDecimal.ROUND_HALF_UP);
         otherfee02 = bdotherFee.multiply(otherunit2).setScale(2, BigDecimal.ROUND_HALF_UP);
BigTotal = BigTotal.add(otherfee02);
otherstr2 = otherfee02.toString();
StringBuffer sotherBuffer = new StringBuffer(otherstr2);
int f = otherstr2.indexOf('.');
sotherBuffer.deleteCharAt(f);
sotherBuffer.insert(f,"");
otherstr2 = sotherBuffer.toString();

     if (otherdbcode2.compareTo("P009A")==0 || otherdbcode2.compareTo("P006A") == 0 || otherdbcode2.compareTo("P011A")==0 || otherdbcode2.compareTo("P041A") == 0 || otherdbcode2.compareTo("P018A") == 0 || otherdbcode2.compareTo("P038A") == 0 || otherdbcode2.compareTo("P020A") == 0){
   	    pValue1 = pValue1.add(otherfee02);
     pCode =otherdbcode2;
       	 pDesc = otherdesc2;
       	 pValue = pValue1.toString();

       if (eCode.compareTo("") == 0){
       }else{
       eFlag = "1";
       }


  }
  }
}
}




  if (othercode3.compareTo("") == 0 || othercode3 == null || othercode3.length() < 5) {
  otherflag3 = 0;
  othercode3 = "00000";
  }
  else {
  if (othercode3unit.compareTo("") == 0 || othercode3unit == null) {
  othercode3unit = "1";
  }

 ResultSet rsother = null;

 rsother = null;
 rsother = apptMainBean.queryResults(othercode3.substring(0,5), "search_servicecode_detail");
 while(rsother.next()){
 otherdesc3 = rsother.getString("description");
  otherdbcode3 = rsother.getString("service_code");
   otherfee3 = rsother.getString("value");

   }

  if (otherdesc3.compareTo("") == 0 || otherdesc3 == null ) {
  otherflag3 = 0;
  }
  else
  {
 if (otherfee3.compareTo(".00") == 0) {
    		 otherunit = new BigDecimal(Double.parseDouble(othercode3unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherperc3)).setScale(4, BigDecimal.ROUND_HALF_UP);
           BigDecimal otherperc03 = bdotherFee.multiply(otherunit).setScale(4, BigDecimal.ROUND_HALF_UP);


    if (otherdbcode3.compareTo("E411A") == 0) {



			 eCode = otherdbcode3;
         		eDesc = otherdesc3;
         		ePerc = otherperc03.toString();
         		eValue =otherfee3;
         		eUnit = othercode3unit;
         		eFlag = "1";
         		}
         		else{

         		xCode = otherdbcode3;
 			xDesc = otherdesc3;
 			xPerc = otherperc03.toString();
 			xValue = otherfee3;
 			xUnit = othercode3unit;
        		xFlag = "1";
          }
     }else{

  otherflag3 = 1;
        BigDecimal otherunit3 = new BigDecimal(Double.parseDouble(othercode3unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
	BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherfee3)).setScale(2, BigDecimal.ROUND_HALF_UP);
   otherfee03 = bdotherFee.multiply(otherunit3).setScale(2, BigDecimal.ROUND_HALF_UP);
BigTotal = BigTotal.add(otherfee03);
otherstr3 = otherfee03.toString();
StringBuffer sotherBuffer = new StringBuffer(otherstr3);
int f = otherstr3.indexOf('.');
sotherBuffer.deleteCharAt(f);
sotherBuffer.insert(f,"");
otherstr3 = sotherBuffer.toString();


     	if (otherdbcode3.compareTo("P009A")==0 || otherdbcode3.compareTo("P006A") == 0 || otherdbcode3.compareTo("P011A")==0 || otherdbcode3.compareTo("P041A") == 0 || otherdbcode3.compareTo("P018A") == 0 || otherdbcode3.compareTo("P038A") == 0 || otherdbcode3.compareTo("P020A") == 0){
   	    pValue1 = pValue1.add(otherfee03);
     pCode =otherdbcode3;
       	 pDesc = otherdesc3;
       	 pValue = pValue1.toString();

       if (eCode.compareTo("") == 0){
       }else{
       eFlag = "1";
       }


  }
  }
  }
  }









  %>
<table width="600" border="1">
	<tr>
		<td width="22%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Service Code</font></b></td>
		<td width="58%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Description</font></b></td>
		<td width="6%">
		<div align="right"><b><font size="2"
			face="Arial, Helvetica, sans-serif">#Unit</font></b></div>
		</td>
		<td width="14%">
		<div align="right"><b><font
			face="Arial, Helvetica, sans-serif" size="2">$ Fee</font></b></div>
		</td>
	</tr>
	<% //String premium = request.getParameter("xml_premium");
       //String longlist = request.getParameter("xml_longlist");


   double dSFee = 0.00;
 double dFee = 0.00;



	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if( temp.indexOf("xml_")==-1 ) continue;
//////////////////////////////////////////////////////////////////////////////////////
  	content+="<" +temp+ ">" +SxmlMisc.replaceHTMLContent(request.getParameter(temp))+ "</" +temp+ ">";
//////////////////////////////////////////////////////////////////////////////////////


        fee = request.getParameter("price_" + temp);
        desc = request.getParameter("desc_" + temp);
        scode = temp.substring(4).toUpperCase();
        if (scode.substring(scode.length()-1).compareTo("A") != 0){
        scode = scode;
        }

                     if (fee == null) {
        		fee = "";
        		}else{

        		if (fee.compareTo(".00")==0) {

        		if (scode.compareTo("E411A") == 0) {
        		eCode = scode;
        		eDesc = desc;
        		ePerc = request.getParameter("perc_"+temp);
        		eValue = fee;
        		eUnit= "1";
        		eFlag = "1";
        		}
        		else{

        		xCode = scode;
			xDesc = desc;
			xPerc = request.getParameter("perc_"+temp);
			xValue = fee;
			xUnit = "1";
        		xFlag = "1";


        		}


          		}else{

          				dFee = Double.parseDouble(fee);
			        		BigDecimal bdFee = new BigDecimal(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
        				BigTotal = BigTotal.add(bdFee);

       	 if (scode.compareTo("P009A")==0 || scode.compareTo("P006A") == 0 || scode.compareTo("P011A")==0 || scode.compareTo("P041A") == 0 || scode.compareTo("P018A") == 0 || scode.compareTo("P038A") == 0 || scode.compareTo("P020A") == 0){

     	 pValue1 = pValue1.add(bdFee);
       	 pCode = scode;
       	 pDesc = desc;
       	 pValue = pValue1.toString();

       if (eCode.compareTo("") == 0){
       }else{
       eFlag = "1";
       }




               }





        if (desc == null){

        desc = "";
        }

        String str = fee.toString();
StringBuffer sBuffer = new StringBuffer(str);
int i = str.indexOf('.');
sBuffer.deleteCharAt(i);
sBuffer.insert(i,"");
str = sBuffer.toString();



        %>
	<tr>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=scode%></font></td>
		<input type="hidden" name="billrec<%=counter%>" value="<%=scode%>">
		<input type="hidden" name="pricerec<%=counter%>" value="<%=str%>">
		<input type="hidden" name="billrecdesc<%=counter%>" value="<%=desc%>">
		<input type="hidden" name="billrecunit<%=counter%>" value="1">
		<td width="58%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=desc%></font></td>
		<td width="6%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2">1</font></div>
		</td>
		<td width="14%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"><font
			face="Arial, Helvetica, sans-serif"><font size="2"><%=fee%></font></font></font></div>
		</td>
	</tr>
	<% counter = counter + 1;

}
}
}
%>



	<% if (otherflag1 == 1) { %>
	<tr>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=otherdbcode1%></font></td>
		<input type="hidden" name="billrec<%=counter%>"
			value="<%=otherdbcode1%>">
		<input type="hidden" name="pricerec<%=counter%>" value="<%=otherstr%>">
		<input type="hidden" name="billrecdesc<%=counter%>"
			value="<%=otherdesc1%>">
		<input type="hidden" name="billrecunit<%=counter%>"
			value="<%=othercode1unit%>">
		<td width="58%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=otherdesc1%></font></td>
		<td width="6%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=othercode1unit%></font></div>
		</td>
		<td width="14%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"><font
			face="Arial, Helvetica, sans-serif"><font size="2"><%=otherfee%></font></font></font></div>
		</td>
	</tr>
	<% counter = counter + 1;
    } %>
	<% if (otherflag2 == 1) { %>
	<tr>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=otherdbcode2%></font></td>
		<input type="hidden" name="billrec<%=counter%>"
			value="<%=otherdbcode2%>">
		<input type="hidden" name="pricerec<%=counter%>"
			value="<%=otherstr2%>">
		<input type="hidden" name="billrecdesc<%=counter%>"
			value="<%=otherdesc2%>">
		<input type="hidden" name="billrecunit<%=counter%>"
			value="<%=othercode2unit%>">
		<td width="58%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=otherdesc2%></font></td>
		<td width="6%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=othercode2unit%></font></div>
		</td>
		<td width="14%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"><font
			face="Arial, Helvetica, sans-serif"><font size="2"><%=otherfee02%></font></font></font></div>
		</td>
	</tr>
	<% counter = counter + 1;
    } %>
	<% if (otherflag3 == 1) { %>
	<tr>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=otherdbcode3%></font></td>
		<input type="hidden" name="billrec<%=counter%>"
			value="<%=otherdbcode3%>">
		<input type="hidden" name="pricerec<%=counter%>"
			value="<%=otherstr3%>">
		<input type="hidden" name="billrecdesc<%=counter%>"
			value="<%=otherdesc3%>">
		<input type="hidden" name="billrecunit<%=counter%>"
			value="<%=othercode3unit%>">
		<td width="58%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=otherdesc3%></font></td>
		<td width="6%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=othercode3unit%></font></div>
		</td>
		<td width="14%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"><font
			face="Arial, Helvetica, sans-serif"><font size="2"><%=otherfee03%></font></font></font></div>
		</td>
	</tr>
	<% counter = counter + 1;
    } %>
	<% if (eFlag.compareTo("1")==0) {

   percent = new BigDecimal(Double.parseDouble(ePerc)).setScale(4, BigDecimal.ROUND_HALF_UP);
   percentPremium = percent.multiply(pValue1).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigTotal = BigTotal.add(percentPremium);

    String str = percentPremium.toString();
StringBuffer sBuffer = new StringBuffer(str);
int i = str.indexOf('.');
sBuffer.deleteCharAt(i);
sBuffer.insert(i,"");
str = sBuffer.toString();
      %>
	<tr>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=eCode%></font></td>
		<input type="hidden" name="billrec<%=counter%>" value="<%=eCode%>">
		<input type="hidden" name="pricerec<%=counter%>" value="<%=str%>">
		<input type="hidden" name="billrecdesc<%=counter%>" value="<%=eDesc%>">
		<input type="hidden" name="billrecunit<%=counter%>" value="<%=eUnit%>">
		<td width="58%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=eDesc%></font></td>
		<td width="6%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=eUnit%></font></div>
		</td>
		<td width="14%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"><font
			face="Arial, Helvetica, sans-serif"><font size="2"><%=percentPremium%></font></font></font></div>
		</td>
	</tr>
	<% counter = counter + 1;
    } %>

	<% if (xFlag.compareTo("1")==0) {

           BigTotal = BigTotal.subtract(percentPremium);
         xPercent = new BigDecimal(Double.parseDouble(xPerc)).setScale(4, BigDecimal.ROUND_HALF_UP);
   	 xPercentPremium = xPercent.multiply(pValue1).setScale(2, BigDecimal.ROUND_HALF_UP);
     BigTotal = BigTotal.add(xPercentPremium);
         BigTotal = BigTotal.add(percentPremium);

        String str = xPercentPremium.toString();
    StringBuffer sBuffer = new StringBuffer(str);
    int i = str.indexOf('.');
    sBuffer.deleteCharAt(i);
    sBuffer.insert(i,"");
    str = sBuffer.toString();
          %>
	<tr>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=xCode%></font></td>
		<input type="hidden" name="billrec<%=counter%>" value="<%=xCode%>">
		<input type="hidden" name="pricerec<%=counter%>" value="<%=str%>">
		<input type="hidden" name="billrecdesc<%=counter%>" value="<%=xDesc%>">
		<input type="hidden" name="billrecunit<%=counter%>" value="<%=xUnit%>">
		<td width="58%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=xDesc%></font></td>
		<td width="6%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=xUnit%></font></div>
		</td>
		<td width="14%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"><font
			face="Arial, Helvetica, sans-serif"><font size="2"><%=xPercentPremium%></font></font></font></div>
		</td>
	</tr>
	<% counter = counter + 1;
    } %>


	<% if (counter == 0) {
    errorFlag = "1";
    errorMsg = errorMsg + "Error: No Service/Procedure Code selected. <br>";
    }

    if (errorFlag.compareTo("1")==0){
    %>
</table>
<p><%=errorMsg%></p>
<% session.setAttribute("content", content); %>
<form><input type=button name=back value='Go Back and Change'
	onClick='javascript:location.href="billingOB.jsp?billForm=<%=request.getParameter("billForm")%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>&demographic_no=<%=request.getParameter("demographic_no")%>&user_no=<%=request.getParameter("user_no")%>&providerview=<%=request.getParameter("apptProvider_no")%>&apptProvider_no=<%=request.getParameter("apptProvider_no")%>&appointment_date=<%=request.getParameter("xml_appointment_date")%>&status=<%=request.getParameter("status")%>&start_time=<%=request.getParameter("start_time")%>&bNewForm=0"'>
</form>

<% } else { %>

<tr>
	<td width="22%"><font face="Arial, Helvetica, sans-serif" size="2"><b>Diagnostic
	Code</b></font></td>
	<td colspan="2"><font face="Arial, Helvetica, sans-serif" size="2"><%=diagnostic_code%></font></td>
	<td width="14%">
	<div align="right"><font face="Arial, Helvetica, sans-serif"><font
		face="Arial, Helvetica, sans-serif"><font size="2"></font></font></font></div>
	</td>
</tr>

<% if (request.getParameter("xml_research1") == null || request.getParameter("xml_research1").compareTo("") == 0) { %>
<tr>
	<td width="22%"><font face="Arial, Helvetica, sans-serif" size="2"></font></td>
	<td colspan="2">
	<div align="right"><font face="Arial, Helvetica, sans-serif"
		size="2">Total:</font></div>
	</td>
	<td width="14%">
	<div align="right"><font face="Arial, Helvetica, sans-serif"
		size="2"><%=BigTotal%></font></div>
	</td>
</tr>
</table>
<% } else { %>

<tr>
	<td width="22%"><font face="Arial, Helvetica, sans-serif" size="2"><b>Research
	Code</b></font></td>
	<td colspan="2"><font face="Arial, Helvetica, sans-serif" size="2"><%=request.getParameter("xml_research1")%>
	<%=request.getParameter("xml_research2").compareTo("")==0?"":", "+request.getParameter("xml_research2")%>
	<%=request.getParameter("xml_research3").compareTo("")==0?"":", "+request.getParameter("xml_research3")%></font></td>
	<td width="14%">
	<div align="right"><font face="Arial, Helvetica, sans-serif"><font
		face="Arial, Helvetica, sans-serif"><font size="2"></font></font></font></div>
	</td>
</tr>
<tr>
	<td width="22%"><font face="Arial, Helvetica, sans-serif" size="2"></font></td>
	<td colspan="2">
	<div align="right"><font face="Arial, Helvetica, sans-serif"
		size="2">Total:</font></div>
	</td>
	<td width="14%">
	<div align="right"><font face="Arial, Helvetica, sans-serif"
		size="2"><%=BigTotal%></font></div>
	</td>
</tr>
</table>
<% } %>
<p>
<%
  content = content + "<xml_providername>" +  proFirst + " " + proLast + "</xml_providername>";
  content = content + "<xml_apptprovidername>" + apptFirst + " " + apptLast + "</xml_apptprovidername>";
  content = content + "<xml_asstprovidername>" +  asstFirst + " " + asstLast + "</xml_asstprovidername>";
      content = content + "<xml_creator>" +  crFirst + " " + crLast + "</xml_creator>";
       content = content + "<rdohip>" + r_doctor_ohip+"</rdohip>" + "<rd>" + r_doctor + "</rd>";
    content = content + "<hctype>" + demoHCTYPE+"</hctype>" + "<demosex>" + demoSex + "</demosex>";
  %> <input type="hidden" name="pohip_no" value="<%=proOHIPNO%>">
<input type="hidden" name="prma_no" value="<%=proRMA%>"> <input
	type="hidden" name="record" value="<%=counter%>"> <input
	type="hidden" name="diagcode" value="<%=diagcode%>"> <input
	type="hidden" name="visittype"
	value="<%=request.getParameter("xml_visittype").substring(0,2)%>">
<input type="hidden" name="billtype"
	value="<%=billtype.substring(0,1)%>"> <input type="hidden"
	name="content" value="<%=content%>"> <input type="hidden"
	name="provider_no" value="<%=proNO%>"> <input type="hidden"
	name="clinic_no" value="<%=request.getParameter("clinic_no")%>">
<input type="hidden" name="demographic_no"
	value="<%=request.getParameter("demographic_no")%>"> <input
	type="hidden" name="billing_name" value="OB2"> <input
	type="hidden" name="user_no"
	value="<%=request.getParameter("user_no")%>"> <input
	type="hidden" name="apptProvider_no"
	value="<%=request.getParameter("apptProvider_no")%>"> <input
	type="hidden" name="asstProvider_no"
	value="<%=request.getParameter("asstProvider_no")%>"> <input
	type="hidden" name="billing_date"
	value="<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>">
<input type="hidden" name="billing_time"
	value="<%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)%>">
<input type="hidden" name="billingservice_code" value=""> 
<input
	type="hidden" name="appointment_date"
	value="<%=request.getParameter("xml_appointment_date")%>"> <input
	type="hidden" name="appointment_no"
	value="<%=request.getParameter("appointment_no")%>"> <input
	type="hidden" name="status" value="<%=request.getParameter("status")%>">
<input type="hidden" name="start_time"
	value="<%=request.getParameter("start_time")%>"> <input
	type="hidden" name="displaymode" value="savebill"> <input
	type="hidden" name="demographic_dob" value="<%=demoDOB%>"> <input
	type="hidden" name="demographic_name" value="<%=demoname%>"> <input
	type="hidden" name="hin" value="<%=demoHIN%>"> <input
	type="hidden" name="ohip_version" value="V03G"> <input
	type="hidden" name="total" value="<%=BigTotal%>"> <input
	type="hidden" name="clinic_ref_code" value="<%=location1%>"> <input
	type="hidden" name="visitdate" value="<%=visitdate%>"> <input
	type="button" name="Submit" value="Confirm" onDblClick=""
	onClick="form.submit()"> <!--  <input type="button" name="Submit2" value="Edit" onclick="javascript:history.go(-1);return false;">
  --> <a
	href="billingOB.jsp?billForm=<%=request.getParameter("billForm")%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>&demographic_no=<%=request.getParameter("demographic_no")%>&user_no=<%=request.getParameter("user_no")%>&providerview=<%=request.getParameter("apptProvider_no")%>&apptProvider_no=<%=request.getParameter("apptProvider_no")%>&appointment_date=<%=request.getParameter("xml_appointment_date")%>&status=<%=request.getParameter("status")%>&start_time=<%=request.getParameter("start_time")%>&bNewForm=0">edit</a>
<% session.setAttribute("content", content); %>
</p>
<p></p>
<p>&nbsp;</p>
</form>
</body>

<% }

%>
</html>
