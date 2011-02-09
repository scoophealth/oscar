<%--  
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
--%>
<%
if(session.getAttribute("user") == null) response.sendRedirect("../../logout.jsp");
%>


<%@ page contentType="text/xml"%>
<%@ page
	import="java.util.*, java.sql.*,  org.w3c.dom.*, oscar.util.*,java.io.*"
	errorPage="../../appointment/errorpage.jsp"%>

<%@page import="org.oscarehr.util.MiscUtils"%><jsp:useBean id="studyMapping" class="java.util.Properties" scope="page" />
<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean"
	scope="page" />

<%@ include file="../../admin/dbconnection.jsp"%>
<% 
String [][] dbQueries=new String[][] { 
	{"search_demographic", "select * from demographic where demographic_no=? "}, 
    {"search_formtype2diabete", "select * from formType2Diabetes where demographic_no= ? order by formEdited desc, ID desc limit 0,1"}, 
    //{"search_echart", "select ongoingConcerns from eChart where demographicNo=? order by timeStamp desc limit 1"}, 
    //{"search_allergies", "select DESCRIPTION from allergies where demographic_no=? "}, 
    //{"search_drugs", "select * from drugs where demographic_no=? and rx_date >= ? order by rx_date desc, drugid desc "}, 
};
studyBean.doConfigure(dbQueries);
%>
<%
    String demoNo = request.getParameter("demographic_no");
    Properties demo = new Properties();
    Properties form = new Properties();
    Properties echart = new Properties();
    Properties allergy = new Properties();
    Properties drug = new Properties();

	//read the mapping file
    try {
      studyMapping.load(new FileInputStream("../webapps/"+ oscarVariables.getProperty("project_home") +"/form/study/formdiabete2pingmapping.txt")); //change to speciallll name
    } catch(Exception e) {
    	MiscUtils.getLogger().error("*** No Mapping File ***", e);
    	}

	//take data from demographic
    ResultSet rsdemo = studyBean.queryResults(demoNo, "search_demographic");
    while (rsdemo.next()) { 
        demo.setProperty("demographic.first_name", rsdemo.getString("first_name"));
        demo.setProperty("demographic.last_name", rsdemo.getString("last_name"));
        demo.setProperty("demographic.sex", rsdemo.getString("sex"));
        demo.setProperty("demographic.phone", rsdemo.getString("phone"));
        demo.setProperty("demographic.hin", rsdemo.getString("hin"));

        demo.setProperty("demographic.postal", rsdemo.getString("postal")!=null?rsdemo.getString("postal").replaceAll(" ", ""):"");
	}

    //take data from form
    rsdemo = studyBean.queryResults(demoNo, "search_formtype2diabete");
    while (rsdemo.next()) { 
        form.setProperty("formType2Diabetes.birthDate", rsdemo.getString("birthDate"));
		//get the column number
		int k=0;
		for (int i = 5; i > 0 ; i--) {
			if (rsdemo.getString("date"+i) != null) {
				k = i;
				break;
			}
		}

		form.setProperty("formType2Diabetes.formEdited", UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(rsdemo.getString("formEdited"), "yyyyMMddHHmmss"), "yyyy-MM-dd hh:mm:ss a") );

		form.setProperty("formType2Diabetes.date", rsdemo.getString("date" + k));
		form.setProperty("formType2Diabetes.bp", rsdemo.getString("bp" + k)==null?"":rsdemo.getString("bp" + k));
		form.setProperty("formType2Diabetes.glucoseA", rsdemo.getString("glucoseA" + k)==null?"":rsdemo.getString("glucoseA" + k));
		form.setProperty("formType2Diabetes.glucoseC", rsdemo.getString("glucoseC" + k)==null?"":rsdemo.getString("glucoseC" + k));
		form.setProperty("formType2Diabetes.lifestyle", rsdemo.getString("lifestyle" + k)==null?"":rsdemo.getString("lifestyle" + k));
		form.setProperty("formType2Diabetes.exercise", rsdemo.getString("exercise" + k)==null?"":rsdemo.getString("exercise" + k));
		
		form.setProperty("formType2Diabetes.weight", rsdemo.getString("weight" + k)==null?"":rsdemo.getString("weight" + k));
		form.setProperty("formType2Diabetes.aceInhibitor", rsdemo.getString("aceInhibitor")==null?"":rsdemo.getString("aceInhibitor"));
		form.setProperty("formType2Diabetes.asa", rsdemo.getString("asa")==null?"":rsdemo.getString("asa"));
		form.setProperty("formType2Diabetes.lipidsA", rsdemo.getString("lipidsA" + k)==null?"":rsdemo.getString("lipidsA" + k));
		form.setProperty("formType2Diabetes.urineRatio", rsdemo.getString("urineRatio" + k)==null?"":rsdemo.getString("urineRatio" + k));

		form.setProperty("formType2Diabetes.feet", rsdemo.getString("feet" + k)==null?"":rsdemo.getString("feet" + k));
		form.setProperty("formType2Diabetes.eyes", rsdemo.getString("eyes" + k)==null?"":rsdemo.getString("eyes" + k));
	}

	//xml part
    Document doc = UtilXML.newDocument();

	String [] elementName1 = {"fpVisit", "bloodPressure", "hbA1c", "glucose", "smoking", "exercise", "weight", "medsACE", "medsASA","lipids", "albuminuria", "footCheck", "eyeCheck"} ;
	String nodeName = "DMRecord";
	String dtdFileName = "ping_dm_1_0.dtd";

	UtilXML.addNode(doc, nodeName);

	Node encounter = doc.getLastChild();
	for (int i = 0; i < elementName1.length; i++) {
		UtilXML.addNode(encounter, elementName1[i], form.getProperty(studyMapping.getProperty(nodeName+"."+elementName1[i]), "") );
	}


	out.clear();
    out.flush();
	out.println(UtilXML.toXML(doc, dtdFileName));
%>