
<%
//  
//  String curUser_no = (String) session.getAttribute("user");
%>
<%@ page
	import="java.util.*, java.sql.*,java.io.*, oscar.*, oscar.login.*, org.apache.commons.lang.StringEscapeUtils"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties"
	scope="session" />

<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing</title>
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
	ResultSet rs = dbObj.searchDBRecord(sql);
	while(rs.next()) {
    	billing_no =id = "" + rs.getInt("billing_no");
    	clinic_no = dbObj.getString(rs,"clinic_no");
    	location = clinic_no;
    	demographic_no = dbObj.getString(rs,"demographic_no");
    	provider_no = dbObj.getString(rs,"provider_no");
    	appointment_no = dbObj.getString(rs,"appointment_no");
    	//organization_spec_code ; //|???
    	demographic_name = dbObj.getString(rs,"demographic_name");  
    	demographic_name = demographic_name.replaceAll("\\,", "0");
    	demographic_name = demographic_name.replaceAll("\\W", "");
    	demographic_name = demographic_name.replaceAll("0", ",");
    	hin = dbObj.getString(rs,"hin");
    	String [] temp = getHinVer(hin);
    	hin = temp[0];
    	ver = temp[1];
    	
    	update_date = dbObj.getString(rs,"update_date");
    	update_time = dbObj.getString(rs,"update_time");
    	
    	// for mysql 4.1
    	timestamp = update_date + " " + update_time;
    	// for mysql 4.0
    	//timestamp = update_date + " " + update_time;

    	billing_date  = dbObj.getString(rs,"billing_date");
    	billing_time  = dbObj.getString(rs,"billing_time");
    	clinic_ref_code  = dbObj.getString(rs,"clinic_ref_code");
    	facilty_num = clinic_ref_code;
    	content  = dbObj.getString(rs,"content");
    	//; //  | text        | YES  |     | NULL    |                |
    	total   = dbObj.getString(rs,"total");
    	status   = dbObj.getString(rs,"status");
    	dob  = dbObj.getString(rs,"dob");
    	visitdate   = dbObj.getString(rs,"visitdate");
    	admission_date = visitdate;
    	visittype  = dbObj.getString(rs,"visittype");
    	provider_ohip_no  = dbObj.getString(rs,"provider_ohip_no");
    	provider_rma_no  = dbObj.getString(rs,"provider_rma_no");
    	apptProvider_no  = dbObj.getString(rs,"apptProvider_no");
    	asstProvider_no  = dbObj.getString(rs,"asstProvider_no");
    	creator  = dbObj.getString(rs,"creator");
    	
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
		 
		DBHelp.updateDBRecord(sql);
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
		service_code = dbObj.getString(rs,"service_code");
		billing_amount = dbObj.getString(rs,"billing_amount");
		billing_amount = setDecNum(billing_amount);
		diagnostic_code = dbObj.getString(rs,"diagnostic_code");
		appointment_date = dbObj.getString(rs,"appointment_date");
    	billingunit = dbObj.getString(rs,"billingunit");  
    	
    	status = dbObj.getString(rs,"status");  
	    
		sql="insert into billing_on_item values (" + billing_dt_no + "," + id + ", 'HE', 'T', '" + service_code + "', '" 
				+ billing_amount + "', '"+billingunit+"','"
				+ appointment_date + "', '" + diagnostic_code + "', '','','" +status + "',\\N)";
		if(!DBHelp.updateDBRecord(sql)) {
			MiscUtils.getLogger().error("error billing_on_item:" + id);
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
	}
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
