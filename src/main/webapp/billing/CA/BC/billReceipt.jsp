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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
  if (session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.*, oscar.oscarDemographic.data.*"%>
<%@page import="oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.*,oscar.oscarClinic.*"%>
<%
            double totalPayments = 0;
            double totalRefunds = 0;
            String color = "", colorflag = "";
            BillingHistoryDAO dao = new BillingHistoryDAO();
            BillingViewBean bean = (BillingViewBean) pageContext.findAttribute("billingViewBean");
            request.setAttribute("paymentTypes", bean.getPaymentTypes());
            oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
            org.oscarehr.common.model.Demographic demo = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), bean.getPatientNo());
            ArrayList billItem = bean.getBillItem();
            BillingFormData billform = new BillingFormData();
            OscarProperties props = OscarProperties.getInstance();
            ClinicData clinic = new ClinicData();
            String strPhones = clinic.getClinicDelimPhone();
            if (strPhones == null) {
                strPhones = "";
            }
            String strFaxes = clinic.getClinicDelimFax();
            if (strFaxes == null) {
                strFaxes = "";
            }
            Vector vecPhones = new Vector();
            Vector vecFaxes = new Vector();
            StringTokenizer st = new StringTokenizer(strPhones, "|");
            while (st.hasMoreTokens()) {
                vecPhones.add(st.nextToken());
            }
            st = new StringTokenizer(strFaxes, "|");
            while (st.hasMoreTokens()) {
                vecFaxes.add(st.nextToken());
            }
%>
<html:html>
    <head>
        <title>
            <bean:message key="billing.bc.title"/>
        </title>
        <style type="text/css" media="print">

            BODY
            {
                background-color: white;
                color: black;
                width:600px;
                font-size:7pt;
                margin-top: 0px;
                margin-right: 0px;
                margin-bottom: 0px;
                margin-left: 0px
            }

            .detailHeader {
                font-weight: bold;
                text-decoration: underline;
                text-align: center;
            }

            .header {
                display:none;
            }
            .header INPUT {
                display:none;
            }

            .header A {
                display:none;
            }

            input {
                border:none;
            }
            select {
                border:none;
            }
            .totals_cell {
                text-align: right;
            }

            .rcvPayment{
                display:none
            }


        </style>
        <style type="text/css">
            .detailHeader {
                font-weight: bold;
                text-decoration: underline;
                text-align: center;
            }
            .payTo{border:solid black 1px;
            }
            .secHead {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 12px;
                font-weight: bold;
                color: #000000;
                background-color: #FFFFFF;
                border-top: thin none #000000;
                border-right: thin none #000000;
                border-bottom: thin solid #000000;
                border-left: thin none #000000;
            }
            <!--
            A, BODY, INPUT, OPTION ,SELECT , TABLE, TEXTAREA, TD, TR {font-family:tahoma,sans-serif; font-size:12px;}
            -->
        </style>
        <script language="JavaScript">
            <!--

            function printInvoice(){
                window.print()


            }

            function reloadPage(init) {  //reloads the window if Nav4 resized
                if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
                        document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
                else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
            }
            //reloadPage(true);
            // -->

            function findObj(n, d) { //v4.0
                var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
                    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
                if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
                for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
                if(!x && document.getElementById) x=document.getElementById(n); return x;
            }

            function showHideLayers() { //v3.0
                var i,p,v,obj,args=showHideLayers.arguments;
                for (i=0; i<(args.length-2); i+=3) if ((obj=findObj(args[i]))!=null) { v=args[i+2];
                    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
                    obj.visibility=v; }
            }
            function popupPage(vheight,vwidth,varpage) {

                var page = "" + varpage;
                windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no";
                var popup=window.open(page, "mywindow", windowprops);
                if (popup != null) {
                    if (popup.opener == null) {
                        popup.opener = self;
                    }
                }
            }


            var remote=null;

            function rs(n,u,w,h,x) {
                args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
                remote=window.open(u,n,args);
                if (remote != null) {
                    if (remote.opener == null)
                        remote.opener = self;
                }
                if (x == 1) { return remote; }
            }


            var awnd=null;

            function scriptAttach(elementName) {
                var d = elementName;
                popupPage('600', '700', 'onSearch3rdBillAddr.jsp');
            }

        </script>
        <link rel="stylesheet" href="../billing/billing.css" type="text/css">
        <style type="text/css">
            <!--
            .style1 {
                font-size: 18px;
                font-weight: bold;
            }
            .style2 {font-size: 12px}
            .tbBody{
                width:750;
            }
            -->
        </style>
    </head>
    <body bgcolor="#FFFFFF" text="#000000" rightmargin="0" leftmargin="0" topmargin="10" marginwidth="0" marginheight="0">
        <html:form action="/billing/CA/BC/UpdateBilling">
            <html:hidden property="billingNo"/>
            <table width="650" border="0" align="center" style="border:black solid 1px ">
                <tr>
                    <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td valign="top" height="221">
                                    <table width="100%">
                                        <tr valign="top">
                                            <td width="59%">
                                                <table width="100%" border="0" name="innerTable">
                                                    <tr valign="top">
                                                        <td colspan="2" class="title4">
                                                            <table width="100%" border="0">
                                                                <tr>
                                                                    <td class="secHead" align="left">
                                                                        <h2>                                    INVOICE -
                                                                        <%=bean.getBillingNo()%>                                  </h2>
                                                                    </td>
                                                                    <%
                                                  java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("MMM d, yyyy 'at' h:mm aaa");
                                                  String fmtDate = fmt.format(new java.util.Date());
                                                                    %>
                                                                    <td class="secHead" align="right">                                  Date:
                                                                    <%=fmtDate%>                                </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="2" class="title4">
                                                            <div align="left" class="style1"><%=clinic.getClinicName()%>                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="2" class="address"><%=clinic.getClinicAddress()%>                            ,
                                                            <%=clinic.getClinicCity()%>                            ,
                                                        <%=clinic.getClinicProvince()%><%=clinic.getClinicPostal()%>                          </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="address" id="clinicPhone">                            Telephone:
                                                        <%=vecPhones.size() >= 1 ? vecPhones.elementAt(0) : clinic.getClinicPhone()%>                          </td>
                                                        <td class="address" id="clinicFax">&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="address" id="clinicFax">                            Fax:
                                                        <%=vecFaxes.size() >= 1 ? vecFaxes.elementAt(0) : clinic.getClinicFax()%>                          </td>
                                                        <td class="address" id="clinicFax">&nbsp;</td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <table width="100%" border="0">
                                        <tr>
                                            <td valign="top">
                                                <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                    <tr>
                                                        <td colspan="6" class="secHead">Billing To [<a href=# onclick="scriptAttach('billto'); return false;">Search</a>] </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="6">
                                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">

                                                                <tr>
                                                                    <td>
                                                                        <strong>Name:</strong>                                </td>
                                                                    <td>
                                                                    <html:text styleClass="billTo" maxlength="100" property="recipientName" size="50"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <strong>Address:</strong>                                </td>
                                                                    <td>
                                                                    <html:text styleClass="billTo" maxlength="100" property="recipientAddress" size="50"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <strong>City:</strong>                                </td>
                                                                    <td>
                                                                    <html:text styleClass="billTo" maxlength="100" property="recipientCity" size="50"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <strong>Province:</strong>                                </td>
                                                                    <td>
                                                                    <html:text styleClass="billTo" maxlength="100" property="recipientProvince" size="50"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <strong>Postal:</strong>                                </td>
                                                                    <td>
                                                                    <html:text styleClass="billTo" maxlength="6" property="recipientPostal" size="50"/>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                            <td width="50%" valign="top">
                                                <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                    <tr>
                                                        <td colspan="2" valign="top" class="secHead">                            Patient (
                                                            <%=bean.getPatientPHN()%>                            )
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="64" colspan="2" valign="top">
                                                            <p>
                                                                <strong>Name:</strong>
                                                                <%=bean.getPatientLastName()%>                              ,
                                                                <%=bean.getPatientFirstName()%>                              &nbsp;
                                                                <br>
                                                                <strong>Address:</strong>
                                                                <br>
                                                                <%=demo.getAddress()%>                              <br>
                                                                <%=demo.getCity()%>                              ,
                                                                <%=demo.getProvince()%>                              <br>
                                                                <%=demo.getPostal()%>                              <br>
                                                                <strong>Gender:</strong>
                                                                <%=demo.getSex()%>                              <br>
                                                                <strong>Birth Date :</strong>
                                                            <%=DemographicData.getDob(demo,"-")%>                            </p>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <table width="100%" border="0">
                                        <tr>
                                            <td class="secHead">Bill  Details</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="100%" border="0">
                                                    <tr class="detailHeader">
                                                        <td>Date</td>
                                                        <td>Practitioner</td>
                                                        <td>Payee</td>
                                                        <td>Ref. Doctor 1:</td>
                                                        <td>Ref. Type 1:</td>
                                                        <td>Ref. Doctor 2:</td>
                                                        <td>Ref. Type 2:</td>
                                                    </tr>
                                                    <tr align="center">
                                                        <td><%=bean.getServiceDate()%>                          </td>
                                                        <td><%=billform.getProviderName(bean.getApptProviderNo())%>                          </td>
                                                        <td><%=billform.getProviderName(bean.getBillingProvider())%>                          </td>
                                                        <td><%=bean.getReferral1()%>                          </td>
                                                        <td><%=bean.getReferType1()%>                          </td>
                                                        <td><%=bean.getReferral2()%>                          </td>
                                                        <td><%=bean.getReferType2()%>                          </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="137">
                                        <tr>
                                            <td valign="top">
                                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                    <tr class="detailHeader">
                                                        <td class="rcvPayment">&nbsp;</td>
                                                        <td>Line#</td>
                                                        <td>
                                                        <bean:message key="billing.service.desc"/>                          </td>
                                                        <td>Service Code</td>
                                                        <td>QTY</td>
                                                        <td>DX Codes</td>
                                                        <td>Amount</td>
                                                    </tr>
                                                    <%for (int i = 0; i < billItem.size(); i++) {%>
                                                    <tr align="center">
                                                        <td class="rcvPayment"><a href="#" onClick="popupPage(300,450,'viewReceivePaymentAction.do?lineNo=<%=((BillingBillingManager.BillingItem) billItem.get(i)).getLineNo()%>&amp;billNo=<%=bean.getBillingNo()%> ')">Receive Payment</a></td>
                                                        <td><%=((BillingBillingManager.BillingItem) billItem.get(i)).getLineNo()%></td>
                                                        <td><%=((BillingBillingManager.BillingItem) billItem.get(i)).getDescription()%></td>
                                                        <td><%=((BillingBillingManager.BillingItem) billItem.get(i)).getServiceCode()%></td>
                                                        <td><%=((BillingBillingManager.BillingItem) billItem.get(i)).getUnit()%></td>
                                                        <td align="right"><%=bean.getDx1()%>&nbsp;<%=bean.getDx2()%>&nbsp;<%=bean.getDx3()%></td>
                                                        <%
                                                            double lnTotal = ((BillingBillingManager.BillingItem) billItem.get(i)).getPrice();
                                                        %>
                                                        <td align="right"><%=java.text.NumberFormat.getCurrencyInstance().format(lnTotal).replace('$', ' ')%></td>
                                                    </tr>
                                                    <%
     String num = String.valueOf(((BillingBillingManager.BillingItem) billItem.get(i)).getLineNo());
     List trans = dao.getBillHistory(num);
     for (Iterator iter = trans.iterator(); iter.hasNext();) {
         oscar.entities.BillHistory item = (oscar.entities.BillHistory) iter.next();
         int paymentType = Integer.parseInt(item.getPaymentTypeId());
         if (paymentType != 10) {
             double amtReceived = item.getAmountReceived();

             if (amtReceived != 0) {
                 String label = "";
                 if (amtReceived < 0) {
                     label = "Refund";
                     totalRefunds += amtReceived;
                 } else {
                     label = "Payment";
                     totalPayments += amtReceived;
                 }
                                                    %>
                                                    <tr align="center">
                                                        <td colspan="3">&nbsp;</td>
                                                        <td><%=label%>(<%=item.getPaymentTypeDesc()%>)</td>
                                                        <td colspan="2"><%=item.getArchiveDate()%></td>

                                                        <td align="right"><%=java.text.NumberFormat.getCurrencyInstance().format(amtReceived * -1.0).replace('$', ' ')%></td>
                                                    </tr>
                                                    <%}
        }
    }%>
                                                    <%}%>
                                                    <tr>
                                                        <td colspan="7">&nbsp;</td>
                                                    </tr>

                                                    <tr>
                                                        <td colspan="7" class="secHead">
                                                            <table align="right" width="50%" cellpadding="1" cellspacing="1">
                                                            </table>                          </td>
                                                    </tr>
                                                    <tr valign="top">
                                                        <td colspan="5" rowspan="5"><table class="payTo" width="100%" border="0">
                                                                <tr>
                                                                    <td align="right" colspan="2"></td>
                                                                </tr>
                                                                <tr class="secHead">
                                                                    <td height="14" colspan="2">Please Make Cheque Payable To: </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="title4">
                                                                        Dr. <%=bean.getDefaultPayeeFirstName() + " " + bean.getDefaultPayeeLastName()%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="title4">
                                                                        <%=clinic.getClinicName()%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="address"><%=clinic.getClinicAddress()%> , <%=clinic.getClinicCity()%> , <%=clinic.getClinicProvince()%><%=clinic.getClinicPostal()%> </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="address" id="clinicPhone"> Telephone: <%=vecPhones.size() >= 1 ? vecPhones.elementAt(0) : clinic.getClinicPhone()%> </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="address" id="clinicFax"> Fax: <%=vecFaxes.size() >= 1 ? vecFaxes.elementAt(0) : clinic.getClinicFax()%> </td>
                                                                </tr>
                                                            </table></td>
                                                        <td align="right">Total:</td>
                                                        <td align="right"><%=java.text.NumberFormat.getCurrencyInstance().format(bean.calculateSubtotal()).replace('$', ' ')%>                          </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Payments:</td>
                                                        <td align="right"><%=java.text.NumberFormat.getCurrencyInstance().format(totalPayments).replace('$', ' ')%>                          </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Refunds:</td>
                                                        <td align="right"><%=java.text.NumberFormat.getCurrencyInstance().format(totalRefunds).replace('$', ' ')%>                          </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <div align="right" class="style2">
                                                                <strong>Balance</strong>
                                                                :</div>                          </td>
                                                                <%double gtotal = bean.calculateSubtotal() - totalPayments - totalRefunds;%>
                                                        <td align="right">
                                                            <strong><%=java.text.NumberFormat.getCurrencyInstance().format(gtotal).replace('$', ' ')%>                            </strong>                          </td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                    </tr>
                                                </table>
                                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                    <tr>
                                                        <td width="17%" class="header">Billing Notes:</td>
                                                    </tr>
                                                    <tr>
                                                        <td rowspan="2">
                                                            <html:textarea cols="60" styleClass="header" rows="5" property="messageNotes"></html:textarea>
                                                        </td>
                                                    </tr>

                                                </table>

                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td align="right">    <table width="100%" border="0">
                                                                <tr>
                                                                    <td align="right" colspan="2">                          </td>
                                                                </tr>

                                                                <tr>
                                                                    <td colspan="2" align="left" valign="bottom">
                                                                        <html:submit styleClass="header" value="Update Invoice" property=""/>
                                                                        <html:button styleClass="header" value="Print" property="Submit" onclick="javascript:printInvoice()"/>
                                                                        <html:button styleClass="header" value="Cancel" property="Submit2" onclick="javascript:window.close()"/>
                                                                        &nbsp;                          </td>
                                                                </tr>
                                                            </table>                         </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
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
