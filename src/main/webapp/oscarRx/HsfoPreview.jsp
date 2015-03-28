<%--

    Copyright (C) 2007  Heart & Stroke Foundation
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="oscar.oscarProvider.data.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="oscar.*,java.lang.*"%>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Print Preview</title>
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
    function onPrint() {
        //document.forms[0].submit.value="print";
        //var ret = checkAllDates();
        //if(ret==true) {
        	// params are set in session at the page bottom
            document.forms[0].action = "../form/createpdf?__title=<%= oscar.form.pdfservlet.FrmPDFServlet.HSFO_RX_DATA_KEY %>";
            document.forms[0].target="_blank";
        //}
       return ret;
    }
</script>

</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<%

oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
oscar.oscarRx.data.RxPatientData.Patient patient = RxPatientData.getPatient(LoggedInInfo.getLoggedInInfoFromSession(request), bean.getDemographicNo());
oscar.oscarRx.data.RxProviderData.Provider provider = new oscar.oscarRx.data.RxProviderData().getProvider(bean.getProviderNo());
oscar.oscarRx.data.RxPrescriptionData.Prescription rx;
int i;
ProSignatureData sig = new ProSignatureData();
boolean hasSig = sig.hasSignature(bean.getProviderNo());
String doctorName = "";
if (hasSig){
   doctorName = sig.getSignature(bean.getProviderNo());
}else{
   doctorName = (provider.getFirstName() + ' ' + provider.getSurname());
}

doctorName = doctorName.replaceAll("\\d{6}","");
doctorName = doctorName.replaceAll("\\-","");
OscarProperties props = OscarProperties.getInstance();
%>
<html:form action="/form/formname">

	<table width="400px" height="500px" cellspacing=0 cellpadding=8
		border=0 style="border: 2px ridge;">
		<tr>
			<td valign=top height="100px"><input type="image"
				src="img/rx.gif" border="0" value="submit" alt="[Submit]"
				name="submit" title="Print in a half letter size paper"
				onclick="javascript:return onPrint();"> <input type="hidden"
				name="printPageSize" value="PageSize.A6" /> <% 	String clinicTitle = provider.getClinicName().replaceAll("\\(\\d{6}\\)","") + "<br>" ;
			 	clinicTitle += provider.getClinicAddress() + "<br>" ;
			 	clinicTitle += provider.getClinicCity() + "   " + provider.getClinicPostal()  ;
			%> <input type="hidden" name="doctorName"
				value="<%= StringEscapeUtils.escapeHtml(doctorName) %>" /> <c:choose>
				<c:when test="${empty infirmaryView_programAddress}">
					<input type="hidden" name="clinicName"
						value="<%= StringEscapeUtils.escapeHtml(clinicTitle.replaceAll("(<br>)","\\\n")) %>" />
					<input type="hidden" name="clinicPhone"
						value="<%= StringEscapeUtils.escapeHtml(provider.getClinicPhone()) %>" />
					<input type="hidden" name="clinicFax"
						value="<%= StringEscapeUtils.escapeHtml(provider.getClinicFax()) %>" />
				</c:when>
				<c:otherwise>
					<input type="hidden" name="clinicName"
						value="<c:out value="${infirmaryView_programAddress}"/>" />
					<input type="hidden" name="clinicPhone"
						value="<c:out value="${infirmaryView_programTel}"/>" />
					<input type="hidden" name="clinicFax"
						value="<c:out value="${infirmaryView_programFax}"/>" />
				</c:otherwise>
			</c:choose> <input type="hidden" name="patientName"
				value="<%= StringEscapeUtils.escapeHtml(patient.getFirstName())+ " " +StringEscapeUtils.escapeHtml(patient.getSurname()) %>" />
			<input type="hidden" name="patientAddress"
				value="<%= StringEscapeUtils.escapeHtml(patient.getAddress()) %>" />
			<input type="hidden" name="patientCityPostal"
				value="<%= StringEscapeUtils.escapeHtml(patient.getCity())+ " " + StringEscapeUtils.escapeHtml(patient.getPostal())%>" />
			<input type="hidden" name="patientPhone"
				value="<%= "Tel: " + StringEscapeUtils.escapeHtml(patient.getPhone()) %>" />

			<input type="hidden" name="rxDate"
				value="<%= StringEscapeUtils.escapeHtml(oscar.oscarRx.util.RxUtil.DateToString(oscar.oscarRx.util.RxUtil.Today(), "MMMM d, yyyy")) %>" />
			<input type="hidden" name="sigDoctorName"
				value="<%= StringEscapeUtils.escapeHtml(doctorName) %>" /> <!--img src="img/rx.gif" border="0"-->
			</td>
			<td valign=top height="100px" id="clinicAddress"><span
				style="font: 12pt; font-weight: bold;">HSFO</span> <br>
			<c:choose>
				<c:when test="${empty infirmaryView_programAddress}">
					<%= provider.getClinicName().replaceAll("\\(\\d{6}\\)","") %><br>
					<%= provider.getClinicAddress() %><br>
					<%= provider.getClinicCity() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        		    <%= provider.getClinicPostal() %><br>
		            Tel: <%= provider.getClinicPhone() %><br>
        		    Fax: <%= provider.getClinicFax() %><br>
				</c:when>
				<c:otherwise>
					<c:out value="${infirmaryView_programAddress}" escapeXml="false" />
					<br />
        			Tel: <c:out value="${infirmaryView_programTel}" />
					<br />
        			Fax: <c:out value="${infirmaryView_programFax}" />
				</c:otherwise>
			</c:choose></td>
		</tr>
		<tr>
			<td colspan=2 valign=top height="75px">
			<table width=100% cellspacing=0 cellpadding=0>
				<tr>
					<td align=left valign=top><br>
					<%= patient.getFirstName() %> <%= patient.getSurname() %><br>
					<%= patient.getAddress() %><br>
					<%= patient.getCity() %> <%= patient.getPostal() %><br>
					<%= patient.getPhone() %><br>
					<b> <% if(!props.getProperty("showRxHin", "").equals("false")) { %>
					<bean:message key="oscar.oscarRx.hin" /><%= patient.getHin() %> <% } %>
					</b></td>
					<td align=right valign=top><b> <%= oscar.oscarRx.util.RxUtil.DateToString(oscar.oscarRx.util.RxUtil.Today(), "MMMM d, yyyy") %>
					</b></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td colspan=2 valign=top height="275px">
			<table height=100% width=100%>
				<tr valign=top>
					<td colspan=2 height=180px>
					<% 
                        int dx = Integer.parseInt(request.getParameter("dxCode"), 10);
                        // persist dx code to DB
                        oscar.form.study.HSFO.HSFODAO hsfoDAO = new oscar.form.study.HSFO.HSFODAO();
                        hsfoDAO.updatePatientDx(String.valueOf(bean.getDemographicNo()),dx);
                        
            			java.util.HashMap params = new java.util.HashMap();
            			params.put("clinicName", provider.getClinicName().replaceAll("\\(\\d{6}\\)",""));
            			params.put("clinicAddress", provider.getClinicAddress());
            			params.put("clinicCity", provider.getClinicCity());
            			params.put("clinicZip", provider.getClinicPostal());
            			params.put("clinicPhone", provider.getClinicPhone());
            			params.put("clinicFax", provider.getClinicFax());
            			params.put("rxDate", oscar.oscarRx.util.RxUtil.Today());
            			params.put("patientName", patient.getFirstName()+" "+patient.getSurname());
            			params.put("patientAddress", patient.getAddress());
            			params.put("patientCity", patient.getCity());
            			params.put("patientZip", patient.getPostal());
            			params.put("patientPhone", patient.getPhone());
            			params.put("patientHealthInsNo", patient.getHin());
            			params.put("doctorName", doctorName);
            			params.put("dxCode", dx);
            			
           				oscar.form.pdfservlet.HsfoRxDataHolder rdh = new oscar.form.pdfservlet.HsfoRxDataHolder();
            			rdh.setParams(params);
            			rdh.setOutlines(new java.util.ArrayList());
            			
                        String strRx = "";
                        StringBuffer strRxNoNewLines = new StringBuffer();
                        for(i=0;i<bean.getStashSize();i++)
                        {
                        rx = bean.getStashItem(i);
                        // added by vic, hsfo
                        rdh.getOutlines().add(new oscar.form.pdfservlet.HsfoRxDataHolder.ValueBean(rx.getFullOutLine().replaceAll(";","\n")));
                        
                        %>
					<hr>
					<%= rx.getFullOutLine().replaceAll(";","<br/>") %> <%
                        strRx += rx.getFullOutLine() + ";;";
                        strRxNoNewLines.append(rx.getFullOutLine().replaceAll(";"," ")+ "\n");
                        }
                        
                        // added by vic, hsfo
                        // set the data to session in case user wants to generate PDF
                        session.setAttribute(oscar.form.pdfservlet.FrmPDFServlet.HSFO_RX_DATA_KEY, rdh);
                        %> <input type="hidden" name="rx"
						value="<%= StringEscapeUtils.escapeHtml(strRx.replaceAll(";","\\\n")) %>" />
					<input type="hidden" name="rx_no_newlines"
						value="<%= strRxNoNewLines.toString() %>" /></td>
				</tr>

				<tr valign=bottom>
					<td colspan=2 height=25px>
					<table width="100%" border="0" cellspacing="0"
						style="border-bottom: 1px gray solid; font: 8pt Verdana, Arial, Helvetica, sans-serif">
						<tr>
							<td colspan="3"><span style="font: bold 16pt;">D</span>x</td>
						</tr>
						<tr>
							<td width="35%"><input type="checkbox"
								onclick="return false" <%= (dx&1)!=0?"checked":"" %>>
							Hypertension</td>
							<td width="31%"><input type="checkbox"
								onclick="return false" <%= (dx&2)!=0?"checked":"" %>>
							Diabetes</td>
							<td><input type="checkbox" onclick="return false"
								<%= (dx&4)!=0?"checked":"" %>> Dyslipidemia</td>
						</tr>
						<tr>
							<td colspan="3">
							<div align="center">Heart & Stroke Hypertension Management
							Initiative</div>
							</td>
						</tr>
					</table>
					<br>
					</td>
				</tr>

				<% if ( oscar.OscarProperties.getInstance().getProperty("RX_FOOTER") != null ){ out.write(oscar.OscarProperties.getInstance().getProperty("RX_FOOTER")); }%>

				<tr valign=bottom>
					<td height=25px width=25%>Signature:</td>
					<td height=25px width=75%
						style="border-width: 0; border-bottom-width: 1; border-style: solid;">
					&nbsp;</td>
				</tr>
				<tr valign=bottom>
					<td height=25px></td>
					<td height=25px>&nbsp; <%= doctorName%></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
