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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.billing.CA.ON.model.BillingPercLimit"%>
<%@page import="org.oscarehr.billing.CA.ON.dao.BillingPercLimitDao"%>
<%@page import="org.oscarehr.common.model.BillingService"%>
<%@page import="org.oscarehr.common.dao.BillingServiceDao"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../../../logout.jsp");
  }

  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);


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
<%@ page errorPage="errorpage.jsp" import="java.util.*,java.math.*,java.net.*,java.sql.*, oscar.util.*, oscar.*"%>

<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Billing" %>
<%@ page import="org.oscarehr.common.dao.BillingDao" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao" %>
<%
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
%>

<%
  if(request.getParameter("submit")!=null && "Back to Edit".equals(request.getParameter("button")  ) ) {
%>
<jsp:forward page="billingShortcutPg1.jsp" />
<%}%>
<%
  String            clinicview        = oscarVariables.getProperty("clinic_view", "");
  String            clinicNo          = oscarVariables.getProperty("clinic_no", "");
  String            visitType         = oscarVariables.getProperty("visit_type", "");
  String            appt_no           = request.getParameter("appointment_no");
  String            demoname          = request.getParameter("demographic_name");
  String            demo_no           = request.getParameter("demographic_no");
  String            apptProvider_no   = request.getParameter("apptProvider_no");
  String ctlBillForm = request.getParameter("billForm");
  String            assgProvider_no   = request.getParameter("assgProvider_no");
  if (assgProvider_no==null) assgProvider_no = new String();
  
  String            demoSex           = request.getParameter("DemoSex");
  GregorianCalendar now               = new GregorianCalendar();
  int               curYear           = now.get(Calendar.YEAR);
  int               curMonth          = (now.get(Calendar.MONTH) + 1);
  int               curDay            = now.get(Calendar.DAY_OF_MONTH);
  int               dob_year          = 0, dob_month = 0, dob_date = 0, age = 0;
  String content = "";
  String total = "";

  
  String            msg               = "<tr><td colspan='2'>Calculation</td></tr>";
  String            action            = "edit";
  Properties        propHist          = null;
  Vector            vecHist           = new Vector();
  // get provider's detail
  String proOHIPNO="", proRMA="";
  
  ProviderDao prDao = SpringUtils.getBean(ProviderDao.class);
  Provider pr = prDao.getProvider(request.getParameter("xml_provider"));
  if (pr != null) {
	proOHIPNO = pr.getOhipNo();
	proRMA = pr.getRmaNo();
  }
  
  if(request.getParameter("xml_provider")!=null) {
	  providerview = request.getParameter("xml_provider");
  }
  
  // get patient's detail
  String errorFlag = "";
  String warningMsg = "", errorMsg = "";
  String r_doctor="", r_doctor_ohip="" ;
  String demoFirst="", demoLast="", demoHIN="", demoDOB="", demoDOBYY="", demoDOBMM="", demoDOBDD="", demoHCTYPE="";
  
  DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
  Demographic demo = demoDao.getDemographic(demo_no);
  if (demo != null) {
    assgProvider_no = demo.getProviderNo();
    if (assgProvider_no==null) assgProvider_no = new String();
    
	demoFirst = demo.getFirstName();
	demoLast = demo.getLastName();
	demoSex = demo.getSex();
	if (demo.getHin()!=null && demo.getVer()!=null) demoHIN = demo.getHin() + demo.getVer();
	if (demoSex.compareTo("M")==0) demoSex ="1";
	if (demoSex.compareTo("F")==0) demoSex ="2";

	demoHCTYPE = demo.getHcType()==null?"":demo.getHcType();
	if (demoHCTYPE.compareTo("") == 0 || demoHCTYPE == null || demoHCTYPE.length() <2) {
		demoHCTYPE="ON";
	}else{
		demoHCTYPE=demoHCTYPE.substring(0,2).toUpperCase();
	}
	demoDOBYY = demo.getYearOfBirth();
	demoDOBMM = demo.getMonthOfBirth();
	demoDOBDD = demo.getDateOfBirth();

	if (demo.getFamilyDoctor() == null) {
		r_doctor = "N/A"; r_doctor_ohip="000000";
	}else{
		r_doctor=SxmlMisc.getXmlContent(demo.getFamilyDoctor(),"rd")==null ? "" : SxmlMisc.getXmlContent(demo.getFamilyDoctor(), "rd");
		r_doctor_ohip=SxmlMisc.getXmlContent(demo.getFamilyDoctor(),"rdohip")==null ? "" : SxmlMisc.getXmlContent(demo.getFamilyDoctor(), "rdohip");
	}

	demoDOBMM = demoDOBMM.length() == 1 ? ("0" + demoDOBMM) : demoDOBMM;
	demoDOBDD = demoDOBDD.length() == 1 ? ("0" + demoDOBDD) : demoDOBDD;
	demoDOB = demoDOBYY + demoDOBMM + demoDOBDD;

	if (demo.getHin()==null || demo.getHin().equals("")) {
		warningMsg += "<br><font color='orange'>Warning: The patient does not have a valid HIN. </font><br>";
	}
	if (r_doctor_ohip != null && r_doctor_ohip.length()>0 && r_doctor_ohip.length() != 6) {
		warningMsg += "<br><font color='orange'>Warning: the referral doctor's no is wrong. </font><br>";
	}
	if (demoDOB.length() != 8) {
		errorFlag = "1";
		errorMsg = errorMsg + "<br><font color='red'>Error: The patient does not have a valid DOB. </font><br>";
	}
  }

  // save the billing if needed
  if(request.getParameter("submit")!=null && ("Next".equals(request.getParameter("submit"))
          || "Save".equals(request.getParameter("submit")) || "Save and Back".equals(request.getParameter("submit"))  ) ) {
    // parse billing date
    int NUMTYPEINFIELD = 5;
	// get billing detail info
	// get form service code
	Vector vecServiceCode = new Vector();
	Vector vecServiceCodeDesc = new Vector();
	Vector vecServiceCodeUnit = new Vector();
	Vector vecServiceCodePrice = new Vector();
	Vector vecServiceCodePerc = new Vector();
	String rulePerc = "allAboveCode"; // onlyAboveCode;
	rulePerc = request.getParameter("rulePerc")!=null?request.getParameter("rulePerc"):"allAboveCode";
	int rulePercLabelNum = -1; //only use when onlyAboveCode

	int recordCount = 0;
	String[] billrec = new String[]{"","","","",""};
	String[] billrecunit = new String[]{"","","","",""};
	String[] billrecdesc = new String[]{"","","","",""};
	String[] pricerec = new String[]{"","","","",""};
	String[] percrec = new String[]{"","","","",""};
	for(int i=0; i<NUMTYPEINFIELD; i++) {
		if("".equals(request.getParameter("serviceDate"+i))) break;
		else {
			billrec[i] = request.getParameter("serviceDate"+i);
			billrecunit[i] = request.getParameter("serviceUnit"+i);
			if("".equals(billrecunit[i])) billrecunit[i]="1";
			recordCount++;
		}
	}

    String billingDate = request.getParameter("billDate");
	String [] tempDate = billingDate.split("\\s");

    for( int idx = 0; idx < tempDate.length; ++idx ) {
    }
	for(int i=0; i<recordCount; i++) {
		BillingServiceDao bsDao = SpringUtils.getBean(BillingServiceDao.class);
		
		for(BillingService bs : bsDao.findByServiceCodeAndLatestDate(billrec[i], ConversionUtils.fromDateString(request.getParameter("billDate")))) {
			billrecdesc[i] = bs.getDescription();
			pricerec[i] = bs.getValue() == null ? "" : bs.getValue();
			percrec[i] = bs.getPercentage();

			if( (!"".equals(pricerec[i]) && Double.parseDouble(pricerec[i])>0.) || ( "".equals(percrec[i])) ) {
				vecServiceCode.add( billrec[i] );
				vecServiceCodeDesc.add( billrecdesc[i] );
				vecServiceCodePrice.add( pricerec[i] );
				vecServiceCodeUnit.add( billrecunit[i] );
			} else {
				if(!"allAboveCode".equals(rulePerc) && rulePercLabelNum == -1 ) {
                                    rulePercLabelNum = i-1 == -1 ? 0 : i-1;
                                }
				vecServiceCodePerc.add( billrec[i] ); // code
				vecServiceCodePerc.add( percrec[i] ); // price
				vecServiceCodePerc.add( billrecunit[i] ); // unit
				vecServiceCodePerc.add( billrecdesc[i] ); // desc
			}

		}
	}
	//get the form service code
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		String temp=e.nextElement().toString();
		if( temp.indexOf("code_xml_")==-1 ) continue;

		temp = temp.substring("code_".length());
		String desc = request.getParameter("desc_" + temp);
		String fee = request.getParameter("price_" + temp);
		String perc = request.getParameter("perc_" + temp);
		String tempUnit = request.getParameter("unit_" + temp);
		tempUnit = (tempUnit==null||"".equals(tempUnit) )? "1":tempUnit;
		String code = temp.substring("xml_".length()).toUpperCase();
		if( (!"".equals(fee) && Double.parseDouble(fee)>0.) || ( "".equals(perc)) ) {
			vecServiceCodePrice.add( fee );
			vecServiceCodeUnit.add( tempUnit );
			vecServiceCode.add( code );
			vecServiceCodeDesc.add( desc );
		} else {
			vecServiceCodePerc.add( code );
			vecServiceCodePerc.add( perc );
			vecServiceCodePerc.add( tempUnit ); // unit
			vecServiceCodePerc.add( desc ); // desc
		}
		//vecServiceCode.add( code );
	}
	//check perc code limitation
	boolean bLimit = false;
	String minFee = "", maxFee = "";
        int size = vecServiceCodePerc.size()/4;
        String[] aMinFee = new String[size];
        String[] aMaxFee = new String[size];
        Boolean[] aLimits = new Boolean[size];
        for( int idx1 = 0; idx1 < aLimits.length; ++idx1 ) {
            aLimits[idx1] = false;
        }

    int codeIdx = 0;
	for(int idx2 = 0; idx2 < size; ++idx2) {
		//TODO: only one perc code allowed, otherwise error msg
		BillingPercLimitDao bplDao = SpringUtils.getBean(BillingPercLimitDao.class);
		for(BillingPercLimit bpl : bplDao.findByServiceCode("" + vecServiceCodePerc.get(codeIdx))) {
			aLimits[idx2] = true;
			aMinFee[idx2] = bpl.getMin();
			aMaxFee[idx2] = bpl.getMax();
		}
        codeIdx += 4;
	}

    // calculate total
    BigDecimal bdTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal bdPercBase = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
	BigDecimal bdPerc = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
    // calculate base
	for(int i=0; i<vecServiceCodePrice.size(); i++) {
    	BigDecimal price = new BigDecimal(Double.parseDouble((String)vecServiceCodePrice.get(i))).setScale(2, BigDecimal.ROUND_HALF_UP);
    	BigDecimal unit = new BigDecimal(Double.parseDouble((String)vecServiceCodeUnit.get(i))).setScale(2, BigDecimal.ROUND_HALF_UP);
    	bdTotal = bdTotal.add(price.multiply(unit).setScale(2, BigDecimal.ROUND_HALF_UP));
    	if(i==rulePercLabelNum) {
            bdPercBase = bdTotal;
        }
		msg += "<tr bgcolor='#EEEEFF'><td align='right' width='20%'>" + vecServiceCode.get(i) + " ("+Math.round(unit.floatValue())+")</td><td align='right'>" + (i==0?"":" + ") + price + " x " + unit + " = " + bdTotal + "</td></tr>";
	}

		// calculate perc base
		if("allAboveCode".equals(rulePerc) ) {
			bdPercBase = bdTotal;
		}
        codeIdx = 1;
        BigDecimal[] bdPercs = new BigDecimal[size];
        for( int idx3 = 0; idx3 < size; ++idx3) {
		// calculate perc
		BigDecimal perc = new BigDecimal(Double.parseDouble((String)vecServiceCodePerc.get(codeIdx))).setScale(2, BigDecimal.ROUND_HALF_UP);
		bdPerc = bdPercBase.multiply(perc).setScale(2, BigDecimal.ROUND_HALF_UP);
		msg += "<tr bgcolor='#EEEEFF'><td align='right'>"+vecServiceCodePerc.get(codeIdx-1)+" (1)</td><td align='right'>Percentage : " + bdPercBase + " x " + perc + " = " + bdPerc + "</td></tr>";
		// adjust perc by min/max
		if(aLimits[idx3]) {
			bdPerc = bdPerc.min(new BigDecimal(Double.parseDouble(aMaxFee[idx3]) ).setScale(2, BigDecimal.ROUND_HALF_UP) );
			bdPerc = bdPerc.max(new BigDecimal(Double.parseDouble(aMinFee[idx3]) ).setScale(2, BigDecimal.ROUND_HALF_UP) );
		msg += "<tr bgcolor='ivory'><td align='right' colspan='2'>Adjust to (" + aMinFee[idx3] + ", " + aMaxFee[idx3] + "): </td><td align='right'>" + bdPerc + "</td></tr>";
		}
    	bdTotal = bdTotal.add(bdPerc);
        bdPercs[idx3] = bdPerc;
        codeIdx += 4;
	}

    total = "" + bdTotal;
		msg += "<tr><td align='right' colspan='2'>Total: " + bdTotal + "</td></tr>";
	// referral
    content = "";
    String referalCode = (request.getParameter("referralCode")!=null&&request.getParameter("referralCode").length()==6)?request.getParameter("referralCode"):null;
    if(referalCode!=null) {
    	content += "<xml_referral>checked</xml_referral>" ;
    	content += "<rdohip>" + referalCode + "</rdohip>" ;
    }
    content += "<hctype>" + demoHCTYPE + "</hctype>" ;
    content += "<demosex>" + demoSex + "</demosex>" ;

    if(request.getParameter("addition")!=null && "Confirm".equals(request.getParameter("addition").trim())) {
            Boolean bServicePerc = false;
	    for(int k=0; k<tempDate.length; k++) {
	    	if(tempDate[k].trim().length()!=10) continue;
			String[] param =new String[23];
			param[0]=request.getParameter("clinic_no");
			param[1]=request.getParameter("demographic_no");
			param[2]=request.getParameter("xml_provider"); //request.getParameter("provider_no");
			param[3]=request.getParameter("appointment_no");

			param[4]=request.getParameter("ohip_version");
			param[5]=request.getParameter("demographic_name");
			param[6]=request.getParameter("hin");
			param[7] = UtilDateUtilities.DateToString(new java.util.Date(), "yyyy-MM-dd");
			param[8] = UtilDateUtilities.DateToString(new java.util.Date(), "HH:mm:ss");
			//param[7]=request.getParameter("billing_date");
			//param[8]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("billing_time"));
			param[9]= tempDate[k] ; // parse(billingDate) ;//request.getParameter("appointment_date");
			param[10]=request.getParameter("start_time");
			param[11]=request.getParameter("xml_location").substring(0,request.getParameter("xml_location").indexOf("|")).trim(); //request.getParameter("clinic_ref_code");
			param[12]=content; //request.getParameter("content");
			param[13]=total; // calculate total; //request.getParameter("total");
			param[14]=request.getParameter("xml_billtype").substring(0,1);
			param[15]=request.getParameter("demographic_dob");

			param[16]=request.getParameter("xml_vdate"); // don't consider the appt date. request.getParameter("visitdate");
			param[17]=request.getParameter("xml_visittype").substring(0,2); //request.getParameter("visittype");
			param[18]=proOHIPNO; //request.getParameter("pohip_no");
			param[19]=proRMA; //request.getParameter("prma_no");
			param[20]=request.getParameter("apptProvider_no"); //"none"; //
			param[21]=""; //request.getParameter("asstProvider_no"); //"";//
			param[22] = user_no;//request.getParameter("user_no");
			int nBillNo = 0;
			int nBillDetailNo = 0;

			// for new billing
			OscarProperties props = OscarProperties.getInstance();
			if(props.getProperty("isNewONbilling", "").equals("true")) {
				BillingSavePrep saveObj = new BillingSavePrep();
				// combine two vecs into one
				if(!bServicePerc && vecServiceCodePerc.size()>1) {
                                        bServicePerc = true;
                                        codeIdx = 0;
                                        for( int idx4 = 0; idx4 < size; ++idx4 ) {
                                            vecServiceCodePrice.add( "" + bdPercs[idx4]); //vecServiceCodePerc.get(1) );
                                            vecServiceCodeUnit.add( vecServiceCodePerc.get(codeIdx+2) );
                                            vecServiceCode.add( vecServiceCodePerc.get(codeIdx) );
                                            vecServiceCodeDesc.add( vecServiceCodePerc.get(codeIdx+3) );
                                            codeIdx += 4;
                                        }
				}
				Vector vecT = saveObj.getBillingClaimHospObj(request, tempDate[k], total, vecServiceCode, vecServiceCodeUnit, vecServiceCodePrice);
				saveObj.addABillingRecord(loggedInInfo, vecT);
			} else {
				Billing b = new Billing();
				b.setClinicNo(Integer.parseInt(param[0]));
				b.setDemographicNo(Integer.parseInt(param[1]));
				b.setProviderNo(param[2]);
				b.setAppointmentNo(Integer.parseInt(param[3]));
				b.setOrganizationSpecCode(param[4]);
				b.setDemographicName(param[5]);
				b.setHin(param[6]);
				b.setUpdateDate(new java.util.Date());
				b.setUpdateTime(new java.util.Date());
				b.setBillingDate(MyDateFormat.getSysDate(param[9]));
				b.setBillingTime(MyDateFormat.getSysTime(param[10]));
				b.setClinicRefCode(param[11]);
				b.setContent(param[12]);
				b.setTotal(param[13]);
				b.setStatus(param[14]);
				b.setDob(param[15]);
				b.setVisitDate(MyDateFormat.getSysDate(param[16]));
				b.setVisitType(param[17]);
				b.setProviderOhipNo(param[18]);
				b.setProviderRmaNo(param[19]);
				b.setApptProviderNo(param[20]);
				b.setAsstProviderNo(param[21]);
				b.setCreator(param[22]);
				billingDao.persist(b);

			nBillNo = b.getId();

			//int recordCount = Integer.parseInt(request.getParameter("record"));
			// combine two vecs into one
			if(vecServiceCodePerc.size()>1) {
				vecServiceCodePrice.add( "" + bdPerc); //vecServiceCodePerc.get(1) );
				vecServiceCodeUnit.add( vecServiceCodePerc.get(2) );
				vecServiceCode.add( vecServiceCodePerc.get(0) );
				vecServiceCodeDesc.add( vecServiceCodePerc.get(3) );
			}

			for (int i=0; i<vecServiceCode.size(); i++){ //recordCount
				BigDecimal bdEachPrice = new BigDecimal(Double.parseDouble((String)vecServiceCodePrice.get(i))).setScale(2, BigDecimal.ROUND_HALF_UP);
		    	BigDecimal bdEachUnit = new BigDecimal(Double.parseDouble((String)vecServiceCodeUnit.get(i))).setScale(2, BigDecimal.ROUND_HALF_UP);
		    	BigDecimal bdEachTotal = bdEachPrice.multiply(bdEachUnit).setScale(2, BigDecimal.ROUND_HALF_UP);

		    	BillingDetail bd = new BillingDetail();
		    	bd.setBillingNo(nBillNo);
				bd.setServiceCode((String)vecServiceCode.get(i));
				bd.setServiceDesc((String)vecServiceCodeDesc.get(i));
				bd.setBillingAmount((""+bdEachTotal).replaceAll("\\.", ""));
				bd.setDiagnosticCode(request.getParameter("dxCode"));
				bd.setAppointmentDate(MyDateFormat.getSysDate(tempDate[k]));
				bd.setStatus(request.getParameter("xml_billtype").substring(0,1));
				bd.setBillingUnit((String)vecServiceCodeUnit.get(i));
				billingDetailDao.persist(bd);

				nBillDetailNo = bd.getId();

		    	if (nBillDetailNo == 0) {
		    		// roll back
		    		Billing bb = billingDao.find(nBillNo);
		    		if(bb != null) {
		    			bb.setStatus("D");
		    			billingDao.merge(bb);
		    		}
		    		
		    		break;
		    	}
			}

			}
		} // end of for loop
		msg = "<br>Billing records were added.<br>";
		if("Save".equals(request.getParameter("submit"))) {
			msg += "<script language=\"JavaScript\"> self.close();</script>";
		} else {
		    msg += "<script language=\"JavaScript\">window.location = 'billingShortcutPg1.jsp?billRegion=&billForm="
		        + URLEncoder.encode(oscarVariables.getProperty("hospital_view", oscarVariables.getProperty("default_view"))) + "&hotclick=&appointment_no=0&demographic_name="
		        + URLEncoder.encode(demoLast) + "%2C"
		        + URLEncoder.encode(demoFirst) + "&demographic_no="
		        + demo_no + "&providerview=1&user_no="
		        + user_no + "&apptProvider_no=none&appointment_date="
		        + curYear +"-"+curMonth+"-"+curDay + "&start_time=0:00:00&bNewForm=1&status=t'</script>";
		}
	}
  }


  // create msg
  String wrongMsg = errorMsg + warningMsg;
  //msg += errorMsg + warningMsg;
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OscarBilling</title>
<script language="JavaScript">
	<!--
	    function onSave() {
	        var submitTypeString = document.forms[0].submitType.value;
	        var ret = true;
	        if(ret==true)
	        {
	            ret = confirm("Are you sure you want to " + submitTypeString + "?");
	        }
	        return ret;
	    }
	//-->
	</script>
</head>

<body topmargin="0">

<table border="0" cellpadding="0" cellspacing="2" width="100%"
	bgcolor="#CCCCFF">
	<form method="post" name="titlesearch" action="billingShortcutPg2.jsp" onsubmit="return onSave();">
	<input type="hidden" value="" name="submitType" />
	<tr>
		<td>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td><b>Confirmation </b></td>
				<td align="right"><input type="hidden" name="addition"
					value="Confirm" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr bgcolor="#33CCCC">
				<td nowrap bgcolor="#FFCC99" width="10%" align="center"><%= demoname %>
				<%= demoSex.equals("1")?"Male":"Female" %> <%= " DOB: " + demoDOBYY + "/" + demoDOBMM + "/" + demoDOBDD + " HIN: " + demoHIN %>
				</td>
				<td bgcolor="#99CCCC" align="center"><%= wrongMsg %></td>
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
						<td nowrap width="30%" align="center" valign="top"><b>Service
						Date</b><br>
						<%=request.getParameter("billDate").replaceAll("\\n", "<br>")%></td>
						<td align="center" width="33%"><b>Diagnostic Code</b><br>
						<%=request.getParameter("dxCode")%>
						<hr>
						<b>Cal.% mode</b><br>
						<%=request.getParameter("rulePerc")%></td>
						<td valign="top"><b>Refer. Doctor</b><br><%=request.getParameter("referralDocName")%><br>
						<b>Refer. Doctor #</b><br><%=request.getParameter("referralCode")%>
						</td>
					</tr>
				</table>

				</td>
				<td valign="top">

				<table border="1" cellspacing="2" cellpadding="0" width="100%"
					bordercolorlight="#99A005" bordercolordark="#FFFFFF"
					bgcolor="#EEEEFF">
					<tr>
						<td nowrap width="30%"><b>Billing Physician</b></td>
						<td width="20%"><%=providerBean.getProperty(request.getParameter("xml_provider"), "")%></td>
						<td nowrap width="30%"><b>Assig. Physician</b></td>
						<td width="20%"><%=providerBean.getProperty(assgProvider_no, "")%>
						</td>
					</tr>
					<tr>

						<td width="30%"><b>Visit Type</b></td>
						<td width="20%"><%=request.getParameter("xml_visittype").substring(request.getParameter("xml_visittype").indexOf("|")+1)%>
						</td>

						<td width="30%"><b>Billing Type</b></td>
						<td width="20%"><%=request.getParameter("xml_billtype").substring(request.getParameter("xml_billtype").indexOf("|")+1)%>
						</td>
					</tr>
					<tr>
						<td><b>Visit Location</b></td>
						<td colspan="3"><%=request.getParameter("xml_location").substring(request.getParameter("xml_location").indexOf("|")+1)%></td>
					</tr>
					<tr>
					<td><b>SLI Code</b></td>
						<td><%String testSliCode = request.getParameter("xml_slicode").substring(
							request.getParameter("xml_slicode").indexOf("|") + 1);
							if (testSliCode.startsWith(oscarVariables.getProperty("clinic_no", "").trim())) {%>
								Not Applicable &nbsp;
							<%} else {%>
								<%=testSliCode%> &nbsp;
							<%}%>
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
		<table border="1" width="50%" bordercolorlight="#99A005"
			bordercolordark="#FFFFFF">

			<%= msg %>

			<tr>

				<td colspan='2' align='center' bgcolor="silver">
				<input type="submit" name="button" value="Back to Edit"	onclick="document.forms[0].submitType.value='Back to Edit'"  style="width: 120px;" /> 
				<input type="submit" name="button" value="Save" onclick="document.forms[0].submitType.value='Save'"style="width: 120px;" /> 
				<input type="submit" name="button" value="Save and Back" onclick="document.forms[0].submitType.value='Save and Back'"style="width: 120px;" /></td>
			</tr>
		</table>
		</td>
	</tr>


	<%
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		String temp=e.nextElement().toString();
%>
	<input type="hidden" name="<%= temp %>"
		value="<%=StringEscapeUtils.escapeHtml(request.getParameter(temp))%>">
	<%
}
%>
	<input type="hidden" name="hc_type" value="<%=demoHCTYPE%>">
	<input type="hidden" name="referralCode" value="<%=r_doctor_ohip%>">
	<input type="hidden" name="sex" value="<%=demoSex%>">
	<input type="hidden" name="proOHIPNO" value="<%=proOHIPNO%>">
	</form>

</table>


</body>
</html>
