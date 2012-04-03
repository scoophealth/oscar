<%@page import="oscar.oscarReport.data.DemographicSets,java.util.ArrayList"%>

<%
    String demoNo = request.getParameter("demoNo");
    String patientSet = request.getParameter("patientSet");

    DemographicSets ds = new DemographicSets();
    java.util.List<String> demoSet = ds.getDemographicSet(patientSet);

    if (!demoSet.contains(demoNo)) {
	java.util.List<String> newSet = new ArrayList<String>();
	newSet.add(demoNo);
	ds.addDemographicSet(patientSet, newSet);
    }
    response.sendRedirect("../close.html");
%>

<html>
    <head>
        <title>Add Demographic to Patient Set</title>
    </head>
    <body>

    Adding...

    </body>
</html>
