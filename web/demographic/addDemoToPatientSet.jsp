<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%> 
<%@page import="oscar.oscarReport.data.DemographicSets,java.util.ArrayList"%>

<%
    String demoNo = request.getParameter("demoNo");
    String patientSet = request.getParameter("patientSet");
    
    DemographicSets ds = new DemographicSets();
    ArrayList demoSet = ds.getDemographicSet(patientSet);
    
    if (!demoSet.contains(demoNo)) {
	ArrayList newSet = new ArrayList();
	newSet.add(demoNo);
	ds.addDemographicSet(patientSet, newSet);
    }
    response.sendRedirect("../close.html");
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Add Demographic to Patient Set</title>
    </head>
    <body>

    Adding...
    
    </body>
</html>
