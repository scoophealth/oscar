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
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@page import="org.oscarehr.common.dao.BillingServiceDao"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.dao.DxresearchDAO" %>
<%@page import="org.oscarehr.common.model.Dxresearch" %>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%! boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page errorPage="errorpage.jsp"
	import="java.util.*,java.math.*,java.net.*,java.sql.*,oscar.util.*,oscar.*,oscar.appt.*"%>
<%@ page import="oscar.oscarBilling.ca.on.administration.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*, java.util.Properties"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.DiagnosticCode" %>
<%@ page import="org.oscarehr.common.dao.DiagnosticCodeDao" %>
<%@ page import="org.oscarehr.common.dao.BillingONCHeader1Dao, org.oscarehr.common.model.BillingONCHeader1" %>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script>
    jQuery.noConflict();
</script>


<%
	DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);
        BillingONCHeader1Dao billingONCHeader1Dao = (BillingONCHeader1Dao) SpringUtils.getBean("billingONCHeader1Dao");
%>
<%//
	

			String user_no = (String) session.getAttribute("user");
			String providerview = request.getParameter("providerview") == null ? "" : request
					.getParameter("providerview");
			String asstProvider_no = "";
			String color = "";
			String premiumFlag = "";
			String service_form = "";
			String prevId = "";
            if (request.getParameter("prevId") != null) {
            	prevId = request.getParameter("prevId");
            }
%>

<%
Properties gstProp = new Properties();
GstControlAction db = new GstControlAction();
GstReport gstRep = new GstReport();
gstProp = db.readDatabase();
String gstFlag;
String flag = gstProp.getProperty("gstFlag", "");
String percent = gstProp.getProperty("gstPercent", "");
BigDecimal stotal = new BigDecimal(0);
BigDecimal gstTotal = new BigDecimal(0);
BigDecimal gstbilledtotal = new BigDecimal(0);
boolean dupServiceCode = false;
%>

<%//
	BillingReviewPrep prepObj = new BillingReviewPrep();
	@SuppressWarnings("unchecked")
	Vector<String>[] vecServiceParam = new Vector[3];
	if(oscarVariables.getProperty("onBillingSingleClick", "").equals("yes")) {
		vecServiceParam[0] = new Vector<String>();
		vecServiceParam[1] = new Vector<String>();
		vecServiceParam[2] = new Vector<String>();
	} else {
		vecServiceParam = prepObj.getRequestFormCodeVec(request, "xml_", "1", "1");
	}

	Vector<String>[] vecServiceParam0 = prepObj.getRequestCodeVec(request, "serviceCode", "serviceUnit", "serviceAt", BillingDataHlp.FIELD_SERVICE_NUM);
	vecServiceParam[0].addAll(vecServiceParam0[0]);
	vecServiceParam[1].addAll(vecServiceParam0[1]);
	vecServiceParam[2].addAll(vecServiceParam0[2]);

        //Check whether there are duplicated service code existing
        //User double click a service code, and then check off that
        //service code in billingON page will cause duplicated service
        //code in billing review page.
        TreeMap<String,Integer> mapServiceParam = new TreeMap<String,Integer>();
        for (int i = 0 ; i< vecServiceParam[0].size(); i++){
            mapServiceParam.put(vecServiceParam[0].get(i),i);
        }
        if (mapServiceParam.size()!=vecServiceParam[0].size())
            dupServiceCode = true;

        /////// hack used to order the billing codes
        /////// Would make sense to change getServiceCodeReviewVec method to accept the hashtable
        /////// But should cause that much of a performance hit. It's generally under 3 items
        String billReferalDate = request.getParameter("service_date");
        Vector v = new Vector();
        for (int ii = 0 ; ii< vecServiceParam[0].size(); ii++){
            Hashtable h = new Hashtable();
            h.put("serviceCode",vecServiceParam[0].get(ii));
            h.put("serviceUnit",vecServiceParam[1].get(ii));
            h.put("serviceAt",vecServiceParam[2].get(ii));
            h.put("billReferenceDate", billReferalDate);
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

    String warningMsg = "";
	String errorFlag = "";
	String errorMsg = "";
        
	Vector vecCodeItem = prepObj.getServiceCodeReviewVec(vecServiceParam[0], vecServiceParam[1],vecServiceParam[2],billReferalDate);
	Vector vecPercCodeItem = prepObj.getPercCodeReviewVec(vecServiceParam[0], vecServiceParam[1], vecCodeItem,billReferalDate);  //LINE CAUSING ERROR


        Properties propCodeDesc = (new JdbcBillingCodeImpl()).getCodeDescByNames(vecServiceParam[0]);
			String dxCode = request.getParameter("dxCode");
			String dxDesc = prepObj.getDxDescription(dxCode);
			String clinicview = oscarVariables.getProperty("clinic_view", "");
			String clinicNo = oscarVariables.getProperty("clinic_no", "");
			String visitType = oscarVariables.getProperty("visit_type", "");
			String appt_no = request.getParameter("appointment_no");
			String demoname = request.getParameter("demographic_name");
			String demo_no = request.getParameter("demographic_no");
			String apptProvider_no = request.getParameter("apptProvider_no");
			String ctlBillForm = request.getParameter("billForm");
			String assgProvider_no = request.getParameter("assgProvider_no");
			String billType = request.getParameter("xml_billtype").substring(0,((String)request.getParameter("xml_billtype")).indexOf("|")).trim();
			String demoSex = request.getParameter("DemoSex");
			GregorianCalendar now = new GregorianCalendar();
			int curYear = now.get(Calendar.YEAR);
			int curMonth = (now.get(Calendar.MONTH) + 1);
			int curDay = now.get(Calendar.DAY_OF_MONTH);
			int dob_year = 0, dob_month = 0, dob_date = 0, age = 0;
			String content = "";
			String total = "";
			
			//add to patientDx (or not)
			if ("yes".equals(request.getParameter("addToPatientDx"))) {
				String dxCodeMatch = request.getParameter("codeMatchToPatientDx");
				String dxCodeAdd = dxCodeMatch.isEmpty() ? dxCode : dxCodeMatch;
				
				DxresearchDAO dxresearchDao = SpringUtils.getBean(DxresearchDAO.class);
				java.util.Date d = new java.util.Date();
				Dxresearch dx = new Dxresearch(Integer.valueOf(demo_no), d, d, 'A', dxCodeAdd, "icd9", (byte)0, user_no);
				dxresearchDao.save(dx);
			}

			String msg = "<tr><td colspan='2'>Calculation</td></tr>";
			String action = "edit";
			Properties propHist = null;
			Vector vecHist = new Vector();
			// get provider's detail
			String proOHIPNO = "", proRMA = "";
			
			ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
			Provider ppp = providerDao.getProvider(request.getParameter("xml_provider"));
			if (ppp != null) {
				proOHIPNO = ppp.getOhipNo(); 
				proRMA = ppp.getRmaNo();
			}
			if (request.getParameter("xml_provider") != null)
				providerview = request.getParameter("xml_provider");
			// get patient's detail
			String r_doctor = "", r_doctor_ohip = "";
			String demoFirst = "", demoLast = "", demoHIN = "", demoVer = "", demoDOB = "", demoDOBYY = "", demoDOBMM = "", demoDOBDD = "", demoHCTYPE = "";
			String strPatientAddr = "";
			
			DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
			Demographic demo = demoDao.getDemographic(demo_no);
			if (demo != null) {
				strPatientAddr = demo.getFirstName() + " " + demo.getLastName() + "\n"
				+ demo.getAddress() + "\n"
				+ demo.getCity() + ", " + demo.getProvince() + "\n"
				+ demo.getPostal() + "\n"
				+ "Tel: " + demo.getPhone();

				assgProvider_no = demo.getProviderNo();
				demoFirst = demo.getFirstName();
				demoLast = demo.getLastName();
				demoHIN = demo.getHin();
				demoVer = demo.getVer();
				demoSex = demo.getSex();
				if (demoSex.compareTo("M") == 0)
					demoSex = "1";
				if (demoSex.compareTo("F") == 0)
					demoSex = "2";

				demoHCTYPE = demo.getHcType() == null ? "" : demo.getHcType();
				if (demoHCTYPE.compareTo("") == 0 || demoHCTYPE == null || demoHCTYPE.length() < 2) {
					demoHCTYPE = "ON";
				} else {
					demoHCTYPE = demoHCTYPE.substring(0, 2).toUpperCase();
				}
				demoDOBYY = demo.getYearOfBirth();
				demoDOBMM = demo.getMonthOfBirth();
				demoDOBDD = demo.getDateOfBirth();

				if (demo.getFamilyDoctor() == null) {
					r_doctor = "N/A";
					r_doctor_ohip = "000000";
				} else {
					r_doctor = SxmlMisc.getXmlContent(demo.getFamilyDoctor(), "rd") == null ? "" : SxmlMisc
							.getXmlContent(demo.getFamilyDoctor(), "rd");
					r_doctor_ohip = SxmlMisc.getXmlContent(demo.getFamilyDoctor(), "rdohip") == null ? ""
							: SxmlMisc.getXmlContent(demo.getFamilyDoctor(), "rdohip");
				}

				demoDOBMM = demoDOBMM.length() == 1 ? ("0" + demoDOBMM) : demoDOBMM;
				demoDOBDD = demoDOBDD.length() == 1 ? ("0" + demoDOBDD) : demoDOBDD;
				demoDOB = demoDOBYY + demoDOBMM + demoDOBDD;

				if (demo.getHin() == null) {
					errorFlag = "1";
					errorMsg = errorMsg
							+ "<br><div class='myError'>Error: The patient does not have a valid HIN. </div><br>";
				} else if (demo.getHin().equals("")) {
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

			%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="demographicNo" value="${param.demographic_no}" scope="request"/>
<oscar:customInterface section="billingreview"/>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OscarBilling</title>
<link rel="stylesheet" type="text/css" href="billingON.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="billingReview"/>
<script language="JavaScript">
        ctx = "<c:out value="${ctx}"/>";
    	demographicNo = "<c:out value="${demographicNo}"/>";
    
		var bClick = false;
	    
		function onSave() {
		var value=jQuery("#payee").val();
		jQuery("#payeename").val(value);
            var ret = checkTotal();
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
		  document.forms[0].payment.value = document.forms[0].total.value;
		}

		function scriptAttach(elementName) {
		     var d = elementName;
		     //t0 = escape("document.forms[0].elements[\'"+d+"\'].value");
		     t0 = d;
		     popupPage('600', '700', 'onSearch3rdBillAddr.jsp?param='+t0);
		}
                function showtotal(){
                	document.getElementById('payMethod_0').checked=true;
                    var subtotal = document.getElementById("total").value;
                    //subtotal = subtotal * 1 + document.getElementById("gst").value * 1;
                    var element = document.getElementById("stotal");
                    if( element != null )
                        element.value = subtotal;
                }
                
                function validatePaymentNumberic(idx) {
                	var oldVal = document.getElementById("percCodeSubtotal_" + idx).value;
                	var val = document.getElementById("paid_" + idx).value;
                	/* if (val.length == 0) {
                		document.getElementById("paid_" + idx).value = "0.00";
                		oldVal = "0.00";
                		return;
                	} */
                	//var regexNumberic = /^([1-9]\d*|0)(\.\d{1,2})?$/;
                	var regexNumberic = /^([1-9]\d{0,9}|0)(\.\d{1,2})?$/;
                	if (!regexNumberic.test(val)) {
                		document.getElementById("paid_" + idx).value = oldVal;
                		alert("Please enter digital numbers !");
                		return;
                	}
                	oldVal = val;
		}


                function validateDiscountNumberic(idx) {
                	var oldVal = "0.00";
                	var val = document.getElementById("discount_" + idx).value;
                	if (val.length == 0) {
                		document.getElementById("discount_" + idx).value = "0.00";
                		oldVal = "0.00";
                		return;
                	}
                	//var regexNumberic = /^([1-9]\d*|0)(\.\d{1,2})?$/;
                	var regexNumberic = /^([1-9]\d{0,9}|0)(\.\d{1,2})?$/;
                	if (!regexNumberic.test(val)) {
                		document.getElementById("discount_" + idx).value = oldVal;
                		alert("Please enter digital numbers !");
                		return;
                	}
                	oldVal = val;
                }
                
                function validateFeeNumberic(idx) {
                	var oldVal = "0.00";
                	var val = document.getElementById("percCodeSubtotal_" + idx).value;
                	if (val.length == 0) {
                		document.getElementById("percCodeSubtotal_" + idx).value = "0.00";
                		oldVal = "0.00";
                		return;
                	}
                	//var regexNumberic = /^([1-9]\d*|0)(\.\d{1,2})?$/;
                	var regexNumberic = /^([1-9]\d{0,9}|0)(\.\d{1,2})?$/;
                	if (!regexNumberic.test(val)) {
                		document.getElementById("percCodeSubtotal_" + idx).value = oldVal;
                		alert("Please enter digital numbers !");
                		return;
                	}
                	oldVal = val;
                }


                
       function updateElement(eId, data) {
    	   jQuery("#"+eId).val(data);
       }
       
       function checkTotal() {
    	   var totValue = document.getElementById("total").value;
    	   if(isNaN(totValue)) {
    		   alert("Please enter a valid fee");
    		   return false;
    	   }
    	   return true;
       }
       
       function updateTotal(e) {
    	   var editedValue = e.value;
    	   if( isNaN(editedValue) ) {
    		   alert("Please enter a valid fee");
    		   e.focus();
    	   }
    	   else {
    			var codeFees;
    			var unit;
    			var total = 0.0;
    			var idx = 0;
    			var displayTotal = "0.00";
    			
    			while( (codeFees = document.getElementById("percCodeSubtotal_" + idx)) ) {    				    			
    				total += parseInt(codeFees.value);
    				++idx;
    			}
    		    			
    			updateElement("total", formatTotal(total));    			    			
    			total += new Number(jQuery("#gst").val());    			
    			updateElement("gstBilledTotal", formatTotal(total));
    			
    	   }
    	   
       }
       
       function formatTotal(total) {
    	   var displayTotal = "0.00";
    	   var decimal = total % 1;
    	   
    	   if( decimal == 0 ) {
				displayTotal = total + ".00";
			}
			else if( (decimal*10) % 1 < 0.1 ) {
				displayTotal = total + "0";
			}
			else {
				displayTotal = total;
			}
    	   
    	   return displayTotal;
       }
       
       function checkPaymentMethod(settle) {
    	   var payMethods = document.getElementsByName("payMethod");
    	   var checkedMethod = false;
    	   
    	   if( settle != "Settle" && document.forms[0].payment.value == 0 ) {
    		   return true;
    	   }
    	   
    	   
    	   for( var idx = 0; idx < payMethods.length; ++idx ) {
    		   if( payMethods[idx].checked ) {
    			   checkedMethod = true;
    			   break;
    		   }
    	   }
    	   
    	   if( !checkedMethod ) {
    		   alert("Please select a payment method");
    	   }
    	   else if( settle == "Settle") {
    		   document.forms['titlesearch'].btnPressed.value='Settle';
    		   document.forms['titlesearch'].submit();
    		   popupPage(700,720,'billingON3rdInv.jsp');
    	   }
    	   
    	   return checkedMethod;
    	   
       }

	//-->

</script>

<style type="text/css">
div.wrapper{
    background-color: #eeeeff;
    margin-top:0px;
    padding-top:0px;
    margin-bottom:0px;
    padding-bottom:0px;
}

div.wrapper br{
    clear: left;
}

div.wrapper ul{
    width: 80%;
    background-color: #eeeeff;
    list-style:none;
    list-style-type:none;
    list-style-position:outside;
    padding-left:1px;
    margin-left:1px;
    margin-top:0px;
    padding-top:1px;
    margin-bottom:0px;
    padding-bottom:0px;
}

div.wrapper ul li{
    background-color: #eeeeff;
}

div.dxBox{
    width:90%;
    background-color: #eeeeff;
    margin-top: 2px;
    margin-left:3px;
    margin-right:3px;
    margin-bottom:0px;
    padding-bottom:0px;
    float: left;
}


div.dxBox h3 {
    background-color: #ccccff;
  /*font-size: 1.25em;*/
    font-size: 10pt;
    font-variant:small-caps;
    font-weight: bold;
    margin-top:0px;
    padding-top:0px;
    margin-bottom:0px;
    padding-bottom:0px;
}


div.dxBox form {
    margin-top:0px;
    padding-top:0px;
    margin-bottom: 0px;
    padding-bottom: 0px;
}

div.dxBox input {
    margin-top:0px;
    padding-top:0px;
    margin-bottom: 0px;
    padding-bottom: 0px;
}

</style>
<script type="text/javascript" src="../../../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../../../share/javascript/nifty.js"></script>
<link rel="stylesheet" type="text/css" href="../../../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css" href="../../../share/css/niftyPrint.css" media="print"/>

<script type="text/javascript">
window.onload=function(){
          if(!NiftyCheck())
                      return;
          Rounded("div.dxBox","top","transparent","#CCCCFF","small border #CCCCFF");
          Rounded("div.dxBox","bottom","transparent","#EEEEFF","small border #CCCCFF");
}
</script>

</head>

<body topmargin="0" onload="showtotal(),calculatePayment()">

<form method="post" name="titlesearch" action="billingONSave.jsp" onsubmit="return onSave();">
    <input type="hidden" name="url_back" value="<%=request.getParameter("url_back")%>">
    <input type="hidden" name="billNo_old" id="billNo_old" value="<%=request.getParameter("billNo_old")%>" />
	<input type="hidden" name="billStatus_old" id="billStatus_old" value="<%=request.getParameter("billStatus_old")%>" />
	<input type="hidden" name="billForm" id="billForm" value="<%=request.getParameter("billForm")%>" />
    <input type="hidden" name="payeename" id="payeename" value="" />
    <input type="hidden" name="prevId" id="prevId" value="<%=prevId %>"/>
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
						<!--<input type="text" name="checkFlag" id="checkFlag" value="<%=request.getParameter("checkFlag") %>" />  -->
						<td nowrap width="30%" align="center" ><b>Service Date</b><br>
						<%=request.getParameter("service_date").replaceAll("\\n", "<br>")%></td>
						<td align="center" width="33%"><b>Diagnostic Code</b><br>
						<%=dxCode%></br>
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
						<td width="20%"><%=assgProvider_no == null ? "N/A" : providerBean.getProperty(assgProvider_no, "")%></td>
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
						<td><%=request.getParameter("xml_location").substring(
							request.getParameter("xml_location").indexOf("|") + 1)%> &nbsp;
							<% if(request.getParameter("m_review")!=null) { out.println("<b>Manual: Y</b>"); } %>
							</td>

					<% if (bMultisites) { %>
						<td width="30%"><b>Billing Clinic</b></td>
						<td width="20%" nowrap="nowrap"><%=request.getParameter("site")%>
						</td>
					<% } %>
					</tr>
					<tr>
                   		<td><b>SLI Code</b></td>
                        <td><%=request.getParameter("xml_slicode").substring(request.getParameter("xml_slicode").indexOf("|") + 1)%> &nbsp;</td>
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

    //validation that user hasn't already had billed to OHIP an annual physical this year
    String serviceCodeValue = null;
    int srvCodeIdx = 0;
    while (codeValid && (srvCodeIdx < BillingDataHlp.FIELD_SERVICE_NUM)) {
         
         serviceCodeValue = request.getParameter("serviceCode" + srvCodeIdx);
         //Only worry about this check if we are billing OHIP for A003
         if (serviceCodeValue.equals("A003A") && request.getParameter("xml_billtype").matches("ODP.*")) {
            BillingONCHeader1 bCh1 = billingONCHeader1Dao.getLastOHIPBillingDateForServiceCode(Integer.parseInt(demo_no),"A003A");
            if (bCh1 != null) {                
                Calendar serviceDateCal = Calendar.getInstance();                
                java.util.Date serviceDate = null;               
                try {                   
                    serviceDate = oscar.util.DateUtils.parseDate(request.getParameter("service_date"),request.getLocale()); 
                    serviceDateCal.setTime(serviceDate);                    
                } catch (java.text.ParseException e) {}
                
                Calendar nextBillDateCal = Calendar.getInstance();               
                nextBillDateCal.setTime(bCh1.getBillingDate());
                //year plus a day
                nextBillDateCal.add(Calendar.YEAR,1); 
                nextBillDateCal.add(Calendar.DATE,1);
                if (nextBillDateCal.after(serviceDateCal)) {
                      codeValid = false;
    %>
                       <tr style="color:white">
                            <td align=center>
                                <div class='myError'>
                                  (<bean:message key="oscar.billing.ca.on.billingON.review.invoiceNo"/><%=String.valueOf(bCh1.getId())%>) A003A - <bean:message key="oscar.billing.ca.on.billingON.review.msgServiceCodeAlreadyBilled"/>                                
                                </div>
                            </td>
                       </tr>
    <%               
                }
            }
        }
        srvCodeIdx++;
    }
    
    //validation of user entered service codes    
    serviceCodeValue = null;
    for (int i = 0; i < BillingDataHlp.FIELD_SERVICE_NUM; i++) {
	serviceCodeValue = request.getParameter("serviceCode" + i);

	if (!serviceCodeValue.equals("")) {
		BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class); 
		
		List<Object> svcCodes = billingServiceDao.findBillingCodesByCodeAndTerminationDate(serviceCodeValue.trim().replaceAll("_","\\_"),
				ConversionUtils.fromDateString(billReferalDate));
	    
	    if (svcCodes.isEmpty()) {
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
	if (i==0) dxCodeValue=dxCode;
	else dxCodeValue=request.getParameter("dxCode" + i);
	if (!dxCodeValue.equals("")) {
		List<DiagnosticCode> dcodes = diagnosticCodeDao.findByDiagnosticCode(dxCodeValue.trim());
		if(dcodes.size() == 0) {
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
				<%if(!"PAT".equals(billType)){%>
				<td>Description</td>
				<%}else{%>
				<td width="14%">Description</td><td width="3%">Payment</td><td width="3%">Discount</td>
				<%}%>
			</tr>
<%  }
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
					String codeName = vecServiceParam[0].get(i);
					if(nCode<vecCodeItem.size() && codeName.equals( ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeName())) {
						n++;
						String codeDescription = ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeDescription();
						String codeUnit = ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeUnit();
						String codeFee = ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeFee();
						String codeTotal = ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeTotal();
                        String strWarning = ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getMsg();
                        String codeAt = ((BillingReviewCodeItem)vecCodeItem.get(nCode)).getCodeAt();
                                                gstFlag = gstRep.getGstFlag(codeName,billReferalDate);  // Retrieve whether the code has gst involved
                                                BigDecimal cTotal = new BigDecimal(codeTotal);
                                                if ( gstFlag.equals("1") ){   // If it does, update the total with the gst calculated
                                                    BigDecimal perc = new BigDecimal(percent);
                                                    BigDecimal hund = new BigDecimal(100);
                                                    stotal = cTotal;
                                                    stotal = stotal.multiply(perc);
                                                    stotal = stotal.divide(hund);
                                                    gstTotal = gstTotal.add(stotal).setScale(2, BigDecimal.ROUND_HALF_UP);  // Total up GST Charged
                                                    stotal = stotal.add(cTotal).setScale(2, BigDecimal.ROUND_HALF_UP); // Finally update the new codeTotal
                                                    codeTotal = stotal + "";
                                                    BigDecimal temp = new BigDecimal(codeTotal);
                                                    gstbilledtotal = gstbilledtotal.add(temp).setScale(2, BigDecimal.ROUND_HALF_UP);
                                                }
                                                else {
                                                    gstbilledtotal = gstbilledtotal.add(cTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
                                                }
                        if (codeValid) {
			%>
			<tr class="myGreen">
				<td align='center' width='3%'><%=""+n %></td>
				<td align='right' width='12%'><%=codeName %> (<%=codeUnit %>)</td>
				<td>
                    <% if( strWarning.length() > 0 ) { %>
                    <span style="color:red; float:left;"><%=strWarning%></span>
                    <%}%>
                    <span style="float:right;"> <%=codeFee %> x <%=codeUnit %><% if (gstFlag.equals("1")){%> + <%=percent%>% GST<%}%> =
				<input type="text" name="percCodeSubtotal_<%=i %>" size="5" value="<%=codeTotal %>" id="percCodeSubtotal_<%=i %>" onBlur="calculateTotal();" onchange="validateFeeNumberic(<%=i%>)"/>
				<input type="hidden" name="xserviceCode_<%=i %>" value="<%=codeName %>" />
				<input type="hidden" id="xserviceUnit_<%=i %>" name="xserviceUnit_<%=i %>" value="<%=codeUnit %>" />
                    </span>
				</td>
				<%if(!"PAT".equals(billType)){%>
					<td width='25%'><%=propCodeDesc.getProperty(codeName, "") %></td>
				<%}else{
				String paid_value = "0.00";
				%>
				<oscar:oscarPropertiesCheck property="BILLING_REVIEW_AUTO_PAYMENT" value="yes">
				<%paid_value = codeTotal; %>
				</oscar:oscarPropertiesCheck>
				<td nowrap width='14%'><pre><%=codeDescription%></pre></td>
				<td nowrap width='3%'><input type="text" id="paid_<%=i%>" name="paid_<%=i %>" value="<%=paid_value%>" onBlur="calculatePayment();" onchange="validatePaymentNumberic(<%=i %>)"/></td>
				<td nowrap width='3%'><input type="text" id="discount_<%=i%>" name="discount_<%=i %>" value="0.00" onBlur="calculateDiscount();" onchange="validateDiscountNumberic(<%=i %>)"/></td>
				<%}%>
			</tr>
			<%
                        }
						nCode++;
					}
					else if(nPerc<vecPercCodeItem.size() && codeName.equals( ((BillingReviewPercItem)vecPercCodeItem.get(nPerc)).getCodeName())) {
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
						String codeUnit = percItem.getCodeUnit();
						for(int j=0; j<vecPercTotal.size(); j++) {
							String percTotal = (Float.parseFloat((String)vecPercTotal.get(j)) )*Integer.parseInt(codeUnit) + "";
				if (codeValid) {
                                                        %>
						<input type="checkbox" name="percCode_<%=i %>" value="<%=percTotal %>" onclick="onCheckMaster();" /> <%=percTotal %><font size='-2'>(<%=vecPercFee.get(j) %>x<%=percFee %>x<%=codeUnit %>)</font> |
				<%
                                }
                                                }
                                if (codeValid) {
				%> = <input type="text" name="percCodeSubtotal_<%=i %>" size="5" value="0.00" />
				<input type="hidden" name="xserviceCode_<%=i %>" value="<%=codeName %>" />
				<input type="hidden" name="xserviceUnit_<%=i %>" value="<%=codeUnit %>" />
				</td>
				<td width='25%'><%=propCodeDesc.getProperty(codeName, "") %>
				</td>
			</tr>
			<%
                                }
						nPerc++;
						vecPercNo.add(""+i);
						String nMin = percItem.getCodeMinFee();
						String nMax = percItem.getCodeMaxFee();
						nMin = (nMin == null || "".equals(nMin))? "0" : nMin;
						nMax = (nMax == null || "".equals(nMax))? "9999" : nMax;
						vecPercMin.add(nMin);
						vecPercMax.add(nMax);
					}
				}
                        if (codeValid) {
			%>
			<tr>
				<td align='right' colspan='3' class="myGreen">Total: <input type="text" id="total" name="total" size="5" value="0.00" onchange="onTotalChanged();"/>
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
        var num = new Number(stotal);
	document.forms[0].total.value = num.toFixed(2);
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
        var num = new Number(stotal);
	document.forms[0].total.value = num.toFixed(2);

-->
</script>

			</tr>
                        <% } %>
			<tr>

				<td colspan='3' align='center' bgcolor="silver">
				    <input type="submit" name="button" value="Back to Edit" style="width: 120px;" />
                                    <% if (codeValid && !dupServiceCode) { %>
                                    <input type="submit" name="submit" value="Save" style="width: 120px;" onClick="onClickSave();"/>
				    <input type="submit" name="submit" value="Save & Add Another Bill" onClick="onClickSave();"/>
                                    <% }else if (dupServiceCode){%>
                                    <td><div class='myError'>Warning: Duplicated service codes. </div></td>
                                    <% }
                                    %>
                                    </td>
			</tr>
		</table>
		</td>
	</tr>
<% if (codeValid) {
        if(request.getParameter("xml_billtype")!=null && request.getParameter("xml_billtype").matches("ODP.*|WCB.*|NOT.*|BON.*")) { %>
	<tr>
			<td >
			Billing Notes:<br>
			<%
			String tempLoc = "";
		if (!bMultisites) {
            OscarProperties props = OscarProperties.getInstance();
            boolean bMoreAddr = props.getProperty("scheduleSiteID", "").equals("") ? false : true;
            if(bMoreAddr) {
            	tempLoc = request.getParameter("siteId").trim();
            } else {
            	tempLoc = props.getProperty("BILLING_NOTE","");
            }
		} else {
			tempLoc = request.getParameter("site");
		}
			%>
			<textarea name="comment" cols=60 rows=4><%=tempLoc %></textarea>
			</td>
	</tr>
<%      }
  }     %>
<%//
if(request.getParameter("xml_billtype")!=null && !request.getParameter("xml_billtype").matches("ODP.*|WCB.*|NOT.*|BON.*")) {
	JdbcBillingPageUtil pObj = new JdbcBillingPageUtil();
	List al = pObj.getPaymentType();

	Billing3rdPartPrep privateObj = new Billing3rdPartPrep();
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

<%
// for satellite clinics
String clinicAddress = null;
// get Site ID from billingON.jsp
if (bMultisites) {
	String siteName = request.getParameter("site");
	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
  	List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));
  	Site s = ApptUtil.getSiteFromName(sites, siteName);

  	if (s==null)
  		clinicAddress = strClinicAddr;
  	else {
  		clinicAddress = s.getName()+"\n"+s.getAddress()+"\n"+s.getCity()+", "+s.getProvince()+" "+s.getPostal()+"\nTel: "+s.getPhone()+"\nFax: "+s.getFax();
  	}

} else {
	String siteID = request.getParameter("siteId");
	OscarProperties props2 = OscarProperties.getInstance();
	if(props2.getProperty("clinicSatelliteCity") != null) {
	    //compare the site id with clinicSatelliteCity to get the current address index
	    //in properties file  clinicSatelliteCity and scheduleSiteID must have same value
	    String[] clinicCity = props2.getProperty("clinicSatelliteCity", "").split("\\|");
	    //current address index
	    int siteFlag = 0;
	    for(int i = 0; i < clinicCity.length; i++){
	    	if (siteID.equals(clinicCity[i]))	siteFlag = i;
	    }
	    String[] temp0 = props2.getProperty("clinicSatelliteName", "").split("\\|");
	    String[] temp1 = props2.getProperty("clinicSatelliteAddress", "").split("\\|");
	    String[] temp3 = props2.getProperty("clinicSatelliteProvince", "").split("\\|");
	    String[] temp4 = props2.getProperty("clinicSatellitePostal", "").split("\\|");
	    String[] temp5 = props2.getProperty("clinicSatellitePhone", "").split("\\|");
	    String[] temp6 = props2.getProperty("clinicSatelliteFax", "").split("\\|");
	    clinicAddress = temp0[siteFlag]+"\n"+temp1[siteFlag] + "\n" + clinicCity[siteFlag] + ", " + temp3[siteFlag] + " " + temp4[siteFlag] + "\nTel: " + temp5[siteFlag] + "\nFax: " + temp6[siteFlag];
	}else{
		clinicAddress = strClinicAddr;
	}
}
	OscarProperties props = OscarProperties.getInstance();
	String tempLoc = props.getProperty("BILLING_NOTE","");
%>
<tr><td>
		<table border="1" width="100%" bordercolorlight="#99A005" bordercolordark="#FFFFFF">
			<tr class="myYellow">
				<td colspan='2'>Private Billing</td>
			</tr>
			<tr><td width="80%">

			<table id="privateBillInfo" border="0" width="100%" >
			<tr><td>Bill To [<a href=# onclick="scriptAttach('billTo'); return false;">Search</a>]<br>
			<textarea name="billto" id="billTo" cols=30 rows=6><%=strPatientAddr %></textarea></td>
			<td>Remit To [<a href=# onclick="scriptAttach('remitTo'); return false;">Search</a>]<br>
			<textarea name="remitto" id="remitTo" value="" cols=30 rows=6><%=clinicAddress%></textarea></td>
			<td>Payee<br>
             <% 
             String  providerNo= request.getParameter("xml_provider");
             int indexnumber=providerNo.indexOf("|");
             if(indexnumber!=-1)
             {
             providerNo=providerNo.substring(0,indexnumber);
             }
             
             String payeename="";
             String lname="";
             String fname="";
             Provider p = providerDao.getProvider(providerNo);
             lname = p.getLastName();
             fname = p.getFirstName();   
             payeename=fname+" "+lname;
             	  
Properties prop = oscar.OscarProperties.getInstance();
   String payee = prop.getProperty("PAYEE", "");
   payee = payee.trim();
   if( payee.length() > 0 ) {
%>
   <textarea id="payee" name="payee" value="" cols=20 rows=6><%=payee%></textarea></td>
<% } else { %>
   <textarea id="payee"  name="payee" value="" cols=20 rows=6><%=payeename%></textarea></td>
   <input type="hidden" name="payeename1" id="payeename1" value="<%=payeename%>" />
<% } %>
			</tr>
			</table>
			<table border="0" width="100%" >
			<tr>
			<td >
			Billing Notes:<br>
			<textarea name="comment" cols=100 rows=6><%=tempLoc %></textarea>
			</td>
			<td align="right">
                        <input type="hidden" name="provider_no" value="<%=request.getParameter("xml_provider").substring(0,request.getParameter("xml_provider").indexOf("|"))%>"/>
                        GST Billed:<input type="text" id="gst" name="gst" value="<%=gstTotal%>" size="6"/><br>
                        <input type="hidden" id="gstBilledTotal" name="gstBilledTotal" value="<%=gstbilledtotal%>" size="6" />
                        Total:<input type="text" id="stotal" disabled name = "stotal" value="0.00" size="6" /><br>
			Payments:<input type="text"  disabled name="payment1" id="payment"value="0.00" size="6" onDblClick="settlePayment();" /><br/>
			Discount:<input type="text" disabled name="discount2" id="discount"value="0.00" size="6"/>
			</td>
			</tr>
			</table>

			<td class="myGreen">
			Payment Date:<br/>
			<input type="text" name="payment_date" id="payment_date" value="<%=DateFormatUtils.format(new java.util.Date(), "yyyy-MM-dd")%>" size="10"/>
			<br/>
			<br/>
			
			Payment Method:<br/>
			<% for(int i=0; i<al.size(); i=i+2) { %>
			<input type="radio" name="payMethod" value="<%=al.get(i) %>" id="payMethod_<%=i %>"/><%=al.get(i+1) %><br/>
			<% } %>
			</td></tr>
			<tr>
				<td colspan='2' align='center' bgcolor="silver"><input type="submit" name="submit" value="Save & Print Invoice"
					style="width: 150px;" /><input type="submit" name="submit" id="settlePrintBtn"
					value="Settle & Print Invoice" onClick="document.forms['titlesearch'].btnPressed.value='Settle'; document.forms['titlesearch'].submit();javascript:popupPage(700,720,'billingON3rdInv.jsp');" style="width: 160px;" />
				<input type="hidden"  name="btnPressed" value="">
				<input type="hidden" name="total_payment" id="total_payment" value="0.00"/>
				<input type="hidden" name="total_discount" id="total_discount" value="0.00"/>
				<input type="hidden" name="refund" id="refund" value="0.00"/>
				</td>
			</tr>
		</table>

</td></tr>
<% }} %>
	<%for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
				String temp = e.nextElement().toString();
%>
	<input type="hidden" name="<%= temp %>" value="<%=StringEscapeUtils.escapeHtml(request.getParameter(temp))%>" />
	<%}

		%>


</table>
<% if(bPerc) { out.println("* Click the code you want the % code to apply to [1 or 2 ...]."); } %>
</form>


<script language="JavaScript">
function calculatePayment(){
    var payment = 0.00;
    jQuery("input[id^='paid_']").each(function(index) {
    	if (this != null && this.value.length > 0) {
    		payment = parseFloat(payment) + parseFloat(this.value);
    		payment = payment.toFixed(2);
    	}
    });

	document.getElementById("payment").value=payment;
	document.getElementById("total_payment").value=payment;
}

function calculateDiscount(){
	var discount = 0.00;
	jQuery("input[id^='discount_']").each(function(index) {
		if (this != null && this.value.length > 0) {
			discount = parseFloat(discount) + parseFloat(this.value);
			discount = discount.toFixed(2);
		}
	});
	
	document.getElementById("discount").value = discount;
	document.getElementById("total_discount").value = discount;
}

function calculateTotal() {
	var total = 0.00;
	jQuery("input[id^='percCodeSubtotal_']").each(function (index) {
		if (this != null && this.value.length > 0) {
			total = parseFloat(total) + parseFloat(this.value);
			total = total.toFixed(2);
		}
	});
	jQuery("#total").val(total);
	jQuery("#gstBilledTotal").val(total);
	jQuery("#stotal").val(total);
}

function onTotalChanged() {
	var val = document.getElementById("total").value;
	var regexNumberic = /^([1-9]\d{0,9}|0)(\.\d{1,2})?$/;
	if (!regexNumberic.test(val)) {
		calculateTotal();
		alert("Please enter digital numbers !");
		return;
	}
	
	var total = jQuery("#total").val();
	jQuery("#gstBilledTotal").val(total);
	jQuery("#stotal").val(total);
}

function addToDiseaseRegistry(){
	if ( validateItems() ) {
		var url = "../../../oscarResearch/oscarDxResearch/dxResearch.do";
		var data = Form.serialize(dxForm);
		
		new Ajax.Updater('dxListing',url, {method: 'post',postBody: data,asynchronous:true,onComplete: getNewCurrentDxCodeList});
	}
}

function validateItems(){
	var ret = false;
	
	dxChecks = document.getElementsByName("xml_research");
	for( idx = 0; idx < dxChecks.length; ++idx ) {
		if( dxChecks[idx].checked ) {
			ret = true;
			break;
		}
	}
	if (!ret) alert("Error: Nothing was selected");
	else ret = confirm("Are you sure to add to the patient's disease registry?");
	return ret;
}


function getNewCurrentDxCodeList(origRequest){
   //alert("calling get NEW current Dx Code List");
   var url = "../../../oscarResearch/oscarDxResearch/currentCodeList.jsp";
   var ran_number=Math.round(Math.random()*1000000);
   var params = "demographicNo=<%=demo_no%>&rand="+ran_number;  //hack to get around ie caching the page
   //alert(params);
   new Ajax.Updater('dxFullListing',url, {method:'get',parameters:params,asynchronous:true});
   //alert(origRequest.responseText);
}

</script>


<oscar:oscarPropertiesCheck property="DX_QUICK_LIST_BILLING_REVIEW" value="yes">

<div class="dxBox">
    <h3>&nbsp;Current Patient Dx List &nbsp;<a href="#" onclick="Element.toggle('dxFullListing'); return false;" style="font-size:small;" >show/hide</a></h3>
       <div class="wrapper" id="dxFullListing">
       <jsp:include page="../../../oscarResearch/oscarDxResearch/currentCodeList.jsp">
          <jsp:param name="demographicNo" value="<%=demo_no%>"/>
       </jsp:include>
       </div>
</div>

<div class="dxBox">
    <h3>&nbsp;Dx Quick Pick Add Lists</h3>
       <form id="dxForm">
       <input type="hidden" name="demographicNo" value="<%=demo_no%>" />
       <input type="hidden" name="providerNo" value="<%=session.getAttribute("user")%>" />
       <input type="hidden" name="forward" value="" />
       <input type="hidden" name="forwardTo" value="codeList"/>
       <div class="wrapper" id="dxListing">
       <jsp:include page="../../../oscarResearch/oscarDxResearch/quickCodeList.jsp">
          <jsp:param name="demographicNo" value="<%=demo_no%>"/>
       </jsp:include>
       </div>
       <input type="button" value="Add To Disease Registry" onclick="addToDiseaseRegistry()"/>
       </form>
</div>

</oscar:oscarPropertiesCheck>

</body>
</html>
