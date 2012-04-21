<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%
  if(session.getValue("user") == null || !( ((String) session.getValue("userprofession")).equalsIgnoreCase("doctor") ))
    response.sendRedirect("../../logout.jsp");
%>


<%@ page contentType="text/xml"%>
<%@ page
	import="java.util.*, java.sql.*, javax.xml.parsers.*, org.w3c.dom.*, oscar.util.*,java.io.*,org.xml.sax.InputSource"
	errorPage="../../appointment/errorpage.jsp"%>

<%@page import="org.oscarehr.util.MiscUtils"%><jsp:useBean id="studyMapping" class="java.util.Properties" scope="page" />
<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean"
	scope="page" />
<%--database command part  --%>
<%@ include file="../../admin/dbconnection.jsp"%>
<%
  String [][] dbQueries=new String[][] {
    {"search_demographic", "select * from demographic where demographic_no=? "},
    {"search_formtype2diabete", "select * from formType2Diabetes where demographic_no= ? order by formEdited desc, ID desc limit 0,1"},
    {"search_echart", "select ongoingConcerns from eChart where demographicNo=? order by timeStamp desc limit 1"},
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
      studyMapping.load(new FileInputStream("../webapps/"+ oscarVariables.getProperty("project_home") +"/form/study/formdiabete2studymapping.txt")); //change to speciallll name
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
        demo.setProperty("demographic.dob", rsdemo.getString("year_of_birth") +"-"+ (rsdemo.getString("month_of_birth").length()<2?("0"+rsdemo.getString("month_of_birth")):rsdemo.getString("month_of_birth")) +"-"+ (rsdemo.getString("date_of_birth").length()<2?("0"+rsdemo.getString("date_of_birth")):rsdemo.getString("date_of_birth")) );
	}

	//xml part
    Document doc = UtilXML.newDocument();

	UtilXML.addNode(doc, "contact");

	Node contact1 = doc.getFirstChild();
	UtilXML.addNode(contact1, "contact_firstname", demo.getProperty(studyMapping.getProperty("contact.contact_firstname")));
	UtilXML.addNode(contact1, "contact_lastname", demo.getProperty(studyMapping.getProperty("contact.contact_lastname")));
	UtilXML.addNode(contact1, "contact_sex", demo.getProperty(studyMapping.getProperty("contact.contact_sex")));
	UtilXML.addNode(contact1, "contact_dob", demo.getProperty(studyMapping.getProperty("contact.contact_dob")));

	UtilXML.addNode(contact1, "conphone");
	Node conphone = contact1.getLastChild();
	UtilXML.addNode(conphone, "conphone_phonenumber", demo.getProperty(studyMapping.getProperty("contact.conphone.conphone_phonenumber")));

	UtilXML.addNode(contact1, "conidentity");
	Node conidentity = contact1.getLastChild();
	UtilXML.addNode(conidentity, "conidentity_addrpostal", demo.getProperty(studyMapping.getProperty("contact.conidentity.conidentity_addrpostal")));

	UtilXML.addNode(contact1, "conEHRpatient");
	Node conEHRpatient = contact1.getLastChild();
	UtilXML.addNode(conEHRpatient, "conEHRpatient_EHR_id", studyMapping.getProperty("contact.conEHRpatient.conEHRpatient_EHR_id"));

	out.clear();
    out.flush();
    out.println(UtilXML.toXML(doc, "contact1_2.dtd"));
%>
