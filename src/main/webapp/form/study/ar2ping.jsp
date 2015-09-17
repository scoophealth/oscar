<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarDB.DBHandler"%>
<%@ page import="java.lang.reflect.*, java.sql.*"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<jsp:useBean id="studyMapping" class="java.util.Properties" scope="page" />

<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@ page import="java.util.*,oscar.ping.xml.*"%>
<%@ page import="org.chip.ping.xml.*"%>
<%@ page import="org.chip.ping.xml.cddm.*"%>
<%@ page import="oscar.OscarPingTalk"%>
<%@ page import="oscar.oscarDemographic.data.*"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Desaprisk" %>
<%@page import="org.oscarehr.common.dao.DesapriskDao" %>
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

<%
	DesapriskDao desapriskDao = SpringUtils.getBean(DesapriskDao.class);
%>

<%
out.println("Please wait ...<br>");
out.flush();


String actorTicket = null;
String actor = "clinic@iampregnant.org"; //"clinic@citizenhealth.ca"; // marcelle@citizenhealth.ca
String actorPassword = "password";
DemographicData demoData = new DemographicData();
String patientPingId = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), request.getParameter("demographic_no")).getEmail();

OscarPingTalk ping = new OscarPingTalk();
boolean connected = true;
String connectErrorMsg = "";
if(session.getAttribute("ticket") == null) {
	try{
		actorTicket = ping.connect(actor,actorPassword);
		session.setAttribute("ticket", actorTicket);
	}catch(Exception eCon){
	    connectErrorMsg = eCon.getMessage();
	    connected = false;
	}
} else {
	actorTicket = (String)session.getAttribute("ticket");
}

String owner = actor;
String originAgent = actor;
String author = actor;
String level1 = CddmLevels.CUMULATIVE;
String level2 = CddmLevels.HEALTH_MAINTENANCE;


String className = request.getParameter("classname");
String riskContent = "";
Properties prop = new Properties();
Object obj = null ;


//take data from form
String demoNo = request.getParameter("demographic_no");
ResultSet rsdemo = DBHandler.GetSQL("select * from formAR where demographic_no= "+demoNo+" order by formEdited desc, ID desc limit 0,1");
ResultSetMetaData md = rsdemo.getMetaData();
if (rsdemo.next()) {
	for(int i = 1; i <= md.getColumnCount(); i++)  {
        String name = md.getColumnName(i);

        // for "_a", to change to "A"
        String jaxbName = "" ;
        /*
        if (name.indexOf("_")>0) {
        	//int j = name.indexOf("_");
        	String[] aName = name.split("_");
        	for(int k=0; k<aName.length; k++) {
        		if (k==0) jaxbName += aName[k];
        		else jaxbName += aName[k].substring(0, 1).toUpperCase()
                        + aName[k].substring(1);
        	}
        } else {
        	jaxbName = name ;
        }
        */
		int N = name.length();
		boolean bCap = false;
		StringBuffer sb = new StringBuffer(N);
		for (int j = 0; j < N; j++) {
			char c = name.charAt(j);
			if (c == '_' ) { //skip it
				bCap = true;
			} else if ( Character.isDigit(c)) {
				bCap = true;
				sb.append(c);
			} else if ( Character.isLetter(c) && bCap ) {
				bCap = false;
				sb.append(Character.toUpperCase(c));
			} else
				sb.append(c);
		}
		jaxbName = sb.toString();


        String type = md.getColumnTypeName(i);
		if (type.equals("TINY") || name.equals("ID")) {
			prop.setProperty(jaxbName, "" + rsdemo.getInt(name) );
		} else {
			prop.setProperty(jaxbName, (rsdemo.getString(name)==null?"":rsdemo.getString(name) ) );
		}
	}

	//get ar plan data
	String formNo = "" + rsdemo.getInt("ID");
	Desaprisk desa = desapriskDao.search(Integer.parseInt(formNo), Integer.parseInt(demoNo));
	if (desa != null) {
		riskContent = desa.getRiskContent();
		riskContent += desa.getChecklistContent();
	}
}


// send to ping
oscar.ping.xml.ObjectFactory _respFactory = new oscar.ping.xml.ObjectFactory();
ARRecord ARRecord = _respFactory.createARRecord();
//ARRecord.setSubject("Antenatal Record");
prop.setProperty("subject", "Antenatal Record" );
//set ar planner
prop.setProperty("arPlanner", riskContent );


if(connected){

    try {
        Class c = oscar.ping.xml.ARRecord.class;

        Method method;
        Method[] theMethods = c.getMethods();
        for (int i = 0; i < theMethods.length; i++) {
			String methodString = theMethods[i].getName();

			if (methodString.startsWith("set")) {
                String fieldNameU = methodString.substring(3);
                String fieldNameL = fieldNameU.substring(0, 1).toLowerCase()
                        + fieldNameU.substring(1);
                String fieldValue = prop.getProperty(fieldNameL) == null ? prop
                        .getProperty(fieldNameU, "") : prop
                        .getProperty(fieldNameL);
                Object[] arguments = new Object[] { fieldValue};

                theMethods[i].invoke(ARRecord, arguments);
            }
        }

    } catch (Exception e) {
        MiscUtils.getLogger().error("Error", e);
    }

	DataType dataType = ping.getDataType(ARRecord);
	CddmType cddmType = ping.getCddm(owner,originAgent,author,level1,level2,dataType);

	connectErrorMsg = "The record was sent to PING server.<br><p><input type='button' name='but' onclick='window.close()' value='Close'>";
    try{
        ping.sendCddm(actorTicket, patientPingId,cddmType);
    }catch(Exception sendCon){
        connectErrorMsg = "<font style=\"font-size: 19px; color: red; font-family : tahoma, Arial,Helvetica,Sans Serif;\">Could Not Send to PHR</font>: " + patientPingId + " .<br><p><input type='button' name='but' onclick='window.close()' value='Close'>";
    }

	out.println(connectErrorMsg);

}
%>
