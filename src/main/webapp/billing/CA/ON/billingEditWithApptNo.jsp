<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.common.dao.CtlBillingServiceDao"%>
<%@page import="org.oscarehr.common.model.BillingONItem"%>
<%@page import="org.oscarehr.common.dao.BillingONItemDao"%>
<%@page import="java.util.*, java.text.*,java.sql.*, java.net.*, oscar.*, oscar.util.*, org.oscarehr.provider.model.PreventionManager" %>
<%@page import="oscar.oscarBilling.ca.on.data.*"%>
<%@page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.common.dao.ProviderPreferenceDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%
	String prov = request.getParameter("billRegion");
	String billForm = request.getParameter("billForm");
	String hotclick = request.getParameter("hotclick");
	String appointment_no = request.getParameter("appointment_no");
	String name = request.getParameter("demographic_name");
	String status = request.getParameter("status");
	String demographic_no = request.getParameter("demographic_no");
	String providerview = request.getParameter("providerview");
	String curUser_no = request.getParameter("user_no");
	String apptProvider_no = request.getParameter("apptProvider_no");
	String appointment_date = request.getParameter("appointment_date");
	String start_time = request.getParameter("start_time");
	
	
    oscar.OscarProperties oscarVariables = oscar.OscarProperties.getInstance();
           
	String user_no = (String) session.getAttribute("user");

	String provider_no;
	if( apptProvider_no.equalsIgnoreCase("none") ) {
		provider_no = user_no;
    } else {
    	provider_no = apptProvider_no;
    }
	
	ProviderPreferenceDao preferenceDao = (ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao");	
	ProviderPreference preference = null;	
	preference=preferenceDao.find(provider_no);
	
	String xml_serviceCode = "";
	String billNo = "";
	String xml_vdate="";
	String xml_visittype = "";
	String xml_location = "";
	String billing_date = "";
	String clinicNo = "";
	String asstProvider_no = "";
	String assgProvider_no= "";
	String m_review = "";
	String curBillForm = ""; //????
	String xml_provider = "";
	String referralCode = "";
	String site = "";
	String xml_billtype = "";	
	String demoHIN = "";
	String demoHCTYPE = "";
	String demoVer = "";
	String demoDOB = "";
	
	
	String dxCode = "";
	String dxCode1 = "";
	String dxCode2 = "";
	String service_date = "";
	String service_code = "";
	String demoname = "";
	
	// get patient's billing history
	boolean bFirst = true;
	JdbcBillingReviewImpl hdbObj = new JdbcBillingReviewImpl();
	List aL = hdbObj.getBillingByApptNo(appointment_no);
	
	/////////////////////////////////////
	//sql = "select id,billing_date,admission_date,visitType, timestamp, facilty_num, ref_num from billing_on_cheader1 "+ " where demographic_no="+ demo_no	+ " and status!='D' order by billing_date desc, id desc limit 1";
	//rs = dbObj.searchDBRecord(sql);
	Properties propHist = null;
	List<BillingONItem> rs2 = new ArrayList<BillingONItem>();
	int size =0; 
	

	if (aL.size()>0) {					
		BillingClaimHeader1Data obj= (BillingClaimHeader1Data) aL.get(0);
		BillingItemData iobj = (BillingItemData) aL.get(1);
		propHist = new Properties();
		
		billNo = obj.getId();
		status = obj.getStatus();
		xml_vdate = obj.getAdmission_date(); // admission date
		billing_date = obj.getBilling_date();
		//propHist.setProperty("update_date", rs.getString("timestamp")); // create date
		xml_visittype = obj.getVisittype();
		xml_location =  obj.getFacilty_num();
		clinicNo = obj.getClinic();
		asstProvider_no = obj.getAsstProvider_no();
		assgProvider_no = obj.getAsstProvider_no();
		m_review = obj.getMan_review();
		xml_provider = obj.getProviderNo();
		
		referralCode = obj.getRef_num();
		if(referralCode == null)  referralCode="";
		
		site = obj.getClinic();
		xml_billtype = obj.getPay_program();
		//providerview = obj.getProviderNo();
		
		demoHIN = obj.getHin();
		demoVer = obj.getVer();
		demoHCTYPE = obj.getProvince();
		demoDOB = obj.getDob();
		demoname = obj.getDemographic_name();
		
		service_date = iobj.getService_date();
		service_code = iobj.getService_code();
		
		dxCode = iobj.getDx();
		dxCode1 = iobj.getDx1();
		dxCode2 = iobj.getDx2();
		
		BillingONItemDao dao = SpringUtils.getBean(BillingONItemDao.class);
		rs2 = dao.getActiveBillingItemByCh1Id(ConversionUtils.fromIntString(billNo));
	}
	
		String action_str =	"../../../billing.do?billRegion="+URLEncoder.encode(prov)+"&billForm="+URLEncoder.encode(billForm)+"&hotclick="+URLEncoder.encode(hotclick)+"&appointment_no="+appointment_no+"&demographic_name="+URLEncoder.encode(name)+"&status="+status+"&demographic_no="+demographic_no+"&providerview="+providerview+"&user_no="+curUser_no+"&apptProvider_no="+apptProvider_no+"&appointment_date="+appointment_date+"&start_time="+start_time+"&bNewForm=1";

		if (status.substring(0,1).compareTo("B") == 0) {
		   %>
		<p>
		<h1>Sorry, cannot delete billed items.</h1>
		</p>
		<form><input type="button" value="Back to previous page"
			onClick="window.close()"></form>
		<% 
		} else {
		%>


 
<form method="post" name="editBillingForm" action="billingON.jsp" >
	<input type="hidden" name="billNo_old" id="billNo_old" value="<%=billNo%>" />	
	<input type="hidden" name="billStatus_old" id="billStatus_old" value="<%=status%>" />		
	<input type="hidden" name="apptProvider_no" id="apptProvider_no" value="<%=apptProvider_no%>" />				
	<input type="hidden" name="providerview" id="providerview" value="<%=providerview%>"/>
	<input type="hidden" name="service_date" id="service_date" value="<%=service_date%>" />
	<input type="hidden" name="appointment_date" id="appointment_date" value="<%=appointment_date%>"/>
	<input type="hidden" name="billing_date" id="billing_date" value="<%=billing_date%>" />
    <input type="hidden" name="demographic_name" id="demographic_name" value="<%=demoname%>" />
    <input type="hidden" name="appointment_no" id="appointment_no" value="<%=appointment_no %>"/>    
    <input type="hidden" name="clinic_no" id="clinic_no" value="<%=clinicNo%>" />
    <input type="hidden" name="demographic_no" id="demographic_no" value="<%=demographic_no%>" />
	<input type="hidden" name="asstProvider_no" id="asstProvider_no" value="<%=asstProvider_no%>" />    
    <input type="hidden" name="assgProvider_no" id="assgProvider_no" value="<%=assgProvider_no%>" />      
    <input type="hidden" name="sex" id="sex"/>
    <input type="hidden" name="m_review" id="m_review" value="<%=m_review%>" />
    <input type="hidden" name="xml_provider" id="xml_provider" value="<%=xml_provider%>" />
    <input type="hidden" name="dxCode" id="dxCode" value="<%=dxCode%>" />
    <input type="hidden" name="dxCode1" id="dxCode1" value="<%=dxCode1%>" />
    <input type="hidden" name="dxCode2" id="dxCode2" value="<%=dxCode2%>" />
     <input type="hidden" name="service_code" id="service_code" value="<%=service_code%>"/>
    <input type="hidden" name="xml_visittype" id="xml_visittype" value="<%=xml_visittype%>" />
    <input type="hidden" name="xml_location" id="xml_location" value="<%=xml_location%>" />
   <input type="hidden" name="xml_vdate" id="xml_vdate" value="<%=xml_vdate%>" />   
   

  	<input type="hidden" name="checkFlag" id="checkFlag" />
   	<input type="hidden" name="rfcheck" id="rfcheck" />
	<input type="hidden" name="referralDocName" id="referralDocName" />
	<input type="hidden" name="referralCode" id="referralCode " value="<%=referralCode%>" />
	<input type="hidden" name="referralSpet" id="referralSpet" />
   	<input type="hidden" name="site" id="site" value="<%=site %>" />
	<input type="hidden" name="xml_billtype" id="xml_billtype" value="<%=xml_billtype %>" />   
    <input type="hidden" name="ohip_version" id="ohip_version" value="V03G" />
    <input type="hidden" name="hin" id="hin" value="<%=demoHIN%>" />
    <input type="hidden" name="ver" id="ver" value="<%=demoVer%>" />
    <input type="hidden" name="hc_type" id="hc_type" value="<%=demoHCTYPE%>" />    
    <input type="hidden" name="start_time" id="start_time" value="<%=start_time%>" />
    <input type="hidden" name="demographic_dob" id="demographic_dob" value="<%=demoDOB%>" />       
    <input type="hidden" name="url_back" id="url_back">

<%	
	int serviceN = 0;
	int services_checked_num = 0;
	String service_code_="", service_code_num_="",serviceCode_="", serviceUnit_="";
	
	CtlBillingServiceDao cBillDao = SpringUtils.getBean(CtlBillingServiceDao.class);
	for(BillingONItem b : rs2) {
		service_code_ = b.getServiceCode();
		service_code_num_ = b.getServiceCount();
		
		if("1".equals(service_code_num_)) {	
			for(Object[] svc : cBillDao.findUniqueServiceTypesByCode(service_code)) {
				curBillForm = String.valueOf(svc[1]); // servicetype
				billForm = curBillForm;
				break;
			}
			
			xml_serviceCode = "xml_".concat(service_code_);
			services_checked_num ++;
%>
 			<input type="hidden" name="<%=xml_serviceCode %>" id="<%=xml_serviceCode %>" value="checked" />   			
<%
		} else {
			serviceCode_ = "serviceCode".concat(String.valueOf(serviceN));
			serviceUnit_ = "serviceUnit".concat(String.valueOf(serviceN));
			serviceN ++;
%>
		 
			<input type="hidden" name="<%=serviceCode_ %>" id="<%=serviceCode_ %>" value="<%=service_code_%>" />  
			<input type="hidden" name="<%=serviceUnit_ %>" id="<%=serviceUnit_ %>" value="<%=service_code_num_%>" />  
			

<%
		}
	}
%>
	<input type="hidden" name="services_checked" id="services_checked" value=<%=services_checked_num %>>	
<%

  }	
%>
 <input type="hidden" name="curBillForm" id="curBillForm" value="<%=curBillForm%>" /> 
 <input type="hidden" name="billForm" id="billForm" value="<%=billForm%>" />	
<center>
<p>
Do you want to edit the billing?
<p>
<input type="submit" name="submit2" value="Yes" />
<input type="button" name="close" value="No" onclick="window.close()" />
</center>
</form>
<SCRIPT LANGUAGE="JavaScript"><!--
setTimeout('document.editBillingForm.submit()',50);
//--></SCRIPT>
