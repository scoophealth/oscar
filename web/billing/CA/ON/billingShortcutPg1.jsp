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
  <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
  <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
  <%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
  <%@ page errorPage="errorpage.jsp"import="java.util.*,java.math.*,java.net.*,
                                            java.sql.*, oscar.util.*, oscar.*" %>
  <%@ page import="oscar.oscarBilling.ca.on.data.BillingONDataHelp" %>
  <%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
  <jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
  <jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
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
  //String            dob               = request.getParameter("dob");
  String            demoSex           = request.getParameter("DemoSex");
  GregorianCalendar now               = new GregorianCalendar();
  int               curYear           = now.get(Calendar.YEAR);
  int               curMonth          = (now.get(Calendar.MONTH) + 1);
  int               curDay            = now.get(Calendar.DAY_OF_MONTH);
  int               dob_year          = 0, dob_month = 0, dob_date = 0, age = 0;

  BillingONDataHelp dbObj             = new BillingONDataHelp();
  String            msg               = "You can select multiple dates from the calendar to bill. The default unit value is 1.";
  String            action            = "edit";
  Properties        propHist          = null;
  Vector            vecHist           = new Vector();
  // get provider's detail
  String proOHIPNO="", proRMA="";
  String sql = "select * from provider where provider_no='" + request.getParameter("xml_provider") + "'";
  ResultSet rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
	proOHIPNO = rs.getString("ohip_no");
	proRMA = rs.getString("rma_no");
  }
  if(request.getParameter("xml_provider")!=null) providerview = request.getParameter("xml_provider");
  // get patient's detail
  String errorFlag = "";
  String warningMsg = "", errorMsg = "";
  String r_doctor="", r_doctor_ohip="" ;
  String demoFirst="", demoLast="", demoHIN="", demoDOB="", demoDOBYY="", demoDOBMM="", demoDOBDD="", demoHCTYPE="";
  sql = "select * from demographic where demographic_no=" + demo_no;
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
    assgProvider_no = rs.getString("provider_no");
	demoFirst = rs.getString("first_name");
	demoLast = rs.getString("last_name");
	demoHIN = rs.getString("hin") + rs.getString("ver");
	demoSex = rs.getString("sex");
	if (demoSex.compareTo("M")==0) demoSex ="1";
	if (demoSex.compareTo("F")==0) demoSex ="2";

	demoHCTYPE = rs.getString("hc_type")==null?"":rs.getString("hc_type");
	if (demoHCTYPE.compareTo("") == 0 || demoHCTYPE == null || demoHCTYPE.length() <2) {
		demoHCTYPE="ON";
	}else{
		demoHCTYPE=demoHCTYPE.substring(0,2).toUpperCase();
	}
	demoDOBYY = rs.getString("year_of_birth");
	demoDOBMM = rs.getString("month_of_birth");
	demoDOBDD = rs.getString("date_of_birth");

	if (rs.getString("family_doctor") == null){
		r_doctor = "N/A"; r_doctor_ohip="000000";
	}else{
		r_doctor=SxmlMisc.getXmlContent(rs.getString("family_doctor"),"rd")==null ? "" : SxmlMisc.getXmlContent(rs.getString("family_doctor"), "rd");
		r_doctor_ohip=SxmlMisc.getXmlContent(rs.getString("family_doctor"),"rdohip")==null ? "" : SxmlMisc.getXmlContent(rs.getString("family_doctor"), "rdohip");
	}

	demoDOBMM = demoDOBMM.length() == 1 ? ("0" + demoDOBMM) : demoDOBMM;
	demoDOBDD = demoDOBDD.length() == 1 ? ("0" + demoDOBDD) : demoDOBDD;
	demoDOB = demoDOBYY + demoDOBMM + demoDOBDD;

	if (rs.getString("hin") == null ) {
		errorFlag = "1";
		errorMsg = errorMsg + "<br><font color='red'>Error: The patient does not have a valid HIN. </font><br>";
	} else if (rs.getString("hin").equals("")) {
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
  if(request.getParameter("submit")!=null && "Save".equals(request.getParameter("submit"))) {
    // parse billing date
    int NUMTYPEINFIELD = 5;
    String billingDate = request.getParameter("billDate");
    String [] tempDate = billingDate.split("\\s");
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
	for(int i=0; i<recordCount; i++) {
		sql = "select service_code, description, value, percentage from billingservice where service_code='"
			+ billrec[i] + "'";
		rs = dbObj.searchDBRecord(sql);
		while (rs.next()) {
			billrecdesc[i] = rs.getString("description");
			//otherdbcode2 = rs.getString("service_code");
			pricerec[i] = rs.getString("value")==null?"":rs.getString("value");
			percrec[i] = rs.getString("percentage");
			//otherperc2 = rs.getString("percentage");

			if( (!"".equals(pricerec[i]) && Double.parseDouble(pricerec[i])>0.) || ( "".equals(percrec[i])) ) {
				vecServiceCode.add( billrec[i] );
				vecServiceCodeDesc.add( billrecdesc[i] );
				vecServiceCodePrice.add( pricerec[i] );
				vecServiceCodeUnit.add( billrecunit[i] );
			} else {
				if(!"allAboveCode".equals(rulePerc) ) rulePercLabelNum = i-1;
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
	if(vecServiceCodePerc.size()>0) {
		//TODO: only one perc code allowed, otherwise error msg
		sql = "select min, max from billingperclimit where service_code='"
			+ vecServiceCodePerc.get(0) + "'";
		rs = dbObj.searchDBRecord(sql);
		while (rs.next()) {
			bLimit = true;
			minFee = rs.getString("min");
			maxFee = rs.getString("max");
		}
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
    	if(i==rulePercLabelNum) bdPercBase = bdTotal;
System.out.println(i + " :" + price + " x " + unit + " = " + bdTotal);
	}
	if(vecServiceCodePerc.size()>1) {
		// calculate perc base
		if("allAboveCode".equals(rulePerc) ) {
			bdPercBase = bdTotal;
		}
		// calculate perc
		BigDecimal perc = new BigDecimal(Double.parseDouble((String)vecServiceCodePerc.get(1))).setScale(2, BigDecimal.ROUND_HALF_UP);
		bdPerc = bdPercBase.multiply(perc).setScale(2, BigDecimal.ROUND_HALF_UP);
System.out.println("Percentage :" + bdPercBase + " x " + perc + " = " + bdPerc);
		// adjust perc by min/max
		if(bLimit) {
			bdPerc = bdPerc.min(new BigDecimal(Double.parseDouble(maxFee) ).setScale(2, BigDecimal.ROUND_HALF_UP) );
			bdPerc = bdPerc.max(new BigDecimal(Double.parseDouble(minFee) ).setScale(2, BigDecimal.ROUND_HALF_UP) );
System.out.println("Adjust to (" + minFee + ", " + maxFee + "): " + bdPerc);
		}
    	bdTotal = bdTotal.add(bdPerc);
	}

    String total = "" + bdTotal;
    System.out.println(billingDate + "***************total****************:" + bdTotal);
	// referral
    String content = "";
    String referalCode = (request.getParameter("referralCode")!=null&&request.getParameter("referralCode").length()==6)?request.getParameter("referralCode"):null;
    if(referalCode!=null) {
    	content += "<xml_referral>checked</xml_referral>" ;
    	content += "<rdohip>" + referalCode + "</rdohip>" ;
    }
    content += "<hctype>" + demoHCTYPE + "</hctype>" ;
    content += "<demosex>" + demoSex + "</demosex>" ;

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
		param[7] = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd");
		param[8] = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
		//param[7]=request.getParameter("billing_date");
		//param[8]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("billing_time"));
		param[9]= tempDate[k] ; // parse(billingDate) ;//request.getParameter("appointment_date");
		param[10]=request.getParameter("start_time");
		param[11]=request.getParameter("xml_location"); //request.getParameter("clinic_ref_code");
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
		//BillingONDataHelp billObj = new BillingONDataHelp();
		//String sql = "insert into billing values('\\N',?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)";
		sql = "insert into billing(clinic_no, demographic_no, provider_no, appointment_no, organization_spec_code, demographic_name, hin, update_date, update_time, billing_date, billing_time, clinic_ref_code, content, total, status, dob, visitdate, visittype, provider_ohip_no, provider_rma_no, apptProvider_no, asstProvider_no, creator) values( "
			+ param[0] + "," + param[1] + "," + UtilMisc.nullMySQLEscape(param[2]) + "," + param[3] + "," + UtilMisc.nullMySQLEscape(param[4]) + ","
			+ UtilMisc.nullMySQLEscape(param[5]) + "," + UtilMisc.nullMySQLEscape(param[6]) + "," + UtilMisc.nullMySQLEscape(param[7]) + "," + UtilMisc.nullMySQLEscape(param[8]) + "," + UtilMisc.nullMySQLEscape(param[9]) + ","
			+ UtilMisc.nullMySQLEscape(param[10]) + "," + UtilMisc.nullMySQLEscape(param[11]) + "," + UtilMisc.nullMySQLEscape(param[12]) + "," + UtilMisc.nullMySQLEscape(param[13]) + "," + UtilMisc.nullMySQLEscape(param[14]) + ","
			+ UtilMisc.nullMySQLEscape(param[15]) + "," + UtilMisc.nullMySQLEscape(param[16]) + "," + UtilMisc.nullMySQLEscape(param[17]) + "," + UtilMisc.nullMySQLEscape(param[18]) + "," + UtilMisc.nullMySQLEscape(param[19]) + ","
			+ UtilMisc.nullMySQLEscape(param[20]) + "," + UtilMisc.nullMySQLEscape(param[21]) + ",'" + param[22] + "')";
	    System.out.println("*******************************" + sql);
		nBillNo = dbObj.saveBillingRecord(sql);


		//int recordCount = Integer.parseInt(request.getParameter("record"));
		// combine two vecs into one
		if(vecServiceCodePerc.size()>1) {
			vecServiceCodePrice.add( "" + bdPerc); //vecServiceCodePerc.get(1) );
			vecServiceCodeUnit.add( vecServiceCodePerc.get(2) );
			vecServiceCode.add( vecServiceCodePerc.get(0) );
			vecServiceCodeDesc.add( vecServiceCodePerc.get(3) );
		}

		for (int i=0; i<vecServiceCode.size(); i++){ //recordCount
			String[] param2 = new String[8];
			param2[0] = "" + nBillNo; // billNo;
			param2[1] = (String)vecServiceCode.get(i);//billrec[i]; //request.getParameter("billrec"+i);
			param2[2] = (String)vecServiceCodeDesc.get(i);//billrecdesc[i]; //request.getParameter("billrecdesc"+i);
			param2[3] = ((String)vecServiceCodePrice.get(i)).replaceAll("\\.", "");//pricerec[i]; //request.getParameter("pricerec"+i);
			param2[4] = request.getParameter("dxCode"); //request.getParameter("diagcode");
			param2[5] = tempDate[k]; //request.getParameter("appointment_date");
			param2[6] = request.getParameter("xml_billtype").substring(0,1); //request.getParameter("billtype");
			param2[7] = (String)vecServiceCodeUnit.get(i);//billrecunit[i]; //request.getParameter("billrecunit"+i);

			//insert into billingdetail values('\\N',?,?,?,?,?, ?,?,?)
	    	sql = "insert into billingdetail(billing_no, service_code, service_desc, billing_amount, diagnostic_code, appointment_date, status, billingunit) values( "
	    	+ param2[0] + "," + UtilMisc.nullMySQLEscape(param2[1]) + "," + UtilMisc.nullMySQLEscape(param2[2]) + "," + UtilMisc.nullMySQLEscape(param2[3]) + "," + UtilMisc.nullMySQLEscape(param2[4]) + ","
	    	+ UtilMisc.nullMySQLEscape(param2[5]) + "," + UtilMisc.nullMySQLEscape(param2[6]) + "," + UtilMisc.nullMySQLEscape(param2[7]) + ")";
			nBillDetailNo = 0;
	    	nBillDetailNo = dbObj.saveBillingRecord(sql);
	    	if (nBillDetailNo == 0) {
	    		// roll back
	    		sql = "update billing set status='D' where billing_no = " + nBillNo;
				dbObj.updateDBRecord(sql);
	    		break;
	    	}
	    	System.out.println(nBillNo + sql);
		}
	} // end of for loop
	msg += "<br>Billing records were added.";
  }

  // get patient's billing history
  sql               =
          "select billing_no,billing_date,visitdate,visitType, update_date, clinic_ref_code from billing where demographic_no=" + demo_no +
          " and status!='D' order by billing_date desc, billing_no desc limit 5";
  rs                = dbObj.searchDBRecord(sql);

  while (rs.next()) {
    propHist = new Properties();

    propHist.setProperty("billing_no", "" + rs.getInt("billing_no"));
    propHist.setProperty("visitdate", rs.getString("visitdate"));
    propHist.setProperty("billing_date", rs.getString("billing_date"));
    propHist.setProperty("update_date", rs.getString("update_date"));
    propHist.setProperty("visitType", rs.getString("visitType"));
    propHist.setProperty("clinic_ref_code", rs.getString("clinic_ref_code"));
    vecHist.add(propHist);
  }

  Vector vecHistD = new Vector();

  for (int i = 0; i < vecHist.size(); i++) {
    String billingNo = ((Properties)vecHist.get(i)).getProperty("billing_no", "");

    sql = "select service_code,diagnostic_code,billingunit from billingdetail where billing_no=" + billingNo +
            " and status!='D' order by service_code";
    rs = dbObj.searchDBRecord(sql);

    String dx      = "";
    String serCode = "";

    while (rs.next()) {
      if (dx.equals("") || !dx.equals(rs.getString("diagnostic_code"))) {
        dx += (dx.equals("")
        ? ""
        : ", ") + rs.getString("diagnostic_code");
      }

      if (serCode.equals("") || !serCode.equals(rs.getString("service_code"))) {
        serCode += (serCode.equals("")
        ? ""
        : ", ") + rs.getString("service_code") + " x " + rs.getString("billingunit");
      }
    }

    propHist = new Properties();

    propHist.setProperty("service_code", serCode);
    propHist.setProperty("diagnostic_code", dx);
    vecHistD.add(propHist);
  }

  // display the fixed billing part
  // Retrieving Provider
  Vector vecProvider = new Vector();
  Properties propT = null;
  sql = "select first_name,last_name,provider_no from provider "
   + "where provider_type='doctor' and status='1' and ohip_no || null order by last_name, first_name";
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
    propT = new Properties();
    propT.setProperty("last_name",rs.getString("last_name"));
    propT.setProperty("first_name",rs.getString("first_name"));
    propT.setProperty("proOHIP",rs.getString("provider_no"));
    vecProvider.add(propT);
  }
  // clinic location
  Vector vecLocation = new Vector();
  sql = "select * from clinic_location order by clinic_location_no";
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
    propT = new Properties();
    propT.setProperty("clinic_location_name",rs.getString("clinic_location_name"));
    propT.setProperty("clinic_location_no",rs.getString("clinic_location_no"));
    vecLocation.add(propT);
  }

  // set default value
  // use parameter -> history record
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

  String visitdate = request.getParameter("xml_vdate")==null?"":request.getParameter("xml_vdate");


  // get billing dx/form info
  Vector vecCodeCol1 = new Vector();
  Vector vecCodeCol2 = new Vector();
  Vector vecCodeCol3 = new Vector();
  Properties propPremium = new Properties();
  String serviceCode, serviceDesc, serviceValue, servicePercentage, serviceType,serviceDisp="";
  String headerTitle1="", headerTitle2="", headerTitle3="";

  //int CountService = 0;
  //int Count2 = 0;
  sql = "select c.service_group_name, c.service_order,b.service_code, b.description, b.value, b.percentage from billingservice b, ctl_billingservice c where c.service_code=b.service_code and c.status='A' and c.servicetype ='"
   + ctlBillForm + "' and c.service_group ='" + "Group1" + "' order by c.service_order";
//System.out.println(ctlBillForm+" * ******************************" + sql);
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
    propT = new Properties();
	//serviceCode = rs.getString("service_code");
	//serviceDesc = rs.getString("description");
	//serviceValue = rs.getString("value");
	//servicePercentage = rs.getString("percentage");
	headerTitle1 = rs.getString("service_group_name");
	//serviceDisp = serviceValue;
    propT.setProperty("serviceCode",rs.getString("service_code"));
    propT.setProperty("serviceDesc",rs.getString("description"));
    propT.setProperty("serviceDisp",rs.getString("value"));
    propT.setProperty("servicePercentage",rs.getString("percentage"));
    //propT.setProperty("headerTitle1",rs.getString("service_group_name"));
	vecCodeCol1.add(propT);
  }
  sql = "select service_code,status from ctl_billingservice_premium where ";
  for(int i=0; i<vecCodeCol1.size(); i++) {
  	sql += (i==0?"":" or ") + "service_code='" + ((Properties)vecCodeCol1.get(i)).getProperty("serviceCode") + "'";
  }
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
    propPremium.setProperty(rs.getString("service_code"), "A");
  }

  sql = "select c.service_group_name, c.service_order,b.service_code, b.description, b.value, b.percentage from billingservice b, ctl_billingservice c where c.service_code=b.service_code and c.status='A' and c.servicetype ='"
   + ctlBillForm + "' and c.service_group ='" + "Group2" + "' order by c.service_order";
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
    propT = new Properties();
	headerTitle2 = rs.getString("service_group_name");
    propT.setProperty("serviceCode",rs.getString("service_code"));
    propT.setProperty("serviceDesc",rs.getString("description"));
    propT.setProperty("serviceDisp",rs.getString("value"));
    propT.setProperty("servicePercentage",rs.getString("percentage"));
	vecCodeCol2.add(propT);
  }
  sql = "select service_code,status from ctl_billingservice_premium where ";
  for(int i=0; i<vecCodeCol2.size(); i++) {
  	sql += (i==0?"":" or ") + "service_code='" + ((Properties)vecCodeCol2.get(i)).getProperty("serviceCode") + "'";
  }
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
    propPremium.setProperty(rs.getString("service_code"), "A");
  }

  sql = "select c.service_group_name, c.service_order,b.service_code, b.description, b.value, b.percentage from billingservice b, ctl_billingservice c where c.service_code=b.service_code and c.status='A' and c.servicetype ='"
   + ctlBillForm + "' and c.service_group ='" + "Group3" + "' order by c.service_order";
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
    propT = new Properties();
	headerTitle3 = rs.getString("service_group_name");
    propT.setProperty("serviceCode",rs.getString("service_code"));
    propT.setProperty("serviceDesc",rs.getString("description"));
    propT.setProperty("serviceDisp",rs.getString("value"));
    propT.setProperty("servicePercentage",rs.getString("percentage"));
	vecCodeCol3.add(propT);
  }
  sql = "select service_code,status from ctl_billingservice_premium where ";
  for(int i=0; i<vecCodeCol3.size(); i++) {
  	sql += (i==0?"":" or ") + "service_code='" + ((Properties)vecCodeCol3.get(i)).getProperty("serviceCode") + "'";
  }
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
    propPremium.setProperty(rs.getString("service_code"), "A");
  }

  // create msg
  msg += errorMsg + warningMsg;
System.out.println(" * ******************************" + sql);

%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>OscarBilling - shortcut</title>
  <!-- calendar stylesheet -->
  <link rel="stylesheet" type="text/css" media="all" href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
  <!-- main calendar program -->
  <script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
  <!-- language for the calendar -->
  <script type="text/javascript" src="../../../share/calendar/lang/calendar-en.js"></script>
  <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
  <script type="text/javascript" src="../../../share/calendar/calendar-setup.js"></script>
</head>

  <script language="JavaScript">

            <!--
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
    function onSave() {
        //document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true)
        {
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    function checkAllDates() {
        var b = true;
        if(document.forms[0].billDate.value.length<1){
        	alert("No billing date!");
            b = false;
        } else if(!isChecked("xml_") && document.forms[0].serviceDate0.value.length!=5 || !isServiceCode(document.forms[0].serviceDate0.value)){
        	alert("Need service code!");
            b = false;
        } else if(document.forms[0].serviceDate1.value.length>0 && document.forms[0].serviceDate1.value.length!=5 || !isServiceCode(document.forms[0].serviceDate1.value)){
        	alert("Wrong service code 2!");
            b = false;
        } else if(document.forms[0].serviceDate2.value.length>0 && document.forms[0].serviceDate2.value.length!=5 || !isServiceCode(document.forms[0].serviceDate2.value)){
        	alert("Wrong service code 3!");
            b = false;
        //} else if(document.forms[0].serviceDate3.value.length>0 && document.forms[0].serviceDate3.value.length!=5 || !isServiceCode(document.forms[0].serviceDate3.value)){
        //	alert("Wrong service code 4!");
        //    b = false;
        //} else if(document.forms[0].serviceDate4.value.length>0 && document.forms[0].serviceDate4.value.length!=5 || !isServiceCode(document.forms[0].serviceDate4.value)){
        //	alert("Wrong service code 5!");
        //    b = false;
        } else if(document.forms[0].dxCode.value.length!=3){
        	alert("Wrong dx code!");
            b = false;
        } else if(document.forms[0].xml_provider.options[0].selected){
        	alert("Please select a provider.");
            b = false;
        } else if(document.forms[0].xml_visittype.options[2].selected && document.forms[0].xml_vdate.value==""){
        	alert("Need an admission date.");
            b = false;
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
        if (document.forms[0].elements[i].name.indexOf(s)==0 && document.forms[0].elements[i].name.length==9) {
            if (document.forms[0].elements[i].checked) {
				return true;
			}
    	}
	}
	return false;
}

//-->

  </script>
  <body topmargin="0" >
<div id="Layer1" style="position:absolute; left:1px; top:159px; width:410px; height:200px; z-index:1; background-color: #FFCC00; layer-background-color: #FFCC00; border: 1px none #000000; visibility: hidden">
	<table width="98%" border="0" cellspacing="0" cellpadding="0" align=center>
	<tr bgcolor="#393764">
		<td width="96%" height="7" bgcolor="#FFCC00"><font size="-2" face="Geneva, Arial, Helvetica, san-serif" color="#000000"><b>Billing Form</b></font></td>
		<td width="3%" bgcolor="#FFCC00" height="7"><b><a href="#" onClick="showHideLayers('Layer1','','hide');return false;">X</a></b></font></td>
	</tr>

<%
String ctlcode="", ctlcodename="", currentFormName="";
int ctlCount = 0;
  sql = "select distinct servicetype_name, servicetype from ctl_billingservice where status='A'";
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
	ctlcode = rs.getString("servicetype");
	ctlcodename = rs.getString("servicetype_name");
	ctlCount++;
	if(ctlcode.equals(ctlBillForm)) currentFormName = ctlcodename;
%>
	<tr bgcolor=<%=ctlCount%2==0 ? "#FFFFFF" : "#EEEEFF"%>>
		<td colspan="2"><b><font size="-2" color="#7A388D"><a href="billingShortcutPg1.jsp?billForm=<%=ctlcode%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>&demographic_no=<%=request.getParameter("demographic_no")%>&user_no=<%=user_no%>&apptProvider_no=<%=request.getParameter("apptProvider_no")%>&providerview=<%=request.getParameter("apptProvider_no")%>&appointment_date=<%=request.getParameter("appointment_date")%>&status=<%=request.getParameter("status")%>&start_time=<%=request.getParameter("start_time")%>&bNewForm=1" onClick="showHideLayers('Layer1','','hide');"><%=ctlcodename%></a></font></b></td>
	</tr>
<%
}
%>
	</table>
</div>
<div id="Layer2" style="position:absolute; left:362px; top:26px; width:332px; height:600px; z-index:2; background-color: #FFCC00; layer-background-color: #FFCC00; border: 1px none #000000; visibility: hidden">
	<table width="98%" border="0" cellspacing="0" cellpadding="0" align=center>
	<tr>
		<td width="18%"><b><font size="-2">Dx Code</font></b></td>
		<td width="76%"><b><font size="-2">Description</font></b></td>
		<td width="6%"><a href="#" onClick="showHideLayers('Layer2','','hide');return false">X</a></td>
	</tr>

<%
String ctldiagcode="", ctldiagcodename="";
ctlCount = 0;
  sql = "select d.diagnostic_code dcode, d.description des from diagnosticcode d, ctl_diagcode c where c.diagnostic_code=d.diagnostic_code and c.servicetype='" + ctlBillForm + "' order by d.description";
  rs = dbObj.searchDBRecord(sql);
  while (rs.next()) {
	ctldiagcode = rs.getString("dcode");
	ctldiagcodename = rs.getString("des");
%>
	<tr bgcolor=<%=ctlCount%2==0 ? "#FFFFFF" : "#EEEEFF"%>>
		<td width="18%"><b><font size="-2" color="#7A388D"><a href="#" onClick="document.forms[0].dxCode.value='<%=ctldiagcode%>';showHideLayers('Layer2','','hide');return false;"><%=ctldiagcode%></a></font></b></td>
		<td colspan="2"><font size="-2" color="#7A388D">
		<%=ctldiagcodename.length() < 56 ? ctldiagcodename : ctldiagcodename.substring(0,55)%></font></td>
	</tr>
<%
}
%>
	</table>
</div>


  <table border="0" cellpadding="0" cellspacing="2" width="100%" bgcolor="#CCCCFF">
    <form method="post" name="titlesearch" action="billingShortcutPg2.jsp" onsubmit="return checkTypeIn()">
      <tr>
        <td>
          <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr><td>
          	<b>OscarBilling </b>
        	</td>
			<td align="right">
            <input type="submit" name="submit" value="Save" onclick="javascript:return onSave();" />
            </td>
			</tr>
		  </table>
        </td>
      </tr>
      <tr>
        <td>
          <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr bgcolor="#33CCCC">
              <td nowrap bgcolor="#FFCC99" width="10%" align="center">
                <%= demoname %>
              </td>
              <td bgcolor="#99CCCC" align="center">
                <font color="black"><%= msg %></font>
              </td>
            </tr>
          </table>

          <table border="1" cellspacing="0" cellpadding="0" width="100%" bordercolorlight="#99A005" bordercolordark="#FFFFFF" bgcolor="#FFFFFF">
            <tr >
              <td width="50%">

              <table border="1" cellspacing="2" cellpadding="0" width="100%" bordercolorlight="#99A005" bordercolordark="#FFFFFF" bgcolor="ivory">
              <tr><td nowrap width="30%" align="center">
                <a id="trigger" href="#">[Service Date]</a><br>
                <textarea name="billDate" cols="11" rows="5" readonly></textarea>
              </td>
              <td align="center" width="33%">
                Service Code x Unit<br>
                <input type="text" name="serviceDate0" size="5" maxlength="5" value="">x<input type="text" name="serviceUnit0" size="2" maxlength="2" value=""><br>
                <input type="text" name="serviceDate1" size="5" maxlength="5" value="">x<input type="text" name="serviceUnit1" size="2" maxlength="2" value=""><br>
                <input type="text" name="serviceDate2" size="5" maxlength="5" value="">x<input type="text" name="serviceUnit2" size="2" maxlength="2" value=""><br>
              </td>
              <td valign="top">
              	<table border="0" cellspacing="0" cellpadding="0" width="100%"><tr><td>
	                <a href="#" onClick="showHideLayers('Layer2','','show','Layer1','','hide'); return false;">Dx</a><br>
	                <input type="text" name="dxCode" size="5" maxlength="5" value="<%=dxCode%>">
	                </td><td>
	                Cal.% mode<br>
					<select name="rulePerc" >
					<option value="allAboveCode" >All</option>
					<option value="onlyAboveCode" >Only Above</option>
	                </select>
	                </td></tr>
                </table>

                <hr>
                Referral Doctor<br>
                <input type="text" name="referralCode" size="5" maxlength="6" value=""> Code search
              </td>
              </tr>
              </table>

              </td><td valign="top">

              <table border="1" cellspacing="2" cellpadding="0" width="100%" bordercolorlight="#99A005" bordercolordark="#FFFFFF" bgcolor="#EEEEFF">
              <tr><td nowrap width="30%" align="center">
				<b>Billing Physician</b></td>
				<td width="20%">
				<select name="xml_provider" datafld='xml_provider'>
				<option value="000000" <%=providerview.equals("000000")?"selected":""%>><b>Select Provider</b></option>
				<%
				for(int i=0; i<vecProvider.size(); i++) {
					propT = (Properties) vecProvider.get(i);
				%>
					<option value="<%=propT.getProperty("proOHIP")%>" <%=providerview.equals(propT.getProperty("proOHIP"))?"selected":""%>><b><%=propT.getProperty("last_name")%>, <%=propT.getProperty("first_name")%></b></option>
				<%
				}
				%>
				</select></td>
				<td nowrap width="30%" align="center"><b>Assig. Physician</b></td>
				<td width="20%"><%=providerBean.getProperty(assgProvider_no, "")%>
              	</td>
              </tr>
              <tr>

				<td width="30%"><b>Visit Type</b></td>
				<td width="20%">
				<select name="xml_visittype" datafld='xml_visittype'>
					<option value="00| Clinic Visit" <%=visitType.equals("00")?"selected":""%>>00 | Clinic Visit</option>
					<option value="01| Outpatient Visit" <%=visitType.equals("01")?"selected":""%>>01 | Outpatient Visit</option>
					<option value="02| Hospital Visit" <%=visitType.equals("02")?"selected":""%>>02 | Hospital Visit</option>
					<option value="03| ER" <%=visitType.equals("03")?"selected":""%>>03 | ER</option>
					<option value="04| Nursing Home" <%=visitType.equals("04")?"selected":""%>>04 | Nursing Home</option>
					<option value="05| Home Visit" <%=visitType.equals("05")?"selected":""%>>05 | Home Visit</option>
				</select>
				</td>

				<td width="30%"><b>Billing Type</b></td>
				<td width="20%">
				<select name="xml_billtype" datafld='xml_billtype' >
					<option value="ODP | Bill OHIP" selected>Bill OHIP</option>
					<option value="PAT | Bill Patient">Bill Patient</option>
					<option value="WCB | Worker's Compensation Board">WSIB</option>
				</select>
				</td>
              </tr>
              <tr>
				<td><b>Visit Location</b></td>
				<td colspan="3">
				<select name="xml_location" datafld='xml_location'>
				<%
				for(int i=0; i<vecLocation.size(); i++) {
					propT = (Properties) vecLocation.get(i);
				%>
					<option value="<%=propT.getProperty("clinic_location_no")%>" <%=clinicview.equals(propT.getProperty("clinic_location_no"))?"selected":""%>><%=propT.getProperty("clinic_location_name")%></option>
				<%
				}
				%>
				</select></td>
              </tr>
              <tr>
				<td><b>Admission Date</b></td>
				<td >
				<input type="text" name="xml_vdate" value="<%=visitdate%>" size='10' maxlength='10' readonly>
              	<img src="../../../images/cal.gif" id="xml_vdate_cal">
				</td>
				<td>
				<a href="#" onClick="showHideLayers('Layer1','','show');return false;">Billing form</a>:</font></b>
				</td>
				<td><%=currentFormName.length()<30 ? currentFormName : currentFormName.substring(0,30)%>
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


		<table width="100%" border="0" cellspacing="0" cellpadding="0" height="137">
		<tr>
			<td valign="top" width="33%">

			<table width="100%" border="1" cellspacing="0" cellpadding="0" height="0" bordercolorlight="#99A005" bordercolordark="#FFFFFF" >
			<tr bgcolor="#CCCCFF">
				<th width="10%" nowrap><font size="-1" color="#000000"><%=headerTitle1%>
				</font>
				</th>
				<th width="70%" bgcolor="#CCCCFF"><font size="-1" color="#000000">Description</font></th>
				<th>
				<font size="-1" color="#000000">
				Fee</font>
				</th>
			</tr>
			<%
			for(int i=0; i<vecCodeCol1.size(); i++) {
					propT = (Properties) vecCodeCol1.get(i);
					serviceCode = propT.getProperty("serviceCode");
					serviceDesc = propT.getProperty("serviceDesc");
					serviceDisp = propT.getProperty("serviceDisp");
					servicePercentage = propT.getProperty("servicePercentage");
					if(propPremium.getProperty(serviceCode)!=null) premiumFlag = "A";
					else premiumFlag = "";
			%>
			<tr bgcolor=<%=i%2==0?"#FFFFFF":"#EEEEFF"%>>
				<td nowrap>
				<input type="checkbox" name="code_xml_<%=serviceCode%>" value="checked" >
				<b><font size="-1" color="<%=premiumFlag.equals("A")? "#993333" : "black"%>"><%=serviceCode%></font></b>
				<input type="text" name="unit_xml_<%=serviceCode%>" value="" size="1" maxlength="2" style="width:20px; height:20px;">
				</td>
				<td <%=serviceDesc.length()>30?"title=\""+serviceDesc+"\"":""%>><font size="-1" ><%=serviceDesc.length()>30?serviceDesc.substring(0,30)+"...":serviceDesc%>
				<input type="hidden" name="desc_xml_<%=serviceCode%>" value="<%=serviceDesc%>">
				</font></td>
				<td align="right">
				<font size="-1" ><%=serviceDisp%></font>

				<input type="hidden" name="price_xml_<%=serviceCode%>" value="<%=serviceDisp%>">
				<input type="hidden" name="perc_xml_<%=serviceCode%>" value="<%=servicePercentage%>">
				</font>
				</td>
			</tr>
			<% } %>
			</table>

		</td><td width="33%" valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0" height="0" bordercolorlight="#99A005" bordercolordark="#FFFFFF" >
			<tr bgcolor="#CCCCFF">
				<th width="10%" nowrap><font size="-1" color="#000000"><%=headerTitle2%>
				</font>
				</th>
				<th width="70%" bgcolor="#CCCCFF"><font size="-1" color="#000000">Description</font></th>
				<th>
				<font size="-1" color="#000000">
				Fee</font>
				</th>
			</tr>
			<%
			for(int i=0; i<vecCodeCol2.size(); i++) {
					propT = (Properties) vecCodeCol2.get(i);
					serviceCode = propT.getProperty("serviceCode");
					serviceDesc = propT.getProperty("serviceDesc");
					serviceDisp = propT.getProperty("serviceDisp");
					servicePercentage = propT.getProperty("servicePercentage");
					if(propPremium.getProperty(serviceCode)!=null) premiumFlag = "A";
					else premiumFlag = "";
			%>
			<tr bgcolor=<%=i%2==0?"#FFFFFF":"#EEEEFF"%>>
				<td nowrap>
				<input type="checkbox" name="code_xml_<%=serviceCode%>" value="checked" >
				<b><font size="-1" color="<%=premiumFlag.equals("A")? "#993333" : "black"%>"><%=serviceCode%></font></b>
				<input type="text" name="unit_xml_<%=serviceCode%>" value="" size="1" maxlength="2" style="width:20px; height:20px;">
				</td>
				<td <%=serviceDesc.length()>30?"title=\""+serviceDesc+"\"":""%>><font size="-1" ><%=serviceDesc.length()>30?serviceDesc.substring(0,30)+"...":serviceDesc%>
				<input type="hidden" name="desc_xml_<%=serviceCode%>" value="<%=serviceDesc%>">
				</font></td>
				<td align="right">
				<font size="-1" ><%=serviceDisp%></font>

				<input type="hidden" name="price_xml_<%=serviceCode%>" value="<%=serviceDisp%>">
				<input type="hidden" name="perc_xml_<%=serviceCode%>" value="<%=servicePercentage%>">
				</font>
				</td>
			</tr>
			<% } %>
			</table>


		</td><td width="33%" valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0" height="0" bordercolorlight="#99A005" bordercolordark="#FFFFFF" >
			<tr bgcolor="#CCCCFF">
				<th width="10%" nowrap><font size="-1" color="#000000"><%=headerTitle3%>
				</font>
				</th>
				<th width="70%" bgcolor="#CCCCFF"><font size="-1" color="#000000">Description</font></th>
				<th>
				<font size="-1" color="#000000">
				Fee</font>
				</th>
			</tr>
			<%
			for(int i=0; i<vecCodeCol3.size(); i++) {
					propT = (Properties) vecCodeCol3.get(i);
					serviceCode = propT.getProperty("serviceCode");
					serviceDesc = propT.getProperty("serviceDesc");
					serviceDisp = propT.getProperty("serviceDisp");
					servicePercentage = propT.getProperty("servicePercentage");
					if(propPremium.getProperty(serviceCode)!=null) premiumFlag = "A";
					else premiumFlag = "";
			%>
			<tr bgcolor=<%=i%2==0?"#FFFFFF":"#EEEEFF"%>>
				<td nowrap>
				<input type="checkbox" name="code_xml_<%=serviceCode%>" value="checked" >
				<b><font size="-1" color="<%=premiumFlag.equals("A")? "#993333" : "black"%>"><%=serviceCode%></font></b>
				<input type="text" name="unit_xml_<%=serviceCode%>" value="" size="1" maxlength="2" style="width:20px; height:20px;">
				</td>
				<td <%=serviceDesc.length()>30?"title=\""+serviceDesc+"\"":""%>><font size="-1" ><%=serviceDesc.length()>30?serviceDesc.substring(0,30)+"...":serviceDesc%>
				<input type="hidden" name="desc_xml_<%=serviceCode%>" value="<%=serviceDesc%>">
				</font></td>
				<td align="right">
				<font size="-1" ><%=serviceDisp%></font>

				<input type="hidden" name="price_xml_<%=serviceCode%>" value="<%=serviceDisp%>">
				<input type="hidden" name="perc_xml_<%=serviceCode%>" value="<%=servicePercentage%>">
				</font>
				</td>
			</tr>
			<% } %>
			</table>


		</td>
		</tr>
		</table>



        </td>
      </tr>

	<input type="hidden" name="clinic_no" value="<%=clinicNo%>">
	<input type="hidden" name="demographic_no" value="<%=demo_no%>">
	<input type="hidden" name="appointment_no" value="<%=appt_no%>">

	<input type="hidden" name="ohip_version" value="V03G">
	<input type="hidden" name="hin" value="<%=demoHIN%>">

	<input type="hidden" name="start_time" value="<%=request.getParameter("start_time")%>">

	<input type="hidden" name="demographic_dob" value="<%=demoDOB%>">

	<input type="hidden" name="apptProvider_no" value="<%=request.getParameter("apptProvider_no")%>">
	<input type="hidden" name="asstProvider_no" value="<%=request.getParameter("asstProvider_no")%>">

	<input type="hidden" name="demographic_name" value="<%=demoname%>">
	<input type="hidden" name="providerview" value="<%=providerview%>">
	<input type="hidden" name="appointment_date" value="<%=request.getParameter("appointment_date")%>">
	<input type="hidden" name="assgProvider_no" value="<%=assgProvider_no%>">
	<input type="hidden" name="billForm" value="<%=ctlBillForm%>">
    </form>

  </table>


  <br>
  <table border="0" cellpadding="0" cellspacing="2" width="100%" bgcolor="#CCCCFF">
    <tr>
      <td colspan="6" class="RowTop">
        <%= demoname %>
        -
        <b>Billing History</b> (last 5 records)
      </td>
    </tr>
    <tr>
      <td>
        <table border="1" cellspacing="0" cellpadding="0" bordercolorlight="#99A005" bordercolordark="#FFFFFF" width="100%" bgcolor="#FFFFFF">
          <tr bgcolor="#99CCCC" align="center">
            <td nowrap>
              Serial No.
            </td>
            <td nowrap>
              Billing Date
            </td>
            <td nowrap>
              Appt/Adm Date
            </td>
            <td nowrap>
              Service Code
            </td>
            <td nowrap>
              Dx
            </td>
            <td>
              Create Date
            </td>
          </tr>
<%
          for (int i = 0; i < vecHist.size(); i++) {
            Properties prop  = (Properties)vecHist.get(i);
            Properties propD = (Properties)vecHistD.get(i);
%>
            <tr bgcolor="<%=i%2==0?"ivory":"#EEEEFF"%>" align="center">
              <td>
                <%= prop.getProperty("billing_no", "&nbsp;") %>
              </td>
              <td>
                <%= prop.getProperty("billing_date", "&nbsp;") %>
              </td>
              <td>
                <%= prop.getProperty("visitdate", "&nbsp;") %>
              </td>
              <td>
                <%= propD.getProperty("service_code", "&nbsp;") %>
              </td>
              <td>
                <%= propD.getProperty("diagnostic_code", "&nbsp;") %>
              </td>
              <td>
                <%= prop.getProperty("update_date", "&nbsp;") %>
              </td>
            </tr>
<%
          }
%>
        </table>
      </td>
    </tr>
  </table>

  <script type="text/javascript">//<![CDATA[
    // the default multiple dates selected, first time the calendar is instantiated
    var MA = [];
    function closed(cal) {
      //var el = document.getElementById("output");
      var el = document.titlesearch.billDate;
      // reset initial content.
      el.innerHTML = "";
      MA.length = 0;
      for (var i in cal.multiple) {
        var d = cal.multiple[i];
        if (d) {
          //el.innerHTML += d.print("%Y-%m-%d") + "<br />";
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
      button     : "trigger"
    });
  //]]>
Calendar.setup( { inputField : "xml_vdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 } );

  </script>
<%!
String getDefaultValue(String paraName, Vector vec, String propName) {
  String ret = "";
  if(paraName!=null && !"".equals(paraName)) {
    ret = paraName;
  } else if(vec.get(0)!=null) {
    ret = ((Properties)vec.get(0)).getProperty(propName, "") ;
  }
  System.out.println("paraName:" + paraName + " propName:" + propName + " :" + ret);
  return ret;
}
%>
  </body>
</html>