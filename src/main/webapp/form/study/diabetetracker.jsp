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
if(session.getAttribute("user") == null || !( ((String) session.getAttribute("userprofession")).equalsIgnoreCase("doctor") ))
	response.sendRedirect("../../logout.jsp");
%>


<%@ page contentType="text/xml"%>
<%@ page
	import="java.util.*, java.sql.*,  org.w3c.dom.*, oscar.util.*,java.io.*"
	errorPage="../../appointment/errorpage.jsp"%>


<%@page import="org.oscarehr.common.dao.AllergyDao"%>
<%@page import="org.oscarehr.common.model.Allergy"%>
<%@page import="org.oscarehr.util.SpringUtils" %>

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
    }
	catch(Exception e)
    {
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
		form.setProperty("formType2Diabetes.bp", rsdemo.getString("bp" + k));
		form.setProperty("formType2Diabetes.weight", rsdemo.getString("weight" + k));
		form.setProperty("formType2Diabetes.height", rsdemo.getString("height"));
		form.setProperty("formType2Diabetes.lipidsA", rsdemo.getString("lipidsA" + k));
		form.setProperty("formType2Diabetes.lipidsB", rsdemo.getString("lipidsB" + k));
		form.setProperty("formType2Diabetes.lipidsC", rsdemo.getString("lipidsC" + k));
		form.setProperty("formType2Diabetes.eyes", rsdemo.getString("eyes" + k));
		form.setProperty("formType2Diabetes.feet", rsdemo.getString("feet" + k));

		form.setProperty("formType2Diabetes.lifestyle", rsdemo.getString("lifestyle" + k));
		form.setProperty("formType2Diabetes.exercise", rsdemo.getString("exercise" + k));
		form.setProperty("formType2Diabetes.alcohol", rsdemo.getString("alcohol" + k));
		form.setProperty("formType2Diabetes.sexualFunction", rsdemo.getString("sexualFunction" + k));
		form.setProperty("formType2Diabetes.diet", rsdemo.getString("diet" + k));
	}

    //take data from eChart
	String health_condition_name = null;
    rsdemo = studyBean.queryResults(demoNo, "search_echart");
    while (rsdemo.next()) {
        health_condition_name = rsdemo.getString("ongoingConcerns");
	}
	if (health_condition_name != null) {
        StringTokenizer st = new StringTokenizer(health_condition_name, "\n");
		int i =0;
		while (st.hasMoreTokens()) {
    		echart.setProperty("eChart.ongoingConcerns"+i, st.nextToken());
            i++;
        }
	}

    //take data from allergies
	int k = 0;
    AllergyDao allergyDao = (AllergyDao)SpringUtils.getBean("allergyDao");
    List<Allergy> allergies = allergyDao.findActiveAllergies(Integer.parseInt(demoNo));
    for(k=0;k<allergies.size();k++) {
    	allergy.setProperty("encounter.adverse_reactions.adverse_reactions_offending_drug^" + k, allergies.get(k).getDescription());
    }


	//xml part
    Document doc = UtilXML.newDocument();

	UtilXML.addNode(doc, "encounter");

	Node encounter = doc.getLastChild();
	UtilXML.addNode(encounter, "contact_healthnumber", demo.getProperty(studyMapping.getProperty("encounter.contact_healthnumber")));
	UtilXML.addNode(encounter, "encounter_date", form.getProperty(studyMapping.getProperty("encounter.encounter_date")));
	UtilXML.addNode(encounter, "encounter_entry_date", form.getProperty(studyMapping.getProperty("encounter.encounter_entry_date")));
	UtilXML.addNode(encounter, "encounter_class_ID", studyMapping.getProperty("encounter.encounter_class_ID"));

	UtilXML.addNode(encounter, "vitals");
	Node vitals1 = encounter.getLastChild();
	UtilXML.addNode(vitals1, "vital_sign_name", studyMapping.getProperty("encounter.vitals.vital_sign_name^1"));
	UtilXML.addNode(vitals1, "vitals_result", form.getProperty(studyMapping.getProperty("encounter.vitals.vitals_result^1")));
	UtilXML.addNode(vitals1, "vital_notes", "");

	UtilXML.addNode(encounter, "vitals");
	Node vitals2 = encounter.getLastChild();
	UtilXML.addNode(vitals2, "vital_sign_name", studyMapping.getProperty("encounter.vitals.vital_sign_name^2"));
	UtilXML.addNode(vitals2, "vitals_result", form.getProperty(studyMapping.getProperty("encounter.vitals.vitals_result^2")));
	UtilXML.addNode(vitals2, "vital_notes", "");

	UtilXML.addNode(encounter, "vitals");
	Node vitals3 = encounter.getLastChild();
	UtilXML.addNode(vitals3, "vital_sign_name", studyMapping.getProperty("encounter.vitals.vital_sign_name^3"));
	UtilXML.addNode(vitals3, "vitals_result", form.getProperty(studyMapping.getProperty("encounter.vitals.vitals_result^3")));
	UtilXML.addNode(vitals3, "vital_notes", "");

	UtilXML.addNode(encounter, "lab");
	Node lab1 = encounter.getLastChild();
	UtilXML.addNode(lab1, "lab_test_name", studyMapping.getProperty("encounter.lab.lab_test_name^1"));
	UtilXML.addNode(lab1, "lab_results", form.getProperty(studyMapping.getProperty("encounter.lab.lab_results^1")));

	UtilXML.addNode(encounter, "lab");
	Node lab2 = encounter.getLastChild();
	UtilXML.addNode(lab2, "lab_test_name", studyMapping.getProperty("encounter.lab.lab_test_name^2"));
	UtilXML.addNode(lab2, "lab_results", form.getProperty(studyMapping.getProperty("encounter.lab.lab_results^2")));

	UtilXML.addNode(encounter, "lab");
	Node lab3 = encounter.getLastChild();
	UtilXML.addNode(lab3, "lab_test_name", studyMapping.getProperty("encounter.lab.lab_test_name^3"));
	UtilXML.addNode(lab3, "lab_results", form.getProperty(studyMapping.getProperty("encounter.lab.lab_results^3")));

    Node health_conditions = null;
	UtilXML.addNode(encounter, "health_conditions");
	health_conditions = encounter.getLastChild();
	UtilXML.addNode(health_conditions, "health_condition_name", studyMapping.getProperty("encounter.health_conditions.health_condition_name^1"));
	UtilXML.addNode(health_conditions, "health_condition_notes", form.getProperty(studyMapping.getProperty("encounter.health_conditions.health_condition_notes^1")));
	UtilXML.addNode(health_conditions, "health_condition_past_history_flag", studyMapping.getProperty("encounter.health_conditions.health_condition_past_history_flag^1"));

	UtilXML.addNode(encounter, "health_conditions");
	health_conditions = encounter.getLastChild();
	UtilXML.addNode(health_conditions, "health_condition_name", studyMapping.getProperty("encounter.health_conditions.health_condition_name^2"));
	UtilXML.addNode(health_conditions, "health_condition_notes", form.getProperty(studyMapping.getProperty("encounter.health_conditions.health_condition_notes^2")));
	UtilXML.addNode(health_conditions, "health_condition_past_history_flag", studyMapping.getProperty("encounter.health_conditions.health_condition_past_history_flag^2"));

	int n_health_conditions = 0;
	while (echart.getProperty(studyMapping.getProperty("encounter.health_conditions.health_condition_name^*")+n_health_conditions) != null) {
	UtilXML.addNode(encounter, "health_conditions");
	health_conditions = encounter.getLastChild();
	UtilXML.addNode(health_conditions, "health_condition_name", echart.getProperty(studyMapping.getProperty("encounter.health_conditions.health_condition_name^*")+n_health_conditions));
	UtilXML.addNode(health_conditions, "health_condition_past_history_flag", studyMapping.getProperty("encounter.health_conditions.health_condition_past_history_flag^*"));
	n_health_conditions++;
	}

	UtilXML.addNode(encounter, "risk_factors");
	Node risk_factors1 = encounter.getLastChild();
	UtilXML.addNode(risk_factors1, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^1"));
	UtilXML.addNode(risk_factors1, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^1")));
	UtilXML.addNode(risk_factors1, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^1")));

	UtilXML.addNode(encounter, "risk_factors");
	Node risk_factors2 = encounter.getLastChild();
	UtilXML.addNode(risk_factors2, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^2"));
	UtilXML.addNode(risk_factors2, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^2")));
	UtilXML.addNode(risk_factors2, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^2")));

	UtilXML.addNode(encounter, "risk_factors");
	Node risk_factors3 = encounter.getLastChild();
	UtilXML.addNode(risk_factors3, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^3"));
	UtilXML.addNode(risk_factors3, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^3")));
	UtilXML.addNode(risk_factors3, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^3")));

	UtilXML.addNode(encounter, "risk_factors");
	Node risk_factors4 = encounter.getLastChild();
	UtilXML.addNode(risk_factors4, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^4"));
	UtilXML.addNode(risk_factors4, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^4")));
	UtilXML.addNode(risk_factors4, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^4")));

	UtilXML.addNode(encounter, "risk_factors");
	Node risk_factors5 = encounter.getLastChild();
	UtilXML.addNode(risk_factors5, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^5"));
	UtilXML.addNode(risk_factors5, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^5")));
	UtilXML.addNode(risk_factors5, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^5")));

    int n_adverse_reactions = 0;
    Node adverse_reactions = null;
	while (allergy.getProperty("encounter.adverse_reactions.adverse_reactions_offending_drug^"+n_adverse_reactions) != null) {
	UtilXML.addNode(encounter, "adverse_reactions");
	adverse_reactions = encounter.getLastChild();
	UtilXML.addNode(adverse_reactions, "adverse_reactions_name", studyMapping.getProperty("encounter.adverse_reactions.adverse_reactions_name"));
	UtilXML.addNode(adverse_reactions, "adverse_reactions_offending_drug", allergy.getProperty("encounter.adverse_reactions.adverse_reactions_offending_drug^"+n_adverse_reactions));
	n_adverse_reactions++;
	}

	out.clear();
    out.flush();
    out.println(UtilXML.toXML(doc, "encounter1_3.dtd"));

%>
