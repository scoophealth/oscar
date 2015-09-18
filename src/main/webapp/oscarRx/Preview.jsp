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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="oscar.oscarProvider.data.*, oscar.log.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="oscar.*,java.lang.*,java.util.Date"%>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>

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
<title><bean:message key="RxPreview.title"/></title>
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
    function onPrint(cfgPage) {
        //document.forms[0].submit.value="print";
        //var ret = checkAllDates();
        //if(ret==true) {
            document.forms[0].action = "../form/createpdf?__title=Rx&__cfgfile=" + cfgPage + "&__template=a6blank";
            document.forms[0].target="_blank";
        //}
       return true;
    }
</script>

</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<%
Date rxDate = oscar.oscarRx.util.RxUtil.Today();
String rePrint = request.getParameter("rePrint");
oscar.oscarRx.pageUtil.RxSessionBean bean;
oscar.oscarRx.data.RxProviderData.Provider provider;
String signingProvider;
if( rePrint != null && rePrint.equalsIgnoreCase("true") ) {
    bean = (oscar.oscarRx.pageUtil.RxSessionBean)session.getAttribute("tmpBeanRX");
    signingProvider = bean.getStashItem(0).getProviderNo();
    rxDate = bean.getStashItem(0).getRxDate();
    provider = new oscar.oscarRx.data.RxProviderData().getProvider(signingProvider);
    session.setAttribute("tmpBeanRX", null);
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


oscar.oscarRx.data.RxPatientData.Patient patient = RxPatientData.getPatient(LoggedInInfo.getLoggedInInfoFromSession(request), bean.getDemographicNo());

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

doctorName = doctorName.replaceAll("\\d{6}","");
doctorName = doctorName.replaceAll("\\-","");

OscarProperties props = OscarProperties.getInstance();

String pracNo = provider.getPractitionerNo();
String strUser = (String)session.getAttribute("user");
ProviderData user = new ProviderData(strUser);
%>
<html:form action="/form/formname">

	<table width="400px" height="500px" cellspacing=0 cellpadding=10
		border=2>
		<tr>
			<td valign=top height="100px"><input type="image"
				src="img/rx.gif" border="0" value="submit" alt="[Submit]"
				name="submit" title="Print in a half letter size paper"
				onclick="<%=rePrint.equalsIgnoreCase("true") ? "javascript:return onPrint('oscarRxRePrintCfgPg1');" : "javascript:return onPrint('oscarRxPrintCfgPg1');" %>">
			<input type="hidden" name="printPageSize" value="PageSize.A6" /> <% 	String clinicTitle = provider.getClinicName().replaceAll("\\(\\d{6}\\)","") + "<br>" ;
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
				value="<%= StringEscapeUtils.escapeHtml(patient.getCity())+ ", " + StringEscapeUtils.escapeHtml(patient.getProvince()) + " " + StringEscapeUtils.escapeHtml(patient.getPostal())%>" />
			<input type="hidden" name="patientPhone"
				value="<bean:message key="RxPreview.msgTel"/><%=StringEscapeUtils.escapeHtml(patient.getPhone()) %>" />

			<input type="hidden" name="rxDate"
				value="<%= StringEscapeUtils.escapeHtml(oscar.oscarRx.util.RxUtil.DateToString(rxDate, "MMMM d, yyyy")) %>" />
			<input type="hidden" name="sigDoctorName"
				value="<%= StringEscapeUtils.escapeHtml(doctorName) %>" /> <!--img src="img/rx.gif" border="0"-->
			</td>
                        <td valign=top height="100px" id="clinicAddress"><b><%=doctorName%></b><br>
			<c:choose>
				<c:when test="${empty infirmaryView_programAddress}">
					<%= provider.getClinicName().replaceAll("\\(\\d{6}\\)","") %><br>
					<%= provider.getClinicAddress() %><br>
					<%= provider.getClinicCity() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        		    <%= provider.getClinicPostal() %><br>
		            <bean:message key="RxPreview.msgTel"/>: <%= provider.getClinicPhone() %><br>
        		    <bean:message key="RxPreview.msgFax"/>: <%= provider.getClinicFax() %><br>
				</c:when>
				<c:otherwise>
					<c:out value="${infirmaryView_programAddress}" escapeXml="false" />
					<br />
        			<bean:message key="RxPreview.msgTel"/>: <c:out value="${infirmaryView_programTel}" />
					<br />
        			<bean:message key="RxPreview.msgFax"/>: <c:out value="${infirmaryView_programFax}" />
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
					<%= patient.getCity() %>, <%= patient.getProvince() %> <%= patient.getPostal() %><br>
					<%= patient.getPhone() %><br>
					<b> <% if(!props.getProperty("showRxHin", "").equals("false")) { %>
					<bean:message key="oscar.oscarRx.hin" /><%= patient.getHin() %> <% } %>                                        
					</b><br>
                                        <% if(props.getProperty("showRxChartNo", "").equalsIgnoreCase("true")) { %>
					<bean:message key="oscar.oscarRx.chartNo" /><%=patient.getChartNo()%> <% } %></td>
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
						value="<%= strRxNoNewLines.toString() %>" /></td>
				</tr>
                                
                                <tr valign="bottom">
					<td colspan="2" id="additNotes"></td>
                                </tr>
                                
		
				<% if ( oscar.OscarProperties.getInstance().getProperty("RX_FOOTER") != null ){ out.write(oscar.OscarProperties.getInstance().getProperty("RX_FOOTER")); }%>


				<tr valign=bottom>
					<td height=25px width=25%><bean:message key="RxPreview.msgSignature"/>:</td>
					<td height=25px width=75%
						style="border-width: 0; border-bottom-width: 1; border-style: solid;">
					&nbsp;</td>
				</tr>
				<tr valign=bottom>
					<td height=25px></td>
                   	
                 <td height=25px>&nbsp; <%= doctorName%> <% if ( pracNo != null && ! pracNo.equals("") && !pracNo.equalsIgnoreCase("null")) { %>
                    <br /> &nbsp;  <bean:message key="RxPreview.PractNo"/> <%= pracNo%> <% } %>                                                         
                 </td>


				</tr>
				<% if( rePrint.equalsIgnoreCase("true") && rx != null ) { %>
				<tr valign=bottom style="font-size: 6px;">
                                    <td height=25px colspan="2"><bean:message key="RxPreview.msgReprintBy"/> <%=user.getProviderName(strUser)%><span style="float: left;">
					<bean:message key="RxPreview.msgOrigPrinted"/>:&nbsp;<%=rx.getPrintDate()%></span> <span
						style="float: right;"><bean:message key="RxPreview.msgTimesPrinted"/>:&nbsp;<%=String.valueOf(rx.getNumPrints())%></span>
					</td>
					<input type="hidden" name="origPrintDate"
						value="<%=rx.getPrintDate()%>">
					<input type="hidden" name="numPrints"
						value="<%=String.valueOf(rx.getNumPrints())%>">
				</tr>

				<%
                   }
                 if (oscar.OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null){%>
				<tr valign=bottom align="center" style="font-size: 9px">
					<td height=25px colspan="2"></br>
					<%= oscar.OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") %>
					</td>
				</tr>
				<%}%>
			</table>
			</td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
