<!-- 
 *
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved. *
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
 * Yi Li
 */
-->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.*,java.math.*,java.net.*,java.sql.*,oscar.util.*,oscar.*,oscar.appt.*"%>
<%@ page import="oscar.oscarBilling.ca.on.administration.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*, java.util.Properties"%> 
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />

<%//
	if (session.getAttribute("user") == null) {
		response.sendRedirect("../../../logout.jsp");
	}

			String user_no = (String) session.getAttribute("user");
			String providerview = request.getParameter("providerview") == null ? "" : request
					.getParameter("providerview");
			String asstProvider_no = "";
			String color = "";
			String premiumFlag = "";
			String service_form = "";
%> 
 
<% 
Properties gstProp = new Properties(); 
GstControlAction db = new GstControlAction(); 
gstProp = db.readDatabase(); 
String flag = gstProp.getProperty("gstFlag", ""); 
String percent = gstProp.getProperty("gstPercent", "");
%>

<%//
	BillingReviewPrep prepObj = new BillingReviewPrep();
	Vector[] vecServiceParam = new Vector[3];
	if(oscarVariables.getProperty("onBillingSingleClick", "").equals("yes")) {
		vecServiceParam[0] = new Vector();
		vecServiceParam[1] = new Vector();
		vecServiceParam[2] = new Vector();
	} else {
                //This will find data in the checkboxes/ "1","1" is the default 1 unit and 100% of the fee item
		vecServiceParam = prepObj.getRequestFormCodeVec(request, "xml_", "1", "1");
	}
	
	System.out.println("line1=="+vecServiceParam[0] + ":" + vecServiceParam[1] + vecServiceParam[2] + " :::" + vecServiceParam.length);
        System.out.println("numItem "+BillingDataHlp.FIELD_SERVICE_NUM);
        
        //This finds data in the PINK AREA
	Vector[] vecServiceParam0 = prepObj.getRequestCodeVec(request, "serviceCode", "serviceUnit", "serviceAt", BillingDataHlp.FIELD_SERVICE_NUM);
	System.out.println("line2=="+vecServiceParam0[0] +":"+vecServiceParam0[1] +" :: :" + vecServiceParam0.length);
	
        vecServiceParam[0].addAll(vecServiceParam0[0]);
	vecServiceParam[1].addAll(vecServiceParam0[1]);
	vecServiceParam[2].addAll(vecServiceParam0[2]);
        /////// hack used to order the billing codes
        /////// Would make sense to change getServiceCodeReviewVec method to accept the hashtable 
        /////// But should cause that much of a performance hit. It's generally under 3 items
        Vector v = new Vector();
        for (int ii = 0 ; ii< vecServiceParam[0].size(); ii++){
            Hashtable h = new Hashtable();
            h.put("serviceCode",vecServiceParam[0].get(ii));
            h.put("serviceUnit",vecServiceParam[1].get(ii));
            h.put("serviceAt",vecServiceParam[2].get(ii));
            v.add(h);
        }
        Collections.sort(v,new BillingSortComparator());
        
        vecServiceParam[0] = new Vector();
        vecServiceParam[1] = new Vector();
        vecServiceParam[2] = new Vector();
        
        for (int ii = 0; ii < v.size(); ii++){
            Hashtable h = (Hashtable) v.get(ii);
            vecServiceParam[0].add((String) h.get("serviceCode") );
            vecServiceParam[1].add((String) h.get("serviceUnit"));
            vecServiceParam[2].add((String) h.get("serviceAt"));
        }
        ///////--------
	Vector vecCodeItem = prepObj.getServiceCodeReviewVec(vecServiceParam[0], vecServiceParam[1],vecServiceParam[2]);
	Vector vecPercCodeItem = prepObj.getPercCodeReviewVec(vecServiceParam[0], vecServiceParam[1], vecCodeItem);  //LINE CAUSING ERROR

        
        
	Properties propCodeDesc = (new JdbcBillingCodeImpl()).getCodeDescByNames(vecServiceParam[0]);
			String dxDesc = prepObj.getDxDescription(request.getParameter("dxCode"));
			String clinicview = oscarVariables.getProperty("clinic_view", "");
			String clinicNo = oscarVariables.getProperty("clinic_no", "");
			String visitType = oscarVariables.getProperty("visit_type", "");
			String appt_no = request.getParameter("appointment_no");
			String demoname = request.getParameter("demographic_name");
			String demo_no = request.getParameter("demographic_no");
			String apptProvider_no = request.getParameter("apptProvider_no");
			String ctlBillForm = request.getParameter("billForm");
			String assgProvider_no = request.getParameter("assgProvider_no");
			//String            dob               = request.getParameter("dob");
			String demoSex = request.getParameter("DemoSex");
			GregorianCalendar now = new GregorianCalendar();
			int curYear = now.get(Calendar.YEAR);
			int curMonth = (now.get(Calendar.MONTH) + 1);
			int curDay = now.get(Calendar.DAY_OF_MONTH);
			int dob_year = 0, dob_month = 0, dob_date = 0, age = 0;
			String content = "";
			String total = "";

			BillingONDataHelp dbObj = new BillingONDataHelp();
			String msg = "<tr><td colspan='2'>Calculation</td></tr>";
			String action = "edit";
			Properties propHist = null;
			Vector vecHist = new Vector();
			// get provider's detail
			String proOHIPNO = "", proRMA = "";
			String sql = "select * from provider where provider_no='" + request.getParameter("xml_provider") + "'";
			ResultSet rs = dbObj.searchDBRecord(sql);
			while (rs.next()) {
				proOHIPNO = rs.getString("ohip_no");
				proRMA = rs.getString("rma_no");
			}
			if (request.getParameter("xml_provider") != null)
				providerview = request.getParameter("xml_provider");
			// get patient's detail
			String errorFlag = "";
			String warningMsg = "", errorMsg = "";
			String r_doctor = "", r_doctor_ohip = "";
			String demoFirst = "", demoLast = "", demoHIN = "", demoVer = "", demoDOB = "", demoDOBYY = "", demoDOBMM = "", demoDOBDD = "", demoHCTYPE = "";
			String strPatientAddr = ""; 
			sql = "select * from demographic where demographic_no=" + demo_no;
			rs = dbObj.searchDBRecord(sql);
			while (rs.next()) {
				strPatientAddr = rs.getString("first_name") + " " + rs.getString("last_name") + "\n" 
				+ rs.getString("address") + "\n" 
				+ rs.getString("city") + ", " + rs.getString("province") + "\n" 
				+ rs.getString("postal") + "\n" 
				+ "Tel: " + rs.getString("phone") ;
				
				assgProvider_no = rs.getString("provider_no");
				demoFirst = rs.getString("first_name");
				demoLast = rs.getString("last_name");
				demoHIN = rs.getString("hin");
				demoVer = rs.getString("ver");
				demoSex = rs.getString("sex");
				if (demoSex.compareTo("M") == 0)
					demoSex = "1";
				if (demoSex.compareTo("F") == 0)
					demoSex = "2";

				demoHCTYPE = rs.getString("hc_type") == null ? "" : rs.getString("hc_type");
				if (demoHCTYPE.compareTo("") == 0 || demoHCTYPE == null || demoHCTYPE.length() < 2) {
					demoHCTYPE = "ON";
				} else {
					demoHCTYPE = demoHCTYPE.substring(0, 2).toUpperCase();
				}
				demoDOBYY = rs.getString("year_of_birth");
				demoDOBMM = rs.getString("month_of_birth");
				demoDOBDD = rs.getString("date_of_birth");

				if (rs.getString("family_doctor") == null) {
					r_doctor = "N/A";
					r_doctor_ohip = "000000";
				} else {
					r_doctor = SxmlMisc.getXmlContent(rs.getString("family_doctor"), "rd") == null ? "" : SxmlMisc
							.getXmlContent(rs.getString("family_doctor"), "rd");
					r_doctor_ohip = SxmlMisc.getXmlContent(rs.getString("family_doctor"), "rdohip") == null ? ""
							: SxmlMisc.getXmlContent(rs.getString("family_doctor"), "rdohip");
				}

				demoDOBMM = demoDOBMM.length() == 1 ? ("0" + demoDOBMM) : demoDOBMM;
				demoDOBDD = demoDOBDD.length() == 1 ? ("0" + demoDOBDD) : demoDOBDD;
				demoDOB = demoDOBYY + demoDOBMM + demoDOBDD;

				if (rs.getString("hin") == null) {
					errorFlag = "1";
					errorMsg = errorMsg
							+ "<br><div class='myError'>Error: The patient does not have a valid HIN. </div><br>";
				} else if (rs.getString("hin").equals("")) {
					warningMsg += "<br><div class='myError'>Warning: The patient does not have a valid HIN. </div><br>";
				}
				if (r_doctor_ohip != null && r_doctor_ohip.length() > 0 && r_doctor_ohip.length() != 6) {
					warningMsg += "<br><div class='myError'>Warning: the referral doctor's no is wrong. </div><br>";
				}
				if (demoDOB.length() != 8) {
					errorFlag = "1";
					errorMsg = errorMsg
							+ "<br><div class='myError'>Error: The patient does not have a valid DOB. </div><br>";
				}
			}


			// create msg
			String wrongMsg = errorMsg + warningMsg;
			//msg += errorMsg + warningMsg;
			//System.out.println(" * ******************************" + sql);

			%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OscarBilling</title>
<link rel="stylesheet" type="text/css" href="billingON.css" />
<script language="JavaScript">
var bClick = false;

function onSave() {
    var ret = true;
    bClick = false;
    return ret;
}

function onClickSave() {
    bClick = true;
}

function popupPage(vheight,vwidth,varpage) {
    var page = "" + varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
    var popup=window.open(page, "billcorrection", windowprops);
    if (popup != null) {
	if (popup.opener == null) {
	    popup.opener = self;
	}
	popup.focus();
    }
}

function settlePayment() {
    document.forms[0].payment.value = document.forms[0].finaltotal.value;
}

function scriptAttach(elementName) {
    var d = elementName;
    t0 = escape("document.forms[0].elements[\'"+d+"\'].value");
    popupPage('600', '700', 'onSearch3rdBillAddr.jsp?param='+t0);
}

function showtotal(){ 
 	var subtotal = document.getElementById("total").value; 
  	
 	<% if (flag.equals("1")) {%> 
 	var percent = <%=percent%>/100; 
 	var gst = subtotal * percent; 
 	
 	gst *= 100; 
 	gst = Math.round(gst); 
 	gst /= 100; 
 	
 	document.getElementById("gsttotal").value = gst; 
 	var total = subtotal * 1 + gst; 
 	total *= 100; 
 	total = Math.round(total); 
 	total /= 100; 
 	document.getElementById("finaltotal").value = total; 
 	<%} else {%> 
 	document.getElementById("finaltotal").value = subtotal; 
 	<%}%> 
 	} 
</script>
	
</head>

<body topmargin="0"  onload="showtotal()">
<form method="post" name="titlesearch" action="billingONSave.jsp" onsubmit="return onSave();">
<table border="0" cellpadding="0" cellspacing="2" width="100%" class="myIvory">
	<tr>
		<td>
		<table border="0" cellspacing="0" cellpadding="0" width="100%" class="myDarkGreen">
			<tr>
				<td><b><font color="#FFFFFF">&nbsp;Confirmation </font></b></td>
				<td align="right"><input type="hidden" name="addition" value="Confirm" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="0" cellspacing="0" cellpadding="0" width="100%" class="myYellow">
			<tr>
				<td nowrap width="10%" align="center"><%=demoname%> <%=demoSex.equals("1") ? "Male" : "Female"%> 
				<%=" DOB: " + demoDOBYY + "/" + demoDOBMM + "/" + demoDOBDD + " HIN: " + demoHIN + "" + demoVer%>
				</td>
				<td align="center"><%=wrongMsg%></td>
			</tr>
		</table>

		<table border="1" cellspacing="0" cellpadding="0" width="100%" bordercolorlight="#99A005" bordercolordark="#FFFFFF"
			 class="myIvory">
			<tr>
				<td width="50%">

				<table border="1" cellspacing="2" cellpadding="0" width="100%" bordercolorlight="#99A005" bordercolordark="#FFFFFF"
					>
					<tr>
						<td nowrap width="30%" align="center" ><b>Service Date</b><br>
						<%=request.getParameter("service_date").replaceAll("\\n", "<br>")%></td>
						<td align="center" width="33%"><b>Diagnostic Code</b><br>
						<%=request.getParameter("dxCode")%></br>
						<%=dxDesc%>
						</td>
						<td valign="top"><b>Refer. Doctor</b><br>
						<%=request.getParameter("referralDocName")%><br>
						<b>Refer. Doctor #</b><br>
						<%=request.getParameter("referralCode")%></td>
					</tr>
				</table>

				</td>
				<td valign="top">

				<table border="1" cellspacing="2" cellpadding="0" width="100%" bordercolorlight="#99A005" bordercolordark="#FFFFFF"
					 class="myGreen">
					<tr>
						<td nowrap width="30%"><b>Billing Physician</b></td>
						<td width="20%"><%=providerBean.getProperty(request.getParameter("xml_provider")!=null?request.getParameter("xml_provider").substring(0,request.getParameter("xml_provider").indexOf("|")):"", "")%></td>
						<td nowrap width="30%"><b>Assig. Physician</b></td>
						<td width="20%"><%=providerBean.getProperty(assgProvider_no, "")%></td>
					</tr>
					<tr>

						<td width="30%"><b>Visit Type</b></td>
						<td width="20%"><%=request.getParameter("xml_visittype").substring(
							request.getParameter("xml_visittype").indexOf("|") + 1)%>
						</td>

						<td width="30%"><b>Billing Type</b></td>
						<td width="20%"><%=request.getParameter("xml_billtype").substring(
							request.getParameter("xml_billtype").indexOf("|") + 1)%>
						</td>
					</tr>
					<tr>
						<td><b>Visit Location</b></td>
						<td colspan="3"><%=request.getParameter("xml_location").substring(
							request.getParameter("xml_location").indexOf("|") + 1)%> &nbsp;
							<% if(request.getParameter("m_review")!=null) { out.println("<b>Manual: Y</b>"); } %>
							</td>
					</tr>
					<tr>
						<td><b>Admission Date</b></td>
						<td><%=request.getParameter("xml_vdate")%></td>
						<td colspan="2"></td>

					</tr>
				</table>


				</td>
			</tr>
		</table>

		</td>

	</tr>
	<tr>
		<td align="center">
		<table border="1" width="100%" bordercolorlight="#99A005" bordercolordark="#FFFFFF">

<%  boolean codeValid = true;
    
    //validation of user entered service codes
    String serviceCodeValue = null;
    for (int i = 0; i < BillingDataHlp.FIELD_SERVICE_NUM; i++) {
	serviceCodeValue = request.getParameter("serviceCode" + i);
	if (!serviceCodeValue.equals("")) {
	    sql = "select distinct(service_code) from billingservice where service_code='" + serviceCodeValue.trim().replaceAll("_","\\_") + "'";
	   System.out.println(sql);
            rs = dbObj.searchDBRecord(sql);
	    if (!rs.next()) {
		codeValid = false;
		%>
		<tr class="myErrorText"><td align=center>
		    &nbsp;<br>
		    Service code "<%=serviceCodeValue%>" is invalid. Please go back to correct it.
		</td></tr>  
		<%
	    }
	}
    }
    
    //validation of diagnostic code (dxcode)
    String dxCodeValue = null;
    for (int i = 0; i < 3; i++) {
	if (i==0) dxCodeValue=request.getParameter("dxCode");
	else dxCodeValue=request.getParameter("dxCode" + i);
	if (!dxCodeValue.equals("")) {
	    sql = "select diagnostic_code from diagnosticcode where diagnostic_code='" + dxCodeValue.trim() +"'";
	    rs = dbObj.searchDBRecord(sql);
	    if (!rs.next()) {
		codeValid = false;
		%>
		<tr class="myErrorText"><td align=center>
		    &nbsp;<br>
		    Diagnostic code "<%=dxCodeValue%>" is invalid. Please go back to correct it.
		</td></tr>
		<%
	    }
	}
    }

    if (codeValid) {
%>

			<%--= msg --%>
			<tr class="myYellow">
				<td colspan='3'>Calculation</td>
				<td>Description</td>
			</tr>
<% }
			//Vector[] vecServiceParam = prepObj.getRequestCodeVec(request, "serviceDate", "serviceUnit", "serviceAt", 8);
			//Vector vecCodeItem = prepObj.getServiceCodeReviewVec(vecServiceParam[0], vecServiceParam[1],
			//				vecServiceParam[2]);
			//Vector vecPercCodeItem = prepObj.getPercCodeReviewVec(vecServiceParam[0], vecCodeItem);
				boolean bPerc = false;
				int n = 0;
				int nCode = 0;
				int nPerc = 0;
				Vector vecPercNo = new Vector();
				Vector vecPercMin = new Vector();
				Vector vecPercMax = new Vector();
				for(int i=0; i<vecServiceParam[0].size(); i++) { 
					//System.out.println("big end: " + vecServiceParam[0].size());
					String codeName = (String)vecServiceParam[0].get(i);
					//System.out.println("big end: " + codeName);
					//String codeItemName = (String) ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeName();
					//System.out.println("codeItemName end: " + (String) ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeName());
					//String PercItemName = (String) ((BillingReviewPercItem)vecPercCodeItem.get(nPerc)).getCodeName();
					if(nCode<vecCodeItem.size() && codeName.equals((String) ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeName())) {
						n++;
						String codeUnit = (String)((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeUnit();
						String codeFee = (String)((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeFee();
						String codeTotal = (String)((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeTotal();
if (codeValid) {
			%>
			<tr class="myGreen">
				<td align='center' width='3%'><%=""+n %></td>
				<td align='right' width='12%'><%=codeName %> (<%=codeUnit %>)</td>
				<td align='right'><%=codeFee %> x <%=codeUnit %> = 
				<input type="text" name="percCodeSubtotal_<%=i %>" size="5" value="<%=codeTotal %>" />
				<input type="hidden" name="xserviceCode_<%=i %>" value="<%=codeName %>" />
				<input type="hidden" name="xserviceUnit_<%=i %>" value="<%=codeUnit %>" />
				</td>
				<td width='25%'><%=propCodeDesc.getProperty(codeName, "") %></td>
			</tr>
<% }
						nCode++;
					} 
					//System.out.println("vecPercCodeItem end: " + codeName);
					else if(nPerc<vecPercCodeItem.size() && codeName.equals((String) ((BillingReviewPercItem)vecPercCodeItem.get(nPerc)).getCodeName())) {
if (codeValid) {
			%>
			<tr class="myPink">
				<td align='center' ><%="&nbsp;" %></td>
				<td align='right' ><%=codeName %> (1)</td>
				<td align='right'>
<% }
						bPerc = true;
						BillingReviewPercItem percItem = (BillingReviewPercItem)vecPercCodeItem.get(nPerc);
						String percFee = percItem.getCodeFee();
						Vector vecPercFee = percItem.getVecCodeFee();
						Vector vecPercTotal = percItem.getVecCodeTotal();
						String codeUnit = (String)percItem.getCodeUnit();
						for(int j=0; j<vecPercTotal.size(); j++) {
							String percTotal = (Float.parseFloat((String)vecPercTotal.get(j)) )*Integer.parseInt(codeUnit) + "";
if (codeValid) {
				%>
						<input type="checkbox" name="percCode_<%=i %>" value="<%=percTotal %>" onclick="onCheckMaster();" /> <%=percTotal %><font size='-2'>(<%=vecPercFee.get(j) %>x<%=percFee %>x<%=codeUnit %>)</font> | 
<% }		} 
if (codeValid) {
				%> = <input type="text" name="percCodeSubtotal_<%=i %>" size="5" value="0.00" />
				<input type="hidden" name="xserviceCode_<%=i %>" value="<%=codeName %>" />
				<input type="hidden" name="xserviceUnit_<%=i %>" value="<%=codeUnit %>" />
				</td>
				<td width='25%'><%=propCodeDesc.getProperty(codeName, "") %>
				</td>
			</tr>
<% }
						nPerc++;
						vecPercNo.add(""+i);
						String nMin = percItem.getCodeMinFee();
						String nMax = percItem.getCodeMaxFee();
						nMin = (nMin == null || "".equals(nMin))? "0" : nMin;
						nMax = (nMax == null || "".equals(nMax))? "9999" : nMax;
						vecPercMin.add(nMin);
						vecPercMax.add(nMax);
					}
					//System.out.println(i + "end: " + nCode);
				}
if (codeValid) {
			%>
			<tr>
				<td align='right' colspan='3' class="myGreen">Total: <input type="text" id="total" name="total" size="5" value="0.00" />
				<input type="hidden" name="totalItem" value="<%=vecServiceParam[0].size() %>" /></td>
<script Language="JavaScript">
<!--
function onCheckMaster() {
<%
	for(int i=0; i<vecPercNo.size(); i++) {
		String iCheckNo = (String)vecPercNo.get(i);
%>
	var nSubtotal = 0.00;
	var nMin = <%=vecPercMin.get(i)%>;
	var nMax = <%=vecPercMax.get(i)%>;
    	//alert(":" + document.forms[0].percCode_<%=iCheckNo%>.type);
    if(document.forms[0].percCode_<%=iCheckNo%>.length == undefined) {
		if (document.forms[0].percCode_<%=iCheckNo%>.checked){ 
			nSubtotal = nSubtotal + eval(document.forms[0].percCode_<%=iCheckNo%>.value*100);
		}
    }
	for (n = 0; n < document.forms[0].percCode_<%=iCheckNo%>.length; n++){
		// If a checkbox has been selected it will return true
		if (document.forms[0].percCode_<%=iCheckNo%>[n].checked){ 
			nSubtotal = nSubtotal + eval(document.forms[0].percCode_<%=iCheckNo%>[n].value*100);
		}
		//alert(nSubtotal+"here:" +document.forms[0].percCode_2.length);
	}
	nSubtotal = Math.round(nSubtotal);
	ssubtotal = nSubtotal/100 + "";
	if(ssubtotal.indexOf(".")<0) {
		ssubtotal = ssubtotal + ".00";
	} else if((ssubtotal.length - ssubtotal.indexOf('.')) <= 2) {
		//alert(ssubtotal.length + " : " + ssubtotal.indexOf("."));
		ssubtotal = ssubtotal + "00".substring(0, (ssubtotal.length - ssubtotal.indexOf('.') - 1));
	}
	document.forms[0].percCodeSubtotal_<%=iCheckNo%>.value = ssubtotal; 
	if(nMin > document.forms[0].percCodeSubtotal_<%=iCheckNo%>.value) {
		document.forms[0].percCodeSubtotal_<%=iCheckNo%>.value = nMin;
	} else if (nMax < document.forms[0].percCodeSubtotal_<%=iCheckNo%>.value) {
		document.forms[0].percCodeSubtotal_<%=iCheckNo%>.value = nMax;
	}
<%	}
%>
	nSubtotal = 0.00;
    for (var i =0; i <document.forms[0].elements.length; i++) {
        if (document.forms[0].elements[i].name.indexOf("percCodeSubtotal") >=0 ) {
			nSubtotal = nSubtotal + document.forms[0].elements[i].value*10*10;
    	}
    	//alert(i + ":" + nSubtotal);
	}
	stotal = nSubtotal/100 + "";
	if(stotal.indexOf(".")<0) {
		stotal = stotal + ".00";
	} else if((stotal.length - stotal.indexOf('.')) <= 2) {
		//alert(stotal.length + " : " + stotal.indexOf("."));
		stotal = stotal + "00".substring(0, (stotal.length - stotal.indexOf('.') - 1));
	}
	document.forms[0].total.value = stotal; 
}
	var ntotal = 0.00;
    for (var i =0; i <document.forms[0].elements.length; i++) {
        if (document.forms[0].elements[i].name.indexOf("percCodeSubtotal") >=0 ) {
			ntotal = ntotal + (document.forms[0].elements[i].value*10*10);
			//alert(":::" + document.forms[0].elements[i].value*10*10);
    	}
    	//alert(ntotal);
	}
	stotal = ntotal/100 + "";
	//alert(stotal);
	if(stotal.indexOf(".")<0) {
		stotal = stotal + ".00";
	} else if((stotal.length - stotal.indexOf('.')) <= 2) {
		//alert(stotal.length + " : " + stotal.indexOf("."));
		stotal = stotal + "00".substring(0, (stotal.length - stotal.indexOf('.') - 1));
	}
	document.forms[0].total.value = stotal; 

-->
</script>

			</tr>
<% } %>
			<tr>

				<td colspan='3' align='center' bgcolor="silver">
                                    <input type="submit" name="button" value="Back to Edit" style="width: 120px;" />
<% if (codeValid) { %>
                                    <input type="submit" name="submit" value="Save" style="width: 120px;" onClick="onClickSave();"/>
				    <input type="submit" name="submit" value="Save & Add Another Bill" onClick="onClickSave();"/>
<% } %>
                                </td>
			</tr>
		</table>
		</td>
	</tr>
<% if (codeValid) {
   if(request.getParameter("xml_billtype")!=null && request.getParameter("xml_billtype").matches("ODP.*|WCB.*|NOT.*")) { %>
	<tr>
			<td >
			Billing Notes:<br>
			<%
            OscarProperties props = OscarProperties.getInstance();
            boolean bMoreAddr = props.getProperty("scheduleSiteID", "").equals("") ? false : true;
			String tempLoc = "";
            if(bMoreAddr) {
            	//String pNo = request.getParameter("xml_provider")!=null?request.getParameter("xml_provider").substring(0,request.getParameter("xml_provider").indexOf("|")):"";
            	//System.out.println(";;; " + request.getParameter("service_date"));
            	tempLoc = request.getParameter("siteId").trim();
            }
			%>
			<textarea name="comment" cols=60 rows=4><%=tempLoc %></textarea>
			</td>
	</tr>
<% } } %>
<%
if(request.getParameter("xml_billtype")!=null && !request.getParameter("xml_billtype").matches("ODP.*|WCB.*|NOT.*")) {
	JdbcBillingPageUtil pObj = new JdbcBillingPageUtil();
	List al = pObj.getPaymentType();
	
	Billing3rdPartPrep privateObj = new Billing3rdPartPrep();
	//Properties propClinic = privateObj.getLocalClinicAddr();
        oscar.oscarRx.data.RxProviderData.Provider provider = new oscar.oscarRx.data.RxProviderData().getProvider((String) session.getAttribute("user"));
	 
                /*
                = propClinic.getProperty("clinic_name", "") + "\n" 
		+ propClinic.getProperty("clinic_address", "") + "\n" 
		+ propClinic.getProperty("clinic_city", "") + ", " + propClinic.getProperty("clinic_province", "") + "\n" 
		+ propClinic.getProperty("clinic_postal", "") + "\n" 
		+ "Tel: " + propClinic.getProperty("clinic_phone", "") + "\n" 
		+ "Fax: " + propClinic.getProperty("clinic_fax", "") ;
                */
        String strClinicAddr = provider.getClinicName().replaceAll("\\(\\d{6}\\)","") +"\n"
                             + provider.getClinicAddress() +"\n"
                             + provider.getClinicCity() +","+ provider.getClinicProvince()+"\n"
                             + provider.getClinicPostal() +"\n"
                             + "Tel: "+provider.getClinicPhone() +"\n"
                             + "Fax: "+provider.getClinicFax() ;
                
if (codeValid) { %>
<tr><td>
		<table border="1" width="100%" bordercolorlight="#99A005" bordercolordark="#FFFFFF">
			<tr class="myYellow">
				<td colspan='2'>Private Billing</td>
			</tr>
			<tr><td width="80%">
			
			<table border="0" width="100%" >
			<tr><td>Bill To [<a href=# onclick="scriptAttach('billto'); return false;">Search</a>]<br>
			<textarea name="billto" value="" cols=30 rows=6><%=strPatientAddr %></textarea></td>
			<td>Remit To<br>
			<textarea name="remitto" value="" cols=30 rows=6><%=strClinicAddr %></textarea></td>
			</tr>
			</table>
			<table border="0" width="100%" >
			<tr>
			<td >
			Billing Notes:<br>
			<textarea name="comment" value="" cols=60 rows=4></textarea>
			</td>
			<td align="right">
                        <% if (flag.equals("1")) { %> 
 	                GST <%=percent%>%:<input type="text" disabled id="gsttotal" value="0.00" size="6" /><br> 
 	                <%}%> 
 	                Total:<input type="text" disabled id="finaltotal" value="0.00" size="6" /><br>    
			Payments:<input type="text" name="payment" value="0.00" size="6" onDblClick="settlePayment();" /><br/>
			Refunds:<input type="text" name="refund" value="0.00" size="6"/>
			</td>
			</tr>
			</table>
			
			<td class="myGreen">
			Payment Method:<br/>
			<% for(int i=0; i<al.size(); i=i+2) { %>
			    <input type="radio" name="payMethod" value="<%=al.get(i) %>"/><%=al.get(i+1) %><br/>
			<% } %>
			</td></tr>
			<tr>
				<td colspan='2' align='center' bgcolor="silver"><input type="submit" name="submit" value="Save & Print Invoice" 
					style="width: 120px;" /><input type="submit" name="submit" 
					value="Settle & Print Invoice" onClick="document.forms['titlesearch'].btnPressed.value='Settle'; document.forms['titlesearch'].submit();javascript:popupPage(700,720,'billingON3rdInv.jsp');" style="width: 120px;" /> 
				<input type="hidden"  name="btnPressed" value="">
				</td>
			</tr>
		</table>

</td></tr>
<% } } %>
	<%for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
				String temp = e.nextElement().toString();
%>
	<input type="hidden" name="<%= temp %>" value="<%=StringEscapeUtils.escapeHtml(request.getParameter(temp))%>" />
	<%}

		%>
	</form>

</table>
<% if(bPerc) { out.println("* Click the code you want the % code to apply to [1 or 2 ...]."); } %>
</body>
</html>
