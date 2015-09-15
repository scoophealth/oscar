<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page contentType="text/xml"%>
<%@ page import="java.util.*, java.sql.*,  org.w3c.dom.*, oscar.util.*,java.io.*" errorPage="../../appointment/errorpage.jsp"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.model.Demographic"%>
<%@ page import="org.oscarehr.common.dao.DemographicDao"%>
<%
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
%>

<jsp:useBean id="studyMapping" class="java.util.Properties" scope="page" />

<%@ include file="../../admin/dbconnection.jsp"%>

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
    Demographic d = demographicDao.getDemographic(demoNo);
    if (d != null) {
        demo.setProperty("demographic.first_name", d.getFirstName());
        demo.setProperty("demographic.last_name", d.getLastName());
        demo.setProperty("demographic.sex", d.getSex());
        demo.setProperty("demographic.phone", d.getPhone());
        demo.setProperty("demographic.hin", d.getHin());

        demo.setProperty("demographic.postal", d.getPostal()!=null?d.getPostal().replaceAll(" ", ""):"");
	}

    //take data from form
    ResultSet rsdemo = oscar.oscarDB.DBHandler.GetSQL("select * from formType2Diabetes where demographic_no= "+demoNo+" order by formEdited desc, ID desc limit 0,1");
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
