<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarProvider.data.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<% response.setHeader("Cache-Control","no-cache");%>

<!--
/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
-->


<html:html locale="true">
<head>
<title>Print Preview</title>
<html:base/>

<logic:notPresent name="RxSessionBean" scope="session">
    <logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
    <bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean" name="RxSessionBean" scope="session" />
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
            document.forms[0].action = "../form/createpdf?__title=Rx&__cfgfile=oscarRxPrintCfgPg1&__template=a6blank";
            document.forms[0].target="_blank";
        //}
       return ret;
    }
</script>

</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
oscar.oscarRx.data.RxPatientData.Patient patient = new oscar.oscarRx.data.RxPatientData().getPatient(bean.getDemographicNo());
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
%>
<html:form action="/form/formname">

<table width="400px" height="500px" cellspacing=0 cellpadding=10 border=2>
    <tr>
        <td valign=top height="100px">
        	<input type="image" src="img/rx.gif" border="0" value="submit" alt="[Submit]" name="submit"
 			title="Print in a half letter size paper" onclick="javascript:return onPrint();">
			<input type="hidden" name="printPageSize" value="PageSize.A6"/>
			<input type="hidden" name="doctorName" value="<%= StringEscapeUtils.escapeHtml(doctorName) %>"/>
			<input type="hidden" name="clinicName" value="<%= StringEscapeUtils.escapeHtml(provider.getClinicName()) %>"/>
			<input type="hidden" name="clinicAddress" value="<%= StringEscapeUtils.escapeHtml(provider.getClinicAddress()) %>"/>
			<input type="hidden" name="clinicCityPostal" value="<%= StringEscapeUtils.escapeHtml(provider.getClinicCity())+"   " + StringEscapeUtils.escapeHtml(provider.getClinicPostal())%>"/>
			<input type="hidden" name="clinicPhone" value="<%= StringEscapeUtils.escapeHtml(provider.getClinicPhone()) %>"/>
			<input type="hidden" name="clinicFax" value="<%= StringEscapeUtils.escapeHtml(provider.getClinicFax()) %>"/>

			<input type="hidden" name="patientName" value="<%= StringEscapeUtils.escapeHtml(patient.getFirstName())+ " " +StringEscapeUtils.escapeHtml(patient.getSurname()) %>"/>
			<input type="hidden" name="patientAddress" value="<%= StringEscapeUtils.escapeHtml(patient.getAddress()) %>"/>
			<input type="hidden" name="patientCityPostal" value="<%= StringEscapeUtils.escapeHtml(patient.getCity())+ " " + StringEscapeUtils.escapeHtml(patient.getPostal())%>"/>
			<input type="hidden" name="patientPhone" value="<%= "Tel: " + StringEscapeUtils.escapeHtml(patient.getPhone()) %>"/>

			<input type="hidden" name="rxDate" value="<%= StringEscapeUtils.escapeHtml(oscar.oscarRx.util.RxUtil.DateToString(oscar.oscarRx.util.RxUtil.Today(), "MMMM d, yyyy")) %>"/>
			<input type="hidden" name="sigDoctorName" value="<%= StringEscapeUtils.escapeHtml(doctorName) %>"/>
            <!--img src="img/rx.gif" border="0"-->
        </td>
        <td valign=top height="100px">
            <b><%=doctorName %></b><br>
            <%= provider.getClinicName() %><br>
            <%= provider.getClinicAddress() %><br>
            <%= provider.getClinicCity() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <%= provider.getClinicPostal() %><br>
            Tel: <%= provider.getClinicPhone() %><br>
            Fax: <%= provider.getClinicFax() %><br>
        </td>
    </tr>
    <tr>
        <td colspan=2 valign=top height="75px">
            <table width=100% cellspacing=0 cellpadding=0>
                <tr>
                    <td align=left valign=top>
                      	<br>
                        <%= patient.getFirstName() %> <%= patient.getSurname() %><br>
                        <%= patient.getAddress() %><br>
                        <%= patient.getCity() %> <%= patient.getPostal() %><br>
                        <%= patient.getPhone() %><br>
                        <b>
                        <bean:message key="oscar.oscarRx.hin"/><%= patient.getHin() %>
                      </b>
                    </td>
                    <td align=right valign=top><b>
                        <%= oscar.oscarRx.util.RxUtil.DateToString(oscar.oscarRx.util.RxUtil.Today(), "MMMM d, yyyy") %>
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
                        for(i=0;i<bean.getStashSize();i++)
                        {
                            rx = bean.getStashItem(i);
                            %>

                            <%= rx.getFullOutLine().replaceAll(";","<br/>") %>
                            <hr>
                            <%
                            strRx += rx.getFullOutLine() + ";;";
                        }
                        %>
			<input type="hidden" name="rx" value="<%= StringEscapeUtils.escapeHtml(strRx.replaceAll(";","\\\n")) %>"/>
                    </td>
                </tr>
                <tr valign=bottom>
                    <td height=25px width=25%>
                        Signature:
                    </td>
                    <td height=25px width=75% style="border-width:0;border-bottom-width:1;border-style:solid;">
                        &nbsp;
                    </td>
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
