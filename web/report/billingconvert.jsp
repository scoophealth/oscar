<%
//  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
//  String curUser_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*,java.io.*, oscar.*, oscar.login.*, org.apache.commons.lang.StringEscapeUtils" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<html>
<head>
<title>Billing </title>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv=Expires content=-1>
</head>
<body>
busy ...
<% 
	//FileWriter tableOut = new FileWriter("refDemo2.sql");
	DBHelp dbObj = new DBHelp();
	String billing_no ="" , id="";
	String clinic_no ="";
	String demographic_no ="";
	String provider_no    ="" ;
	String appointment_no  ="" ;
	String demographic_name =""  ;
	String hin    =""        , ver="";
	String update_date    =""      , timestamp="";
	String update_time     =""     ;//  |timestamp
	String billing_date   =""      ;
	String billing_time    =""     ;
	String clinic_ref_code =""  , facilty_num="";
	String content  =""           ; //  | text        | YES  |     | NULL    |                |
	String total   =""             ; //  | 
	String status    =""           ;
	String dob     =""             ; 
	String visitdate   =""         , admission_date ="";
	String visittype     =""       ;
	String provider_ohip_no  =""  ;
	String provider_rma_no  =""    ;
	String apptProvider_no  =""    ;
	String asstProvider_no  =""    ;
	String creator     =""         ;

	String pay_program="" ;
	String location ="";
	String sex ="";
	String province ="";
	String ref_num ="";
	String man_review ="";

	String slimit = request.getParameter("d") != null ? request.getParameter("d") : "0";
	String sql="select * from billing order by billing_no limit " + slimit + ", 1000000";
 	//System.out.println(sql);
	ResultSet rs = dbObj.searchDBRecord(sql);
	while(rs.next()) {
    	billing_no =id = "" + rs.getInt("billing_no");
    	clinic_no = rs.getString("clinic_no");
    	location = clinic_no;
    	demographic_no = rs.getString("demographic_no");
    	provider_no = rs.getString("provider_no");
    	appointment_no = rs.getString("appointment_no");
    	//organization_spec_code ; //|???
    	demographic_name = rs.getString("demographic_name");  
    	demographic_name = demographic_name.replaceAll("\\,", "0");
    	demographic_name = demographic_name.replaceAll("\\W", "");
    	demographic_name = demographic_name.replaceAll("0", ",");
    	hin = rs.getString("hin");
    	String [] temp = getHinVer(hin);
    	hin = temp[0];
    	ver = temp[1];
    	
    	update_date = rs.getString("update_date");
    	update_time = rs.getString("update_time");
    	
    	// for mysql 4.1
    	timestamp = update_date + " " + update_time;
    	// for mysql 4.0
    	//timestamp = update_date + " " + update_time;

    	billing_date  = rs.getString("billing_date");
    	billing_time  = rs.getString("billing_time");
    	clinic_ref_code  = rs.getString("clinic_ref_code");
    	facilty_num = clinic_ref_code;
    	content  = rs.getString("content");
    	//; //  | text        | YES  |     | NULL    |                |
    	total   = rs.getString("total");
    	status   = rs.getString("status");
    	dob  = rs.getString("dob");
    	visitdate   = rs.getString("visitdate");
    	admission_date = visitdate;
    	visittype  = rs.getString("visittype");
    	provider_ohip_no  = rs.getString("provider_ohip_no");
    	provider_rma_no  = rs.getString("provider_rma_no");
    	apptProvider_no  = rs.getString("apptProvider_no");
    	asstProvider_no  = rs.getString("asstProvider_no");
    	creator  = rs.getString("creator");
    	
    	// get demo data
    	sex = getXMLStringWithDefault(content, "demosex", "1");
    	province = getXMLStringWithDefault(content, "hctype", "ON");
    	ref_num = getXMLStringWithDefault(content, "rdohip", "");
    	ref_num = ref_num.equals("000000")? "" : ref_num;
    	man_review = getXMLStringWithDefault(content, "mreview", "");
    	man_review = man_review.equals("checked") ? "Y" : man_review;
    	pay_program = province.equals("ON")? "HCP" : "RMB";
	   
		sql="insert into billing_on_cheader1 values (" + id + ", 0, 'HE', 'H', '" + hin + "', '" + ver + "', '"+dob+"','"
				+ pay_program + "', 'P', '" + ref_num + "', '" +facilty_num + "','" + admission_date + "','','" + man_review+ "','" 
				+location+"', " + demographic_no + ", '" + provider_no +"'," + appointment_no+ ",'"+ demographic_name+ "','"
				+ sex + "', '" + province + "', '" + billing_date + "','" +billing_time+"','"+total+"','','"
				+status+"','','"+visittype+"','"+provider_ohip_no+"','"+provider_rma_no+"','"+apptProvider_no+"','"
				+asstProvider_no+"','" +creator+"','"+timestamp+"')";
		 System.out.println(sql);
		if(!dbObj.updateDBRecord(sql)) {
			System.out.println("error:" + id);
		} else {
			System.out.println("::" + id);
		}
	}
	rs.close();
	
	
	sql="select * from billingdetail order by billing_dt_no limit " + slimit + ", 1000000";
	String billing_dt_no = "";
	String service_code = "";
	//String service_desc = "";
	String billing_amount = "";
	String diagnostic_code = "";
	String appointment_date = "";
	//String status = "";
	String billingunit = "";
	rs = dbObj.searchDBRecord(sql);
	while(rs.next()) {
		billing_dt_no = "" + rs.getInt("billing_dt_no");
		id = "" + rs.getInt("billing_no");
		service_code = rs.getString("service_code");
		billing_amount = rs.getString("billing_amount");
		billing_amount = setDecNum(billing_amount);
		diagnostic_code = rs.getString("diagnostic_code");
		appointment_date = rs.getString("appointment_date");
    	billingunit = rs.getString("billingunit");  
    	
    	status = rs.getString("status");  
	    
		sql="insert into billing_on_item values (" + billing_dt_no + "," + id + ", 'HE', 'T', '" + service_code + "', '" 
				+ billing_amount + "', '"+billingunit+"','"
				+ appointment_date + "', '" + diagnostic_code + "', '','','" +status + "',\\N)";
		if(!dbObj.updateDBRecord(sql)) {
			System.out.println("error billing_on_item:" + id);
		} else {
			System.out.println("billing_on_item::" + id + sql);
		}
	}
	rs.close();
	
%> 

<%!	private static String[] getHinVer(String name) {
	String ret[] = new String[] { "", "" };
	if (name.length() > 1) {
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) >= '0' && name.charAt(i) <= '9')
				ret[0] += name.charAt(i);
			else
				ret[1] += name.charAt(i);
		}
	} else {
		//System.out.println(" no hin!!!");
	}
	// System.out.println(ret[0] + " name!" + ret[1]);
	return ret;
}
%>
<%!	private String getXMLStringWithDefault(String xmlStr, String xmlName, String strDefault) {
	String retval = SxmlMisc.getXmlContent(xmlStr, "<" + xmlName + ">", "</" + xmlName + ">");
	retval = retval == null || "".equals(retval) ? strDefault : retval;
	return retval;
}
%>
<%! private String setDecNum(String amount) {
	String ret = amount;
	if(amount != null && amount.length()>2) {
		int n = amount.length();
		ret = amount.substring(0, (n-2)) + "." + amount.substring(n-2);
		//System.out.println(ret);
	} else if(amount != null && amount.length()==2) {
		ret = "0." + amount;
	} else if(amount != null && amount.length()==1) {
		ret = "0.0" + amount;
	}
	return ret;
}
%>
done.
</body>
</html>
