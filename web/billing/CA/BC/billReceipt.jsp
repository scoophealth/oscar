
<%
  if (session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
%>
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
<%@page language="java" contentType="text/html"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.*, oscar.oscarDemographic.data.*"%>
<%@page import="oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.*,oscar.oscarClinic.*"%>
<%
  double totalPayments = 0;
  double totalRefunds = 0;
  String color = "", colorflag = "";
  PrivateBillTransactionsDAO dao = new PrivateBillTransactionsDAO();
  BillingViewBean bean = (BillingViewBean) pageContext.findAttribute("billingViewBean");
  request.setAttribute("paymentTypes",bean.getPaymentTypes());
  oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
  oscar.oscarDemographic.data.DemographicData.Demographic demo = demoData.getDemographic(bean.getPatientNo());
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
    display:none;
    }
    .totals_cell {
    text-align: right;
    }

    .recPaymentSection{
    display:none
    }
</style>
  <style type="text/css">
    .detailHeader {
    font-weight: bold;
    text-decoration: underline;
    text-align: center;
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

function setfocus() {
		  //document.serviceform.xml_diagnostic_code.focus();
		  //document.serviceform.xml_diagnostic_code.select();
		}

function RecordAttachments(Files, File0, File1, File2) {
  window.document.serviceform.elements["File0Data"].value = File0;
  window.document.serviceform.elements["File1Data"].value = File1;
  window.document.serviceform.elements["File2Data"].value = File2;
    window.document.all.Atts.innerText = Files;
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
function ScriptAttach() {


  t0 = escape(document.serviceform.xml_diagnostic_detail1.value);
  t1 = escape(document.serviceform.xml_diagnostic_detail2.value);
  t2 = escape(document.serviceform.xml_diagnostic_detail3.value);
  awnd=rs('att','billingDigNewSearch.jsp?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',600,600,1);
  awnd.focus();



}



function OtherScriptAttach() {
  t0 = escape(document.serviceform.xml_other1.value);
  t1 = escape(document.serviceform.xml_other2.value);
  t2 = escape(document.serviceform.xml_other3.value);
 // f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','billingCodeSearch.jsp?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',600,600,1);
  awnd.focus();
}
function ResearchScriptAttach() {
  t0 = escape(document.serviceform.xml_research1.value);
  t1 = escape(document.serviceform.xml_research2.value);
  t2 = escape(document.serviceform.xml_research3.value);
 // f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','billingResearchCodeSearch.jsp?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',600,600,1);
  awnd.focus();
}


function ResearchScriptAttach() {
  t0 = escape(document.serviceform.xml_referral1.value);
  t1 = escape(document.serviceform.xml_referral2.value);

  awnd=rs('att','billingReferralCodeSearch.jsp?name='+t0 + '&name1=' + t1 +  '&search=',600,600,1);
  awnd.focus();
}

function POP(n,h,v) {
  window.open(n,'OSCAR','toolbar=no,location=no,directories=no,status=yes,menubar=no,resizable=yes,copyhistory=no,scrollbars=yes,width='+h+',height='+v+',top=100,left=200');
}
//-->
</SCRIPT>  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="JavaScript">
<!--
<!--
function reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
  else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
}
reloadPage(true);
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
//-->
</script>  <link rel="stylesheet" href="../billing/billing.css" type="text/css">
  <style type="text/css">
    <!--
      .style1 {
      font-size: 18px;
      font-weight: bold;
      }
      .style2 {font-size: 12px}
    -->
  </style>
</head>
<body bgcolor="#FFFFFF" text="#000000" rightmargin="0" leftmargin="0" topmargin="10" marginwidth="0" marginheight="0" onLoad="setfocus();showHideLayers('Layer1','','hide')" onUnload="self.opener.refresh();">
  <html:form action="/billing/CA/BC/UpdateBilling">
    <html:hidden property="billingNo"/>
    <table width="755" border="0" align="center" style="border:black solid 1px ">
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
<%=vecPhones.size()>=1?vecPhones.elementAt(0):clinic.getClinicPhone()%>                          </td>
                          <td class="address" id="clinicFax">&nbsp;</td>
                        </tr>
                        <tr>
                          <td class="address" id="clinicFax">                            Fax:
<%=vecFaxes.size()>=1?vecFaxes.elementAt(0):clinic.getClinicFax()%>                          </td>
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
                          <td colspan="6" class="secHead">Billing To</td>
                        </tr>
                        <tr>
                          <td colspan="6">
                            <table width="100%" border="0" cellspacing="3" cellpadding="3">
                              <tr>
                                <td>
                                  <strong>Name:</strong>
                                </td>
                                <td>
                                  <html:text maxlength="100" property="recipientName" size="50"/>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                  <strong>Address:</strong>
                                </td>
                                <td>
                                  <html:text maxlength="100" property="recipientAddress" size="50"/>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                  <strong>City:</strong>
                                </td>
                                <td>
                                  <html:text maxlength="100" property="recipientCity" size="50"/>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                  <strong>Province:</strong>
                                </td>
                                <td>
                                  <html:text maxlength="100" property="recipientProvince" size="50"/>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                  <strong>Postal:</strong>
                                </td>
                                <td>
                                  <html:text maxlength="6" property="recipientPostal" size="50"/>
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
<%=demo.getDob("-")%>                            </p>
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
                          <td>Line#</td>
                          <td>
                            <bean:message key="billing.service.desc"/>
                          </td>
                          <td>Service Code</td>
                          <td>QTY</td>
                          <td>DX Codes</td>
                          <td>Amount</td>
                        </tr>
                      <%for (int i = 0; i < billItem.size(); i++) {                      %>
                        <tr align="center">
                          <td><%=((BillingBillingManager.BillingItem)billItem.get(i)).getLineNo()%>                          </td>
                          <td><%=((BillingBillingManager.BillingItem)billItem.get(i)).getDescription()%>                          </td>
                          <td><%=((BillingBillingManager.BillingItem)billItem.get(i)).getServiceCode()%>                          </td>
                          <td><%=((BillingBillingManager.BillingItem)billItem.get(i)).getUnit()%>                          </td>
                          <td align="right"><%=bean.getDx1()%>                            &nbsp;
<%=bean.getDx2()%>                            &nbsp;
<%=bean.getDx3()%>                          </td>
                          <td align="right"><%=((BillingBillingManager.BillingItem)billItem.get(i)).getLineTotal()%>                          </td>
                        </tr>
                      <%}                      %>
                        <tr>
                          <td colspan="6">&nbsp;</td>
                        </tr>
                        <tr>
                          <td colspan="6" class="secHead">Transaction History</td>
                        </tr>
                        <tr>
                          <td colspan="6" class="secHead">
                            <table width="50%">
                              <tr>
                                <th>Type</th>
                                <th>Amount</th>
                                <th>Date</th>
                              </tr>
                            <%
                              List trans = dao.getPrivateBillTransactionsByBillNo(bean.getBillingNo());
                              for (Iterator iter = trans.iterator(); iter.hasNext(); ) {
                                oscar.entities.PrivateBillTransaction item = (oscar.entities.PrivateBillTransaction) iter.next();
                                double amtReceived = item.getAmount_received();
                                String label = "";
                                if (amtReceived < 0) {
                                  label = "Refund";
                                  totalRefunds += amtReceived;
                                }
                                else {
                                  label = "Payment";
                                  totalPayments += amtReceived;
                                }
                            %>
                              <tr align="center">
                                <td><%=label%>                                </td>
                                <td><%=java.text.NumberFormat.getCurrencyInstance().format(amtReceived).replace('$',' ')%>                                </td>
                                <td><%=item.getCreation_date()%>                                </td>
                              </tr>
                            <%}                            %>
                            </table>
                          </td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td align="right">Total:</td>
                          <td align="right"><%=bean.getGrandtotal()%>                          </td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td align="right">Payments:</td>
                          <td align="right"><%=java.text.NumberFormat.getCurrencyInstance().format(totalPayments).replace('$',' ')%>                          </td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td align="right">Refunds:</td>
                          <td align="right"><%=java.text.NumberFormat.getCurrencyInstance().format(totalRefunds).replace('$',' ')%>                          </td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>
                            <div align="right" class="style2">
                              <strong>Balance</strong>
                              :
</div>
                          </td>
                        <%double gtotal = new Double(bean.getGrandtotal()).doubleValue() - totalPayments - totalRefunds;                        %>
                          <td align="right">
                            <strong><%=java.text.NumberFormat.getCurrencyInstance().format(gtotal).replace('$',' ')%>                            </strong>
                          </td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                      </table>
                      <table width="100%" border="0" cellspacing="1" cellpadding="1">
                        <tr>
                          <td height="14" colspan="4" class="secHead">
                            <table width="100%" border="0" cellspacing="3" cellpadding="3">
                              <tr>
                                <td><label>Receive Payment - $
                                    <input type="text" maxlength="6" name="amountReceived"/>
                                </label>
                                  <br>
                                  <label> Payment Method -
                                  <html:select property="paymentMethod">
                                   <html:options collection="paymentTypes" property="id" labelProperty="paymentType"/>
                                  </html:select>
                                  </label></td>
                              </tr>
                            </table>
                            <label></label>
                          <label></label></td>
                        </tr>
                        <tr>
                          <td width="105%" height="14" colspan="4" class="secHead">Notes</td>
                        </tr>
                        <tr>
                          <td height="14" colspan="4"><%=bean.getMessageNotes()%>                          </td>
                        </tr>
                      </table>
                      <table width="100%" border="0">
                        <tr>
                          <td align="right" colspan="3">                          </td>
                        </tr>
                        <tr class="secHead">
                          <td height="14" colspan="3" class="header secHead">Update Bill</td>
                        </tr>
                        <tr>
                          <td width="17%" class="header">Billing Notes:</td>
                          <td colspan="2" class="header">                            &nbsp;
                            Bill Status
                            &nbsp;
</td>
                        </tr>
                        <tr>
                          <td rowspan="2">
                            <html:textarea cols="60" styleClass="header" rows="5" property="messageNotes"></html:textarea>
                          </td>
                          <td width="81%" colspan="1" valign="top">
                            <html:select styleClass="header" property="billStatus" style="font-size:110%;">
                              <html:option value="O">O | Bill MSP</html:option>
                              <html:option value="P">P | Bill Patient</html:option>
                              <html:option value="N">N | Do Not Bill</html:option>
                              <html:option value="X">X | Bad Debt</html:option>
                              <html:option value="D">D | Deleted Bill</html:option>
                              <html:option value="T">T | Transfer to Collection</html:option>
                            </html:select>
                          </td>
                          <td width="2%">&nbsp;</td>
                        </tr>
                        <tr>
                          <td colspan="2" align="right" valign="bottom">
                            <html:submit styleClass="header" value="Update Bill" property=""/>
                            <html:button styleClass="header" value="Print Bill" property="Submit" onclick="javascript:window.print()"/>
                            <html:button styleClass="header" value="Cancel" property="Submit2" onclick="javascript:window.close()"/>
                            &nbsp;
                          </td>
                        </tr>
                      </table>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td align="right">                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
          <p>&nbsp;</p>
        </td>
      </tr>
    </table>
  </html:form>
</body>
</html:html>
