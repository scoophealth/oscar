// javac -classpath .;C:\jakarta-tomcat-4.0.6\common\lib;%CLASSPATH% SAClient.java
/*activation.jar
commons-logging.jar
dom.jar
dom4j.jar
jaxm-api.jar
jaxm-runtime.jar
jaxp-api.jar
mail.jar
sax.jar
saaj-api.jar
saaj-ri.jar
xalan.jar
xercesImpl.jar
xsltc.jar
javac -d . FrmStudyXMLClient.java
java -classpath .:%CLASSPATH%  oscar.form.study.FrmStudyXMLClient /root/oscar_sfhc.properties 
java -classpath .;D:\j2sdk1.4.1_02\jre\lib\ext\mysql-connector-java-3.0.7-stable-bin.jar oscar.form.study.FrmStudyXMLClient e:\\root\\oscar_sfhc.properties 
*/

package src.oscar.form.study;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.xml.soap.*;
import javax.xml.messaging.*;
import java.net.URL;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import oscar.oscarDB.*;
import oscar.util.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class FrmStudyXMLClient {
	private String DBName = null;
	private String URLService = null;

	Properties param = new Properties();
	Properties studyMapping = new Properties();
	DBHandler db = null; 
	String sql = null; 
	ResultSet rs = null; 

	Properties studyTableName = null;
	Vector studyDemoNo = null;
	String dateYesterday = null;
	String dateTomorrow = null;

	public static void main (String[] args) throws java.sql.SQLException, java.io.IOException  {
		if (args.length != 1) {
			System.out.println("Please run: java path/FrmStudyXMLClient dbname");
			return; 
		}
		FrmStudyXMLClient aStudy = new FrmStudyXMLClient();

		//initial
		aStudy.init(args[0]);
		if (aStudy.studyTableName.size() == 0) {aStudy.db.CloseConn(); return;}

		//loop for each form table
		for (Enumeration e = aStudy.studyTableName.propertyNames() ; e.hasMoreElements() ;)	{
			String tempStudyNo = (String) e.nextElement();
			
			//get demono
			aStudy.getDemoNo(tempStudyNo);
			if (aStudy.studyDemoNo.size() == 0) continue;

			//search and insert the current study records one by one
			for (int i = 0; i < aStudy.studyDemoNo.size(); i++)	{
				aStudy.getSetStudyRecord(tempStudyNo, aStudy.studyTableName.getProperty(tempStudyNo), (String)aStudy.studyDemoNo.get(i), aStudy.dateYesterday, aStudy.dateTomorrow);
			}

			//sendOneRecord();
			//updateRecordStatus;
		}
 
		aStudy.db.CloseConn();

	}

	private synchronized void getSetStudyRecord (String studyNo, String formTable, String demoNo, String date1, String date2) throws java.sql.SQLException, java.io.IOException  {
		String providerNo = null; 
		String formEdited = null; 
		String content = null; 
        sql = "SELECT demographic_no, provider_no, formEdited from " + formTable + " where demographic_no=" + demoNo + " and formEdited > '" + date1 + "' and formEdited < '" + date2 + "' order by formEdited desc limit 0,1";
        rs = db.GetSQL(sql);
        while(rs.next()) {
			System.out.println(rs.getString("demographic_no")); 
			providerNo = rs.getString("provider_no"); 
			formEdited = rs.getString("formEdited"); 

			//set contact xml content
			content = getContactContent(demoNo, formTable);
			if (content != null) {
				sql = "insert into studydata (demographic_no, study_no, provider_no, timestamp, status, content) values (" + demoNo + ", " 
				+studyNo + ", " + providerNo + ", '" + formEdited + "', 'ready', '" + UtilMisc.charEscape(content, '\'') + "')";
				if (db.RunSQL(sql)) throw new java.sql.SQLException();
			}

			//get encounter xml content
			content = getContent(demoNo, formTable);
			//save the record
			sql = "insert into studydata (demographic_no, study_no, provider_no, timestamp, status, content) values (" + demoNo + ", " 
				+studyNo + ", " + providerNo + ", '" + formEdited + "', 'ready', '" + UtilMisc.charEscape(content, '\'') + "')";
			//System.out.println(sql); 
			if (db.RunSQL(sql)) throw new java.sql.SQLException();

		}
        rs.close();

	}

	private synchronized String getContactContent (String demoNo, String formTable) throws java.sql.SQLException, java.io.IOException  {
		//if it is a new patient, create contact string. otherwise skip it.
		sql = "select count(*) from studydata where demographic_no=" + demoNo;
	    ResultSet rsdemo = db.GetSQL(sql);
		if (rsdemo.next())	{
			if (rsdemo.getInt("count(*)") != 0) 	return null;
		}

		rsdemo = null;
	    Properties demo = new Properties();

		//read the mapping file
		try {
	      studyMapping.load(new FileInputStream("../../../../webapps/"+ param.getProperty("project_home") +"/form/study/formdiabete2studymapping.txt")); //change to speciallll name
		} catch(Exception e) {System.out.println("*** No Mapping File ***"); }

		//take data from demographic
		sql = "select * from demographic where demographic_no=" + demoNo;
	    rsdemo = db.GetSQL(sql);
	    while (rsdemo.next()) { 
		    demo.setProperty("demographic.demographic_no", rsdemo.getString("demographic_no"));
		    demo.setProperty("demographic.first_name", rsdemo.getString("first_name"));
			demo.setProperty("demographic.last_name", rsdemo.getString("last_name"));
	        demo.setProperty("demographic.sex", rsdemo.getString("sex"));
	        demo.setProperty("demographic.address", rsdemo.getString("address"));
	        demo.setProperty("demographic.city", rsdemo.getString("city"));
	        demo.setProperty("demographic.province", rsdemo.getString("province"));
		    demo.setProperty("demographic.phone", rsdemo.getString("phone"));
	        demo.setProperty("demographic.hin", rsdemo.getString("hin"));
	        demo.setProperty("demographic.postal", rsdemo.getString("postal")!=null?rsdemo.getString("postal").replaceAll(" ", ""):"");
	        demo.setProperty("demographic.dob", rsdemo.getString("year_of_birth") +"-"+ (rsdemo.getString("month_of_birth").length()<2?("0"+rsdemo.getString("month_of_birth")):rsdemo.getString("month_of_birth")) +"-"+ (rsdemo.getString("date_of_birth").length()<2?("0"+rsdemo.getString("date_of_birth")):rsdemo.getString("date_of_birth")) );
		}
        rsdemo.close();

		//xml part
	    Document doc = UtilXML.newDocument();

		UtilXML.addNode(doc, "contact");

		org.w3c.dom.Node contact1 = doc.getLastChild();
		UtilXML.addNode(contact1, "contact_healthnumber", demo.getProperty(studyMapping.getProperty("encounter.contact_healthnumber")));
		UtilXML.addNode(contact1, "contact_firstname", demo.getProperty(studyMapping.getProperty("contact.contact_firstname")));
		UtilXML.addNode(contact1, "contact_lastname", demo.getProperty(studyMapping.getProperty("contact.contact_lastname")));
		UtilXML.addNode(contact1, "contact_sex", demo.getProperty(studyMapping.getProperty("contact.contact_sex")));
		UtilXML.addNode(contact1, "contact_dob", demo.getProperty(studyMapping.getProperty("contact.contact_dob")));

		UtilXML.addNode(contact1, "conidentity");
		org.w3c.dom.Node conidentity = contact1.getLastChild();
		UtilXML.addNode(conidentity, "conidentity_type", studyMapping.getProperty("contact.conidentity.conidentity_type"));
		UtilXML.addNode(conidentity, "conidentity_addrstreet", demo.getProperty(studyMapping.getProperty("contact.conidentity.conidentity_addrstreet")));
		UtilXML.addNode(conidentity, "conidentity_addrcity", demo.getProperty(studyMapping.getProperty("contact.conidentity.conidentity_addrcity")));
		UtilXML.addNode(conidentity, "conidentity_addrprovince", demo.getProperty(studyMapping.getProperty("contact.conidentity.conidentity_addrprovince")));
		UtilXML.addNode(conidentity, "conidentity_addrcountry", studyMapping.getProperty("contact.conidentity.conidentity_addrcountry"));
		UtilXML.addNode(conidentity, "conidentity_addrpostal", demo.getProperty(studyMapping.getProperty("contact.conidentity.conidentity_addrpostal")));

		UtilXML.addNode(contact1, "conphone");
		org.w3c.dom.Node conphone = contact1.getLastChild();
		UtilXML.addNode(conphone, "conphone_phonetype", studyMapping.getProperty("contact.conphone.conphone_phonetype"));
		UtilXML.addNode(conphone, "conphone_phonenumber", demo.getProperty(studyMapping.getProperty("contact.conphone.conphone_phonenumber")));

		UtilXML.addNode(contact1, "conEHRpatient");
		org.w3c.dom.Node conEHRpatient = contact1.getLastChild();
		UtilXML.addNode(conEHRpatient, "conEHRpatient_EHR_id", studyMapping.getProperty("contact.conEHRpatient.conEHRpatient_EHR_id"));
		UtilXML.addNode(conEHRpatient, "conEHRpatient_EHRpatient_id", demo.getProperty(studyMapping.getProperty("contact.conEHRpatient.conEHRpatient_EHRpatient_id")));

	    return(UtilXML.toXML(doc, "contact1_2.dtd"));

	}

	private synchronized String getContent (String demoNo, String formTable) throws java.sql.SQLException, java.io.IOException  {
	    Properties demo = new Properties();
		Properties form = new Properties();
		Properties echart = new Properties();
		Properties allergy = new Properties();
		Properties drug = new Properties();

		//read the mapping file
		try {
	      studyMapping.load(new FileInputStream("../../../../webapps/"+ param.getProperty("project_home") +"/form/study/formdiabete2studymapping.txt")); //change to speciallll name
		} catch(Exception e) {System.out.println("*** No Mapping File ***"); }

		//take data from demographic
		sql = "select * from demographic where demographic_no=" + demoNo;
	    ResultSet rsdemo = db.GetSQL(sql);
	    while (rsdemo.next()) { 
		    demo.setProperty("demographic.first_name", rsdemo.getString("first_name"));
			demo.setProperty("demographic.last_name", rsdemo.getString("last_name"));
	        demo.setProperty("demographic.sex", rsdemo.getString("sex"));
		    demo.setProperty("demographic.phone", rsdemo.getString("phone"));
	        demo.setProperty("demographic.hin", rsdemo.getString("hin"));
	        demo.setProperty("demographic.postal", rsdemo.getString("postal")!=null?rsdemo.getString("postal").replaceAll(" ", ""):"");
		}
        rsdemo.close();

	    //take data from form
		sql = "select * from " + formTable + " where demographic_no= " + demoNo + " order by formEdited desc, ID desc limit 0,1";
		rsdemo = db.GetSQL(sql);
	    while (rsdemo.next()) { 
		    form.setProperty(formTable+".birthDate", rsdemo.getString("birthDate"));
			//get the column number
			int k=0;
			for (int i = 5; i > 0 ; i--) {
				if (rsdemo.getString("date"+i) != null) {
					k = i;
					break;
				}
			}

			form.setProperty(formTable+".formEdited", UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(rsdemo.getString("formEdited"), "yyyyMMddHHmmss"), "yyyy-MM-dd hh:mm:ss a") );
			form.setProperty(formTable+".bp", rsdemo.getString("bp" + k));
			form.setProperty(formTable+".weight", rsdemo.getString("weight" + k));
			form.setProperty(formTable+".height", rsdemo.getString("height"));
			form.setProperty(formTable+".lipidsA", rsdemo.getString("lipidsA" + k));
			form.setProperty(formTable+".lipidsB", rsdemo.getString("lipidsB" + k));
			form.setProperty(formTable+".lipidsC", rsdemo.getString("lipidsC" + k));
			form.setProperty(formTable+".eyes", rsdemo.getString("eyes" + k));
			form.setProperty(formTable+".feet", rsdemo.getString("feet" + k));

			form.setProperty(formTable+".lifestyle", rsdemo.getString("lifestyle" + k));
			form.setProperty(formTable+".exercise", rsdemo.getString("exercise" + k));
			form.setProperty(formTable+".alcohol", rsdemo.getString("alcohol" + k));
			form.setProperty(formTable+".sexualFunction", rsdemo.getString("sexualFunction" + k));
			form.setProperty(formTable+".diet", rsdemo.getString("diet" + k));
		}
        rsdemo.close();

	    //take data from eChart
		String health_condition_name = null;

		sql = "select ongoingConcerns from eChart where demographicNo= " + demoNo + " order by timeStamp desc limit 1";
		rsdemo = db.GetSQL(sql);
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
        rsdemo.close();

		//take data from allergies
		int k = 0;
		sql = "select DESCRIPTION from allergies where demographic_no= " + demoNo;
		rsdemo = db.GetSQL(sql);
	    while (rsdemo.next()) { 
			allergy.setProperty("encounter.adverse_reactions.adverse_reactions_offending_drug^" + k, rsdemo.getString("DESCRIPTION"));
			k++;
		}
        rsdemo.close();

		//take data from drugs
	    GregorianCalendar now=new GregorianCalendar();
		now.add(now.DATE, -7);
	    String rxLastTime = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) ;
		oscar.oscarRx.data.RxDrugData.GCN gcn = null;

		int ntemp = 1;
		k = 0;
		sql = "select * from drugs where demographic_no= " + demoNo + " and rx_date >=" + rxLastTime + " order by rx_date desc, drugid desc";
	    rsdemo = db.GetSQL(sql);
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
        rsdemo.close();

		//xml part
	    Document doc = UtilXML.newDocument();

		UtilXML.addNode(doc, "encounter");

		org.w3c.dom.Node encounter = doc.getLastChild();
		UtilXML.addNode(encounter, "contact_healthnumber", demo.getProperty(studyMapping.getProperty("encounter.contact_healthnumber")));
		UtilXML.addNode(encounter, "encounter_date", form.getProperty(studyMapping.getProperty("encounter.encounter_date")));
		UtilXML.addNode(encounter, "encounter_entry_date", form.getProperty(studyMapping.getProperty("encounter.encounter_entry_date")));
		UtilXML.addNode(encounter, "encounter_class_ID", studyMapping.getProperty("encounter.encounter_class_ID"));

		int inNum = 3;
		org.w3c.dom.Node[] vitals = new org.w3c.dom.Node[inNum];
		for (int in = 0; in < inNum; in++)	{
			UtilXML.addNode(encounter, "vitals");
			vitals[in] = encounter.getLastChild();
			UtilXML.addNode(vitals[in], "vital_sign_name", studyMapping.getProperty("encounter.vitals.vital_sign_name^" + (in+1)));
			UtilXML.addNode(vitals[in], "vitals_result", form.getProperty(studyMapping.getProperty("encounter.vitals.vitals_result^" + (in+1))));
			UtilXML.addNode(vitals[in], "vital_notes", "");
		}
/*
		UtilXML.addNode(encounter, "vitals");
		org.w3c.dom.Node vitals1 = encounter.getLastChild();
		UtilXML.addNode(vitals1, "vital_sign_name", studyMapping.getProperty("encounter.vitals.vital_sign_name^1"));
		UtilXML.addNode(vitals1, "vitals_result", form.getProperty(studyMapping.getProperty("encounter.vitals.vitals_result^1")));
		UtilXML.addNode(vitals1, "vital_notes", "");

		UtilXML.addNode(encounter, "vitals");
		org.w3c.dom.Node vitals2 = encounter.getLastChild();
		UtilXML.addNode(vitals2, "vital_sign_name", studyMapping.getProperty("encounter.vitals.vital_sign_name^2"));
		UtilXML.addNode(vitals2, "vitals_result", form.getProperty(studyMapping.getProperty("encounter.vitals.vitals_result^2")));
		UtilXML.addNode(vitals2, "vital_notes", "");

		UtilXML.addNode(encounter, "vitals");
		org.w3c.dom.Node vitals3 = encounter.getLastChild();
		UtilXML.addNode(vitals3, "vital_sign_name", studyMapping.getProperty("encounter.vitals.vital_sign_name^3"));
		UtilXML.addNode(vitals3, "vitals_result", form.getProperty(studyMapping.getProperty("encounter.vitals.vitals_result^3")));
		UtilXML.addNode(vitals3, "vital_notes", "");
*/
		UtilXML.addNode(encounter, "lab");
		org.w3c.dom.Node lab1 = encounter.getLastChild();
		UtilXML.addNode(lab1, "lab_test_name", studyMapping.getProperty("encounter.lab.lab_test_name^1"));
		UtilXML.addNode(lab1, "lab_results", form.getProperty(studyMapping.getProperty("encounter.lab.lab_results^1")));

		UtilXML.addNode(encounter, "lab");
		org.w3c.dom.Node lab2 = encounter.getLastChild();
		UtilXML.addNode(lab2, "lab_test_name", studyMapping.getProperty("encounter.lab.lab_test_name^2"));
		UtilXML.addNode(lab2, "lab_results", form.getProperty(studyMapping.getProperty("encounter.lab.lab_results^2")));

		UtilXML.addNode(encounter, "lab");
		org.w3c.dom.Node lab3 = encounter.getLastChild();
		UtilXML.addNode(lab3, "lab_test_name", studyMapping.getProperty("encounter.lab.lab_test_name^3"));
		UtilXML.addNode(lab3, "lab_results", form.getProperty(studyMapping.getProperty("encounter.lab.lab_results^3")));

	    org.w3c.dom.Node health_conditions = null;
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
		org.w3c.dom.Node risk_factors1 = encounter.getLastChild();
		UtilXML.addNode(risk_factors1, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^1"));
		UtilXML.addNode(risk_factors1, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^1")));
		UtilXML.addNode(risk_factors1, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^1")));

		UtilXML.addNode(encounter, "risk_factors");
		org.w3c.dom.Node risk_factors2 = encounter.getLastChild();
		UtilXML.addNode(risk_factors2, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^2"));
		UtilXML.addNode(risk_factors2, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^2")));
		UtilXML.addNode(risk_factors2, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^2")));

		UtilXML.addNode(encounter, "risk_factors");
		org.w3c.dom.Node risk_factors3 = encounter.getLastChild();
		UtilXML.addNode(risk_factors3, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^3"));
		UtilXML.addNode(risk_factors3, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^3")));
		UtilXML.addNode(risk_factors3, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^3")));

		UtilXML.addNode(encounter, "risk_factors");
		org.w3c.dom.Node risk_factors4 = encounter.getLastChild();
		UtilXML.addNode(risk_factors4, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^4"));
		UtilXML.addNode(risk_factors4, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^4")));
		UtilXML.addNode(risk_factors4, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^4")));

		UtilXML.addNode(encounter, "risk_factors");
		org.w3c.dom.Node risk_factors5 = encounter.getLastChild();
		UtilXML.addNode(risk_factors5, "risk_factors_name", studyMapping.getProperty("encounter.risk_factors.risk_factors_name^5"));
		UtilXML.addNode(risk_factors5, "risk_factors_notes", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_notes^5")));
		UtilXML.addNode(risk_factors5, "risk_factors_quantity", form.getProperty(studyMapping.getProperty("encounter.risk_factors.risk_factors_quantity^5")));

	    int n_adverse_reactions = 0;
		org.w3c.dom.Node adverse_reactions = null;
		while (allergy.getProperty("encounter.adverse_reactions.adverse_reactions_offending_drug^"+n_adverse_reactions) != null) {
			UtilXML.addNode(encounter, "adverse_reactions");
			adverse_reactions = encounter.getLastChild();
			UtilXML.addNode(adverse_reactions, "adverse_reactions_name", studyMapping.getProperty("encounter.adverse_reactions.adverse_reactions_name"));
			UtilXML.addNode(adverse_reactions, "adverse_reactions_offending_drug", allergy.getProperty("encounter.adverse_reactions.adverse_reactions_offending_drug^"+n_adverse_reactions));
			n_adverse_reactions++;
		}
	
	    int n_medication = 0;
		org.w3c.dom.Node medication = null;
		while (drug.getProperty(studyMapping.getProperty("encounter.medication.medication_EHR_supplied_drug_name^*")+n_medication) != null) {
			UtilXML.addNode(encounter, "medication");
			medication = encounter.getLastChild();
			UtilXML.addNode(medication, "medication_EHR_supplied_drug_name", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_EHR_supplied_drug_name^*") + n_medication));
			UtilXML.addNode(medication, "medication_prescription_date", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_prescription_date^*") + n_medication));
			UtilXML.addNode(medication, "medication_drug_strength", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_strength^*") + n_medication));

//			UtilXML.addNode(medication, "medication_drug_dose", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_dose^*") + n_medication));

			UtilXML.addNode(medication, "medication_drug_quantity", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_quantity^*") + n_medication));
			UtilXML.addNode(medication, "medication_drug_PRN", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_PRN^*") + n_medication));
			UtilXML.addNode(medication, "medication_duration_days", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_duration_days^*") + n_medication));
			UtilXML.addNode(medication, "medication_special_instructions", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_special_instructions^*") + n_medication));
			UtilXML.addNode(medication, "medication_end_date", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_end_date^*") + n_medication));
			UtilXML.addNode(medication, "medication_drug_form_description", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_form_description^*") + n_medication));
			UtilXML.addNode(medication, "medication_drug_route_description", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_route_description^*") + n_medication));
			UtilXML.addNode(medication, "medication_drug_named_frequency_description", drug.getProperty(studyMapping.getProperty("encounter.medication.medication_drug_named_frequency_description^*") + n_medication));
		    n_medication++;
		}
	
		//out.clear();
	    //out.flush();
	    return(UtilXML.toXML(doc, "encounter1_3.dtd"));

	}

	private synchronized void init (String file) throws java.sql.SQLException, java.io.IOException  {
		param.load(new FileInputStream(file)); 
		DBHandler.init(param.getProperty("db_name"),param.getProperty("db_driver"),param.getProperty("db_uri") ,param.getProperty("db_username"),param.getProperty("db_password") );
		//DBHandler.init("oscar_sfhc", "org.gjt.mm.mysql.Driver","jdbc:mysql:///","root","liyi" );
        db = new DBHandler(DBHandler.OSCAR_DATA);
		studyTableName = new Properties();
        sql = "SELECT s.study_no, e.form_table from study s LEFT JOIN encounterForm e ON s.form_name = e.form_name";
        rs = db.GetSQL(sql);
        while(rs.next()) {
			studyTableName.setProperty(rs.getString("s.study_no"), rs.getString("e.form_table")); 
		}
        rs.close();

		GregorianCalendar now=new GregorianCalendar();
		now.add(now.DATE, -1);
		dateYesterday = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) ;
		now.add(now.DATE, +2);
		dateTomorrow = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) ;
	}

	private synchronized void getDemoNo (String studyNo) throws java.sql.SQLException  {
		studyDemoNo = new Vector();
        sql = "SELECT demographic_no from demographicstudy where study_no = " + studyNo;
        rs = db.GetSQL(sql);
        while(rs.next()) {
			studyDemoNo.add(rs.getString("demographic_no")); 
		}
        rs.close();
	}

}
