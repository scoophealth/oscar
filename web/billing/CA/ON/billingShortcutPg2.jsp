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
  String content = "";
  String total = "";

  BillingONDataHelp dbObj             = new BillingONDataHelp();
  String            msg               = "The calculation:<br>";
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
		msg += vecServiceCode.get(i) + ": + " + price + " x " + unit + " = " + bdTotal + "<br>";
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
		msg += "Percentage :" + bdPercBase + " x " + perc + " = " + bdPerc + "<br>";
		// adjust perc by min/max
		if(bLimit) {
			bdPerc = bdPerc.min(new BigDecimal(Double.parseDouble(maxFee) ).setScale(2, BigDecimal.ROUND_HALF_UP) );
			bdPerc = bdPerc.max(new BigDecimal(Double.parseDouble(minFee) ).setScale(2, BigDecimal.ROUND_HALF_UP) );
System.out.println("Adjust to (" + minFee + ", " + maxFee + "): " + bdPerc);
		msg += "Adjust to (" + minFee + ", " + maxFee + "): " + bdPerc + "<br>";
		}
    	bdTotal = bdTotal.add(bdPerc);
	}

    total = "" + bdTotal;
    System.out.println("***************total****************:" + bdTotal);
		msg += "Total: " + bdTotal + "<br>";
		msg += "<input type=\"button\" name=\"submit\" value=\" Re-Edit \" onclick=\"history.go(-1)\" />";
	// referral
    content = "";
    String referalCode = (request.getParameter("referralCode")!=null&&request.getParameter("referralCode").length()==6)?request.getParameter("referralCode"):null;
    if(referalCode!=null) {
    	content += "<xml_referral>checked</xml_referral>" ;
    	content += "<rdohip>" + referalCode + "</rdohip>" ;
    }
    content += "<hctype>" + demoHCTYPE + "</hctype>" ;
    content += "<demosex>" + demoSex + "</demosex>" ;

    if(request.getParameter("addition")!=null && "Confirm".equals(request.getParameter("addition"))) {
	    String billingDate = request.getParameter("billDate");
	    String [] tempDate = billingDate.split("\\s");

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
		msg = "<br>Billing records were added.<br>";
		msg += "<input type=\"button\" name=\"submit\" value=\" Close \" onclick=\"self.close();\"/>";
	}
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

//-->

  </script>
  <body topmargin="0" >

  <table border="0" cellpadding="0" cellspacing="2" width="100%" bgcolor="#CCCCFF">
    <form method="post" name="titlesearch" action="billingShortcutPg2.jsp" >
      <tr>
        <td>
          <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr><td>
          	<b>OscarBilling </b>
        	</td>
			<td align="right">
            <input type="submit" name="submit" value="Save" />
            <input type="hidden" name="addition" value="Confirm" />
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

<%
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		String temp=e.nextElement().toString();
%>
	<input type="hidden" name="<%= temp %>" value="<%=StringEscapeUtils.escapeHtml(request.getParameter(temp))%>">
<%
}
%>
    </form>

  </table>


  </body>
</html>