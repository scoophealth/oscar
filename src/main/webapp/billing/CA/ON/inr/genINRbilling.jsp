<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%
if(session.getAttribute("user") == null)    response.sendRedirect("../../../../logout.jsp");
String user_no="";
user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="../../../errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbINR.jspf"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.model.Billing"%>
<%@ page import="org.oscarehr.common.dao.BillingDao"%>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail"%>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao"%>
<%
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("HH:mm");
	BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
%>
<%
String temp="";
String clinic_no= request.getParameter("clinic_no");
String clinic_ref_code = request.getParameter("xml_location");
String creator = request.getParameter("curUser");
String updatedate = request.getParameter("curDate");
String verCode = request.getParameter("verCode");

String demono="",demo_name="",demo_dob="",demo_hin="", billinginr_no="", provider_no="";
    String provider_ohip_no="", provider_rma_no="", diagnostic_code="", service_desc="",service_code="", billing_amount="";
    String billing_unit="";
    int colorCount = 0;
       String color="";
  int Count1 = 0;

for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if( temp.indexOf("inrbilling")==-1 ) continue;
//////////////////////////////////////////////////////////////////////////////////////
// 	content+="<" +temp+ ">" +SxmlMisc.replaceHTMLContent(request.getParameter(temp))+ "</" +temp+ ">";
//////////////////////////////////////////////////////////////////////////////////////






     ResultSet rsdemo = null;
      rsdemo = apptMainBean.queryResults(temp.substring(10), "search_inrbilling_dt_billno");
      while(rsdemo.next()){

      demono = rsdemo.getString("demographic_no");
      demo_name = rsdemo.getString("demographic_name");
      demo_hin = rsdemo.getString("hin") + rsdemo.getString("ver");
      demo_dob = rsdemo.getString("year_of_birth") + rsdemo.getString("month_of_birth") + rsdemo.getString("date_of_birth");
      provider_no = rsdemo.getString("provider_no");
      provider_ohip_no = rsdemo.getString("provider_ohip_no");
      provider_rma_no = rsdemo.getString("provider_rma_no");
      diagnostic_code = rsdemo.getString("diagnostic_code");
      service_code = rsdemo.getString("service_code");
      service_desc = rsdemo.getString("service_desc");
      billing_amount = rsdemo.getString("billing_amount");
      billing_unit = rsdemo.getString("billing_unit");



	StringBuffer sotherBuffer = new StringBuffer(billing_amount);
	int f = billing_amount.indexOf('.');
	sotherBuffer.deleteCharAt(f);
	sotherBuffer.insert(f,"");
	billing_amount = sotherBuffer.toString();


	Billing b = new Billing();
	b.setClinicNo(Integer.parseInt(clinic_no));
	b.setDemographicNo(Integer.parseInt(demono));
	b.setProviderNo(provider_no);
	b.setAppointmentNo(0);
	b.setOrganizationSpecCode("V03");
	b.setDemographicName(demo_name);
	b.setHin(demo_hin);
	b.setUpdateDate(MyDateFormat.getSysDate(request.getParameter("curDate")));
	b.setUpdateTime(timeFormatter.parse("00:00"));
	b.setBillingDate(MyDateFormat.getSysDate(request.getParameter("xml_appointment_date")));
	b.setBillingTime(timeFormatter.parse("00:00"));
	b.setClinicRefCode(clinic_ref_code);
	b.setContent("");
	b.setTotal(billing_amount);
	b.setStatus("O");
	b.setDob(demo_dob);
	b.setVisitDate(MyDateFormat.getSysDate(request.getParameter("xml_appointment_date")));
	b.setVisitType("00");
	b.setProviderOhipNo(provider_ohip_no);
	b.setProviderRmaNo(provider_rma_no);
	b.setApptProviderNo("");
	b.setAsstProviderNo("");
	b.setCreator(creator);


	billingDao.persist(b);

	    String billNo = null;
	    String[] param4 = new String[2];
	    param4[0] = demono;
	    param4[1] = "0";
	    rsdemo = null;

	    rsdemo = apptMainBean.queryResults(param4, "search_billing_no_by_appt");
   while (rsdemo.next()) {
   billNo = rsdemo.getString("billing_no");
   }

   int recordAffected=0;
         String[] param3 = new String[3];
                 param3[0] = "A";
                 param3[1] = request.getParameter("xml_appointment_date");
                 param3[2] = temp.substring(10);
          recordAffected = apptMainBean.queryExecuteUpdate(param3,"update_inrbilling_dt_billno");


   int recordCount = 1;
       for (int i=0;i<recordCount;i++){
    	   BillingDetail bd = new BillingDetail();
    	   bd.setBillingNo(Integer.parseInt(billNo));
    	   bd.setServiceCode(service_code);
    	   bd.setServiceDesc(service_desc);
    	   bd.setBillingAmount(billing_amount);
    	   bd.setDiagnosticCode(diagnostic_code);
    	   bd.setAppointmentDate(MyDateFormat.getSysDate(request.getParameter("xml_appointment_date")));
    	   bd.setStatus("O");
    	   bd.setBillingUnit(billing_unit);
    	   billingDetailDao.persist(bd);
       }

	}
}
%>
<script LANGUAGE="JavaScript">
      self.close();
     // self.opener.refresh();
</script>
