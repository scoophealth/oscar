<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarProvider.data.*"%>
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

<table width="400px" height="500px" cellspacing=0 cellpadding=10 border=2>
    <tr>
        <td valign=top height="100px">
            <img src="img/rx.gif" border="0">
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
                        <%= patient.getFirstName() %> <%= patient.getSurname() %><br>
                        <%= patient.getAddress() %><br>
                        <%= patient.getCity() %> <%= patient.getPostal() %><br>
                        <%= patient.getPhone() %><br>
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
                        for(i=0;i<bean.getStashSize();i++)
                        {
                            rx = bean.getStashItem(i);
                            %>
                           
                            <%= rx.getFullOutLine().replaceAll(";","<br/>") %>
                            <hr>
                            <%
                        }
                        %>
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
</body>
</html:html>
