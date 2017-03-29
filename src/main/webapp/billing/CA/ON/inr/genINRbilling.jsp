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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%
String user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*" errorPage="../../../errorpage.jsp"%>


<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />

<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.model.Demographic"%>
<%@ page import="org.oscarehr.common.model.Billing"%>
<%@ page import="org.oscarehr.common.dao.BillingDao"%>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail"%>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao"%>
<%@page import="org.oscarehr.billing.CA.model.BillingInr" %>
<%@page import="org.oscarehr.billing.CA.dao.BillingInrDao" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	BillingInrDao billingInrDao = SpringUtils.getBean(BillingInrDao.class);
%>
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





	List<Object[]> results = billingInrDao.search_inrbilling_dt_billno(Integer.parseInt(temp.substring(10)));

      for(Object[] result: results){
		BillingInr bi = (BillingInr)result[0];
		Demographic d = (Demographic)result[1];
    	  
      demono = String.valueOf(bi.getDemographicNo());
      demo_name = bi.getDemographicName();
      demo_hin = d.getHin() + d.getVer();
      demo_dob = d.getYearOfBirth() + d.getMonthOfBirth() + d.getDateOfBirth();
      provider_no = String.valueOf(bi.getProviderNo());
      provider_ohip_no = bi.getProviderOhipNo();
      provider_rma_no = bi.getProviderRmaNo();
      diagnostic_code = bi.getDiagnosticCode();
      service_code = bi.getServiceCode();
      service_desc = bi.getServiceDesc();
      billing_amount = bi.getBillingAmount();
      billing_unit = bi.getBillingUnit();



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
	    ResultSet rsdemo = null;

	    billNo = String.valueOf(billingDao.search_billing_no_by_appt(Integer.parseInt(demono),0));
	    
   int recordAffected=0;
  
          bi = billingInrDao.find(Integer.parseInt(temp.substring(10)));
          if(bi != null && !bi.getStatus().equals("D")) {
				bi.setStatus("A");
				bi.setCreateDateTime(ConversionUtils.fromDateString(request.getParameter("xml_appointment_date")));
				billingInrDao.merge(bi);
				recordAffected++;
			}
          

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
