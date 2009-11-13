
<%
	

	String curUser_no = (String) session.getAttribute("user");
    String userName = (String) session.getAttribute("userlastname") + "," + (String) session.getAttribute("userfirstname");

	String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
    
	String demographic_no = request.getParameter("demographic_no");
	int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    EctARRecord rec = new EctARRecord();
    java.util.Properties props = rec.getARPrintRecord(demoNo, formId);
%>

<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ page
	import="oscar.oscarEncounter.data.*,oscar.oscarEncounter.pageUtil.EctSessionBean"%>
<%@ page import="org.oscarcitizens.oscarClient.main.*"%>
<%@ page import="org.oscarcitizens.util.*"%>
<%@ page import="javax.xml.soap.*"%>
<%@ page import="javax.xml.messaging.*"%>
<%@ page import="javax.xml.transform.*"%>
<%@ page import="javax.xml.transform.dom.*"%>
<%@ page import="org.w3c.dom.*"%>
<%@ page import="org.xml.sax.InputSource"%>
<%@ page import="javax.xml.parsers.*"%>
<%@ page import="javax.xml.transform.*"%>
<%@ page import="javax.xml.transform.dom.*"%>
<%@ page import="javax.xml.transform.stream.*"%>


<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean"
	scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<%@ include file="../admin/dbconnection.jsp"%>
<% 
	String [][] dbQueries=new String[][] { 
		{"search_master","select pin from demographic where demographic_no=?" }, 
	};
	daySheetBean.doConfigure(dbQueries);
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
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ENCOUNTER SHEET</title>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv=Expires content=-1>
<script language="JavaScript">
<!--

//-->
</SCRIPT>
</head>
<body onLoad="setfocus()">
<%
	String pin = null;
	ResultSet rs = null ;

	String[] par =new String[1];
	par[0]=demographic_no; 

	rs = daySheetBean.queryResults(par, "search_master");
	while (rs.next()) { 
		pin = daySheetBean.getString(rs,"pin") ;
	}
	daySheetBean.closePstmtConn();

	if(pin == null) {
		daySheetBean.closePstmtConn();
		out.println("The patient does not have a pin number.<br><br>Please close this window first,<br> then set a pin for the patient before trying to send eChart.<br><br><input type='button' name='button' value='Close' onclick='window.close()'>");
		return;
	}
%>

<%
	String _authCode = PropsUtil.get("citizensauthcode").trim() ;
	String _msgContent = "profile" ;

	String _receiverID = pin ;
	String _senderName = userName ;
	String _senderID = curUser_no ;
	String _srcLocationID = "3821" ;
	String _srcLocationDesc = "SFHC" ;
	String _profileTimeStamp = props.getProperty("formEdited", "") ;
	String _subject = "Form AR pg1" ;
	String _profile = null ;

	// take data from the table
	
	_profile = "<html>\n<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>\n<title> ANTENATAL RECORD 1</title>\n<link rel=\"stylesheet\" href=\"antenatalRecordPrint.css\" >\n<meta http-equiv=\"expires\" content=\"Mon,12 May 1998 00:36:05 GMT\">\n<meta http-equiv=\"Pragma\" content=\"no-cache\">\n<script language=\"JavaScript\">\n<!--\nfunction setfocus() {\n  this.focus();\n}\nvar ox = 0;\nvar oy = 0;\nfunction ff(x,y,w,h,name) { \n  x = eval(ox+x);\n  y = eval(oy+y);\n  document.writeln('<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:'+x+'px; top:'+y+'px; width:'+w+'px; height:'+h+'px;\"> ');\n  document.writeln(name);\n  document.writeln('</div>');\n}\n\n-->\n</SCRIPT>\n</head>\n\n<body onLoad=\"setfocus()\" topmargin=\"0\" leftmargin=\"1\" rightmargin=\"1\" bgcolor=\"navy\">\n<img src=\"graphics/formAR1.gif\"/>\n\n<script language=\"JavaScript\">\nff(750,10,50,20,'<span class=\"title\"><a href=\"javascript: window.print();\">Print</a></span>' );\nff(750,40,50,20,'<span class=\"title\"><a href=# onClick=\"window.close()\">Cancel</a></span>' );\n\nff(192,0,300,20,'<span class=\"title\">Antenatal Record 1</span>' );\nff(8,68,100,20,'<span class=\"tdname\">Name</span>' );\nff(8,90,100,20,'<span class=\"tdname\">Address</span>' );\nff(5,114,200,20,'<span class=\"smalltdname\">Date of birth (yyyy/mm/dd)</span>' );\nff(113,113,100,20,'<span class=\"tdname\">Age</span>' );\nff(145,113,200,20,'<span class=\"tdname\">Marital status</span>' );\nff(240,113,200,20,'<span class=\"tdname\">Education level</span>' );\nff(165,123,50,20,'<span class=\"tdname\">M</span>' );\nff(194,123,50,20,'<span class=\"tdname\">CL</span>' );\nff(224,123,50,20,'<span class=\"tdname\">S</span>' );\n" ;
	_profile += "ff(50,75,500,20,\"" + UtilMisc.JSEscape(props.getProperty("c_pName", ""))  + "\" );\n" ;
	_profile += "ff(50,98,500,20,\"" +UtilMisc.JSEscape(props.getProperty("c_address", ""))  + "\" );\n" ;
	_profile += "ff(25,126,100,20,\"" + props.getProperty("pg1_dateOfBirth", "")  + "\");\n" ;
	_profile += "ff(120,126,50,20,\"" + props.getProperty("pg1_age", "")  + "\" );\n" ;
	_profile += "ff(153,123,20,20,\"" + (props.getProperty("pg1_msMarried", "").equals("")?"":"X") + "\" );\n" ;
	_profile += "ff(181,123,20,20,\"" + (props.getProperty("pg1_msCommonLaw", "").equals("")?"":"X") + "\" );\n" ;
	_profile += "ff(212,123,20,20,\"" + (props.getProperty("pg1_msSingle", "").equals("")?"":"X") + "\" );\n"  ;
	_profile += "ff(240,126,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_eduLevel", ""))  + "\" );\n" ;

	_profile += "ff(8,137,200,20,'<span class=\"tdname\">Occupation</span>' );\nff(113,137,200,20,'<span class=\"tdname\">Language</span>' );\nff(208,137,200,20,'<span class=\"tdname\">Home phone</span>' );\nff(305,137,200,20,'<span class=\"tdname\">Work phone</span>' );\nff(400,137,200,20,'<span class=\"tdname\">Name of partner</span>' );\nff(550,137,200,20,'<span class=\"tdname\">Age</span>' );\nff(580,137,200,20,'<span class=\"tdname\">Occupation</span>' );\n" ;

	_profile += "ff(8,148,140,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_occupation", ""))  + "\" );\n" ;
	_profile += "ff(113,148,140,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_language", ""))  + "\" );\n" ;
	_profile += "ff(213,148,140,20,\"" + props.getProperty("pg1_homePhone", "")  + "\" );\n" ;
	_profile += "ff(310,148,140,20,\"" + props.getProperty("pg1_workPhone", "")  + "\" );\n" ;
	_profile += "ff(400,148,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_partnerName", ""))  + "\" );\n" ;
	_profile += "ff(555,148,50,20,\"" + props.getProperty("pg1_partnerAge", "")  + "\" );\n" ;
	_profile += "ff(580,148,140,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_partnerOccupation", ""))  + "\" );\n"  ;

	_profile += "ff(8,160,200,20,'<span class=\"tdname\">Birth attendants</span>' );\nff(150,160,200,20,'<span class=\"tdname\">Family physician</span>' );\nff(320,160,200,20,'<span class=\"tdname\">Newborn care</span>' );\nff(481,160,200,20,'<span class=\"tdname\">Ethnic background of mother/father</span>' );\nff(25,175,50,20,'<span class=\"tdname\">OBS</span>' );\nff(66,175,50,20,'<span class=\"tdname\">FP</span>' );\nff(100,175,100,20,'<span class=\"tdname\">Midwife</span>' );\nff(337,175,50,20,'<span class=\"tdname\">Ped.</span>' );\nff(378,175,50,20,'<span class=\"tdname\">FP</span>' );\nff(415,175,100,20,'<span class=\"tdname\">Midwife</span>' );\n" ;

	_profile += "ff(13,175,20,20,\"" + (props.getProperty("pg1_baObs", "").equals("")?"":"X")   + "\" );\n" ;
	_profile += "ff(55,175,20,20,\"" + (props.getProperty("pg1_baFP", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(90,175,20,20,\"" + (props.getProperty("pg1_baMidwife", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(10,195,200,20,\"" + UtilMisc.JSEscape(props.getProperty("c_ba", ""))  + "\" );\n" ;
	_profile += "ff(150,175,200,100,\"" + UtilMisc.JSEscape(props.getProperty("pg1_famPhys", "")).replace('\r', ' ')  + "\" );\n" ;
	_profile += "ff(324,175,20,20,\"" + (props.getProperty("pg1_ncPed", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(365,175,20,20,\"" + (props.getProperty("pg1_ncFP", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(400,175,20,20,\"" + (props.getProperty("pg1_ncMidwife", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(320,195,200,20,\"" + UtilMisc.JSEscape(props.getProperty("c_nc", ""))  + "\" );\n" ;

	_profile += "ff(23,216,200,20,'<span class=\"tdname\">VBAC</span>' );\nff(23,236,200,20,'<span class=\"tdname\">Repeat CS</span>' );\nff(81,214,200,20,'<span class=\"tdname\">Allergies (list)</span>' );\nff(392,214,200,20,'<span class=\"tdname\">Medications (list)</span>' );\n" ;
	
	_profile += "ff(12,216,20,20,\"" + (props.getProperty("pg1_vbac", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(12,236,20,20,\"" + (props.getProperty("pg1_repeatCS", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(6,275,200,20,'<span class=\"tdname\">Menstrual history(LMP):</span>' );\nff(230,275,200,20,'<span class=\"tdname\">Cycle</span>' );\nff(354,278,200,20,'<span class=\"tdname\">Regular</span>' );\nff(420,278,200,20,'<span class=\"tdname\">EDB</span>' );\n" ;

	_profile += "ff(121,271,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_menLMP", ""))  + "\" );\n" ;
	_profile += "ff(270,273,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_menCycle", ""))  + "\" );\n" ;
	_profile += "ff(342,277,20,20,\"" + (props.getProperty("pg1_menReg", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(453,273,100,20,\"" + props.getProperty("pg1_menEDB", "")  + "\" );\n" ;
	_profile += "ff(550,298,100,20,\"" + props.getProperty("c_finalEDB", "")  + "\" );\n" ;

	_profile += "ff(6,292,200,20,'<span class=\"tdname\">Contraception:</span>' );\nff(24,306,200,20,'<span class=\"tdname\">IUD</span>' );\nff(65,306,200,20,'<span class=\"tdname\">Hormonal(type)</span>' );\nff(257,306,200,20,'<span class=\"tdname\">Other</span>' );\nff(390,303,200,20,'<span class=\"tdname\">Last used</span>' );\n" ;
	
	_profile += "ff(12,306,20,20,\"" + (props.getProperty("pg1_iud", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(53,306,20,20,\"" + (props.getProperty("pg1_hormone", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(140,306,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_hormoneType", ""))  + "\" );\n" ;
	_profile += "ff(244,306,20,20,\"" + (props.getProperty("pg1_otherAR1", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(286,303,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_otherAR1Name", ""))  + "\" );\n" ;
	_profile += "ff(440,298,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_lastUsed", ""))  + "\" );\n" ;
	
	_profile += "ff(6,334,200,20,'<span class=\"tdname\">Gravida</span>' );\nff(66,334,200,20,'<span class=\"tdname\">Term</span>' );\nff(93,334,200,20,'<span class=\"tdname\">Prem</span>' );\nff(121,334,200,20,'<span class=\"tdname\">No. of pregnancy loss(es)</span>' );\nff(141,347,200,20,'<span class=\"tdname\">Ectopic</span>' );\nff(253,347,200,20,'<span class=\"tdname\">Termination</span>' );\nff(377,347,200,20,'<span class=\"tdname\">Spontaneous</span>' );\nff(513,347,200,20,'<span class=\"tdname\">Stillborn</span>' );\nff(605,334,100,20,'<span class=\"tdname\">Living</span>' );\nff(638,334,200,20,'<span class=\"tdname\">Multipregnancy</span>' );\nff(639,349,50,20,'<span class=\"tdname\">No.</span>' );\n" ;

	_profile += "ff(12,348,100,20,\"" + UtilMisc.JSEscape(props.getProperty("c_gravida", ""))  + "\" );\n" ;
	_profile += "ff(70,348,100,20,\"" + UtilMisc.JSEscape(props.getProperty("c_term", ""))  + "\" );\n" ;
	_profile += "ff(93,348,100,20,\"" + UtilMisc.JSEscape(props.getProperty("c_prem", ""))  + "\" );\n" ;
	_profile += "ff(131,347,20,20,\"" + (props.getProperty("pg1_ectopic", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(189,344,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_ectopicBox", ""))  + "\" );\n" ;
	_profile += "ff(244,347,20,20,\"" + (props.getProperty("pg1_termination", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(312,344,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_terminationBox", ""))  + "\" );\n" ;
	_profile += "ff(367,347,20,20,\"" + (props.getProperty("pg1_spontaneous", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(442,344,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_spontaneousBox", ""))  + "\" );\n" ;
	_profile += "ff(504,347,20,20,\"" + (props.getProperty("pg1_stillborn", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(555,344,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_stillbornBox", ""))  + "\" );\n" ;
	_profile += "ff(605,348,100,20,\"" + UtilMisc.JSEscape(props.getProperty("c_living", ""))  + "\" );\n" ;
	_profile += "ff(653,350,200,20,\"" + props.getProperty("pg1_multi", "")  + "\" );\n" ;

	_profile += "ff(12,395,50,20,'<span class=\"tdname\">No.</span>' );\nff(38,395,50,20,'<span class=\"tdname\">Year</span>' );\nff(73,388,50,20,'<span class=\"tdname\">Sex</span>' );\nff(73,400,50,20,'<span class=\"tdname\">M/F</span>' );\nff(98,388,100,20,'<span class=\"tdname\">Gest. age</span>' );\nff(105,400,100,20,'<span class=\"tdname\">(weeks)</span>' );\nff(158,388,100,20,'<span class=\"tdname\">Birth</span>' );\nff(156,400,100,20,'<span class=\"tdname\">weight</span>' );\nff(205,388,100,20,'<span class=\"tdname\">Length</span>' );\nff(200,400,100,20,'<span class=\"tdname\">of labour</span>' );\nff(273,388,100,20,'<span class=\"tdname\">Place</span>' );\nff(270,400,100,20,'<span class=\"tdname\">of birth</span>' );\nff(330,388,100,20,'<span class=\"tdname\">Type of birth</span>' );\nff(332,400,150,20,'<span class=\"smalltdname\">SVB CS Ass&#39;d</span>' );\nff(455,395,250,20,'<span class=\"tdname\">Comments regarding pregnancy and birth</span>' );\n" ;

	_profile += "ff(12,421,20,20,\"1\" );\n" ;
	_profile += "ff(36,421,100,20,\"" + props.getProperty("pg1_year1", "")  + "\" );\n" ;
	_profile += "ff(78,421,20,20,\"" + props.getProperty("pg1_sex1", "")  + "\" );\n" ;
	_profile += "ff(100,421,100,20,\"" + props.getProperty("pg1_oh_gest1", "")  + "\" );\n" ;
	_profile += "ff(148,421,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_weight1", ""))  + "\" );\n" ;
	_profile += "ff(200,421,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_length1", ""))  + "\" );\n" ;
	_profile += "ff(245,421,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_place1", ""))  + "\" );\n" ;
	_profile += "ff(338,421,20,20,\"" + (props.getProperty("pg1_svb1", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(353,421,20,20,\"" + (props.getProperty("pg1_cs1", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(369,421,20,20,\"" + (props.getProperty("pg1_ass1", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(12,439,20,20,\"2\" );\n" ;
	_profile += "ff(36,439,100,20,\"" + props.getProperty("pg1_year2", "")  + "\" );\n" ;
	_profile += "ff(78,439,20,20,\"" + props.getProperty("pg1_sex2", "")  + "\" );\n" ;
	_profile += "ff(100,439,100,20,\"" + props.getProperty("pg1_oh_gest2", "")  + "\" );\n" ;
	_profile += "ff(148,439,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_weight2", ""))  + "\" );\n" ;
	_profile += "ff(200,439,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_length2", ""))  + "\" );\n" ;
	_profile += "ff(245,439,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_place2", ""))  + "\" );\n" ;
	_profile += "ff(338,439,20,20,\"" + (props.getProperty("pg1_svb2", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(353,439,20,20,\"" + (props.getProperty("pg1_cs2", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(369,439,20,20,\"" + (props.getProperty("pg1_ass2", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(12,457,20,20,\"3\" );\n" ;
	_profile += "ff(36,457,100,20,\"" + props.getProperty("pg1_year3", "")  + "\" );\n" ;
	_profile += "ff(78,457,20,20,\"" + props.getProperty("pg1_sex3", "")  + "\" );\n" ;
	_profile += "ff(100,457,100,20,\"" + props.getProperty("pg1_oh_gest3", "")  + "\" );\n" ;
	_profile += "ff(148,457,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_weight3", ""))  + "\" );\n" ;
	_profile += "ff(200,457,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_length3", ""))  + "\" );\n" ;
	_profile += "ff(245,457,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_place3", ""))  + "\" );\n" ;
	_profile += "ff(338,457,20,20,\"" + (props.getProperty("pg1_svb3", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(353,457,20,20,\"" + (props.getProperty("pg1_cs3", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(369,457,20,20,\"" + (props.getProperty("pg1_ass3", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(12,476,20,20,\"4\" );\n" ;
	_profile += "ff(36,476,100,20,\"" + props.getProperty("pg1_year4", "")  + "\" );\n" ;
	_profile += "ff(78,476,20,20,\"" + props.getProperty("pg1_sex4", "")  + "\" );\n" ;
	_profile += "ff(100,476,100,20,\"" + props.getProperty("pg1_oh_gest4", "")  + "\" );\n" ;
	_profile += "ff(148,476,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_weight4", ""))  + "\" );\n" ;
	_profile += "ff(200,476,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_length4", ""))  + "\" );\n" ;
	_profile += "ff(245,476,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_place4", ""))  + "\" );\n" ;
	_profile += "ff(338,476,20,20,\"" + (props.getProperty("pg1_svb4", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(353,476,20,20,\"" + (props.getProperty("pg1_cs4", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(369,476,20,20,\"" + (props.getProperty("pg1_ass4", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(12,494,20,20,\"5\" );\n" ;
	_profile += "ff(36,494,100,20,\"" + props.getProperty("pg1_year5", "")  + "\" );\n" ;
	_profile += "ff(78,494,20,20,\"" + props.getProperty("pg1_sex5", "")  + "\" );\n" ;
	_profile += "ff(100,494,100,20,\"" + props.getProperty("pg1_oh_gest5", "")  + "\" );\n" ;
	_profile += "ff(148,494,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_weight5", ""))  + "\" );\n" ;
	_profile += "ff(200,494,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_length5", ""))  + "\" );\n" ;
	_profile += "ff(245,494,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_place5", ""))  + "\" );\n" ;
	_profile += "ff(338,494,20,20,\"" + (props.getProperty("pg1_svb5", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(353,494,20,20,\"" + (props.getProperty("pg1_cs5", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(369,494,20,20,\"" + (props.getProperty("pg1_ass5", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(12,512,20,20,\"6\" );\n" ;
	_profile += "ff(36,512,100,20,\"" + props.getProperty("pg1_year6", "")  + "\" );\n" ;
	_profile += "ff(78,512,20,20,\"" + props.getProperty("pg1_sex6", "")  + "\" );\n" ;
	_profile += "ff(100,512,100,20,\"" + props.getProperty("pg1_oh_gest6", "")  + "\" );\n" ;
	_profile += "ff(148,512,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_weight6", ""))  + "\" );\n" ;
	_profile += "ff(200,512,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_length6", ""))  + "\" );\n" ;
	_profile += "ff(245,512,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_place6", ""))  + "\" );\n" ;
	_profile += "ff(338,512,20,20,\"" + (props.getProperty("pg1_svb6", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(353,512,20,20,\"" + (props.getProperty("pg1_cs6", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(369,512,20,20,\"" + (props.getProperty("pg1_ass6", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(16,549,200,20,'<span class=\"tdname\">Current Pregnancy</span>' );\nff(145,549,200,20,'<span class=\"tdname\">Medical</span>' );\nff(225,549,200,20,'<span class=\"tdname\">Yes</span>' );\nff(245,549,200,20,'<span class=\"tdname\">No</span>' );\nff(295,549,200,20,'<span class=\"tdname\">Genetic/Family</span>' );\nff(400,549,200,20,'<span class=\"tdname\">Yes</span>' );\nff(420,549,200,20,'<span class=\"tdname\">No</span>' );\nff(450,549,200,20,'<span class=\"tdname\">Infection Discussion Topics</span>' );\nff(595,549,200,20,'<span class=\"tdname\"><b>Physical examination</b></span>' );\n\n\nff(10,561,200,20,'<span class=\"smalltdname\"><i>(check if positive)</i></span>' );\nff(7,571,200,20,'<span class=\"smalltdname\">1.</span>' );\nff(25,571,200,20,'<span class=\"smalltdname\">Bleeding</span>' );\nff(7,583,200,20,'<span class=\"smalltdname\">2.</span>' );\nff(25,583,200,20,'<span class=\"smalltdname\">Vomiting</span>' );\nff(7,595,200,20,'<span class=\"smalltdname\">3.</span>' );\nff(25,595,200,20,'<span class=\"smalltdname\">Smoking</span>' );\nff(25,604,200,20,'<span class=\"smalltdname\">cig/day</span>' );\nff(7,619,200,20,'<span class=\"smalltdname\">4.</span>' );\nff(25,619,200,20,'<span class=\"smalltdname\">Drugs</span>' );\nff(7,632,200,20,'<span class=\"smalltdname\">5.</span>' );\nff(25,632,200,20,'<span class=\"smalltdname\">Alcohol</span>' );\nff(25,643,200,20,'<span class=\"smalltdname\">drinks/day</span>' );\nff(7,655,200,20,'<span class=\"smalltdname\">6.</span>' );\nff(25,655,200,20,'<span class=\"smalltdname\">Infertility</span>' );\nff(7,667,200,20,'<span class=\"smalltdname\">7.</span>' );\nff(25,667,200,20,'<span class=\"smalltdname\">Radiation</span>' );\nff(7,679,200,20,'<span class=\"smalltdname\">8.</span>' );\nff(25,679,200,20,'<span class=\"smalltdname\">Occup./Env.</span>' );\nff(25,689,200,20,'<span class=\"smalltdname\">hazards</span>' );\n" ;

	_profile += "ff(102,572,20,20,\"" + (props.getProperty("pg1_cp1", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(102,584,20,20,\"" + (props.getProperty("pg1_cp2", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(66,605,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_box3", ""))  + "\" );\n" ;
	_profile += "ff(102,608,20,20,\"" + (props.getProperty("pg1_cp3", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(102,620,20,20,\"" + (props.getProperty("pg1_cp4", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(69,641,100,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_box5", ""))  + "\" );\n" ;
	_profile += "ff(102,644,20,20,\"" + (props.getProperty("pg1_cp5", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(102,656,20,20,\"" + (props.getProperty("pg1_cp6", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(102,668,20,20,\"" + (props.getProperty("pg1_cp7", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(102,680,20,20,\"" + (props.getProperty("pg1_cp8", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(10,704,200,20,'<span class=\"smalltdname\"><b>Nutrition Assessment</b></span>' );\nff(10,714,200,20,'<span class=\"smalltdname\"><i>(check if positive)</i></span>' );\nff(8,726,200,20,'<span class=\"smalltdname\">Folic acid/vitamins</span>' );\nff(8,738,200,20,'<span class=\"smalltdname\">Milk products</span>' );\nff(8,750,200,20,'<span class=\"smalltdname\">Diet</span>' );\nff(27,759,200,20,'<span class=\"smalltdname\">Balanced</span>' );\nff(27,770,200,20,'<span class=\"smalltdname\">Restricted</span>' );\nff(8,782,200,20,'<span class=\"smalltdname\">Dietitian referral</span>' );\n" ;

	_profile += "ff(102,727,20,20,\"" + (props.getProperty("pg1_naFolic", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(102,739,20,20,\"" + (props.getProperty("pg1_naMilk", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(102,758,20,20,\"" + (props.getProperty("pg1_naDietBal", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(102,771,20,20,\"" + (props.getProperty("pg1_naDietRes", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(102,783,20,20,\"" + (props.getProperty("pg1_naRef", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(122,572,200,20,'<span class=\"smalltdname\">9.</span>' );\nff(137,572,200,20,'<span class=\"smalltdname\">Hypertension</span>' );\nff(122,584,200,20,'<span class=\"smalltdname\">10.</span>' );\nff(137,584,200,20,'<span class=\"smalltdname\">Endocrine/Diabetes</span>' );\nff(122,596,200,20,'<span class=\"smalltdname\">11.</span>' );\nff(137,596,200,20,'<span class=\"smalltdname\">Heart</span>' );\nff(122,608,200,20,'<span class=\"smalltdname\">12.</span>' );\nff(137,608,200,20,'<span class=\"smalltdname\">Renal/urinary tract</span>' );\nff(122,620,200,20,'<span class=\"smalltdname\">13.</span>' );\nff(137,620,200,20,'<span class=\"smalltdname\">Respiratory</span>' );\nff(122,632,200,20,'<span class=\"smalltdname\">14.</span>' );\nff(137,632,200,20,'<span class=\"smalltdname\">Liver/Hepatitis/GI</span>' );\nff(122,644,200,20,'<span class=\"smalltdname\">15.</span>' );\nff(137,644,200,20,'<span class=\"smalltdname\">Neurological</span>' );\nff(122,656,200,20,'<span class=\"smalltdname\">16.</span>' );\nff(137,656,200,20,'<span class=\"smalltdname\">Autoimmune</span>' );\nff(122,668,200,20,'<span class=\"smalltdname\">17.</span>' );\nff(137,668,200,20,'<span class=\"smalltdname\">Breast</span>' );\nff(122,680,200,20,'<span class=\"smalltdname\">18.</span>' );\nff(137,680,200,20,'<span class=\"smalltdname\">Gyn/PAP</span>' );\nff(122,692,200,20,'<span class=\"smalltdname\">19.</span>' );\nff(137,692,200,20,'<span class=\"smalltdname\">Hospitalizations</span>' );\nff(122,704,200,20,'<span class=\"smalltdname\">20.</span>' );\nff(137,704,200,20,'<span class=\"smalltdname\">Surgeries</span>' );\nff(122,716,200,20,'<span class=\"smalltdname\">21.</span>' );\nff(137,716,200,20,'<span class=\"smalltdname\">Anesthetics</span>' );\nff(122,728,200,20,'<span class=\"smalltdname\">22.</span>' );\nff(137,728,200,20,'<span class=\"smalltdname\">Hem./Transfusions</span>' );\nff(122,740,200,20,'<span class=\"smalltdname\">23.</span>' );\nff(137,740,200,20,'<span class=\"smalltdname\">Varicosities/Phlebitis</span>' );\nff(122,752,200,20,'<span class=\"smalltdname\">24.</span>' );\nff(137,752,200,20,'<span class=\"smalltdname\">Psychiatric illness</span>' );\nff(122,764,200,20,'<span class=\"smalltdname\">25.</span>' );\nff(137,764,200,20,'<span class=\"smalltdname\">Other</span>' );\n" ;
	
	_profile += "ff(228,573,20,20,\"" + (props.getProperty("pg1_yes9", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,573,20,20,\"" + (props.getProperty("pg1_no9", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,585,20,20,\"" + (props.getProperty("pg1_yes10", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,585,20,20,\"" + (props.getProperty("pg1_no10", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,597,20,20,\"" + (props.getProperty("pg1_yes11", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,597,20,20,\"" + (props.getProperty("pg1_no11", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,609,20,20,\"" + (props.getProperty("pg1_yes12", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,609,20,20,\"" + (props.getProperty("pg1_no12", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,621,20,20,\"" + (props.getProperty("pg1_yes13", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,621,20,20,\"" + (props.getProperty("pg1_no13", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,633,20,20,\"" + (props.getProperty("pg1_yes14", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,633,20,20,\"" + (props.getProperty("pg1_no14", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,645,20,20,\"" + (props.getProperty("pg1_yes15", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,645,20,20,\"" + (props.getProperty("pg1_no15", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,657,20,20,\"" + (props.getProperty("pg1_yes16", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,657,20,20,\"" + (props.getProperty("pg1_no16", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,669,20,20,\"" + (props.getProperty("pg1_yes17", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,669,20,20,\"" + (props.getProperty("pg1_no17", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(228,681,20,20,\"" + (props.getProperty("pg1_yes18", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,681,20,20,\"" + (props.getProperty("pg1_no18", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,693,20,20,\"" + (props.getProperty("pg1_yes19", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,693,20,20,\"" + (props.getProperty("pg1_no19", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,705,20,20,\"" + (props.getProperty("pg1_yes20", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,705,20,20,\"" + (props.getProperty("pg1_no20", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,717,20,20,\"" + (props.getProperty("pg1_yes21", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,717,20,20,\"" + (props.getProperty("pg1_no21", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,729,20,20,\"" + (props.getProperty("pg1_yes22", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,729,20,20,\"" + (props.getProperty("pg1_no22", "").equals("")?"":"X")  + "\" );\n" ;


	_profile += "ff(228,741,20,20,\"" + (props.getProperty("pg1_yes23", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,741,20,20,\"" + (props.getProperty("pg1_no23", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,753,20,20,\"" + (props.getProperty("pg1_yes24", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,753,20,20,\"" + (props.getProperty("pg1_no24", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(228,765,20,20,\"" + (props.getProperty("pg1_yes25", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(246,765,20,20,\"" + (props.getProperty("pg1_no25", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(136,780,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_box25", ""))  + "\" );\n" ;

	_profile += "ff(267,571,200,20,'<span class=\"smalltdname\">26.</span>' );\nff(283,571,200,20,'<span class=\"smalltdname\">Age&gt;=35 at EDB</span>' );\nff(267,583,200,20,'<span class=\"smalltdname\">27.</span>' );\nff(283,583,200,20,'<span class=\"smalltdname\">\"At risk\" population</span>' );\nff(283,591,200,20,'<span class=\"minitdname\">(Tay-Sach&#39;s, sicke cell,</span>' );\nff(283,600,200,20,'<span class=\"minitdname\">thalassemia, etc.)</span>' );\n\n\nff(267,611,200,20,'<span class=\"smalltdname\">28.</span>' );\nff(283,611,200,20,'<span class=\"smalltdname\">Known teratogen exposure</span>' );\nff(283,619,200,20,'<span class=\"minitdname\">(includes maternal diabetes)</span>' );\nff(267,630,200,20,'<span class=\"smalltdname\">29.</span>' );\nff(283,630,200,20,'<span class=\"smalltdname\">Previous birth defect</span>' );\n" ;

	_profile += "ff(402,573,20,20,\"" + (props.getProperty("pg1_yes26", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,573,20,20,\"" + (props.getProperty("pg1_no26", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,585,20,20,\"" + (props.getProperty("pg1_yes27", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,585,20,20,\"" + (props.getProperty("pg1_no27", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,613,20,20,\"" + (props.getProperty("pg1_yes28", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,613,20,20,\"" + (props.getProperty("pg1_no28", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,632,20,20,\"" + (props.getProperty("pg1_yes29", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,632,20,20,\"" + (props.getProperty("pg1_no29", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(267,640,200,20,'<span class=\"smalltdname\"><b>Family history of:</b></span>' );\nff(267,651,200,20,'<span class=\"smalltdname\">30.</span>' );\nff(283,651,200,20,'<span class=\"smalltdname\">Neural tube defects</span>' );\nff(267,662,200,20,'<span class=\"smalltdname\">31.</span>' );\nff(283,662,200,20,'<span class=\"smalltdname\">Development delay</span>' );\nff(267,673,200,20,'<span class=\"smalltdname\">32.</span>' );\nff(283,673,200,20,'<span class=\"smalltdname\">Congenital physical</span>' );\nff(283,681,200,20,'<span class=\"minitdname\">anomalies (includes</span>' );\nff(283,689,200,20,'<span class=\"minitdname\">congenital heart disease)</span>' );\nff(267,698,200,20,'<span class=\"smalltdname\">33.</span>' );\nff(283,698,200,20,'<span class=\"smalltdname\">Congenital hypotonias</span>' );\nff(267,710,200,20,'<span class=\"smalltdname\">34.</span>' );\nff(283,710,200,20,'<span class=\"smalltdname\">Chromosomal disease</span>' );\nff(283,718,200,20,'<span class=\"minitdname\">(Down&#39;s, Turner&#39;s, etc.)</span>' );\nff(267,729,200,20,'<span class=\"smalltdname\">35.</span>' );\nff(283,729,200,20,'<span class=\"smalltdname\">Genetic disease</span>' );\nff(283,737,200,20,'<span class=\"minitdname\">(cystic fibrosis, muscular</span>' );\nff(283,746,200,20,'<span class=\"minitdname\">dystrophy, etc.)</span>' );\nff(267,757,200,20,'<span class=\"smalltdname\">36.</span>' );\nff(283,757,200,20,'<span class=\"smalltdname\">Further investigations</span>' );\nff(267,768,200,20,'<span class=\"smalltdname\">37.</span>' );\nff(283,768,200,20,'<span class=\"smalltdname\">MSS</span>' );\nff(283,777,200,20,'<span class=\"smalltdname\">Offered</span>' );\nff(283,786,200,20,'<span class=\"smalltdname\">Accepted</span>' );\n" ;

	_profile += "ff(402,652,20,20,\"" + (props.getProperty("pg1_yes30", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,652,20,20,\"" + (props.getProperty("pg1_no30", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,664,20,20,\"" + (props.getProperty("pg1_yes31", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,664,20,20,\"" + (props.getProperty("pg1_no31", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,676,20,20,\"" + (props.getProperty("pg1_yes32", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,676,20,20,\"" + (props.getProperty("pg1_no32", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,699,20,20,\"" + (props.getProperty("pg1_yes33", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,699,20,20,\"" + (props.getProperty("pg1_no33", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,712,20,20,\"" + (props.getProperty("pg1_yes34", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,712,20,20,\"" + (props.getProperty("pg1_no34", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,731,20,20,\"" + (props.getProperty("pg1_yes35", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,731,20,20,\"" + (props.getProperty("pg1_no35", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,759,20,20,\"" + (props.getProperty("pg1_yes36", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,759,20,20,\"" + (props.getProperty("pg1_no36", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,776,20,20,\"" + (props.getProperty("pg1_yes37off", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,776,20,20,\"" + (props.getProperty("pg1_no37off", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(402,788,20,20,\"" + (props.getProperty("pg1_yes37acc", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(420,788,20,20,\"" + (props.getProperty("pg1_no37acc", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(442,571,200,20,'<span class=\"smalltdname\">38.</span>' );\nff(461,571,200,20,'<span class=\"smalltdname\">STDs/Herpes</span>' );\nff(442,583,200,20,'<span class=\"smalltdname\">39.</span>' );\nff(461,583,200,20,'<span class=\"smalltdname\">HIV</span>' );\nff(442,595,200,20,'<span class=\"smalltdname\">40.</span>' );\nff(461,595,200,20,'<span class=\"smalltdname\">Varicella</span>' );\nff(442,607,200,20,'<span class=\"smalltdname\">41.</span>' );\nff(461,607,200,20,'<span class=\"smalltdname\">Toxo/CMV/Parvo</span>' );\nff(442,619,200,20,'<span class=\"smalltdname\">42.</span>' );\nff(461,619,200,20,'<span class=\"smalltdname\">TB/Other</span>' );\n" ;

	_profile += "ff(572,573,20,20,\"" + (props.getProperty("pg1_idt38", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(572,585,20,20,\"" + (props.getProperty("pg1_idt39", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(572,597,20,20,\"" + (props.getProperty("pg1_idt40", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(572,609,20,20,\"" + (props.getProperty("pg1_idt41", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(500,617,200,20,'<span class=\"smalltdname\">" + UtilMisc.JSEscape(props.getProperty("pg1_box42", ""))  + "</span>' );\n" ;
	_profile += "ff(572,621,20,20,\"" + (props.getProperty("pg1_idt42", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(442,640,200,20,'<span class=\"smalltdname\"><b>Psychosocial discussion topics</b></span>' );\nff(442,654,200,20,'<span class=\"smalltdname\">43.</span>' );\nff(461,654,200,20,'<span class=\"smalltdname\">Social support</span>' );\nff(442,666,200,20,'<span class=\"smalltdname\">44.</span>' );\nff(461,666,200,20,'<span class=\"smalltdname\">Couple&#39;s relationship</span>' );\nff(442,678,200,20,'<span class=\"smalltdname\">45.</span>' );\nff(461,678,200,20,'<span class=\"smalltdname\">Emotional/Depression</span>' );\nff(442,690,200,20,'<span class=\"smalltdname\">46.</span>' );\nff(461,690,200,20,'<span class=\"smalltdname\">Substance abuse</span>' );\nff(442,702,200,20,'<span class=\"smalltdname\">47.</span>' );\nff(461,702,200,20,'<span class=\"smalltdname\">Family violence</span>' );\nff(442,714,200,20,'<span class=\"smalltdname\">48.</span>' );\nff(461,714,200,20,'<span class=\"smalltdname\">Parenting concerns</span>' );\n" ;
	
	_profile += "ff(572,656,20,20,\"" + (props.getProperty("pg1_pdt43", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(572,668,20,20,\"" + (props.getProperty("pg1_pdt44", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(572,680,20,20,\"" + (props.getProperty("pg1_pdt45", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(572,692,20,20,\"" + (props.getProperty("pg1_pdt46", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(572,704,20,20,\"" + (props.getProperty("pg1_pdt47", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(572,716,20,20,\"" + (props.getProperty("pg1_pdt48", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(442,734,200,20,'<span class=\"smalltdname\"><b>Risk factors identified</b></span>' );\n\nff(590,565,100,20,'<span class=\"smalltdname\">Ht.</span>' );\nff(652,565,100,20,'<span class=\"smalltdname\">Wt.</span>' );\nff(590,587,200,20,'<span class=\"smalltdname\">Pre-preg. wt.</span>' );\nff(590,608,100,20,'<span class=\"smalltdname\">BP</span>' );\n" ;
	
	_profile += "ff(602,565,20,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_ht", ""))  + "\" );\n" ;
	_profile += "ff(672,565,20,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_wt", ""))  + "\" );\n" ;
	_profile += "ff(642,585,20,20,\"" + UtilMisc.JSEscape(props.getProperty("c_ppWt", ""))  + "\" );\n" ;
	_profile += "ff(610,608,20,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_BP", ""))  + "\" );\n" ;

	_profile += "ff(591,630,200,20,'<span class=\"smalltdname\"><b>Checkmark if normal:</b></span>' );\nff(591,643,200,20,'<span class=\"smalltdname\">Head, teeth, ENT</span>' );\nff(591,655,200,20,'<span class=\"smalltdname\">Thyroid</span>' );\nff(591,667,200,20,'<span class=\"smalltdname\">Chest</span>' );\nff(591,679,200,20,'<span class=\"smalltdname\">Breasts</span>' );\nff(591,691,200,20,'<span class=\"smalltdname\">Cardiovascular</span>' );\nff(591,703,200,20,'<span class=\"smalltdname\">Abdomen</span>' );\nff(591,715,200,20,'<span class=\"smalltdname\">Varicosities, extremities</span>' );\nff(591,727,200,20,'<span class=\"smalltdname\">Neurological</span>' );\nff(591,739,200,20,'<span class=\"smalltdname\">Pelvic architecture</span>' );\nff(591,750,200,20,'<span class=\"smalltdname\">Ext. genitalia</span>' );\nff(591,761,200,20,'<span class=\"smalltdname\">Cervix, vagina</span>' );\nff(591,771,200,20,'<span class=\"smalltdname\">Uterus</span>' );\nff(640,771,200,20,'<span class=\"smalltdname\">(no. of wks.)</span>' );\nff(591,785,200,20,'<span class=\"smalltdname\">Adnexa</span>' );\n" ;
	
	_profile += "ff(693,643,20,20,\"" + (props.getProperty("pg1_head", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,655,20,20,\"" + (props.getProperty("pg1_thyroid", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,667,20,20,\"" + (props.getProperty("pg1_chest", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,679,20,20,\"" + (props.getProperty("pg1_breasts", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,691,20,20,\"" + (props.getProperty("pg1_cardio", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,703,20,20,\"" + (props.getProperty("pg1_abdomen", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,715,20,20,\"" + (props.getProperty("pg1_vari", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,727,20,20,\"" + (props.getProperty("pg1_neuro", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,739,20,20,\"" + (props.getProperty("pg1_pelvic", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,751,20,20,\"" + (props.getProperty("pg1_extGen", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,763,20,20,\"" + (props.getProperty("pg1_cervix", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(622,771,20,20,'<span class=\"smalltdname\">" + UtilMisc.JSEscape(props.getProperty("pg1_uterusBox", ""))  + "</span>' );\n" ;
	_profile += "ff(693,775,20,20,\"" + (props.getProperty("pg1_uterus", "").equals("")?"":"X")  + "\" );\n" ;
	_profile += "ff(693,787,20,20,\"" + (props.getProperty("pg1_adnexa", "").equals("")?"":"X")  + "\" );\n" ;

	_profile += "ff(20,935,200,20,\"" + UtilMisc.JSEscape(props.getProperty("pg1_signature", ""))  + "\" );\n" ;
	_profile += "ff(500,935,100,20,\"" + props.getProperty("pg1_formDate", "")  + "\" );\n" ;

	_profile += "ff(6,919,300,20,'<span class=\"tdname\">Signature of attendant</span>' );\nff(430,919,300,20,'<span class=\"tdname\">Date (yyyy/mm/dd)</span>' );\n\nff(15,958,200,20,'<span class=\"smalltdname\">0374-64 (99/12)</span>' );\nff(180,958,350,20,'<span class=\"smalltdname\">Canary - Mother&#39;s chart - forward to hospital  Pink - Attendant&#39;s copy  White - Infant&#39;s chart</span>' );\nff(665,958,200,20,'<span class=\"smalltdname\">7530-4654</span>' );\n</script>\n" ;

	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +480+"" + "px; top:" +""+175 + "px; width:230px; height:30px;\">\n<pre>" + props.getProperty("pg1_ethnicBg") + "</pre>\n</div>\n" ;
	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +80+"" + "px; top:" +""+228 + "px; width:280px; height:50px;\">\n<span class=\"smalltdname\">" + props.getProperty("c_allergies", "") + "</span>\n</div>\n" ;
	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +395+"" + "px; top:" +""+228 + "px; width:280px; height:50px;\">\n<span class=\"smalltdname\">" + props.getProperty("c_meds", "") + "</span>\n</div>\n" ;
	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +440+"" + "px; top:" +""+748 + "px; width:150px; height:60px;\">\n<span class=\"smalltdname\">" + props.getProperty("c_riskFactors", "") + "</span>\n</div>\n" ;
	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +12+"" + "px; top:" +""+830 + "px; width:300px; height:60px;\">\n<pre>" + props.getProperty("pg1_commentsAR1", "") + "</pre>\n</div>\n" ;
	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +392+"" + "px; top:" +""+421 + "px; width:340px; height:20px;\">\n" + props.getProperty("pg1_oh_comments1", "") + "\n</div>\n" ;
	
	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +392+"" + "px; top:" +""+439 + "px; width:340px; height:20px;\">\n" + props.getProperty("pg1_oh_comments2", "") + "\n</div>\n" ;

	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +392+"" + "px; top:" +""+457 + "px; width:340px; height:20px;\">\n" + props.getProperty("pg1_oh_comments3", "") + "\n</div>\n" ;

	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +392+"" + "px; top:" +""+476 + "px; width:340px; height:20px;\">\n" + props.getProperty("pg1_oh_comments4", "") + "\n</div>\n" ;

	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +392+"" + "px; top:" +""+494 + "px; width:340px; height:20px;\">\n" + props.getProperty("pg1_oh_comments5", "") + "\n</div>\n" ;

	_profile += "<div ID=\"bdiv1\" STYLE=\"position:absolute; visibility:visible; z-index:2; left:" +392+"" + "px; top:" +""+512 + "px; width:340px; height:20px;\">\n" + props.getProperty("pg1_oh_comments6", "") + "\n</div>\n</body>\n</html>"	 ;


	// send jaxm to oscarcitizens ----------------
	String [] args = new String[2];
	args[0] = PropsUtil.get("citizenscerpath").trim() ; 
	args[1] = PropsUtil.get("citizensserverurl").trim() ; 

	Vector nameV = new Vector();
	Vector tagV = new Vector();
	nameV.add("authCode");
	nameV.add("msgContent");
	
	nameV.add("receiverID");
	nameV.add("senderName");
	nameV.add("senderID"); 
	nameV.add("srcLocationID");
	nameV.add("srcLocationDesc");
	nameV.add("profileTimeStamp");
	nameV.add("subject");
	nameV.add("profile");

	tagV.add(_authCode); tagV.add(_msgContent); tagV.add(_receiverID); tagV.add(_senderName); tagV.add(_senderID);
	tagV.add(_srcLocationID); tagV.add(_srcLocationDesc); tagV.add(_profileTimeStamp); tagV.add(_subject); tagV.add(_profile);

	OCClientSend one = new OCClientSend();
	one.init(args[0], nameV, tagV);
	one.sendMessage(args[1],one.createMessage());

	out.println("<script language=\"JavaScript\"> self.close();</SCRIPT>");
%>

</body>
</html>