<!DOCTYPE html>
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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.common.dao.BillingOnItemPaymentDao"%>
<%@page import="org.oscarehr.managers.SecurityInfoManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.math.*,java.util.*,java.sql.*,oscar.*,java.net.*" %> <!-- errorPage="errorpage.jsp" -->
<%@page import="oscar.oscarBilling.ca.on.data.*"%>
<%@page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@page import="oscar.oscarDemographic.data.*"%>
<%@page import="oscar.util.UtilDateUtilities"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.common.model.BillingONItem"%>
<%@page import="org.oscarehr.common.model.BillingONErrorCode, org.oscarehr.common.dao.BillingONErrorCodeDao"%>
<%@page import="org.oscarehr.common.dao.BillingONEAReportDao, org.oscarehr.common.model.BillingONEAReport"%>
<%@page import="org.oscarehr.common.model.RaDetail, org.oscarehr.common.dao.RaDetailDao"%>
<%@page import="org.oscarehr.common.model.ClinicLocation, org.oscarehr.common.dao.ClinicLocationDao"%>
<%@page import="org.oscarehr.common.dao.BillingONPaymentDao, org.oscarehr.common.model.BillingONPayment"%>
<%@page import="org.oscarehr.common.model.Provider,org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.dao.BillingONCHeader1Dao, org.oscarehr.common.model.BillingONCHeader1"%>
<%@page import="org.oscarehr.common.model.BillingONExt, org.oscarehr.common.dao.BillingONExtDao"%>
<%@page import="org.oscarehr.common.model.BillingService, org.oscarehr.common.dao.BillingServiceDao"%>
<%@page import="org.oscarehr.common.model.ClinicNbr, org.oscarehr.common.dao.ClinicNbrDao"%>
<%@page import="org.oscarehr.common.model.Site, org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.ProviderSite, org.oscarehr.common.dao.ProviderSiteDao"%>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist" %>
<%@page import="org.oscarehr.common.dao.ProfessionalSpecialistDao" %>
<%@page import="org.oscarehr.common.service.BillingONService"%> 
<%@page import="java.text.NumberFormat" %>
<%@page import="org.oscarehr.PMmodule.dao.ProgramProviderDAO" %>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider" %>
<%@page import="org.oscarehr.common.dao.BillingONPaymentDao" %>
<%@page import="org.oscarehr.common.model.BillingONPayment" %>

<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

<%	

	String userfirstname, userlastname;		

    String userProviderNo = (String) session.getAttribute("user");
    ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
    BillingONExtDao bExtDao = (BillingONExtDao) SpringUtils.getBean("billingONExtDao");
    BillingONPaymentDao billingOnPaymentDao = SpringUtils.getBean(BillingONPaymentDao.class);
    Provider userProvider = providerDao.getProvider(userProviderNo);
       
  
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false;
    
    SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    
%>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
    <%isSiteAccessPrivacy=true; %>
</security:oscarSec>

<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
    <%isTeamAccessPrivacy=true; %>
</security:oscarSec>
<%
    ProviderSiteDao providerSiteDao = (ProviderSiteDao) SpringUtils.getBean("providerSiteDao");
    Set<String> providerAccessList = new HashSet<String>();
    
    //multisites function
    if (isSiteAccessPrivacy) {
        List<ProviderSite> providerSite = providerSiteDao.findByProviderNo(userProviderNo);
        for (ProviderSite pSite : providerSite) {
            providerAccessList.add(pSite.getId().getProviderNo());
        }                       
    }
    
    if (isTeamAccessPrivacy) {
        List<Provider> providers = providerDao.getBillableProvidersOnTeam(userProvider);
        for (Provider p : providers) {
            providerAccessList.add(p.getProviderNo());
        }            
    }
    
    boolean isTeamBillingOnly=false;
    boolean isMultiSiteProvider = true;
%>
<security:oscarSec objectName="_team_billing_only" roleName="<%=roleName$%>" rights="r" reverse="false">
    <% isTeamBillingOnly=true; %>
</security:oscarSec>
<%    
    boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();
    List<String> mgrSites = new ArrayList<String>();

    if (bMultisites) {
        SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
        List<Site> sites = siteDao.getActiveSitesByProviderNo(userProviderNo);
        for (Site s : sites) {
                mgrSites.add(s.getName());
        }
    }
    
    int MAXRECORDS = 6;  //number of billing items to display if record has less than 6
    String UpdateDate = "";
    String DemoNo = "";
    String DemoName = "";
    String DemoAddress = "";
    String DemoCity = "";
    String DemoProvince = "";
    String DemoPostal = "";
    String DemoDOB = "";
    String DemoRS = "";
    String DemoSex = "";
    String hin = "";
    String location = "";
    String BillLocation = "";
    String BillLocationNo = "";
    String BillDate = "";
    String Provider = "";
    String BillType = "";
    String payProgram = "";
    String BillTotal = "";
    String visitdate = "";
    String visittype = "";
    String sliCode = "";
    String BillDTNo = "";
    String HCTYPE = "";
    String HCSex = "";
    String r_doctor_ohip = "";
    String r_doctor = "";
    String r_doctor_ohip_s = "";
    String r_doctor_s = "";
    String m_review = "";
    String specialty = "";
    String r_status = "";
    String roster_status = "";
    String comment = "";
    String payer = "";
    String htmlPaid = "";
    int rowCount = 0;
    int rowReCount = 0;
    ResultSet rslocation = null;
    ResultSet rsPatient = null;			        
%>

<html:html locale="true">
<head>
<title><bean:message key="billing.billingCorrection.title" /></title>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	
<oscar:customInterface section="editInvoice"/>

<script type="text/javascript">
<!--

function setfocus() {	
	document.form1.billing_no.focus();
	document.form1.billing_no.select();
}
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
	f0 = escape(document.forms[1].xml_diagnostic_detail.value);
	f1 = document.forms[1].xml_dig_search1.value;
	// f2 = escape(document.serviceform.elements["File2Data"].value);
	// fname = escape(document.Compose.elements["FName"].value);
	awnd=rs('att','billingDigSearch.jsp?name='+f0 + '&search=' + f1,600,600,1);
	awnd.focus();
}
function referralScriptAttach2(elementName, name2) {
     var d = elementName;
     t0 = escape("document.forms[1].elements[\'"+d+"\'].value");
     t1 = escape("document.forms[1].elements[\'"+name2+"\'].value");
     awnd=rs('att',('searchRefDoc.jsp?param='+t0+'&param2='+t1),600,600,1);
     awnd.focus();
}
function scScriptAttach(nameF) {
	f0 = document.forms[1].elements[nameF].value;
    f1 = escape("document.forms[1].elements[\'"+nameF+ "\'].value");
	awnd=rs('att','billingCodeSearch.jsp?name='+f0 + '&search=&name1=&name2=&nameF='+f1,600,600,1);
	awnd.focus();
}

function search3rdParty(elementName) {
    var d = elementName;
    t0 = escape("document.forms[1].elements[\'"+d+"\'].value");
    popupPage('600', '700', 'onSearch3rdBillAddr.jsp?param='+t0);
}

function validateNum(el){
   var val = el.value;
   var tval = ""+val;

   if (isNaN(val)){
      alert("Item value must be numeric.");
      el.select();
      el.focus();
      return false;
   }
   if ( val > 999999.99 ){
     alert("Item value must be below $1000000");
     el.select();
     el.focus();
     return false;
   }
   decLen = tval.indexOf(".");
   if (decLen != -1  &&   ( tval.length - decLen ) > 3  ){
      alert("Item value has a maximum of 2 decimal places");
      el.select();
      el.focus();
      return false;
   }
   
   return true;
}

function validateAllItems(){

   var provider = document.getElementById("provider_no");
   if( provider.options[provider.selectedIndex].value == "" ) {
      alert("Billing provider must be set");
      return false;
   }

   var billamt;
   for( idx = 0; idx < <%=MAXRECORDS%>; ++idx ) {
       billamt = document.getElementById("billingamount" + idx);       
       if( billamt != undefined && !validateNum(billamt) ) {    	   
           return false;
       }
   }
   
   var statusOpts = document.getElementById("status");
   var status = statusOpts.options[statusOpts.selectedIndex].value;
   var payPrgrmOpts = document.getElementById("payProgram");
   var payPrgrm = payPrgrmOpts.options[payPrgrmOpts.selectedIndex].value;   
   var is3rdParty = true;
   if (payPrgrm == "HCP" || payPrgrm == "RMB" || payPrgrm == "WCB" ){
      is3rdParty = false;
   }
   if( status == "P" && !is3rdParty) {
	   alert("Pay Program does not match bill status.");
	   return false;
   }
   

   var outstandingAmt = document.getElementById("outstandingBalance").value;

   if ((outstandingAmt != "0.00") && (status == "S" && is3rdParty)) {
       if(!confirm('Warning: Settling this invoice will also settle the outstanding balance as paid. Continue?')){ 
          return false;
       }
   }
                                     
   return true;
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

function sanityCheck(id) {
    if( id != "" ) {
        location.href = "billingON3rdInv.jsp?billingNo=" + id;
    }
    else {
        alert("Please search a valid invoice number");
    }
    return false;
}

function checkPayProgram(payProgram) {
    //enable 3rd party elements
    if(payProgram == 'PAT' || payProgram == 'OCF' || payProgram == 'ODS' || payProgram == 'CPP' || payProgram == 'STD' || payProgram == 'IFH' ) {
        document.getElementById("thirdParty").style.display = "inline";
        document.getElementById("thirdPartyPymnt").style.display = "inline";

        document.getElementById("billTo").disabled = false;
    }
    else {
        document.getElementById("thirdParty").style.display = "none";
        document.getElementById("thirdPartyPymnt").style.display = "none";
        
        document.getElementById("billTo").disabled = true;
    }
}

function checkSettle(status) {
    if( status == 'S' ) {
        var payElem = document.getElementById("payment");
        if( payElem != null ) {
            payElem.value = document.getElementById("billTotal").value;
        }
    }        
    else if( status == 'P') {
    	document.getElementById("thirdParty").style.display = "inline";
    	//document.getElementById("thirdPartyPymnt").style.display = "inline";
    	
    	document.getElementById("payment").disabled = false;
    	if (document.getElementById("oldPayment") != null) {
    		document.getElementById("oldPayment").disabled = false;    		
    	}
    	document.getElementById("payDate").disabled = false;
    	document.getElementById("refund").disabled = false;
    	document.getElementById("billTo").disabled = false;
    }
    else {
    	document.getElementById("thirdParty").style.display = "none";
    	document.getElementById("thirdPartyPymnt").style.display = "none";
    	
    	document.getElementById("payment").disabled = true;
    	if (document.getElementById("oldPayment") != null) {
    		document.getElementById("oldPayment").disabled = true;
    	}
    	document.getElementById("payDate").disabled = true;
    	document.getElementById("refund").disabled = true;
    	document.getElementById("billTo").disabled = true;
    }

}

function validateAmountNumberic(idx) {
	var oldVal = document.getElementById("billingamounttmp" + idx).value;
	var val = document.getElementById("billingamount" + idx).value;
	if (val.length == 0) {
		if (document.getElementsByName("servicecode" + idx)[0].value.trim().length > 0) {
			document.getElementById("billingamount" + idx).value = " ";
		}
		return;
	}
	//var regexNumberic = /^([1-9]\d*|0)(\.\d{1,2})?$/;
	var regexNumberic = /^([1-9]\d{0,9}|0)(\.\d{1,2})?$/;
	if (!regexNumberic.test(val)) {
		document.getElementById("billingamount" + idx).value = oldVal;
		alert("Please enter digital numbers !");
		return;
	}
	oldVal = val;
}

//-->
</script>

</head>

<body onload="$('#billing_no').focus()">
<%//
    RaDetailDao raDetailDao = (RaDetailDao) SpringUtils.getBean("raDetailDao");
    BillingONCHeader1Dao bCh1Dao = (BillingONCHeader1Dao) SpringUtils.getBean("billingONCHeader1Dao");
    BillingServiceDao bServiceDao = (BillingServiceDao) SpringUtils.getBean("billingServiceDao");
    ProgramProviderDAO programProviderDAO = SpringUtils.getBean(ProgramProviderDAO.class);
    
    // bFlag - fill in data?
    boolean bFlag = false;
    boolean billNoErr = false;
    
    String billNo = request.getParameter("billing_no").trim();
    String claimNo = request.getParameter("claim_no");
    if( claimNo != null && claimNo.equals("null") ) {
		claimNo = null;
    }

    if( billNo == null || billNo.length() == 0 ) {
        if( claimNo != null && claimNo.length() > 0 ) {
            claimNo = claimNo.trim();
            List<RaDetail> raDetails = raDetailDao.getRaDetailByClaimNo(claimNo);
            if (!raDetails.isEmpty()) {
                billNo = String.valueOf(raDetails.get(0).getBillingNo());
            }
        }
    }
    if (billNo != null && billNo.length() > 0) {
            bFlag = true;
    }
    
    Locale locale = request.getLocale();
    BillingONCHeader1 bCh1 = null;
    Integer billingNo = null;    
    String createTimestamp = null;
    String clinicSite = "";

    if (bFlag) {

        billingNo = Integer.parseInt(billNo);
		bCh1 = bCh1Dao.find(billingNo);

		
		//do a program check here.
		if(bCh1 != null && bCh1.getProgramNo() != null) {
			List<ProgramProvider> ppList = programProviderDAO.getProgramDomain(loggedInInfo.getLoggedInProviderNo());
			boolean found=false;
			for(ProgramProvider pp:ppList) {
				if(pp.getProgramId().intValue() == bCh1.getProgramNo().intValue()) {
					found=true;
					break;
				}
			}
			
			if(!found) {
				throw new RuntimeException("You do not have access to this invoice! It's from another program.");
			}
		}
		
        if (bCh1 != null) {	

	    clinicSite = bCh1.getClinic();

                //multisite. check provider no
            if (((isSiteAccessPrivacy || isTeamAccessPrivacy) && !providerAccessList.contains(bCh1.getProviderNo())) 
                 || (bMultisites && !mgrSites.contains(clinicSite))) { 
                
                isMultiSiteProvider = false;
	     }
	     if (!isMultiSiteProvider) {
                DemoNo = "";
                DemoName = "";
                DemoAddress = "";
                DemoCity = "";
                DemoProvince = "";
                DemoPostal = "";
                DemoDOB = "";
                DemoSex = "";
                r_doctor = "";
                r_doctor_ohip_s = "";
                r_doctor_s = "";
                m_review = bCh1.getManReview();
                specialty = "";
                r_status = "";
                roster_status = "";

                out.write("<script>window.alert('sorry, billing access denied.')</script>");               

            }else {
                createTimestamp = DateUtils.formatDateTime(bCh1.getTimestamp(), locale);                
                DemoNo = bCh1.getDemographicNo().toString();
                DemoName = bCh1.getDemographicName();
                DemoAddress = "";
                DemoCity = "";
                DemoProvince = "";
                DemoPostal = "";
                DemoDOB = bCh1.getDob();
                DemoSex = bCh1.getSex().equals("1") ? "M" : "F";

                BigDecimal billTotal = bCh1.getTotal();                

                org.oscarehr.common.model.Demographic sdemo = (new DemographicData()).getDemographic(loggedInInfo, DemoNo);
                hin = sdemo.getHin()+sdemo.getVer();
                DemoDOB = sdemo.getYearOfBirth() + sdemo.getMonthOfBirth() + sdemo.getDateOfBirth();
                DemoSex = sdemo.getSex();
                DemoRS = sdemo.getRosterStatus();
                //hin = ch1Obj.getHin() + ch1Obj.getVer();
                location = bCh1.getFaciltyNum();
                BillLocation = "";
                BillLocationNo = location;
                BillDate = DateUtils.formatDate(bCh1.getBillingDate(), locale);
                Provider = bCh1.getProviderNo();
                BillType = bCh1.getStatus();
                payProgram = bCh1.getPayProgram();
                BillTotal = billTotal.toPlainString();
				try {
                	visitdate = DateUtils.formatDate(bCh1.getAdmissionDate(), locale);
				}
				catch (java.text.ParseException e) {
					visitdate = "";
				}
                visittype = bCh1.getVisitType();
                sliCode = bCh1.getLocation();
                BillDTNo = "";
                HCTYPE = bCh1.getProvince();
                HCSex = bCh1.getSex();
                r_doctor_ohip = bCh1.getRefNum();
                ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
				List<ProfessionalSpecialist> professionalSpecialists = professionalSpecialistDao.findByReferralNo(r_doctor_ohip);
				if (professionalSpecialists != null)
					r_doctor = professionalSpecialists.get(0).getLastName()+", "+professionalSpecialists.get(0).getFirstName();
				else
					r_doctor = "";
                r_doctor_ohip_s = "";
                r_doctor_s = "";
                m_review = bCh1.getManReview();
                specialty = "";
                r_status = "";
                roster_status = "";
                comment = bCh1.getComment();
                
				// get ohip claim number
				JdbcBillingRAImpl raObj = new JdbcBillingRAImpl();
				claimNo = raObj.getRAClaimNo4BillingNo( billNo );

            }
        }
    }
    
				boolean thirdParty = false;
				Billing3rdPartPrep tObj = new Billing3rdPartPrep();
				
				if("HCP".equals(payProgram) || "RMB".equals(payProgram) || "WCB".equals(payProgram)
						|| billNo.length() < 1) {
					
					Properties tProp = null;					
					if( billNo.length() > 0 ) {
						tProp = tObj.get3rdPartBillPropInactive(billNo.trim());						
					}
					
					if( tProp == null || tProp.size() == 0 ) {
						htmlPaid = "Paid<br><input type='text' id='payment' name='payment' size=5 value='0.00'/>" +
							"<input type='hidden' id='oldPayment' name='oldPayment' value='0.00'/> <input type='hidden' id='payDate' name='payDate' value='" +
                        	UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "'/><br> Refund<br><input type='text' id='refund' name='refund' size=5 value='0.00'/><br>";
                        	payer = "";
					}
					else {
						htmlPaid = "Paid<br><input type='text' id='payment' name='payment' size=5 value='"
					    	+ tProp.getProperty("payment","0.00") + "' /><input type='hidden' id='oldPayment' name='oldPayment' value='"
		                    + tProp.getProperty("payment","0.00") + "' /><input type='hidden' id='payDate' name='payDate' value='"
		                    + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "'/><br>";
						htmlPaid += "Refund<br><input type='text' id='refund' name='refund' size=5 value='"
							+ tProp.getProperty("refund") + "' /><br>";
						payer = tProp.getProperty("billTo");
                        if( payer == null ) {
                        	payer = "";
                       	}
					}
				} else {
					thirdParty = true;
					Properties tProp = tObj.get3rdPartBillProp(billNo.trim());	
					NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);

				if(isMultiSiteProvider) {	
					BigDecimal payment = BigDecimal.ZERO;
					BigDecimal balance = BigDecimal.ZERO;
					BigDecimal total = BigDecimal.ZERO;
					BigDecimal refund = BigDecimal.ZERO;
					BigDecimal discount = BigDecimal.ZERO;
					BigDecimal credit = BigDecimal.ZERO;
					
					List<BillingONPayment> bops = billingOnPaymentDao.find3rdPartyPaymentsByBillingNo(Integer.parseInt(request.getParameter("billing_no").trim()));
					for(BillingONPayment bop:bops) {
						credit = credit.add(bop.getTotal_credit());
						discount = discount.add(bop.getTotal_discount());
						payment = payment.add(bop.getTotal_payment());
						refund = refund.add(bop.getTotal_refund());				
					}
					/*
					
					BillingONExtDao billingOnExtDao = (BillingONExtDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("billingONExtDao");
					BillingONExt paymentItem = billingOnExtDao.getClaimExtItem(Integer.parseInt(request.getParameter("billing_no").trim()), Integer.parseInt(DemoNo), BillingONExtDao.KEY_PAYMENT);
					if (paymentItem != null) {
						payment = new BigDecimal(paymentItem.getValue());
					}
					BillingONExt discountItem = billingOnExtDao.getClaimExtItem(Integer.parseInt(request.getParameter("billing_no").trim()), Integer.parseInt(DemoNo), BillingONExtDao.KEY_DISCOUNT);
					if (discountItem != null) {
						discount = new BigDecimal(discountItem.getValue());
					}
					BillingONExt refundItem = billingOnExtDao.getClaimExtItem(Integer.parseInt(request.getParameter("billing_no").trim()), Integer.parseInt(DemoNo), BillingONExtDao.KEY_REFUND);
					if (refundItem != null) {
						refund = new BigDecimal(refundItem.getValue());
					}
					BillingONExt totalItem = billingOnExtDao.getClaimExtItem(Integer.parseInt(request.getParameter("billing_no").trim()), Integer.parseInt(DemoNo), BillingONExtDao.KEY_TOTAL);
					if (totalItem != null) {
						total = new BigDecimal(totalItem.getValue());
					}
					BillingONExt creditItem = billingOnExtDao.getClaimExtItem(Integer.parseInt(request.getParameter("billing_no").trim()), Integer.parseInt(DemoNo), BillingONExtDao.KEY_CREDIT);
					if (creditItem != null) {
						credit = new BigDecimal(creditItem.getValue());
					}
					*/
					total = bCh1.getTotal();
					
					balance = total.subtract(payment).subtract(discount).add(credit);
					payment = payment.subtract(credit);

                    htmlPaid = "<br/>&nbsp;&nbsp;<span style='font-size:large;font-weight:bold'>Paid:</span>&nbsp;&nbsp;&nbsp;<span id='payment' style='font-size:large;font-weight:bold'>"
                    	+ ((payment.compareTo(BigDecimal.ZERO) == -1) ? "-" : "") + currency.format(payment) + "</span>";
					htmlPaid += "&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:large;font-weight:bold'>Balance:</span>&nbsp;&nbsp;&nbsp;<span id='balance' style='font-size:large;font-weight:bold'>"
						+ ((balance.compareTo(BigDecimal.ZERO) == -1) ? "-" : "") + currency.format(balance) + "</span>";
					htmlPaid += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:display3rdPartyPayments()'>Payments List</a>";
				}	
                    		payer = tProp.getProperty("billTo");
                    		if( payer == null ) {
                    		    payer = "";
                    		}
			}

%>

<h3><bean:message key="admin.admin.btnBillingCorrection" /></h3>

<div class="container-fluid">

<%if (request.getParameter("adminSubmit")!=null) { %>
    <div class="alert alert-success" id="alert_message">
    	<button type="button" class="close" data-dismiss="alert">&times;</button>
  		<strong>Success! </strong> Your entry was saved!
    </div>
<%} %>

<%if(billNoErr){ %>
    <div class="alert alert-error" id="alert_message">
    	<button type="button" class="close" data-dismiss="alert">&times;</button>
  		<strong>Error! </strong> Invoice number does not exist!
    </div>
<%} %>

<div class="row well well-small">
<%if(createTimestamp!=null){%>
<bean:message key="billing.billingCorrection.msgLastUpdate" />: <%=nullToEmpty(createTimestamp)%>
<%}%>

<form name="form1" method="post" action="billingONCorrection.jsp<%if (request.getParameter("admin")!=null) { out.print("?admin"); }%>">
<input type="hidden" id="billTotal" value="<%=BillTotal%>" />
    
<div class="span2">
<a href="#" onclick="return sanityCheck('<%=nullToEmpty(billNo)%>');"><bean:message key="billing.billingCorrection.formInvoiceNo" /></a><br>
<input type="text" id="billing_no" name="billing_no"  value="<%=nullToEmpty(billNo)%>" class="span2" required>
</div>


<div class="span2">
OHIP Claim No  <br>     
<input type="text" name="claim_no" value="<%=nullToEmpty(claimNo)%>" class="span2">
</div>

<div class="span2">
<br>
<input class="btn btn-primary" type="submit" name="submit" value="Search">
</div>

</form>
</div><!--well-->



<!-- RA error -->
<%
    if(bFlag) {       
        BillingONEAReportDao billingONEAReportDao = (BillingONEAReportDao) SpringUtils.getBean("billingONEAReportDao");
	List<String> lReject = billingONEAReportDao.getBillingErrorList(billingNo);
	List<String> lError = raDetailDao.getBillingExplanatoryList(billingNo);
	lError.addAll(lReject);	
        
        BillingONErrorCodeDao billingONErrorCodeDao = (BillingONErrorCodeDao) SpringUtils.getBean("billingONErrorCodeDao");        
%>
<table>
<% 
        for(int i=0; i<lError.size(); i++) {
            String codeNo = lError.get(i);
            if("".equals(codeNo)) continue;

            BillingONErrorCode errorCode = billingONErrorCodeDao.find(codeNo);
            String codeDesc = null;
            if (errorCode != null) {
                codeDesc = errorCode.getDescription();
            }
            codeDesc = codeDesc == null ? "Unknown" : codeDesc;
%>
    <tr>
        <th width="10%"><b><%=codeNo %></b></th>
        <td align="left"><%=codeDesc %></td>
    </tr>
<%      } %>
</table>
<%  } 
    String curSite = request.getParameter("site")==null?clinicSite:request.getParameter("site");
%>


<html:form action="/billing/CA/ON/BillingONCorrection"> 
    
    <input type="hidden" name="method" value="updateInvoice"/>
    <input type="hidden" name="xml_billing_no" value="<%=billNo%>" /> 
    <input type="hidden" name="update_date" value="<%=nullToEmpty(createTimestamp)%>"/>
    <input type="hidden" name="payDate" value="<%=UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss")%>" />
    <input type="hidden" name="demoNo" value="<%=DemoNo%>" />
    <input type="hidden" name="oldStatus" value="<%=thirdParty ? "thirdParty" : "" %>" />

<div class="row well well-small">  
<div class="span10">      
<table>
	<tr>
		<th align="left" colspan="2"><bean:message
			key="billing.billingCorrection.msgPatientInformation" /></th>
	</tr>
	<tr>
		<td width="54%"><bean:message
			key="billing.billingCorrection.msgPatientName" />: <a href=#
			onclick="popupPage(720,860,'../../../demographic/demographiccontrol.jsp?demographic_no=<%=DemoNo %>&displaymode=edit&dboperation=search_detail');return false;">
		<%=DemoName%></a> <input type="hidden" name="demo_name"
			value="<%=DemoName%>"></td>
		<td width="46%"><bean:message
			key="billing.billingCorrection.formHealth" />: <%=hin%> <input
			type="hidden" name="xml_hin" value="<%=hin%>">
		RS: <%=DemoRS%></td>
	</tr>
	<tr>
		<td><bean:message key="billing.billingCorrection.msgSex" />:
			<%=DemoSex%> <input type="hidden" name="demo_sex" value="<%=DemoSex%>">
			<input type="hidden" name="hc_sex" value="<%=HCSex%>"></td>
		<td><bean:message key="billing.billingCorrection.formDOB" />:
			<input type="hidden" name="xml_dob" value="<%=DemoDOB%>"> <%=DemoDOB%>
		</td>
	</tr>
	<tr>
		<td><bean:message key="billing.billingCorrection.msgDoctor" />:<br>
			<input type="text" name="rd" value="<%=r_doctor%>" size=20 readonly>
		</td>
		<td>

			<bean:message
			key="billing.billingCorrection.msgDoctorNo" />:
			<div class="input-append">
			 <input type="text" name="rdohip" value="<%=r_doctor_ohip%>" class="span2" readonly />
			<a href="javascript:referralScriptAttach2('rdohip','rd')" class="btn"><i class="icon icon-search"></i></a>
			</div>
			</td>
	</tr>
</table>
</div><!--span-->
</div>

<div class="row well well-small">
<div class="span10">

<strong><bean:message key="billing.billingCorrection.msgAditInfo" /></strong><br>

<bean:message key="billing.billingCorrection.formHCType" />: 
				<select name="hc_type" style="font-size: 80%;">
				<option value="OT" <%=HCTYPE.equals("OT")?" selected":""%>>OT-Other</option>
				<option value="AB" <%=HCTYPE.equals("AB")?" selected":""%>>AB-Alberta</option>
				<option value="BC" <%=HCTYPE.equals("BC")?" selected":""%>>BC-British Columbia</option>
				<option value="MB" <%=HCTYPE.equals("MB")?" selected":""%>>MB-Manitoba</option>
				<option value="NB" <%=HCTYPE.equals("NB")?" selected":""%>>NB-New Brunswick</option>
				<option value="NF" <%=HCTYPE.equals("NF")?" selected":""%>>NF-Newfoundland & Labrador</option>
				<option value="NT" <%=HCTYPE.equals("NT")?" selected":""%>>NT-Northwest Territory</option>
				<option value="NS" <%=HCTYPE.equals("NS")?" selected":""%>>NS-Nova Scotia</option>
				<option value="NU" <%=HCTYPE.equals("NU")?" selected":""%>>NU-Nunavut</option>
				<option value="ON" <%=HCTYPE.equals("ON")?" selected":""%>>ON-Ontario</option>
				<option value="PE" <%=HCTYPE.equals("PE")?" selected":""%>>PE-Prince Edward Island</option>
				<option value="QC" <%=HCTYPE.equals("QC")?" selected":""%>>QC-Quebec</option>
				<option value="SK" <%=HCTYPE.equals("SK")?" selected":""%>>SK-Saskatchewan</option>
				<option value="YT" <%=HCTYPE.equals("YT")?" selected":""%>>YT-Yukon</option>
				<option value="US" <%=HCTYPE.equals("US")?" selected":""%>>US resident</option>
				<option value="US-AK" <%=HCTYPE.equals("US-AK")?" selected":""%>>US-AK-Alaska</option>
				<option value="US-AL" <%=HCTYPE.equals("US-AL")?" selected":""%>>US-AL-Alabama</option>
				<option value="US-AR" <%=HCTYPE.equals("US-AR")?" selected":""%>>US-AR-Arkansas</option>
				<option value="US-AZ" <%=HCTYPE.equals("US-AZ")?" selected":""%>>US-AZ-Arizona</option>
				<option value="US-CA" <%=HCTYPE.equals("US-CA")?" selected":""%>>US-CA-California</option>
				<option value="US-CO" <%=HCTYPE.equals("US-CO")?" selected":""%>>US-CO-Colorado</option>
				<option value="US-CT" <%=HCTYPE.equals("US-CT")?" selected":""%>>US-CT-Connecticut</option>
				<option value="US-CZ" <%=HCTYPE.equals("US-CZ")?" selected":""%>>US-CZ-Canal Zone</option>
				<option value="US-DC" <%=HCTYPE.equals("US-DC")?" selected":""%>>US-DC-District Of Columbia</option>
				<option value="US-DE" <%=HCTYPE.equals("US-DE")?" selected":""%>>US-DE-Delaware</option>
				<option value="US-FL" <%=HCTYPE.equals("US-FL")?" selected":""%>>US-FL-Florida</option>
				<option value="US-GA" <%=HCTYPE.equals("US-GA")?" selected":""%>>US-GA-Georgia</option>
				<option value="US-GU" <%=HCTYPE.equals("US-GU")?" selected":""%>>US-GU-Guam</option>
				<option value="US-HI" <%=HCTYPE.equals("US-HI")?" selected":""%>>US-HI-Hawaii</option>
				<option value="US-IA" <%=HCTYPE.equals("US-IA")?" selected":""%>>US-IA-Iowa</option>
				<option value="US-ID" <%=HCTYPE.equals("US-ID")?" selected":""%>>US-ID-Idaho</option>
				<option value="US-IL" <%=HCTYPE.equals("US-IL")?" selected":""%>>US-IL-Illinois</option>
				<option value="US-IN" <%=HCTYPE.equals("US-IN")?" selected":""%>>US-IN-Indiana</option>
				<option value="US-KS" <%=HCTYPE.equals("US-KS")?" selected":""%>>US-KS-Kansas</option>
				<option value="US-KY" <%=HCTYPE.equals("US-KY")?" selected":""%>>US-KY-Kentucky</option>
				<option value="US-LA" <%=HCTYPE.equals("US-LA")?" selected":""%>>US-LA-Louisiana</option>
				<option value="US-MA" <%=HCTYPE.equals("US-MA")?" selected":""%>>US-MA-Massachusetts</option>
				<option value="US-MD" <%=HCTYPE.equals("US-MD")?" selected":""%>>US-MD-Maryland</option>
				<option value="US-ME" <%=HCTYPE.equals("US-ME")?" selected":""%>>US-ME-Maine</option>
				<option value="US-MI" <%=HCTYPE.equals("US-MI")?" selected":""%>>US-MI-Michigan</option>
				<option value="US-MN" <%=HCTYPE.equals("US-MN")?" selected":""%>>US-MN-Minnesota</option>
				<option value="US-MO" <%=HCTYPE.equals("US-MO")?" selected":""%>>US-MO-Missouri</option>
				<option value="US-MS" <%=HCTYPE.equals("US-MS")?" selected":""%>>US-MS-Mississippi</option>
				<option value="US-MT" <%=HCTYPE.equals("US-MT")?" selected":""%>>US-MT-Montana</option>
				<option value="US-NC" <%=HCTYPE.equals("US-NC")?" selected":""%>>US-NC-North Carolina</option>
				<option value="US-ND" <%=HCTYPE.equals("US-ND")?" selected":""%>>US-ND-North Dakota</option>
				<option value="US-NE" <%=HCTYPE.equals("US-NE")?" selected":""%>>US-NE-Nebraska</option>
				<option value="US-NH" <%=HCTYPE.equals("US-NH")?" selected":""%>>US-NH-New Hampshire</option>
				<option value="US-NJ" <%=HCTYPE.equals("US-NJ")?" selected":""%>>US-NJ-New Jersey</option>
                <option value="US-NM" <%=HCTYPE.equals("US-NM")?" selected":""%>>US-NM-New Mexico</option>
                <option value="US-NU" <%=HCTYPE.equals("US-NU")?" selected":""%>>US-NU-Nunavut</option>
                <option value="US-NV" <%=HCTYPE.equals("US-NV")?" selected":""%>>US-NV-Nevada</option>
                <option value="US-NY" <%=HCTYPE.equals("US-NY")?" selected":""%>>US-NY-New York</option>
                <option value="US-OH" <%=HCTYPE.equals("US-OH")?" selected":""%>>US-OH-Ohio</option>
                <option value="US-OK" <%=HCTYPE.equals("US-OK")?" selected":""%>>US-OK-Oklahoma</option>
                <option value="US-OR" <%=HCTYPE.equals("US-OR")?" selected":""%>>US-OR-Oregon</option>
                <option value="US-PA" <%=HCTYPE.equals("US-PA")?" selected":""%>>US-PA-Pennsylvania</option>
                <option value="US-PR" <%=HCTYPE.equals("US-PR")?" selected":""%>>US-PR-Puerto Rico</option>
                <option value="US-RI" <%=HCTYPE.equals("US-RI")?" selected":""%>>US-RI-Rhode Island</option>
                <option value="US-SC" <%=HCTYPE.equals("US-SC")?" selected":""%>>US-SC-South Carolina</option>
                <option value="US-SD" <%=HCTYPE.equals("US-SD")?" selected":""%>>US-SD-South Dakota</option>
                <option value="US-TN" <%=HCTYPE.equals("US-TN")?" selected":""%>>US-TN-Tennessee</option>
                <option value="US-TX" <%=HCTYPE.equals("US-TX")?" selected":""%>>US-TX-Texas</option>
                <option value="US-UT" <%=HCTYPE.equals("US-UT")?" selected":""%>>US-UT-Utah</option>
                <option value="US-VA" <%=HCTYPE.equals("US-VA")?" selected":""%>>US-VA-Virginia</option>
                <option value="US-VI" <%=HCTYPE.equals("US-VI")?" selected":""%>>US-VI-Virgin Islands</option>
                <option value="US-VT" <%=HCTYPE.equals("US-VT")?" selected":""%>>US-VT-Vermont</option>
                <option value="US-WA" <%=HCTYPE.equals("US-WA")?" selected":""%>>US-WA-Washington</option>
                <option value="US-WI" <%=HCTYPE.equals("US-WI")?" selected":""%>>US-WI-Wisconsin</option>
                <option value="US-WV" <%=HCTYPE.equals("US-WV")?" selected":""%>>US-WV-West Virginia</option>
                <option value="US-WY" <%=HCTYPE.equals("US-WY")?" selected":""%>>US-WY-Wyoming</option>
		</select>


<bean:message key="billing.billingCorrection.formManualReview" />: <input type="checkbox" name="m_review" value="Y" <%=m_review.equals("Y")?"checked":""%> >
</div><!--span-->
</div>

<div class="row well well-small">

<div class="span10">
<b><bean:message key="billing.billingCorrection.msgBillingInf" /></b><br>

<div class="span4"> 

<div class="span4" style="margin-left:0px;">		
<label><bean:message key="billing.billingCorrection.btnBillingDate" />:</label>
<div class="input-append">
	<input type="text" name="xml_appointment_date" id="xml_appointment_date" value="<%=BillDate%>" style="width:90px" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
	<span class="add-on"><i class="icon-calendar"></i></span>
</div>
</div><!--cal span2-->

<bean:message key="billing.billingCorrection.formBillingType" />:<br>
<input type="hidden" name="xml_status" value="<%=BillType%>"> 
<select style="font-size: 80%;" id="status" name="status" onchange="checkSettle(this.options[this.selectedIndex].value);">
<option value=""><bean:message
	key="billing.billingCorrection.formSelectBillType" /></option>
<option value="H" <%=BillType.equals("H")?"selected":""%>><bean:message
	key="billing.billingCorrection.formBillTypeH" /></option>
<option value="O" <%=BillType.equals("O")?"selected":""%>><bean:message
	key="billing.billingCorrection.formBillTypeO" /></option>
<option value="P" <%=BillType.equals("P")?"selected":""%>><bean:message
	key="billing.billingCorrection.formBillTypeP" /></option>
<option value="N" <%=BillType.equals("N")?"selected":""%>><bean:message
	key="billing.billingCorrection.formBillTypeN" /></option>
<option value="W" <%=BillType.equals("W")?"selected":""%>><bean:message
	key="billing.billingCorrection.formBillTypeW" /></option>
<option value="B" <%=BillType.equals("B")?"selected":""%>><bean:message
	key="billing.billingCorrection.formBillTypeB" /></option>
<option value="S" <%=BillType.equals("S")?"selected":""%>>S
| Settled</option>
<option value="X" <%=BillType.equals("X")?"selected":""%>><bean:message
	key="billing.billingCorrection.formBillTypeX" /></option>
<option value="D" <%=BillType.equals("D")?"selected":""%>><bean:message
	key="billing.billingCorrection.formBillTypeD" /></option>
<option value="I" <%=BillType.equals("I")?"selected":""%>><bean:message
	key="billing.billingCorrection.formBillTypeI" /></option>
</select><br>

Pay Program:<br>
<input type="hidden" name="xml_payProgram" value="<%=BillDate%>" />
<select style="font-size: 80%;" id="payProgram" name="payProgram" onchange="checkPayProgram(this.options[this.selectedIndex].value)">
<%for (int i = 0; i < BillingDataHlp.getVecPaymentType().size(); i = i + 2) {

	%>
<option value="<%=BillingDataHlp.getVecPaymentType().get(i) %>"
<%=payProgram.equals(BillingDataHlp.getVecPaymentType().get(i))? "selected":"" %>><%=BillingDataHlp.getVecPaymentType().get(i + 1)%></option>
<%}

%>
</select><br>
<bean:message key="billing.billingCorrection.formBillingPhysician" />: <br />

<% // multisite start ==========================================
    if (bMultisites) {
        SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
        List<Site> sites = siteDao.getActiveSitesByProviderNo(userProviderNo);
        // now get all providers eligible


                List pList;

                if (isTeamBillingOnly || isTeamAccessPrivacy) {
                        pList = (new JdbcBillingPageUtil()).getCurTeamProviderStr(userProviderNo);
                }
                else if (isSiteAccessPrivacy) {
                        pList = (new JdbcBillingPageUtil()).getCurSiteProviderStr(userProviderNo);
                }
                else {
                        pList =  (new JdbcBillingPageUtil()).getCurProviderStr();
                }

        HashSet<String> pros=new HashSet<String>();
        for (Object s:pList) {
                pros.add(((String)s).substring(0, ((String)s).indexOf("|")));
        }
%>
      <script>
var _providers = [];
<%	for (int i=0; i<sites.size(); i++) {  
	Set<Provider> siteProviders = sites.get(i).getProviders();
	List<Provider>  siteProvidersList = new ArrayList<Provider> (siteProviders);
     Collections.sort(siteProvidersList,(new Provider()).ComparatorName());%>
	_providers["<%= sites.get(i).getName() %>"]="<% Iterator<Provider> iter = siteProvidersList.iterator();
	while (iter.hasNext()) {
		Provider p=iter.next();
		if (pros.contains(p.getProviderNo())) {
	%><option value='<%= p.getProviderNo() %>'><%= p.getLastName() %>, <%= p.getFirstName() %></option><% }} %>";
<% } %>
function changeSite(sel) {
	sel.form.provider_no.innerHTML=sel.value=="none"?"":_providers[sel.value];
	sel.style.backgroundColor=sel.options[sel.selectedIndex].style.backgroundColor;
}
      </script>
      	<select id="site" name="site" style="font-size: 80%;" onchange="changeSite(this)">
      		<option value="none" style="background-color:white">---select clinic---</option>
      	<%
      	for (int i=0; i<sites.size(); i++) {
      	%>
      		<option value="<%= sites.get(i).getName() %>" style="background-color:<%= sites.get(i).getBgColor() %>"
      			 <%=sites.get(i).getName().toString().equals(curSite)?"selected":"" %>><%= sites.get(i).getName() %></option>
      	<% } %>
      	</select>
      	<select id="provider_no" name="provider_no" style="font-size: 80%;width:140px"></select>
      	<script>
     	changeSite(document.getElementById("site"));
      	document.getElementById("provider_no").value='<%=Provider%>';
      	</script>
<%  // multisite end ==========================================
    } else {
%>
		<select
			id="provider_no" style="font-size: 80%;" name="provider_no">
			<option value=""><bean:message
				key="billing.billingCorrection.msgSelectProvider" /></option>
			<%

			List pList;

			if (isTeamBillingOnly || isTeamAccessPrivacy) {
				pList = (new JdbcBillingPageUtil()).getCurTeamProviderStr(userProviderNo);
			}
			else if (isSiteAccessPrivacy) {
				pList = (new JdbcBillingPageUtil()).getCurSiteProviderStr(userProviderNo);
			}
			else {
				pList =  (new JdbcBillingPageUtil()).getCurProviderStr();
			}


				for (int i = 0; i < pList.size(); i++) {
					String temp[] = ((String) pList.get(i)).split("\\|");

					%>
			<option value="<%=temp[0]%>"
				<%=Provider.equals(temp[0])?"selected":""%>><%=temp[0]%> |
			<%=temp[1]%>, <%=temp[2]%></option>
			<%}

				%>
		</select>
<% } %>
<input type="hidden" name="xml_provider_no" value="<%=Provider%>">
</div><!--span4-->

<div class="span4"> 
<input type="hidden" name="xml_visitdate" value="<%=visitdate%>" /> 
<div class="span4" style="margin-left:0px;">		
<label><bean:message key="billing.billingCorrection.btnAdmissionDate" />:</label>
<div class="input-append">
	<input type="text" name="xml_vdate" id="xml_vdate" value="<%=visitdate%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" style="width:90px" autocomplete="off" />
	<span class="add-on"><i class="icon-calendar"></i></span>
</div>
</div><!--date span-->
<br>

<bean:message key="billing.billingCorrection.formVisit" />: <br>
<input type="hidden" name="xml_clinic_ref_code" value="<%=location%>"> 
<select name="clinic_ref_code">
<option value=""><bean:message key="billing.billingCorrection.msgSelectLocation" /></option>
<%//
ClinicLocationDao clinicLocationDao = (ClinicLocationDao) SpringUtils.getBean("clinicLocationDao");
List<ClinicLocation> clinicLocations = clinicLocationDao.findByClinicNo(1);				
	for (ClinicLocation clinicLoc : clinicLocations) {
		BillLocationNo = clinicLoc.getClinicLocationNo();
		BillLocation = clinicLoc.getClinicLocationName();
%>
<option value="<%=BillLocationNo%>"
	<%=location.equals(BillLocationNo)?"selected":""%>><%=BillLocationNo%>
| <%=BillLocation%></option>

<%}%>
</select><br>

<%if (OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %> Clinic Nbr <% } else { %> <bean:message key="billing.billingCorrection.formVisitType"/> <% } %>: <br>

<input type="hidden" name="xml_visittype" value="<%=visittype%>">
<select style="font-size: 80%;" name="visittype">
<option value=""><bean:message key="billing.billingCorrection.msgSelectVisitType" /></option>
<% if (OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %>
 <%
    ClinicNbrDao cnDao = (ClinicNbrDao) SpringUtils.getBean("clinicNbrDao");
	ArrayList<ClinicNbr> nbrs = cnDao.findAll();
for (ClinicNbr clinic : nbrs) {
		String valueString = String.format("%s | %s", clinic.getNbrValue(), clinic.getNbrString());
		%>
	<option value="<%=valueString%>" <%=visittype.startsWith(clinic.getNbrValue())?"selected":""%>><%=valueString%></option>
<%	}
} else { %>
<option value="00" <%=visittype.equals("00")?"selected":""%>><bean:message key="billing.billingCorrection.formClinicVisit" /></option>
<option value="01" <%=visittype.equals("01")?"selected":""%>><bean:message key="billing.billingCorrection.formOutpatientVisit" /></option>
<option value="02" <%=visittype.equals("02")?"selected":""%>><bean:message key="billing.billingCorrection.formHospitalVisit" /></option>
<option value="03" <%=visittype.equals("03")?"selected":""%>><bean:message key="billing.billingCorrection.formER" /></option>
<option value="04" <%=visittype.equals("04")?"selected":""%>><bean:message key="billing.billingCorrection.formNursingHome" /></option>
<option value="05" <%=visittype.equals("05")?"selected":""%>><bean:message key="billing.billingCorrection.formHomeVisit" /></option>
<% } %>
</select><br>


<%String clinicNo = OscarProperties.getInstance().getProperty("clinic_no", "").trim();%>
<bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode"/>: <br>
	<select name="xml_slicode">
		<option value="<%=clinicNo%>" ><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.NA" /></option>
		<option value="HDS " <%=sliCode.startsWith("HDS")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HDS" /></option>
		<option value="HED " <%=sliCode.startsWith("HED")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HED" /></option>
		<option value="HIP " <%=sliCode.startsWith("HIP")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HIP" /></option>
		<option value="HOP " <%=sliCode.startsWith("HOP")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HOP" /></option>
		<option value="HRP " <%=sliCode.startsWith("HRP")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HRP" /></option>
		<option value="IHF " <%=sliCode.startsWith("IHF")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.IHF" /></option>
		<option value="OFF " <%=sliCode.startsWith("OFF")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OFF" /></option>
		<option value="OTN " <%=sliCode.startsWith("OTN")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OTN" /></option>
		<option value="PDF " <%=sliCode.startsWith("PDF")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.PDF" /></option>
		<option value="RTF " <%=sliCode.startsWith("RTF")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.RTF" /></option>
	</select>
</div><!--span4-->
</div><!-- span10 -->
</div><!--well-->


<div class="row well well-small">

<table class="table table-striped table-hover">
<thead>
	<tr>
		<td colspan=2><b><bean:message key="billing.billingCorrection.formServiceCode" /></b></td>
		<th><b><bean:message key="billing.billingCorrection.formDescription" /></b></th>
		<th><b><bean:message key="billing.billingCorrection.formUnit" /></b></th>
		<th align="right"><b><bean:message key="billing.billingCorrection.formFee" /></b></th>
		<th><font size="-1">Settle</font></th>
	</tr>
</thead>

<tbody>
<%//
        String serviceCode = "";
        String serviceDesc = "";
        String billAmount = "";
        String diagCode = "";
        String diagDesc = "";
        String billingunit = "";
        String itemStatus = "";

        if (bFlag) {
            BillingONService billingONService = (BillingONService) SpringUtils.getBean("billingONService"); 
            List<BillingONItem> bItems = billingONService.getNonDeletedInvoices(bCh1.getId());
            
            if (!bItems.isEmpty()) {

                int maxRecs = Math.max(bItems.size(), MAXRECORDS);
                for (int i = 0; i < maxRecs; i++) {
                    //multisite. skip display if billing provider_no not in current access privacy
                    if (!isMultiSiteProvider) 
                        continue;

                    serviceCode = "";
                    serviceDesc = "";
                    billAmount = "";								
                    billingunit = "";								
                    itemStatus = "";

                    if(i < bItems.size()) {

                        BillingONItem bItem = bItems.get(i);
                   
                        BillingService bService = null;                
                        if (bItem.getServiceCode().startsWith("_"))                    
                            bService = bServiceDao.searchPrivateBillingCode(bItem.getServiceCode(),bItem.getServiceDate());                
                        else                    
                            bService = bServiceDao.searchBillingCode(bItem.getServiceCode(),"ON",bItem.getServiceDate());

                        serviceCode = bItem.getServiceCode();
                        serviceDesc = bService == null ? "N/A" : bService.getDescription();
                        billAmount = bItem.getFee();
                        diagCode = bItem.getDx();
                        billingunit = bItem.getServiceCount();								
                        itemStatus = bItem.getStatus().equals("S") ? "checked" : "";                      
                    }

                    rowCount = rowCount + 1;
%>

	<tr>
		<th width="25%"><input type="hidden"
			name="xml_service_code<%=rowCount%>" value="<%=serviceCode%>">
		<input type="text" style="width: 100%"
			name="servicecode<%=rowCount-1%>" value="<%=serviceCode%>"></th>
		<td><a href=# onClick="scScriptAttach('servicecode<%=rowCount-1%>')">Search</a></td>
		<th><font size="-1"><%=serviceDesc%></font></th>
		<th><input type="hidden" name="xml_billing_unit<%=rowCount%>"
			value="<%=billingunit%>"> <input type="text"
			style="width: 100%" name="billingunit<%=rowCount-1%>"
			value="<%=billingunit%>" size="5" maxlength="5"></th>
		<th align="right"><input type="hidden"
			name="xml_billing_amount<%=rowCount%>" value="<%=billAmount%>">
		<input type="text" style="width: 100%" size="5" maxlength="6"
			id="billingamount<%=rowCount-1%>" name="billingamount<%=rowCount-1%>"
			value="<%=billAmount%>" onchange="javascript:validateNum(this)"></th>
		<td align="center"><input type="checkbox"
			name="itemStatus<%=rowCount-1%>" id="itemStatus<%=rowCount-1%>"
			value="S" <%=itemStatus %>></td>
	</tr>
<%		
                }	
            }  
        }
%>
</tbody>
</table>
</div>


<div class="row well well-small">
<div class="span10">

<b> <bean:message key="billing.billingCorrection.formDiagnosticCode" /></b>
<br>
	
<input type="hidden" name="xml_diagnostic_code"	value="<%=diagCode%>"> 
<input type="hidden" name="xml_dig_search1"> 

<div class="input-append">
<input type="text"  name="xml_diagnostic_detail" value="<%=diagCode%>" class="span8"> 
<a href="javascript:ScriptAttach()" class="btn"><i class="icon icon-search"></i></a>
</div>

</div>
</div>
	
<div class="row well well-small">
<div class="span10">

<%
	if(securityInfoManager.hasPrivilege(loggedInInfo, "_billing", "w", null)) {
%>
<%if (request.getParameter("admin")!=null || request.getParameter("adminSubmit")!=null ) { %>
<input type="hidden" name="adminSubmit" value="adminSubmit">
<input class="btn btn-primary" type="submit" name="submit" onclick="return validateAllItems();" value="Save">
<%}else{%> 
<input class="btn btn-primary" type="submit" name="submit" onclick="return validateAllItems();" value="Save">
<input class="btn" type="submit" name="submit" onclick="return validateAllItems();" value="Save&Correct Another">
<%}%>
<%}%>

<%if(billNo!=null){%>

		<a id="reprintLink" name="reprintLink" href="billingON3rdInv.jsp?billingNo=<%=billNo%>" class="btn"><i class="icon icon-print"></i> Reprint</a>
<%}%>
	
<br><br>
               
                <div class="row">
                <div class="span5">
                    <bean:message key="billing.billingCorrection.msgNotes"/>:<br>
                    <textarea name="comment" cols="32" rows=4><%=comment %></textarea>
                    <%               
                        
                        if (thirdParty && bCh1 != null && OscarProperties.getInstance().hasProperty("invoice_due_date")) {
                            BillingONExt bExtDueDate = bExtDao.getDueDate(bCh1);
                            String dueDateStr;
	                                                               
                            if (bExtDueDate == null) {
                                Integer numDaysTilDue = Integer.parseInt(OscarProperties.getInstance().getProperty("invoice_due_date", "0"));
                                dueDateStr = DateUtils.sumDate(bCh1.getBillingDate(), numDaysTilDue, request.getLocale());
                            } else {
                                dueDateStr = bExtDueDate.getValue();
                            }
                    %>
                    <br/>
                    <!--  
                    <bean:message key="billing.billingCorrection.dueDate"/><img src="../../../images/cal.gif" id="invoiceDueDate_cal" />
                    :<input type="text" maxlength="10" id="invoiceDueDate" name="invoiceDueDate" value="<%=dueDateStr%>"/>
                    -->
					<div class="span2">		
					<label><bean:message key="billing.billingCorrection.dueDate"/>:</label>
					<div class="input-append">
						<input type="text" name="invoiceDueDate" id="invoiceDueDate" value="<%=dueDateStr%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" style="width:90px" />
						<span class="add-on"><i class="icon-calendar"></i></span>
					</div>
					</div>
                    <% } %>
                </div>
                
                    <div class="span5" id="thirdParty" style=" <%=thirdParty ? "" : "display:none"%>">
                        <a href="#" onclick="search3rdParty('billTo');return false;"><bean:message key="billing.billingCorrection.msgPayer"/></a><br>
                        <textarea id="billTo" name="billTo" cols="32" rows=4><%=payer%></textarea>
                          <% String useDemoClinicInfoOnInvoice = oscar.OscarProperties.getInstance().getProperty("useDemoClinicInfoOnInvoice","");
                             if (bCh1 != null && !useDemoClinicInfoOnInvoice.isEmpty() && useDemoClinicInfoOnInvoice.equals("true")) { 
                                BillingONExt bExtUseBillTo = bExtDao.getUseBillTo(bCh1);
                                String selectUseBillTo=""; 
                                                               
                                if ((bExtUseBillTo != null) && bExtUseBillTo.getValue().equalsIgnoreCase("on")) {
                                    selectUseBillTo = "checked";
                                } 
                           %>
                                <br><bean:message key="billing.billingCorrection.useDemoContactYesNo"/>:<input type="checkbox" name="overrideUseDemoContact" id="overrideUseDemoContact" <%=selectUseBillTo%> />
                          <% } %>
                    </div>
                    </div>
 
</div>
</div>

</div>
<div id="thirdPartyPymnt" style="<%=thirdParty ? "" : "display:none"%>"">
<%=htmlPaid %>
</div>

</html:form>
        
<div >



<% if (thirdParty && bCh1 != null && OscarProperties.getInstance().hasProperty("invoice_due_date")) { %>
<script type="text/javascript">
    var startDate = $("#invoiceDueDate").datepicker({
    	format : "yyyy-mm-dd"
    });
</script>
<% } %>
<%!
    String nullToEmpty(String str) {
        return (str == null ? "" : str);
    }	
%>


</div>
</body>
<script type="text/javascript">
    var startDate = $("#xml_appointment_date").datepicker({
    	format : "yyyy-mm-dd"
    });
    var startDate = $("#xml_vdate").datepicker({
    	format : "yyyy-mm-dd"
    });

    window.setTimeout(function() {
    	  $("#alert_message").fadeTo(500, 0).slideUp(500, function(){
    	    $(this).remove(); 
    	  });
    }, 5000);
	
    $( document ).ready(function() {
    	parent.parent.resizeIframe($('html').height());

    });

Calendar.setup( { inputField : "xml_appointment_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "xml_appointment_date_cal", singleClick : true, step : 1 } );
Calendar.setup( { inputField : "xml_vdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 } );
function display3rdPartyPayments() {
    popupPage('800', '860', 'billingON3rdPayments.do?method=listPayments&billingNo=<%= billNo %>');
}
</script>

</html:html>
