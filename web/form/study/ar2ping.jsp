<%
if(session.getAttribute("user") == null) response.sendRedirect("../../logout.jsp");
%>


<%@ page contentType="text/xml" %>
<%@ page import="java.lang.reflect.*, java.util.*, java.sql.*,  org.w3c.dom.*, oscar.util.*,java.io.*"%>
<jsp:useBean id="studyMapping" class="java.util.Properties" scope="page" />
<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp" %>  
<%@ page import="java.util.*,oscar.ping.xml.*,oscar.ping.xml.impl.*,javax.xml.bind.*" %>
<%@ page import="org.chip.ping.client.*"%>
<%@ page import="org.chip.ping.xml.*"%>
<%@ page import="org.chip.ping.xml.talk.*"%>
<%@ page import="org.chip.ping.xml.cddm.*"%>
<%@ page import="org.chip.ping.xml.record.*"%>
<%@ page import="org.chip.ping.xml.record.impl.*"%>
<%@ page import="org.chip.ping.xml.cddm.impl.*,org.w3c.dom.*,javax.xml.parsers.*"%>
<%@ page import="oscar.OscarPingTalk"%>
<%@ page import="oscar.oscarDemographic.data.*"%>

<%@ include file="../../admin/dbconnection.jsp" %>
<% 
String [][] dbQueries=new String[][] { 
	//{"search_demographic", "select * from demographic where demographic_no=? "}, 
    {"search_formar", "select * from formAR where demographic_no= ? order by formEdited desc, ID desc limit 0,1"}, 
};
studyBean.doConfigure(dbParams,dbQueries);
%>


<% response.setHeader("Cache-Control","no-cache");%>
<%
String actorTicket = null;
String actor = "clinic@citizenhealth.ca";
String actorPassword = "password";
DemographicData demoData = new DemographicData();
String patientPingId = demoData.getDemographic(request.getParameter("demographic_no")).getEmail();

OscarPingTalk ping = new OscarPingTalk();
boolean connected = true;
String connectErrorMsg = "";
try{
	actorTicket = ping.connect(actor,actorPassword);
}catch(Exception eCon){
    connectErrorMsg = eCon.getMessage();
    connected = false;
}

String owner = actor;
String originAgent = actor;
String author = actor;
String level1 = CddmLevels.CUMULATIVE;
String level2 = CddmLevels.HEALTH_MAINTENANCE;


String className = request.getParameter("classname");;
Properties prop = new Properties();
Object obj = null ;


//take data from form
String demoNo = request.getParameter("demographic_no");
ResultSet rsdemo = studyBean.queryResults(demoNo, "search_formar");
ResultSetMetaData md = rsdemo.getMetaData();
if (rsdemo.next()) { 
	for(int i = 1; i <= md.getColumnCount(); i++)  {
            String name = md.getColumnName(i);
            String type = md.getColumnTypeName(i);
    		//System.out.println(demoNo + " l :" + name);
    		if (type.equals("TINY") || name.equals("ID")) {
				prop.setProperty(name, "" + rsdemo.getInt(name) );
			} else {
				prop.setProperty(name, (rsdemo.getString(name)==null?"":rsdemo.getString(name) ) );
			}
	}
}

studyBean.closePstmtConn();

	// send to ping
	oscar.ping.xml.ObjectFactory _respFactory = new oscar.ping.xml.ObjectFactory();
	ARRecord ARRecord = _respFactory.createARRecord();

    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

if(connected){

    System.out.println("$$$$       $$$$$$$$$$$$$$$$$$$$$$$$$$44");

        try {
            //Class c = Class.forName(className);
            Class c = Class.forName("ARRecord");

            Method method;
            Method[] theMethods = c.getMethods();
            for (int i = 0; i < theMethods.length; i++) {
                String methodString = theMethods[i].getName();
                System.out.println("Name: " + methodString);
                String fieldNameU = methodString.substring(3);
                System.out.println("fieldNameU: " + fieldNameU);
                String fieldNameL = fieldNameU.substring(0, 1).toLowerCase()
                        + fieldNameU.substring(1);
                String fieldValue = prop.getProperty(fieldNameL) == null ? prop
                        .getProperty(fieldNameU, "") : prop
                        .getProperty(fieldNameL);
                System.out.println("Name: " + methodString);
                //String returnString =
                // theMethods[i].getReturnType().getName();
                //System.out.println(" Return Type: " + returnString);
                //Class[] parameterTypes = theMethods[i].getParameterTypes();
                //System.out.print(" Parameter Types:");
                //for (int k = 0; k < parameterTypes.length; k ++) {
                //   String parameterString = parameterTypes[k].getName();
                //System.out.print(" " + parameterString);
                //}
                Object[] arguments = new Object[] { fieldValue};

                theMethods[i].invoke(obj, arguments);
            }

        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (InvocationTargetException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	DataType dataType = ping.getDataType(ARRecord);
	CddmType cddmType = ping.getCddm(owner,originAgent,author,level1,level2,dataType);
	
	//xml part
//    Document doc = UtilXML.newDocument();


	//out.clear();
    //out.flush();
	//out.println(UtilXML.toXML(doc, dtdFileName));
	//out.println("The record was sent to PING server.<br><p><input type='button' name='but' onclick='window.close()' value='Close'>");
    //System.out.println(UtilXML.toXML(doc));

//	ping.sendCddm(actorTicket, patientPingId,cddmType);
        try{                                        
            ping.sendCddm(actorTicket, patientPingId,cddmType);                                        
        }catch(Exception sendCon){
            connectErrorMsg = "<font style=\"font-size: 19px; color: red; font-family : tahoma, Arial,Helvetica,Sans Serif;\">Could Not Send to PHR</font>";
        }


}
%>