<%--

    Copyright (c) 2012- Centre de Medecine Integree

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
    Centre de Medecine Integree, Saint-Laurent, Quebec, Canada to be provided
    as part of the OSCAR McMaster EMR System

--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@ page import="org.oscarehr.common.model.UserProperty"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%
    if (session.getAttribute("userrole") == null) { response.sendRedirect("../logout.jsp"); }
    String curUser_no = (String) session.getAttribute("user");
    UserPropertyDAO propertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
    String defaultPrinterNameAppointmentReceipt = "";
    boolean silentPrintAppointmentReceipt = false;
    UserProperty prop = null;
    prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_APPOINTMENT_RECEIPT);
    if (prop != null) {
        defaultPrinterNameAppointmentReceipt = prop.getValue();
    }
    prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_APPOINTMENT_RECEIPT_SILENT_PRINT);
    if (prop != null) {
        if (prop.getValue().equalsIgnoreCase("yes")) {
            silentPrintAppointmentReceipt = true;
        }
    }
%>
<html:html locale="true">
    <head>
        <title><bean:message key="report.appointmentReceipt.title" /></title>
    </head>
    <body>
        <% if (!defaultPrinterNameAppointmentReceipt.isEmpty()) { 
            if( silentPrintAppointmentReceipt == true) {%>
                <bean:message key="report.appointmentReceipt.SilentlyPrintToDefaultPrinter"/>
            <%} else {%> 
                <bean:message key="report.appointmentReceipt.DefaultPrinter"/> 
            <%}%>
            <%=defaultPrinterNameAppointmentReceipt%>
        <%}%>
        <br>
        <object id="apptpdf" type="application/pdf"  data="printAppointmentReceiptAction.do?appointment_no=<%=request.getParameter("appointment_no")%>" height="80%" width="100%"></object>  
    </body>
</html:html>
