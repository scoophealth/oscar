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
<%@page import="oscar.oscarRx.data.RxPatientData"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="oscar.oscarProvider.data.ProSignatureData, oscar.oscarProvider.data.ProviderData"%>
<%@ page import="oscar.log.*,oscar.oscarRx.data.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.*" %>
<%@page import="oscar.dms.EDocUtil"%>

<%@ page import="oscar.*,java.lang.*,java.util.Date,java.text.SimpleDateFormat,oscar.oscarRx.util.RxUtil,org.springframework.web.context.WebApplicationContext,
         org.springframework.web.context.support.WebApplicationContextUtils,
         org.oscarehr.common.dao.UserPropertyDAO,org.oscarehr.common.model.UserProperty"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>

<!-- Classes needed for signature injection -->
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.managers.DemographicManager"%>
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%>
<!-- end -->
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo=loggedInInfo.getLoggedInProviderNo();
	String scriptid=request.getParameter("scriptId");
	String rx_enhance = OscarProperties.getInstance().getProperty("rx_enhance");
	boolean rxWaterMark = OscarProperties.getInstance().getBooleanProperty("enable_rx_watermark", "true");
	String rx_watermark_file = OscarProperties.getInstance().getProperty("rx_watermark_file_name");
%>	

<%@page import="org.oscarehr.web.PrescriptionQrCodeUIBean"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"/></script>
<title><bean:message key="RxPreview.title"/></title>
<style type="text/css" media="print">
 .noprint {
	 display: none;
 }
 </style>
<html:base />

<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
		name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>

<link rel="stylesheet" type="text/css" href="styles.css">
<script type="text/javascript" language="Javascript">
	

    function onPrint2(method) {

            document.getElementById("preview2Form").action = "../form/createcustomedpdf?__title=Rx&__method=" + method;
            document.getElementById("preview2Form").target="_blank";
            document.getElementById("preview2Form").submit();
       return true;
    }
    <%if (rxWaterMark){ %>
    function scaleWaterMark() {
    	document.getElementById("watermark").style.maxHeight 
    		= document.getElementsByTagName("table")[0].getHeight() + "px";
    }
    <%}%>
</script>

</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF" 
<%if (rxWaterMark){ %>
onload="scaleWaterMark();"
<%} %>
>
<%
Date rxDate = oscar.oscarRx.util.RxUtil.Today();
//String rePrint = request.getParameter("rePrint");
String rePrint = (String)request.getSession().getAttribute("rePrint");
//String rePrint = (String)request.getSession().getAttribute("rePrint");
oscar.oscarRx.pageUtil.RxSessionBean bean;
oscar.oscarRx.data.RxProviderData.Provider provider;
String signingProvider;
if( rePrint != null && rePrint.equalsIgnoreCase("true") ) {
    bean = (oscar.oscarRx.pageUtil.RxSessionBean)session.getAttribute("tmpBeanRX");
    signingProvider = bean.getStashItem(0).getProviderNo();
    rxDate = bean.getStashItem(0).getRxDate();
    provider = new oscar.oscarRx.data.RxProviderData().getProvider(signingProvider);
//    session.setAttribute("tmpBeanRX", null);
    String ip = request.getRemoteAddr();
    //LogAction.addLog((String) session.getAttribute("user"), LogConst.UPDATE, LogConst.CON_PRESCRIPTION, String.valueOf(bean.getDemographicNo()), ip);
}
else {
    bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");

    //set Date to latest in stash
    Date tmp;

    for( int idx = 0; idx < bean.getStashSize(); ++idx ) {
        tmp = bean.getStashItem(idx).getRxDate();
        if( tmp.after(rxDate) ) {
            rxDate = tmp;
        }
    }
    rePrint = "";
    signingProvider = bean.getProviderNo();
    provider = new oscar.oscarRx.data.RxProviderData().getProvider(bean.getProviderNo());
}


oscar.oscarRx.data.RxPatientData.Patient patient = RxPatientData.getPatient(loggedInInfo, bean.getDemographicNo());
String patientAddress = patient.getAddress()==null ? "" : patient.getAddress();
String patientCity = patient.getCity()==null ? "" : patient.getCity();
String patientProvince = patient.getProvince()==null ? "" : patient.getProvince();
String patientPostal = patient.getPostal()==null ? "" : patient.getPostal();
String patientPhone = patient.getPhone()==null ? "" : patient.getPhone();
String patientHin = patient.getHin()==null ? "" : patient.getHin();

DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);

oscar.oscarRx.data.RxPrescriptionData.Prescription rx = null;
int i;
ProSignatureData sig = new ProSignatureData();
boolean hasSig = sig.hasSignature(signingProvider);
String doctorName = "";
if (hasSig){
   doctorName = sig.getSignature(signingProvider);
}else{
   doctorName = (provider.getFirstName() + ' ' + provider.getSurname());
}

//doctorName = doctorName.replaceAll("\\d{6}","");
//doctorName = doctorName.replaceAll("\\-","");

if ( "true".equalsIgnoreCase(OscarProperties.getInstance().getProperty("FIRST_NATIONS_MODULE") ) ) {
	// Addition of First Nations Band Number to prescriptions
	DemographicExt demographicExtStatusNum = demographicManager.getDemographicExt(loggedInInfo, bean.getDemographicNo() , "statusNum" );
	DemographicExt demographicExtBandName = null;
	DemographicExt demographicExtBandFamily = null;	
	DemographicExt demographicExtBandFamilyPosition = null;
	String bandNumber = "";
	String bandName = "";
	String bandFamily = "";
	String bandFamilyPosition = "";
	
	if( demographicExtStatusNum != null ) {
		bandNumber = demographicExtStatusNum.getValue();
	}
	
	if(bandNumber == null) {
		bandNumber = "";
	}
	
	// if band number is empty try the alternate composite.
	if( bandNumber.isEmpty() ) {
		
		demographicExtBandName = demographicManager.getDemographicExt(loggedInInfo, bean.getDemographicNo(), "fNationCom");
		demographicExtBandFamily = demographicManager.getDemographicExt(loggedInInfo, bean.getDemographicNo(), "fNationFamilyNumber");	
		demographicExtBandFamilyPosition = demographicManager.getDemographicExt(loggedInInfo, bean.getDemographicNo(), "fNationFamilyPosition");
		
		if(demographicExtBandName != null ) {
			bandName = demographicExtBandName.getValue();
		}
		
		if(demographicExtBandFamily != null  ) {
			bandFamily = demographicExtBandFamily.getValue();
		}
		
		if(demographicExtBandFamilyPosition != null ) {
			bandFamilyPosition = demographicExtBandFamilyPosition.getValue();
		}
		
		if(bandName == null) {
			bandName = "";
		}
		
		if(bandFamily == null) {
			bandFamily = "";
		}
		
		if(bandFamilyPosition == null) {
			bandFamilyPosition = "";
		}
		
		StringBuilder bandNumberString = new StringBuilder(); 
		
		if( ! bandName.isEmpty() ) {
			bandNumberString.append(bandName);
		}
		
		if( ! bandFamily.isEmpty() ) {
			bandNumberString.append("-" + bandFamily);
		}
		
		if( ! bandFamilyPosition.isEmpty() ) {
			bandNumberString.append("-" + bandFamilyPosition);
		}
		
		bandNumber = bandNumberString.toString();
	}
	
	pageContext.setAttribute("bandNumber", bandNumber);
}

OscarProperties props = OscarProperties.getInstance();

String pracNo = provider.getPractitionerNo();
String strUser = (String)session.getAttribute("user");
ProviderData user = new ProviderData(strUser);
String pharmaFax = "";
String pharmaFax2 = "";
String pharmaName = "";
String pharmaTel="";
String pharmaAddress1="";
String pharmaAddress2="";
String pharmaEmail="";
String pharmaNote="";
RxPharmacyData pharmacyData = new RxPharmacyData();
PharmacyInfo pharmacy;
String pharmacyId = request.getParameter("pharmacyId");

if (pharmacyId != null && !"null".equalsIgnoreCase(pharmacyId)) {
    pharmacy = pharmacyData.getPharmacy(pharmacyId);
    if( pharmacy != null ) {
		pharmaFax = pharmacy.getFax();
		pharmaFax2 = "<bean:message key='RxPreview.msgFax'/>"+": " + pharmacy.getFax();
		pharmaName = pharmacy.getName();
		pharmaTel = pharmacy.getPhone1() + ((pharmacy.getPhone2()!=null && !pharmacy.getPhone2().isEmpty())? "," + pharmacy.getPhone2():"");
		pharmaAddress1 = pharmacy.getAddress();
		pharmaAddress2 = pharmacy.getCity() + ", " + pharmacy.getProvince() + " " +pharmacy.getPostalCode();
		pharmaEmail = pharmacy.getEmail();
		pharmaNote = pharmacy.getNotes();
    }
}

String patientDOBStr=RxUtil.DateToString(patient.getDOB(), "MMM d, yyyy") ;
boolean showPatientDOB=false;

//check if user prefer to show dob in print
WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
UserPropertyDAO userPropertyDAO = (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
UserProperty prop = userPropertyDAO.getProp(signingProvider, UserProperty.RX_SHOW_PATIENT_DOB);
if(prop!=null && prop.getValue().equalsIgnoreCase("yes")){
    showPatientDOB=true;
}
%>
<html:form action="/form/formname" styleId="preview2Form">
	<%if (rxWaterMark){ %>
	<div style="position: relative;">
	    <% if(rx_watermark_file !=null) { %>
	    	<img id="watermark" src="../images/<%=rx_watermark_file %>" style="z-index:-100; position: absolute; display: block; max-height: 500px;">
	    <%} else { %>
			<img id="watermark" src="../images/watermark.png" style="z-index:-100; position: absolute; display: block; max-height: 500px;">
		<%} %>
	</div>
	<%} %>
	<input type="hidden" name="demographic_no" value="<%=bean.getDemographicNo()%>"/>
    <table>
        <tr>
            <td>
                            <table id="pwTable" width="400px" height="500px" cellspacing=0 cellpadding=10 border=2>
                                    <tr>
                                            <td valign=top height="100px"><input type="image"
                                                    src="img/rx.gif" border="0" alt="[Submit]"
                                                    name="submit" title="Print in a half letter size paper"
                                                    onclick="<%=rePrint.equalsIgnoreCase("true") ? "javascript:return onPrint2('rePrint');" : "javascript:return onPrint2('print');"  %>"/>
                                            <!--input type="hidden" name="printPageSize" value="PageSize.A6" /--> <% 	
                                            String clinicTitle = provider.getClinicName().replaceAll("\\(\\d{6}\\)","") + "<br>" ;
                                                    
                                            clinicTitle += provider.getClinicAddress() + "<br>" ;
                                            clinicTitle += provider.getClinicCity() + "   " + provider.getClinicPostal()  ;
                                            
                                            if (rx_enhance!=null && rx_enhance.equals("true")) {
                                            	
                                            	SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dd");
                                    			String patientDOB = patient.getDOB() == null ? "" : formatter.format(patient.getDOB());
                                    			
                                            	String docInfo = doctorName + "\n"+provider.getClinicName().replaceAll("\\(\\d{6}\\)","")
														+"<bean:message key='RxPreview.PractNo'/>"+ pracNo
														+ "\n" + provider.getClinicAddress() + "\n"
														+ provider.getClinicCity() + "   "
														+ provider.getClinicPostal() + "\n"
														+"<bean:message key='RxPreview.msgTel'/>"+": "
														+ provider.getClinicPhone() + "\n"
														+"<bean:message key='RxPreview.msgFax'/>"+": "
														+ provider.getClinicFax();
                                        
                                            	String patientInfo = patient.getFirstName() + " "
														+ patient.getSurname() + "\n"
														+ patientAddress + "\n"
														+ patientCity + "   "
														+ patientPostal + "\n"
														+ "<bean:message key='RxPreview.msgTel'/>"+": "+ patientPhone
														+ (patientDOB != null && !patientDOB.trim().equals("") ? "\n"
														+"<bean:message key='RxPreview.msgDOB'/>"+": "+ patientDOB : "") 
														+ (!patientHin.trim().equals("") ? "\n"+"<bean:message key='oscar.oscarRx.hin'/>"+": " + patientHin : "");
                                            }    
                                                    
                                            %> <input type="hidden" name="doctorName"
                                                    value="<%= StringEscapeUtils.escapeHtml(doctorName) %>" /> <c:choose>
                                                    <c:when test="${empty infirmaryView_programAddress}">
                                               <%
                                                	UserProperty phoneProp = userPropertyDAO.getProp(provider.getProviderNo(),"rxPhone");
                                                	
                                                
                                                	String finalPhone = provider.getClinicPhone();
                                                	
                                                	//if(providerPhone != null) {
                                                	//	finalPhone = providerPhone;
                                                	//}
                                                	if(phoneProp != null && phoneProp.getValue().length()>0) {                                                		
                                                		finalPhone = phoneProp.getValue();
                                                	}
                                                	
                                                	
                                                	
                                                	request.setAttribute("phone",finalPhone);
                                                
                                             	%>                     
                                                            <input type="hidden" name="clinicName"
                                                                    value="<%= StringEscapeUtils.escapeHtml(clinicTitle.replaceAll("(<br>)","\\\n")) %>" />
                                                            <input type="hidden" name="clinicPhone"
                                                                    value="<%= StringEscapeUtils.escapeHtml(finalPhone) %>" />
                                                            <input type="hidden" id="finalFax" name="clinicFax"
                                                                    value="" />
                                                    </c:when>
                                                    <c:otherwise>
                                               <%
                                                	UserProperty phoneProp = userPropertyDAO.getProp(provider.getProviderNo(),"rxPhone");
                                                	UserProperty faxProp = userPropertyDAO.getProp(provider.getProviderNo(),"faxnumber");
                                                
                                                	String finalPhone = (String)session.getAttribute("infirmaryView_programTel");
                                                	String finalFax =(String)session.getAttribute("infirmaryView_programFax");
                                                	
                                                	//if(providerPhone != null) {
                                                	//	finalPhone = providerPhone;
                                                	//}
                                                	if(phoneProp != null && phoneProp.getValue().length()>0) {                                                		
                                                		finalPhone = phoneProp.getValue();
                                                	}
                                                	
                                                	if(faxProp != null && faxProp.getValue().length()>0) {                                                		
                                                		finalFax = faxProp.getValue();
                                                	}
                                                	
                                                	request.setAttribute("phone",finalPhone);                                                                                                                
                                                
                                             	%>
                                                            <input type="hidden" name="clinicName"
                                                                    value="<c:out value="${infirmaryView_programAddress}"/>" />
                                                            <input type="hidden" name="clinicPhone"
                                                                    value="<%=finalPhone%>" />
                                                            <input type="hidden" id="finalFax" name="clinicFax"
                                                                    value="" />
                                                    </c:otherwise>
                                            </c:choose> 
                                            
                                                            <input type="hidden" name="patientName"
                                                    value="<%= StringEscapeUtils.escapeHtml(patient.getFirstName())+ " " +StringEscapeUtils.escapeHtml(patient.getSurname()) %>" />
                                            <input type="hidden" name="patientDOB" value="<%= StringEscapeUtils.escapeHtml(patientDOBStr) %>" />
                                            <input type="hidden" name="pharmaFax" value="<%=pharmaFax%>" />
                                            <input type="hidden" name="pharmaName" value="<%=pharmaName%>" />
                                            <input type="hidden" name="pharmaTel" value="<%=pharmaTel%>" />
                                            <input type="hidden" name="pharmaAddress1" value="<%=pharmaAddress1%>" />
                                            <input type="hidden" name="pharmaAddress2" value="<%=pharmaAddress2%>" />
                                            <input type="hidden" name="pharmaEmail" value="<%=pharmaEmail%>" />
                                            <input type="hidden" name="pharmaNote" value="<%=pharmaNote%>" />
                                            <input type="hidden" name="pharmaShow" id="pharmaShow" value="false" />
                                            <input type="hidden" name="pracNo" value="<%= StringEscapeUtils.escapeHtml(pracNo) %>" />
                                            <input type="hidden" name="showPatientDOB" value="<%=showPatientDOB%>"/>
                                            <input type="hidden" name="pdfId" id="pdfId" value="" />
                                            <input type="hidden" name="patientAddress" value="<%= StringEscapeUtils.escapeHtml(patientAddress) %>" />
                                            <%
											int check = (patientCity.trim().length()>0 ? 1 : 0) | (patientProvince.trim().length()>0 ? 2 : 0);  
											String patientCityPostal = String.format("%s%s%s %s",
											patientCity,
											check == 3 ? ", " : check == 2 ? "" : " ",
											patientProvince,
											patientPostal);
											
											String ptChartNo = ""; 
											if(props.getProperty("showRxChartNo", "").equalsIgnoreCase("true")) {
												ptChartNo = patient.getChartNo()==null ? "" : patient.getChartNo();
											} 
                                            %>
                                            <input type="hidden" name="patientCityPostal" value="<%= StringEscapeUtils.escapeHtml(patientCityPostal)%>" />
                                            <input type="hidden" name="patientHIN" value="<%= StringEscapeUtils.escapeHtml(patientHin) %>" />
                                            <input type="hidden" name="patientChartNo" value="<%=StringEscapeUtils.escapeHtml(ptChartNo)%>" />
                                            <input type="hidden" name="bandNumber" value="${ bandNumber }" />
                                            <input type="hidden" name="patientPhone"
                                                    value="<bean:message key="RxPreview.msgTel"/><%=StringEscapeUtils.escapeHtml(patientPhone) %>" />

                                            <input type="hidden" name="rxDate"
                                                    value="<%= StringEscapeUtils.escapeHtml(oscar.oscarRx.util.RxUtil.DateToString(rxDate, "MMMM d, yyyy")) %>" />
                                            <input type="hidden" name="sigDoctorName" value="<%= StringEscapeUtils.escapeHtml(doctorName) %>" /> <!--img src="img/rx.gif" border="0"-->
                                            </td>
                                            <td valign=top height="100px" id="clinicAddress"><b><%=doctorName%></b><br>
                                            <c:choose>
                                                    <c:when test="${empty infirmaryView_programAddress}">
                                                            <%= provider.getClinicName().replaceAll("\\(\\d{6}\\)","") %><br>
                                                            <%= provider.getClinicAddress() %><br>
                                                            <%= provider.getClinicCity() %>&nbsp;&nbsp;<%=provider.getClinicProvince()%>&nbsp;&nbsp;
                                                <%= provider.getClinicPostal() %>
                                                <% if(provider.getPractitionerNo() != null && !provider.getPractitionerNo().equals("")){ %><br><bean:message key="RxPreview.PractNo"/>:<%= provider.getPractitionerNo() %><% } %>
                                                <br>
                                               <%
                                                	UserProperty phoneProp = userPropertyDAO.getProp(provider.getProviderNo(),"rxPhone");
                                                	UserProperty faxProp = userPropertyDAO.getProp(provider.getProviderNo(),"faxnumber");
                                                
                                                	String finalPhone = provider.getClinicPhone();
                                                	String finalFax = provider.getClinicFax();
                                                	//if(providerPhone != null) {
                                                	//	finalPhone = providerPhone;
                                                	//}
                                                	if(phoneProp != null && phoneProp.getValue().length()>0) {                                                		
                                                		finalPhone = phoneProp.getValue();
                                                	}
                                                	
                                                	if(faxProp != null && faxProp.getValue().length()>0) {                                                		
                                                		finalFax = faxProp.getValue();
                                                	}
                                                	
                                                	request.setAttribute("phone",finalPhone);
                                                
                                             	%>                                                        
                                                <bean:message key="RxPreview.msgTel"/>: <%= finalPhone %><br>
                                                <oscar:oscarPropertiesCheck property="RXFAX" value="yes">
                                                    <bean:message key="RxPreview.msgFax"/>: <%= finalFax %><br>
                                                </oscar:oscarPropertiesCheck>
                                                    </c:when>
                                                    <c:otherwise>
                                               <%
                                                	UserProperty phoneProp = userPropertyDAO.getProp(provider.getProviderNo(),"rxPhone");
                                                	UserProperty faxProp = userPropertyDAO.getProp(provider.getProviderNo(),"faxnumber");
                                                
                                                	String finalPhone = (String)session.getAttribute("infirmaryView_programTel");
                                                	String finalFax =(String)session.getAttribute("infirmaryView_programFax");
                                                	
                                                	//if(providerPhone != null) {
                                                	//	finalPhone = providerPhone;
                                                	//}
                                                	if(phoneProp != null && phoneProp.getValue().length()>0) {                                                		
                                                		finalPhone = phoneProp.getValue();
                                                	}
                                                	
                                                	if(faxProp != null && faxProp.getValue().length()>0) {                                                		
                                                		finalFax = faxProp.getValue();
                                                	}
                                                	
                                                	request.setAttribute("phone",finalPhone);
                                                
                                             	%>                                                    
                                                            <c:out value="${infirmaryView_programAddress}" escapeXml="false" />
                                                            <br />
                                                    <bean:message key="RxPreview.msgTel"/>: <%=finalPhone %>
                                                            <br />
                                                            <oscar:oscarPropertiesCheck property="RXFAX" value="yes">
                                                        <bean:message key="RxPreview.msgFax"/>: <%=finalFax %>
                                                    </oscar:oscarPropertiesCheck>
                                                    </c:otherwise>
                                            </c:choose></td>
                                    </tr>
                                    <tr>
                                            <td colspan=2 valign=top height="75px">
                                            <table width=100% cellspacing=0 cellpadding=0>
                                                    <tr>
                                                            <td align=left valign=top><br>
                                                                <%= patient.getFirstName() %> <%= patient.getSurname() %> <%if(showPatientDOB){%>&nbsp;&nbsp; DOB:<%= StringEscapeUtils.escapeHtml(patientDOBStr) %> <%}%><br>

                                                            <%= patient.getAddress() %><br>
                                                            <%= patientPostal %><br>
                                                            <%= patient.getPhone() %>
                                                           <oscar:oscarPropertiesCheck value="true" property="showRxBandNumber">	
                                                            	<c:if test="${ not empty bandNumber }">
                                                            	<br />
	                                                            <b><bean:message key="oscar.oscarRx.bandNumber" /></b>	  
	                                                            	<c:out value="${ bandNumber }" />
                                                            	</c:if>
                                                            </oscar:oscarPropertiesCheck>
                                                            <% if(!props.getProperty("showRxHin", "").equals("false")) { %>
                                                            <br />
                      										<b><bean:message key="oscar.oscarRx.hin" /></b>                                                           
                                                            <%= patient.getHin() %>                                                             
                                                            <% } %>                                                                                                                        

                                                                <% if(props.getProperty("showRxChartNo", "").equalsIgnoreCase("true")) { %>
                                                            <bean:message key="oscar.oscarRx.chartNo" /><%=ptChartNo%>
                                                            <% } %>
                                                            
                                                            </td>
                                                            <td align=right valign=top><b> <%= oscar.oscarRx.util.RxUtil.DateToString(rxDate, "MMMM d, yyyy",request.getLocale()) %>
                                                            </b></td>
                                                    </tr>
                                            </table>
                                            </td>
                                    </tr>
                                    <tr>
                                            <td colspan=2 valign=top height="275px">
                                            <table height=100% width=100%>
                                                    <tr valign=top>
                                                            <td colspan=2 height=225px>
                                                            <%
                                            String strRx = "";
                                            StringBuffer strRxNoNewLines = new StringBuffer();
                                            for(i=0;i<bean.getStashSize();i++)
                                            {
                                            rx = bean.getStashItem(i);
                                                                    String fullOutLine=rx.getFullOutLine().replaceAll(";","<br />");

                                                                    if (fullOutLine==null || fullOutLine.length()<=6)
                                                                    {
                                                                            Logger.getLogger("preview_jsp").error("drug full outline was null");
                                                                            fullOutLine="<span style=\"color:red;font-size:16;font-weight:bold\">An error occurred, please write a new prescription.</span><br />"+fullOutLine;
                                                                    }
                                            %>
                                            <%=fullOutLine%>
                                                            <hr>
                                                            <%
                                            strRx += rx.getFullOutLine() + ";;";
                                            strRxNoNewLines.append(rx.getFullOutLine().replaceAll(";"," ")+ "\n");
                                            }
                                            %> <input type="hidden" name="rx"
                                                                    value="<%= StringEscapeUtils.escapeHtml(strRx.replaceAll(";","\\\n")) %>" />
                                                            <input type="hidden" name="rx_no_newlines"
                                                                    value="<%= strRxNoNewLines.toString() %>" />
                                                            <input type="hidden" name="additNotes" value=""/>
                                                                    </td>
                                                             
                                                    </tr>

                                                    <tr valign="bottom">
                                                            <td colspan="2" id="additNotes">
                                                            
                                                            </td>
                                                            
                                                    </tr>
                                                    <tr valign="bottom">
														<td colspan="2" id="pharmInfo">
														</td>
													</tr>


                                                    <% if ( oscar.OscarProperties.getInstance().getProperty("RX_FOOTER") != null ){ out.write(oscar.OscarProperties.getInstance().getProperty("RX_FOOTER")); }%>
 

                                                    <tr valign=bottom>
                                                            <td height=25px width=25%><bean:message key="RxPreview.msgSignature"/>:</td>
                                                            <td height=25px width=75%
                                                                    style="border-width: 0; border-bottom-width: 1; border-style: solid;">
                                                                    <%
																	String signatureRequestId = null;	
																	String imageUrl=null;
																	String startimageUrl=null;
																	String statusUrl=null;

																	signatureRequestId=loggedInInfo.getLoggedInProviderNo();
																	imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_preview.name()+"&"+DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;
																	startimageUrl=request.getContextPath()+"/images/1x1.gif";		
																	statusUrl = request.getContextPath()+"/PMmodule/ClientManager/check_signature_status.jsp?" + DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;

																	DocumentDao docdao = (DocumentDao) ctx.getBean("documentDao");
																	boolean showwritesignature = true;
																	int new_doc_no = 0;
																	//Get the current user's private signature files.
																	List<Document> doc_list = docdao.findByDoctypeAndProviderNo("signature", provider.getProviderNo(), 0);	
																	String signature_imgpath = "";
																	if(doc_list.size() > 0){
																		showwritesignature = false;
																		new_doc_no = doc_list.get(0).getId().intValue();
																		Document d = docdao.findByDocumentNo(new_doc_no);
																		signature_imgpath = EDocUtil.getDocumentPath(d.getDocfilename());
																	}
																	%>
																	<input type="hidden" name="<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>" value="<%=signatureRequestId%>" />	
																	<%if(showwritesignature){%>
																	<img id="signature" style="width:300px; height:60px" src="<%=startimageUrl%>" alt="digital_signature" />
																	<%}else{%>
																	<img id="test" style="width:300px; height:60px" src="<%= request.getContextPath() + "/dms/ManageDocument.do?method=viewDocPage&doc_no=" + new_doc_no%>" />
				 													<%}%>
				 													<input type="hidden" name="imgFile" id="imgFile" value="" />
																	<script type="text/javascript">
																		
																		var POLL_TIME=2500;
																		var counter=0;

																		function refreshImage()
																			{
																			counter=counter+1;
																			var img=document.getElementById("signature");
																			img.src='<%=imageUrl%>&rand='+counter;

																			var request = dojo.io.bind({
																	                            url: '<%=statusUrl%>',
																	                            method: "post",
																	                            mimetype: "text/html",
																	                            load: function(type, data, evt){   
 																	                               	var x = data.trim();                                	
																	                                    //document.getElementById('signature_status').value=x;                                   
																	                            }
																	                    	});
						
																			}
																	</script>
                                                                    
                                                            &nbsp;</td>
                                                    </tr>
                                                    <tr valign=bottom>
                                                            <td height=25px>
                                                            <% if (props.getProperty("signature_tablet", "").equals("yes")) { %>
                                                            <input type="button" value=<bean:message key="RxPreview.digitallySign"/> class="noprint" onclick="setInterval('refreshImage()', POLL_TIME); document.location='<%=request.getContextPath()%>/signature_pad/topaz_signature_pad.jnlp.jsp?<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>'"  />
                                                            	<% } %>
                                                            </td>
                                                            <td height=25px>&nbsp; <%= doctorName%> <% if ( pracNo != null && ! pracNo.equals("") && !pracNo.equalsIgnoreCase("null")) { %>
                                                                <br /> &nbsp; <bean:message key="RxPreview.PractNo"/> <%= pracNo%> <% } %>                                                         
                                                            </td>
                                                    </tr>
                                                    <% 
                                                    	 if( rePrint.equalsIgnoreCase("true") && rx != null ) 
                                                    	 { 
                                                    	 %>
		                                                    <tr valign=bottom style="font-size: 6px;">
		                                                        <td height=25px colspan="2"><bean:message key="RxPreview.msgReprintBy"/> <%=ProviderData.getProviderName(strUser)%><span style="float: left;">
		                                                            <bean:message key="RxPreview.msgOrigPrinted"/>:&nbsp;<%=rx.getPrintDate()%></span> <span
		                                                                    style="float: right;"><bean:message key="RxPreview.msgTimesPrinted"/>:&nbsp;<%=String.valueOf(rx.getNumPrints())%></span>
		                                                            <input type="hidden" name="origPrintDate" value="<%=rx.getPrintDate()%>"/>
		                                                            <input type="hidden" name="numPrints" value="<%=String.valueOf(rx.getNumPrints())%>"/>
		                                                        </td>
		                                                    </tr>
	                                                    <%
                                     					}
                                                    
                                                    	if (PrescriptionQrCodeUIBean.isPrescriptionQrCodeEnabledForProvider(providerNo))
                                                    	{
                                                    	%>                                                    
		                                                    <tr>
			                                                    <td colspan="2">
			                                                    	<img src="<%=request.getContextPath()%>/contentRenderingServlet/prescription_qr_code_<%=rx.getScript_no()%>.png?source=prescriptionQrCode&prescriptionId=<%=rx.getScript_no()%>" alt="qr_code" />
			                                                    </td>
		                                                    </tr>
                                                    	<%
                                                    	}
                                                    	
                                                    	
	                                     				if (oscar.OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null)
	                                     				{
	                                     				%>
		                                                    <tr valign=bottom align="center" style="font-size: 9px">
		                                                            <td height=25px colspan="2"></br>
		                                                            <%= oscar.OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") %>
		                                                            </td>
		                                                    </tr>
	                                                    <%
                                                    	}
                                                    %>
                                            </table>
                                            </td>
                                    </tr>
                            </table>
			</td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
