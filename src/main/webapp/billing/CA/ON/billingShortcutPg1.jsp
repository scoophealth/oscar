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
<%@page import="org.oscarehr.common.model.DiagnosticCode"%>
<%@page import="org.oscarehr.common.dao.DiagnosticCodeDao"%>
<%@page import="org.oscarehr.common.dao.CtlBillingServiceDao"%>
<%@page import="org.oscarehr.common.model.CtlBillingServicePremium"%>
<%@page import="org.oscarehr.common.dao.CtlBillingServicePremiumDao"%>
<%@page import="java.util.Date"%>
<%@page import="org.oscarehr.common.model.BillingService"%>
<%@page import="org.oscarehr.common.model.CtlBillingService"%>
<%@page import="org.oscarehr.common.dao.BillingServiceDao"%>
<%@page import="org.oscarehr.common.model.ClinicLocation"%>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao"%>
<%@page import="org.oscarehr.billing.CA.model.BillingDetail"%>
<%@page import="org.oscarehr.billing.CA.dao.BillingDetailDao"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.common.model.Billing"%>
<%@page import="org.oscarehr.common.dao.BillingDao"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../../../logout.jsp");
  }

  String user_no         = (String)session.getAttribute("user");
  String providerview    = request.getParameter("providerview") == null
                           ? ""
                           : request.getParameter("providerview");
  String asstProvider_no = "";
  String color           = "";
  String premiumFlag     = "";
  String service_form    = "";
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page errorPage="errorpage.jsp"%>
<%@ page import="java.util.*,java.net.*, java.sql.*, oscar.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.ClinicNbr"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.dao.ClinicNbrDao"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>

<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist" %>
<%@page import="org.oscarehr.common.dao.ProfessionalSpecialistDao" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%
	ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
%>
<%
  boolean bHospitalBilling = true;
  String            clinicview        = bHospitalBilling? oscarVariables.getProperty("clinic_hospital", "") : oscarVariables.getProperty("clinic_view", "");
  String            clinicNo          = oscarVariables.getProperty("clinic_no", "");
  String            visitType         = bHospitalBilling? "02" : oscarVariables.getProperty("visit_type", "");
  String            appt_no           = request.getParameter("appointment_no");
  String            demoname          = request.getParameter("demographic_name");
  String            demo_no           = request.getParameter("demographic_no");
  String            apptProvider_no   = request.getParameter("apptProvider_no");
  String ctlBillForm = request.getParameter("billForm");
  String            assgProvider_no   = request.getParameter("assgProvider_no");
  //String            dob               = request.getParameter("dob");
  String            demoSex           = request.getParameter("DemoSex");
  GregorianCalendar now               = new GregorianCalendar();
  int               curYear           = now.get(Calendar.YEAR);
  int               curMonth          = (now.get(Calendar.MONTH) + 1);
  int               curDay            = now.get(Calendar.DAY_OF_MONTH);
  int               dob_year          = 0, dob_month = 0, dob_date = 0, age = 0;

  ResourceBundle res = ResourceBundle.getBundle("oscarResources", request.getLocale());
  
  String            msg               = res.getString("billing.hospitalBilling.msgDates");
  String            action            = "edit";
  Properties        propHist          = null;
  Vector            vecHist           = new Vector();

  // get provider's detail
  String proOHIPNO="", proRMA="";
  ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
  Provider prov = null;
  if(request.getParameter("xml_provider") != null) {
  	prov = providerDao.getProvider(request.getParameter("xml_provider"));
  }
  if (prov != null) {
	proOHIPNO = prov.getOhipNo();
	proRMA = prov.getRmaNo();
  }
  if(request.getParameter("xml_provider")!=null) providerview = request.getParameter("xml_provider");
  // get patient's detail
  String errorFlag = "";
  String warningMsg = "", errorMsg = "";
  String r_doctor="", r_doctor_ohip="" ;
  String demoFirst="", demoLast="", demoHIN="", demoDOB="", demoDOBYY="", demoDOBMM="", demoDOBDD="", demoHCTYPE="";
  
  DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
  Demographic demo = demoDao.getDemographic(demo_no);
  if (demo != null) {
    assgProvider_no = demo.getProviderNo();
	demoFirst = demo.getFirstName();
	demoLast = demo.getLastName();
	demoHIN = demo.getHin() + demo.getVer();
	demoSex = demo.getSex();
	if (demoSex.compareTo("M")==0) demoSex ="1";
	if (demoSex.compareTo("F")==0) demoSex ="2";

	demoHCTYPE = demo.getHcType() == null?"":demo.getHcType();
	if (demoHCTYPE.compareTo("") == 0 || demoHCTYPE == null || demoHCTYPE.length() <2) {
		demoHCTYPE="ON";
	}else{
		demoHCTYPE=demoHCTYPE.substring(0,2).toUpperCase();
	}
	demoDOBYY = demo.getYearOfBirth();
	demoDOBMM = demo.getMonthOfBirth();
	demoDOBDD = demo.getDateOfBirth();

	if (demo.getFamilyDoctor() == null){
		r_doctor = "N/A"; r_doctor_ohip="000000";
	}else{
		r_doctor=SxmlMisc.getXmlContent(demo.getFamilyDoctor(),"rd")==null ? "" : SxmlMisc.getXmlContent(demo.getFamilyDoctor(), "rd");
		r_doctor_ohip=SxmlMisc.getXmlContent(demo.getFamilyDoctor(),"rdohip")==null ? "" : SxmlMisc.getXmlContent(demo.getFamilyDoctor(), "rdohip");
	}

	demoDOBMM = demoDOBMM.length() == 1 ? ("0" + demoDOBMM) : demoDOBMM;
	demoDOBDD = demoDOBDD.length() == 1 ? ("0" + demoDOBDD) : demoDOBDD;
	demoDOB = demoDOBYY + demoDOBMM + demoDOBDD;

	if (demo.getHin() == null ) {
		errorFlag = "1";
		errorMsg = errorMsg + "<br><b><font color='red'>Error: The patient does not have a valid HIN. </font></b><br>";
	} else if (demo.getHin().equals("")) {
		warningMsg += "<br><b><font color='orange'>Warning: The patient does not have a valid HIN. </font></b><br>";
	}
	if (r_doctor_ohip != null && r_doctor_ohip.length()>0 && r_doctor_ohip.length() != 6) {
		warningMsg += "<br><font color='orange'>Warning: the referral doctor's no is wrong. </font><br>";
	}
	if (demoDOB.length() != 8) {
		errorFlag = "1";
		errorMsg = errorMsg + "<br><b><font color='red'>Error: The patient does not have a valid DOB. </font></b><br>";
	}
  }

  // get patient's billing history
  boolean bFirst = true;
  Vector vecHistD = new Vector();
  List aL = null;

  OscarProperties props = OscarProperties.getInstance();
  if(!props.getProperty("isNewONbilling", "").equals("true")) {

  BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
  for(Billing b : billingDao.findActiveBillingsByDemoNo(ConversionUtils.fromIntString(demo_no), 5)) {
    propHist = new Properties();

    propHist.setProperty("billing_no", "" + b.getId());
    propHist.setProperty("visitdate", ConversionUtils.toDateString(b.getVisitDate())); // admission date
    propHist.setProperty("billing_date", ConversionUtils.toDateString(b.getBillingDate())); // service date
    propHist.setProperty("update_date", ConversionUtils.toDateString(b.getUpdateDate())); // create date
    propHist.setProperty("visitType", b.getVisitType());
    propHist.setProperty("clinic_ref_code", b.getClinicRefCode());
    vecHist.add(propHist);

    // get the latest ref. doctor number
    if(bFirst && "checked".equals(SxmlMisc.getXmlContent(b.getContent(),"xml_referral")) ) {
        bFirst = false;
		r_doctor_ohip= SxmlMisc.getXmlContent(b.getContent(), "rdohip");
    }
  }

  BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
  for (int i = 0; i < vecHist.size(); i++) {
    String billingNo = ((Properties)vecHist.get(i)).getProperty("billing_no", "");

    String dx      = "";
    String serCode = "";

    for(BillingDetail bd : billingDetailDao.findByBillingNo(ConversionUtils.fromIntString(billingNo))) {
      if (dx.equals("") || !dx.equals(bd.getDiagnosticCode())) {
        dx += (dx.equals("")
        ? ""
        : ", ") + bd.getDiagnosticCode();
      }

      if (serCode.equals("") || !serCode.equals(bd.getServiceCode())) {
        serCode += (serCode.equals("")
        ? ""
        : ", ") + bd.getServiceCode() + " x " + bd.getBillingUnit();
      }
    }

    propHist = new Properties();

    propHist.setProperty("service_code", serCode);
    propHist.setProperty("diagnostic_code", dx);
    vecHistD.add(propHist);
  }
  } else {
		JdbcBillingReviewImpl hdbObj = new JdbcBillingReviewImpl();
		aL = hdbObj.getBillingHist(demo_no, 5,0, null);
		if (aL.size()>0) {
			BillingClaimHeader1Data obj = (BillingClaimHeader1Data) aL.get(0);
			BillingItemData iobj = (BillingItemData) aL.get(1);

			propHist = new Properties();

			propHist.setProperty("visitdate", obj.getAdmission_date() == null ? "" : obj.getAdmission_date()); // admission date
			propHist.setProperty("visitType", obj.getVisittype());
			propHist.setProperty("clinic_ref_code", obj.getFacilty_num());
			vecHist.add(propHist);
			propHist.setProperty("diagnostic_code", iobj.getDx());
			vecHistD.add(propHist);
		}

  }

  // display the fixed billing part
  // Retrieving Provider
  Vector vecProvider = new Vector();
  Properties propT = null;
  
  ProviderDao prDao = SpringUtils.getBean(ProviderDao.class);
  for(Provider pr : prDao.getDoctorsWithOhip()) {
    propT = new Properties();
    propT.setProperty("last_name", pr.getLastName());
    propT.setProperty("first_name", pr.getFirstName());
    propT.setProperty("proOHIP", pr.getProviderNo());
    vecProvider.add(propT);
  }
  // clinic location
  Vector vecLocation = new Vector();
  
  ClinicLocationDao clDao = SpringUtils.getBean(ClinicLocationDao.class);
  for(ClinicLocation cl : clDao.findAll()) {
    propT = new Properties();
    propT.setProperty("clinic_location_name", cl.getClinicLocationName());
    propT.setProperty("clinic_location_no", cl.getClinicLocationNo());
    vecLocation.add(propT);
  }

  // set default value
  // use parameter -> history record
  if( StringUtils.trimToNull(r_doctor_ohip) != null ) {
  	ProfessionalSpecialist specialist = professionalSpecialistDao.getByReferralNo(r_doctor_ohip);
  	if(specialist != null) {
  	          	r_doctor = specialist.getLastName() + "," + specialist.getFirstName();
  	}
  }

  String paraName = request.getParameter("dxCode");
  String dxCode = getDefaultValue(paraName, vecHistD, "diagnostic_code");

  //visitType
  paraName = request.getParameter("xml_visittype");
  String xml_visittype = getDefaultValue(paraName, vecHist, "visitType");
  if(!"".equals(xml_visittype)) {
    visitType = xml_visittype;
  } else {
    visitType = visitType==null? "":visitType;
  }

  paraName = request.getParameter("xml_location");
  String xml_location = getDefaultValue(paraName, vecHist, "clinic_ref_code");
  if(!"".equals(xml_location)) {
    clinicview = xml_location;
  } else {
    clinicview = clinicview==null? "":clinicview;
  }

  String visitdate = null;
  paraName = request.getParameter("xml_vdate");
  String xml_vdate = getDefaultValue(paraName, vecHist, "visitdate");
  if(!"".equals(xml_vdate)) {
    visitdate = xml_vdate;
  } else {
    visitdate = visitdate==null? "":visitdate;
  }


  // get billing dx/form info
  Vector vecCodeCol1 = new Vector();
  Vector vecCodeCol2 = new Vector();
  Vector vecCodeCol3 = new Vector();
  Properties propPremium = new Properties();
  String serviceCode="", serviceDesc="", serviceValue="", servicePercentage="", serviceType="",serviceDisp="", serviceSLI="";
  String headerTitle1="", headerTitle2="", headerTitle3="";

  //int CountService = 0;
  //int Count2 = 0;
  BillingServiceDao bsDao = SpringUtils.getBean(BillingServiceDao.class);
  // "select c.service_group_name, c.service_order,b.service_code, b.description, b.value, b.percentage, b.sliFlag"
  for(Object[] o : bsDao.findBillingServiceAndCtlBillingServiceByMagic(ctlBillForm, "Group1", new Date())) {
	BillingService b = (BillingService) o[0];
	CtlBillingService c = (CtlBillingService) o[1];
	  
	propT = new Properties();
	headerTitle1 = c.getServiceGroupName();
    propT.setProperty("serviceCode",b.getServiceCode());
    propT.setProperty("serviceDesc",b.getDescription());
    propT.setProperty("serviceDisp",b.getValue());
    propT.setProperty("servicePercentage",Misc.getStr(b.getPercentage(), ""));
    propT.setProperty("serviceSLI", Misc.getStr("" + b.getSliFlag(), "false"));
    vecCodeCol1.add(propT);
  }
  
  if(!vecCodeCol1.isEmpty()) {
	  List<String> svcCodes = new ArrayList<String>();
	  for(int i=0; i<vecCodeCol1.size(); i++) {
		  svcCodes.add(((Properties)vecCodeCol1.get(i)).getProperty("serviceCode"));
	  }
	  
	  CtlBillingServicePremiumDao cprDao = SpringUtils.getBean(CtlBillingServicePremiumDao.class); 
	  for(CtlBillingServicePremium pr : cprDao.findByServceCodes(svcCodes)) {
	    propPremium.setProperty(pr.getServiceCode(), "A");
	  }
  }

  for(Object[] o : bsDao.findBillingServiceAndCtlBillingServiceByMagic(ctlBillForm, "Group2", new Date())) {
		BillingService b = (BillingService) o[0];
		CtlBillingService c = (CtlBillingService) o[1];

		propT = new Properties();
		headerTitle1 = c.getServiceGroupName();
	    propT.setProperty("serviceCode",b.getServiceCode());
	    propT.setProperty("serviceDesc",b.getDescription());
	    propT.setProperty("serviceDisp",b.getValue());
	    propT.setProperty("servicePercentage",Misc.getStr(b.getPercentage(), ""));
	    propT.setProperty("serviceSLI", Misc.getStr("" + b.getSliFlag(), "false"));
		vecCodeCol2.add(propT);
  }
  
  if(!vecCodeCol2.isEmpty()) {
	  List<String> svcCodes = new ArrayList<String>();
	  for(int i=0; i<vecCodeCol2.size(); i++) {
		  svcCodes.add(((Properties)vecCodeCol2.get(i)).getProperty("serviceCode"));
	  }
	  
	  CtlBillingServicePremiumDao cprDao = SpringUtils.getBean(CtlBillingServicePremiumDao.class); 
	  for(CtlBillingServicePremium pr : cprDao.findByServceCodes(svcCodes)) {
	    propPremium.setProperty(pr.getServiceCode(), "A");
	  }
  }

  for(Object[] o : bsDao.findBillingServiceAndCtlBillingServiceByMagic(ctlBillForm, "Group3", new Date())) {
	BillingService b = (BillingService) o[0];
	CtlBillingService c = (CtlBillingService) o[1];
	  
	propT = new Properties();
	headerTitle1 = c.getServiceGroupName();
    propT.setProperty("serviceCode",b.getServiceCode());
    propT.setProperty("serviceDesc",b.getDescription());
    propT.setProperty("serviceDisp",b.getValue());
    propT.setProperty("servicePercentage",Misc.getStr(b.getPercentage(), ""));
    propT.setProperty("serviceSLI", Misc.getStr("" + b.getSliFlag(), "false"));
    vecCodeCol3.add(propT);
  }
  
  if(!vecCodeCol3.isEmpty()) {
	  List<String> svcCodes = new ArrayList<String>();
	  for(int i=0; i<vecCodeCol3.size(); i++) {
		  svcCodes.add(((Properties)vecCodeCol3.get(i)).getProperty("serviceCode"));
	  }
	  
	  CtlBillingServicePremiumDao cprDao = SpringUtils.getBean(CtlBillingServicePremiumDao.class); 
	  for(CtlBillingServicePremium pr : cprDao.findByServceCodes(svcCodes)) {
	    propPremium.setProperty(pr.getServiceCode(), "A");
	  }
  }
  // create msg
  msg += errorMsg + warningMsg;

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>HospitalBilling</title>
<link rel="stylesheet" type="text/css" href="billingON.css" />

<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript"
	src="../../../share/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.js"></script>
<script type="text/javascript" language="JavaScript">

            <!--
window.focus();

function checkSli() {
	var needsSli = false;
    jQuery("input[name^=code_xml_]:checked").each(function() {
            needsSli = needsSli || eval(jQuery("input[name='sli_xml_" + this.name.substring(9) + "']").val());
    });
    jQuery("input[name^=serviceDate][value!='']").each(function() {
            needsSli = needsSli || eval(jQuery("input[name='sli_xml_" + this.value + "']").val());
    });
    return !needsSli || jQuery("select[name='xml_slicode']").get(0).selectedIndex != 0;
}


function gotoBillingOB() {
  if(self.location.href.lastIndexOf("?") > 0) {
    a = self.location.href.substring(self.location.href.lastIndexOf("?"));
  }
  self.location.href = "billingOB.jsp" + a ;
}
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
    function onNext() {
        //document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if (!(ret = checkSli())) {
        	alert("You have selected billing codes that require an SLI code but have not provided an SLI code.");
        }
        return ret;
    }
    function checkAllDates() {
	    document.forms[0].serviceDate0.value = document.forms[0].serviceDate0.value.toUpperCase();
	    document.forms[0].serviceDate1.value = document.forms[0].serviceDate1.value.toUpperCase();
	    document.forms[0].serviceDate2.value = document.forms[0].serviceDate2.value.toUpperCase();
        var b = true;
        if(document.forms[0].billDate.value.length<1){
        	alert("No billing date!");
            b = false;
        } else if(!isChecked("code_xml_") && document.forms[0].serviceDate0.value.length!=5 || !isServiceCode(document.forms[0].serviceDate0.value)){
        	alert("Need service code!");
            b = false;
        } else if(document.forms[0].serviceDate1.value.length>0 && document.forms[0].serviceDate1.value.length!=5 || !isServiceCode(document.forms[0].serviceDate1.value)){
        	alert("Wrong service code 2!");
            b = false;
        } else if(document.forms[0].serviceDate2.value.length>0 && document.forms[0].serviceDate2.value.length!=5 || !isServiceCode(document.forms[0].serviceDate2.value)){
        	alert("Wrong service code 3!");
            b = false;
        } else if(document.forms[0].serviceDate3.value.length>0 && document.forms[0].serviceDate3.value.length!=5 || !isServiceCode(document.forms[0].serviceDate3.value)){
        	alert("Wrong service code 4!");
            b = false;
        } else if(document.forms[0].serviceDate4.value.length>0 && document.forms[0].serviceDate4.value.length!=5 || !isServiceCode(document.forms[0].serviceDate4.value)){
        	alert("Wrong service code 5!");
            b = false;
        } else if(document.forms[0].dxCode.value.length!=3){
        	alert("Wrong dx code!");
            b = false;
        //} else if(document.forms[0].xml_provider.options[0].selected){
        } else if(document.forms[0].xml_provider.value=="000000"){
        	alert("Please select a provider.");
            b = false;
        }
        <% if (!OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %>
        else if(document.forms[0].xml_visittype.options[2].selected && (document.forms[0].xml_vdate.value=="" || document.forms[0].xml_vdate.value=="0000-00-00")){
        	alert("Need an admission date.");
            b = false;
        }
        <% } %>

		if(document.forms[0].xml_vdate.value.length>0) {
        	b = checkServiceDate(document.forms[0].xml_vdate.value);
        }
        if(document.forms[0].billDate.value.length>0) {
        	var billDateA = document.forms[0].billDate.value.split("\n");
        	for (var i in billDateA) {
        		var v = billDateA[i];
        		if (v) {
					//alert(" !" + v);
					b = checkServiceDate(v);
				}
			}
        }

        if(!isInteger(document.forms[0].dxCode.value)) {
        	alert("Wrong dx code!");
            b = false;
        }
        if(document.forms[0].referralCode.value.length>0) {
          if(document.forms[0].referralCode.value.length!=6 || !isInteger(document.forms[0].referralCode.value)) {
        	alert("Wrong referral code!");
            b = false;
          }
        }

        return b;
    }
function checkServiceDate(s) {
	var calDate=new Date();
	varYear = calDate.getFullYear();
	varMonth = calDate.getMonth()+1;
	varDate = calDate.getDate();
    var str_date = s; //document.forms[0].xml_appointment_date.value;
    var yyyy = str_date.substring(0, str_date.indexOf("-"));
	var mm = str_date.substring(eval(str_date.indexOf("-")+1), str_date.lastIndexOf("-"));
	var dd = str_date.substring(eval(str_date.lastIndexOf("-")+1));
	var bWrongDate = false;
	sMsg = "";
	if(yyyy > varYear) {
		sMsg = "year";
		bWrongDate = true;
	} else if(yyyy == varYear && mm > varMonth) {
		sMsg = "month";
		bWrongDate = true;
	} else if(yyyy == varYear && mm == varMonth && dd > varDate) {
		sMsg = "date";
		bWrongDate = true;
	}
	if(bWrongDate) {
		alert("Warning - Service/Admission Date is future dated!");
		return false;
	} else {
		return true;
	}
}

    function isInteger(s){
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        // All characters are numbers.
        return true;
    }

    function isServiceCode(s){
        // temp for 0.
    	if(s.length==0) return true;
    	if(s.length!=5) return false;
        if((s.charAt(0) < "A") || (s.charAt(0) > "Z")) return false;
        if((s.charAt(4) < "A") || (s.charAt(4) > "Z")) return false;

        var i;
        for (i = 1; i < s.length-1; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        return true;
    }
function isChecked(s) {
    for (var i =0; i <document.forms[0].elements.length; i++) {
        if (document.forms[0].elements[i].name.indexOf(s)==0 && document.forms[0].elements[i].name.length==14) {
            if (document.forms[0].elements[i].checked) {
				return true;
			}
    	}
	}
	return false;
}
var remote=null;
function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
  //if (remote != null) {
  //  if (remote.opener == null)
  //    remote.opener = self;
  //}
  //if (x == 1) { return remote; }
}
var awnd=null;
function referralScriptAttach(elementName) {
     var d = elementName;
     t0 = escape("document.forms[0].elements[\'"+d+"\'].value");
     //t1 = escape("");
     //alert(('searchRefDoc.jsp?param='+t0));
     awnd=rs('att',('searchRefDoc.jsp?param='+t0),600,600,1);
     awnd.focus();
}
function referralScriptAttach2(elementName, name2) {
     var d = elementName;
     t0 = escape("document.forms[0].elements[\'"+d+"\'].value");
     t1 = escape("document.forms[0].elements[\'"+name2+"\'].value");
     awnd=rs('att',('searchRefDoc.jsp?param='+t0+'&param2='+t1),600,600,1);
     awnd.focus();
}
function dxScriptAttach(name2) {
	f0 = escape(document.forms[0].dxCode.value);
    f1 = escape("document.forms[0].elements[\'"+name2+"\'].value");
	awnd=rs('att','billingDigSearch.jsp?name='+f0 + '&search=&name2='+f1,600,600,1);
	awnd.focus();
}

function onDblClickServiceCode(item) {
	//alert(item.id);
	if(document.forms[0].serviceDate0.value=="") {
		document.forms[0].serviceDate0.value = item.id.substring(3);
	} else if(document.forms[0].serviceDate1.value=="") {
		document.forms[0].serviceDate1.value = item.id.substring(3);
	} else if(document.forms[0].serviceDate2.value=="") {
		document.forms[0].serviceDate2.value = item.id.substring(3);
	}
}

//-->

  </script>
</head>

<body onload="setfocus();" topmargin="0">
<div id="Layer1"
	style="position: absolute; left: 360px; top: 165px; width: 410px; height: 200px; z-index: 1; background-color: #FFCC00; layer-background-color: #FFCC00; border: 1px none #000000; visibility: hidden">
<table width="98%" border="0" cellspacing="0" cellpadding="0"
	align=center>
	<tr bgcolor="#393764">
		<td width="96%" height="7" bgcolor="#FFCC00"><font size="-2"
			face="Geneva, Arial, Helvetica, san-serif" color="#000000"><b><bean:message key="billing.billingform"/>
		</b></font></td>
		<td width="3%" bgcolor="#FFCC00" height="7"><b><a href="#"
			onClick="showHideLayers('Layer1','','hide');return false;">X</a></b></td>
	</tr>

	<%
String ctlcode="", ctlcodename="", currentFormName="";
int ctlCount = 0;

  CtlBillingServiceDao cbsDao = SpringUtils.getBean(CtlBillingServiceDao.class); 
  for(Object[] o : cbsDao.findServiceTypesByStatus("A")) {
	ctlcodename = String.valueOf(o[0]);
	ctlcode = String.valueOf(o[1]);
	ctlCount++;
	if(ctlcode.equals(ctlBillForm)) {
		currentFormName = ctlcodename;
	}
%>
	<tr bgcolor=<%=ctlCount%2==0 ? "#FFFFFF" : "#EEEEFF"%>>
		<td colspan="2"><b><font size="-2" color="#7A388D"><a
			href="billingShortcutPg1.jsp?billForm=<%=ctlcode%>&hotclick=<%=URLEncoder.encode("","UTF-8")%>&appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname,"UTF-8")%>&demographic_no=<%=request.getParameter("demographic_no")%>&user_no=<%=user_no%>&apptProvider_no=<%=request.getParameter("apptProvider_no")%>&providerview=<%=request.getParameter("apptProvider_no")%>&appointment_date=<%=request.getParameter("appointment_date")%>&status=<%=request.getParameter("status")%>&start_time=<%=request.getParameter("start_time")%>&bNewForm=1"
			onClick="showHideLayers('Layer1','','hide');"><%=ctlcodename%></a></font></b></td>
	</tr>
	<%
}
%>
</table>
</div>
<div id="Layer2"
	style="position: absolute; left: 1px; top: 26px; width: 332px; height: 600px; z-index: 2; background-color: #FFCC00; layer-background-color: #FFCC00; border: 1px none #000000; visibility: hidden">
<table width="98%" border="0" cellspacing="0" cellpadding="0"
	align=center>
	<tr>
		<td width="18%"><b><font size="-2"><bean:message key="billing.hospitalBilling.formDxCode"/></font></b></td>
		<td width="76%"><b><font size="-2"><bean:message key="billing.billingCorrection.formDescription"/></font></b></td>
		<td width="6%"><a href="#"
			onClick="showHideLayers('Layer2','','hide');return false">X</a></td>
	</tr>

	<%
String ctldiagcode="", ctldiagcodename="";
ctlCount = 0;
  DiagnosticCodeDao dcDao = SpringUtils.getBean(DiagnosticCodeDao.class);
  for(Object[] o : dcDao.findDiagnosictsAndCtlDiagCodesByServiceType(ctlBillForm)){
	  DiagnosticCode dc = (DiagnosticCode) o[0];
	  ctldiagcode = dc.getDiagnosticCode();
	  ctldiagcodename = dc.getDescription();
%>
	<tr bgcolor=<%=ctlCount%2==0 ? "#FFFFFF" : "#EEEEFF"%>>
		<td width="18%"><b><font size="-2" color="#7A388D"><a
			href="#"
			onClick="document.forms[0].dxCode.value='<%=ctldiagcode%>';showHideLayers('Layer2','','hide');return false;"><%=ctldiagcode%></a></font></b></td>
		<td colspan="2"><font size="-2" color="#7A388D"><a
			href="#"
			onClick="document.forms[0].dxCode.value='<%=ctldiagcode%>';showHideLayers('Layer2','','hide');return false;">
		<%=ctldiagcodename.length() < 56 ? ctldiagcodename : ctldiagcodename.substring(0,55)%></a></font></td>
	</tr>
	<%
}
%>
</table>
</div>


<form method="post" name="titlesearch" action="billingShortcutPg2.jsp"
	onsubmit="return onNext();">
<table border="0" cellpadding="0" cellspacing="2" width="100%"
	bgcolor="#CCCCFF">
	<tr>
		<td>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td><b><bean:message key="billing.hospitalBilling.formOscarBilling"/> </b></td>
				<td align="right"><input type="submit" name="submit"
					value="<bean:message key="billing.hospitalBilling.btnNext"/>" style="width: 120px;" /> <input type="button"
					name="button" value="<bean:message key="global.btnExit"/>" style="width: 120px;"
					onClick="self.close();" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr bgcolor="#33CCCC">
				<td nowrap bgcolor="#FFCC99" width="10%" align="center"><%= demoname %>
				</td>
				<td bgcolor="#99CCCC" align="center"><font color="black"><%= msg %></font>
				</td>
			</tr>
		</table>

		<table border="1" cellspacing="0" cellpadding="0" width="100%"
			bordercolorlight="#99A005" bordercolordark="#FFFFFF"
			bgcolor="#FFFFFF">
			<tr>
				<td width="50%">

				<table border="1" cellspacing="2" cellpadding="0" width="100%"
					bordercolorlight="#99A005" bordercolordark="#FFFFFF"
					bgcolor="ivory">
					<tr>
						<td nowrap width="30%" align="center"><a id="trigger"
							href="#">[<bean:message key="billing.servicedate"/>]</a><br>
						<textarea name="billDate" cols="11" rows="5" readonly><%=request.getParameter("billDate")!=null?request.getParameter("billDate"):""%></textarea>
						</td>
						<td nowrap align="center"><bean:message key="billing.billingCorrection.formServiceCode"/> x <bean:message key="billing.billingCorrection.formUnit"/><br>
						<input type="text" name="serviceDate0" size="5" maxlength="5"
							value="<%=request.getParameter("serviceDate0")!=null?request.getParameter("serviceDate0"):""%>">x
						<input type="text" name="serviceUnit0" size="2" maxlength="2"
							style=""
							value="<%=request.getParameter("serviceUnit0")!=null?request.getParameter("serviceUnit0"):""%>"><br>
						<input type="text" name="serviceDate1" size="5" maxlength="5"
							value="<%=request.getParameter("serviceDate1")!=null?request.getParameter("serviceDate1"):""%>">x
						<input type="text" name="serviceUnit1" size="2" maxlength="2"
							style=""
							value="<%=request.getParameter("serviceUnit1")!=null?request.getParameter("serviceUnit1"):""%>"><br>
						<input type="text" name="serviceDate2" size="5" maxlength="5"
							value="<%=request.getParameter("serviceDate2")!=null?request.getParameter("serviceDate2"):""%>">x
						<input type="text" name="serviceUnit2" size="2" maxlength="2"
							style=""
							value="<%=request.getParameter("serviceUnit2")!=null?request.getParameter("serviceUnit2"):""%>"><br>
                                                 <input type="text" name="serviceDate3" size="5" maxlength="5"
							value="<%=request.getParameter("serviceDate3")!=null?request.getParameter("serviceDate3"):""%>">x
						<input type="text" name="serviceUnit3" size="2" maxlength="2"
							style=""
							value="<%=request.getParameter("serviceUnit3")!=null?request.getParameter("serviceUnit3"):""%>"><br>
                                                <input type="text" name="serviceDate4" size="5" maxlength="5"
							value="<%=request.getParameter("serviceDate4")!=null?request.getParameter("serviceDate4"):""%>">x
						<input type="text" name="serviceUnit4" size="2" maxlength="2"
							style=""
							value="<%=request.getParameter("serviceUnit4")!=null?request.getParameter("serviceUnit4"):""%>">
						</td>
						<td valign="top">
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td><a href="#"
									onClick="showHideLayers('Layer2','','show','Layer1','','hide'); return false;"><bean:message key="billing.hospitalBilling.formDx"/></a><br>
								<input type="text" name="dxCode" size="5" maxlength="5"
									onDblClick="dxScriptAttach('dxCode')"
									value="<%=request.getParameter("dxCode")!=null?request.getParameter("dxCode"):dxCode%>">
								</td>
								<td>Cal.% mode<br>
								<select name="rulePerc">
									<% String rulePerc= request.getParameter("rulePerc")!=null?request.getParameter("rulePerc"):""; %>
									<option value="onlyAboveCode"
										<%="onlyAboveCode".equals(rulePerc)?"selected":""%>><bean:message key="billing.hospitalBilling.optAbove"/>
									</option>
									<option value="allAboveCode"
										<%="allAboveCode".equals(rulePerc)?"selected":""%>><bean:message key="billing.hospitalBilling.optAll"/></option>
								</select></td>
							</tr>
						</table>

						<hr>
						<a
							href="javascript:referralScriptAttach2('referralCode','referralDocName')"><bean:message key="billing.hospitalBilling.btnReferral"/>
                                                </a> <input type="text" name="referralCode" size="5"
							maxlength="6"
							value="<%=request.getParameter("referralCode")!=null?request.getParameter("referralCode"):r_doctor_ohip%>"><br>
						<input type="text" name="referralDocName" size="22" maxlength="30"
							value="<%=request.getParameter("referralDocName")!=null?request.getParameter("referralDocName"):r_doctor%>">
						</td>
					</tr>
				</table>

				</td>
				<td valign="top">

				<table border="1" cellspacing="2" cellpadding="0" width="100%"
					bordercolorlight="#99A005" bordercolordark="#FFFFFF"
					bgcolor="#EEEEFF">
					<tr>
						<td nowrap width="30%" align="center"><b><bean:message key="billing.hospitalBilling.frmBillPhysician"/>
						</b></td>
						<td width="20%"><select name="xml_provider">
							<%
				if(vecProvider.size()==1) {
					propT = (Properties) vecProvider.get(0);
				%>
							<option value="<%=propT.getProperty("proOHIP")%>"
								<%=providerview.equals(propT.getProperty("proOHIP"))?"selected":""%>><b><%=propT.getProperty("last_name")%>,
							<%=propT.getProperty("first_name")%></b></option>
							<%	} else { %>
							<option value="000000"
								<%=providerview.equals("000000")?"selected":""%>><b><bean:message key="billing.billingCorrection.msgSelectProvider"/>
							</b></option>
							<%
				for(int i=0; i<vecProvider.size(); i++) {
					propT = (Properties) vecProvider.get(i);
				%>
							<option value="<%=propT.getProperty("proOHIP")%>"
								<%=providerview.equals(propT.getProperty("proOHIP"))?"selected":""%>><b><%=propT.getProperty("last_name")%>,
							<%=propT.getProperty("first_name")%></b></option>
							<%	}
				}
				%>
						</select></td>
						<td nowrap width="30%" align="center"><b><bean:message key="billing.hospitalBilling.frmAssgnPhysician"/></b></td>
						<td width="20%"><%=providerBean.getProperty(assgProvider_no, "")%>
						</td>
					</tr>
					<tr>

						<td width="30%"><b><%if (OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %> Clinic Nbr <% } else { %> <bean:message key="billing.billingCorrection.formVisitType"/> <% } %></b></td>
						<td width="20%"><select name="xml_visittype">
						<% if (OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %>
					    <%
					    ClinicNbrDao cnDao = (ClinicNbrDao) SpringUtils.getBean("clinicNbrDao");
						ArrayList<ClinicNbr> nbrs = cnDao.findAll();
			            
			            String providerSearch = apptProvider_no.equalsIgnoreCase("none") ? user_no : apptProvider_no;
			            Provider p = providerDao.getProvider(providerSearch);
			            String providerNbr = SxmlMisc.getXmlContent(p.getComments(),"xml_p_nbr");
	                    for (ClinicNbr clinic : nbrs) {
							String valueString = String.format("%s | %s", clinic.getNbrValue(), clinic.getNbrString());
							%>
					    	<option value="<%=valueString%>" <%=providerNbr.startsWith(clinic.getNbrValue())?"selected":""%>><%=valueString%></option>
					    <%}%>
					    <% } else { %>
							<option value="00| Clinic Visit"
								<%=visitType.startsWith("00")?"selected":""%>><bean:message key="billing.billingCorrection.formClinicVisit"/>
							</option>
							<option value="01| Outpatient Visit"
								<%=visitType.startsWith("01")?"selected":""%>><bean:message key="billing.billingCorrection.formOutpatientVisit"/>
							</option>
							<option value="02| Hospital Visit"
								<%=visitType.startsWith("02")?"selected":""%>><bean:message key="billing.billingCorrection.formHospitalVisit"/>
							</option>
							<option value="03| ER"
								<%=visitType.startsWith("03")?"selected":""%>><bean:message key="billing.billingCorrection.formER"/></option>
							<option value="04| Nursing Home"
								<%=visitType.startsWith("04")?"selected":""%>><bean:message key="billing.billingCorrection.formNursingHome"/>
							</option>
							<option value="05| Home Visit"
								<%=visitType.startsWith("05")?"selected":""%>><bean:message key="billing.billingCorrection.formHomeVisit"/>
							</option>
							<% } %>
						</select></td>

						<td width="30%"><b>Billing Type</b></td>
						<td width="20%">
						<% String srtBillType = request.getParameter("xml_billtype")!=null? request.getParameter("xml_billtype"):""; %>
						<select name="xml_billtype">
							<option value="ODP | Bill OHIP"
								<%=srtBillType.startsWith("ODP")?"selected" : ""%>><bean:message key="billing.billingCorrection.formBillTypeO"/>
							</option>
							<option value="PAT | Bill Patient"
								<%=srtBillType.startsWith("PAT")?"selected" : ""%>><bean:message key="billing.billingCorrection.formBillTypeP"/>
							</option>
							<option value="WCB | Worker's Compensation Board"
								<%=srtBillType.startsWith("WCB")?"selected" : ""%>><bean:message key="billing.billingCorrection.formBillTypeW"/></option>
						</select></td>
					</tr>
					<tr>
						<td><b><bean:message key="billing.billingCorrection.msgVisitLocation"/></b></td>
						<td colspan="3"><select name="xml_location">
							<%
				for(int i=0; i<vecLocation.size(); i++) {
					propT = (Properties) vecLocation.get(i);
					String strLocation = request.getParameter("xml_location")!=null? request.getParameter("xml_location"):clinicview;
				%>
							<option
								value="<%=propT.getProperty("clinic_location_no") + "|" + propT.getProperty("clinic_location_name")%>"
								<%=strLocation.startsWith(propT.getProperty("clinic_location_no"))?"selected":""%>>
							<%=propT.getProperty("clinic_location_name")%></option>
							<%
				}
				%>
						</select></td>
					</tr>
						<%
				
					String providerNumForQuery = null;
                    if( apptProvider_no.equalsIgnoreCase("none") ) {
                    	providerNumForQuery = user_no;
                    }
                    else {
                    	providerNumForQuery = apptProvider_no;
                    }
				Provider pr = providerDao.getProvider(providerNumForQuery);
				if (pr != null) { 
					String prComments = pr.getComments();
				%>
					<tr>
						<td><b><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode"/></b></td>
				   	 	<td colspan="3">
						<select name="xml_slicode">

							<option value="<%=clinicNo%>"><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.NA" /></option>

							<%if (SxmlMisc.getXmlContent(prComments,"xml_p_sli").trim().equals("HDS")) {%>
								<option selected value="HDS "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HDS" /></option>
							<%} else { %>
								<option value="HDS "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HDS" /></option>
							<%}%>

							<%if (SxmlMisc.getXmlContent(prComments,"xml_p_sli").trim().equals("HED")) {%>
								<option selected value="HED "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HED" /></option>
							<%} else { %>
								<option value="HED "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HED" /></option>
							<%}%>

							<%if (SxmlMisc.getXmlContent(prComments,"xml_p_sli").trim().equals("HIP")) {%>
								<option selected value="HIP "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HIP" /></option>
							<%} else { %>
								<option value="HIP "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HIP" /></option>
							<%}%>

							<%if (SxmlMisc.getXmlContent(prComments,"xml_p_sli").trim().equals("HOP")) {%>
								<option selected value="HOP "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HOP" /></option>
							<%} else { %>
								<option value="HOP "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HOP" /></option>
							<%}%>

							<%if (SxmlMisc.getXmlContent(prComments,"xml_p_sli").trim().equals("HRP")) {%>
								<option selected value="HRP "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HRP" /></option>
							<%} else { %>
								<option value="HRP "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HRP" /></option>
							<%}%>

							<%if (SxmlMisc.getXmlContent(prComments,"xml_p_sli").trim().equals("IHF")) {%>
								<option selected value="IHF "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.IHF" /></option>
							<%} else { %>
								<option value="IHF "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.IHF" /></option>
							<%}%>

							<%if (SxmlMisc.getXmlContent(prComments,"xml_p_sli").trim().equals("OFF")) {%>
								<option selected value="OFF "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OFF" /></option>
							<%} else { %>
								<option value="OFF "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OFF" /></option>
							<%}%>

							<%if (SxmlMisc.getXmlContent(prComments,"xml_p_sli").trim().equals("OTN")) {%>
								<option selected value="OTN "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OTN" /></option>
							<%} else { %>
								<option value="OTN "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OTN" /></option>
							<%}%>

							<%if (SxmlMisc.getXmlContent(prComments,"xml_p_sli").trim().equals("PDF")) {%>
								<option selected value="PDF "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.PDF" /></option>
							<%} else { %>
								<option value="PDF "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.PDF" /></option>
							<%}%>
							</select>
				   		</td>
					</tr>
				<%} else {%>
				<tr>
				    <td><b><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode"/></b></td>
				    <td colspan="3">
					<select name="xml_slicode">
						<option value="<%=clinicNo%>"><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.NA" /></option>
						<option value="HDS "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HDS" /></option>
						<option value="HED "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HED" /></option>
						<option value="HIP "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HIP" /></option>
						<option value="HOP "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HOP" /></option>
						<option value="HRP "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HRP" /></option>
						<option value="IHF "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.IHF" /></option>
						<option value="OFF "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OFF" /></option>
						<option value="OTN "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OTN" /></option>
						<option value="OTN "><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.PDF" /></option>
					</select>
				    </td>
				</tr>
				<%} %>
					<tr>
						<td><b><bean:message key="billing.admissiondate"/></b></td>
						<td>
						<%
				String admDate = "";
				if(visitType.startsWith("02")||visitType.startsWith("04") ) {
					admDate = visitdate;
				} %> <!--input type="text" name="xml_vdate" id="xml_vdate" value="<%--=request.getParameter("xml_vdate")!=null? request.getParameter("xml_vdate"):visitdate--%>" size='10' maxlength='10' -->
						<input type="text" name="xml_vdate" id="xml_vdate"
							value="<%=request.getParameter("xml_vdate")!=null? request.getParameter("xml_vdate"):admDate%>"
							size='10' maxlength='10'> <img
							src="../../../images/cal.gif" id="xml_vdate_cal"></td>
						<td colspan="2"><a href="#"
							onClick="showHideLayers('Layer1','','show');return false;"><bean:message key="billing.billingform"/>
						</a>:</font></b> <%=currentFormName.length()<30 ? currentFormName : currentFormName.substring(0,30)%>
						</td>

					</tr>
				</table>


				</td>
			</tr>
		</table>

		</td>
	</tr>
	<tr>
		<td>


		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			height="137">
			<tr>
				<td valign="top" width="33%">

				<table width="100%" border="1" cellspacing="0" cellpadding="0"
					height="0" bordercolorlight="#99A005" bordercolordark="#FFFFFF">
					<tr bgcolor="#CCCCFF">
						<th width="10%" nowrap><font size="-1" color="#000000"><%=headerTitle1%>
						</font></th>
						<th width="70%" bgcolor="#CCCCFF"><font size="-1"
							color="#000000">Description</font></th>
						<th><font size="-1" color="#000000"> Fee</font></th>
					</tr>
					<%
			for(int i=0; i<vecCodeCol1.size(); i++) {
					propT = (Properties) vecCodeCol1.get(i);
					serviceCode = propT.getProperty("serviceCode");
					serviceDesc = propT.getProperty("serviceDesc");
					serviceDisp = propT.getProperty("serviceDisp");
					servicePercentage = propT.getProperty("servicePercentage");
					serviceSLI = propT.getProperty("serviceSLI");
					if(propPremium.getProperty(serviceCode)!=null) premiumFlag = "A";
					else premiumFlag = "";
			%>
					<tr bgcolor=<%=i%2==0?"#FFFFFF":"#EEEEFF"%>>
						<td nowrap><input type="checkbox"
							name="code_xml_<%=serviceCode%>" value="checked"
							<%="checked".equals(request.getParameter("code_xml_"+serviceCode))? "checked":""%>>
						<b><font size="-1"
							color="<%=premiumFlag.equals("A")? "#993333" : "black"%>"><span
							id="sc<%=(""+i).substring(0,1)+serviceCode%>"
							onDblClick="onDblClickServiceCode(this)"><%=serviceCode%></span></font></b>
						<input type="text" name="unit_xml_<%=serviceCode%>"
							value="<%=request.getParameter("unit_xml_"+serviceCode)!=null? request.getParameter("unit_xml_"+serviceCode):""%>"
							size="1" maxlength="2" style="width: 20px; height: 12px;"></td>
						<td <%=serviceDesc.length()>30?"title=\""+serviceDesc+"\"":""%>><font
							size="-1"><%=serviceDesc.length()>30?serviceDesc.substring(0,30)+"...":serviceDesc%>
						<input type="hidden" name="desc_xml_<%=serviceCode%>"
							value="<%=serviceDesc%>" />
						<input type="hidden" name="sli_xml_<%=serviceCode%>" value="<%=serviceSLI%>" />
							</font></td>
						<td align="right"><font size="-1"><%=serviceDisp%></font> <input
							type="hidden" name="price_xml_<%=serviceCode%>"
							value="<%=serviceDisp%>" /> <input type="hidden"
							name="perc_xml_<%=serviceCode%>" value="<%=servicePercentage%>" />
						</td>
					</tr>
					<% } %>
				</table>

				</td>
				<td width="33%" valign="top">

				<table width="100%" border="1" cellspacing="0" cellpadding="0"
					height="0" bordercolorlight="#99A005" bordercolordark="#FFFFFF">
					<tr bgcolor="#CCCCFF">
						<th width="10%" nowrap><font size="-1" color="#000000"><%=headerTitle2%>
						</font></th>
						<th width="70%" bgcolor="#CCCCFF"><font size="-1"
							color="#000000">Description</font></th>
						<th><font size="-1" color="#000000"> Fee</font></th>
					</tr>
					<%
			for(int i=0; i<vecCodeCol2.size(); i++) {
					propT = (Properties) vecCodeCol2.get(i);
					serviceCode = propT.getProperty("serviceCode");
					serviceDesc = propT.getProperty("serviceDesc");
					serviceDisp = propT.getProperty("serviceDisp");
					servicePercentage = propT.getProperty("servicePercentage");
					serviceSLI = propT.getProperty("serviceSLI");
					if(propPremium.getProperty(serviceCode)!=null) premiumFlag = "A";
					else premiumFlag = "";
			%>
					<tr bgcolor=<%=i%2==0?"#FFFFFF":"#EEEEFF"%>>
						<td nowrap><input type="checkbox"
							name="code_xml_<%=serviceCode%>" value="checked"
							<%="checked".equals(request.getParameter("code_xml_"+serviceCode))? "checked":""%> />
						<b><font size="-1"
							color="<%=premiumFlag.equals("A")? "#993333" : "black"%>"><span
							id="sc<%=(""+i).substring(0,1)+serviceCode%>"
							onDblClick="onDblClickServiceCode(this)"><%=serviceCode%></span></font></b>
						<input type="text" name="unit_xml_<%=serviceCode%>"
							value="<%=request.getParameter("unit_xml_"+serviceCode)!=null? request.getParameter("unit_xml_"+serviceCode):""%>"
							size="1" maxlength="2" style="width: 20px; height: 12px;" /></td>
						<td <%=serviceDesc.length()>30?"title=\""+serviceDesc+"\"":""%>><font
							size="-1"><%=serviceDesc.length()>30?serviceDesc.substring(0,30)+"...":serviceDesc%>
						<input type="hidden" name="desc_xml_<%=serviceCode%>"
							value="<%=serviceDesc%>" /> </font></td>
						<td align="right"><font size="-1"><%=serviceDisp%></font> <input
							type="hidden" name="price_xml_<%=serviceCode%>"
							value="<%=serviceDisp%>" /> <input type="hidden"
							name="perc_xml_<%=serviceCode%>" value="<%=servicePercentage%>" />
							<input type="hidden" name="sli_xml_<%=serviceCode%>" value="<%=serviceSLI%>" />
						</td>
					</tr>
					<% } %>
				</table>


				</td>
				<td width="33%" valign="top">

				<table width="100%" border="1" cellspacing="0" cellpadding="0"
					height="0" bordercolorlight="#99A005" bordercolordark="#FFFFFF">
					<tr bgcolor="#CCCCFF">
						<th width="10%" nowrap><font size="-1" color="#000000"><%=headerTitle3%>
						</font></th>
						<th width="70%" bgcolor="#CCCCFF"><font size="-1"
							color="#000000"><bean:message key="billing.service.desc"/></font></th>
						<th><font size="-1" color="#000000"> <bean:message key="billing.service.fee"/></font></th>
					</tr>
					<%
			for(int i=0; i<vecCodeCol3.size(); i++) {
					propT = (Properties) vecCodeCol3.get(i);
					serviceCode = propT.getProperty("serviceCode");
					serviceDesc = propT.getProperty("serviceDesc");
					serviceDisp = propT.getProperty("serviceDisp");
					servicePercentage = propT.getProperty("servicePercentage");
					serviceSLI = propT.getProperty("serviceSLI");
					if(propPremium.getProperty(serviceCode)!=null) premiumFlag = "A";
					else premiumFlag = "";
			%>
					<tr bgcolor=<%=i%2==0?"#FFFFFF":"#EEEEFF"%>>
						<td nowrap><input type="checkbox"
							name="code_xml_<%=serviceCode%>" value="checked"
							<%="checked".equals(request.getParameter("code_xml_"+serviceCode))? "checked":""%> />
						<b><font size="-1"
							color="<%=premiumFlag.equals("A")? "#993333" : "black"%>"><span
							id="sc<%=(""+i).substring(0,1)+serviceCode%>"
							onDblClick="onDblClickServiceCode(this)"><%=serviceCode%></span></font></b>
						<input type="text" name="unit_xml_<%=serviceCode%>"
							value="<%=request.getParameter("unit_xml_"+serviceCode)!=null? request.getParameter("unit_xml_"+serviceCode):""%>"
							size="1" maxlength="2" style="width: 20px; height: 12px;" /></td>
						<td <%=serviceDesc.length()>30?"title=\""+serviceDesc+"\"":""%>><font
							size="-1"><%=serviceDesc.length()>30?serviceDesc.substring(0,30)+"...":serviceDesc%>
						<input type="hidden" name="desc_xml_<%=serviceCode%>"
							value="<%=serviceDesc%>" /> </font></td>
						<td align="right"><font size="-1"><%=serviceDisp%></font> <input
							type="hidden" name="price_xml_<%=serviceCode%>"
							value="<%=serviceDisp%>" /> <input type="hidden"
							name="perc_xml_<%=serviceCode%>" value="<%=servicePercentage%>" />
							<input type="hidden" name="sli_xml_<%=serviceCode%>" value="<%=serviceSLI%>" />
						</td>
					</tr>
					<% } %>
				</table>


				</td>
			</tr>
		</table>



		</td>
	</tr>

	<input type="hidden" name="clinic_no" value="<%=clinicNo%>" />
	<input type="hidden" name="demographic_no" value="<%=demo_no%>" />
	<input type="hidden" name="appointment_no" value="<%=appt_no%>" />

	<input type="hidden" name="ohip_version" value="V03G" />
	<input type="hidden" name="hin" value="<%=demoHIN%>" />

	<input type="hidden" name="start_time"
		value="<%=request.getParameter("start_time")%>" />

	<input type="hidden" name="demographic_dob" value="<%=demoDOB%>" />

	<input type="hidden" name="apptProvider_no"
		value="<%=request.getParameter("apptProvider_no")%>" />
	<input type="hidden" name="asstProvider_no"
		value="<%=request.getParameter("asstProvider_no")%>" />

	<input type="hidden" name="demographic_name" value="<%=demoname%>" />
	<input type="hidden" name="providerview" value="<%=providerview%>" />
	<input type="hidden" name="appointment_date"
		value="<%=request.getParameter("appointment_date")%>" />
	<input type="hidden" name="assgProvider_no"
		value="<%=assgProvider_no%>" />
	<input type="hidden" name="billForm" value="<%=ctlBillForm%>" />

</table>
</form>


<br />
<%   if(!props.getProperty("isNewONbilling", "").equals("true")) {
%>
<table border="0" cellpadding="0" cellspacing="2" width="100%"
	bgcolor="#CCCCFF">
	<tr>
		<td colspan="6" class="RowTop"><%= demoname %> - <b><bean:message key="billing.hospitalBilling.frmBillHistory"/>
                </b> <bean:message key="billing.hospitalBilling.frmLastFive"/></td>
	</tr>
	<tr>
		<td>
		<table border="1" cellspacing="0" cellpadding="0"
			bordercolorlight="#99A005" bordercolordark="#FFFFFF" width="100%"
			bgcolor="#FFFFFF">
			<tr bgcolor="#99CCCC" align="center">
				<td nowrap><bean:message key="billing.hospitalBilling.frmSerial"/></td>
				<td nowrap><bean:message key="billing.billingCorrection.msgBillingDate"/></td>
				<td nowrap><bean:message key="billing.hospitalBilling.frmApptAdmDate"/></td>
				<td nowrap><bean:message key="billing.billingCorrection.formServiceCode"/></td>
				<td nowrap><bean:message key="billing.hospitalBilling.formDx"/></td>
				<td><bean:message key="billing.hospitalBilling.frmCreateDate"/></td>
			</tr>
			<%
          for (int i = 0; i < vecHist.size(); i++) {
            Properties prop  = (Properties)vecHist.get(i);
            Properties propD = (Properties)vecHistD.get(i);
%>
			<tr bgcolor="<%=i%2==0?"ivory":"#EEEEFF"%>" align="center">
				<td><%= prop.getProperty("billing_no", "&nbsp;") %></td>
				<td><%= prop.getProperty("billing_date", "&nbsp;") %></td>
				<td><%= prop.getProperty("visitdate", "&nbsp;") %></td>
				<td><%= propD.getProperty("service_code", "&nbsp;") %></td>
				<td><%= propD.getProperty("diagnostic_code", "&nbsp;") %></td>
				<td><%= prop.getProperty("update_date", "&nbsp;") %></td>
			</tr>
			<%
          }
%>
		</table>
		</td>
	</tr>
</table>
<% } else { %>
<table border="0" cellpadding="1" cellspacing="2" width="100%"
	class="myIvory">
	<tr class="myYellow">
		<td colspan="6"><%=demoname%> - <b><bean:message key="billing.hospitalBilling.frmBillHistory"/></b> <bean:message key="billing.hospitalBilling.frmLastFive"/>
		</td>
	</tr>
	<tr>
		<td>
		<table border="1" cellspacing="0" cellpadding="1"
			bordercolorlight="#99A005" bordercolordark="#FFFFFF" width="100%">
			<tr class="myYellow" align="center">
				<th><bean:message key="billing.hospitalBilling.frmSerial"/></th>
				<th><bean:message key="billing.billingCorrection.msgBillingDate"/></th>
				<th><bean:message key="billing.hospitalBilling.frmApptAdmDate"/></th>
				<th><bean:message key="billing.billingCorrection.formServiceCode"/></th>
				<th><bean:message key="billing.hospitalBilling.formDx"/></th>
				<th><bean:message key="billing.hospitalBilling.frmCreateDate"/></th>
			</tr>
			<%// new billing records
			for (int i = 0; i < aL.size(); i = i + 2) {
				BillingClaimHeader1Data obj = (BillingClaimHeader1Data) aL.get(i);
				BillingItemData iobj = (BillingItemData) aL.get(i + 1);

				%>
			<tr <%=i%4==0? "class=\"myGreen\"":""%> align="center">
				<td><%=obj.getId()%></td>
				<td><%=obj.getBilling_date()%></td>
				<td><%=iobj.getService_date()%></td>
				<td><%=iobj.getService_code()%></td>
				<td><%=iobj.getDx()%></td>
				<td><%=obj.getUpdate_datetime().substring(0, 10)%></td>
			</tr>
			<%}

		%>
		</table>
		</td>
	</tr>
</table>
<% } %>
<script type="text/javascript">//<![CDATA[
    // the default multiple dates selected, first time the calendar is instantiated
    var MA = [];
    setupServiceDates();
    
    function setupServiceDates() {
		var el = document.titlesearch.billDate.value;
		
		var dates = el.split('\n');
		
		MA.length = 0;
		for (var i in dates) {
			if (dates[i])
				MA[MA.length] = new Date(dates[i]);
		}
	}
    
    function closed(cal) {
      var el = document.titlesearch.billDate;
      // reset initial content.
      el.value = "";
      MA.length = 0;
      for (var i in cal.multiple) {
        var d = cal.multiple[i];
        
        if (d) {
          el.value += d.print("%Y-%m-%d") + "\n";
          MA[MA.length] = d;
        }
      }
      cal.hide();
      
      return true;
    };

    Calendar.setup({
      align      : "BR",
      showOthers : true,
      multiple   : MA, // pass the initial or computed array of multiple dates to be initially selected
      onClose    : closed,
      button     : "trigger",
      inputField : "billDate"
    });
  //]]>
Calendar.setup( { inputField : "xml_vdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 } );

  </script>
<%!
String getDefaultValue(String paraName, Vector vec, String propName) {
  String ret = "";
  if(paraName!=null && !"".equals(paraName)) {
    ret = paraName;
  } else if(vec!=null && vec.size()>0 && vec.get(0)!=null) {
    ret = ((Properties)vec.get(0)).getProperty(propName, "") ;
  }
  return ret;
}
%>
</body>
</html>
