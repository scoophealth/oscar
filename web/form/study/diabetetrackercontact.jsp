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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
--%>
<%
  if(session.getValue("user") == null || !( ((String) session.getValue("userprofession")).equalsIgnoreCase("doctor") ))
    response.sendRedirect("../../logout.jsp");
%>


<%@ page contentType="text/xml"%>
<%@ page
	import="java.util.*, java.sql.*, javax.xml.parsers.*, org.w3c.dom.*, oscar.util.*,java.io.*,org.xml.sax.InputSource"
	errorPage="../../appointment/errorpage.jsp"%>
<jsp:useBean id="studyMapping" class="java.util.Properties" scope="page" />
<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean"
	scope="page" />
<%--database command part  --%>
<%@ include file="../../admin/dbconnection.jsp"%>
<% 
  String [][] dbQueries=new String[][] { 
    {"search_demographic", "select * from demographic where demographic_no=? "}, 
    {"search_formtype2diabete", "select * from formType2Diabetes where demographic_no= ? order by formEdited desc, ID desc limit 0,1"}, 
    {"search_echart", "select ongoingConcerns from eChart where demographicNo=? order by timeStamp desc limit 1"}, 
    {"search_allergies", "select DESCRIPTION from allergies where demographic_no=? "}, 
    {"search_drugs", "select * from drugs where demographic_no=? and rx_date >= ? order by rx_date desc, drugid desc "}, 
  };
  studyBean.doConfigure(dbQueries);
%>


<% response.setHeader("Cache-Control","no-cache");%>
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
    } catch(Exception e) {System.out.println("*** No Mapping File ***"); }

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
/*
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
    rsdemo = studyBean.queryResults(demoNo, "search_allergies");
    while (rsdemo.next()) { 
    	allergy.setProperty("encounter.adverse_reactions.adverse_reactions_offending_drug^" + k, rsdemo.getString("DESCRIPTION"));
		k++;
	}

    //take data from drugs
    GregorianCalendar now=new GregorianCalendar();
    now.add(now.DATE, -7);
    String rxLastTime = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) ;
    oscar.oscarRx.data.RxDrugData.GCN gcn = null;

	int ntemp = 1;
	k = 0;
    rsdemo = studyBean.queryResults(new String[] {demoNo, rxLastTime}, "search_drugs");
    while (rsdemo.next()) { 
    	drug.setProperty("drugs.BN" + k, rsdemo.getString("BN"));
    	drug.setProperty("drugs.rx_date" + k, rsdemo.getString("rx_date"));
    	drug.setProperty("drugs.freqcode" + k, rsdemo.getString("freqcode"));
    	drug.setProperty("drugs.quantity" + k, rsdemo.getString("quantity"));
    	drug.setProperty("drugs.prn" + k, rsdemo.getString("prn").equals("1")?"true":"false" );
		ntemp = 1;
		if (rsdemo.getString("durunit")!=null && !rsdemo.getString("durunit").equals("D")) {
			ntemp = rsdemo.getString("durunit").equals("W") ? 7 : 30;
		}
		if (rsdemo.getString("repeat")!=null) {
			ntemp = ntemp * (Integer.parseInt(rsdemo.getString("repeat"))+1) ;
		}
    	drug.setProperty("drugs.duration" + k, ""+(Integer.parseInt(rsdemo.getString("duration")) * ntemp) );
    	drug.setProperty("drugs.special" + k, rsdemo.getString("special"));
    	drug.setProperty("drugs.end_date" + k, rsdemo.getString("end_date"));

        if(rsdemo.getString("BN") != null && rsdemo.getInt("GCN_SEQNO") != 0) {
            gcn = new oscar.oscarRx.data.RxDrugData().getGCN(rsdemo.getString("BN"), rsdemo.getInt("GCN_SEQNO"));
		    //System.out.println(Integer.parseInt(rsdemo.getString("GCN_SEQNO")) + gcn.getStrength());
    	    drug.setProperty("RxDrugData.GCN.getStrength()" + k, gcn.getStrength());
    	    drug.setProperty("RxDrugData.GCN.getDoseForm()" + k, gcn.getDoseForm());
    	    drug.setProperty("RxDrugData.GCN.getRoute()" + k, gcn.getRoute());
		}

		k++;
	}
*/
    studyBean.closePstmtConn();

	//xml part
    Document doc = UtilXML.newDocument();
	//UtilXML.addNode(doc, "diabete-tracker");

/*	factory.setValidating(true);
xml_document.setDoctype( "-//IETF//DTD RFCxxxx CPL 1.0//EN","cpl.dtd","");
<!DOCTYPE address SYSTEM "dtd_uri">
XmlDocument XmlDoc = new XmlDocument();
XmlDoc.setDoctype("Finance","yyy.dtd", null);
*/
	//Node dt = doc.getFirstChild();
	//UtilXML.addNode(dt, "contact");

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

//	UtilXML.addNode(dt, "encounter");

/*
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
	
    int n_medication = 0;
    Node medication = null;
	while (drug.getProperty(studyMapping.getProperty("encounter.medication.medication_EHR_supplied_drug_name^*")+n_medication) != null) {
	UtilXML.addNode(encounter, "medication");
	medication = encounter.getLastChild();
	UtilXML.addNode(medication, "medication_EHR_supplied_drug_name", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_EHR_supplied_drug_name^*") + n_medication));
	UtilXML.addNode(medication, "medication_prescription_date", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_prescription_date^*") + n_medication));
	UtilXML.addNode(medication, "medication_drug_strength", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_strength^*") + n_medication));
	UtilXML.addNode(medication, "medication_drug_dose", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_dose^*") + n_medication));

	UtilXML.addNode(medication, "medication_drug_quantity", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_quantity^*") + n_medication));
	UtilXML.addNode(medication, "medication_drug_PRN", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_PRN^*") + n_medication));
	UtilXML.addNode(medication, "medication_duration_days", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_duration_days^*") + n_medication));
	UtilXML.addNode(medication, "medication_special_instructions", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_special_instructions^*") + n_medication));
	UtilXML.addNode(medication, "medication_end_date", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_end_date^*") + n_medication));
	UtilXML.addNode(medication, "medication_drug_route_description", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_route_description^*") + n_medication));
    n_medication++;
	UtilXML.addNode(medication, "medication_drug_named_frequency_description", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_named_frequency_description^*") + n_medication));
	}
*/	
	out.clear();
    out.flush();
    out.println(UtilXML.toXML(doc, "contact1_2.dtd"));
    //System.out.println(UtilXML.toXML(doc));

%>