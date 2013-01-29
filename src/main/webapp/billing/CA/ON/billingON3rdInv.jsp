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

<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="oscar.util.DateUtils,org.oscarehr.util.SpringUtils, org.oscarehr.util.MiscUtils"%>
<%@page import="java.util.Properties,java.util.Date,java.util.List,java.util.ArrayList,java.math.BigDecimal"%>
<%@page import="org.oscarehr.common.dao.BillingONPaymentDao,org.oscarehr.common.model.BillingONPayment"%>
<%@page import="org.oscarehr.common.dao.BillingServiceDao,org.oscarehr.common.model.BillingService"%>
<%@page import="org.oscarehr.common.dao.ClinicDAO,org.oscarehr.common.model.Clinic"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao,org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.dao.DemographicDao,org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.BillingONExtDao,org.oscarehr.common.model.BillingONExt"%>
<%@page import="org.oscarehr.common.dao.BillingONCHeader1Dao,org.oscarehr.common.model.BillingONCHeader1"%>
<%@page import="org.oscarehr.common.model.BillingONItem, org.oscarehr.common.service.BillingONService"%> 
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
    String invoiceNoStr = request.getParameter("billingNo");

    Integer invoiceNo = null;
    try {
        invoiceNo = Integer.parseInt(invoiceNoStr);
    } catch(NumberFormatException e) {
        invoiceNoStr = "";
        MiscUtils.getLogger().warn("Invalid Invoice No.");
    }
    
    BillingONCHeader1Dao bCh1Dao = (BillingONCHeader1Dao) SpringUtils.getBean("billingONCHeader1Dao");
    BillingONCHeader1 bCh1 = null;
    
    if (invoiceNo != null) 
        bCh1 = bCh1Dao.find(invoiceNo);
    
    String billTo = ""; 
    String remitTo = "";
    BigDecimal totalOwed = new BigDecimal("0.00");
    BigDecimal paidTotal = new BigDecimal("0.00");
    BigDecimal refundTotal = new BigDecimal("0.00");
    BigDecimal balanceOwing = new BigDecimal("0.00");
    List<BillingONItem> billingItems = new ArrayList<BillingONItem>();
    Demographic demo = null; 
    String providerFormattedName = "";
    String invoiceComment = "";
    String invoiceRefNum = "";
    String billingDateStr ="";
    String dueDateStr = "";
    
    ClinicDAO clinicDao = (ClinicDAO) SpringUtils.getBean("clinicDAO");
    Clinic clinic = clinicDao.getClinic();              
    oscar.OscarProperties props = oscar.OscarProperties.getInstance();
    
    if (bCh1 != null) {
        BillingONExtDao billExtDao = (BillingONExtDao) SpringUtils.getBean("billingONExtDao");
        BillingONPaymentDao billPaymentDao = (BillingONPaymentDao) SpringUtils.getBean("billingONPaymentDao");

        DemographicDao demoDAO = (DemographicDao)SpringUtils.getBean("demographicDao");
        ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
        
        billingDateStr = DateUtils.formatDate(bCh1.getBillingDate(),request.getLocale());
        invoiceRefNum = bCh1.getRefNum();
        
        BillingONService billingONService = (BillingONService) SpringUtils.getBean("billingONService");
        billingItems = billingONService.getNonDeletedInvoices(bCh1.getId());

        invoiceComment = bCh1.getComment(); 
        
        totalOwed = new BigDecimal(bCh1.getTotal());

        List<BillingONPayment> paymentRecords = billPaymentDao.find3rdPartyPayRecordsByBill(bCh1);
        paidTotal = BillingONPaymentDao.calculatePaymentTotal(paymentRecords);
        refundTotal = BillingONPaymentDao.calculateRefundTotal(paymentRecords);
        balanceOwing = billingONService.calculateBalanceOwing(bCh1.getId());

        demo = demoDAO.getDemographic(bCh1.getDemographicNo().toString());
        
        Provider provider = providerDao.getProvider(bCh1.getProviderNo());
        providerFormattedName = provider.getFormattedName();
           
        String clinicBillingPhone = props.getProperty("clinic_billing_phone","");
        if (clinicBillingPhone.isEmpty()) {
            clinicBillingPhone = clinic.getClinicDelimPhone();
        }
                   
        BillingONExt billToBillExt = billExtDao.getBillTo(bCh1);
        
        String useDemoClinicInfoOnInvoice = props.getProperty("useDemoClinicInfoOnInvoice","");
        if (!useDemoClinicInfoOnInvoice.isEmpty() && useDemoClinicInfoOnInvoice.equals("true")) { 
            BillingONExt useBillToExt = billExtDao.getUseBillTo(bCh1);
            if (useBillToExt != null && billToBillExt != null 
                    && useBillToExt.getValue().equalsIgnoreCase("on")) {  
                billTo = billToBillExt.getValue();                                    
            } else {
                StringBuilder buildBillTo = new StringBuilder();
                buildBillTo.append(demo.getFirstName()).append(" ").append(demo.getLastName()).append("\n")
                        .append(demo.getAddress()).append("\n")
                        .append(demo.getCity()).append(",").append(demo.getProvince()).append("\n")
                        .append(demo.getPostal()).append("\n\n")                       
                        .append("\n\n\n\n\n")
                        .append(LocaleUtils.getMessage(request.getLocale(),"billing.billing3rdInv.chartNo"))
                        .append(": ")
                        .append(demo.getChartNo());
                billTo = buildBillTo.toString();
            }
            
            StringBuilder buildRemitTo = new StringBuilder();
            buildRemitTo.append(clinic.getClinicName()).append("\n")
                    .append(clinic.getClinicAddress()).append("\n")
                    .append(clinic.getClinicCity()).append(",").append(clinic.getClinicProvince()).append("\n")
                    .append(clinic.getClinicPostal()).append("\n")
                    .append("Ph:").append(clinicBillingPhone).append("\n");
            remitTo = buildRemitTo.toString();
        } else {
            if (billToBillExt != null)
                billTo = billToBillExt.getValue();

            BillingONExt remitToBillExt = billExtDao.getRemitTo(bCh1);

            if (remitToBillExt != null)
                remitTo = remitToBillExt.getValue();
        }

        if (props.hasProperty("invoice_due_date")) {
            BillingONExt dueDateExt = billExtDao.getDueDate(bCh1);
            if (dueDateExt != null) {
                dueDateStr = dueDateExt.getValue();
            } else {
                Integer numDaysTilDue = Integer.parseInt(props.getProperty("invoice_due_date", "0"));
                Date serviceDate = bCh1.getBillingDate();
                dueDateStr = DateUtils.sumDate(serviceDate, numDaysTilDue, request.getLocale());
            }            
        }                                 
    }
   
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <style type="text/css" media="print">
        .doNotPrint {
            display: none;
        }
    </style>
    <style type="text/css" media="">
        .titleBar {
            background-color: gray;  
            padding-top: .5em;
            padding-bottom: .5em;
            padding-left: .5em;
        }
    </style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
    <script>
	    jQuery.noConflict();
    </script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript">
    function submitForm(methodName) {
        if (methodName=="email"){
            document.forms[0].method.value="sendEmail";
        } else if (methodName=="print") {            
            document.forms[0].method.value="getPrintPDF";
        }
        document.forms[0].submit();
    }
</script>
<title>Billing Invoice</title>
</head>
<body>
    <form action="<%=request.getContextPath()%>/BillingInvoice.do"> 
        <input type="hidden" name="method" value=""/>
        <input type="hidden" name="invoiceNo" id="invoiceNo" value="<%=invoiceNoStr%>"/>
        <div class="doNotPrint">
            <div class="titleBar">
                <input type="button" name="printInvoice" value="<bean:message key="billing.billing3rdInv.printPDF"/>" onClick="submitForm('print')"/>
                <input type="button" name="emailInvoice" value="<bean:message key="billing.billing3rdInv.email"/>" onClick="submitForm('email')"/>
            </div>
        </div>
    </form>
        <table width="100%" border="0">
            <tr>
             <% if (clinic != null) {%>
                <td><b><%=clinic.getClinicName()%></b><br />
                       <%=clinic.getClinicAddress()%><br />
                       <%=clinic.getClinicCity()%>, <%=clinic.getClinicProvince()%><br />
                       <%=clinic.getClinicPostal()%><br />
                        Tel.: <%=clinic.getClinicPhone()%><br />
                </td>
            <%  }%>
                <td align="right" valign="top"><font size="+2"><b>Invoice - <%=invoiceNoStr%></b></font><br />
                    <bean:message key="oscar.billing.CA.ON.3rdpartyinvoice.printDate"/>:<%=DateUtils.sumDate("yyyy-MM-dd HH:mm","0") %><br/>
                 <% if (props.hasProperty("invoice_due_date")) {
                        Integer numDaysTilDue = Integer.parseInt(props.getProperty("invoice_due_date", "0")); 
                        Date serviceDate = null;
                        serviceDate = bCh1.getBillingDate();
                  %>
                    <b><bean:message key="oscar.billing.CA.ON.3rdpartyinvoice.dueDate"/>:</b><%=dueDateStr%>
                 <% }%>
                </td>
            </tr>
        </table>
        <hr>
        <table width="100%" border="0">
            <tr>
                <td width="50%" valign="top">Bill To<br />
                    <pre><%=billTo%></pre>
                </td>
                <td valign="top">Remit To<br />
                    <pre><%=remitTo%></pre>
                </td>
            </tr>
        </table>

<oscar:customInterface section="billingInvoice"/>
<table width="100%" border="0">
	<tr>
            <td id="ptName">Patient: <%=bCh1.getDemographicName() %></td>
            <td id="ptDemoNo"> (<%=bCh1.getDemographicNo() %>)</td>
            <td id="ptGender"><%=bCh1.getSex().equals("1")? "Male":"Female" %></td>
            <td id="ptDOB"> DOB: <%=bCh1.getDob() %></td>
        </tr>
        <tr>    
		<td id="ptHin">
                   Insurance No: <%=demo.getHin()%>
                </td>
            </tr>
        </table>

        <hr>

        <table width="100%" border="0">
            <tr>
                <td><%=invoiceComment%></td>
            </tr>
        </table>

        <table width="100%" border="0">
            <tr>
                <th>Service Date</th>
                <th>Practitioner</th>
                <th>Payee</th>
                <th>Ref. Doctor</th>
            </tr>
            <tr align="center">
                <td><%=billingDateStr%></td>
                <td><%=providerFormattedName%></td>

                <% Properties prop = oscar.OscarProperties.getInstance();
                   String payee = prop.getProperty("PAYEE", "");
                   payee = payee.trim();
                   if( payee.length() > 0 ) {
                %>
                <td><%=payee%></td>
                <% } else { %>
                <td><%=providerFormattedName%></td>
                <% } %>
                <td><%=invoiceRefNum%></td>
            </tr>
        </table>

        <hr />

        <table width="100%" border="0">
            <tr>
                <th>Item #:</th>
                <th>Description</th>
                <th>Service Code</th>
                <th>Qty</th>
                <th>Dx</th>
                <th>Amount</th>
            </tr>
            <%
            BillingServiceDao billingServiceDao = (BillingServiceDao) SpringUtils.getBean("billingServiceDao");
            
            for(BillingONItem billItem : billingItems) { 	
                BillingService bs = null;
                String serviceDesc = "N/A";
                if (billItem.getServiceCode().startsWith("_"))
                    bs = billingServiceDao.searchPrivateBillingCode(billItem.getServiceCode(),billItem.getServiceDate());
                else
                    bs = billingServiceDao.searchBillingCode(billItem.getServiceCode(),"ON",billItem.getServiceDate());
                 
                if (bs != null) {
                    serviceDesc = bs.getDescription();
                }    
             %>
            <tr align="center">
                <td><%=billItem.getId() %></td>
                <td><%=serviceDesc%></td>
                <td><%=billItem.getServiceCode()%></td>
                <td><%=billItem.getServiceCount()%></td>
                <td><%=billItem.getDx()%></td>
                <td align="right"><%=billItem.getFee()%></td>
            </tr>
            <% } %>
        </table>

        <hr />

        <table width="100%" border="0">
            <tr align="right">
                <td width="86%">Total:</td>
                <td><%=totalOwed.toPlainString()%></td>
            </tr>
            <tr align="right">
                <td>Payments:</td>
                <td><%=paidTotal.toPlainString()%></td>
            </tr>
            <tr align="right">
                <td>Refunds:</td>
                <td><%=refundTotal.toPlainString()%></td>
            </tr>
            <tr align="right">
                <td><b>Balance:</b></td>
                <td><%=balanceOwing.toPlainString()%></td>
            </tr>	
        </table>
    </body>
</html>
