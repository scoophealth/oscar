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
<%@ page import="oscar.oscarProvider.data.*"%>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@ page import="org.oscarehr.common.model.UserProperty"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="oscar.OscarProperties"%>


<%
    if (session.getAttribute("userrole") == null) {
        response.sendRedirect("../logout.jsp");
    }
    String curUser_no = (String) session.getAttribute("user");
    UserPropertyDAO propertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
    
    OscarProperties oscarProps = OscarProperties.getInstance();
%>
<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/global.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
        <html:base />
        <link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">

        <title><bean:message key="provider.setDefaultPrinter.title" /></title>

        <script language="javascript">
                function createMessageHandler() { 
                     var PDFObject = document.getElementById("myPdf"); 
                    PDFObject.messageHandler = { 
                        onMessage: function(msg) { 
                            var select = document.getElementById("printerList");
                            select.options[select.options.length] = new Option("", 0);
                            for(index in msg) {
                                select.options[select.options.length] = new Option(msg[index], index);
                            }
                        }, 
                        onError: function(error, msg) { 
                            alert(error.message); 
                        } 
                    } 
                 }
                 
                 function setPrinter()
                 {
                     var select = document.getElementById("printerList");
                     document.getElementById("defaultPrinterName"+$('input[name=labelTypeRadioName]:checked').val()).value=select.options[select.selectedIndex].text;
                 }
        </script>
        
<style>
.alert-box{
    color:#555;
    border-radius:10px;
    font-family:Tahoma,Geneva,Arial,sans-serif;font-size:11px;
    padding:10px 10px 10px 36px;
    margin:10px;
}

.alert-box span {
    font-weight:bold;
    text-transform:uppercase;
}

.warning {
    background:#fff8c4;
    border:1px solid #f2c779;
}
        
</style>
    </head>

    <body class="BodyStyle" vlink="#0000FF" onLoad="createMessageHandler();">
        <table class="MainTable" id="scrollNumber1" name="encounterTable">
            <tr class="MainTableTopRow">
                <td class="MainTableTopRowLeftColumn"><bean:message key="provider.setDefaultPrinter.msgPrefs" /></td>
                <td style="color: white" class="MainTableTopRowRightColumn"><bean:message
                        key="provider.setDefaultPrinter.msgdefaulPrinter" /></td>
            </tr>
            <tr>
                <td class="MainTableLeftColumn">&nbsp;</td>
                <td class="MainTableRightColumn">
                    <%if(oscarProps.getProperty("new_label_print") == null || oscarProps.getProperty("new_label_print").equals("false")) { %>
                    
                    <div class="alert-box warning"><span>Warning: </span>This feature is currently disabled and requires the property "new_label_print" to be enabled. Please contact your support to enable this property.</div>

				    <%}%>
                
                    <%
                        String defaultPrinterNameAppointmentReceipt = "", defaultPrinterNamePDFEnvelope = "", defaultPrinterNamePDFLabel = "", defaultPrinterNamePDFAddressLabel = "";
                        String defaultPrinterNamePDFChartLabel = "", defaultPrinterNameClientLabLabel = "";
                        Boolean silentPrintAppointmentReceipt = false, silentPrintPDFEnvelope = false, silentPrintPDFLabel = false;
                        Boolean silentPrintPDFAddressLabel = false, silentPrintPDFChartLabel = false, silentPrintClientLabLabel = false;

                        UserProperty prop = null;
                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_APPOINTMENT_RECEIPT);
                        if (prop != null) {
                            defaultPrinterNameAppointmentReceipt = prop.getValue();
                        }
                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_ENVELOPE);
                        if (prop != null) {
                            defaultPrinterNamePDFEnvelope = prop.getValue();
                        }
                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_LABEL);
                        if (prop != null) {
                            defaultPrinterNamePDFLabel = prop.getValue();
                        }
                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_ADDRESS_LABEL);
                        if (prop != null) {
                            defaultPrinterNamePDFAddressLabel = prop.getValue();
                        }
                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_CHART_LABEL);
                        if (prop != null) {
                            defaultPrinterNamePDFChartLabel = prop.getValue();
                        }
                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_CLIENT_LAB_LABEL);
                        if (prop != null) {;
                            defaultPrinterNameClientLabLabel = prop.getValue();
                        }

                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_APPOINTMENT_RECEIPT_SILENT_PRINT);
                        if (prop != null) {
                            if (prop.getValue().equalsIgnoreCase("yes")) {
                                silentPrintAppointmentReceipt = true;
                            }
                        }

                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_ENVELOPE_SILENT_PRINT);
                        if (prop != null) {
                            if (prop.getValue().equalsIgnoreCase("yes")) {
                                silentPrintPDFEnvelope = true;
                            }
                        }

                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_LABEL_SILENT_PRINT);
                        if (prop != null) {
                            if (prop.getValue().equalsIgnoreCase("yes")) {
                                silentPrintPDFLabel = true;
                            }
                        }

                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_ADDRESS_LABEL_SILENT_PRINT);
                        if (prop != null) {
                            if (prop.getValue().equalsIgnoreCase("yes")) {
                                silentPrintPDFAddressLabel = true;
                            }
                        }
                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_PDF_CHART_LABEL_SILENT_PRINT);
                        if (prop != null) {
                            if (prop.getValue().equalsIgnoreCase("yes")) {
                                silentPrintPDFChartLabel = true;
                            }
                        }
                        prop = propertyDao.getProp(curUser_no, UserProperty.DEFAULT_PRINTER_CLIENT_LAB_LABEL_SILENT_PRINT);
                        if (prop != null) {
                            if (prop.getValue().equalsIgnoreCase("yes")) {
                                silentPrintClientLabLabel = true;
                            }
                        }

                        if (request.getAttribute("status") == null) {
                    %> 

                    <html:form action="/EditPrinter.do" >
                        <bean:message key="provider.setDefaultPrinter.setDefaultPrinterFor"/>:<br>
                        <table>
                            <tr><td>
                                    <input type=radio name="labelTypeRadioName" value="0" checked><bean:message key="provider.setDefaultPrinter.appointmentReceipt"/></td><td><input type="text" id="defaultPrinterName0" name="defaultPrinterNameAppointmentReceipt" value="<%=defaultPrinterNameAppointmentReceipt%>" size="40" readonly>
                                    <input type="checkbox" name="silentPrintAppointmentReceipt" <%=silentPrintAppointmentReceipt == true ? "checked" : ""%> ><bean:message key="provider.setDefaultPrinter.silentPrint"/>
                                </td>
                            </tr>
                            <tr><td>
                                    <input type=radio name="labelTypeRadioName" value="1"><bean:message key="provider.setDefaultPrinter.PDFEnvelope"/></td><td><input type="text" id="defaultPrinterName1" name="defaultPrinterNamePDFEnvelope" value="<%=defaultPrinterNamePDFEnvelope%>" size="40" readonly>
                                    <input type="checkbox" name="silentPrintPDFEnvelope" <%=silentPrintPDFEnvelope == true ? "checked" : ""%> ><bean:message key="provider.setDefaultPrinter.silentPrint"/>
                                </td>
                            </tr>
                            <tr><td>
                                    <input type=radio name="labelTypeRadioName" value="2" ><bean:message key="provider.setDefaultPrinter.PDFLabel"/></td><td><input type="text" id="defaultPrinterName2" name="defaultPrinterNamePDFLabel" value="<%=defaultPrinterNamePDFLabel%>" size="40" readonly>
                                    <input type="checkbox" name="silentPrintPDFLabel" <%=silentPrintPDFLabel == true ? "checked" : ""%> ><bean:message key="provider.setDefaultPrinter.silentPrint"/>
                                </td>
                            </tr>
                            <tr><td>
                                    <input type=radio name="labelTypeRadioName" value="3" ><bean:message key="provider.setDefaultPrinter.PDFAddressLabel"/></td><td><input type="text" id="defaultPrinterName3" name="defaultPrinterNamePDFAddressLabel" value="<%=defaultPrinterNamePDFAddressLabel%>" size="40" readonly>
                                    <input type="checkbox" name="silentPrintPDFAddressLabel" <%=silentPrintPDFAddressLabel == true ? "checked" : ""%> ><bean:message key="provider.setDefaultPrinter.silentPrint"/>
                                </td>
                            </tr>
                            <tr><td>
                                    <input type=radio name="labelTypeRadioName" value="4"><bean:message key="provider.setDefaultPrinter.PDFChartLabel"/></td><td><input type="text" id="defaultPrinterName4" name="defaultPrinterNamePDFChartLabel" value="<%=defaultPrinterNamePDFChartLabel%>" size="40" readonly>
                                    <input type="checkbox" name="silentPrintPDFChartLabel" <%=silentPrintPDFChartLabel == true ? "checked" : ""%> ><bean:message key="provider.setDefaultPrinter.silentPrint"/>
                                </td>
                            </tr>
                            <tr><td>
                                    <input type=radio name="labelTypeRadioName" value="5"><bean:message key="provider.setDefaultPrinter.ClientLabLabel"/></td><td><input type="text" id="defaultPrinterName5" name="defaultPrinterNameClientLabLabel" value="<%=defaultPrinterNameClientLabLabel%>" size="40" readonly>
                                    <input type="checkbox" name="silentPrintClientLabLabel" <%=silentPrintClientLabLabel == true ? "checked" : ""%> ><bean:message key="provider.setDefaultPrinter.silentPrint"/>
                                </td>
                            </tr>
                            <tr><td colspan="2"><select id="printerList" size="5" onclick="setPrinter();"></select></td></tr>
                            <tr><td colspan="2">
                            		<%if(oscarProps.getProperty("new_label_print") != null && oscarProps.getProperty("new_label_print").equals("true")) { %>
                                    <input type="submit" onclick="return true;" value="<bean:message key="provider.setDefaultPrinter.btnSave" />" />
                                    <br><br>
                                    <%}%>
                                    <bean:message key="provider.setDefaultPrinter.requirement" />       <br>                         
                                    <bean:message key="provider.setDefaultPrinter.requirementSilentPrint" />
                                    <div  style="visibility: hidden; display:inline;">
                                        <object id="myPdf" type="application/pdf"  data="<%=request.getContextPath()%>/PrinterList.do?method=generatePrinterListInPDF" height="100%" width="100%"></object>  
                                    </div>                                    
                                </td>
                            </tr>
                        </table>
                        </html:form> <%
                        } else if (((String) request.getAttribute("status")).equals("complete")) {%> 
                        <bean:message key="provider.setDefaultPrinter.msgSuccess" /> <br>
                </td>
            </tr><%}%>
            <tr>
                <td class="MainTableBottomRowLeftColumn"></td>
                <td class="MainTableBottomRowRightColumn"></td>
            </tr>
        </table>
    </body>
</html:html>
