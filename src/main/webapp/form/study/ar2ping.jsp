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


<%@ page import="java.lang.reflect.*, java.sql.*"%>

<%@page import="org.oscarehr.util.MiscUtils"%><jsp:useBean id="studyMapping" class="java.util.Properties" scope="page" />
<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean"
	scope="page" />
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@ page import="java.util.*,oscar.ping.xml.*"%>
<%@ page import="org.chip.ping.xml.*"%>
<%@ page import="org.chip.ping.xml.cddm.*"%>
<%@ page import="oscar.OscarPingTalk"%>
<%@ page import="oscar.oscarDemographic.data.*"%>

<% 
String [][] dbQueries=new String[][] { 
    {"search_formar", "select * from formAR where demographic_no= ? order by formEdited desc, ID desc limit 0,1"}, 
	{"search_desaprisk", "select risk_content, checklist_content from desaprisk where form_no <= ? and demographic_no = ? order by form_no desc, desaprisk_date desc, desaprisk_time desc limit 1 " }, 
};
studyBean.doConfigure(dbQueries);
%>
<%
out.println("Please wait ...<br>");
out.flush();


String actorTicket = null;
String actor = "clinic@iampregnant.org"; //"clinic@citizenhealth.ca"; // marcelle@citizenhealth.ca
String actorPassword = "password";
DemographicData demoData = new DemographicData();
String patientPingId = demoData.getDemographic(request.getParameter("demographic_no")).getEmail();

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
ResultSet rsdemo = studyBean.queryResults(demoNo, "search_formar");
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
	ResultSet rsrisk = studyBean.queryResults((new String[]{formNo, demoNo}), "search_desaprisk");
	if (rsrisk.next()) { 
		riskContent = rsrisk.getString("risk_content");
		riskContent += rsrisk.getString("checklist_content");
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